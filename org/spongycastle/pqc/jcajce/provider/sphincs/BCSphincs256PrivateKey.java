package org.spongycastle.pqc.jcajce.provider.sphincs;

import java.security.*;
import org.spongycastle.pqc.jcajce.interfaces.*;
import org.spongycastle.pqc.crypto.sphincs.*;
import org.spongycastle.asn1.pkcs.*;
import java.io.*;
import org.spongycastle.util.*;
import org.spongycastle.pqc.asn1.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;
import org.spongycastle.crypto.*;

public class BCSphincs256PrivateKey implements PrivateKey, SPHINCSKey
{
    private static final long serialVersionUID = 1L;
    private final SPHINCSPrivateKeyParameters params;
    private final ASN1ObjectIdentifier treeDigest;
    
    public BCSphincs256PrivateKey(final ASN1ObjectIdentifier treeDigest, final SPHINCSPrivateKeyParameters params) {
        this.treeDigest = treeDigest;
        this.params = params;
    }
    
    public BCSphincs256PrivateKey(final PrivateKeyInfo privateKeyInfo) throws IOException {
        this.treeDigest = SPHINCS256KeyParams.getInstance(privateKeyInfo.getPrivateKeyAlgorithm().getParameters()).getTreeDigest().getAlgorithm();
        this.params = new SPHINCSPrivateKeyParameters(ASN1OctetString.getInstance(privateKeyInfo.parsePrivateKey()).getOctets());
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof BCSphincs256PrivateKey) {
            final BCSphincs256PrivateKey bcSphincs256PrivateKey = (BCSphincs256PrivateKey)o;
            if (this.treeDigest.equals(bcSphincs256PrivateKey.treeDigest) && Arrays.areEqual(this.params.getKeyData(), bcSphincs256PrivateKey.params.getKeyData())) {
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
            return new PrivateKeyInfo(new AlgorithmIdentifier(PQCObjectIdentifiers.sphincs256, new SPHINCS256KeyParams(new AlgorithmIdentifier(this.treeDigest))), new DEROctetString(this.params.getKeyData())).getEncoded();
        }
        catch (IOException ex) {
            return null;
        }
    }
    
    @Override
    public String getFormat() {
        return "PKCS#8";
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
