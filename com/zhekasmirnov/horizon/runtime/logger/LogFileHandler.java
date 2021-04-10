package com.zhekasmirnov.horizon.runtime.logger;

import java.io.*;
import java.util.*;
import android.os.*;

public class LogFileHandler
{
    private static LogFileHandler instance;
    private final File directory;
    private final int numLastLogsStored;
    private boolean initializedWithErrors;
    
    public static LogFileHandler getInstance() {
        return LogFileHandler.instance;
    }
    
    public LogFileHandler(final File directory, final int numLastLogsStored) {
        this.initializedWithErrors = false;
        this.directory = directory;
        this.numLastLogsStored = numLastLogsStored;
    }
    
    private void initializeInDirectory() {
        if (!this.directory.isDirectory()) {
            this.directory.mkdirs();
            if (this.directory.isDirectory()) {
                this.initializedWithErrors = true;
            }
        }
    }
    
    public File getExistingLogFile(final String name) {
        this.initializeInDirectory();
        if (this.initializedWithErrors) {
            return null;
        }
        final File log = new File(this.directory, name);
        return log.exists() ? log : null;
    }
    
    private boolean renameOldRecursive(final File file, final String name, final int index) {
        final File older = new File(this.directory, name + "." + index);
        if (older.exists()) {
            if (index > this.numLastLogsStored) {
                return older.delete();
            }
            if (!this.renameOldRecursive(older, name, index + 1)) {
                return false;
            }
        }
        return file.renameTo(older);
    }
    
    public void archiveOldFile(final String name) {
        final File log = new File(this.directory, name);
        if (log.exists()) {
            this.renameOldRecursive(log, name, 0);
        }
    }
    
    public File getNewLogFile(final String name) {
        this.initializeInDirectory();
        if (this.initializedWithErrors) {
            return null;
        }
        final File log = new File(this.directory, name);
        if (log.exists() && log.length() > 0L) {
            this.renameOldRecursive(log, name, 0);
        }
        return log;
    }
    
    public List<File> getAllFiles(final String name) {
        final List<File> files = new ArrayList<File>();
        for (final File file : this.directory.listFiles()) {
            if (file.getName().startsWith(name) && file.length() > 0L) {
                files.add(file);
            }
        }
        return files;
    }
    
    static {
        LogFileHandler.instance = new LogFileHandler(new File(Environment.getExternalStorageDirectory(), "games/horizon/logs"), 5);
    }
}
