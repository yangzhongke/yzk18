package com.yzk18.docs;

import com.yzk18.commons.CommonHelpers;
import com.yzk18.commons.ImageHelpers;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * <div lang="zh-cn">PDF文件相关的工具类。</div>
 */
public class PDFHelpers {

    /**
     * <div lang="zh-cn">打开PDF文件pdfFile，返回PDDocument对象。</div>
     * @param pdfFile
     * @return
     */
    public static PDDocument openFile(File pdfFile)
    {
        try(InputStream inputStream = new FileInputStream(pdfFile))
        {
            PDDocument pdfDocument = PDDocument.load(inputStream);
            return pdfDocument;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">打开字节数组格式的PDF文件，返回PDDocument对象。</div>
     * @param bytes
     * @return
     */
    public static PDDocument openFile(byte[] bytes)
    {
        try(ByteArrayInputStream bais =new ByteArrayInputStream(bytes))
        {
            return openFile(bais);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">打开PDF文件pdfFile，返回PDDocument对象。</div>
     * @param pdfFile
     * @return
     */
    public static PDDocument openFile(String pdfFile)
    {
        return openFile(new File(pdfFile));
    }

    /**
     * <div lang="zh-cn">打开inputStream流中的PDF文件，返回PDDocument对象。</div>
     * @param inputStream
     * @return
     */
    public static PDDocument openFile(InputStream inputStream)
    {
        try
        {
            PDDocument pdfDocument = PDDocument.load(inputStream);
            return pdfDocument;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">关闭PDDocument对象。</div>
     * @param doc
     */
    public static void close(PDDocument doc)
    {
        CommonHelpers.close(doc);
    }

    /**
     * <div lang="zh-cn">从PDF一页中解析出所有的字符串。</div>
     * @param pdPage
     * @return
     */
    public static String parseText(PDPage pdPage)
    {
        try(PDDocument pdDoc = new PDDocument())
        {
            pdDoc.addPage(pdPage);
            PDFTextStripper textStripper = new PDFTextStripper();
            textStripper.processPage(pdPage);
            return textStripper.getText(pdDoc);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">从PDF文档中解析出所有的字符串。</div>
     * @param pdfDoc
     * @return
     */
    public static String parseText(PDDocument pdfDoc)
    {
        StringBuilder sb = new StringBuilder();
        for(PDPage page : pdfDoc.getPages())
        {
            String txt = parseText(page);
            sb.append(txt).append("\r\n");
        }
        return sb.toString();
    }

    /**
     * <div lang="zh-cn">从PDF一页中解析出所有的图片资源，返回的内容就是图片的字节数组。。</div>
     * @param pdPage <div lang="zh-cn">PDF一页</div>
     * @param formatName <div lang="zh-cn">图片的格式，可以是jpg、png等。</div>
     * @return <div lang="zh-cn">每张图片是List中的一项，每一项就是一个图片的字节数组。</div>
     */
    public static List<byte[]> parseImages(PDPage pdPage,String formatName)
    {
        try(PDDocument pdDoc = new PDDocument())
        {
            pdDoc.addPage(pdPage);
            PDResources pdResources=pdPage.getResources();
            Iterable<COSName> iterable = pdResources.getXObjectNames();
            List<byte[]> images = new LinkedList<>();
            for(COSName cosName : iterable)
            {
                if(!pdResources.isImageXObject(cosName))
                {
                    continue;
                }
                PDImageXObject pdImageXObject=(PDImageXObject) pdResources.getXObject(cosName);
                BufferedImage image=pdImageXObject.getImage();
                byte[] bytes = ImageHelpers.toByteArray(image,formatName);
                images.add(bytes);
            }
            return images;
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
