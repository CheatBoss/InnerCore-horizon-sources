package org.spongycastle.jcajce.provider.symmetric;

import org.spongycastle.jcajce.provider.util.*;
import org.spongycastle.jcajce.provider.config.*;
import org.spongycastle.jcajce.provider.symmetric.util.*;
import org.spongycastle.asn1.*;
import javax.crypto.*;
import java.security.spec.*;
import org.spongycastle.crypto.generators.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.params.*;
import javax.crypto.spec.*;

public final class OpenSSLPBKDF
{
    private OpenSSLPBKDF() {
    }
    
    public static class Mappings extends AlgorithmProvider
    {
        private static final String PREFIX;
        
        static {
            PREFIX = OpenSSLPBKDF.class.getName();
        }
        
        @Override
        public void configure(final ConfigurableProvider configurableProvider) {
            final StringBuilder sb = new StringBuilder();
            sb.append(Mappings.PREFIX);
            sb.append("$PBKDF");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBKDF-OPENSSL", sb.toString());
        }
    }
    
    public static class PBKDF extends BaseSecretKeyFactory
    {
        public PBKDF() {
            super("PBKDF-OpenSSL", null);
        }
        
        @Override
        protected SecretKey engineGenerateSecret(final KeySpec keySpec) throws InvalidKeySpecException {
            if (!(keySpec instanceof PBEKeySpec)) {
                throw new InvalidKeySpecException("Invalid KeySpec");
            }
            final PBEKeySpec pbeKeySpec = (PBEKeySpec)keySpec;
            if (pbeKeySpec.getSalt() == null) {
                throw new InvalidKeySpecException("missing required salt");
            }
            if (pbeKeySpec.getIterationCount() <= 0) {
                final StringBuilder sb = new StringBuilder();
                sb.append("positive iteration count required: ");
                sb.append(pbeKeySpec.getIterationCount());
                throw new InvalidKeySpecException(sb.toString());
            }
            if (pbeKeySpec.getKeyLength() <= 0) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("positive key length required: ");
                sb2.append(pbeKeySpec.getKeyLength());
                throw new InvalidKeySpecException(sb2.toString());
            }
            if (pbeKeySpec.getPassword().length != 0) {
                final OpenSSLPBEParametersGenerator openSSLPBEParametersGenerator = new OpenSSLPBEParametersGenerator();
                openSSLPBEParametersGenerator.init(Strings.toByteArray(pbeKeySpec.getPassword()), pbeKeySpec.getSalt());
                return new SecretKeySpec(((KeyParameter)openSSLPBEParametersGenerator.generateDerivedParameters(pbeKeySpec.getKeyLength())).getKey(), "OpenSSLPBKDF");
            }
            throw new IllegalArgumentException("password empty");
        }
    }
}
