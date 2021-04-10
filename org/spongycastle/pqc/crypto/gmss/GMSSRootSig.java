package org.spongycastle.pqc.crypto.gmss;

import org.spongycastle.pqc.crypto.gmss.util.*;
import org.spongycastle.crypto.*;
import java.lang.reflect.*;
import org.spongycastle.util.encoders.*;

public class GMSSRootSig
{
    private long big8;
    private int checksum;
    private int counter;
    private GMSSRandom gmssRandom;
    private byte[] hash;
    private int height;
    private int ii;
    private int k;
    private int keysize;
    private int mdsize;
    private Digest messDigestOTS;
    private int messagesize;
    private byte[] privateKeyOTS;
    private int r;
    private byte[] seed;
    private byte[] sign;
    private int steps;
    private int test;
    private long test8;
    private int w;
    
    public GMSSRootSig(final Digest messDigestOTS, final int w, final int height) {
        this.messDigestOTS = messDigestOTS;
        this.gmssRandom = new GMSSRandom(this.messDigestOTS);
        final int digestSize = this.messDigestOTS.getDigestSize();
        this.mdsize = digestSize;
        this.w = w;
        this.height = height;
        this.k = (1 << w) - 1;
        final double n = digestSize << 3;
        final double n2 = w;
        Double.isNaN(n);
        Double.isNaN(n2);
        this.messagesize = (int)Math.ceil(n / n2);
    }
    
    public GMSSRootSig(final Digest messDigestOTS, final byte[][] array, final int[] array2) {
        this.messDigestOTS = messDigestOTS;
        this.gmssRandom = new GMSSRandom(this.messDigestOTS);
        this.counter = array2[0];
        this.test = array2[1];
        this.ii = array2[2];
        this.r = array2[3];
        this.steps = array2[4];
        this.keysize = array2[5];
        this.height = array2[6];
        this.w = array2[7];
        this.checksum = array2[8];
        final int digestSize = this.messDigestOTS.getDigestSize();
        this.mdsize = digestSize;
        final int w = this.w;
        this.k = (1 << w) - 1;
        final double n = digestSize << 3;
        final double n2 = w;
        Double.isNaN(n);
        Double.isNaN(n2);
        this.messagesize = (int)Math.ceil(n / n2);
        this.privateKeyOTS = array[0];
        this.seed = array[1];
        this.hash = array[2];
        this.sign = array[3];
        this.test8 = ((long)(array[4][1] & 0xFF) << 8 | (long)(array[4][0] & 0xFF) | (long)(array[4][2] & 0xFF) << 16 | (long)(array[4][3] & 0xFF) << 24 | (long)(array[4][4] & 0xFF) << 32 | (long)(array[4][5] & 0xFF) << 40 | (long)(array[4][6] & 0xFF) << 48 | (long)(array[4][7] & 0xFF) << 56);
        this.big8 = ((long)(array[4][8] & 0xFF) | (long)(array[4][9] & 0xFF) << 8 | (long)(array[4][10] & 0xFF) << 16 | (long)(array[4][11] & 0xFF) << 24 | (long)(array[4][12] & 0xFF) << 32 | (long)(array[4][13] & 0xFF) << 40 | (long)(array[4][14] & 0xFF) << 48 | (long)(array[4][15] & 0xFF) << 56);
    }
    
    private void oneStep() {
        final int w = this.w;
        if (8 % w == 0) {
            final int test = this.test;
            if (test == 0) {
                this.privateKeyOTS = this.gmssRandom.nextSeed(this.seed);
                final int ii = this.ii;
                if (ii < this.mdsize) {
                    final byte[] hash = this.hash;
                    this.test = (hash[ii] & this.k);
                    hash[ii] >>>= (byte)this.w;
                }
                else {
                    final int checksum = this.checksum;
                    this.test = (this.k & checksum);
                    this.checksum = checksum >>> this.w;
                }
            }
            else if (test > 0) {
                final Digest messDigestOTS = this.messDigestOTS;
                final byte[] privateKeyOTS = this.privateKeyOTS;
                messDigestOTS.update(privateKeyOTS, 0, privateKeyOTS.length);
                final byte[] privateKeyOTS2 = new byte[this.messDigestOTS.getDigestSize()];
                this.privateKeyOTS = privateKeyOTS2;
                this.messDigestOTS.doFinal(privateKeyOTS2, 0);
                --this.test;
            }
            if (this.test == 0) {
                final byte[] privateKeyOTS3 = this.privateKeyOTS;
                final byte[] sign = this.sign;
                final int counter = this.counter;
                final int mdsize = this.mdsize;
                System.arraycopy(privateKeyOTS3, 0, sign, counter * mdsize, mdsize);
                final int counter2 = this.counter + 1;
                this.counter = counter2;
                if (counter2 % (8 / this.w) == 0) {
                    ++this.ii;
                }
            }
        }
        else if (w < 8) {
            final int test2 = this.test;
            if (test2 == 0) {
                final int counter3 = this.counter;
                if (counter3 % 8 == 0) {
                    final int ii2 = this.ii;
                    final int mdsize2 = this.mdsize;
                    if (ii2 < mdsize2) {
                        this.big8 = 0L;
                        if (counter3 < mdsize2 / w << 3) {
                            for (int i = 0; i < this.w; ++i) {
                                final long big8 = this.big8;
                                final byte[] hash2 = this.hash;
                                final int ii3 = this.ii;
                                this.big8 = (big8 ^ (long)((hash2[ii3] & 0xFF) << (i << 3)));
                                this.ii = ii3 + 1;
                            }
                        }
                        else {
                            for (int j = 0; j < this.mdsize % this.w; ++j) {
                                final long big9 = this.big8;
                                final byte[] hash3 = this.hash;
                                final int ii4 = this.ii;
                                this.big8 = (big9 ^ (long)((hash3[ii4] & 0xFF) << (j << 3)));
                                this.ii = ii4 + 1;
                            }
                        }
                    }
                }
                if (this.counter == this.messagesize) {
                    this.big8 = this.checksum;
                }
                this.test = (int)(this.big8 & (long)this.k);
                this.privateKeyOTS = this.gmssRandom.nextSeed(this.seed);
            }
            else if (test2 > 0) {
                final Digest messDigestOTS2 = this.messDigestOTS;
                final byte[] privateKeyOTS4 = this.privateKeyOTS;
                messDigestOTS2.update(privateKeyOTS4, 0, privateKeyOTS4.length);
                final byte[] privateKeyOTS5 = new byte[this.messDigestOTS.getDigestSize()];
                this.privateKeyOTS = privateKeyOTS5;
                this.messDigestOTS.doFinal(privateKeyOTS5, 0);
                --this.test;
            }
            if (this.test == 0) {
                final byte[] privateKeyOTS6 = this.privateKeyOTS;
                final byte[] sign2 = this.sign;
                final int counter4 = this.counter;
                final int mdsize3 = this.mdsize;
                System.arraycopy(privateKeyOTS6, 0, sign2, counter4 * mdsize3, mdsize3);
                this.big8 >>>= this.w;
                ++this.counter;
            }
        }
        else if (w < 57) {
            final long test3 = this.test8;
            if (test3 == 0L) {
                this.big8 = 0L;
                this.ii = 0;
                final int r = this.r;
                int k = r >>> 3;
                int mdsize4 = this.mdsize;
                if (k < mdsize4) {
                    if (r <= (mdsize4 << 3) - w) {
                        final int r2 = w + r;
                        this.r = r2;
                        mdsize4 = r2 + 7 >>> 3;
                    }
                    else {
                        this.r = w + r;
                    }
                    while (k < mdsize4) {
                        final long big10 = this.big8;
                        final byte b = this.hash[k];
                        final int ii5 = this.ii;
                        this.big8 = (big10 ^ (long)((b & 0xFF) << (ii5 << 3)));
                        this.ii = ii5 + 1;
                        ++k;
                    }
                    final long big11 = this.big8 >>> r % 8;
                    this.big8 = big11;
                    this.test8 = (big11 & (long)this.k);
                }
                else {
                    final int checksum2 = this.checksum;
                    this.test8 = (this.k & checksum2);
                    this.checksum = checksum2 >>> w;
                }
                this.privateKeyOTS = this.gmssRandom.nextSeed(this.seed);
            }
            else if (test3 > 0L) {
                final Digest messDigestOTS3 = this.messDigestOTS;
                final byte[] privateKeyOTS7 = this.privateKeyOTS;
                messDigestOTS3.update(privateKeyOTS7, 0, privateKeyOTS7.length);
                final byte[] privateKeyOTS8 = new byte[this.messDigestOTS.getDigestSize()];
                this.privateKeyOTS = privateKeyOTS8;
                this.messDigestOTS.doFinal(privateKeyOTS8, 0);
                --this.test8;
            }
            if (this.test8 == 0L) {
                final byte[] privateKeyOTS9 = this.privateKeyOTS;
                final byte[] sign3 = this.sign;
                final int counter5 = this.counter;
                final int mdsize5 = this.mdsize;
                System.arraycopy(privateKeyOTS9, 0, sign3, counter5 * mdsize5, mdsize5);
                ++this.counter;
            }
        }
    }
    
    public int getLog(final int n) {
        int n2 = 1;
        for (int i = 2; i < n; i <<= 1, ++n2) {}
        return n2;
    }
    
    public byte[] getSig() {
        return this.sign;
    }
    
    public byte[][] getStatByte() {
        final byte[][] array = (byte[][])Array.newInstance(Byte.TYPE, 5, this.mdsize);
        array[0] = this.privateKeyOTS;
        array[1] = this.seed;
        array[2] = this.hash;
        array[3] = this.sign;
        array[4] = this.getStatLong();
        return array;
    }
    
    public int[] getStatInt() {
        return new int[] { this.counter, this.test, this.ii, this.r, this.steps, this.keysize, this.height, this.w, this.checksum };
    }
    
    public byte[] getStatLong() {
        final long test8 = this.test8;
        final byte b = (byte)(test8 & 0xFFL);
        final byte b2 = (byte)(test8 >> 8 & 0xFFL);
        final byte b3 = (byte)(test8 >> 16 & 0xFFL);
        final byte b4 = (byte)(test8 >> 24 & 0xFFL);
        final byte b5 = (byte)(test8 >> 32 & 0xFFL);
        final byte b6 = (byte)(test8 >> 40 & 0xFFL);
        final byte b7 = (byte)(test8 >> 48 & 0xFFL);
        final byte b8 = (byte)(test8 >> 56 & 0xFFL);
        final long big8 = this.big8;
        return new byte[] { b, b2, b3, b4, b5, b6, b7, b8, (byte)(big8 & 0xFFL), (byte)(big8 >> 8 & 0xFFL), (byte)(big8 >> 16 & 0xFFL), (byte)(big8 >> 24 & 0xFFL), (byte)(big8 >> 32 & 0xFFL), (byte)(big8 >> 40 & 0xFFL), (byte)(big8 >> 48 & 0xFFL), (byte)(big8 >> 56 & 0xFFL) };
    }
    
    public void initSign(final byte[] array, byte[] array2) {
        this.hash = new byte[this.mdsize];
        this.messDigestOTS.update(array2, 0, array2.length);
        array2 = new byte[this.messDigestOTS.getDigestSize()];
        this.hash = array2;
        this.messDigestOTS.doFinal(array2, 0);
        final int mdsize = this.mdsize;
        array2 = new byte[mdsize];
        System.arraycopy(this.hash, 0, array2, 0, mdsize);
        final int log = this.getLog((this.messagesize << this.w) + 1);
        final int w = this.w;
        int n2;
        if (8 % w == 0) {
            final int n = 8 / w;
            int i = 0;
            n2 = 0;
            while (i < this.mdsize) {
                for (int j = 0; j < n; ++j) {
                    n2 += (array2[i] & this.k);
                    array2[i] >>>= (byte)this.w;
                }
                ++i;
            }
            int checksum = (this.messagesize << this.w) - n2;
            this.checksum = checksum;
            int w2;
            for (int k = 0; k < log; k += w2) {
                n2 += (this.k & checksum);
                w2 = this.w;
                checksum >>>= w2;
            }
        }
        else if (w < 8) {
            final int n3 = this.mdsize / w;
            int l = 0;
            int n4 = 0;
            int n5 = 0;
            while (l < n3) {
                int n6;
                long n7;
                long n8;
                for (n6 = 0, n7 = 0L; n6 < this.w; ++n6, n7 ^= n8) {
                    n8 = (array2[n4] & 0xFF) << (n6 << 3);
                    ++n4;
                }
                for (int n9 = 0; n9 < 8; ++n9) {
                    n5 += (int)((long)this.k & n7);
                    n7 >>>= this.w;
                }
                ++l;
            }
            final int n10 = this.mdsize % this.w;
            final int n11 = 0;
            long n12 = 0L;
            int n13 = n4;
            long n15;
            for (int n14 = n11; n14 < n10; ++n14, n12 ^= n15) {
                n15 = (array2[n13] & 0xFF) << (n14 << 3);
                ++n13;
            }
            int w3;
            for (int n16 = 0; n16 < n10 << 3; n16 += w3) {
                n5 += (int)((long)this.k & n12);
                w3 = this.w;
                n12 >>>= w3;
            }
            int checksum2 = (this.messagesize << this.w) - n5;
            this.checksum = checksum2;
            int n17 = 0;
            while (true) {
                n2 = n5;
                if (n17 >= log) {
                    break;
                }
                n5 += (this.k & checksum2);
                final int w4 = this.w;
                checksum2 >>>= w4;
                n17 += w4;
            }
        }
        else if (w < 57) {
            int n18 = 0;
            int n19 = 0;
            int mdsize2;
            while (true) {
                mdsize2 = this.mdsize;
                final int w5 = this.w;
                if (n18 > (mdsize2 << 3) - w5) {
                    break;
                }
                int n20 = n18 >>> 3;
                final int n21 = w5 + n18;
                long n22 = 0L;
                int n23 = 0;
                while (n20 < n21 + 7 >>> 3) {
                    final long n24 = (array2[n20] & 0xFF) << (n23 << 3);
                    ++n23;
                    ++n20;
                    n22 ^= n24;
                }
                n19 += (int)(n22 >>> n18 % 8 & (long)this.k);
                n18 = n21;
            }
            final int n25 = n18 >>> 3;
            int n26 = n19;
            if (n25 < mdsize2) {
                final int n27 = 0;
                long n28 = 0L;
                int n29 = n25;
                int n30 = n27;
                while (n29 < this.mdsize) {
                    final long n31 = (array2[n29] & 0xFF) << (n30 << 3);
                    ++n30;
                    ++n29;
                    n28 ^= n31;
                }
                n26 = (int)(n19 + (n28 >>> n18 % 8 & (long)this.k));
            }
            int checksum3 = (this.messagesize << this.w) - n26;
            this.checksum = checksum3;
            int w6;
            for (int n32 = 0; n32 < log; n32 += w6) {
                n26 += (this.k & checksum3);
                w6 = this.w;
                checksum3 >>>= w6;
            }
            n2 = n26;
        }
        else {
            n2 = 0;
        }
        final int messagesize = this.messagesize;
        final double n33 = log;
        final double n34 = this.w;
        Double.isNaN(n33);
        Double.isNaN(n34);
        final int keysize = messagesize + (int)Math.ceil(n33 / n34);
        this.keysize = keysize;
        final double n35 = keysize + n2;
        final double n36 = 1 << this.height;
        Double.isNaN(n35);
        Double.isNaN(n36);
        this.steps = (int)Math.ceil(n35 / n36);
        final int keysize2 = this.keysize;
        final int mdsize3 = this.mdsize;
        this.sign = new byte[keysize2 * mdsize3];
        this.counter = 0;
        this.test = 0;
        this.ii = 0;
        this.test8 = 0L;
        this.r = 0;
        this.privateKeyOTS = new byte[mdsize3];
        array2 = new byte[mdsize3];
        System.arraycopy(array, 0, this.seed = array2, 0, mdsize3);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(this.big8);
        sb.append("  ");
        String s = sb.toString();
        final int[] statInt = this.getStatInt();
        final int mdsize = this.mdsize;
        final Class<Byte> type = Byte.TYPE;
        final int n = 0;
        final byte[][] array = (byte[][])Array.newInstance(type, 5, mdsize);
        final byte[][] statByte = this.getStatByte();
        int n2 = 0;
        String string;
        int i;
        while (true) {
            string = s;
            i = n;
            if (n2 >= 9) {
                break;
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append(statInt[n2]);
            sb2.append(" ");
            s = sb2.toString();
            ++n2;
        }
        while (i < 5) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(string);
            sb3.append(new String(Hex.encode(statByte[i])));
            sb3.append(" ");
            string = sb3.toString();
            ++i;
        }
        return string;
    }
    
    public boolean updateSign() {
        for (int i = 0; i < this.steps; ++i) {
            if (this.counter < this.keysize) {
                this.oneStep();
            }
            if (this.counter == this.keysize) {
                return true;
            }
        }
        return false;
    }
}
