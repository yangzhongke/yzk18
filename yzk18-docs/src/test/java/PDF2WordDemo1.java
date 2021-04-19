import com.yzk18.commons.DesktopHelpers;
import com.yzk18.docs.PDFHelpers;
import com.yzk18.docs.WordHelpers;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import java.util.List;

public class PDF2WordDemo1 {
    public static void main(String[] args) {
        XWPFDocument wordDoc = WordHelpers.createDocxDocument();
        PDDocument document = PDFHelpers.openFile("E:\\ebooks\\study less,learn more 中文.pdf");
        PDPageTree pages = document.getPages();
        for(PDPage page : pages)
        {
            String s = PDFHelpers.parseText(page);
            XWPFParagraph p = wordDoc.createParagraph();
            p.createRun().setText(s);
            List<byte[]> images = PDFHelpers.parseImages(page,"png");
            for (byte[] imgBytes : images)
            {
                WordHelpers.addPicture(wordDoc,imgBytes);
            }
        }
        WordHelpers.saveToFile(wordDoc,"e:/temp/a.docx");
        PDFHelpers.close(document);
        WordHelpers.close(wordDoc);
        DesktopHelpers.openFile("e:/temp/a.docx");
    }
}
