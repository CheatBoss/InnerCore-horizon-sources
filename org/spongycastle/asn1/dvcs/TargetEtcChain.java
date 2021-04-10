package org.spongycastle.asn1.dvcs;

import org.spongycastle.asn1.*;

public class TargetEtcChain extends ASN1Object
{
    private ASN1Sequence chain;
    private PathProcInput pathProcInput;
    private CertEtcToken target;
    
    private TargetEtcChain(final ASN1Sequence asn1Sequence) {
        this.target = CertEtcToken.getInstance(asn1Sequence.getObjectAt(0));
        if (asn1Sequence.size() > 1) {
            final ASN1Encodable object = asn1Sequence.getObjectAt(1);
            if (object instanceof ASN1TaggedObject) {
                this.extractPathProcInput(object);
                return;
            }
            this.chain = ASN1Sequence.getInstance(object);
            if (asn1Sequence.size() > 2) {
                this.extractPathProcInput(asn1Sequence.getObjectAt(2));
            }
        }
    }
    
    public TargetEtcChain(final CertEtcToken certEtcToken) {
        this(certEtcToken, null, null);
    }
    
    public TargetEtcChain(final CertEtcToken certEtcToken, final PathProcInput pathProcInput) {
        this(certEtcToken, null, pathProcInput);
    }
    
    public TargetEtcChain(final CertEtcToken certEtcToken, final CertEtcToken[] array) {
        this(certEtcToken, array, null);
    }
    
    public TargetEtcChain(final CertEtcToken target, final CertEtcToken[] array, final PathProcInput pathProcInput) {
        this.target = target;
        if (array != null) {
            this.chain = new DERSequence(array);
        }
        this.pathProcInput = pathProcInput;
    }
    
    public static TargetEtcChain[] arrayFromSequence(final ASN1Sequence asn1Sequence) {
        final int size = asn1Sequence.size();
        final TargetEtcChain[] array = new TargetEtcChain[size];
        for (int i = 0; i != size; ++i) {
            array[i] = getInstance(asn1Sequence.getObjectAt(i));
        }
        return array;
    }
    
    private void extractPathProcInput(final ASN1Encodable asn1Encodable) {
        final ASN1TaggedObject instance = ASN1TaggedObject.getInstance(asn1Encodable);
        if (instance.getTagNo() == 0) {
            this.pathProcInput = PathProcInput.getInstance(instance, false);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Unknown tag encountered: ");
        sb.append(instance.getTagNo());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static TargetEtcChain getInstance(final Object o) {
        if (o instanceof TargetEtcChain) {
            return (TargetEtcChain)o;
        }
        if (o != null) {
            return new TargetEtcChain(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static TargetEtcChain getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public CertEtcToken[] getChain() {
        final ASN1Sequence chain = this.chain;
        if (chain != null) {
            return CertEtcToken.arrayFromSequence(chain);
        }
        return null;
    }
    
    public PathProcInput getPathProcInput() {
        return this.pathProcInput;
    }
    
    public CertEtcToken getTarget() {
        return this.target;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.target);
        final ASN1Sequence chain = this.chain;
        if (chain != null) {
            asn1EncodableVector.add(chain);
        }
        if (this.pathProcInput != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 0, this.pathProcInput));
        }
        return new DERSequence(asn1EncodableVector);
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("TargetEtcChain {\n");
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("target: ");
        sb2.append(this.target);
        sb2.append("\n");
        sb.append(sb2.toString());
        if (this.chain != null) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("chain: ");
            sb3.append(this.chain);
            sb3.append("\n");
            sb.append(sb3.toString());
        }
        if (this.pathProcInput != null) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("pathProcInput: ");
            sb4.append(this.pathProcInput);
            sb4.append("\n");
            sb.append(sb4.toString());
        }
        sb.append("}\n");
        return sb.toString();
    }
}
