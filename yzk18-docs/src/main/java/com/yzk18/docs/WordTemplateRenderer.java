package com.yzk18.docs;

import org.apache.poi.xwpf.usermodel.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * adapted from: https://blog.csdn.net/qq_38148461/article/details/93658114
 */
public class WordTemplateRenderer {
    public static void render(XWPFDocument document, Map<String, String> map)
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

    public static void modifyParagraphs(Iterator<XWPFParagraph> paragraphs, Map<String, String> data)
    {
        while (paragraphs.hasNext())
        {
            XWPFParagraph paragraph = paragraphs.next();
            List<XWPFRun> runs = paragraph.getRuns();
            for (Map.Entry<String, String> kv : data.entrySet())
            {
                String oldText = kv.getKey();
                String newText = kv.getValue();
                replaceText(paragraph, runs, oldText, newText);
            }
        }
    }

    private static void replaceText(XWPFParagraph xwpfParagraph, List<XWPFRun> runs, String oldText, String newText) {
        TextSegment txtSegment = xwpfParagraph.searchText(oldText, new PositionInParagraph());
        if(txtSegment==null)
        {
            return;
        }
        int beginRun = txtSegment.getBeginRun();
        int endRun = txtSegment.getEndRun();
        StringBuilder b = new StringBuilder();
        for (int runPos = beginRun; runPos <= endRun; runPos++)
        {
            XWPFRun run = runs.get(runPos);
            b.append(run.getText(0));
        }
        String connectedRuns = b.toString();
        String replaced = connectedRuns.replace(oldText, newText);
        XWPFRun partOne = runs.get(beginRun);
        partOne.setText(replaced, 0);
        for (int runPos = beginRun + 1; runPos <= endRun; runPos++)
        {
            XWPFRun partNext = runs.get(runPos);
            partNext.setText("", 0);
        }
        if (endRun <= runs.size())
        {
            replaceText(xwpfParagraph, runs, oldText, newText);
        }
    }
}
