import com.yzk18.GUI.GUI;
import com.yzk18.commons.DesktopHelpers;
import com.yzk18.commons.IOHelpers;
import com.yzk18.commons.JDBCExecutor;
import com.yzk18.docs.PDFHelpers;
import com.yzk18.docs.WordHelpers;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CiPinFenXi1 {
    public static void main(String[] args) throws IOException {
        String dir = "F:\\网友提供的阅读\\已经使用\\广东自考学位真题";
        String[] pdfFiles = IOHelpers.getFilesRecursively(dir,"pdf");
        LinkedHashMap<String,Integer> map = new LinkedHashMap<>();
        for (String pdfFile : pdfFiles)
        {
            PDDocument doc = PDFHelpers.openFile(pdfFile);
            String text = PDFHelpers.parseText(doc);//读取这个pdf所有的文本
            String[] words = text.toLowerCase().split("\\s|\\.|\\,|\\?|\\:|\\!|\\?|;|\\(|\\)");//分割出单词
            for(String word : words)
            {
                //if(isEnglishWord(word)==false)
                if(!isEnglishWord(word))//如果这个word不是英语单词，则跳过
                {
                    continue;//处理下一个单词
                }
                if(word.equals(""))
                {
                    continue;
                }
                //是否是全部由字母组成的
                Integer freq = map.get(word);//获得当前的词频
                if(freq==null)
                {
                    map.put(word,1);//之前没出现过这个单词，记录为1
                }
                else
                {
                    map.put(word,freq+1);//如果出现过则递增
                }
            }
            PDFHelpers.close(doc);
        }
        /*
        String outputString="";
        for(String word : map.keySet())//keySet()是map中所有key的集合
        {
            int freq = map.get(word);
            //System.out.println(word+"="+freq);
            outputString=outputString+word+" "+freq+"\r\n";
        }
        IOHelpers.writeAllText("d:/1.txt",outputString);*/

        HashMap<String,String> enChMap = new HashMap<>();//英汉对应的Map。加快查询效率
        /*
        String[] lines = IOHelpers.readAllLines("E:\\主同步盘\\我的坚果云\\UoZ\\SE101-玩着学编程\\Part7课件\\ecdict.csv");
        for(int i=1;i<lines.length-1;i++)
        {
            String line = lines[i];
            String[] segments = line.split(",");
            String word = segments[0];//英文
            String chinese = segments[3];//中文
            enChMap.put(word,chinese);
        }*/
        Reader in = new FileReader("E:\\主同步盘\\我的坚果云\\UoZ\\SE101-玩着学编程\\Part7课件\\ecdict.csv");
        Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
        for (CSVRecord record : records) {
            String word = record.get(0);
            String chinese = record.get(3);
            enChMap.put(word,chinese);
        }

        List<Map.Entry<String,Integer>> entries = map.entrySet().stream().sorted((o1, o2)->o2.getValue()-o1.getValue()).toList();
        /*
        String outputString="";
        for(var entry: entries)//遍历排序之后的键值对
        {
            String word =entry.getKey();
            int freq = entry.getValue();
            //System.out.println(word+"="+freq);
            outputString=outputString+word+" "+freq+"\r\n";

            String chinese = enChMap.get(word);//查单词的中文含义
            outputString=outputString+(chinese==null?"查无此词":chinese)+"\r\n";//大量字符串拼接最好用StringBuilder
            outputString=outputString+"\r\n************************\r\n";
        }
        IOHelpers.writeAllText("d:/1.txt",outputString);*/
        var wordDoc = WordHelpers.createDocxDocument();
/*
        for(var entry: entries)//遍历排序之后的键值对
        {
            String word =entry.getKey();
            int freq = entry.getValue();
            WordHelpers.createRun(wordDoc,word+" "+freq);
            String chinese = enChMap.get(word);//查单词的中文含义。
            WordHelpers.createRun(wordDoc,chinese==null?"查无此词":chinese);
            WordHelpers.createRun(wordDoc,"************************");
        }*/
        JDBCExecutor jdbcExecutor = new JDBCExecutor("jdbc:sqlite:E:\\主同步盘\\我的坚果云\\UoZ\\SE101-玩着学编程\\Part7课件\\ecDict.db",null,null);
        for(var entry: entries)//遍历排序之后的键值对
        {
            String word =entry.getKey();
            int freq = entry.getValue();
            WordHelpers.createRun(wordDoc,word+" "+freq);
            //String chinese = enChMap.get(word);//查单词的中文含义。
            //可以给Words表的Word加上“索引”提升查询效率
            var items = jdbcExecutor.query("select Definition from Words where Word=?",word);
            String chinese;
            if(items.size()<=0)
            {
                chinese="查无此词";
            }
            else
            {
                chinese = items.get(0).getString("Definition");
            }
            WordHelpers.createRun(wordDoc,chinese==null?"查无此词":chinese);
            WordHelpers.createRun(wordDoc,"************************");
        }

        String wordFilename = GUI.fileSaveBox("请选择导出的文件","docx");
        WordHelpers.saveToFile(wordDoc,wordFilename);
        DesktopHelpers.openFile(wordFilename);
    }

    public static boolean isEnglishWord(String s)
    {
        for(int i=0;i<s.length();i++)
        {
            char ch = s.charAt(i);
            //Character.isAlphabetic()
            //if(Character.isLetter(ch)==false)//只要碰到一个非字母，函数就返回false
            if(Character.isLowerCase(ch)==false)//只要碰到一个非字母，函数就返回false
            {
                return false;
            }
        }
        return true;//如果运行到这里，就说明每个都是字母
    }
}
