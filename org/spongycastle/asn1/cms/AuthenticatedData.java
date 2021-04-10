package org.spongycastle.asn1.cms;

import org.spongycastle.asn1.x509.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class AuthenticatedData extends ASN1Object
{
    private ASN1Set authAttrs;
    private AlgorithmIdentifier digestAlgorithm;
    private ContentInfo encapsulatedContentInfo;
    private ASN1OctetString mac;
    private AlgorithmIdentifier macAlgorithm;
    private OriginatorInfo originatorInfo;
    private ASN1Set recipientInfos;
    private ASN1Set unauthAttrs;
    private ASN1Integer version;
    
    private AuthenticatedData(final ASN1Sequence asn1Sequence) {
        this.version = (ASN1Integer)asn1Sequence.getObjectAt(0);
        final ASN1Encodable object = asn1Sequence.getObjectAt(1);
        final boolean b = object instanceof ASN1TaggedObject;
        int n = 2;
        ASN1Encodable object2 = object;
        if (b) {
            this.originatorInfo = OriginatorInfo.getInstance((ASN1TaggedObject)object, false);
            object2 = asn1Sequence.getObjectAt(2);
            n = 3;
        }
        this.recipientInfos = ASN1Set.getInstance(object2);
        final int n2 = n + 1;
        this.macAlgorithm = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(n));
        final int n3 = n2 + 1;
        ASN1Encodable asn1Encodable2;
        final ASN1Encodable asn1Encodable = asn1Encodable2 = asn1Sequence.getObjectAt(n2);
        int n4 = n3;
        if (asn1Encodable instanceof ASN1TaggedObject) {
            this.digestAlgorithm = AlgorithmIdentifier.getInstance((ASN1TaggedObject)asn1Encodable, false);
            asn1Encodable2 = asn1Sequence.getObjectAt(n3);
            n4 = n3 + 1;
        }
        this.encapsulatedContentInfo = ContentInfo.getInstance(asn1Encodable2);
        final int n5 = n4 + 1;
        final ASN1Encodable object3 = asn1Sequence.getObjectAt(n4);
        int n6 = n5;
        ASN1Encodable object4 = object3;
        if (object3 instanceof ASN1TaggedObject) {
            this.authAttrs = ASN1Set.getInstance((ASN1TaggedObject)object3, false);
            object4 = asn1Sequence.getObjectAt(n5);
            n6 = n5 + 1;
        }
        this.mac = ASN1OctetString.getInstance(object4);
        if (asn1Sequence.size() > n6) {
            this.unauthAttrs = ASN1Set.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(n6), false);
        }
    }
    
    public AuthenticatedData(final OriginatorInfo originatorInfo, final ASN1Set recipientInfos, final AlgorithmIdentifier macAlgorithm, final AlgorithmIdentifier digestAlgorithm, final ContentInfo encapsulatedContentInfo, final ASN1Set authAttrs, final ASN1OctetString mac, final ASN1Set unauthAttrs) {
        if ((digestAlgorithm == null && authAttrs == null) || (digestAlgorithm != null && authAttrs != null)) {
            this.version = new ASN1Integer(calculateVersion(originatorInfo));
            this.originatorInfo = originatorInfo;
            this.macAlgorithm = macAlgorithm;
            this.digestAlgorithm = digestAlgorithm;
            this.recipientInfos = recipientInfos;
            this.encapsulatedContentInfo = encapsulatedContentInfo;
            this.authAttrs = authAttrs;
            this.mac = mac;
            this.unauthAttrs = unauthAttrs;
            return;
        }
        throw new IllegalArgumentException("digestAlgorithm and authAttrs must be set together");
    }
    
    public static int calculateVersion(final OriginatorInfo originatorInfo) {
        int n = 0;
        if (originatorInfo == null) {
            return 0;
        }
        final Enumeration objects = originatorInfo.getCertificates().getObjects();
        int n2;
        while (true) {
            n2 = n;
            if (!objects.hasMoreElements()) {
                break;
            }
            final ASN1TaggedObject nextElement = objects.nextElement();
            if (!(nextElement instanceof ASN1TaggedObject)) {
                continue;
            }
            final ASN1TaggedObject asn1TaggedObject = nextElement;
            if (asn1TaggedObject.getTagNo() == 2) {
                n = 1;
            }
            else {
                if (asn1TaggedObject.getTagNo() == 3) {
                    n2 = 3;
                    break;
                }
                continue;
            }
        }
        if (originatorInfo.getCRLs() != null) {
            final Enumeration objects2 = originatorInfo.getCRLs().getObjects();
            while (objects2.hasMoreElements()) {
                final ASN1TaggedObject nextElement2 = objects2.nextElement();
                if (nextElement2 instanceof ASN1TaggedObject && nextElement2.getTagNo() == 1) {
                    return 3;
                }
            }
        }
        return n2;
    }
    
    public static AuthenticatedData getInstance(final Object o) {
        if (o instanceof AuthenticatedData) {
            return (AuthenticatedData)o;
        }
        if (o != null) {
            return new AuthenticatedData(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static AuthenticatedData getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public ASN1Set getAuthAttrs() {
        return this.authAttrs;
    }
    
    public AlgorithmIdentifier getDigestAlgorithm() {
        return this.digestAlgorithm;
    }
    
    public ContentInfo getEncapsulatedContentInfo() {
        return this.encapsulatedContentInfo;
    }
    
    public ASN1OctetString getMac() {
        return this.mac;
    }
    
    public AlgorithmIdentifier getMacAlgorithm() {
        return this.macAlgorithm;
    }
    
    public OriginatorInfo getOriginatorInfo() {
        return this.originatorInfo;
    }
    
    public ASN1Set getRecipientInfos() {
        return this.recipientInfos;
    }
    
    public ASN1Set getUnauthAttrs() {
        return this.unauthAttrs;
    }
    
    public ASN1Integer getVersion() {
        return this.version;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.version);
        if (this.originatorInfo != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 0, this.originatorInfo));
        }
        asn1EncodableVector.add(this.recipientInfos);
        asn1EncodableVector.add(this.macAlgorithm);
        if (this.digestAlgorithm != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 1, this.digestAlgorithm));
        }
        asn1EncodableVector.add(this.encapsulatedContentInfo);
        if (this.authAttrs != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 2, this.authAttrs));
        }
        asn1EncodableVector.add(this.mac);
        if (this.unauthAttrs != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 3, this.unauthAttrs));
        }
        return new BERSequence(asn1EncodableVector);
    }
}
