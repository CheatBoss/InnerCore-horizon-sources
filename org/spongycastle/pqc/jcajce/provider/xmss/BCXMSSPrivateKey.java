package org.spongycastle.pqc.jcajce.provider.xmss;

import java.security.*;
import org.spongycastle.pqc.jcajce.interfaces.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.pqc.crypto.xmss.*;
import java.io.*;
import org.spongycastle.util.*;
import org.spongycastle.pqc.asn1.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;
import org.spongycastle.crypto.*;

public class BCXMSSPrivateKey implements PrivateKey, XMSSKey
{
    private final XMSSPrivateKeyParameters keyParams;
    private final ASN1ObjectIdentifier treeDigest;
    
    public BCXMSSPrivateKey(final ASN1ObjectIdentifier treeDigest, final XMSSPrivateKeyParameters keyParams) {
        this.treeDigest = treeDigest;
        this.keyParams = keyParams;
    }
    
    public BCXMSSPrivateKey(final PrivateKeyInfo privateKeyInfo) throws IOException {
        final XMSSKeyParams instance = XMSSKeyParams.getInstance(privateKeyInfo.getPrivateKeyAlgorithm().getParameters());
        this.treeDigest = instance.getTreeDigest().getAlgorithm();
        final XMSSPrivateKey instance2 = XMSSPrivateKey.getInstance(privateKeyInfo.parsePrivateKey());
        try {
            final XMSSPrivateKeyParameters.Builder withRoot = new XMSSPrivateKeyParameters.Builder(new XMSSParameters(instance.getHeight(), DigestUtil.getDigest(this.treeDigest))).withIndex(instance2.getIndex()).withSecretKeySeed(instance2.getSecretKeySeed()).withSecretKeyPRF(instance2.getSecretKeyPRF()).withPublicSeed(instance2.getPublicSeed()).withRoot(instance2.getRoot());
            if (instance2.getBdsState() != null) {
                withRoot.withBDSState((BDS)XMSSUtil.deserialize(instance2.getBdsState()));
            }
            this.keyParams = withRoot.build();
        }
        catch (ClassNotFoundException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("ClassNotFoundException processing BDS state: ");
            sb.append(ex.getMessage());
            throw new IOException(sb.toString());
        }
    }
    
    private XMSSPrivateKey createKeyStructure() {
        final byte[] byteArray = this.keyParams.toByteArray();
        final int digestSize = this.keyParams.getParameters().getDigestSize();
        final int height = this.keyParams.getParameters().getHeight();
        final int n = (int)XMSSUtil.bytesToXBigEndian(byteArray, 0, 4);
        if (XMSSUtil.isIndexValid(height, n)) {
            final byte[] bytesAtOffset = XMSSUtil.extractBytesAtOffset(byteArray, 4, digestSize);
            final int n2 = digestSize + 4;
            final byte[] bytesAtOffset2 = XMSSUtil.extractBytesAtOffset(byteArray, n2, digestSize);
            final int n3 = n2 + digestSize;
            final byte[] bytesAtOffset3 = XMSSUtil.extractBytesAtOffset(byteArray, n3, digestSize);
            final int n4 = n3 + digestSize;
            final byte[] bytesAtOffset4 = XMSSUtil.extractBytesAtOffset(byteArray, n4, digestSize);
            final int n5 = n4 + digestSize;
            return new XMSSPrivateKey(n, bytesAtOffset, bytesAtOffset2, bytesAtOffset3, bytesAtOffset4, XMSSUtil.extractBytesAtOffset(byteArray, n5, byteArray.length - n5));
        }
        throw new IllegalArgumentException("index out of bounds");
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof BCXMSSPrivateKey) {
            final BCXMSSPrivateKey bcxmssPrivateKey = (BCXMSSPrivateKey)o;
            if (this.treeDigest.equals(bcxmssPrivateKey.treeDigest) && Arrays.areEqual(this.keyParams.toByteArray(), bcxmssPrivateKey.keyParams.toByteArray())) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String getAlgorithm() {
        return "XMSS";
    }
    
    @Override
    public byte[] getEncoded() {
        try {
            return new PrivateKeyInfo(new AlgorithmIdentifier(PQCObjectIdentifiers.xmss, new XMSSKeyParams(this.keyParams.getParameters().getHeight(), new AlgorithmIdentifier(this.treeDigest))), this.createKeyStructure()).getEncoded();
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
    
    ASN1ObjectIdentifier getTreeDigestOID() {
        return this.treeDigest;
    }
    
    @Override
    public int hashCode() {
        return this.treeDigest.hashCode() + Arrays.hashCode(this.keyParams.toByteArray()) * 37;
    }
}
