import com.yzk18.commons.CommonHelpers;
import com.yzk18.commons.JDBCExecutor;

import java.util.Map;

public class TestJDBC1 {
    public static void main(String[] args) {
        JDBCExecutor jdbc = new JDBCExecutor("jdbc:mysql://localhost:3306/test",
                "root","root");
        /*
        var rows = jdbc.queryAsMap("select * from reviews");
        for(var row : rows)
        {
            System.out.println(row.get("name")+"|"+row.get("review")+"|"+row.get("productCode"));
        }*/
        var items = jdbc.query(Review.class,"select * from reviews");
        CommonHelpers.println(items);
    }
}
