package okhttp3.internal.connection;

import java.io.*;
import java.lang.reflect.*;

public final class RouteException extends RuntimeException
{
    private static final Method addSuppressedExceptionMethod;
    private IOException lastException;
    
    static {
        Method declaredMethod;
        try {
            declaredMethod = Throwable.class.getDeclaredMethod("addSuppressed", Throwable.class);
        }
        catch (Exception ex) {
            declaredMethod = null;
        }
        addSuppressedExceptionMethod = declaredMethod;
    }
    
    public RouteException(final IOException lastException) {
        super(lastException);
        this.lastException = lastException;
    }
    
    private void addSuppressedIfPossible(final IOException ex, final IOException ex2) {
        final Method addSuppressedExceptionMethod = RouteException.addSuppressedExceptionMethod;
        if (addSuppressedExceptionMethod != null) {
            try {
                addSuppressedExceptionMethod.invoke(ex, ex2);
            }
            catch (InvocationTargetException ex3) {}
            catch (IllegalAccessException ex4) {}
        }
    }
    
    public void addConnectException(final IOException lastException) {
        this.addSuppressedIfPossible(lastException, this.lastException);
        this.lastException = lastException;
    }
    
    public IOException getLastConnectException() {
        return this.lastException;
    }
}
