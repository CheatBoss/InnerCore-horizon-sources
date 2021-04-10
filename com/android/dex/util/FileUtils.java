package com.android.dex.util;

import java.io.*;

public final class FileUtils
{
    private FileUtils() {
    }
    
    public static boolean hasArchiveSuffix(final String s) {
        return s.endsWith(".zip") || s.endsWith(".jar") || s.endsWith(".apk");
    }
    
    public static byte[] readFile(final File file) {
        if (!file.exists()) {
            final StringBuilder sb = new StringBuilder();
            sb.append(file);
            sb.append(": file not found");
            throw new RuntimeException(sb.toString());
        }
        if (!file.isFile()) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(file);
            sb2.append(": not a file");
            throw new RuntimeException(sb2.toString());
        }
        if (!file.canRead()) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(file);
            sb3.append(": file not readable");
            throw new RuntimeException(sb3.toString());
        }
        final long length = file.length();
        int n = (int)length;
        if (n != length) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(file);
            sb4.append(": file too long");
            throw new RuntimeException(sb4.toString());
        }
        while (true) {
            final byte[] array = new byte[n];
            while (true) {
                int n2;
                int read;
                try {
                    final FileInputStream fileInputStream = new FileInputStream(file);
                    n2 = 0;
                    if (n <= 0) {
                        fileInputStream.close();
                        return array;
                    }
                    read = fileInputStream.read(array, n2, n);
                    if (read == -1) {
                        final StringBuilder sb5 = new StringBuilder();
                        sb5.append(file);
                        sb5.append(": unexpected EOF");
                        throw new RuntimeException(sb5.toString());
                    }
                }
                catch (IOException ex) {
                    final StringBuilder sb6 = new StringBuilder();
                    sb6.append(file);
                    sb6.append(": trouble reading");
                    throw new RuntimeException(sb6.toString(), ex);
                }
                n2 += read;
                n -= read;
                continue;
            }
        }
    }
    
    public static byte[] readFile(final String s) {
        return readFile(new File(s));
    }
}
