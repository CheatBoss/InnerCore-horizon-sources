package org.reflections.util;

import org.slf4j.*;
import javax.annotation.*;
import com.google.common.collect.*;
import org.reflections.*;
import java.lang.reflect.*;
import com.google.common.base.*;
import java.util.*;
import java.io.*;

public abstract class Utils
{
    public static void close(final InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            }
            catch (IOException ex) {
                if (Reflections.log != null) {
                    Reflections.log.warn("Could not close InputStream", (Throwable)ex);
                }
            }
        }
    }
    
    @Nullable
    public static Logger findLogger(final Class<?> clazz) {
        try {
            Class.forName("org.slf4j.impl.StaticLoggerBinder");
            return LoggerFactory.getLogger((Class)clazz);
        }
        catch (Throwable t) {
            return null;
        }
    }
    
    public static Set<Constructor> getConstructorsFromDescriptors(final Iterable<String> iterable, final ClassLoader... array) {
        final HashSet hashSet = Sets.newHashSet();
        for (final String s : iterable) {
            if (isConstructor(s)) {
                final Constructor constructor = (Constructor)getMemberFromDescriptor(s, array);
                if (constructor == null) {
                    continue;
                }
                hashSet.add(constructor);
            }
        }
        return (Set<Constructor>)hashSet;
    }
    
    public static Field getFieldFromString(String substring, final ClassLoader... array) {
        final String substring2 = substring.substring(0, substring.lastIndexOf(46));
        substring = substring.substring(substring.lastIndexOf(46) + 1);
        try {
            return ReflectionUtils.forName(substring2, array).getDeclaredField(substring);
        }
        catch (NoSuchFieldException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Can't resolve field named ");
            sb.append(substring);
            throw new ReflectionsException(sb.toString(), ex);
        }
    }
    
    public static Member getMemberFromDescriptor(final String s, ClassLoader... o) throws ReflectionsException {
        final int lastIndex = s.lastIndexOf(40);
        int i = 0;
        String substring;
        if (lastIndex != -1) {
            substring = s.substring(0, lastIndex);
        }
        else {
            substring = s;
        }
        String substring2;
        if (lastIndex != -1) {
            substring2 = s.substring(lastIndex + 1, s.lastIndexOf(41));
        }
        else {
            substring2 = "";
        }
        final int max = Math.max(substring.lastIndexOf(46), substring.lastIndexOf("$"));
        final String substring3 = substring.substring(substring.lastIndexOf(32) + 1, max);
        final String substring4 = substring.substring(max + 1);
        Class<?>[] array = null;
        if (!isEmpty(substring2)) {
            final String[] split = substring2.split(",");
            final ArrayList list = new ArrayList<Class<?>>(split.length);
            while (i < split.length) {
                list.add(ReflectionUtils.forName(split[i].trim(), (ClassLoader[])o));
                ++i;
            }
            array = list.toArray(new Class[list.size()]);
        }
        o = ReflectionUtils.forName(substring3, (ClassLoader[])o);
        while (o != null) {
            try {
                if (!s.contains("(")) {
                    if (((Class)o).isInterface()) {
                        return ((Class)o).getField(substring4);
                    }
                    return ((Class)o).getDeclaredField(substring4);
                }
                else if (isConstructor(s)) {
                    if (((Class)o).isInterface()) {
                        return ((Class<Object>)o).getConstructor(array);
                    }
                    return ((Class<Object>)o).getDeclaredConstructor(array);
                }
                else {
                    if (((Class)o).isInterface()) {
                        return ((Class)o).getMethod(substring4, array);
                    }
                    return ((Class)o).getDeclaredMethod(substring4, array);
                }
            }
            catch (Exception ex) {
                o = ((Class<Object>)o).getSuperclass();
                continue;
            }
            break;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Can't resolve member named ");
        sb.append(substring4);
        sb.append(" for class ");
        sb.append(substring3);
        throw new ReflectionsException(sb.toString());
    }
    
    public static Set<Member> getMembersFromDescriptors(Iterable<String> s, final ClassLoader... array) {
        final HashSet hashSet = Sets.newHashSet();
        final Iterator<String> iterator = ((Iterable<String>)s).iterator();
        while (iterator.hasNext()) {
            s = iterator.next();
            try {
                hashSet.add(getMemberFromDescriptor(s, array));
                continue;
            }
            catch (ReflectionsException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Can't resolve member named ");
                sb.append(s);
                throw new ReflectionsException(sb.toString(), ex);
            }
            break;
        }
        return (Set<Member>)hashSet;
    }
    
    public static Set<Method> getMethodsFromDescriptors(final Iterable<String> iterable, final ClassLoader... array) {
        final HashSet hashSet = Sets.newHashSet();
        for (final String s : iterable) {
            if (!isConstructor(s)) {
                final Method method = (Method)getMemberFromDescriptor(s, array);
                if (method == null) {
                    continue;
                }
                hashSet.add(method);
            }
        }
        return (Set<Method>)hashSet;
    }
    
    public static boolean isConstructor(final String s) {
        return s.contains("init>");
    }
    
    public static boolean isEmpty(final String s) {
        return s == null || s.length() == 0;
    }
    
    public static boolean isEmpty(final Object[] array) {
        return array == null || array.length == 0;
    }
    
    public static String name(Class componentType) {
        if (!componentType.isArray()) {
            return componentType.getName();
        }
        int n = 0;
        while (componentType.isArray()) {
            ++n;
            componentType = componentType.getComponentType();
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(componentType.getName());
        sb.append(repeat("[]", n));
        return sb.toString();
    }
    
    public static String name(final Constructor constructor) {
        final StringBuilder sb = new StringBuilder();
        sb.append(constructor.getName());
        sb.append(".");
        sb.append("<init>");
        sb.append("(");
        sb.append(Joiner.on(",").join((Iterable)names((Class<?>[])constructor.getParameterTypes())));
        sb.append(")");
        return sb.toString();
    }
    
    public static String name(final Field field) {
        final StringBuilder sb = new StringBuilder();
        sb.append(field.getDeclaringClass().getName());
        sb.append(".");
        sb.append(field.getName());
        return sb.toString();
    }
    
    public static String name(final Method method) {
        final StringBuilder sb = new StringBuilder();
        sb.append(method.getDeclaringClass().getName());
        sb.append(".");
        sb.append(method.getName());
        sb.append("(");
        sb.append(Joiner.on(", ").join((Iterable)names(method.getParameterTypes())));
        sb.append(")");
        return sb.toString();
    }
    
    public static List<String> names(final Iterable<Class<?>> iterable) {
        final ArrayList<String> list = new ArrayList<String>();
        final Iterator<Class<?>> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            list.add(name(iterator.next()));
        }
        return list;
    }
    
    public static List<String> names(final Class<?>... array) {
        return names(Arrays.asList(array));
    }
    
    public static File prepareFile(final String s) {
        final File file = new File(s);
        final File parentFile = file.getAbsoluteFile().getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        return file;
    }
    
    public static String repeat(final String s, final int n) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; ++i) {
            sb.append(s);
        }
        return sb.toString();
    }
}
