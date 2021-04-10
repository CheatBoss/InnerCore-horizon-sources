package bo.app;

import java.nio.charset.*;
import java.io.*;

final class be
{
    static final Charset a;
    static final Charset b;
    
    static {
        a = Charset.forName("US-ASCII");
        b = Charset.forName("UTF-8");
    }
    
    static void a(final Closeable closeable) {
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
    
    static void a(File file) {
        final File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (int length = listFiles.length, i = 0; i < length; ++i) {
                file = listFiles[i];
                if (file.isDirectory()) {
                    a(file);
                }
                if (!file.delete()) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("failed to delete file: ");
                    sb.append(file);
                    throw new IOException(sb.toString());
                }
            }
            return;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("not a readable directory: ");
        sb2.append(file);
        throw new IOException(sb2.toString());
    }
}
