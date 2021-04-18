package com.yzk18.commons;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class ImageHelpers {

    private static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    private static ImageType detectImageTypeByHeaders(byte[] magicHeaders)
    {
        if(magicHeaders.length!=4)
        {
            throw new IllegalArgumentException("magicHeaders.length!=4");
        }
        String type = bytesToHexString(magicHeaders).toUpperCase();
        if (type.contains("FFD8FF")) {
            return ImageType.JPEG;
        } else if (type.contains("89504E47")) {
            return ImageType.PNG;
        } else if (type.contains("47494638")) {
            return ImageType.GIF;
        } else if (type.contains("424D")) {
            return ImageType.BMP;
        }else{
            return ImageType.UNKNOWN;
        }
    }

    public static ImageType detectImageType(InputStream inputStream)
    {
        if(inputStream.markSupported()==false)
        {
            throw new IllegalArgumentException("inputStream.markSupported()==false");
        }
        byte[] headers = new byte[4];
        try
        {
            inputStream.read(headers, 0, headers.length);
            return  detectImageTypeByHeaders(headers);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static ImageType detectImageType(byte[] picData)
    {
        if(picData.length<4)
        {
            throw new IllegalArgumentException("bytes.length<4");
        }
        byte[] headers = Arrays.copyOfRange(picData,0,4);
        return  detectImageTypeByHeaders(headers);
    }

    public static Dimension getImageSize(byte[] picData)
    {
        try(ByteArrayInputStream baInStream = new ByteArrayInputStream(picData))
        {
            BufferedImage img = ImageIO.read(baInStream);
            return new Dimension(img.getWidth(),img.getHeight());
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
