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

/**
 * <div lang="zh-cn">二维码相关工具类。</div>
 */
public class QRCodeHelpers {

    /**
     * <div lang="zh-cn">生成条形码图片。</div>
     * @param text <div lang="zh-cn">放入条形码中的文本内容。</div>
     * @param format <div lang="zh-cn">条码格式</div>
     * @param width <div lang="zh-cn">图片宽度</div>
     * @param height <div lang="zh-cn">图片高度</div>
     * @return
     */
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

    /**
     * <div lang="zh-cn">生成二维码图片。</div>
     * @param text <div lang="zh-cn">放入二维码中的文本内容</div>
     * @param width <div lang="zh-cn">图片宽度。</div>
     * @param height <div lang="zh-cn">图片高度。</div>
     * @return
     */
    public static BufferedImage generateQRCodeImage(String text, int width, int height)
    {
        return generateBarCodeImage(text,BarcodeFormat.QR_CODE,width,height);
    }

    /**
     * <div lang="zh-cn">尝试从inStream这个流中解析出来条形码。</div>
     * @param inStream
     * @return
     */
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

    /**
     * <div lang="zh-cn">尝试从imgBytes这个字节数组数据中解析出来条形码。</div>
     * @param imgBytes
     * @return
     */
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

    /**
     * <div lang="zh-cn">尝试从file这个文件中解析出来条形码。</div>
     * @param file
     * @return
     */
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

    /**
     * <div lang="zh-cn"><div lang="zh-cn">尝试从fileName这个文件中解析出来条形码。</div></div>
     * @param fileName
     * @return
     */
    public static  Result parseImage(String fileName)
    {
        return parseImage(new File(fileName));
    }
}
