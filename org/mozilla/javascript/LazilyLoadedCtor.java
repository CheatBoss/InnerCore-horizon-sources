package org.mozilla.javascript;

import java.io.*;
import java.security.*;
import java.lang.reflect.*;

public final class LazilyLoadedCtor implements Serializable
{
    private static final int STATE_BEFORE_INIT = 0;
    private static final int STATE_INITIALIZING = 1;
    private static final int STATE_WITH_VALUE = 2;
    private static final long serialVersionUID = 1L;
    private final String className;
    private Object initializedValue;
    private final boolean privileged;
    private final String propertyName;
    private final ScriptableObject scope;
    private final boolean sealed;
    private int state;
    
    public LazilyLoadedCtor(final ScriptableObject scriptableObject, final String s, final String s2, final boolean b) {
        this(scriptableObject, s, s2, b, false);
    }
    
    LazilyLoadedCtor(final ScriptableObject scope, final String propertyName, final String className, final boolean sealed, final boolean privileged) {
        this.scope = scope;
        this.propertyName = propertyName;
        this.className = className;
        this.sealed = sealed;
        this.privileged = privileged;
        scope.addLazilyInitializedValue(propertyName, this.state = 0, this, 2);
    }
    
    private Object buildValue() {
        if (this.privileged) {
            return AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction<Object>() {
                @Override
                public Object run() {
                    return LazilyLoadedCtor.this.buildValue0();
                }
            });
        }
        return this.buildValue0();
    }
    
    private Object buildValue0() {
        final Class<? extends Scriptable> cast = this.cast(Kit.classOrNull(this.className));
        if (cast != null) {
            try {
                final BaseFunction buildClassCtor = ScriptableObject.buildClassCtor(this.scope, cast, this.sealed, false);
                if (buildClassCtor != null) {
                    return buildClassCtor;
                }
                final Object value = this.scope.get(this.propertyName, this.scope);
                if (value != Scriptable.NOT_FOUND) {
                    return value;
                }
                goto Label_0076;
            }
            catch (SecurityException ex2) {}
            catch (IllegalAccessException ex3) {
                goto Label_0076;
            }
            catch (InstantiationException ex4) {
                goto Label_0076;
            }
            catch (RhinoException ex5) {}
            catch (InvocationTargetException ex) {
                final Throwable targetException = ex.getTargetException();
                if (targetException instanceof RuntimeException) {
                    throw (RuntimeException)targetException;
                }
                goto Label_0076;
            }
        }
        return Scriptable.NOT_FOUND;
    }
    
    private Class<? extends Scriptable> cast(final Class<?> clazz) {
        return (Class<? extends Scriptable>)clazz;
    }
    
    Object getValue() {
        if (this.state != 2) {
            throw new IllegalStateException(this.propertyName);
        }
        return this.initializedValue;
    }
    
    void init() {
        synchronized (this) {
            if (this.state == 1) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Recursive initialization for ");
                sb.append(this.propertyName);
                throw new IllegalStateException(sb.toString());
            }
            if (this.state == 0) {
                this.state = 1;
                final Object not_FOUND = Scriptable.NOT_FOUND;
                try {
                    this.buildValue();
                }
                finally {
                    this.initializedValue = not_FOUND;
                    this.state = 2;
                }
            }
        }
    }
}
