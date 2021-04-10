package org.spongycastle.asn1.cmc;

import org.spongycastle.asn1.x500.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.util.*;
import java.math.*;
import org.spongycastle.asn1.*;

public class RevokeRequest extends ASN1Object
{
    private DERUTF8String comment;
    private ASN1GeneralizedTime invalidityDate;
    private final X500Name name;
    private ASN1OctetString passphrase;
    private final CRLReason reason;
    private final ASN1Integer serialNumber;
    
    private RevokeRequest(final ASN1Sequence asn1Sequence) {
        final int size = asn1Sequence.size();
        final int n = 3;
        if (size >= 3 && asn1Sequence.size() <= 6) {
            this.name = X500Name.getInstance(asn1Sequence.getObjectAt(0));
            this.serialNumber = ASN1Integer.getInstance(asn1Sequence.getObjectAt(1));
            this.reason = CRLReason.getInstance(asn1Sequence.getObjectAt(2));
            int n2 = n;
            if (asn1Sequence.size() > 3) {
                n2 = n;
                if (asn1Sequence.getObjectAt(3).toASN1Primitive() instanceof ASN1GeneralizedTime) {
                    this.invalidityDate = ASN1GeneralizedTime.getInstance(asn1Sequence.getObjectAt(3));
                    n2 = 4;
                }
            }
            int n3;
            if (asn1Sequence.size() > (n3 = n2)) {
                n3 = n2;
                if (asn1Sequence.getObjectAt(n2).toASN1Primitive() instanceof ASN1OctetString) {
                    this.passphrase = ASN1OctetString.getInstance(asn1Sequence.getObjectAt(n2));
                    n3 = n2 + 1;
                }
            }
            if (asn1Sequence.size() > n3 && asn1Sequence.getObjectAt(n3).toASN1Primitive() instanceof DERUTF8String) {
                this.comment = DERUTF8String.getInstance(asn1Sequence.getObjectAt(n3));
            }
            return;
        }
        throw new IllegalArgumentException("incorrect sequence size");
    }
    
    public RevokeRequest(final X500Name name, final ASN1Integer serialNumber, final CRLReason reason, final ASN1GeneralizedTime invalidityDate, final ASN1OctetString passphrase, final DERUTF8String comment) {
        this.name = name;
        this.serialNumber = serialNumber;
        this.reason = reason;
        this.invalidityDate = invalidityDate;
        this.passphrase = passphrase;
        this.comment = comment;
    }
    
    public static RevokeRequest getInstance(final Object o) {
        if (o instanceof RevokeRequest) {
            return (RevokeRequest)o;
        }
        if (o != null) {
            return new RevokeRequest(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public DERUTF8String getComment() {
        return this.comment;
    }
    
    public ASN1GeneralizedTime getInvalidityDate() {
        return this.invalidityDate;
    }
    
    public X500Name getName() {
        return this.name;
    }
    
    public byte[] getPassPhrase() {
        final ASN1OctetString passphrase = this.passphrase;
        if (passphrase != null) {
            return Arrays.clone(passphrase.getOctets());
        }
        return null;
    }
    
    public ASN1OctetString getPassphrase() {
        return this.passphrase;
    }
    
    public CRLReason getReason() {
        return this.reason;
    }
    
    public BigInteger getSerialNumber() {
        return this.serialNumber.getValue();
    }
    
    public void setComment(final DERUTF8String comment) {
        this.comment = comment;
    }
    
    public void setInvalidityDate(final ASN1GeneralizedTime invalidityDate) {
        this.invalidityDate = invalidityDate;
    }
    
    public void setPassphrase(final ASN1OctetString passphrase) {
        this.passphrase = passphrase;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.name);
        asn1EncodableVector.add(this.serialNumber);
        asn1EncodableVector.add(this.reason);
        final ASN1GeneralizedTime invalidityDate = this.invalidityDate;
        if (invalidityDate != null) {
            asn1EncodableVector.add(invalidityDate);
        }
        final ASN1OctetString passphrase = this.passphrase;
        if (passphrase != null) {
            asn1EncodableVector.add(passphrase);
        }
        final DERUTF8String comment = this.comment;
        if (comment != null) {
            asn1EncodableVector.add(comment);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
