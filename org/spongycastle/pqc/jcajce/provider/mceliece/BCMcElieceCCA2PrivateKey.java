package org.spongycastle.pqc.jcajce.provider.mceliece;

import java.security.*;
import org.spongycastle.pqc.crypto.mceliece.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.pqc.asn1.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.asn1.*;
import java.io.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.pqc.math.linearalgebra.*;

public class BCMcElieceCCA2PrivateKey implements PrivateKey
{
    private static final long serialVersionUID = 1L;
    private McElieceCCA2PrivateKeyParameters params;
    
    public BCMcElieceCCA2PrivateKey(final McElieceCCA2PrivateKeyParameters params) {
        this.params = params;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b2;
        final boolean b = b2 = false;
        if (o != null) {
            if (!(o instanceof BCMcElieceCCA2PrivateKey)) {
                return false;
            }
            final BCMcElieceCCA2PrivateKey bcMcElieceCCA2PrivateKey = (BCMcElieceCCA2PrivateKey)o;
            b2 = b;
            if (this.getN() == bcMcElieceCCA2PrivateKey.getN()) {
                b2 = b;
                if (this.getK() == bcMcElieceCCA2PrivateKey.getK()) {
                    b2 = b;
                    if (this.getField().equals(bcMcElieceCCA2PrivateKey.getField())) {
                        b2 = b;
                        if (this.getGoppaPoly().equals(bcMcElieceCCA2PrivateKey.getGoppaPoly())) {
                            b2 = b;
                            if (this.getP().equals(bcMcElieceCCA2PrivateKey.getP())) {
                                b2 = b;
                                if (this.getH().equals(bcMcElieceCCA2PrivateKey.getH())) {
                                    b2 = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return b2;
    }
    
    @Override
    public String getAlgorithm() {
        return "McEliece-CCA2";
    }
    
    @Override
    public byte[] getEncoded() {
        try {
            return new PrivateKeyInfo(new AlgorithmIdentifier(PQCObjectIdentifiers.mcElieceCca2), new McElieceCCA2PrivateKey(this.getN(), this.getK(), this.getField(), this.getGoppaPoly(), this.getP(), Utils.getDigAlgId(this.params.getDigest()))).getEncoded();
        }
        catch (IOException ex) {
            return null;
        }
    }
    
    public GF2mField getField() {
        return this.params.getField();
    }
    
    @Override
    public String getFormat() {
        return "PKCS#8";
    }
    
    public PolynomialGF2mSmallM getGoppaPoly() {
        return this.params.getGoppaPoly();
    }
    
    public GF2Matrix getH() {
        return this.params.getH();
    }
    
    public int getK() {
        return this.params.getK();
    }
    
    AsymmetricKeyParameter getKeyParams() {
        return this.params;
    }
    
    public int getN() {
        return this.params.getN();
    }
    
    public Permutation getP() {
        return this.params.getP();
    }
    
    public PolynomialGF2mSmallM[] getQInv() {
        return this.params.getQInv();
    }
    
    public int getT() {
        return this.params.getGoppaPoly().getDegree();
    }
    
    @Override
    public int hashCode() {
        return ((((this.params.getK() * 37 + this.params.getN()) * 37 + this.params.getField().hashCode()) * 37 + this.params.getGoppaPoly().hashCode()) * 37 + this.params.getP().hashCode()) * 37 + this.params.getH().hashCode();
    }
}
