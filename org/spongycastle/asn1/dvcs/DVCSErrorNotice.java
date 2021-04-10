package org.spongycastle.asn1.dvcs;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.cmp.*;
import org.spongycastle.asn1.*;

public class DVCSErrorNotice extends ASN1Object
{
    private GeneralName transactionIdentifier;
    private PKIStatusInfo transactionStatus;
    
    private DVCSErrorNotice(final ASN1Sequence asn1Sequence) {
        this.transactionStatus = PKIStatusInfo.getInstance(asn1Sequence.getObjectAt(0));
        if (asn1Sequence.size() > 1) {
            this.transactionIdentifier = GeneralName.getInstance(asn1Sequence.getObjectAt(1));
        }
    }
    
    public DVCSErrorNotice(final PKIStatusInfo pkiStatusInfo) {
        this(pkiStatusInfo, null);
    }
    
    public DVCSErrorNotice(final PKIStatusInfo transactionStatus, final GeneralName transactionIdentifier) {
        this.transactionStatus = transactionStatus;
        this.transactionIdentifier = transactionIdentifier;
    }
    
    public static DVCSErrorNotice getInstance(final Object o) {
        if (o instanceof DVCSErrorNotice) {
            return (DVCSErrorNotice)o;
        }
        if (o != null) {
            return new DVCSErrorNotice(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static DVCSErrorNotice getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public GeneralName getTransactionIdentifier() {
        return this.transactionIdentifier;
    }
    
    public PKIStatusInfo getTransactionStatus() {
        return this.transactionStatus;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.transactionStatus);
        final GeneralName transactionIdentifier = this.transactionIdentifier;
        if (transactionIdentifier != null) {
            asn1EncodableVector.add(transactionIdentifier);
        }
        return new DERSequence(asn1EncodableVector);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("DVCSErrorNotice {\ntransactionStatus: ");
        sb.append(this.transactionStatus);
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
