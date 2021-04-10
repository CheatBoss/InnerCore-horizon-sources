package org.spongycastle.jcajce.provider.asymmetric.dh;

import javax.crypto.spec.*;
import org.spongycastle.asn1.pkcs.*;
import java.io.*;
import java.security.spec.*;

public class AlgorithmParametersSpi extends java.security.AlgorithmParametersSpi
{
    DHParameterSpec currentSpec;
    
    @Override
    protected byte[] engineGetEncoded() {
        final DHParameter dhParameter = new DHParameter(this.currentSpec.getP(), this.currentSpec.getG(), this.currentSpec.getL());
        try {
            return dhParameter.getEncoded("DER");
        }
        catch (IOException ex) {
            throw new RuntimeException("Error encoding DHParameters");
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
        if (algorithmParameterSpec instanceof DHParameterSpec) {
            this.currentSpec = (DHParameterSpec)algorithmParameterSpec;
            return;
        }
        throw new InvalidParameterSpecException("DHParameterSpec required to initialise a Diffie-Hellman algorithm parameters object");
    }
    
    @Override
    protected void engineInit(final byte[] array) throws IOException {
        try {
            final DHParameter instance = DHParameter.getInstance(array);
            if (instance.getL() != null) {
                this.currentSpec = new DHParameterSpec(instance.getP(), instance.getG(), instance.getL().intValue());
                return;
            }
            this.currentSpec = new DHParameterSpec(instance.getP(), instance.getG());
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            throw new IOException("Not a valid DH Parameter encoding.");
        }
        catch (ClassCastException ex2) {
            throw new IOException("Not a valid DH Parameter encoding.");
        }
    }
    
    @Override
    protected void engineInit(final byte[] array, final String s) throws IOException {
        if (this.isASN1FormatString(s)) {
            this.engineInit(array);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Unknown parameter format ");
        sb.append(s);
        throw new IOException(sb.toString());
    }
    
    @Override
    protected String engineToString() {
        return "Diffie-Hellman Parameters";
    }
    
    protected boolean isASN1FormatString(final String s) {
        return s == null || s.equals("ASN.1");
    }
    
    protected AlgorithmParameterSpec localEngineGetParameterSpec(final Class clazz) throws InvalidParameterSpecException {
        if (clazz != DHParameterSpec.class && clazz != AlgorithmParameterSpec.class) {
            throw new InvalidParameterSpecException("unknown parameter spec passed to DH parameters object.");
        }
        return this.currentSpec;
    }
}
