package com.yzk18.net;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class SimpleCookieJar implements CookieJar
{
    protected final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

    @Override
    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list)
    {
        //The list may be UnmodifiableCollection,
        //but putCookie should add element to storedCookieList,
        //so we should convert list to a modifiable list.
        List<Cookie> newList = new LinkedList<>(list);
        cookieStore.put(httpUrl.host(), newList);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl httpUrl)
    {
        List<Cookie> cookies = cookieStore.get(httpUrl.host());
        return cookies != null ? cookies : new ArrayList<Cookie>();
    }

    public void setCookie(String url, String cookieName, String cookieValue) {
        final HttpUrl httpUrl = HttpUrl.parse(url);
        String value = HttpHelpers.encodeURIComponent(cookieName)
                + "=" + HttpHelpers.encodeURIComponent(cookieValue);
        setCookie(url, Cookie.parse(httpUrl, value));
    }
    private void setCookie(String url, Cookie cookie) {

        final String host = HttpUrl.parse(url).host();

        List<Cookie> cookieListForUrl = cookieStore.get(host);
        if (cookieListForUrl == null) {
            cookieListForUrl = new ArrayList<Cookie>();
            cookieStore.put(host, cookieListForUrl);
        }
        putCookie(cookieListForUrl, cookie);

    }

    private void putCookie(List<Cookie> storedCookieList, Cookie newCookie) {
        Cookie oldCookie = null;
        for (Cookie storedCookie : storedCookieList) {

            // create key for comparison
            final String oldCookieKey = storedCookie.name() + storedCookie.path();
            final String newCookieKey = newCookie.name() + newCookie.path();

            if (oldCookieKey.equals(newCookieKey)) {
                oldCookie = storedCookie;
                break;
            }
        }
        if (oldCookie != null) {
            storedCookieList.remove(oldCookie);
        }
        storedCookieList.add(newCookie);
    }

}
