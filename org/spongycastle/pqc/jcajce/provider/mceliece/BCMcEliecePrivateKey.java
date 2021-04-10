package org.spongycastle.pqc.jcajce.provider.mceliece;

import java.security.*;
import org.spongycastle.crypto.*;
import org.spongycastle.pqc.crypto.mceliece.*;
import org.spongycastle.pqc.asn1.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.asn1.*;
import java.io.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.pqc.math.linearalgebra.*;

public class BCMcEliecePrivateKey implements PrivateKey, CipherParameters
{
    private static final long serialVersionUID = 1L;
    private McEliecePrivateKeyParameters params;
    
    public BCMcEliecePrivateKey(final McEliecePrivateKeyParameters params) {
        this.params = params;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof BCMcEliecePrivateKey;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final BCMcEliecePrivateKey bcMcEliecePrivateKey = (BCMcEliecePrivateKey)o;
        boolean b3 = b2;
        if (this.getN() == bcMcEliecePrivateKey.getN()) {
            b3 = b2;
            if (this.getK() == bcMcEliecePrivateKey.getK()) {
                b3 = b2;
                if (this.getField().equals(bcMcEliecePrivateKey.getField())) {
                    b3 = b2;
                    if (this.getGoppaPoly().equals(bcMcEliecePrivateKey.getGoppaPoly())) {
                        b3 = b2;
                        if (this.getSInv().equals(bcMcEliecePrivateKey.getSInv())) {
                            b3 = b2;
                            if (this.getP1().equals(bcMcEliecePrivateKey.getP1())) {
                                b3 = b2;
                                if (this.getP2().equals(bcMcEliecePrivateKey.getP2())) {
                                    b3 = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return b3;
    }
    
    @Override
    public String getAlgorithm() {
        return "McEliece";
    }
    
    @Override
    public byte[] getEncoded() {
        final McEliecePrivateKey mcEliecePrivateKey = new McEliecePrivateKey(this.params.getN(), this.params.getK(), this.params.getField(), this.params.getGoppaPoly(), this.params.getP1(), this.params.getP2(), this.params.getSInv());
        try {
            return new PrivateKeyInfo(new AlgorithmIdentifier(PQCObjectIdentifiers.mcEliece), mcEliecePrivateKey).getEncoded();
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
    
    public Permutation getP1() {
        return this.params.getP1();
    }
    
    public Permutation getP2() {
        return this.params.getP2();
    }
    
    public PolynomialGF2mSmallM[] getQInv() {
        return this.params.getQInv();
    }
    
    public GF2Matrix getSInv() {
        return this.params.getSInv();
    }
    
    @Override
    public int hashCode() {
        return (((((this.params.getK() * 37 + this.params.getN()) * 37 + this.params.getField().hashCode()) * 37 + this.params.getGoppaPoly().hashCode()) * 37 + this.params.getP1().hashCode()) * 37 + this.params.getP2().hashCode()) * 37 + this.params.getSInv().hashCode();
    }
}
