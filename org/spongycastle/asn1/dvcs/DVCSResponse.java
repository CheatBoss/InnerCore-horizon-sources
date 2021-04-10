package org.spongycastle.asn1.dvcs;

import java.io.*;
import org.spongycastle.asn1.*;

public class DVCSResponse extends ASN1Object implements ASN1Choice
{
    private DVCSCertInfo dvCertInfo;
    private DVCSErrorNotice dvErrorNote;
    
    public DVCSResponse(final DVCSCertInfo dvCertInfo) {
        this.dvCertInfo = dvCertInfo;
    }
    
    public DVCSResponse(final DVCSErrorNotice dvErrorNote) {
        this.dvErrorNote = dvErrorNote;
    }
    
    public static DVCSResponse getInstance(final Object o) {
        if (o == null || o instanceof DVCSResponse) {
            return (DVCSResponse)o;
        }
        if (o instanceof byte[]) {
            try {
                return getInstance(ASN1Primitive.fromByteArray((byte[])o));
            }
            catch (IOException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("failed to construct sequence from byte[]: ");
                sb.append(ex.getMessage());
                throw new IllegalArgumentException(sb.toString());
            }
        }
        if (o instanceof ASN1Sequence) {
            return new DVCSResponse(DVCSCertInfo.getInstance(o));
        }
        if (o instanceof ASN1TaggedObject) {
            return new DVCSResponse(DVCSErrorNotice.getInstance(ASN1TaggedObject.getInstance(o), false));
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Couldn't convert from object to DVCSResponse: ");
        sb2.append(o.getClass().getName());
        throw new IllegalArgumentException(sb2.toString());
    }
    
    public static DVCSResponse getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public DVCSCertInfo getCertInfo() {
        return this.dvCertInfo;
    }
    
    public DVCSErrorNotice getErrorNotice() {
        return this.dvErrorNote;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final DVCSCertInfo dvCertInfo = this.dvCertInfo;
        if (dvCertInfo != null) {
            return dvCertInfo.toASN1Primitive();
        }
        return new DERTaggedObject(false, 0, this.dvErrorNote);
    }
    
    @Override
    public String toString() {
        StringBuilder sb;
        String s;
        if (this.dvCertInfo != null) {
            sb = new StringBuilder();
            sb.append("DVCSResponse {\ndvCertInfo: ");
            s = this.dvCertInfo.toString();
        }
        else {
            sb = new StringBuilder();
            sb.append("DVCSResponse {\ndvErrorNote: ");
            s = this.dvErrorNote.toString();
        }
        sb.append(s);
        sb.append("}\n");
        return sb.toString();
    }
}
