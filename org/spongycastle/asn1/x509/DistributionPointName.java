package org.spongycastle.asn1.x509;

import org.spongycastle.asn1.*;
import org.spongycastle.util.*;

public class DistributionPointName extends ASN1Object implements ASN1Choice
{
    public static final int FULL_NAME = 0;
    public static final int NAME_RELATIVE_TO_CRL_ISSUER = 1;
    ASN1Encodable name;
    int type;
    
    public DistributionPointName(final int type, final ASN1Encodable name) {
        this.type = type;
        this.name = name;
    }
    
    public DistributionPointName(final ASN1TaggedObject asn1TaggedObject) {
        final int tagNo = asn1TaggedObject.getTagNo();
        this.type = tagNo;
        ASN1Object name;
        if (tagNo == 0) {
            name = GeneralNames.getInstance(asn1TaggedObject, false);
        }
        else {
            name = ASN1Set.getInstance(asn1TaggedObject, false);
        }
        this.name = name;
    }
    
    public DistributionPointName(final GeneralNames generalNames) {
        this(0, generalNames);
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
    
    public static DistributionPointName getInstance(final Object o) {
        if (o == null || o instanceof DistributionPointName) {
            return (DistributionPointName)o;
        }
        if (o instanceof ASN1TaggedObject) {
            return new DistributionPointName((ASN1TaggedObject)o);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("unknown object in factory: ");
        sb.append(o.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static DistributionPointName getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1TaggedObject.getInstance(asn1TaggedObject, true));
    }
    
    public ASN1Encodable getName() {
        return this.name;
    }
    
    public int getType() {
        return this.type;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return new DERTaggedObject(false, this.type, this.name);
    }
    
    @Override
    public String toString() {
        final String lineSeparator = Strings.lineSeparator();
        final StringBuffer sb = new StringBuffer();
        sb.append("DistributionPointName: [");
        sb.append(lineSeparator);
        String s;
        if (this.type == 0) {
            s = "fullName";
        }
        else {
            s = "nameRelativeToCRLIssuer";
        }
        this.appendObject(sb, lineSeparator, s, this.name.toString());
        sb.append("]");
        sb.append(lineSeparator);
        return sb.toString();
    }
}
