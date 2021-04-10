package com.google.android.vending.expansion.downloader.impl;

import android.app.*;
import android.content.*;
import android.util.*;
import android.os.*;

public abstract class CustomIntentService extends Service
{
    private static final String LOG_TAG = "CustomIntentService";
    private static final int WHAT_MESSAGE = -10;
    private String mName;
    private boolean mRedelivery;
    private volatile ServiceHandler mServiceHandler;
    private volatile Looper mServiceLooper;
    
    public CustomIntentService(final String mName) {
        this.mName = mName;
    }
    
    public IBinder onBind(final Intent intent) {
        return null;
    }
    
    public void onCreate() {
        super.onCreate();
        final StringBuilder sb = new StringBuilder();
        sb.append("IntentService[");
        sb.append(this.mName);
        sb.append("]");
        final HandlerThread handlerThread = new HandlerThread(sb.toString());
        handlerThread.start();
        this.mServiceLooper = handlerThread.getLooper();
        this.mServiceHandler = new ServiceHandler(this.mServiceLooper);
    }
    
    public void onDestroy() {
        final Thread thread = this.mServiceLooper.getThread();
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
        this.mServiceLooper.quit();
        Log.d("CustomIntentService", "onDestroy");
    }
    
    protected abstract void onHandleIntent(final Intent p0);
    
    public void onStart(final Intent obj, final int arg1) {
        if (!this.mServiceHandler.hasMessages(-10)) {
            final Message obtainMessage = this.mServiceHandler.obtainMessage();
            obtainMessage.arg1 = arg1;
            obtainMessage.obj = obj;
            obtainMessage.what = -10;
            this.mServiceHandler.sendMessage(obtainMessage);
        }
    }
    
    public int onStartCommand(final Intent intent, final int n, final int n2) {
        this.onStart(intent, n2);
        if (this.mRedelivery) {
            return 3;
        }
        return 2;
    }
    
    public void setIntentRedelivery(final boolean mRedelivery) {
        this.mRedelivery = mRedelivery;
    }
    
    protected abstract boolean shouldStop();
    
    private final class ServiceHandler extends Handler
    {
        public ServiceHandler(final Looper looper) {
            super(looper);
        }
        
        public void handleMessage(final Message message) {
            CustomIntentService.this.onHandleIntent((Intent)message.obj);
            if (CustomIntentService.this.shouldStop()) {
                Log.d("CustomIntentService", "stopSelf");
                CustomIntentService.this.stopSelf(message.arg1);
                Log.d("CustomIntentService", "afterStopSelf");
            }
        }
    }
}
