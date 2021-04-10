package com.faendir.rhino_android;

import android.support.annotation.*;
import java.io.*;
import org.mozilla.javascript.*;

@VisibleForTesting
public class AndroidContextFactory extends ContextFactory
{
    private final File cacheDirectory;
    
    public AndroidContextFactory(final File cacheDirectory) {
        this.cacheDirectory = cacheDirectory;
        this.initApplicationClassLoader((ClassLoader)this.createClassLoader(AndroidContextFactory.class.getClassLoader()));
    }
    
    protected AndroidClassLoader createClassLoader(final ClassLoader classLoader) {
        return new AndroidClassLoader(classLoader, this.cacheDirectory);
    }
    
    protected void onContextReleased(final Context context) {
        super.onContextReleased(context);
        ((AndroidClassLoader)context.getApplicationClassLoader()).reset();
    }
}
