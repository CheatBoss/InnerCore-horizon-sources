package com.appsflyer;

import com.google.firebase.messaging.*;
import com.appsflyer.internal.*;
import android.content.*;

public class FirebaseMessagingServiceListener extends FirebaseMessagingService
{
    @Override
    public void onNewToken(final String s) {
        super.onNewToken(s);
        final long currentTimeMillis = System.currentTimeMillis();
        if (s != null) {
            AFLogger.afInfoLog("Firebase Refreshed Token = ".concat(String.valueOf(s)));
            final c.c \u03b9 = c.c.\u03b9(AppsFlyerProperties.getInstance().getString("afUninstallToken"));
            final c.c c = new c.c(currentTimeMillis, s);
            if (\u03b9.\u0269(c)) {
                af.\u0131(((Context)this).getApplicationContext(), c.\u0269);
            }
        }
    }
}
