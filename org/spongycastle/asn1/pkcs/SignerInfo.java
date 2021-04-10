package org.spongycastle.asn1.pkcs;

import org.spongycastle.asn1.x509.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class SignerInfo extends ASN1Object
{
    private ASN1Set authenticatedAttributes;
    private AlgorithmIdentifier digAlgorithm;
    private AlgorithmIdentifier digEncryptionAlgorithm;
    private ASN1OctetString encryptedDigest;
    private IssuerAndSerialNumber issuerAndSerialNumber;
    private ASN1Set unauthenticatedAttributes;
    private ASN1Integer version;
    
    public SignerInfo(final ASN1Integer version, final IssuerAndSerialNumber issuerAndSerialNumber, final AlgorithmIdentifier digAlgorithm, final ASN1Set authenticatedAttributes, final AlgorithmIdentifier digEncryptionAlgorithm, final ASN1OctetString encryptedDigest, final ASN1Set unauthenticatedAttributes) {
        this.version = version;
        this.issuerAndSerialNumber = issuerAndSerialNumber;
        this.digAlgorithm = digAlgorithm;
        this.authenticatedAttributes = authenticatedAttributes;
        this.digEncryptionAlgorithm = digEncryptionAlgorithm;
        this.encryptedDigest = encryptedDigest;
        this.unauthenticatedAttributes = unauthenticatedAttributes;
    }
    
    public SignerInfo(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.version = objects.nextElement();
        this.issuerAndSerialNumber = IssuerAndSerialNumber.getInstance(objects.nextElement());
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
    
    public static SignerInfo getInstance(final Object o) {
        if (o instanceof SignerInfo) {
            return (SignerInfo)o;
        }
        if (o instanceof ASN1Sequence) {
            return new SignerInfo((ASN1Sequence)o);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("unknown object in factory: ");
        sb.append(o.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
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
    
    public IssuerAndSerialNumber getIssuerAndSerialNumber() {
        return this.issuerAndSerialNumber;
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
        asn1EncodableVector.add(this.issuerAndSerialNumber);
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
