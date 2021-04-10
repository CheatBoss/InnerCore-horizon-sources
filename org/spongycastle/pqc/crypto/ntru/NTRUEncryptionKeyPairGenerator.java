package org.spongycastle.pqc.crypto.ntru;

import org.spongycastle.pqc.math.ntru.util.*;
import org.spongycastle.pqc.math.ntru.polynomial.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class NTRUEncryptionKeyPairGenerator implements AsymmetricCipherKeyPairGenerator
{
    private NTRUEncryptionKeyGenerationParameters params;
    
    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        final int n = this.params.N;
        final int q = this.params.q;
        final int df = this.params.df;
        final int df2 = this.params.df1;
        final int df3 = this.params.df2;
        final int df4 = this.params.df3;
        final int dg = this.params.dg;
        final boolean fastFp = this.params.fastFp;
        final boolean sparse = this.params.sparse;
        IntegerPolynomial integerPolynomial = null;
        Object o;
        IntegerPolynomial invertFq;
        while (true) {
            IntegerPolynomial integerPolynomial2;
            if (fastFp) {
                if (this.params.polyType == 0) {
                    o = Util.generateRandomTernary(n, df, df, sparse, this.params.getRandom());
                }
                else {
                    o = ProductFormPolynomial.generateRandom(n, df2, df3, df4, df4, this.params.getRandom());
                }
                integerPolynomial2 = ((Polynomial)o).toIntegerPolynomial();
                integerPolynomial2.mult(3);
                final int[] coeffs = integerPolynomial2.coeffs;
                ++coeffs[0];
            }
            else {
                Object o2;
                if (this.params.polyType == 0) {
                    o2 = Util.generateRandomTernary(n, df, df - 1, sparse, this.params.getRandom());
                }
                else {
                    o2 = ProductFormPolynomial.generateRandom(n, df2, df3, df4, df4 - 1, this.params.getRandom());
                }
                integerPolynomial2 = ((Polynomial)o2).toIntegerPolynomial();
                final IntegerPolynomial invertF3 = integerPolynomial2.invertF3();
                o = o2;
                integerPolynomial = invertF3;
                if (invertF3 == null) {
                    integerPolynomial = invertF3;
                    continue;
                }
            }
            invertFq = integerPolynomial2.invertFq(q);
            if (invertFq == null) {
                continue;
            }
            break;
        }
        if (fastFp) {
            integerPolynomial = new IntegerPolynomial(n);
            integerPolynomial.coeffs[0] = 1;
        }
        DenseTernaryPolynomial generateRandom;
        do {
            generateRandom = DenseTernaryPolynomial.generateRandom(n, dg, dg - 1, this.params.getRandom());
        } while (generateRandom.invertFq(q) == null);
        final IntegerPolynomial mult = generateRandom.mult(invertFq, q);
        mult.mult3(q);
        mult.ensurePositive(q);
        generateRandom.clear();
        invertFq.clear();
        return new AsymmetricCipherKeyPair(new NTRUEncryptionPublicKeyParameters(mult, this.params.getEncryptionParameters()), new NTRUEncryptionPrivateKeyParameters(mult, (Polynomial)o, integerPolynomial, this.params.getEncryptionParameters()));
    }
    
    @Override
    public void init(final KeyGenerationParameters keyGenerationParameters) {
        this.params = (NTRUEncryptionKeyGenerationParameters)keyGenerationParameters;
    }
}
