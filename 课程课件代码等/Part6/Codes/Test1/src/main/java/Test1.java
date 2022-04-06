import com.yzk18.commons.IOHelpers;
import com.yzk18.net.HttpSender;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Test1 {
    public static void main(String[] args) throws IOException {
       // Document doc = Jsoup.connect("https://www.youzack.com/").get();
        /*
        Element tucao = doc.getElementById("tucao");
        String href = tucao.attr("href");
        String text = tucao.text();
        System.out.println(href+":"+text);*/
        /*
        Elements links = doc.getElementsByTag("a");
        for(Element link: links)
        {
            String href = link.attr("href");
            String text = link.text();
            System.out.println(href+":"+text);
        }*/
        /*
        Document doc = Jsoup.connect("https://www.youzack.com/ListeningExercise/AlbumIndex/10/").get();
        var weui_cells  = doc.getElementsByClass("weui-cells").get(0);
        Elements links = weui_cells.getElementsByTag("a");
        for(Element link : links)
        {
            String text = link.text();
            System.out.println(text);
        }*/

        //下载博客园文章
        /*
        Document doc = Jsoup.connect("https://www.cnblogs.com/").get();
        Elements articles = doc.getElementById("post_list").getElementsByTag("article");
        for(Element article: articles)
        {
            Element link = article.getElementsByClass("post-item-title").get(0);
            String href = link.attr("href");
            String text = link.text();
            String mainContent = Jsoup.connect(href).get().getElementById("mainContent").text();
            String filename = text.replace("*","").replace("?","").replace("|","").replace("/","").replace("\\","");
            IOHelpers.writeAllText("e:/temp/"+filename+".txt",mainContent);
        }*/

        //下载昵图网
        /*
        Document doc = Jsoup.connect("https://www.nipic.com/photo/renwu/nvxing/index.html").get();
        int totalPage = Integer.parseInt(doc.getElementsByClass("totalPage").get(0).text());
        System.out.println(totalPage);
        for(int i=1;i<=totalPage;i++)
        {
            Document docPage = Jsoup.connect("https://www.nipic.com/photo/renwu/nvxing/index.html?page="+i).get();
            Element resultbox = docPage.getElementsByClass("new-search-result-box").get(0);
            Elements imgs = resultbox.getElementsByTag("img");

            for(Element img : imgs)
            {
                String imgSrc = "https:"+img.attr("data-src");
                String fileName = getFileNameFromUrl(imgSrc);
                byte[] bytes = new HttpSender().sendGetBytes(imgSrc);
                IOHelpers.writeAllBytes("e:/tmep/"+fileName,bytes);
            }
        }*/
    }

    static String getFileNameFromUrl(String url)
    {
        String[] strs = url.split("/");
        return strs[strs.length-1];
    }
}
