package org.spongycastle.pqc.crypto.xmss;

import org.spongycastle.crypto.*;

final class KeyedHashFunctions
{
    private final Digest digest;
    private final int digestSize;
    
    protected KeyedHashFunctions(final Digest digest, final int digestSize) {
        if (digest != null) {
            this.digest = digest;
            this.digestSize = digestSize;
            return;
        }
        throw new NullPointerException("digest == null");
    }
    
    private byte[] coreDigest(int digestSize, byte[] array, final byte[] array2) {
        final byte[] bytesBigEndian = XMSSUtil.toBytesBigEndian(digestSize, this.digestSize);
        this.digest.update(bytesBigEndian, 0, bytesBigEndian.length);
        this.digest.update(array, 0, array.length);
        this.digest.update(array2, 0, array2.length);
        digestSize = this.digestSize;
        array = new byte[digestSize];
        final Digest digest = this.digest;
        if (digest instanceof Xof) {
            ((Xof)digest).doFinal(array, 0, digestSize);
            return array;
        }
        digest.doFinal(array, 0);
        return array;
    }
    
    protected byte[] F(final byte[] array, final byte[] array2) {
        final int length = array.length;
        final int digestSize = this.digestSize;
        if (length != digestSize) {
            throw new IllegalArgumentException("wrong key length");
        }
        if (array2.length == digestSize) {
            return this.coreDigest(0, array, array2);
        }
        throw new IllegalArgumentException("wrong in length");
    }
    
    protected byte[] H(final byte[] array, final byte[] array2) {
        final int length = array.length;
        final int digestSize = this.digestSize;
        if (length != digestSize) {
            throw new IllegalArgumentException("wrong key length");
        }
        if (array2.length == digestSize * 2) {
            return this.coreDigest(1, array, array2);
        }
        throw new IllegalArgumentException("wrong in length");
    }
    
    protected byte[] HMsg(final byte[] array, final byte[] array2) {
        if (array.length == this.digestSize * 3) {
            return this.coreDigest(2, array, array2);
        }
        throw new IllegalArgumentException("wrong key length");
    }
    
    protected byte[] PRF(final byte[] array, final byte[] array2) {
        if (array.length != this.digestSize) {
            throw new IllegalArgumentException("wrong key length");
        }
        if (array2.length == 32) {
            return this.coreDigest(3, array, array2);
        }
        throw new IllegalArgumentException("wrong address length");
    }
}
