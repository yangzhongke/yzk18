import com.yzk18.commons.IOHelpers;
import com.yzk18.net.HttpSender;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

public class SeleniumTest1 {
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver","C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe");
        ChromeDriver driver = new ChromeDriver();
        /*
        driver.get("https://image.baidu.com/");
        WebElement kw= driver.findElement(By.id("kw"));//findElement如果有多个，就返回第一个，如果一个都没有，就抛异常
        kw.sendKeys("美女");
        WebElement s_newBtn = driver.findElement(By.className("s_newBtn"));
        s_newBtn.click();
        */
        driver.get("https://image.baidu.com/search/index?tn=baiduimage&ipn=r&ct=201326592&cl=2&lm=-1&st=-1&sf=1&fmq=&pv=&ic=0&nc=1&z=&se=1&showtab=0&fb=0&width=&height=&face=0&istype=2&ie=utf-8&fm=index&pos=history&word=%E7%BE%8E%E5%A5%B3");
        List<WebElement> imgs =  driver.findElements(By.className("imgitem"));
        int i=1;//文件名计数器
        for(WebElement img : imgs)
        {
            String imgUrl = img.getAttribute("data-thumburl");
            System.out.println(imgUrl);
            String imgText = img.findElement(By.className("imgitem-title")).getText();
            imgText = imgText.replace("*","").replace("?","").replace("\\","").replace("/","");
            byte[] bytes = new HttpSender().sendGetBytes(imgUrl);
            IOHelpers.writeAllBytes("E:/meinv/"+imgText+i+".jpg",bytes);
            i++;
        }

        //Thread.sleep(5000);
        driver.close();
        driver.quit();
    }
}
