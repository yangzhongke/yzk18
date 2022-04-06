import com.yzk18.commons.IOHelpers;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class CnBlogsTest1 {
    public static void main(String[] args) throws IOException {
      Document doc = Jsoup.connect("https://www.cnblogs.com/").get();
      Elements links = doc.getElementsByClass("post-item-title");
      for(Element link : links)
      {
          String text = link.text();
          String href=link.attr("href");
          System.out.println(text+","+href);
          String artcileText = Jsoup.connect(href).get().getElementsByClass("post").get(0).text();
          //System.out.println(text);
         // System.out.println(artcileText);
          String fileName= text.replace("*","").replace("?","").replace("\\","").replace("/","");
          IOHelpers.writeAllText("e:/cnblogs/"+fileName+".txt",artcileText);
      }
    }
}
