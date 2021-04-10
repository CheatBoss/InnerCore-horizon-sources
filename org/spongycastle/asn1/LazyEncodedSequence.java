package org.spongycastle.asn1;

import java.io.*;
import java.util.*;

class LazyEncodedSequence extends ASN1Sequence
{
    private byte[] encoded;
    
    LazyEncodedSequence(final byte[] encoded) throws IOException {
        this.encoded = encoded;
    }
    
    private void parse() {
        final LazyConstructionEnumeration lazyConstructionEnumeration = new LazyConstructionEnumeration(this.encoded);
        while (lazyConstructionEnumeration.hasMoreElements()) {
            this.seq.addElement(lazyConstructionEnumeration.nextElement());
        }
        this.encoded = null;
    }
    
    @Override
    void encode(final ASN1OutputStream asn1OutputStream) throws IOException {
        final byte[] encoded = this.encoded;
        if (encoded != null) {
            asn1OutputStream.writeEncoded(48, encoded);
            return;
        }
        super.toDLObject().encode(asn1OutputStream);
    }
    
    @Override
    int encodedLength() throws IOException {
        final byte[] encoded = this.encoded;
        if (encoded != null) {
            return StreamUtil.calculateBodyLength(encoded.length) + 1 + this.encoded.length;
        }
        return super.toDLObject().encodedLength();
    }
    
    @Override
    public ASN1Encodable getObjectAt(final int n) {
        synchronized (this) {
            if (this.encoded != null) {
                this.parse();
            }
            return super.getObjectAt(n);
        }
    }
    
    @Override
    public Enumeration getObjects() {
        synchronized (this) {
            if (this.encoded == null) {
                return super.getObjects();
            }
            return new LazyConstructionEnumeration(this.encoded);
        }
    }
    
    @Override
    public int size() {
        synchronized (this) {
            if (this.encoded != null) {
                this.parse();
            }
            return super.size();
        }
    }
    
    @Override
    ASN1Primitive toDERObject() {
        if (this.encoded != null) {
            this.parse();
        }
        return super.toDERObject();
    }
    
    @Override
    ASN1Primitive toDLObject() {
        if (this.encoded != null) {
            this.parse();
        }
        return super.toDLObject();
    }
}
