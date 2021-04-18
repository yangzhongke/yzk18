import com.yzk18.commons.CommonHelpers;
import com.yzk18.docs.ExcelHelpers;
import org.apache.poi.ss.usermodel.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;

public class TestExcel1 {
    public static void main(String[] args) {
        //用户只要通过x，y坐标赋值即可，自动创建createSheet、createRow等，还可以根据数据类型自动设置CellType
        {
            LinkedList<Person> persons = new LinkedList<>();
            persons.add(new Person().setName("杨中科").setHeight(180)
                    .setBirthDay(LocalDate.of(1981, 8, 8))
                    .setPhoneNum("18999999999")
            );
            persons.add(new Person().setName("张三").setHeight(160)
                    .setBirthDay(LocalDate.of(1986, 5, 8))
                    .setPhoneNum("13998888888888888"));

            String filename = "d:\\temp\\write.xlsx";
            Workbook wb = ExcelHelpers.createXLSX();
            Sheet sheet = wb.createSheet("学生表");
            ExcelHelpers.setCellValue(sheet, 0, 0, "姓名");
            ExcelHelpers.setCellValue(sheet, 0, 1, "身高");
            ExcelHelpers.setCellValue(sheet, 0, 2, "生日");
            ExcelHelpers.setCellValue(sheet, 0, 3, "手机号");
            for (int i = 0; i < persons.size(); i++) {
                Person p = persons.get(i);
                ExcelHelpers.setCellValue(sheet, i + 1, 0, p.getName());
                ExcelHelpers.setCellValue(sheet, i + 1, 1, p.getHeight());
                ExcelHelpers.setCellValue(sheet, i + 1, 2, p.getBirthDay());
                ExcelHelpers.setCellValue(sheet, i + 1, 3, p.getPhoneNum());
            }
            ExcelHelpers.setCellValue(sheet, persons.size()+5, 4, LocalDateTime.now());
            ExcelHelpers.saveToFile(wb, filename);
            CommonHelpers.close(wb);
            //DesktopHelpers.openFile(filename);
        }
        {
            String filename = "d:\\temp\\write.xlsx";
            Workbook wb = ExcelHelpers.openFile(filename);
            Sheet sheet = wb.getSheetAt(0);
            int firstRowNum = sheet.getFirstRowNum();
            int lastRowNum = sheet.getLastRowNum();
            System.out.println(firstRowNum+","+lastRowNum);
            int lastDataRowNum=0;
            for(int i=1;i<=lastRowNum;i++)
            {
                Row row = sheet.getRow(i);
                if(row==null) {
                    lastDataRowNum = i;
                    break;//last line of data
                }
                String name = ExcelHelpers.getCellStringValue(row.getCell(0));
                int age = ExcelHelpers.getCellIntValue(row.getCell(1));
                LocalDate birthDay = ExcelHelpers.getCellLocalDateValue(row.getCell(2));
                String phoneNum = ExcelHelpers.getCellStringValue(row.getCell(3));
                System.out.println(name+","+age+","+birthDay+","+phoneNum);
            }
            LocalDateTime dt = ExcelHelpers.getCellLocalDateTimeValue(sheet,lastDataRowNum,4);
            System.out.println(dt);
            CommonHelpers.close(wb);
        }
    }
}
