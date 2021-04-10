package org.spongycastle.asn1.smime;

import org.spongycastle.asn1.*;

public class SMIMECapabilityVector
{
    private ASN1EncodableVector capabilities;
    
    public SMIMECapabilityVector() {
        this.capabilities = new ASN1EncodableVector();
    }
    
    public void addCapability(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        this.capabilities.add(new DERSequence(asn1ObjectIdentifier));
    }
    
    public void addCapability(final ASN1ObjectIdentifier asn1ObjectIdentifier, final int n) {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(asn1ObjectIdentifier);
        asn1EncodableVector.add(new ASN1Integer(n));
        this.capabilities.add(new DERSequence(asn1EncodableVector));
    }
    
    public void addCapability(final ASN1ObjectIdentifier asn1ObjectIdentifier, final ASN1Encodable asn1Encodable) {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(asn1ObjectIdentifier);
        asn1EncodableVector.add(asn1Encodable);
        this.capabilities.add(new DERSequence(asn1EncodableVector));
    }
    
    public ASN1EncodableVector toASN1EncodableVector() {
        return this.capabilities;
    }
}
