package com.yzk18.docs;

import com.yzk18.commons.CommonHelpers;
import com.yzk18.commons.IOHelpers;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 * <div lang="zh-cn">Excel文件处理的帮助类。</div>
 */
public class ExcelHelpers {

    /**
     * <div lang="zh-cn">创建旧的2003格式(*.xls)的Excel文档Workbook对象。</div>
     * @return
     */
    public static HSSFWorkbook createXLS()
    {
        return new HSSFWorkbook();
    }

    /**
     * <div lang="zh-cn">创建新格式(*.xlsx)的Excel文档Workbook对象。</div>
     * @return
     */
    public static XSSFWorkbook createXLSX()
    {
        return new XSSFWorkbook();
    }

    /**
     * <div lang="zh-cn">创建CellStyle对象</div>
     * @param cell
     * @return
     */
    public static CellStyle createCellStyle(Cell cell)
    {
        CellStyle cellStyle = cell.getSheet().getWorkbook().createCellStyle();
        return  cellStyle;
    }

    /**
     * <div lang="zh-cn">设置cell的值，根据传入的value类型会自动设置单元格的cellStyle。支持String、int、double、LocalDate、LocalTime、LocalDateTime、Date、boolean等类型。</div>
     * @param cell
     * @param value
     */
    public static void setCellValue(Cell cell, Object value)
    {
        if(value==null)
        {
            cell.setBlank();
        }
        else if(value instanceof  CharSequence)
        {
            cell.setCellValue(value.toString());
        }
        else if(value instanceof  Number)
        {
            Number number = (Number)value;
            cell.setCellValue(number.doubleValue());
        }
        else if(value instanceof Boolean)
        {
            Boolean b = (Boolean)value;
            cell.setCellValue(b);
        }
        else if(value instanceof LocalDate)
        {
            //cell.getCellStyle() doesn't work,
            CellStyle cellStyle = createCellStyle(cell);
            cellStyle.setDataFormat((short)0xe);
            cell.setCellStyle(cellStyle);
            LocalDate d= (LocalDate)value;
            cell.setCellValue(d);
        }
        else if(value instanceof LocalDateTime)
        {
            CellStyle cellStyle = createCellStyle(cell);
            cellStyle.setDataFormat((short)0x16);
            cell.setCellStyle(cellStyle);
            LocalDateTime dt= (LocalDateTime)value;
            cell.setCellValue(dt);
        }
        else if(value instanceof Calendar)
        {
            CellStyle cellStyle = createCellStyle(cell);
            cellStyle.setDataFormat((short)0x16);
            cell.setCellStyle(cellStyle);
            Calendar d= (Calendar)value;
            cell.setCellValue(d);
        }
        else if(value instanceof Date)
        {
            CellStyle cellStyle = createCellStyle(cell);
            cellStyle.setDataFormat((short)0xe);
            cell.setCellStyle(cellStyle);
            Date d= (Date)value;
            cell.setCellValue(d);
        }
        else
        {
            cell.setCellValue(value.toString());
        }
    }

    /**
     * <div lang="zh-cn">设置sheet这个页的第rowIndex行的第colIndex列的值为value。
     * 根据传入的value类型会自动设置单元格的cellStyle。支持String、int、double、LocalDate、LocalTime、LocalDateTime、Date、boolean等类型。
     * 如果设定的行或者列不存在，则会自动创建行或者列。
     * </div>
     * @param sheet
     * @param rowIndex
     * @param colIndex
     * @param value
     */
    public static void setCellValue(Sheet sheet, int rowIndex, int colIndex, Object value)
    {
        Row row = sheet.getRow(rowIndex);
        if(row==null)
        {
            row = sheet.createRow(rowIndex);
        }
        Cell cell = row.getCell(colIndex);
        if(cell==null)
        {
            cell = row.createCell(colIndex);
        }
        setCellValue(cell,value);
    }

    /**
     * <div lang="zh-cn">得到sheet的第rowIndex行的第colIndex列的单元格。
     * 如果行或者列不存在，则返回null。
     * </div>
     * @param sheet
     * @param rowIndex
     * @param colIndex
     * @return
     */
    public static Cell getCell(Sheet sheet, int rowIndex, int colIndex)
    {
        Row row = sheet.getRow(rowIndex);
        if(row==null)
        {
            return null;
        }
        return row.getCell(colIndex);
    }

    /**
     * <div lang="zh-cn">获得sheet的第rowIndex行的第colIndex列的Integer类型的值，如果值是空的或者不存在这个单元格，则返回null。</div>
     * @param sheet
     * @param rowIndex
     * @param colIndex
     * @return
     */
    public static Integer getCellIntValue(Sheet sheet, int rowIndex, int colIndex)
    {
        Cell cell = getCell(sheet,rowIndex, colIndex);
        if(cell==null)
        {
            return null;
        }
        return getCellIntValue(cell);
    }

    /**
     * <div lang="zh-cn">获得cell的Integer类型的值，如果值是空的或者不存在这个单元格，则返回null。</div>
     * @param cell
     * @return
     */
    public static Integer getCellIntValue(Cell cell)
    {
        CellType cellType = cell.getCellType();
        switch (cellType)
        {
            case BLANK:
            case ERROR:
            case _NONE:
                return null;
            case BOOLEAN:
                boolean b = cell.getBooleanCellValue();
                return b?1:0;
            case FORMULA:
            case NUMERIC:
                double d = cell.getNumericCellValue();
                return (int)d;
            case STRING:
                String s = cell.getStringCellValue();
                return CommonHelpers.toInt(s);
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * <div lang="zh-cn">获得sheet的第rowIndex行的第colIndex列的Double类型的值，如果值是空的或者不存在这个单元格，则返回null。</div>
     * @param sheet
     * @param rowIndex
     * @param colIndex
     * @return
     */
    public static Double getCellDoubleValue(Sheet sheet, int rowIndex, int colIndex)
    {
        Cell cell = getCell(sheet,rowIndex, colIndex);
        if(cell==null)
        {
            return null;
        }
        return getCellDoubleValue(cell);
    }

    /**
     * <div lang="zh-cn">获得cell的Double类型的值，如果值是空的或者不存在这个单元格，则返回null。</div>
     * @param cell
     * @return
     */
    public static Double getCellDoubleValue(Cell cell)
    {
        CellType cellType = cell.getCellType();
        switch (cellType)
        {
            case BLANK:
            case ERROR:
            case _NONE:
                return null;
            case BOOLEAN:
                boolean b = cell.getBooleanCellValue();
                return b?1.0:0.0;
            case FORMULA:
            case NUMERIC:
                double d = cell.getNumericCellValue();
                return d;
            case STRING:
                String s = cell.getStringCellValue();
                return CommonHelpers.toDouble(s);
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * <div lang="zh-cn">获得sheet的第rowIndex行的第colIndex列的String类型的值，如果值是空的或者不存在这个单元格，则返回null。</div>
     * @param sheet
     * @param rowIndex
     * @param colIndex
     * @return
     */
    public static String getCellStringValue(Sheet sheet, int rowIndex, int colIndex)
    {
        Cell cell = getCell(sheet,rowIndex, colIndex);
        if(cell==null)
        {
            return null;
        }
        return getCellStringValue(cell);
    }

    /**
     * <div lang="zh-cn">获得cell的String类型的值，如果值是空的或者不存在这个单元格，则返回null。</div>
     * @param cell
     * @return
     */
    public static String getCellStringValue(Cell cell)
    {
        CellType cellType = cell.getCellType();
        switch (cellType)
        {
            case BLANK:
            case ERROR:
            case _NONE:
                return null;
            case BOOLEAN:
                boolean b = cell.getBooleanCellValue();
                return Boolean.toString(b);
            case NUMERIC:
                double d = cell.getNumericCellValue();
                return Double.toString(d);
            case FORMULA:
            case STRING:
                String s = cell.getStringCellValue();
                return s;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * <div lang="zh-cn">获得sheet的第rowIndex行的第colIndex列的LocalDate类型的值，如果值是空的或者不存在这个单元格，则返回null。</div>
     * @param sheet
     * @param rowIndex
     * @param colIndex
     * @return
     */
    public static LocalDate getCellLocalDateValue(Sheet sheet, int rowIndex, int colIndex)
    {
        Cell cell = getCell(sheet,rowIndex, colIndex);
        if(cell==null)
        {
            return null;
        }
        return getCellLocalDateValue(cell);
    }

    /**
     * <div lang="zh-cn">获得cell的LocalDate类型的值，如果值是空的或者不存在这个单元格，则返回null。</div>
     * @param cell
     * @return
     */
    public static LocalDate getCellLocalDateValue(Cell cell)
    {
        CellType cellType = cell.getCellType();
        switch (cellType)
        {
            case BLANK:
            case ERROR:
            case _NONE:
            case BOOLEAN:
                return null;
            case FORMULA:
            case NUMERIC:
                LocalDateTime d = cell.getLocalDateTimeCellValue();
                return d.toLocalDate();
            case STRING:
                String s = cell.getStringCellValue();
                return CommonHelpers.toLocalDate(s);
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * <div lang="zh-cn">获得sheet的第rowIndex行的第colIndex列的LocalDateTime类型的值，如果值是空的或者不存在这个单元格，则返回null。</div>
     * @param sheet
     * @param rowIndex
     * @param colIndex
     * @return
     */
    public static LocalDateTime getCellLocalDateTimeValue(Sheet sheet, int rowIndex, int colIndex)
    {
        Cell cell = getCell(sheet,rowIndex, colIndex);
        if(cell==null)
        {
            return null;
        }
        return getCellLocalDateTimeValue(cell);
    }

    /**
     * <div lang="zh-cn">获得cell的LocalDateTime类型的值，如果值是空的或者不存在这个单元格，则返回null。</div>
     * @param cell
     * @return
     */
    public static LocalDateTime getCellLocalDateTimeValue(Cell cell)
    {
        CellType cellType = cell.getCellType();
        switch (cellType)
        {
            case BLANK:
            case ERROR:
            case _NONE:
            case BOOLEAN:
                return null;
            case FORMULA:
            case NUMERIC:
                LocalDateTime d = cell.getLocalDateTimeCellValue();
                return d;
            case STRING:
                String s = cell.getStringCellValue();
                return CommonHelpers.toLocalDateTime(s);
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * <div lang="zh-cn">在sheet上创建一个图表对象，显示到左上角坐标为(col1,row1)、右下角坐标为(col2,row2)这个位置。</div>
     * @param sheet
     * @param col1 <div lang="zh-cn">左上角的列号</div>
     * @param row1 <div lang="zh-cn">左上角的行号</div>
     * @param col2 <div lang="zh-cn">右下角的列号</div>
     * @param row2 <div lang="zh-cn">右下角的行号</div>
     * @return
     */
    public static XSSFChart createChart(Sheet sheet,int col1, int row1, int col2, int row2)
    {
        return createChart(sheet,0,0,0,0,col1,row1,col2,row2);
    }

    /**
     * <div lang="zh-cn">在sheet上创建一个图表对象，显示到左上角坐标为(col1,row1)、右下角坐标为(col2,row2)这个位置。
     * dx1、dy1是左上角在单元格内的偏移量，dx2、dy2是右下角在单元格内的偏移量。</div>
     * @param sheet
     * @param dx1 左上角在单元格内的x坐标偏移量
     * @param dy1 左上角在单元格内的y坐标偏移量
     * @param dx2 右下角在单元格内的x坐标偏移量
     * @param dy2 右下角在单元格内的y坐标偏移量
     * @param col1 <div lang="zh-cn">左上角的列号</div>
     * @param row1 <div lang="zh-cn">左上角的行号</div>
     * @param col2 <div lang="zh-cn">右下角的列号</div>
     * @param row2 <div lang="zh-cn">右下角的行号</div>
     * @return
     */
    public static XSSFChart createChart(Sheet sheet,int dx1, int dy1, int dx2, int dy2,
                                        int col1, int row1, int col2, int row2)
    {
        XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
        //前四个默认0，[0,5]：从0列5行开始;[7,26]:宽度7个单元格，26向下扩展到26行
        //默认宽度(14-8)*12
        XSSFClientAnchor anchor = drawing.createAnchor(dx1,dy1,dx2,dy2,col1,row1,col2,row2);
        XSSFChart chart = drawing.createChart(anchor);
        return chart;
    }

    /**
     * <div lang="zh-cn">重新计算workbook这个表格中所有的公式。</div>
     * @param wb
     */
    //https://blog.csdn.net/hantiannan/article/details/6733955
    public static void evaluateAllFormulas(Workbook wb)
    {
        wb.getCreationHelper().createFormulaEvaluator().evaluateAll();
    }

    /**
     * <div lang="zh-cn">把workbook保存到文件file中。</div>
     * @param workbook
     * @param file
     */
    public static void saveToFile(Workbook workbook,File file)
    {
        String ext = IOHelpers.getExtension(file);
        if(workbook instanceof XSSFWorkbook)
        {
            if(!"xlsx".equalsIgnoreCase(ext))
            {
                throw new IllegalArgumentException("extension of filename should be xlsx");
            }
        }
        if(workbook instanceof HSSFWorkbook)
        {
            if(!"xls".equalsIgnoreCase(ext))
            {
                throw new IllegalArgumentException("extension of filename should be xls");
            }
        }
        byte[] bytes = toByteArray(workbook);
        IOHelpers.writeAllBytes(file,bytes);
    }

    /**
     *<div lang="zh-cn">把workbook保存到文件filename中。</div>
     * @param workbook
     * @param filename
     */
    public static void saveToFile(Workbook workbook,String filename)
    {
        saveToFile(workbook,new File(filename));
    }

    /**
     * <div lang="zh-cn">把workbook生成为内容的字节数组</div>
     * @param workbook
     * @return
     */
    public static  byte[] toByteArray(Workbook workbook)
    {
        //https://blog.csdn.net/hantiannan/article/details/6733955
        workbook.setForceFormulaRecalculation(true);
        try(ByteArrayOutputStream fos = new ByteArrayOutputStream())
        {
            workbook.write(fos);
            return fos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">打开Excel文件file，返回文档的Workbook对象。</div>
     * @param file
     * @return
     */
    public static Workbook openFile(File file)
    {
        try
        {
            Workbook wb = WorkbookFactory.create(file);
            evaluateAllFormulas(wb);
            return wb;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">打开Excel文件，文件的内容是bytes，返回文档的Workbook对象。</div>
     * @param bytes
     * @return
     */
    public static Workbook openFile(byte[] bytes)
    {
        try(ByteArrayInputStream bais = new ByteArrayInputStream(bytes))
        {
            return openFile(bais);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">打开inStream代表的Excel文件，返回文档的Workbook对象。</div>
     * @param inStream
     * @return
     */
    public static Workbook openFile(InputStream inStream)
    {
        try
        {
            Workbook wb = WorkbookFactory.create(inStream);
            evaluateAllFormulas(wb);
            return wb;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">打开Excel文件filename，返回文档的Workbook对象。</div>
     * @param filename
     * @return
     */
    public static Workbook openFile(String filename)
    {
        return openFile(new File(filename));
    }

    /**
     * <div lang="zh-cn">关闭Workbook</div>
     * @param wb
     */
    public static void close(Workbook wb)
    {
        CommonHelpers.close(wb);
    }
}
