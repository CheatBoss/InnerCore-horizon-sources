package org.spongycastle.asn1.x509;

import org.spongycastle.asn1.*;

public class Target extends ASN1Object implements ASN1Choice
{
    public static final int targetGroup = 1;
    public static final int targetName = 0;
    private GeneralName targGroup;
    private GeneralName targName;
    
    public Target(final int n, final GeneralName generalName) {
        this(new DERTaggedObject(n, generalName));
    }
    
    private Target(final ASN1TaggedObject asn1TaggedObject) {
        final int tagNo = asn1TaggedObject.getTagNo();
        if (tagNo == 0) {
            this.targName = GeneralName.getInstance(asn1TaggedObject, true);
            return;
        }
        if (tagNo == 1) {
            this.targGroup = GeneralName.getInstance(asn1TaggedObject, true);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("unknown tag: ");
        sb.append(asn1TaggedObject.getTagNo());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static Target getInstance(final Object o) {
        if (o == null || o instanceof Target) {
            return (Target)o;
        }
        if (o instanceof ASN1TaggedObject) {
            return new Target((ASN1TaggedObject)o);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("unknown object in factory: ");
        sb.append(o.getClass());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public GeneralName getTargetGroup() {
        return this.targGroup;
    }
    
    public GeneralName getTargetName() {
        return this.targName;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        if (this.targName != null) {
            return new DERTaggedObject(true, 0, this.targName);
        }
        return new DERTaggedObject(true, 1, this.targGroup);
    }
}
