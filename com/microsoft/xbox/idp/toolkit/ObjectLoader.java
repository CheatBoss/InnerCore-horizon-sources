package com.microsoft.xbox.idp.toolkit;

import android.content.*;
import com.google.gson.*;
import com.microsoft.xbox.idp.util.*;
import java.io.*;

public class ObjectLoader<T> extends WorkerLoader<Result<T>>
{
    private static final String TAG;
    
    static {
        TAG = ObjectLoader.class.getSimpleName();
    }
    
    public ObjectLoader(final Context context, final Cache cache, final Object o, final Class<T> clazz, final Gson gson, final HttpCall httpCall) {
        super(context, (Worker<?>)new MyWorker(cache, o, (Class)clazz, gson, httpCall));
    }
    
    public ObjectLoader(final Context context, final Class<T> clazz, final Gson gson, final HttpCall httpCall) {
        this(context, null, null, clazz, gson, httpCall);
    }
    
    @Override
    protected boolean isDataReleased(final Result<T> result) {
        return result.isReleased();
    }
    
    @Override
    protected void releaseData(final Result<T> result) {
        result.release();
    }
    
    public interface Cache
    {
        void clear();
        
         <T> Result<T> get(final Object p0);
        
         <T> Result<T> put(final Object p0, final Result<T> p1);
        
         <T> Result<T> remove(final Object p0);
    }
    
    private static class MyWorker<T> implements Worker<Result<T>>
    {
        private final Cache cache;
        private final Class<T> cls;
        private final Gson gson;
        private final HttpCall httpCall;
        private final Object resultKey;
        
        private MyWorker(final Cache cache, final Object resultKey, final Class<T> cls, final Gson gson, final HttpCall httpCall) {
            this.cache = cache;
            this.resultKey = resultKey;
            this.cls = cls;
            this.gson = gson;
            this.httpCall = httpCall;
        }
        
        private boolean hasCache() {
            return this.cache != null && this.resultKey != null;
        }
        
        @Override
        public void cancel() {
        }
        
        @Override
        public void start(final ResultListener<Result<T>> resultListener) {
            if (this.hasCache()) {
                synchronized (this.cache) {
                    final Result<Object> value = this.cache.get(this.resultKey);
                    // monitorexit(this.cache)
                    if (value != null) {
                        resultListener.onResult((Result)value);
                        return;
                    }
                }
            }
            this.httpCall.getResponseAsync((HttpCall.Callback)new HttpCall.Callback() {
                @Override
                public void processResponse(final int n, InputStream inputStream, HttpHeaders httpHeaders) throws Exception {
                    if (n >= 200) {
                        if (n <= 299) {
                            if (MyWorker.this.cls == Void.class) {
                                resultListener.onResult(new Result<Object>((Object)null));
                                return;
                            }
                            httpHeaders = (HttpHeaders)new StringWriter();
                            try {
                                inputStream = (InputStream)new InputStreamReader(new BufferedInputStream(inputStream));
                                try {
                                    final Result result = new Result(MyWorker.this.gson.fromJson((Reader)inputStream, MyWorker.this.cls));
                                    if (MyWorker.this.hasCache()) {
                                        synchronized (MyWorker.this.cache) {
                                            MyWorker.this.cache.put(MyWorker.this.resultKey, (Result<Object>)result);
                                        }
                                    }
                                    resultListener.onResult(result);
                                    return;
                                }
                                finally {
                                    ((InputStreamReader)inputStream).close();
                                }
                            }
                            finally {
                                ((StringWriter)httpHeaders).close();
                            }
                        }
                    }
                    final Result result2 = new Result(new HttpError(n, n, inputStream));
                    if (MyWorker.this.hasCache()) {
                        synchronized (MyWorker.this.cache) {
                            MyWorker.this.cache.put(MyWorker.this.resultKey, (Result<Object>)result2);
                        }
                    }
                    resultListener.onResult(result2);
                }
            });
        }
    }
    
    public static class Result<T> extends LoaderResult<T>
    {
        protected Result(final HttpError httpError) {
            super(null, httpError);
        }
        
        protected Result(final T t) {
            super(t, null);
        }
        
        @Override
        public boolean isReleased() {
            return true;
        }
        
        @Override
        public void release() {
        }
    }
}
