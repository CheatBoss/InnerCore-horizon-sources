package com.googleplay.licensing;

import android.content.*;
import org.apache.http.client.utils.*;
import org.apache.http.*;
import android.util.*;
import java.net.*;
import java.util.*;

public class ServerManagedPolicy implements Policy
{
    private static final String DEFAULT_MAX_RETRIES = "0";
    private static final String DEFAULT_RETRY_COUNT = "0";
    private static final String DEFAULT_RETRY_UNTIL = "0";
    private static final String DEFAULT_VALIDITY_TIMESTAMP = "0";
    private static final long MILLIS_PER_MINUTE = 60000L;
    private static final String PREFS_FILE = "com.android.vending.licensing.ServerManagedPolicy";
    private static final String PREF_LAST_RESPONSE = "lastResponse";
    private static final String PREF_MAX_RETRIES = "maxRetries";
    private static final String PREF_RETRY_COUNT = "retryCount";
    private static final String PREF_RETRY_UNTIL = "retryUntil";
    private static final String PREF_VALIDITY_TIMESTAMP = "validityTimestamp";
    private static final String TAG = "ServerManagedPolicy";
    private static boolean isServerResponse;
    private int mLastResponse;
    private long mLastResponseTime;
    private long mMaxRetries;
    private long mOriginalGT;
    private long mOriginalRetries;
    private long mOriginalVT;
    private PreferenceObfuscator mPreferences;
    private long mRetryCount;
    private long mRetryUntil;
    private long mValidityTimestamp;
    
    public ServerManagedPolicy(final Context context, final Obfuscator obfuscator) {
        this.mOriginalVT = 60000L;
        this.mOriginalGT = 0L;
        this.mOriginalRetries = 0L;
        this.mLastResponseTime = 0L;
        final PreferenceObfuscator mPreferences = new PreferenceObfuscator(context.getSharedPreferences("com.android.vending.licensing.ServerManagedPolicy", 0), obfuscator);
        this.mPreferences = mPreferences;
        this.mLastResponse = Integer.parseInt(mPreferences.getString("lastResponse", Integer.toString(291)));
        this.mValidityTimestamp = Long.parseLong(this.mPreferences.getString("validityTimestamp", "0"));
        this.mRetryUntil = Long.parseLong(this.mPreferences.getString("retryUntil", "0"));
        this.mMaxRetries = Long.parseLong(this.mPreferences.getString("maxRetries", "0"));
        this.mRetryCount = Long.parseLong(this.mPreferences.getString("retryCount", "0"));
    }
    
    private Map<String, String> decodeExtras(final String s) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("?");
            sb.append(s);
            for (final NameValuePair nameValuePair : URLEncodedUtils.parse(new URI(sb.toString()), "UTF-8")) {
                hashMap.put(nameValuePair.getName(), nameValuePair.getValue());
            }
        }
        catch (URISyntaxException ex) {
            Log.w("ServerManagedPolicy", "Invalid syntax error while decoding extras data from server.");
        }
        return hashMap;
    }
    
    private void setLastResponse(final int mLastResponse) {
        this.mLastResponseTime = System.currentTimeMillis();
        this.mLastResponse = mLastResponse;
        this.mPreferences.putString("lastResponse", Integer.toString(mLastResponse));
    }
    
    private void setMaxRetries(String s) {
        Long n;
        try {
            n = Long.parseLong(s);
            this.mOriginalRetries = n;
        }
        catch (NumberFormatException ex) {
            Log.w("ServerManagedPolicy", "Licence retry count (GR) missing, grace period disabled");
            n = 0L;
            s = "0";
        }
        Log.i("ServerManagedPolicy", String.format("license check retries = %d", n));
        this.mMaxRetries = n;
        this.mPreferences.putString("maxRetries", s);
    }
    
    private void setRetryCount(final long mRetryCount) {
        this.mRetryCount = mRetryCount;
        this.mPreferences.putString("retryCount", Long.toString(mRetryCount));
    }
    
    private void setRetryUntil(String s) {
        Long n;
        try {
            n = Long.parseLong(s);
            this.mOriginalGT = n - System.currentTimeMillis();
        }
        catch (NumberFormatException ex) {
            Log.w("ServerManagedPolicy", "License retry timestamp (GT) missing, grace period disabled");
            n = 0L;
            s = "0";
        }
        Log.i("ServerManagedPolicy", String.format("license retry until timestamp = %d", n));
        this.mRetryUntil = n;
        this.mPreferences.putString("retryUntil", s);
    }
    
    private void setValidityTimestamp(String string) {
        Long n;
        try {
            n = Long.parseLong(string);
            this.mOriginalVT = n - System.currentTimeMillis();
        }
        catch (NumberFormatException ex) {
            Log.w("ServerManagedPolicy", "License validity timestamp (VT) missing, caching for a minute");
            n = System.currentTimeMillis() + 60000L;
            string = Long.toString(n);
        }
        this.mValidityTimestamp = n;
        this.mPreferences.putString("validityTimestamp", string);
    }
    
    @Override
    public boolean allowAccess() {
        final long currentTimeMillis = System.currentTimeMillis();
        final int mLastResponse = this.mLastResponse;
        final boolean b = true;
        boolean b2;
        if (mLastResponse == 256) {
            b2 = b;
            if (currentTimeMillis <= this.mValidityTimestamp) {
                String s;
                if (ServerManagedPolicy.isServerResponse) {
                    s = "Server license response";
                }
                else {
                    s = "Cached license response";
                }
                Log.i("ServerManagedPolicy", s);
                return true;
            }
        }
        else {
            b2 = b;
            if (mLastResponse == 291) {
                b2 = b;
                if (currentTimeMillis < this.mLastResponseTime + 60000L) {
                    b2 = b;
                    if (currentTimeMillis > this.mRetryUntil) {
                        if (this.mRetryCount <= this.mMaxRetries) {
                            return true;
                        }
                        b2 = false;
                    }
                }
            }
        }
        return b2;
    }
    
    public long[] getExtraLicenseData() {
        return new long[] { this.mOriginalVT, this.mOriginalGT, this.mOriginalRetries };
    }
    
    public long getMaxRetries() {
        return this.mMaxRetries;
    }
    
    public long getRetryCount() {
        return this.mRetryCount;
    }
    
    public long getRetryUntil() {
        return this.mRetryUntil;
    }
    
    public long getValidityTimestamp() {
        return this.mValidityTimestamp;
    }
    
    @Override
    public void processServerResponse(final int n, final ResponseData responseData) {
        ServerManagedPolicy.isServerResponse = true;
        if (n != 291) {
            this.setRetryCount(0L);
        }
        else {
            this.setRetryCount(this.mRetryCount + 1L);
        }
        Label_0128: {
            String maxRetries;
            if (n == 256) {
                final Map<String, String> decodeExtras = this.decodeExtras(responseData.extra);
                this.mLastResponse = n;
                this.setValidityTimestamp(decodeExtras.get("VT"));
                this.setRetryUntil(decodeExtras.get("GT"));
                maxRetries = decodeExtras.get("GR");
            }
            else {
                if (n != 561) {
                    break Label_0128;
                }
                this.setValidityTimestamp("0");
                this.setRetryUntil("0");
                maxRetries = "0";
            }
            this.setMaxRetries(maxRetries);
        }
        this.setLastResponse(n);
        this.mPreferences.commit();
    }
}
