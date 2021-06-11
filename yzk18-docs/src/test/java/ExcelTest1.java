import com.yzk18.docs.ExcelHelpers;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelTest1 {
    public static void main(String[] args) {
        Workbook wb = ExcelHelpers.openFile("e:/temp/1.xlsx");
        Sheet sheet = wb.getSheetAt(0);
        String s = ExcelHelpers.getCellStringValue(sheet,0,2);
        System.out.println(s);
        System.out.println(ExcelHelpers.getCellDoubleValue(sheet,0,2));
    }
}
