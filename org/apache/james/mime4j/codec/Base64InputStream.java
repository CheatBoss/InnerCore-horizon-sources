package org.apache.james.mime4j.codec;

import org.apache.commons.logging.*;
import java.io.*;

public class Base64InputStream extends InputStream
{
    private static final int[] BASE64_DECODE;
    private static final byte BASE64_PAD = 61;
    private static final int ENCODED_BUFFER_SIZE = 1536;
    private static final int EOF = -1;
    private static Log log;
    private boolean closed;
    private final byte[] encoded;
    private boolean eof;
    private final InputStream in;
    private int position;
    private final ByteQueue q;
    private final byte[] singleByte;
    private int size;
    private boolean strict;
    
    static {
        Base64InputStream.log = LogFactory.getLog((Class)Base64InputStream.class);
        BASE64_DECODE = new int[256];
        final int n = 0;
        int n2 = 0;
        int i;
        while (true) {
            i = n;
            if (n2 >= 256) {
                break;
            }
            Base64InputStream.BASE64_DECODE[n2] = -1;
            ++n2;
        }
        while (i < Base64OutputStream.BASE64_TABLE.length) {
            Base64InputStream.BASE64_DECODE[Base64OutputStream.BASE64_TABLE[i] & 0xFF] = i;
            ++i;
        }
    }
    
    public Base64InputStream(final InputStream inputStream) {
        this(inputStream, false);
    }
    
    public Base64InputStream(final InputStream in, final boolean strict) {
        this.singleByte = new byte[1];
        this.closed = false;
        this.encoded = new byte[1536];
        this.position = 0;
        this.size = 0;
        this.q = new ByteQueue();
        if (in != null) {
            this.in = in;
            this.strict = strict;
            return;
        }
        throw new IllegalArgumentException();
    }
    
    private int decodePad(int n, final int n2, final byte[] array, final int n3, final int n4) throws IOException {
        this.eof = true;
        if (n2 == 2) {
            final byte b = (byte)(n >>> 4);
            if (n3 < n4) {
                array[n3] = b;
                return n3 + 1;
            }
            this.q.enqueue(b);
            return n3;
        }
        else {
            if (n2 != 3) {
                this.handleUnexpecedPad(n2);
                return n3;
            }
            final byte b2 = (byte)(n >>> 10);
            final byte b3 = (byte)(n >>> 2 & 0xFF);
            if (n3 < n4 - 1) {
                n = n3 + 1;
                array[n3] = b2;
                array[n] = b3;
                return n + 1;
            }
            if (n3 < n4) {
                array[n3] = b2;
                this.q.enqueue(b3);
                return n3 + 1;
            }
            this.q.enqueue(b2);
            this.q.enqueue(b3);
            return n3;
        }
    }
    
    private void handleUnexpecedPad(final int n) throws IOException {
        if (!this.strict) {
            final Log log = Base64InputStream.log;
            final StringBuilder sb = new StringBuilder();
            sb.append("unexpected padding character; dropping ");
            sb.append(n);
            sb.append(" sextet(s)");
            log.warn((Object)sb.toString());
            return;
        }
        throw new IOException("unexpected padding character");
    }
    
    private void handleUnexpectedEof(final int n) throws IOException {
        if (!this.strict) {
            final Log log = Base64InputStream.log;
            final StringBuilder sb = new StringBuilder();
            sb.append("unexpected end of file; dropping ");
            sb.append(n);
            sb.append(" sextet(s)");
            log.warn((Object)sb.toString());
            return;
        }
        throw new IOException("unexpected end of file");
    }
    
    private int read0(final byte[] array, final int n, final int n2) throws IOException {
        int count;
        int i;
        for (count = this.q.count(), i = n; count > 0 && i < n2; ++i, --count) {
            array[i] = this.q.dequeue();
        }
        if (!this.eof) {
            int n3 = 0;
            int n4 = 0;
            while (i < n2) {
                while (this.position == this.size) {
                    final InputStream in = this.in;
                    final byte[] encoded = this.encoded;
                    final int read = in.read(encoded, 0, encoded.length);
                    if (read == -1) {
                        this.eof = true;
                        if (n3 != 0) {
                            this.handleUnexpectedEof(n3);
                        }
                        if (i == n) {
                            return -1;
                        }
                        return i - n;
                    }
                    else {
                        if (read <= 0) {
                            continue;
                        }
                        this.position = 0;
                        this.size = read;
                    }
                }
                final int n5 = n3;
                final int n6 = n4;
                int n7 = i;
                int n8 = n5;
                int n9 = n6;
                while (true) {
                    final int position = this.position;
                    if (position >= this.size || n7 >= n2) {
                        final int n10 = n9;
                        i = n7;
                        n3 = n8;
                        n4 = n10;
                        break;
                    }
                    final byte[] encoded2 = this.encoded;
                    this.position = position + 1;
                    final int n11 = encoded2[position] & 0xFF;
                    if (n11 == 61) {
                        return this.decodePad(n9, n8, array, n7, n2) - n;
                    }
                    final int n12 = Base64InputStream.BASE64_DECODE[n11];
                    if (n12 < 0) {
                        continue;
                    }
                    final int n13 = n9 << 6 | n12;
                    final int n14 = n8 + 1;
                    n9 = n13;
                    if ((n8 = n14) != 4) {
                        continue;
                    }
                    final byte b = (byte)(n13 >>> 16);
                    final byte b2 = (byte)(n13 >>> 8);
                    final byte b3 = (byte)n13;
                    if (n7 >= n2 - 2) {
                        if (n7 < n2 - 1) {
                            array[n7] = b;
                            array[n7 + 1] = b2;
                        }
                        else {
                            if (n7 < n2) {
                                array[n7] = b;
                            }
                            else {
                                this.q.enqueue(b);
                            }
                            this.q.enqueue(b2);
                        }
                        this.q.enqueue(b3);
                        return n2 - n;
                    }
                    final int n15 = n7 + 1;
                    array[n7] = b;
                    final int n16 = n15 + 1;
                    array[n15] = b2;
                    n7 = n16 + 1;
                    array[n16] = b3;
                    n8 = 0;
                    n9 = n13;
                }
            }
            return n2 - n;
        }
        if (i == n) {
            return -1;
        }
        return i - n;
    }
    
    @Override
    public void close() throws IOException {
        if (this.closed) {
            return;
        }
        this.closed = true;
    }
    
    @Override
    public int read() throws IOException {
        if (!this.closed) {
            int i;
            do {
                i = this.read0(this.singleByte, 0, 1);
                if (i == -1) {
                    return -1;
                }
            } while (i != 1);
            return this.singleByte[0] & 0xFF;
        }
        throw new IOException("Base64InputStream has been closed");
    }
    
    @Override
    public int read(final byte[] array) throws IOException {
        if (this.closed) {
            throw new IOException("Base64InputStream has been closed");
        }
        if (array == null) {
            throw null;
        }
        if (array.length == 0) {
            return 0;
        }
        return this.read0(array, 0, array.length);
    }
    
    @Override
    public int read(final byte[] array, final int n, final int n2) throws IOException {
        if (this.closed) {
            throw new IOException("Base64InputStream has been closed");
        }
        if (array != null) {
            if (n >= 0 && n2 >= 0) {
                final int n3 = n + n2;
                if (n3 <= array.length) {
                    if (n2 == 0) {
                        return 0;
                    }
                    return this.read0(array, n, n3);
                }
            }
            throw new IndexOutOfBoundsException();
        }
        throw null;
    }
}
