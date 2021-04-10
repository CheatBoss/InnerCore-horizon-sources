package org.spongycastle.jcajce.provider.asymmetric.util;

import org.spongycastle.crypto.ec.*;
import java.math.*;
import org.spongycastle.math.ec.*;
import org.spongycastle.util.*;
import org.spongycastle.math.field.*;
import java.security.spec.*;
import org.spongycastle.jce.spec.*;
import org.spongycastle.asn1.x9.*;
import org.spongycastle.asn1.*;
import org.spongycastle.jce.provider.*;
import org.spongycastle.jcajce.provider.config.*;
import java.util.*;
import org.spongycastle.crypto.params.*;

public class EC5Util
{
    private static Map customCurves;
    
    static {
        EC5Util.customCurves = new HashMap();
        final Enumeration names = CustomNamedCurves.getNames();
        while (names.hasMoreElements()) {
            final String s = names.nextElement();
            final X9ECParameters byName = ECNamedCurveTable.getByName(s);
            if (byName != null) {
                EC5Util.customCurves.put(byName.getCurve(), CustomNamedCurves.getByName(s).getCurve());
            }
        }
        final X9ECParameters byName2 = CustomNamedCurves.getByName("Curve25519");
        EC5Util.customCurves.put(new ECCurve.Fp(byName2.getCurve().getField().getCharacteristic(), byName2.getCurve().getA().toBigInteger(), byName2.getCurve().getB().toBigInteger()), byName2.getCurve());
    }
    
    public static EllipticCurve convertCurve(final ECCurve ecCurve, final byte[] array) {
        return new EllipticCurve(convertField(ecCurve.getField()), ecCurve.getA().toBigInteger(), ecCurve.getB().toBigInteger(), null);
    }
    
    public static ECCurve convertCurve(final EllipticCurve ellipticCurve) {
        final ECField field = ellipticCurve.getField();
        final BigInteger a = ellipticCurve.getA();
        final BigInteger b = ellipticCurve.getB();
        if (field instanceof ECFieldFp) {
            ECCurve ecCurve;
            final ECCurve.Fp fp = (ECCurve.Fp)(ecCurve = new ECCurve.Fp(((ECFieldFp)field).getP(), a, b));
            if (EC5Util.customCurves.containsKey(fp)) {
                ecCurve = (ECCurve)EC5Util.customCurves.get(fp);
            }
            return ecCurve;
        }
        final ECFieldF2m ecFieldF2m = (ECFieldF2m)field;
        final int m = ecFieldF2m.getM();
        final int[] convertMidTerms = ECUtil.convertMidTerms(ecFieldF2m.getMidTermsOfReductionPolynomial());
        return new ECCurve.F2m(m, convertMidTerms[0], convertMidTerms[1], convertMidTerms[2], a, b);
    }
    
    public static ECField convertField(final FiniteField finiteField) {
        if (ECAlgorithms.isFpField(finiteField)) {
            return new ECFieldFp(finiteField.getCharacteristic());
        }
        final Polynomial minimalPolynomial = ((PolynomialExtensionField)finiteField).getMinimalPolynomial();
        final int[] exponentsPresent = minimalPolynomial.getExponentsPresent();
        return new ECFieldF2m(minimalPolynomial.getDegree(), Arrays.reverse(Arrays.copyOfRange(exponentsPresent, 1, exponentsPresent.length - 1)));
    }
    
    public static org.spongycastle.math.ec.ECPoint convertPoint(final ECParameterSpec ecParameterSpec, final ECPoint ecPoint, final boolean b) {
        return convertPoint(convertCurve(ecParameterSpec.getCurve()), ecPoint, b);
    }
    
    public static org.spongycastle.math.ec.ECPoint convertPoint(final ECCurve ecCurve, final ECPoint ecPoint, final boolean b) {
        return ecCurve.createPoint(ecPoint.getAffineX(), ecPoint.getAffineY());
    }
    
    public static ECParameterSpec convertSpec(final EllipticCurve ellipticCurve, final org.spongycastle.jce.spec.ECParameterSpec ecParameterSpec) {
        if (ecParameterSpec instanceof ECNamedCurveParameterSpec) {
            return new ECNamedCurveSpec(((ECNamedCurveParameterSpec)ecParameterSpec).getName(), ellipticCurve, new ECPoint(ecParameterSpec.getG().getAffineXCoord().toBigInteger(), ecParameterSpec.getG().getAffineYCoord().toBigInteger()), ecParameterSpec.getN(), ecParameterSpec.getH());
        }
        return new ECParameterSpec(ellipticCurve, new ECPoint(ecParameterSpec.getG().getAffineXCoord().toBigInteger(), ecParameterSpec.getG().getAffineYCoord().toBigInteger()), ecParameterSpec.getN(), ecParameterSpec.getH().intValue());
    }
    
    public static org.spongycastle.jce.spec.ECParameterSpec convertSpec(final ECParameterSpec ecParameterSpec, final boolean b) {
        final ECCurve convertCurve = convertCurve(ecParameterSpec.getCurve());
        return new org.spongycastle.jce.spec.ECParameterSpec(convertCurve, convertPoint(convertCurve, ecParameterSpec.getGenerator(), b), ecParameterSpec.getOrder(), BigInteger.valueOf(ecParameterSpec.getCofactor()), ecParameterSpec.getCurve().getSeed());
    }
    
    public static ECParameterSpec convertToSpec(final X962Parameters x962Parameters, final ECCurve ecCurve) {
        if (x962Parameters.isNamedCurve()) {
            final ASN1ObjectIdentifier asn1ObjectIdentifier = (ASN1ObjectIdentifier)x962Parameters.getParameters();
            final X9ECParameters namedCurveByOid = ECUtil.getNamedCurveByOid(asn1ObjectIdentifier);
            X9ECParameters x9ECParameters;
            if ((x9ECParameters = namedCurveByOid) == null) {
                final Map additionalECParameters = BouncyCastleProvider.CONFIGURATION.getAdditionalECParameters();
                x9ECParameters = namedCurveByOid;
                if (!additionalECParameters.isEmpty()) {
                    x9ECParameters = additionalECParameters.get(asn1ObjectIdentifier);
                }
            }
            return new ECNamedCurveSpec(ECUtil.getCurveName(asn1ObjectIdentifier), convertCurve(ecCurve, x9ECParameters.getSeed()), new ECPoint(x9ECParameters.getG().getAffineXCoord().toBigInteger(), x9ECParameters.getG().getAffineYCoord().toBigInteger()), x9ECParameters.getN(), x9ECParameters.getH());
        }
        if (x962Parameters.isImplicitlyCA()) {
            return null;
        }
        final X9ECParameters instance = X9ECParameters.getInstance(x962Parameters.getParameters());
        final EllipticCurve convertCurve = convertCurve(ecCurve, instance.getSeed());
        if (instance.getH() != null) {
            return new ECParameterSpec(convertCurve, new ECPoint(instance.getG().getAffineXCoord().toBigInteger(), instance.getG().getAffineYCoord().toBigInteger()), instance.getN(), instance.getH().intValue());
        }
        return new ECParameterSpec(convertCurve, new ECPoint(instance.getG().getAffineXCoord().toBigInteger(), instance.getG().getAffineYCoord().toBigInteger()), instance.getN(), 1);
    }
    
    public static ECParameterSpec convertToSpec(final X9ECParameters x9ECParameters) {
        return new ECParameterSpec(convertCurve(x9ECParameters.getCurve(), null), new ECPoint(x9ECParameters.getG().getAffineXCoord().toBigInteger(), x9ECParameters.getG().getAffineYCoord().toBigInteger()), x9ECParameters.getN(), x9ECParameters.getH().intValue());
    }
    
    public static ECCurve getCurve(final ProviderConfiguration providerConfiguration, final X962Parameters x962Parameters) {
        final Set acceptableNamedCurves = providerConfiguration.getAcceptableNamedCurves();
        if (x962Parameters.isNamedCurve()) {
            final ASN1ObjectIdentifier instance = ASN1ObjectIdentifier.getInstance(x962Parameters.getParameters());
            if (!acceptableNamedCurves.isEmpty() && !acceptableNamedCurves.contains(instance)) {
                throw new IllegalStateException("named curve not acceptable");
            }
            X9ECParameters namedCurveByOid;
            if ((namedCurveByOid = ECUtil.getNamedCurveByOid(instance)) == null) {
                namedCurveByOid = providerConfiguration.getAdditionalECParameters().get(instance);
            }
            return namedCurveByOid.getCurve();
        }
        else {
            if (x962Parameters.isImplicitlyCA()) {
                return providerConfiguration.getEcImplicitlyCa().getCurve();
            }
            if (acceptableNamedCurves.isEmpty()) {
                return X9ECParameters.getInstance(x962Parameters.getParameters()).getCurve();
            }
            throw new IllegalStateException("encoded parameters not acceptable");
        }
    }
    
    public static ECDomainParameters getDomainParameters(final ProviderConfiguration providerConfiguration, final ECParameterSpec ecParameterSpec) {
        if (ecParameterSpec == null) {
            final org.spongycastle.jce.spec.ECParameterSpec ecImplicitlyCa = providerConfiguration.getEcImplicitlyCa();
            return new ECDomainParameters(ecImplicitlyCa.getCurve(), ecImplicitlyCa.getG(), ecImplicitlyCa.getN(), ecImplicitlyCa.getH(), ecImplicitlyCa.getSeed());
        }
        return ECUtil.getDomainParameters(providerConfiguration, convertSpec(ecParameterSpec, false));
    }
}
