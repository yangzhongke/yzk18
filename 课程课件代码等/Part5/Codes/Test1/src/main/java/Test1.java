import com.yzk18.commons.CommonHelpers;
import com.yzk18.commons.JDBCExecutor;
import com.yzk18.commons.JDBCRow;

import java.util.List;

public class Test1 {
    public static void main(String[] args) {
        String url="jdbc:sqlite:E:\\主同步盘\\我的坚果云\\UoZ\\SE101-玩着学编程\\Part5课件\\Codes\\Test1\\db.db";
        JDBCExecutor jdbcExecutor = new JDBCExecutor(url,null,null);
        /*
        jdbcExecutor.execute("Insert into T_Persons(Name,Age) Values('abc',88)");
        jdbcExecutor.execute("Insert into T_Persons(Name,Age) Values(?,?)","yzk",18);

        List<JDBCRow> rows1 = jdbcExecutor.query("select * from T_Persons");
        for(JDBCRow row : rows1)
        {
            String name = row.getString("Name");
            int age = row.getInt("Age");
            System.out.println(name+age);
        }

        List<Person> persons = jdbcExecutor.query(Person.class,"select * from T_Persons");
        for(Person p : persons)
        {
            System.out.println(p);
        }*/
        System.out.println("请输入用户名");
        String userName = CommonHelpers.readLine();
        System.out.println("请输入密码");
        String password = CommonHelpers.readLine();
        //List<JDBCRow> rows1 = jdbcExecutor.query("select * from T_Users where UserName='"+userName+"' and Password='"+password+"'");
        List<JDBCRow> rows1 = jdbcExecutor.query("select * from T_Users where UserName=? and Password=?",userName,password);
        if(rows1.size()>0)
        {
            System.out.println("登录成功");
        }
        else
        {
            System.out.println("登录失败");
        }
    }
}
