package com.yzk18.commons;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;

/**
 * <div lang="zh-cn">图片相关函数</div>
 */
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

    /**
     * <div lang="zh-cn">检测inputStream中图片的格式。</div>
     * @param inputStream
     * @return
     */
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

    /**
     * <div lang="zh-cn">检测picData代表的图片的格式。</div>
     * @param picData
     * @return
     */
    public static ImageType detectImageType(byte[] picData)
    {
        if(picData.length<4)
        {
            throw new IllegalArgumentException("bytes.length<4");
        }
        byte[] headers = Arrays.copyOfRange(picData,0,4);
        return  detectImageTypeByHeaders(headers);
    }

    /**
     * <div lang="zh-cn">得到picData图片的大小。</div>
     * @param picData
     * @return
     */
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

    public  static void writeToFile(BufferedImage img,String formatName,File file)
    {
        writeToFile(img,formatName,file.toString());
    }
    /**
     * <div lang="zh-cn">把图片img按照格式formatName写入文件fileName。</div>
     * @param img
     * @param formatName 可选值有jpg、png等
     * @param fileName
     */
    public  static void writeToFile(BufferedImage img,String formatName,String fileName)
    {
        IOHelpers.mkParentDirs(fileName);
        try
        {
            ImageIO.write(img,formatName,new File(fileName));
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }


    /**
     * <div lang="zh-cn">把BufferedImage这个图片转换为formatName格式的图片字节数据。</div>
     * @param img
     * @param formatName 可选值有jpg、png等
     * @return
     */
    public static byte[] toByteArray(BufferedImage img,String formatName)
    {
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream())
        {
            ImageIO.write(img,formatName,outputStream);
            return outputStream.toByteArray();
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">把图片字节数据转换为BufferedImage</div>
     * @param bytes
     * @return
     */
    public static BufferedImage toBufferedImage(byte[] bytes)
    {
        try(InputStream inStream = new ByteArrayInputStream(bytes))
        {
            return ImageIO.read(inStream);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">把图片inStream转换为BufferedImage<</div>
     * @param inStream
     * @return
     */
    public static BufferedImage toBufferedImage(InputStream inStream)
    {
        try
        {
            return ImageIO.read(inStream);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">把图片文件file转换为BufferedImage</div>
     * @param file
     * @return
     */
    public static BufferedImage toBufferedImage(File file)
    {
        try(InputStream inStream = new FileInputStream(file))
        {
            return ImageIO.read(inStream);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
