package com.google.android.gms.measurement.internal;

import android.os.*;
import java.io.*;
import android.content.*;
import javax.net.ssl.*;
import java.net.*;
import com.google.android.gms.common.util.*;
import android.net.*;

public final class zzat extends zzez
{
    private final SSLSocketFactory zzamq;
    
    public zzat(final zzfa zzfa) {
        super(zzfa);
        zzfl zzamq;
        if (Build$VERSION.SDK_INT < 19) {
            zzamq = new zzfl();
        }
        else {
            zzamq = null;
        }
        this.zzamq = zzamq;
    }
    
    private static byte[] zzb(final HttpURLConnection httpURLConnection) throws IOException {
        InputStream inputStream2;
        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final InputStream inputStream = httpURLConnection.getInputStream();
            try {
                final byte[] array = new byte[1024];
                while (true) {
                    final int read = inputStream.read(array);
                    if (read <= 0) {
                        break;
                    }
                    byteArrayOutputStream.write(array, 0, read);
                }
                final byte[] byteArray = byteArrayOutputStream.toByteArray();
                if (inputStream != null) {
                    inputStream.close();
                }
                return byteArray;
            }
            finally {}
        }
        finally {
            inputStream2 = null;
        }
        if (inputStream2 != null) {
            inputStream2.close();
        }
    }
    
    protected final HttpURLConnection zzb(final URL url) throws IOException {
        final URLConnection openConnection = url.openConnection();
        if (openConnection instanceof HttpURLConnection) {
            final SSLSocketFactory zzamq = this.zzamq;
            if (zzamq != null && openConnection instanceof HttpsURLConnection) {
                ((HttpsURLConnection)openConnection).setSSLSocketFactory(zzamq);
            }
            final HttpsURLConnection httpsURLConnection = (HttpsURLConnection)openConnection;
            httpsURLConnection.setDefaultUseCaches(false);
            httpsURLConnection.setConnectTimeout(60000);
            httpsURLConnection.setReadTimeout(61000);
            httpsURLConnection.setInstanceFollowRedirects(false);
            httpsURLConnection.setDoInput(true);
            return httpsURLConnection;
        }
        throw new IOException("Failed to obtain HTTP connection");
    }
    
    public final boolean zzfb() {
        this.zzcl();
        final ConnectivityManager connectivityManager = (ConnectivityManager)this.getContext().getSystemService("connectivity");
        NetworkInfo activeNetworkInfo;
        try {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        catch (SecurityException ex) {
            activeNetworkInfo = null;
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    
    @Override
    protected final boolean zzgt() {
        return false;
    }
}
