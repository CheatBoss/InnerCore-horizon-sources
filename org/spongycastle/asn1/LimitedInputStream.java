package org.spongycastle.asn1;

import java.io.*;

abstract class LimitedInputStream extends InputStream
{
    protected final InputStream _in;
    private int _limit;
    
    LimitedInputStream(final InputStream in, final int limit) {
        this._in = in;
        this._limit = limit;
    }
    
    int getRemaining() {
        return this._limit;
    }
    
    protected void setParentEofDetect(final boolean eofOn00) {
        final InputStream in = this._in;
        if (in instanceof IndefiniteLengthInputStream) {
            ((IndefiniteLengthInputStream)in).setEofOn00(eofOn00);
        }
    }
}
