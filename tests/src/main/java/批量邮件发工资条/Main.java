package 批量邮件发工资条;

import com.yzk18.GUI.GUI;
import com.yzk18.commons.IOHelpers;
import com.yzk18.docs.ExcelHelpers;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.LinkedHashMap;

import static com.yzk18.GUI.GUI.*;
import static com.yzk18.docs.ExcelHelpers.*;

public class Main {
    public static void main(String[] args) {
        String 原始表路径 = fileOpenBox("选择本月工资表","xlsx");
        var wb工资表 = openFile(原始表路径);
        var sheet = wb工资表.getSheetAt(0);
        if(!"姓名".equals(getCellStringValue(sheet,0,0))||
                !"实发工资".equals(getCellStringValue(sheet,0,8)))
        {
            errorBox("文件格式不对");
            return;
        }
        for(int i = 1;i<=sheet.getLastRowNum()-2;i++)
        {
            String 姓名 = getCellStringValue(sheet,i,0);
            double 基本工资 = getCellDoubleValue(sheet,i,2);
            double 绩效工资 = getCellDoubleValue(sheet,i,3);
            double 奖金 = getCellDoubleValue(sheet,i,4);
            Double 考勤罚款 = getCellDoubleValue(sheet,i,5);
            if(考勤罚款==null)
            {
                考勤罚款=0.0;
            }
            double 社保 = getCellDoubleValue(sheet,i,6);
            //double 合计=getCellDoubleValue(sheet,i,8);
            //System.out.println("合计："+合计);
            double 应纳税所得额=基本工资+绩效工资+奖金+考勤罚款+社保-5000;
            double 税率=0;
            double 速算扣除数=0;
            if(应纳税所得额<=3000)
            {
                税率=0.03;
                速算扣除数=0;
            }
            else if(应纳税所得额>3000&&应纳税所得额<=12000)
            {
                税率=0.1;
                速算扣除数=210;
            }
            else if(应纳税所得额>12000&&应纳税所得额<=25000)
            {
                税率=0.2;
                速算扣除数=1410;
            }
            else if(应纳税所得额>25000&&应纳税所得额<=35000)
            {
                税率=0.25;
                速算扣除数=2660;
            }
            else if(应纳税所得额>35000&&应纳税所得额<=55000)
            {
                税率=0.3;
                速算扣除数=4410;
            }
            else if(应纳税所得额>55000&&应纳税所得额<=80000)
            {
                税率=0.35;
                速算扣除数=7160;
            }
            else
            {
                税率=0.45;
                速算扣除数=15160;
            }
            double 个税=应纳税所得额*税率-速算扣除数;
            //System.out.println(姓名+","+基本工资+","+个税);
            setCellValue(sheet,i,7,-个税);
        }
        //https://blog.csdn.net/hantiannan/article/details/6733955
        evaluateAllFormulas(wb工资表);//公式不会自动计算，需要手动调用。openFile中已经自动调用了evaluateAllFormulas，
        //因此在打开的时候不需要手动触发计算，但是如果中间修改了单元格的值，则需要手动evaluateAllFormulas才能读到新的值
        double d1= getCellDoubleValue(wb工资表.getSheetAt(0),1,8);
        System.out.println(d1);

        String newFileName = IOHelpers.getParentDir(原始表路径)+"/"+IOHelpers.getFileNameWithoutExtension(原始表路径)
                +"-完成扣税计算.xlsx";
        ExcelHelpers.saveToFile(wb工资表,newFileName);

        var wbNew = ExcelHelpers.openFile(newFileName);
        var sheet工资表 = wbNew.getSheetAt(0);
        double d= getCellDoubleValue(sheet工资表,1,8);
        System.out.println(d);


        var map员工的邮箱 = load员工信息表(GUI.fileOpenBox("请选择员工信息表","xlsx"));
        for(int i=1;i<=sheet工资表.getLastRowNum()-2;i++)
        {
            String name = getCellStringValue(sheet工资表,i,0);
            String deptName = getCellStringValue(sheet工资表,i,1);
            double 实发工资 =getCellDoubleValue(sheet工资表,i,8);
            String email = map员工的邮箱.get(deptName+"-"+name);
            System.out.println(email+","+实发工资);
        }
    }

    static LinkedHashMap<String,String> load员工信息表(String fileName)
    {
        var wb员工信息表 = openFile(fileName);
        var map员工的邮箱 = new LinkedHashMap<String,String>();
        for(int i=0;i<wb员工信息表.getNumberOfSheets();i++)
        {
            var sheet部门 = wb员工信息表.getSheetAt(i);
            String 部门名称 =sheet部门.getSheetName();
            for(int rowNum=1;rowNum<=sheet部门.getLastRowNum();rowNum++)
            {
                Row row = sheet部门.getRow(rowNum);
                String name = row.getCell(0).getStringCellValue();
                String email = row.getCell(3).getStringCellValue();
                map员工的邮箱.put(部门名称+"-"+name,email);
            }
        }
        return map员工的邮箱;
    }
}
