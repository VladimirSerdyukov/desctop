package org.example.commands;

import java.lang.reflect.Method;
import java.util.HashMap;

public class AllCommands {

    private static final HashMap<Method, Boolean> photoCommands = new HashMap<>();
    private static final HashMap<Method, Boolean> generalCommands = new HashMap<>();

    public static void setCommandsFilters(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].isAnnotationPresent(AppBotCommand.class)) {
                photoCommands.put(methods[i], methods[i].getAnnotation(AppBotCommand.class).showInMenuPhotoCommands());
            }
        }
    }

    public static HashMap<Method, Boolean> getCommandsFilter(){
        return photoCommands;
    }

    public static void setCommandsGeneral(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].isAnnotationPresent(AppBotCommand.class)) {
                generalCommands.put(methods[i], methods[i].getAnnotation(AppBotCommand.class).showInMenuPhotoCommands());
            }
        }
    }

    public static HashMap<Method, Boolean> getCommandsGeneral(){
        return generalCommands;
    }
}
