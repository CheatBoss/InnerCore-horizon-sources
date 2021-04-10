package bo.app;

import java.io.*;
import java.util.*;

final class bc
{
    final String a;
    final long[] b;
    boolean c;
    bb.a d;
    long e;
    private int f;
    private File g;
    
    bc(final String a, final int f, final File g) {
        this.a = a;
        this.f = f;
        this.g = g;
        this.b = new long[f];
    }
    
    public File a(final int n) {
        final File g = this.g;
        final StringBuilder sb = new StringBuilder();
        sb.append(this.a);
        sb.append(".");
        sb.append(n);
        return new File(g, sb.toString());
    }
    
    public String a() {
        final StringBuilder sb = new StringBuilder();
        final long[] b = this.b;
        for (int length = b.length, i = 0; i < length; ++i) {
            final long n = b[i];
            sb.append(' ');
            sb.append(n);
        }
        return sb.toString();
    }
    
    void a(final String[] array) {
        if (array.length == this.f) {
            int i = 0;
            try {
                while (i < array.length) {
                    this.b[i] = Long.parseLong(array[i]);
                    ++i;
                }
                return;
            }
            catch (NumberFormatException ex) {
                throw this.b(array);
            }
        }
        throw this.b(array);
    }
    
    public File b(final int n) {
        final File g = this.g;
        final StringBuilder sb = new StringBuilder();
        sb.append(this.a);
        sb.append(".");
        sb.append(n);
        sb.append(".tmp");
        return new File(g, sb.toString());
    }
    
    IOException b(final String[] array) {
        final StringBuilder sb = new StringBuilder();
        sb.append("unexpected journal line: ");
        sb.append(Arrays.toString(array));
        throw new IOException(sb.toString());
    }
}
