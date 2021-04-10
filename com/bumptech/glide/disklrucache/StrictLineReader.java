package com.bumptech.glide.disklrucache;

import java.nio.charset.*;
import java.io.*;

class StrictLineReader implements Closeable
{
    private static final byte CR = 13;
    private static final byte LF = 10;
    private byte[] buf;
    private final Charset charset;
    private int end;
    private final InputStream in;
    private int pos;
    
    public StrictLineReader(final InputStream in, final int n, final Charset charset) {
        if (in == null || charset == null) {
            throw new NullPointerException();
        }
        if (n < 0) {
            throw new IllegalArgumentException("capacity <= 0");
        }
        if (!charset.equals(Util.US_ASCII)) {
            throw new IllegalArgumentException("Unsupported encoding");
        }
        this.in = in;
        this.charset = charset;
        this.buf = new byte[n];
    }
    
    public StrictLineReader(final InputStream inputStream, final Charset charset) {
        this(inputStream, 8192, charset);
    }
    
    private void fillBuf() throws IOException {
        final int read = this.in.read(this.buf, 0, this.buf.length);
        if (read == -1) {
            throw new EOFException();
        }
        this.pos = 0;
        this.end = read;
    }
    
    @Override
    public void close() throws IOException {
        synchronized (this.in) {
            if (this.buf != null) {
                this.buf = null;
                this.in.close();
            }
        }
    }
    
    public boolean hasUnterminatedLine() {
        return this.end == -1;
    }
    
    public String readLine() throws IOException {
    Label_0191_Outer:
        while (true) {
            while (true) {
                int i = 0;
            Label_0275:
                while (true) {
                    Label_0268: {
                        while (true) {
                            synchronized (this.in) {
                                if (this.buf == null) {
                                    throw new IOException("LineReader is closed");
                                }
                                if (this.pos >= this.end) {
                                    this.fillBuf();
                                }
                                i = this.pos;
                                if (i != this.end) {
                                    if (this.buf[i] != 10) {
                                        break Label_0268;
                                    }
                                    if (i != this.pos && this.buf[i - 1] == 13) {
                                        final int n = i - 1;
                                        final String s = new String(this.buf, this.pos, n - this.pos, this.charset.name());
                                        this.pos = i + 1;
                                        return s;
                                    }
                                }
                                else {
                                    final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(this.end - this.pos + 80) {
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
                                                return new String(this.buf, 0, count, StrictLineReader.this.charset.name());
                                            }
                                            catch (UnsupportedEncodingException ex) {
                                                throw new AssertionError((Object)ex);
                                            }
                                        }
                                    };
                                    do {
                                        byteArrayOutputStream.write(this.buf, this.pos, this.end - this.pos);
                                        this.end = -1;
                                        this.fillBuf();
                                        i = this.pos;
                                    } while (i == this.end);
                                    if (this.buf[i] == 10) {
                                        if (i != this.pos) {
                                            byteArrayOutputStream.write(this.buf, this.pos, i - this.pos);
                                        }
                                        this.pos = i + 1;
                                        return byteArrayOutputStream.toString();
                                    }
                                    break Label_0275;
                                }
                            }
                            final int n = i;
                            continue Label_0191_Outer;
                        }
                    }
                    ++i;
                    continue Label_0191_Outer;
                }
                ++i;
                continue;
            }
        }
    }
}
