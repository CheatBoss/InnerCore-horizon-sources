package com.microsoft.aad.adal;

import android.util.*;
import java.util.*;
import java.net.*;

final class HttpEvent extends DefaultEvent
{
    private static final String TAG;
    
    static {
        TAG = HttpEvent.class.getSimpleName();
    }
    
    HttpEvent(final String s) {
        this.getEventList().add((Pair<String, String>)Pair.create((Object)"Microsoft.ADAL.event_name", (Object)s));
    }
    
    @Override
    public void processEvent(final Map<String, String> map) {
        final String s = map.get("Microsoft.ADAL.http_event_count");
        if (s == null) {
            map.put("Microsoft.ADAL.http_event_count", "1");
        }
        else {
            map.put("Microsoft.ADAL.http_event_count", Integer.toString(Integer.parseInt(s) + 1));
        }
        if (map.containsKey("Microsoft.ADAL.response_code")) {
            map.put("Microsoft.ADAL.response_code", "");
        }
        if (map.containsKey("Microsoft.ADAL.oauth_error_code")) {
            map.put("Microsoft.ADAL.oauth_error_code", "");
        }
        if (map.containsKey("Microsoft.ADAL.http_path")) {
            map.put("Microsoft.ADAL.http_path", "");
        }
        if (map.containsKey("Microsoft.ADAL.x_ms_request_id")) {
            map.put("Microsoft.ADAL.x_ms_request_id", "");
        }
        if (map.containsKey("Microsoft.ADAL.server_error_code")) {
            map.remove("Microsoft.ADAL.server_error_code");
        }
        if (map.containsKey("Microsoft.ADAL.server_sub_error_code")) {
            map.remove("Microsoft.ADAL.server_sub_error_code");
        }
        if (map.containsKey("Microsoft.ADAL.rt_age")) {
            map.remove("Microsoft.ADAL.rt_age");
        }
        if (map.containsKey("Microsoft.ADAL.spe_info")) {
            map.remove("Microsoft.ADAL.spe_info");
        }
        for (final Pair<String, String> pair : this.getEventList()) {
            final String s2 = (String)pair.first;
            if (s2.equals("Microsoft.ADAL.response_code") || s2.equals("Microsoft.ADAL.x_ms_request_id") || s2.equals("Microsoft.ADAL.oauth_error_code") || s2.equals("Microsoft.ADAL.http_path") || s2.equals("Microsoft.ADAL.server_error_code") || s2.equals("Microsoft.ADAL.server_sub_error_code") || s2.equals("Microsoft.ADAL.rt_age") || s2.equals("Microsoft.ADAL.spe_info")) {
                map.put(s2, (String)pair.second);
            }
        }
    }
    
    void setApiVersion(final String s) {
        this.setProperty("Microsoft.ADAL.api_version", s);
    }
    
    void setHttpPath(final URL url) {
        final String authority = url.getAuthority();
        if (!Discovery.getValidHosts().contains(authority)) {
            return;
        }
        final String[] split = url.getPath().split("/");
        final StringBuilder sb = new StringBuilder();
        sb.append(url.getProtocol());
        sb.append("://");
        sb.append(authority);
        sb.append("/");
        for (int i = 2; i < split.length; ++i) {
            sb.append(split[i]);
            sb.append("/");
        }
        this.setProperty("Microsoft.ADAL.http_path", sb.toString());
    }
    
    void setMethod(final String s) {
        this.setProperty("Microsoft.ADAL.method", s);
    }
    
    void setOauthErrorCode(final String s) {
        this.setProperty("Microsoft.ADAL.oauth_error_code", s);
    }
    
    void setQueryParameters(final String s) {
        this.setProperty("Microsoft.ADAL.query_params", s);
    }
    
    void setRefreshTokenAge(final String s) {
        if (!StringExtensions.isNullOrBlank(s)) {
            this.setProperty("Microsoft.ADAL.rt_age", s.trim());
        }
    }
    
    void setRequestIdHeader(final String s) {
        this.setProperty("Microsoft.ADAL.x_ms_request_id", s);
    }
    
    void setResponseCode(final int n) {
        this.setProperty("Microsoft.ADAL.response_code", String.valueOf(n));
    }
    
    void setServerErrorCode(final String s) {
        if (!StringExtensions.isNullOrBlank(s) && !s.equals("0")) {
            this.setProperty("Microsoft.ADAL.server_error_code", s.trim());
        }
    }
    
    void setServerSubErrorCode(final String s) {
        if (!StringExtensions.isNullOrBlank(s) && !s.equals("0")) {
            this.setProperty("Microsoft.ADAL.server_sub_error_code", s.trim());
        }
    }
    
    void setSpeRing(final String s) {
        if (!StringExtensions.isNullOrBlank(s)) {
            this.setProperty("Microsoft.ADAL.spe_info", s.trim());
        }
    }
    
    void setUserAgent(final String s) {
        this.setProperty("Microsoft.ADAL.user_agent", s);
    }
    
    void setXMsCliTelemData(final TelemetryUtils.CliTelemInfo cliTelemInfo) {
        if (cliTelemInfo == null) {
            return;
        }
        this.setServerErrorCode(cliTelemInfo.getServerErrorCode());
        this.setServerSubErrorCode(cliTelemInfo.getServerSubErrorCode());
        this.setRefreshTokenAge(cliTelemInfo.getRefreshTokenAge());
        this.setSpeRing(cliTelemInfo.getSpeRing());
    }
}
