package org.spongycastle.asn1;

import java.io.*;
import org.spongycastle.util.io.*;

class DefiniteLengthInputStream extends LimitedInputStream
{
    private static final byte[] EMPTY_BYTES;
    private final int _originalLength;
    private int _remaining;
    
    static {
        EMPTY_BYTES = new byte[0];
    }
    
    DefiniteLengthInputStream(final InputStream inputStream, final int n) {
        super(inputStream, n);
        if (n >= 0) {
            this._originalLength = n;
            if ((this._remaining = n) == 0) {
                this.setParentEofDetect(true);
            }
            return;
        }
        throw new IllegalArgumentException("negative lengths not allowed");
    }
    
    @Override
    int getRemaining() {
        return this._remaining;
    }
    
    @Override
    public int read() throws IOException {
        if (this._remaining == 0) {
            return -1;
        }
        final int read = this._in.read();
        if (read >= 0) {
            if (--this._remaining == 0) {
                this.setParentEofDetect(true);
            }
            return read;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("DEF length ");
        sb.append(this._originalLength);
        sb.append(" object truncated by ");
        sb.append(this._remaining);
        throw new EOFException(sb.toString());
    }
    
    @Override
    public int read(final byte[] array, int read, int min) throws IOException {
        final int remaining = this._remaining;
        if (remaining == 0) {
            return -1;
        }
        min = Math.min(min, remaining);
        read = this._in.read(array, read, min);
        if (read >= 0) {
            min = this._remaining - read;
            if ((this._remaining = min) == 0) {
                this.setParentEofDetect(true);
            }
            return read;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("DEF length ");
        sb.append(this._originalLength);
        sb.append(" object truncated by ");
        sb.append(this._remaining);
        throw new EOFException(sb.toString());
    }
    
    byte[] toByteArray() throws IOException {
        final int remaining = this._remaining;
        if (remaining == 0) {
            return DefiniteLengthInputStream.EMPTY_BYTES;
        }
        final byte[] array = new byte[remaining];
        if ((this._remaining = remaining - Streams.readFully(this._in, array)) == 0) {
            this.setParentEofDetect(true);
            return array;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("DEF length ");
        sb.append(this._originalLength);
        sb.append(" object truncated by ");
        sb.append(this._remaining);
        throw new EOFException(sb.toString());
    }
}
