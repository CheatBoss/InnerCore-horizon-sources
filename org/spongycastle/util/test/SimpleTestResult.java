package org.spongycastle.util.test;

import org.spongycastle.util.*;

public class SimpleTestResult implements TestResult
{
    private static final String SEPARATOR;
    private Throwable exception;
    private String message;
    private boolean success;
    
    static {
        SEPARATOR = Strings.lineSeparator();
    }
    
    public SimpleTestResult(final boolean success, final String message) {
        this.success = success;
        this.message = message;
    }
    
    public SimpleTestResult(final boolean success, final String message, final Throwable exception) {
        this.success = success;
        this.message = message;
        this.exception = exception;
    }
    
    public static TestResult failed(final Test test, final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append(test.getName());
        sb.append(": ");
        sb.append(s);
        return new SimpleTestResult(false, sb.toString());
    }
    
    public static TestResult failed(final Test test, final String s, final Object o, final Object o2) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(SimpleTestResult.SEPARATOR);
        sb.append("Expected: ");
        sb.append(o);
        sb.append(SimpleTestResult.SEPARATOR);
        sb.append("Found   : ");
        sb.append(o2);
        return failed(test, sb.toString());
    }
    
    public static TestResult failed(final Test test, final String s, final Throwable t) {
        final StringBuilder sb = new StringBuilder();
        sb.append(test.getName());
        sb.append(": ");
        sb.append(s);
        return new SimpleTestResult(false, sb.toString(), t);
    }
    
    public static String failedMessage(final String s, final String s2, final String s3, final String s4) {
        final StringBuffer sb = new StringBuffer(s);
        sb.append(" failing ");
        sb.append(s2);
        sb.append(SimpleTestResult.SEPARATOR);
        sb.append("    expected: ");
        sb.append(s3);
        sb.append(SimpleTestResult.SEPARATOR);
        sb.append("    got     : ");
        sb.append(s4);
        return sb.toString();
    }
    
    public static TestResult successful(final Test test, final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append(test.getName());
        sb.append(": ");
        sb.append(s);
        return new SimpleTestResult(true, sb.toString());
    }
    
    @Override
    public Throwable getException() {
        return this.exception;
    }
    
    @Override
    public boolean isSuccessful() {
        return this.success;
    }
    
    @Override
    public String toString() {
        return this.message;
    }
}
