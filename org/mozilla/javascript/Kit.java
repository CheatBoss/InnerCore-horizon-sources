package org.mozilla.javascript;

import java.lang.reflect.*;
import java.util.*;
import java.io.*;

public class Kit
{
    private static Method Throwable_initCause;
    
    static {
        Kit.Throwable_initCause = null;
        try {
            final Class<?> classOrNull = classOrNull("java.lang.Throwable");
            Kit.Throwable_initCause = classOrNull.getMethod("initCause", classOrNull);
        }
        catch (Exception ex) {}
    }
    
    public static Object addListener(final Object o, final Object o2) {
        if (o2 == null) {
            throw new IllegalArgumentException();
        }
        if (o2 instanceof Object[]) {
            throw new IllegalArgumentException();
        }
        if (o == null) {
            return o2;
        }
        if (!(o instanceof Object[])) {
            return new Object[] { o, o2 };
        }
        final Object[] array = (Object[])o;
        final int length = array.length;
        if (length < 2) {
            throw new IllegalArgumentException();
        }
        final Object[] array2 = new Object[length + 1];
        System.arraycopy(array, 0, array2, 0, length);
        array2[length] = o2;
        return array2;
    }
    
    public static Class<?> classOrNull(final ClassLoader classLoader, final String s) {
        try {
            return classLoader.loadClass(s);
        }
        catch (IllegalArgumentException ex) {}
        catch (LinkageError linkageError) {}
        catch (SecurityException ex2) {}
        catch (ClassNotFoundException ex3) {}
        return null;
    }
    
    public static Class<?> classOrNull(final String s) {
        try {
            return Class.forName(s);
        }
        catch (IllegalArgumentException ex) {}
        catch (LinkageError linkageError) {}
        catch (SecurityException ex2) {}
        catch (ClassNotFoundException ex3) {}
        return null;
    }
    
    public static RuntimeException codeBug() throws RuntimeException {
        final IllegalStateException ex = new IllegalStateException("FAILED ASSERTION");
        ex.printStackTrace(System.err);
        throw ex;
    }
    
    public static RuntimeException codeBug(final String s) throws RuntimeException {
        final StringBuilder sb = new StringBuilder();
        sb.append("FAILED ASSERTION: ");
        sb.append(s);
        final IllegalStateException ex = new IllegalStateException(sb.toString());
        ex.printStackTrace(System.err);
        throw ex;
    }
    
    public static Object getListener(final Object o, final int n) {
        if (n == 0) {
            if (o == null) {
                return null;
            }
            if (!(o instanceof Object[])) {
                return o;
            }
            final Object[] array = (Object[])o;
            if (array.length < 2) {
                throw new IllegalArgumentException();
            }
            return array[0];
        }
        else if (n == 1) {
            if (o instanceof Object[]) {
                return ((Object[])o)[1];
            }
            if (o == null) {
                throw new IllegalArgumentException();
            }
            return null;
        }
        else {
            final Object[] array2 = (Object[])o;
            final int length = array2.length;
            if (length < 2) {
                throw new IllegalArgumentException();
            }
            if (n == length) {
                return null;
            }
            return array2[n];
        }
    }
    
    public static RuntimeException initCause(final RuntimeException ex, final Throwable t) {
        if (Kit.Throwable_initCause != null) {
            try {
                Kit.Throwable_initCause.invoke(ex, t);
                return ex;
            }
            catch (Exception ex2) {}
        }
        return ex;
    }
    
    static Object initHash(final Map<Object, Object> map, final Object o, Object o2) {
        while (true) {
            final Object value;
            synchronized (map) {
                value = map.get(o);
                if (value == null) {
                    map.put(o, o2);
                    return o2;
                }
            }
            o2 = value;
            return o2;
        }
    }
    
    public static Object makeHashKeyFromPair(final Object o, final Object o2) {
        if (o == null) {
            throw new IllegalArgumentException();
        }
        if (o2 == null) {
            throw new IllegalArgumentException();
        }
        return new ComplexKey(o, o2);
    }
    
    static Object newInstanceOrNull(final Class<?> clazz) {
        try {
            return clazz.newInstance();
        }
        catch (IllegalAccessException ex) {}
        catch (InstantiationException ex2) {}
        catch (LinkageError linkageError) {}
        catch (SecurityException ex3) {}
        return null;
    }
    
    public static String readReader(final Reader reader) throws IOException {
        char[] array = new char[512];
        int n = 0;
        while (true) {
            final int read = reader.read(array, n, array.length - n);
            if (read < 0) {
                break;
            }
            n += read;
            char[] array2 = array;
            if (n == array.length) {
                array2 = new char[array.length * 2];
                System.arraycopy(array, 0, array2, 0, n);
            }
            array = array2;
        }
        return new String(array, 0, n);
    }
    
    public static byte[] readStream(final InputStream inputStream, int n) throws IOException {
        if (n <= 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Bad initialBufferCapacity: ");
            sb.append(n);
            throw new IllegalArgumentException(sb.toString());
        }
        byte[] array = new byte[n];
        n = 0;
        while (true) {
            final int read = inputStream.read(array, n, array.length - n);
            if (read < 0) {
                break;
            }
            n += read;
            byte[] array2 = array;
            if (n == array.length) {
                array2 = new byte[array.length * 2];
                System.arraycopy(array, 0, array2, 0, n);
            }
            array = array2;
        }
        byte[] array3 = array;
        if (n != array.length) {
            array3 = new byte[n];
            System.arraycopy(array, 0, array3, 0, n);
        }
        return array3;
    }
    
    public static Object removeListener(final Object o, final Object o2) {
        if (o2 == null) {
            throw new IllegalArgumentException();
        }
        if (o2 instanceof Object[]) {
            throw new IllegalArgumentException();
        }
        if (o == o2) {
            return null;
        }
        if (o instanceof Object[]) {
            final Object[] array = (Object[])o;
            final int length = array.length;
            if (length < 2) {
                throw new IllegalArgumentException();
            }
            if (length == 2) {
                if (array[1] == o2) {
                    return array[0];
                }
                if (array[0] == o2) {
                    return array[1];
                }
            }
            else {
                int n = length;
                int n2;
                do {
                    n2 = n - 1;
                    if (array[n2] == o2) {
                        final Object[] array2 = new Object[length - 1];
                        System.arraycopy(array, 0, array2, 0, n2);
                        System.arraycopy(array, n2 + 1, array2, n2, length - (n2 + 1));
                        return array2;
                    }
                } while ((n = n2) != 0);
            }
        }
        return o;
    }
    
    static boolean testIfCanLoadRhinoClasses(final ClassLoader classLoader) {
        final Class<?> contextFactoryClass = ScriptRuntime.ContextFactoryClass;
        return classOrNull(classLoader, contextFactoryClass.getName()) == contextFactoryClass;
    }
    
    public static int xDigitToInt(int n, final int n2) {
        if (n <= 57) {
            n -= 48;
            if (n < 0) {
                return -1;
            }
        }
        else if (n <= 70) {
            if (65 > n) {
                return -1;
            }
            n -= 55;
        }
        else {
            if (n > 102 || 97 > n) {
                return -1;
            }
            n -= 87;
        }
        return n2 << 4 | n;
    }
    
    private static final class ComplexKey
    {
        private int hash;
        private Object key1;
        private Object key2;
        
        ComplexKey(final Object key1, final Object key2) {
            this.key1 = key1;
            this.key2 = key2;
        }
        
        @Override
        public boolean equals(final Object o) {
            final boolean b = o instanceof ComplexKey;
            final boolean b2 = false;
            if (!b) {
                return false;
            }
            final ComplexKey complexKey = (ComplexKey)o;
            boolean b3 = b2;
            if (this.key1.equals(complexKey.key1)) {
                b3 = b2;
                if (this.key2.equals(complexKey.key2)) {
                    b3 = true;
                }
            }
            return b3;
        }
        
        @Override
        public int hashCode() {
            if (this.hash == 0) {
                this.hash = (this.key1.hashCode() ^ this.key2.hashCode());
            }
            return this.hash;
        }
    }
}
