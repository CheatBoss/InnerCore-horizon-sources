package org.spongycastle.asn1.cmp;

import java.util.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.crmf.*;
import org.spongycastle.asn1.*;

public class RevRepContent extends ASN1Object
{
    private ASN1Sequence crls;
    private ASN1Sequence revCerts;
    private ASN1Sequence status;
    
    private RevRepContent(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.status = ASN1Sequence.getInstance(objects.nextElement());
        while (objects.hasMoreElements()) {
            final ASN1TaggedObject instance = ASN1TaggedObject.getInstance(objects.nextElement());
            if (instance.getTagNo() == 0) {
                this.revCerts = ASN1Sequence.getInstance(instance, true);
            }
            else {
                this.crls = ASN1Sequence.getInstance(instance, true);
            }
        }
    }
    
    private void addOptional(final ASN1EncodableVector asn1EncodableVector, final int n, final ASN1Encodable asn1Encodable) {
        if (asn1Encodable != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, n, asn1Encodable));
        }
    }
    
    public static RevRepContent getInstance(final Object o) {
        if (o instanceof RevRepContent) {
            return (RevRepContent)o;
        }
        if (o != null) {
            return new RevRepContent(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public CertificateList[] getCrls() {
        final ASN1Sequence crls = this.crls;
        if (crls == null) {
            return null;
        }
        final int size = crls.size();
        final CertificateList[] array = new CertificateList[size];
        for (int i = 0; i != size; ++i) {
            array[i] = CertificateList.getInstance(this.crls.getObjectAt(i));
        }
        return array;
    }
    
    public CertId[] getRevCerts() {
        final ASN1Sequence revCerts = this.revCerts;
        if (revCerts == null) {
            return null;
        }
        final int size = revCerts.size();
        final CertId[] array = new CertId[size];
        for (int i = 0; i != size; ++i) {
            array[i] = CertId.getInstance(this.revCerts.getObjectAt(i));
        }
        return array;
    }
    
    public PKIStatusInfo[] getStatus() {
        final int size = this.status.size();
        final PKIStatusInfo[] array = new PKIStatusInfo[size];
        for (int i = 0; i != size; ++i) {
            array[i] = PKIStatusInfo.getInstance(this.status.getObjectAt(i));
        }
        return array;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.status);
        this.addOptional(asn1EncodableVector, 0, this.revCerts);
        this.addOptional(asn1EncodableVector, 1, this.crls);
        return new DERSequence(asn1EncodableVector);
    }
}
