package org.spongycastle.pqc.crypto.mceliece;

import java.security.*;
import org.spongycastle.pqc.math.linearalgebra.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class McElieceKeyPairGenerator implements AsymmetricCipherKeyPairGenerator
{
    private static final String OID = "1.3.6.1.4.1.8301.3.1.3.4.1";
    private int fieldPoly;
    private boolean initialized;
    private int m;
    private McElieceKeyGenerationParameters mcElieceParams;
    private int n;
    private SecureRandom random;
    private int t;
    
    public McElieceKeyPairGenerator() {
        this.initialized = false;
    }
    
    private AsymmetricCipherKeyPair genKeyPair() {
        if (!this.initialized) {
            this.initializeDefault();
        }
        final GF2mField gf2mField = new GF2mField(this.m, this.fieldPoly);
        final PolynomialGF2mSmallM polynomialGF2mSmallM = new PolynomialGF2mSmallM(gf2mField, this.t, 'I', this.random);
        new PolynomialRingGF2m(gf2mField, polynomialGF2mSmallM).getSquareRootMatrix();
        final GoppaCode.MaMaPe computeSystematicForm = GoppaCode.computeSystematicForm(GoppaCode.createCanonicalCheckMatrix(gf2mField, polynomialGF2mSmallM), this.random);
        final GF2Matrix secondMatrix = computeSystematicForm.getSecondMatrix();
        final Permutation permutation = computeSystematicForm.getPermutation();
        final GF2Matrix gf2Matrix = (GF2Matrix)secondMatrix.computeTranspose();
        final GF2Matrix extendLeftCompactForm = gf2Matrix.extendLeftCompactForm();
        final int numRows = gf2Matrix.getNumRows();
        final GF2Matrix[] randomRegularMatrixAndItsInverse = GF2Matrix.createRandomRegularMatrixAndItsInverse(numRows, this.random);
        final Permutation permutation2 = new Permutation(this.n, this.random);
        return new AsymmetricCipherKeyPair(new McEliecePublicKeyParameters(this.n, this.t, (GF2Matrix)((GF2Matrix)randomRegularMatrixAndItsInverse[0].rightMultiply(extendLeftCompactForm)).rightMultiply(permutation2)), new McEliecePrivateKeyParameters(this.n, numRows, gf2mField, polynomialGF2mSmallM, permutation, permutation2, randomRegularMatrixAndItsInverse[1]));
    }
    
    private void initialize(final KeyGenerationParameters keyGenerationParameters) {
        this.mcElieceParams = (McElieceKeyGenerationParameters)keyGenerationParameters;
        this.random = new SecureRandom();
        this.m = this.mcElieceParams.getParameters().getM();
        this.n = this.mcElieceParams.getParameters().getN();
        this.t = this.mcElieceParams.getParameters().getT();
        this.fieldPoly = this.mcElieceParams.getParameters().getFieldPoly();
        this.initialized = true;
    }
    
    private void initializeDefault() {
        this.initialize(new McElieceKeyGenerationParameters(new SecureRandom(), new McElieceParameters()));
    }
    
    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        return this.genKeyPair();
    }
    
    @Override
    public void init(final KeyGenerationParameters keyGenerationParameters) {
        this.initialize(keyGenerationParameters);
    }
}
