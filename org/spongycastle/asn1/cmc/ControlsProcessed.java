package org.spongycastle.asn1.cmc;

import org.spongycastle.asn1.*;

public class ControlsProcessed extends ASN1Object
{
    private final ASN1Sequence bodyPartReferences;
    
    private ControlsProcessed(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() == 1) {
            this.bodyPartReferences = ASN1Sequence.getInstance(asn1Sequence.getObjectAt(0));
            return;
        }
        throw new IllegalArgumentException("incorrect sequence size");
    }
    
    public ControlsProcessed(final BodyPartReference bodyPartReference) {
        this.bodyPartReferences = new DERSequence(bodyPartReference);
    }
    
    public ControlsProcessed(final BodyPartReference[] array) {
        this.bodyPartReferences = new DERSequence(array);
    }
    
    public static ControlsProcessed getInstance(final Object o) {
        if (o instanceof ControlsProcessed) {
            return (ControlsProcessed)o;
        }
        if (o != null) {
            return new ControlsProcessed(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public BodyPartReference[] getBodyList() {
        final BodyPartReference[] array = new BodyPartReference[this.bodyPartReferences.size()];
        for (int i = 0; i != this.bodyPartReferences.size(); ++i) {
            array[i] = BodyPartReference.getInstance(this.bodyPartReferences.getObjectAt(i));
        }
        return array;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return new DERSequence(this.bodyPartReferences);
    }
}
