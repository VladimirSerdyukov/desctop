package org.example;

import org.example.commands.AppBotCommand;
import org.example.commands.AllCommands;
import org.example.commands.GeneralCommands;
import org.example.functions.ImageOperation;
import org.example.functions.MyImageFilter;
import org.example.utils.ImageUtils;
import org.example.utils.PhotoMessageUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.swing.table.DefaultTableModel;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bot extends TelegramLongPollingBot {

    HashMap<String, Message> messages = new HashMap<>();

    private DefaultTableModel table;

    public Bot(DefaultTableModel table) {
        this.table = table;
    }

    @Override
    public String getBotUsername() {
        return "t_e_s_t_i_n_g_j_a_v_a_bot";
    }

    @Override
    public String getBotToken() {
        return "6676249918:AAHBSc0XBWM4rb4LXl_G5eXj-4kPsTK3lHg";
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();

        try {
            SendMessage responseTextMessage = runCommonCommand(message);
            if (responseTextMessage != null) {
                execute(responseTextMessage);
                return;
            }

            responseTextMessage = runPhotoMessage(message);
            if (responseTextMessage != null) {
                execute(responseTextMessage);
                return;
            }

            Object responseMediaMessage = runPhotoFilter(message);
            if (responseMediaMessage != null) {
                if (responseMediaMessage instanceof SendMediaGroup) {
                    execute((SendMediaGroup) responseMediaMessage);
                } else if (responseMediaMessage instanceof SendMessage) {
                    SendMessage sendMessage = (SendMessage) responseMediaMessage;
                    sendMessage.setReplyMarkup(getKeyboard(MyImageFilter.class));
                    execute(sendMessage);
                }
                return;
            }

        } catch (InvocationTargetException | IllegalAccessException |
                 TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private SendMessage runPhotoMessage(Message message) {
        List<File> files = getPhotosByMessage(message);
        if (files.isEmpty()) {
            return null;
        }
        messages.put(message.getChatId().toString(), message);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setReplyMarkup(getKeyboard(MyImageFilter.class));
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText("Выберите фильтр");
        return sendMessage;
    }

    private SendMessage runCommonCommand(Message message) throws InvocationTargetException, IllegalAccessException {
        String text = message.getText();
        GeneralCommands commands = new GeneralCommands();
        Method[] methods = commands.getClass().getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(AppBotCommand.class)) {
                AppBotCommand command = method.getAnnotation(AppBotCommand.class);
                if (command.name().equals(text)) {
                    method.setAccessible(true);
                    String responseText = (String) method.invoke(commands);
                    System.out.println(responseText);
                    if (responseText != null) {
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setChatId(message.getChatId().toString());
                        sendMessage.setText(responseText);
                        sendMessage.setReplyMarkup(getKeyboard(GeneralCommands.class));
                        return sendMessage;
                    }
                }
            }
        }
        return null;
    }


    private Object runPhotoFilter(Message message) {
        // метод для реализации команд...
        ImageOperation operation = ImageUtils.getOperation(message.getText());
        if (operation == null) return null;
        Message photoMessage = messages.get(message.getChatId().toString());
        if (photoMessage != null) {
            List<File> files = getPhotosByMessage(messages.get(message.getChatId().toString()));
            try {
                List<String> paths = PhotoMessageUtils.savePhotos(files, getBotToken());
                updateTable(message);
                return preparePhotoMessage(message.getChatId().toString(), operation, paths);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Сначала отправьте фото");
            sendMessage.setChatId(message.getChatId().toString());
            return sendMessage;

        }
        return null;
    }

    private List<File> getPhotosByMessage(Message message) {
        List<PhotoSize> photoSizes = message.getPhoto();
        ArrayList<File> files = new ArrayList<>();
        if (photoSizes == null) return files;
        for (PhotoSize photo : photoSizes) {
            String fileId = photo.getFileId();
            try {
                files.add(sendApiMethod(new GetFile(fileId)));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
        return files;
    }

    private SendMediaGroup preparePhotoMessage(String chatId, ImageOperation operation, List<String> paths) {
        SendMediaGroup mediaGroup = new SendMediaGroup();
        ArrayList<InputMedia> medias = new ArrayList<>();
        for (String path : paths) {
            InputMedia inputMedia = new InputMediaPhoto();
            PhotoMessageUtils.processingImage(path, operation);
            inputMedia.setMedia(new java.io.File("jobImages/" + path), path);
            medias.add(inputMedia);
        }

        mediaGroup.setMedias(medias);
        mediaGroup.setChatId(chatId);

        return mediaGroup;
    }


    private ReplyKeyboardMarkup getKeyboard(Class<?> someClass) {

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        ArrayList<KeyboardRow> allKeyboardRow = new ArrayList<>();
        allKeyboardRow.addAll(getKeyboardRow(someClass));
        replyKeyboardMarkup.setKeyboard(allKeyboardRow);
        return replyKeyboardMarkup;
    }

    private ArrayList<KeyboardRow> getKeyboardRow(Class<?> someClass) {
        ArrayList<Method> commands = new ArrayList<>();
        if(someClass.getName().equals(MyImageFilter.class.getName())) {
            for(Map.Entry<Method, Boolean> m : AllCommands.getCommandsFilter().entrySet()) {
                if(m.getValue()) {
                    commands.add(m.getKey());
                }
            }
        } else if(someClass.getName().equals(GeneralCommands.class.getName())) {
            for (Map.Entry<Method, Boolean> m : AllCommands.getCommandsGeneral().entrySet()) {
                if (m.getValue()) {
                    commands.add(m.getKey());
                }
            }
        }
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        int columnCount = 3;
        int rowsCount = commands.size() / columnCount + (commands.size() % columnCount == 0 ? 0 : 1);
        for (int rowIndex = 0; rowIndex < rowsCount; rowIndex++) {
            KeyboardRow row = new KeyboardRow();
            for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                int index = rowIndex * columnCount + columnIndex;
                if (index >= commands.size()) continue;
                Method command = commands.get(rowIndex * columnCount + columnIndex);
                row.add(new KeyboardButton(command.getName()));
            }
            keyboardRows.add(row);
        }
        return keyboardRows;
    }

    private void updateTable(Message message) {
        List<PhotoSize> photo = messages.get(message.getChatId().toString()).getPhoto();
        table.addRow(new String[]{message.getChatId().toString(),
                message.getText(), photo.get(0).getFileId()});
    }
}