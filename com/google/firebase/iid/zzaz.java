package com.google.firebase.iid;

import java.util.*;
import com.google.android.gms.tasks.*;
import android.support.v4.util.*;
import android.util.*;
import java.io.*;
import android.text.*;

final class zzaz
{
    private final zzav zzag;
    private int zzdi;
    private final Map<Integer, TaskCompletionSource<Void>> zzdj;
    
    zzaz(final zzav zzag) {
        this.zzdi = 0;
        this.zzdj = new ArrayMap<Integer, TaskCompletionSource<Void>>();
        this.zzag = zzag;
    }
    
    private static boolean zza(final FirebaseInstanceId firebaseInstanceId, String s) {
        final String[] split = s.split("!");
        if (split.length == 2) {
            Block_1: {
                break Block_1;
            Block_3:
                while (true) {
                    int i = 0;
                    do {
                        while (true) {
                            Label_0167: {
                                Label_0076: {
                                    break Label_0076;
                                    s = split[0];
                                    final String s2 = split[1];
                                    i = -1;
                                    try {
                                        final int hashCode = s.hashCode();
                                        if (hashCode != 83) {
                                            if (hashCode != 85) {
                                                break Label_0167;
                                            }
                                            if (s.equals("U")) {
                                                i = 1;
                                            }
                                            break Label_0167;
                                        }
                                        else {
                                            if (s.equals("S")) {
                                                i = 0;
                                            }
                                            break Label_0167;
                                        }
                                        firebaseInstanceId.zzc(s2);
                                        // iftrue(Label_0165:, !FirebaseInstanceId.zzk())
                                        // iftrue(Label_0165:, !FirebaseInstanceId.zzk())
                                        Block_10: {
                                            break Block_10;
                                            firebaseInstanceId.zzb(s2);
                                            Log.d("FirebaseInstanceId", "subscribe operation succeeded");
                                            return true;
                                        }
                                        Log.d("FirebaseInstanceId", "unsubscribe operation succeeded");
                                        return true;
                                    }
                                    catch (IOException ex) {
                                        final String value = String.valueOf(ex.getMessage());
                                        String concat;
                                        if (value.length() != 0) {
                                            concat = "Topic sync failed: ".concat(value);
                                        }
                                        else {
                                            concat = new String("Topic sync failed: ");
                                        }
                                        Log.e("FirebaseInstanceId", concat);
                                        return false;
                                    }
                                }
                                return true;
                            }
                            if (i != 0) {
                                continue Block_3;
                            }
                            continue;
                        }
                    } while (i == 1);
                    break;
                }
            }
            return true;
        }
        Label_0165: {
            return true;
        }
    }
    
    private final String zzaq() {
        Object o = this.zzag;
        synchronized (o) {
            final String zzaj = this.zzag.zzaj();
            // monitorexit(o)
            if (!TextUtils.isEmpty((CharSequence)zzaj)) {
                o = zzaj.split(",");
                if (o.length > 1 && !TextUtils.isEmpty(o[1])) {
                    return o[1];
                }
            }
            return null;
        }
    }
    
    private final boolean zzk(String o) {
        synchronized (this) {
            synchronized (this.zzag) {
                final String zzaj = this.zzag.zzaj();
                final String value = String.valueOf(o);
                String concat;
                if (value.length() != 0) {
                    concat = ",".concat(value);
                }
                else {
                    concat = new String(",");
                }
                if (zzaj.startsWith(concat)) {
                    o = String.valueOf(o);
                    if (((String)o).length() != 0) {
                        o = ",".concat((String)o);
                    }
                    else {
                        o = new String(",");
                    }
                    o = zzaj.substring(((String)o).length());
                    this.zzag.zzf((String)o);
                    return true;
                }
                return false;
            }
        }
    }
    
    final boolean zzap() {
        synchronized (this) {
            return this.zzaq() != null;
        }
    }
    
    final boolean zzc(final FirebaseInstanceId firebaseInstanceId) {
        while (true) {
            synchronized (this) {
                final String zzaq = this.zzaq();
                if (zzaq == null) {
                    Log.d("FirebaseInstanceId", "topic sync succeeded");
                    return true;
                }
                // monitorexit(this)
                if (!zza(firebaseInstanceId, zzaq)) {
                    return false;
                }
                synchronized (this) {
                    final TaskCompletionSource<Void> taskCompletionSource = this.zzdj.remove(this.zzdi);
                    this.zzk(zzaq);
                    ++this.zzdi;
                    // monitorexit(this)
                    if (taskCompletionSource != null) {
                        taskCompletionSource.setResult(null);
                        continue;
                    }
                    continue;
                }
            }
        }
    }
}
