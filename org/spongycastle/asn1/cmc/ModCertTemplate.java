package org.spongycastle.asn1.cmc;

import org.spongycastle.asn1.crmf.*;
import org.spongycastle.asn1.*;

public class ModCertTemplate extends ASN1Object
{
    private final BodyPartList certReferences;
    private final CertTemplate certTemplate;
    private final BodyPartPath pkiDataReference;
    private final boolean replace;
    
    private ModCertTemplate(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() != 4 && asn1Sequence.size() != 3) {
            throw new IllegalArgumentException("incorrect sequence size");
        }
        this.pkiDataReference = BodyPartPath.getInstance(asn1Sequence.getObjectAt(0));
        this.certReferences = BodyPartList.getInstance(asn1Sequence.getObjectAt(1));
        ASN1Encodable asn1Encodable;
        if (asn1Sequence.size() == 4) {
            this.replace = ASN1Boolean.getInstance(asn1Sequence.getObjectAt(2)).isTrue();
            asn1Encodable = asn1Sequence.getObjectAt(3);
        }
        else {
            this.replace = true;
            asn1Encodable = asn1Sequence.getObjectAt(2);
        }
        this.certTemplate = CertTemplate.getInstance(asn1Encodable);
    }
    
    public ModCertTemplate(final BodyPartPath pkiDataReference, final BodyPartList certReferences, final boolean replace, final CertTemplate certTemplate) {
        this.pkiDataReference = pkiDataReference;
        this.certReferences = certReferences;
        this.replace = replace;
        this.certTemplate = certTemplate;
    }
    
    public static ModCertTemplate getInstance(final Object o) {
        if (o instanceof ModCertTemplate) {
            return (ModCertTemplate)o;
        }
        if (o != null) {
            return new ModCertTemplate(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public BodyPartList getCertReferences() {
        return this.certReferences;
    }
    
    public CertTemplate getCertTemplate() {
        return this.certTemplate;
    }
    
    public BodyPartPath getPkiDataReference() {
        return this.pkiDataReference;
    }
    
    public boolean isReplacingFields() {
        return this.replace;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.pkiDataReference);
        asn1EncodableVector.add(this.certReferences);
        final boolean replace = this.replace;
        if (!replace) {
            asn1EncodableVector.add(ASN1Boolean.getInstance(replace));
        }
        asn1EncodableVector.add(this.certTemplate);
        return new DERSequence(asn1EncodableVector);
    }
}
