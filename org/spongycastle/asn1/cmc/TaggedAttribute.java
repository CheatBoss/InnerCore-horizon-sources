package org.spongycastle.asn1.cmc;

import org.spongycastle.asn1.*;

public class TaggedAttribute extends ASN1Object
{
    private final ASN1ObjectIdentifier attrType;
    private final ASN1Set attrValues;
    private final BodyPartID bodyPartID;
    
    private TaggedAttribute(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() == 3) {
            this.bodyPartID = BodyPartID.getInstance(asn1Sequence.getObjectAt(0));
            this.attrType = ASN1ObjectIdentifier.getInstance(asn1Sequence.getObjectAt(1));
            this.attrValues = ASN1Set.getInstance(asn1Sequence.getObjectAt(2));
            return;
        }
        throw new IllegalArgumentException("incorrect sequence size");
    }
    
    public TaggedAttribute(final BodyPartID bodyPartID, final ASN1ObjectIdentifier attrType, final ASN1Set attrValues) {
        this.bodyPartID = bodyPartID;
        this.attrType = attrType;
        this.attrValues = attrValues;
    }
    
    public static TaggedAttribute getInstance(final Object o) {
        if (o instanceof TaggedAttribute) {
            return (TaggedAttribute)o;
        }
        if (o != null) {
            return new TaggedAttribute(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ASN1ObjectIdentifier getAttrType() {
        return this.attrType;
    }
    
    public ASN1Set getAttrValues() {
        return this.attrValues;
    }
    
    public BodyPartID getBodyPartID() {
        return this.bodyPartID;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return new DERSequence(new ASN1Encodable[] { this.bodyPartID, this.attrType, this.attrValues });
    }
}
