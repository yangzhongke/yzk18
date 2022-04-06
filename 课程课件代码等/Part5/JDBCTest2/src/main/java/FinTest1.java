import com.yzk18.commons.JDBCExecutor;
import com.yzk18.commons.JDBCRow;
import com.yzk18.docs.ExcelHelpers;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class FinTest1 {
    public static void main(String[] args) {
        JDBCExecutor jdbcExecutor = new JDBCExecutor("jdbc:sqlite:E:/fin.db",null,null);
        /*
        Workbook wb = ExcelHelpers.openFile("E:\\主同步盘\\我的坚果云\\UoZ\\SE101-玩着学编程\\Part5课件\\测试用的财务流水帐.xlsx");
        Sheet sheet = wb.getSheetAt(0);
        for(int i=1;i<=sheet.getLastRowNum();i++)
        {
            String date = ExcelHelpers.getCellStringValue(sheet,i,0);
            String item = ExcelHelpers.getCellStringValue(sheet,i,1);
            Double inAmount = ExcelHelpers.getCellDoubleValue(sheet,i,2);
            Double outAmount = ExcelHelpers.getCellDoubleValue(sheet,i,3);
            System.out.println(date+","+item+","+inAmount+","+outAmount);
            String dir = inAmount!=null?"In":"Out";
            Double amount = inAmount!=null?inAmount:outAmount;
            jdbcExecutor.execute("Insert into FinItems(Date,Item,Amount,Dir) values(?,?,?,?)",
                    date,item,amount,dir);
        }*/
        /*
        JDBCRow row =  jdbcExecutor.query("select sum(Amount) as amount from FinItems where Dir='In'").get(0);
        Double amount =  row.getDouble("amount");
        System.out.println(amount);*/
        List<JDBCRow> rows = jdbcExecutor.query("select Item,Sum(Amount) as amount from FinItems group by Item");
        for(JDBCRow row : rows)
        {
            String item = row.getString("Item");
            Double amount = row.getDouble("amount");
            System.out.println(item+"="+amount);
        }
    }
}
