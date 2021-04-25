package com.yzk18.docs;

import com.yzk18.commons.CommonHelpers;
import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * <div lang="zh-cn">用于使用占位符渲染模板文档来渲染Word文档。</div>
 * adapted from: https://blog.csdn.net/qq_38148461/article/details/93658114
 */
public class WordTemplateRenderer {

    /**
     * <div lang="zh-cn">读取word模板文件templateFilename，替换data代表的占位符数据，outFile为渲染后的目标文件。</div>
     * @param templateFile <div lang="zh-cn">模板文件</div>
     * @param data <div lang="zh-cn">占位符数据，key为占位符名字，比如${姓名}；value为被替换的值，
     * value的值如果是byte[]类型的，则替换为图片，否则替换为value的字符串格式。</div>
     * @param outFile <div lang="zh-cn">目标文件</div>
     */
    public static void render(File templateFile, Map<String,Object> data, File outFile)
    {
        try(XWPFDocument doc = WordHelpers.openDocx(templateFile))
        {
            WordTemplateRenderer.inPlaceRender(doc,data);
            WordHelpers.saveToFile(doc,outFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">读取word模板文件templateFilename，替换data代表的占位符数据，outFile为渲染后的目标文件。</div>
     * @param templateFilename <div lang="zh-cn">模板文件</div>
     * @param data <div lang="zh-cn">占位符数据，key为占位符名字，比如${姓名}；value为被替换的值，
     * value的值如果是byte[]类型的，则替换为图片，否则替换为value的字符串格式。</div>
     * @param outFileName <div lang="zh-cn">目标文件</div>
     */
    public static void render(String templateFilename, Map<String,Object> data, String outFileName)
    {
        render(new File(templateFilename),data,new File(outFileName));
    }

    /**
     * <div lang="zh-cn">根据模板document以及占位数据map来渲染一份新的word文档</div>
     * @param document <div lang="zh-cn">模板文档</div>
     * @param map <div lang="zh-cn">占位符数据，一般是"${姓名}":"张三"这样的键值对。如果值是byte[]类型，则会把byte[]数据当成图片渲染。</div>
     * @return <div lang="zh-cn">渲染完成的新文档</div>
     */
    public static XWPFDocument render(XWPFDocument document, Map<String, Object> map)
    {
        XWPFDocument clonedDoc = WordHelpers.clone(document);
        inPlaceRender(clonedDoc,map);
        return clonedDoc;
    }

    /**
     * <div lang="zh-cn">在文档document上进行在位的模板占位符替换。</div>
     * @param document <div lang="zh-cn">被替换的模板文档</div>
     * @param map <div lang="zh-cn">占位符数据，一般是"${姓名}":"张三"这样的键值对。如果值是byte[]类型，则会把byte[]数据当成图片渲染。</div>
     */
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

    private static void modifyParagraphs(Iterator<XWPFParagraph> paragraphs, Map<String, Object> data)
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
