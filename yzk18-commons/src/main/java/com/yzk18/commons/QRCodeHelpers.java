package com.yzk18.commons;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;

public class QRCodeHelpers {
    public static BufferedImage generateBarCodeImage(String text, BarcodeFormat format,
                                                     int width, int height)
    {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = null;
        try
        {
            bitMatrix = qrCodeWriter.encode(text, format, width, height);
        } catch (WriterException e)
        {
            throw new RuntimeException(e);
        }
        BufferedImage buffImg = MatrixToImageWriter.toBufferedImage(bitMatrix);
        return buffImg;
    }

    public static BufferedImage generateQRCodeImage(String text, int width, int height)
    {
        return generateBarCodeImage(text,BarcodeFormat.QR_CODE,width,height);
    }

    public static Result parseImage(InputStream inStream)
    {
        MultiFormatReader multiFormatReader = new MultiFormatReader();
        try
        {
            BufferedImage image = ImageIO.read(inStream);
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            HashMap hints = new HashMap();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            return multiFormatReader.decode(binaryBitmap, hints);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        catch (NotFoundException e)
        {
            return null;
        }
    }

    public static  Result parseImage(byte[] imgBytes)
    {
        try(InputStream inStream = new ByteArrayInputStream(imgBytes))
        {
            return parseImage(inStream);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static  Result parseImage(File file)
    {
        try(InputStream inStream = new FileInputStream(file))
        {
            return parseImage(inStream);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static  Result parseImage(String fileName)
    {
        return parseImage(new File(fileName));
    }
}
