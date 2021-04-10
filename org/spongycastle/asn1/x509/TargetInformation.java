package org.spongycastle.asn1.x509;

import java.util.*;
import org.spongycastle.asn1.*;

public class TargetInformation extends ASN1Object
{
    private ASN1Sequence targets;
    
    private TargetInformation(final ASN1Sequence targets) {
        this.targets = targets;
    }
    
    public TargetInformation(final Targets targets) {
        this.targets = new DERSequence(targets);
    }
    
    public TargetInformation(final Target[] array) {
        this(new Targets(array));
    }
    
    public static TargetInformation getInstance(final Object o) {
        if (o instanceof TargetInformation) {
            return (TargetInformation)o;
        }
        if (o != null) {
            return new TargetInformation(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public Targets[] getTargetsObjects() {
        final Targets[] array = new Targets[this.targets.size()];
        final Enumeration objects = this.targets.getObjects();
        int n = 0;
        while (objects.hasMoreElements()) {
            array[n] = Targets.getInstance(objects.nextElement());
            ++n;
        }
        return array;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.targets;
    }
}
