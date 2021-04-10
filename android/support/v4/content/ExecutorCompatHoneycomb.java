package android.support.v4.content;

import java.util.concurrent.*;
import android.os.*;

class ExecutorCompatHoneycomb
{
    public static Executor getParallelExecutor() {
        return AsyncTask.THREAD_POOL_EXECUTOR;
    }
}
