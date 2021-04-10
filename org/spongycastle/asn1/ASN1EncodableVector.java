package org.spongycastle.asn1;

import java.util.*;

public class ASN1EncodableVector
{
    private final Vector v;
    
    public ASN1EncodableVector() {
        this.v = new Vector();
    }
    
    public void add(final ASN1Encodable asn1Encodable) {
        this.v.addElement(asn1Encodable);
    }
    
    public void addAll(final ASN1EncodableVector asn1EncodableVector) {
        final Enumeration<Object> elements = asn1EncodableVector.v.elements();
        while (elements.hasMoreElements()) {
            this.v.addElement(elements.nextElement());
        }
    }
    
    public ASN1Encodable get(final int n) {
        return this.v.elementAt(n);
    }
    
    public int size() {
        return this.v.size();
    }
}
