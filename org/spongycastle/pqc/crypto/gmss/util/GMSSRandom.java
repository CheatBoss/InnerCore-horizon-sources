package org.spongycastle.pqc.crypto.gmss.util;

import org.spongycastle.crypto.*;

public class GMSSRandom
{
    private Digest messDigestTree;
    
    public GMSSRandom(final Digest messDigestTree) {
        this.messDigestTree = messDigestTree;
    }
    
    private void addByteArrays(final byte[] array, final byte[] array2) {
        int i = 0;
        int n = 0;
        while (i < array.length) {
            final int n2 = (array[i] & 0xFF) + (array2[i] & 0xFF) + n;
            array[i] = (byte)n2;
            n = (byte)(n2 >> 8);
            ++i;
        }
    }
    
    private void addOne(final byte[] array) {
        int n = 1;
        for (int i = 0; i < array.length; ++i) {
            final int n2 = (array[i] & 0xFF) + n;
            array[i] = (byte)n2;
            n = (byte)(n2 >> 8);
        }
    }
    
    public byte[] nextSeed(final byte[] array) {
        final byte[] array2 = new byte[array.length];
        this.messDigestTree.update(array, 0, array.length);
        final byte[] array3 = new byte[this.messDigestTree.getDigestSize()];
        this.messDigestTree.doFinal(array3, 0);
        this.addByteArrays(array, array3);
        this.addOne(array);
        return array3;
    }
}
