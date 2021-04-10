package org.spongycastle.asn1.dvcs;

import org.spongycastle.asn1.smime.*;
import org.spongycastle.asn1.ocsp.*;
import org.spongycastle.asn1.cms.*;
import org.spongycastle.asn1.cmp.*;
import org.spongycastle.asn1.ess.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class CertEtcToken extends ASN1Object implements ASN1Choice
{
    public static final int TAG_ASSERTION = 3;
    public static final int TAG_CAPABILITIES = 8;
    public static final int TAG_CERTIFICATE = 0;
    public static final int TAG_CRL = 4;
    public static final int TAG_ESSCERTID = 1;
    public static final int TAG_OCSPCERTID = 6;
    public static final int TAG_OCSPCERTSTATUS = 5;
    public static final int TAG_OCSPRESPONSE = 7;
    public static final int TAG_PKISTATUS = 2;
    private static final boolean[] explicit;
    private Extension extension;
    private int tagNo;
    private ASN1Encodable value;
    
    static {
        explicit = new boolean[] { false, true, false, true, false, true, false, false, true };
    }
    
    public CertEtcToken(final int tagNo, final ASN1Encodable value) {
        this.tagNo = tagNo;
        this.value = value;
    }
    
    private CertEtcToken(final ASN1TaggedObject asn1TaggedObject) {
        ASN1Object value = null;
        switch (this.tagNo = asn1TaggedObject.getTagNo()) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unknown tag: ");
                sb.append(this.tagNo);
                throw new IllegalArgumentException(sb.toString());
            }
            case 8: {
                value = SMIMECapabilities.getInstance(asn1TaggedObject.getObject());
                break;
            }
            case 7: {
                value = OCSPResponse.getInstance(asn1TaggedObject, false);
                break;
            }
            case 6: {
                value = CertID.getInstance(asn1TaggedObject, false);
                break;
            }
            case 5: {
                value = CertStatus.getInstance(asn1TaggedObject.getObject());
                break;
            }
            case 4: {
                value = CertificateList.getInstance(asn1TaggedObject, false);
                break;
            }
            case 3: {
                value = ContentInfo.getInstance(asn1TaggedObject.getObject());
                break;
            }
            case 2: {
                value = PKIStatusInfo.getInstance(asn1TaggedObject, false);
                break;
            }
            case 1: {
                value = ESSCertID.getInstance(asn1TaggedObject.getObject());
                break;
            }
            case 0: {
                value = Certificate.getInstance(asn1TaggedObject, false);
                break;
            }
        }
        this.value = value;
    }
    
    public CertEtcToken(final Extension extension) {
        this.tagNo = -1;
        this.extension = extension;
    }
    
    public static CertEtcToken[] arrayFromSequence(final ASN1Sequence asn1Sequence) {
        final int size = asn1Sequence.size();
        final CertEtcToken[] array = new CertEtcToken[size];
        for (int i = 0; i != size; ++i) {
            array[i] = getInstance(asn1Sequence.getObjectAt(i));
        }
        return array;
    }
    
    public static CertEtcToken getInstance(final Object o) {
        if (o instanceof CertEtcToken) {
            return (CertEtcToken)o;
        }
        if (o instanceof ASN1TaggedObject) {
            return new CertEtcToken((ASN1TaggedObject)o);
        }
        if (o != null) {
            return new CertEtcToken(Extension.getInstance(o));
        }
        return null;
    }
    
    public Extension getExtension() {
        return this.extension;
    }
    
    public int getTagNo() {
        return this.tagNo;
    }
    
    public ASN1Encodable getValue() {
        return this.value;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final Extension extension = this.extension;
        if (extension == null) {
            final boolean[] explicit = CertEtcToken.explicit;
            final int tagNo = this.tagNo;
            return new DERTaggedObject(explicit[tagNo], tagNo, this.value);
        }
        return extension.toASN1Primitive();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("CertEtcToken {\n");
        sb.append(this.value);
        sb.append("}\n");
        return sb.toString();
    }
}
