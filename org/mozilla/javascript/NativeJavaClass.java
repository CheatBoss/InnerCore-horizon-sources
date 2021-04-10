package org.mozilla.javascript;

import java.util.*;
import java.lang.reflect.*;

public class NativeJavaClass extends NativeJavaObject implements Function
{
    static final String javaClassPropertyName = "__javaObject__";
    static final long serialVersionUID = -6460763940409461664L;
    private Map<String, FieldAndMethods> staticFieldAndMethods;
    
    public NativeJavaClass() {
    }
    
    public NativeJavaClass(final Scriptable scriptable, final Class<?> clazz) {
        this(scriptable, clazz, false);
    }
    
    public NativeJavaClass(final Scriptable scriptable, final Class<?> clazz, final boolean b) {
        super(scriptable, clazz, null, b);
    }
    
    static Object constructInternal(final Object[] array, final MemberBox memberBox) {
        final Class<?>[] argTypes = memberBox.argTypes;
        final boolean vararg = memberBox.vararg;
        int n = 0;
        final int n2 = 0;
        Object[] array2;
        if (vararg) {
            array2 = new Object[argTypes.length];
            for (int i = 0; i < argTypes.length - 1; ++i) {
                array2[i] = Context.jsToJava(array[i], argTypes[i]);
            }
            Object jsToJava;
            if (array.length == argTypes.length && (array[array.length - 1] == null || array[array.length - 1] instanceof NativeArray || array[array.length - 1] instanceof NativeJavaArray)) {
                jsToJava = Context.jsToJava(array[array.length - 1], argTypes[argTypes.length - 1]);
            }
            else {
                final Class<?> componentType = argTypes[argTypes.length - 1].getComponentType();
                final Object instance = Array.newInstance(componentType, array.length - argTypes.length + 1);
                for (int j = n2; j < Array.getLength(instance); ++j) {
                    Array.set(instance, j, Context.jsToJava(array[argTypes.length - 1 + j], componentType));
                }
                jsToJava = instance;
            }
            array2[argTypes.length - 1] = jsToJava;
        }
        else {
            Object[] array3 = array;
            while (true) {
                array2 = array3;
                if (n >= array3.length) {
                    break;
                }
                final Object o = array3[n];
                final Object jsToJava2 = Context.jsToJava(o, argTypes[n]);
                Object[] array4 = array3;
                if (jsToJava2 != o) {
                    if ((array4 = array3) == array) {
                        array4 = array.clone();
                    }
                    array4[n] = jsToJava2;
                }
                ++n;
                array3 = array4;
            }
        }
        return memberBox.newInstance(array2);
    }
    
    static Scriptable constructSpecific(final Context context, Scriptable topLevelScope, final Object[] array, final MemberBox memberBox) {
        final Object constructInternal = constructInternal(array, memberBox);
        topLevelScope = ScriptableObject.getTopLevelScope(topLevelScope);
        return context.getWrapFactory().wrapNewObject(context, topLevelScope, constructInternal);
    }
    
    private static Class<?> findNestedClass(final Class<?> clazz, String string) {
        final StringBuilder sb = new StringBuilder();
        sb.append(clazz.getName());
        sb.append('$');
        sb.append(string);
        string = sb.toString();
        final ClassLoader classLoader = clazz.getClassLoader();
        if (classLoader == null) {
            return Kit.classOrNull(string);
        }
        return Kit.classOrNull(classLoader, string);
    }
    
    @Override
    public Object call(final Context context, final Scriptable scriptable, Scriptable prototype, final Object[] array) {
        if (array.length == 1 && array[0] instanceof Scriptable) {
            final Class<?> classObject = this.getClassObject();
            prototype = (Scriptable)array[0];
            while (!(prototype instanceof Wrapper) || !classObject.isInstance(((Wrapper)prototype).unwrap())) {
                if ((prototype = prototype.getPrototype()) == null) {
                    return this.construct(context, scriptable, array);
                }
            }
            return prototype;
        }
        return this.construct(context, scriptable, array);
    }
    
    @Override
    public Scriptable construct(final Context context, final Scriptable scriptable, final Object[] array) {
        final Class<?> classObject = this.getClassObject();
        final int modifiers = classObject.getModifiers();
        if (!Modifier.isInterface(modifiers) && !Modifier.isAbstract(modifiers)) {
            final NativeJavaMethod ctors = this.members.ctors;
            final int cachedFunction = ctors.findCachedFunction(context, array);
            if (cachedFunction < 0) {
                throw Context.reportRuntimeError2("msg.no.java.ctor", classObject.getName(), NativeJavaMethod.scriptSignature(array));
            }
            return constructSpecific(context, scriptable, array, ctors.methods[cachedFunction]);
        }
        else {
            if (array.length == 0) {
                throw Context.reportRuntimeError0("msg.adapter.zero.args");
            }
            final Scriptable topLevelScope = ScriptableObject.getTopLevelScope(this);
            final String s = "";
            String s2;
            try {
                if ("Dalvik".equals(System.getProperty("java.vm.name")) && classObject.isInterface()) {
                    return context.getWrapFactory().wrapAsJavaObject(context, scriptable, NativeJavaObject.createInterfaceAdapter(classObject, ScriptableObject.ensureScriptableObject(array[0])), null);
                }
                final Object value = topLevelScope.get("JavaAdapter", topLevelScope);
                if (value != NativeJavaClass.NOT_FOUND) {
                    return ((Function)value).construct(context, topLevelScope, new Object[] { this, array[0] });
                }
                s2 = s;
            }
            catch (Exception ex) {
                final String message = ex.getMessage();
                s2 = s;
                if (message != null) {
                    s2 = message;
                }
            }
            throw Context.reportRuntimeError2("msg.cant.instantiate", s2, classObject.getName());
        }
    }
    
    @Override
    public Object get(final String s, Scriptable topLevelScope) {
        if (s.equals("prototype")) {
            return null;
        }
        if (this.staticFieldAndMethods != null) {
            final FieldAndMethods value = this.staticFieldAndMethods.get(s);
            if (value != null) {
                return value;
            }
        }
        if (this.members.has(s, true)) {
            return this.members.get(this, s, this.javaObject, true);
        }
        final Context context = Context.getContext();
        topLevelScope = ScriptableObject.getTopLevelScope(topLevelScope);
        final WrapFactory wrapFactory = context.getWrapFactory();
        if ("__javaObject__".equals(s)) {
            return wrapFactory.wrap(context, topLevelScope, this.javaObject, ScriptRuntime.ClassClass);
        }
        final Class<?> nestedClass = findNestedClass(this.getClassObject(), s);
        if (nestedClass != null) {
            final Scriptable wrapJavaClass = wrapFactory.wrapJavaClass(context, topLevelScope, nestedClass);
            wrapJavaClass.setParentScope(this);
            return wrapJavaClass;
        }
        throw this.members.reportMemberNotFound(s);
    }
    
    @Override
    public String getClassName() {
        return "JavaClass";
    }
    
    public Class<?> getClassObject() {
        return (Class<?>)super.unwrap();
    }
    
    @Override
    public Object getDefaultValue(final Class<?> clazz) {
        if (clazz == null || clazz == ScriptRuntime.StringClass) {
            return this.toString();
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
        return this.members.getIds(true);
    }
    
    @Override
    public boolean has(final String s, final Scriptable scriptable) {
        final JavaMembers members = this.members;
        boolean b = true;
        if (!members.has(s, true)) {
            if ("__javaObject__".equals(s)) {
                return true;
            }
            b = false;
        }
        return b;
    }
    
    @Override
    public boolean hasInstance(final Scriptable scriptable) {
        return scriptable instanceof Wrapper && !(scriptable instanceof NativeJavaClass) && this.getClassObject().isInstance(((Wrapper)scriptable).unwrap());
    }
    
    @Override
    protected void initMembers() {
        final Class clazz = (Class)this.javaObject;
        this.members = JavaMembers.lookupClass(this.parent, clazz, clazz, this.isAdapter);
        this.staticFieldAndMethods = this.members.getFieldAndMethodsObjects(this, clazz, true);
    }
    
    @Override
    public void put(final String s, final Scriptable scriptable, final Object o) {
        this.members.put(this, s, this.javaObject, o, true);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[JavaClass ");
        sb.append(this.getClassObject().getName());
        sb.append("]");
        return sb.toString();
    }
}
