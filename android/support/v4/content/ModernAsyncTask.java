package android.support.v4.content;

import java.util.concurrent.atomic.*;
import android.util.*;
import java.util.concurrent.*;
import android.os.*;

abstract class ModernAsyncTask<Params, Progress, Result>
{
    private static final int CORE_POOL_SIZE = 5;
    private static final int KEEP_ALIVE = 1;
    private static final String LOG_TAG = "AsyncTask";
    private static final int MAXIMUM_POOL_SIZE = 128;
    private static final int MESSAGE_POST_PROGRESS = 2;
    private static final int MESSAGE_POST_RESULT = 1;
    public static final Executor THREAD_POOL_EXECUTOR;
    private static volatile Executor sDefaultExecutor;
    private static InternalHandler sHandler;
    private static final BlockingQueue<Runnable> sPoolWorkQueue;
    private static final ThreadFactory sThreadFactory;
    private final FutureTask<Result> mFuture;
    private volatile Status mStatus;
    private final AtomicBoolean mTaskInvoked;
    private final WorkerRunnable<Params, Result> mWorker;
    
    static {
        sThreadFactory = new ThreadFactory() {
            private final AtomicInteger mCount = new AtomicInteger(1);
            
            @Override
            public Thread newThread(final Runnable runnable) {
                final StringBuilder sb = new StringBuilder();
                sb.append("ModernAsyncTask #");
                sb.append(this.mCount.getAndIncrement());
                return new Thread(runnable, sb.toString());
            }
        };
        sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(10);
        THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(5, 128, 1L, TimeUnit.SECONDS, ModernAsyncTask.sPoolWorkQueue, ModernAsyncTask.sThreadFactory);
        ModernAsyncTask.sDefaultExecutor = ModernAsyncTask.THREAD_POOL_EXECUTOR;
    }
    
    public ModernAsyncTask() {
        this.mStatus = Status.PENDING;
        this.mTaskInvoked = new AtomicBoolean();
        this.mWorker = (WorkerRunnable<Params, Result>)new WorkerRunnable<Params, Result>() {
            @Override
            public Result call() throws Exception {
                ModernAsyncTask.this.mTaskInvoked.set(true);
                Process.setThreadPriority(10);
                return (Result)ModernAsyncTask.this.postResult(ModernAsyncTask.this.doInBackground(this.mParams));
            }
        };
        this.mFuture = new FutureTask<Result>(this.mWorker) {
            @Override
            protected void done() {
                try {
                    ModernAsyncTask.this.postResultIfNotInvoked(((FutureTask<Object>)this).get());
                }
                catch (Throwable t) {
                    throw new RuntimeException("An error occurred while executing doInBackground()", t);
                }
                catch (CancellationException ex3) {
                    ModernAsyncTask.this.postResultIfNotInvoked(null);
                }
                catch (ExecutionException ex) {
                    throw new RuntimeException("An error occurred while executing doInBackground()", ex.getCause());
                }
                catch (InterruptedException ex2) {
                    Log.w("AsyncTask", (Throwable)ex2);
                }
            }
        };
    }
    
    public static void execute(final Runnable runnable) {
        ModernAsyncTask.sDefaultExecutor.execute(runnable);
    }
    
    private void finish(final Result result) {
        if (this.isCancelled()) {
            this.onCancelled(result);
        }
        else {
            this.onPostExecute(result);
        }
        this.mStatus = Status.FINISHED;
    }
    
    private static Handler getHandler() {
        synchronized (ModernAsyncTask.class) {
            if (ModernAsyncTask.sHandler == null) {
                ModernAsyncTask.sHandler = new InternalHandler();
            }
            return ModernAsyncTask.sHandler;
        }
    }
    
    private Result postResult(final Result result) {
        getHandler().obtainMessage(1, (Object)new AsyncTaskResult(this, new Object[] { result })).sendToTarget();
        return result;
    }
    
    private void postResultIfNotInvoked(final Result result) {
        if (!this.mTaskInvoked.get()) {
            this.postResult(result);
        }
    }
    
    public static void setDefaultExecutor(final Executor sDefaultExecutor) {
        ModernAsyncTask.sDefaultExecutor = sDefaultExecutor;
    }
    
    public final boolean cancel(final boolean b) {
        return this.mFuture.cancel(b);
    }
    
    protected abstract Result doInBackground(final Params... p0);
    
    public final ModernAsyncTask<Params, Progress, Result> execute(final Params... array) {
        return this.executeOnExecutor(ModernAsyncTask.sDefaultExecutor, array);
    }
    
    public final ModernAsyncTask<Params, Progress, Result> executeOnExecutor(final Executor executor, final Params... mParams) {
        if (this.mStatus != Status.PENDING) {
            switch (this.mStatus) {
                case FINISHED: {
                    throw new IllegalStateException("Cannot execute task: the task has already been executed (a task can be executed only once)");
                }
                case RUNNING: {
                    throw new IllegalStateException("Cannot execute task: the task is already running.");
                }
            }
        }
        this.mStatus = Status.RUNNING;
        this.onPreExecute();
        this.mWorker.mParams = mParams;
        executor.execute(this.mFuture);
        return this;
    }
    
    public final Result get() throws InterruptedException, ExecutionException {
        return this.mFuture.get();
    }
    
    public final Result get(final long n, final TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        return this.mFuture.get(n, timeUnit);
    }
    
    public final Status getStatus() {
        return this.mStatus;
    }
    
    public final boolean isCancelled() {
        return this.mFuture.isCancelled();
    }
    
    protected void onCancelled() {
    }
    
    protected void onCancelled(final Result result) {
        this.onCancelled();
    }
    
    protected void onPostExecute(final Result result) {
    }
    
    protected void onPreExecute() {
    }
    
    protected void onProgressUpdate(final Progress... array) {
    }
    
    protected final void publishProgress(final Progress... array) {
        if (!this.isCancelled()) {
            getHandler().obtainMessage(2, (Object)new AsyncTaskResult(this, (Object[])array)).sendToTarget();
        }
    }
    
    private static class AsyncTaskResult<Data>
    {
        final Data[] mData;
        final ModernAsyncTask mTask;
        
        AsyncTaskResult(final ModernAsyncTask mTask, final Data... mData) {
            this.mTask = mTask;
            this.mData = mData;
        }
    }
    
    private static class InternalHandler extends Handler
    {
        public InternalHandler() {
            super(Looper.getMainLooper());
        }
        
        public void handleMessage(final Message message) {
            final AsyncTaskResult asyncTaskResult = (AsyncTaskResult)message.obj;
            switch (message.what) {
                default: {}
                case 2: {
                    asyncTaskResult.mTask.onProgressUpdate(asyncTaskResult.mData);
                }
                case 1: {
                    asyncTaskResult.mTask.finish(asyncTaskResult.mData[0]);
                }
            }
        }
    }
    
    public enum Status
    {
        FINISHED, 
        PENDING, 
        RUNNING;
    }
    
    private abstract static class WorkerRunnable<Params, Result> implements Callable<Result>
    {
        Params[] mParams;
    }
}
