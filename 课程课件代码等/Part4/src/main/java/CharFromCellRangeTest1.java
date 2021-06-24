import com.yzk18.commons.DesktopHelpers;
import com.yzk18.docs.ChartFromCellRangeBuilder;
import com.yzk18.docs.ExcelHelpers;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class CharFromCellRangeTest1 {
    public static void main(String[] args) {
        String filename = "d:/2.xlsx";
        Workbook wb = ExcelHelpers.openFile(filename);
        Sheet sheet = wb.getSheetAt(0);
        //读取有效数据条数
        int count = sheet.getLastRowNum();
        XSSFChart chart = ExcelHelpers.createChart(sheet, 4, 0, 10, 18);
        ChartFromCellRangeBuilder chartBuilder
                = new ChartFromCellRangeBuilder(ChartTypes.LINE,(XSSFSheet)sheet);
        chartBuilder.setCategoriesCellRange(new CellRangeAddress(1,count,0,0));
        chartBuilder.putCellRanges("血压",new CellRangeAddress(1,count,1,1));
        chartBuilder.putCellRanges("体重",new CellRangeAddress(1,count,2,2));
        chartBuilder.build(chart);
        ExcelHelpers.saveToFile(wb,filename);
        ExcelHelpers.close(wb);
        DesktopHelpers.openFile(filename);
    }
}
