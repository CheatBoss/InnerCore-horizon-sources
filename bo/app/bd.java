package bo.app;

import java.nio.charset.*;
import java.io.*;

class bd implements Closeable
{
    private final InputStream a;
    private final Charset b;
    private byte[] c;
    private int d;
    private int e;
    
    public bd(final InputStream a, final int n, final Charset b) {
        if (a == null || b == null) {
            throw null;
        }
        if (n < 0) {
            throw new IllegalArgumentException("capacity <= 0");
        }
        if (b.equals(be.a)) {
            this.a = a;
            this.b = b;
            this.c = new byte[n];
            return;
        }
        throw new IllegalArgumentException("Unsupported encoding");
    }
    
    public bd(final InputStream inputStream, final Charset charset) {
        this(inputStream, 8192, charset);
    }
    
    private void c() {
        final InputStream a = this.a;
        final byte[] c = this.c;
        final int read = a.read(c, 0, c.length);
        if (read != -1) {
            this.d = 0;
            this.e = read;
            return;
        }
        throw new EOFException();
    }
    
    public String a() {
    Label_0185_Outer:
        while (true) {
            while (true) {
                int i = 0;
            Label_0274:
                while (true) {
                    synchronized (this.a) {
                        if (this.c == null) {
                            throw new IOException("LineReader is closed");
                        }
                        if (this.d >= this.e) {
                            this.c();
                        }
                        i = this.d;
                        if (i != this.e) {
                            if (this.c[i] == 10) {
                                int n = 0;
                                Label_0085: {
                                    if (i != this.d) {
                                        final byte[] c = this.c;
                                        n = i - 1;
                                        if (c[n] == 13) {
                                            break Label_0085;
                                        }
                                    }
                                    n = i;
                                }
                                final String s = new String(this.c, this.d, n - this.d, this.b.name());
                                this.d = i + 1;
                                return s;
                            }
                        }
                        else {
                            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(this.e - this.d + 80) {
                                @Override
                                public String toString() {
                                    int count;
                                    if (this.count > 0 && this.buf[this.count - 1] == 13) {
                                        count = this.count - 1;
                                    }
                                    else {
                                        count = this.count;
                                    }
                                    try {
                                        return new String(this.buf, 0, count, bd.this.b.name());
                                    }
                                    catch (UnsupportedEncodingException ex) {
                                        throw new AssertionError((Object)ex);
                                    }
                                }
                            };
                            do {
                                byteArrayOutputStream.write(this.c, this.d, this.e - this.d);
                                this.e = -1;
                                this.c();
                                i = this.d;
                            } while (i == this.e);
                            if (this.c[i] == 10) {
                                if (i != this.d) {
                                    byteArrayOutputStream.write(this.c, this.d, i - this.d);
                                }
                                this.d = i + 1;
                                return byteArrayOutputStream.toString();
                            }
                            break Label_0274;
                        }
                    }
                    ++i;
                    continue Label_0185_Outer;
                }
                ++i;
                continue;
            }
        }
    }
    
    public boolean b() {
        return this.e == -1;
    }
    
    @Override
    public void close() {
        synchronized (this.a) {
            if (this.c != null) {
                this.c = null;
                this.a.close();
            }
        }
    }
}
