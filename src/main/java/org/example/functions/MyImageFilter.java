package org.example.functions;

import org.example.commands.AppBotCommand;

import java.util.Random;

public class MyImageFilter {



    @AppBotCommand(name ="greyScale", description = "серый", showInMenuPhotoCommands = true)
    public static float[] greyScale(float[] rgb) {
        float mean = (rgb[0] + rgb[1] + rgb[2]) / 3;
        rgb[0] = mean;
        rgb[1] = mean;
        rgb[2] = mean;
        return rgb;
    }
    @AppBotCommand(name ="onlyRed", description = "onlyRed", showInMenuPhotoCommands = true)
    public static float[] onlyRed(float[] rgb) {
        rgb[1] = 0;
        rgb[2] = 0;
        return rgb;
    }
    @AppBotCommand(name ="onlyGreen", description = "onlyGreen", showInMenuPhotoCommands = true)
    public static float[] onlyGreen(float[] rgb) {
        rgb[0] = 0;
        rgb[2] = 0;
        return rgb;
    }
    @AppBotCommand(name ="onlyBlue", description = "onlyBlue", showInMenuPhotoCommands = true)
    public static float[] onlyBlue(float[] rgb) {
        rgb[0] = 0;
        rgb[1] = 0;
        return rgb;
    }
    @AppBotCommand(name ="sepia", description = "sepia", showInMenuPhotoCommands = true)
    public static float[] sepia(float[] rgb) {
        Random random = new Random();

        rgb[0] += random.nextFloat() * 10 / 255;
        rgb[1] += random.nextFloat() * 10 / 255;
        rgb[2] += random.nextFloat() * 10 / 255;

        for (int i = 0; i < rgb.length; i++) {
            if (rgb[i] > 1) {
                rgb[i] = 1;
            }
        }
        return rgb;
    }
}
