import com.yzk18.commons.IOHelpers;
import com.yzk18.net.HttpSender;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class NiPicTest1 {
    public static void main(String[] args) throws IOException {
        Document indexDoc = Jsoup.connect("https://www.nipic.com/photo/renwu/richang/index.html").get();
        String sTotalPage = indexDoc.getElementsByClass("totalPage").get(0).text();
        int totalPage = Integer.parseInt(sTotalPage);
        //System.out.println(totalPage);
        for(int pageIndex=1;pageIndex<=totalPage;pageIndex++)
        {
            System.out.println("第"+pageIndex+"页");
            String pageUrl = "https://www.nipic.com/photo/renwu/richang/index.html?page="+pageIndex;
            Document docPage = Jsoup.connect(pageUrl).get();
            Elements imgs = docPage.getElementsByClass("new-search-result-box").get(0).getElementsByTag("img");
            for(Element img : imgs)
            {
                //经分析发现，图片的真实路径在data-src属性下
                String imgSrc = "https:"+img.attr("data-src");
                System.out.println(imgSrc);
                String fileName = getFileName(imgSrc);
                byte[] bytes = new HttpSender().sendGetBytes(imgSrc);//图片文件是byte[]格式。获取到指定路径内容的byte[]内容
                IOHelpers.writeAllBytes("E:\\images\\"+fileName,bytes);//写入磁盘
            }
        }
    }

    //取路径的最后一部分作为文件名
    ////pic2.ntimg.cn/pic/20210714/23777965_235819157108_4.jpg ---> 23777965_235819157108_4.jpg
    static String getFileName(String url)
    {
        String[] strs = url.split("/");
        return strs[strs.length-1];
    }
}
