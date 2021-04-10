package org.spongycastle.asn1.esf;

import java.util.*;
import org.spongycastle.asn1.*;

public class CrlListID extends ASN1Object
{
    private ASN1Sequence crls;
    
    private CrlListID(ASN1Sequence crls) {
        crls = (ASN1Sequence)crls.getObjectAt(0);
        this.crls = crls;
        final Enumeration objects = crls.getObjects();
        while (objects.hasMoreElements()) {
            CrlValidatedID.getInstance(objects.nextElement());
        }
    }
    
    public CrlListID(final CrlValidatedID[] array) {
        this.crls = new DERSequence(array);
    }
    
    public static CrlListID getInstance(final Object o) {
        if (o instanceof CrlListID) {
            return (CrlListID)o;
        }
        if (o != null) {
            return new CrlListID(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public CrlValidatedID[] getCrls() {
        final int size = this.crls.size();
        final CrlValidatedID[] array = new CrlValidatedID[size];
        for (int i = 0; i < size; ++i) {
            array[i] = CrlValidatedID.getInstance(this.crls.getObjectAt(i));
        }
        return array;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return new DERSequence(this.crls);
    }
}
