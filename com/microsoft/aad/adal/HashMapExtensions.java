package com.microsoft.aad.adal;

import android.text.*;
import org.json.*;
import java.util.*;
import java.io.*;

final class HashMapExtensions
{
    private static final String TAG = "HashMapExtensions";
    
    private HashMapExtensions() {
    }
    
    static Map<String, String> getJsonResponse(final HttpWebResponse httpWebResponse) throws JSONException {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        if (httpWebResponse != null && !TextUtils.isEmpty((CharSequence)httpWebResponse.getBody())) {
            final JSONObject jsonObject = new JSONObject(httpWebResponse.getBody());
            final Iterator keys = jsonObject.keys();
            while (keys.hasNext()) {
                final String s = keys.next();
                hashMap.put(s, jsonObject.getString(s));
            }
        }
        return hashMap;
    }
    
    static HashMap<String, String> jsonStringAsMap(final String s) throws JSONException {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        if (!StringExtensions.isNullOrBlank(s)) {
            final JSONObject jsonObject = new JSONObject(s);
            final Iterator keys = jsonObject.keys();
            while (keys.hasNext()) {
                final String s2 = keys.next();
                hashMap.put(s2, jsonObject.getString(s2));
            }
        }
        return hashMap;
    }
    
    static HashMap<String, List<String>> jsonStringAsMapList(final String s) throws JSONException {
        final HashMap<String, ArrayList<String>> hashMap = (HashMap<String, ArrayList<String>>)new HashMap<String, List<String>>();
        if (!StringExtensions.isNullOrBlank(s)) {
            final JSONObject jsonObject = new JSONObject(s);
            final Iterator keys = jsonObject.keys();
            while (keys.hasNext()) {
                final String s2 = keys.next();
                final ArrayList<String> list = new ArrayList<String>();
                final JSONArray jsonArray = new JSONArray(jsonObject.getString(s2));
                for (int i = 0; i < jsonArray.length(); ++i) {
                    list.add(jsonArray.get(i).toString());
                }
                hashMap.put(s2, list);
            }
        }
        return (HashMap<String, List<String>>)hashMap;
    }
    
    static HashMap<String, String> urlFormDecode(final String s) {
        return urlFormDecodeData(s, "&");
    }
    
    static HashMap<String, String> urlFormDecodeData(String urlFormDecode, String s) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        if (!StringExtensions.isNullOrBlank(urlFormDecode)) {
            final StringTokenizer stringTokenizer = new StringTokenizer(urlFormDecode, s);
            while (stringTokenizer.hasMoreTokens()) {
                final String[] split = stringTokenizer.nextToken().split("=");
                final int length = split.length;
                urlFormDecode = null;
                Label_0128: {
                    if (length == 2) {
                        try {
                            s = StringExtensions.urlFormDecode(split[0].trim());
                            urlFormDecode = StringExtensions.urlFormDecode(split[1].trim());
                            break Label_0128;
                        }
                        catch (UnsupportedEncodingException ex) {
                            Logger.i("HashMapExtensions", ADALError.ENCODING_IS_NOT_SUPPORTED.getDescription(), ex.getMessage(), null);
                            continue;
                        }
                    }
                    if (split.length == 1) {
                        s = StringExtensions.urlFormDecode(split[0].trim());
                        urlFormDecode = "";
                    }
                    else {
                        s = null;
                    }
                }
                if (!StringExtensions.isNullOrBlank(s)) {
                    hashMap.put(s, urlFormDecode);
                }
            }
        }
        return hashMap;
    }
}
