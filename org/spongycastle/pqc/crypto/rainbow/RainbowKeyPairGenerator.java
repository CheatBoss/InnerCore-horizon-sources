package org.spongycastle.pqc.crypto.rainbow;

import java.security.*;
import java.lang.reflect.*;
import org.spongycastle.pqc.crypto.rainbow.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class RainbowKeyPairGenerator implements AsymmetricCipherKeyPairGenerator
{
    private short[][] A1;
    private short[][] A1inv;
    private short[][] A2;
    private short[][] A2inv;
    private short[] b1;
    private short[] b2;
    private boolean initialized;
    private Layer[] layers;
    private int numOfLayers;
    private short[][] pub_quadratic;
    private short[] pub_scalar;
    private short[][] pub_singular;
    private RainbowKeyGenerationParameters rainbowParams;
    private SecureRandom sr;
    private int[] vi;
    
    public RainbowKeyPairGenerator() {
        this.initialized = false;
    }
    
    private void compactPublicKey(final short[][][] array) {
        final int length = array.length;
        final int length2 = array[0].length;
        this.pub_quadratic = (short[][])Array.newInstance(Short.TYPE, length, (length2 + 1) * length2 / 2);
        for (int i = 0; i < length; ++i) {
            int j = 0;
            int n = 0;
            while (j < length2) {
                for (int k = j; k < length2; ++k) {
                    if (k == j) {
                        this.pub_quadratic[i][n] = array[i][j][k];
                    }
                    else {
                        this.pub_quadratic[i][n] = GF2Field.addElem(array[i][j][k], array[i][k][j]);
                    }
                    ++n;
                }
                ++j;
            }
        }
    }
    
    private void computePublicKey() {
        final Class<Short> type = Short.TYPE;
        final ComputeInField computeInField = new ComputeInField();
        final int[] vi = this.vi;
        final int n = vi[vi.length - 1] - vi[0];
        final int n2 = vi[vi.length - 1];
        final short[][][] array = (short[][][])Array.newInstance(Short.TYPE, n, n2, n2);
        this.pub_singular = (short[][])Array.newInstance(Short.TYPE, n, n2);
        this.pub_scalar = new short[n];
        final short[] array2 = new short[n2];
        int n3 = 0;
        int n4 = 0;
        while (true) {
            int i = 0;
            final Layer[] layers = this.layers;
            if (n3 >= layers.length) {
                break;
            }
            final short[][][] coeffAlpha = layers[n3].getCoeffAlpha();
            final short[][][] coeffBeta = this.layers[n3].getCoeffBeta();
            final short[][] coeffGamma = this.layers[n3].getCoeffGamma();
            final short[] coeffEta = this.layers[n3].getCoeffEta();
            final int length = coeffAlpha[0].length;
            final int length2 = coeffBeta[0].length;
            while (i < length) {
                for (int j = 0; j < length; ++j) {
                    for (int k = 0; k < length2; ++k) {
                        final short n5 = coeffAlpha[i][j][k];
                        final short[][] a2 = this.A2;
                        final int n6 = j + length2;
                        final short[] multVect = computeInField.multVect(n5, a2[n6]);
                        final int n7 = n4 + i;
                        array[n7] = computeInField.addSquareMatrix(array[n7], computeInField.multVects(multVect, this.A2[k]));
                        final short[] multVect2 = computeInField.multVect(this.b2[k], multVect);
                        final short[][] pub_singular = this.pub_singular;
                        pub_singular[n7] = computeInField.addVect(multVect2, pub_singular[n7]);
                        final short[] multVect3 = computeInField.multVect(this.b2[n6], computeInField.multVect(coeffAlpha[i][j][k], this.A2[k]));
                        final short[][] pub_singular2 = this.pub_singular;
                        pub_singular2[n7] = computeInField.addVect(multVect3, pub_singular2[n7]);
                        final short multElem = GF2Field.multElem(coeffAlpha[i][j][k], this.b2[n6]);
                        final short[] pub_scalar = this.pub_scalar;
                        pub_scalar[n7] = GF2Field.addElem(pub_scalar[n7], GF2Field.multElem(multElem, this.b2[k]));
                    }
                }
                for (int l = 0; l < length2; ++l) {
                    for (int n8 = 0; n8 < length2; ++n8) {
                        final short[] multVect4 = computeInField.multVect(coeffBeta[i][l][n8], this.A2[l]);
                        final int n9 = n4 + i;
                        array[n9] = computeInField.addSquareMatrix(array[n9], computeInField.multVects(multVect4, this.A2[n8]));
                        final short[] multVect5 = computeInField.multVect(this.b2[n8], multVect4);
                        final short[][] pub_singular3 = this.pub_singular;
                        pub_singular3[n9] = computeInField.addVect(multVect5, pub_singular3[n9]);
                        final short[] multVect6 = computeInField.multVect(this.b2[l], computeInField.multVect(coeffBeta[i][l][n8], this.A2[n8]));
                        final short[][] pub_singular4 = this.pub_singular;
                        pub_singular4[n9] = computeInField.addVect(multVect6, pub_singular4[n9]);
                        final short multElem2 = GF2Field.multElem(coeffBeta[i][l][n8], this.b2[l]);
                        final short[] pub_scalar2 = this.pub_scalar;
                        pub_scalar2[n9] = GF2Field.addElem(pub_scalar2[n9], GF2Field.multElem(multElem2, this.b2[n8]));
                    }
                }
                for (int n10 = 0; n10 < length2 + length; ++n10) {
                    final short[] multVect7 = computeInField.multVect(coeffGamma[i][n10], this.A2[n10]);
                    final short[][] pub_singular5 = this.pub_singular;
                    final int n11 = n4 + i;
                    pub_singular5[n11] = computeInField.addVect(multVect7, pub_singular5[n11]);
                    final short[] pub_scalar3 = this.pub_scalar;
                    pub_scalar3[n11] = GF2Field.addElem(pub_scalar3[n11], GF2Field.multElem(coeffGamma[i][n10], this.b2[n10]));
                }
                final short[] pub_scalar4 = this.pub_scalar;
                final int n12 = n4 + i;
                pub_scalar4[n12] = GF2Field.addElem(pub_scalar4[n12], coeffEta[i]);
                ++i;
            }
            n4 += length;
            ++n3;
        }
        final short[][][] array3 = (short[][][])Array.newInstance(type, n, n2, n2);
        final short[][] pub_singular6 = (short[][])Array.newInstance(type, n, n2);
        final short[] pub_scalar5 = new short[n];
        for (int n13 = 0; n13 < n; ++n13) {
            int n14 = 0;
            while (true) {
                final short[][] a3 = this.A1;
                if (n14 >= a3.length) {
                    break;
                }
                array3[n13] = computeInField.addSquareMatrix(array3[n13], computeInField.multMatrix(a3[n13][n14], array[n14]));
                pub_singular6[n13] = computeInField.addVect(pub_singular6[n13], computeInField.multVect(this.A1[n13][n14], this.pub_singular[n14]));
                pub_scalar5[n13] = GF2Field.addElem(pub_scalar5[n13], GF2Field.multElem(this.A1[n13][n14], this.pub_scalar[n14]));
                ++n14;
            }
            pub_scalar5[n13] = GF2Field.addElem(pub_scalar5[n13], this.b1[n13]);
        }
        this.pub_singular = pub_singular6;
        this.pub_scalar = pub_scalar5;
        this.compactPublicKey(array3);
    }
    
    private void generateF() {
        this.layers = new Layer[this.numOfLayers];
        int n2;
        for (int i = 0; i < this.numOfLayers; i = n2) {
            final Layer[] layers = this.layers;
            final int[] vi = this.vi;
            final int n = vi[i];
            n2 = i + 1;
            layers[i] = new Layer(n, vi[n2], this.sr);
        }
    }
    
    private void generateL1() {
        final int[] vi = this.vi;
        final int n = vi[vi.length - 1];
        final int n2 = 0;
        final int n3 = n - vi[0];
        this.A1 = (short[][])Array.newInstance(Short.TYPE, n3, n3);
        this.A1inv = null;
        final ComputeInField computeInField = new ComputeInField();
        while (this.A1inv == null) {
            for (int i = 0; i < n3; ++i) {
                for (int j = 0; j < n3; ++j) {
                    this.A1[i][j] = (short)(this.sr.nextInt() & 0xFF);
                }
            }
            this.A1inv = computeInField.inverse(this.A1);
        }
        this.b1 = new short[n3];
        for (int k = n2; k < n3; ++k) {
            this.b1[k] = (short)(this.sr.nextInt() & 0xFF);
        }
    }
    
    private void generateL2() {
        final int[] vi = this.vi;
        final int n = vi[vi.length - 1];
        final Class<Short> type = Short.TYPE;
        final int n2 = 0;
        this.A2 = (short[][])Array.newInstance(type, n, n);
        this.A2inv = null;
        final ComputeInField computeInField = new ComputeInField();
        while (this.A2inv == null) {
            for (int i = 0; i < n; ++i) {
                for (int j = 0; j < n; ++j) {
                    this.A2[i][j] = (short)(this.sr.nextInt() & 0xFF);
                }
            }
            this.A2inv = computeInField.inverse(this.A2);
        }
        this.b2 = new short[n];
        for (int k = n2; k < n; ++k) {
            this.b2[k] = (short)(this.sr.nextInt() & 0xFF);
        }
    }
    
    private void initializeDefault() {
        this.initialize(new RainbowKeyGenerationParameters(new SecureRandom(), new RainbowParameters()));
    }
    
    private void keygen() {
        this.generateL1();
        this.generateL2();
        this.generateF();
        this.computePublicKey();
    }
    
    public AsymmetricCipherKeyPair genKeyPair() {
        if (!this.initialized) {
            this.initializeDefault();
        }
        this.keygen();
        final RainbowPrivateKeyParameters rainbowPrivateKeyParameters = new RainbowPrivateKeyParameters(this.A1inv, this.b1, this.A2inv, this.b2, this.vi, this.layers);
        final int[] vi = this.vi;
        return new AsymmetricCipherKeyPair(new RainbowPublicKeyParameters(vi[vi.length - 1] - vi[0], this.pub_quadratic, this.pub_singular, this.pub_scalar), rainbowPrivateKeyParameters);
    }
    
    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        return this.genKeyPair();
    }
    
    @Override
    public void init(final KeyGenerationParameters keyGenerationParameters) {
        this.initialize(keyGenerationParameters);
    }
    
    public void initialize(final KeyGenerationParameters keyGenerationParameters) {
        final RainbowKeyGenerationParameters rainbowParams = (RainbowKeyGenerationParameters)keyGenerationParameters;
        this.rainbowParams = rainbowParams;
        this.sr = rainbowParams.getRandom();
        this.vi = this.rainbowParams.getParameters().getVi();
        this.numOfLayers = this.rainbowParams.getParameters().getNumOfLayers();
        this.initialized = true;
    }
}
