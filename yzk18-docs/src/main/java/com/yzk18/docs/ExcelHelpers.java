package com.yzk18.docs;

import com.yzk18.commons.CommonHelpers;
import com.yzk18.commons.IOHelpers;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class ExcelHelpers {
    public static HSSFWorkbook createXLS()
    {
        return new HSSFWorkbook();
    }

    public static XSSFWorkbook createXLSX()
    {
        return new XSSFWorkbook();
    }

    public static CellStyle createCellStyle(Cell cell)
    {
        CellStyle cellStyle = cell.getSheet().getWorkbook().createCellStyle();
        return  cellStyle;
    }

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

    public static Cell getCell(Sheet sheet, int rowIndex, int colIndex)
    {
        Row row = sheet.getRow(rowIndex);
        if(row==null)
        {
            return null;
        }
        return row.getCell(colIndex);
    }

    public static Integer getCellIntValue(Sheet sheet, int rowIndex, int colIndex)
    {
        Cell cell = getCell(sheet,rowIndex, colIndex);
        if(cell==null)
        {
            return null;
        }
        return getCellIntValue(cell);
    }

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

    public static Double getCellDoubleValue(Sheet sheet, int rowIndex, int colIndex)
    {
        Cell cell = getCell(sheet,rowIndex, colIndex);
        if(cell==null)
        {
            return null;
        }
        return getCellDoubleValue(cell);
    }

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

    public static String getCellStringValue(Sheet sheet, int rowIndex, int colIndex)
    {
        Cell cell = getCell(sheet,rowIndex, colIndex);
        if(cell==null)
        {
            return null;
        }
        return getCellStringValue(cell);
    }

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

    public static LocalDate getCellLocalDateValue(Sheet sheet, int rowIndex, int colIndex)
    {
        Cell cell = getCell(sheet,rowIndex, colIndex);
        if(cell==null)
        {
            return null;
        }
        return getCellLocalDateValue(cell);
    }

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

    public static LocalDateTime getCellLocalDateTimeValue(Sheet sheet, int rowIndex, int colIndex)
    {
        Cell cell = getCell(sheet,rowIndex, colIndex);
        if(cell==null)
        {
            return null;
        }
        return getCellLocalDateTimeValue(cell);
    }

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

    public static XSSFChart createChart(Sheet sheet,int col1, int row1, int col2, int row2)
    {
        return createChart(sheet,0,0,0,0,col1,row1,col2,row2);
    }

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

    //https://blog.csdn.net/hantiannan/article/details/6733955
    public static void evaluateAllFormulas(Workbook wb)
    {
        wb.getCreationHelper().createFormulaEvaluator().evaluateAll();
    }

    public static void saveToFile(Workbook workbook,String filename)
    {
        String ext = IOHelpers.getExtension(filename);
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
        //https://blog.csdn.net/hantiannan/article/details/6733955
        workbook.setForceFormulaRecalculation(true);
        IOHelpers.mkParentDirs(filename);
        try(FileOutputStream fos = new FileOutputStream(filename))
        {
            workbook.write(fos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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

    public static Workbook openFile(String filename)
    {
        return openFile(new File(filename));
    }

    public static void close(Workbook wb)
    {
        CommonHelpers.close(wb);
    }
}
