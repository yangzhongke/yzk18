import com.yzk18.GUI.GUI;
import com.yzk18.commons.DesktopHelpers;
import com.yzk18.commons.IOHelpers;
import com.yzk18.docs.ExcelHelpers;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

public class Test1 {
    public static void main(String[] args) throws InterruptedException {

        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe");
        ChromeDriver driver = new ChromeDriver();

        //等待登录
        driver.get("https://aiqicha.baidu.com/");
        GUI.msgBox("登录完成后，点击【确定】键继续");
        WebDriverWait driverWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        String[] companyNames = IOHelpers.readAllLines("E:\\主同步盘\\我的坚果云\\UoZ\\SE101-玩着学编程\\Part7课件\\公司名单.txt");
        LinkedList<CompanyInfo> list = new LinkedList<>();
        for(String companyName : companyNames)
        {
            //System.out.println(companyName);
            driver.get("https://aiqicha.baidu.com/s?q="+companyName);
            checkVCode(driver);
            driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("company-list")));
            List<WebElement> cards =  driver.findElement(By.className("company-list")).findElements(By.className("card"));
            WebElement card = cards.get(0);
            String href = card.findElement(By.tagName("a")).getAttribute("href");
            driver.get(href);
            checkVCode(driver);

            driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("detail-main")));

            CompanyInfo companyInfo = new CompanyInfo();
            companyInfo.setName(companyName);

            WebElement addr = driver.findElement(By.xpath("//span[text()='地址：']/following-sibling::*[1]"));
            companyInfo.setAddress(addr.getText());

            WebElement email = driver.findElement(By.xpath("//span[text()='邮箱：']/following-sibling::*[1]"));
            companyInfo.setEmail(email.getText());

            companyInfo.setId(driver.findElement(By.className("social-credit-code-text")).getText());

            WebElement legalRep = tryFindElement(driver,By.xpath("//td[text()='法定代表人']/following-sibling::*[1]"));
            if(legalRep!=null)
            {
                companyInfo.setLegalRep(legalRep.findElement(By.tagName("a")).getText());
            }

            WebElement paidinCapital = driver.findElement(By.className("table-paidinCapital-lable"));
            companyInfo.setPaidInCapital(paidinCapital.getText());

            WebElement regCapital =  driver.findElement(By.xpath("//td[text()='注册资本']/following-sibling::*[1]"));
            companyInfo.setRegisteredCapital(regCapital.getText());

            WebElement phoneNum = driver.findElement(By.xpath("//span[text()='电话：']/following-sibling::*[1]"));
            companyInfo.setPhoneNum(phoneNum.getText());

            WebElement regDate = driver.findElement(By.xpath("//td[text()='成立日期']/following-sibling::td[1]"));
            companyInfo.setRegDate(regDate.getText());

            var webSite = driver.findElements(By.className("website"));
            if(webSite.size()>0)
            {
                companyInfo.setWebSite(webSite.get(0).getText());
            }

            list.add(companyInfo);
        }
        driver.close();
        driver.quit();

        var workbook = ExcelHelpers.createXLSX();
        var sheet = workbook.createSheet();
        ExcelHelpers.setCellValue(sheet,0,0,"名称");
        ExcelHelpers.setCellValue(sheet,0,1,"电话");
        ExcelHelpers.setCellValue(sheet,0,2,"邮箱");
        ExcelHelpers.setCellValue(sheet,0,3,"网站");
        ExcelHelpers.setCellValue(sheet,0,4,"地址");
        ExcelHelpers.setCellValue(sheet,0,5,"信用代码");
        ExcelHelpers.setCellValue(sheet,0,6,"法定代表人");
        ExcelHelpers.setCellValue(sheet,0,7,"注册时间");
        ExcelHelpers.setCellValue(sheet,0,8,"注册资本");
        ExcelHelpers.setCellValue(sheet,0,9,"实缴资本");
        for(int i=0;i<list.size();i++)
        {
            var companyInfo = list.get(i);
            ExcelHelpers.setCellValue(sheet,i+1,0,companyInfo.getName());
            ExcelHelpers.setCellValue(sheet,i+1,1,companyInfo.getPhoneNum());
            ExcelHelpers.setCellValue(sheet,i+1,2,companyInfo.getEmail());
            ExcelHelpers.setCellValue(sheet,i+1,3,companyInfo.getWebSite());
            ExcelHelpers.setCellValue(sheet,i+1,4,companyInfo.getAddress());
            ExcelHelpers.setCellValue(sheet,i+1,5,companyInfo.getId());
            ExcelHelpers.setCellValue(sheet,i+1,6,companyInfo.getLegalRep());
            ExcelHelpers.setCellValue(sheet,i+1,7,companyInfo.getRegDate());
            ExcelHelpers.setCellValue(sheet,i+1,8,companyInfo.getRegisteredCapital());
            ExcelHelpers.setCellValue(sheet,i+1,9,companyInfo.getPaidInCapital());
        }

        ExcelHelpers.saveToFile(workbook,"e:/1.xlsx");
        DesktopHelpers.openFile("e:/1.xlsx");
    }

    static WebElement tryFindElement(ChromeDriver driver,By by)
    {
        var elements = driver.findElements(by);
        if(elements.size()<=0)
        {
            return null;
        }
        else
        {
            return elements.get(0);
        }
    }

    static void checkVCode(ChromeDriver driver)
    {
        WebDriverWait driverWait = new WebDriverWait(driver, Duration.ofSeconds(1));
        try
        {
            driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("mod-vcodes")));
        }
        catch (TimeoutException ex)
        {}
        if(driver.findElements(By.className("mod-vcodes")).size()>0)
        {
            GUI.msgBox("需要通过验证，完成后点【确定】");
        }
    }
}
