import com.yzk18.commons.IOHelpers;
import com.yzk18.docs.PDFHelpers;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.util.List;

public class PDFTEst22 {
    public static void main(String[] args) {
        PDDocument doc = PDFHelpers.openFile("E:\\temp\\重学Java设计模式.pdf");
        String text = PDFHelpers.parseText(doc);
        IOHelpers.writeAllText("E:\\temp\\重学Java设计模式.txt",text);
        int imgNumber=0;
        for(int i=0;i<doc.getNumberOfPages();i++)
        {
            PDPage page = doc.getPage(i);
            List<byte[]> images = PDFHelpers.parseImages(page,"png");
            for (byte[] bytes : images)
            {
                IOHelpers.writeAllBytes("E:\\temp\\图片\\"+imgNumber+".png",bytes);
                imgNumber++;
            }
        }
        PDFHelpers.close(doc);
    }
}
