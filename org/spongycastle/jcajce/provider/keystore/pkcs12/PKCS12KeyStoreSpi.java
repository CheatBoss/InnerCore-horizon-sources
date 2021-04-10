package org.spongycastle.jcajce.provider.keystore.pkcs12;

import org.spongycastle.jcajce.util.*;
import org.spongycastle.asn1.oiw.*;
import javax.crypto.spec.*;
import org.spongycastle.jcajce.spec.*;
import java.security.spec.*;
import javax.crypto.*;
import org.spongycastle.jce.interfaces.*;
import org.spongycastle.crypto.util.*;
import org.spongycastle.crypto.*;
import java.math.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;
import org.spongycastle.util.encoders.*;
import org.spongycastle.asn1.util.*;
import org.spongycastle.asn1.pkcs.*;
import java.io.*;
import java.security.*;
import org.spongycastle.jcajce.*;
import org.spongycastle.jce.provider.*;
import java.security.cert.*;
import org.spongycastle.asn1.nist.*;
import org.spongycastle.asn1.ntt.*;
import org.spongycastle.asn1.cryptopro.*;
import java.util.*;
import org.spongycastle.util.*;

public class PKCS12KeyStoreSpi extends KeyStoreSpi implements PKCSObjectIdentifiers, X509ObjectIdentifiers, BCKeyStore
{
    static final int CERTIFICATE = 1;
    static final int KEY = 2;
    static final int KEY_PRIVATE = 0;
    static final int KEY_PUBLIC = 1;
    static final int KEY_SECRET = 2;
    private static final int MIN_ITERATIONS = 1024;
    static final int NULL = 0;
    static final String PKCS12_MAX_IT_COUNT_PROPERTY = "org.spongycastle.pkcs12.max_it_count";
    private static final int SALT_SIZE = 20;
    static final int SEALED = 4;
    static final int SECRET = 3;
    private static final DefaultSecretKeyProvider keySizeProvider;
    private ASN1ObjectIdentifier certAlgorithm;
    private CertificateFactory certFact;
    private IgnoresCaseHashtable certs;
    private Hashtable chainCerts;
    private final JcaJceHelper helper;
    private int itCount;
    private ASN1ObjectIdentifier keyAlgorithm;
    private Hashtable keyCerts;
    private IgnoresCaseHashtable keys;
    private Hashtable localIds;
    private AlgorithmIdentifier macAlgorithm;
    protected SecureRandom random;
    private int saltLength;
    
    static {
        keySizeProvider = new DefaultSecretKeyProvider();
    }
    
    public PKCS12KeyStoreSpi(final Provider provider, final ASN1ObjectIdentifier keyAlgorithm, final ASN1ObjectIdentifier certAlgorithm) {
        this.helper = new BCJcaJceHelper();
        this.keys = new IgnoresCaseHashtable();
        this.localIds = new Hashtable();
        this.certs = new IgnoresCaseHashtable();
        this.chainCerts = new Hashtable();
        this.keyCerts = new Hashtable();
        this.random = new SecureRandom();
        this.macAlgorithm = new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1, DERNull.INSTANCE);
        this.itCount = 1024;
        this.saltLength = 20;
        this.keyAlgorithm = keyAlgorithm;
        this.certAlgorithm = certAlgorithm;
        Label_0138: {
            if (provider == null) {
                break Label_0138;
            }
            while (true) {
                try {
                    this.certFact = CertificateFactory.getInstance("X.509", provider);
                    return;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("can't create cert factory - ");
                    final Exception ex;
                    sb.append(ex.toString());
                    throw new IllegalArgumentException(sb.toString());
                    this.certFact = CertificateFactory.getInstance("X.509");
                }
                catch (Exception ex) {
                    continue;
                }
                break;
            }
        }
    }
    
    private byte[] calculatePbeMac(final ASN1ObjectIdentifier asn1ObjectIdentifier, final byte[] array, final int n, final char[] array2, final boolean b, final byte[] array3) throws Exception {
        final PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(array, n);
        final Mac mac = this.helper.createMac(asn1ObjectIdentifier.getId());
        mac.init(new PKCS12Key(array2, b), pbeParameterSpec);
        mac.update(array3);
        return mac.doFinal();
    }
    
    private Cipher createCipher(final int n, final char[] array, final AlgorithmIdentifier algorithmIdentifier) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchProviderException {
        final PBES2Parameters instance = PBES2Parameters.getInstance(algorithmIdentifier.getParameters());
        final PBKDF2Params instance2 = PBKDF2Params.getInstance(instance.getKeyDerivationFunc().getParameters());
        final AlgorithmIdentifier instance3 = AlgorithmIdentifier.getInstance(instance.getEncryptionScheme());
        final SecretKeyFactory secretKeyFactory = this.helper.createSecretKeyFactory(instance.getKeyDerivationFunc().getAlgorithm().getId());
        SecretKey secretKey;
        if (instance2.isDefaultPrf()) {
            secretKey = secretKeyFactory.generateSecret(new PBEKeySpec(array, instance2.getSalt(), this.validateIterationCount(instance2.getIterationCount()), PKCS12KeyStoreSpi.keySizeProvider.getKeySize(instance3)));
        }
        else {
            secretKey = secretKeyFactory.generateSecret(new PBKDF2KeySpec(array, instance2.getSalt(), this.validateIterationCount(instance2.getIterationCount()), PKCS12KeyStoreSpi.keySizeProvider.getKeySize(instance3), instance2.getPrf()));
        }
        final Cipher instance4 = Cipher.getInstance(instance.getEncryptionScheme().getAlgorithm().getId());
        final ASN1Encodable parameters = instance.getEncryptionScheme().getParameters();
        AlgorithmParameterSpec algorithmParameterSpec;
        if (parameters instanceof ASN1OctetString) {
            algorithmParameterSpec = new IvParameterSpec(ASN1OctetString.getInstance(parameters).getOctets());
        }
        else {
            final GOST28147Parameters instance5 = GOST28147Parameters.getInstance(parameters);
            algorithmParameterSpec = new GOST28147ParameterSpec(instance5.getEncryptionParamSet(), instance5.getIV());
        }
        instance4.init(n, secretKey, algorithmParameterSpec);
        return instance4;
    }
    
    private SubjectKeyIdentifier createSubjectKeyId(final PublicKey publicKey) {
        try {
            return new SubjectKeyIdentifier(getDigest(SubjectPublicKeyInfo.getInstance(publicKey.getEncoded())));
        }
        catch (Exception ex) {
            throw new RuntimeException("error creating key");
        }
    }
    
    private void doStore(final OutputStream outputStream, final char[] array, final boolean b) throws IOException {
        if (array != null) {
            final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
            final Enumeration keys = this.keys.keys();
            while (keys.hasMoreElements()) {
                final byte[] array2 = new byte[20];
                this.random.nextBytes(array2);
                final String s = keys.nextElement();
                final PrivateKey privateKey = (PrivateKey)this.keys.get(s);
                final PKCS12PBEParams pkcs12PBEParams = new PKCS12PBEParams(array2, 1024);
                final EncryptedPrivateKeyInfo encryptedPrivateKeyInfo = new EncryptedPrivateKeyInfo(new AlgorithmIdentifier(this.keyAlgorithm, pkcs12PBEParams.toASN1Primitive()), this.wrapKey(this.keyAlgorithm.getId(), privateKey, pkcs12PBEParams, array));
                final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
                boolean b2;
                if (privateKey instanceof PKCS12BagAttributeCarrier) {
                    final PKCS12BagAttributeCarrier pkcs12BagAttributeCarrier = (PKCS12BagAttributeCarrier)privateKey;
                    final DERBMPString derbmpString = (DERBMPString)pkcs12BagAttributeCarrier.getBagAttribute(PKCS12KeyStoreSpi.pkcs_9_at_friendlyName);
                    if (derbmpString == null || !derbmpString.getString().equals(s)) {
                        pkcs12BagAttributeCarrier.setBagAttribute(PKCS12KeyStoreSpi.pkcs_9_at_friendlyName, new DERBMPString(s));
                    }
                    if (pkcs12BagAttributeCarrier.getBagAttribute(PKCS12KeyStoreSpi.pkcs_9_at_localKeyId) == null) {
                        pkcs12BagAttributeCarrier.setBagAttribute(PKCS12KeyStoreSpi.pkcs_9_at_localKeyId, this.createSubjectKeyId(this.engineGetCertificate(s).getPublicKey()));
                    }
                    final Enumeration bagAttributeKeys = pkcs12BagAttributeCarrier.getBagAttributeKeys();
                    b2 = false;
                    while (bagAttributeKeys.hasMoreElements()) {
                        final ASN1ObjectIdentifier asn1ObjectIdentifier = bagAttributeKeys.nextElement();
                        final ASN1EncodableVector asn1EncodableVector3 = new ASN1EncodableVector();
                        asn1EncodableVector3.add(asn1ObjectIdentifier);
                        asn1EncodableVector3.add(new DERSet(pkcs12BagAttributeCarrier.getBagAttribute(asn1ObjectIdentifier)));
                        asn1EncodableVector2.add(new DERSequence(asn1EncodableVector3));
                        b2 = true;
                    }
                }
                else {
                    b2 = false;
                }
                if (!b2) {
                    final ASN1EncodableVector asn1EncodableVector4 = new ASN1EncodableVector();
                    final Certificate engineGetCertificate = this.engineGetCertificate(s);
                    asn1EncodableVector4.add(PKCS12KeyStoreSpi.pkcs_9_at_localKeyId);
                    asn1EncodableVector4.add(new DERSet(this.createSubjectKeyId(engineGetCertificate.getPublicKey())));
                    asn1EncodableVector2.add(new DERSequence(asn1EncodableVector4));
                    final ASN1EncodableVector asn1EncodableVector5 = new ASN1EncodableVector();
                    asn1EncodableVector5.add(PKCS12KeyStoreSpi.pkcs_9_at_friendlyName);
                    asn1EncodableVector5.add(new DERSet(new DERBMPString(s)));
                    asn1EncodableVector2.add(new DERSequence(asn1EncodableVector5));
                }
                asn1EncodableVector.add(new SafeBag(PKCS12KeyStoreSpi.pkcs8ShroudedKeyBag, encryptedPrivateKeyInfo.toASN1Primitive(), new DERSet(asn1EncodableVector2)));
            }
            final BEROctetString berOctetString = new BEROctetString(new DERSequence(asn1EncodableVector).getEncoded("DER"));
            final byte[] array3 = new byte[20];
            this.random.nextBytes(array3);
            final ASN1EncodableVector asn1EncodableVector6 = new ASN1EncodableVector();
            final AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(this.certAlgorithm, new PKCS12PBEParams(array3, 1024).toASN1Primitive());
            final Hashtable<Certificate, Certificate> hashtable = new Hashtable<Certificate, Certificate>();
            final Enumeration keys2 = this.keys.keys();
        Label_1034:
            while (keys2.hasMoreElements()) {
            Label_0845_Outer:
                while (true) {
                    while (true) {
                        Label_2088: {
                            while (true) {
                                Label_2085: {
                                    try {
                                        final String s2 = keys2.nextElement();
                                        final Certificate engineGetCertificate2 = this.engineGetCertificate(s2);
                                        final CertBag certBag = new CertBag(PKCS12KeyStoreSpi.x509Certificate, new DEROctetString(engineGetCertificate2.getEncoded()));
                                        final ASN1EncodableVector asn1EncodableVector7 = new ASN1EncodableVector();
                                        if (!(engineGetCertificate2 instanceof PKCS12BagAttributeCarrier)) {
                                            break Label_2088;
                                        }
                                        final PKCS12BagAttributeCarrier pkcs12BagAttributeCarrier2 = (PKCS12BagAttributeCarrier)engineGetCertificate2;
                                        final DERBMPString derbmpString2 = (DERBMPString)pkcs12BagAttributeCarrier2.getBagAttribute(PKCS12KeyStoreSpi.pkcs_9_at_friendlyName);
                                        if (derbmpString2 == null || !derbmpString2.getString().equals(s2)) {
                                            pkcs12BagAttributeCarrier2.setBagAttribute(PKCS12KeyStoreSpi.pkcs_9_at_friendlyName, new DERBMPString(s2));
                                            if (pkcs12BagAttributeCarrier2.getBagAttribute(PKCS12KeyStoreSpi.pkcs_9_at_localKeyId) == null) {
                                                pkcs12BagAttributeCarrier2.setBagAttribute(PKCS12KeyStoreSpi.pkcs_9_at_localKeyId, this.createSubjectKeyId(engineGetCertificate2.getPublicKey()));
                                            }
                                            final Enumeration bagAttributeKeys2 = pkcs12BagAttributeCarrier2.getBagAttributeKeys();
                                            int n = 0;
                                            while (bagAttributeKeys2.hasMoreElements()) {
                                                final ASN1ObjectIdentifier asn1ObjectIdentifier2 = bagAttributeKeys2.nextElement();
                                                final ASN1EncodableVector asn1EncodableVector8 = new ASN1EncodableVector();
                                                asn1EncodableVector8.add(asn1ObjectIdentifier2);
                                                asn1EncodableVector8.add(new DERSet(pkcs12BagAttributeCarrier2.getBagAttribute(asn1ObjectIdentifier2)));
                                                asn1EncodableVector7.add(new DERSequence(asn1EncodableVector8));
                                                n = 1;
                                            }
                                            if (n == 0) {
                                                final ASN1EncodableVector asn1EncodableVector9 = new ASN1EncodableVector();
                                                asn1EncodableVector9.add(PKCS12KeyStoreSpi.pkcs_9_at_localKeyId);
                                                asn1EncodableVector9.add(new DERSet(this.createSubjectKeyId(engineGetCertificate2.getPublicKey())));
                                                asn1EncodableVector7.add(new DERSequence(asn1EncodableVector9));
                                                final ASN1EncodableVector asn1EncodableVector10 = new ASN1EncodableVector();
                                                asn1EncodableVector10.add(PKCS12KeyStoreSpi.pkcs_9_at_friendlyName);
                                                asn1EncodableVector10.add(new DERSet(new DERBMPString(s2)));
                                                asn1EncodableVector7.add(new DERSequence(asn1EncodableVector10));
                                            }
                                            asn1EncodableVector6.add(new SafeBag(PKCS12KeyStoreSpi.certBag, certBag.toASN1Primitive(), new DERSet(asn1EncodableVector7)));
                                            hashtable.put(engineGetCertificate2, engineGetCertificate2);
                                            break;
                                        }
                                        break Label_2085;
                                    }
                                    catch (CertificateEncodingException ex) {
                                        final StringBuilder sb = new StringBuilder();
                                        sb.append("Error encoding certificate: ");
                                        sb.append(ex.toString());
                                        throw new IOException(sb.toString());
                                    }
                                    break Label_1034;
                                }
                                continue Label_0845_Outer;
                            }
                        }
                        int n = 0;
                        continue;
                    }
                }
            }
            final Enumeration keys3 = this.certs.keys();
        Label_1436:
            while (keys3.hasMoreElements()) {
                String s3;
                Certificate certificate;
                CertBag certBag2;
                ASN1EncodableVector asn1EncodableVector11;
                PKCS12BagAttributeCarrier pkcs12BagAttributeCarrier3;
                DERBMPString derbmpString3;
                Enumeration bagAttributeKeys3;
                int n2;
                ASN1ObjectIdentifier asn1ObjectIdentifier3;
                ASN1EncodableVector asn1EncodableVector12;
                ASN1EncodableVector asn1EncodableVector13;
                StringBuilder sb2;
                Block_26_Outer:Label_1299_Outer:
                while (true) {
                    while (true) {
                        Label_2097: {
                            Label_2094: {
                                try {
                                    s3 = keys3.nextElement();
                                    certificate = (Certificate)this.certs.get(s3);
                                    if (this.keys.get(s3) != null) {
                                        break;
                                    }
                                    certBag2 = new CertBag(PKCS12KeyStoreSpi.x509Certificate, new DEROctetString(certificate.getEncoded()));
                                    asn1EncodableVector11 = new ASN1EncodableVector();
                                    if (certificate instanceof PKCS12BagAttributeCarrier) {
                                        pkcs12BagAttributeCarrier3 = (PKCS12BagAttributeCarrier)certificate;
                                        derbmpString3 = (DERBMPString)pkcs12BagAttributeCarrier3.getBagAttribute(PKCS12KeyStoreSpi.pkcs_9_at_friendlyName);
                                        if (derbmpString3 == null || !derbmpString3.getString().equals(s3)) {
                                            pkcs12BagAttributeCarrier3.setBagAttribute(PKCS12KeyStoreSpi.pkcs_9_at_friendlyName, new DERBMPString(s3));
                                        }
                                        bagAttributeKeys3 = pkcs12BagAttributeCarrier3.getBagAttributeKeys();
                                        n2 = 0;
                                        while (bagAttributeKeys3.hasMoreElements()) {
                                            asn1ObjectIdentifier3 = bagAttributeKeys3.nextElement();
                                            if (asn1ObjectIdentifier3.equals(PKCSObjectIdentifiers.pkcs_9_at_localKeyId)) {
                                                continue Block_26_Outer;
                                            }
                                            asn1EncodableVector12 = new ASN1EncodableVector();
                                            asn1EncodableVector12.add(asn1ObjectIdentifier3);
                                            asn1EncodableVector12.add(new DERSet(pkcs12BagAttributeCarrier3.getBagAttribute(asn1ObjectIdentifier3)));
                                            asn1EncodableVector11.add(new DERSequence(asn1EncodableVector12));
                                            n2 = 1;
                                        }
                                        break Label_2094;
                                    }
                                    break Label_2097;
                                    // iftrue(Label_1356:, n2 != 0)
                                Label_1356:
                                    while (true) {
                                        asn1EncodableVector13 = new ASN1EncodableVector();
                                        asn1EncodableVector13.add(PKCS12KeyStoreSpi.pkcs_9_at_friendlyName);
                                        asn1EncodableVector13.add(new DERSet(new DERBMPString(s3)));
                                        asn1EncodableVector11.add(new DERSequence(asn1EncodableVector13));
                                        break Label_1356;
                                        continue Label_1299_Outer;
                                    }
                                    asn1EncodableVector6.add(new SafeBag(PKCS12KeyStoreSpi.certBag, certBag2.toASN1Primitive(), new DERSet(asn1EncodableVector11)));
                                    hashtable.put(certificate, certificate);
                                    break;
                                }
                                catch (CertificateEncodingException ex2) {
                                    sb2 = new StringBuilder();
                                    sb2.append("Error encoding certificate: ");
                                    sb2.append(ex2.toString());
                                    throw new IOException(sb2.toString());
                                }
                                break Label_1436;
                            }
                            continue;
                        }
                        n2 = 0;
                        continue;
                    }
                }
            }
            final Set usedCertificateSet = this.getUsedCertificateSet();
            final Enumeration<CertId> keys4 = (Enumeration<CertId>)this.chainCerts.keys();
            Hashtable<Certificate, Certificate> hashtable2 = hashtable;
            while (keys4.hasMoreElements()) {
                try {
                    final Certificate certificate2 = this.chainCerts.get(keys4.nextElement());
                    if (!usedCertificateSet.contains(certificate2)) {
                        continue;
                    }
                    if (hashtable2.get(certificate2) != null) {
                        continue;
                    }
                    final CertBag certBag3 = new CertBag(PKCS12KeyStoreSpi.x509Certificate, new DEROctetString(certificate2.getEncoded()));
                    final ASN1EncodableVector asn1EncodableVector14 = new ASN1EncodableVector();
                    Hashtable<Certificate, Certificate> hashtable3 = hashtable2;
                    if (certificate2 instanceof PKCS12BagAttributeCarrier) {
                        final PKCS12BagAttributeCarrier pkcs12BagAttributeCarrier4 = (PKCS12BagAttributeCarrier)certificate2;
                        final Enumeration bagAttributeKeys4 = pkcs12BagAttributeCarrier4.getBagAttributeKeys();
                        while (true) {
                            hashtable3 = hashtable2;
                            if (!bagAttributeKeys4.hasMoreElements()) {
                                break;
                            }
                            final ASN1ObjectIdentifier asn1ObjectIdentifier4 = bagAttributeKeys4.nextElement();
                            if (asn1ObjectIdentifier4.equals(PKCSObjectIdentifiers.pkcs_9_at_localKeyId)) {
                                continue;
                            }
                            final ASN1EncodableVector asn1EncodableVector15 = new ASN1EncodableVector();
                            asn1EncodableVector15.add(asn1ObjectIdentifier4);
                            asn1EncodableVector15.add(new DERSet(pkcs12BagAttributeCarrier4.getBagAttribute(asn1ObjectIdentifier4)));
                            asn1EncodableVector14.add(new DERSequence(asn1EncodableVector15));
                        }
                    }
                    asn1EncodableVector6.add(new SafeBag(PKCS12KeyStoreSpi.certBag, certBag3.toASN1Primitive(), new DERSet(asn1EncodableVector14)));
                    hashtable2 = hashtable3;
                    continue;
                }
                catch (CertificateEncodingException ex3) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("Error encoding certificate: ");
                    sb3.append(ex3.toString());
                    throw new IOException(sb3.toString());
                }
                break;
            }
            final AuthenticatedSafe authenticatedSafe = new AuthenticatedSafe(new ContentInfo[] { new ContentInfo(PKCS12KeyStoreSpi.data, berOctetString), new ContentInfo(PKCS12KeyStoreSpi.encryptedData, new EncryptedData(PKCS12KeyStoreSpi.data, algorithmIdentifier, new BEROctetString(this.cryptData(true, algorithmIdentifier, array, false, new DERSequence(asn1EncodableVector6).getEncoded("DER")))).toASN1Primitive()) });
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DEROutputStream derOutputStream;
            if (b) {
                derOutputStream = new DEROutputStream(byteArrayOutputStream);
            }
            else {
                derOutputStream = new BEROutputStream(byteArrayOutputStream);
            }
            derOutputStream.writeObject(authenticatedSafe);
            final ContentInfo contentInfo = new ContentInfo(PKCS12KeyStoreSpi.data, new BEROctetString(byteArrayOutputStream.toByteArray()));
            final byte[] array4 = new byte[this.saltLength];
            this.random.nextBytes(array4);
            final byte[] octets = ((ASN1OctetString)contentInfo.getContent()).getOctets();
            try {
                final Pfx pfx = new Pfx(contentInfo, new MacData(new DigestInfo(this.macAlgorithm, this.calculatePbeMac(this.macAlgorithm.getAlgorithm(), array4, this.itCount, array, false, octets)), array4, this.itCount));
                DEROutputStream derOutputStream2;
                if (b) {
                    derOutputStream2 = new DEROutputStream(outputStream);
                }
                else {
                    derOutputStream2 = new BEROutputStream(outputStream);
                }
                derOutputStream2.writeObject(pfx);
                return;
            }
            catch (Exception ex4) {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("error constructing MAC: ");
                sb4.append(ex4.toString());
                throw new IOException(sb4.toString());
            }
        }
        throw new NullPointerException("No password supplied for PKCS#12 KeyStore.");
    }
    
    private static byte[] getDigest(final SubjectPublicKeyInfo subjectPublicKeyInfo) {
        final Digest sha1 = DigestFactory.createSHA1();
        final byte[] array = new byte[sha1.getDigestSize()];
        final byte[] bytes = subjectPublicKeyInfo.getPublicKeyData().getBytes();
        sha1.update(bytes, 0, bytes.length);
        sha1.doFinal(array, 0);
        return array;
    }
    
    private Set getUsedCertificateSet() {
        final HashSet<Certificate> set = new HashSet<Certificate>();
        final Enumeration keys = this.keys.keys();
        while (keys.hasMoreElements()) {
            final Certificate[] engineGetCertificateChain = this.engineGetCertificateChain(keys.nextElement());
            for (int i = 0; i != engineGetCertificateChain.length; ++i) {
                set.add(engineGetCertificateChain[i]);
            }
        }
        final Enumeration keys2 = this.certs.keys();
        while (keys2.hasMoreElements()) {
            set.add(this.engineGetCertificate(keys2.nextElement()));
        }
        return set;
    }
    
    private int validateIterationCount(BigInteger bigInteger) {
        final int intValue = bigInteger.intValue();
        if (intValue < 0) {
            throw new IllegalStateException("negative iteration count found");
        }
        bigInteger = Properties.asBigInteger("org.spongycastle.pkcs12.max_it_count");
        if (bigInteger == null) {
            return intValue;
        }
        if (bigInteger.intValue() >= intValue) {
            return intValue;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("iteration count ");
        sb.append(intValue);
        sb.append(" greater than ");
        sb.append(bigInteger.intValue());
        throw new IllegalStateException(sb.toString());
    }
    
    protected byte[] cryptData(final boolean b, final AlgorithmIdentifier algorithmIdentifier, final char[] array, final boolean b2, final byte[] array2) throws IOException {
        final ASN1ObjectIdentifier algorithm = algorithmIdentifier.getAlgorithm();
        int n;
        if (b) {
            n = 1;
        }
        else {
            n = 2;
        }
        if (algorithm.on(PKCSObjectIdentifiers.pkcs_12PbeIds)) {
            final PKCS12PBEParams instance = PKCS12PBEParams.getInstance(algorithmIdentifier.getParameters());
            try {
                final PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(instance.getIV(), instance.getIterations().intValue());
                final PKCS12Key pkcs12Key = new PKCS12Key(array, b2);
                final Cipher cipher = this.helper.createCipher(algorithm.getId());
                cipher.init(n, pkcs12Key, pbeParameterSpec);
                return cipher.doFinal(array2);
            }
            catch (Exception ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("exception decrypting data - ");
                sb.append(ex.toString());
                throw new IOException(sb.toString());
            }
        }
        if (algorithm.equals(PKCSObjectIdentifiers.id_PBES2)) {
            try {
                return this.createCipher(n, array, algorithmIdentifier).doFinal(array2);
            }
            catch (Exception ex2) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("exception decrypting data - ");
                sb2.append(ex2.toString());
                throw new IOException(sb2.toString());
            }
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("unknown PBE algorithm: ");
        sb3.append(algorithm);
        throw new IOException(sb3.toString());
    }
    
    @Override
    public Enumeration engineAliases() {
        final Hashtable<String, String> hashtable = new Hashtable<String, String>();
        final Enumeration keys = this.certs.keys();
        while (keys.hasMoreElements()) {
            hashtable.put(keys.nextElement(), "cert");
        }
        final Enumeration keys2 = this.keys.keys();
        while (keys2.hasMoreElements()) {
            final String s = keys2.nextElement();
            if (hashtable.get(s) == null) {
                hashtable.put(s, "key");
            }
        }
        return hashtable.keys();
    }
    
    @Override
    public boolean engineContainsAlias(final String s) {
        return this.certs.get(s) != null || this.keys.get(s) != null;
    }
    
    @Override
    public void engineDeleteEntry(final String s) throws KeyStoreException {
        final Key key = (Key)this.keys.remove(s);
        final Certificate certificate = (Certificate)this.certs.remove(s);
        if (certificate != null) {
            this.chainCerts.remove(new CertId(certificate.getPublicKey()));
        }
        if (key != null) {
            final String s2 = this.localIds.remove(s);
            Certificate certificate2 = certificate;
            if (s2 != null) {
                certificate2 = this.keyCerts.remove(s2);
            }
            if (certificate2 != null) {
                this.chainCerts.remove(new CertId(certificate2.getPublicKey()));
            }
        }
    }
    
    @Override
    public Certificate engineGetCertificate(final String s) {
        if (s != null) {
            Certificate certificate;
            if ((certificate = (Certificate)this.certs.get(s)) == null) {
                final String s2 = this.localIds.get(s);
                if (s2 != null) {
                    return (Certificate)this.keyCerts.get(s2);
                }
                certificate = this.keyCerts.get(s);
            }
            return certificate;
        }
        throw new IllegalArgumentException("null alias passed to getCertificate.");
    }
    
    @Override
    public String engineGetCertificateAlias(final Certificate certificate) {
        final Enumeration elements = this.certs.elements();
        final Enumeration keys = this.certs.keys();
        while (elements.hasMoreElements()) {
            final Certificate certificate2 = elements.nextElement();
            final String s = keys.nextElement();
            if (certificate2.equals(certificate)) {
                return s;
            }
        }
        final Enumeration<Certificate> elements2 = (Enumeration<Certificate>)this.keyCerts.elements();
        final Enumeration<String> keys2 = this.keyCerts.keys();
        while (elements2.hasMoreElements()) {
            final Certificate certificate3 = elements2.nextElement();
            final String s2 = keys2.nextElement();
            if (certificate3.equals(certificate)) {
                return s2;
            }
        }
        return null;
    }
    
    @Override
    public Certificate[] engineGetCertificateChain(String s) {
        if (s == null) {
            throw new IllegalArgumentException("null alias passed to getCertificateChain.");
        }
        final boolean engineIsKeyEntry = this.engineIsKeyEntry((String)s);
        final Certificate[] array = null;
        if (!engineIsKeyEntry) {
            return null;
        }
        final Certificate engineGetCertificate = this.engineGetCertificate((String)s);
        Certificate[] array2 = array;
        if (engineGetCertificate != null) {
            final Vector<Certificate> vector = new Vector<Certificate>();
            s = engineGetCertificate;
            while (s != null) {
                final X509Certificate x509Certificate = (X509Certificate)s;
                final byte[] extensionValue = x509Certificate.getExtensionValue(Extension.authorityKeyIdentifier.getId());
                Certificate certificate = null;
                Label_0161: {
                    if (extensionValue != null) {
                        try {
                            final AuthorityKeyIdentifier instance = AuthorityKeyIdentifier.getInstance(new ASN1InputStream(((ASN1OctetString)new ASN1InputStream(extensionValue).readObject()).getOctets()).readObject());
                            if (instance.getKeyIdentifier() != null) {
                                certificate = this.chainCerts.get(new CertId(instance.getKeyIdentifier()));
                                break Label_0161;
                            }
                        }
                        catch (IOException ex) {
                            throw new RuntimeException(ex.toString());
                        }
                    }
                    certificate = null;
                }
                X509Certificate x509Certificate2 = (X509Certificate)certificate;
                if (certificate == null) {
                    final Principal issuerDN = x509Certificate.getIssuerDN();
                    x509Certificate2 = (X509Certificate)certificate;
                    if (!issuerDN.equals(x509Certificate.getSubjectDN())) {
                        final Enumeration keys = this.chainCerts.keys();
                        while (true) {
                            x509Certificate2 = (X509Certificate)certificate;
                            if (!keys.hasMoreElements()) {
                                break;
                            }
                            x509Certificate2 = (X509Certificate)this.chainCerts.get(keys.nextElement());
                            if (x509Certificate2.getSubjectDN().equals(issuerDN)) {
                                try {
                                    x509Certificate.verify(x509Certificate2.getPublicKey());
                                }
                                catch (Exception ex2) {
                                    continue;
                                }
                                break;
                            }
                        }
                    }
                }
                if (!vector.contains(s)) {
                    vector.addElement((Certificate)s);
                    if (x509Certificate2 != s) {
                        s = x509Certificate2;
                        continue;
                    }
                }
                s = null;
            }
            final int size = vector.size();
            final Certificate[] array3 = new Certificate[size];
            int n = 0;
            while (true) {
                array2 = array3;
                if (n == size) {
                    break;
                }
                array3[n] = vector.elementAt(n);
                ++n;
            }
        }
        return array2;
    }
    
    @Override
    public Date engineGetCreationDate(final String s) {
        if (s == null) {
            throw new NullPointerException("alias == null");
        }
        if (this.keys.get(s) == null && this.certs.get(s) == null) {
            return null;
        }
        return new Date();
    }
    
    @Override
    public Key engineGetKey(final String s, final char[] array) throws NoSuchAlgorithmException, UnrecoverableKeyException {
        if (s != null) {
            return (Key)this.keys.get(s);
        }
        throw new IllegalArgumentException("null alias passed to getKey.");
    }
    
    @Override
    public boolean engineIsCertificateEntry(final String s) {
        return this.certs.get(s) != null && this.keys.get(s) == null;
    }
    
    @Override
    public boolean engineIsKeyEntry(final String s) {
        return this.keys.get(s) != null;
    }
    
    @Override
    public void engineLoad(final InputStream inputStream, final char[] array) throws IOException {
        if (inputStream == null) {
            return;
        }
        if (array == null) {
            throw new NullPointerException("No password supplied for PKCS#12 KeyStore.");
        }
        final BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        bufferedInputStream.mark(10);
        if (bufferedInputStream.read() == 48) {
            bufferedInputStream.reset();
            final Pfx instance = Pfx.getInstance(new ASN1InputStream(bufferedInputStream).readObject());
            final ContentInfo authSafe = instance.getAuthSafe();
            final Vector<SafeBag> vector = new Vector<SafeBag>();
            boolean b = false;
            Label_0287: {
                if (instance.getMacData() != null) {
                    final MacData macData = instance.getMacData();
                    final DigestInfo mac = macData.getMac();
                    this.macAlgorithm = mac.getAlgorithmId();
                    final byte[] salt = macData.getSalt();
                    this.itCount = this.validateIterationCount(macData.getIterationCount());
                    this.saltLength = salt.length;
                    final byte[] octets = ((ASN1OctetString)authSafe.getContent()).getOctets();
                    try {
                        final byte[] calculatePbeMac = this.calculatePbeMac(this.macAlgorithm.getAlgorithm(), salt, this.itCount, array, false, octets);
                        final byte[] digest = mac.getDigest();
                        if (!Arrays.constantTimeAreEqual(calculatePbeMac, digest)) {
                            if (array.length > 0) {
                                throw new IOException("PKCS12 key store mac invalid - wrong password or corrupted file.");
                            }
                            if (Arrays.constantTimeAreEqual(this.calculatePbeMac(this.macAlgorithm.getAlgorithm(), salt, this.itCount, array, true, octets), digest)) {
                                b = true;
                                break Label_0287;
                            }
                            throw new IOException("PKCS12 key store mac invalid - wrong password or corrupted file.");
                        }
                    }
                    catch (Exception ex) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("error constructing MAC: ");
                        sb.append(ex.toString());
                        throw new IOException(sb.toString());
                    }
                    catch (IOException ex2) {
                        throw ex2;
                    }
                }
                b = false;
            }
            this.keys = new IgnoresCaseHashtable();
            this.localIds = new Hashtable();
            int n3;
            if (authSafe.getContentType().equals(PKCS12KeyStoreSpi.data)) {
                final ContentInfo[] contentInfo = AuthenticatedSafe.getInstance(new ASN1InputStream(((ASN1OctetString)authSafe.getContent()).getOctets()).readObject()).getContentInfo();
                int n = 0;
                int n2 = 0;
                while (true) {
                    n3 = n2;
                    if (n == contentInfo.length) {
                        break;
                    }
                    int n4;
                    if (contentInfo[n].getContentType().equals(PKCS12KeyStoreSpi.data)) {
                        final ASN1Sequence asn1Sequence = (ASN1Sequence)new ASN1InputStream(((ASN1OctetString)contentInfo[n].getContent()).getOctets()).readObject();
                        for (int i = 0; i != asn1Sequence.size(); ++i) {
                            final SafeBag instance2 = SafeBag.getInstance(asn1Sequence.getObjectAt(i));
                            if (instance2.getBagId().equals(PKCS12KeyStoreSpi.pkcs8ShroudedKeyBag)) {
                                final EncryptedPrivateKeyInfo instance3 = EncryptedPrivateKeyInfo.getInstance(instance2.getBagValue());
                                final PrivateKey unwrapKey = this.unwrapKey(instance3.getEncryptionAlgorithm(), instance3.getEncryptedData(), array, b);
                                final PKCS12BagAttributeCarrier pkcs12BagAttributeCarrier = (PKCS12BagAttributeCarrier)unwrapKey;
                                String s;
                                Object o;
                                if (instance2.getBagAttributes() != null) {
                                    final Enumeration objects = instance2.getBagAttributes().getObjects();
                                    o = (s = null);
                                    while (objects.hasMoreElements()) {
                                        final ASN1Sequence asn1Sequence2 = objects.nextElement();
                                        final ASN1ObjectIdentifier asn1ObjectIdentifier = (ASN1ObjectIdentifier)asn1Sequence2.getObjectAt(0);
                                        final ASN1Set set = (ASN1Set)asn1Sequence2.getObjectAt(1);
                                        ASN1Primitive asn1Primitive;
                                        if (set.size() > 0) {
                                            asn1Primitive = (ASN1Primitive)set.getObjectAt(0);
                                            final ASN1Encodable bagAttribute = pkcs12BagAttributeCarrier.getBagAttribute(asn1ObjectIdentifier);
                                            if (bagAttribute != null) {
                                                if (!bagAttribute.toASN1Primitive().equals(asn1Primitive)) {
                                                    throw new IOException("attempt to add existing attribute with different value");
                                                }
                                            }
                                            else {
                                                pkcs12BagAttributeCarrier.setBagAttribute(asn1ObjectIdentifier, asn1Primitive);
                                            }
                                        }
                                        else {
                                            asn1Primitive = null;
                                        }
                                        String string;
                                        if (asn1ObjectIdentifier.equals(PKCS12KeyStoreSpi.pkcs_9_at_friendlyName)) {
                                            string = ((DERBMPString)asn1Primitive).getString();
                                            this.keys.put(string, unwrapKey);
                                        }
                                        else {
                                            string = s;
                                            if (asn1ObjectIdentifier.equals(PKCS12KeyStoreSpi.pkcs_9_at_localKeyId)) {
                                                o = asn1Primitive;
                                                string = s;
                                            }
                                        }
                                        s = string;
                                    }
                                }
                                else {
                                    o = null;
                                    s = null;
                                }
                                if (o != null) {
                                    final String s2 = new String(Hex.encode(((ASN1OctetString)o).getOctets()));
                                    if (s == null) {
                                        this.keys.put(s2, unwrapKey);
                                    }
                                    else {
                                        this.localIds.put(s, s2);
                                    }
                                }
                                else {
                                    this.keys.put("unmarked", unwrapKey);
                                    n2 = 1;
                                }
                            }
                            else if (instance2.getBagId().equals(PKCS12KeyStoreSpi.certBag)) {
                                vector.addElement(instance2);
                            }
                            else {
                                final PrintStream out = System.out;
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append("extra in data ");
                                sb2.append(instance2.getBagId());
                                out.println(sb2.toString());
                                System.out.println(ASN1Dump.dumpAsString(instance2));
                            }
                        }
                        n4 = n2;
                    }
                    else if (contentInfo[n].getContentType().equals(PKCS12KeyStoreSpi.encryptedData)) {
                        final EncryptedData instance4 = EncryptedData.getInstance(contentInfo[n].getContent());
                        final ASN1Sequence asn1Sequence3 = (ASN1Sequence)ASN1Primitive.fromByteArray(this.cryptData(false, instance4.getEncryptionAlgorithm(), array, b, instance4.getContent().getOctets()));
                        int n5 = 0;
                        while (true) {
                            n4 = n2;
                            if (n5 == asn1Sequence3.size()) {
                                break;
                            }
                            final SafeBag instance5 = SafeBag.getInstance(asn1Sequence3.getObjectAt(n5));
                            if (instance5.getBagId().equals(PKCS12KeyStoreSpi.certBag)) {
                                vector.addElement(instance5);
                            }
                            else if (instance5.getBagId().equals(PKCS12KeyStoreSpi.pkcs8ShroudedKeyBag)) {
                                final EncryptedPrivateKeyInfo instance6 = EncryptedPrivateKeyInfo.getInstance(instance5.getBagValue());
                                final PrivateKey unwrapKey2 = this.unwrapKey(instance6.getEncryptionAlgorithm(), instance6.getEncryptedData(), array, b);
                                final PKCS12BagAttributeCarrier pkcs12BagAttributeCarrier2 = (PKCS12BagAttributeCarrier)unwrapKey2;
                                final Enumeration objects2 = instance5.getBagAttributes().getObjects();
                                ASN1OctetString asn1OctetString = null;
                                String s3 = null;
                                while (objects2.hasMoreElements()) {
                                    final ASN1Sequence asn1Sequence4 = objects2.nextElement();
                                    final ASN1ObjectIdentifier asn1ObjectIdentifier2 = (ASN1ObjectIdentifier)asn1Sequence4.getObjectAt(0);
                                    final ASN1Set set2 = (ASN1Set)asn1Sequence4.getObjectAt(1);
                                    ASN1Primitive asn1Primitive2;
                                    if (set2.size() > 0) {
                                        asn1Primitive2 = (ASN1Primitive)set2.getObjectAt(0);
                                        final ASN1Encodable bagAttribute2 = pkcs12BagAttributeCarrier2.getBagAttribute(asn1ObjectIdentifier2);
                                        if (bagAttribute2 != null) {
                                            if (!bagAttribute2.toASN1Primitive().equals(asn1Primitive2)) {
                                                throw new IOException("attempt to add existing attribute with different value");
                                            }
                                        }
                                        else {
                                            pkcs12BagAttributeCarrier2.setBagAttribute(asn1ObjectIdentifier2, asn1Primitive2);
                                        }
                                    }
                                    else {
                                        asn1Primitive2 = null;
                                    }
                                    String string2;
                                    if (asn1ObjectIdentifier2.equals(PKCS12KeyStoreSpi.pkcs_9_at_friendlyName)) {
                                        string2 = ((DERBMPString)asn1Primitive2).getString();
                                        this.keys.put(string2, unwrapKey2);
                                    }
                                    else {
                                        string2 = s3;
                                        if (asn1ObjectIdentifier2.equals(PKCS12KeyStoreSpi.pkcs_9_at_localKeyId)) {
                                            asn1OctetString = (ASN1OctetString)asn1Primitive2;
                                            string2 = s3;
                                        }
                                    }
                                    s3 = string2;
                                }
                                final String s4 = new String(Hex.encode(asn1OctetString.getOctets()));
                                if (s3 == null) {
                                    this.keys.put(s4, unwrapKey2);
                                }
                                else {
                                    this.localIds.put(s3, s4);
                                }
                            }
                            else if (instance5.getBagId().equals(PKCS12KeyStoreSpi.keyBag)) {
                                final PrivateKey privateKey = BouncyCastleProvider.getPrivateKey(PrivateKeyInfo.getInstance(instance5.getBagValue()));
                                final PKCS12BagAttributeCarrier pkcs12BagAttributeCarrier3 = (PKCS12BagAttributeCarrier)privateKey;
                                final Enumeration objects3 = instance5.getBagAttributes().getObjects();
                                String s5 = null;
                                ASN1OctetString asn1OctetString2 = null;
                                while (objects3.hasMoreElements()) {
                                    final ASN1Sequence instance7 = ASN1Sequence.getInstance(objects3.nextElement());
                                    final ASN1ObjectIdentifier instance8 = ASN1ObjectIdentifier.getInstance(instance7.getObjectAt(0));
                                    final ASN1Set instance9 = ASN1Set.getInstance(instance7.getObjectAt(1));
                                    String string3 = s5;
                                    ASN1OctetString asn1OctetString3 = asn1OctetString2;
                                    if (instance9.size() > 0) {
                                        final ASN1Primitive asn1Primitive3 = (ASN1Primitive)instance9.getObjectAt(0);
                                        final ASN1Encodable bagAttribute3 = pkcs12BagAttributeCarrier3.getBagAttribute(instance8);
                                        if (bagAttribute3 != null) {
                                            if (!bagAttribute3.toASN1Primitive().equals(asn1Primitive3)) {
                                                throw new IOException("attempt to add existing attribute with different value");
                                            }
                                        }
                                        else {
                                            pkcs12BagAttributeCarrier3.setBagAttribute(instance8, asn1Primitive3);
                                        }
                                        if (instance8.equals(PKCS12KeyStoreSpi.pkcs_9_at_friendlyName)) {
                                            string3 = ((DERBMPString)asn1Primitive3).getString();
                                            this.keys.put(string3, privateKey);
                                            asn1OctetString3 = asn1OctetString2;
                                        }
                                        else {
                                            string3 = s5;
                                            asn1OctetString3 = asn1OctetString2;
                                            if (instance8.equals(PKCS12KeyStoreSpi.pkcs_9_at_localKeyId)) {
                                                asn1OctetString3 = (ASN1OctetString)asn1Primitive3;
                                                string3 = s5;
                                            }
                                        }
                                    }
                                    s5 = string3;
                                    asn1OctetString2 = asn1OctetString3;
                                }
                                final String s6 = new String(Hex.encode(asn1OctetString2.getOctets()));
                                if (s5 == null) {
                                    this.keys.put(s6, privateKey);
                                }
                                else {
                                    this.localIds.put(s5, s6);
                                }
                            }
                            else {
                                final PrintStream out2 = System.out;
                                final StringBuilder sb3 = new StringBuilder();
                                sb3.append("extra in encryptedData ");
                                sb3.append(instance5.getBagId());
                                out2.println(sb3.toString());
                                System.out.println(ASN1Dump.dumpAsString(instance5));
                            }
                            ++n5;
                        }
                    }
                    else {
                        final int n6 = n;
                        final PrintStream out3 = System.out;
                        final StringBuilder sb4 = new StringBuilder();
                        sb4.append("extra ");
                        sb4.append(contentInfo[n6].getContentType().getId());
                        out3.println(sb4.toString());
                        final PrintStream out4 = System.out;
                        final StringBuilder sb5 = new StringBuilder();
                        sb5.append("extra ");
                        sb5.append(ASN1Dump.dumpAsString(contentInfo[n6].getContent()));
                        out4.println(sb5.toString());
                        n4 = n2;
                    }
                    ++n;
                    n2 = n4;
                }
            }
            else {
                n3 = 0;
            }
            this.certs = new IgnoresCaseHashtable();
            this.chainCerts = new Hashtable();
            this.keyCerts = new Hashtable();
            int j = 0;
            while (j != vector.size()) {
                final SafeBag safeBag = vector.elementAt(j);
                final CertBag instance10 = CertBag.getInstance(safeBag.getBagValue());
                if (instance10.getCertId().equals(PKCS12KeyStoreSpi.x509Certificate)) {
                    try {
                        final Certificate generateCertificate = this.certFact.generateCertificate(new ByteArrayInputStream(((ASN1OctetString)instance10.getCertValue()).getOctets()));
                        ASN1OctetString asn1OctetString4;
                        Object string4;
                        if (safeBag.getBagAttributes() != null) {
                            final Enumeration objects4 = safeBag.getBagAttributes().getObjects();
                            string4 = (asn1OctetString4 = null);
                            while (objects4.hasMoreElements()) {
                                final ASN1Sequence instance11 = ASN1Sequence.getInstance(objects4.nextElement());
                                final ASN1ObjectIdentifier instance12 = ASN1ObjectIdentifier.getInstance(instance11.getObjectAt(0));
                                final ASN1Set instance13 = ASN1Set.getInstance(instance11.getObjectAt(1));
                                if (instance13.size() > 0) {
                                    final ASN1Primitive asn1Primitive4 = (ASN1Primitive)instance13.getObjectAt(0);
                                    if (generateCertificate instanceof PKCS12BagAttributeCarrier) {
                                        final PKCS12BagAttributeCarrier pkcs12BagAttributeCarrier4 = (PKCS12BagAttributeCarrier)generateCertificate;
                                        final ASN1Encodable bagAttribute4 = pkcs12BagAttributeCarrier4.getBagAttribute(instance12);
                                        if (bagAttribute4 != null) {
                                            if (!bagAttribute4.toASN1Primitive().equals(asn1Primitive4)) {
                                                throw new IOException("attempt to add existing attribute with different value");
                                            }
                                        }
                                        else {
                                            pkcs12BagAttributeCarrier4.setBagAttribute(instance12, asn1Primitive4);
                                        }
                                    }
                                    if (instance12.equals(PKCS12KeyStoreSpi.pkcs_9_at_friendlyName)) {
                                        string4 = ((DERBMPString)asn1Primitive4).getString();
                                    }
                                    else {
                                        if (!instance12.equals(PKCS12KeyStoreSpi.pkcs_9_at_localKeyId)) {
                                            continue;
                                        }
                                        asn1OctetString4 = (ASN1OctetString)asn1Primitive4;
                                    }
                                }
                            }
                        }
                        else {
                            string4 = (asn1OctetString4 = null);
                        }
                        this.chainCerts.put(new CertId(generateCertificate.getPublicKey()), generateCertificate);
                        if (n3 != 0) {
                            if (this.keyCerts.isEmpty()) {
                                final String s7 = new String(Hex.encode(this.createSubjectKeyId(generateCertificate.getPublicKey()).getKeyIdentifier()));
                                this.keyCerts.put(s7, generateCertificate);
                                final IgnoresCaseHashtable keys = this.keys;
                                keys.put(s7, keys.remove("unmarked"));
                            }
                        }
                        else {
                            if (asn1OctetString4 != null) {
                                this.keyCerts.put(new String(Hex.encode(asn1OctetString4.getOctets())), generateCertificate);
                            }
                            if (string4 != null) {
                                this.certs.put((String)string4, generateCertificate);
                            }
                        }
                        ++j;
                        continue;
                    }
                    catch (Exception ex3) {
                        throw new RuntimeException(ex3.toString());
                    }
                }
                final StringBuilder sb6 = new StringBuilder();
                sb6.append("Unsupported certificate type: ");
                sb6.append(instance10.getCertId());
                throw new RuntimeException(sb6.toString());
            }
            return;
        }
        throw new IOException("stream does not represent a PKCS12 key store");
    }
    
    @Override
    public void engineSetCertificateEntry(final String s, final Certificate certificate) throws KeyStoreException {
        if (this.keys.get(s) == null) {
            this.certs.put(s, certificate);
            this.chainCerts.put(new CertId(certificate.getPublicKey()), certificate);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("There is a key entry with the name ");
        sb.append(s);
        sb.append(".");
        throw new KeyStoreException(sb.toString());
    }
    
    @Override
    public void engineSetKeyEntry(final String s, final Key key, final char[] array, final Certificate[] array2) throws KeyStoreException {
        final boolean b = key instanceof PrivateKey;
        if (!b) {
            throw new KeyStoreException("PKCS12 does not support non-PrivateKeys");
        }
        if (b && array2 == null) {
            throw new KeyStoreException("no certificate chain for private key");
        }
        if (this.keys.get(s) != null) {
            this.engineDeleteEntry(s);
        }
        this.keys.put(s, key);
        if (array2 != null) {
            final IgnoresCaseHashtable certs = this.certs;
            int i = 0;
            certs.put(s, array2[0]);
            while (i != array2.length) {
                this.chainCerts.put(new CertId(array2[i].getPublicKey()), array2[i]);
                ++i;
            }
        }
    }
    
    @Override
    public void engineSetKeyEntry(final String s, final byte[] array, final Certificate[] array2) throws KeyStoreException {
        throw new RuntimeException("operation not supported");
    }
    
    @Override
    public int engineSize() {
        final Hashtable<String, String> hashtable = new Hashtable<String, String>();
        final Enumeration keys = this.certs.keys();
        while (keys.hasMoreElements()) {
            hashtable.put(keys.nextElement(), "cert");
        }
        final Enumeration keys2 = this.keys.keys();
        while (keys2.hasMoreElements()) {
            final String s = keys2.nextElement();
            if (hashtable.get(s) == null) {
                hashtable.put(s, "key");
            }
        }
        return hashtable.size();
    }
    
    @Override
    public void engineStore(final OutputStream outputStream, final char[] array) throws IOException {
        this.doStore(outputStream, array, false);
    }
    
    @Override
    public void engineStore(final KeyStore.LoadStoreParameter loadStoreParameter) throws IOException, NoSuchAlgorithmException, CertificateException {
        if (loadStoreParameter == null) {
            throw new IllegalArgumentException("'param' arg cannot be null");
        }
        final boolean b = loadStoreParameter instanceof PKCS12StoreParameter;
        if (!b && !(loadStoreParameter instanceof JDKPKCS12StoreParameter)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("No support for 'param' of type ");
            sb.append(loadStoreParameter.getClass().getName());
            throw new IllegalArgumentException(sb.toString());
        }
        PKCS12StoreParameter pkcs12StoreParameter;
        if (b) {
            pkcs12StoreParameter = (PKCS12StoreParameter)loadStoreParameter;
        }
        else {
            final JDKPKCS12StoreParameter jdkpkcs12StoreParameter = (JDKPKCS12StoreParameter)loadStoreParameter;
            pkcs12StoreParameter = new PKCS12StoreParameter(jdkpkcs12StoreParameter.getOutputStream(), loadStoreParameter.getProtectionParameter(), jdkpkcs12StoreParameter.isUseDEREncoding());
        }
        final KeyStore.ProtectionParameter protectionParameter = loadStoreParameter.getProtectionParameter();
        char[] password;
        if (protectionParameter == null) {
            password = null;
        }
        else {
            if (!(protectionParameter instanceof KeyStore.PasswordProtection)) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("No support for protection parameter of type ");
                sb2.append(((KeyStore.PasswordProtection)protectionParameter).getClass().getName());
                throw new IllegalArgumentException(sb2.toString());
            }
            password = ((KeyStore.PasswordProtection)protectionParameter).getPassword();
        }
        this.doStore(pkcs12StoreParameter.getOutputStream(), password, pkcs12StoreParameter.isForDEREncoding());
    }
    
    @Override
    public void setRandom(final SecureRandom random) {
        this.random = random;
    }
    
    protected PrivateKey unwrapKey(final AlgorithmIdentifier algorithmIdentifier, final byte[] array, final char[] array2, final boolean b) throws IOException {
        final ASN1ObjectIdentifier algorithm = algorithmIdentifier.getAlgorithm();
        try {
            if (algorithm.on(PKCSObjectIdentifiers.pkcs_12PbeIds)) {
                final PKCS12PBEParams instance = PKCS12PBEParams.getInstance(algorithmIdentifier.getParameters());
                final PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(instance.getIV(), this.validateIterationCount(instance.getIterations()));
                final Cipher cipher = this.helper.createCipher(algorithm.getId());
                cipher.init(4, new PKCS12Key(array2, b), pbeParameterSpec);
                return (PrivateKey)cipher.unwrap(array, "", 2);
            }
            if (algorithm.equals(PKCSObjectIdentifiers.id_PBES2)) {
                return (PrivateKey)this.createCipher(4, array2, algorithmIdentifier).unwrap(array, "", 2);
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("exception unwrapping private key - cannot recognise: ");
            sb.append(algorithm);
            throw new IOException(sb.toString());
        }
        catch (Exception ex) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("exception unwrapping private key - ");
            sb2.append(ex.toString());
            throw new IOException(sb2.toString());
        }
    }
    
    protected byte[] wrapKey(final String s, final Key key, final PKCS12PBEParams pkcs12PBEParams, final char[] array) throws IOException {
        final PBEKeySpec pbeKeySpec = new PBEKeySpec(array);
        try {
            final SecretKeyFactory secretKeyFactory = this.helper.createSecretKeyFactory(s);
            final PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(pkcs12PBEParams.getIV(), pkcs12PBEParams.getIterations().intValue());
            final Cipher cipher = this.helper.createCipher(s);
            cipher.init(3, secretKeyFactory.generateSecret(pbeKeySpec), pbeParameterSpec);
            return cipher.wrap(key);
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("exception encrypting data - ");
            sb.append(ex.toString());
            throw new IOException(sb.toString());
        }
    }
    
    public static class BCPKCS12KeyStore extends PKCS12KeyStoreSpi
    {
        public BCPKCS12KeyStore() {
            super(new BouncyCastleProvider(), BCPKCS12KeyStore.pbeWithSHAAnd3_KeyTripleDES_CBC, BCPKCS12KeyStore.pbeWithSHAAnd40BitRC2_CBC);
        }
    }
    
    public static class BCPKCS12KeyStore3DES extends PKCS12KeyStoreSpi
    {
        public BCPKCS12KeyStore3DES() {
            super(new BouncyCastleProvider(), BCPKCS12KeyStore3DES.pbeWithSHAAnd3_KeyTripleDES_CBC, BCPKCS12KeyStore3DES.pbeWithSHAAnd3_KeyTripleDES_CBC);
        }
    }
    
    private class CertId
    {
        byte[] id;
        
        CertId(final PublicKey publicKey) {
            this.id = PKCS12KeyStoreSpi.this.createSubjectKeyId(publicKey).getKeyIdentifier();
        }
        
        CertId(final byte[] id) {
            this.id = id;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o == this || (o instanceof CertId && Arrays.areEqual(this.id, ((CertId)o).id));
        }
        
        @Override
        public int hashCode() {
            return Arrays.hashCode(this.id);
        }
    }
    
    public static class DefPKCS12KeyStore extends PKCS12KeyStoreSpi
    {
        public DefPKCS12KeyStore() {
            super(null, DefPKCS12KeyStore.pbeWithSHAAnd3_KeyTripleDES_CBC, DefPKCS12KeyStore.pbeWithSHAAnd40BitRC2_CBC);
        }
    }
    
    public static class DefPKCS12KeyStore3DES extends PKCS12KeyStoreSpi
    {
        public DefPKCS12KeyStore3DES() {
            super(null, DefPKCS12KeyStore3DES.pbeWithSHAAnd3_KeyTripleDES_CBC, DefPKCS12KeyStore3DES.pbeWithSHAAnd3_KeyTripleDES_CBC);
        }
    }
    
    private static class DefaultSecretKeyProvider
    {
        private final Map KEY_SIZES;
        
        DefaultSecretKeyProvider() {
            final HashMap<ASN1ObjectIdentifier, Integer> hashMap = new HashMap<ASN1ObjectIdentifier, Integer>();
            hashMap.put(new ASN1ObjectIdentifier("1.2.840.113533.7.66.10"), Integers.valueOf(128));
            hashMap.put(PKCSObjectIdentifiers.des_EDE3_CBC, Integers.valueOf(192));
            hashMap.put(NISTObjectIdentifiers.id_aes128_CBC, Integers.valueOf(128));
            hashMap.put(NISTObjectIdentifiers.id_aes192_CBC, Integers.valueOf(192));
            hashMap.put(NISTObjectIdentifiers.id_aes256_CBC, Integers.valueOf(256));
            hashMap.put(NTTObjectIdentifiers.id_camellia128_cbc, Integers.valueOf(128));
            hashMap.put(NTTObjectIdentifiers.id_camellia192_cbc, Integers.valueOf(192));
            hashMap.put(NTTObjectIdentifiers.id_camellia256_cbc, Integers.valueOf(256));
            hashMap.put(CryptoProObjectIdentifiers.gostR28147_gcfb, Integers.valueOf(256));
            this.KEY_SIZES = Collections.unmodifiableMap((Map<?, ?>)hashMap);
        }
        
        public int getKeySize(final AlgorithmIdentifier algorithmIdentifier) {
            final Integer n = this.KEY_SIZES.get(algorithmIdentifier.getAlgorithm());
            if (n != null) {
                return n;
            }
            return -1;
        }
    }
    
    private static class IgnoresCaseHashtable
    {
        private Hashtable keys;
        private Hashtable orig;
        
        private IgnoresCaseHashtable() {
            this.orig = new Hashtable();
            this.keys = new Hashtable();
        }
        
        public Enumeration elements() {
            return this.orig.elements();
        }
        
        public Object get(String lowerCase) {
            final Hashtable keys = this.keys;
            if (lowerCase == null) {
                lowerCase = null;
            }
            else {
                lowerCase = Strings.toLowerCase(lowerCase);
            }
            lowerCase = keys.get(lowerCase);
            if (lowerCase == null) {
                return null;
            }
            return this.orig.get(lowerCase);
        }
        
        public Enumeration keys() {
            return this.orig.keys();
        }
        
        public void put(final String s, final Object o) {
            String lowerCase;
            if (s == null) {
                lowerCase = null;
            }
            else {
                lowerCase = Strings.toLowerCase(s);
            }
            final String s2 = this.keys.get(lowerCase);
            if (s2 != null) {
                this.orig.remove(s2);
            }
            this.keys.put(lowerCase, s);
            this.orig.put(s, o);
        }
        
        public Object remove(String lowerCase) {
            final Hashtable keys = this.keys;
            if (lowerCase == null) {
                lowerCase = null;
            }
            else {
                lowerCase = Strings.toLowerCase(lowerCase);
            }
            lowerCase = keys.remove(lowerCase);
            if (lowerCase == null) {
                return null;
            }
            return this.orig.remove(lowerCase);
        }
    }
}
