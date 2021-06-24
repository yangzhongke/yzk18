import com.yzk18.docs.PDFHelpers;
import com.yzk18.docs.WordHelpers;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.poi.xwpf.usermodel.*;

import java.util.List;

public class PDF2Word1 {
    public static void main(String[] args) {
        PDDocument pdfDoc = PDFHelpers.openFile("E:\\ebooks\\计算机视觉\\Mastering_OpenCV.pdf");
        XWPFDocument docx = WordHelpers.createDocxDocument();
        int pages = pdfDoc.getNumberOfPages();

        /*
        XWPFTable table = docx.createTable();
        XWPFTableRow row = table.createRow();
        XWPFTableCell cell = row.createCell();
        cell.setText("你好");*/
        //cell.setVerticalAlignment();

        //for(int i=0;i<pdfDoc.getNumberOfPages();i++)
        for(int i=0;i<pages;i++)
        {
            PDPage page = pdfDoc.getPage(i);
            String pageText = PDFHelpers.parseText(page);
            List<byte[]> images = PDFHelpers.parseImages(page,"png");
            XWPFRun run = WordHelpers.createRun(docx, pageText);//这样就能插入到word里吗？
            run.setFontSize(30);
            run.setItalic(true);
            for(byte[] imgBytes : images)
            {
                WordHelpers.addPicture(docx,imgBytes);
            }
        }
        WordHelpers.saveToFile(docx,"E:\\ebooks\\计算机视觉\\Mastering_OpenCV.docx");
        WordHelpers.close(docx);
        PDFHelpers.close(pdfDoc);
    }
}
