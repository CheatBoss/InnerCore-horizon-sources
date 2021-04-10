package org.spongycastle.jcajce.provider.asymmetric.dsa;

import org.spongycastle.asn1.*;
import org.spongycastle.asn1.x9.*;
import org.spongycastle.asn1.oiw.*;
import java.math.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.params.*;
import java.security.*;
import java.security.interfaces.*;
import org.spongycastle.asn1.x509.*;

public class DSAUtil
{
    public static final ASN1ObjectIdentifier[] dsaOids;
    
    static {
        dsaOids = new ASN1ObjectIdentifier[] { X9ObjectIdentifiers.id_dsa, OIWObjectIdentifiers.dsaWithSHA1 };
    }
    
    static String generateKeyFingerprint(final BigInteger bigInteger, final DSAParams dsaParams) {
        return new Fingerprint(Arrays.concatenate(bigInteger.toByteArray(), dsaParams.getP().toByteArray(), dsaParams.getQ().toByteArray(), dsaParams.getG().toByteArray())).toString();
    }
    
    public static AsymmetricKeyParameter generatePrivateKeyParameter(final PrivateKey privateKey) throws InvalidKeyException {
        if (privateKey instanceof DSAPrivateKey) {
            final DSAPrivateKey dsaPrivateKey = (DSAPrivateKey)privateKey;
            return new DSAPrivateKeyParameters(dsaPrivateKey.getX(), new DSAParameters(dsaPrivateKey.getParams().getP(), dsaPrivateKey.getParams().getQ(), dsaPrivateKey.getParams().getG()));
        }
        throw new InvalidKeyException("can't identify DSA private key.");
    }
    
    public static AsymmetricKeyParameter generatePublicKeyParameter(final PublicKey publicKey) throws InvalidKeyException {
        if (publicKey instanceof BCDSAPublicKey) {
            return ((BCDSAPublicKey)publicKey).engineGetKeyParameters();
        }
        if (publicKey instanceof DSAPublicKey) {
            return new BCDSAPublicKey((DSAPublicKey)publicKey).engineGetKeyParameters();
        }
        try {
            return new BCDSAPublicKey(SubjectPublicKeyInfo.getInstance(publicKey.getEncoded())).engineGetKeyParameters();
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("can't identify DSA public key: ");
            sb.append(publicKey.getClass().getName());
            throw new InvalidKeyException(sb.toString());
        }
    }
    
    public static boolean isDsaOid(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        int n = 0;
        while (true) {
            final ASN1ObjectIdentifier[] dsaOids = DSAUtil.dsaOids;
            if (n == dsaOids.length) {
                return false;
            }
            if (asn1ObjectIdentifier.equals(dsaOids[n])) {
                return true;
            }
            ++n;
        }
    }
    
    static DSAParameters toDSAParameters(final DSAParams dsaParams) {
        if (dsaParams != null) {
            return new DSAParameters(dsaParams.getP(), dsaParams.getQ(), dsaParams.getG());
        }
        return null;
    }
}
