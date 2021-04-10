package org.mozilla.javascript;

import java.lang.reflect.*;

public class InterfaceAdapter
{
    private final Object proxyHelper;
    
    private InterfaceAdapter(final ContextFactory contextFactory, final Class<?> clazz) {
        this.proxyHelper = VMBridge.instance.getInterfaceProxyHelper(contextFactory, new Class[] { clazz });
    }
    
    static Object create(final Context context, final Class<?> clazz, final ScriptableObject scriptableObject) {
        if (!clazz.isInterface()) {
            throw new IllegalArgumentException();
        }
        final Scriptable topCallScope = ScriptRuntime.getTopCallScope(context);
        final ClassCache value = ClassCache.get(topCallScope);
        final InterfaceAdapter interfaceAdapter = (InterfaceAdapter)value.getInterfaceAdapter(clazz);
        final ContextFactory factory = context.getFactory();
        InterfaceAdapter interfaceAdapter2;
        if ((interfaceAdapter2 = interfaceAdapter) == null) {
            final Method[] methods = clazz.getMethods();
            if (scriptableObject instanceof Callable) {
                final int length = methods.length;
                if (length == 0) {
                    throw Context.reportRuntimeError1("msg.no.empty.interface.conversion", clazz.getName());
                }
                int i = 1;
                if (length > 1) {
                    final String name = methods[0].getName();
                    while (i < length) {
                        if (!name.equals(methods[i].getName())) {
                            throw Context.reportRuntimeError1("msg.no.function.interface.conversion", clazz.getName());
                        }
                        ++i;
                    }
                }
            }
            interfaceAdapter2 = new InterfaceAdapter(factory, clazz);
            value.cacheInterfaceAdapter(clazz, interfaceAdapter2);
        }
        return VMBridge.instance.newInterfaceProxy(interfaceAdapter2.proxyHelper, factory, interfaceAdapter2, scriptableObject, topCallScope);
    }
    
    public Object invoke(final ContextFactory contextFactory, final Object o, final Scriptable scriptable, final Object o2, final Method method, final Object[] array) {
        return contextFactory.call(new ContextAction() {
            @Override
            public Object run(final Context context) {
                return InterfaceAdapter.this.invokeImpl(context, o, scriptable, o2, method, array);
            }
        });
    }
    
    Object invokeImpl(final Context context, final Object o, final Scriptable scriptable, final Object o2, final Method method, final Object[] array) {
        Callable callable;
        if (o instanceof Callable) {
            callable = (Callable)o;
        }
        else {
            final Scriptable scriptable2 = (Scriptable)o;
            final String name = method.getName();
            final Object property = ScriptableObject.getProperty(scriptable2, name);
            if (property == ScriptableObject.NOT_FOUND) {
                Context.reportWarning(ScriptRuntime.getMessage1("msg.undefined.function.interface", name));
                final Class<?> returnType = method.getReturnType();
                if (returnType == Void.TYPE) {
                    return null;
                }
                return Context.jsToJava(null, returnType);
            }
            else {
                if (!(property instanceof Callable)) {
                    throw Context.reportRuntimeError1("msg.not.function.interface", name);
                }
                callable = (Callable)property;
            }
        }
        final WrapFactory wrapFactory = context.getWrapFactory();
        Object[] emptyArgs;
        if (array == null) {
            emptyArgs = ScriptRuntime.emptyArgs;
        }
        else {
            int n = 0;
            final int length = array.length;
            while (true) {
                emptyArgs = array;
                if (n == length) {
                    break;
                }
                final Object o3 = array[n];
                if (!(o3 instanceof String) && !(o3 instanceof Number) && !(o3 instanceof Boolean)) {
                    array[n] = wrapFactory.wrap(context, scriptable, o3, null);
                }
                ++n;
            }
        }
        final Object call = callable.call(context, scriptable, wrapFactory.wrapAsJavaObject(context, scriptable, o2, null), emptyArgs);
        final Class<?> returnType2 = method.getReturnType();
        if (returnType2 == Void.TYPE) {
            return null;
        }
        return Context.jsToJava(call, returnType2);
    }
}
