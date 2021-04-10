package org.spongycastle.asn1.dvcs;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class DVCSRequest extends ASN1Object
{
    private Data data;
    private DVCSRequestInformation requestInformation;
    private GeneralName transactionIdentifier;
    
    private DVCSRequest(final ASN1Sequence asn1Sequence) {
        this.requestInformation = DVCSRequestInformation.getInstance(asn1Sequence.getObjectAt(0));
        this.data = Data.getInstance(asn1Sequence.getObjectAt(1));
        if (asn1Sequence.size() > 2) {
            this.transactionIdentifier = GeneralName.getInstance(asn1Sequence.getObjectAt(2));
        }
    }
    
    public DVCSRequest(final DVCSRequestInformation dvcsRequestInformation, final Data data) {
        this(dvcsRequestInformation, data, null);
    }
    
    public DVCSRequest(final DVCSRequestInformation requestInformation, final Data data, final GeneralName transactionIdentifier) {
        this.requestInformation = requestInformation;
        this.data = data;
        this.transactionIdentifier = transactionIdentifier;
    }
    
    public static DVCSRequest getInstance(final Object o) {
        if (o instanceof DVCSRequest) {
            return (DVCSRequest)o;
        }
        if (o != null) {
            return new DVCSRequest(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static DVCSRequest getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public Data getData() {
        return this.data;
    }
    
    public DVCSRequestInformation getRequestInformation() {
        return this.requestInformation;
    }
    
    public GeneralName getTransactionIdentifier() {
        return this.transactionIdentifier;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.requestInformation);
        asn1EncodableVector.add(this.data);
        final GeneralName transactionIdentifier = this.transactionIdentifier;
        if (transactionIdentifier != null) {
            asn1EncodableVector.add(transactionIdentifier);
        }
        return new DERSequence(asn1EncodableVector);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("DVCSRequest {\nrequestInformation: ");
        sb.append(this.requestInformation);
        sb.append("\ndata: ");
        sb.append(this.data);
        sb.append("\n");
        String string;
        if (this.transactionIdentifier != null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("transactionIdentifier: ");
            sb2.append(this.transactionIdentifier);
            sb2.append("\n");
            string = sb2.toString();
        }
        else {
            string = "";
        }
        sb.append(string);
        sb.append("}\n");
        return sb.toString();
    }
}
