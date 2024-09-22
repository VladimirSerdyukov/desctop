package org.example;

import org.example.commands.AllCommands;
import org.example.functions.MyImageFilter;
import org.example.ui.GeneralWindow;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.swing.table.DefaultTableModel;
import java.lang.reflect.Method;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        GeneralWindow window = new GeneralWindow();
        window.init();
        DefaultTableModel table = window.getTable();

        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            BotSession botSession = api.registerBot(new Bot(table));
            window.setBotSession(botSession);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}