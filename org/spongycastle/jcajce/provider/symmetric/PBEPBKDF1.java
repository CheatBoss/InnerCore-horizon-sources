package org.spongycastle.jcajce.provider.symmetric;

import org.spongycastle.jcajce.provider.symmetric.util.*;
import java.io.*;
import javax.crypto.spec.*;
import java.security.spec.*;
import org.spongycastle.jcajce.provider.util.*;
import org.spongycastle.jcajce.provider.config.*;
import org.spongycastle.asn1.pkcs.*;

public class PBEPBKDF1
{
    private PBEPBKDF1() {
    }
    
    public static class AlgParams extends BaseAlgorithmParameters
    {
        PBEParameter params;
        
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
                this.params = new PBEParameter(pbeParameterSpec.getSalt(), pbeParameterSpec.getIterationCount());
                return;
            }
            throw new InvalidParameterSpecException("PBEParameterSpec required to initialise a PBKDF1 PBE parameters algorithm parameters object");
        }
        
        @Override
        protected void engineInit(final byte[] array) throws IOException {
            this.params = PBEParameter.getInstance(array);
        }
        
        @Override
        protected void engineInit(final byte[] array, final String s) throws IOException {
            if (this.isASN1FormatString(s)) {
                this.engineInit(array);
                return;
            }
            throw new IOException("Unknown parameters format in PBKDF2 parameters object");
        }
        
        @Override
        protected String engineToString() {
            return "PBKDF1 Parameters";
        }
        
        @Override
        protected AlgorithmParameterSpec localEngineGetParameterSpec(final Class clazz) throws InvalidParameterSpecException {
            if (clazz == PBEParameterSpec.class) {
                return new PBEParameterSpec(this.params.getSalt(), this.params.getIterationCount().intValue());
            }
            throw new InvalidParameterSpecException("unknown parameter spec passed to PBKDF1 PBE parameters object.");
        }
    }
    
    public static class Mappings extends AlgorithmProvider
    {
        private static final String PREFIX;
        
        static {
            PREFIX = PBEPBKDF1.class.getName();
        }
        
        @Override
        public void configure(final ConfigurableProvider configurableProvider) {
            final StringBuilder sb = new StringBuilder();
            sb.append(Mappings.PREFIX);
            sb.append("$AlgParams");
            configurableProvider.addAlgorithm("AlgorithmParameters.PBKDF1", sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Alg.Alias.AlgorithmParameters.");
            sb2.append(PKCSObjectIdentifiers.pbeWithMD2AndDES_CBC);
            configurableProvider.addAlgorithm(sb2.toString(), "PBKDF1");
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Alg.Alias.AlgorithmParameters.");
            sb3.append(PKCSObjectIdentifiers.pbeWithMD5AndDES_CBC);
            configurableProvider.addAlgorithm(sb3.toString(), "PBKDF1");
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Alg.Alias.AlgorithmParameters.");
            sb4.append(PKCSObjectIdentifiers.pbeWithMD5AndRC2_CBC);
            configurableProvider.addAlgorithm(sb4.toString(), "PBKDF1");
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("Alg.Alias.AlgorithmParameters.");
            sb5.append(PKCSObjectIdentifiers.pbeWithSHA1AndDES_CBC);
            configurableProvider.addAlgorithm(sb5.toString(), "PBKDF1");
            final StringBuilder sb6 = new StringBuilder();
            sb6.append("Alg.Alias.AlgorithmParameters.");
            sb6.append(PKCSObjectIdentifiers.pbeWithSHA1AndRC2_CBC);
            configurableProvider.addAlgorithm(sb6.toString(), "PBKDF1");
        }
    }
}
