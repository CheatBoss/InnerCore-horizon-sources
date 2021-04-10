package org.spongycastle.asn1.x509;

import org.spongycastle.asn1.*;
import org.spongycastle.util.*;

public class DistributionPoint extends ASN1Object
{
    GeneralNames cRLIssuer;
    DistributionPointName distributionPoint;
    ReasonFlags reasons;
    
    public DistributionPoint(final ASN1Sequence asn1Sequence) {
        for (int i = 0; i != asn1Sequence.size(); ++i) {
            final ASN1TaggedObject instance = ASN1TaggedObject.getInstance(asn1Sequence.getObjectAt(i));
            final int tagNo = instance.getTagNo();
            if (tagNo != 0) {
                if (tagNo != 1) {
                    if (tagNo != 2) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Unknown tag encountered in structure: ");
                        sb.append(instance.getTagNo());
                        throw new IllegalArgumentException(sb.toString());
                    }
                    this.cRLIssuer = GeneralNames.getInstance(instance, false);
                }
                else {
                    this.reasons = new ReasonFlags(DERBitString.getInstance(instance, false));
                }
            }
            else {
                this.distributionPoint = DistributionPointName.getInstance(instance, true);
            }
        }
    }
    
    public DistributionPoint(final DistributionPointName distributionPoint, final ReasonFlags reasons, final GeneralNames crlIssuer) {
        this.distributionPoint = distributionPoint;
        this.reasons = reasons;
        this.cRLIssuer = crlIssuer;
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
    
    public static DistributionPoint getInstance(final Object o) {
        if (o == null || o instanceof DistributionPoint) {
            return (DistributionPoint)o;
        }
        if (o instanceof ASN1Sequence) {
            return new DistributionPoint((ASN1Sequence)o);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Invalid DistributionPoint: ");
        sb.append(o.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static DistributionPoint getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public GeneralNames getCRLIssuer() {
        return this.cRLIssuer;
    }
    
    public DistributionPointName getDistributionPoint() {
        return this.distributionPoint;
    }
    
    public ReasonFlags getReasons() {
        return this.reasons;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.distributionPoint != null) {
            asn1EncodableVector.add(new DERTaggedObject(0, this.distributionPoint));
        }
        if (this.reasons != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 1, this.reasons));
        }
        if (this.cRLIssuer != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 2, this.cRLIssuer));
        }
        return new DERSequence(asn1EncodableVector);
    }
    
    @Override
    public String toString() {
        final String lineSeparator = Strings.lineSeparator();
        final StringBuffer sb = new StringBuffer();
        sb.append("DistributionPoint: [");
        sb.append(lineSeparator);
        final DistributionPointName distributionPoint = this.distributionPoint;
        if (distributionPoint != null) {
            this.appendObject(sb, lineSeparator, "distributionPoint", distributionPoint.toString());
        }
        final ReasonFlags reasons = this.reasons;
        if (reasons != null) {
            this.appendObject(sb, lineSeparator, "reasons", reasons.toString());
        }
        final GeneralNames crlIssuer = this.cRLIssuer;
        if (crlIssuer != null) {
            this.appendObject(sb, lineSeparator, "cRLIssuer", crlIssuer.toString());
        }
        sb.append("]");
        sb.append(lineSeparator);
        return sb.toString();
    }
}
