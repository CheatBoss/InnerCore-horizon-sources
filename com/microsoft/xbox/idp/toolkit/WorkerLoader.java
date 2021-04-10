package com.microsoft.xbox.idp.toolkit;

import android.content.*;
import android.os.*;

public abstract class WorkerLoader<D> extends Loader<D>
{
    private final Handler dispatcher;
    private final Object lock;
    private D result;
    private ResultListener<D> resultListener;
    private final Worker<D> worker;
    
    public WorkerLoader(final Context context, final Worker<D> worker) {
        super(context);
        this.lock = new Object();
        this.dispatcher = new Handler();
        this.worker = worker;
    }
    
    private boolean cancelLoadCompat() {
        if (Build$VERSION.SDK_INT < 16) {
            return this.onCancelLoad();
        }
        return this.cancelLoad();
    }
    
    public void deliverResult(final D result) {
        if (this.isReset()) {
            if (result != null) {
                this.releaseData(result);
            }
            return;
        }
        final D result2 = this.result;
        this.result = result;
        if (this.isStarted()) {
            super.deliverResult((Object)result);
        }
        if (result2 != null && result2 != result && !this.isDataReleased(result2)) {
            this.releaseData(result2);
        }
    }
    
    protected abstract boolean isDataReleased(final D p0);
    
    protected boolean onCancelLoad() {
        synchronized (this.lock) {
            if (this.resultListener != null) {
                this.worker.cancel();
                this.resultListener = null;
                return true;
            }
            return false;
        }
    }
    
    public void onCanceled(final D n) {
        if (n != null && !this.isDataReleased(n)) {
            this.releaseData(n);
        }
    }
    
    protected void onForceLoad() {
        super.onForceLoad();
        this.cancelLoadCompat();
        synchronized (this.lock) {
            final ResultListenerImpl resultListener = new ResultListenerImpl();
            this.resultListener = (ResultListener<D>)resultListener;
            this.worker.start(resultListener);
        }
    }
    
    protected void onReset() {
        this.cancelLoadCompat();
        final D result = this.result;
        if (result != null && !this.isDataReleased(result)) {
            this.releaseData(this.result);
        }
        this.result = null;
    }
    
    protected void onStartLoading() {
        final D result = this.result;
        if (result != null) {
            this.deliverResult(result);
        }
        if (this.takeContentChanged() || this.result == null) {
            this.forceLoad();
        }
    }
    
    protected void onStopLoading() {
        this.cancelLoadCompat();
    }
    
    protected abstract void releaseData(final D p0);
    
    public interface ResultListener<D>
    {
        void onResult(final D p0);
    }
    
    private class ResultListenerImpl implements ResultListener<D>
    {
        @Override
        public void onResult(final D n) {
            while (true) {
                while (true) {
                    synchronized (WorkerLoader.this.lock) {
                        if (this != WorkerLoader.this.resultListener) {
                            final boolean b = true;
                            WorkerLoader.this.resultListener = null;
                            WorkerLoader.this.dispatcher.post((Runnable)new Runnable() {
                                @Override
                                public void run() {
                                    if (b) {
                                        WorkerLoader.this.onCanceled(n);
                                        return;
                                    }
                                    WorkerLoader.this.deliverResult(n);
                                }
                            });
                            return;
                        }
                    }
                    final boolean b = false;
                    continue;
                }
            }
        }
    }
    
    public interface Worker<D>
    {
        void cancel();
        
        void start(final ResultListener<D> p0);
    }
}
