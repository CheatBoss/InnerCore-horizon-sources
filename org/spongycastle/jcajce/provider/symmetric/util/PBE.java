package org.spongycastle.jcajce.provider.symmetric.util;

import org.spongycastle.crypto.util.*;
import org.spongycastle.crypto.digests.*;
import org.spongycastle.crypto.generators.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import org.spongycastle.crypto.*;
import java.security.spec.*;
import org.spongycastle.crypto.params.*;
import java.security.*;

public interface PBE
{
    public static final int GOST3411 = 6;
    public static final int MD2 = 5;
    public static final int MD5 = 0;
    public static final int OPENSSL = 3;
    public static final int PKCS12 = 2;
    public static final int PKCS5S1 = 0;
    public static final int PKCS5S1_UTF8 = 4;
    public static final int PKCS5S2 = 1;
    public static final int PKCS5S2_UTF8 = 5;
    public static final int RIPEMD160 = 2;
    public static final int SHA1 = 1;
    public static final int SHA224 = 7;
    public static final int SHA256 = 4;
    public static final int SHA384 = 8;
    public static final int SHA3_224 = 10;
    public static final int SHA3_256 = 11;
    public static final int SHA3_384 = 12;
    public static final int SHA3_512 = 13;
    public static final int SHA512 = 9;
    public static final int TIGER = 3;
    
    public static class Util
    {
        private static byte[] convertPassword(final int n, final PBEKeySpec pbeKeySpec) {
            if (n == 2) {
                return PBEParametersGenerator.PKCS12PasswordToBytes(pbeKeySpec.getPassword());
            }
            if (n != 5 && n != 4) {
                return PBEParametersGenerator.PKCS5PasswordToBytes(pbeKeySpec.getPassword());
            }
            return PBEParametersGenerator.PKCS5PasswordToUTF8Bytes(pbeKeySpec.getPassword());
        }
        
        private static PBEParametersGenerator makePBEGenerator(final int n, final int n2) {
            if (n != 0 && n != 4) {
                if (n != 1 && n != 5) {
                    if (n != 2) {
                        return new OpenSSLPBEParametersGenerator();
                    }
                    switch (n2) {
                        default: {
                            throw new IllegalStateException("unknown digest scheme for PBE encryption.");
                        }
                        case 9: {
                            return new PKCS12ParametersGenerator(DigestFactory.createSHA512());
                        }
                        case 8: {
                            return new PKCS12ParametersGenerator(DigestFactory.createSHA384());
                        }
                        case 7: {
                            return new PKCS12ParametersGenerator(DigestFactory.createSHA224());
                        }
                        case 6: {
                            return new PKCS12ParametersGenerator(new GOST3411Digest());
                        }
                        case 5: {
                            return new PKCS12ParametersGenerator(new MD2Digest());
                        }
                        case 4: {
                            return new PKCS12ParametersGenerator(DigestFactory.createSHA256());
                        }
                        case 3: {
                            return new PKCS12ParametersGenerator(new TigerDigest());
                        }
                        case 2: {
                            return new PKCS12ParametersGenerator(new RIPEMD160Digest());
                        }
                        case 1: {
                            return new PKCS12ParametersGenerator(DigestFactory.createSHA1());
                        }
                        case 0: {
                            return new PKCS12ParametersGenerator(DigestFactory.createMD5());
                        }
                    }
                }
                else {
                    switch (n2) {
                        default: {
                            throw new IllegalStateException("unknown digest scheme for PBE PKCS5S2 encryption.");
                        }
                        case 13: {
                            return new PKCS5S2ParametersGenerator(DigestFactory.createSHA3_512());
                        }
                        case 12: {
                            return new PKCS5S2ParametersGenerator(DigestFactory.createSHA3_384());
                        }
                        case 11: {
                            return new PKCS5S2ParametersGenerator(DigestFactory.createSHA3_256());
                        }
                        case 10: {
                            return new PKCS5S2ParametersGenerator(DigestFactory.createSHA3_224());
                        }
                        case 9: {
                            return new PKCS5S2ParametersGenerator(DigestFactory.createSHA512());
                        }
                        case 8: {
                            return new PKCS5S2ParametersGenerator(DigestFactory.createSHA384());
                        }
                        case 7: {
                            return new PKCS5S2ParametersGenerator(DigestFactory.createSHA224());
                        }
                        case 6: {
                            return new PKCS5S2ParametersGenerator(new GOST3411Digest());
                        }
                        case 5: {
                            return new PKCS5S2ParametersGenerator(new MD2Digest());
                        }
                        case 4: {
                            return new PKCS5S2ParametersGenerator(DigestFactory.createSHA256());
                        }
                        case 3: {
                            return new PKCS5S2ParametersGenerator(new TigerDigest());
                        }
                        case 2: {
                            return new PKCS5S2ParametersGenerator(new RIPEMD160Digest());
                        }
                        case 1: {
                            return new PKCS5S2ParametersGenerator(DigestFactory.createSHA1());
                        }
                        case 0: {
                            return new PKCS5S2ParametersGenerator(DigestFactory.createMD5());
                        }
                    }
                }
            }
            else {
                if (n2 == 0) {
                    return new PKCS5S1ParametersGenerator(DigestFactory.createMD5());
                }
                if (n2 == 1) {
                    return new PKCS5S1ParametersGenerator(DigestFactory.createSHA1());
                }
                if (n2 == 5) {
                    return new PKCS5S1ParametersGenerator(new MD2Digest());
                }
                throw new IllegalStateException("PKCS5 scheme 1 only supports MD2, MD5 and SHA1.");
            }
        }
        
        public static CipherParameters makePBEMacParameters(final SecretKey secretKey, int i, final int n, final int n2, final PBEParameterSpec pbeParameterSpec) {
            final PBEParametersGenerator pbeGenerator = makePBEGenerator(i, n);
            final byte[] encoded = secretKey.getEncoded();
            pbeGenerator.init(secretKey.getEncoded(), pbeParameterSpec.getSalt(), pbeParameterSpec.getIterationCount());
            final CipherParameters generateDerivedMacParameters = pbeGenerator.generateDerivedMacParameters(n2);
            for (i = 0; i != encoded.length; ++i) {
                encoded[i] = 0;
            }
            return generateDerivedMacParameters;
        }
        
        public static CipherParameters makePBEMacParameters(final PBEKeySpec pbeKeySpec, int i, final int n, final int n2) {
            final PBEParametersGenerator pbeGenerator = makePBEGenerator(i, n);
            final byte[] convertPassword = convertPassword(i, pbeKeySpec);
            pbeGenerator.init(convertPassword, pbeKeySpec.getSalt(), pbeKeySpec.getIterationCount());
            final CipherParameters generateDerivedMacParameters = pbeGenerator.generateDerivedMacParameters(n2);
            for (i = 0; i != convertPassword.length; ++i) {
                convertPassword[i] = 0;
            }
            return generateDerivedMacParameters;
        }
        
        public static CipherParameters makePBEMacParameters(final BCPBEKey bcpbeKey, final AlgorithmParameterSpec algorithmParameterSpec) {
            if (algorithmParameterSpec != null && algorithmParameterSpec instanceof PBEParameterSpec) {
                final PBEParameterSpec pbeParameterSpec = (PBEParameterSpec)algorithmParameterSpec;
                final PBEParametersGenerator pbeGenerator = makePBEGenerator(bcpbeKey.getType(), bcpbeKey.getDigest());
                pbeGenerator.init(bcpbeKey.getEncoded(), pbeParameterSpec.getSalt(), pbeParameterSpec.getIterationCount());
                return pbeGenerator.generateDerivedMacParameters(bcpbeKey.getKeySize());
            }
            throw new IllegalArgumentException("Need a PBEParameter spec with a PBE key.");
        }
        
        public static CipherParameters makePBEParameters(final PBEKeySpec pbeKeySpec, int i, final int n, final int n2, final int n3) {
            final PBEParametersGenerator pbeGenerator = makePBEGenerator(i, n);
            final byte[] convertPassword = convertPassword(i, pbeKeySpec);
            pbeGenerator.init(convertPassword, pbeKeySpec.getSalt(), pbeKeySpec.getIterationCount());
            CipherParameters cipherParameters;
            if (n3 != 0) {
                cipherParameters = pbeGenerator.generateDerivedParameters(n2, n3);
            }
            else {
                cipherParameters = pbeGenerator.generateDerivedParameters(n2);
            }
            for (i = 0; i != convertPassword.length; ++i) {
                convertPassword[i] = 0;
            }
            return cipherParameters;
        }
        
        public static CipherParameters makePBEParameters(final BCPBEKey bcpbeKey, final AlgorithmParameterSpec algorithmParameterSpec, final String s) {
            if (algorithmParameterSpec != null && algorithmParameterSpec instanceof PBEParameterSpec) {
                final PBEParameterSpec pbeParameterSpec = (PBEParameterSpec)algorithmParameterSpec;
                final PBEParametersGenerator pbeGenerator = makePBEGenerator(bcpbeKey.getType(), bcpbeKey.getDigest());
                byte[] encoded = bcpbeKey.getEncoded();
                if (bcpbeKey.shouldTryWrongPKCS12()) {
                    encoded = new byte[2];
                }
                pbeGenerator.init(encoded, pbeParameterSpec.getSalt(), pbeParameterSpec.getIterationCount());
                CipherParameters cipherParameters;
                if (bcpbeKey.getIvSize() != 0) {
                    cipherParameters = pbeGenerator.generateDerivedParameters(bcpbeKey.getKeySize(), bcpbeKey.getIvSize());
                }
                else {
                    cipherParameters = pbeGenerator.generateDerivedParameters(bcpbeKey.getKeySize());
                }
                if (s.startsWith("DES")) {
                    if (cipherParameters instanceof ParametersWithIV) {
                        DESParameters.setOddParity(((KeyParameter)((ParametersWithIV)cipherParameters).getParameters()).getKey());
                        return cipherParameters;
                    }
                    DESParameters.setOddParity(((KeyParameter)cipherParameters).getKey());
                }
                return cipherParameters;
            }
            throw new IllegalArgumentException("Need a PBEParameter spec with a PBE key.");
        }
        
        public static CipherParameters makePBEParameters(final byte[] array, final int n, final int n2, final int n3, final int n4, final AlgorithmParameterSpec algorithmParameterSpec, final String s) throws InvalidAlgorithmParameterException {
            if (algorithmParameterSpec != null && algorithmParameterSpec instanceof PBEParameterSpec) {
                final PBEParameterSpec pbeParameterSpec = (PBEParameterSpec)algorithmParameterSpec;
                final PBEParametersGenerator pbeGenerator = makePBEGenerator(n, n2);
                pbeGenerator.init(array, pbeParameterSpec.getSalt(), pbeParameterSpec.getIterationCount());
                CipherParameters cipherParameters;
                if (n4 != 0) {
                    cipherParameters = pbeGenerator.generateDerivedParameters(n3, n4);
                }
                else {
                    cipherParameters = pbeGenerator.generateDerivedParameters(n3);
                }
                if (s.startsWith("DES")) {
                    if (cipherParameters instanceof ParametersWithIV) {
                        DESParameters.setOddParity(((KeyParameter)((ParametersWithIV)cipherParameters).getParameters()).getKey());
                        return cipherParameters;
                    }
                    DESParameters.setOddParity(((KeyParameter)cipherParameters).getKey());
                }
                return cipherParameters;
            }
            throw new InvalidAlgorithmParameterException("Need a PBEParameter spec with a PBE key.");
        }
    }
}
