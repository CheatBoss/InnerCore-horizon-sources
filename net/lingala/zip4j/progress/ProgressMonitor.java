package net.lingala.zip4j.progress;

import net.lingala.zip4j.exception.*;

public class ProgressMonitor
{
    public static final int OPERATION_ADD = 0;
    public static final int OPERATION_CALC_CRC = 3;
    public static final int OPERATION_EXTRACT = 1;
    public static final int OPERATION_MERGE = 4;
    public static final int OPERATION_NONE = -1;
    public static final int OPERATION_REMOVE = 2;
    public static final int RESULT_CANCELLED = 3;
    public static final int RESULT_ERROR = 2;
    public static final int RESULT_SUCCESS = 0;
    public static final int RESULT_WORKING = 1;
    public static final int STATE_BUSY = 1;
    public static final int STATE_READY = 0;
    private boolean cancelAllTasks;
    private int currentOperation;
    private Throwable exception;
    private String fileName;
    private boolean pause;
    private int percentDone;
    private int result;
    private int state;
    private long totalWork;
    private long workCompleted;
    
    public ProgressMonitor() {
        this.reset();
        this.percentDone = 0;
    }
    
    public void cancelAllTasks() {
        this.cancelAllTasks = true;
    }
    
    public void endProgressMonitorError(final Throwable exception) throws ZipException {
        this.reset();
        this.result = 2;
        this.exception = exception;
    }
    
    public void endProgressMonitorSuccess() throws ZipException {
        this.reset();
        this.result = 0;
    }
    
    public void fullReset() {
        this.reset();
        this.exception = null;
        this.result = 0;
        this.percentDone = 0;
    }
    
    public int getCurrentOperation() {
        return this.currentOperation;
    }
    
    public Throwable getException() {
        return this.exception;
    }
    
    public String getFileName() {
        return this.fileName;
    }
    
    public int getPercentDone() {
        return this.percentDone;
    }
    
    public int getResult() {
        return this.result;
    }
    
    public int getState() {
        return this.state;
    }
    
    public long getTotalWork() {
        return this.totalWork;
    }
    
    public long getWorkCompleted() {
        return this.workCompleted;
    }
    
    public boolean isCancelAllTasks() {
        return this.cancelAllTasks;
    }
    
    public boolean isPause() {
        return this.pause;
    }
    
    public void reset() {
        this.currentOperation = -1;
        this.state = 0;
        this.fileName = null;
        this.totalWork = 0L;
        this.workCompleted = 0L;
    }
    
    public void setCurrentOperation(final int currentOperation) {
        this.currentOperation = currentOperation;
    }
    
    public void setException(final Throwable exception) {
        this.exception = exception;
    }
    
    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }
    
    public void setPause(final boolean pause) {
        this.pause = pause;
    }
    
    public void setPercentDone(final int percentDone) {
        this.percentDone = percentDone;
    }
    
    public void setResult(final int result) {
        this.result = result;
    }
    
    public void setState(final int state) {
        this.state = state;
    }
    
    public void setTotalWork(final long totalWork) {
        this.totalWork = totalWork;
    }
    
    public void updateWorkCompleted(final long n) {
        this.workCompleted += n;
        this.percentDone = (int)(this.workCompleted * 100L / this.totalWork);
        if (this.percentDone > 100) {
            this.percentDone = 100;
        }
    Label_0056_Outer:
        while (this.pause) {
            while (true) {
                try {
                    Thread.sleep(150L);
                    continue Label_0056_Outer;
                }
                catch (InterruptedException ex) {
                    continue;
                }
                break;
            }
            break;
        }
    }
}
