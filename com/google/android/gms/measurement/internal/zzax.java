package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.*;
import java.net.*;
import java.util.*;
import java.io.*;

final class zzax implements Runnable
{
    private final String packageName;
    private final URL url;
    private final byte[] zzamv;
    private final zzav zzamw;
    private final Map<String, String> zzamx;
    private final /* synthetic */ zzat zzamy;
    
    public zzax(final zzat zzamy, final String packageName, final URL url, final byte[] zzamv, final Map<String, String> zzamx, final zzav zzamw) {
        this.zzamy = zzamy;
        Preconditions.checkNotEmpty(packageName);
        Preconditions.checkNotNull(url);
        Preconditions.checkNotNull(zzamw);
        this.url = url;
        this.zzamv = zzamv;
        this.zzamw = zzamw;
        this.packageName = packageName;
        this.zzamx = zzamx;
    }
    
    @Override
    public final void run() {
        while (true) {
            this.zzamy.zzgc();
            Object o = null;
            Object o2 = null;
            while (true) {
                Object o3 = null;
                HttpURLConnection httpURLConnection = null;
                Label_0397: {
                    try {
                        final HttpURLConnection zzb = this.zzamy.zzb(this.url);
                        try {
                            if (this.zzamx != null) {
                                for (final Map.Entry<String, V> entry : this.zzamx.entrySet()) {
                                    zzb.addRequestProperty(entry.getKey(), (String)entry.getValue());
                                }
                            }
                            if (this.zzamv != null) {
                                final byte[] zzb2 = this.zzamy.zzjo().zzb(this.zzamv);
                                this.zzamy.zzgo().zzjl().zzg("Uploading data. size", zzb2.length);
                                zzb.setDoOutput(true);
                                zzb.addRequestProperty("Content-Encoding", "gzip");
                                zzb.setFixedLengthStreamingMode(zzb2.length);
                                zzb.connect();
                                o3 = zzb.getOutputStream();
                                try {
                                    ((OutputStream)o3).write(zzb2);
                                    ((OutputStream)o3).close();
                                }
                                catch (IOException ex3) {
                                    break Label_0397;
                                }
                                finally {
                                    goto Label_0292;
                                }
                            }
                            final int responseCode = zzb.getResponseCode();
                            try {
                                final Map<String, List<String>> headerFields = zzb.getHeaderFields();
                                try {
                                    final byte[] zza = zzat.zza(this.zzamy, zzb);
                                    if (zzb != null) {
                                        zzb.disconnect();
                                    }
                                    final zzbo zzbo = this.zzamy.zzgn();
                                    final zzaw zzaw = new zzaw(this.packageName, this.zzamw, responseCode, null, zza, headerFields, null);
                                    zzbo.zzc(zzaw);
                                    return;
                                }
                                catch (IOException ex4) {}
                            }
                            catch (IOException o3) {}
                            finally {
                                goto Label_0311;
                            }
                        }
                        catch (IOException ex5) {}
                    }
                    catch (IOException ex2) {
                        httpURLConnection = null;
                    }
                    finally {
                        httpURLConnection = null;
                    }
                    try {
                        ((OutputStream)o2).close();
                        goto Label_0350;
                    }
                    catch (IOException ex6) {}
                    o3 = null;
                }
                final IOException ex = null;
                final IOException ex2;
                o2 = ex2;
                o = o3;
                final int responseCode = 0;
                ex2 = ex;
                o3 = o2;
                if (o != null) {
                    try {
                        ((OutputStream)o).close();
                    }
                    catch (IOException o) {
                        this.zzamy.zzgo().zzjd().zze("Error closing HTTP compressed POST connection output stream. appId", zzap.zzbv(this.packageName), o);
                    }
                }
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                final zzbo zzbo = this.zzamy.zzgn();
                final zzaw zzaw = new zzaw(this.packageName, this.zzamw, responseCode, (Throwable)o3, null, (Map)ex2, null);
                continue;
            }
        }
    }
}
