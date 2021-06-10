import com.yzk18.commons.IOHelpers;
import com.yzk18.docs.PDFHelpers;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.FileNotFoundException;
import java.io.IOException;

public class PDF文件合并2 {
    public static void main(String[] args) throws IOException {
        PDFMergerUtility merger = new PDFMergerUtility();
        String[] pdfFiles = IOHelpers.getFilesRecursively("F:\\网友提供的阅读\\已经使用\\广东自考学位真题","pdf");
        //String[] pdfFiles = IOHelpers.getFilesRecursively("F:\\网友提供的阅读\\已经使用\\考研英语阅读1997-2015","pdf");
        for(String pdfFile : pdfFiles)
        {
            merger.addSource(pdfFile);
        }
        merger.setDestinationFileName("d:/1.pdf");
        merger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
    }
}
