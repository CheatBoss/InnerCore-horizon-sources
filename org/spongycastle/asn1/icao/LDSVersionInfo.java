package org.spongycastle.asn1.icao;

import org.spongycastle.asn1.*;

public class LDSVersionInfo extends ASN1Object
{
    private DERPrintableString ldsVersion;
    private DERPrintableString unicodeVersion;
    
    public LDSVersionInfo(final String s, final String s2) {
        this.ldsVersion = new DERPrintableString(s);
        this.unicodeVersion = new DERPrintableString(s2);
    }
    
    private LDSVersionInfo(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() == 2) {
            this.ldsVersion = DERPrintableString.getInstance(asn1Sequence.getObjectAt(0));
            this.unicodeVersion = DERPrintableString.getInstance(asn1Sequence.getObjectAt(1));
            return;
        }
        throw new IllegalArgumentException("sequence wrong size for LDSVersionInfo");
    }
    
    public static LDSVersionInfo getInstance(final Object o) {
        if (o instanceof LDSVersionInfo) {
            return (LDSVersionInfo)o;
        }
        if (o != null) {
            return new LDSVersionInfo(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public String getLdsVersion() {
        return this.ldsVersion.getString();
    }
    
    public String getUnicodeVersion() {
        return this.unicodeVersion.getString();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.ldsVersion);
        asn1EncodableVector.add(this.unicodeVersion);
        return new DERSequence(asn1EncodableVector);
    }
}
