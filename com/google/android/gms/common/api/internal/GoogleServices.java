package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.*;
import android.content.*;
import com.google.android.gms.common.*;
import android.text.*;
import android.content.res.*;
import com.google.android.gms.common.internal.*;

@Deprecated
public final class GoogleServices
{
    private static final Object sLock;
    private static GoogleServices zzku;
    private final String zzkv;
    private final Status zzkw;
    private final boolean zzkx;
    private final boolean zzky;
    
    static {
        sLock = new Object();
    }
    
    GoogleServices(final Context context) {
        final Resources resources = context.getResources();
        final int identifier = resources.getIdentifier("google_app_measurement_enable", "integer", resources.getResourcePackageName(R$string.common_google_play_services_unknown_issue));
        final boolean b = true;
        boolean zzkx = true;
        if (identifier != 0) {
            if (resources.getInteger(identifier) == 0) {
                zzkx = false;
            }
            this.zzky = (zzkx ^ true);
        }
        else {
            this.zzky = false;
            zzkx = b;
        }
        this.zzkx = zzkx;
        String zzkv;
        if ((zzkv = MetadataValueReader.getGoogleAppId(context)) == null) {
            zzkv = new StringResourceValueReader(context).getString("google_app_id");
        }
        if (TextUtils.isEmpty((CharSequence)zzkv)) {
            this.zzkw = new Status(10, "Missing google app id value from from string resources with name google_app_id.");
            this.zzkv = null;
            return;
        }
        this.zzkv = zzkv;
        this.zzkw = Status.RESULT_SUCCESS;
    }
    
    private static GoogleServices checkInitialized(final String s) {
        synchronized (GoogleServices.sLock) {
            if (GoogleServices.zzku != null) {
                return GoogleServices.zzku;
            }
            final StringBuilder sb = new StringBuilder(String.valueOf(s).length() + 34);
            sb.append("Initialize must be called before ");
            sb.append(s);
            sb.append(".");
            throw new IllegalStateException(sb.toString());
        }
    }
    
    public static String getGoogleAppId() {
        return checkInitialized("getGoogleAppId").zzkv;
    }
    
    public static Status initialize(final Context context) {
        Preconditions.checkNotNull(context, "Context must not be null.");
        synchronized (GoogleServices.sLock) {
            if (GoogleServices.zzku == null) {
                GoogleServices.zzku = new GoogleServices(context);
            }
            return GoogleServices.zzku.zzkw;
        }
    }
    
    public static boolean isMeasurementExplicitlyDisabled() {
        return checkInitialized("isMeasurementExplicitlyDisabled").zzky;
    }
}
