package org.spongycastle.jcajce.provider.asymmetric.rsa;

import org.spongycastle.asn1.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.asn1.x509.*;
import java.math.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.params.*;
import java.security.interfaces.*;

public class RSAUtil
{
    public static final ASN1ObjectIdentifier[] rsaOids;
    
    static {
        rsaOids = new ASN1ObjectIdentifier[] { PKCSObjectIdentifiers.rsaEncryption, X509ObjectIdentifiers.id_ea_rsa, PKCSObjectIdentifiers.id_RSAES_OAEP, PKCSObjectIdentifiers.id_RSASSA_PSS };
    }
    
    static String generateKeyFingerprint(final BigInteger bigInteger, final BigInteger bigInteger2) {
        return new Fingerprint(Arrays.concatenate(bigInteger.toByteArray(), bigInteger2.toByteArray())).toString();
    }
    
    static RSAKeyParameters generatePrivateKeyParameter(final RSAPrivateKey rsaPrivateKey) {
        if (rsaPrivateKey instanceof RSAPrivateCrtKey) {
            final RSAPrivateCrtKey rsaPrivateCrtKey = (RSAPrivateCrtKey)rsaPrivateKey;
            return new RSAPrivateCrtKeyParameters(rsaPrivateCrtKey.getModulus(), rsaPrivateCrtKey.getPublicExponent(), rsaPrivateCrtKey.getPrivateExponent(), rsaPrivateCrtKey.getPrimeP(), rsaPrivateCrtKey.getPrimeQ(), rsaPrivateCrtKey.getPrimeExponentP(), rsaPrivateCrtKey.getPrimeExponentQ(), rsaPrivateCrtKey.getCrtCoefficient());
        }
        return new RSAKeyParameters(true, rsaPrivateKey.getModulus(), rsaPrivateKey.getPrivateExponent());
    }
    
    static RSAKeyParameters generatePublicKeyParameter(final RSAPublicKey rsaPublicKey) {
        return new RSAKeyParameters(false, rsaPublicKey.getModulus(), rsaPublicKey.getPublicExponent());
    }
    
    public static boolean isRsaOid(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        int n = 0;
        while (true) {
            final ASN1ObjectIdentifier[] rsaOids = RSAUtil.rsaOids;
            if (n == rsaOids.length) {
                return false;
            }
            if (asn1ObjectIdentifier.equals(rsaOids[n])) {
                return true;
            }
            ++n;
        }
    }
}
