package org.spongycastle.asn1.cmp;

import java.util.*;
import org.spongycastle.asn1.*;

public class PKIMessage extends ASN1Object
{
    private PKIBody body;
    private ASN1Sequence extraCerts;
    private PKIHeader header;
    private DERBitString protection;
    
    private PKIMessage(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.header = PKIHeader.getInstance(objects.nextElement());
        this.body = PKIBody.getInstance(objects.nextElement());
        while (objects.hasMoreElements()) {
            final ASN1TaggedObject asn1TaggedObject = objects.nextElement();
            if (asn1TaggedObject.getTagNo() == 0) {
                this.protection = DERBitString.getInstance(asn1TaggedObject, true);
            }
            else {
                this.extraCerts = ASN1Sequence.getInstance(asn1TaggedObject, true);
            }
        }
    }
    
    public PKIMessage(final PKIHeader pkiHeader, final PKIBody pkiBody) {
        this(pkiHeader, pkiBody, null, null);
    }
    
    public PKIMessage(final PKIHeader pkiHeader, final PKIBody pkiBody, final DERBitString derBitString) {
        this(pkiHeader, pkiBody, derBitString, null);
    }
    
    public PKIMessage(final PKIHeader header, final PKIBody body, final DERBitString protection, final CMPCertificate[] array) {
        this.header = header;
        this.body = body;
        this.protection = protection;
        if (array != null) {
            final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
            for (int i = 0; i < array.length; ++i) {
                asn1EncodableVector.add(array[i]);
            }
            this.extraCerts = new DERSequence(asn1EncodableVector);
        }
    }
    
    private void addOptional(final ASN1EncodableVector asn1EncodableVector, final int n, final ASN1Encodable asn1Encodable) {
        if (asn1Encodable != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, n, asn1Encodable));
        }
    }
    
    public static PKIMessage getInstance(final Object o) {
        if (o instanceof PKIMessage) {
            return (PKIMessage)o;
        }
        if (o != null) {
            return new PKIMessage(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public PKIBody getBody() {
        return this.body;
    }
    
    public CMPCertificate[] getExtraCerts() {
        final ASN1Sequence extraCerts = this.extraCerts;
        if (extraCerts == null) {
            return null;
        }
        final int size = extraCerts.size();
        final CMPCertificate[] array = new CMPCertificate[size];
        for (int i = 0; i < size; ++i) {
            array[i] = CMPCertificate.getInstance(this.extraCerts.getObjectAt(i));
        }
        return array;
    }
    
    public PKIHeader getHeader() {
        return this.header;
    }
    
    public DERBitString getProtection() {
        return this.protection;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.header);
        asn1EncodableVector.add(this.body);
        this.addOptional(asn1EncodableVector, 0, this.protection);
        this.addOptional(asn1EncodableVector, 1, this.extraCerts);
        return new DERSequence(asn1EncodableVector);
    }
}
