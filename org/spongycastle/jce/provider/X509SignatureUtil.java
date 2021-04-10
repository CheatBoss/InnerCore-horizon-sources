package org.spongycastle.jce.provider;

import org.spongycastle.asn1.oiw.*;
import org.spongycastle.asn1.nist.*;
import org.spongycastle.asn1.teletrust.*;
import org.spongycastle.asn1.cryptopro.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.asn1.x9.*;
import org.spongycastle.asn1.*;
import java.security.spec.*;
import java.io.*;
import java.security.*;

class X509SignatureUtil
{
    private static final ASN1Null derNull;
    
    static {
        derNull = DERNull.INSTANCE;
    }
    
    private static String getDigestAlgName(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
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
    
    static String getSignatureName(final AlgorithmIdentifier algorithmIdentifier) {
        final ASN1Encodable parameters = algorithmIdentifier.getParameters();
        if (parameters != null && !X509SignatureUtil.derNull.equals(parameters)) {
            if (algorithmIdentifier.getAlgorithm().equals(PKCSObjectIdentifiers.id_RSASSA_PSS)) {
                final RSASSAPSSparams instance = RSASSAPSSparams.getInstance(parameters);
                final StringBuilder sb = new StringBuilder();
                sb.append(getDigestAlgName(instance.getHashAlgorithm().getAlgorithm()));
                sb.append("withRSAandMGF1");
                return sb.toString();
            }
            if (algorithmIdentifier.getAlgorithm().equals(X9ObjectIdentifiers.ecdsa_with_SHA2)) {
                final ASN1Sequence instance2 = ASN1Sequence.getInstance(parameters);
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(getDigestAlgName(ASN1ObjectIdentifier.getInstance(instance2.getObjectAt(0))));
                sb2.append("withECDSA");
                return sb2.toString();
            }
        }
        return algorithmIdentifier.getAlgorithm().getId();
    }
    
    static void setSignatureParameters(final Signature signature, final ASN1Encodable asn1Encodable) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        if (asn1Encodable != null && !X509SignatureUtil.derNull.equals(asn1Encodable)) {
            final AlgorithmParameters instance = AlgorithmParameters.getInstance(signature.getAlgorithm(), signature.getProvider());
            try {
                instance.init(asn1Encodable.toASN1Primitive().getEncoded());
                if (signature.getAlgorithm().endsWith("MGF1")) {
                    try {
                        signature.setParameter(instance.getParameterSpec(PSSParameterSpec.class));
                    }
                    catch (GeneralSecurityException ex) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Exception extracting parameters: ");
                        sb.append(ex.getMessage());
                        throw new SignatureException(sb.toString());
                    }
                }
            }
            catch (IOException ex2) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("IOException decoding parameters: ");
                sb2.append(ex2.getMessage());
                throw new SignatureException(sb2.toString());
            }
        }
    }
}
