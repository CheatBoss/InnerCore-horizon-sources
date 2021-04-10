package org.spongycastle.asn1.dvcs;

import org.spongycastle.asn1.cmp.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class DVCSCertInfo extends ASN1Object
{
    private static final int DEFAULT_VERSION = 1;
    private static final int TAG_CERTS = 3;
    private static final int TAG_DV_STATUS = 0;
    private static final int TAG_POLICY = 1;
    private static final int TAG_REQ_SIGNATURE = 2;
    private ASN1Sequence certs;
    private DVCSRequestInformation dvReqInfo;
    private PKIStatusInfo dvStatus;
    private Extensions extensions;
    private DigestInfo messageImprint;
    private PolicyInformation policy;
    private ASN1Set reqSignature;
    private DVCSTime responseTime;
    private ASN1Integer serialNumber;
    private int version;
    
    private DVCSCertInfo(final ASN1Sequence asn1Sequence) {
        this.version = 1;
        ASN1Encodable asn1Encodable = asn1Sequence.getObjectAt(0);
        int n;
        try {
            this.version = ASN1Integer.getInstance(asn1Encodable).getValue().intValue();
            try {
                asn1Encodable = asn1Sequence.getObjectAt(1);
            }
            catch (IllegalArgumentException ex) {}
            n = 2;
        }
        catch (IllegalArgumentException ex2) {
            n = 1;
        }
        this.dvReqInfo = DVCSRequestInformation.getInstance(asn1Encodable);
        final int n2 = n + 1;
        this.messageImprint = DigestInfo.getInstance(asn1Sequence.getObjectAt(n));
        final int n3 = n2 + 1;
        this.serialNumber = ASN1Integer.getInstance(asn1Sequence.getObjectAt(n2));
        int i = n3 + 1;
        this.responseTime = DVCSTime.getInstance(asn1Sequence.getObjectAt(n3));
        while (i < asn1Sequence.size()) {
            final ASN1Encodable object = asn1Sequence.getObjectAt(i);
            if (object instanceof ASN1TaggedObject) {
                final ASN1TaggedObject instance = ASN1TaggedObject.getInstance(object);
                final int tagNo = instance.getTagNo();
                if (tagNo != 0) {
                    if (tagNo != 1) {
                        if (tagNo != 2) {
                            if (tagNo != 3) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("Unknown tag encountered: ");
                                sb.append(tagNo);
                                throw new IllegalArgumentException(sb.toString());
                            }
                            this.certs = ASN1Sequence.getInstance(instance, false);
                        }
                        else {
                            this.reqSignature = ASN1Set.getInstance(instance, false);
                        }
                    }
                    else {
                        this.policy = PolicyInformation.getInstance(ASN1Sequence.getInstance(instance, false));
                    }
                }
                else {
                    this.dvStatus = PKIStatusInfo.getInstance(instance, false);
                }
            }
            else {
                try {
                    this.extensions = Extensions.getInstance(object);
                }
                catch (IllegalArgumentException ex3) {}
            }
            ++i;
        }
    }
    
    public DVCSCertInfo(final DVCSRequestInformation dvReqInfo, final DigestInfo messageImprint, final ASN1Integer serialNumber, final DVCSTime responseTime) {
        this.version = 1;
        this.dvReqInfo = dvReqInfo;
        this.messageImprint = messageImprint;
        this.serialNumber = serialNumber;
        this.responseTime = responseTime;
    }
    
    public static DVCSCertInfo getInstance(final Object o) {
        if (o instanceof DVCSCertInfo) {
            return (DVCSCertInfo)o;
        }
        if (o != null) {
            return new DVCSCertInfo(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static DVCSCertInfo getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    private void setDvReqInfo(final DVCSRequestInformation dvReqInfo) {
        this.dvReqInfo = dvReqInfo;
    }
    
    private void setMessageImprint(final DigestInfo messageImprint) {
        this.messageImprint = messageImprint;
    }
    
    private void setVersion(final int version) {
        this.version = version;
    }
    
    public TargetEtcChain[] getCerts() {
        final ASN1Sequence certs = this.certs;
        if (certs != null) {
            return TargetEtcChain.arrayFromSequence(certs);
        }
        return null;
    }
    
    public DVCSRequestInformation getDvReqInfo() {
        return this.dvReqInfo;
    }
    
    public PKIStatusInfo getDvStatus() {
        return this.dvStatus;
    }
    
    public Extensions getExtensions() {
        return this.extensions;
    }
    
    public DigestInfo getMessageImprint() {
        return this.messageImprint;
    }
    
    public PolicyInformation getPolicy() {
        return this.policy;
    }
    
    public ASN1Set getReqSignature() {
        return this.reqSignature;
    }
    
    public DVCSTime getResponseTime() {
        return this.responseTime;
    }
    
    public ASN1Integer getSerialNumber() {
        return this.serialNumber;
    }
    
    public int getVersion() {
        return this.version;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.version != 1) {
            asn1EncodableVector.add(new ASN1Integer(this.version));
        }
        asn1EncodableVector.add(this.dvReqInfo);
        asn1EncodableVector.add(this.messageImprint);
        asn1EncodableVector.add(this.serialNumber);
        asn1EncodableVector.add(this.responseTime);
        if (this.dvStatus != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 0, this.dvStatus));
        }
        if (this.policy != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 1, this.policy));
        }
        if (this.reqSignature != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 2, this.reqSignature));
        }
        if (this.certs != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 3, this.certs));
        }
        final Extensions extensions = this.extensions;
        if (extensions != null) {
            asn1EncodableVector.add(extensions);
        }
        return new DERSequence(asn1EncodableVector);
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("DVCSCertInfo {\n");
        if (this.version != 1) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("version: ");
            sb2.append(this.version);
            sb2.append("\n");
            sb.append(sb2.toString());
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("dvReqInfo: ");
        sb3.append(this.dvReqInfo);
        sb3.append("\n");
        sb.append(sb3.toString());
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("messageImprint: ");
        sb4.append(this.messageImprint);
        sb4.append("\n");
        sb.append(sb4.toString());
        final StringBuilder sb5 = new StringBuilder();
        sb5.append("serialNumber: ");
        sb5.append(this.serialNumber);
        sb5.append("\n");
        sb.append(sb5.toString());
        final StringBuilder sb6 = new StringBuilder();
        sb6.append("responseTime: ");
        sb6.append(this.responseTime);
        sb6.append("\n");
        sb.append(sb6.toString());
        if (this.dvStatus != null) {
            final StringBuilder sb7 = new StringBuilder();
            sb7.append("dvStatus: ");
            sb7.append(this.dvStatus);
            sb7.append("\n");
            sb.append(sb7.toString());
        }
        if (this.policy != null) {
            final StringBuilder sb8 = new StringBuilder();
            sb8.append("policy: ");
            sb8.append(this.policy);
            sb8.append("\n");
            sb.append(sb8.toString());
        }
        if (this.reqSignature != null) {
            final StringBuilder sb9 = new StringBuilder();
            sb9.append("reqSignature: ");
            sb9.append(this.reqSignature);
            sb9.append("\n");
            sb.append(sb9.toString());
        }
        if (this.certs != null) {
            final StringBuilder sb10 = new StringBuilder();
            sb10.append("certs: ");
            sb10.append(this.certs);
            sb10.append("\n");
            sb.append(sb10.toString());
        }
        if (this.extensions != null) {
            final StringBuilder sb11 = new StringBuilder();
            sb11.append("extensions: ");
            sb11.append(this.extensions);
            sb11.append("\n");
            sb.append(sb11.toString());
        }
        sb.append("}\n");
        return sb.toString();
    }
}
