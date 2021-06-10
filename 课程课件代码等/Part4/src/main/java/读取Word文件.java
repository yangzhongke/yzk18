import com.yzk18.commons.IOHelpers;
import com.yzk18.docs.WordHelpers;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;

public class 读取Word文件 {
    public static void main(String[] args) {
        XWPFDocument doc = WordHelpers.openDocx("e:/2006-2020考研真题合并.docx");
        String s = WordHelpers.readAllText("e:/2006-2020考研真题合并.docx");
        System.out.println(s);
        for(XWPFPictureData pic : doc.getAllPictures())
        {
            IOHelpers.writeAllBytes("e:/pics/"+pic.getFileName(),pic.getData());
        }
        WordHelpers.close(doc);
    }
}
