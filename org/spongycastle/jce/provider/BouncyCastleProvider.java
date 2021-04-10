package org.spongycastle.jce.provider;

import org.spongycastle.jcajce.provider.config.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.pkcs.*;
import java.io.*;
import org.spongycastle.asn1.x509.*;
import java.security.*;
import org.spongycastle.jcajce.provider.symmetric.util.*;
import org.spongycastle.jcajce.provider.util.*;
import org.spongycastle.pqc.asn1.*;
import org.spongycastle.pqc.jcajce.provider.sphincs.*;
import org.spongycastle.pqc.jcajce.provider.newhope.*;
import org.spongycastle.pqc.jcajce.provider.xmss.*;
import org.spongycastle.pqc.jcajce.provider.mceliece.*;
import org.spongycastle.pqc.jcajce.provider.rainbow.*;
import java.util.*;

public final class BouncyCastleProvider extends Provider implements ConfigurableProvider
{
    private static final String[] ASYMMETRIC_CIPHERS;
    private static final String[] ASYMMETRIC_GENERIC;
    private static final String ASYMMETRIC_PACKAGE = "org.spongycastle.jcajce.provider.asymmetric.";
    public static final ProviderConfiguration CONFIGURATION;
    private static final String[] DIGESTS;
    private static final String DIGEST_PACKAGE = "org.spongycastle.jcajce.provider.digest.";
    private static final String[] KEYSTORES;
    private static final String KEYSTORE_PACKAGE = "org.spongycastle.jcajce.provider.keystore.";
    public static final String PROVIDER_NAME = "SC";
    private static final String[] SECURE_RANDOMS;
    private static final String SECURE_RANDOM_PACKAGE = "org.spongycastle.jcajce.provider.drbg.";
    private static final String[] SYMMETRIC_CIPHERS;
    private static final String[] SYMMETRIC_GENERIC;
    private static final String[] SYMMETRIC_MACS;
    private static final String SYMMETRIC_PACKAGE = "org.spongycastle.jcajce.provider.symmetric.";
    private static String info = "BouncyCastle Security Provider v1.58";
    private static final Map keyInfoConverters;
    
    static {
        CONFIGURATION = new BouncyCastleProviderConfiguration();
        keyInfoConverters = new HashMap();
        SYMMETRIC_GENERIC = new String[] { "PBEPBKDF1", "PBEPBKDF2", "PBEPKCS12", "TLSKDF" };
        SYMMETRIC_MACS = new String[] { "SipHash", "Poly1305" };
        SYMMETRIC_CIPHERS = new String[] { "AES", "ARC4", "ARIA", "Blowfish", "Camellia", "CAST5", "CAST6", "ChaCha", "DES", "DESede", "GOST28147", "Grainv1", "Grain128", "HC128", "HC256", "IDEA", "Noekeon", "RC2", "RC5", "RC6", "Rijndael", "Salsa20", "SEED", "Serpent", "Shacal2", "Skipjack", "SM4", "TEA", "Twofish", "Threefish", "VMPC", "VMPCKSA3", "XTEA", "XSalsa20", "OpenSSLPBKDF", "DSTU7624" };
        ASYMMETRIC_GENERIC = new String[] { "X509", "IES" };
        ASYMMETRIC_CIPHERS = new String[] { "DSA", "DH", "EC", "RSA", "GOST", "ECGOST", "ElGamal", "DSTU4145", "GM" };
        DIGESTS = new String[] { "GOST3411", "Keccak", "MD2", "MD4", "MD5", "SHA1", "RIPEMD128", "RIPEMD160", "RIPEMD256", "RIPEMD320", "SHA224", "SHA256", "SHA384", "SHA512", "SHA3", "Skein", "SM3", "Tiger", "Whirlpool", "Blake2b", "DSTU7564" };
        KEYSTORES = new String[] { "BC", "BCFKS", "PKCS12" };
        SECURE_RANDOMS = new String[] { "DRBG" };
    }
    
    public BouncyCastleProvider() {
        super("SC", 1.58, BouncyCastleProvider.info);
        AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction() {
            @Override
            public Object run() {
                BouncyCastleProvider.this.setup();
                return null;
            }
        });
    }
    
    private static AsymmetricKeyInfoConverter getAsymmetricKeyInfoConverter(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        synchronized (BouncyCastleProvider.keyInfoConverters) {
            return BouncyCastleProvider.keyInfoConverters.get(asn1ObjectIdentifier);
        }
    }
    
    public static PrivateKey getPrivateKey(final PrivateKeyInfo privateKeyInfo) throws IOException {
        final AsymmetricKeyInfoConverter asymmetricKeyInfoConverter = getAsymmetricKeyInfoConverter(privateKeyInfo.getPrivateKeyAlgorithm().getAlgorithm());
        if (asymmetricKeyInfoConverter == null) {
            return null;
        }
        return asymmetricKeyInfoConverter.generatePrivate(privateKeyInfo);
    }
    
    public static PublicKey getPublicKey(final SubjectPublicKeyInfo subjectPublicKeyInfo) throws IOException {
        final AsymmetricKeyInfoConverter asymmetricKeyInfoConverter = getAsymmetricKeyInfoConverter(subjectPublicKeyInfo.getAlgorithm().getAlgorithm());
        if (asymmetricKeyInfoConverter == null) {
            return null;
        }
        return asymmetricKeyInfoConverter.generatePublic(subjectPublicKeyInfo);
    }
    
    private void loadAlgorithms(final String s, final String[] array) {
        for (int i = 0; i != array.length; ++i) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(array[i]);
            sb.append("$Mappings");
            final Class loadClass = ClassUtil.loadClass(BouncyCastleProvider.class, sb.toString());
            if (loadClass != null) {
                try {
                    loadClass.newInstance().configure(this);
                }
                catch (Exception ex) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("cannot create instance of ");
                    sb2.append(s);
                    sb2.append(array[i]);
                    sb2.append("$Mappings : ");
                    sb2.append(ex);
                    throw new InternalError(sb2.toString());
                }
            }
        }
    }
    
    private void loadPQCKeys() {
        this.addKeyInfoConverter(PQCObjectIdentifiers.sphincs256, new Sphincs256KeyFactorySpi());
        this.addKeyInfoConverter(PQCObjectIdentifiers.newHope, new NHKeyFactorySpi());
        this.addKeyInfoConverter(PQCObjectIdentifiers.xmss, new XMSSKeyFactorySpi());
        this.addKeyInfoConverter(PQCObjectIdentifiers.xmss_mt, new XMSSMTKeyFactorySpi());
        this.addKeyInfoConverter(PQCObjectIdentifiers.mcEliece, new McElieceKeyFactorySpi());
        this.addKeyInfoConverter(PQCObjectIdentifiers.mcElieceCca2, new McElieceCCA2KeyFactorySpi());
        this.addKeyInfoConverter(PQCObjectIdentifiers.rainbow, new RainbowKeyFactorySpi());
    }
    
    private void setup() {
        this.loadAlgorithms("org.spongycastle.jcajce.provider.digest.", BouncyCastleProvider.DIGESTS);
        this.loadAlgorithms("org.spongycastle.jcajce.provider.symmetric.", BouncyCastleProvider.SYMMETRIC_GENERIC);
        this.loadAlgorithms("org.spongycastle.jcajce.provider.symmetric.", BouncyCastleProvider.SYMMETRIC_MACS);
        this.loadAlgorithms("org.spongycastle.jcajce.provider.symmetric.", BouncyCastleProvider.SYMMETRIC_CIPHERS);
        this.loadAlgorithms("org.spongycastle.jcajce.provider.asymmetric.", BouncyCastleProvider.ASYMMETRIC_GENERIC);
        this.loadAlgorithms("org.spongycastle.jcajce.provider.asymmetric.", BouncyCastleProvider.ASYMMETRIC_CIPHERS);
        this.loadAlgorithms("org.spongycastle.jcajce.provider.keystore.", BouncyCastleProvider.KEYSTORES);
        this.loadAlgorithms("org.spongycastle.jcajce.provider.drbg.", BouncyCastleProvider.SECURE_RANDOMS);
        this.loadPQCKeys();
        this.put("X509Store.CERTIFICATE/COLLECTION", "org.spongycastle.jce.provider.X509StoreCertCollection");
        this.put("X509Store.ATTRIBUTECERTIFICATE/COLLECTION", "org.spongycastle.jce.provider.X509StoreAttrCertCollection");
        this.put("X509Store.CRL/COLLECTION", "org.spongycastle.jce.provider.X509StoreCRLCollection");
        this.put("X509Store.CERTIFICATEPAIR/COLLECTION", "org.spongycastle.jce.provider.X509StoreCertPairCollection");
        this.put("X509Store.CERTIFICATE/LDAP", "org.spongycastle.jce.provider.X509StoreLDAPCerts");
        this.put("X509Store.CRL/LDAP", "org.spongycastle.jce.provider.X509StoreLDAPCRLs");
        this.put("X509Store.ATTRIBUTECERTIFICATE/LDAP", "org.spongycastle.jce.provider.X509StoreLDAPAttrCerts");
        this.put("X509Store.CERTIFICATEPAIR/LDAP", "org.spongycastle.jce.provider.X509StoreLDAPCertPairs");
        this.put("X509StreamParser.CERTIFICATE", "org.spongycastle.jce.provider.X509CertParser");
        this.put("X509StreamParser.ATTRIBUTECERTIFICATE", "org.spongycastle.jce.provider.X509AttrCertParser");
        this.put("X509StreamParser.CRL", "org.spongycastle.jce.provider.X509CRLParser");
        this.put("X509StreamParser.CERTIFICATEPAIR", "org.spongycastle.jce.provider.X509CertPairParser");
        this.put("Cipher.BROKENPBEWITHMD5ANDDES", "org.spongycastle.jce.provider.BrokenJCEBlockCipher$BrokePBEWithMD5AndDES");
        this.put("Cipher.BROKENPBEWITHSHA1ANDDES", "org.spongycastle.jce.provider.BrokenJCEBlockCipher$BrokePBEWithSHA1AndDES");
        this.put("Cipher.OLDPBEWITHSHAANDTWOFISH-CBC", "org.spongycastle.jce.provider.BrokenJCEBlockCipher$OldPBEWithSHAAndTwofish");
        this.put("CertPathValidator.RFC3281", "org.spongycastle.jce.provider.PKIXAttrCertPathValidatorSpi");
        this.put("CertPathBuilder.RFC3281", "org.spongycastle.jce.provider.PKIXAttrCertPathBuilderSpi");
        this.put("CertPathValidator.RFC3280", "org.spongycastle.jce.provider.PKIXCertPathValidatorSpi");
        this.put("CertPathBuilder.RFC3280", "org.spongycastle.jce.provider.PKIXCertPathBuilderSpi");
        this.put("CertPathValidator.PKIX", "org.spongycastle.jce.provider.PKIXCertPathValidatorSpi");
        this.put("CertPathBuilder.PKIX", "org.spongycastle.jce.provider.PKIXCertPathBuilderSpi");
        this.put("CertStore.Collection", "org.spongycastle.jce.provider.CertStoreCollectionSpi");
        this.put("CertStore.LDAP", "org.spongycastle.jce.provider.X509LDAPCertStoreSpi");
        this.put("CertStore.Multi", "org.spongycastle.jce.provider.MultiCertStoreSpi");
        this.put("Alg.Alias.CertStore.X509LDAP", "LDAP");
    }
    
    @Override
    public void addAlgorithm(final String s, final String s2) {
        if (!this.containsKey(s)) {
            this.put(s, s2);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("duplicate provider key (");
        sb.append(s);
        sb.append(") found");
        throw new IllegalStateException(sb.toString());
    }
    
    @Override
    public void addAlgorithm(final String s, final ASN1ObjectIdentifier asn1ObjectIdentifier, final String s2) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(".");
        sb.append(asn1ObjectIdentifier);
        this.addAlgorithm(sb.toString(), s2);
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(s);
        sb2.append(".OID.");
        sb2.append(asn1ObjectIdentifier);
        this.addAlgorithm(sb2.toString(), s2);
    }
    
    @Override
    public void addAttributes(final String s, final Map<String, String> map) {
        for (final String s2 : map.keySet()) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(" ");
            sb.append(s2);
            final String string = sb.toString();
            if (this.containsKey(string)) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("duplicate provider attribute key (");
                sb2.append(string);
                sb2.append(") found");
                throw new IllegalStateException(sb2.toString());
            }
            this.put(string, map.get(s2));
        }
    }
    
    @Override
    public void addKeyInfoConverter(final ASN1ObjectIdentifier asn1ObjectIdentifier, final AsymmetricKeyInfoConverter asymmetricKeyInfoConverter) {
        synchronized (BouncyCastleProvider.keyInfoConverters) {
            BouncyCastleProvider.keyInfoConverters.put(asn1ObjectIdentifier, asymmetricKeyInfoConverter);
        }
    }
    
    @Override
    public boolean hasAlgorithm(final String s, final String s2) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(".");
        sb.append(s2);
        if (!this.containsKey(sb.toString())) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Alg.Alias.");
            sb2.append(s);
            sb2.append(".");
            sb2.append(s2);
            if (!this.containsKey(sb2.toString())) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void setParameter(final String s, final Object o) {
        synchronized (BouncyCastleProvider.CONFIGURATION) {
            ((BouncyCastleProviderConfiguration)BouncyCastleProvider.CONFIGURATION).setParameter(s, o);
        }
    }
}
