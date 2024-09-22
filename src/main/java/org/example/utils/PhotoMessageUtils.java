package org.example.utils;

import org.example.functions.ImageOperation;
import org.example.functions.MyImageFilter;
import org.telegram.telegrambots.meta.api.objects.File;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PhotoMessageUtils {

    public static void processingImage(String fileName, ImageOperation operation) {
        BufferedImage bi = ImageUtils.getImage("save/" + fileName);
        RgbMaster rgbMaster = new RgbMaster(bi);
        rgbMaster.changeImage(operation);
        ImageUtils.saveImage(rgbMaster.getImage(), "jobImages/" + fileName);
}

public static ArrayList<String> savePhotos(List<File> photos, String botToken) {
    ArrayList<String> paths = new ArrayList<>();
    for (File photo : photos) {
        String urlFile = "https://api.telegram.org/file/bot" + botToken + "/" + photo.getFilePath();
        saveImages(urlFile, (photo.getFileId() + ".jpeg"));
        paths.add(photo.getFileId() + ".jpeg");
    }
    return paths;
}

private static void saveImages(String url, String fileName) {
    try {
        URL file = new URL(url);
        InputStream inputStream = file.openStream();
        OutputStream out = new FileOutputStream("save/" + fileName);
        byte[] b = new byte[8192];
        int length;
        while ((length = inputStream.read(b)) != -1) {
            out.write(b, 0, length);
        }
        inputStream.close();
        out.close();
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}
}
