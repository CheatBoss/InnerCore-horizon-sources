package org.spongycastle.pqc.jcajce.provider.sphincs;

import java.security.*;
import org.spongycastle.pqc.jcajce.interfaces.*;
import org.spongycastle.pqc.crypto.sphincs.*;
import org.spongycastle.util.*;
import org.spongycastle.pqc.asn1.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;
import java.io.*;
import org.spongycastle.crypto.*;

public class BCSphincs256PublicKey implements PublicKey, SPHINCSKey
{
    private static final long serialVersionUID = 1L;
    private final SPHINCSPublicKeyParameters params;
    private final ASN1ObjectIdentifier treeDigest;
    
    public BCSphincs256PublicKey(final ASN1ObjectIdentifier treeDigest, final SPHINCSPublicKeyParameters params) {
        this.treeDigest = treeDigest;
        this.params = params;
    }
    
    public BCSphincs256PublicKey(final SubjectPublicKeyInfo subjectPublicKeyInfo) {
        this.treeDigest = SPHINCS256KeyParams.getInstance(subjectPublicKeyInfo.getAlgorithm().getParameters()).getTreeDigest().getAlgorithm();
        this.params = new SPHINCSPublicKeyParameters(subjectPublicKeyInfo.getPublicKeyData().getBytes());
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof BCSphincs256PublicKey) {
            final BCSphincs256PublicKey bcSphincs256PublicKey = (BCSphincs256PublicKey)o;
            if (this.treeDigest.equals(bcSphincs256PublicKey.treeDigest) && Arrays.areEqual(this.params.getKeyData(), bcSphincs256PublicKey.params.getKeyData())) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public final String getAlgorithm() {
        return "SPHINCS-256";
    }
    
    @Override
    public byte[] getEncoded() {
        try {
            return new SubjectPublicKeyInfo(new AlgorithmIdentifier(PQCObjectIdentifiers.sphincs256, new SPHINCS256KeyParams(new AlgorithmIdentifier(this.treeDigest))), this.params.getKeyData()).getEncoded();
        }
        catch (IOException ex) {
            return null;
        }
    }
    
    @Override
    public String getFormat() {
        return "X.509";
    }
    
    @Override
    public byte[] getKeyData() {
        return this.params.getKeyData();
    }
    
    CipherParameters getKeyParams() {
        return this.params;
    }
    
    @Override
    public int hashCode() {
        return this.treeDigest.hashCode() + Arrays.hashCode(this.params.getKeyData()) * 37;
    }
}
