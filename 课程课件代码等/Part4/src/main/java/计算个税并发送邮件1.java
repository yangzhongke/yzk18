import com.yzk18.docs.ExcelHelpers;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class 计算个税并发送邮件1 {
    public static void main(String[] args) {
        Workbook wb = ExcelHelpers.openFile("d:/2020年4月份工资表.xlsx");
        Sheet sheet = wb.getSheetAt(0);
        for(int i=1;i<=sheet.getLastRowNum();i++)
        {
            String 姓名 = ExcelHelpers.getCellStringValue(sheet,i,0);
            String 部门 = ExcelHelpers.getCellStringValue(sheet,i,1);
            //遇到员工数据行结束，就break
            if(姓名==null||姓名.equals(""))
            {
                break;
            }
            double 基本工资 = ExcelHelpers.getCellDoubleValue(sheet,i,2);
            double 绩效工资 = ExcelHelpers.getCellDoubleValue(sheet,i,3);
            double 奖金 = ExcelHelpers.getCellDoubleValue(sheet,i,4);
            Double 考勤罚款 = ExcelHelpers.getCellDoubleValue(sheet,i,5);
            if(考勤罚款==null)
            {
                考勤罚款=0.0;
            }
            double 社保 = ExcelHelpers.getCellDoubleValue(sheet,i,6);
            //应纳税所得额=基本工资+绩效工资+奖金+考勤罚款+社保-5000
            //个税=应纳税所得额*税率-速算扣除数
            double 应纳税所得额 = 基本工资+绩效工资+奖金+考勤罚款+社保-5000;
            double 税率;
            double 速算扣除数;
            if(应纳税所得额<=3000)
            {
                税率=0.03;
                速算扣除数=0;
            }
            else if(应纳税所得额>3000 && 应纳税所得额<=12000)
            {
                税率=0.1;
                速算扣除数=210;
            }
            //if(12000<应纳税所得额<=25000)
            else if(应纳税所得额>12000 && 应纳税所得额<=25000)
            {
                税率=0.2;
                速算扣除数=1410;
            }
            else if(应纳税所得额>25000 && 应纳税所得额 <=35000)
            {
                税率=0.25;	速算扣除数=2660;
            }
            else if(应纳税所得额>35000 &&  应纳税所得额<=55000)
            {
                税率=0.3;		速算扣除数=4410;
            }
            else if(应纳税所得额>55000 && 应纳税所得额<=80000)
            {
                税率=0.35;	速算扣除数=7160;
            }
            else
            {
                税率=0.45;	速算扣除数=15160;
            }
            double 个税=应纳税所得额*税率-速算扣除数;
            ExcelHelpers.setCellValue(sheet,i,7,个税);
            //System.out.println(基本工资+","+个税);
        }
        ExcelHelpers.saveToFile(wb,"d:/2020年4月份工资表-算完了个税.xlsx");
        ExcelHelpers.close(wb);
    }
}
