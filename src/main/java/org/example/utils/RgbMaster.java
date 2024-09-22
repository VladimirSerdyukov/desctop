package org.example.utils;

import org.example.functions.ImageOperation;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class RgbMaster {
    private BufferedImage image;
    private int width;
    private int height;
    private boolean hasAlphaChannel;
    private int[] pixels;

    public RgbMaster(BufferedImage image) {
        this.image = image;
        width = image.getWidth();
        height = image.getHeight();
        hasAlphaChannel = image.getAlphaRaster() != null;
        pixels = image.getRGB(0, 0, width, height, null, 0, width);
    }

    public BufferedImage getImage() {
        return image;
    }


    public void changeImage(ImageOperation imageOperation) {
        for (int i = 0; i < pixels.length; i++) {
            float[] pixel = ImageUtils.rgbIntToArray(pixels[i]);
            float[] newPixel = imageOperation.execute(pixel);
            try {
                pixels[i] = ImageUtils.arrayRgbToInt(newPixel);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        image.setRGB(0, 0, width, height, pixels, 0, width);
    }

    public BufferedImage getRotateImage(double angle) {
        double radian = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(radian));
        double cos = Math.abs(Math.cos(radian));

        int nWidth = (int) Math.floor((double) width * cos + (double) height * sin);
        int nHeight = (int) Math.floor((double) height *cos + (double) width * sin);

        BufferedImage rotateImage = new BufferedImage(nWidth, nHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = image.createGraphics();
        //graphics.setColor(Color.WHITE);
        graphics.fillRect(0,0, nWidth, nHeight);
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics.translate((nWidth - width) / 2, (nHeight - height) / 2);
        graphics.rotate(radian, ((double) width / 2), ((double) height / 2));
        graphics.drawImage(rotateImage, 0, 0, null);
        graphics.dispose();

        return rotateImage;
    }
}
