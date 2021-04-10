package com.microsoft.xbox.service.model;

import java.util.*;
import com.microsoft.xbox.toolkit.network.*;
import com.microsoft.xbox.xle.app.*;
import com.microsoft.xbox.toolkit.*;

public abstract class ModelBase<T> extends XLEObservable<UpdateData> implements ModelData<T>
{
    protected static final long MilliSecondsInADay = 86400000L;
    protected static final long MilliSecondsInAnHour = 3600000L;
    protected static final long MilliSecondsInHalfHour = 1800000L;
    protected boolean isLoading;
    protected long lastInvalidatedTick;
    protected Date lastRefreshTime;
    protected long lifetime;
    protected IDataLoaderRunnable<T> loaderRunnable;
    private SingleEntryLoadingStatus loadingStatus;
    
    public ModelBase() {
        this.lifetime = 86400000L;
        this.isLoading = false;
        this.lastInvalidatedTick = 0L;
        this.loadingStatus = new SingleEntryLoadingStatus();
    }
    
    public boolean getIsLoading() {
        return this.loadingStatus.getIsLoading();
    }
    
    public boolean hasValidData() {
        return this.lastRefreshTime != null;
    }
    
    public void invalidateData() {
        this.lastRefreshTime = null;
    }
    
    protected boolean isLoaded() {
        return this.lastRefreshTime != null;
    }
    
    protected AsyncResult<T> loadData(final boolean b, final IDataLoaderRunnable<T> dataLoaderRunnable) {
        XLEAssert.assertIsNotUIThread();
        return DataLoadUtil.Load(b, this.lifetime, this.lastRefreshTime, this.loadingStatus, dataLoaderRunnable);
    }
    
    protected void loadInternal(final boolean b, final UpdateType updateType, final IDataLoaderRunnable<T> dataLoaderRunnable) {
        this.loadInternal(b, updateType, dataLoaderRunnable, this.lastRefreshTime);
    }
    
    protected void loadInternal(final boolean b, final UpdateType updateType, final IDataLoaderRunnable<T> dataLoaderRunnable, final Date date) {
        XLEAssert.assertIsUIThread();
        AsyncResult<UpdateData> asyncResult;
        if (!this.getIsLoading() && (b || this.shouldRefresh(date))) {
            DataLoadUtil.StartLoadFromUI(b, this.lifetime, this.lastRefreshTime, this.loadingStatus, dataLoaderRunnable);
            asyncResult = new AsyncResult<UpdateData>(new UpdateData(updateType, false), this, null);
        }
        else {
            asyncResult = new AsyncResult<UpdateData>(new UpdateData(updateType, this.getIsLoading() ^ true), this, null);
        }
        this.notifyObservers(asyncResult);
    }
    
    public boolean shouldRefresh() {
        return this.shouldRefresh(this.lastRefreshTime);
    }
    
    protected boolean shouldRefresh(final Date date) {
        return XLEUtil.shouldRefresh(date, this.lifetime);
    }
    
    @Override
    public void updateWithNewData(final AsyncResult<T> asyncResult) {
        this.isLoading = false;
        if (asyncResult.getException() == null && asyncResult.getStatus() == AsyncActionStatus.SUCCESS) {
            this.lastRefreshTime = new Date();
        }
    }
}
