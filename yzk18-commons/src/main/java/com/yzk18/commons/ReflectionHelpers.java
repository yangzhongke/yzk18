package com.yzk18.commons;

import org.apache.commons.lang3.StringUtils;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * <div lang="zh-cn">用户简化反射的工具类。</div>
 */
public class ReflectionHelpers {

    /**
     * <div lang="zh-cn">获取clz类型的所有可读且可写的属性（排除getClass()方法），支持chain setter方法，比如Person setName(String value)</div>
     * @param clz
     * @return
     */
    public static PropertyDescriptor[] getRWPropertyDescriptors(Class clz)
    {
        try {
            PropertyDescriptor[] props = Introspector.getBeanInfo(clz).getPropertyDescriptors();
            List<PropertyDescriptor> results = new LinkedList<>();
            for(PropertyDescriptor propDesc : props)
            {
                //skip getClass
                if(propDesc.getName().equals("class"))
                {
                    continue;
                }
                //skip write-only property
                if(propDesc.getReadMethod()==null)
                {
                    continue;
                }
                String propName = propDesc.getName();
                //according to specification of JavaBean, chain setter method is invalid,
                //so getWriteMethod() of chain setter method is null.
                //Therefore we should build PropertyDescriptor for chain setter method manually.
                if(propDesc.getWriteMethod()==null)
                {
                    String setterMethodName = "set"+ StringUtils.capitalize(propName);
                    Method setterMethod = clz.getMethod(setterMethodName,propDesc.getPropertyType());
                    PropertyDescriptor newPropDesc = new PropertyDescriptor(propName,
                            propDesc.getReadMethod(),setterMethod);
                    results.add(newPropDesc);
                }
                else
                {
                    results.add(propDesc);
                }
            }
            return results.toArray(new PropertyDescriptor[results.size()]);
        } catch (IntrospectionException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }


    public static PropertyDescriptor getPropertyDescriptor(Object obj, String propName)
    {
        Class clz = obj.getClass();
        Optional<PropertyDescriptor> optPropDesc = Arrays.stream(getRWPropertyDescriptors(clz))
                .filter(p->p.getName().equals(propName)).findFirst();
        if(!optPropDesc.isPresent())
        {
            return null;
        }
        return optPropDesc.get();
    }

    public static Object getPropertyValue(Object obj, String propName)
    {
        PropertyDescriptor propDesc = getPropertyDescriptor(obj,propName);
        return getPropertyValue(obj,propDesc);
    }

    public static Object getPropertyValue(Object obj, PropertyDescriptor propDesc)
    {
        Object value = null;
        try
        {
            value = propDesc.getReadMethod().invoke(obj);
        } catch (IllegalAccessException| InvocationTargetException e)
        {
            throw new RuntimeException(e);
        }
        return value;
    }

    public static void setPropertyValue(Object obj, String propName,Object value)
    {
        PropertyDescriptor propDesc = getPropertyDescriptor(obj,propName);
        if(propDesc==null)
        {
            throw new IllegalArgumentException(propName+" not found");
        }
        setPropertyValue(obj,propDesc,value);
    }
    public static void setPropertyValue(Object obj, PropertyDescriptor propDesc,Object value)
    {
        Class propType = propDesc.getPropertyType();
        if(value!=null&&propType!=value.getClass())
        {
            value = CommonHelpers.convert(value,propType);
        }
        try
        {
            propDesc.getWriteMethod().invoke(obj,value);
        } catch (IllegalAccessException|InvocationTargetException e)
        {
            throw new RuntimeException(e);
        }
    }
}
