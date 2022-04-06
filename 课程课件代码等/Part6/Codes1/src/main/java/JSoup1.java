import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class JSoup1 {
    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("https://www.youzack.com/").get();
        /*
        Element link = doc.getElementById("tucao");
        String href = link.attr("href");
        String txt = link.text();
        System.out.println(href);
        System.out.println(txt);


        Elements links = doc.getElementsByTag("a");
        for(Element a : links)
        {
            System.out.println(a.attr("href")+","+a.text());
        }*/
        /*
        Elements elements = doc.getElementsByTag("img");
        for(Element img : elements)
        {
            System.out.println(img.attr("src"));
        }*/
        Elements covers = doc.getElementsByClass("cover");
        for(Element c : covers)
        {
            System.out.println(c.text());
        }
    }
}
