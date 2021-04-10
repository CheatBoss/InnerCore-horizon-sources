package org.spongycastle.asn1.x509;

import java.util.*;
import org.spongycastle.asn1.*;

public class X509Extensions extends ASN1Object
{
    public static final ASN1ObjectIdentifier AuditIdentity;
    public static final ASN1ObjectIdentifier AuthorityInfoAccess;
    public static final ASN1ObjectIdentifier AuthorityKeyIdentifier;
    public static final ASN1ObjectIdentifier BasicConstraints;
    public static final ASN1ObjectIdentifier BiometricInfo;
    public static final ASN1ObjectIdentifier CRLDistributionPoints;
    public static final ASN1ObjectIdentifier CRLNumber;
    public static final ASN1ObjectIdentifier CertificateIssuer;
    public static final ASN1ObjectIdentifier CertificatePolicies;
    public static final ASN1ObjectIdentifier DeltaCRLIndicator;
    public static final ASN1ObjectIdentifier ExtendedKeyUsage;
    public static final ASN1ObjectIdentifier FreshestCRL;
    public static final ASN1ObjectIdentifier InhibitAnyPolicy;
    public static final ASN1ObjectIdentifier InstructionCode;
    public static final ASN1ObjectIdentifier InvalidityDate;
    public static final ASN1ObjectIdentifier IssuerAlternativeName;
    public static final ASN1ObjectIdentifier IssuingDistributionPoint;
    public static final ASN1ObjectIdentifier KeyUsage;
    public static final ASN1ObjectIdentifier LogoType;
    public static final ASN1ObjectIdentifier NameConstraints;
    public static final ASN1ObjectIdentifier NoRevAvail;
    public static final ASN1ObjectIdentifier PolicyConstraints;
    public static final ASN1ObjectIdentifier PolicyMappings;
    public static final ASN1ObjectIdentifier PrivateKeyUsagePeriod;
    public static final ASN1ObjectIdentifier QCStatements;
    public static final ASN1ObjectIdentifier ReasonCode;
    public static final ASN1ObjectIdentifier SubjectAlternativeName;
    public static final ASN1ObjectIdentifier SubjectDirectoryAttributes;
    public static final ASN1ObjectIdentifier SubjectInfoAccess;
    public static final ASN1ObjectIdentifier SubjectKeyIdentifier;
    public static final ASN1ObjectIdentifier TargetInformation;
    private Hashtable extensions;
    private Vector ordering;
    
    static {
        SubjectDirectoryAttributes = new ASN1ObjectIdentifier("2.5.29.9");
        SubjectKeyIdentifier = new ASN1ObjectIdentifier("2.5.29.14");
        KeyUsage = new ASN1ObjectIdentifier("2.5.29.15");
        PrivateKeyUsagePeriod = new ASN1ObjectIdentifier("2.5.29.16");
        SubjectAlternativeName = new ASN1ObjectIdentifier("2.5.29.17");
        IssuerAlternativeName = new ASN1ObjectIdentifier("2.5.29.18");
        BasicConstraints = new ASN1ObjectIdentifier("2.5.29.19");
        CRLNumber = new ASN1ObjectIdentifier("2.5.29.20");
        ReasonCode = new ASN1ObjectIdentifier("2.5.29.21");
        InstructionCode = new ASN1ObjectIdentifier("2.5.29.23");
        InvalidityDate = new ASN1ObjectIdentifier("2.5.29.24");
        DeltaCRLIndicator = new ASN1ObjectIdentifier("2.5.29.27");
        IssuingDistributionPoint = new ASN1ObjectIdentifier("2.5.29.28");
        CertificateIssuer = new ASN1ObjectIdentifier("2.5.29.29");
        NameConstraints = new ASN1ObjectIdentifier("2.5.29.30");
        CRLDistributionPoints = new ASN1ObjectIdentifier("2.5.29.31");
        CertificatePolicies = new ASN1ObjectIdentifier("2.5.29.32");
        PolicyMappings = new ASN1ObjectIdentifier("2.5.29.33");
        AuthorityKeyIdentifier = new ASN1ObjectIdentifier("2.5.29.35");
        PolicyConstraints = new ASN1ObjectIdentifier("2.5.29.36");
        ExtendedKeyUsage = new ASN1ObjectIdentifier("2.5.29.37");
        FreshestCRL = new ASN1ObjectIdentifier("2.5.29.46");
        InhibitAnyPolicy = new ASN1ObjectIdentifier("2.5.29.54");
        AuthorityInfoAccess = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.1.1");
        SubjectInfoAccess = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.1.11");
        LogoType = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.1.12");
        BiometricInfo = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.1.2");
        QCStatements = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.1.3");
        AuditIdentity = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.1.4");
        NoRevAvail = new ASN1ObjectIdentifier("2.5.29.56");
        TargetInformation = new ASN1ObjectIdentifier("2.5.29.55");
    }
    
    public X509Extensions(final Hashtable hashtable) {
        this(null, hashtable);
    }
    
    public X509Extensions(final Vector vector, final Hashtable hashtable) {
        this.extensions = new Hashtable();
        this.ordering = new Vector();
        Enumeration<Object> enumeration;
        if (vector == null) {
            enumeration = hashtable.keys();
        }
        else {
            enumeration = vector.elements();
        }
        while (enumeration.hasMoreElements()) {
            this.ordering.addElement(ASN1ObjectIdentifier.getInstance(enumeration.nextElement()));
        }
        final Enumeration<Object> elements = this.ordering.elements();
        while (elements.hasMoreElements()) {
            final ASN1ObjectIdentifier instance = ASN1ObjectIdentifier.getInstance(elements.nextElement());
            this.extensions.put(instance, hashtable.get(instance));
        }
    }
    
    public X509Extensions(final Vector vector, final Vector vector2) {
        this.extensions = new Hashtable();
        this.ordering = new Vector();
        final Enumeration<Object> elements = vector.elements();
        while (elements.hasMoreElements()) {
            this.ordering.addElement(elements.nextElement());
        }
        int n = 0;
        final Enumeration<ASN1ObjectIdentifier> elements2 = this.ordering.elements();
        while (elements2.hasMoreElements()) {
            this.extensions.put(elements2.nextElement(), vector2.elementAt(n));
            ++n;
        }
    }
    
    public X509Extensions(ASN1Sequence instance) {
        this.extensions = new Hashtable();
        this.ordering = new Vector();
        final Enumeration objects = instance.getObjects();
        while (objects.hasMoreElements()) {
            instance = ASN1Sequence.getInstance(objects.nextElement());
            if (instance.size() == 3) {
                this.extensions.put(instance.getObjectAt(0), new X509Extension(ASN1Boolean.getInstance(instance.getObjectAt(1)), ASN1OctetString.getInstance(instance.getObjectAt(2))));
            }
            else {
                if (instance.size() != 2) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Bad sequence size: ");
                    sb.append(instance.size());
                    throw new IllegalArgumentException(sb.toString());
                }
                this.extensions.put(instance.getObjectAt(0), new X509Extension(false, ASN1OctetString.getInstance(instance.getObjectAt(1))));
            }
            this.ordering.addElement(instance.getObjectAt(0));
        }
    }
    
    private ASN1ObjectIdentifier[] getExtensionOIDs(final boolean b) {
        final Vector<Object> vector = new Vector<Object>();
        for (int i = 0; i != this.ordering.size(); ++i) {
            final Object element = this.ordering.elementAt(i);
            if (((X509Extension)this.extensions.get(element)).isCritical() == b) {
                vector.addElement(element);
            }
        }
        return this.toOidArray(vector);
    }
    
    public static X509Extensions getInstance(final Object o) {
        if (o == null || o instanceof X509Extensions) {
            return (X509Extensions)o;
        }
        if (o instanceof ASN1Sequence) {
            return new X509Extensions((ASN1Sequence)o);
        }
        if (o instanceof Extensions) {
            return new X509Extensions((ASN1Sequence)((Extensions)o).toASN1Primitive());
        }
        if (o instanceof ASN1TaggedObject) {
            return getInstance(((ASN1TaggedObject)o).getObject());
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("illegal object in getInstance: ");
        sb.append(o.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static X509Extensions getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    private ASN1ObjectIdentifier[] toOidArray(final Vector vector) {
        final int size = vector.size();
        final ASN1ObjectIdentifier[] array = new ASN1ObjectIdentifier[size];
        for (int i = 0; i != size; ++i) {
            array[i] = vector.elementAt(i);
        }
        return array;
    }
    
    public boolean equivalent(final X509Extensions x509Extensions) {
        if (this.extensions.size() != x509Extensions.extensions.size()) {
            return false;
        }
        final Enumeration<Object> keys = this.extensions.keys();
        while (keys.hasMoreElements()) {
            final Object nextElement = keys.nextElement();
            if (!this.extensions.get(nextElement).equals(x509Extensions.extensions.get(nextElement))) {
                return false;
            }
        }
        return true;
    }
    
    public ASN1ObjectIdentifier[] getCriticalExtensionOIDs() {
        return this.getExtensionOIDs(true);
    }
    
    public X509Extension getExtension(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        return this.extensions.get(asn1ObjectIdentifier);
    }
    
    public ASN1ObjectIdentifier[] getExtensionOIDs() {
        return this.toOidArray(this.ordering);
    }
    
    public ASN1ObjectIdentifier[] getNonCriticalExtensionOIDs() {
        return this.getExtensionOIDs(false);
    }
    
    public Enumeration oids() {
        return this.ordering.elements();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        final Enumeration<ASN1ObjectIdentifier> elements = this.ordering.elements();
        while (elements.hasMoreElements()) {
            final ASN1ObjectIdentifier asn1ObjectIdentifier = elements.nextElement();
            final X509Extension x509Extension = this.extensions.get(asn1ObjectIdentifier);
            final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
            asn1EncodableVector2.add(asn1ObjectIdentifier);
            if (x509Extension.isCritical()) {
                asn1EncodableVector2.add(ASN1Boolean.TRUE);
            }
            asn1EncodableVector2.add(x509Extension.getValue());
            asn1EncodableVector.add(new DERSequence(asn1EncodableVector2));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
