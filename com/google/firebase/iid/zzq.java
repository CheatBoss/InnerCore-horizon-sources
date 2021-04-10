package com.google.firebase.iid;

import com.google.firebase.*;
import java.util.concurrent.*;
import android.os.*;
import java.io.*;
import android.util.*;
import com.google.android.gms.tasks.*;

final class zzq implements MessagingChannel
{
    private final FirebaseApp zzaj;
    private final zzam zzak;
    private final zzas zzbf;
    private final Executor zzbg;
    
    zzq(final FirebaseApp firebaseApp, final zzam zzam, final Executor executor) {
        this(firebaseApp, zzam, executor, new zzas(firebaseApp.getApplicationContext(), zzam));
    }
    
    private zzq(final FirebaseApp zzaj, final zzam zzak, final Executor zzbg, final zzas zzbf) {
        this.zzaj = zzaj;
        this.zzak = zzak;
        this.zzbf = zzbf;
        this.zzbg = zzbg;
    }
    
    private final Task<Bundle> zza(final String s, final String s2, final String s3, final Bundle bundle) {
        bundle.putString("scope", s3);
        bundle.putString("sender", s2);
        bundle.putString("subtype", s2);
        bundle.putString("appid", s);
        bundle.putString("gmp_app_id", this.zzaj.getOptions().getApplicationId());
        bundle.putString("gmsv", Integer.toString(this.zzak.zzae()));
        bundle.putString("osv", Integer.toString(Build$VERSION.SDK_INT));
        bundle.putString("app_ver", this.zzak.zzac());
        bundle.putString("app_ver_name", this.zzak.zzad());
        bundle.putString("cliv", "fiid-12451000");
        final TaskCompletionSource<Bundle> taskCompletionSource = new TaskCompletionSource<Bundle>();
        this.zzbg.execute(new zzr(this, bundle, taskCompletionSource));
        return taskCompletionSource.getTask();
    }
    
    private static String zza(final Bundle bundle) throws IOException {
        if (bundle == null) {
            throw new IOException("SERVICE_NOT_AVAILABLE");
        }
        final String string = bundle.getString("registration_id");
        if (string != null) {
            return string;
        }
        final String string2 = bundle.getString("unregistered");
        if (string2 != null) {
            return string2;
        }
        final String string3 = bundle.getString("error");
        if ("RST".equals(string3)) {
            throw new IOException("INSTANCE_ID_RESET");
        }
        if (string3 != null) {
            throw new IOException(string3);
        }
        final String value = String.valueOf(bundle);
        final StringBuilder sb = new StringBuilder(String.valueOf(value).length() + 21);
        sb.append("Unexpected response: ");
        sb.append(value);
        Log.w("FirebaseInstanceId", sb.toString(), new Throwable());
        throw new IOException("SERVICE_NOT_AVAILABLE");
    }
    
    private final <T> Task<Void> zzb(final Task<T> task) {
        return task.continueWith(zzi.zzd(), (Continuation<T, Void>)new zzs(this));
    }
    
    private final Task<String> zzc(final Task<Bundle> task) {
        return task.continueWith(this.zzbg, (Continuation<Bundle, String>)new zzt(this));
    }
    
    @Override
    public final Task<Void> buildChannel(final String s, final String s2) {
        return Tasks.forResult((Void)null);
    }
    
    @Override
    public final Task<String> getToken(final String s, final String s2, final String s3, final String s4) {
        return this.zzc(this.zza(s, s3, s4, new Bundle()));
    }
    
    @Override
    public final boolean isAvailable() {
        return this.zzak.zzab() != 0;
    }
    
    @Override
    public final boolean isChannelBuilt() {
        return true;
    }
    
    @Override
    public final Task<Void> subscribeToTopic(final String s, final String s2, String o) {
        final Bundle bundle = new Bundle();
        final String value = String.valueOf(o);
        String concat;
        if (value.length() != 0) {
            concat = "/topics/".concat(value);
        }
        else {
            concat = new String("/topics/");
        }
        bundle.putString("gcm.topic", concat);
        o = String.valueOf(o);
        if (((String)o).length() != 0) {
            o = "/topics/".concat((String)o);
        }
        else {
            o = new String("/topics/");
        }
        return this.zzb(this.zzc(this.zza(s, s2, (String)o, bundle)));
    }
    
    @Override
    public final Task<Void> unsubscribeFromTopic(final String s, final String s2, String o) {
        final Bundle bundle = new Bundle();
        final String value = String.valueOf(o);
        String concat;
        if (value.length() != 0) {
            concat = "/topics/".concat(value);
        }
        else {
            concat = new String("/topics/");
        }
        bundle.putString("gcm.topic", concat);
        bundle.putString("delete", "1");
        o = String.valueOf(o);
        if (((String)o).length() != 0) {
            o = "/topics/".concat((String)o);
        }
        else {
            o = new String("/topics/");
        }
        return this.zzb(this.zzc(this.zza(s, s2, (String)o, bundle)));
    }
}
