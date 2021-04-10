package org.spongycastle.asn1.cms;

import org.spongycastle.asn1.x509.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class SignerInfo extends ASN1Object
{
    private ASN1Set authenticatedAttributes;
    private AlgorithmIdentifier digAlgorithm;
    private AlgorithmIdentifier digEncryptionAlgorithm;
    private ASN1OctetString encryptedDigest;
    private SignerIdentifier sid;
    private ASN1Set unauthenticatedAttributes;
    private ASN1Integer version;
    
    public SignerInfo(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.version = objects.nextElement();
        this.sid = SignerIdentifier.getInstance(objects.nextElement());
        this.digAlgorithm = AlgorithmIdentifier.getInstance(objects.nextElement());
        ASN1Integer asn1Integer = objects.nextElement();
        if (asn1Integer instanceof ASN1TaggedObject) {
            this.authenticatedAttributes = ASN1Set.getInstance((ASN1TaggedObject)asn1Integer, false);
            asn1Integer = objects.nextElement();
        }
        else {
            this.authenticatedAttributes = null;
        }
        this.digEncryptionAlgorithm = AlgorithmIdentifier.getInstance(asn1Integer);
        this.encryptedDigest = ASN1OctetString.getInstance(objects.nextElement());
        if (objects.hasMoreElements()) {
            this.unauthenticatedAttributes = ASN1Set.getInstance((ASN1TaggedObject)objects.nextElement(), false);
            return;
        }
        this.unauthenticatedAttributes = null;
    }
    
    public SignerInfo(final SignerIdentifier sid, final AlgorithmIdentifier digAlgorithm, final ASN1Set authenticatedAttributes, final AlgorithmIdentifier digEncryptionAlgorithm, final ASN1OctetString encryptedDigest, final ASN1Set unauthenticatedAttributes) {
        ASN1Integer version;
        if (sid.isTagged()) {
            version = new ASN1Integer(3L);
        }
        else {
            version = new ASN1Integer(1L);
        }
        this.version = version;
        this.sid = sid;
        this.digAlgorithm = digAlgorithm;
        this.authenticatedAttributes = authenticatedAttributes;
        this.digEncryptionAlgorithm = digEncryptionAlgorithm;
        this.encryptedDigest = encryptedDigest;
        this.unauthenticatedAttributes = unauthenticatedAttributes;
    }
    
    public SignerInfo(final SignerIdentifier sid, final AlgorithmIdentifier digAlgorithm, final Attributes attributes, final AlgorithmIdentifier digEncryptionAlgorithm, final ASN1OctetString encryptedDigest, final Attributes attributes2) {
        ASN1Integer version;
        if (sid.isTagged()) {
            version = new ASN1Integer(3L);
        }
        else {
            version = new ASN1Integer(1L);
        }
        this.version = version;
        this.sid = sid;
        this.digAlgorithm = digAlgorithm;
        this.authenticatedAttributes = ASN1Set.getInstance(attributes);
        this.digEncryptionAlgorithm = digEncryptionAlgorithm;
        this.encryptedDigest = encryptedDigest;
        this.unauthenticatedAttributes = ASN1Set.getInstance(attributes2);
    }
    
    public static SignerInfo getInstance(final Object o) throws IllegalArgumentException {
        if (o instanceof SignerInfo) {
            return (SignerInfo)o;
        }
        if (o != null) {
            return new SignerInfo(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ASN1Set getAuthenticatedAttributes() {
        return this.authenticatedAttributes;
    }
    
    public AlgorithmIdentifier getDigestAlgorithm() {
        return this.digAlgorithm;
    }
    
    public AlgorithmIdentifier getDigestEncryptionAlgorithm() {
        return this.digEncryptionAlgorithm;
    }
    
    public ASN1OctetString getEncryptedDigest() {
        return this.encryptedDigest;
    }
    
    public SignerIdentifier getSID() {
        return this.sid;
    }
    
    public ASN1Set getUnauthenticatedAttributes() {
        return this.unauthenticatedAttributes;
    }
    
    public ASN1Integer getVersion() {
        return this.version;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.version);
        asn1EncodableVector.add(this.sid);
        asn1EncodableVector.add(this.digAlgorithm);
        if (this.authenticatedAttributes != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 0, this.authenticatedAttributes));
        }
        asn1EncodableVector.add(this.digEncryptionAlgorithm);
        asn1EncodableVector.add(this.encryptedDigest);
        if (this.unauthenticatedAttributes != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 1, this.unauthenticatedAttributes));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
