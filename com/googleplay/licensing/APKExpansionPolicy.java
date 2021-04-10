package com.googleplay.licensing;

import android.content.*;
import org.apache.http.client.utils.*;
import org.apache.http.*;
import android.util.*;
import java.net.*;
import java.util.*;

public class APKExpansionPolicy implements Policy
{
    private static final String DEFAULT_MAX_RETRIES = "0";
    private static final String DEFAULT_RETRY_COUNT = "0";
    private static final String DEFAULT_RETRY_UNTIL = "0";
    private static final String DEFAULT_VALIDITY_TIMESTAMP = "0";
    public static final int MAIN_FILE_URL_INDEX = 0;
    private static final long MILLIS_PER_MINUTE = 60000L;
    public static final int PATCH_FILE_URL_INDEX = 1;
    private static final String PREFS_FILE = "com.android.vending.licensing.APKExpansionPolicy";
    private static final String PREF_LAST_RESPONSE = "lastResponse";
    private static final String PREF_MAX_RETRIES = "maxRetries";
    private static final String PREF_RETRY_COUNT = "retryCount";
    private static final String PREF_RETRY_UNTIL = "retryUntil";
    private static final String PREF_VALIDITY_TIMESTAMP = "validityTimestamp";
    private static final String TAG = "APKExpansionPolicy";
    private Vector<String> mExpansionFileNames;
    private Vector<Long> mExpansionFileSizes;
    private Vector<String> mExpansionURLs;
    private int mLastResponse;
    private long mLastResponseTime;
    private long mMaxRetries;
    private PreferenceObfuscator mPreferences;
    private long mRetryCount;
    private long mRetryUntil;
    private long mValidityTimestamp;
    
    public APKExpansionPolicy(final Context context, final Obfuscator obfuscator) {
        this.mLastResponseTime = 0L;
        this.mExpansionURLs = new Vector<String>();
        this.mExpansionFileNames = new Vector<String>();
        this.mExpansionFileSizes = new Vector<Long>();
        final PreferenceObfuscator mPreferences = new PreferenceObfuscator(context.getSharedPreferences("com.android.vending.licensing.APKExpansionPolicy", 0), obfuscator);
        this.mPreferences = mPreferences;
        this.mLastResponse = Integer.parseInt(mPreferences.getString("lastResponse", Integer.toString(291)));
        this.mValidityTimestamp = Long.parseLong(this.mPreferences.getString("validityTimestamp", "0"));
        this.mRetryUntil = Long.parseLong(this.mPreferences.getString("retryUntil", "0"));
        this.mMaxRetries = Long.parseLong(this.mPreferences.getString("maxRetries", "0"));
        this.mRetryCount = Long.parseLong(this.mPreferences.getString("retryCount", "0"));
    }
    
    private Map<String, String> decodeExtras(String s) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("?");
            sb.append(s);
            for (final NameValuePair nameValuePair : URLEncodedUtils.parse(new URI(sb.toString()), "UTF-8")) {
                s = nameValuePair.getName();
                int n = 0;
                while (hashMap.containsKey(s)) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(nameValuePair.getName());
                    ++n;
                    sb2.append(n);
                    s = sb2.toString();
                }
                hashMap.put(s, nameValuePair.getValue());
            }
        }
        catch (URISyntaxException ex) {
            Log.w("APKExpansionPolicy", "Invalid syntax error while decoding extras data from server.");
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
        }
        catch (NumberFormatException ex) {
            Log.w("APKExpansionPolicy", "Licence retry count (GR) missing, grace period disabled");
            n = 0L;
            s = "0";
        }
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
        }
        catch (NumberFormatException ex) {
            Log.w("APKExpansionPolicy", "License retry timestamp (GT) missing, grace period disabled");
            n = 0L;
            s = "0";
        }
        this.mRetryUntil = n;
        this.mPreferences.putString("retryUntil", s);
    }
    
    private void setValidityTimestamp(String string) {
        Long n;
        try {
            n = Long.parseLong(string);
        }
        catch (NumberFormatException ex) {
            Log.w("APKExpansionPolicy", "License validity timestamp (VT) missing, caching for a minute");
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
    
    public String getExpansionFileName(final int n) {
        if (n < this.mExpansionFileNames.size()) {
            return this.mExpansionFileNames.elementAt(n);
        }
        return null;
    }
    
    public long getExpansionFileSize(final int n) {
        if (n < this.mExpansionFileSizes.size()) {
            return this.mExpansionFileSizes.elementAt(n);
        }
        return -1L;
    }
    
    public String getExpansionURL(final int n) {
        if (n < this.mExpansionURLs.size()) {
            return this.mExpansionURLs.elementAt(n);
        }
        return null;
    }
    
    public int getExpansionURLCount() {
        return this.mExpansionURLs.size();
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
        if (n != 291) {
            this.setRetryCount(0L);
        }
        else {
            this.setRetryCount(this.mRetryCount + 1L);
        }
        if (n == 256) {
            final Map<String, String> decodeExtras = this.decodeExtras(responseData.extra);
            this.mLastResponse = n;
            this.setValidityTimestamp(Long.toString(System.currentTimeMillis() + 60000L));
            for (final String s : decodeExtras.keySet()) {
                if (s.equals("VT")) {
                    this.setValidityTimestamp(decodeExtras.get(s));
                }
                else if (s.equals("GT")) {
                    this.setRetryUntil(decodeExtras.get(s));
                }
                else if (s.equals("GR")) {
                    this.setMaxRetries(decodeExtras.get(s));
                }
                else if (s.startsWith("FILE_URL")) {
                    this.setExpansionURL(Integer.parseInt(s.substring(8)) - 1, decodeExtras.get(s));
                }
                else if (s.startsWith("FILE_NAME")) {
                    this.setExpansionFileName(Integer.parseInt(s.substring(9)) - 1, decodeExtras.get(s));
                }
                else {
                    if (!s.startsWith("FILE_SIZE")) {
                        continue;
                    }
                    this.setExpansionFileSize(Integer.parseInt(s.substring(9)) - 1, Long.parseLong(decodeExtras.get(s)));
                }
            }
        }
        else if (n == 561) {
            this.setValidityTimestamp("0");
            this.setRetryUntil("0");
            this.setMaxRetries("0");
        }
        this.setLastResponse(n);
        this.mPreferences.commit();
    }
    
    public void resetPolicy() {
        this.mPreferences.putString("lastResponse", Integer.toString(291));
        this.setRetryUntil("0");
        this.setMaxRetries("0");
        this.setRetryCount(Long.parseLong("0"));
        this.setValidityTimestamp("0");
        this.mPreferences.commit();
    }
    
    public void setExpansionFileName(final int n, final String s) {
        if (n >= this.mExpansionFileNames.size()) {
            this.mExpansionFileNames.setSize(n + 1);
        }
        this.mExpansionFileNames.set(n, s);
    }
    
    public void setExpansionFileSize(final int n, final long n2) {
        if (n >= this.mExpansionFileSizes.size()) {
            this.mExpansionFileSizes.setSize(n + 1);
        }
        this.mExpansionFileSizes.set(n, n2);
    }
    
    public void setExpansionURL(final int n, final String s) {
        if (n >= this.mExpansionURLs.size()) {
            this.mExpansionURLs.setSize(n + 1);
        }
        this.mExpansionURLs.set(n, s);
    }
}
