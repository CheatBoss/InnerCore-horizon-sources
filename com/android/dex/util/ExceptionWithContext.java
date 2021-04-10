package com.android.dex.util;

import java.io.*;

public class ExceptionWithContext extends RuntimeException
{
    private StringBuffer context;
    
    public ExceptionWithContext(final String s) {
        this(s, null);
    }
    
    public ExceptionWithContext(String s, final Throwable t) {
        if (s == null) {
            if (t != null) {
                s = t.getMessage();
            }
            else {
                s = null;
            }
        }
        super(s, t);
        if (t instanceof ExceptionWithContext) {
            s = ((ExceptionWithContext)t).context.toString();
            (this.context = new StringBuffer(s.length() + 200)).append(s);
            return;
        }
        this.context = new StringBuffer(200);
    }
    
    public ExceptionWithContext(final Throwable t) {
        this(null, t);
    }
    
    public static ExceptionWithContext withContext(final Throwable t, final String s) {
        ExceptionWithContext exceptionWithContext;
        if (t instanceof ExceptionWithContext) {
            exceptionWithContext = (ExceptionWithContext)t;
        }
        else {
            exceptionWithContext = new ExceptionWithContext(t);
        }
        exceptionWithContext.addContext(s);
        return exceptionWithContext;
    }
    
    public void addContext(final String s) {
        if (s == null) {
            throw new NullPointerException("str == null");
        }
        this.context.append(s);
        if (!s.endsWith("\n")) {
            this.context.append('\n');
        }
    }
    
    public String getContext() {
        return this.context.toString();
    }
    
    public void printContext(final PrintStream printStream) {
        printStream.println(this.getMessage());
        printStream.print(this.context);
    }
    
    public void printContext(final PrintWriter printWriter) {
        printWriter.println(this.getMessage());
        printWriter.print(this.context);
    }
    
    @Override
    public void printStackTrace(final PrintStream printStream) {
        super.printStackTrace(printStream);
        printStream.println(this.context);
    }
    
    @Override
    public void printStackTrace(final PrintWriter printWriter) {
        super.printStackTrace(printWriter);
        printWriter.println(this.context);
    }
}
