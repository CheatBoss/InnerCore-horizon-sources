package org.spongycastle.pqc.asn1;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class XMSSMTKeyParams extends ASN1Object
{
    private final int height;
    private final int layers;
    private final AlgorithmIdentifier treeDigest;
    private final ASN1Integer version;
    
    public XMSSMTKeyParams(final int height, final int layers, final AlgorithmIdentifier treeDigest) {
        this.version = new ASN1Integer(0L);
        this.height = height;
        this.layers = layers;
        this.treeDigest = treeDigest;
    }
    
    private XMSSMTKeyParams(final ASN1Sequence asn1Sequence) {
        this.version = ASN1Integer.getInstance(asn1Sequence.getObjectAt(0));
        this.height = ASN1Integer.getInstance(asn1Sequence.getObjectAt(1)).getValue().intValue();
        this.layers = ASN1Integer.getInstance(asn1Sequence.getObjectAt(2)).getValue().intValue();
        this.treeDigest = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(3));
    }
    
    public static XMSSMTKeyParams getInstance(final Object o) {
        if (o instanceof XMSSMTKeyParams) {
            return (XMSSMTKeyParams)o;
        }
        if (o != null) {
            return new XMSSMTKeyParams(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public int getLayers() {
        return this.layers;
    }
    
    public AlgorithmIdentifier getTreeDigest() {
        return this.treeDigest;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.version);
        asn1EncodableVector.add(new ASN1Integer(this.height));
        asn1EncodableVector.add(new ASN1Integer(this.layers));
        asn1EncodableVector.add(this.treeDigest);
        return new DERSequence(asn1EncodableVector);
    }
}
