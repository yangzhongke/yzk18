import com.yzk18.docs.ExcelHelpers;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class 公司流水表生成Word报表 {
    public static void main(String[] args) {
        Workbook wb = ExcelHelpers.openFile("E:\\主同步盘\\我的坚果云\\UoZ\\SE101-玩着学编程\\Part4课件\\公司账目2021年.xlsx");
        int sheetCount = wb.getNumberOfSheets();
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
        for(int sheetIndex = 0;sheetIndex<sheetCount;sheetIndex++)
        {
            Sheet sheet = wb.getSheetAt(sheetIndex);
            String 月份名 = sheet.getSheetName();
            double 学费收入总额 = 0;
            double 其他收入总额=0;
            double 工资支出总额 =0;
            double 其他支出总额  = 0;
            double 总收入 =0;
            double 总支出 =0;

            int lastRowNum = sheet.getLastRowNum();
            for(int rowIndex = 1;rowIndex<=lastRowNum;rowIndex++)
            {
                String 收支类别 = ExcelHelpers.getCellStringValue(sheet,rowIndex,2);
                double 金额 = ExcelHelpers.getCellDoubleValue(sheet,rowIndex,3);
                if(金额>0)
                {
                    总收入=总收入+金额;
                }
                else
                {
                    总支出=总支出+金额;
                }
                switch (收支类别)
                {
                    case "学费收入":
                        学费收入总额+=金额;
                        break;
                    case "工资支出":
                        工资支出总额+=金额;
                        break;
                    default:
                        if(金额>0)
                        {
                            其他收入总额=其他收入总额+金额;
                        }
                        else
                        {
                            其他支出总额+=金额;
                        }
                        break;
                }
            }
            System.out.println("月份名:"+月份名+",学费收入总额:"
                    +df.format(学费收入总额)+",其他收入总额="+df.format(其他收入总额)
                        +",工资支出总额:"+df.format(工资支出总额)
                    +",其他支出总额:"+df.format(其他支出总额)+",总收入:"
                    +df.format(总收入)+",总支出="+df.format(总支出));
        }
        ExcelHelpers.close(wb);
    }
}
