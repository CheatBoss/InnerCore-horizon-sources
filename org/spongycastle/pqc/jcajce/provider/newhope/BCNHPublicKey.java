package org.spongycastle.pqc.jcajce.provider.newhope;

import org.spongycastle.pqc.jcajce.interfaces.*;
import org.spongycastle.pqc.crypto.newhope.*;
import org.spongycastle.util.*;
import org.spongycastle.pqc.asn1.*;
import org.spongycastle.asn1.x509.*;
import java.io.*;
import org.spongycastle.crypto.*;

public class BCNHPublicKey implements NHPublicKey
{
    private static final long serialVersionUID = 1L;
    private final NHPublicKeyParameters params;
    
    public BCNHPublicKey(final SubjectPublicKeyInfo subjectPublicKeyInfo) {
        this.params = new NHPublicKeyParameters(subjectPublicKeyInfo.getPublicKeyData().getBytes());
    }
    
    public BCNHPublicKey(final NHPublicKeyParameters params) {
        this.params = params;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o != null && o instanceof BCNHPublicKey && Arrays.areEqual(this.params.getPubData(), ((BCNHPublicKey)o).params.getPubData());
    }
    
    @Override
    public final String getAlgorithm() {
        return "NH";
    }
    
    @Override
    public byte[] getEncoded() {
        try {
            return new SubjectPublicKeyInfo(new AlgorithmIdentifier(PQCObjectIdentifiers.newHope), this.params.getPubData()).getEncoded();
        }
        catch (IOException ex) {
            return null;
        }
    }
    
    @Override
    public String getFormat() {
        return "X.509";
    }
    
    CipherParameters getKeyParams() {
        return this.params;
    }
    
    @Override
    public byte[] getPublicData() {
        return this.params.getPubData();
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.params.getPubData());
    }
}
