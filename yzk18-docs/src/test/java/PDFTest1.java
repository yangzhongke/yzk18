import com.yzk18.commons.IOHelpers;
import com.yzk18.docs.PDFHelpers;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import java.util.List;
import java.util.UUID;

public class PDFTest1 {
    public static void main(String[] args) {
        PDDocument document = PDFHelpers.openFile("E:\\ebooks\\study less,learn more 中文.pdf");
        PDPageTree pages = document.getPages();
        int i=0;
        for(PDPage page : pages)
        {
            String s = PDFHelpers.parseText(page);
            System.out.println((i++)+"********************");
            System.out.println(s);
            List<byte[]> images = PDFHelpers.parseImages(page,"png");
            for (byte[] bytes : images)
            {
                String file = "e:/temp/a/"+i+"/"+ UUID.randomUUID().toString()+".png";
                IOHelpers.writeAllBytes(file,bytes);
            }
        }
        PDFHelpers.close(document);
    }
}
