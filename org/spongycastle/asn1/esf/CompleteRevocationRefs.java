package org.spongycastle.asn1.esf;

import java.util.*;
import org.spongycastle.asn1.*;

public class CompleteRevocationRefs extends ASN1Object
{
    private ASN1Sequence crlOcspRefs;
    
    private CompleteRevocationRefs(final ASN1Sequence crlOcspRefs) {
        final Enumeration objects = crlOcspRefs.getObjects();
        while (objects.hasMoreElements()) {
            CrlOcspRef.getInstance(objects.nextElement());
        }
        this.crlOcspRefs = crlOcspRefs;
    }
    
    public CompleteRevocationRefs(final CrlOcspRef[] array) {
        this.crlOcspRefs = new DERSequence(array);
    }
    
    public static CompleteRevocationRefs getInstance(final Object o) {
        if (o instanceof CompleteRevocationRefs) {
            return (CompleteRevocationRefs)o;
        }
        if (o != null) {
            return new CompleteRevocationRefs(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public CrlOcspRef[] getCrlOcspRefs() {
        final int size = this.crlOcspRefs.size();
        final CrlOcspRef[] array = new CrlOcspRef[size];
        for (int i = 0; i < size; ++i) {
            array[i] = CrlOcspRef.getInstance(this.crlOcspRefs.getObjectAt(i));
        }
        return array;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.crlOcspRefs;
    }
}
