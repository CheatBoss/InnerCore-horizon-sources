package org.spongycastle.pqc.crypto.ntru;

import org.spongycastle.crypto.*;
import org.spongycastle.util.*;

public class IndexGenerator
{
    private int N;
    private BitString buf;
    private int c;
    private int counter;
    private int hLen;
    private Digest hashAlg;
    private boolean initialized;
    private int minCallsR;
    private int remLen;
    private byte[] seed;
    private int totLen;
    
    IndexGenerator(final byte[] seed, final NTRUEncryptionParameters ntruEncryptionParameters) {
        this.seed = seed;
        this.N = ntruEncryptionParameters.N;
        this.c = ntruEncryptionParameters.c;
        this.minCallsR = ntruEncryptionParameters.minCallsR;
        this.totLen = 0;
        this.remLen = 0;
        this.counter = 0;
        final Digest hashAlg = ntruEncryptionParameters.hashAlg;
        this.hashAlg = hashAlg;
        this.hLen = hashAlg.getDigestSize();
        this.initialized = false;
    }
    
    private void appendHash(final BitString bitString, final byte[] array) {
        final Digest hashAlg = this.hashAlg;
        final byte[] seed = this.seed;
        hashAlg.update(seed, 0, seed.length);
        this.putInt(this.hashAlg, this.counter);
        this.hashAlg.doFinal(array, 0);
        bitString.appendBits(array);
    }
    
    private static byte[] copyOf(final byte[] array, int length) {
        final byte[] array2 = new byte[length];
        if (length >= array.length) {
            length = array.length;
        }
        System.arraycopy(array, 0, array2, 0, length);
        return array2;
    }
    
    private void putInt(final Digest digest, final int n) {
        digest.update((byte)(n >> 24));
        digest.update((byte)(n >> 16));
        digest.update((byte)(n >> 8));
        digest.update((byte)n);
    }
    
    int nextIndex() {
        if (!this.initialized) {
            this.buf = new BitString();
            final byte[] array = new byte[this.hashAlg.getDigestSize()];
            int minCallsR;
            while (true) {
                final int counter = this.counter;
                minCallsR = this.minCallsR;
                if (counter >= minCallsR) {
                    break;
                }
                this.appendHash(this.buf, array);
                ++this.counter;
            }
            final int n = minCallsR * 8 * this.hLen;
            this.totLen = n;
            this.remLen = n;
            this.initialized = true;
        }
        int i;
        int n2;
        int n3;
        do {
            this.totLen += this.c;
            final BitString trailing = this.buf.getTrailing(this.remLen);
            final int remLen = this.remLen;
            final int c = this.c;
            if (remLen < c) {
                int n4 = c - remLen;
                final int counter2 = this.counter;
                final int hLen = this.hLen;
                final int n5 = (n4 + hLen - 1) / hLen;
                final byte[] array2 = new byte[this.hashAlg.getDigestSize()];
                while (this.counter < counter2 + n5) {
                    this.appendHash(trailing, array2);
                    ++this.counter;
                    final int n6 = this.hLen * 8;
                    if (n4 > n6) {
                        n4 -= n6;
                    }
                }
                this.remLen = this.hLen * 8 - n4;
                (this.buf = new BitString()).appendBits(array2);
            }
            else {
                this.remLen = remLen - c;
            }
            i = trailing.getLeadingAsInt(this.c);
            final int c2 = this.c;
            n3 = this.N;
            n2 = 1 << c2;
        } while (i >= n2 - n2 % n3);
        return i % n3;
    }
    
    public static class BitString
    {
        byte[] bytes;
        int lastByteBits;
        int numBytes;
        
        public BitString() {
            this.bytes = new byte[4];
        }
        
        public void appendBits(final byte b) {
            final int numBytes = this.numBytes;
            final byte[] bytes = this.bytes;
            if (numBytes == bytes.length) {
                this.bytes = copyOf(bytes, bytes.length * 2);
            }
            final int numBytes2 = this.numBytes;
            if (numBytes2 == 0) {
                this.numBytes = 1;
                this.bytes[0] = b;
                this.lastByteBits = 8;
                return;
            }
            final int lastByteBits = this.lastByteBits;
            if (lastByteBits == 8) {
                final byte[] bytes2 = this.bytes;
                this.numBytes = numBytes2 + 1;
                bytes2[numBytes2] = b;
                return;
            }
            final byte[] bytes3 = this.bytes;
            final int n = numBytes2 - 1;
            final byte b2 = bytes3[n];
            final int n2 = b & 0xFF;
            bytes3[n] = (byte)(b2 | n2 << lastByteBits);
            this.numBytes = numBytes2 + 1;
            bytes3[numBytes2] = (byte)(n2 >> 8 - lastByteBits);
        }
        
        void appendBits(final byte[] array) {
            for (int i = 0; i != array.length; ++i) {
                this.appendBits(array[i]);
            }
        }
        
        public byte[] getBytes() {
            return Arrays.clone(this.bytes);
        }
        
        public int getLeadingAsInt(int n) {
            n = (this.numBytes - 1) * 8 + this.lastByteBits - n;
            int n2 = n / 8;
            n %= 8;
            int n3 = (this.bytes[n2] & 0xFF) >>> n;
            n = 8 - n;
            while (true) {
                ++n2;
                if (n2 >= this.numBytes) {
                    break;
                }
                n3 |= (this.bytes[n2] & 0xFF) << n;
                n += 8;
            }
            return n3;
        }
        
        public BitString getTrailing(int lastByteBits) {
            final BitString bitString = new BitString();
            final int numBytes = (lastByteBits + 7) / 8;
            bitString.numBytes = numBytes;
            bitString.bytes = new byte[numBytes];
            int n = 0;
            int numBytes2;
            while (true) {
                numBytes2 = bitString.numBytes;
                if (n >= numBytes2) {
                    break;
                }
                bitString.bytes[n] = this.bytes[n];
                ++n;
            }
            lastByteBits %= 8;
            if ((bitString.lastByteBits = lastByteBits) == 0) {
                bitString.lastByteBits = 8;
                return bitString;
            }
            lastByteBits = 32 - lastByteBits;
            final byte[] bytes = bitString.bytes;
            final int n2 = numBytes2 - 1;
            bytes[n2] = (byte)(bytes[n2] << lastByteBits >>> lastByteBits);
            return bitString;
        }
    }
}
