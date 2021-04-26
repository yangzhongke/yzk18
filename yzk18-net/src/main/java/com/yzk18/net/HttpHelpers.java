package com.yzk18.net;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class HttpHelpers {
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
}
