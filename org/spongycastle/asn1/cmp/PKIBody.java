package org.spongycastle.asn1.cmp;

import org.spongycastle.asn1.crmf.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.asn1.*;

public class PKIBody extends ASN1Object implements ASN1Choice
{
    public static final int TYPE_CA_KEY_UPDATE_ANN = 15;
    public static final int TYPE_CERT_ANN = 16;
    public static final int TYPE_CERT_CONFIRM = 24;
    public static final int TYPE_CERT_REP = 3;
    public static final int TYPE_CERT_REQ = 2;
    public static final int TYPE_CONFIRM = 19;
    public static final int TYPE_CRL_ANN = 18;
    public static final int TYPE_CROSS_CERT_REP = 14;
    public static final int TYPE_CROSS_CERT_REQ = 13;
    public static final int TYPE_ERROR = 23;
    public static final int TYPE_GEN_MSG = 21;
    public static final int TYPE_GEN_REP = 22;
    public static final int TYPE_INIT_REP = 1;
    public static final int TYPE_INIT_REQ = 0;
    public static final int TYPE_KEY_RECOVERY_REP = 10;
    public static final int TYPE_KEY_RECOVERY_REQ = 9;
    public static final int TYPE_KEY_UPDATE_REP = 8;
    public static final int TYPE_KEY_UPDATE_REQ = 7;
    public static final int TYPE_NESTED = 20;
    public static final int TYPE_P10_CERT_REQ = 4;
    public static final int TYPE_POLL_REP = 26;
    public static final int TYPE_POLL_REQ = 25;
    public static final int TYPE_POPO_CHALL = 5;
    public static final int TYPE_POPO_REP = 6;
    public static final int TYPE_REVOCATION_ANN = 17;
    public static final int TYPE_REVOCATION_REP = 12;
    public static final int TYPE_REVOCATION_REQ = 11;
    private ASN1Encodable body;
    private int tagNo;
    
    public PKIBody(final int tagNo, final ASN1Encodable asn1Encodable) {
        this.tagNo = tagNo;
        this.body = getBodyForType(tagNo, asn1Encodable);
    }
    
    private PKIBody(final ASN1TaggedObject asn1TaggedObject) {
        final int tagNo = asn1TaggedObject.getTagNo();
        this.tagNo = tagNo;
        this.body = getBodyForType(tagNo, asn1TaggedObject.getObject());
    }
    
    private static ASN1Encodable getBodyForType(final int n, final ASN1Encodable asn1Encodable) {
        switch (n) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("unknown tag number: ");
                sb.append(n);
                throw new IllegalArgumentException(sb.toString());
            }
            case 26: {
                return PollRepContent.getInstance(asn1Encodable);
            }
            case 25: {
                return PollReqContent.getInstance(asn1Encodable);
            }
            case 24: {
                return CertConfirmContent.getInstance(asn1Encodable);
            }
            case 23: {
                return ErrorMsgContent.getInstance(asn1Encodable);
            }
            case 22: {
                return GenRepContent.getInstance(asn1Encodable);
            }
            case 21: {
                return GenMsgContent.getInstance(asn1Encodable);
            }
            case 20: {
                return PKIMessages.getInstance(asn1Encodable);
            }
            case 19: {
                return PKIConfirmContent.getInstance(asn1Encodable);
            }
            case 18: {
                return CRLAnnContent.getInstance(asn1Encodable);
            }
            case 17: {
                return RevAnnContent.getInstance(asn1Encodable);
            }
            case 16: {
                return CMPCertificate.getInstance(asn1Encodable);
            }
            case 15: {
                return CAKeyUpdAnnContent.getInstance(asn1Encodable);
            }
            case 14: {
                return CertRepMessage.getInstance(asn1Encodable);
            }
            case 13: {
                return CertReqMessages.getInstance(asn1Encodable);
            }
            case 12: {
                return RevRepContent.getInstance(asn1Encodable);
            }
            case 11: {
                return RevReqContent.getInstance(asn1Encodable);
            }
            case 10: {
                return KeyRecRepContent.getInstance(asn1Encodable);
            }
            case 9: {
                return CertReqMessages.getInstance(asn1Encodable);
            }
            case 8: {
                return CertRepMessage.getInstance(asn1Encodable);
            }
            case 7: {
                return CertReqMessages.getInstance(asn1Encodable);
            }
            case 6: {
                return POPODecKeyRespContent.getInstance(asn1Encodable);
            }
            case 5: {
                return POPODecKeyChallContent.getInstance(asn1Encodable);
            }
            case 4: {
                return CertificationRequest.getInstance(asn1Encodable);
            }
            case 3: {
                return CertRepMessage.getInstance(asn1Encodable);
            }
            case 2: {
                return CertReqMessages.getInstance(asn1Encodable);
            }
            case 1: {
                return CertRepMessage.getInstance(asn1Encodable);
            }
            case 0: {
                return CertReqMessages.getInstance(asn1Encodable);
            }
        }
    }
    
    public static PKIBody getInstance(final Object o) {
        if (o == null || o instanceof PKIBody) {
            return (PKIBody)o;
        }
        if (o instanceof ASN1TaggedObject) {
            return new PKIBody((ASN1TaggedObject)o);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Invalid object: ");
        sb.append(o.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public ASN1Encodable getContent() {
        return this.body;
    }
    
    public int getType() {
        return this.tagNo;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return new DERTaggedObject(true, this.tagNo, this.body);
    }
}
