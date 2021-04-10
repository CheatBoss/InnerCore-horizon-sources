package org.mozilla.javascript;

import java.io.*;
import java.lang.reflect.*;

final class MemberBox implements Serializable
{
    private static final Class<?>[] primitives;
    static final long serialVersionUID = 6358550398665688245L;
    transient Class<?>[] argTypes;
    transient Object delegateTo;
    private transient Member memberObject;
    transient boolean vararg;
    
    static {
        primitives = new Class[] { Boolean.TYPE, Byte.TYPE, Character.TYPE, Double.TYPE, Float.TYPE, Integer.TYPE, Long.TYPE, Short.TYPE, Void.TYPE };
    }
    
    MemberBox(final Constructor<?> constructor) {
        this.init(constructor);
    }
    
    MemberBox(final Method method) {
        this.init(method);
    }
    
    private void init(final Constructor<?> memberObject) {
        this.memberObject = memberObject;
        this.argTypes = (Class<?>[])memberObject.getParameterTypes();
        this.vararg = VMBridge.instance.isVarArgs(memberObject);
    }
    
    private void init(final Method memberObject) {
        this.memberObject = memberObject;
        this.argTypes = memberObject.getParameterTypes();
        this.vararg = VMBridge.instance.isVarArgs(memberObject);
    }
    
    private static Member readMember(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        if (!objectInputStream.readBoolean()) {
            return null;
        }
        final boolean boolean1 = objectInputStream.readBoolean();
        final String s = (String)objectInputStream.readObject();
        final Class clazz = (Class)objectInputStream.readObject();
        final Class<?>[] parameters = readParameters(objectInputStream);
        Label_0046: {
            if (!boolean1) {
                break Label_0046;
            }
            while (true) {
                try {
                    return clazz.getMethod(s, (Class[])parameters);
                    return clazz.getConstructor((Class[])parameters);
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Cannot find member: ");
                    final NoSuchMethodException ex;
                    sb.append(ex);
                    throw new IOException(sb.toString());
                }
                catch (NoSuchMethodException ex) {
                    continue;
                }
                break;
            }
        }
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        final Member member = readMember(objectInputStream);
        if (member instanceof Method) {
            this.init((Method)member);
            return;
        }
        this.init((Constructor<?>)member);
    }
    
    private static Class<?>[] readParameters(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        final Class[] array = new Class[objectInputStream.readShort()];
        for (int i = 0; i < array.length; ++i) {
            if (!objectInputStream.readBoolean()) {
                array[i] = (Class)objectInputStream.readObject();
            }
            else {
                array[i] = MemberBox.primitives[objectInputStream.readByte()];
            }
        }
        return (Class<?>[])array;
    }
    
    private static Method searchAccessibleMethod(Method method, final Class<?>[] array) {
        final int modifiers = method.getModifiers();
        if (Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers)) {
            final Class<?> declaringClass = method.getDeclaringClass();
            if (!Modifier.isPublic(declaringClass.getModifiers())) {
                final String name = method.getName();
                final Class<?>[] interfaces = declaringClass.getInterfaces();
                int n = 0;
                final int length = interfaces.length;
                Class<?> clazz;
                while (true) {
                    clazz = declaringClass;
                    if (n == length) {
                        break;
                    }
                    final Class<?> clazz2 = interfaces[n];
                    if (Modifier.isPublic(clazz2.getModifiers())) {
                        try {
                            method = clazz2.getMethod(name, array);
                            return method;
                        }
                        catch (SecurityException ex) {}
                        catch (NoSuchMethodException ex2) {}
                    }
                    ++n;
                }
                while (true) {
                    final Class<?> superclass = clazz.getSuperclass();
                    if (superclass == null) {
                        break;
                    }
                    clazz = superclass;
                    if (!Modifier.isPublic(superclass.getModifiers())) {
                        continue;
                    }
                    try {
                        method = superclass.getMethod(name, array);
                        final int modifiers2 = method.getModifiers();
                        if (Modifier.isPublic(modifiers2) && !Modifier.isStatic(modifiers2)) {
                            return method;
                        }
                    }
                    catch (SecurityException ex3) {
                        clazz = superclass;
                        continue;
                    }
                    catch (NoSuchMethodException ex4) {}
                    clazz = superclass;
                }
            }
        }
        return null;
    }
    
    private static void writeMember(final ObjectOutputStream objectOutputStream, final Member member) throws IOException {
        if (member == null) {
            objectOutputStream.writeBoolean(false);
            return;
        }
        objectOutputStream.writeBoolean(true);
        if (!(member instanceof Method) && !(member instanceof Constructor)) {
            throw new IllegalArgumentException("not Method or Constructor");
        }
        objectOutputStream.writeBoolean(member instanceof Method);
        objectOutputStream.writeObject(member.getName());
        objectOutputStream.writeObject(member.getDeclaringClass());
        if (member instanceof Method) {
            writeParameters(objectOutputStream, ((Method)member).getParameterTypes());
            return;
        }
        writeParameters(objectOutputStream, ((Constructor)member).getParameterTypes());
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        writeMember(objectOutputStream, this.memberObject);
    }
    
    private static void writeParameters(final ObjectOutputStream objectOutputStream, final Class<?>[] array) throws IOException {
        objectOutputStream.writeShort(array.length);
    Label_0074:
        for (int i = 0; i < array.length; ++i) {
            final Class<?> clazz = array[i];
            final boolean primitive = clazz.isPrimitive();
            objectOutputStream.writeBoolean(primitive);
            if (primitive) {
                for (int j = 0; j < MemberBox.primitives.length; ++j) {
                    if (clazz.equals(MemberBox.primitives[j])) {
                        objectOutputStream.writeByte(j);
                        continue Label_0074;
                    }
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("Primitive ");
                sb.append(clazz);
                sb.append(" not found");
                throw new IllegalArgumentException(sb.toString());
            }
            objectOutputStream.writeObject(clazz);
        }
    }
    
    Constructor<?> ctor() {
        return (Constructor<?>)this.memberObject;
    }
    
    Class<?> getDeclaringClass() {
        return this.memberObject.getDeclaringClass();
    }
    
    String getName() {
        return this.memberObject.getName();
    }
    
    Object invoke(Object ex, final Object[] array) {
        final Method method = this.method();
        try {
            try {
                return method.invoke(ex, array);
            }
            catch (Exception ex) {}
            catch (InvocationTargetException ex) {
                Throwable t;
                do {
                    t = (ex = (Exception)((InvocationTargetException)ex).getTargetException());
                } while (t instanceof InvocationTargetException);
                if (t instanceof ContinuationPending) {
                    throw (ContinuationPending)t;
                }
                throw Context.throwAsScriptRuntimeEx(t);
                final Method memberObject = method;
                // iftrue(Label_0068:, VMBridge.instance.tryToMakeAccessible((Object)method))
                final IllegalAccessException ex2;
                throw Context.throwAsScriptRuntimeEx(ex2);
                this.memberObject = memberObject;
                return memberObject.invoke(ex, array);
                Label_0068: {
                    ex = (Exception)memberObject.invoke(ex, array);
                }
                return ex;
            }
        }
        catch (IllegalAccessException ex3) {}
    }
    
    boolean isCtor() {
        return this.memberObject instanceof Constructor;
    }
    
    boolean isMethod() {
        return this.memberObject instanceof Method;
    }
    
    boolean isStatic() {
        return Modifier.isStatic(this.memberObject.getModifiers());
    }
    
    Member member() {
        return this.memberObject;
    }
    
    Method method() {
        return (Method)this.memberObject;
    }
    
    Object newInstance(final Object[] ex) {
        final Constructor<?> ctor = this.ctor();
        try {
            try {
                return ctor.newInstance((Object[])(Object)ex);
            }
            catch (Exception ex) {}
        }
        catch (IllegalAccessException ex2) {
            if (!VMBridge.instance.tryToMakeAccessible(ctor)) {
                throw Context.throwAsScriptRuntimeEx(ex2);
            }
            return ctor.newInstance((Object[])(Object)ex);
        }
        throw Context.throwAsScriptRuntimeEx(ex);
    }
    
    String toJavaDeclaration() {
        final StringBuilder sb = new StringBuilder();
        if (this.isMethod()) {
            final Method method = this.method();
            sb.append(method.getReturnType());
            sb.append(' ');
            sb.append(method.getName());
        }
        else {
            final String name = this.ctor().getDeclaringClass().getName();
            final int lastIndex = name.lastIndexOf(46);
            String substring = name;
            if (lastIndex >= 0) {
                substring = name.substring(lastIndex + 1);
            }
            sb.append(substring);
        }
        sb.append(JavaMembers.liveConnectSignature(this.argTypes));
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return this.memberObject.toString();
    }
}
