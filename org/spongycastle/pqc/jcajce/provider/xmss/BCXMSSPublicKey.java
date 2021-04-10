package org.spongycastle.pqc.jcajce.provider.xmss;

import java.security.*;
import org.spongycastle.pqc.jcajce.interfaces.*;
import org.spongycastle.pqc.crypto.xmss.*;
import java.io.*;
import org.spongycastle.util.*;
import org.spongycastle.pqc.asn1.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;
import org.spongycastle.crypto.*;

public class BCXMSSPublicKey implements PublicKey, XMSSKey
{
    private final XMSSPublicKeyParameters keyParams;
    private final ASN1ObjectIdentifier treeDigest;
    
    public BCXMSSPublicKey(final ASN1ObjectIdentifier treeDigest, final XMSSPublicKeyParameters keyParams) {
        this.treeDigest = treeDigest;
        this.keyParams = keyParams;
    }
    
    public BCXMSSPublicKey(final SubjectPublicKeyInfo subjectPublicKeyInfo) throws IOException {
        final XMSSKeyParams instance = XMSSKeyParams.getInstance(subjectPublicKeyInfo.getAlgorithm().getParameters());
        this.treeDigest = instance.getTreeDigest().getAlgorithm();
        final XMSSPublicKey instance2 = XMSSPublicKey.getInstance(subjectPublicKeyInfo.parsePublicKey());
        this.keyParams = new XMSSPublicKeyParameters.Builder(new XMSSParameters(instance.getHeight(), DigestUtil.getDigest(this.treeDigest))).withPublicSeed(instance2.getPublicSeed()).withRoot(instance2.getRoot()).build();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof BCXMSSPublicKey) {
            final BCXMSSPublicKey bcxmssPublicKey = (BCXMSSPublicKey)o;
            if (this.treeDigest.equals(bcxmssPublicKey.treeDigest) && Arrays.areEqual(this.keyParams.toByteArray(), bcxmssPublicKey.keyParams.toByteArray())) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public final String getAlgorithm() {
        return "XMSS";
    }
    
    @Override
    public byte[] getEncoded() {
        try {
            return new SubjectPublicKeyInfo(new AlgorithmIdentifier(PQCObjectIdentifiers.xmss, new XMSSKeyParams(this.keyParams.getParameters().getHeight(), new AlgorithmIdentifier(this.treeDigest))), new XMSSPublicKey(this.keyParams.getPublicSeed(), this.keyParams.getRoot())).getEncoded();
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
    public int getHeight() {
        return this.keyParams.getParameters().getHeight();
    }
    
    CipherParameters getKeyParams() {
        return this.keyParams;
    }
    
    @Override
    public String getTreeDigest() {
        return DigestUtil.getXMSSDigestName(this.treeDigest);
    }
    
    @Override
    public int hashCode() {
        return this.treeDigest.hashCode() + Arrays.hashCode(this.keyParams.toByteArray()) * 37;
    }
}
