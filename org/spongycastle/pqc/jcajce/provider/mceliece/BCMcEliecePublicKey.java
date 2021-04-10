package org.spongycastle.pqc.jcajce.provider.mceliece;

import java.security.*;
import org.spongycastle.pqc.crypto.mceliece.*;
import org.spongycastle.pqc.asn1.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;
import java.io.*;
import org.spongycastle.pqc.math.linearalgebra.*;
import org.spongycastle.crypto.params.*;

public class BCMcEliecePublicKey implements PublicKey
{
    private static final long serialVersionUID = 1L;
    private McEliecePublicKeyParameters params;
    
    public BCMcEliecePublicKey(final McEliecePublicKeyParameters params) {
        this.params = params;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o instanceof BCMcEliecePublicKey) {
            final BCMcEliecePublicKey bcMcEliecePublicKey = (BCMcEliecePublicKey)o;
            if (this.params.getN() == bcMcEliecePublicKey.getN() && this.params.getT() == bcMcEliecePublicKey.getT() && this.params.getG().equals(bcMcEliecePublicKey.getG())) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String getAlgorithm() {
        return "McEliece";
    }
    
    @Override
    public byte[] getEncoded() {
        final McEliecePublicKey mcEliecePublicKey = new McEliecePublicKey(this.params.getN(), this.params.getT(), this.params.getG());
        final AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PQCObjectIdentifiers.mcEliece);
        try {
            return new SubjectPublicKeyInfo(algorithmIdentifier, mcEliecePublicKey).getEncoded();
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
        sb3.append(this.params.getG());
        return sb3.toString();
    }
}
