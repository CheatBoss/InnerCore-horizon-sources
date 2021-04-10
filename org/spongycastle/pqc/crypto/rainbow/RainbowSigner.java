package org.spongycastle.pqc.crypto.rainbow;

import org.spongycastle.pqc.crypto.*;
import java.security.*;
import org.spongycastle.pqc.crypto.rainbow.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class RainbowSigner implements MessageSigner
{
    private static final int MAXITS = 65536;
    private ComputeInField cf;
    RainbowKeyParameters key;
    private SecureRandom random;
    int signableDocumentLength;
    private short[] x;
    
    public RainbowSigner() {
        this.cf = new ComputeInField();
    }
    
    private short[] initSign(final Layer[] array, short[] array2) {
        final short[] array3 = new short[array2.length];
        array2 = this.cf.addVect(((RainbowPrivateKeyParameters)this.key).getB1(), array2);
        array2 = this.cf.multiplyMatrix(((RainbowPrivateKeyParameters)this.key).getInvA1(), array2);
        for (int i = 0; i < array[0].getVi(); ++i) {
            this.x[i] = (short)this.random.nextInt();
            final short[] x = this.x;
            x[i] &= 0xFF;
        }
        return array2;
    }
    
    private short[] makeMessageRepresentative(final byte[] array) {
        final int signableDocumentLength = this.signableDocumentLength;
        final short[] array2 = new short[signableDocumentLength];
        int i = 0;
        int n = 0;
        while (i < array.length) {
            array2[i] = array[n];
            array2[i] &= 0xFF;
            ++n;
            if (++i >= signableDocumentLength) {
                return array2;
            }
        }
        return array2;
    }
    
    private short[] verifySignatureIntern(final short[] array) {
        final short[][] coeffQuadratic = ((RainbowPublicKeyParameters)this.key).getCoeffQuadratic();
        final short[][] coeffSingular = ((RainbowPublicKeyParameters)this.key).getCoeffSingular();
        final short[] coeffScalar = ((RainbowPublicKeyParameters)this.key).getCoeffScalar();
        final short[] array2 = new short[coeffQuadratic.length];
        final int length = coeffSingular[0].length;
        for (int i = 0; i < coeffQuadratic.length; ++i) {
            int j = 0;
            int n = 0;
            while (j < length) {
                for (int k = j; k < length; ++k) {
                    array2[i] = GF2Field.addElem(array2[i], GF2Field.multElem(coeffQuadratic[i][n], GF2Field.multElem(array[j], array[k])));
                    ++n;
                }
                array2[i] = GF2Field.addElem(array2[i], GF2Field.multElem(coeffSingular[i][j], array[j]));
                ++j;
            }
            array2[i] = GF2Field.addElem(array2[i], coeffScalar[i]);
        }
        return array2;
    }
    
    @Override
    public byte[] generateSignature(byte[] messageRepresentative) {
        final Layer[] layers = ((RainbowPrivateKeyParameters)this.key).getLayers();
        final int length = layers.length;
        this.x = new short[((RainbowPrivateKeyParameters)this.key).getInvA2().length];
        final int viNext = layers[length - 1].getViNext();
        final byte[] array = new byte[viNext];
        messageRepresentative = (byte[])this.makeMessageRepresentative(messageRepresentative);
        int n = 0;
        int n4 = 0;
    Label_0074_Outer:
        while (true) {
            while (true) {
                int n2 = 0;
                Label_0345: {
                    boolean b;
                    try {
                        final short[] initSign = this.initSign(layers, (short[])messageRepresentative);
                        n2 = 0;
                        int n3 = 0;
                        if (n2 < length) {
                            final short[] array2 = new short[layers[n2].getOi()];
                            final short[] array3 = new short[layers[n2].getOi()];
                            for (int i = 0; i < layers[n2].getOi(); ++i) {
                                array2[i] = initSign[n3];
                                ++n3;
                            }
                            final short[] solveEquation = this.cf.solveEquation(layers[n2].plugInVinegars(this.x), array2);
                            if (solveEquation != null) {
                                for (int j = 0; j < solveEquation.length; ++j) {
                                    this.x[layers[n2].getVi() + j] = solveEquation[j];
                                }
                                break Label_0345;
                            }
                            throw new Exception("LES is not solveable!");
                        }
                        else {
                            final short[] multiplyMatrix = this.cf.multiplyMatrix(((RainbowPrivateKeyParameters)this.key).getInvA2(), this.cf.addVect(((RainbowPrivateKeyParameters)this.key).getB2(), this.x));
                            for (int k = 0; k < viNext; ++k) {
                                array[k] = (byte)multiplyMatrix[k];
                            }
                            b = true;
                        }
                    }
                    catch (Exception ex) {
                        b = false;
                    }
                    n4 = n;
                    if (b) {
                        break;
                    }
                    n4 = n + 1;
                    if ((n = n4) >= 65536) {
                        break;
                    }
                    continue Label_0074_Outer;
                }
                ++n2;
                continue;
            }
        }
        if (n4 != 65536) {
            return array;
        }
        throw new IllegalStateException("unable to generate signature - LES not solvable");
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) {
        Label_0067: {
            RainbowKeyParameters key;
            if (b) {
                if (cipherParameters instanceof ParametersWithRandom) {
                    final ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
                    this.random = parametersWithRandom.getRandom();
                    this.key = (RainbowPrivateKeyParameters)parametersWithRandom.getParameters();
                    break Label_0067;
                }
                this.random = new SecureRandom();
                key = (RainbowPrivateKeyParameters)cipherParameters;
            }
            else {
                key = (RainbowPublicKeyParameters)cipherParameters;
            }
            this.key = key;
        }
        this.signableDocumentLength = this.key.getDocLength();
    }
    
    @Override
    public boolean verifySignature(final byte[] array, final byte[] array2) {
        final short[] array3 = new short[array2.length];
        for (int i = 0; i < array2.length; ++i) {
            array3[i] = (short)(array2[i] & 0xFF);
        }
        final short[] messageRepresentative = this.makeMessageRepresentative(array);
        final short[] verifySignatureIntern = this.verifySignatureIntern(array3);
        if (messageRepresentative.length != verifySignatureIntern.length) {
            return false;
        }
        int j = 0;
        boolean b = true;
        while (j < messageRepresentative.length) {
            b = (b && messageRepresentative[j] == verifySignatureIntern[j]);
            ++j;
        }
        return b;
    }
}
