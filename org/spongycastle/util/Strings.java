package org.spongycastle.util;

import java.security.*;
import java.io.*;
import java.util.stream.*;
import java.util.*;
import java.util.function.*;

public final class Strings
{
    private static String LINE_SEPARATOR;
    
    static {
        try {
            Strings.LINE_SEPARATOR = AccessController.doPrivileged((PrivilegedAction<String>)new PrivilegedAction<String>() {
                @Override
                public String run() {
                    return System.getProperty("line.separator");
                }
            });
        }
        catch (Exception ex) {
            try {
                Strings.LINE_SEPARATOR = String.format("%n", new Object[0]);
            }
            catch (Exception ex2) {
                Strings.LINE_SEPARATOR = "\n";
            }
        }
    }
    
    public static char[] asCharArray(final byte[] array) {
        final int length = array.length;
        final char[] array2 = new char[length];
        for (int i = 0; i != length; ++i) {
            array2[i] = (char)(array[i] & 0xFF);
        }
        return array2;
    }
    
    public static String fromByteArray(final byte[] array) {
        return new String(asCharArray(array));
    }
    
    public static String fromUTF8ByteArray(final byte[] array) {
        final int n = 0;
        int i = 0;
        int n2 = 0;
        while (i < array.length) {
            ++n2;
            if ((array[i] & 0xF0) == 0xF0) {
                ++n2;
                i += 4;
            }
            else if ((array[i] & 0xE0) == 0xE0) {
                i += 3;
            }
            else if ((array[i] & 0xC0) == 0xC0) {
                i += 2;
            }
            else {
                ++i;
            }
        }
        final char[] array2 = new char[n2];
        int n3 = 0;
        int j = n;
        while (j < array.length) {
            char c2 = '\0';
            Label_0357: {
                if ((array[j] & 0xF0) == 0xF0) {
                    final int n4 = ((array[j] & 0x3) << 18 | (array[j + 1] & 0x3F) << 12 | (array[j + 2] & 0x3F) << 6 | (array[j + 3] & 0x3F)) - 65536;
                    final char c = (char)(0xD800 | n4 >> 10);
                    c2 = (char)((n4 & 0x3FF) | 0xDC00);
                    array2[n3] = c;
                    j += 4;
                    ++n3;
                }
                else if ((array[j] & 0xE0) == 0xE0) {
                    c2 = (char)((array[j] & 0xF) << 12 | (array[j + 1] & 0x3F) << 6 | (array[j + 2] & 0x3F));
                    j += 3;
                }
                else {
                    int n5;
                    byte b;
                    if ((array[j] & 0xD0) == 0xD0) {
                        n5 = (array[j] & 0x1F) << 6;
                        b = array[j + 1];
                    }
                    else {
                        if ((array[j] & 0xC0) != 0xC0) {
                            c2 = (char)(array[j] & 0xFF);
                            ++j;
                            break Label_0357;
                        }
                        n5 = (array[j] & 0x1F) << 6;
                        b = array[j + 1];
                    }
                    c2 = (char)(n5 | (b & 0x3F));
                    j += 2;
                }
            }
            array2[n3] = c2;
            ++n3;
        }
        return new String(array2);
    }
    
    public static String lineSeparator() {
        return Strings.LINE_SEPARATOR;
    }
    
    public static StringList newList() {
        return new StringListImpl();
    }
    
    public static String[] split(String substring, final char c) {
        final Vector<String> vector = new Vector<String>();
        final int n = 0;
        int i = 1;
        while (i != 0) {
            final int index = substring.indexOf(c);
            if (index > 0) {
                vector.addElement(substring.substring(0, index));
                substring = substring.substring(index + 1);
            }
            else {
                vector.addElement(substring);
                i = 0;
            }
        }
        final int size = vector.size();
        final String[] array = new String[size];
        for (int j = n; j != size; ++j) {
            array[j] = vector.elementAt(j);
        }
        return array;
    }
    
    public static int toByteArray(final String s, final byte[] array, final int n) {
        final int length = s.length();
        for (int i = 0; i < length; ++i) {
            array[n + i] = (byte)s.charAt(i);
        }
        return length;
    }
    
    public static byte[] toByteArray(final String s) {
        final int length = s.length();
        final byte[] array = new byte[length];
        for (int i = 0; i != length; ++i) {
            array[i] = (byte)s.charAt(i);
        }
        return array;
    }
    
    public static byte[] toByteArray(final char[] array) {
        final int length = array.length;
        final byte[] array2 = new byte[length];
        for (int i = 0; i != length; ++i) {
            array2[i] = (byte)array[i];
        }
        return array2;
    }
    
    public static String toLowerCase(String s) {
        final char[] charArray = s.toCharArray();
        int i = 0;
        int n = 0;
        while (i != charArray.length) {
            final char c = charArray[i];
            int n2 = n;
            if ('A' <= c) {
                n2 = n;
                if ('Z' >= c) {
                    charArray[i] = (char)(c - 'A' + 97);
                    n2 = 1;
                }
            }
            ++i;
            n = n2;
        }
        if (n != 0) {
            s = new String(charArray);
        }
        return s;
    }
    
    public static void toUTF8ByteArray(final char[] array, final OutputStream outputStream) throws IOException {
        for (int i = 0; i < array.length; ++i) {
            int n = array[i];
            if (n >= 128) {
                Label_0142: {
                    int n2;
                    if (n < 2048) {
                        n2 = (n >> 6 | 0xC0);
                    }
                    else if (n >= 55296 && n <= 57343) {
                        ++i;
                        if (i >= array.length) {
                            throw new IllegalStateException("invalid UTF-16 codepoint");
                        }
                        final char c = array[i];
                        if (n <= 56319) {
                            n = ((n & 0x3FF) << 10 | (c & '\u03ff')) + 65536;
                            outputStream.write(n >> 18 | 0xF0);
                            outputStream.write((n >> 12 & 0x3F) | 0x80);
                            outputStream.write((n >> 6 & 0x3F) | 0x80);
                            break Label_0142;
                        }
                        throw new IllegalStateException("invalid UTF-16 codepoint");
                    }
                    else {
                        outputStream.write(n >> 12 | 0xE0);
                        n2 = ((n >> 6 & 0x3F) | 0x80);
                    }
                    outputStream.write(n2);
                }
                n = ((n & 0x3F) | 0x80);
            }
            outputStream.write(n);
        }
    }
    
    public static byte[] toUTF8ByteArray(final String s) {
        return toUTF8ByteArray(s.toCharArray());
    }
    
    public static byte[] toUTF8ByteArray(final char[] array) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            toUTF8ByteArray(array, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
        catch (IOException ex) {
            throw new IllegalStateException("cannot encode string to byte array!");
        }
    }
    
    public static String toUpperCase(String s) {
        final char[] charArray = s.toCharArray();
        int i = 0;
        int n = 0;
        while (i != charArray.length) {
            final char c = charArray[i];
            int n2 = n;
            if ('a' <= c) {
                n2 = n;
                if ('z' >= c) {
                    charArray[i] = (char)(c - 'a' + 65);
                    n2 = 1;
                }
            }
            ++i;
            n = n2;
        }
        if (n != 0) {
            s = new String(charArray);
        }
        return s;
    }
    
    private static class StringListImpl extends ArrayList<String> implements StringList
    {
        @Override
        public void add(final int n, final String s) {
            super.add(n, s);
        }
        
        @Override
        public boolean add(final String s) {
            return super.add(s);
        }
        
        @Override
        public Stream<Object> parallelStream() {
            return (Stream<Object>)Collection-CC.$default$parallelStream((Collection)this);
        }
        
        @Override
        public String set(final int n, final String s) {
            return super.set(n, s);
        }
        
        @Override
        public Stream<Object> stream() {
            return (Stream<Object>)Collection-CC.$default$stream((Collection)this);
        }
        
        public <T> T[] toArray(final IntFunction<T[]> intFunction) {
            return (T[])Collection-CC.$default$toArray((Collection)this, (IntFunction)intFunction);
        }
        
        @Override
        public String[] toStringArray() {
            final int size = this.size();
            final String[] array = new String[size];
            for (int i = 0; i != size; ++i) {
                array[i] = this.get(i);
            }
            return array;
        }
        
        @Override
        public String[] toStringArray(final int n, final int n2) {
            final String[] array = new String[n2 - n];
            for (int n3 = n; n3 != this.size() && n3 != n2; ++n3) {
                array[n3 - n] = this.get(n3);
            }
            return array;
        }
    }
}
