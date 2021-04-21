import com.yzk18.commons.CommonHelpers;
import com.yzk18.commons.JDBCExecutor;

import java.util.Map;

public class TestJDBC1 {
    public static void main(String[] args) {
        JDBCExecutor jdbc = new JDBCExecutor("jdbc:mysql://localhost:3306/test",
                "root","root");

        var rows = jdbc.queryAsMap("select * from reviews");
        for(var row : rows)
        {
            //System.out.println(row);
            System.out.println(row.getString("name")+"|"+row.getString("review")
                    +"|"+row.getString("productCode")+"|"+row.getInt("age")
                    +"|"+row.getLocalDate("Birthday")+"|"+row.getLocalDate("time1")
                    +"|"+row.getLocalDateTime("time1")
                    +"|"+row.getString("time1")+"|"+row.getString("Birthday"));
        }

        var items = jdbc.query(Review.class,"select * from reviews");
        CommonHelpers.println(items);
    }
}
