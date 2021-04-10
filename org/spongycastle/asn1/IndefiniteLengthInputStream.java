package org.spongycastle.asn1;

import java.io.*;

class IndefiniteLengthInputStream extends LimitedInputStream
{
    private int _b1;
    private int _b2;
    private boolean _eofOn00;
    private boolean _eofReached;
    
    IndefiniteLengthInputStream(final InputStream inputStream, int read) throws IOException {
        super(inputStream, read);
        this._eofReached = false;
        this._eofOn00 = true;
        this._b1 = inputStream.read();
        read = inputStream.read();
        this._b2 = read;
        if (read >= 0) {
            this.checkForEof();
            return;
        }
        throw new EOFException();
    }
    
    private boolean checkForEof() {
        if (!this._eofReached && this._eofOn00 && this._b1 == 0 && this._b2 == 0) {
            this.setParentEofDetect(this._eofReached = true);
        }
        return this._eofReached;
    }
    
    @Override
    public int read() throws IOException {
        if (this.checkForEof()) {
            return -1;
        }
        final int read = this._in.read();
        if (read >= 0) {
            final int b1 = this._b1;
            this._b1 = this._b2;
            this._b2 = read;
            return b1;
        }
        throw new EOFException();
    }
    
    @Override
    public int read(final byte[] array, int read, int read2) throws IOException {
        if (this._eofOn00 || read2 < 3) {
            return super.read(array, read, read2);
        }
        if (this._eofReached) {
            return -1;
        }
        read2 = this._in.read(array, read + 2, read2 - 2);
        if (read2 < 0) {
            throw new EOFException();
        }
        array[read] = (byte)this._b1;
        array[read + 1] = (byte)this._b2;
        this._b1 = this._in.read();
        read = this._in.read();
        if ((this._b2 = read) >= 0) {
            return read2 + 2;
        }
        throw new EOFException();
    }
    
    void setEofOn00(final boolean eofOn00) {
        this._eofOn00 = eofOn00;
        this.checkForEof();
    }
}
