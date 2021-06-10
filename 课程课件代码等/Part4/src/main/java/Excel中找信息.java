import com.yzk18.commons.IOHelpers;
import com.yzk18.docs.ExcelHelpers;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class Excel中找信息 {
    public static void main(String[] args) {
       String[] files = IOHelpers.getFilesRecursively("D:\\缴费记录","xlsx");
        for(String file : files)
        {
            Workbook workbook = ExcelHelpers.openFile(file);
            for(int sheetIndex =0;sheetIndex<workbook.getNumberOfSheets();sheetIndex++)
            {
                Sheet sheet = workbook.getSheetAt(sheetIndex);
                for(int rowIndex = sheet.getFirstRowNum();rowIndex<=sheet.getLastRowNum();rowIndex++)
                {
                    Row row = sheet.getRow(rowIndex);
                    if(row==null)
                        continue;
                    for(int colIndex = row.getFirstCellNum();colIndex<=row.getLastCellNum();colIndex++)
                    {
                        Cell cell = row.getCell(colIndex);
                        if(cell==null)
                            continue;
                        String value = ExcelHelpers.getCellStringValue(cell);
                        if(value==null)
                            continue;
                        if(value.contains("杨中科"))
                        {
                            System.out.println("找到一个："+file+","+sheet.getSheetName()
                                    +"，第"+rowIndex+"的第"+colIndex+"列");
                        }
                    }
                }
            }
        }
    }
}
