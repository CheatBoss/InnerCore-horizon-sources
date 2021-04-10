package com.google.firebase.iid;

import android.content.*;
import android.util.*;

@Deprecated
public class FirebaseInstanceIdService extends zzb
{
    @Deprecated
    public void onTokenRefresh() {
    }
    
    @Override
    protected final Intent zzb(final Intent intent) {
        return zzau.zzah().zzcx.poll();
    }
    
    @Override
    public final void zzd(final Intent intent) {
        if ("com.google.firebase.iid.TOKEN_REFRESH".equals(intent.getAction())) {
            this.onTokenRefresh();
            return;
        }
        final String stringExtra = intent.getStringExtra("CMD");
        if (stringExtra != null) {
            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                final String value = String.valueOf(intent.getExtras());
                final StringBuilder sb = new StringBuilder(String.valueOf(stringExtra).length() + 21 + String.valueOf(value).length());
                sb.append("Received command: ");
                sb.append(stringExtra);
                sb.append(" - ");
                sb.append(value);
                Log.d("FirebaseInstanceId", sb.toString());
            }
            if (!"RST".equals(stringExtra) && !"RST_FULL".equals(stringExtra)) {
                if ("SYNC".equals(stringExtra)) {
                    FirebaseInstanceId.getInstance().zzp();
                }
            }
            else {
                FirebaseInstanceId.getInstance().zzl();
            }
        }
    }
}
