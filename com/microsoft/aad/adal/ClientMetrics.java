package com.microsoft.aad.adal;

import java.net.*;
import java.util.*;
import android.text.*;

enum ClientMetrics
{
    private static final String CLIENT_METRICS_HEADER_LAST_ENDPOINT = "x-client-last-endpoint";
    private static final String CLIENT_METRICS_HEADER_LAST_ERROR = "x-client-last-error";
    private static final String CLIENT_METRICS_HEADER_LAST_REQUEST = "x-client-last-request";
    private static final String CLIENT_METRICS_HEADER_LAST_RESPONSE_TIME = "x-client-last-response-time";
    
    INSTANCE;
    
    private boolean mIsPending;
    private UUID mLastCorrelationId;
    private String mLastEndpoint;
    private String mLastError;
    private long mLastResponseTime;
    private URL mQueryUrl;
    private long mStartTimeMillis;
    
    private ClientMetrics() {
        this.mStartTimeMillis = 0L;
        this.mIsPending = false;
    }
    
    private void addClientMetricsHeadersToRequest(final Map<String, String> map) {
        final String mLastError = this.mLastError;
        if (mLastError != null) {
            map.put("x-client-last-error", mLastError);
        }
        final UUID mLastCorrelationId = this.mLastCorrelationId;
        if (mLastCorrelationId != null) {
            map.put("x-client-last-request", mLastCorrelationId.toString());
        }
        map.put("x-client-last-response-time", Long.toString(this.mLastResponseTime));
        map.put("x-client-last-endpoint", this.mLastEndpoint);
    }
    
    public void beginClientMetricsRecord(final URL mQueryUrl, final UUID mLastCorrelationId, final Map<String, String> map) {
        if (UrlExtensions.isADFSAuthority(mQueryUrl)) {
            this.mLastCorrelationId = null;
            return;
        }
        if (this.mIsPending) {
            this.addClientMetricsHeadersToRequest(map);
        }
        this.mStartTimeMillis = System.currentTimeMillis();
        this.mQueryUrl = mQueryUrl;
        this.mLastCorrelationId = mLastCorrelationId;
        this.mLastError = "";
        this.mIsPending = false;
    }
    
    public void endClientMetricsRecord(final String mLastEndpoint, final UUID mLastCorrelationId) {
        if (UrlExtensions.isADFSAuthority(this.mQueryUrl)) {
            return;
        }
        this.mLastEndpoint = mLastEndpoint;
        if (this.mStartTimeMillis != 0L) {
            this.mLastResponseTime = System.currentTimeMillis() - this.mStartTimeMillis;
            this.mLastCorrelationId = mLastCorrelationId;
        }
        this.mIsPending = true;
    }
    
    public void setLastError(String replaceAll) {
        final String s = "";
        if (replaceAll == null) {
            replaceAll = s;
        }
        else {
            replaceAll = replaceAll.replaceAll("[\\[\\]]", "");
        }
        this.mLastError = replaceAll;
    }
    
    public void setLastErrorCodes(final String[] array) {
        String join;
        if (array == null) {
            join = null;
        }
        else {
            join = TextUtils.join((CharSequence)",", (Object[])array);
        }
        this.mLastError = join;
    }
}
