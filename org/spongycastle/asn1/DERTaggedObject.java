package org.spongycastle.asn1;

import java.io.*;

public class DERTaggedObject extends ASN1TaggedObject
{
    private static final byte[] ZERO_BYTES;
    
    static {
        ZERO_BYTES = new byte[0];
    }
    
    public DERTaggedObject(final int n, final ASN1Encodable asn1Encodable) {
        super(true, n, asn1Encodable);
    }
    
    public DERTaggedObject(final boolean b, final int n, final ASN1Encodable asn1Encodable) {
        super(b, n, asn1Encodable);
    }
    
    @Override
    void encode(final ASN1OutputStream asn1OutputStream) throws IOException {
        final boolean empty = this.empty;
        int n = 160;
        if (empty) {
            asn1OutputStream.writeEncoded(160, this.tagNo, DERTaggedObject.ZERO_BYTES);
            return;
        }
        final ASN1Primitive derObject = this.obj.toASN1Primitive().toDERObject();
        if (this.explicit) {
            asn1OutputStream.writeTag(160, this.tagNo);
            asn1OutputStream.writeLength(derObject.encodedLength());
            asn1OutputStream.writeObject(derObject);
            return;
        }
        if (!derObject.isConstructed()) {
            n = 128;
        }
        asn1OutputStream.writeTag(n, this.tagNo);
        asn1OutputStream.writeImplicitObject(derObject);
    }
    
    @Override
    int encodedLength() throws IOException {
        if (!this.empty) {
            int encodedLength = this.obj.toASN1Primitive().toDERObject().encodedLength();
            int calculateTagLength;
            if (this.explicit) {
                calculateTagLength = StreamUtil.calculateTagLength(this.tagNo) + StreamUtil.calculateBodyLength(encodedLength);
            }
            else {
                --encodedLength;
                calculateTagLength = StreamUtil.calculateTagLength(this.tagNo);
            }
            return calculateTagLength + encodedLength;
        }
        return StreamUtil.calculateTagLength(this.tagNo) + 1;
    }
    
    @Override
    boolean isConstructed() {
        return this.empty || this.explicit || this.obj.toASN1Primitive().toDERObject().isConstructed();
    }
}
