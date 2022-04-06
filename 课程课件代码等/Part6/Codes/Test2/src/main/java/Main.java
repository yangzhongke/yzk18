import com.yzk18.commons.CommonHelpers;
import com.yzk18.commons.IOHelpers;
import com.yzk18.commons.ImageHelpers;
import com.yzk18.commons.ImageType;
import com.yzk18.net.HttpSender;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
/*
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe");
        ChromeDriver driver = new ChromeDriver();
        String url="https://image.baidu.com/search/index?tn=baiduimage&ipn=r&ct=201326592&cl=2&lm=-1&st=-1&fm=result&fr=&sf=1&fmq=1648111493741_R&pv=&ic=&nc=1&z=&hd=&latest=&copyright=&se=1&showtab=0&fb=0&width=&height=&face=0&istype=2&dyTabStr=MCwzLDIsMSw0LDUsOCw3LDYsOQ%3D%3D&ie=utf-8&sid=&word=%E7%BE%8E%E5%A5%B3";
        driver.get(url);
        List<WebElement> imgs = driver.findElement(By.className("imglist")).findElements(By.tagName("img"));
        for(int i=0;i<imgs.size();i++)
        {
            WebElement img = imgs.get(i);
            String imgSrc = img.getAttribute("data-imgurl");
            byte[] bytes = new HttpSender().sendGetBytes(imgSrc);//yzk18-net
            ImageType imgType = ImageHelpers.detectImageType(bytes);//yzk18-commons
            IOHelpers.writeAllBytes("e:/temp/"+i+"."+imgType,bytes);
        }
        driver.close();
        driver.quit();
*/
        String[] words = new String[]{"China","print","information"};
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe");
        ChromeDriver driver = new ChromeDriver();
        driver.get("https://bdc2.youzack.com/Recitation/Home");
        for(String word : words)
        {
            WebElement searchInput = driver.findElement(By.id("searchInput"));
            searchInput.clear();
            searchInput.sendKeys(word);
            searchInput.sendKeys(Keys.ENTER);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("js_half_screen_dialog")));
            WebElement dialog = driver.findElement(By.id("js_half_screen_dialog"));
            String text = dialog.findElement(By.className("weui-half-screen-dialog__bd")).getText();
            System.out.println(text);
            System.out.println("-----------------");
            dialog.findElement(By.id("dialogClose")).click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("js_half_screen_dialog")));
        }
        driver.close();
        driver.quit();
    }
}
