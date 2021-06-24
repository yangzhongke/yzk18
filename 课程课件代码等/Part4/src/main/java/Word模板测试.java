import com.yzk18.commons.IOHelpers;
import com.yzk18.docs.WordTemplateRenderer;

import java.util.HashMap;

public class Word模板测试 {
    public static void main(String[] args) {
        String tFile = "E:\\主同步盘\\我的坚果云\\UoZ\\SE101-玩着学编程\\Part4课件\\录取通知书模板.docx";
        String outputFile ="E:\\主同步盘\\我的坚果云\\UoZ\\SE101-玩着学编程\\Part4课件\\1.docx";
        HashMap<String,Object> data = new HashMap<>();
        data.put("{姓名}","王思葱");
        data.put("{专业名}","天狗学");
       // data.put("[这里是学生照片]","输液");
        byte[] bytes = IOHelpers.readAllBytes("E:\\主同步盘\\我的坚果云\\UoZ\\SE101-玩着学编程\\Part4课件\\yzk.png");
        data.put("[这里是学生照片]",bytes);
        WordTemplateRenderer.render(tFile,data,outputFile);
    }
}
