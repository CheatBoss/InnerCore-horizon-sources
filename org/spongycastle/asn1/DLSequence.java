package org.spongycastle.asn1;

import java.util.*;
import java.io.*;

public class DLSequence extends ASN1Sequence
{
    private int bodyLength;
    
    public DLSequence() {
        this.bodyLength = -1;
    }
    
    public DLSequence(final ASN1Encodable asn1Encodable) {
        super(asn1Encodable);
        this.bodyLength = -1;
    }
    
    public DLSequence(final ASN1EncodableVector asn1EncodableVector) {
        super(asn1EncodableVector);
        this.bodyLength = -1;
    }
    
    public DLSequence(final ASN1Encodable[] array) {
        super(array);
        this.bodyLength = -1;
    }
    
    private int getBodyLength() throws IOException {
        if (this.bodyLength < 0) {
            int bodyLength = 0;
            final Enumeration objects = this.getObjects();
            while (objects.hasMoreElements()) {
                bodyLength += objects.nextElement().toASN1Primitive().toDLObject().encodedLength();
            }
            this.bodyLength = bodyLength;
        }
        return this.bodyLength;
    }
    
    @Override
    void encode(final ASN1OutputStream asn1OutputStream) throws IOException {
        final ASN1OutputStream dlSubStream = asn1OutputStream.getDLSubStream();
        final int bodyLength = this.getBodyLength();
        asn1OutputStream.write(48);
        asn1OutputStream.writeLength(bodyLength);
        final Enumeration objects = this.getObjects();
        while (objects.hasMoreElements()) {
            dlSubStream.writeObject(objects.nextElement());
        }
    }
    
    @Override
    int encodedLength() throws IOException {
        final int bodyLength = this.getBodyLength();
        return StreamUtil.calculateBodyLength(bodyLength) + 1 + bodyLength;
    }
}
