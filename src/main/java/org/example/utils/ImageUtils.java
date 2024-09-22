package org.example.utils;

import org.example.Main;
import org.example.commands.AppBotCommand;
import org.example.functions.ImageOperation;
import org.example.functions.MyImageFilter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ImageUtils {

    public static BufferedImage getImage(String path) {

        try {
            return ImageIO.read(new File(path));
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveImage(BufferedImage image, String path){
        try {
            ImageIO.write(image, "jpg", new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static float[] rgbIntToArray(int pixel) {
        Color color = new Color(pixel);
        return color.getRGBColorComponents(null);
    }


    public static int arrayRgbToInt(float[] pixel) throws IOException {

        if(pixel.length == 3) {
            return new Color(pixel[0], pixel[1], pixel[2]).getRGB();
        } else if(pixel.length == 4) {
            return new Color(pixel[0], pixel[1], pixel[2], pixel[3]).getRGB();
        } else {
            throw new IOException("Ошибка создания пикселя!!!!");
        }
    }

    public static ImageOperation getOperation(String operationName) {
        MyImageFilter commands = new MyImageFilter();
        Method[] methods = commands.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(AppBotCommand.class)) {
                AppBotCommand command = method.getAnnotation(AppBotCommand.class);
                if (command.name().equals(operationName)) {
                        return (f) -> {
                            try {
                                return (float[]) method.invoke(commands, f);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new RuntimeException(e);
                            }
                        };
                    }
                }
            }
        return null;
    }
}
