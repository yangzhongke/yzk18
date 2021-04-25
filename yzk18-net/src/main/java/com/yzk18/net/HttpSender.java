package com.yzk18.net;

import org.apache.commons.io.IOUtils;
import org.mozilla.intl.chardet.nsDetector;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * <div lang="zh-cn">发出Http请求或Https请求的类</div>
 */
public class HttpSender
{
    private static String encodeURIComponent(String s)
    {
        try
        {
            return URLEncoder.encode(s,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private static void trustAllCertificates(HttpsURLConnection urlConnection)
            throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustManagers = new TrustManager[]{
                new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    }

                    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    }

                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                }
        };
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(null,trustManagers,null);
        urlConnection.setSSLSocketFactory(ctx.getSocketFactory());
    }

    /**
     * <div lang="zh-cn">发送Get请求，获得响应的byte数组。</div>
     * @param url <div lang="zh-cn">路径</div>
     * @param requestHeaders <div lang="zh-cn">请求头</div>
     * @return <div lang="zh-cn">返回的byte[]</div>
     */
    public byte[] sendGetBytes(String url, HttpHeaders requestHeaders)
    {
        try
        {
            HttpURLConnection urlConnection = (HttpURLConnection)new URL(url).openConnection();
            if(urlConnection instanceof HttpsURLConnection)
            {
                trustAllCertificates((HttpsURLConnection)urlConnection);
            }

            urlConnection.setRequestMethod("GET");
            if(requestHeaders!=null)
            {
                StringBuilder sbCookie = new StringBuilder();
                for(Map.Entry<String,String> cookie : requestHeaders.getCookies().entrySet())
                {
                    String name = encodeURIComponent(cookie.getKey());
                    String value = encodeURIComponent(cookie.getValue());
                    sbCookie.append(name).append("=").append(value).append(";");
                }
                if(sbCookie.length()>0)
                {
                    urlConnection.setRequestProperty("Cookie",sbCookie.toString());
                }

                for(String headerName : requestHeaders.getNames())
                {
                    String headerValue = requestHeaders.get(headerName);
                    urlConnection.setRequestProperty(headerName,headerValue);
                }
            }
            try(InputStream inStream = urlConnection.getInputStream())
            {
                return IOUtils.toByteArray(inStream);
            }
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
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
