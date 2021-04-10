package org.spongycastle.pqc.crypto.mceliece;

import java.security.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.pqc.math.linearalgebra.*;

public class McElieceCCA2KeyPairGenerator implements AsymmetricCipherKeyPairGenerator
{
    public static final String OID = "1.3.6.1.4.1.8301.3.1.3.4.2";
    private int fieldPoly;
    private boolean initialized;
    private int m;
    private McElieceCCA2KeyGenerationParameters mcElieceCCA2Params;
    private int n;
    private SecureRandom random;
    private int t;
    
    public McElieceCCA2KeyPairGenerator() {
        this.initialized = false;
    }
    
    private void initializeDefault() {
        this.init(new McElieceCCA2KeyGenerationParameters(new SecureRandom(), new McElieceCCA2Parameters()));
    }
    
    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        if (!this.initialized) {
            this.initializeDefault();
        }
        final GF2mField gf2mField = new GF2mField(this.m, this.fieldPoly);
        final PolynomialGF2mSmallM polynomialGF2mSmallM = new PolynomialGF2mSmallM(gf2mField, this.t, 'I', this.random);
        final GoppaCode.MaMaPe computeSystematicForm = GoppaCode.computeSystematicForm(GoppaCode.createCanonicalCheckMatrix(gf2mField, polynomialGF2mSmallM), this.random);
        final GF2Matrix secondMatrix = computeSystematicForm.getSecondMatrix();
        final Permutation permutation = computeSystematicForm.getPermutation();
        final GF2Matrix gf2Matrix = (GF2Matrix)secondMatrix.computeTranspose();
        return new AsymmetricCipherKeyPair(new McElieceCCA2PublicKeyParameters(this.n, this.t, gf2Matrix, this.mcElieceCCA2Params.getParameters().getDigest()), new McElieceCCA2PrivateKeyParameters(this.n, gf2Matrix.getNumRows(), gf2mField, polynomialGF2mSmallM, permutation, this.mcElieceCCA2Params.getParameters().getDigest()));
    }
    
    @Override
    public void init(final KeyGenerationParameters keyGenerationParameters) {
        this.mcElieceCCA2Params = (McElieceCCA2KeyGenerationParameters)keyGenerationParameters;
        this.random = new SecureRandom();
        this.m = this.mcElieceCCA2Params.getParameters().getM();
        this.n = this.mcElieceCCA2Params.getParameters().getN();
        this.t = this.mcElieceCCA2Params.getParameters().getT();
        this.fieldPoly = this.mcElieceCCA2Params.getParameters().getFieldPoly();
        this.initialized = true;
    }
}
