package org.spongycastle.jcajce.provider.asymmetric.gost;

import org.spongycastle.asn1.cryptopro.*;
import java.io.*;
import java.security.spec.*;
import org.spongycastle.asn1.*;
import org.spongycastle.jce.spec.*;

public class AlgorithmParametersSpi extends java.security.AlgorithmParametersSpi
{
    GOST3410ParameterSpec currentSpec;
    
    @Override
    protected byte[] engineGetEncoded() {
        final GOST3410PublicKeyAlgParameters gost3410PublicKeyAlgParameters = new GOST3410PublicKeyAlgParameters(new ASN1ObjectIdentifier(this.currentSpec.getPublicKeyParamSetOID()), new ASN1ObjectIdentifier(this.currentSpec.getDigestParamSetOID()), new ASN1ObjectIdentifier(this.currentSpec.getEncryptionParamSetOID()));
        try {
            return gost3410PublicKeyAlgParameters.getEncoded("DER");
        }
        catch (IOException ex) {
            throw new RuntimeException("Error encoding GOST3410Parameters");
        }
    }
    
    @Override
    protected byte[] engineGetEncoded(final String s) {
        if (!this.isASN1FormatString(s) && !s.equalsIgnoreCase("X.509")) {
            return null;
        }
        return this.engineGetEncoded();
    }
    
    @Override
    protected AlgorithmParameterSpec engineGetParameterSpec(final Class clazz) throws InvalidParameterSpecException {
        if (clazz != null) {
            return this.localEngineGetParameterSpec(clazz);
        }
        throw new NullPointerException("argument to getParameterSpec must not be null");
    }
    
    @Override
    protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
        if (algorithmParameterSpec instanceof GOST3410ParameterSpec) {
            this.currentSpec = (GOST3410ParameterSpec)algorithmParameterSpec;
            return;
        }
        throw new InvalidParameterSpecException("GOST3410ParameterSpec required to initialise a GOST3410 algorithm parameters object");
    }
    
    @Override
    protected void engineInit(final byte[] array) throws IOException {
        try {
            this.currentSpec = GOST3410ParameterSpec.fromPublicKeyAlg(new GOST3410PublicKeyAlgParameters((ASN1Sequence)ASN1Primitive.fromByteArray(array)));
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            throw new IOException("Not a valid GOST3410 Parameter encoding.");
        }
        catch (ClassCastException ex2) {
            throw new IOException("Not a valid GOST3410 Parameter encoding.");
        }
    }
    
    @Override
    protected void engineInit(final byte[] array, final String s) throws IOException {
        if (!this.isASN1FormatString(s) && !s.equalsIgnoreCase("X.509")) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unknown parameter format ");
            sb.append(s);
            throw new IOException(sb.toString());
        }
        this.engineInit(array);
    }
    
    @Override
    protected String engineToString() {
        return "GOST3410 Parameters";
    }
    
    protected boolean isASN1FormatString(final String s) {
        return s == null || s.equals("ASN.1");
    }
    
    protected AlgorithmParameterSpec localEngineGetParameterSpec(final Class clazz) throws InvalidParameterSpecException {
        if (clazz != GOST3410PublicKeyParameterSetSpec.class && clazz != AlgorithmParameterSpec.class) {
            throw new InvalidParameterSpecException("unknown parameter spec passed to GOST3410 parameters object.");
        }
        return this.currentSpec;
    }
}
