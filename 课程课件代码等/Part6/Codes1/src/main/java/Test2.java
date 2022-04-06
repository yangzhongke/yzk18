import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Test2 {
    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("https://www.youzack.com/ListeningExercise/AlbumIndex/92/").get();
        Element div = doc.getElementsByClass("weui-cells").get(0);

        Elements links = div.getElementsByTag("a");//缩小范围在class="weui-cells"的div内部找所有的a标签

        for(Element link : links)
        {
            String text = link.text();
            String href=link.attr("href");
            System.out.println(text+":"+href);
        }
    }
}
