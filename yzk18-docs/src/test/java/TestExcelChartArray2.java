import com.yzk18.commons.CommonHelpers;
import com.yzk18.commons.DesktopHelpers;
import com.yzk18.docs.ChartFromArrayBuilder;
import com.yzk18.docs.ChartFromCellRangeBuilder;
import com.yzk18.docs.ExcelHelpers;
import com.yzk18.docs.WordHelpers;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.LegendPosition;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFChart;

import java.util.LinkedList;
import java.util.List;

public class TestExcelChartArray2 {
    public static void main(String[] args) {
        XSSFWorkbook wb = ExcelHelpers.createXLSX();
        XSSFSheet sheet = wb.createSheet();

        String[] monthNames = {"一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"};
        Double[] sales1 = {3.1,5.2,8.0,9.0,3.0,5.0,8.0,9.0,3.0,5.0,8.0,0.9};
        Double[] sales2={5.0,81.0,4.0,9.8,1.8,3.9,5.8,81.8,4.8,9.8,1.8,3.9};

        ChartFromArrayBuilder<Double> chartBuilder = new ChartFromArrayBuilder<>(ChartTypes.LINE);
        //XSSFChart chart = ExcelHelpers.createChart(sheet,0, 0, 0, 0, 0, 5, 7, 26);
        XSSFChart chart = ExcelHelpers.createChart(sheet,0, 5, 7, 26);
        chart.setTitleText("销售龙虎榜");
        chart.getOrAddLegend().setPosition(LegendPosition.LEFT);
        chartBuilder.setCategoryNames(monthNames)
                .putValues("小王",sales1)
                .putValues("小李",sales2)
                .setCategoryAxisTitle("月份").setValueAxisTitle("销量")
                .build(chart);

        ExcelHelpers.saveToFile(wb, "d:/temp/1.xlsx");
        CommonHelpers.close(wb);
        DesktopHelpers.openFile("d:/temp/1.xlsx");


    }
}
