import com.yzk18.commons.IOHelpers;
import com.yzk18.docs.PDFHelpers;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.util.LinkedHashMap;

public class PDF词频统计 {
    public static void main(String[] args) {
        /*
        声明一个LinkedHashMap，Key是单词（忽略大小写），Value是词频
遍历每个pdf文件
{
   读取这个pdf的所有文本
   对于文本按照“空格 逗号 句号等分割为单词”。全部单词转为小写。
   遍历每个单词，看在map中是否存在，存在则词频+1，不存在则put增加新的单词
}
         */
        //System.out.println(isEnglishWord("中暑"));

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
        String outputString="";
        for(String word : map.keySet())//keySet()是map中所有key的集合
        {
            int freq = map.get(word);
            //System.out.println(word+"="+freq);
            outputString=outputString+word+" "+freq+"\r\n";
        }
        IOHelpers.writeAllText("d:/1.txt",outputString);
    }

    //判断s是否是一个英文单词(全部由英文字母组成)
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
