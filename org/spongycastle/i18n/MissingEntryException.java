package org.spongycastle.i18n;

import java.util.*;
import java.net.*;

public class MissingEntryException extends RuntimeException
{
    private String debugMsg;
    protected final String key;
    protected final ClassLoader loader;
    protected final Locale locale;
    protected final String resource;
    
    public MissingEntryException(final String s, final String resource, final String key, final Locale locale, final ClassLoader loader) {
        super(s);
        this.resource = resource;
        this.key = key;
        this.locale = locale;
        this.loader = loader;
    }
    
    public MissingEntryException(final String s, final Throwable t, final String resource, final String key, final Locale locale, final ClassLoader loader) {
        super(s, t);
        this.resource = resource;
        this.key = key;
        this.locale = locale;
        this.loader = loader;
    }
    
    public ClassLoader getClassLoader() {
        return this.loader;
    }
    
    public String getDebugMsg() {
        if (this.debugMsg == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Can not find entry ");
            sb.append(this.key);
            sb.append(" in resource file ");
            sb.append(this.resource);
            sb.append(" for the locale ");
            sb.append(this.locale);
            sb.append(".");
            this.debugMsg = sb.toString();
            final ClassLoader loader = this.loader;
            if (loader instanceof URLClassLoader) {
                final URL[] urLs = ((URLClassLoader)loader).getURLs();
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(this.debugMsg);
                sb2.append(" The following entries in the classpath were searched: ");
                this.debugMsg = sb2.toString();
                for (int i = 0; i != urLs.length; ++i) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append(this.debugMsg);
                    sb3.append(urLs[i]);
                    sb3.append(" ");
                    this.debugMsg = sb3.toString();
                }
            }
        }
        return this.debugMsg;
    }
    
    public String getKey() {
        return this.key;
    }
    
    public Locale getLocale() {
        return this.locale;
    }
    
    public String getResource() {
        return this.resource;
    }
}
