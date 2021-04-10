package com.microsoft.aad.adal;

import java.util.concurrent.atomic.*;
import android.os.*;

final class CallbackExecutor<T>
{
    private static final String TAG;
    private final AtomicReference<Callback<T>> mCallbackReference;
    private final Handler mHandler;
    
    static {
        TAG = CallbackExecutor.class.getSimpleName();
    }
    
    CallbackExecutor(final Callback<T> callback) {
        Handler mHandler = null;
        this.mCallbackReference = new AtomicReference<Callback<T>>(null);
        if (Looper.myLooper() != null) {
            mHandler = new Handler();
        }
        this.mHandler = mHandler;
        this.mCallbackReference.set(callback);
    }
    
    public void onError(final Throwable t) {
        final Callback<T> callback = this.mCallbackReference.getAndSet(null);
        if (callback == null) {
            Logger.v(CallbackExecutor.TAG, "Callback does not exist.");
            return;
        }
        final Handler mHandler = this.mHandler;
        if (mHandler == null) {
            callback.onError(t);
            return;
        }
        mHandler.post((Runnable)new Runnable() {
            @Override
            public void run() {
                callback.onError(t);
            }
        });
    }
    
    public void onSuccess(final T t) {
        final Callback<T> callback = this.mCallbackReference.getAndSet(null);
        if (callback == null) {
            Logger.v(CallbackExecutor.TAG, "Callback does not exist.");
            return;
        }
        final Handler mHandler = this.mHandler;
        if (mHandler == null) {
            callback.onSuccess(t);
            return;
        }
        mHandler.post((Runnable)new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(t);
            }
        });
    }
}
