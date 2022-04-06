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

public class CompanyTest1 {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver","C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe");
        ChromeDriver driver = new ChromeDriver();

        WebDriverWait driverWait = new WebDriverWait(driver,Duration.ofSeconds(2));

        driver.get("https://aiqicha.baidu.com/");
       //driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("login")));
        GUI.msgBox("请完成登录，然后点击【确定】继续");

        LinkedList<CompanyInfo> list = new LinkedList<>();
        String[] companyNames = IOHelpers.readAllLines("E:\\主同步盘\\我的坚果云\\UoZ\\SE101-玩着学编程\\Part7课件\\公司名单.txt");
        for(int i=0;i<companyNames.length;i++)
        {
            String companyName = companyNames[i];
            System.out.println(companyName);

            driver.get("https://aiqicha.baidu.com/");
            checkVCode(driver);
            //搜索公司
            driver.findElement(By.id("aqc-search-input")).sendKeys(companyName);
            driver.findElement(By.className("search-btn")).click();

            checkVCode(driver);

            //找到列表中的第一个公司
            WebElement companyCard = driver.findElement(By.className("card"));
            //找到公司链接的<a>，然后点击
            //companyCard.findElement(By.tagName("a")).click();
            String companyLink = companyCard.findElement(By.tagName("a")).getAttribute("href");
            driver.get(companyLink);
            checkVCode(driver);

            CompanyInfo company = new CompanyInfo();
            company.setName(companyName);

            WebElement tel = driver.findElement(By.xpath("//span[text()='电话：']/following-sibling::*[1]"));
            company.setPhoneNum(tel.getText());
            WebElement email = driver.findElement(By.xpath("//span[text()='邮箱：']/following-sibling::*[1]"));
            company.setEmail(email.getText());
            WebElement website = driver.findElement(By.xpath("//span[text()='网址：']/following-sibling::*[1]"));
            company.setWebSite(website.getText());

            WebElement addr = driver.findElement(By.xpath("//span[text()='地址：']/following-sibling::*[1]"));
            company.setAddress(addr.getText());

            company.setId(driver.findElement(By.className("social-credit-code-text")).getText());

            WebElement legalRep = tryFindElement(driver,By.xpath("//td[text()='法定代表人']/following-sibling::*[1]"));
            if(legalRep!=null)
            {
                company.setLegalRep(legalRep.findElement(By.tagName("a")).getText());
            }

            WebElement paidinCapital = driver.findElement(By.className("table-paidinCapital-lable"));
            company.setPaidInCapital(paidinCapital.getText());

            WebElement regCapital =  driver.findElement(By.xpath("//td[text()='注册资本']/following-sibling::*[1]"));
            company.setRegisteredCapital(regCapital.getText());

            WebElement regDate = driver.findElement(By.xpath("//td[text()='成立日期']/following-sibling::td[1]"));
            company.setRegDate(regDate.getText());

            var webSite = tryFindElement(driver,By.className("website"));
            if(webSite!=null)
            {
                company.setWebSite(webSite.getText());
            }
            System.out.println(company);
            list.add(company);
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

    static void checkVCode(ChromeDriver driver)
    {
        WebDriverWait driverWait = new WebDriverWait(driver,Duration.ofSeconds(1));
        try {
            driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("mod-vcodes")));
            //说明弹出验证码的框了
            //GUI.msgBox("请通过验证码，然后继续");
        }
        catch (TimeoutException ex)
        {

        }

        if(driver.findElements(By.className("mod-vcodes")).size()>0)
        {
            GUI.msgBox("需要通过验证，完成后点【确定】");
        }
    }

    static WebElement tryFindElement(ChromeDriver driver,By by)
    {
        List<WebElement> elements = driver.findElements(by);
        if(elements.size()>=1)
        {
            return elements.get(0);
        }
        else
        {
            return null;
        }
    }
}
