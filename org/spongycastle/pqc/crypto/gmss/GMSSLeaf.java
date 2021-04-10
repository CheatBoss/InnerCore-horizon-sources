package org.spongycastle.pqc.crypto.gmss;

import org.spongycastle.pqc.crypto.gmss.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.util.*;
import org.spongycastle.util.encoders.*;

public class GMSSLeaf
{
    private byte[] concHashs;
    private GMSSRandom gmssRandom;
    private int i;
    private int j;
    private int keysize;
    private byte[] leaf;
    private int mdsize;
    private Digest messDigestOTS;
    byte[] privateKeyOTS;
    private byte[] seed;
    private int steps;
    private int two_power_w;
    private int w;
    
    GMSSLeaf(final Digest messDigestOTS, int mdsize, final int n) {
        this.w = mdsize;
        this.messDigestOTS = messDigestOTS;
        this.gmssRandom = new GMSSRandom(this.messDigestOTS);
        final int digestSize = this.messDigestOTS.getDigestSize();
        this.mdsize = digestSize;
        final double n2 = digestSize << 3;
        final double n3 = mdsize;
        Double.isNaN(n2);
        Double.isNaN(n3);
        final int n4 = (int)Math.ceil(n2 / n3);
        final double n5 = this.getLog((n4 << mdsize) + 1);
        Double.isNaN(n5);
        Double.isNaN(n3);
        final int keysize = n4 + (int)Math.ceil(n5 / n3);
        this.keysize = keysize;
        mdsize = 1 << mdsize;
        this.two_power_w = mdsize;
        final double n6 = (mdsize - 1) * keysize + 1 + keysize;
        final double n7 = n;
        Double.isNaN(n6);
        Double.isNaN(n7);
        this.steps = (int)Math.ceil(n6 / n7);
        mdsize = this.mdsize;
        this.seed = new byte[mdsize];
        this.leaf = new byte[mdsize];
        this.privateKeyOTS = new byte[mdsize];
        this.concHashs = new byte[mdsize * this.keysize];
    }
    
    public GMSSLeaf(final Digest messDigestOTS, int mdsize, final int n, final byte[] array) {
        this.w = mdsize;
        this.messDigestOTS = messDigestOTS;
        this.gmssRandom = new GMSSRandom(this.messDigestOTS);
        final int digestSize = this.messDigestOTS.getDigestSize();
        this.mdsize = digestSize;
        final double n2 = digestSize << 3;
        final double n3 = mdsize;
        Double.isNaN(n2);
        Double.isNaN(n3);
        final int n4 = (int)Math.ceil(n2 / n3);
        final double n5 = this.getLog((n4 << mdsize) + 1);
        Double.isNaN(n5);
        Double.isNaN(n3);
        final int keysize = n4 + (int)Math.ceil(n5 / n3);
        this.keysize = keysize;
        mdsize = 1 << mdsize;
        this.two_power_w = mdsize;
        final double n6 = (mdsize - 1) * keysize + 1 + keysize;
        final double n7 = n;
        Double.isNaN(n6);
        Double.isNaN(n7);
        this.steps = (int)Math.ceil(n6 / n7);
        mdsize = this.mdsize;
        this.seed = new byte[mdsize];
        this.leaf = new byte[mdsize];
        this.privateKeyOTS = new byte[mdsize];
        this.concHashs = new byte[mdsize * this.keysize];
        this.initLeafCalc(array);
    }
    
    public GMSSLeaf(final Digest messDigestOTS, final byte[][] array, final int[] array2) {
        this.i = array2[0];
        this.j = array2[1];
        this.steps = array2[2];
        this.w = array2[3];
        this.messDigestOTS = messDigestOTS;
        this.gmssRandom = new GMSSRandom(this.messDigestOTS);
        final int digestSize = this.messDigestOTS.getDigestSize();
        this.mdsize = digestSize;
        final double n = digestSize << 3;
        final double n2 = this.w;
        Double.isNaN(n);
        Double.isNaN(n2);
        final int n3 = (int)Math.ceil(n / n2);
        final double n4 = this.getLog((n3 << this.w) + 1);
        final double n5 = this.w;
        Double.isNaN(n4);
        Double.isNaN(n5);
        this.keysize = n3 + (int)Math.ceil(n4 / n5);
        this.two_power_w = 1 << this.w;
        this.privateKeyOTS = array[0];
        this.seed = array[1];
        this.concHashs = array[2];
        this.leaf = array[3];
    }
    
    private GMSSLeaf(final GMSSLeaf gmssLeaf) {
        this.messDigestOTS = gmssLeaf.messDigestOTS;
        this.mdsize = gmssLeaf.mdsize;
        this.keysize = gmssLeaf.keysize;
        this.gmssRandom = gmssLeaf.gmssRandom;
        this.leaf = Arrays.clone(gmssLeaf.leaf);
        this.concHashs = Arrays.clone(gmssLeaf.concHashs);
        this.i = gmssLeaf.i;
        this.j = gmssLeaf.j;
        this.two_power_w = gmssLeaf.two_power_w;
        this.w = gmssLeaf.w;
        this.steps = gmssLeaf.steps;
        this.seed = Arrays.clone(gmssLeaf.seed);
        this.privateKeyOTS = Arrays.clone(gmssLeaf.privateKeyOTS);
    }
    
    private int getLog(final int n) {
        int n2 = 1;
        for (int i = 2; i < n; i <<= 1, ++n2) {}
        return n2;
    }
    
    private void updateLeafCalc() {
        final byte[] privateKeyOTS = new byte[this.messDigestOTS.getDigestSize()];
        for (int i = 0; i < this.steps + 10000; ++i) {
            if (this.i == this.keysize && this.j == this.two_power_w - 1) {
                final Digest messDigestOTS = this.messDigestOTS;
                final byte[] concHashs = this.concHashs;
                messDigestOTS.update(concHashs, 0, concHashs.length);
                final byte[] leaf = new byte[this.messDigestOTS.getDigestSize()];
                this.leaf = leaf;
                this.messDigestOTS.doFinal(leaf, 0);
                return;
            }
            if (this.i != 0 && this.j != this.two_power_w - 1) {
                final Digest messDigestOTS2 = this.messDigestOTS;
                final byte[] privateKeyOTS2 = this.privateKeyOTS;
                messDigestOTS2.update(privateKeyOTS2, 0, privateKeyOTS2.length);
                this.privateKeyOTS = privateKeyOTS;
                this.messDigestOTS.doFinal(privateKeyOTS, 0);
                if (++this.j == this.two_power_w - 1) {
                    final byte[] privateKeyOTS3 = this.privateKeyOTS;
                    final byte[] concHashs2 = this.concHashs;
                    final int mdsize = this.mdsize;
                    System.arraycopy(privateKeyOTS3, 0, concHashs2, (this.i - 1) * mdsize, mdsize);
                }
            }
            else {
                ++this.i;
                this.j = 0;
                this.privateKeyOTS = this.gmssRandom.nextSeed(this.seed);
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("unable to updateLeaf in steps: ");
        sb.append(this.steps);
        sb.append(" ");
        sb.append(this.i);
        sb.append(" ");
        sb.append(this.j);
        throw new IllegalStateException(sb.toString());
    }
    
    public byte[] getLeaf() {
        return Arrays.clone(this.leaf);
    }
    
    public byte[][] getStatByte() {
        final byte[][] array = new byte[4][];
        final int mdsize = this.mdsize;
        array[0] = new byte[mdsize];
        array[1] = new byte[mdsize];
        array[2] = new byte[this.keysize * mdsize];
        array[3] = new byte[mdsize];
        array[0] = this.privateKeyOTS;
        array[1] = this.seed;
        array[2] = this.concHashs;
        array[3] = this.leaf;
        return array;
    }
    
    public int[] getStatInt() {
        return new int[] { this.i, this.j, this.steps, this.w };
    }
    
    void initLeafCalc(final byte[] array) {
        this.i = 0;
        this.j = 0;
        final byte[] array2 = new byte[this.mdsize];
        System.arraycopy(array, 0, array2, 0, this.seed.length);
        this.seed = this.gmssRandom.nextSeed(array2);
    }
    
    GMSSLeaf nextLeaf() {
        final GMSSLeaf gmssLeaf = new GMSSLeaf(this);
        gmssLeaf.updateLeafCalc();
        return gmssLeaf;
    }
    
    @Override
    public String toString() {
        final int n = 0;
        String string = "";
        for (int i = 0; i < 4; ++i) {
            final StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append(this.getStatInt()[i]);
            sb.append(" ");
            string = sb.toString();
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(string);
        sb2.append(" ");
        sb2.append(this.mdsize);
        sb2.append(" ");
        sb2.append(this.keysize);
        sb2.append(" ");
        sb2.append(this.two_power_w);
        sb2.append(" ");
        String s = sb2.toString();
        final byte[][] statByte = this.getStatByte();
        for (int j = n; j < 4; ++j) {
            StringBuilder sb3;
            String s2;
            if (statByte[j] != null) {
                sb3 = new StringBuilder();
                sb3.append(s);
                sb3.append(new String(Hex.encode(statByte[j])));
                s2 = " ";
            }
            else {
                sb3 = new StringBuilder();
                sb3.append(s);
                s2 = "null ";
            }
            sb3.append(s2);
            s = sb3.toString();
        }
        return s;
    }
}
