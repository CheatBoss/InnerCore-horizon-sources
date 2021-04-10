package org.mozilla.javascript;

import java.lang.reflect.*;

public class NativeJavaArray extends NativeJavaObject
{
    static final long serialVersionUID = -924022554283675333L;
    Object array;
    Class<?> cls;
    int length;
    
    public NativeJavaArray(final Scriptable scriptable, final Object array) {
        super(scriptable, null, ScriptRuntime.ObjectClass);
        final Class<?> class1 = array.getClass();
        if (!class1.isArray()) {
            throw new RuntimeException("Array expected");
        }
        this.array = array;
        this.length = Array.getLength(array);
        this.cls = class1.getComponentType();
    }
    
    public static NativeJavaArray wrap(final Scriptable scriptable, final Object o) {
        return new NativeJavaArray(scriptable, o);
    }
    
    @Override
    public Object get(final int n, final Scriptable scriptable) {
        if (n >= 0 && n < this.length) {
            final Context context = Context.getContext();
            return context.getWrapFactory().wrap(context, this, Array.get(this.array, n), this.cls);
        }
        return Undefined.instance;
    }
    
    @Override
    public Object get(final String s, final Scriptable scriptable) {
        if (s.equals("length")) {
            return this.length;
        }
        final Object value = super.get(s, scriptable);
        if (value == NativeJavaArray.NOT_FOUND && !ScriptableObject.hasProperty(this.getPrototype(), s)) {
            throw Context.reportRuntimeError2("msg.java.member.not.found", this.array.getClass().getName(), s);
        }
        return value;
    }
    
    @Override
    public String getClassName() {
        return "JavaArray";
    }
    
    @Override
    public Object getDefaultValue(final Class<?> clazz) {
        if (clazz == null || clazz == ScriptRuntime.StringClass) {
            return this.array.toString();
        }
        if (clazz == ScriptRuntime.BooleanClass) {
            return Boolean.TRUE;
        }
        if (clazz == ScriptRuntime.NumberClass) {
            return ScriptRuntime.NaNobj;
        }
        return this;
    }
    
    @Override
    public Object[] getIds() {
        final Object[] array = new Object[this.length];
        int length = this.length;
        while (true) {
            --length;
            if (length < 0) {
                break;
            }
            array[length] = length;
        }
        return array;
    }
    
    @Override
    public Scriptable getPrototype() {
        if (this.prototype == null) {
            this.prototype = ScriptableObject.getArrayPrototype(this.getParentScope());
        }
        return this.prototype;
    }
    
    @Override
    public boolean has(final int n, final Scriptable scriptable) {
        return n >= 0 && n < this.length;
    }
    
    @Override
    public boolean has(final String s, final Scriptable scriptable) {
        return s.equals("length") || super.has(s, scriptable);
    }
    
    @Override
    public boolean hasInstance(final Scriptable scriptable) {
        return scriptable instanceof Wrapper && this.cls.isInstance(((Wrapper)scriptable).unwrap());
    }
    
    @Override
    public void put(final int n, final Scriptable scriptable, final Object o) {
        if (n >= 0 && n < this.length) {
            Array.set(this.array, n, Context.jsToJava(o, this.cls));
            return;
        }
        throw Context.reportRuntimeError2("msg.java.array.index.out.of.bounds", String.valueOf(n), String.valueOf(this.length - 1));
    }
    
    @Override
    public void put(final String s, final Scriptable scriptable, final Object o) {
        if (!s.equals("length")) {
            throw Context.reportRuntimeError1("msg.java.array.member.not.found", s);
        }
    }
    
    @Override
    public Object unwrap() {
        return this.array;
    }
}
