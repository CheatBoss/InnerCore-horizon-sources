package org.spongycastle.crypto.digests;

import org.spongycastle.crypto.*;
import org.spongycastle.util.*;

public abstract class GeneralDigest implements ExtendedDigest, Memoable
{
    private static final int BYTE_LENGTH = 64;
    private long byteCount;
    private final byte[] xBuf;
    private int xBufOff;
    
    protected GeneralDigest() {
        this.xBuf = new byte[4];
        this.xBufOff = 0;
    }
    
    protected GeneralDigest(final GeneralDigest generalDigest) {
        this.xBuf = new byte[4];
        this.copyIn(generalDigest);
    }
    
    protected GeneralDigest(final byte[] array) {
        final byte[] xBuf = new byte[4];
        System.arraycopy(array, 0, this.xBuf = xBuf, 0, xBuf.length);
        this.xBufOff = Pack.bigEndianToInt(array, 4);
        this.byteCount = Pack.bigEndianToLong(array, 8);
    }
    
    protected void copyIn(final GeneralDigest generalDigest) {
        final byte[] xBuf = generalDigest.xBuf;
        System.arraycopy(xBuf, 0, this.xBuf, 0, xBuf.length);
        this.xBufOff = generalDigest.xBufOff;
        this.byteCount = generalDigest.byteCount;
    }
    
    public void finish() {
        final long byteCount = this.byteCount;
        byte b = -128;
        while (true) {
            this.update(b);
            if (this.xBufOff == 0) {
                break;
            }
            b = 0;
        }
        this.processLength(byteCount << 3);
        this.processBlock();
    }
    
    @Override
    public int getByteLength() {
        return 64;
    }
    
    protected void populateState(final byte[] array) {
        System.arraycopy(this.xBuf, 0, array, 0, this.xBufOff);
        Pack.intToBigEndian(this.xBufOff, array, 4);
        Pack.longToBigEndian(this.byteCount, array, 8);
    }
    
    protected abstract void processBlock();
    
    protected abstract void processLength(final long p0);
    
    protected abstract void processWord(final byte[] p0, final int p1);
    
    @Override
    public void reset() {
        this.byteCount = 0L;
        this.xBufOff = 0;
        int n = 0;
        while (true) {
            final byte[] xBuf = this.xBuf;
            if (n >= xBuf.length) {
                break;
            }
            xBuf[n] = 0;
            ++n;
        }
    }
    
    @Override
    public void update(final byte b) {
        final byte[] xBuf = this.xBuf;
        final int xBufOff = this.xBufOff;
        final int xBufOff2 = xBufOff + 1;
        this.xBufOff = xBufOff2;
        xBuf[xBufOff] = b;
        if (xBufOff2 == xBuf.length) {
            this.processWord(xBuf, 0);
            this.xBufOff = 0;
        }
        ++this.byteCount;
    }
    
    @Override
    public void update(final byte[] array, final int n, int i) {
        final int n2 = 0;
        final int max = Math.max(0, i);
        i = n2;
        if (this.xBufOff != 0) {
            byte[] xBuf;
            int xBufOff;
            int xBufOff2;
            int n3;
            for (i = 0; i < max; i = n3) {
                xBuf = this.xBuf;
                xBufOff = this.xBufOff;
                xBufOff2 = xBufOff + 1;
                this.xBufOff = xBufOff2;
                n3 = i + 1;
                xBuf[xBufOff] = array[i + n];
                if (xBufOff2 == 4) {
                    this.processWord(xBuf, 0);
                    this.xBufOff = 0;
                    i = n3;
                    break;
                }
            }
        }
        int n4 = i;
        int j;
        while (true) {
            j = n4;
            if (n4 >= (max - i & 0xFFFFFFFC) + i) {
                break;
            }
            this.processWord(array, n + n4);
            n4 += 4;
        }
        while (j < max) {
            final byte[] xBuf2 = this.xBuf;
            i = this.xBufOff++;
            xBuf2[i] = array[j + n];
            ++j;
        }
        this.byteCount += max;
    }
}
