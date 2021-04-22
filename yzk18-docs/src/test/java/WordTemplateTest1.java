import com.yzk18.commons.DesktopHelpers;
import com.yzk18.commons.IOHelpers;
import com.yzk18.docs.WordTemplateRenderer;

import java.util.LinkedHashMap;
import java.util.Map;

public class WordTemplateTest1 {
    public static void main(String[] args) {
        String templateFile = "e:/temp/通知书模板.docx";
        String outFile = "e:/temp/渲染完成.docx";
        Map<String,Object> data =new LinkedHashMap<>();
        data.put("${姓名}","杨中科");
        data.put("${专业名}","软件工程");
        data.put("${学费}",8000);
        data.put("${书本费}",800.3);
        data.put("${总计}",8800.3);
        data.put("${照片}", IOHelpers.readAllBytes("e:/temp/small.jpg"));
        WordTemplateRenderer.render(templateFile,data,outFile);
        DesktopHelpers.openFile(outFile);
    }
}
