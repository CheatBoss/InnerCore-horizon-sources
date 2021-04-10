package com.google.firebase.iid;

import android.support.v4.content.*;
import com.google.android.gms.common.util.*;
import android.content.*;
import android.util.*;
import android.os.*;

public final class FirebaseInstanceIdReceiver extends WakefulBroadcastReceiver
{
    private static boolean zzbc;
    private static zzh zzbd;
    private static zzh zzbe;
    
    public static int zza(final BroadcastReceiver broadcastReceiver, final Context context, final String s, final Intent intent) {
        if (Log.isLoggable("FirebaseInstanceId", 3)) {
            final String value = String.valueOf(s);
            String concat;
            if (value.length() != 0) {
                concat = "Starting service: ".concat(value);
            }
            else {
                concat = new String("Starting service: ");
            }
            Log.d("FirebaseInstanceId", concat);
        }
        if (PlatformVersion.isAtLeastO() && context.getApplicationInfo().targetSdkVersion >= 26) {
            if (broadcastReceiver.isOrderedBroadcast()) {
                broadcastReceiver.setResultCode(-1);
            }
            zza(context, s).zza(intent, broadcastReceiver.goAsync());
            return -1;
        }
        return zzau.zzah().zzb(context, s, intent);
    }
    
    private static zzh zza(final Context context, final String s) {
        synchronized (FirebaseInstanceIdReceiver.class) {
            zzh zzh;
            if ("com.google.firebase.MESSAGING_EVENT".equals(s)) {
                if (FirebaseInstanceIdReceiver.zzbe == null) {
                    FirebaseInstanceIdReceiver.zzbe = new zzh(context, s);
                }
                zzh = FirebaseInstanceIdReceiver.zzbe;
            }
            else {
                if (FirebaseInstanceIdReceiver.zzbd == null) {
                    FirebaseInstanceIdReceiver.zzbd = new zzh(context, s);
                }
                zzh = FirebaseInstanceIdReceiver.zzbd;
            }
            return zzh;
        }
    }
    
    private final void zza(final Context context, final Intent intent, String s) {
        final String s2 = null;
        intent.setComponent((ComponentName)null);
        intent.setPackage(context.getPackageName());
        if (Build$VERSION.SDK_INT <= 18) {
            intent.removeCategory(context.getPackageName());
        }
        final String stringExtra = intent.getStringExtra("gcm.rawData64");
        if (stringExtra != null) {
            intent.putExtra("rawData", Base64.decode(stringExtra, 0));
            intent.removeExtra("gcm.rawData64");
        }
        if (!"google.com/iid".equals(intent.getStringExtra("from")) && !"com.google.firebase.INSTANCE_ID_EVENT".equals(s)) {
            if (!"com.google.android.c2dm.intent.RECEIVE".equals(s) && !"com.google.firebase.MESSAGING_EVENT".equals(s)) {
                Log.d("FirebaseInstanceId", "Unexpected intent");
                s = s2;
            }
            else {
                s = "com.google.firebase.MESSAGING_EVENT";
            }
        }
        else {
            s = "com.google.firebase.INSTANCE_ID_EVENT";
        }
        int zza = -1;
        if (s != null) {
            zza = zza(this, context, s, intent);
        }
        if (this.isOrderedBroadcast()) {
            this.setResultCode(zza);
        }
    }
    
    public final void onReceive(final Context context, final Intent intent) {
        if (intent == null) {
            return;
        }
        final Parcelable parcelableExtra = intent.getParcelableExtra("wrapped_intent");
        Intent intent2;
        if (parcelableExtra instanceof Intent) {
            intent2 = (Intent)parcelableExtra;
        }
        else {
            intent2 = null;
        }
        if (intent2 != null) {
            this.zza(context, intent2, intent.getAction());
            return;
        }
        this.zza(context, intent, intent.getAction());
    }
}
