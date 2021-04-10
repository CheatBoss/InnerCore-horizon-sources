package com.microsoft.xbox.toolkit;

import com.microsoft.xbox.toolkit.network.*;

public abstract class XLEAsyncTask<Result>
{
    protected boolean cancelled;
    private XLEAsyncTask chainedTask;
    private Runnable doBackgroundAndPostExecuteRunnable;
    protected boolean isBusy;
    private XLEThreadPool threadPool;
    
    public XLEAsyncTask(final XLEThreadPool threadPool) {
        this.doBackgroundAndPostExecuteRunnable = null;
        this.threadPool = null;
        this.cancelled = false;
        this.isBusy = false;
        this.chainedTask = null;
        this.threadPool = threadPool;
        this.doBackgroundAndPostExecuteRunnable = new Runnable() {
            @Override
            public void run() {
                Object doInBackground;
                if (!XLEAsyncTask.this.cancelled) {
                    doInBackground = XLEAsyncTask.this.doInBackground();
                }
                else {
                    doInBackground = null;
                }
                ThreadManager.UIThreadPost(new Runnable() {
                    @Override
                    public void run() {
                        XLEAsyncTask.this.isBusy = false;
                        if (!XLEAsyncTask.this.cancelled) {
                            XLEAsyncTask.this.onPostExecute(doInBackground);
                            if (XLEAsyncTask.this.chainedTask != null) {
                                XLEAsyncTask.this.chainedTask.execute();
                            }
                        }
                    }
                });
            }
        };
    }
    
    public static void executeAll(final XLEAsyncTask... array) {
        if (array.length > 0) {
            XLEAsyncTask xleAsyncTask;
            for (int i = 0; i < array.length - 1; ++i, xleAsyncTask.chainedTask = array[i]) {
                xleAsyncTask = array[i];
            }
            array[0].execute();
        }
    }
    
    public void cancel() {
        XLEAssert.assertTrue(Thread.currentThread() == ThreadManager.UIThread);
        this.cancelled = true;
    }
    
    protected abstract Result doInBackground();
    
    public void execute() {
        XLEAssert.assertTrue(Thread.currentThread() == ThreadManager.UIThread);
        this.cancelled = false;
        this.isBusy = true;
        this.onPreExecute();
        this.executeBackground();
    }
    
    protected void executeBackground() {
        this.cancelled = false;
        this.threadPool.run(this.doBackgroundAndPostExecuteRunnable);
    }
    
    public boolean getIsBusy() {
        return this.isBusy && !this.cancelled;
    }
    
    protected abstract void onPostExecute(final Result p0);
    
    protected abstract void onPreExecute();
}
