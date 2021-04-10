package org.mozilla.javascript;

public class WrappedException extends EvaluatorException
{
    static final long serialVersionUID = -1551979216966520648L;
    private Throwable exception;
    
    public WrappedException(final Throwable exception) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Wrapped ");
        sb.append(exception.toString());
        super(sb.toString());
        Kit.initCause(this, this.exception = exception);
        final int[] array = { 0 };
        final String sourcePositionFromStack = Context.getSourcePositionFromStack(array);
        final int n = array[0];
        if (sourcePositionFromStack != null) {
            this.initSourceName(sourcePositionFromStack);
        }
        if (n != 0) {
            this.initLineNumber(n);
        }
    }
    
    public Throwable getWrappedException() {
        return this.exception;
    }
    
    @Deprecated
    public Object unwrap() {
        return this.getWrappedException();
    }
}
