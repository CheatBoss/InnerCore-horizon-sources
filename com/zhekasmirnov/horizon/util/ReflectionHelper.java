package com.zhekasmirnov.horizon.util;

import android.util.*;
import java.lang.reflect.*;

public class ReflectionHelper
{
    public static String getClassStructureString(final Class clazz, final Object obj, final String prefix, final boolean showSuperclasses) {
        final StringBuilder result = new StringBuilder();
        result.append(prefix).append(clazz.toString()).append("\n");
        for (final Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                result.append(prefix).append("  ").append(field).append(" = ").append(field.get(obj)).append("\n");
            }
            catch (IllegalAccessException e) {
                result.append(e).append("\n");
            }
            catch (NullPointerException ex) {}
        }
        for (final Constructor constructor : clazz.getDeclaredConstructors()) {
            result.append(prefix).append("  ").append(constructor).append("\n");
        }
        for (final Method method : clazz.getDeclaredMethods()) {
            result.append(prefix).append("  ").append(method).append("\n");
        }
        if (showSuperclasses && clazz.getSuperclass() != Object.class && clazz.getSuperclass() != null) {
            result.append("\n").append(getClassStructureString(clazz.getSuperclass(), obj, prefix, showSuperclasses));
        }
        return result.toString();
    }
    
    public static void printClassStructure(final Class clazz, final Object obj, final String tag, final String prefix, final boolean superclassStructure) {
        for (final String line : getClassStructureString(clazz, obj, prefix, superclassStructure).split("\n")) {
            Log.i(tag, line);
        }
    }
    
    public static Method getDeclaredMethod(final Class clazz, final String name, final Class... params) {
        try {
            return clazz.getDeclaredMethod(name, (Class[])params);
        }
        catch (NoSuchMethodException e) {
            return null;
        }
    }
    
    public static Object invokeMethod(final Object target, final Class clazz, final String method, final Class[] paramTypes, final Object[] params) throws NoSuchMethodException {
        try {
            return clazz.getDeclaredMethod(method, (Class[])paramTypes).invoke(target, params);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        catch (InvocationTargetException e2) {
            e2.printStackTrace();
            throw new RuntimeException(e2);
        }
    }
}
