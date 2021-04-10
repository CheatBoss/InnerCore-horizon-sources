package org.spongycastle.pqc.crypto.rainbow;

import java.security.*;
import java.lang.reflect.*;
import org.spongycastle.util.*;
import org.spongycastle.pqc.crypto.rainbow.util.*;

public class Layer
{
    private short[][][] coeff_alpha;
    private short[][][] coeff_beta;
    private short[] coeff_eta;
    private short[][] coeff_gamma;
    private int oi;
    private int vi;
    private int viNext;
    
    public Layer(final byte b, final byte b2, final short[][][] coeff_alpha, final short[][][] coeff_beta, final short[][] coeff_gamma, final short[] coeff_eta) {
        final int vi = b & 0xFF;
        this.vi = vi;
        final int viNext = b2 & 0xFF;
        this.viNext = viNext;
        this.oi = viNext - vi;
        this.coeff_alpha = coeff_alpha;
        this.coeff_beta = coeff_beta;
        this.coeff_gamma = coeff_gamma;
        this.coeff_eta = coeff_eta;
    }
    
    public Layer(int i, int j, final SecureRandom secureRandom) {
        this.vi = i;
        this.viNext = j;
        j -= i;
        this.oi = j;
        final Class<Short> type = Short.TYPE;
        final int n = 0;
        this.coeff_alpha = (short[][][])Array.newInstance(type, j, j, i);
        i = this.oi;
        j = this.vi;
        this.coeff_beta = (short[][][])Array.newInstance(Short.TYPE, i, j, j);
        this.coeff_gamma = (short[][])Array.newInstance(Short.TYPE, this.oi, this.viNext);
        final int oi = this.oi;
        this.coeff_eta = new short[oi];
        int k;
        for (i = 0; i < oi; ++i) {
            for (j = 0; j < this.oi; ++j) {
                for (k = 0; k < this.vi; ++k) {
                    this.coeff_alpha[i][j][k] = (short)(secureRandom.nextInt() & 0xFF);
                }
            }
        }
        int l;
        for (i = 0; i < oi; ++i) {
            for (j = 0; j < this.vi; ++j) {
                for (l = 0; l < this.vi; ++l) {
                    this.coeff_beta[i][j][l] = (short)(secureRandom.nextInt() & 0xFF);
                }
            }
        }
        i = 0;
        while (true) {
            j = n;
            if (i >= oi) {
                break;
            }
            for (j = 0; j < this.viNext; ++j) {
                this.coeff_gamma[i][j] = (short)(secureRandom.nextInt() & 0xFF);
            }
            ++i;
        }
        while (j < oi) {
            this.coeff_eta[j] = (short)(secureRandom.nextInt() & 0xFF);
            ++j;
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b2;
        final boolean b = b2 = false;
        if (o != null) {
            if (!(o instanceof Layer)) {
                return false;
            }
            final Layer layer = (Layer)o;
            b2 = b;
            if (this.vi == layer.getVi()) {
                b2 = b;
                if (this.viNext == layer.getViNext()) {
                    b2 = b;
                    if (this.oi == layer.getOi()) {
                        b2 = b;
                        if (RainbowUtil.equals(this.coeff_alpha, layer.getCoeffAlpha())) {
                            b2 = b;
                            if (RainbowUtil.equals(this.coeff_beta, layer.getCoeffBeta())) {
                                b2 = b;
                                if (RainbowUtil.equals(this.coeff_gamma, layer.getCoeffGamma())) {
                                    b2 = b;
                                    if (RainbowUtil.equals(this.coeff_eta, layer.getCoeffEta())) {
                                        b2 = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return b2;
    }
    
    public short[][][] getCoeffAlpha() {
        return this.coeff_alpha;
    }
    
    public short[][][] getCoeffBeta() {
        return this.coeff_beta;
    }
    
    public short[] getCoeffEta() {
        return this.coeff_eta;
    }
    
    public short[][] getCoeffGamma() {
        return this.coeff_gamma;
    }
    
    public int getOi() {
        return this.oi;
    }
    
    public int getVi() {
        return this.vi;
    }
    
    public int getViNext() {
        return this.viNext;
    }
    
    @Override
    public int hashCode() {
        return (((((this.vi * 37 + this.viNext) * 37 + this.oi) * 37 + Arrays.hashCode(this.coeff_alpha)) * 37 + Arrays.hashCode(this.coeff_beta)) * 37 + Arrays.hashCode(this.coeff_gamma)) * 37 + Arrays.hashCode(this.coeff_eta);
    }
    
    public short[][] plugInVinegars(short[] array) {
        final int oi = this.oi;
        final Class<Short> type = Short.TYPE;
        final int n = 0;
        final short[][] array2 = (short[][])Array.newInstance(type, oi, oi + 1);
        final short[] array3 = new short[this.oi];
        for (int i = 0; i < this.oi; ++i) {
            for (int j = 0; j < this.vi; ++j) {
                for (int k = 0; k < this.vi; ++k) {
                    array3[i] = GF2Field.addElem(array3[i], GF2Field.multElem(GF2Field.multElem(this.coeff_beta[i][j][k], array[j]), array[k]));
                }
            }
        }
        for (int l = 0; l < this.oi; ++l) {
            for (int n2 = 0; n2 < this.oi; ++n2) {
                for (int n3 = 0; n3 < this.vi; ++n3) {
                    array2[l][n2] = GF2Field.addElem(array2[l][n2], GF2Field.multElem(this.coeff_alpha[l][n2][n3], array[n3]));
                }
            }
        }
        for (int n4 = 0; n4 < this.oi; ++n4) {
            for (int n5 = 0; n5 < this.vi; ++n5) {
                array3[n4] = GF2Field.addElem(array3[n4], GF2Field.multElem(this.coeff_gamma[n4][n5], array[n5]));
            }
        }
        for (int n6 = 0; n6 < this.oi; ++n6) {
            for (int vi = this.vi; vi < this.viNext; ++vi) {
                array = array2[n6];
                final int n7 = vi - this.vi;
                array[n7] = GF2Field.addElem(this.coeff_gamma[n6][vi], array2[n6][n7]);
            }
        }
        int n8 = 0;
        int n9;
        while (true) {
            n9 = n;
            if (n8 >= this.oi) {
                break;
            }
            array3[n8] = GF2Field.addElem(array3[n8], this.coeff_eta[n8]);
            ++n8;
        }
        while (true) {
            final int oi2 = this.oi;
            if (n9 >= oi2) {
                break;
            }
            array2[n9][oi2] = array3[n9];
            ++n9;
        }
        return array2;
    }
}
