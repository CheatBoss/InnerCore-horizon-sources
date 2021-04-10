package org.spongycastle.asn1.x509;

import java.io.*;
import org.spongycastle.asn1.*;

public class Extension extends ASN1Object
{
    public static final ASN1ObjectIdentifier auditIdentity;
    public static final ASN1ObjectIdentifier authorityInfoAccess;
    public static final ASN1ObjectIdentifier authorityKeyIdentifier;
    public static final ASN1ObjectIdentifier basicConstraints;
    public static final ASN1ObjectIdentifier biometricInfo;
    public static final ASN1ObjectIdentifier cRLDistributionPoints;
    public static final ASN1ObjectIdentifier cRLNumber;
    public static final ASN1ObjectIdentifier certificateIssuer;
    public static final ASN1ObjectIdentifier certificatePolicies;
    public static final ASN1ObjectIdentifier deltaCRLIndicator;
    public static final ASN1ObjectIdentifier expiredCertsOnCRL;
    public static final ASN1ObjectIdentifier extendedKeyUsage;
    public static final ASN1ObjectIdentifier freshestCRL;
    public static final ASN1ObjectIdentifier inhibitAnyPolicy;
    public static final ASN1ObjectIdentifier instructionCode;
    public static final ASN1ObjectIdentifier invalidityDate;
    public static final ASN1ObjectIdentifier issuerAlternativeName;
    public static final ASN1ObjectIdentifier issuingDistributionPoint;
    public static final ASN1ObjectIdentifier keyUsage;
    public static final ASN1ObjectIdentifier logoType;
    public static final ASN1ObjectIdentifier nameConstraints;
    public static final ASN1ObjectIdentifier noRevAvail;
    public static final ASN1ObjectIdentifier policyConstraints;
    public static final ASN1ObjectIdentifier policyMappings;
    public static final ASN1ObjectIdentifier privateKeyUsagePeriod;
    public static final ASN1ObjectIdentifier qCStatements;
    public static final ASN1ObjectIdentifier reasonCode;
    public static final ASN1ObjectIdentifier subjectAlternativeName;
    public static final ASN1ObjectIdentifier subjectDirectoryAttributes;
    public static final ASN1ObjectIdentifier subjectInfoAccess;
    public static final ASN1ObjectIdentifier subjectKeyIdentifier;
    public static final ASN1ObjectIdentifier targetInformation;
    private boolean critical;
    private ASN1ObjectIdentifier extnId;
    private ASN1OctetString value;
    
    static {
        subjectDirectoryAttributes = new ASN1ObjectIdentifier("2.5.29.9").intern();
        subjectKeyIdentifier = new ASN1ObjectIdentifier("2.5.29.14").intern();
        keyUsage = new ASN1ObjectIdentifier("2.5.29.15").intern();
        privateKeyUsagePeriod = new ASN1ObjectIdentifier("2.5.29.16").intern();
        subjectAlternativeName = new ASN1ObjectIdentifier("2.5.29.17").intern();
        issuerAlternativeName = new ASN1ObjectIdentifier("2.5.29.18").intern();
        basicConstraints = new ASN1ObjectIdentifier("2.5.29.19").intern();
        cRLNumber = new ASN1ObjectIdentifier("2.5.29.20").intern();
        reasonCode = new ASN1ObjectIdentifier("2.5.29.21").intern();
        instructionCode = new ASN1ObjectIdentifier("2.5.29.23").intern();
        invalidityDate = new ASN1ObjectIdentifier("2.5.29.24").intern();
        deltaCRLIndicator = new ASN1ObjectIdentifier("2.5.29.27").intern();
        issuingDistributionPoint = new ASN1ObjectIdentifier("2.5.29.28").intern();
        certificateIssuer = new ASN1ObjectIdentifier("2.5.29.29").intern();
        nameConstraints = new ASN1ObjectIdentifier("2.5.29.30").intern();
        cRLDistributionPoints = new ASN1ObjectIdentifier("2.5.29.31").intern();
        certificatePolicies = new ASN1ObjectIdentifier("2.5.29.32").intern();
        policyMappings = new ASN1ObjectIdentifier("2.5.29.33").intern();
        authorityKeyIdentifier = new ASN1ObjectIdentifier("2.5.29.35").intern();
        policyConstraints = new ASN1ObjectIdentifier("2.5.29.36").intern();
        extendedKeyUsage = new ASN1ObjectIdentifier("2.5.29.37").intern();
        freshestCRL = new ASN1ObjectIdentifier("2.5.29.46").intern();
        inhibitAnyPolicy = new ASN1ObjectIdentifier("2.5.29.54").intern();
        authorityInfoAccess = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.1.1").intern();
        subjectInfoAccess = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.1.11").intern();
        logoType = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.1.12").intern();
        biometricInfo = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.1.2").intern();
        qCStatements = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.1.3").intern();
        auditIdentity = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.1.4").intern();
        noRevAvail = new ASN1ObjectIdentifier("2.5.29.56").intern();
        targetInformation = new ASN1ObjectIdentifier("2.5.29.55").intern();
        expiredCertsOnCRL = new ASN1ObjectIdentifier("2.5.29.60").intern();
    }
    
    public Extension(final ASN1ObjectIdentifier asn1ObjectIdentifier, final ASN1Boolean asn1Boolean, final ASN1OctetString asn1OctetString) {
        this(asn1ObjectIdentifier, asn1Boolean.isTrue(), asn1OctetString);
    }
    
    public Extension(final ASN1ObjectIdentifier extnId, final boolean critical, final ASN1OctetString value) {
        this.extnId = extnId;
        this.critical = critical;
        this.value = value;
    }
    
    public Extension(final ASN1ObjectIdentifier asn1ObjectIdentifier, final boolean b, final byte[] array) {
        this(asn1ObjectIdentifier, b, new DEROctetString(array));
    }
    
    private Extension(final ASN1Sequence asn1Sequence) {
        ASN1Encodable asn1Encodable;
        if (asn1Sequence.size() == 2) {
            this.extnId = ASN1ObjectIdentifier.getInstance(asn1Sequence.getObjectAt(0));
            this.critical = false;
            asn1Encodable = asn1Sequence.getObjectAt(1);
        }
        else {
            if (asn1Sequence.size() != 3) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Bad sequence size: ");
                sb.append(asn1Sequence.size());
                throw new IllegalArgumentException(sb.toString());
            }
            this.extnId = ASN1ObjectIdentifier.getInstance(asn1Sequence.getObjectAt(0));
            this.critical = ASN1Boolean.getInstance(asn1Sequence.getObjectAt(1)).isTrue();
            asn1Encodable = asn1Sequence.getObjectAt(2);
        }
        this.value = ASN1OctetString.getInstance(asn1Encodable);
    }
    
    private static ASN1Primitive convertValueToObject(final Extension extension) throws IllegalArgumentException {
        try {
            return ASN1Primitive.fromByteArray(extension.getExtnValue().getOctets());
        }
        catch (IOException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("can't convert extension: ");
            sb.append(ex);
            throw new IllegalArgumentException(sb.toString());
        }
    }
    
    public static Extension getInstance(final Object o) {
        if (o instanceof Extension) {
            return (Extension)o;
        }
        if (o != null) {
            return new Extension(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof Extension;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final Extension extension = (Extension)o;
        boolean b3 = b2;
        if (extension.getExtnId().equals(this.getExtnId())) {
            b3 = b2;
            if (extension.getExtnValue().equals(this.getExtnValue())) {
                b3 = b2;
                if (extension.isCritical() == this.isCritical()) {
                    b3 = true;
                }
            }
        }
        return b3;
    }
    
    public ASN1ObjectIdentifier getExtnId() {
        return this.extnId;
    }
    
    public ASN1OctetString getExtnValue() {
        return this.value;
    }
    
    public ASN1Encodable getParsedValue() {
        return convertValueToObject(this);
    }
    
    @Override
    public int hashCode() {
        if (this.isCritical()) {
            return this.getExtnValue().hashCode() ^ this.getExtnId().hashCode();
        }
        return ~(this.getExtnValue().hashCode() ^ this.getExtnId().hashCode());
    }
    
    public boolean isCritical() {
        return this.critical;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.extnId);
        if (this.critical) {
            asn1EncodableVector.add(ASN1Boolean.getInstance(true));
        }
        asn1EncodableVector.add(this.value);
        return new DERSequence(asn1EncodableVector);
    }
}
