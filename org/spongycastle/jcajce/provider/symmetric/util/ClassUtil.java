package org.spongycastle.jcajce.provider.symmetric.util;

import java.security.*;

public class ClassUtil
{
    public static Class loadClass(Class clazz, final String s) {
        try {
            final ClassLoader classLoader = clazz.getClassLoader();
            if (classLoader != null) {
                return classLoader.loadClass(s);
            }
            clazz = AccessController.doPrivileged((PrivilegedAction<Class>)new PrivilegedAction() {
                @Override
                public Object run() {
                    try {
                        return Class.forName(s);
                    }
                    catch (Exception ex) {
                        return null;
                    }
                }
            });
            return clazz;
        }
        catch (ClassNotFoundException ex) {
            return null;
        }
    }
}
