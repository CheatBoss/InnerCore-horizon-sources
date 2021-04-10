package org.mozilla.javascript;

import java.util.*;
import java.lang.reflect.*;

public abstract class VMBridge
{
    static final VMBridge instance;
    
    static {
        instance = makeInstance();
    }
    
    private static VMBridge makeInstance() {
        final String[] array = new String[4];
        int i = 0;
        array[0] = "org.mozilla.javascript.VMBridge_custom";
        array[1] = "org.mozilla.javascript.jdk15.VMBridge_jdk15";
        array[2] = "org.mozilla.javascript.jdk13.VMBridge_jdk13";
        array[3] = "org.mozilla.javascript.jdk11.VMBridge_jdk11";
        while (i != array.length) {
            final Class<?> classOrNull = Kit.classOrNull(array[i]);
            if (classOrNull != null) {
                final VMBridge vmBridge = (VMBridge)Kit.newInstanceOrNull(classOrNull);
                if (vmBridge != null) {
                    return vmBridge;
                }
            }
            ++i;
        }
        throw new IllegalStateException("Failed to create VMBridge instance");
    }
    
    protected abstract Context getContext(final Object p0);
    
    protected abstract ClassLoader getCurrentThreadClassLoader();
    
    protected Object getInterfaceProxyHelper(final ContextFactory contextFactory, final Class<?>[] array) {
        throw Context.reportRuntimeError("VMBridge.getInterfaceProxyHelper is not supported");
    }
    
    public Iterator<?> getJavaIterator(final Context context, final Scriptable scriptable, final Object o) {
        if (o instanceof Wrapper) {
            final Object unwrap = ((Wrapper)o).unwrap();
            Iterator<?> iterator = null;
            if (unwrap instanceof Iterator) {
                iterator = (Iterator<?>)unwrap;
            }
            return iterator;
        }
        return null;
    }
    
    protected abstract Object getThreadContextHelper();
    
    protected abstract boolean isVarArgs(final Member p0);
    
    protected Object newInterfaceProxy(final Object o, final ContextFactory contextFactory, final InterfaceAdapter interfaceAdapter, final Object o2, final Scriptable scriptable) {
        throw Context.reportRuntimeError("VMBridge.newInterfaceProxy is not supported");
    }
    
    protected abstract void setContext(final Object p0, final Context p1);
    
    protected abstract boolean tryToMakeAccessible(final Object p0);
}
