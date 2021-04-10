package org.spongycastle.asn1.bc;

import java.math.*;
import java.util.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.*;

public class ObjectData extends ASN1Object
{
    private final String comment;
    private final ASN1GeneralizedTime creationDate;
    private final ASN1OctetString data;
    private final String identifier;
    private final ASN1GeneralizedTime lastModifiedDate;
    private final BigInteger type;
    
    public ObjectData(final BigInteger type, final String identifier, final Date date, final Date date2, final byte[] array, final String comment) {
        this.type = type;
        this.identifier = identifier;
        this.creationDate = new DERGeneralizedTime(date);
        this.lastModifiedDate = new DERGeneralizedTime(date2);
        this.data = new DEROctetString(Arrays.clone(array));
        this.comment = comment;
    }
    
    private ObjectData(final ASN1Sequence asn1Sequence) {
        this.type = ASN1Integer.getInstance(asn1Sequence.getObjectAt(0)).getValue();
        this.identifier = DERUTF8String.getInstance(asn1Sequence.getObjectAt(1)).getString();
        this.creationDate = ASN1GeneralizedTime.getInstance(asn1Sequence.getObjectAt(2));
        this.lastModifiedDate = ASN1GeneralizedTime.getInstance(asn1Sequence.getObjectAt(3));
        this.data = ASN1OctetString.getInstance(asn1Sequence.getObjectAt(4));
        String string;
        if (asn1Sequence.size() == 6) {
            string = DERUTF8String.getInstance(asn1Sequence.getObjectAt(5)).getString();
        }
        else {
            string = null;
        }
        this.comment = string;
    }
    
    public static ObjectData getInstance(final Object o) {
        if (o instanceof ObjectData) {
            return (ObjectData)o;
        }
        if (o != null) {
            return new ObjectData(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public String getComment() {
        return this.comment;
    }
    
    public ASN1GeneralizedTime getCreationDate() {
        return this.creationDate;
    }
    
    public byte[] getData() {
        return Arrays.clone(this.data.getOctets());
    }
    
    public String getIdentifier() {
        return this.identifier;
    }
    
    public ASN1GeneralizedTime getLastModifiedDate() {
        return this.lastModifiedDate;
    }
    
    public BigInteger getType() {
        return this.type;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(new ASN1Integer(this.type));
        asn1EncodableVector.add(new DERUTF8String(this.identifier));
        asn1EncodableVector.add(this.creationDate);
        asn1EncodableVector.add(this.lastModifiedDate);
        asn1EncodableVector.add(this.data);
        if (this.comment != null) {
            asn1EncodableVector.add(new DERUTF8String(this.comment));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
