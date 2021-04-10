package org.spongycastle.asn1.x509;

import org.spongycastle.asn1.*;
import org.spongycastle.util.*;

public class IssuingDistributionPoint extends ASN1Object
{
    private DistributionPointName distributionPoint;
    private boolean indirectCRL;
    private boolean onlyContainsAttributeCerts;
    private boolean onlyContainsCACerts;
    private boolean onlyContainsUserCerts;
    private ReasonFlags onlySomeReasons;
    private ASN1Sequence seq;
    
    private IssuingDistributionPoint(final ASN1Sequence seq) {
        this.seq = seq;
        for (int i = 0; i != seq.size(); ++i) {
            final ASN1TaggedObject instance = ASN1TaggedObject.getInstance(seq.getObjectAt(i));
            final int tagNo = instance.getTagNo();
            if (tagNo != 0) {
                if (tagNo != 1) {
                    if (tagNo != 2) {
                        if (tagNo != 3) {
                            if (tagNo != 4) {
                                if (tagNo != 5) {
                                    throw new IllegalArgumentException("unknown tag in IssuingDistributionPoint");
                                }
                                this.onlyContainsAttributeCerts = ASN1Boolean.getInstance(instance, false).isTrue();
                            }
                            else {
                                this.indirectCRL = ASN1Boolean.getInstance(instance, false).isTrue();
                            }
                        }
                        else {
                            this.onlySomeReasons = new ReasonFlags(DERBitString.getInstance(instance, false));
                        }
                    }
                    else {
                        this.onlyContainsCACerts = ASN1Boolean.getInstance(instance, false).isTrue();
                    }
                }
                else {
                    this.onlyContainsUserCerts = ASN1Boolean.getInstance(instance, false).isTrue();
                }
            }
            else {
                this.distributionPoint = DistributionPointName.getInstance(instance, true);
            }
        }
    }
    
    public IssuingDistributionPoint(final DistributionPointName distributionPointName, final boolean b, final boolean b2) {
        this(distributionPointName, false, false, null, b, b2);
    }
    
    public IssuingDistributionPoint(final DistributionPointName distributionPoint, final boolean onlyContainsUserCerts, final boolean onlyContainsCACerts, final ReasonFlags onlySomeReasons, final boolean indirectCRL, final boolean onlyContainsAttributeCerts) {
        this.distributionPoint = distributionPoint;
        this.indirectCRL = indirectCRL;
        this.onlyContainsAttributeCerts = onlyContainsAttributeCerts;
        this.onlyContainsCACerts = onlyContainsCACerts;
        this.onlyContainsUserCerts = onlyContainsUserCerts;
        this.onlySomeReasons = onlySomeReasons;
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (distributionPoint != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 0, distributionPoint));
        }
        if (onlyContainsUserCerts) {
            asn1EncodableVector.add(new DERTaggedObject(false, 1, ASN1Boolean.getInstance(true)));
        }
        if (onlyContainsCACerts) {
            asn1EncodableVector.add(new DERTaggedObject(false, 2, ASN1Boolean.getInstance(true)));
        }
        if (onlySomeReasons != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 3, onlySomeReasons));
        }
        if (indirectCRL) {
            asn1EncodableVector.add(new DERTaggedObject(false, 4, ASN1Boolean.getInstance(true)));
        }
        if (onlyContainsAttributeCerts) {
            asn1EncodableVector.add(new DERTaggedObject(false, 5, ASN1Boolean.getInstance(true)));
        }
        this.seq = new DERSequence(asn1EncodableVector);
    }
    
    private void appendObject(final StringBuffer sb, final String s, final String s2, final String s3) {
        sb.append("    ");
        sb.append(s2);
        sb.append(":");
        sb.append(s);
        sb.append("    ");
        sb.append("    ");
        sb.append(s3);
        sb.append(s);
    }
    
    private String booleanToString(final boolean b) {
        if (b) {
            return "true";
        }
        return "false";
    }
    
    public static IssuingDistributionPoint getInstance(final Object o) {
        if (o instanceof IssuingDistributionPoint) {
            return (IssuingDistributionPoint)o;
        }
        if (o != null) {
            return new IssuingDistributionPoint(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static IssuingDistributionPoint getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public DistributionPointName getDistributionPoint() {
        return this.distributionPoint;
    }
    
    public ReasonFlags getOnlySomeReasons() {
        return this.onlySomeReasons;
    }
    
    public boolean isIndirectCRL() {
        return this.indirectCRL;
    }
    
    public boolean onlyContainsAttributeCerts() {
        return this.onlyContainsAttributeCerts;
    }
    
    public boolean onlyContainsCACerts() {
        return this.onlyContainsCACerts;
    }
    
    public boolean onlyContainsUserCerts() {
        return this.onlyContainsUserCerts;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.seq;
    }
    
    @Override
    public String toString() {
        final String lineSeparator = Strings.lineSeparator();
        final StringBuffer sb = new StringBuffer();
        sb.append("IssuingDistributionPoint: [");
        sb.append(lineSeparator);
        final DistributionPointName distributionPoint = this.distributionPoint;
        if (distributionPoint != null) {
            this.appendObject(sb, lineSeparator, "distributionPoint", distributionPoint.toString());
        }
        final boolean onlyContainsUserCerts = this.onlyContainsUserCerts;
        if (onlyContainsUserCerts) {
            this.appendObject(sb, lineSeparator, "onlyContainsUserCerts", this.booleanToString(onlyContainsUserCerts));
        }
        final boolean onlyContainsCACerts = this.onlyContainsCACerts;
        if (onlyContainsCACerts) {
            this.appendObject(sb, lineSeparator, "onlyContainsCACerts", this.booleanToString(onlyContainsCACerts));
        }
        final ReasonFlags onlySomeReasons = this.onlySomeReasons;
        if (onlySomeReasons != null) {
            this.appendObject(sb, lineSeparator, "onlySomeReasons", onlySomeReasons.toString());
        }
        final boolean onlyContainsAttributeCerts = this.onlyContainsAttributeCerts;
        if (onlyContainsAttributeCerts) {
            this.appendObject(sb, lineSeparator, "onlyContainsAttributeCerts", this.booleanToString(onlyContainsAttributeCerts));
        }
        final boolean indirectCRL = this.indirectCRL;
        if (indirectCRL) {
            this.appendObject(sb, lineSeparator, "indirectCRL", this.booleanToString(indirectCRL));
        }
        sb.append("]");
        sb.append(lineSeparator);
        return sb.toString();
    }
}
