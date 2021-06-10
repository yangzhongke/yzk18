import com.yzk18.docs.ExcelHelpers;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class 遍历Excel文件1 {
    public static void main(String[] args) {
        Workbook workbook = ExcelHelpers.openFile("d:/Book1.xlsx");
        for(int sheetIndex = 0;sheetIndex<workbook.getNumberOfSheets();sheetIndex++)
        {
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            for(int rowIndex = sheet.getFirstRowNum();rowIndex<=sheet.getLastRowNum();rowIndex++)
            {
                Row row = sheet.getRow(rowIndex);
                if(row==null)
                {
                    continue;
                }
                for(int colIndex = row.getFirstCellNum();colIndex<=row.getLastCellNum();colIndex++)
                {
                    Cell cell = row.getCell(colIndex);
                    //cell.getStringCellValue()
                    if(cell==null)
                        continue;
                    String value = ExcelHelpers.getCellStringValue(cell);
                    System.out.println(value);
                }
            }
        }
        ExcelHelpers.close(workbook);
    }
}
