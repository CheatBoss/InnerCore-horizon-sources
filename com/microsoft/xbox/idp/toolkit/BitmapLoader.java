package com.microsoft.xbox.idp.toolkit;

import android.content.*;
import android.util.*;
import android.graphics.*;
import java.net.*;
import java.io.*;

public class BitmapLoader extends WorkerLoader<Result>
{
    private static final String TAG;
    
    static {
        TAG = BitmapLoader.class.getSimpleName();
    }
    
    public BitmapLoader(final Context context, final Cache cache, final Object o, final String s) {
        super(context, (Worker)new MyWorker(cache, o, s));
    }
    
    public BitmapLoader(final Context context, final String s) {
        this(context, null, null, s);
    }
    
    @Override
    protected boolean isDataReleased(final Result result) {
        return result.isReleased();
    }
    
    @Override
    protected void releaseData(final Result result) {
        result.release();
    }
    
    public interface Cache
    {
        void clear();
        
        Bitmap get(final Object p0);
        
        Bitmap put(final Object p0, final Bitmap p1);
        
        Bitmap remove(final Object p0);
    }
    
    private static class MyWorker implements Worker<Result>
    {
        private final Cache cache;
        private final Object resultKey;
        private final String urlString;
        
        private MyWorker(final Cache cache, final Object resultKey, final String urlString) {
            this.cache = cache;
            this.resultKey = resultKey;
            this.urlString = urlString;
        }
        
        private boolean hasCache() {
            return this.cache != null && this.resultKey != null;
        }
        
        @Override
        public void cancel() {
        }
        
        @Override
        public void start(final ResultListener<Result> resultListener) {
            while (true) {
                Label_0071: {
                    if (!this.hasCache()) {
                        break Label_0071;
                    }
                    synchronized (this.cache) {
                        final Bitmap value = this.cache.get(this.resultKey);
                        // monitorexit(this.cache)
                        if (value != null) {
                            Log.d(BitmapLoader.TAG, "Successfully retrieved Bitmap from BitmapLoader.Cache");
                            final Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    resultListener.onResult(new Result(value));
                                }
                            });
                            thread.start();
                            return;
                        }
                    }
                }
                final Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final URL url = new URL(MyWorker.this.urlString);
                            final String access$100 = BitmapLoader.TAG;
                            final StringBuilder sb = new StringBuilder();
                            sb.append("url created: ");
                            sb.append(url);
                            Log.d(access$100, sb.toString());
                            final InputStream openStream = url.openStream();
                            try {
                                final Bitmap decodeStream = BitmapFactory.decodeStream(openStream);
                                if (MyWorker.this.hasCache()) {
                                    synchronized (MyWorker.this.cache) {
                                        Log.d(BitmapLoader.TAG, "Caching retrieved bitmap");
                                        MyWorker.this.cache.put(MyWorker.this.resultKey, decodeStream);
                                    }
                                }
                                resultListener.onResult(new Result(decodeStream));
                            }
                            catch (Exception ex) {
                                resultListener.onResult(new Result(ex));
                            }
                            openStream.close();
                        }
                        catch (Exception ex2) {
                            final ResultListener resultListener = resultListener;
                            final Result result = new Result(ex2);
                        }
                        catch (MalformedURLException ex3) {
                            final String access$101 = BitmapLoader.TAG;
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("Received malformed URL: ");
                            sb2.append(MyWorker.this.urlString);
                            Log.e(access$101, sb2.toString());
                            final ResultListener resultListener = resultListener;
                            final Result result = new Result(ex3);
                            goto Label_0177;
                        }
                    }
                });
                continue;
            }
        }
    }
    
    public static class Result extends LoaderResult<Bitmap>
    {
        protected Result(final Bitmap bitmap) {
            super(bitmap, null);
        }
        
        protected Result(final Exception ex) {
            super(ex);
        }
        
        @Override
        public boolean isReleased() {
            return this.hasData() && this.getData().isRecycled();
        }
        
        @Override
        public void release() {
            if (this.hasData()) {
                this.getData().recycle();
            }
        }
    }
}
