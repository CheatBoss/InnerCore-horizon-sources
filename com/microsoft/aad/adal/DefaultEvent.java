package com.microsoft.aad.adal;

import android.util.*;
import java.util.*;
import android.content.*;
import android.content.pm.*;
import android.provider.*;
import java.io.*;
import java.security.*;
import android.text.*;

class DefaultEvent implements IEvents
{
    private static String sApplicationName;
    private static String sApplicationVersion = "NA";
    private static String sClientId = "NA";
    private static String sDeviceId = "NA";
    private static int sEventListSize = 30;
    private int mDefaultEventCount;
    private final List<Pair<String, String>> mEventList;
    private String mRequestId;
    
    DefaultEvent() {
        this.mEventList = new ArrayList<Pair<String, String>>(DefaultEvent.sEventListSize);
        final String sApplicationName = DefaultEvent.sApplicationName;
        if (sApplicationName != null) {
            this.setProperty("Microsoft.ADAL.application_name", sApplicationName);
            this.setProperty("Microsoft.ADAL.application_version", DefaultEvent.sApplicationVersion);
            this.setProperty("Microsoft.ADAL.client_id", DefaultEvent.sClientId);
            this.setProperty("Microsoft.ADAL.device_id", DefaultEvent.sDeviceId);
            this.mDefaultEventCount = this.mEventList.size();
        }
    }
    
    static boolean isPrivacyCompliant(final String s) {
        return Telemetry.getAllowPii() || !TelemetryUtils.GDPR_FILTERED_FIELDS.contains(s);
    }
    
    @Override
    public int getDefaultEventCount() {
        return this.mDefaultEventCount;
    }
    
    List<Pair<String, String>> getEventList() {
        return this.mEventList;
    }
    
    @Override
    public List<Pair<String, String>> getEvents() {
        return Collections.unmodifiableList((List<? extends Pair<String, String>>)this.mEventList);
    }
    
    String getTelemetryRequestId() {
        return this.mRequestId;
    }
    
    @Override
    public void processEvent(final Map<String, String> map) {
        if (DefaultEvent.sApplicationName != null && isPrivacyCompliant("Microsoft.ADAL.application_name")) {
            map.put("Microsoft.ADAL.application_name", DefaultEvent.sApplicationName);
        }
        if (DefaultEvent.sApplicationVersion != null && isPrivacyCompliant("Microsoft.ADAL.application_version")) {
            map.put("Microsoft.ADAL.application_version", DefaultEvent.sApplicationVersion);
        }
        if (DefaultEvent.sClientId != null && isPrivacyCompliant("Microsoft.ADAL.client_id")) {
            map.put("Microsoft.ADAL.client_id", DefaultEvent.sClientId);
        }
        if (DefaultEvent.sDeviceId != null && isPrivacyCompliant("Microsoft.ADAL.device_id")) {
            map.put("Microsoft.ADAL.device_id", DefaultEvent.sDeviceId);
        }
    }
    
    void setCorrelationId(final String s) {
        this.mEventList.add(0, (Pair<String, String>)new Pair((Object)"Microsoft.ADAL.correlation_id", (Object)s));
        ++this.mDefaultEventCount;
    }
    
    void setDefaults(final Context context, final String sClientId) {
        DefaultEvent.sClientId = sClientId;
        DefaultEvent.sApplicationName = context.getPackageName();
        try {
            DefaultEvent.sApplicationVersion = context.getPackageManager().getPackageInfo(DefaultEvent.sApplicationName, 0).versionName;
        }
        catch (PackageManager$NameNotFoundException ex) {
            DefaultEvent.sApplicationVersion = "NA";
        }
        try {
            DefaultEvent.sDeviceId = StringExtensions.createHash(Settings$Secure.getString(context.getContentResolver(), "android_id"));
        }
        catch (UnsupportedEncodingException | NoSuchAlgorithmException ex2) {
            DefaultEvent.sDeviceId = "";
        }
        if (this.mDefaultEventCount == 0) {
            this.setProperty("Microsoft.ADAL.application_name", DefaultEvent.sApplicationName);
            this.setProperty("Microsoft.ADAL.application_version", DefaultEvent.sApplicationVersion);
            this.setProperty("Microsoft.ADAL.client_id", DefaultEvent.sClientId);
            this.setProperty("Microsoft.ADAL.device_id", DefaultEvent.sDeviceId);
            this.mDefaultEventCount = this.mEventList.size();
        }
    }
    
    @Override
    public void setProperty(final String s, final String s2) {
        if (!TextUtils.isEmpty((CharSequence)s)) {
            if (s2 != null) {
                if (!isPrivacyCompliant(s)) {
                    return;
                }
                this.mEventList.add((Pair<String, String>)Pair.create((Object)s, (Object)s2));
            }
            return;
        }
        throw new IllegalArgumentException("Telemetry setProperty on null name");
    }
    
    void setRequestId(final String mRequestId) {
        this.mRequestId = mRequestId;
        this.mEventList.add(0, (Pair<String, String>)new Pair((Object)"Microsoft.ADAL.request_id", (Object)mRequestId));
        ++this.mDefaultEventCount;
    }
}
