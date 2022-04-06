import com.yzk18.commons.CommonHelpers;
import com.yzk18.commons.JDBCExecutor;
import com.yzk18.commons.JDBCRow;

import java.util.List;

public class Test1 {
    public static void main(String[] args) {
        JDBCExecutor jdbcExecutor = new JDBCExecutor("jdbc:sqlite:E:/test1.db",null,null);
        //jdbcExecutor.execute("insert into T_Authors(Name,Age) values('曹雪芹',18)");
        /*
        List<JDBCRow> rows = jdbcExecutor.query("select * from T_Authors");
        for (JDBCRow row : rows)
        {
            long id = row.getLong("Id");
            String name = row.getString("Name");
            int age = row.getInt("Age");
            System.out.println("id="+id+",name="+name+",age="+age);
        }*/
        /*
        List<Author> authors = jdbcExecutor.query(Author.class,"select * from T_Authors where Age>15");
        for(Author a : authors)
        {
            System.out.println(a);
            System.out.println(a.getName());
        }*/
        /*
        System.out.println("请输入姓名");
        String name = CommonHelpers.readLine();
        System.out.println("请输入年龄");
        int age = CommonHelpers.readInt();
        String sql = "insert into T_Authors(Name,Age) values('"+name+"',"+age+")";//非常危险
        System.out.println(sql);
        jdbcExecutor.execute(sql);*/
        System.out.println("请输入用户名");
        String username = CommonHelpers.readLine();
        System.out.println("请输入密码");
        String password = CommonHelpers.readLine();
        /*
        String sql = "select * from Users where UserName='"+username+"' and Password='"+
                password+"'";
        System.out.println(sql);
        List<JDBCRow> rows = jdbcExecutor.query(sql);*/
        List<JDBCRow> rows = jdbcExecutor.query("select * from Users where UserName=? and Password=?",
                username,password);
        if(rows.size()==0)
        {
            System.out.println("登陆失败");
        }
        else
        {
            System.out.println("登录成功");
        }


    }
}
