package okhttp3.internal.platform;

import java.lang.reflect.*;

class OptionalMethod<T>
{
    private final String methodName;
    private final Class[] methodParams;
    private final Class<?> returnType;
    
    OptionalMethod(final Class<?> returnType, final String methodName, final Class... methodParams) {
        this.returnType = returnType;
        this.methodName = methodName;
        this.methodParams = methodParams;
    }
    
    private Method getMethod(final Class<?> clazz) {
        final String methodName = this.methodName;
        Method method;
        if (methodName != null) {
            final Method publicMethod = getPublicMethod(clazz, methodName, this.methodParams);
            if ((method = publicMethod) != null) {
                final Class<?> returnType = this.returnType;
                method = publicMethod;
                if (returnType != null) {
                    method = publicMethod;
                    if (!returnType.isAssignableFrom(publicMethod.getReturnType())) {
                        return null;
                    }
                }
            }
        }
        else {
            method = null;
        }
        return method;
    }
    
    private static Method getPublicMethod(Class<?> method, final String s, final Class[] array) {
        try {
            method = ((Class)method).getMethod(s, (Class[])array);
            try {
                if ((method.getModifiers() & 0x1) == 0x0) {
                    return null;
                }
            }
            catch (NoSuchMethodException ex) {}
            return method;
        }
        catch (NoSuchMethodException ex2) {
            return null;
        }
    }
    
    public Object invoke(final T t, final Object... array) throws InvocationTargetException {
        final Method method = this.getMethod(t.getClass());
        if (method != null) {
            try {
                return method.invoke(t, array);
            }
            catch (IllegalAccessException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unexpectedly could not call: ");
                sb.append(method);
                final AssertionError assertionError = new AssertionError((Object)sb.toString());
                assertionError.initCause(ex);
                throw assertionError;
            }
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Method ");
        sb2.append(this.methodName);
        sb2.append(" not supported for object ");
        sb2.append(t);
        throw new AssertionError((Object)sb2.toString());
    }
    
    public Object invokeOptional(final T t, final Object... array) throws InvocationTargetException {
        final Method method = this.getMethod(t.getClass());
        if (method == null) {
            return null;
        }
        try {
            return method.invoke(t, array);
        }
        catch (IllegalAccessException ex) {
            return null;
        }
    }
    
    public Object invokeOptionalWithoutCheckedException(final T t, final Object... array) {
        try {
            return this.invokeOptional(t, array);
        }
        catch (InvocationTargetException ex) {
            final Throwable targetException = ex.getTargetException();
            if (targetException instanceof RuntimeException) {
                throw (RuntimeException)targetException;
            }
            final AssertionError assertionError = new AssertionError((Object)"Unexpected exception");
            assertionError.initCause(targetException);
            throw assertionError;
        }
    }
    
    public Object invokeWithoutCheckedException(final T t, final Object... array) {
        try {
            return this.invoke(t, array);
        }
        catch (InvocationTargetException ex) {
            final Throwable targetException = ex.getTargetException();
            if (targetException instanceof RuntimeException) {
                throw (RuntimeException)targetException;
            }
            final AssertionError assertionError = new AssertionError((Object)"Unexpected exception");
            assertionError.initCause(targetException);
            throw assertionError;
        }
    }
    
    public boolean isSupported(final T t) {
        return this.getMethod(t.getClass()) != null;
    }
}
