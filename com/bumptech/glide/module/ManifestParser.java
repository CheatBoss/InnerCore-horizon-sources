package com.bumptech.glide.module;

import android.content.*;
import android.content.pm.*;
import java.util.*;

public final class ManifestParser
{
    private static final String GLIDE_MODULE_VALUE = "GlideModule";
    private final Context context;
    
    public ManifestParser(final Context context) {
        this.context = context;
    }
    
    private static GlideModule parseModule(String forName) {
        try {
            forName = (String)Class.forName(forName);
            try {
                final GlideModule instance = ((Class<GlideModule>)forName).newInstance();
                if (!(instance instanceof GlideModule)) {
                    forName = (String)new StringBuilder();
                    ((StringBuilder)forName).append("Expected instanceof GlideModule, but found: ");
                    ((StringBuilder)forName).append(instance);
                    throw new RuntimeException(((StringBuilder)forName).toString());
                }
                return instance;
            }
            catch (IllegalAccessException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unable to instantiate GlideModule implementation for ");
                sb.append((Object)forName);
                throw new RuntimeException(sb.toString(), ex);
            }
            catch (InstantiationException ex2) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Unable to instantiate GlideModule implementation for ");
                sb2.append((Object)forName);
                throw new RuntimeException(sb2.toString(), ex2);
            }
        }
        catch (ClassNotFoundException ex3) {
            throw new IllegalArgumentException("Unable to find GlideModule implementation", ex3);
        }
    }
    
    public List<GlideModule> parse() {
        final ArrayList<GlideModule> list = new ArrayList<GlideModule>();
        try {
            final ApplicationInfo applicationInfo = this.context.getPackageManager().getApplicationInfo(this.context.getPackageName(), 128);
            if (applicationInfo.metaData != null) {
                for (final String s : applicationInfo.metaData.keySet()) {
                    if ("GlideModule".equals(applicationInfo.metaData.get(s))) {
                        list.add(parseModule(s));
                    }
                }
            }
            return list;
        }
        catch (PackageManager$NameNotFoundException ex) {
            throw new RuntimeException("Unable to find metadata to parse GlideModules", (Throwable)ex);
        }
    }
}
