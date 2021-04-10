package org.spongycastle.asn1.x509;

import java.util.*;
import org.spongycastle.asn1.*;

public class Targets extends ASN1Object
{
    private ASN1Sequence targets;
    
    private Targets(final ASN1Sequence targets) {
        this.targets = targets;
    }
    
    public Targets(final Target[] array) {
        this.targets = new DERSequence(array);
    }
    
    public static Targets getInstance(final Object o) {
        if (o instanceof Targets) {
            return (Targets)o;
        }
        if (o != null) {
            return new Targets(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public Target[] getTargets() {
        final Target[] array = new Target[this.targets.size()];
        final Enumeration objects = this.targets.getObjects();
        int n = 0;
        while (objects.hasMoreElements()) {
            array[n] = Target.getInstance(objects.nextElement());
            ++n;
        }
        return array;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.targets;
    }
}
