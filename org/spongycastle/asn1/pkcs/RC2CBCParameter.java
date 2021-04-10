package org.spongycastle.asn1.pkcs;

import java.math.*;
import org.spongycastle.asn1.*;

public class RC2CBCParameter extends ASN1Object
{
    ASN1OctetString iv;
    ASN1Integer version;
    
    public RC2CBCParameter(final int n, final byte[] array) {
        this.version = new ASN1Integer(n);
        this.iv = new DEROctetString(array);
    }
    
    private RC2CBCParameter(final ASN1Sequence asn1Sequence) {
        ASN1Encodable asn1Encodable;
        if (asn1Sequence.size() == 1) {
            this.version = null;
            asn1Encodable = asn1Sequence.getObjectAt(0);
        }
        else {
            this.version = (ASN1Integer)asn1Sequence.getObjectAt(0);
            asn1Encodable = asn1Sequence.getObjectAt(1);
        }
        this.iv = (ASN1OctetString)asn1Encodable;
    }
    
    public RC2CBCParameter(final byte[] array) {
        this.version = null;
        this.iv = new DEROctetString(array);
    }
    
    public static RC2CBCParameter getInstance(final Object o) {
        if (o instanceof RC2CBCParameter) {
            return (RC2CBCParameter)o;
        }
        if (o != null) {
            return new RC2CBCParameter(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public byte[] getIV() {
        return this.iv.getOctets();
    }
    
    public BigInteger getRC2ParameterVersion() {
        final ASN1Integer version = this.version;
        if (version == null) {
            return null;
        }
        return version.getValue();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        final ASN1Integer version = this.version;
        if (version != null) {
            asn1EncodableVector.add(version);
        }
        asn1EncodableVector.add(this.iv);
        return new DERSequence(asn1EncodableVector);
    }
}
