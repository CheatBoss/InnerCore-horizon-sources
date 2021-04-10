package org.spongycastle.jcajce.provider.asymmetric.ec;

import java.io.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import java.security.spec.*;
import org.spongycastle.asn1.*;
import org.spongycastle.jce.spec.*;
import org.spongycastle.jce.provider.*;
import org.spongycastle.asn1.x9.*;
import org.spongycastle.math.ec.*;

public class AlgorithmParametersSpi extends java.security.AlgorithmParametersSpi
{
    private String curveName;
    private ECParameterSpec ecParameterSpec;
    
    @Override
    protected byte[] engineGetEncoded() throws IOException {
        return this.engineGetEncoded("ASN.1");
    }
    
    @Override
    protected byte[] engineGetEncoded(final String s) throws IOException {
        if (this.isASN1FormatString(s)) {
            final ECParameterSpec ecParameterSpec = this.ecParameterSpec;
            X962Parameters x962Parameters;
            if (ecParameterSpec == null) {
                x962Parameters = new X962Parameters(DERNull.INSTANCE);
            }
            else if (this.curveName != null) {
                x962Parameters = new X962Parameters(ECUtil.getNamedCurveOid(this.curveName));
            }
            else {
                final org.spongycastle.jce.spec.ECParameterSpec convertSpec = EC5Util.convertSpec(ecParameterSpec, false);
                x962Parameters = new X962Parameters(new X9ECParameters(convertSpec.getCurve(), convertSpec.getG(), convertSpec.getN(), convertSpec.getH(), convertSpec.getSeed()));
            }
            return x962Parameters.getEncoded();
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Unknown parameters format in AlgorithmParameters object: ");
        sb.append(s);
        throw new IOException(sb.toString());
    }
    
    @Override
    protected <T extends AlgorithmParameterSpec> T engineGetParameterSpec(final Class<T> clazz) throws InvalidParameterSpecException {
        if (!ECParameterSpec.class.isAssignableFrom(clazz) && clazz != AlgorithmParameterSpec.class) {
            if (ECGenParameterSpec.class.isAssignableFrom(clazz)) {
                final String curveName = this.curveName;
                if (curveName != null) {
                    final ASN1ObjectIdentifier namedCurveOid = ECUtil.getNamedCurveOid(curveName);
                    if (namedCurveOid != null) {
                        return (T)new ECGenParameterSpec(namedCurveOid.getId());
                    }
                    return (T)new ECGenParameterSpec(this.curveName);
                }
                else {
                    final ASN1ObjectIdentifier namedCurveOid2 = ECUtil.getNamedCurveOid(EC5Util.convertSpec(this.ecParameterSpec, false));
                    if (namedCurveOid2 != null) {
                        return (T)new ECGenParameterSpec(namedCurveOid2.getId());
                    }
                }
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("EC AlgorithmParameters cannot convert to ");
            sb.append(clazz.getName());
            throw new InvalidParameterSpecException(sb.toString());
        }
        return (T)this.ecParameterSpec;
    }
    
    @Override
    protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
        ECParameterSpec convertToSpec;
        if (algorithmParameterSpec instanceof ECGenParameterSpec) {
            final ECGenParameterSpec ecGenParameterSpec = (ECGenParameterSpec)algorithmParameterSpec;
            final X9ECParameters domainParametersFromGenSpec = ECUtils.getDomainParametersFromGenSpec(ecGenParameterSpec);
            if (domainParametersFromGenSpec == null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("EC curve name not recognized: ");
                sb.append(ecGenParameterSpec.getName());
                throw new InvalidParameterSpecException(sb.toString());
            }
            this.curveName = ecGenParameterSpec.getName();
            convertToSpec = EC5Util.convertToSpec(domainParametersFromGenSpec);
        }
        else {
            if (!(algorithmParameterSpec instanceof ECParameterSpec)) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("AlgorithmParameterSpec class not recognized: ");
                sb2.append(algorithmParameterSpec.getClass().getName());
                throw new InvalidParameterSpecException(sb2.toString());
            }
            String name;
            if (algorithmParameterSpec instanceof ECNamedCurveSpec) {
                name = ((ECNamedCurveSpec)algorithmParameterSpec).getName();
            }
            else {
                name = null;
            }
            this.curveName = name;
            convertToSpec = (ECParameterSpec)algorithmParameterSpec;
        }
        this.ecParameterSpec = convertToSpec;
    }
    
    @Override
    protected void engineInit(final byte[] array) throws IOException {
        this.engineInit(array, "ASN.1");
    }
    
    @Override
    protected void engineInit(final byte[] array, final String s) throws IOException {
        if (this.isASN1FormatString(s)) {
            final X962Parameters instance = X962Parameters.getInstance(array);
            final ECCurve curve = EC5Util.getCurve(BouncyCastleProvider.CONFIGURATION, instance);
            if (instance.isNamedCurve()) {
                final ASN1ObjectIdentifier instance2 = ASN1ObjectIdentifier.getInstance(instance.getParameters());
                if ((this.curveName = ECNamedCurveTable.getName(instance2)) == null) {
                    this.curveName = instance2.getId();
                }
            }
            this.ecParameterSpec = EC5Util.convertToSpec(instance, curve);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Unknown encoded parameters format in AlgorithmParameters object: ");
        sb.append(s);
        throw new IOException(sb.toString());
    }
    
    @Override
    protected String engineToString() {
        return "EC AlgorithmParameters ";
    }
    
    protected boolean isASN1FormatString(final String s) {
        return s == null || s.equals("ASN.1");
    }
}
