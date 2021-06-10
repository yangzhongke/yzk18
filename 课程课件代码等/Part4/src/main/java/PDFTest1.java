import com.yzk18.commons.IOHelpers;
import com.yzk18.docs.PDFHelpers;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.util.List;

public class PDFTest1 {
    public static void main(String[] args) {
        PDDocument doc = PDFHelpers.openFile("E:\\temp\\重学Java设计模式.pdf");
        System.out.println(doc.getNumberOfPages());
        PDPage page = doc.getPage(90);
        String s = PDFHelpers.parseText(page);
        System.out.println(s);
        List<byte[]> images = PDFHelpers.parseImages(page,"png");
        //for(byte[] bytes : images)
        for(int i=0;i<images.size();i++)
        {
            byte[] bytes = images.get(i);
            IOHelpers.writeAllBytes("E:\\temp\\"+i+".png",bytes);
        }
        PDFHelpers.close(doc);
    }
}
