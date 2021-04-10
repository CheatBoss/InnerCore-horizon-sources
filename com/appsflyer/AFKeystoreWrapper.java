package com.appsflyer;

import android.content.*;
import java.io.*;
import java.security.cert.*;
import java.util.*;
import android.os.*;
import android.security.keystore.*;
import javax.security.auth.x500.*;
import java.math.*;
import android.security.*;
import java.security.*;
import java.security.spec.*;

class AFKeystoreWrapper
{
    final Object \u0131;
    private Context \u01c3;
    int \u0269;
    KeyStore \u0399;
    String \u03b9;
    
    public AFKeystoreWrapper(final Context \u01c3) {
        this.\u0131 = new Object();
        this.\u01c3 = \u01c3;
        this.\u03b9 = "";
        this.\u0269 = 0;
        AFLogger.afInfoLog("Initialising KeyStore..");
        try {
            (this.\u0399 = KeyStore.getInstance("AndroidKeyStore")).load(null);
        }
        catch (IOException | NoSuchAlgorithmException | CertificateException | KeyStoreException ex) {
            final Throwable t;
            AFLogger.afErrorLog("Couldn't load keystore instance of type: AndroidKeyStore", t);
        }
    }
    
    private static boolean \u0399(final String s) {
        return s.startsWith("com.appsflyer");
    }
    
    final String \u0131() {
        synchronized (this.\u0131) {
            return this.\u03b9;
        }
    }
    
    final boolean \u01c3() {
        synchronized (this.\u0131) {
            final KeyStore \u03b9 = this.\u0399;
            final boolean b = false;
            final boolean b2 = false;
            boolean b3 = b;
            if (\u03b9 != null) {
                try {
                    final Enumeration<String> aliases = this.\u0399.aliases();
                    String s = null;
                    Block_7: {
                        while (aliases.hasMoreElements()) {
                            s = aliases.nextElement();
                            if (s != null && \u0399(s)) {
                                break Block_7;
                            }
                        }
                        return b3;
                    }
                    final String[] split = s.split(",");
                    if (split.length != 3) {
                        return b3;
                    }
                    AFLogger.afInfoLog("Found a matching AF key with alias:\n".concat(String.valueOf(s)));
                    try {
                        final String[] split2 = split[1].trim().split("=");
                        final String[] split3 = split[2].trim().split("=");
                        if (split2.length == 2 && split3.length == 2) {
                            this.\u03b9 = split2[1].trim();
                            this.\u0269 = Integer.parseInt(split3[1].trim());
                        }
                    }
                    finally {}
                }
                finally {
                    b3 = b2;
                }
                final StringBuilder sb = new StringBuilder("Couldn't list KeyStore Aliases: ");
                final Throwable t;
                sb.append(t.getClass().getName());
                AFLogger.afErrorLog(sb.toString(), t);
            }
            return b3;
        }
    }
    
    final String \u0269() {
        final StringBuilder sb = new StringBuilder();
        sb.append("com.appsflyer,");
        synchronized (this.\u0131) {
            sb.append("KSAppsFlyerId=");
            sb.append(this.\u03b9);
            sb.append(",");
            sb.append("KSAppsFlyerRICounter=");
            sb.append(this.\u0269);
            // monitorexit(this.\u0131)
            return sb.toString();
        }
    }
    
    final void \u0269(final String alias) {
        AFLogger.afInfoLog("Creating a new key with alias: ".concat(String.valueOf(alias)));
        try {
            final Calendar instance = Calendar.getInstance();
            final Calendar instance2 = Calendar.getInstance();
            instance2.add(1, 5);
            final AlgorithmParameterSpec algorithmParameterSpec = null;
            synchronized (this.\u0131) {
                if (!this.\u0399.containsAlias(alias)) {
                    Object o;
                    if (Build$VERSION.SDK_INT >= 23) {
                        o = new KeyGenParameterSpec$Builder(alias, 3).setCertificateSubject(new X500Principal("CN=AndroidSDK, O=AppsFlyer")).setCertificateSerialNumber(BigInteger.ONE).setCertificateNotBefore(instance.getTime()).setCertificateNotAfter(instance2.getTime()).build();
                    }
                    else {
                        o = algorithmParameterSpec;
                        if (Build$VERSION.SDK_INT >= 18) {
                            o = algorithmParameterSpec;
                            if (!AndroidUtils.\u0269()) {
                                o = new KeyPairGeneratorSpec$Builder(this.\u01c3).setAlias(alias).setSubject(new X500Principal("CN=AndroidSDK, O=AppsFlyer")).setSerialNumber(BigInteger.ONE).setStartDate(instance.getTime()).setEndDate(instance2.getTime()).build();
                            }
                        }
                    }
                    final KeyPairGenerator instance3 = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
                    instance3.initialize((AlgorithmParameterSpec)o);
                    instance3.generateKeyPair();
                }
                else {
                    AFLogger.afInfoLog("Alias already exists: ".concat(String.valueOf(alias)));
                }
            }
        }
        finally {
            final StringBuilder sb = new StringBuilder("Exception ");
            final Throwable t;
            sb.append(t.getMessage());
            sb.append(" occurred");
            AFLogger.afErrorLog(sb.toString(), t);
        }
    }
    
    final int \u0399() {
        synchronized (this.\u0131) {
            return this.\u0269;
        }
    }
}
