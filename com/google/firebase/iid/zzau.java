package com.google.firebase.iid;

import android.support.v4.util.*;
import java.util.*;
import android.app.*;
import android.os.*;
import android.util.*;
import android.support.v4.content.*;
import android.content.*;
import android.content.pm.*;

public final class zzau
{
    private static zzau zzcu;
    private final SimpleArrayMap<String, String> zzcv;
    private Boolean zzcw;
    final Queue<Intent> zzcx;
    private final Queue<Intent> zzcy;
    
    private zzau() {
        this.zzcv = new SimpleArrayMap<String, String>();
        this.zzcw = null;
        this.zzcx = new ArrayDeque<Intent>();
        this.zzcy = new ArrayDeque<Intent>();
    }
    
    public static PendingIntent zza(final Context context, final int n, final Intent intent, final int n2) {
        return PendingIntent.getBroadcast(context, n, zza(context, "com.google.firebase.MESSAGING_EVENT", intent), 1073741824);
    }
    
    private static Intent zza(final Context context, final String action, final Intent intent) {
        final Intent intent2 = new Intent(context, (Class)FirebaseInstanceIdReceiver.class);
        intent2.setAction(action);
        intent2.putExtra("wrapped_intent", (Parcelable)intent);
        return intent2;
    }
    
    public static zzau zzah() {
        synchronized (zzau.class) {
            if (zzau.zzcu == null) {
                zzau.zzcu = new zzau();
            }
            return zzau.zzcu;
        }
    }
    
    public static void zzb(final Context context, final Intent intent) {
        context.sendBroadcast(zza(context, "com.google.firebase.INSTANCE_ID_EVENT", intent));
    }
    
    public static void zzc(final Context context, final Intent intent) {
        context.sendBroadcast(zza(context, "com.google.firebase.MESSAGING_EVENT", intent));
    }
    
    private final int zzd(final Context context, final Intent intent) {
        Object o = this.zzcv;
        synchronized (o) {
            final String s = this.zzcv.get(intent.getAction());
            // monitorexit(o)
            boolean b = false;
            o = s;
            Label_0368: {
                Label_0302: {
                    if (s == null) {
                        o = context.getPackageManager().resolveService(intent, 0);
                        if (o != null && ((ResolveInfo)o).serviceInfo != null) {
                            final ServiceInfo serviceInfo = ((ResolveInfo)o).serviceInfo;
                            if (context.getPackageName().equals(serviceInfo.packageName)) {
                                if (serviceInfo.name != null) {
                                    final String s2 = (String)(o = serviceInfo.name);
                                    if (s2.startsWith(".")) {
                                        o = String.valueOf(context.getPackageName());
                                        final String value = String.valueOf(s2);
                                        if (value.length() != 0) {
                                            o = ((String)o).concat(value);
                                        }
                                        else {
                                            o = new String((String)o);
                                        }
                                    }
                                    synchronized (this.zzcv) {
                                        this.zzcv.put(intent.getAction(), (String)o);
                                        break Label_0302;
                                    }
                                }
                            }
                            o = serviceInfo.packageName;
                            final String name = serviceInfo.name;
                            final StringBuilder sb = new StringBuilder(String.valueOf(o).length() + 94 + String.valueOf(name).length());
                            sb.append("Error resolving target intent service, skipping classname enforcement. Resolved service was: ");
                            sb.append((String)o);
                            sb.append("/");
                            sb.append(name);
                            o = sb.toString();
                        }
                        else {
                            o = "Failed to resolve target intent service, skipping classname enforcement";
                        }
                        Log.e("FirebaseInstanceId", (String)o);
                        break Label_0368;
                    }
                }
                if (Log.isLoggable("FirebaseInstanceId", 3)) {
                    final String value2 = String.valueOf(o);
                    String concat;
                    if (value2.length() != 0) {
                        concat = "Restricting intent to a specific service: ".concat(value2);
                    }
                    else {
                        concat = new String("Restricting intent to a specific service: ");
                    }
                    Log.d("FirebaseInstanceId", concat);
                }
                intent.setClassName(context.getPackageName(), (String)o);
                try {
                    if (this.zzcw == null) {
                        if (context.checkCallingOrSelfPermission("android.permission.WAKE_LOCK") == 0) {
                            b = true;
                        }
                        this.zzcw = b;
                    }
                    ComponentName componentName;
                    if (this.zzcw) {
                        componentName = WakefulBroadcastReceiver.startWakefulService(context, intent);
                    }
                    else {
                        componentName = context.startService(intent);
                        Log.d("FirebaseInstanceId", "Missing wake lock permission, service start may be delayed");
                    }
                    if (componentName == null) {
                        Log.e("FirebaseInstanceId", "Error while delivering the message: ServiceIntent not found.");
                        return 404;
                    }
                    return -1;
                }
                catch (IllegalStateException ex) {
                    final String value3 = String.valueOf(ex);
                    final StringBuilder sb2 = new StringBuilder(String.valueOf(value3).length() + 45);
                    sb2.append("Failed to start service while in background: ");
                    sb2.append(value3);
                    Log.e("FirebaseInstanceId", sb2.toString());
                    return 402;
                }
                catch (SecurityException ex2) {
                    Log.e("FirebaseInstanceId", "Error while delivering the message to the serviceIntent", (Throwable)ex2);
                    return 401;
                }
            }
        }
    }
    
    public final Intent zzai() {
        return this.zzcy.poll();
    }
    
    public final int zzb(final Context context, final String s, final Intent intent) {
        final int hashCode = s.hashCode();
        int n = 0;
        Label_0056: {
            if (hashCode != -842411455) {
                if (hashCode == 41532704) {
                    if (s.equals("com.google.firebase.MESSAGING_EVENT")) {
                        n = 1;
                        break Label_0056;
                    }
                }
            }
            else if (s.equals("com.google.firebase.INSTANCE_ID_EVENT")) {
                n = 0;
                break Label_0056;
            }
            n = -1;
        }
        Queue<Intent> queue;
        if (n != 0) {
            if (n != 1) {
                final String value = String.valueOf(s);
                String concat;
                if (value.length() != 0) {
                    concat = "Unknown service action: ".concat(value);
                }
                else {
                    concat = new String("Unknown service action: ");
                }
                Log.w("FirebaseInstanceId", concat);
                return 500;
            }
            queue = this.zzcy;
        }
        else {
            queue = this.zzcx;
        }
        queue.offer(intent);
        final Intent intent2 = new Intent(s);
        intent2.setPackage(context.getPackageName());
        return this.zzd(context, intent2);
    }
}
