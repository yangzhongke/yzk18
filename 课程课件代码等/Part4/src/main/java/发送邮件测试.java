import com.yzk18.net.MailSender;

import java.io.File;

public class 发送邮件测试 {
    public static void main(String[] args) {
        /*
        MailSender mailSender = new MailSender();
        mailSender.setHostName("smtp.126.com");
        mailSender.setUserName("yzktest@126.com");//yzktest
        mailSender.setPassword("LFNSLAXMDDRODJGK");
        mailSender.setFrom("yzktest@126.com");//发送者邮箱
        mailSender.addTo("yangzhongke8@gmail.com");
        mailSender.setSubject("明天下午咱们部门开个会");
        mailSender.setTextMessage("在5号会议室，中午管饭。");
        mailSender.send();*/
        MailSender mailSender = new MailSender();
        mailSender.setHostName("smtp.126.com");
        mailSender.setUserName("yzktest@126.com");//yzktest
        mailSender.setPassword("LFNSLAXMDDRODJGK");
        mailSender.setFrom("yzktest@126.com");//发送者邮箱
        mailSender.addTo("yangzhongke8@gmail.com");
        mailSender.setSubject("这是明天出差人员的注意事项");
        mailSender.setTextMessage("在附件中。");
        mailSender.addAttachment(new File("D:\\报表.docx"));
        mailSender.send();
    }
}
