package org.spongycastle.asn1.cmp;

import java.util.*;
import org.spongycastle.asn1.*;

public class KeyRecRepContent extends ASN1Object
{
    private ASN1Sequence caCerts;
    private ASN1Sequence keyPairHist;
    private CMPCertificate newSigCert;
    private PKIStatusInfo status;
    
    private KeyRecRepContent(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.status = PKIStatusInfo.getInstance(objects.nextElement());
        while (objects.hasMoreElements()) {
            final ASN1TaggedObject instance = ASN1TaggedObject.getInstance(objects.nextElement());
            final int tagNo = instance.getTagNo();
            if (tagNo != 0) {
                if (tagNo != 1) {
                    if (tagNo != 2) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("unknown tag number: ");
                        sb.append(instance.getTagNo());
                        throw new IllegalArgumentException(sb.toString());
                    }
                    this.keyPairHist = ASN1Sequence.getInstance(instance.getObject());
                }
                else {
                    this.caCerts = ASN1Sequence.getInstance(instance.getObject());
                }
            }
            else {
                this.newSigCert = CMPCertificate.getInstance(instance.getObject());
            }
        }
    }
    
    private void addOptional(final ASN1EncodableVector asn1EncodableVector, final int n, final ASN1Encodable asn1Encodable) {
        if (asn1Encodable != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, n, asn1Encodable));
        }
    }
    
    public static KeyRecRepContent getInstance(final Object o) {
        if (o instanceof KeyRecRepContent) {
            return (KeyRecRepContent)o;
        }
        if (o != null) {
            return new KeyRecRepContent(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public CMPCertificate[] getCaCerts() {
        final ASN1Sequence caCerts = this.caCerts;
        if (caCerts == null) {
            return null;
        }
        final int size = caCerts.size();
        final CMPCertificate[] array = new CMPCertificate[size];
        for (int i = 0; i != size; ++i) {
            array[i] = CMPCertificate.getInstance(this.caCerts.getObjectAt(i));
        }
        return array;
    }
    
    public CertifiedKeyPair[] getKeyPairHist() {
        final ASN1Sequence keyPairHist = this.keyPairHist;
        if (keyPairHist == null) {
            return null;
        }
        final int size = keyPairHist.size();
        final CertifiedKeyPair[] array = new CertifiedKeyPair[size];
        for (int i = 0; i != size; ++i) {
            array[i] = CertifiedKeyPair.getInstance(this.keyPairHist.getObjectAt(i));
        }
        return array;
    }
    
    public CMPCertificate getNewSigCert() {
        return this.newSigCert;
    }
    
    public PKIStatusInfo getStatus() {
        return this.status;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.status);
        this.addOptional(asn1EncodableVector, 0, this.newSigCert);
        this.addOptional(asn1EncodableVector, 1, this.caCerts);
        this.addOptional(asn1EncodableVector, 2, this.keyPairHist);
        return new DERSequence(asn1EncodableVector);
    }
}
