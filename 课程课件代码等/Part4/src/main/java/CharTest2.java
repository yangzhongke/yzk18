import com.yzk18.commons.DesktopHelpers;
import com.yzk18.docs.ChartFromArrayBuilder;
import com.yzk18.docs.ExcelHelpers;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CharTest2 {
    public static void main(String[] args) {
        XSSFWorkbook wb = ExcelHelpers.createXLSX();
        XSSFSheet sheet = wb.createSheet();
        XSSFChart chart = ExcelHelpers.createChart(sheet, 0, 0, 10, 20);
        chart.setTitleText("成绩分布情况");
        //chart.getOrAddLegend().setPosition(LegendPosition.RIGHT);

        ChartFromArrayBuilder<Double> chartBuilder
                = new ChartFromArrayBuilder<>(ChartTypes.PIE);
        String[] names = new String[]{"优秀","良","合格","不及格"};
        Double[] sales1 = new Double[]{3.0,5.0,9.0,2.43};
        chartBuilder.setCategoryNames(names);
        chartBuilder.putValues("张三",sales1);
        chartBuilder.build(chart);

        ExcelHelpers.saveToFile(wb,"d:/1.xlsx");
        ExcelHelpers.close(wb);
        DesktopHelpers.openFile("d:/1.xlsx");
    }
}
