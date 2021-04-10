package com.microsoft.xbox.toolkit;

import java.net.*;

public class UrlUtil
{
    public static boolean UrisEqualCaseInsensitive(final URI uri, final URI uri2) {
        return uri == uri2 || (uri != null && uri2 != null && JavaUtil.stringsEqualCaseInsensitive(uri.toString(), uri2.toString()));
    }
    
    public static String encodeUrl(final String s) {
        if (s != null) {
            if (s.length() == 0) {
                return null;
            }
            final URI encodedUri = getEncodedUri(s);
            if (encodedUri != null) {
                return encodedUri.toString();
            }
        }
        return null;
    }
    
    public static URI getEncodedUri(final String s) {
        if (s != null && s.length() != 0) {
            return getEncodedUriNonNull(s);
        }
        return null;
    }
    
    public static URI getEncodedUriNonNull(final String s) {
        try {
            final URL url = new URL(s);
            return new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
        }
        catch (URISyntaxException | MalformedURLException ex) {
            return null;
        }
    }
    
    public static URI getUri(final String s) {
        if (JavaUtil.isNullOrEmpty(s)) {
            return null;
        }
        try {
            return new URI(s);
        }
        catch (Exception ex) {
            return null;
        }
    }
}
