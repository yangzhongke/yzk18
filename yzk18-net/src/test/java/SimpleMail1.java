import com.yzk18.commons.IOHelpers;
import com.yzk18.net.MailSender;
import org.apache.commons.io.IOUtils;

public class SimpleMail1 {
    public static void main(String[] args) {
        String pwd = IOHelpers.readAllText("E:\\temp\\163pwd.txt");
        MailSender sender = new MailSender();
        sender.setHostName("smtp.163.com")
                .setHtmlMessage("<a href='https://youzack.com'>hello</a><i>附件中是你的工资条，工资38800</i>")
                .setFrom("about521@163.com")
                .addTo("about521@163.com")
                .addCC("yangzhongke8@gmail.com")
                .setHostName("smtp.163.com")
                .setUserName("about521@163.com")
                .setPassword(pwd)
                .setSubject("你好吗？这是你的工资条。")
                .send();

    }
}
