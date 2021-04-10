package org.spongycastle.jcajce.provider.asymmetric.x509;

import org.spongycastle.jcajce.util.*;
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
        final String digestName = MessageDigestUtils.getDigestName(asn1ObjectIdentifier);
        final int index = digestName.indexOf(45);
        if (index > 0 && !digestName.startsWith("SHA3")) {
            final StringBuilder sb = new StringBuilder();
            sb.append(digestName.substring(0, index));
            sb.append(digestName.substring(index + 1));
            return sb.toString();
        }
        return MessageDigestUtils.getDigestName(asn1ObjectIdentifier);
    }
    
    static String getSignatureName(final AlgorithmIdentifier algorithmIdentifier) {
        final ASN1Encodable parameters = algorithmIdentifier.getParameters();
        int i = 0;
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
                sb2.append(getDigestAlgName((ASN1ObjectIdentifier)instance2.getObjectAt(0)));
                sb2.append("withECDSA");
                return sb2.toString();
            }
        }
        final Provider provider = Security.getProvider("SC");
        if (provider != null) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Alg.Alias.Signature.");
            sb3.append(algorithmIdentifier.getAlgorithm().getId());
            final String property = provider.getProperty(sb3.toString());
            if (property != null) {
                return property;
            }
        }
        for (Provider[] providers = Security.getProviders(); i != providers.length; ++i) {
            final Provider provider2 = providers[i];
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Alg.Alias.Signature.");
            sb4.append(algorithmIdentifier.getAlgorithm().getId());
            final String property2 = provider2.getProperty(sb4.toString());
            if (property2 != null) {
                return property2;
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
