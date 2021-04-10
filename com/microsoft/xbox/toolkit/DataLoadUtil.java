package com.microsoft.xbox.toolkit;

import java.util.*;
import com.microsoft.xbox.toolkit.network.*;
import com.microsoft.xbox.xle.app.*;

public class DataLoadUtil
{
    public static <T> AsyncResult<T> Load(final boolean b, final long n, final Date date, final SingleEntryLoadingStatus singleEntryLoadingStatus, final IDataLoaderRunnable<T> dataLoaderRunnable) {
        XLEAssert.assertNotNull(singleEntryLoadingStatus);
        XLEAssert.assertNotNull(dataLoaderRunnable);
        XLEAssert.assertIsNotUIThread();
        final SingleEntryLoadingStatus.WaitResult waitForNotLoading = singleEntryLoadingStatus.waitForNotLoading();
        if (!waitForNotLoading.waited) {
            if (!XLEUtil.shouldRefresh(date, n) && !b) {
                singleEntryLoadingStatus.setSuccess();
                return safeReturnResult((T)null, dataLoaderRunnable, null, AsyncActionStatus.NO_CHANGE);
            }
            ThreadManager.UIThreadSend(new Runnable() {
                @Override
                public void run() {
                    dataLoaderRunnable.onPreExecute();
                }
            });
            final int shouldRetryCountOnTokenError = dataLoaderRunnable.getShouldRetryCountOnTokenError();
            int i = 0;
            XLEException failed = null;
            while (i <= shouldRetryCountOnTokenError) {
                try {
                    final T buildData = dataLoaderRunnable.buildData();
                    postExecute(buildData, dataLoaderRunnable, null, AsyncActionStatus.SUCCESS);
                    singleEntryLoadingStatus.setSuccess();
                    return new AsyncResult<T>(buildData, dataLoaderRunnable, null, AsyncActionStatus.SUCCESS);
                }
                catch (Exception ex) {
                    failed = new XLEException(dataLoaderRunnable.getDefaultErrorCode(), ex);
                }
                catch (XLEException failed) {
                    if (failed.getErrorCode() == 1020L) {
                        ++i;
                        continue;
                    }
                    failed.getErrorCode();
                }
                break;
            }
            singleEntryLoadingStatus.setFailed(failed);
            return safeReturnResult((T)null, dataLoaderRunnable, failed, AsyncActionStatus.FAIL);
        }
        else {
            final XLEException error = waitForNotLoading.error;
            if (error == null) {
                return safeReturnResult((T)null, dataLoaderRunnable, null, AsyncActionStatus.NO_OP_SUCCESS);
            }
            return safeReturnResult((T)null, dataLoaderRunnable, error, AsyncActionStatus.NO_OP_FAIL);
        }
    }
    
    public static <T> NetworkAsyncTask StartLoadFromUI(final boolean b, final long n, final Date date, final SingleEntryLoadingStatus singleEntryLoadingStatus, final IDataLoaderRunnable<T> dataLoaderRunnable) {
        final NetworkAsyncTask<T> networkAsyncTask = new NetworkAsyncTask<T>() {
            @Override
            protected boolean checkShouldExecute() {
                return this.forceLoad;
            }
            
            @Override
            protected T loadDataInBackground() {
                return DataLoadUtil.Load(this.forceLoad, n, date, singleEntryLoadingStatus, dataLoaderRunnable).getResult();
            }
            
            @Override
            protected T onError() {
                return null;
            }
            
            @Override
            protected void onNoAction() {
            }
            
            @Override
            protected void onPostExecute(final T t) {
            }
            
            @Override
            protected void onPreExecute() {
            }
        };
        networkAsyncTask.execute();
        return networkAsyncTask;
    }
    
    private static <T> void postExecute(final T t, final IDataLoaderRunnable<T> dataLoaderRunnable, final XLEException ex, final AsyncActionStatus asyncActionStatus) {
        ThreadManager.UIThreadSend(new Runnable() {
            @Override
            public void run() {
                dataLoaderRunnable.onPostExcute(new AsyncResult<Object>(t, dataLoaderRunnable, ex, asyncActionStatus));
            }
        });
    }
    
    private static <T> AsyncResult<T> safeReturnResult(final T t, final IDataLoaderRunnable<T> dataLoaderRunnable, final XLEException ex, final AsyncActionStatus asyncActionStatus) {
        postExecute(t, dataLoaderRunnable, ex, asyncActionStatus);
        return new AsyncResult<T>(t, dataLoaderRunnable, ex, asyncActionStatus);
    }
}
