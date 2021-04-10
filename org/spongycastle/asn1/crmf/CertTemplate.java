package org.spongycastle.asn1.crmf;

import org.spongycastle.asn1.x500.*;
import org.spongycastle.asn1.x509.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class CertTemplate extends ASN1Object
{
    private Extensions extensions;
    private X500Name issuer;
    private DERBitString issuerUID;
    private SubjectPublicKeyInfo publicKey;
    private ASN1Sequence seq;
    private ASN1Integer serialNumber;
    private AlgorithmIdentifier signingAlg;
    private X500Name subject;
    private DERBitString subjectUID;
    private OptionalValidity validity;
    private ASN1Integer version;
    
    private CertTemplate(final ASN1Sequence seq) {
        this.seq = seq;
        final Enumeration objects = seq.getObjects();
        while (objects.hasMoreElements()) {
            final ASN1TaggedObject asn1TaggedObject = objects.nextElement();
            switch (asn1TaggedObject.getTagNo()) {
                default: {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("unknown tag: ");
                    sb.append(asn1TaggedObject.getTagNo());
                    throw new IllegalArgumentException(sb.toString());
                }
                case 9: {
                    this.extensions = Extensions.getInstance(asn1TaggedObject, false);
                    continue;
                }
                case 8: {
                    this.subjectUID = DERBitString.getInstance(asn1TaggedObject, false);
                    continue;
                }
                case 7: {
                    this.issuerUID = DERBitString.getInstance(asn1TaggedObject, false);
                    continue;
                }
                case 6: {
                    this.publicKey = SubjectPublicKeyInfo.getInstance(asn1TaggedObject, false);
                    continue;
                }
                case 5: {
                    this.subject = X500Name.getInstance(asn1TaggedObject, true);
                    continue;
                }
                case 4: {
                    this.validity = OptionalValidity.getInstance(ASN1Sequence.getInstance(asn1TaggedObject, false));
                    continue;
                }
                case 3: {
                    this.issuer = X500Name.getInstance(asn1TaggedObject, true);
                    continue;
                }
                case 2: {
                    this.signingAlg = AlgorithmIdentifier.getInstance(asn1TaggedObject, false);
                    continue;
                }
                case 1: {
                    this.serialNumber = ASN1Integer.getInstance(asn1TaggedObject, false);
                    continue;
                }
                case 0: {
                    this.version = ASN1Integer.getInstance(asn1TaggedObject, false);
                    continue;
                }
            }
        }
    }
    
    public static CertTemplate getInstance(final Object o) {
        if (o instanceof CertTemplate) {
            return (CertTemplate)o;
        }
        if (o != null) {
            return new CertTemplate(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public Extensions getExtensions() {
        return this.extensions;
    }
    
    public X500Name getIssuer() {
        return this.issuer;
    }
    
    public DERBitString getIssuerUID() {
        return this.issuerUID;
    }
    
    public SubjectPublicKeyInfo getPublicKey() {
        return this.publicKey;
    }
    
    public ASN1Integer getSerialNumber() {
        return this.serialNumber;
    }
    
    public AlgorithmIdentifier getSigningAlg() {
        return this.signingAlg;
    }
    
    public X500Name getSubject() {
        return this.subject;
    }
    
    public DERBitString getSubjectUID() {
        return this.subjectUID;
    }
    
    public OptionalValidity getValidity() {
        return this.validity;
    }
    
    public int getVersion() {
        return this.version.getValue().intValue();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.seq;
    }
}
