import com.yzk18.commons.IOHelpers;
import com.yzk18.docs.ExcelHelpers;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Arrays;

public class 合并Excel1 {
    public static void main(String[] args) {
        String dir = "D:\\公司人员信息";
        String[] excelFiles = IOHelpers.getFilesRecursively(dir, "xlsx");
        //System.out.println(Arrays.toString(excelFiles));
        XSSFWorkbook newWb = ExcelHelpers.createXLSX();
        XSSFSheet newSheet = newWb.createSheet();
        //创建表头
        ExcelHelpers.setCellValue(newSheet,0,0,"部门");
        ExcelHelpers.setCellValue(newSheet,0,1,"姓名");
        ExcelHelpers.setCellValue(newSheet,0,2,"电话");
        ExcelHelpers.setCellValue(newSheet,0,3,"性别");
        int rowNum = 1;
        for(String excelFile : excelFiles)
        {
            /*
            if(excelFile.contains("~$"))
                continue;*/
            //部门名称
            String deptName = IOHelpers.getFileNameWithoutExtension(excelFile);
            Workbook workbook = ExcelHelpers.openFile(excelFile);
            Sheet sheet = workbook.getSheetAt(0);
            int lastRowNum = sheet.getLastRowNum();
            for(int i=1;i<=lastRowNum;i++)
            {
                String name = ExcelHelpers.getCellStringValue(sheet,i,0);
                String phoneNum = ExcelHelpers.getCellStringValue(sheet,i,1);
                String gender = ExcelHelpers.getCellStringValue(sheet,i,2);
                //System.out.println(name+phoneNum+gender);
                ExcelHelpers.setCellValue(newSheet,rowNum,0,deptName);
                ExcelHelpers.setCellValue(newSheet,rowNum,1,name);
                ExcelHelpers.setCellValue(newSheet,rowNum,2,phoneNum);
                ExcelHelpers.setCellValue(newSheet,rowNum,3,gender);
                rowNum++;
            }
            ExcelHelpers.close(workbook);

        }
        ExcelHelpers.saveToFile(newWb,"d:/合并.xlsx");
        ExcelHelpers.close(newWb);
    }
}
