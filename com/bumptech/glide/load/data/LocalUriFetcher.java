package com.bumptech.glide.load.data;

import android.net.*;
import android.util.*;
import com.bumptech.glide.*;
import android.content.*;
import java.io.*;

public abstract class LocalUriFetcher<T> implements DataFetcher<T>
{
    private static final String TAG = "LocalUriFetcher";
    private final Context context;
    private T data;
    private final Uri uri;
    
    public LocalUriFetcher(final Context context, final Uri uri) {
        this.context = context.getApplicationContext();
        this.uri = uri;
    }
    
    @Override
    public void cancel() {
    }
    
    @Override
    public void cleanup() {
        if (this.data != null) {
            try {
                this.close(this.data);
            }
            catch (IOException ex) {
                if (Log.isLoggable("LocalUriFetcher", 2)) {
                    Log.v("LocalUriFetcher", "failed to close data", (Throwable)ex);
                }
            }
        }
    }
    
    protected abstract void close(final T p0) throws IOException;
    
    @Override
    public String getId() {
        return this.uri.toString();
    }
    
    @Override
    public final T loadData(final Priority priority) throws Exception {
        return this.data = this.loadResource(this.uri, this.context.getContentResolver());
    }
    
    protected abstract T loadResource(final Uri p0, final ContentResolver p1) throws FileNotFoundException;
}
