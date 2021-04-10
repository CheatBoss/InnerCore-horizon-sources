package org.spongycastle.asn1.dvcs;

import java.math.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class DVCSRequestInformation extends ASN1Object
{
    private static final int DEFAULT_VERSION = 1;
    private static final int TAG_DATA_LOCATIONS = 3;
    private static final int TAG_DVCS = 2;
    private static final int TAG_EXTENSIONS = 4;
    private static final int TAG_REQUESTER = 0;
    private static final int TAG_REQUEST_POLICY = 1;
    private GeneralNames dataLocations;
    private GeneralNames dvcs;
    private Extensions extensions;
    private BigInteger nonce;
    private PolicyInformation requestPolicy;
    private DVCSTime requestTime;
    private GeneralNames requester;
    private ServiceType service;
    private int version;
    
    private DVCSRequestInformation(final ASN1Sequence asn1Sequence) {
        this.version = 1;
        int n;
        if (asn1Sequence.getObjectAt(0) instanceof ASN1Integer) {
            this.version = ASN1Integer.getInstance(asn1Sequence.getObjectAt(0)).getValue().intValue();
            n = 1;
        }
        else {
            this.version = 1;
            n = 0;
        }
        final int n2 = n + 1;
        this.service = ServiceType.getInstance(asn1Sequence.getObjectAt(n));
        for (int i = n2; i < asn1Sequence.size(); ++i) {
            final ASN1Encodable object = asn1Sequence.getObjectAt(i);
            if (object instanceof ASN1Integer) {
                this.nonce = ASN1Integer.getInstance(object).getValue();
            }
            else if (!(object instanceof ASN1GeneralizedTime) && object instanceof ASN1TaggedObject) {
                final ASN1TaggedObject instance = ASN1TaggedObject.getInstance(object);
                final int tagNo = instance.getTagNo();
                if (tagNo != 0) {
                    if (tagNo != 1) {
                        if (tagNo != 2) {
                            if (tagNo != 3) {
                                if (tagNo != 4) {
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append("unknown tag number encountered: ");
                                    sb.append(tagNo);
                                    throw new IllegalArgumentException(sb.toString());
                                }
                                this.extensions = Extensions.getInstance(instance, false);
                            }
                            else {
                                this.dataLocations = GeneralNames.getInstance(instance, false);
                            }
                        }
                        else {
                            this.dvcs = GeneralNames.getInstance(instance, false);
                        }
                    }
                    else {
                        this.requestPolicy = PolicyInformation.getInstance(ASN1Sequence.getInstance(instance, false));
                    }
                }
                else {
                    this.requester = GeneralNames.getInstance(instance, false);
                }
            }
            else {
                this.requestTime = DVCSTime.getInstance(object);
            }
        }
    }
    
    public static DVCSRequestInformation getInstance(final Object o) {
        if (o instanceof DVCSRequestInformation) {
            return (DVCSRequestInformation)o;
        }
        if (o != null) {
            return new DVCSRequestInformation(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static DVCSRequestInformation getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public GeneralNames getDVCS() {
        return this.dvcs;
    }
    
    public GeneralNames getDataLocations() {
        return this.dataLocations;
    }
    
    public Extensions getExtensions() {
        return this.extensions;
    }
    
    public BigInteger getNonce() {
        return this.nonce;
    }
    
    public PolicyInformation getRequestPolicy() {
        return this.requestPolicy;
    }
    
    public DVCSTime getRequestTime() {
        return this.requestTime;
    }
    
    public GeneralNames getRequester() {
        return this.requester;
    }
    
    public ServiceType getService() {
        return this.service;
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
        asn1EncodableVector.add(this.service);
        if (this.nonce != null) {
            asn1EncodableVector.add(new ASN1Integer(this.nonce));
        }
        final DVCSTime requestTime = this.requestTime;
        if (requestTime != null) {
            asn1EncodableVector.add(requestTime);
        }
        final GeneralNames requester = this.requester;
        final PolicyInformation requestPolicy = this.requestPolicy;
        final GeneralNames dvcs = this.dvcs;
        final GeneralNames dataLocations = this.dataLocations;
        final Extensions extensions = this.extensions;
        for (int i = 0; i < 5; ++i) {
            final int n = (new int[] { 0, 1, 2, 3, 4 })[i];
            final ASN1Encodable asn1Encodable = (new ASN1Encodable[] { requester, requestPolicy, dvcs, dataLocations, extensions })[i];
            if (asn1Encodable != null) {
                asn1EncodableVector.add(new DERTaggedObject(false, n, asn1Encodable));
            }
        }
        return new DERSequence(asn1EncodableVector);
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("DVCSRequestInformation {\n");
        if (this.version != 1) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("version: ");
            sb2.append(this.version);
            sb2.append("\n");
            sb.append(sb2.toString());
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("service: ");
        sb3.append(this.service);
        sb3.append("\n");
        sb.append(sb3.toString());
        if (this.nonce != null) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("nonce: ");
            sb4.append(this.nonce);
            sb4.append("\n");
            sb.append(sb4.toString());
        }
        if (this.requestTime != null) {
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("requestTime: ");
            sb5.append(this.requestTime);
            sb5.append("\n");
            sb.append(sb5.toString());
        }
        if (this.requester != null) {
            final StringBuilder sb6 = new StringBuilder();
            sb6.append("requester: ");
            sb6.append(this.requester);
            sb6.append("\n");
            sb.append(sb6.toString());
        }
        if (this.requestPolicy != null) {
            final StringBuilder sb7 = new StringBuilder();
            sb7.append("requestPolicy: ");
            sb7.append(this.requestPolicy);
            sb7.append("\n");
            sb.append(sb7.toString());
        }
        if (this.dvcs != null) {
            final StringBuilder sb8 = new StringBuilder();
            sb8.append("dvcs: ");
            sb8.append(this.dvcs);
            sb8.append("\n");
            sb.append(sb8.toString());
        }
        if (this.dataLocations != null) {
            final StringBuilder sb9 = new StringBuilder();
            sb9.append("dataLocations: ");
            sb9.append(this.dataLocations);
            sb9.append("\n");
            sb.append(sb9.toString());
        }
        if (this.extensions != null) {
            final StringBuilder sb10 = new StringBuilder();
            sb10.append("extensions: ");
            sb10.append(this.extensions);
            sb10.append("\n");
            sb.append(sb10.toString());
        }
        sb.append("}\n");
        return sb.toString();
    }
}
