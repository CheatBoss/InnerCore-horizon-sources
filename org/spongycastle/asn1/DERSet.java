package org.spongycastle.asn1;

import java.util.*;
import java.io.*;

public class DERSet extends ASN1Set
{
    private int bodyLength;
    
    public DERSet() {
        this.bodyLength = -1;
    }
    
    public DERSet(final ASN1Encodable asn1Encodable) {
        super(asn1Encodable);
        this.bodyLength = -1;
    }
    
    public DERSet(final ASN1EncodableVector asn1EncodableVector) {
        super(asn1EncodableVector, true);
        this.bodyLength = -1;
    }
    
    DERSet(final ASN1EncodableVector asn1EncodableVector, final boolean b) {
        super(asn1EncodableVector, b);
        this.bodyLength = -1;
    }
    
    public DERSet(final ASN1Encodable[] array) {
        super(array, true);
        this.bodyLength = -1;
    }
    
    private int getBodyLength() throws IOException {
        if (this.bodyLength < 0) {
            int bodyLength = 0;
            final Enumeration objects = this.getObjects();
            while (objects.hasMoreElements()) {
                bodyLength += objects.nextElement().toASN1Primitive().toDERObject().encodedLength();
            }
            this.bodyLength = bodyLength;
        }
        return this.bodyLength;
    }
    
    @Override
    void encode(final ASN1OutputStream asn1OutputStream) throws IOException {
        final ASN1OutputStream derSubStream = asn1OutputStream.getDERSubStream();
        final int bodyLength = this.getBodyLength();
        asn1OutputStream.write(49);
        asn1OutputStream.writeLength(bodyLength);
        final Enumeration objects = this.getObjects();
        while (objects.hasMoreElements()) {
            derSubStream.writeObject(objects.nextElement());
        }
    }
    
    @Override
    int encodedLength() throws IOException {
        final int bodyLength = this.getBodyLength();
        return StreamUtil.calculateBodyLength(bodyLength) + 1 + bodyLength;
    }
}
