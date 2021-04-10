package org.spongycastle.jce.provider;

import org.spongycastle.crypto.digests.*;
import org.spongycastle.crypto.generators.*;
import org.spongycastle.jcajce.provider.symmetric.util.*;
import java.security.spec.*;
import org.spongycastle.crypto.*;
import javax.crypto.spec.*;
import org.spongycastle.crypto.params.*;

public interface BrokenPBE
{
    public static final int MD5 = 0;
    public static final int OLD_PKCS12 = 3;
    public static final int PKCS12 = 2;
    public static final int PKCS5S1 = 0;
    public static final int PKCS5S2 = 1;
    public static final int RIPEMD160 = 2;
    public static final int SHA1 = 1;
    
    public static class Util
    {
        private static PBEParametersGenerator makePBEGenerator(final int n, final int n2) {
            if (n == 0) {
                if (n2 == 0) {
                    return new PKCS5S1ParametersGenerator(new MD5Digest());
                }
                if (n2 == 1) {
                    return new PKCS5S1ParametersGenerator(new SHA1Digest());
                }
                throw new IllegalStateException("PKCS5 scheme 1 only supports only MD5 and SHA1.");
            }
            else {
                if (n == 1) {
                    return new PKCS5S2ParametersGenerator();
                }
                if (n == 3) {
                    if (n2 == 0) {
                        return new OldPKCS12ParametersGenerator(new MD5Digest());
                    }
                    if (n2 == 1) {
                        return new OldPKCS12ParametersGenerator(new SHA1Digest());
                    }
                    if (n2 == 2) {
                        return new OldPKCS12ParametersGenerator(new RIPEMD160Digest());
                    }
                    throw new IllegalStateException("unknown digest scheme for PBE encryption.");
                }
                else {
                    if (n2 == 0) {
                        return new PKCS12ParametersGenerator(new MD5Digest());
                    }
                    if (n2 == 1) {
                        return new PKCS12ParametersGenerator(new SHA1Digest());
                    }
                    if (n2 == 2) {
                        return new PKCS12ParametersGenerator(new RIPEMD160Digest());
                    }
                    throw new IllegalStateException("unknown digest scheme for PBE encryption.");
                }
            }
        }
        
        static CipherParameters makePBEMacParameters(final BCPBEKey bcpbeKey, final AlgorithmParameterSpec algorithmParameterSpec, int i, final int n, final int n2) {
            if (algorithmParameterSpec != null && algorithmParameterSpec instanceof PBEParameterSpec) {
                final PBEParameterSpec pbeParameterSpec = (PBEParameterSpec)algorithmParameterSpec;
                final PBEParametersGenerator pbeGenerator = makePBEGenerator(i, n);
                final byte[] encoded = bcpbeKey.getEncoded();
                pbeGenerator.init(encoded, pbeParameterSpec.getSalt(), pbeParameterSpec.getIterationCount());
                final CipherParameters generateDerivedMacParameters = pbeGenerator.generateDerivedMacParameters(n2);
                for (i = 0; i != encoded.length; ++i) {
                    encoded[i] = 0;
                }
                return generateDerivedMacParameters;
            }
            throw new IllegalArgumentException("Need a PBEParameter spec with a PBE key.");
        }
        
        static CipherParameters makePBEParameters(final BCPBEKey bcpbeKey, final AlgorithmParameterSpec algorithmParameterSpec, int i, final int n, final String s, final int n2, final int n3) {
            if (algorithmParameterSpec != null && algorithmParameterSpec instanceof PBEParameterSpec) {
                final PBEParameterSpec pbeParameterSpec = (PBEParameterSpec)algorithmParameterSpec;
                final PBEParametersGenerator pbeGenerator = makePBEGenerator(i, n);
                final byte[] encoded = bcpbeKey.getEncoded();
                pbeGenerator.init(encoded, pbeParameterSpec.getSalt(), pbeParameterSpec.getIterationCount());
                CipherParameters cipherParameters;
                if (n3 != 0) {
                    cipherParameters = pbeGenerator.generateDerivedParameters(n2, n3);
                }
                else {
                    cipherParameters = pbeGenerator.generateDerivedParameters(n2);
                }
                if (s.startsWith("DES")) {
                    KeyParameter keyParameter;
                    if (cipherParameters instanceof ParametersWithIV) {
                        keyParameter = (KeyParameter)((ParametersWithIV)cipherParameters).getParameters();
                    }
                    else {
                        keyParameter = (KeyParameter)cipherParameters;
                    }
                    setOddParity(keyParameter.getKey());
                }
                for (i = 0; i != encoded.length; ++i) {
                    encoded[i] = 0;
                }
                return cipherParameters;
            }
            throw new IllegalArgumentException("Need a PBEParameter spec with a PBE key.");
        }
        
        private static void setOddParity(final byte[] array) {
            for (int i = 0; i < array.length; ++i) {
                final byte b = array[i];
                array[i] = (byte)((b & 0xFE) | (b >> 7 ^ (b >> 1 ^ b >> 2 ^ b >> 3 ^ b >> 4 ^ b >> 5 ^ b >> 6) ^ 0x1));
            }
        }
    }
}
