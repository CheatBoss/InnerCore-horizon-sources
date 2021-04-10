package org.spongycastle.asn1.cms.ecc;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.*;

public class ECCCMSSharedInfo extends ASN1Object
{
    private final byte[] entityUInfo;
    private final AlgorithmIdentifier keyInfo;
    private final byte[] suppPubInfo;
    
    private ECCCMSSharedInfo(final ASN1Sequence asn1Sequence) {
        this.keyInfo = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(0));
        ASN1Encodable asn1Encodable;
        if (asn1Sequence.size() == 2) {
            this.entityUInfo = null;
            asn1Encodable = asn1Sequence.getObjectAt(1);
        }
        else {
            this.entityUInfo = ASN1OctetString.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(1), true).getOctets();
            asn1Encodable = asn1Sequence.getObjectAt(2);
        }
        this.suppPubInfo = ASN1OctetString.getInstance((ASN1TaggedObject)asn1Encodable, true).getOctets();
    }
    
    public ECCCMSSharedInfo(final AlgorithmIdentifier keyInfo, final byte[] array) {
        this.keyInfo = keyInfo;
        this.entityUInfo = null;
        this.suppPubInfo = Arrays.clone(array);
    }
    
    public ECCCMSSharedInfo(final AlgorithmIdentifier keyInfo, final byte[] array, final byte[] array2) {
        this.keyInfo = keyInfo;
        this.entityUInfo = Arrays.clone(array);
        this.suppPubInfo = Arrays.clone(array2);
    }
    
    public static ECCCMSSharedInfo getInstance(final Object o) {
        if (o instanceof ECCCMSSharedInfo) {
            return (ECCCMSSharedInfo)o;
        }
        if (o != null) {
            return new ECCCMSSharedInfo(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static ECCCMSSharedInfo getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.keyInfo);
        if (this.entityUInfo != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 0, new DEROctetString(this.entityUInfo)));
        }
        asn1EncodableVector.add(new DERTaggedObject(true, 2, new DEROctetString(this.suppPubInfo)));
        return new DERSequence(asn1EncodableVector);
    }
}
