package com.yzk18.docs;

import com.yzk18.commons.CommonHelpers;
import org.apache.poi.xwpf.usermodel.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * adapted from: https://blog.csdn.net/qq_38148461/article/details/93658114
 */
public class WordTemplateRenderer {
    public static void render(String templateFilename, Map<String,Object> data, String outFile)
    {
        try(XWPFDocument doc = WordHelpers.openDocx(templateFilename))
        {
            WordTemplateRenderer.inPlaceRender(doc,data);
            WordHelpers.saveToFile(doc,outFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static XWPFDocument render(XWPFDocument document, Map<String, Object> map)
    {
        XWPFDocument clonedDoc = clone(document);
        inPlaceRender(clonedDoc,map);
        return clonedDoc;
    }

    public static XWPFDocument clone(XWPFDocument document)
    {
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream();)
        {
            document.write(baos);
            byte[] bytes = baos.toByteArray();
            try(ByteArrayInputStream bais = new ByteArrayInputStream(bytes))
            {
                return new XWPFDocument(bais);
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void inPlaceRender(XWPFDocument document, Map<String, Object> map)
    {
        //1. process tables
        Iterator<XWPFTable> itTable = document.getTablesIterator();
        while (itTable.hasNext())
        {
            XWPFTable table = itTable.next();
            Stream<XWPFParagraph> paragraphs = table.getRows().stream().flatMap(r->r.getTableCells().stream())
                    .flatMap(c->c.getParagraphs().stream());
            modifyParagraphs(paragraphs.iterator(),map);
        }
        //2. process other paragraphs
        modifyParagraphs(document.getParagraphsIterator(),map);
    }

    public static void modifyParagraphs(Iterator<XWPFParagraph> paragraphs, Map<String, Object> data)
    {
        while (paragraphs.hasNext())
        {
            XWPFParagraph paragraph = paragraphs.next();
            List<XWPFRun> runs = paragraph.getRuns();
            for (Map.Entry<String, Object> kv : data.entrySet())
            {
                String target = kv.getKey();
                Object replacement = kv.getValue();
                replaceText(paragraph, runs, target, replacement);
            }
        }
    }

    private static void replaceText(XWPFParagraph paragraph, List<XWPFRun> runs,
                                    String target, Object replacement) {
        TextSegment foundSegment = paragraph.searchText(target, new PositionInParagraph());
        if(foundSegment==null)//not found
        {
            return;
        }
        int beginRun = foundSegment.getBeginRun();
        int endRun = foundSegment.getEndRun();
        StringBuilder foundText = new StringBuilder();//replaced text of foundSegment
        for (int runPos = beginRun; runPos <= endRun; runPos++)
        {
            XWPFRun run = runs.get(runPos);
            foundText.append(run.getText(0));
        }
        XWPFRun partOne = runs.get(beginRun);
        if(replacement instanceof  byte[])
        {
            byte[] imgBytes = (byte[])replacement;
            partOne.setText("",0);//clear
            WordHelpers.addPicture(partOne,imgBytes);
        }
        else
        {
            String replacementStr = CommonHelpers.toString(replacement);
            String replaced = foundText.toString().replace(target, replacementStr);
            partOne.setText(replaced, 0);
        }

        //remove the remnant part of target, like "{name}"
        for (int runPos = beginRun + 1; runPos <= endRun; runPos++)
        {
            XWPFRun partNext = runs.get(runPos);
            partNext.setText("", 0);
        }
        if (endRun <= runs.size())
        {
            replaceText(paragraph, runs, target, replacement);
        }
    }
}
