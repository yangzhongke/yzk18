import com.yzk18.commons.CommonHelpers;
import com.yzk18.commons.DesktopHelpers;
import com.yzk18.docs.ChartFromCellRangeBuilder;
import com.yzk18.docs.ExcelHelpers;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.LinkedList;
import java.util.List;

public class TestExcelChartCellRange1 {
    public static void main(String[] args) {
        List<DailyInfo> list = new LinkedList<>();
        list.add(new DailyInfo().setWeight(80.1).setBloodPressure(79));
        list.add(new DailyInfo().setWeight(79.5).setBloodPressure(78));
        list.add(new DailyInfo().setWeight(79.2).setBloodPressure(78));
        list.add(new DailyInfo().setWeight(79).setBloodPressure(79));
        list.add(new DailyInfo().setWeight(78.1).setBloodPressure(76));

        XSSFWorkbook wb = ExcelHelpers.createXLSX();
        XSSFSheet sheet = wb.createSheet("健康表");
        ExcelHelpers.setCellValue(sheet,0,0,"天数");
        ExcelHelpers.setCellValue(sheet,0,1,"体重");
        ExcelHelpers.setCellValue(sheet,0,2,"血压");
        for(int i=0;i<list.size();i++)
        {
            DailyInfo info = list.get(i);
            ExcelHelpers.setCellValue(sheet,i+1,0,i+1);
            ExcelHelpers.setCellValue(sheet,i+1,1,info.getBloodPressure());
            ExcelHelpers.setCellValue(sheet,i+1,2,info.getWeight());
        }
        XSSFChart chart = ExcelHelpers.createChart(sheet,0, 0, 0, 0, 0, 5, 7, 26);
        chart.setTitleText("杨中科健康变化");
        ChartFromCellRangeBuilder chartBuilder = new ChartFromCellRangeBuilder(ChartTypes.LINE,sheet);
        chartBuilder.setCategoriesCellRange(new CellRangeAddress(1,list.size(),0,0));
        chartBuilder.putCellRanges("体重",new CellRangeAddress(1,list.size(),1,1));
        chartBuilder.putCellRanges("血压",new CellRangeAddress(1,list.size(),2,2));
        chartBuilder.setCategoryAxisTitle("天数");
        chartBuilder.build(chart);

        ExcelHelpers.saveToFile(wb, "d:/temp/1.xlsx");
        CommonHelpers.close(wb);
        DesktopHelpers.openFile("d:/temp/1.xlsx");


    }
}
