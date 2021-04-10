package org.mozilla.javascript;

public class WrapFactory
{
    private boolean javaPrimitiveWrap;
    
    public WrapFactory() {
        this.javaPrimitiveWrap = true;
    }
    
    public final boolean isJavaPrimitiveWrap() {
        return this.javaPrimitiveWrap;
    }
    
    public final void setJavaPrimitiveWrap(final boolean javaPrimitiveWrap) {
        final Context currentContext = Context.getCurrentContext();
        if (currentContext != null && currentContext.isSealed()) {
            Context.onSealedMutation();
        }
        this.javaPrimitiveWrap = javaPrimitiveWrap;
    }
    
    public Object wrap(final Context context, final Scriptable scriptable, final Object o, final Class<?> clazz) {
        if (o == null || o == Undefined.instance) {
            return o;
        }
        if (o instanceof Scriptable) {
            return o;
        }
        if (clazz != null && clazz.isPrimitive()) {
            if (clazz == Void.TYPE) {
                return Undefined.instance;
            }
            if (clazz == Character.TYPE) {
                return o;
            }
            return o;
        }
        else {
            if (!this.isJavaPrimitiveWrap()) {
                if (o instanceof String || o instanceof Number) {
                    return o;
                }
                if (o instanceof Boolean) {
                    return o;
                }
                if (o instanceof Character) {
                    return String.valueOf((char)o);
                }
            }
            if (o.getClass().isArray()) {
                return NativeJavaArray.wrap(scriptable, o);
            }
            return this.wrapAsJavaObject(context, scriptable, o, clazz);
        }
    }
    
    public Scriptable wrapAsJavaObject(final Context context, final Scriptable scriptable, final Object o, final Class<?> clazz) {
        return new NativeJavaObject(scriptable, o, clazz);
    }
    
    public Scriptable wrapJavaClass(final Context context, final Scriptable scriptable, final Class<?> clazz) {
        return new NativeJavaClass(scriptable, clazz);
    }
    
    public Scriptable wrapNewObject(final Context context, final Scriptable scriptable, final Object o) {
        if (o instanceof Scriptable) {
            return (Scriptable)o;
        }
        if (o.getClass().isArray()) {
            return NativeJavaArray.wrap(scriptable, o);
        }
        return this.wrapAsJavaObject(context, scriptable, o, null);
    }
}
