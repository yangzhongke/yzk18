package com.yzk18.docs;

import com.yzk18.commons.*;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;

import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.function.Consumer;

/**
 * <div lang="zh-cn">读写Word文档的工具类。</div>
 */
public class WordHelpers {

    /**
     * <div lang="en">创建document的一个副本。</div>
     * <div lang="zh-cn">create a copy of document。</div>
     * @param document
     * @return
     */
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

    /**
     * <div lang="zh-cn">读取word文档filename中的全部文本。</div>
     * @param filename
     * @return
     */
    public static String readAllText(String filename)
    {
        return readAllText(new File(filename));
    }

    /**
     * <div lang="zh-cn">读取word文档file中的全部文本。</div>
     * @param file
     * @return
     */
    public static String readAllText(File file)
    {
        try(FileInputStream fis = new FileInputStream(file))
        {
            return readAllText(fis);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">读取inStream中word文档的全部文本。</div>
     * @param inStream
     * @return
     */
    public static String readAllText(InputStream inStream)
    {
        //getFileMagic() only operates on streams which support mark(int)
        try(InputStream memStream = IOHelpers.toByteArrayInputStream(inStream))
        {
            FileMagic fileMagic = FileMagic.valueOf(memStream);
            if ( fileMagic== FileMagic.OLE2)
            {
                try(WordExtractor ex = new WordExtractor(memStream))
                {
                    return ex.getText();
                }
            } else if(fileMagic == FileMagic.OOXML)
            {
                try(XWPFDocument doc = new XWPFDocument(memStream);
                    XWPFWordExtractor extractor = new XWPFWordExtractor(doc);)
                {
                    return extractor.getText();
                }
            }
            else
            {
                throw new RuntimeException("unknow file type:"+fileMagic);
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">创建XWPFDocument类型(*.docx格式)的Word内存对象。</div>
     * @return
     */
    public static XWPFDocument createDocxDocument()
    {
        return new XWPFDocument();
    }

    /**
     * <div lang="zh-cn">从inputStream中打开*.docx格式的Word文档的XWPFDocument对象。</div>
     * @param inputStream
     * @return
     */
    public static XWPFDocument openDocx(InputStream inputStream)
    {
        try {
            return new XWPFDocument(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">从byte[]中打开*.docx格式的Word文档的XWPFDocument对象。</div>
     * @param bytes
     * @return
     */
    public static XWPFDocument openDocx(byte[] bytes)
    {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes))
        {
            return new XWPFDocument(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">打开*.docx格式的Word文档的XWPFDocument对象。</div>
     * @param file
     * @return
     */
    public static XWPFDocument openDocx(String file)
    {
        return openDocx(new File(file));
    }

    /**
     * <div lang="zh-cn">打开*.docx格式的Word文档的XWPFDocument对象。</div>
     * @param file
     * @return
     */
    public static XWPFDocument openDocx(File file)
    {
        try(FileInputStream fos = new FileInputStream(file))
        {
            return openDocx(fos);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">创建一个XWPFRun对象，文本内容为text，左对齐。</div>
     * @param doc <div lang="zh-cn">word文档</div>
     * @param text <div lang="zh-cn">文本内容</div>
     * @return
     */
    public static XWPFRun createRun(XWPFDocument doc, String text)
    {
        return createRun(doc,text,ParagraphAlignment.LEFT);
    }

    /**
     * <div lang="zh-cn">创建一个XWPFRun对象，文本内容为text，对齐方式为alignment。</div>
     * @param doc <div lang="zh-cn">word文档</div>
     * @param text <div lang="zh-cn">文本内容</div>
     * @param alignment <div lang="zh-cn">对齐方式</div>
     * @return
     */
    public static XWPFRun createRun(XWPFDocument doc, String text, ParagraphAlignment alignment)
    {
        XWPFParagraph para = doc.createParagraph();
        para.setAlignment(alignment);
        XWPFRun run = para.createRun();
        run.setText(text);
        return run;
    }

    /**
     * <div lang="zh-cn">向Word文档对象doc中增加一张图片，用图片的默认尺寸显示。</div>
     * @param doc <div lang="zh-cn">word文档对象</div>
     * @param pictureData <div lang="zh-cn">图片内容的字节数组</div>
     * @return
     */
    public static XWPFPicture addPicture(XWPFDocument doc,byte[] pictureData)
    {
        return addPicture(doc,pictureData,0,0);
    }

    /**
     * <div lang="zh-cn">向Word文档对象doc中增加一张图片，图片的显示尺寸为width、height。</div>
     * @param doc <div lang="zh-cn">word文档对象</div>
     * @param pictureData <div lang="zh-cn">图片内容的字节数组</div>
     * @param width <div lang="zh-cn">图片显示的宽度</div>
     * @param height <div lang="zh-cn">图片显示的高度</div>
     * @return
     */
    public static XWPFPicture addPicture(XWPFDocument doc,byte[] pictureData,int width,int height)
    {
        XWPFRun run = createRun(doc,"");
        return addPicture(run,pictureData, width, height);
    }

    /**
     * <div lang="zh-cn">向XWPFRun对象doc中增加一张图片，用图片的默认尺寸显示。</div>
     * @param run
     * @param pictureData
     * @return
     */
    public static XWPFPicture addPicture(XWPFRun run,byte[] pictureData)
    {
        return addPicture(run,pictureData,0,0);
    }

    /**
     * <div lang="zh-cn">向XWPFRun对象中增加一张图片，图片的显示尺寸为width、height。</div>
     * @param run
     * @param pictureData <div lang="zh-cn">图片内容的字节数组</div>
     * @param width <div lang="zh-cn">图片显示的宽度</div>
     * @param height <div lang="zh-cn">图片显示的高度</div>
     * @return
     */
    public static XWPFPicture addPicture(XWPFRun run,byte[] pictureData, int width, int height) {
        ImageType imgType = ImageHelpers.detectImageType(pictureData);
        Dimension imgSize = ImageHelpers.getImageSize(pictureData);
        int picType;
        switch (imgType)
        {
            case JPEG:
                picType = Document.PICTURE_TYPE_JPEG;
                break;
            case PNG:
                picType = Document.PICTURE_TYPE_PNG;
                break;
            case GIF:
                picType = Document.PICTURE_TYPE_GIF;
                break;
            case BMP:
                picType = Document.PICTURE_TYPE_BMP;
                break;
            default:
                throw new IllegalArgumentException("unknow image type");
        }
        try(InputStream inStream = new ByteArrayInputStream(pictureData))
        {
            if(width <=0|| height <=0)
            {
                return run.addPicture(inStream,picType, "",
                        Units.toEMU(imgSize.getWidth()),Units.toEMU(imgSize.getHeight()));
            }
            else
            {
                return run.addPicture(inStream,picType, "",
                        Units.toEMU(width),Units.toEMU(height));
            }
        }
        catch (IOException | InvalidFormatException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">设置table表格的第rowNum行的第colNum列的单元格的值为value。如果行或者列不存在，则自动创建。
     * value的值如果是byte[]类型，则插入为图片，否则就把值设置为value的字符串类型值。
     * </div>
     * @param table <div lang="zh-cn">表格</div>
     * @param rowNum <div lang="zh-cn">行</div>
     * @param colNum <div lang="zh-cn">列</div>
     * @param value <div lang="zh-cn">值。value的值如果是byte[]类型，则插入为图片，否则就把值设置为value的字符串类型值。</div>
     * @return
     */
    public static XWPFTableCell setTableCellValue(XWPFTable table,int rowNum,int colNum,Object value)
    {
        int rows = table.getNumberOfRows();
        for(int i=0;i<=rowNum-rows;i++)
        {
            table.createRow();
        }

        XWPFTableRow row = table.getRow(rowNum);
        List<XWPFTableCell> cells = row.getTableCells();
        int cols = cells.size();
        for(int i=0;i<=colNum-cols;i++)
        {
            row.createCell();
        }
        XWPFTableCell cell = row.getCell(colNum);
        if(value instanceof  byte[])
        {
            byte[] bytes = (byte[])value;
            XWPFRun runPic = cell.addParagraph().createRun();
            addPicture(runPic,bytes);
        }
        else
        {
            cell.setText(CommonHelpers.toString(value));
        }
        return cell;
    }

    /**
     * <div lang="zh-cn">对于cell这个格的每个XWPFRun调用action这个lambda方法。</div>
     * @param cell
     * @param action
     */
    public static void forEachRun(XWPFTableCell cell, Consumer<? super XWPFRun> action)
    {
        cell.getParagraphs().forEach(p->p.getRuns().forEach(action));
    }

    /**
     * <div lang="zh-cn">在doc中添加一个尺寸为(width,height)的图表对象，XWPFChart类型。</div>
     * @param doc <div lang="zh-cn">word文档对象</div>
     * @param width <div lang="zh-cn">图表的宽度，单位为像素</div>
     * @param height <div lang="zh-cn">图表的宽度，单位为像素</div>
     * @return
     */
    public static XWPFChart createChart(XWPFDocument doc, int width, int height)
    {
        try
        {
            return doc.createChart(Units.toEMU(width),Units.toEMU(height));
        } catch (InvalidFormatException | IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">获取Word对象的word文档的byte数组内容。</div>
     * @param doc
     * @return
     */
    public static byte[] toByteArray(XWPFDocument doc)
    {
        try(ByteArrayOutputStream os = new ByteArrayOutputStream())
        {
            doc.write(os);
            return os.toByteArray();
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">把XWPFDocument对象代表的word文档保存到filename文件中。</div>
     * @param doc
     * @param filename
     */
    public static void saveToFile(XWPFDocument doc,String filename)
    {
        saveToFile(doc,new File(filename));
    }

    /**
     * <div lang="zh-cn">把XWPFDocument对象代表的word文档保存到file文件中。</div>
     * @param doc
     * @param file
     */
    public static void saveToFile(XWPFDocument doc,File file)
    {
        String ext = IOHelpers.getExtension(file);
        if(!"docx".equalsIgnoreCase(ext))
        {
            throw new IllegalArgumentException("extension of filename should be docx");
        }

        IOHelpers.mkParentDirs(file);
        try(FileOutputStream fos = new FileOutputStream(file))
        {
            doc.write(fos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">关闭Word对象。</div>
     * @param doc
     */
    public static void close(POIXMLDocument doc)
    {
        CommonHelpers.close(doc);
    }
}
