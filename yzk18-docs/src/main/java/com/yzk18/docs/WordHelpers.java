package com.yzk18.docs;

import com.yzk18.commons.CommonHelpers;
import com.yzk18.commons.IOHelpers;
import com.yzk18.commons.ImageHelpers;
import com.yzk18.commons.ImageType;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.poi.util.Units;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;

import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.function.Consumer;

public class WordHelpers {

    public static String readAllText(String file)
    {
        try(FileInputStream fis = new FileInputStream(file))
        {
            return readAllText(fis);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

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

    public static XWPFDocument createDocxDocument()
    {
        return new XWPFDocument();
    }

    public static XWPFDocument openDocx(InputStream inputStream)
    {
        try {
            return new XWPFDocument(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static XWPFDocument openDocx(String file)
    {
        try(FileInputStream fos = new FileInputStream((file)))
        {
            return openDocx(fos);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static XWPFRun createRun(XWPFDocument doc, String text)
    {
        return createRun(doc,text,ParagraphAlignment.LEFT);
    }
    public static XWPFRun createRun(XWPFDocument doc, String text, ParagraphAlignment alignment)
    {
        XWPFParagraph para = doc.createParagraph();
        para.setAlignment(alignment);
        XWPFRun run = para.createRun();
        run.setText(text);
        return run;
    }

    public static XWPFPicture addPicture(XWPFDocument doc,byte[] pictureData)
    {
        return addPicture(doc,pictureData,0,0);
    }

    public static XWPFPicture addPicture(XWPFDocument doc,byte[] pictureData,int width,int height)
    {
        XWPFRun run = createRun(doc,"");
        return addPicture(run,pictureData, width, height);
    }

    public static XWPFPicture addPicture(XWPFRun run,byte[] pictureData)
    {
        return addPicture(run,pictureData,0,0);
    }

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

    public static void forEachRun(XWPFTableCell cell, Consumer<? super XWPFRun> action)
    {
        cell.getParagraphs().forEach(p->p.getRuns().forEach(action));
    }

    public static void saveToFile(XWPFDocument doc,String filename)
    {
        String ext = IOHelpers.getExtension(filename);
        if(!"docx".equalsIgnoreCase(ext))
        {
            throw new IllegalArgumentException("extension of filename should be docx");
        }

        IOHelpers.mkParentDirs(filename);
        try(FileOutputStream fos = new FileOutputStream((filename)))
        {
            doc.write(fos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
