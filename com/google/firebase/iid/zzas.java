package com.google.firebase.iid;

import android.app.*;
import android.support.v4.util.*;
import android.content.*;
import android.util.*;
import java.util.regex.*;
import java.io.*;
import android.os.*;
import java.util.concurrent.*;
import com.google.android.gms.tasks.*;

final class zzas
{
    private static int zzcc;
    private static PendingIntent zzco;
    private final zzam zzak;
    private final SimpleArrayMap<String, TaskCompletionSource<Bundle>> zzcp;
    private Messenger zzcq;
    private Messenger zzcr;
    private zzk zzcs;
    private final Context zzv;
    
    public zzas(final Context zzv, final zzam zzak) {
        this.zzcp = new SimpleArrayMap<String, TaskCompletionSource<Bundle>>();
        this.zzv = zzv;
        this.zzak = zzak;
        this.zzcq = new Messenger((Handler)new zzat(this, Looper.getMainLooper()));
    }
    
    private static void zza(final Context context, final Intent intent) {
        synchronized (zzas.class) {
            if (zzas.zzco == null) {
                final Intent intent2 = new Intent();
                intent2.setPackage("com.google.example.invalidpackage");
                zzas.zzco = PendingIntent.getBroadcast(context, 0, intent2, 0);
            }
            intent.putExtra("app", (Parcelable)zzas.zzco);
        }
    }
    
    private final void zza(String s, final Bundle result) {
        synchronized (this.zzcp) {
            final TaskCompletionSource<Bundle> taskCompletionSource = this.zzcp.remove(s);
            if (taskCompletionSource == null) {
                s = String.valueOf(s);
                if (s.length() != 0) {
                    s = "Missing callback for ".concat(s);
                }
                else {
                    s = new String("Missing callback for ");
                }
                Log.w("FirebaseInstanceId", s);
                return;
            }
            taskCompletionSource.setResult(result);
        }
    }
    
    private static String zzag() {
        synchronized (zzas.class) {
            final int zzcc = zzas.zzcc;
            zzas.zzcc = zzcc + 1;
            return Integer.toString(zzcc);
        }
    }
    
    private final void zzb(Message zzcp) {
        if (zzcp == null || !(zzcp.obj instanceof Intent)) {
            Log.w("FirebaseInstanceId", "Dropping invalid message");
            return;
        }
        final Intent intent = (Intent)zzcp.obj;
        intent.setExtrasClassLoader((ClassLoader)new zzk.zza());
        if (intent.hasExtra("google.messenger")) {
            final Parcelable parcelableExtra = intent.getParcelableExtra("google.messenger");
            if (parcelableExtra instanceof zzk) {
                this.zzcs = (zzk)parcelableExtra;
            }
            if (parcelableExtra instanceof Messenger) {
                this.zzcr = (Messenger)parcelableExtra;
            }
        }
        final Intent intent2 = (Intent)zzcp.obj;
        final String action = intent2.getAction();
        if (!"com.google.android.c2dm.intent.REGISTRATION".equals(action)) {
            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                final String value = String.valueOf(action);
                String concat;
                if (value.length() != 0) {
                    concat = "Unexpected response action: ".concat(value);
                }
                else {
                    concat = new String("Unexpected response action: ");
                }
                Log.d("FirebaseInstanceId", concat);
            }
            return;
        }
        String s;
        if ((s = intent2.getStringExtra("registration_id")) == null) {
            s = intent2.getStringExtra("unregistered");
        }
        if (s == null) {
            final String stringExtra = intent2.getStringExtra("error");
            if (stringExtra == null) {
                final String value2 = String.valueOf(intent2.getExtras());
                final StringBuilder sb = new StringBuilder(String.valueOf(value2).length() + 49);
                sb.append("Unexpected response, no error or registration id ");
                sb.append(value2);
                Log.w("FirebaseInstanceId", sb.toString());
                return;
            }
            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                final String value3 = String.valueOf(stringExtra);
                String concat2;
                if (value3.length() != 0) {
                    concat2 = "Received InstanceID error ".concat(value3);
                }
                else {
                    concat2 = new String("Received InstanceID error ");
                }
                Log.d("FirebaseInstanceId", concat2);
            }
            if (stringExtra.startsWith("|")) {
                final String[] split = stringExtra.split("\\|");
                if (split.length > 2 && "ID".equals(split[1])) {
                    final String s2 = split[2];
                    String substring;
                    final String s3 = substring = split[3];
                    if (s3.startsWith(":")) {
                        substring = s3.substring(1);
                    }
                    this.zza(s2, intent2.putExtra("error", substring).getExtras());
                    return;
                }
                final String value4 = String.valueOf(stringExtra);
                String concat3;
                if (value4.length() != 0) {
                    concat3 = "Unexpected structured response ".concat(value4);
                }
                else {
                    concat3 = new String("Unexpected structured response ");
                }
                Log.w("FirebaseInstanceId", concat3);
                return;
            }
            else {
                zzcp = (Message)this.zzcp;
                // monitorenter(zzcp)
                int i = 0;
                try {
                    while (i < this.zzcp.size()) {
                        this.zza(this.zzcp.keyAt(i), intent2.getExtras());
                        ++i;
                    }
                    return;
                }
                finally {
                }
                // monitorexit(zzcp)
            }
        }
        final Matcher matcher = Pattern.compile("\\|ID\\|([^|]+)\\|:?+(.*)").matcher(s);
        if (!matcher.matches()) {
            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                final String value5 = String.valueOf(s);
                String concat4;
                if (value5.length() != 0) {
                    concat4 = "Unexpected response string: ".concat(value5);
                }
                else {
                    concat4 = new String("Unexpected response string: ");
                }
                Log.d("FirebaseInstanceId", concat4);
            }
            return;
        }
        final String group = matcher.group(1);
        final String group2 = matcher.group(2);
        final Bundle extras = intent2.getExtras();
        extras.putString("registration_id", group2);
        this.zza(group, extras);
    }
    
    private final Bundle zzd(Bundle zze) throws IOException {
        Bundle zze2;
        final Bundle bundle = zze2 = this.zze(zze);
        if (bundle != null) {
            zze2 = bundle;
            if (bundle.containsKey("google.messenger")) {
                zze = this.zze(zze);
                if ((zze2 = zze) != null) {
                    zze2 = zze;
                    if (zze.containsKey("google.messenger")) {
                        zze2 = null;
                    }
                }
            }
        }
        return zze2;
    }
    
    private final Bundle zze(Bundle zzcp) throws IOException {
        final String zzag = zzag();
        final TaskCompletionSource<Bundle> taskCompletionSource = new TaskCompletionSource<Bundle>();
        Object zzcp2 = this.zzcp;
        synchronized (zzcp2) {
            this.zzcp.put(zzag, taskCompletionSource);
            // monitorexit(zzcp2)
            if (this.zzak.zzab() != 0) {
                final Intent obj = new Intent();
                obj.setPackage("com.google.android.gms");
                if (this.zzak.zzab() == 2) {
                    zzcp2 = "com.google.iid.TOKEN_REQUEST";
                }
                else {
                    zzcp2 = "com.google.android.c2dm.intent.REGISTER";
                }
                obj.setAction((String)zzcp2);
                obj.putExtras(zzcp);
                zza(this.zzv, obj);
                final StringBuilder sb = new StringBuilder(String.valueOf(zzag).length() + 5);
                sb.append("|ID|");
                sb.append(zzag);
                sb.append("|");
                obj.putExtra("kid", sb.toString());
                if (Log.isLoggable("FirebaseInstanceId", 3)) {
                    final String value = String.valueOf(obj.getExtras());
                    zzcp2 = new StringBuilder(String.valueOf(value).length() + 8);
                    ((StringBuilder)zzcp2).append("Sending ");
                    ((StringBuilder)zzcp2).append(value);
                    Log.d("FirebaseInstanceId", ((StringBuilder)zzcp2).toString());
                }
                obj.putExtra("google.messenger", (Parcelable)this.zzcq);
                Label_0337: {
                    if (this.zzcr != null || this.zzcs != null) {
                        final Message obtain = Message.obtain();
                        obtain.obj = obj;
                        try {
                            if (this.zzcr != null) {
                                this.zzcr.send(obtain);
                                break Label_0337;
                            }
                            this.zzcs.send(obtain);
                            break Label_0337;
                        }
                        catch (RemoteException ex) {
                            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                                Log.d("FirebaseInstanceId", "Messenger failed, fallback to startService");
                            }
                        }
                    }
                    if (this.zzak.zzab() == 2) {
                        this.zzv.sendBroadcast(obj);
                        break Label_0337;
                    }
                    this.zzv.startService(obj);
                    try {
                        try {
                            final Bundle bundle = Tasks.await(taskCompletionSource.getTask(), 30000L, TimeUnit.MILLISECONDS);
                            synchronized (this.zzcp) {
                                this.zzcp.remove(zzag);
                                return bundle;
                            }
                        }
                        finally {
                            zzcp = (Bundle)this.zzcp;
                            // monitorenter(zzcp)
                            final zzas zzas = this;
                            final SimpleArrayMap<String, TaskCompletionSource<Bundle>> simpleArrayMap = zzas.zzcp;
                            final String s = zzag;
                            simpleArrayMap.remove(s);
                            final Bundle bundle2 = zzcp;
                        }
                        // monitorexit(bundle2)
                    }
                    catch (ExecutionException ex2) {}
                    catch (InterruptedException ex3) {}
                    catch (TimeoutException ex4) {}
                }
                try {
                    final zzas zzas = this;
                    final SimpleArrayMap<String, TaskCompletionSource<Bundle>> simpleArrayMap = zzas.zzcp;
                    final String s = zzag;
                    simpleArrayMap.remove(s);
                    final Bundle bundle2 = zzcp;
                    // monitorexit(bundle2)
                    throw zzcp2;
                }
                finally {}
            }
            throw new IOException("MISSING_INSTANCEID_SERVICE");
        }
    }
    
    final Bundle zzc(final Bundle bundle) throws IOException {
        if (this.zzak.zzae() >= 12000000) {
            final Task<Bundle> zzb = zzaa.zzc(this.zzv).zzb(1, bundle);
            try {
                return Tasks.await(zzb);
            }
            catch (InterruptedException | ExecutionException ex3) {
                final ExecutionException ex2;
                final ExecutionException ex = ex2;
                if (Log.isLoggable("FirebaseInstanceId", 3)) {
                    final String value = String.valueOf(ex);
                    final StringBuilder sb = new StringBuilder(String.valueOf(value).length() + 22);
                    sb.append("Error making request: ");
                    sb.append(value);
                    Log.d("FirebaseInstanceId", sb.toString());
                }
                if (ex.getCause() instanceof zzak && ((zzak)ex.getCause()).getErrorCode() == 4) {
                    return this.zzd(bundle);
                }
                return null;
            }
        }
        return this.zzd(bundle);
    }
}
