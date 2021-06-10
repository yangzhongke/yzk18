import com.yzk18.docs.ExcelHelpers;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class 创建Excel文件1 {
    public static void main(String[] args) {
        XSSFWorkbook workbook = ExcelHelpers.createXLSX();
        XSSFSheet sheet = workbook.createSheet();
        ExcelHelpers.setCellValue(sheet,0,0,"姓名");
        ExcelHelpers.setCellValue(sheet,0,1,"年龄");
        ExcelHelpers.setCellValue(sheet,0,2,"手机号");

        ExcelHelpers.setCellValue(sheet,1,0,"杨中科");
        ExcelHelpers.setCellValue(sheet,1,1,18);
        ExcelHelpers.setCellValue(sheet,1,2,"189999999999");

        ExcelHelpers.setCellValue(sheet,2,0,"Zack");
        ExcelHelpers.setCellValue(sheet,2,1,80);
        ExcelHelpers.setCellValue(sheet,2,2,"139999999999");

        ExcelHelpers.saveToFile(workbook,"d:/1.xlsx");
        ExcelHelpers.close(workbook);
    }
}
