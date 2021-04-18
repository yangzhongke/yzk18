import com.yzk18.commons.DesktopHelpers;
import com.yzk18.commons.IOHelpers;
import com.yzk18.docs.WordChartBuilder;
import com.yzk18.docs.WordHelpers;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xwpf.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;

public class TestWord1 {


    public static void main(String[] args) {
        /*
        String s = WordHelpers.readAllText("F:\\网友提供的阅读\\零散资料\\高考阅读高频单词.doc");
        System.out.println(s);

        String s1 = WordHelpers.readAllText("E:\\主同步盘\\我的坚果云\\个人资料\\我的文章\\高能预警，ASP.Net MVC的一个大坑，不要跳进去.docx");
        System.out.println("********************");
        System.out.println(s1);*/
        /*
        XWPFDocument doc = WordHelpers.openDocx("E:\\免费视频\\C语言\\C语言也能干大事2019版\\《C语言也能干大事2019版》课件.docx");
        List<XWPFPictureData> pics = doc.getAllPictures();
        for(XWPFPictureData pic : pics)
        {
            IOHelpers.writeAllBytes("d:/temp/"+pic.getFileName(),pic.getData());
        }*/
        //https://www.jianshu.com/p/6603b1ea3ad1

        XWPFDocument doc = WordHelpers.createDocxDocument();

        XWPFRun runTitle = WordHelpers.createRun(doc,"总部通知",ParagraphAlignment.CENTER);
        runTitle.setFontSize(80);
        runTitle.setBold(true);

        WordHelpers.createRun(doc,"各部门：");
        WordHelpers.createRun(doc,"今天下午去吃饭。");

        XWPFParagraph pPlace = doc.createParagraph();
        XWPFRun runP1 = pPlace.createRun();
        runP1.setText("地点：");
        runP1.setColor("ff0000");
        runP1.setBold(true);

        XWPFRun runP2 = pPlace.createRun();
        runP2.setText("贵宾楼308");
        runP2.setColor("0000ff");
        runP2.setItalic(true);

        byte[] picData = IOHelpers.readAllBytes("E:\\temp\\寂寞才说爱.jpg");
        WordHelpers.addPicture(doc,picData,50,50);
        //WordHelpers.addPicture(doc,picData);

        WordHelpers.createRun(doc,"CEO：张三",ParagraphAlignment.RIGHT);


        XWPFTable table1 = doc.createTable();
        WordHelpers.setTableCellValue(table1,0,0,"姓名");
        WordHelpers.setTableCellValue(table1,0,1,"年龄");
        WordHelpers.setTableCellValue(table1,0,2,"生日");
        WordHelpers.setTableCellValue(table1,0,3,"照片");

        XWPFTableCell cellyzk = WordHelpers.setTableCellValue(table1,1,0,"杨中科");
        WordHelpers.forEachRun(cellyzk,r->{r.setBold(true); r.setColor("0000ff");});
        //cellyzk.getParagraphs().forEach(p->p.getRuns().forEach(r->{r.setBold(true); r.setColor("0000ff");}));
        WordHelpers.setTableCellValue(table1,1,1,"18");
        WordHelpers.setTableCellValue(table1,1,2, LocalDate.of(1981,6,6));
        WordHelpers.setTableCellValue(table1,1,3,IOHelpers.readAllBytes("E:\\temp\\small.jpg"));

        String[] monthNames = {"一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"};
        Double[] sales1 = {3.1,5.2,8.0,9.0,3.0,5.0,8.0,9.0,3.0,5.0,8.0,0.9};
        Double[] sales2={5.0,81.0,4.0,9.8,1.8,3.9,5.8,81.8,4.8,9.8,1.8,3.9};

        WordChartBuilder<Double> chartBuilder = new WordChartBuilder<>(ChartTypes.LINE);
        XWPFChart chart = chartBuilder
                .putValues("小王",sales1)
                .putValues("小李",sales2)
                .setCategoryAxisTitle("月份").setValueAxisTitle("销量")
               .setSize(500,300)
                .setCategoryNames(monthNames)
            .build(doc);
        chart.setTitleText("销售龙虎榜");
        chart.getOrAddLegend().setPosition(LegendPosition.LEFT);
        /*
        chart.setChartHeight(Units.toEMU(100));
        chart.setChartWidth(Units.toEMU(300));*/


        //todo: chart. doc.createChart().createData()
        WordHelpers.saveToFile(doc,"d:/temp/a/a.docX");
        DesktopHelpers.openFile("d:/temp/a/a.docx");
    }
}
