package com.yzk18.net;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <div lang="zh-cn">Http请求头</div>
 */
public class HttpHeaders {
    private Map<String,String> data =new HashMap<>();
    private LinkedHashMap<String,String> cookies = new LinkedHashMap<>();
    public String[] getNames()
    {
        return this.data.keySet().toArray(new String[0]);
    }

    public String get(String name)
    {
        return this.data.get(name);
    }

    /**
     * <div lang="zh-cn">设置报文头的值</div>
     * @param name <div lang="zh-cn">名字</div>
     * @param value <div lang="zh-cn">值（不需要encode）</div>
     * @return
     */
    public HttpHeaders set(String name,String value)
    {
        this.data.put(name,value);
        return this;
    }

    /**
     * <div lang="zh-cn">设置报文头中Authorization的值</div>
     * @param value <div lang="zh-cn"></div>
     * @return
     */
    public HttpHeaders setAuthorization(String value)
    {
        this.set("Authorization",value);
        return this;
    }

    /**
     * <div lang="zh-cn">设置报文头中ContentType的值</div>
     * @param value <div lang="zh-cn"></div>
     * @return
     */
    public HttpHeaders setContentType(String value)
    {
        this.set("Content-Type",value);
        return this;
    }

    /**
     * <div lang="zh-cn">设置报文头中UserAgent的值</div>
     * @param value <div lang="zh-cn"></div>
     * @return
     */
    public HttpHeaders setUserAgent(String value)
    {
        this.set("User-Agent",value);
        return this;
    }

    /**
     * <div lang="zh-cn">设置报文头中Referer的值</div>
     * @param value <div lang="zh-cn"></div>
     * @return
     */
    public HttpHeaders setReferer(String value)
    {
        this.set("Referer",value);
        return this;
    }

    /**
     * <div lang="zh-cn">添加Cookie</div>
     * @param name <div lang="zh-cn"></div>
     * @param value <div lang="zh-cn"></div>
     * @return
     */
    public HttpHeaders addCookie(String name,String value)
    {
        this.cookies.put(name,value);
        return this;
    }

    public Map<String,String> getCookies()
    {
        return this.cookies;
    }
}
