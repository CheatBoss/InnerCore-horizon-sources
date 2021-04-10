package org.spongycastle.asn1;

import java.util.*;
import java.io.*;

public class BERTaggedObject extends ASN1TaggedObject
{
    public BERTaggedObject(final int n) {
        super(false, n, new BERSequence());
    }
    
    public BERTaggedObject(final int n, final ASN1Encodable asn1Encodable) {
        super(true, n, asn1Encodable);
    }
    
    public BERTaggedObject(final boolean b, final int n, final ASN1Encodable asn1Encodable) {
        super(b, n, asn1Encodable);
    }
    
    @Override
    void encode(final ASN1OutputStream asn1OutputStream) throws IOException {
        asn1OutputStream.writeTag(160, this.tagNo);
        asn1OutputStream.write(128);
        if (!this.empty) {
            if (!this.explicit) {
                Enumeration enumeration;
                if (this.obj instanceof ASN1OctetString) {
                    BEROctetString berOctetString;
                    if (this.obj instanceof BEROctetString) {
                        berOctetString = (BEROctetString)this.obj;
                    }
                    else {
                        berOctetString = new BEROctetString(((ASN1OctetString)this.obj).getOctets());
                    }
                    enumeration = berOctetString.getObjects();
                }
                else if (this.obj instanceof ASN1Sequence) {
                    enumeration = ((ASN1Sequence)this.obj).getObjects();
                }
                else {
                    if (!(this.obj instanceof ASN1Set)) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("not implemented: ");
                        sb.append(this.obj.getClass().getName());
                        throw new ASN1Exception(sb.toString());
                    }
                    enumeration = ((ASN1Set)this.obj).getObjects();
                }
                while (enumeration.hasMoreElements()) {
                    asn1OutputStream.writeObject(enumeration.nextElement());
                }
            }
            else {
                asn1OutputStream.writeObject(this.obj);
            }
        }
        asn1OutputStream.write(0);
        asn1OutputStream.write(0);
    }
    
    @Override
    int encodedLength() throws IOException {
        if (!this.empty) {
            int encodedLength = this.obj.toASN1Primitive().encodedLength();
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
