package android.support.v4.os;

import android.os.*;

public final class AsyncTaskCompat
{
    private AsyncTaskCompat() {
    }
    
    public static <Params, Progress, Result> AsyncTask<Params, Progress, Result> executeParallel(final AsyncTask<Params, Progress, Result> asyncTask, final Params... array) {
        if (asyncTask == null) {
            throw new IllegalArgumentException("task can not be null");
        }
        if (Build$VERSION.SDK_INT >= 11) {
            AsyncTaskCompatHoneycomb.executeParallel((android.os.AsyncTask<Params, Object, Object>)asyncTask, array);
            return asyncTask;
        }
        asyncTask.execute((Object[])array);
        return asyncTask;
    }
}
