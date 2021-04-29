package com.yzk18.net;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class HttpHelpers {

    /**
     * <div lang="zh-cn">对s进行url编码</div>
     * @param s
     * @return
     */
    public static String encodeURIComponent(String s)
    {
        try
        {
            return URLEncoder.encode(s,"UTF-8");
        } catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">向url发送get请求，获得返回报文体的字符串格式</div>
     * @param url
     * @return
     */
    public static String sendGetString(String url)
    {
        return new HttpSender().sendGetString(url);
    }

    /**
     * <div lang="zh-cn">向url发送get请求，获得返回报文体的字节数组格式</div>
     * @param url
     * @return
     */
    public static byte[] sendGetBytes(String url)
    {
        return new HttpSender().sendGetBytes(url);
    }
}
