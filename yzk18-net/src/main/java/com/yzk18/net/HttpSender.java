package com.yzk18.net;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.mozilla.intl.chardet.nsDetector;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * <div lang="zh-cn">发出Http请求或Https请求的类</div>
 */
public class HttpSender
{
    private final OkHttpClient httpClient;
    private final SimpleCookieJar cookieJar;

    public HttpSender()
    {
        this.cookieJar = new SimpleCookieJar();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        this.httpClient= builder
                .cookieJar(this.cookieJar)
                .sslSocketFactory(OkHttpUtils.createSSLSocketFactory(),
                        new OkHttpUtils.TrustAllManager())
                .hostnameVerifier(new OkHttpUtils.TrustAllHostnameVerifier())
                .retryOnConnectionFailure(true).build();
    }

    /**
     * <div lang="zh-cn">发送Get请求，获得响应的byte数组。</div>
     * @param url <div lang="zh-cn">路径</div>
     * @param requestHeaders <div lang="zh-cn">请求头</div>
     * @return <div lang="zh-cn">返回的byte[]</div>
     */
    public byte[] sendGetBytes(String url, HttpHeaders requestHeaders)
    {
        Request.Builder requestBuilder =new Request.Builder()
                .url(url);
        if(requestHeaders!=null)
        {
            StringBuilder sbCookie = new StringBuilder();
            for(Map.Entry<String,String> cookie : requestHeaders.getCookies().entrySet())
            {
                cookieJar.setCookie(url,cookie.getKey(),cookie.getValue());
            }

            for(String headerName : requestHeaders.getNames())
            {
                String headerValue = requestHeaders.get(headerName);
                requestBuilder.header(headerName,headerValue);
            }
        }
        Request request = requestBuilder.build();
        Call call = this.httpClient.newCall(request);
        try
        {
            return call.execute().body().bytes();
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">发送Get请求，获得响应的byte数组。</div>
     * @param url <div lang="zh-cn">路径</div>
     * @return <div lang="zh-cn">返回的byte[]</div>
     */
    public byte[] sendGetBytes(String url)
    {
        return sendGetBytes(url,null);
    }

    //todo: replace with detectTextEncoding of yzak18-commons-1.5.1
    private static String detectTextEncoding(byte[] bytes)
    {
        String[] charSetFounded = new String[1];
        nsDetector detector = new nsDetector();
        detector.Init(charset->charSetFounded[0] = charset);
        detector.DoIt(bytes, bytes.length, false);
        detector.DataEnd();
        detector.Reset();
        return  charSetFounded[0];
    }

    /**
     * <div lang="zh-cn">发送Get请求，获得响应的响应体字符串</div>
     * @param url <div lang="zh-cn">路径</div>
     * @param encodingName <div lang="zh-cn">编码</div>
     * @param requestHeaders <div lang="zh-cn">请求头</div>
     * @return
     */
    public String sendGetString(String url, String encodingName, HttpHeaders requestHeaders)
    {
        byte[] bytes = sendGetBytes(url,requestHeaders);
        try
        {
            return new String(bytes,encodingName);
        } catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">发送Get请求，获得响应的响应体字符串</div>
     * @param url <div lang="zh-cn">路径</div>
     * @param encodingName <div lang="zh-cn">编码</div>
     * @return <div lang="zh-cn"></div>
     */
    public String sendGetString(String url, String encodingName)
    {
        return sendGetString(url,encodingName,null);
    }

    /**
     * <div lang="zh-cn">发送Get请求，获得响应的响应体字符串。自动检测响应的编码。</div>
     * @param url <div lang="zh-cn">路径</div>
     * @param requestHeaders <div lang="zh-cn">请求头</div>
     * @return <div lang="zh-cn"></div>
     */
    public String sendGetString(String url, HttpHeaders requestHeaders)
    {

        byte[] bytes = sendGetBytes(url,requestHeaders);
        String encodingName = detectTextEncoding(bytes);
        if(encodingName==null)
        {
            encodingName = "UTF-8";
        }
        try
        {
            return new String(bytes,encodingName);
        } catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">发送Get请求，获得响应的响应体字符串。自动检测响应的编码。</div>
     * @param url <div lang="zh-cn">路径</div>
     * @return
     */
    public String sendGetString(String url)
    {
        return sendGetString(url,(HttpHeaders)null);
    }
}
