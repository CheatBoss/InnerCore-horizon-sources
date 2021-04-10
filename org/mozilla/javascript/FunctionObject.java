package org.mozilla.javascript;

import java.lang.reflect.*;
import java.io.*;

public class FunctionObject extends BaseFunction
{
    public static final int JAVA_BOOLEAN_TYPE = 3;
    public static final int JAVA_DOUBLE_TYPE = 4;
    public static final int JAVA_INT_TYPE = 2;
    public static final int JAVA_OBJECT_TYPE = 6;
    public static final int JAVA_SCRIPTABLE_TYPE = 5;
    public static final int JAVA_STRING_TYPE = 1;
    public static final int JAVA_UNSUPPORTED_TYPE = 0;
    private static final short VARARGS_CTOR = -2;
    private static final short VARARGS_METHOD = -1;
    private static boolean sawSecurityException = false;
    static final long serialVersionUID = -5332312783643935019L;
    private String functionName;
    private transient boolean hasVoidReturn;
    private boolean isStatic;
    MemberBox member;
    private int parmsLength;
    private transient int returnTypeTag;
    private transient byte[] typeTags;
    
    public FunctionObject(final String functionName, final Member member, final Scriptable scriptable) {
        if (member instanceof Constructor) {
            this.member = new MemberBox((Constructor<?>)member);
            this.isStatic = true;
        }
        else {
            this.member = new MemberBox((Method)member);
            this.isStatic = this.member.isStatic();
        }
        final String name = this.member.getName();
        this.functionName = functionName;
        final Class<?>[] argTypes = this.member.argTypes;
        final int length = argTypes.length;
        int i = 0;
        if (length == 4 && (argTypes[1].isArray() || argTypes[2].isArray())) {
            if (argTypes[1].isArray()) {
                if (!this.isStatic || argTypes[0] != ScriptRuntime.ContextClass || argTypes[1].getComponentType() != ScriptRuntime.ObjectClass || argTypes[2] != ScriptRuntime.FunctionClass || argTypes[3] != Boolean.TYPE) {
                    throw Context.reportRuntimeError1("msg.varargs.ctor", name);
                }
                this.parmsLength = -2;
            }
            else {
                if (!this.isStatic || argTypes[0] != ScriptRuntime.ContextClass || argTypes[1] != ScriptRuntime.ScriptableClass || argTypes[2].getComponentType() != ScriptRuntime.ObjectClass || argTypes[3] != ScriptRuntime.FunctionClass) {
                    throw Context.reportRuntimeError1("msg.varargs.fun", name);
                }
                this.parmsLength = -1;
            }
        }
        else if ((this.parmsLength = length) > 0) {
            this.typeTags = new byte[length];
            while (i != length) {
                final int typeTag = getTypeTag(argTypes[i]);
                if (typeTag == 0) {
                    throw Context.reportRuntimeError2("msg.bad.parms", argTypes[i].getName(), name);
                }
                this.typeTags[i] = (byte)typeTag;
                ++i;
            }
        }
        if (this.member.isMethod()) {
            final Class<?> returnType = this.member.method().getReturnType();
            if (returnType == Void.TYPE) {
                this.hasVoidReturn = true;
            }
            else {
                this.returnTypeTag = getTypeTag(returnType);
            }
        }
        else {
            final Class<?> declaringClass = this.member.getDeclaringClass();
            if (!ScriptRuntime.ScriptableClass.isAssignableFrom(declaringClass)) {
                throw Context.reportRuntimeError1("msg.bad.ctor.return", declaringClass.getName());
            }
        }
        ScriptRuntime.setFunctionProtoAndParent(this, scriptable);
    }
    
    public static Object convertArg(final Context context, final Scriptable scriptable, final Object o, final int n) {
        switch (n) {
            default: {
                throw new IllegalArgumentException();
            }
            case 6: {
                return o;
            }
            case 5: {
                return ScriptRuntime.toObjectOrNull(context, o, scriptable);
            }
            case 4: {
                if (o instanceof Double) {
                    return o;
                }
                return new Double(ScriptRuntime.toNumber(o));
            }
            case 3: {
                if (o instanceof Boolean) {
                    return o;
                }
                if (ScriptRuntime.toBoolean(o)) {
                    return Boolean.TRUE;
                }
                return Boolean.FALSE;
            }
            case 2: {
                if (o instanceof Integer) {
                    return o;
                }
                return ScriptRuntime.toInt32(o);
            }
            case 1: {
                if (o instanceof String) {
                    return o;
                }
                return ScriptRuntime.toString(o);
            }
        }
    }
    
    @Deprecated
    public static Object convertArg(final Context context, final Scriptable scriptable, final Object o, final Class<?> clazz) {
        final int typeTag = getTypeTag(clazz);
        if (typeTag == 0) {
            throw Context.reportRuntimeError1("msg.cant.convert", clazz.getName());
        }
        return convertArg(context, scriptable, o, typeTag);
    }
    
    static Method findSingleMethod(final Method[] array, final String s) {
        Method method = null;
        Method method3;
        for (int i = 0; i != array.length; ++i, method = method3) {
            final Method method2 = array[i];
            method3 = method;
            if (method2 != null) {
                method3 = method;
                if (s.equals(method2.getName())) {
                    if (method != null) {
                        throw Context.reportRuntimeError2("msg.no.overload", s, method2.getDeclaringClass().getName());
                    }
                    method3 = method2;
                }
            }
        }
        return method;
    }
    
    static Method[] getMethodList(final Class<?> clazz) {
        final Method[] array = null;
        Method[] declaredMethods = null;
        try {
            if (!FunctionObject.sawSecurityException) {
                declaredMethods = clazz.getDeclaredMethods();
            }
        }
        catch (SecurityException ex) {
            FunctionObject.sawSecurityException = true;
            declaredMethods = array;
        }
        Method[] methods = declaredMethods;
        if (declaredMethods == null) {
            methods = clazz.getMethods();
        }
        final int n = 0;
        int n2 = 0;
        for (int i = 0; i < methods.length; ++i) {
            Label_0100: {
                if (FunctionObject.sawSecurityException) {
                    if (methods[i].getDeclaringClass() == clazz) {
                        break Label_0100;
                    }
                }
                else if (Modifier.isPublic(methods[i].getModifiers())) {
                    break Label_0100;
                }
                methods[i] = null;
                continue;
            }
            ++n2;
        }
        final Method[] array2 = new Method[n2];
        int n3 = 0;
        int n4;
        for (int j = n; j < methods.length; ++j, n3 = n4) {
            n4 = n3;
            if (methods[j] != null) {
                array2[n3] = methods[j];
                n4 = n3 + 1;
            }
        }
        return array2;
    }
    
    public static int getTypeTag(final Class<?> clazz) {
        if (clazz == ScriptRuntime.StringClass) {
            return 1;
        }
        if (clazz == ScriptRuntime.IntegerClass || clazz == Integer.TYPE) {
            return 2;
        }
        if (clazz == ScriptRuntime.BooleanClass || clazz == Boolean.TYPE) {
            return 3;
        }
        if (clazz == ScriptRuntime.DoubleClass || clazz == Double.TYPE) {
            return 4;
        }
        if (ScriptRuntime.ScriptableClass.isAssignableFrom(clazz)) {
            return 5;
        }
        if (clazz == ScriptRuntime.ObjectClass) {
            return 6;
        }
        return 0;
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (this.parmsLength > 0) {
            final Class<?>[] argTypes = this.member.argTypes;
            this.typeTags = new byte[this.parmsLength];
            for (int i = 0; i != this.parmsLength; ++i) {
                this.typeTags[i] = (byte)getTypeTag(argTypes[i]);
            }
        }
        if (this.member.isMethod()) {
            final Class<?> returnType = this.member.method().getReturnType();
            if (returnType == Void.TYPE) {
                this.hasVoidReturn = true;
                return;
            }
            this.returnTypeTag = getTypeTag(returnType);
        }
    }
    
    public void addAsConstructor(final Scriptable scriptable, final Scriptable scriptable2) {
        this.initAsConstructor(scriptable, scriptable2);
        ScriptableObject.defineProperty(scriptable, scriptable2.getClassName(), this, 2);
    }
    
    @Override
    public Object call(final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        final boolean b = false;
        final int length = array.length;
        final int n = 0;
        final int n2 = 0;
        for (int i = 0; i < length; ++i) {
            if (array[i] instanceof ConsString) {
                array[i] = array[i].toString();
            }
        }
        Object o;
        boolean b2;
        if (this.parmsLength < 0) {
            if (this.parmsLength == -1) {
                o = this.member.invoke(null, new Object[] { context, scriptable2, array, this });
                b2 = true;
            }
            else {
                Boolean b3;
                if (scriptable2 == null) {
                    b3 = Boolean.TRUE;
                }
                else {
                    b3 = Boolean.FALSE;
                }
                final Object[] array2 = { context, array, this, b3 };
                if (this.member.isCtor()) {
                    o = this.member.newInstance(array2);
                }
                else {
                    o = this.member.invoke(null, array2);
                }
                b2 = b;
            }
        }
        else {
            Scriptable scriptable3 = scriptable2;
            if (!this.isStatic) {
                final Class<?> declaringClass = this.member.getDeclaringClass();
                scriptable3 = scriptable2;
                if (!declaringClass.isInstance(scriptable2)) {
                    boolean instance = false;
                    Scriptable scriptable4;
                    if ((scriptable4 = scriptable2) == scriptable) {
                        final Scriptable parentScope = this.getParentScope();
                        instance = instance;
                        scriptable4 = scriptable2;
                        if (scriptable != parentScope) {
                            final boolean b4 = instance = declaringClass.isInstance(parentScope);
                            scriptable4 = scriptable2;
                            if (b4) {
                                scriptable4 = parentScope;
                                instance = b4;
                            }
                        }
                    }
                    scriptable3 = scriptable4;
                    if (!instance) {
                        throw ScriptRuntime.typeError1("msg.incompat.call", this.functionName);
                    }
                }
            }
            Object[] emptyArgs;
            if (this.parmsLength == length) {
                Object[] array3 = array;
                int n3 = n2;
                while (true) {
                    emptyArgs = array3;
                    if (n3 == this.parmsLength) {
                        break;
                    }
                    final Object o2 = array[n3];
                    final Object convertArg = convertArg(context, scriptable, o2, this.typeTags[n3]);
                    Object[] array4 = array3;
                    if (o2 != convertArg) {
                        if ((array4 = array3) == array) {
                            array4 = array.clone();
                        }
                        array4[n3] = convertArg;
                    }
                    ++n3;
                    array3 = array4;
                }
            }
            else if (this.parmsLength == 0) {
                emptyArgs = ScriptRuntime.emptyArgs;
            }
            else {
                final Object[] array5 = new Object[this.parmsLength];
                int n4 = n;
                while (true) {
                    emptyArgs = array5;
                    if (n4 == this.parmsLength) {
                        break;
                    }
                    Object instance2;
                    if (n4 < length) {
                        instance2 = array[n4];
                    }
                    else {
                        instance2 = Undefined.instance;
                    }
                    array5[n4] = convertArg(context, scriptable, instance2, this.typeTags[n4]);
                    ++n4;
                }
            }
            if (this.member.isMethod()) {
                o = this.member.invoke(scriptable3, emptyArgs);
                b2 = true;
            }
            else {
                o = this.member.newInstance(emptyArgs);
                b2 = b;
            }
        }
        Object wrap = o;
        if (b2) {
            if (this.hasVoidReturn) {
                return Undefined.instance;
            }
            wrap = o;
            if (this.returnTypeTag == 0) {
                wrap = context.getWrapFactory().wrap(context, scriptable, o, null);
            }
        }
        return wrap;
    }
    
    @Override
    public Scriptable createObject(final Context context, final Scriptable scriptable) {
        if (!this.member.isCtor()) {
            if (this.parmsLength != -2) {
                try {
                    final Scriptable scriptable2 = (Scriptable)this.member.getDeclaringClass().newInstance();
                    scriptable2.setPrototype(this.getClassPrototype());
                    scriptable2.setParentScope(this.getParentScope());
                    return scriptable2;
                }
                catch (Exception ex) {
                    throw Context.throwAsScriptRuntimeEx(ex);
                }
            }
        }
        return null;
    }
    
    @Override
    public int getArity() {
        if (this.parmsLength < 0) {
            return 1;
        }
        return this.parmsLength;
    }
    
    @Override
    public String getFunctionName() {
        if (this.functionName == null) {
            return "";
        }
        return this.functionName;
    }
    
    @Override
    public int getLength() {
        return this.getArity();
    }
    
    public Member getMethodOrConstructor() {
        if (this.member.isMethod()) {
            return this.member.method();
        }
        return this.member.ctor();
    }
    
    void initAsConstructor(final Scriptable parentScope, final Scriptable immunePrototypeProperty) {
        ScriptRuntime.setFunctionProtoAndParent(this, parentScope);
        this.setImmunePrototypeProperty(immunePrototypeProperty);
        immunePrototypeProperty.setParentScope(this);
        ScriptableObject.defineProperty(immunePrototypeProperty, "constructor", this, 7);
        this.setParentScope(parentScope);
    }
    
    boolean isVarArgsConstructor() {
        return this.parmsLength == -2;
    }
    
    boolean isVarArgsMethod() {
        return this.parmsLength == -1;
    }
}
