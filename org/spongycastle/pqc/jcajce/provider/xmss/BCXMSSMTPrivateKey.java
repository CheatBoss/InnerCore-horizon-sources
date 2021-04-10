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

public class BCXMSSMTPrivateKey implements PrivateKey, XMSSMTKey
{
    private final XMSSMTPrivateKeyParameters keyParams;
    private final ASN1ObjectIdentifier treeDigest;
    
    public BCXMSSMTPrivateKey(final ASN1ObjectIdentifier treeDigest, final XMSSMTPrivateKeyParameters keyParams) {
        this.treeDigest = treeDigest;
        this.keyParams = keyParams;
    }
    
    public BCXMSSMTPrivateKey(final PrivateKeyInfo privateKeyInfo) throws IOException {
        final XMSSMTKeyParams instance = XMSSMTKeyParams.getInstance(privateKeyInfo.getPrivateKeyAlgorithm().getParameters());
        this.treeDigest = instance.getTreeDigest().getAlgorithm();
        final XMSSPrivateKey instance2 = XMSSPrivateKey.getInstance(privateKeyInfo.parsePrivateKey());
        try {
            final XMSSMTPrivateKeyParameters.Builder withRoot = new XMSSMTPrivateKeyParameters.Builder(new XMSSMTParameters(instance.getHeight(), instance.getLayers(), DigestUtil.getDigest(this.treeDigest))).withIndex(instance2.getIndex()).withSecretKeySeed(instance2.getSecretKeySeed()).withSecretKeyPRF(instance2.getSecretKeyPRF()).withPublicSeed(instance2.getPublicSeed()).withRoot(instance2.getRoot());
            if (instance2.getBdsState() != null) {
                withRoot.withBDSState((BDSStateMap)XMSSUtil.deserialize(instance2.getBdsState()));
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
    
    private XMSSMTPrivateKey createKeyStructure() {
        final byte[] byteArray = this.keyParams.toByteArray();
        final int digestSize = this.keyParams.getParameters().getDigestSize();
        final int height = this.keyParams.getParameters().getHeight();
        final int n = (height + 7) / 8;
        final int n2 = (int)XMSSUtil.bytesToXBigEndian(byteArray, 0, n);
        if (XMSSUtil.isIndexValid(height, n2)) {
            final int n3 = n + 0;
            final byte[] bytesAtOffset = XMSSUtil.extractBytesAtOffset(byteArray, n3, digestSize);
            final int n4 = n3 + digestSize;
            final byte[] bytesAtOffset2 = XMSSUtil.extractBytesAtOffset(byteArray, n4, digestSize);
            final int n5 = n4 + digestSize;
            final byte[] bytesAtOffset3 = XMSSUtil.extractBytesAtOffset(byteArray, n5, digestSize);
            final int n6 = n5 + digestSize;
            final byte[] bytesAtOffset4 = XMSSUtil.extractBytesAtOffset(byteArray, n6, digestSize);
            final int n7 = n6 + digestSize;
            return new XMSSMTPrivateKey(n2, bytesAtOffset, bytesAtOffset2, bytesAtOffset3, bytesAtOffset4, XMSSUtil.extractBytesAtOffset(byteArray, n7, byteArray.length - n7));
        }
        throw new IllegalArgumentException("index out of bounds");
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof BCXMSSMTPrivateKey) {
            final BCXMSSMTPrivateKey bcxmssmtPrivateKey = (BCXMSSMTPrivateKey)o;
            if (this.treeDigest.equals(bcxmssmtPrivateKey.treeDigest) && Arrays.areEqual(this.keyParams.toByteArray(), bcxmssmtPrivateKey.keyParams.toByteArray())) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String getAlgorithm() {
        return "XMSSMT";
    }
    
    @Override
    public byte[] getEncoded() {
        try {
            return new PrivateKeyInfo(new AlgorithmIdentifier(PQCObjectIdentifiers.xmss_mt, new XMSSMTKeyParams(this.keyParams.getParameters().getHeight(), this.keyParams.getParameters().getLayers(), new AlgorithmIdentifier(this.treeDigest))), this.createKeyStructure()).getEncoded();
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
    public int getLayers() {
        return this.keyParams.getParameters().getLayers();
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
