import com.yzk18.commons.IOHelpers;
import com.yzk18.docs.PDFHelpers;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.File;
import java.io.IOException;

public class PDF文件合并 {
    public static void main(String[] args){
        PDDocument newPdDoc = new PDDocument();//新文件的文档
        String[] pdfFiles = IOHelpers.getFilesRecursively("F:\\网友提供的阅读\\已经使用\\广东自考学位真题","pdf");
        for(String pdfFile : pdfFiles)
        {
            PDDocument pdDoc = PDFHelpers.openFile(pdfFile);
            for(int i=0;i<pdDoc.getNumberOfPages();i++)
            {
                PDPage page = pdDoc.getPage(i);
                pdDoc.removePage(page);
                newPdDoc.addPage(page);
            }
            //PDFHelpers.close(pdDoc);
        }
        try {
            newPdDoc.save("F:\\网友提供的阅读\\已经使用\\广东自考学位真题\\合并.pdf");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
