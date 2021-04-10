package com.faendir.rhino_android;

import android.content.*;
import org.mozilla.javascript.*;
import android.support.annotation.*;
import java.io.*;

public class RhinoAndroidHelper
{
    private final File cacheDirectory;
    
    public RhinoAndroidHelper() {
        this(new File(System.getProperty("java.io.tmpdir", "."), "classes"));
    }
    
    public RhinoAndroidHelper(final Context context) {
        this(new File(context.getCacheDir(), "classes"));
    }
    
    public RhinoAndroidHelper(final File cacheDirectory) {
        this.cacheDirectory = cacheDirectory;
    }
    
    @Deprecated
    public static org.mozilla.javascript.Context prepareContext() {
        return new RhinoAndroidHelper().enterContext();
    }
    
    public org.mozilla.javascript.Context enterContext() {
        if (!SecurityController.hasGlobal()) {
            SecurityController.initGlobal((SecurityController)new NoSecurityController());
        }
        return this.getContextFactory().enterContext();
    }
    
    @VisibleForTesting
    public AndroidContextFactory getContextFactory() {
        if (!ContextFactory.hasExplicitGlobal()) {
            final AndroidContextFactory contextFactoryGlobal = new AndroidContextFactory(this.cacheDirectory);
            ContextFactory.getGlobalSetter().setContextFactoryGlobal((ContextFactory)contextFactoryGlobal);
            return contextFactoryGlobal;
        }
        if (!(ContextFactory.getGlobal() instanceof AndroidContextFactory)) {
            throw new IllegalStateException("Cannot initialize factory for Android Rhino: There is already another factory");
        }
        return (AndroidContextFactory)ContextFactory.getGlobal();
    }
    
    public void loadClassJar(final File file) throws IOException {
        ((AndroidClassLoader)this.getContextFactory().getApplicationClassLoader()).loadJar(file);
    }
}
