package com.microsoft.xbox.toolkit;

public class XLEException extends Exception
{
    private long errorCode;
    private boolean isHandled;
    private Object userObject;
    
    public XLEException(final long n) {
        this(n, null, null, null);
    }
    
    public XLEException(final long n, final String s) {
        this(n, s, null, null);
    }
    
    public XLEException(final long n, final String s, final Throwable t) {
        this(n, null, t, null);
    }
    
    public XLEException(final long errorCode, final String s, final Throwable t, final Object userObject) {
        super(s, t);
        this.errorCode = errorCode;
        this.userObject = userObject;
        this.isHandled = false;
    }
    
    public XLEException(final long n, final Throwable t) {
        this(n, null, t, null);
    }
    
    public long getErrorCode() {
        return this.errorCode;
    }
    
    public boolean getIsHandled() {
        return this.isHandled;
    }
    
    public Object getUserObject() {
        return this.userObject;
    }
    
    public void setIsHandled(final boolean isHandled) {
        this.isHandled = isHandled;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        final long errorCode = this.errorCode;
        int i = 0;
        sb.append(String.format("XLEException ErrorCode: %d; ErrorMessage: %s \n\n", errorCode, this.getMessage()));
        if (this.getCause() != null) {
            sb.append(String.format("\t Cause ErrorMessage: %s, StackTrace: ", this.getCause().toString()));
            for (StackTraceElement[] stackTrace = this.getCause().getStackTrace(); i < stackTrace.length; ++i) {
                final StackTraceElement stackTraceElement = stackTrace[i];
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("\n\n \t ");
                sb2.append(stackTraceElement.toString());
                sb.append(sb2.toString());
            }
        }
        return sb.toString();
    }
}
