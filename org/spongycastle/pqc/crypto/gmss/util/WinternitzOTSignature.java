package org.spongycastle.pqc.crypto.gmss.util;

import org.spongycastle.crypto.*;
import java.lang.reflect.*;

public class WinternitzOTSignature
{
    private int checksumsize;
    private GMSSRandom gmssRandom;
    private int keysize;
    private int mdsize;
    private Digest messDigestOTS;
    private int messagesize;
    private byte[][] privateKeyOTS;
    private int w;
    
    public WinternitzOTSignature(final byte[] array, final Digest messDigestOTS, int i) {
        this.w = i;
        this.messDigestOTS = messDigestOTS;
        this.gmssRandom = new GMSSRandom(this.messDigestOTS);
        final int digestSize = this.messDigestOTS.getDigestSize();
        this.mdsize = digestSize;
        final double n = digestSize << 3;
        final double n2 = i;
        Double.isNaN(n);
        Double.isNaN(n2);
        final int messagesize = (int)Math.ceil(n / n2);
        this.messagesize = messagesize;
        i = this.getLog((messagesize << i) + 1);
        this.checksumsize = i;
        final int messagesize2 = this.messagesize;
        final double n3 = i;
        Double.isNaN(n3);
        Double.isNaN(n2);
        final int keysize = messagesize2 + (int)Math.ceil(n3 / n2);
        this.keysize = keysize;
        final int mdsize = this.mdsize;
        final Class<Byte> type = Byte.TYPE;
        i = 0;
        this.privateKeyOTS = (byte[][])Array.newInstance(type, keysize, mdsize);
        final int mdsize2 = this.mdsize;
        final byte[] array2 = new byte[mdsize2];
        System.arraycopy(array, 0, array2, 0, mdsize2);
        while (i < this.keysize) {
            this.privateKeyOTS[i] = this.gmssRandom.nextSeed(array2);
            ++i;
        }
    }
    
    public int getLog(final int n) {
        int n2 = 1;
        for (int i = 2; i < n; i <<= 1, ++n2) {}
        return n2;
    }
    
    public byte[][] getPrivateKey() {
        return this.privateKeyOTS;
    }
    
    public byte[] getPublicKey() {
        final int keysize = this.keysize;
        final int mdsize = this.mdsize;
        final int n = keysize * mdsize;
        final byte[] array = new byte[n];
        final byte[] array2 = new byte[mdsize];
        final int w = this.w;
        for (int i = 0; i < this.keysize; ++i) {
            final Digest messDigestOTS = this.messDigestOTS;
            final byte[][] privateKeyOTS = this.privateKeyOTS;
            messDigestOTS.update(privateKeyOTS[i], 0, privateKeyOTS[i].length);
            byte[] array3 = new byte[this.messDigestOTS.getDigestSize()];
            this.messDigestOTS.doFinal(array3, 0);
            for (int j = 2; j < 1 << w; ++j) {
                this.messDigestOTS.update(array3, 0, array3.length);
                array3 = new byte[this.messDigestOTS.getDigestSize()];
                this.messDigestOTS.doFinal(array3, 0);
            }
            final int mdsize2 = this.mdsize;
            System.arraycopy(array3, 0, array, mdsize2 * i, mdsize2);
        }
        this.messDigestOTS.update(array, 0, n);
        final byte[] array4 = new byte[this.messDigestOTS.getDigestSize()];
        this.messDigestOTS.doFinal(array4, 0);
        return array4;
    }
    
    public byte[] getSignature(byte[] array) {
        final int keysize = this.keysize;
        final int mdsize = this.mdsize;
        final byte[] array2 = new byte[keysize * mdsize];
        final byte[] array3 = new byte[mdsize];
        this.messDigestOTS.update(array, 0, array.length);
        final int digestSize = this.messDigestOTS.getDigestSize();
        final byte[] array4 = new byte[digestSize];
        this.messDigestOTS.doFinal(array4, 0);
        final int w = this.w;
        if (8 % w == 0) {
            final int n = 8 / w;
            final int n2 = (1 << w) - 1;
            array = new byte[this.mdsize];
            int i = 0;
            int n3 = 0;
            int n4 = 0;
            while (i < digestSize) {
                int n6;
                for (int j = 0; j < n; ++j, n3 = n6) {
                    final int n5 = array4[i] & n2;
                    n6 = n3 + n5;
                    System.arraycopy(this.privateKeyOTS[n4], 0, array, 0, this.mdsize);
                    for (int k = n5; k > 0; --k) {
                        this.messDigestOTS.update(array, 0, array.length);
                        array = new byte[this.messDigestOTS.getDigestSize()];
                        this.messDigestOTS.doFinal(array, 0);
                    }
                    final int mdsize2 = this.mdsize;
                    System.arraycopy(array, 0, array2, n4 * mdsize2, mdsize2);
                    array4[i] >>>= (byte)this.w;
                    ++n4;
                }
                ++i;
            }
            int n7 = (this.messagesize << this.w) - n3;
            int w2;
            for (int l = 0; l < this.checksumsize; l += w2) {
                int n8 = n7 & n2;
                System.arraycopy(this.privateKeyOTS[n4], 0, array, 0, this.mdsize);
                while (n8 > 0) {
                    this.messDigestOTS.update(array, 0, array.length);
                    array = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(array, 0);
                    --n8;
                }
                final int mdsize3 = this.mdsize;
                System.arraycopy(array, 0, array2, n4 * mdsize3, mdsize3);
                w2 = this.w;
                n7 >>>= w2;
                ++n4;
            }
        }
        else if (w < 8) {
            final int mdsize4 = this.mdsize;
            final int n9 = mdsize4 / w;
            final int n10 = (1 << w) - 1;
            array = new byte[mdsize4];
            int n11 = 0;
            int n12 = 0;
            int n13 = 0;
            int n14 = 0;
            while (n11 < n9) {
                int n15;
                long n16;
                long n17;
                for (n15 = 0, n16 = 0L; n15 < this.w; ++n15, n16 ^= n17) {
                    n17 = (array4[n12] & 0xFF) << (n15 << 3);
                    ++n12;
                }
                final int n18 = 0;
                int n19 = n13;
                int n22;
                for (int n20 = n18; n20 < 8; ++n20, n19 = n22) {
                    final int n21 = (int)(n16 & (long)n10);
                    n22 = n19 + n21;
                    System.arraycopy(this.privateKeyOTS[n14], 0, array, 0, this.mdsize);
                    for (int n23 = n21; n23 > 0; --n23) {
                        this.messDigestOTS.update(array, 0, array.length);
                        array = new byte[this.messDigestOTS.getDigestSize()];
                        this.messDigestOTS.doFinal(array, 0);
                    }
                    final int mdsize5 = this.mdsize;
                    System.arraycopy(array, 0, array2, n14 * mdsize5, mdsize5);
                    n16 >>>= this.w;
                    ++n14;
                }
                ++n11;
                n13 = n19;
            }
            final int n24 = this.mdsize % this.w;
            final int n25 = 0;
            long n26 = 0L;
            int n27 = n12;
            long n29;
            for (int n28 = n25; n28 < n24; ++n28, n26 ^= n29) {
                n29 = (array4[n27] & 0xFF) << (n28 << 3);
                ++n27;
            }
            int n32;
            int w3;
            for (int n30 = 0; n30 < n24 << 3; n30 += w3, n13 = n32) {
                final int n31 = (int)((long)n10 & n26);
                n32 = n13 + n31;
                System.arraycopy(this.privateKeyOTS[n14], 0, array, 0, this.mdsize);
                for (int n33 = n31; n33 > 0; --n33) {
                    this.messDigestOTS.update(array, 0, array.length);
                    array = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(array, 0);
                }
                final int mdsize6 = this.mdsize;
                System.arraycopy(array, 0, array2, n14 * mdsize6, mdsize6);
                w3 = this.w;
                n26 >>>= w3;
                ++n14;
            }
            int n34 = (this.messagesize << this.w) - n13;
            int w4;
            for (int n35 = 0; n35 < this.checksumsize; n35 += w4) {
                int n36 = n34 & n10;
                System.arraycopy(this.privateKeyOTS[n14], 0, array, 0, this.mdsize);
                while (n36 > 0) {
                    this.messDigestOTS.update(array, 0, array.length);
                    array = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(array, 0);
                    --n36;
                }
                final int mdsize7 = this.mdsize;
                System.arraycopy(array, 0, array2, n14 * mdsize7, mdsize7);
                w4 = this.w;
                n34 >>>= w4;
                ++n14;
            }
        }
        else if (w < 57) {
            final int mdsize8 = this.mdsize;
            final int n37 = (1 << w) - 1;
            array = new byte[mdsize8];
            int n38 = 0;
            int n39 = 0;
            int n40 = 0;
            while (n38 <= (mdsize8 << 3) - w) {
                int n41 = n38 >>> 3;
                final int n42 = this.w + n38;
                long n43 = 0L;
                int n44 = 0;
                while (n41 < n42 + 7 >>> 3) {
                    final long n45 = (array4[n41] & 0xFF) << (n44 << 3);
                    ++n44;
                    ++n41;
                    n43 ^= n45;
                }
                long n46 = n43 >>> n38 % 8 & (long)n37;
                n39 += (int)n46;
                System.arraycopy(this.privateKeyOTS[n40], 0, array, 0, this.mdsize);
                while (n46 > 0L) {
                    this.messDigestOTS.update(array, 0, array.length);
                    array = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(array, 0);
                    --n46;
                }
                final int mdsize9 = this.mdsize;
                System.arraycopy(array, 0, array2, n40 * mdsize9, mdsize9);
                ++n40;
                n38 = n42;
            }
            final int n47 = n38 >>> 3;
            byte[] array5 = array;
            int n48 = n39;
            int n49 = n40;
            if (n47 < this.mdsize) {
                int n50 = 0;
                long n51 = 0L;
                int n52 = n47;
                int mdsize10;
                while (true) {
                    mdsize10 = this.mdsize;
                    if (n52 >= mdsize10) {
                        break;
                    }
                    final long n53 = (array4[n52] & 0xFF) << (n50 << 3);
                    ++n50;
                    ++n52;
                    n51 ^= n53;
                }
                long n54 = n51 >>> n38 % 8 & (long)n37;
                n48 = (int)(n39 + n54);
                System.arraycopy(this.privateKeyOTS[n40], 0, array, 0, mdsize10);
                while (n54 > 0L) {
                    this.messDigestOTS.update(array, 0, array.length);
                    array = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(array, 0);
                    --n54;
                }
                final int mdsize11 = this.mdsize;
                System.arraycopy(array, 0, array2, n40 * mdsize11, mdsize11);
                n49 = n40 + 1;
                array5 = array;
            }
            int n55 = (this.messagesize << this.w) - n48;
            int w5;
            for (int n56 = 0; n56 < this.checksumsize; n56 += w5) {
                long n57 = n55 & n37;
                System.arraycopy(this.privateKeyOTS[n49], 0, array5, 0, this.mdsize);
                while (n57 > 0L) {
                    this.messDigestOTS.update(array5, 0, array5.length);
                    array5 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(array5, 0);
                    --n57;
                }
                final int mdsize12 = this.mdsize;
                System.arraycopy(array5, 0, array2, n49 * mdsize12, mdsize12);
                w5 = this.w;
                n55 >>>= w5;
                ++n49;
            }
        }
        return array2;
    }
}
