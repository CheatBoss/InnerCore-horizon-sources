package org.mozilla.javascript;

import java.util.concurrent.*;
import java.lang.reflect.*;
import java.util.*;

public class NativeJavaMethod extends BaseFunction
{
    private static final int PREFERENCE_AMBIGUOUS = 3;
    private static final int PREFERENCE_EQUAL = 0;
    private static final int PREFERENCE_FIRST_ARG = 1;
    private static final int PREFERENCE_SECOND_ARG = 2;
    private static final boolean debug = false;
    static final long serialVersionUID = -3440381785576412928L;
    private String functionName;
    MemberBox[] methods;
    private transient CopyOnWriteArrayList<ResolvedOverload> overloadCache;
    
    public NativeJavaMethod(final Method method, final String s) {
        this(new MemberBox(method), s);
    }
    
    NativeJavaMethod(final MemberBox memberBox, final String functionName) {
        this.functionName = functionName;
        this.methods = new MemberBox[] { memberBox };
    }
    
    NativeJavaMethod(final MemberBox[] methods) {
        this.functionName = methods[0].getName();
        this.methods = methods;
    }
    
    NativeJavaMethod(final MemberBox[] methods, final String functionName) {
        this.functionName = functionName;
        this.methods = methods;
    }
    
    static int findFunction(final Context context, final MemberBox[] array, final Object[] array2) {
        if (array.length == 0) {
            return -1;
        }
        if (array.length == 1) {
            final MemberBox memberBox = array[0];
            final Class<?>[] argTypes = memberBox.argTypes;
            final int length = argTypes.length;
            int n;
            if (memberBox.vararg) {
                if ((n = length - 1) > array2.length) {
                    return -1;
                }
            }
            else if ((n = length) != array2.length) {
                return -1;
            }
            for (int i = 0; i != n; ++i) {
                if (!NativeJavaObject.canConvert(array2[i], argTypes[i])) {
                    return -1;
                }
            }
            return 0;
        }
        int n2 = 0;
        int[] array3 = null;
        int n3 = -1;
        for (int j = 0; j < array.length; ++j) {
            final MemberBox memberBox2 = array[j];
            final Class<?>[] argTypes2 = memberBox2.argTypes;
            final int length2 = argTypes2.length;
            int n5 = 0;
            int n6 = 0;
            Label_0544: {
                int n4;
                if (memberBox2.vararg) {
                    if ((n4 = length2 - 1) > array2.length) {
                        n5 = n3;
                        n6 = n2;
                        break Label_0544;
                    }
                }
                else if ((n4 = length2) != array2.length) {
                    n5 = n3;
                    n6 = n2;
                    break Label_0544;
                }
                for (int k = 0; k < n4; ++k) {
                    if (!NativeJavaObject.canConvert(array2[k], argTypes2[k])) {
                        n5 = n3;
                        n6 = n2;
                        break Label_0544;
                    }
                }
                if (n3 < 0) {
                    n5 = j;
                    n6 = n2;
                }
                else {
                    int n7 = 0;
                    int n8 = 0;
                    for (int l = -1; l != n2; ++l) {
                        int n9;
                        if (l == -1) {
                            n9 = n3;
                        }
                        else {
                            n9 = array3[l];
                        }
                        final MemberBox memberBox3 = array[n9];
                        if (context.hasFeature(13) && (memberBox3.member().getModifiers() & 0x1) != (memberBox2.member().getModifiers() & 0x1)) {
                            if ((memberBox3.member().getModifiers() & 0x1) == 0x0) {
                                ++n8;
                            }
                            else {
                                ++n7;
                            }
                        }
                        else {
                            final int preferSignature = preferSignature(array2, argTypes2, memberBox2.vararg, memberBox3.argTypes, memberBox3.vararg);
                            if (preferSignature == 3) {
                                break;
                            }
                            if (preferSignature == 1) {
                                ++n8;
                            }
                            else if (preferSignature == 2) {
                                ++n7;
                            }
                            else {
                                if (preferSignature != 0) {
                                    Kit.codeBug();
                                }
                                n5 = n3;
                                n6 = n2;
                                if (!memberBox3.isStatic()) {
                                    break Label_0544;
                                }
                                n5 = n3;
                                n6 = n2;
                                if (!memberBox3.getDeclaringClass().isAssignableFrom(memberBox2.getDeclaringClass())) {
                                    break Label_0544;
                                }
                                if (l == -1) {
                                    n5 = j;
                                    n6 = n2;
                                    break Label_0544;
                                }
                                array3[l] = j;
                                n5 = n3;
                                n6 = n2;
                                break Label_0544;
                            }
                        }
                    }
                    if (n8 == n2 + 1) {
                        n5 = j;
                        n6 = 0;
                    }
                    else {
                        if (n7 != n2 + 1) {
                            if (array3 == null) {
                                array3 = new int[array.length - 1];
                            }
                            array3[n2] = j;
                            ++n2;
                            continue;
                        }
                        n6 = n2;
                        n5 = n3;
                    }
                }
            }
            n3 = n5;
            n2 = n6;
        }
        if (n3 < 0) {
            return -1;
        }
        if (n2 == 0) {
            return n3;
        }
        final StringBuilder sb = new StringBuilder();
        for (int n10 = -1; n10 != n2; ++n10) {
            int n11;
            if (n10 == -1) {
                n11 = n3;
            }
            else {
                n11 = array3[n10];
            }
            sb.append("\n    ");
            sb.append(array[n11].toJavaDeclaration());
        }
        final MemberBox memberBox4 = array[n3];
        final String name = memberBox4.getName();
        final String name2 = memberBox4.getDeclaringClass().getName();
        if (array[0].isCtor()) {
            throw Context.reportRuntimeError3("msg.constructor.ambiguous", name, scriptSignature(array2), sb.toString());
        }
        throw Context.reportRuntimeError4("msg.method.ambiguous", name2, name, scriptSignature(array2), sb.toString());
    }
    
    private static int preferSignature(final Object[] array, final Class<?>[] array2, final boolean b, final Class<?>[] array3, final boolean b2) {
        int n = 0;
        for (int i = 0; i < array.length; ++i) {
            Class<?> clazz;
            if (b && i >= array2.length) {
                clazz = array2[array2.length - 1];
            }
            else {
                clazz = array2[i];
            }
            Class<?> clazz2;
            if (b2 && i >= array3.length) {
                clazz2 = array3[array3.length - 1];
            }
            else {
                clazz2 = array3[i];
            }
            if (clazz != clazz2) {
                final Object o = array[i];
                final int conversionWeight = NativeJavaObject.getConversionWeight(o, clazz);
                final int conversionWeight2 = NativeJavaObject.getConversionWeight(o, clazz2);
                int n2;
                if (conversionWeight < conversionWeight2) {
                    n2 = 1;
                }
                else if (conversionWeight > conversionWeight2) {
                    n2 = 2;
                }
                else if (conversionWeight == 0) {
                    if (clazz.isAssignableFrom(clazz2)) {
                        n2 = 2;
                    }
                    else if (clazz2.isAssignableFrom(clazz)) {
                        n2 = 1;
                    }
                    else {
                        n2 = 3;
                    }
                }
                else {
                    n2 = 3;
                }
                final int n3 = n |= n2;
                if (n3 == 3) {
                    return n3;
                }
            }
        }
        return n;
    }
    
    private static void printDebug(final String s, final MemberBox memberBox, final Object[] array) {
    }
    
    static String scriptSignature(final Object[] array) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i != array.length; ++i) {
            final Object o = array[i];
            String s;
            if (o == null) {
                s = "null";
            }
            else if (o instanceof Boolean) {
                s = "boolean";
            }
            else if (o instanceof String) {
                s = "string";
            }
            else if (o instanceof Number) {
                s = "number";
            }
            else if (o instanceof Scriptable) {
                if (o instanceof Undefined) {
                    s = "undefined";
                }
                else if (o instanceof Wrapper) {
                    s = ((Wrapper)o).unwrap().getClass().getName();
                }
                else if (o instanceof Function) {
                    s = "function";
                }
                else {
                    s = "object";
                }
            }
            else {
                s = JavaMembers.javaSignature(((Wrapper)o).getClass());
            }
            if (i != 0) {
                sb.append(',');
            }
            sb.append(s);
        }
        return sb.toString();
    }
    
    @Override
    public Object call(final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        if (this.methods.length == 0) {
            throw new RuntimeException("No methods defined for call");
        }
        final int cachedFunction = this.findCachedFunction(context, array);
        int n = 0;
        final int n2 = 0;
        if (cachedFunction < 0) {
            final Class<?> declaringClass = this.methods[0].method().getDeclaringClass();
            final StringBuilder sb = new StringBuilder();
            sb.append(declaringClass.getName());
            sb.append('.');
            sb.append(this.getFunctionName());
            sb.append('(');
            sb.append(scriptSignature(array));
            sb.append(')');
            throw Context.reportRuntimeError1("msg.java.no_such_method", sb.toString());
        }
        final MemberBox memberBox = this.methods[cachedFunction];
        final Class<?>[] argTypes = memberBox.argTypes;
        Object[] array2;
        if (memberBox.vararg) {
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
                    if (array == (array4 = array3)) {
                        array4 = array3.clone();
                    }
                    array4[n] = jsToJava2;
                }
                ++n;
                array3 = array4;
            }
        }
        Object o2 = null;
        Label_0521: {
            if (!memberBox.isStatic()) {
                Scriptable prototype = scriptable2;
                final Class<?> declaringClass2 = memberBox.getDeclaringClass();
                while (prototype != null) {
                    if (prototype instanceof Wrapper) {
                        final Object unwrap = ((Wrapper)prototype).unwrap();
                        if (declaringClass2.isInstance(unwrap)) {
                            o2 = unwrap;
                            break Label_0521;
                        }
                    }
                    prototype = prototype.getPrototype();
                }
                throw Context.reportRuntimeError3("msg.nonjava.method", this.getFunctionName(), ScriptRuntime.toString(scriptable2), declaringClass2.getName());
            }
            o2 = null;
        }
        final Object invoke = memberBox.invoke(o2, array2);
        final Class<?> returnType = memberBox.method().getReturnType();
        Object o4;
        final Object o3 = o4 = context.getWrapFactory().wrap(context, scriptable, invoke, returnType);
        if (o3 == null) {
            o4 = o3;
            if (returnType == Void.TYPE) {
                o4 = Undefined.instance;
            }
        }
        return o4;
    }
    
    @Override
    String decompile(int n, final int n2) {
        final StringBuilder sb = new StringBuilder();
        if ((n2 & 0x1) != 0x0) {
            n = 1;
        }
        else {
            n = 0;
        }
        if (n == 0) {
            sb.append("function ");
            sb.append(this.getFunctionName());
            sb.append("() {");
        }
        sb.append("/*\n");
        sb.append(this.toString());
        String s;
        if (n != 0) {
            s = "*/\n";
        }
        else {
            s = "*/}\n";
        }
        sb.append(s);
        return sb.toString();
    }
    
    int findCachedFunction(final Context context, final Object[] array) {
        if (this.methods.length > 1) {
            if (this.overloadCache != null) {
                for (final ResolvedOverload resolvedOverload : this.overloadCache) {
                    if (resolvedOverload.matches(array)) {
                        return resolvedOverload.index;
                    }
                }
            }
            else {
                this.overloadCache = new CopyOnWriteArrayList<ResolvedOverload>();
            }
            final int function = findFunction(context, this.methods, array);
            if (this.overloadCache.size() < this.methods.length * 2) {
                synchronized (this.overloadCache) {
                    final ResolvedOverload resolvedOverload2 = new ResolvedOverload(array, function);
                    if (!this.overloadCache.contains(resolvedOverload2)) {
                        this.overloadCache.add(0, resolvedOverload2);
                    }
                    return function;
                }
            }
            return function;
        }
        return findFunction(context, this.methods, array);
    }
    
    @Override
    public String getFunctionName() {
        return this.functionName;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i != this.methods.length; ++i) {
            if (this.methods[i].isMethod()) {
                final Method method = this.methods[i].method();
                sb.append(JavaMembers.javaSignature(method.getReturnType()));
                sb.append(' ');
                sb.append(method.getName());
            }
            else {
                sb.append(this.methods[i].getName());
            }
            sb.append(JavaMembers.liveConnectSignature(this.methods[i].argTypes));
            sb.append('\n');
        }
        return sb.toString();
    }
}
