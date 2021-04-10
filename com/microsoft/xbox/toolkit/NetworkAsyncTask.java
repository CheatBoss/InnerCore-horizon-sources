package com.microsoft.xbox.toolkit;

import com.microsoft.xbox.toolkit.network.*;

public abstract class NetworkAsyncTask<T> extends XLEAsyncTask<T>
{
    protected boolean forceLoad;
    private boolean shouldExecute;
    
    public NetworkAsyncTask() {
        super(XLEThreadPool.networkOperationsThreadPool);
        this.forceLoad = true;
        this.shouldExecute = true;
    }
    
    public NetworkAsyncTask(final XLEThreadPool xleThreadPool) {
        super(XLEThreadPool.networkOperationsThreadPool);
        this.forceLoad = true;
        this.shouldExecute = true;
    }
    
    protected abstract boolean checkShouldExecute();
    
    @Override
    protected final T doInBackground() {
        try {
            return this.loadDataInBackground();
        }
        catch (Exception ex) {
            return this.onError();
        }
    }
    
    @Override
    public void execute() {
        XLEAssert.assertTrue(Thread.currentThread() == ThreadManager.UIThread);
        final boolean cancelled = this.cancelled;
        final boolean checkShouldExecute = this.checkShouldExecute();
        this.shouldExecute = checkShouldExecute;
        if (!checkShouldExecute && !this.forceLoad) {
            this.onNoAction();
            this.isBusy = false;
            return;
        }
        this.isBusy = true;
        this.onPreExecute();
        super.executeBackground();
    }
    
    public void load(final boolean forceLoad) {
        this.forceLoad = forceLoad;
        this.execute();
    }
    
    protected abstract T loadDataInBackground();
    
    protected abstract T onError();
    
    protected abstract void onNoAction();
}
