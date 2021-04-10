package com.appsflyer.internal;

import com.appsflyer.internal.model.event.*;
import android.net.*;
import com.appsflyer.*;
import java.io.*;
import android.content.*;
import java.net.*;

public final class ad implements Runnable
{
    private final BackgroundEvent \u0399;
    
    public ad(final BackgroundEvent backgroundEvent) {
        this.\u0399 = (BackgroundEvent)backgroundEvent.weakContext();
    }
    
    @Override
    public final void run() {
        this.\u0269();
    }
    
    final HttpURLConnection \u0269() {
        Object o = "";
        final String urlString = this.\u0399.urlString();
        Object body = this.\u0399.body();
        final boolean trackingStopped = this.\u0399.trackingStopped();
        final boolean response = this.\u0399.readResponse();
        final boolean proxyMode = this.\u0399.proxyMode();
        final boolean encrypt = this.\u0399.isEncrypt();
        Object o2 = ((String)body).getBytes();
        if (trackingStopped) {
            return null;
        }
        final int n = 0;
        int n2 = 0;
        Label_0855: {
            Object string = null;
            Label_0849: {
                try {
                    final URL url = new URL(urlString);
                    if (proxyMode) {
                        try {
                            if (ai.\u0269 == null) {
                                ai.\u0269 = new ai();
                            }
                            final ai \u0269 = ai.\u0269;
                            string = url.toString();
                            try {
                                \u0269.\u0131("server_request", (String)string, (String)body);
                                n2 = ((String)body).getBytes("UTF-8").length;
                                string = new StringBuilder("call = ");
                                ((StringBuilder)string).append(url);
                                ((StringBuilder)string).append("; size = ");
                                ((StringBuilder)string).append(n2);
                                ((StringBuilder)string).append(" byte");
                                String s;
                                if (n2 > 1) {
                                    s = "s";
                                }
                                else {
                                    s = "";
                                }
                                ((StringBuilder)string).append(s);
                                ((StringBuilder)string).append("; body = ");
                                ((StringBuilder)string).append((String)body);
                                ah.\u0399(string.toString());
                            }
                            finally {}
                        }
                        finally {}
                    }
                    try {
                        TrafficStats.setThreadStatsTag("AppsFlyer".hashCode());
                        body = url.openConnection();
                        string = o;
                        try {
                            ((URLConnection)body).setReadTimeout(30000);
                            string = o;
                            ((URLConnection)body).setConnectTimeout(30000);
                            string = o;
                            ((HttpURLConnection)body).setRequestMethod("POST");
                            string = o;
                            ((URLConnection)body).setDoInput(true);
                            string = o;
                            ((URLConnection)body).setDoOutput(true);
                            String s2;
                            if (encrypt) {
                                s2 = "application/octet-stream";
                            }
                            else {
                                s2 = "application/json";
                            }
                            string = o;
                            ((URLConnection)body).setRequestProperty("Content-Type", s2);
                            string = o;
                            final OutputStream outputStream = ((URLConnection)body).getOutputStream();
                            final Object o3 = o2;
                            if (encrypt) {
                                string = o;
                                final String key = this.\u0399.key();
                                try {
                                    final Object invoke = ((Class)d.\u01c3(24, '\0', 24)).getMethod("\u01c3", String.class).invoke(null, key);
                                    try {
                                        final byte[] array = (byte[])((Class)d.\u01c3(24, '\0', 24)).getDeclaredMethod("\u0131", byte[].class).invoke(invoke, o2);
                                    }
                                    finally {
                                        string = o;
                                        final Throwable t;
                                        o2 = t.getCause();
                                        if (o2 != null) {
                                            string = o;
                                            throw o2;
                                        }
                                        string = o;
                                    }
                                }
                                finally {
                                    string = o;
                                    o2 = ((Throwable)o3).getCause();
                                    if (o2 != null) {
                                        string = o;
                                        throw o2;
                                    }
                                    string = o;
                                }
                            }
                            string = o;
                            outputStream.write((byte[])o3);
                            string = o;
                            outputStream.close();
                            string = o;
                            ((URLConnection)body).connect();
                            string = o;
                            n2 = ((HttpURLConnection)body).getResponseCode();
                            Object \u01c3 = o;
                            if (response) {
                                string = o;
                                \u01c3 = AppsFlyerLibCore.getInstance().\u01c3((HttpURLConnection)body);
                            }
                            if (proxyMode) {
                                string = \u01c3;
                                if (ai.\u0269 == null) {
                                    string = \u01c3;
                                    ai.\u0269 = new ai();
                                }
                                string = \u01c3;
                                ai.\u0269.\u0131("server_response", url.toString(), String.valueOf(n2), (String)\u01c3);
                            }
                            string = \u01c3;
                            if (n2 != 200) {
                                break Label_0849;
                            }
                            string = \u01c3;
                            AFLogger.afInfoLog("Status 200 ok");
                            string = \u01c3;
                            final Context context = this.\u0399.context();
                            string = \u01c3;
                            o2 = \u01c3;
                            n2 = n;
                            if (!url.toString().startsWith(ServerConfigHandler.getUrl(AppsFlyerLibCore.REGISTER_URL))) {
                                break Label_0855;
                            }
                            o2 = \u01c3;
                            n2 = n;
                            if (context != null) {
                                string = \u01c3;
                                AppsFlyerLibCore.getSharedPreferences(context).edit().putBoolean("sentRegisterRequestToAF", true).apply();
                                string = \u01c3;
                                AFLogger.afDebugLog("Successfully registered for Uninstall Tracking");
                                o2 = \u01c3;
                                n2 = n;
                            }
                            break Label_0855;
                        }
                        finally {
                            string = o;
                        }
                    }
                    finally {}
                }
                finally {}
                body = null;
                final String s3 = "";
                final Throwable t2;
                string = t2;
                t2 = (Throwable)s3;
                AFLogger.afErrorLog("Error while calling ".concat(String.valueOf(urlString)), (Throwable)string);
                o = body;
                string = t2;
            }
            n2 = 1;
            o2 = string;
        }
        final StringBuilder sb = new StringBuilder("Connection ");
        String s4;
        if (n2 != 0) {
            s4 = "error";
        }
        else {
            s4 = "call succeeded";
        }
        sb.append(s4);
        sb.append(": ");
        sb.append((String)o2);
        AFLogger.afInfoLog(sb.toString());
        return (HttpURLConnection)o;
    }
}
