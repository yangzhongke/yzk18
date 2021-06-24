import com.yzk18.commons.IOHelpers;
import com.yzk18.docs.ChartFromArrayBuilder;
import com.yzk18.docs.ExcelHelpers;
import javafx.scene.chart.Chart;
import org.apache.poi.xddf.usermodel.chart.BarDirection;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.XDDFBarChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFChartData;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

public class 读取疫情数据 {
    public static void main(String[] args) {
        String[] lines = IOHelpers.readAllLines("d:/疫情.txt");
        String[] countries = new String[lines.length];
        Integer[] 累计s = new Integer[lines.length];
        Integer[] 治愈s = new Integer[lines.length];
        Integer[] 死亡s = new Integer[lines.length];
        //for(String line : lines)
        for(int i=0;i< lines.length;i++)
        {
            String line = lines[i];
            String[] fields = line.split("\\s");
            String 国家名 = fields[0];
            int 累计 = Integer.parseInt(fields[2]);
            int 治愈 = Integer.parseInt(fields[3]);
            int 死亡 = Integer.parseInt(fields[4]);
            countries[i] = 国家名;
            累计s[i] = 累计;
            治愈s[i] = 治愈;
            死亡s[i] = 死亡;
            //System.out.println(国家名+","+累计+","+治愈+","+死亡);
        }

        XSSFWorkbook wb = ExcelHelpers.createXLSX();
        XSSFSheet sheet = wb.createSheet();
        XSSFChart chart = ExcelHelpers.createChart(sheet,0,0,10,20);
        ChartFromArrayBuilder<Integer> chartBuilder = new ChartFromArrayBuilder<>(ChartTypes.BAR);
        chartBuilder.setCategoryNames(countries);
        chartBuilder.putValues("累计",累计s);
        chartBuilder.putValues("治愈",治愈s);
        chartBuilder.putValues("死亡",死亡s);
        chartBuilder.setConfigurator((data,cc)->{
            XDDFBarChartData barData = (XDDFBarChartData)data;
            barData.setBarDirection(BarDirection.BAR);
            barData.setGapWidth(20);
        });
        chartBuilder.build(chart);
        //List<XDDFChartData> sss = chart.getChartSeries();
        ExcelHelpers.saveToFile(wb,"d:/疫情.xlsx");
        ExcelHelpers.close(wb);
    }
}
