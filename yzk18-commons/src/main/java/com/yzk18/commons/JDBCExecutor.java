package com.yzk18.commons;

import org.apache.commons.beanutils.ConvertUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;
import java.util.function.Consumer;

public class JDBCExecutor {
    private String url;
    private String userName;
    private String password;

    public JDBCExecutor(String url, String userName, String password) {
        this.url = url;
        this.userName = userName;
        this.password = password;
    }

    private String[] getColumnNames(ResultSetMetaData rsmd) throws SQLException {
        List<String> list =new LinkedList<>();
        for(int i=0;i< rsmd.getColumnCount();i++)
        {
            String colName = rsmd.getColumnName(i+1);
            list.add(colName);
        }
        return list.toArray(new String[list.size()]);
    }

    public List<Map<String,Object>> queryAsMap(String sql, Object... parameters)
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
                List<Map<String,Object>> rows = new LinkedList<>();
                while(rs.next())
                {
                    //column name is CaseInsensitive
                    LinkedCaseInsensitiveMap<Object> row = new LinkedCaseInsensitiveMap<Object>();
                    for(int i=0;i<colNames.length;i++)
                    {
                        Object value = rs.getObject(i+1);
                        row.put(colNames[i],value);
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
    public <T> List<T> query(Class<T> clz, String sql, Object... parameters)
    {
        List<Map<String,Object>> rows = queryAsMap(sql,parameters);
        List<T> results = new LinkedList<T>();
        for(Map<String,Object> row : rows)
        {
            T obj = (T)newInstance(clz);
            PropertyDescriptor[] props = ReflectionHelpers.getRWPropertyDescriptors(clz);
            for (PropertyDescriptor pd : props)
            {
                String propName = pd.getName();
                Object value = row.get(propName);
                if(value!=null)
                {
                    ReflectionHelpers.setPropertyValue(obj,propName,value);
                }
            }
            results.add(obj);
        }
        return results;
    }

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

