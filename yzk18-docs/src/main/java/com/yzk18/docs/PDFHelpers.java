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

public class PDFHelpers {
    public static PDDocument openFile(String pdfFile)
    {
        try(InputStream inputStream = new FileInputStream(pdfFile))
        {
            PDDocument pdfDocument = PDDocument.load(inputStream);
            return pdfDocument;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void close(PDDocument doc)
    {
        CommonHelpers.close(doc);
    }

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
