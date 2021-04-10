package org.spongycastle.asn1.dvcs;

import java.math.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.util.*;

public class DVCSRequestInformationBuilder
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
    private DVCSRequestInformation initialInfo;
    private BigInteger nonce;
    private PolicyInformation requestPolicy;
    private DVCSTime requestTime;
    private GeneralNames requester;
    private final ServiceType service;
    private int version;
    
    public DVCSRequestInformationBuilder(final DVCSRequestInformation initialInfo) {
        this.version = 1;
        this.initialInfo = initialInfo;
        this.service = initialInfo.getService();
        this.version = initialInfo.getVersion();
        this.nonce = initialInfo.getNonce();
        this.requestTime = initialInfo.getRequestTime();
        this.requestPolicy = initialInfo.getRequestPolicy();
        this.dvcs = initialInfo.getDVCS();
        this.dataLocations = initialInfo.getDataLocations();
    }
    
    public DVCSRequestInformationBuilder(final ServiceType service) {
        this.version = 1;
        this.service = service;
    }
    
    public DVCSRequestInformation build() {
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
        return DVCSRequestInformation.getInstance(new DERSequence(asn1EncodableVector));
    }
    
    public void setDVCS(final GeneralName generalName) {
        this.setDVCS(new GeneralNames(generalName));
    }
    
    public void setDVCS(final GeneralNames dvcs) {
        this.dvcs = dvcs;
    }
    
    public void setDataLocations(final GeneralName generalName) {
        this.setDataLocations(new GeneralNames(generalName));
    }
    
    public void setDataLocations(final GeneralNames dataLocations) {
        this.dataLocations = dataLocations;
    }
    
    public void setExtensions(final Extensions extensions) {
        if (this.initialInfo == null) {
            this.extensions = extensions;
            return;
        }
        throw new IllegalStateException("cannot change extensions in existing DVCSRequestInformation");
    }
    
    public void setNonce(final BigInteger bigInteger) {
        final DVCSRequestInformation initialInfo = this.initialInfo;
        if (initialInfo != null) {
            if (initialInfo.getNonce() == null) {
                this.nonce = bigInteger;
            }
            else {
                final byte[] byteArray = this.initialInfo.getNonce().toByteArray();
                final byte[] unsignedByteArray = BigIntegers.asUnsignedByteArray(bigInteger);
                final byte[] array = new byte[byteArray.length + unsignedByteArray.length];
                System.arraycopy(byteArray, 0, array, 0, byteArray.length);
                System.arraycopy(unsignedByteArray, 0, array, byteArray.length, unsignedByteArray.length);
                this.nonce = new BigInteger(array);
            }
        }
        this.nonce = bigInteger;
    }
    
    public void setRequestPolicy(final PolicyInformation requestPolicy) {
        if (this.initialInfo == null) {
            this.requestPolicy = requestPolicy;
            return;
        }
        throw new IllegalStateException("cannot change request policy in existing DVCSRequestInformation");
    }
    
    public void setRequestTime(final DVCSTime requestTime) {
        if (this.initialInfo == null) {
            this.requestTime = requestTime;
            return;
        }
        throw new IllegalStateException("cannot change request time in existing DVCSRequestInformation");
    }
    
    public void setRequester(final GeneralName generalName) {
        this.setRequester(new GeneralNames(generalName));
    }
    
    public void setRequester(final GeneralNames requester) {
        this.requester = requester;
    }
    
    public void setVersion(final int version) {
        if (this.initialInfo == null) {
            this.version = version;
            return;
        }
        throw new IllegalStateException("cannot change version in existing DVCSRequestInformation");
    }
}
