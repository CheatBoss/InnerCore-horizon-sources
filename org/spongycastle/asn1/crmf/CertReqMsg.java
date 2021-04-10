package org.spongycastle.asn1.crmf;

import java.util.*;
import org.spongycastle.asn1.*;

public class CertReqMsg extends ASN1Object
{
    private CertRequest certReq;
    private ProofOfPossession pop;
    private ASN1Sequence regInfo;
    
    private CertReqMsg(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.certReq = CertRequest.getInstance(objects.nextElement());
        while (objects.hasMoreElements()) {
            final Object nextElement = objects.nextElement();
            if (!(nextElement instanceof ASN1TaggedObject) && !(nextElement instanceof ProofOfPossession)) {
                this.regInfo = ASN1Sequence.getInstance(nextElement);
            }
            else {
                this.pop = ProofOfPossession.getInstance(nextElement);
            }
        }
    }
    
    public CertReqMsg(final CertRequest certReq, final ProofOfPossession pop, final AttributeTypeAndValue[] array) {
        if (certReq != null) {
            this.certReq = certReq;
            this.pop = pop;
            if (array != null) {
                this.regInfo = new DERSequence(array);
            }
            return;
        }
        throw new IllegalArgumentException("'certReq' cannot be null");
    }
    
    private void addOptional(final ASN1EncodableVector asn1EncodableVector, final ASN1Encodable asn1Encodable) {
        if (asn1Encodable != null) {
            asn1EncodableVector.add(asn1Encodable);
        }
    }
    
    public static CertReqMsg getInstance(final Object o) {
        if (o instanceof CertReqMsg) {
            return (CertReqMsg)o;
        }
        if (o != null) {
            return new CertReqMsg(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static CertReqMsg getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public CertRequest getCertReq() {
        return this.certReq;
    }
    
    public ProofOfPossession getPop() {
        return this.pop;
    }
    
    public ProofOfPossession getPopo() {
        return this.pop;
    }
    
    public AttributeTypeAndValue[] getRegInfo() {
        final ASN1Sequence regInfo = this.regInfo;
        if (regInfo == null) {
            return null;
        }
        final int size = regInfo.size();
        final AttributeTypeAndValue[] array = new AttributeTypeAndValue[size];
        for (int i = 0; i != size; ++i) {
            array[i] = AttributeTypeAndValue.getInstance(this.regInfo.getObjectAt(i));
        }
        return array;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.certReq);
        this.addOptional(asn1EncodableVector, this.pop);
        this.addOptional(asn1EncodableVector, this.regInfo);
        return new DERSequence(asn1EncodableVector);
    }
}
