package com.zhekasmirnov.horizon.launcher;

import android.app.*;
import com.zhekasmirnov.horizon.modloader.*;
import com.zhekasmirnov.horizon.modloader.repo.storage.*;
import com.zhekasmirnov.horizon.runtime.task.*;
import com.zhekasmirnov.horizon.compiler.packages.*;
import android.content.*;
import java.io.*;

public class ContextHolder
{
    private Activity context;
    private ExecutionDirectory executionDir;
    private TemporaryStorage temporaryStorage;
    private TaskManager taskManager;
    
    public ContextHolder(final Activity context, final ExecutionDirectory executionDir, final TemporaryStorage temporaryStorage, final TaskManager taskManager) {
        this.context = context;
        this.executionDir = executionDir;
        this.temporaryStorage = temporaryStorage;
        this.taskManager = taskManager;
    }
    
    public ContextHolder(final Activity context) {
        this(context, new ExecutionDirectory(Environment.getPackExecutionDir((Context)context), true), new TemporaryStorage(new File(Environment.getDataDirFile((Context)context), "tmploc")), new TaskManager());
    }
    
    public Activity getContext() {
        return this.context;
    }
    
    public TaskManager getTaskManager() {
        return this.taskManager;
    }
    
    public ExecutionDirectory getExecutionDir() {
        return this.executionDir;
    }
    
    public TemporaryStorage getTemporaryStorage() {
        return this.temporaryStorage;
    }
}
