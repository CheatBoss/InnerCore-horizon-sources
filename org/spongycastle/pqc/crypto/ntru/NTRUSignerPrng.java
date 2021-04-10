package org.spongycastle.pqc.crypto.ntru;

import org.spongycastle.crypto.*;
import java.nio.*;

public class NTRUSignerPrng
{
    private int counter;
    private Digest hashAlg;
    private byte[] seed;
    
    NTRUSignerPrng(final byte[] seed, final Digest hashAlg) {
        this.counter = 0;
        this.seed = seed;
        this.hashAlg = hashAlg;
    }
    
    byte[] nextBytes(int digestSize) {
        final ByteBuffer allocate = ByteBuffer.allocate(digestSize);
        while (allocate.hasRemaining()) {
            final ByteBuffer allocate2 = ByteBuffer.allocate(this.seed.length + 4);
            allocate2.put(this.seed);
            allocate2.putInt(this.counter);
            final byte[] array = allocate2.array();
            digestSize = this.hashAlg.getDigestSize();
            final byte[] array2 = new byte[digestSize];
            this.hashAlg.update(array, 0, array.length);
            this.hashAlg.doFinal(array2, 0);
            if (allocate.remaining() < digestSize) {
                allocate.put(array2, 0, allocate.remaining());
            }
            else {
                allocate.put(array2);
            }
            ++this.counter;
        }
        return allocate.array();
    }
}
