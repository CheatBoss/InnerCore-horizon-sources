package com.google.android.gms.common;

import android.content.*;
import android.content.res.*;

public final class GooglePlayServicesUtil extends GooglePlayServicesUtilLight
{
    @Deprecated
    public static final int GOOGLE_PLAY_SERVICES_VERSION_CODE;
    
    static {
        GOOGLE_PLAY_SERVICES_VERSION_CODE = GooglePlayServicesUtilLight.GOOGLE_PLAY_SERVICES_VERSION_CODE;
    }
    
    public static Resources getRemoteResource(final Context context) {
        return GooglePlayServicesUtilLight.getRemoteResource(context);
    }
}
