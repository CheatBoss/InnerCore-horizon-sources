package org.spongycastle.jcajce.util;

import java.security.*;
import org.spongycastle.asn1.*;
import java.io.*;

public class AlgorithmParametersUtils
{
    private AlgorithmParametersUtils() {
    }
    
    public static ASN1Encodable extractParameters(final AlgorithmParameters algorithmParameters) throws IOException {
        try {
            return ASN1Primitive.fromByteArray(algorithmParameters.getEncoded("ASN.1"));
        }
        catch (Exception ex) {
            return ASN1Primitive.fromByteArray(algorithmParameters.getEncoded());
        }
    }
    
    public static void loadParameters(final AlgorithmParameters algorithmParameters, final ASN1Encodable asn1Encodable) throws IOException {
        try {
            algorithmParameters.init(asn1Encodable.toASN1Primitive().getEncoded(), "ASN.1");
        }
        catch (Exception ex) {
            algorithmParameters.init(asn1Encodable.toASN1Primitive().getEncoded());
        }
    }
}
