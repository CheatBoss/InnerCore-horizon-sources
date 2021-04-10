package com.microsoft.xbox.service.network.managers;

import java.util.*;
import java.io.*;
import com.microsoft.xbox.toolkit.*;
import org.apache.http.entity.*;
import org.apache.http.*;
import com.microsoft.xbox.toolkit.network.*;
import com.google.gson.*;
import android.text.*;
import java.net.*;
import org.apache.http.client.methods.*;

public class ServiceCommon
{
    public static final int MaxBIErrorParamLength = 2048;
    
    public static void AddWebHeaders(final HttpUriRequest httpUriRequest, final List<Header> list) {
        if (list != null) {
            final Iterator<Header> iterator = list.iterator();
            while (iterator.hasNext()) {
                httpUriRequest.addHeader((Header)iterator.next());
            }
        }
    }
    
    private static void ParseHttpResponseForStatus(final String s, final int n, final String s2) throws XLEException {
        ParseHttpResponseForStatus(s, n, s2, null);
    }
    
    private static void ParseHttpResponseForStatus(final String s, final int n, final String s2, final InputStream inputStream) throws XLEException {
        if (n >= 200 && n < 400) {
            return;
        }
        if (n == -1) {
            throw new XLEException(3L);
        }
        if (n == 401 || n == 403) {
            throw new XLEException(1020L);
        }
        if (n == 400) {
            if (inputStream == null) {
                throw new XLEException(15L);
            }
            throw new XLEException(15L, null, null, StreamUtil.ReadAsString(inputStream));
        }
        else {
            if (n == 500) {
                throw new XLEException(13L);
            }
            if (n == 503) {
                throw new XLEException(18L);
            }
            if (n == 404) {
                throw new XLEException(21L);
            }
            throw new XLEException(4L);
        }
    }
    
    public static boolean delete(final String s, final List<Header> list) throws XLEException {
        final int deleteWithStatus = deleteWithStatus(s, list);
        return deleteWithStatus == 200 || deleteWithStatus == 204;
    }
    
    public static boolean delete(final String s, final List<Header> list, final String s2) throws XLEException {
        try {
            if (JavaUtil.isNullOrEmpty(s2)) {
                return delete(s, list);
            }
            return delete(s, list, s2.getBytes("UTF-8"));
        }
        catch (UnsupportedEncodingException ex) {
            throw new XLEException(5L, ex);
        }
    }
    
    public static boolean delete(String string, final List<Header> list, final byte[] array) throws XLEException {
        final URI encodedUri = UrlUtil.getEncodedUri(string);
        string = encodedUri.toString();
        new TimeMonitor();
        final HttpDeleteWithRequestBody httpDeleteWithRequestBody = new HttpDeleteWithRequestBody(encodedUri);
        if (array != null && array.length > 0) {
            try {
                httpDeleteWithRequestBody.setEntity((HttpEntity)new ByteArrayEntity(array));
            }
            catch (Exception ex) {
                throw new XLEException(5L, ex);
            }
        }
        boolean b = false;
        final XLEHttpStatusAndStream excuteHttpRequest = excuteHttpRequest((HttpUriRequest)httpDeleteWithRequestBody, string, list, false, 0);
        if (excuteHttpRequest.statusCode == 200 || excuteHttpRequest.statusCode == 204) {
            b = true;
        }
        excuteHttpRequest.close();
        return b;
    }
    
    public static int deleteWithStatus(final String s, final List<Header> list) throws XLEException {
        final URI encodedUri = UrlUtil.getEncodedUri(s);
        final String string = encodedUri.toString();
        new TimeMonitor();
        final XLEHttpStatusAndStream excuteHttpRequest = excuteHttpRequest((HttpUriRequest)new HttpDelete(encodedUri), string, list, false, 0);
        excuteHttpRequest.close();
        return excuteHttpRequest.statusCode;
    }
    
    private static XLEHttpStatusAndStream excuteHttpRequest(final HttpUriRequest httpUriRequest, final String s, final List<Header> list, final boolean b, final int n) throws XLEException {
        return excuteHttpRequest(httpUriRequest, s, list, b, n, false);
    }
    
    private static XLEHttpStatusAndStream excuteHttpRequest(HttpUriRequest substring, String s, List<Header> o, final boolean b, int statusCode, final boolean b2) throws XLEException {
        AddWebHeaders(substring, (List<Header>)o);
        new XLEHttpStatusAndStream();
        o = HttpClientFactory.networkOperationsFactory.getHttpClient(statusCode).getHttpStatusAndStreamInternal(substring, true);
        Label_0047: {
            if (b2) {
                break Label_0047;
            }
        Block_9_Outer:
            while (true) {
                try {
                    ParseHttpResponseForStatus(s, ((XLEHttpStatusAndStream)o).statusCode, ((XLEHttpStatusAndStream)o).statusLine);
                    // iftrue(Label_0117:, o != null)
                    // iftrue(Label_0241:, o.length() <= 2048)
                    // iftrue(Label_0134:, substring == null)
                    // iftrue(Label_0220:, substring == null)
                    // iftrue(Label_0220:, substring.getURI() == null)
                    // iftrue(Label_0188:, o == null || TextUtils.isEmpty((CharSequence)o.statusLine))
                    while (true) {
                    Label_0123_Outer:
                        while (true) {
                            if (((XLEHttpStatusAndStream)o).stream != null) {
                                return (XLEHttpStatusAndStream)o;
                            }
                            if (!b) {
                                return (XLEHttpStatusAndStream)o;
                            }
                            throw new XLEException(7L);
                            final JsonObject jsonObject = new JsonObject();
                            final JsonObject jsonObject2 = new JsonObject();
                            while (true) {
                                Block_5: {
                                    break Block_5;
                                    Block_6: {
                                        while (true) {
                                        Label_0241_Outer:
                                            while (true) {
                                                substring = (HttpUriRequest)o;
                                                final String s2;
                                            Block_10:
                                                while (true) {
                                                    Block_12: {
                                                        break Block_12;
                                                        jsonObject.addProperty("Request", (String)substring);
                                                        jsonObject2.addProperty("code", (Number)statusCode);
                                                        jsonObject2.addProperty("description", s);
                                                        jsonObject.add("Response", jsonObject2);
                                                        throw;
                                                        break Block_6;
                                                        o = s2;
                                                        break Block_10;
                                                    }
                                                    substring = (HttpUriRequest)((String)o).substring(0, 2048);
                                                    continue Label_0123_Outer;
                                                }
                                                o = s2;
                                                o = substring.getURI().toString();
                                                continue Label_0241_Outer;
                                            }
                                            s = ((String)o).substring(0, 2048);
                                            continue Block_9_Outer;
                                            Label_0188: {
                                                s = "";
                                            }
                                            continue Block_9_Outer;
                                        }
                                    }
                                    substring.getMethod();
                                    Label_0134: {
                                        final String s2 = "";
                                    }
                                    break Label_0123_Outer;
                                }
                                statusCode = 0;
                                continue Block_9_Outer;
                                Label_0117: {
                                    statusCode = ((XLEHttpStatusAndStream)o).statusCode;
                                }
                                continue Block_9_Outer;
                            }
                            ParseHttpResponseForStatus(s, ((XLEHttpStatusAndStream)o).statusCode, ((XLEHttpStatusAndStream)o).statusLine, ((XLEHttpStatusAndStream)o).stream);
                            continue Label_0123_Outer;
                        }
                        final int length = ((XLEHttpStatusAndStream)o).statusLine.length();
                        o = (s = ((XLEHttpStatusAndStream)o).statusLine);
                        continue;
                    }
                }
                // iftrue(Label_0191:, length <= 2048)
                catch (XLEException ex) {
                    continue;
                }
                break;
            }
        }
    }
    
    public static XLEHttpStatusAndStream getStreamAndStatus(final String s, final List<Header> list) throws XLEException {
        XLEHttpStatusAndStream xleHttpStatusAndStream2;
        final XLEHttpStatusAndStream xleHttpStatusAndStream = xleHttpStatusAndStream2 = getStreamAndStatus(s, list, (boolean)(1 != 0), 0);
        if (xleHttpStatusAndStream != null) {
            xleHttpStatusAndStream2 = xleHttpStatusAndStream;
            if (!JavaUtil.isNullOrEmpty(xleHttpStatusAndStream.redirectUrl)) {
                xleHttpStatusAndStream2 = getStreamAndStatus(xleHttpStatusAndStream.redirectUrl, list);
            }
        }
        return xleHttpStatusAndStream2;
    }
    
    private static XLEHttpStatusAndStream getStreamAndStatus(final String s, final List<Header> list, final boolean b, final int n) throws XLEException {
        return getStreamAndStatus(s, list, b, n, false);
    }
    
    private static XLEHttpStatusAndStream getStreamAndStatus(final String s, final List<Header> list, final boolean b, final int n, final boolean b2) throws XLEException {
        URI encodedUri;
        if (b) {
            encodedUri = UrlUtil.getEncodedUri(s);
        }
        else {
            try {
                encodedUri = new URI(s);
            }
            catch (URISyntaxException ex) {
                encodedUri = null;
            }
        }
        return excuteHttpRequest((HttpUriRequest)new HttpGet(encodedUri), encodedUri.toString(), list, true, n, b2);
    }
    
    public static XLEHttpStatusAndStream postStreamWithStatus(String string, final List<Header> list, final byte[] array) throws XLEException {
        final URI encodedUri = UrlUtil.getEncodedUri(string);
        string = encodedUri.toString();
        final HttpPost httpPost = new HttpPost(encodedUri);
        if (array != null && array.length > 0) {
            try {
                httpPost.setEntity((HttpEntity)new ByteArrayEntity(array));
            }
            catch (Exception ex) {
                throw new XLEException(5L, ex);
            }
        }
        return excuteHttpRequest((HttpUriRequest)httpPost, string, list, false, 0);
    }
    
    public static XLEHttpStatusAndStream postStringWithStatus(final String s, final List<Header> list, final String s2) throws XLEException {
        try {
            return postStreamWithStatus(s, list, s2.getBytes("UTF-8"));
        }
        catch (UnsupportedEncodingException ex) {
            throw new XLEException(5L, ex);
        }
    }
    
    public static XLEHttpStatusAndStream putStreamWithStatus(String string, final List<Header> list, final byte[] array) throws XLEException {
        final URI encodedUri = UrlUtil.getEncodedUri(string);
        string = encodedUri.toString();
        final HttpPut httpPut = new HttpPut(encodedUri);
        if (array != null && array.length > 0) {
            try {
                httpPut.setEntity((HttpEntity)new ByteArrayEntity(array));
            }
            catch (Exception ex) {
                throw new XLEException(5L, ex);
            }
        }
        return excuteHttpRequest((HttpUriRequest)httpPut, string, list, false, 0);
    }
    
    public static XLEHttpStatusAndStream putStringWithStatus(final String s, final List<Header> list, final String s2) throws XLEException {
        try {
            return putStreamWithStatus(s, list, s2.getBytes("UTF-8"));
        }
        catch (UnsupportedEncodingException ex) {
            throw new XLEException(5L, ex);
        }
    }
}
