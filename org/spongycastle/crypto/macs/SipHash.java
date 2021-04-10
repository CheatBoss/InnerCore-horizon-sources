package org.spongycastle.crypto.macs;

import org.spongycastle.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class SipHash implements Mac
{
    protected final int c;
    protected final int d;
    protected long k0;
    protected long k1;
    protected long m;
    protected long v0;
    protected long v1;
    protected long v2;
    protected long v3;
    protected int wordCount;
    protected int wordPos;
    
    public SipHash() {
        this.m = 0L;
        this.wordPos = 0;
        this.wordCount = 0;
        this.c = 2;
        this.d = 4;
    }
    
    public SipHash(final int c, final int d) {
        this.m = 0L;
        this.wordPos = 0;
        this.wordCount = 0;
        this.c = c;
        this.d = d;
    }
    
    protected static long rotateLeft(final long n, final int n2) {
        return n >>> -n2 | n << n2;
    }
    
    protected void applySipRounds(final int n) {
        long v0 = this.v0;
        long v2 = this.v1;
        long v3 = this.v2;
        long v4 = this.v3;
        long n6;
        long rotateLeft4;
        for (int i = 0; i < n; ++i, v2 = (rotateLeft4 ^ n6)) {
            final long n2 = v0 + v2;
            final long n3 = v3 + v4;
            final long rotateLeft = rotateLeft(v2, 13);
            final long rotateLeft2 = rotateLeft(v4, 16);
            final long n4 = rotateLeft ^ n2;
            final long n5 = rotateLeft2 ^ n3;
            final long rotateLeft3 = rotateLeft(n2, 32);
            n6 = n3 + n4;
            v0 = rotateLeft3 + n5;
            rotateLeft4 = rotateLeft(n4, 17);
            v4 = (rotateLeft(n5, 21) ^ v0);
            v3 = rotateLeft(n6, 32);
        }
        this.v0 = v0;
        this.v1 = v2;
        this.v2 = v3;
        this.v3 = v4;
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) throws DataLengthException, IllegalStateException {
        Pack.longToLittleEndian(this.doFinal(), array, n);
        return 8;
    }
    
    public long doFinal() throws DataLengthException, IllegalStateException {
        final long m = this.m;
        final int wordPos = this.wordPos;
        final long i = m >>> (7 - wordPos << 3);
        this.m = i;
        final long j = i >>> 8;
        this.m = j;
        this.m = (j | ((long)((this.wordCount << 3) + wordPos) & 0xFFL) << 56);
        this.processMessageWord();
        this.v2 ^= 0xFFL;
        this.applySipRounds(this.d);
        final long v0 = this.v0;
        final long v2 = this.v1;
        final long v3 = this.v2;
        final long v4 = this.v3;
        this.reset();
        return v0 ^ v2 ^ v3 ^ v4;
    }
    
    @Override
    public String getAlgorithmName() {
        final StringBuilder sb = new StringBuilder();
        sb.append("SipHash-");
        sb.append(this.c);
        sb.append("-");
        sb.append(this.d);
        return sb.toString();
    }
    
    @Override
    public int getMacSize() {
        return 8;
    }
    
    @Override
    public void init(final CipherParameters cipherParameters) throws IllegalArgumentException {
        if (!(cipherParameters instanceof KeyParameter)) {
            throw new IllegalArgumentException("'params' must be an instance of KeyParameter");
        }
        final byte[] key = ((KeyParameter)cipherParameters).getKey();
        if (key.length == 16) {
            this.k0 = Pack.littleEndianToLong(key, 0);
            this.k1 = Pack.littleEndianToLong(key, 8);
            this.reset();
            return;
        }
        throw new IllegalArgumentException("'params' must be a 128-bit key");
    }
    
    protected void processMessageWord() {
        ++this.wordCount;
        this.v3 ^= this.m;
        this.applySipRounds(this.c);
        this.v0 ^= this.m;
    }
    
    @Override
    public void reset() {
        final long k0 = this.k0;
        this.v0 = (k0 ^ 0x736F6D6570736575L);
        final long k2 = this.k1;
        this.v1 = (k2 ^ 0x646F72616E646F6DL);
        this.v2 = (k0 ^ 0x6C7967656E657261L);
        this.v3 = (k2 ^ 0x7465646279746573L);
        this.m = 0L;
        this.wordPos = 0;
        this.wordCount = 0;
    }
    
    @Override
    public void update(final byte b) throws IllegalStateException {
        final long m = this.m >>> 8;
        this.m = m;
        this.m = (m | ((long)b & 0xFFL) << 56);
        final int wordPos = this.wordPos + 1;
        this.wordPos = wordPos;
        if (wordPos == 8) {
            this.processMessageWord();
            this.wordPos = 0;
        }
    }
    
    @Override
    public void update(final byte[] array, final int n, final int n2) throws DataLengthException, IllegalStateException {
        final int n3 = n2 & 0xFFFFFFF8;
        final int wordPos = this.wordPos;
        if (wordPos == 0) {
            int n4 = 0;
            int i;
            while (true) {
                i = n4;
                if (n4 >= n3) {
                    break;
                }
                this.m = Pack.littleEndianToLong(array, n + n4);
                this.processMessageWord();
                n4 += 8;
            }
            while (i < n2) {
                final long m = this.m >>> 8;
                this.m = m;
                this.m = (m | ((long)array[n + i] & 0xFFL) << 56);
                ++i;
            }
            this.wordPos = n2 - n3;
            return;
        }
        final int n5 = wordPos << 3;
        int n6 = 0;
        int j;
        while (true) {
            j = n6;
            if (n6 >= n3) {
                break;
            }
            final long littleEndianToLong = Pack.littleEndianToLong(array, n + n6);
            this.m = (this.m >>> -n5 | littleEndianToLong << n5);
            this.processMessageWord();
            this.m = littleEndianToLong;
            n6 += 8;
        }
        while (j < n2) {
            final long k = this.m >>> 8;
            this.m = k;
            this.m = (k | ((long)array[n + j] & 0xFFL) << 56);
            if (++this.wordPos == 8) {
                this.processMessageWord();
                this.wordPos = 0;
            }
            ++j;
        }
    }
}
