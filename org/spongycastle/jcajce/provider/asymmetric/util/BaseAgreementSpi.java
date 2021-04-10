package org.spongycastle.jcajce.provider.asymmetric.util;

import org.spongycastle.asn1.*;
import java.util.*;
import org.spongycastle.asn1.nist.*;
import org.spongycastle.asn1.ntt.*;
import org.spongycastle.asn1.kisa.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.asn1.oiw.*;
import org.spongycastle.asn1.cryptopro.*;
import org.spongycastle.asn1.misc.*;
import org.spongycastle.asn1.gnu.*;
import org.spongycastle.util.*;
import javax.crypto.*;
import java.security.*;
import org.spongycastle.crypto.agreement.kdf.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;
import javax.crypto.spec.*;

public abstract class BaseAgreementSpi extends KeyAgreementSpi
{
    private static final Map<String, ASN1ObjectIdentifier> defaultOids;
    private static final Hashtable des;
    private static final Map<String, Integer> keySizes;
    private static final Map<String, String> nameTable;
    private static final Hashtable oids;
    private final String kaAlgorithm;
    private final DerivationFunction kdf;
    protected byte[] ukmParameters;
    
    static {
        defaultOids = new HashMap<String, ASN1ObjectIdentifier>();
        keySizes = new HashMap<String, Integer>();
        nameTable = new HashMap<String, String>();
        oids = new Hashtable();
        des = new Hashtable();
        final Integer value = Integers.valueOf(64);
        final Integer value2 = Integers.valueOf(128);
        final Integer value3 = Integers.valueOf(192);
        final Integer value4 = Integers.valueOf(256);
        BaseAgreementSpi.keySizes.put("DES", value);
        BaseAgreementSpi.keySizes.put("DESEDE", value3);
        BaseAgreementSpi.keySizes.put("BLOWFISH", value2);
        BaseAgreementSpi.keySizes.put("AES", value4);
        BaseAgreementSpi.keySizes.put(NISTObjectIdentifiers.id_aes128_ECB.getId(), value2);
        BaseAgreementSpi.keySizes.put(NISTObjectIdentifiers.id_aes192_ECB.getId(), value3);
        BaseAgreementSpi.keySizes.put(NISTObjectIdentifiers.id_aes256_ECB.getId(), value4);
        BaseAgreementSpi.keySizes.put(NISTObjectIdentifiers.id_aes128_CBC.getId(), value2);
        BaseAgreementSpi.keySizes.put(NISTObjectIdentifiers.id_aes192_CBC.getId(), value3);
        BaseAgreementSpi.keySizes.put(NISTObjectIdentifiers.id_aes256_CBC.getId(), value4);
        BaseAgreementSpi.keySizes.put(NISTObjectIdentifiers.id_aes128_CFB.getId(), value2);
        BaseAgreementSpi.keySizes.put(NISTObjectIdentifiers.id_aes192_CFB.getId(), value3);
        BaseAgreementSpi.keySizes.put(NISTObjectIdentifiers.id_aes256_CFB.getId(), value4);
        BaseAgreementSpi.keySizes.put(NISTObjectIdentifiers.id_aes128_OFB.getId(), value2);
        BaseAgreementSpi.keySizes.put(NISTObjectIdentifiers.id_aes192_OFB.getId(), value3);
        BaseAgreementSpi.keySizes.put(NISTObjectIdentifiers.id_aes256_OFB.getId(), value4);
        BaseAgreementSpi.keySizes.put(NISTObjectIdentifiers.id_aes128_wrap.getId(), value2);
        BaseAgreementSpi.keySizes.put(NISTObjectIdentifiers.id_aes192_wrap.getId(), value3);
        BaseAgreementSpi.keySizes.put(NISTObjectIdentifiers.id_aes256_wrap.getId(), value4);
        BaseAgreementSpi.keySizes.put(NISTObjectIdentifiers.id_aes128_CCM.getId(), value2);
        BaseAgreementSpi.keySizes.put(NISTObjectIdentifiers.id_aes192_CCM.getId(), value3);
        BaseAgreementSpi.keySizes.put(NISTObjectIdentifiers.id_aes256_CCM.getId(), value4);
        BaseAgreementSpi.keySizes.put(NISTObjectIdentifiers.id_aes128_GCM.getId(), value2);
        BaseAgreementSpi.keySizes.put(NISTObjectIdentifiers.id_aes192_GCM.getId(), value3);
        BaseAgreementSpi.keySizes.put(NISTObjectIdentifiers.id_aes256_GCM.getId(), value4);
        BaseAgreementSpi.keySizes.put(NTTObjectIdentifiers.id_camellia128_wrap.getId(), value2);
        BaseAgreementSpi.keySizes.put(NTTObjectIdentifiers.id_camellia192_wrap.getId(), value3);
        BaseAgreementSpi.keySizes.put(NTTObjectIdentifiers.id_camellia256_wrap.getId(), value4);
        BaseAgreementSpi.keySizes.put(KISAObjectIdentifiers.id_npki_app_cmsSeed_wrap.getId(), value2);
        BaseAgreementSpi.keySizes.put(PKCSObjectIdentifiers.id_alg_CMS3DESwrap.getId(), value3);
        BaseAgreementSpi.keySizes.put(PKCSObjectIdentifiers.des_EDE3_CBC.getId(), value3);
        BaseAgreementSpi.keySizes.put(OIWObjectIdentifiers.desCBC.getId(), value);
        BaseAgreementSpi.keySizes.put(CryptoProObjectIdentifiers.gostR28147_gcfb.getId(), value4);
        BaseAgreementSpi.keySizes.put(CryptoProObjectIdentifiers.id_Gost28147_89_None_KeyWrap.getId(), value4);
        BaseAgreementSpi.keySizes.put(CryptoProObjectIdentifiers.id_Gost28147_89_CryptoPro_KeyWrap.getId(), value4);
        BaseAgreementSpi.keySizes.put(PKCSObjectIdentifiers.id_hmacWithSHA1.getId(), Integers.valueOf(160));
        BaseAgreementSpi.keySizes.put(PKCSObjectIdentifiers.id_hmacWithSHA256.getId(), value4);
        BaseAgreementSpi.keySizes.put(PKCSObjectIdentifiers.id_hmacWithSHA384.getId(), Integers.valueOf(384));
        BaseAgreementSpi.keySizes.put(PKCSObjectIdentifiers.id_hmacWithSHA512.getId(), Integers.valueOf(512));
        BaseAgreementSpi.defaultOids.put("DESEDE", PKCSObjectIdentifiers.des_EDE3_CBC);
        BaseAgreementSpi.defaultOids.put("AES", NISTObjectIdentifiers.id_aes256_CBC);
        BaseAgreementSpi.defaultOids.put("CAMELLIA", NTTObjectIdentifiers.id_camellia256_cbc);
        BaseAgreementSpi.defaultOids.put("SEED", KISAObjectIdentifiers.id_seedCBC);
        BaseAgreementSpi.defaultOids.put("DES", OIWObjectIdentifiers.desCBC);
        BaseAgreementSpi.nameTable.put(MiscObjectIdentifiers.cast5CBC.getId(), "CAST5");
        BaseAgreementSpi.nameTable.put(MiscObjectIdentifiers.as_sys_sec_alg_ideaCBC.getId(), "IDEA");
        BaseAgreementSpi.nameTable.put(MiscObjectIdentifiers.cryptlib_algorithm_blowfish_ECB.getId(), "Blowfish");
        BaseAgreementSpi.nameTable.put(MiscObjectIdentifiers.cryptlib_algorithm_blowfish_CBC.getId(), "Blowfish");
        BaseAgreementSpi.nameTable.put(MiscObjectIdentifiers.cryptlib_algorithm_blowfish_CFB.getId(), "Blowfish");
        BaseAgreementSpi.nameTable.put(MiscObjectIdentifiers.cryptlib_algorithm_blowfish_OFB.getId(), "Blowfish");
        BaseAgreementSpi.nameTable.put(OIWObjectIdentifiers.desECB.getId(), "DES");
        BaseAgreementSpi.nameTable.put(OIWObjectIdentifiers.desCBC.getId(), "DES");
        BaseAgreementSpi.nameTable.put(OIWObjectIdentifiers.desCFB.getId(), "DES");
        BaseAgreementSpi.nameTable.put(OIWObjectIdentifiers.desOFB.getId(), "DES");
        BaseAgreementSpi.nameTable.put(OIWObjectIdentifiers.desEDE.getId(), "DESede");
        BaseAgreementSpi.nameTable.put(PKCSObjectIdentifiers.des_EDE3_CBC.getId(), "DESede");
        BaseAgreementSpi.nameTable.put(PKCSObjectIdentifiers.id_alg_CMS3DESwrap.getId(), "DESede");
        BaseAgreementSpi.nameTable.put(PKCSObjectIdentifiers.id_alg_CMSRC2wrap.getId(), "RC2");
        BaseAgreementSpi.nameTable.put(PKCSObjectIdentifiers.id_hmacWithSHA1.getId(), "HmacSHA1");
        BaseAgreementSpi.nameTable.put(PKCSObjectIdentifiers.id_hmacWithSHA224.getId(), "HmacSHA224");
        BaseAgreementSpi.nameTable.put(PKCSObjectIdentifiers.id_hmacWithSHA256.getId(), "HmacSHA256");
        BaseAgreementSpi.nameTable.put(PKCSObjectIdentifiers.id_hmacWithSHA384.getId(), "HmacSHA384");
        BaseAgreementSpi.nameTable.put(PKCSObjectIdentifiers.id_hmacWithSHA512.getId(), "HmacSHA512");
        BaseAgreementSpi.nameTable.put(NTTObjectIdentifiers.id_camellia128_cbc.getId(), "Camellia");
        BaseAgreementSpi.nameTable.put(NTTObjectIdentifiers.id_camellia192_cbc.getId(), "Camellia");
        BaseAgreementSpi.nameTable.put(NTTObjectIdentifiers.id_camellia256_cbc.getId(), "Camellia");
        BaseAgreementSpi.nameTable.put(NTTObjectIdentifiers.id_camellia128_wrap.getId(), "Camellia");
        BaseAgreementSpi.nameTable.put(NTTObjectIdentifiers.id_camellia192_wrap.getId(), "Camellia");
        BaseAgreementSpi.nameTable.put(NTTObjectIdentifiers.id_camellia256_wrap.getId(), "Camellia");
        BaseAgreementSpi.nameTable.put(KISAObjectIdentifiers.id_npki_app_cmsSeed_wrap.getId(), "SEED");
        BaseAgreementSpi.nameTable.put(KISAObjectIdentifiers.id_seedCBC.getId(), "SEED");
        BaseAgreementSpi.nameTable.put(KISAObjectIdentifiers.id_seedMAC.getId(), "SEED");
        BaseAgreementSpi.nameTable.put(CryptoProObjectIdentifiers.gostR28147_gcfb.getId(), "GOST28147");
        BaseAgreementSpi.nameTable.put(NISTObjectIdentifiers.id_aes128_wrap.getId(), "AES");
        BaseAgreementSpi.nameTable.put(NISTObjectIdentifiers.id_aes128_CCM.getId(), "AES");
        BaseAgreementSpi.nameTable.put(NISTObjectIdentifiers.id_aes128_CCM.getId(), "AES");
        BaseAgreementSpi.oids.put("DESEDE", PKCSObjectIdentifiers.des_EDE3_CBC);
        BaseAgreementSpi.oids.put("AES", NISTObjectIdentifiers.id_aes256_CBC);
        BaseAgreementSpi.oids.put("DES", OIWObjectIdentifiers.desCBC);
        BaseAgreementSpi.des.put("DES", "DES");
        BaseAgreementSpi.des.put("DESEDE", "DES");
        BaseAgreementSpi.des.put(OIWObjectIdentifiers.desCBC.getId(), "DES");
        BaseAgreementSpi.des.put(PKCSObjectIdentifiers.des_EDE3_CBC.getId(), "DES");
        BaseAgreementSpi.des.put(PKCSObjectIdentifiers.id_alg_CMS3DESwrap.getId(), "DES");
    }
    
    public BaseAgreementSpi(final String kaAlgorithm, final DerivationFunction kdf) {
        this.kaAlgorithm = kaAlgorithm;
        this.kdf = kdf;
    }
    
    protected static String getAlgorithm(final String s) {
        if (s.indexOf(91) > 0) {
            return s.substring(0, s.indexOf(91));
        }
        if (s.startsWith(NISTObjectIdentifiers.aes.getId())) {
            return "AES";
        }
        if (s.startsWith(GNUObjectIdentifiers.Serpent.getId())) {
            return "Serpent";
        }
        final String s2 = BaseAgreementSpi.nameTable.get(Strings.toUpperCase(s));
        if (s2 != null) {
            return s2;
        }
        return s;
    }
    
    protected static int getKeySize(String upperCase) {
        if (upperCase.indexOf(91) > 0) {
            return Integer.parseInt(upperCase.substring(upperCase.indexOf(91) + 1, upperCase.indexOf(93)));
        }
        upperCase = Strings.toUpperCase(upperCase);
        if (!BaseAgreementSpi.keySizes.containsKey(upperCase)) {
            return -1;
        }
        return BaseAgreementSpi.keySizes.get(upperCase);
    }
    
    protected static byte[] trimZeroes(final byte[] array) {
        if (array[0] != 0) {
            return array;
        }
        int n;
        for (n = 0; n < array.length && array[n] == 0; ++n) {}
        final int n2 = array.length - n;
        final byte[] array2 = new byte[n2];
        System.arraycopy(array, n, array2, 0, n2);
        return array2;
    }
    
    protected abstract byte[] calcSecret();
    
    @Override
    protected int engineGenerateSecret(final byte[] array, final int n) throws IllegalStateException, ShortBufferException {
        final byte[] engineGenerateSecret = this.engineGenerateSecret();
        if (array.length - n >= engineGenerateSecret.length) {
            System.arraycopy(engineGenerateSecret, 0, array, n, engineGenerateSecret.length);
            return engineGenerateSecret.length;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(this.kaAlgorithm);
        sb.append(" key agreement: need ");
        sb.append(engineGenerateSecret.length);
        sb.append(" bytes");
        throw new ShortBufferException(sb.toString());
    }
    
    @Override
    protected SecretKey engineGenerateSecret(String algorithm) throws NoSuchAlgorithmException {
        final byte[] calcSecret = this.calcSecret();
        final String upperCase = Strings.toUpperCase(algorithm);
        Object id;
        if (BaseAgreementSpi.oids.containsKey(upperCase)) {
            id = BaseAgreementSpi.oids.get(upperCase).getId();
        }
        else {
            id = algorithm;
        }
        final int keySize = getKeySize((String)id);
        final DerivationFunction kdf = this.kdf;
        byte[] oddParity;
        if (kdf != null) {
            if (keySize < 0) {
                final StringBuilder sb = new StringBuilder();
                sb.append("unknown algorithm encountered: ");
                sb.append((String)id);
                throw new NoSuchAlgorithmException(sb.toString());
            }
            final int n = keySize / 8;
            final byte[] array = new byte[n];
            Label_0167: {
                if (kdf instanceof DHKEKGenerator) {
                    try {
                        id = new DHKDFParameters(new ASN1ObjectIdentifier((String)id), keySize, calcSecret, this.ukmParameters);
                        break Label_0167;
                    }
                    catch (IllegalArgumentException ex) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("no OID for algorithm: ");
                        sb2.append((String)id);
                        throw new NoSuchAlgorithmException(sb2.toString());
                    }
                }
                id = new KDFParameters(calcSecret, this.ukmParameters);
            }
            this.kdf.init((DerivationParameters)id);
            this.kdf.generateBytes(array, 0, n);
            oddParity = array;
        }
        else {
            oddParity = calcSecret;
            if (keySize > 0) {
                final int n2 = keySize / 8;
                oddParity = new byte[n2];
                System.arraycopy(calcSecret, 0, oddParity, 0, n2);
            }
        }
        algorithm = getAlgorithm(algorithm);
        if (BaseAgreementSpi.des.containsKey(algorithm)) {
            DESParameters.setOddParity(oddParity);
        }
        return new SecretKeySpec(oddParity, algorithm);
    }
    
    @Override
    protected byte[] engineGenerateSecret() throws IllegalStateException {
        if (this.kdf == null) {
            return this.calcSecret();
        }
        throw new UnsupportedOperationException("KDF can only be used when algorithm is known");
    }
}
