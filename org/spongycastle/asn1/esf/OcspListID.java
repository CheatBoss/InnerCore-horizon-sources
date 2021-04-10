package org.spongycastle.asn1.esf;

import java.util.*;
import org.spongycastle.asn1.*;

public class OcspListID extends ASN1Object
{
    private ASN1Sequence ocspResponses;
    
    private OcspListID(ASN1Sequence ocspResponses) {
        if (ocspResponses.size() == 1) {
            ocspResponses = (ASN1Sequence)ocspResponses.getObjectAt(0);
            this.ocspResponses = ocspResponses;
            final Enumeration objects = ocspResponses.getObjects();
            while (objects.hasMoreElements()) {
                OcspResponsesID.getInstance(objects.nextElement());
            }
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Bad sequence size: ");
        sb.append(ocspResponses.size());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public OcspListID(final OcspResponsesID[] array) {
        this.ocspResponses = new DERSequence(array);
    }
    
    public static OcspListID getInstance(final Object o) {
        if (o instanceof OcspListID) {
            return (OcspListID)o;
        }
        if (o != null) {
            return new OcspListID(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public OcspResponsesID[] getOcspResponses() {
        final int size = this.ocspResponses.size();
        final OcspResponsesID[] array = new OcspResponsesID[size];
        for (int i = 0; i < size; ++i) {
            array[i] = OcspResponsesID.getInstance(this.ocspResponses.getObjectAt(i));
        }
        return array;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return new DERSequence(this.ocspResponses);
    }
}
