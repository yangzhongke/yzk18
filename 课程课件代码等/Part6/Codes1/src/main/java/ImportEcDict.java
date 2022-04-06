import com.yzk18.commons.JDBCExecutor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class ImportEcDict {
    public static void main(String[] args) throws IOException {
        JDBCExecutor jdbcExecutor = new JDBCExecutor("jdbc:sqlite:E:\\主同步盘\\我的坚果云\\UoZ\\SE101-玩着学编程\\Part7课件\\ecDict.db",null,null);
        Reader in = new FileReader("E:\\主同步盘\\我的坚果云\\UoZ\\SE101-玩着学编程\\Part7课件\\ecdict.csv");
        Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
        for (CSVRecord record : records) {
            String word = record.get(0);
            if(word.contains(" ")) continue;//跳过词组
            String definition = record.get(2);
            String chinese = record.get(3);
            jdbcExecutor.execute("insert into Words(Word,Definition,Translation) values(?,?,?)",
                    word,definition,chinese);
            System.out.println(word);
        }

    }
}
