package org.spongycastle.asn1;

import java.util.*;
import java.io.*;

public class BERSet extends ASN1Set
{
    public BERSet() {
    }
    
    public BERSet(final ASN1Encodable asn1Encodable) {
        super(asn1Encodable);
    }
    
    public BERSet(final ASN1EncodableVector asn1EncodableVector) {
        super(asn1EncodableVector, false);
    }
    
    public BERSet(final ASN1Encodable[] array) {
        super(array, false);
    }
    
    @Override
    void encode(final ASN1OutputStream asn1OutputStream) throws IOException {
        asn1OutputStream.write(49);
        asn1OutputStream.write(128);
        final Enumeration objects = this.getObjects();
        while (objects.hasMoreElements()) {
            asn1OutputStream.writeObject(objects.nextElement());
        }
        asn1OutputStream.write(0);
        asn1OutputStream.write(0);
    }
    
    @Override
    int encodedLength() throws IOException {
        final Enumeration objects = this.getObjects();
        int n = 0;
        while (objects.hasMoreElements()) {
            n += objects.nextElement().toASN1Primitive().encodedLength();
        }
        return n + 2 + 2;
    }
}
