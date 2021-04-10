package org.mozilla.javascript.jdk13;

import org.mozilla.javascript.*;
import java.lang.reflect.*;

public class VMBridge_jdk13 extends VMBridge
{
    private ThreadLocal<Object[]> contextLocal;
    
    public VMBridge_jdk13() {
        this.contextLocal = new ThreadLocal<Object[]>();
    }
    
    @Override
    protected Context getContext(final Object o) {
        return (Context)((Object[])o)[0];
    }
    
    @Override
    protected ClassLoader getCurrentThreadClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
    
    @Override
    protected Object getInterfaceProxyHelper(final ContextFactory contextFactory, final Class<?>[] array) {
        final Class<?> proxyClass = Proxy.getProxyClass(array[0].getClassLoader(), array);
        try {
            return proxyClass.getConstructor(InvocationHandler.class);
        }
        catch (NoSuchMethodException ex) {
            throw Kit.initCause(new IllegalStateException(), ex);
        }
    }
    
    @Override
    protected Object getThreadContextHelper() {
        Object[] array;
        if ((array = this.contextLocal.get()) == null) {
            array = new Object[] { null };
            this.contextLocal.set(array);
        }
        return array;
    }
    
    @Override
    protected boolean isVarArgs(final Member member) {
        return false;
    }
    
    @Override
    protected Object newInterfaceProxy(Object instance, final ContextFactory contextFactory, final InterfaceAdapter interfaceAdapter, final Object o, final Scriptable scriptable) {
        final Constructor constructor = (Constructor)instance;
        final InvocationHandler invocationHandler = new InvocationHandler() {
            @Override
            public Object invoke(final Object o, final Method method, final Object[] array) {
                if (method.getDeclaringClass() == Object.class) {
                    final String name = method.getName();
                    if (name.equals("equals")) {
                        boolean b = false;
                        if (o == array[0]) {
                            b = true;
                        }
                        return b;
                    }
                    if (name.equals("hashCode")) {
                        return o.hashCode();
                    }
                    if (name.equals("toString")) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Proxy[");
                        sb.append(o.toString());
                        sb.append("]");
                        return sb.toString();
                    }
                }
                return interfaceAdapter.invoke(contextFactory, o, scriptable, o, method, array);
            }
        };
        try {
            instance = constructor.newInstance(invocationHandler);
            return instance;
        }
        catch (InstantiationException ex) {
            throw Kit.initCause(new IllegalStateException(), ex);
        }
        catch (IllegalAccessException ex2) {
            throw Kit.initCause(new IllegalStateException(), ex2);
        }
        catch (InvocationTargetException ex3) {
            throw Context.throwAsScriptRuntimeEx(ex3);
        }
    }
    
    @Override
    protected void setContext(final Object o, final Context context) {
        ((Object[])o)[0] = context;
    }
    
    @Override
    protected boolean tryToMakeAccessible(Object o) {
        if (!(o instanceof AccessibleObject)) {
            return false;
        }
        o = o;
        if (((AccessibleObject)o).isAccessible()) {
            return true;
        }
        try {
            ((AccessibleObject)o).setAccessible(true);
        }
        catch (Exception ex) {}
        return ((AccessibleObject)o).isAccessible();
    }
}
