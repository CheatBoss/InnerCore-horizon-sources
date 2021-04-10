package com.bumptech.glide.load.resource.bitmap;

import android.util.*;
import java.io.*;

public class RecyclableBufferedInputStream extends FilterInputStream
{
    private static final String TAG = "BufferedIs";
    private volatile byte[] buf;
    private int count;
    private int marklimit;
    private int markpos;
    private int pos;
    
    public RecyclableBufferedInputStream(final InputStream inputStream, final byte[] buf) {
        super(inputStream);
        this.markpos = -1;
        if (buf != null && buf.length != 0) {
            this.buf = buf;
            return;
        }
        throw new IllegalArgumentException("buffer is null or empty");
    }
    
    private int fillbuf(final InputStream inputStream, final byte[] array) throws IOException {
        if (this.markpos != -1 && this.pos - this.markpos < this.marklimit) {
            byte[] buf;
            if (this.markpos == 0 && this.marklimit > array.length && this.count == array.length) {
                int marklimit;
                if ((marklimit = array.length * 2) > this.marklimit) {
                    marklimit = this.marklimit;
                }
                if (Log.isLoggable("BufferedIs", 3)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("allocate buffer of length: ");
                    sb.append(marklimit);
                    Log.d("BufferedIs", sb.toString());
                }
                buf = new byte[marklimit];
                System.arraycopy(array, 0, buf, 0, array.length);
                this.buf = buf;
            }
            else {
                buf = array;
                if (this.markpos > 0) {
                    System.arraycopy(array, this.markpos, array, 0, array.length - this.markpos);
                    buf = array;
                }
            }
            this.pos -= this.markpos;
            this.markpos = 0;
            this.count = 0;
            final int read = inputStream.read(buf, this.pos, buf.length - this.pos);
            int pos;
            if (read <= 0) {
                pos = this.pos;
            }
            else {
                pos = this.pos + read;
            }
            this.count = pos;
            return read;
        }
        final int read2 = inputStream.read(array);
        if (read2 > 0) {
            this.markpos = -1;
            this.pos = 0;
            this.count = read2;
        }
        return read2;
    }
    
    private static IOException streamClosed() throws IOException {
        throw new IOException("BufferedInputStream is closed");
    }
    
    @Override
    public int available() throws IOException {
        synchronized (this) {
            final InputStream in = this.in;
            if (this.buf != null && in != null) {
                final int count = this.count;
                final int pos = this.pos;
                final int available = in.available();
                // monitorexit(this)
                return count - pos + available;
            }
            throw streamClosed();
        }
    }
    
    @Override
    public void close() throws IOException {
        this.buf = null;
        final InputStream in = this.in;
        this.in = null;
        if (in != null) {
            in.close();
        }
    }
    
    public void fixMarkLimit() {
        synchronized (this) {
            this.marklimit = this.buf.length;
        }
    }
    
    @Override
    public void mark(final int n) {
        synchronized (this) {
            this.marklimit = Math.max(this.marklimit, n);
            this.markpos = this.pos;
        }
    }
    
    @Override
    public boolean markSupported() {
        return true;
    }
    
    @Override
    public int read() throws IOException {
        synchronized (this) {
            final byte[] buf = this.buf;
            final InputStream in = this.in;
            if (buf == null || in == null) {
                throw streamClosed();
            }
            if (this.pos >= this.count && this.fillbuf(in, buf) == -1) {
                return -1;
            }
            byte[] buf2;
            if ((buf2 = buf) != this.buf && (buf2 = this.buf) == null) {
                throw streamClosed();
            }
            if (this.count - this.pos > 0) {
                final byte b = buf2[this.pos++];
                // monitorexit(this)
                return b & 0xFF;
            }
            return -1;
        }
    }
    
    @Override
    public int read(final byte[] array, int n, final int n2) throws IOException {
        synchronized (this) {
            byte[] buf = this.buf;
            if (buf == null) {
                throw streamClosed();
            }
            if (n2 == 0) {
                return 0;
            }
            final InputStream in = this.in;
            if (in == null) {
                throw streamClosed();
            }
            int n5;
            if (this.pos < this.count) {
                int n3;
                if (this.count - this.pos >= n2) {
                    n3 = n2;
                }
                else {
                    n3 = this.count - this.pos;
                }
                System.arraycopy(buf, this.pos, array, n, n3);
                this.pos += n3;
                if (n3 == n2 || in.available() == 0) {
                    return n3;
                }
                final int n4 = n + n3;
                n = n2 - n3;
                n5 = n4;
            }
            else {
                n5 = n;
                n = n2;
            }
            while (true) {
                final int markpos = this.markpos;
                int n6 = -1;
                byte[] buf2;
                int n7;
                if (markpos == -1 && n >= buf.length) {
                    final int read = in.read(array, n5, n);
                    buf2 = buf;
                    if ((n7 = read) == -1) {
                        if (n != n2) {
                            n6 = n2 - n;
                        }
                        return n6;
                    }
                }
                else {
                    if (this.fillbuf(in, buf) == -1) {
                        if (n != n2) {
                            n6 = n2 - n;
                        }
                        return n6;
                    }
                    if ((buf2 = buf) != this.buf && (buf2 = this.buf) == null) {
                        throw streamClosed();
                    }
                    int n8;
                    if (this.count - this.pos >= n) {
                        n8 = n;
                    }
                    else {
                        n8 = this.count - this.pos;
                    }
                    System.arraycopy(buf2, this.pos, array, n5, n8);
                    this.pos += n8;
                    n7 = n8;
                }
                n -= n7;
                if (n == 0) {
                    return n2;
                }
                if (in.available() == 0) {
                    // monitorexit(this)
                    return n2 - n;
                }
                n5 += n7;
                buf = buf2;
            }
        }
    }
    
    @Override
    public void reset() throws IOException {
        synchronized (this) {
            if (this.buf == null) {
                throw new IOException("Stream is closed");
            }
            if (-1 == this.markpos) {
                throw new InvalidMarkException("Mark has been invalidated");
            }
            this.pos = this.markpos;
        }
    }
    
    @Override
    public long skip(long skip) throws IOException {
        synchronized (this) {
            final byte[] buf = this.buf;
            final InputStream in = this.in;
            if (buf == null) {
                throw streamClosed();
            }
            if (skip < 1L) {
                return 0L;
            }
            if (in == null) {
                throw streamClosed();
            }
            if (this.count - this.pos >= skip) {
                this.pos += (int)skip;
                return skip;
            }
            final long n = this.count - this.pos;
            this.pos = this.count;
            if (this.markpos == -1 || skip > this.marklimit) {
                skip = in.skip(skip - n);
                return n + skip;
            }
            if (this.fillbuf(in, buf) == -1) {
                return n;
            }
            if (this.count - this.pos >= skip - n) {
                this.pos += (int)(skip - n);
                return skip;
            }
            skip = this.count;
            final long n2 = this.pos;
            this.pos = this.count;
            // monitorexit(this)
            return n + skip - n2;
        }
    }
    
    public static class InvalidMarkException extends RuntimeException
    {
        private static final long serialVersionUID = -4338378848813561757L;
        
        public InvalidMarkException(final String s) {
            super(s);
        }
    }
}
