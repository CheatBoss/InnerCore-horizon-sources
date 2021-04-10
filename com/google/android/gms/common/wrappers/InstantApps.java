package com.google.android.gms.common.wrappers;

import android.content.*;
import com.google.android.gms.common.util.*;

public class InstantApps
{
    private static Context zzaay;
    private static Boolean zzaaz;
    
    public static boolean isInstantApp(final Context context) {
        synchronized (InstantApps.class) {
            final Context applicationContext = context.getApplicationContext();
            Boolean b;
            if (InstantApps.zzaay != null && InstantApps.zzaaz != null && InstantApps.zzaay == applicationContext) {
                b = InstantApps.zzaaz;
            }
            else {
                InstantApps.zzaaz = null;
                Label_0095: {
                    Boolean zzaaz;
                    if (PlatformVersion.isAtLeastO()) {
                        zzaaz = applicationContext.getPackageManager().isInstantApp();
                    }
                    else {
                        try {
                            context.getClassLoader().loadClass("com.google.android.instantapps.supervisor.InstantAppsRuntime");
                            InstantApps.zzaaz = true;
                            break Label_0095;
                        }
                        catch (ClassNotFoundException ex) {
                            zzaaz = false;
                        }
                    }
                    InstantApps.zzaaz = zzaaz;
                }
                InstantApps.zzaay = applicationContext;
                b = InstantApps.zzaaz;
            }
            return b;
        }
    }
}
