import com.yzk18.commons.CommonHelpers;
import com.yzk18.commons.IOHelpers;
import com.yzk18.docs.PDFHelpers;
import org.apache.pdfbox.pdmodel.PDDocument;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CiPinFenXiTest1 {
    public static void main(String[] args) {
        String[] files = IOHelpers.getFiles("E:\\ebooks\\kubernetes\\",true,"pdf");
        /*
        HashMap<String, Integer> data = new HashMap<>();
        for (String file : files)
        {
            PDDocument doc = PDFHelpers.openFile(file);
            String txt = PDFHelpers.parseText(doc);
            PDFHelpers.close(doc);
            String[] words = txt.split("\\s|\\,|\\.|\\?|\\!");
            for(String word : words)
            {
                if(data.containsKey(word))
                {
                    int c = data.get(word);
                    c++;
                    data.put(word,c);
                }
                else
                {
                    data.put(word,1);
                }
            }
        }
        CommonHelpers.println(data);*/
        List<String> list = new LinkedList<>();
        for (String file : files)
        {
            PDDocument doc = PDFHelpers.openFile(file);
            String txt = PDFHelpers.parseText(doc);
            PDFHelpers.close(doc);
            String[] words = txt.split("\\s|\\,|\\.|\\?|\\!");
            for (String word : words)
            {
                if(word==null||word.length()<=2)
                {
                    continue;
                }
                list.add(word.toLowerCase());
            }
        }
        Map<String, Long> result = list.stream().collect(Collectors.groupingBy(w->w, Collectors.counting()));
        List<Map.Entry<String,Long>> items = result.entrySet().stream()
                .sorted((e1,e2)->e2.getValue().compareTo(e1.getValue())).limit(20).collect(Collectors.toList());

        for(Map.Entry<String,Long> item : items)
        {
            CommonHelpers.println(item.getKey()+":"+item.getValue());
        }
    }
}
