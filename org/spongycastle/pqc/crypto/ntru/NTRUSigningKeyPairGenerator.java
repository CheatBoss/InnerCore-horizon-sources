package org.spongycastle.pqc.crypto.ntru;

import java.security.*;
import org.spongycastle.pqc.math.ntru.euclid.*;
import java.math.*;
import org.spongycastle.pqc.math.ntru.polynomial.*;
import java.util.*;
import org.spongycastle.crypto.params.*;
import java.util.concurrent.*;
import org.spongycastle.crypto.*;

public class NTRUSigningKeyPairGenerator implements AsymmetricCipherKeyPairGenerator
{
    private NTRUSigningKeyGenerationParameters params;
    
    private FGBasis generateBasis() {
        final int n = this.params.N;
        final int q = this.params.q;
        final int d = this.params.d;
        final int d2 = this.params.d1;
        final int d3 = this.params.d2;
        final int d4 = this.params.d3;
        final int basisType = this.params.basisType;
        final int n2 = n * 2 + 1;
        final boolean primeCheck = this.params.primeCheck;
        Polynomial polynomial;
        IntegerPolynomial integerPolynomial;
        IntegerPolynomial invertFq;
        while (true) {
            if (this.params.polyType == 0) {
                polynomial = DenseTernaryPolynomial.generateRandom(n, d + 1, d, new SecureRandom());
            }
            else {
                polynomial = ProductFormPolynomial.generateRandom(n, d2, d3, d4 + 1, d4, new SecureRandom());
            }
            integerPolynomial = polynomial.toIntegerPolynomial();
            if (primeCheck && integerPolynomial.resultant(n2).res.equals(BigInteger.ZERO)) {
                continue;
            }
            invertFq = integerPolynomial.invertFq(q);
            if (invertFq != null) {
                break;
            }
        }
        final Resultant resultant = integerPolynomial.resultant();
        final Polynomial polynomial2 = polynomial;
        Polynomial polynomial3;
        IntegerPolynomial integerPolynomial2;
        Resultant resultant2;
        BigIntEuclidean calculate;
        while (true) {
            if (this.params.polyType == 0) {
                polynomial3 = DenseTernaryPolynomial.generateRandom(n, d + 1, d, new SecureRandom());
            }
            else {
                polynomial3 = ProductFormPolynomial.generateRandom(n, d2, d3, d4 + 1, d4, new SecureRandom());
            }
            integerPolynomial2 = polynomial3.toIntegerPolynomial();
            if (primeCheck && integerPolynomial2.resultant(n2).res.equals(BigInteger.ZERO) && integerPolynomial2.invertFq(q) != null) {
                resultant2 = integerPolynomial2.resultant();
                calculate = BigIntEuclidean.calculate(resultant.res, resultant2.res);
                if (calculate.gcd.equals(BigInteger.ONE)) {
                    break;
                }
                continue;
            }
        }
        final BigIntPolynomial bigIntPolynomial = (BigIntPolynomial)resultant.rho.clone();
        bigIntPolynomial.mult(calculate.x.multiply(BigInteger.valueOf(q)));
        final BigIntPolynomial bigIntPolynomial2 = (BigIntPolynomial)resultant2.rho.clone();
        bigIntPolynomial2.mult(calculate.y.multiply(BigInteger.valueOf(-q)));
        final int keyGenAlg = this.params.keyGenAlg;
        int n3 = 0;
        BigIntPolynomial bigIntPolynomial3;
        if (keyGenAlg == 0) {
            final int[] array = new int[n];
            final int[] array2 = new int[n];
            array[0] = integerPolynomial.coeffs[0];
            array2[0] = integerPolynomial2.coeffs[0];
            for (int i = 1; i < n; ++i) {
                final int[] coeffs = integerPolynomial.coeffs;
                final int n4 = n - i;
                array[i] = coeffs[n4];
                array2[i] = integerPolynomial2.coeffs[n4];
            }
            final IntegerPolynomial integerPolynomial3 = new IntegerPolynomial(array);
            final IntegerPolynomial integerPolynomial4 = new IntegerPolynomial(array2);
            final IntegerPolynomial mult = polynomial2.mult(integerPolynomial3);
            mult.add(polynomial3.mult(integerPolynomial4));
            final Resultant resultant3 = mult.resultant();
            final BigIntPolynomial mult2 = integerPolynomial3.mult(bigIntPolynomial2);
            mult2.add(integerPolynomial4.mult(bigIntPolynomial));
            bigIntPolynomial3 = mult2.mult(resultant3.rho);
            bigIntPolynomial3.div(resultant3.res);
        }
        else {
            for (int j = 1; j < n; j *= 10) {
                ++n3;
            }
            final BigDecimalPolynomial div = resultant.rho.div(new BigDecimal(resultant.res), bigIntPolynomial2.getMaxCoeffLength() + 1 + n3);
            final BigDecimalPolynomial div2 = resultant2.rho.div(new BigDecimal(resultant2.res), bigIntPolynomial.getMaxCoeffLength() + 1 + n3);
            final BigDecimalPolynomial mult3 = div.mult(bigIntPolynomial2);
            mult3.add(div2.mult(bigIntPolynomial));
            mult3.halve();
            bigIntPolynomial3 = mult3.round();
        }
        final BigIntPolynomial bigIntPolynomial4 = (BigIntPolynomial)bigIntPolynomial2.clone();
        bigIntPolynomial4.sub(polynomial2.mult(bigIntPolynomial3));
        final BigIntPolynomial bigIntPolynomial5 = (BigIntPolynomial)bigIntPolynomial.clone();
        bigIntPolynomial5.sub(polynomial3.mult(bigIntPolynomial3));
        final IntegerPolynomial integerPolynomial5 = new IntegerPolynomial(bigIntPolynomial4);
        final IntegerPolynomial integerPolynomial6 = new IntegerPolynomial(bigIntPolynomial5);
        this.minimizeFG(integerPolynomial, integerPolynomial2, integerPolynomial5, integerPolynomial6, n);
        IntegerPolynomial integerPolynomial7;
        if (basisType == 0) {
            integerPolynomial7 = polynomial3.mult(invertFq, q);
            polynomial3 = integerPolynomial5;
        }
        else {
            integerPolynomial7 = integerPolynomial5.mult(invertFq, q);
        }
        integerPolynomial7.modPositive(q);
        return new FGBasis(polynomial2, polynomial3, integerPolynomial7, integerPolynomial5, integerPolynomial6, this.params);
    }
    
    private void minimizeFG(final IntegerPolynomial integerPolynomial, final IntegerPolynomial integerPolynomial2, final IntegerPolynomial integerPolynomial3, final IntegerPolynomial integerPolynomial4, final int n) {
        int i = 0;
        int n2 = 0;
        while (i < n) {
            n2 += n * 2 * (integerPolynomial.coeffs[i] * integerPolynomial.coeffs[i] + integerPolynomial2.coeffs[i] * integerPolynomial2.coeffs[i]);
            ++i;
        }
        final int n3 = n2 - 4;
        final IntegerPolynomial integerPolynomial5 = (IntegerPolynomial)integerPolynomial.clone();
        final IntegerPolynomial integerPolynomial6 = (IntegerPolynomial)integerPolynomial2.clone();
        int n8 = 0;
        for (int n4 = 0, n5 = 0; n4 < n && n5 < n; n4 = n8) {
            int j = 0;
            int n6 = 0;
            while (j < n) {
                n6 += n * 4 * (integerPolynomial3.coeffs[j] * integerPolynomial.coeffs[j] + integerPolynomial4.coeffs[j] * integerPolynomial2.coeffs[j]);
                ++j;
            }
            final int n7 = n6 - (integerPolynomial3.sumCoeffs() + integerPolynomial4.sumCoeffs()) * 4;
            Label_0245: {
                if (n7 > n3) {
                    integerPolynomial3.sub(integerPolynomial5);
                    integerPolynomial4.sub(integerPolynomial6);
                }
                else {
                    n8 = n4;
                    if (n7 >= -n3) {
                        break Label_0245;
                    }
                    integerPolynomial3.add(integerPolynomial5);
                    integerPolynomial4.add(integerPolynomial6);
                }
                n8 = n4 + 1;
                n5 = 0;
            }
            ++n5;
            integerPolynomial5.rotate1();
            integerPolynomial6.rotate1();
        }
    }
    
    public NTRUSigningPrivateKeyParameters.Basis generateBoundedBasis() {
        FGBasis generateBasis;
        do {
            generateBasis = this.generateBasis();
        } while (!generateBasis.isNormOk());
        return generateBasis;
    }
    
    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        final ArrayList<Future<Object>> list = new ArrayList<Future<Object>>();
        int b = this.params.B;
        NTRUSigningPublicKeyParameters ntruSigningPublicKeyParameters;
        while (true) {
            ntruSigningPublicKeyParameters = null;
            if (b < 0) {
                break;
            }
            list.add(cachedThreadPool.submit((Callable<Object>)new BasisGenerationTask()));
            --b;
        }
        cachedThreadPool.shutdown();
        final ArrayList<NTRUSigningPrivateKeyParameters.Basis> list2 = new ArrayList<NTRUSigningPrivateKeyParameters.Basis>();
        int i = this.params.B;
        while (i >= 0) {
            final Future<Object> future = list.get(i);
            try {
                list2.add(future.get());
                if (i == this.params.B) {
                    ntruSigningPublicKeyParameters = new NTRUSigningPublicKeyParameters(future.get().h, this.params.getSigningParameters());
                }
                --i;
                continue;
            }
            catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
            break;
        }
        return new AsymmetricCipherKeyPair(ntruSigningPublicKeyParameters, new NTRUSigningPrivateKeyParameters(list2, ntruSigningPublicKeyParameters));
    }
    
    public AsymmetricCipherKeyPair generateKeyPairSingleThread() {
        final ArrayList<NTRUSigningPrivateKeyParameters.Basis> list = new ArrayList<NTRUSigningPrivateKeyParameters.Basis>();
        int i = this.params.B;
        NTRUSigningPublicKeyParameters ntruSigningPublicKeyParameters = null;
        while (i >= 0) {
            final NTRUSigningPrivateKeyParameters.Basis generateBoundedBasis = this.generateBoundedBasis();
            list.add(generateBoundedBasis);
            if (i == 0) {
                ntruSigningPublicKeyParameters = new NTRUSigningPublicKeyParameters(generateBoundedBasis.h, this.params.getSigningParameters());
            }
            --i;
        }
        return new AsymmetricCipherKeyPair(ntruSigningPublicKeyParameters, new NTRUSigningPrivateKeyParameters(list, ntruSigningPublicKeyParameters));
    }
    
    @Override
    public void init(final KeyGenerationParameters keyGenerationParameters) {
        this.params = (NTRUSigningKeyGenerationParameters)keyGenerationParameters;
    }
    
    private class BasisGenerationTask implements Callable<NTRUSigningPrivateKeyParameters.Basis>
    {
        @Override
        public NTRUSigningPrivateKeyParameters.Basis call() throws Exception {
            return NTRUSigningKeyPairGenerator.this.generateBoundedBasis();
        }
    }
    
    public class FGBasis extends Basis
    {
        public IntegerPolynomial F;
        public IntegerPolynomial G;
        
        FGBasis(final Polynomial polynomial, final Polynomial polynomial2, final IntegerPolynomial integerPolynomial, final IntegerPolynomial f, final IntegerPolynomial g, final NTRUSigningKeyGenerationParameters ntruSigningKeyGenerationParameters) {
            super(polynomial, polynomial2, integerPolynomial, ntruSigningKeyGenerationParameters);
            this.F = f;
            this.G = g;
        }
        
        boolean isNormOk() {
            final double keyNormBoundSq = this.params.keyNormBoundSq;
            final int q = this.params.q;
            return this.F.centeredNormSq(q) < keyNormBoundSq && this.G.centeredNormSq(q) < keyNormBoundSq;
        }
    }
}
