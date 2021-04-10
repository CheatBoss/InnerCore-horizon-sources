package com.appboy.configuration;

import android.content.*;
import bo.app.*;
import com.appboy.support.*;
import java.util.*;

public class CachedConfigurationProvider
{
    private static final String a;
    private final Context b;
    protected final Map<String, Object> mConfigurationCache;
    protected final m mRuntimeAppConfigurationProvider;
    
    static {
        a = AppboyLogger.getAppboyLogTag(CachedConfigurationProvider.class);
    }
    
    public CachedConfigurationProvider(final Context b) {
        this.b = b;
        this.mConfigurationCache = Collections.synchronizedMap(new HashMap<String, Object>());
        this.mRuntimeAppConfigurationProvider = new m(b);
    }
    
    protected boolean getBooleanValue(final String s, final boolean b) {
        if (this.mConfigurationCache.containsKey(s)) {
            return this.mConfigurationCache.get(s);
        }
        boolean b2;
        String s2;
        StringBuilder sb;
        String s3;
        if (this.mRuntimeAppConfigurationProvider.a(s)) {
            b2 = this.mRuntimeAppConfigurationProvider.a(s, b);
            this.mConfigurationCache.put(s, b2);
            s2 = CachedConfigurationProvider.a;
            sb = new StringBuilder();
            s3 = "Using runtime override value for key: ";
        }
        else {
            b2 = this.readBooleanResourceValue(s, b);
            this.mConfigurationCache.put(s, b2);
            s2 = CachedConfigurationProvider.a;
            sb = new StringBuilder();
            s3 = "Defaulting to using xml value for key: ";
        }
        sb.append(s3);
        sb.append(s);
        sb.append(" and value: ");
        sb.append(b2);
        AppboyLogger.d(s2, sb.toString());
        return b2;
    }
    
    protected int getIntValue(final String s, int n) {
        if (this.mConfigurationCache.containsKey(s)) {
            return this.mConfigurationCache.get(s);
        }
        String s2;
        StringBuilder sb;
        String s3;
        if (this.mRuntimeAppConfigurationProvider.a(s)) {
            n = this.mRuntimeAppConfigurationProvider.a(s, n);
            this.mConfigurationCache.put(s, n);
            s2 = CachedConfigurationProvider.a;
            sb = new StringBuilder();
            s3 = "Using runtime override value for key: ";
        }
        else {
            n = this.readIntegerResourceValue(s, n);
            this.mConfigurationCache.put(s, n);
            s2 = CachedConfigurationProvider.a;
            sb = new StringBuilder();
            s3 = "Defaulting to using xml value for key: ";
        }
        sb.append(s3);
        sb.append(s);
        sb.append(" and value: ");
        sb.append(n);
        AppboyLogger.d(s2, sb.toString());
        return n;
    }
    
    protected String getStringValue(final String s, String s2) {
        if (this.mConfigurationCache.containsKey(s)) {
            return this.mConfigurationCache.get(s);
        }
        String s3;
        StringBuilder sb;
        String s4;
        if (this.mRuntimeAppConfigurationProvider.a(s)) {
            s3 = this.mRuntimeAppConfigurationProvider.a(s, s2);
            this.mConfigurationCache.put(s, s3);
            s2 = CachedConfigurationProvider.a;
            sb = new StringBuilder();
            s4 = "Using runtime override value for key: ";
        }
        else {
            s3 = this.readStringResourceValue(s, s2);
            this.mConfigurationCache.put(s, s3);
            s2 = CachedConfigurationProvider.a;
            sb = new StringBuilder();
            s4 = "Defaulting to using xml value for key: ";
        }
        sb.append(s4);
        sb.append(s);
        sb.append(" and value: ");
        sb.append(s3);
        AppboyLogger.d(s2, sb.toString());
        return s3;
    }
    
    protected boolean readBooleanResourceValue(final String s, final boolean b) {
        if (s == null) {
            return b;
        }
        try {
            final int identifier = this.b.getResources().getIdentifier(s, "bool", PackageUtils.getResourcePackageName(this.b));
            if (identifier == 0) {
                final String a = CachedConfigurationProvider.a;
                final StringBuilder sb = new StringBuilder();
                sb.append("Unable to find the xml boolean configuration value with key ");
                sb.append(s);
                sb.append(". Using default value '");
                sb.append(b);
                sb.append("'.");
                AppboyLogger.d(a, sb.toString());
                return b;
            }
            return this.b.getResources().getBoolean(identifier);
        }
        catch (Exception ex) {
            final String a2 = CachedConfigurationProvider.a;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Unexpected exception retrieving the xml boolean configuration value with key ");
            sb2.append(s);
            sb2.append(". Using default value ");
            sb2.append(b);
            sb2.append("'.");
            AppboyLogger.d(a2, sb2.toString());
            return b;
        }
    }
    
    protected int readIntegerResourceValue(final String s, final int n) {
        if (s == null) {
            return n;
        }
        try {
            final int identifier = this.b.getResources().getIdentifier(s, "integer", PackageUtils.getResourcePackageName(this.b));
            if (identifier == 0) {
                final String a = CachedConfigurationProvider.a;
                final StringBuilder sb = new StringBuilder();
                sb.append("Unable to find the xml integer configuration value with key ");
                sb.append(s);
                sb.append(". Using default value '");
                sb.append(n);
                sb.append("'.");
                AppboyLogger.d(a, sb.toString());
                return n;
            }
            return this.b.getResources().getInteger(identifier);
        }
        catch (Exception ex) {
            final String a2 = CachedConfigurationProvider.a;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Unexpected exception retrieving the xml integer configuration value with key ");
            sb2.append(s);
            sb2.append(". Using default value ");
            sb2.append(n);
            sb2.append("'.");
            AppboyLogger.d(a2, sb2.toString());
            return n;
        }
    }
    
    protected String[] readStringArrayResourceValue(final String s, final String[] array) {
        if (s == null) {
            return array;
        }
        try {
            final int identifier = this.b.getResources().getIdentifier(s, "array", PackageUtils.getResourcePackageName(this.b));
            if (identifier == 0) {
                final String a = CachedConfigurationProvider.a;
                final StringBuilder sb = new StringBuilder();
                sb.append("Unable to find the xml string array configuration value with key ");
                sb.append(s);
                sb.append(". Using default value '");
                sb.append(Arrays.toString(array));
                sb.append("'.");
                AppboyLogger.d(a, sb.toString());
                return array;
            }
            return this.b.getResources().getStringArray(identifier);
        }
        catch (Exception ex) {
            final String a2 = CachedConfigurationProvider.a;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Unexpected exception retrieving the xml string array configuration value with key ");
            sb2.append(s);
            sb2.append(". Using default value ");
            sb2.append(Arrays.toString(array));
            sb2.append("'.");
            AppboyLogger.d(a2, sb2.toString());
            return array;
        }
    }
    
    protected String readStringResourceValue(final String s, final String s2) {
        if (s == null) {
            return s2;
        }
        try {
            final int identifier = this.b.getResources().getIdentifier(s, "string", PackageUtils.getResourcePackageName(this.b));
            if (identifier == 0) {
                final String a = CachedConfigurationProvider.a;
                final StringBuilder sb = new StringBuilder();
                sb.append("Unable to find the xml string configuration value with key ");
                sb.append(s);
                sb.append(". Using default value '");
                sb.append(s2);
                sb.append("'.");
                AppboyLogger.d(a, sb.toString());
                return s2;
            }
            return this.b.getResources().getString(identifier);
        }
        catch (Exception ex) {
            final String a2 = CachedConfigurationProvider.a;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Unexpected exception retrieving the xml string configuration value with key ");
            sb2.append(s);
            sb2.append(". Using default value ");
            sb2.append(s2);
            sb2.append("'.");
            AppboyLogger.d(a2, sb2.toString());
            return s2;
        }
    }
}
