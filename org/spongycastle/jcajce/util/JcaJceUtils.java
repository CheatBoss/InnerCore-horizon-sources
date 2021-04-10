package org.spongycastle.jcajce.util;

import java.security.*;
import java.io.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.asn1.oiw.*;
import org.spongycastle.asn1.nist.*;
import org.spongycastle.asn1.teletrust.*;
import org.spongycastle.asn1.cryptopro.*;

public class JcaJceUtils
{
    private JcaJceUtils() {
    }
    
    public static ASN1Encodable extractParameters(final AlgorithmParameters algorithmParameters) throws IOException {
        try {
            return ASN1Primitive.fromByteArray(algorithmParameters.getEncoded("ASN.1"));
        }
        catch (Exception ex) {
            return ASN1Primitive.fromByteArray(algorithmParameters.getEncoded());
        }
    }
    
    public static String getDigestAlgName(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        if (PKCSObjectIdentifiers.md5.equals(asn1ObjectIdentifier)) {
            return "MD5";
        }
        if (OIWObjectIdentifiers.idSHA1.equals(asn1ObjectIdentifier)) {
            return "SHA1";
        }
        if (NISTObjectIdentifiers.id_sha224.equals(asn1ObjectIdentifier)) {
            return "SHA224";
        }
        if (NISTObjectIdentifiers.id_sha256.equals(asn1ObjectIdentifier)) {
            return "SHA256";
        }
        if (NISTObjectIdentifiers.id_sha384.equals(asn1ObjectIdentifier)) {
            return "SHA384";
        }
        if (NISTObjectIdentifiers.id_sha512.equals(asn1ObjectIdentifier)) {
            return "SHA512";
        }
        if (TeleTrusTObjectIdentifiers.ripemd128.equals(asn1ObjectIdentifier)) {
            return "RIPEMD128";
        }
        if (TeleTrusTObjectIdentifiers.ripemd160.equals(asn1ObjectIdentifier)) {
            return "RIPEMD160";
        }
        if (TeleTrusTObjectIdentifiers.ripemd256.equals(asn1ObjectIdentifier)) {
            return "RIPEMD256";
        }
        if (CryptoProObjectIdentifiers.gostR3411.equals(asn1ObjectIdentifier)) {
            return "GOST3411";
        }
        return asn1ObjectIdentifier.getId();
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
