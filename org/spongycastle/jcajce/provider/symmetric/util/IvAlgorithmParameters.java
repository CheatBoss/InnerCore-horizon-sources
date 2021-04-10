package org.spongycastle.jcajce.provider.symmetric.util;

import java.io.*;
import org.spongycastle.util.*;
import javax.crypto.spec.*;
import java.security.spec.*;
import org.spongycastle.asn1.*;

public class IvAlgorithmParameters extends BaseAlgorithmParameters
{
    private byte[] iv;
    
    @Override
    protected byte[] engineGetEncoded() throws IOException {
        return this.engineGetEncoded("ASN.1");
    }
    
    @Override
    protected byte[] engineGetEncoded(final String s) throws IOException {
        if (this.isASN1FormatString(s)) {
            return new DEROctetString(this.engineGetEncoded("RAW")).getEncoded();
        }
        if (s.equals("RAW")) {
            return Arrays.clone(this.iv);
        }
        return null;
    }
    
    @Override
    protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
        if (algorithmParameterSpec instanceof IvParameterSpec) {
            this.iv = ((IvParameterSpec)algorithmParameterSpec).getIV();
            return;
        }
        throw new InvalidParameterSpecException("IvParameterSpec required to initialise a IV parameters algorithm parameters object");
    }
    
    @Override
    protected void engineInit(final byte[] array) throws IOException {
        byte[] octets = array;
        if (array.length % 8 != 0) {
            octets = array;
            if (array[0] == 4) {
                octets = array;
                if (array[1] == array.length - 2) {
                    octets = ((ASN1OctetString)ASN1Primitive.fromByteArray(array)).getOctets();
                }
            }
        }
        this.iv = Arrays.clone(octets);
    }
    
    @Override
    protected void engineInit(final byte[] array, final String s) throws IOException {
        if (this.isASN1FormatString(s)) {
            try {
                this.engineInit(((ASN1OctetString)ASN1Primitive.fromByteArray(array)).getOctets());
                return;
            }
            catch (Exception ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Exception decoding: ");
                sb.append(ex);
                throw new IOException(sb.toString());
            }
        }
        if (s.equals("RAW")) {
            this.engineInit(array);
            return;
        }
        throw new IOException("Unknown parameters format in IV parameters object");
    }
    
    @Override
    protected String engineToString() {
        return "IV Parameters";
    }
    
    @Override
    protected AlgorithmParameterSpec localEngineGetParameterSpec(final Class clazz) throws InvalidParameterSpecException {
        if (clazz != IvParameterSpec.class && clazz != AlgorithmParameterSpec.class) {
            throw new InvalidParameterSpecException("unknown parameter spec passed to IV parameters object.");
        }
        return new IvParameterSpec(this.iv);
    }
}
