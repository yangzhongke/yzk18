import com.yzk18.commons.DesktopHelpers;
import com.yzk18.docs.ChartFromArrayBuilder;
import com.yzk18.docs.ExcelHelpers;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.LegendPosition;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ChartTest1 {
    public static void main(String[] args) {
        XSSFWorkbook wb = ExcelHelpers.createXLSX();
        XSSFSheet sheet = wb.createSheet();
        XSSFChart chart = ExcelHelpers.createChart(sheet, 0, 0, 10, 20);
        chart.setTitleText("中科集团销售月报表");
        //chart.getOrAddLegend().setPosition(LegendPosition.RIGHT);

        ChartFromArrayBuilder<Double>  chartBuilder
                = new ChartFromArrayBuilder<>(ChartTypes.LINE);
        String[] names = new String[]{"一月份","二月份","三月份","四月份"};
        Double[] sales1 = new Double[]{3.0,5.0,9.0,2.43};
        Double[] sales2 = new Double[]{5.5,7.2,3.0,9.3};
        chartBuilder.setCategoryNames(names);
        chartBuilder.putValues("张三",sales1);
        chartBuilder.putValues("李四",sales2);
        chartBuilder.setCategoryAxisTitle("月份");
        chartBuilder.setValueAxisTitle("销售额");
        chartBuilder.build(chart);
        //chartBuilder.putValues()

        ExcelHelpers.saveToFile(wb,"d:/1.xlsx");
        ExcelHelpers.close(wb);
        DesktopHelpers.openFile("d:/1.xlsx");
    }

}
