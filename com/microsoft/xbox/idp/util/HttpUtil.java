package com.microsoft.xbox.idp.util;

import android.net.*;
import android.text.*;

public class HttpUtil
{
    public static HttpCall appendCommonParameters(final HttpCall httpCall, final String xboxContractVersionHeaderValue) {
        httpCall.setXboxContractVersionHeaderValue(xboxContractVersionHeaderValue);
        httpCall.setContentTypeHeaderValue("application/json");
        httpCall.setRetryAllowed(true);
        return httpCall;
    }
    
    public static String getEndpoint(final Uri uri) {
        final StringBuilder sb = new StringBuilder();
        sb.append(uri.getScheme());
        sb.append("://");
        sb.append(uri.getEncodedAuthority());
        return sb.toString();
    }
    
    public static Uri$Builder getImageSizeUrlParams(final Uri$Builder uri$Builder, final ImageSize imageSize) {
        return uri$Builder.appendQueryParameter("w", Integer.toString(imageSize.w)).appendQueryParameter("h", Integer.toString(imageSize.h));
    }
    
    public static String getPathAndQuery(final Uri uri) {
        final String encodedPath = uri.getEncodedPath();
        final String encodedQuery = uri.getEncodedQuery();
        final String encodedFragment = uri.getEncodedFragment();
        final StringBuffer sb = new StringBuffer();
        sb.append(encodedPath);
        if (!TextUtils.isEmpty((CharSequence)encodedQuery)) {
            sb.append("?");
            sb.append(encodedQuery);
        }
        if (!TextUtils.isEmpty((CharSequence)encodedFragment)) {
            sb.append("#");
            sb.append(encodedFragment);
        }
        return sb.toString();
    }
    
    public enum ImageSize
    {
        LARGE(424, 424), 
        MEDIUM(208, 208), 
        SMALL(64, 64);
        
        private final int h;
        private final int w;
        
        private ImageSize(final int w, final int h) {
            this.w = w;
            this.h = h;
        }
    }
}
