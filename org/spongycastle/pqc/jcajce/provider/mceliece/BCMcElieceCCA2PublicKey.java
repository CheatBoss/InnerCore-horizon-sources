package org.spongycastle.pqc.jcajce.provider.mceliece;

import java.security.*;
import org.spongycastle.crypto.*;
import org.spongycastle.pqc.crypto.mceliece.*;
import org.spongycastle.pqc.asn1.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;
import java.io.*;
import org.spongycastle.pqc.math.linearalgebra.*;
import org.spongycastle.crypto.params.*;

public class BCMcElieceCCA2PublicKey implements PublicKey, CipherParameters
{
    private static final long serialVersionUID = 1L;
    private McElieceCCA2PublicKeyParameters params;
    
    public BCMcElieceCCA2PublicKey(final McElieceCCA2PublicKeyParameters params) {
        this.params = params;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b2;
        final boolean b = b2 = false;
        if (o != null) {
            if (!(o instanceof BCMcElieceCCA2PublicKey)) {
                return false;
            }
            final BCMcElieceCCA2PublicKey bcMcElieceCCA2PublicKey = (BCMcElieceCCA2PublicKey)o;
            b2 = b;
            if (this.params.getN() == bcMcElieceCCA2PublicKey.getN()) {
                b2 = b;
                if (this.params.getT() == bcMcElieceCCA2PublicKey.getT()) {
                    b2 = b;
                    if (this.params.getG().equals(bcMcElieceCCA2PublicKey.getG())) {
                        b2 = true;
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
        final McElieceCCA2PublicKey mcElieceCCA2PublicKey = new McElieceCCA2PublicKey(this.params.getN(), this.params.getT(), this.params.getG(), Utils.getDigAlgId(this.params.getDigest()));
        final AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PQCObjectIdentifiers.mcElieceCca2);
        try {
            return new SubjectPublicKeyInfo(algorithmIdentifier, mcElieceCCA2PublicKey).getEncoded();
        }
        catch (IOException ex) {
            return null;
        }
    }
    
    @Override
    public String getFormat() {
        return "X.509";
    }
    
    public GF2Matrix getG() {
        return this.params.getG();
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
    
    public int getT() {
        return this.params.getT();
    }
    
    @Override
    public int hashCode() {
        return (this.params.getN() + this.params.getT() * 37) * 37 + this.params.getG().hashCode();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("McEliecePublicKey:\n");
        sb.append(" length of the code         : ");
        sb.append(this.params.getN());
        sb.append("\n");
        final String string = sb.toString();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(string);
        sb2.append(" error correction capability: ");
        sb2.append(this.params.getT());
        sb2.append("\n");
        final String string2 = sb2.toString();
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(string2);
        sb3.append(" generator matrix           : ");
        sb3.append(this.params.getG().toString());
        return sb3.toString();
    }
}
