package org.spongycastle.jcajce.provider.asymmetric.ec;

import org.spongycastle.crypto.params.*;
import java.security.*;
import java.security.spec.*;
import org.spongycastle.asn1.x9.*;
import org.spongycastle.jce.spec.*;
import org.spongycastle.asn1.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import java.math.*;
import org.spongycastle.math.ec.*;
import java.io.*;

class ECUtils
{
    static AsymmetricKeyParameter generatePublicKeyParameter(final PublicKey publicKey) throws InvalidKeyException {
        if (publicKey instanceof BCECPublicKey) {
            return ((BCECPublicKey)publicKey).engineGetKeyParameters();
        }
        return ECUtil.generatePublicKeyParameter(publicKey);
    }
    
    static X9ECParameters getDomainParametersFromGenSpec(final ECGenParameterSpec ecGenParameterSpec) {
        return getDomainParametersFromName(ecGenParameterSpec.getName());
    }
    
    static X962Parameters getDomainParametersFromName(final ECParameterSpec ecParameterSpec, final boolean b) {
        if (ecParameterSpec instanceof ECNamedCurveSpec) {
            final ECNamedCurveSpec ecNamedCurveSpec = (ECNamedCurveSpec)ecParameterSpec;
            ASN1ObjectIdentifier namedCurveOid;
            if ((namedCurveOid = ECUtil.getNamedCurveOid(ecNamedCurveSpec.getName())) == null) {
                namedCurveOid = new ASN1ObjectIdentifier(ecNamedCurveSpec.getName());
            }
            return new X962Parameters(namedCurveOid);
        }
        if (ecParameterSpec == null) {
            return new X962Parameters(DERNull.INSTANCE);
        }
        final ECCurve convertCurve = EC5Util.convertCurve(ecParameterSpec.getCurve());
        return new X962Parameters(new X9ECParameters(convertCurve, EC5Util.convertPoint(convertCurve, ecParameterSpec.getGenerator(), b), ecParameterSpec.getOrder(), BigInteger.valueOf(ecParameterSpec.getCofactor()), ecParameterSpec.getCurve().getSeed()));
    }
    
    static X9ECParameters getDomainParametersFromName(String ex) {
        try {
            if (((String)ex).charAt(0) >= '0' && ((String)ex).charAt(0) <= '2') {
                return ECUtil.getNamedCurveByOid(new ASN1ObjectIdentifier((String)ex));
            }
            if (((String)ex).indexOf(32) > 0) {
                final Serializable substring = ((String)ex).substring(((String)ex).indexOf(32) + 1);
                try {
                    return ECUtil.getNamedCurveByName((String)substring);
                }
                catch (IllegalArgumentException ex) {
                    ex = (IllegalArgumentException)substring;
                    return ECUtil.getNamedCurveByName((String)ex);
                }
            }
            return ECUtil.getNamedCurveByName((String)ex);
        }
        catch (IllegalArgumentException ex2) {}
        return ECUtil.getNamedCurveByName((String)ex);
    }
}
