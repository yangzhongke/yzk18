import com.yzk18.commons.DesktopHelpers;
import com.yzk18.docs.ChartFromArrayBuilder;
import com.yzk18.docs.WordHelpers;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xwpf.usermodel.XWPFChart;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class Word图表1 {
    public static void main(String[] args) {
        String[] names = new String[]{"一月份","二月份","三月份","四月份"};
        Double[] sales1 = new Double[]{3.0,5.0,9.0,2.43};
        Double[] sales2 = new Double[]{5.5,7.2,3.0,9.3};
        XWPFDocument doc = WordHelpers.createDocxDocument();
        XWPFChart chart = WordHelpers.createChart(doc, 400, 300);
        chart.setTitleText("销售龙虎榜");
        ChartFromArrayBuilder<Double> chartBuilder = new ChartFromArrayBuilder<>(ChartTypes.LINE);
        chartBuilder.setCategoryNames(names);
        chartBuilder.putValues("张三",sales1);
        chartBuilder.putValues("李四",sales2);
        chartBuilder.setCategoryAxisTitle("月份");
        chartBuilder.setValueAxisTitle("销售额");
        chartBuilder.build(chart);

        WordHelpers.saveToFile(doc,"d:/wordchart.docx");
        DesktopHelpers.openFile("d:/wordchart.docx");
    }
}
