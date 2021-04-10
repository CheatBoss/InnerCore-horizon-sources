package com.android.installreferrer.api;

import android.os.*;

public class ReferrerDetails
{
    private static final String KEY_INSTALL_BEGIN_TIMESTAMP = "install_begin_timestamp_seconds";
    private static final String KEY_INSTALL_REFERRER = "install_referrer";
    private static final String KEY_REFERRER_CLICK_TIMESTAMP = "referrer_click_timestamp_seconds";
    private final Bundle mOriginalBundle;
    
    public ReferrerDetails(final Bundle mOriginalBundle) {
        this.mOriginalBundle = mOriginalBundle;
    }
    
    public long getInstallBeginTimestampSeconds() {
        return this.mOriginalBundle.getLong("install_begin_timestamp_seconds");
    }
    
    public String getInstallReferrer() {
        return this.mOriginalBundle.getString("install_referrer");
    }
    
    public long getReferrerClickTimestampSeconds() {
        return this.mOriginalBundle.getLong("referrer_click_timestamp_seconds");
    }
}
