package org.spongycastle.jcajce.provider.asymmetric.dsa;

import org.spongycastle.asn1.x509.*;
import java.io.*;
import java.security.spec.*;
import org.spongycastle.asn1.*;

public class AlgorithmParametersSpi extends java.security.AlgorithmParametersSpi
{
    DSAParameterSpec currentSpec;
    
    @Override
    protected byte[] engineGetEncoded() {
        final DSAParameter dsaParameter = new DSAParameter(this.currentSpec.getP(), this.currentSpec.getQ(), this.currentSpec.getG());
        try {
            return dsaParameter.getEncoded("DER");
        }
        catch (IOException ex) {
            throw new RuntimeException("Error encoding DSAParameters");
        }
    }
    
    @Override
    protected byte[] engineGetEncoded(final String s) {
        if (this.isASN1FormatString(s)) {
            return this.engineGetEncoded();
        }
        return null;
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
        if (algorithmParameterSpec instanceof DSAParameterSpec) {
            this.currentSpec = (DSAParameterSpec)algorithmParameterSpec;
            return;
        }
        throw new InvalidParameterSpecException("DSAParameterSpec required to initialise a DSA algorithm parameters object");
    }
    
    @Override
    protected void engineInit(final byte[] array) throws IOException {
        try {
            final DSAParameter instance = DSAParameter.getInstance(ASN1Primitive.fromByteArray(array));
            this.currentSpec = new DSAParameterSpec(instance.getP(), instance.getQ(), instance.getG());
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            throw new IOException("Not a valid DSA Parameter encoding.");
        }
        catch (ClassCastException ex2) {
            throw new IOException("Not a valid DSA Parameter encoding.");
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
        return "DSA Parameters";
    }
    
    protected boolean isASN1FormatString(final String s) {
        return s == null || s.equals("ASN.1");
    }
    
    protected AlgorithmParameterSpec localEngineGetParameterSpec(final Class clazz) throws InvalidParameterSpecException {
        if (clazz != DSAParameterSpec.class && clazz != AlgorithmParameterSpec.class) {
            throw new InvalidParameterSpecException("unknown parameter spec passed to DSA parameters object.");
        }
        return this.currentSpec;
    }
}
