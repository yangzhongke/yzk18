package 批量邮件发工资条;

import com.yzk18.GUI.GUI;
import com.yzk18.commons.CommonHelpers;
import com.yzk18.commons.IOHelpers;
import com.yzk18.docs.ExcelHelpers;
import com.yzk18.docs.WordHelpers;
import com.yzk18.docs.WordTemplateRenderer;
import com.yzk18.net.MailSender;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFactory;

import java.io.File;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;

import static com.yzk18.GUI.GUI.*;
import static com.yzk18.docs.ExcelHelpers.*;

public class Main {
    public static void main(String[] args) {
        String 原始表路径 = fileOpenBox("选择本月工资表","xlsx");
        Workbook wb工资表 = openFile(原始表路径);
        Sheet sheet = wb工资表.getSheetAt(0);
        if(!"姓名".equals(getCellStringValue(sheet,0,0))||
                !"实发工资".equals(getCellStringValue(sheet,0,8)))
        {
            errorBox("文件格式不对");
            return;
        }
        计算个税(sheet);
        //https://blog.csdn.net/hantiannan/article/details/6733955
        evaluateAllFormulas(wb工资表);//公式不会自动计算，需要手动调用。openFile中已经自动调用了evaluateAllFormulas，
        //因此在打开的时候不需要手动触发计算，但是如果中间修改了单元格的值，则需要手动evaluateAllFormulas才能读到新的值

        double d1= getCellDoubleValue(wb工资表.getSheetAt(0),1,8);
        System.out.println(d1);

        String newFileName = new File(原始表路径).getParent()+"/"+IOHelpers.getFileNameWithoutExtension(原始表路径)
                +"-完成扣税计算.xlsx";
        ExcelHelpers.saveToFile(wb工资表,newFileName);

        Workbook wbNew = ExcelHelpers.openFile(newFileName);
        Sheet sheet工资表 = wbNew.getSheetAt(0);
        double d= getCellDoubleValue(sheet工资表,1,8);
        System.out.println(d);


        String 员工信息表文件名=GUI.fileOpenBox("请选择员工信息表","xlsx");
        LinkedHashMap<String,String> map员工的邮箱 = load员工信息表(员工信息表文件名);
        String 工资表模板文件名=GUI.fileOpenBox("请选择工资条模板文件","docx");
        XWPFDocument doc工资表模板 = WordHelpers.openDocx(工资表模板文件名);
        for(int i=1;i<=sheet工资表.getLastRowNum()-2;i++)
        {
            String name = getCellStringValue(sheet工资表,i,0);
            String deptName = getCellStringValue(sheet工资表,i,1);

            String email = map员工的邮箱.get(deptName+"-"+name);
            double 基本工资 = getCellDoubleValue(sheet,i,2);
            double 绩效工资 = getCellDoubleValue(sheet,i,3);
            double 奖金 = getCellDoubleValue(sheet,i,4);
            Double 考勤罚款 = getCellDoubleValue(sheet,i,5);
            if(考勤罚款==null)
            {
                考勤罚款=0.0;
            }
            double 社保 = getCellDoubleValue(sheet,i,6);
            double 个税 =getCellDoubleValue(sheet工资表,i,7);
            double 实发工资 =getCellDoubleValue(sheet工资表,i,8);
            //System.out.println(email+","+实发工资);

            LinkedHashMap<String,Object> params =new LinkedHashMap<>();
            params.put("${姓名}",name);
            params.put("${部门}",deptName);
            params.put("${基本工资}",基本工资);
            params.put("${绩效工资}",绩效工资);
            params.put("${奖金}",奖金);
            params.put("${考勤罚款}",考勤罚款);
            params.put("${社保}",社保);
            params.put("${个税}",个税);
            params.put("${实发工资}",实发工资);
            XWPFDocument 渲染后doc = WordTemplateRenderer.render(doc工资表模板,params);
            发送邮件(email,name,渲染后doc);
            CommonHelpers.close(渲染后doc);
        }
        CommonHelpers.close(doc工资表模板);


    }

    static void 发送邮件(String email, String name, XWPFDocument wordDoc)
    {
        //资源文件要放到resources底下，路径以/开头，放到src是不会被输出的
        InputStream inStream = Main.class.getResourceAsStream("/smtp.txt");
        String[] lines = IOHelpers.readAllLines(inStream);
        String hostName = lines[0];
        String from = lines[1];
        String pwd = System.getenv("163EmailPassword");
        //System.out.println(hostName+","+from+","+pwd);
        MailSender mailSender = new MailSender();
        mailSender.setTextMessage(name+"你好，您的本月工资表见附件。\r\n人力资源部。\r\n"+ LocalDate.now())
                .setSubject(name+"本月工资表")
                .setUserName(from)
                .setPassword(pwd)
                .setFrom(from,"人力资源部")
                .setHostName(hostName)
                .addTo(email,name)
                .addAttachment(name+"工资表.docx",WordHelpers.toByteArray(wordDoc))
                .send();

    }

    static void 计算个税(org.apache.poi.ss.usermodel.Sheet sheet) {
        for(int i = 1; i<= sheet.getLastRowNum()-2; i++)
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
    }

    static LinkedHashMap<String,String> load员工信息表(String fileName)
    {
        Workbook wb员工信息表 = openFile(fileName);
        LinkedHashMap<String,String> map员工的邮箱 = new LinkedHashMap<String,String>();
        for(int i=0;i<wb员工信息表.getNumberOfSheets();i++)
        {
            Sheet sheet部门 = wb员工信息表.getSheetAt(i);
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
