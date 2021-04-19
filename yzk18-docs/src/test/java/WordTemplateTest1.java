import com.yzk18.commons.CommonHelpers;
import com.yzk18.commons.DesktopHelpers;
import com.yzk18.docs.WordHelpers;
import com.yzk18.docs.WordTemplateRenderer;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.util.LinkedHashMap;
import java.util.Map;

public class WordTemplateTest1 {
    public static void main(String[] args) {
        XWPFDocument doc = WordHelpers.openDocx("e:/temp/通知书模板.docx");
        Map<String,String> data =new LinkedHashMap<>();
        data.put("${姓名}","杨中科");
        data.put("${专业名}","软件工程");
        data.put("${学费}","8000");
        data.put("${书本费}","800");
        data.put("${总计}","8800");
        WordTemplateRenderer.render(doc,data);
        WordHelpers.saveToFile(doc,"e:/temp/渲染完成.docx");
        DesktopHelpers.openFile("e:/temp/渲染完成.docx");
        CommonHelpers.closeQuietly(doc);
    }
}
