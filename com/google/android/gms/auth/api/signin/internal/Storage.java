package com.google.android.gms.auth.api.signin.internal;

import java.util.concurrent.locks.*;
import android.content.*;
import com.google.android.gms.common.internal.*;
import com.google.android.gms.auth.api.signin.*;
import android.text.*;
import org.json.*;
import javax.annotation.*;

public class Storage
{
    private static final Lock zzaf;
    private static Storage zzag;
    private final Lock zzah;
    private final SharedPreferences zzai;
    
    static {
        zzaf = new ReentrantLock();
    }
    
    private Storage(final Context context) {
        this.zzah = new ReentrantLock();
        this.zzai = context.getSharedPreferences("com.google.android.gms.signin", 0);
    }
    
    public static Storage getInstance(final Context context) {
        Preconditions.checkNotNull(context);
        Storage.zzaf.lock();
        try {
            if (Storage.zzag == null) {
                Storage.zzag = new Storage(context.getApplicationContext());
            }
            return Storage.zzag;
        }
        finally {
            Storage.zzaf.unlock();
        }
    }
    
    private static String zza(final String s, final String s2) {
        final StringBuilder sb = new StringBuilder(String.valueOf(s).length() + 1 + String.valueOf(s2).length());
        sb.append(s);
        sb.append(":");
        sb.append(s2);
        return sb.toString();
    }
    
    @Nullable
    private final GoogleSignInAccount zzb(String fromStore) {
        if (TextUtils.isEmpty((CharSequence)fromStore)) {
            return null;
        }
        fromStore = this.getFromStore(zza("googleSignInAccount", fromStore));
        if (fromStore != null) {
            try {
                return GoogleSignInAccount.fromJsonString(fromStore);
            }
            catch (JSONException ex) {}
        }
        return null;
    }
    
    @Nullable
    protected String getFromStore(String string) {
        this.zzah.lock();
        try {
            string = this.zzai.getString(string, (String)null);
            return string;
        }
        finally {
            this.zzah.unlock();
        }
    }
    
    @Nullable
    public GoogleSignInAccount getSavedDefaultGoogleSignInAccount() {
        return this.zzb(this.getFromStore("defaultGoogleSignInAccount"));
    }
}
