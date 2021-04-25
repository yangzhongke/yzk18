import com.yzk18.net.HttpHeaders;
import com.yzk18.net.HttpSender;

public class HttpTest1 {
    public static void main(String[] args) {
        HttpSender httpSender = new HttpSender();
        HttpHeaders headers = new HttpHeaders();
        headers.setReferer("https://www.aaaa.com");
        headers.setUserAgent("fadsfadfad");
        headers.addCookie("XXX","55你好呀");

        String s1 = httpSender.sendGetString("https://www.baidu.com");
        //String s1 = httpSender.sendGetString("https://localhost:44319/",headers);
        System.out.println(s1);
    }
}
