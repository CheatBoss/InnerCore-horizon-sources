package com.google.android.gms.tasks;

import java.util.concurrent.*;

final class zzt implements Executor
{
    @Override
    public final void execute(final Runnable runnable) {
        runnable.run();
    }
}
