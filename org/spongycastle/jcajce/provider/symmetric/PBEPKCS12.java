package org.spongycastle.jcajce.provider.symmetric;

import org.spongycastle.jcajce.provider.symmetric.util.*;
import org.spongycastle.asn1.pkcs.*;
import java.io.*;
import javax.crypto.spec.*;
import java.security.spec.*;
import org.spongycastle.asn1.*;
import org.spongycastle.jcajce.provider.util.*;
import org.spongycastle.jcajce.provider.config.*;

public class PBEPKCS12
{
    private PBEPKCS12() {
    }
    
    public static class AlgParams extends BaseAlgorithmParameters
    {
        PKCS12PBEParams params;
        
        @Override
        protected byte[] engineGetEncoded() {
            try {
                return this.params.getEncoded("DER");
            }
            catch (IOException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Oooops! ");
                sb.append(ex.toString());
                throw new RuntimeException(sb.toString());
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
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
            if (algorithmParameterSpec instanceof PBEParameterSpec) {
                final PBEParameterSpec pbeParameterSpec = (PBEParameterSpec)algorithmParameterSpec;
                this.params = new PKCS12PBEParams(pbeParameterSpec.getSalt(), pbeParameterSpec.getIterationCount());
                return;
            }
            throw new InvalidParameterSpecException("PBEParameterSpec required to initialise a PKCS12 PBE parameters algorithm parameters object");
        }
        
        @Override
        protected void engineInit(final byte[] array) throws IOException {
            this.params = PKCS12PBEParams.getInstance(ASN1Primitive.fromByteArray(array));
        }
        
        @Override
        protected void engineInit(final byte[] array, final String s) throws IOException {
            if (this.isASN1FormatString(s)) {
                this.engineInit(array);
                return;
            }
            throw new IOException("Unknown parameters format in PKCS12 PBE parameters object");
        }
        
        @Override
        protected String engineToString() {
            return "PKCS12 PBE Parameters";
        }
        
        @Override
        protected AlgorithmParameterSpec localEngineGetParameterSpec(final Class clazz) throws InvalidParameterSpecException {
            if (clazz == PBEParameterSpec.class) {
                return new PBEParameterSpec(this.params.getIV(), this.params.getIterations().intValue());
            }
            throw new InvalidParameterSpecException("unknown parameter spec passed to PKCS12 PBE parameters object.");
        }
    }
    
    public static class Mappings extends AlgorithmProvider
    {
        private static final String PREFIX;
        
        static {
            PREFIX = PBEPKCS12.class.getName();
        }
        
        @Override
        public void configure(final ConfigurableProvider configurableProvider) {
            final StringBuilder sb = new StringBuilder();
            sb.append(Mappings.PREFIX);
            sb.append("$AlgParams");
            configurableProvider.addAlgorithm("AlgorithmParameters.PKCS12PBE", sb.toString());
        }
    }
}
