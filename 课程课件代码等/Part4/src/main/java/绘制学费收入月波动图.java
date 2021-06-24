import com.yzk18.docs.ChartFromArrayBuilder;
import com.yzk18.docs.ExcelHelpers;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class 绘制学费收入月波动图 {
    public static void main(String[] args) {
        Workbook wb = ExcelHelpers.openFile("d:/公司账目2021年.xlsx");
        int sheetCount = wb.getNumberOfSheets();
        String[] monthNames = new String[sheetCount];
        Double[] sales = new Double[sheetCount];
        for(int sheetIndex = 0;sheetIndex<sheetCount;sheetIndex++)
        {
            Sheet sheet = wb.getSheetAt(sheetIndex);
            String month = sheet.getSheetName();
            monthNames[sheetIndex]=month;
            double 学费总额=0;
            for(int rowIndex =1;rowIndex<=sheet.getLastRowNum();rowIndex++)
            {
                String 收支类别 = ExcelHelpers.getCellStringValue(sheet,rowIndex,2);
                //System.out.println(收支类别);
                if(收支类别.equals("学费收入"))
                {
                    double 金额= ExcelHelpers.getCellDoubleValue(sheet,rowIndex,3);
                    学费总额+=金额;
                }
            }
            sales[sheetIndex] = 学费总额;
            System.out.println(month+":"+学费总额);
        }

        XSSFWorkbook wbChart = ExcelHelpers.createXLSX();
        XSSFSheet sheet = wbChart.createSheet();
        XSSFChart chart = ExcelHelpers.createChart(sheet, 0, 0, 10, 20);
        ChartFromArrayBuilder<Double> chartBuilder = new ChartFromArrayBuilder<>(ChartTypes.LINE);
        chartBuilder.setCategoryNames(monthNames);
        chartBuilder.putValues("学费收入",sales);
        chartBuilder.build(chart);

        ExcelHelpers.saveToFile(wbChart,"d:/学费波动.xlsx");
        ExcelHelpers.close(wbChart);

        ExcelHelpers.close(wb);
    }
}
