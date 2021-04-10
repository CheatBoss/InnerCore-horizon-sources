package com.google.android.gms.ads.identifier;

import java.net.*;
import android.util.*;
import java.io.*;
import android.net.*;
import java.util.*;

final class zza extends Thread
{
    private final /* synthetic */ Map zzl;
    
    zza(final AdvertisingIdClient advertisingIdClient, final Map zzl) {
        this.zzl = zzl;
    }
    
    @Override
    public final void run() {
        new zzc();
        final Map zzl = this.zzl;
        final Uri$Builder buildUpon = Uri.parse("https://pagead2.googlesyndication.com/pagead/gen_204?id=gmob-apps").buildUpon();
        for (final String s : zzl.keySet()) {
            buildUpon.appendQueryParameter(s, (String)zzl.get(s));
        }
        final String string = buildUpon.build().toString();
        IndexOutOfBoundsException ex = null;
        String s2;
        StringBuilder sb2;
        String s3;
        try {
            final HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(string).openConnection();
            try {
                final int responseCode = httpURLConnection.getResponseCode();
                if (responseCode < 200 || responseCode >= 300) {
                    final StringBuilder sb = new StringBuilder(String.valueOf(string).length() + 65);
                    sb.append("Received non-success response code ");
                    sb.append(responseCode);
                    sb.append(" from pinging URL: ");
                    sb.append(string);
                    Log.w("HttpUrlPinger", sb.toString());
                }
                return;
            }
            finally {
                httpURLConnection.disconnect();
            }
        }
        catch (IOException | RuntimeException ex3) {
            final IndexOutOfBoundsException ex2;
            ex = ex2;
            s2 = ex.getMessage();
            sb2 = new StringBuilder(String.valueOf(string).length() + 27 + String.valueOf(s2).length());
            s3 = "Error while pinging URL: ";
        }
        catch (IndexOutOfBoundsException ex) {
            s2 = ex.getMessage();
            sb2 = new StringBuilder(String.valueOf(string).length() + 32 + String.valueOf(s2).length());
            s3 = "Error while parsing ping URL: ";
        }
        sb2.append(s3);
        sb2.append(string);
        sb2.append(". ");
        sb2.append(s2);
        Log.w("HttpUrlPinger", sb2.toString(), (Throwable)ex);
    }
}
