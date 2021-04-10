package com.bumptech.glide.disklrucache;

import java.nio.charset.*;
import java.io.*;

final class Util
{
    static final Charset US_ASCII;
    static final Charset UTF_8;
    
    static {
        US_ASCII = Charset.forName("US-ASCII");
        UTF_8 = Charset.forName("UTF-8");
    }
    
    private Util() {
    }
    
    static void closeQuietly(final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            }
            catch (Exception ex2) {}
            catch (RuntimeException ex) {
                throw ex;
            }
        }
    }
    
    static void deleteContents(File file) throws IOException {
        final File[] listFiles = file.listFiles();
        if (listFiles == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("not a readable directory: ");
            sb.append(file);
            throw new IOException(sb.toString());
        }
        for (int length = listFiles.length, i = 0; i < length; ++i) {
            file = listFiles[i];
            if (file.isDirectory()) {
                deleteContents(file);
            }
            if (!file.delete()) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("failed to delete file: ");
                sb2.append(file);
                throw new IOException(sb2.toString());
            }
        }
    }
    
    static String readFully(final Reader reader) throws IOException {
        try {
            final StringWriter stringWriter = new StringWriter();
            final char[] array = new char[1024];
            while (true) {
                final int read = reader.read(array);
                if (read == -1) {
                    break;
                }
                stringWriter.write(array, 0, read);
            }
            return stringWriter.toString();
        }
        finally {
            reader.close();
        }
    }
}
