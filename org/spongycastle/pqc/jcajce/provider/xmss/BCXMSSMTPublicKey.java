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

public class BCXMSSMTPublicKey implements PublicKey, XMSSMTKey
{
    private final XMSSMTPublicKeyParameters keyParams;
    private final ASN1ObjectIdentifier treeDigest;
    
    public BCXMSSMTPublicKey(final ASN1ObjectIdentifier treeDigest, final XMSSMTPublicKeyParameters keyParams) {
        this.treeDigest = treeDigest;
        this.keyParams = keyParams;
    }
    
    public BCXMSSMTPublicKey(final SubjectPublicKeyInfo subjectPublicKeyInfo) throws IOException {
        final XMSSMTKeyParams instance = XMSSMTKeyParams.getInstance(subjectPublicKeyInfo.getAlgorithm().getParameters());
        this.treeDigest = instance.getTreeDigest().getAlgorithm();
        final XMSSPublicKey instance2 = XMSSPublicKey.getInstance(subjectPublicKeyInfo.parsePublicKey());
        this.keyParams = new XMSSMTPublicKeyParameters.Builder(new XMSSMTParameters(instance.getHeight(), instance.getLayers(), DigestUtil.getDigest(this.treeDigest))).withPublicSeed(instance2.getPublicSeed()).withRoot(instance2.getRoot()).build();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof BCXMSSMTPublicKey) {
            final BCXMSSMTPublicKey bcxmssmtPublicKey = (BCXMSSMTPublicKey)o;
            if (this.treeDigest.equals(bcxmssmtPublicKey.treeDigest) && Arrays.areEqual(this.keyParams.toByteArray(), bcxmssmtPublicKey.keyParams.toByteArray())) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public final String getAlgorithm() {
        return "XMSSMT";
    }
    
    @Override
    public byte[] getEncoded() {
        try {
            return new SubjectPublicKeyInfo(new AlgorithmIdentifier(PQCObjectIdentifiers.xmss_mt, new XMSSMTKeyParams(this.keyParams.getParameters().getHeight(), this.keyParams.getParameters().getLayers(), new AlgorithmIdentifier(this.treeDigest))), new XMSSPublicKey(this.keyParams.getPublicSeed(), this.keyParams.getRoot())).getEncoded();
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
    public int getLayers() {
        return this.keyParams.getParameters().getLayers();
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
