package org.spongycastle.asn1.ua;

import org.spongycastle.util.*;
import org.spongycastle.asn1.*;

public class DSTU4145Params extends ASN1Object
{
    private static final byte[] DEFAULT_DKE;
    private byte[] dke;
    private DSTU4145ECBinary ecbinary;
    private ASN1ObjectIdentifier namedCurve;
    
    static {
        DEFAULT_DKE = new byte[] { -87, -42, -21, 69, -15, 60, 112, -126, -128, -60, -106, 123, 35, 31, 94, -83, -10, 88, -21, -92, -64, 55, 41, 29, 56, -39, 107, -16, 37, -54, 78, 23, -8, -23, 114, 13, -58, 21, -76, 58, 40, -105, 95, 11, -63, -34, -93, 100, 56, -75, 100, -22, 44, 23, -97, -48, 18, 62, 109, -72, -6, -59, 121, 4 };
    }
    
    public DSTU4145Params(final ASN1ObjectIdentifier namedCurve) {
        this.dke = DSTU4145Params.DEFAULT_DKE;
        this.namedCurve = namedCurve;
    }
    
    public DSTU4145Params(final ASN1ObjectIdentifier namedCurve, final byte[] array) {
        this.dke = DSTU4145Params.DEFAULT_DKE;
        this.namedCurve = namedCurve;
        this.dke = Arrays.clone(array);
    }
    
    public DSTU4145Params(final DSTU4145ECBinary ecbinary) {
        this.dke = DSTU4145Params.DEFAULT_DKE;
        this.ecbinary = ecbinary;
    }
    
    public static byte[] getDefaultDKE() {
        return DSTU4145Params.DEFAULT_DKE;
    }
    
    public static DSTU4145Params getInstance(final Object o) {
        if (o instanceof DSTU4145Params) {
            return (DSTU4145Params)o;
        }
        if (o == null) {
            throw new IllegalArgumentException("object parse error");
        }
        final ASN1Sequence instance = ASN1Sequence.getInstance(o);
        DSTU4145Params dstu4145Params;
        if (instance.getObjectAt(0) instanceof ASN1ObjectIdentifier) {
            dstu4145Params = new DSTU4145Params(ASN1ObjectIdentifier.getInstance(instance.getObjectAt(0)));
        }
        else {
            dstu4145Params = new DSTU4145Params(DSTU4145ECBinary.getInstance(instance.getObjectAt(0)));
        }
        if (instance.size() != 2) {
            return dstu4145Params;
        }
        final byte[] octets = ASN1OctetString.getInstance(instance.getObjectAt(1)).getOctets();
        dstu4145Params.dke = octets;
        if (octets.length == DSTU4145Params.DEFAULT_DKE.length) {
            return dstu4145Params;
        }
        throw new IllegalArgumentException("object parse error");
    }
    
    public byte[] getDKE() {
        return this.dke;
    }
    
    public DSTU4145ECBinary getECBinary() {
        return this.ecbinary;
    }
    
    public ASN1ObjectIdentifier getNamedCurve() {
        return this.namedCurve;
    }
    
    public boolean isNamedCurve() {
        return this.namedCurve != null;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        ASN1Object asn1Object = this.namedCurve;
        if (asn1Object == null) {
            asn1Object = this.ecbinary;
        }
        asn1EncodableVector.add(asn1Object);
        if (!Arrays.areEqual(this.dke, DSTU4145Params.DEFAULT_DKE)) {
            asn1EncodableVector.add(new DEROctetString(this.dke));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
