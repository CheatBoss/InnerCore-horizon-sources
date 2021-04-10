package org.spongycastle.jcajce.provider.symmetric;

import org.spongycastle.jcajce.spec.*;
import org.spongycastle.crypto.util.*;
import org.spongycastle.crypto.macs.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.jcajce.provider.util.*;
import org.spongycastle.jcajce.provider.config.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.spec.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.digests.*;
import org.spongycastle.jcajce.provider.symmetric.util.*;
import org.spongycastle.asn1.*;

public class TLSKDF
{
    private static byte[] PRF_legacy(final TLSKeyMaterialSpec tlsKeyMaterialSpec) {
        final HMac hMac = new HMac(DigestFactory.createMD5());
        final HMac hMac2 = new HMac(DigestFactory.createSHA1());
        final byte[] concatenate = Arrays.concatenate(Strings.toByteArray(tlsKeyMaterialSpec.getLabel()), tlsKeyMaterialSpec.getSeed());
        final byte[] secret = tlsKeyMaterialSpec.getSecret();
        final int n = (secret.length + 1) / 2;
        final byte[] array = new byte[n];
        final byte[] array2 = new byte[n];
        int i = 0;
        System.arraycopy(secret, 0, array, 0, n);
        System.arraycopy(secret, secret.length - n, array2, 0, n);
        final int length = tlsKeyMaterialSpec.getLength();
        final byte[] array3 = new byte[length];
        final byte[] array4 = new byte[length];
        hmac_hash(hMac, array, concatenate, array3);
        hmac_hash(hMac2, array2, concatenate, array4);
        while (i < length) {
            array3[i] ^= array4[i];
            ++i;
        }
        return array3;
    }
    
    private static void hmac_hash(final Mac mac, byte[] array, final byte[] array2, final byte[] array3) {
        mac.init(new KeyParameter(array));
        final int macSize = mac.getMacSize();
        final int n = (array3.length + macSize - 1) / macSize;
        final int macSize2 = mac.getMacSize();
        final byte[] array4 = new byte[macSize2];
        final byte[] array5 = new byte[mac.getMacSize()];
        array = array2;
        for (int i = 0; i < n; ++i, array = array4) {
            mac.update(array, 0, array.length);
            mac.doFinal(array4, 0);
            mac.update(array4, 0, macSize2);
            mac.update(array2, 0, array2.length);
            mac.doFinal(array5, 0);
            final int n2 = macSize * i;
            System.arraycopy(array5, 0, array3, n2, Math.min(macSize, array3.length - n2));
        }
    }
    
    public static class Mappings extends AlgorithmProvider
    {
        private static final String PREFIX;
        
        static {
            PREFIX = TLSKDF.class.getName();
        }
        
        @Override
        public void configure(final ConfigurableProvider configurableProvider) {
            final StringBuilder sb = new StringBuilder();
            sb.append(Mappings.PREFIX);
            sb.append("$TLS10");
            configurableProvider.addAlgorithm("SecretKeyFactory.TLS10KDF", sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(Mappings.PREFIX);
            sb2.append("$TLS11");
            configurableProvider.addAlgorithm("SecretKeyFactory.TLS11KDF", sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(Mappings.PREFIX);
            sb3.append("$TLS12withSHA256");
            configurableProvider.addAlgorithm("SecretKeyFactory.TLS12WITHSHA256KDF", sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(Mappings.PREFIX);
            sb4.append("$TLS12withSHA384");
            configurableProvider.addAlgorithm("SecretKeyFactory.TLS12WITHSHA384KDF", sb4.toString());
            final StringBuilder sb5 = new StringBuilder();
            sb5.append(Mappings.PREFIX);
            sb5.append("$TLS12withSHA512");
            configurableProvider.addAlgorithm("SecretKeyFactory.TLS12WITHSHA512KDF", sb5.toString());
        }
    }
    
    public static final class TLS10 extends TLSKeyMaterialFactory
    {
        public TLS10() {
            super("TLS10KDF");
        }
        
        @Override
        protected SecretKey engineGenerateSecret(final KeySpec keySpec) throws InvalidKeySpecException {
            if (keySpec instanceof TLSKeyMaterialSpec) {
                return new SecretKeySpec(PRF_legacy((TLSKeyMaterialSpec)keySpec), this.algName);
            }
            throw new InvalidKeySpecException("Invalid KeySpec");
        }
    }
    
    public static final class TLS11 extends TLSKeyMaterialFactory
    {
        public TLS11() {
            super("TLS11KDF");
        }
        
        @Override
        protected SecretKey engineGenerateSecret(final KeySpec keySpec) throws InvalidKeySpecException {
            if (keySpec instanceof TLSKeyMaterialSpec) {
                return new SecretKeySpec(PRF_legacy((TLSKeyMaterialSpec)keySpec), this.algName);
            }
            throw new InvalidKeySpecException("Invalid KeySpec");
        }
    }
    
    public static class TLS12 extends TLSKeyMaterialFactory
    {
        private final Mac prf;
        
        protected TLS12(final String s, final Mac prf) {
            super(s);
            this.prf = prf;
        }
        
        private byte[] PRF(final TLSKeyMaterialSpec tlsKeyMaterialSpec, final Mac mac) {
            final byte[] concatenate = Arrays.concatenate(Strings.toByteArray(tlsKeyMaterialSpec.getLabel()), tlsKeyMaterialSpec.getSeed());
            final byte[] secret = tlsKeyMaterialSpec.getSecret();
            final byte[] array = new byte[tlsKeyMaterialSpec.getLength()];
            hmac_hash(mac, secret, concatenate, array);
            return array;
        }
        
        @Override
        protected SecretKey engineGenerateSecret(final KeySpec keySpec) throws InvalidKeySpecException {
            if (keySpec instanceof TLSKeyMaterialSpec) {
                return new SecretKeySpec(this.PRF((TLSKeyMaterialSpec)keySpec, this.prf), this.algName);
            }
            throw new InvalidKeySpecException("Invalid KeySpec");
        }
    }
    
    public static final class TLS12withSHA256 extends TLS12
    {
        public TLS12withSHA256() {
            super("TLS12withSHA256KDF", new HMac(new SHA256Digest()));
        }
    }
    
    public static final class TLS12withSHA384 extends TLS12
    {
        public TLS12withSHA384() {
            super("TLS12withSHA384KDF", new HMac(new SHA384Digest()));
        }
    }
    
    public static final class TLS12withSHA512 extends TLS12
    {
        public TLS12withSHA512() {
            super("TLS12withSHA512KDF", new HMac(new SHA512Digest()));
        }
    }
    
    public static class TLSKeyMaterialFactory extends BaseSecretKeyFactory
    {
        protected TLSKeyMaterialFactory(final String s) {
            super(s, null);
        }
    }
}
