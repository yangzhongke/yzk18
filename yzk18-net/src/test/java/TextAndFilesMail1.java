import com.yzk18.commons.IOHelpers;
import com.yzk18.net.MailSender;

import java.io.File;

public class TextAndFilesMail1 {
    public static void main(String[] args) {
        String pwd = IOHelpers.readAllText("E:\\temp\\163pwd.txt");
        MailSender sender = new MailSender();
        sender.setHostName("smtp.163.com")
                .setTextMessage("<a href='https://youzack.com'>hello</a><i>附件中是你的工资条，工资38800</i>")
                .setFrom("about521@163.com")
                .addTo("about521@163.com")
                //.addCC("yangzhongke8@gmail.com")
                .setHostName("smtp.163.com")
                .setUserName("about521@163.com")
                .setPassword(pwd)
                .setSubject("我的第二个请假条")
                .addAttachment("请假条.docx",IOHelpers.readAllBytes("E:\\temp\\渲染完成.docx"))
                .addAttachment(new File("E:\\temp\\small.jpg"))
                .send();
        System.out.println("done");
    }
}
