package com.yzk18.commons;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;
import java.util.function.Consumer;

/**
 * <div lang="zh-cn">用于简化JDBC中SQL执行的执行器类。</div>
 */
public class JDBCExecutor {
    private String url;
    private String userName;
    private String password;

    /**
     * <div lang="zh-cn">构建实例。</div>
     * @param url <div lang="zh-cn">JDBC的Url路径</div>
     * @param userName <div lang="zh-cn">用户名</div>
     * @param password <div lang="zh-cn">密码</div>
     */
    public JDBCExecutor(String url, String userName, String password)
    {
        this.url = url;
        this.userName = userName;
        this.password = password;
    }

    private String[] getColumnNames(ResultSetMetaData rsmd) throws SQLException
    {
        List<String> list =new LinkedList<>();
        for(int i=0;i< rsmd.getColumnCount();i++)
        {
            String colName = rsmd.getColumnName(i+1);
            list.add(colName);
        }
        return list.toArray(new String[list.size()]);
    }

    /**
     * <div lang="zh-cn">执行查询SQL语句sql，把执行结果放到返回值中，返回值为List<JDBCRow>类型。</div>
     * @param sql <div lang="zh-cn">SQL语句。</div>
     * @param parameters <div lang="zh-cn">查询参数值。</div>
     * @return <div lang="zh-cn">查询结果集，每一行数据对应一个JDBCRow对象。</div>
     */
    public List<JDBCRow> query(String sql, Object... parameters)
    {
        try(Connection conn = DriverManager.getConnection(url,this.userName,this.password);
            PreparedStatement ps = conn.prepareStatement(sql);
        )
        {
            for(int i=0;i<parameters.length;i++)
            {
                ps.setObject(i+1,parameters[i]);
            }
            try(ResultSet rs = ps.executeQuery())
            {
                ResultSetMetaData rsmd = rs.getMetaData();
                String[] colNames = getColumnNames(rsmd);
                List<JDBCRow> rows = new LinkedList<>();
                while(rs.next())
                {
                    //column name is CaseInsensitive
                    JDBCRow row = new JDBCRow();
                    for(int i=0;i<colNames.length;i++)
                    {
                        Object value = rs.getObject(i+1);
                        row.setObject(colNames[i],value);
                    }
                    rows.add(row);
                }
                return rows;
            }
        }
        catch (SQLException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    private Object newInstance(Class clz)
    {
        Object obj = null;
        try
        {
            obj = clz.getConstructor().newInstance();
        } catch (InstantiationException|IllegalAccessException
                |InvocationTargetException|NoSuchMethodException e)
        {
           throw new RuntimeException(e);
        }
        return obj;
    }

    //becuase Java doesn't support new T[3], so the return value should be List<T> instead of T[]

    /**
     * <div lang="zh-cn">执行查询SQL语句sql，把执行结果放到返回值中，返回值为List<T>类型。</div>
     * @param clz <div lang="zh-cn">映射为结果对象类型。</div>
     * @param sql <div lang="zh-cn">SQL语句。</div>
     * @param parameters <div lang="zh-cn">查询参数值。</div>
     * @param <T> <div lang="zh-cn">映射为结果对象类型。</div>
     * @return <div lang="zh-cn">查询结果，每一行数据对应一个T类型对象。</div>
     */
    public <T> List<T> query(Class<T> clz, String sql, Object... parameters)
    {
        List<JDBCRow> rows = query(sql,parameters);
        List<T> results = new LinkedList<T>();
        for(JDBCRow row : rows)
        {
            T obj = (T)newInstance(clz);
            PropertyDescriptor[] props = ReflectionHelpers.getRWPropertyDescriptors(clz);
            for (PropertyDescriptor pd : props)
            {
                String propName = pd.getName();
                Object value = row.getObject(propName);
                if(value!=null)
                {
                    ReflectionHelpers.setPropertyValue(obj,propName,value);
                }
            }
            results.add(obj);
        }
        return results;
    }

    /**
     * <div lang="zh-cn">执行SQL语句，一般为update、delete等没有执行结果集的SQL。</div>
     * @param sql
     * @param parameters
     * @return
     */
    public boolean execute(String sql, Object... parameters)
    {
        try(Connection conn = DriverManager.getConnection(url,this.userName,this.password);
            PreparedStatement ps = conn.prepareStatement(sql);
        )
        {
            for(int i=0;i<parameters.length;i++)
            {
                ps.setObject(i+1,parameters[i]);
            }
            return ps.execute();
        }
        catch (SQLException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    /**
     * <div lang="zh-cn">在一个JDBC连接中执行consumer所代表的操作。</div>
     * @param consumer
     */
    public void run(Consumer<Connection> consumer)
    {
        try(Connection conn = DriverManager.getConnection(url,
                this.userName,this.password))
        {
            consumer.accept(conn);
        }
        catch (SQLException ex)
        {
            throw new RuntimeException(ex);
        }
    }
}

