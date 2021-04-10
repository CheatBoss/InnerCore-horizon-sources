package org.spongycastle.jcajce.provider.asymmetric.elgamal;

import org.spongycastle.jcajce.provider.symmetric.util.*;
import org.spongycastle.jce.spec.*;
import org.spongycastle.asn1.oiw.*;
import java.io.*;
import javax.crypto.spec.*;
import java.security.spec.*;
import org.spongycastle.asn1.*;

public class AlgorithmParametersSpi extends BaseAlgorithmParameters
{
    ElGamalParameterSpec currentSpec;
    
    @Override
    protected byte[] engineGetEncoded() {
        final ElGamalParameter elGamalParameter = new ElGamalParameter(this.currentSpec.getP(), this.currentSpec.getG());
        try {
            return elGamalParameter.getEncoded("DER");
        }
        catch (IOException ex) {
            throw new RuntimeException("Error encoding ElGamalParameters");
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
    protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
        final boolean b = algorithmParameterSpec instanceof ElGamalParameterSpec;
        if (!b && !(algorithmParameterSpec instanceof DHParameterSpec)) {
            throw new InvalidParameterSpecException("DHParameterSpec required to initialise a ElGamal algorithm parameters object");
        }
        if (b) {
            this.currentSpec = (ElGamalParameterSpec)algorithmParameterSpec;
            return;
        }
        final DHParameterSpec dhParameterSpec = (DHParameterSpec)algorithmParameterSpec;
        this.currentSpec = new ElGamalParameterSpec(dhParameterSpec.getP(), dhParameterSpec.getG());
    }
    
    @Override
    protected void engineInit(final byte[] array) throws IOException {
        try {
            final ElGamalParameter instance = ElGamalParameter.getInstance(ASN1Primitive.fromByteArray(array));
            this.currentSpec = new ElGamalParameterSpec(instance.getP(), instance.getG());
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            throw new IOException("Not a valid ElGamal Parameter encoding.");
        }
        catch (ClassCastException ex2) {
            throw new IOException("Not a valid ElGamal Parameter encoding.");
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
        return "ElGamal Parameters";
    }
    
    @Override
    protected AlgorithmParameterSpec localEngineGetParameterSpec(final Class clazz) throws InvalidParameterSpecException {
        if (clazz == ElGamalParameterSpec.class || clazz == AlgorithmParameterSpec.class) {
            return this.currentSpec;
        }
        if (clazz == DHParameterSpec.class) {
            return new DHParameterSpec(this.currentSpec.getP(), this.currentSpec.getG());
        }
        throw new InvalidParameterSpecException("unknown parameter spec passed to ElGamal parameters object.");
    }
}
