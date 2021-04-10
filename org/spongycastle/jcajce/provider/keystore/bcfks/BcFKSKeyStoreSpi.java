package org.spongycastle.jcajce.provider.keystore.bcfks;

import java.math.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.jce.provider.*;
import org.spongycastle.asn1.oiw.*;
import org.spongycastle.asn1.x9.*;
import javax.crypto.spec.*;
import org.spongycastle.asn1.nist.*;
import org.spongycastle.asn1.cms.*;
import java.text.*;
import org.spongycastle.crypto.digests.*;
import org.spongycastle.crypto.generators.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;
import java.util.*;
import org.spongycastle.asn1.pkcs.*;
import java.security.*;
import java.security.spec.*;
import org.spongycastle.asn1.*;
import java.security.cert.*;
import org.spongycastle.util.*;
import java.io.*;
import org.spongycastle.asn1.bc.*;
import javax.crypto.*;

class BcFKSKeyStoreSpi extends KeyStoreSpi
{
    private static final BigInteger CERTIFICATE;
    private static final BigInteger PRIVATE_KEY;
    private static final BigInteger PROTECTED_PRIVATE_KEY;
    private static final BigInteger PROTECTED_SECRET_KEY;
    private static final BigInteger SECRET_KEY;
    private static final Map<String, ASN1ObjectIdentifier> oidMap;
    private static final Map<ASN1ObjectIdentifier, String> publicAlgMap;
    private Date creationDate;
    private final Map<String, ObjectData> entries;
    private AlgorithmIdentifier hmacAlgorithm;
    private KeyDerivationFunc hmacPkbdAlgorithm;
    private Date lastModifiedDate;
    private final Map<String, PrivateKey> privateKeyCache;
    private final BouncyCastleProvider provider;
    
    static {
        oidMap = new HashMap<String, ASN1ObjectIdentifier>();
        publicAlgMap = new HashMap<ASN1ObjectIdentifier, String>();
        BcFKSKeyStoreSpi.oidMap.put("DESEDE", OIWObjectIdentifiers.desEDE);
        BcFKSKeyStoreSpi.oidMap.put("TRIPLEDES", OIWObjectIdentifiers.desEDE);
        BcFKSKeyStoreSpi.oidMap.put("TDEA", OIWObjectIdentifiers.desEDE);
        BcFKSKeyStoreSpi.oidMap.put("HMACSHA1", PKCSObjectIdentifiers.id_hmacWithSHA1);
        BcFKSKeyStoreSpi.oidMap.put("HMACSHA224", PKCSObjectIdentifiers.id_hmacWithSHA224);
        BcFKSKeyStoreSpi.oidMap.put("HMACSHA256", PKCSObjectIdentifiers.id_hmacWithSHA256);
        BcFKSKeyStoreSpi.oidMap.put("HMACSHA384", PKCSObjectIdentifiers.id_hmacWithSHA384);
        BcFKSKeyStoreSpi.oidMap.put("HMACSHA512", PKCSObjectIdentifiers.id_hmacWithSHA512);
        BcFKSKeyStoreSpi.publicAlgMap.put(PKCSObjectIdentifiers.rsaEncryption, "RSA");
        BcFKSKeyStoreSpi.publicAlgMap.put(X9ObjectIdentifiers.id_ecPublicKey, "EC");
        BcFKSKeyStoreSpi.publicAlgMap.put(OIWObjectIdentifiers.elGamalAlgorithm, "DH");
        BcFKSKeyStoreSpi.publicAlgMap.put(PKCSObjectIdentifiers.dhKeyAgreement, "DH");
        BcFKSKeyStoreSpi.publicAlgMap.put(X9ObjectIdentifiers.id_dsa, "DSA");
        CERTIFICATE = BigInteger.valueOf(0L);
        PRIVATE_KEY = BigInteger.valueOf(1L);
        SECRET_KEY = BigInteger.valueOf(2L);
        PROTECTED_PRIVATE_KEY = BigInteger.valueOf(3L);
        PROTECTED_SECRET_KEY = BigInteger.valueOf(4L);
    }
    
    BcFKSKeyStoreSpi(final BouncyCastleProvider provider) {
        this.entries = new HashMap<String, ObjectData>();
        this.privateKeyCache = new HashMap<String, PrivateKey>();
        this.provider = provider;
    }
    
    private byte[] calculateMac(final byte[] array, final AlgorithmIdentifier algorithmIdentifier, final KeyDerivationFunc keyDerivationFunc, char[] array2) throws NoSuchAlgorithmException, IOException {
        final String id = algorithmIdentifier.getAlgorithm().getId();
        final BouncyCastleProvider provider = this.provider;
        Mac mac;
        if (provider != null) {
            mac = Mac.getInstance(id, provider);
        }
        else {
            mac = Mac.getInstance(id);
        }
        Label_0047: {
            if (array2 != null) {
                break Label_0047;
            }
            try {
                array2 = new char[0];
                mac.init(new SecretKeySpec(this.generateKey(keyDerivationFunc, "INTEGRITY_CHECK", array2), id));
                return mac.doFinal(array);
            }
            catch (InvalidKeyException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Cannot set up MAC calculation: ");
                sb.append(ex.getMessage());
                throw new IOException(sb.toString());
            }
        }
    }
    
    private EncryptedPrivateKeyData createPrivateKeySequence(final EncryptedPrivateKeyInfo encryptedPrivateKeyInfo, final Certificate[] array) throws CertificateEncodingException {
        final org.spongycastle.asn1.x509.Certificate[] array2 = new org.spongycastle.asn1.x509.Certificate[array.length];
        for (int i = 0; i != array.length; ++i) {
            array2[i] = org.spongycastle.asn1.x509.Certificate.getInstance(array[i].getEncoded());
        }
        return new EncryptedPrivateKeyData(encryptedPrivateKeyInfo, array2);
    }
    
    private Certificate decodeCertificate(final Object o) {
        final BouncyCastleProvider provider = this.provider;
        if (provider != null) {
            try {
                return CertificateFactory.getInstance("X.509", provider).generateCertificate(new ByteArrayInputStream(org.spongycastle.asn1.x509.Certificate.getInstance(o).getEncoded()));
            }
            catch (Exception ex) {
                return null;
            }
        }
        try {
            return CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(org.spongycastle.asn1.x509.Certificate.getInstance(o).getEncoded()));
        }
        catch (Exception ex2) {
            return null;
        }
    }
    
    private byte[] decryptData(final String s, final AlgorithmIdentifier algorithmIdentifier, char[] array, final byte[] array2) throws IOException {
        if (algorithmIdentifier.getAlgorithm().equals(PKCSObjectIdentifiers.id_PBES2)) {
            final PBES2Parameters instance = PBES2Parameters.getInstance(algorithmIdentifier.getParameters());
            final EncryptionScheme encryptionScheme = instance.getEncryptionScheme();
            if (encryptionScheme.getAlgorithm().equals(NISTObjectIdentifiers.id_aes256_CCM)) {
                try {
                    final CCMParameters instance2 = CCMParameters.getInstance(encryptionScheme.getParameters());
                    Cipher cipher;
                    AlgorithmParameters algorithmParameters;
                    if (this.provider == null) {
                        cipher = Cipher.getInstance("AES/CCM/NoPadding");
                        algorithmParameters = AlgorithmParameters.getInstance("CCM");
                    }
                    else {
                        cipher = Cipher.getInstance("AES/CCM/NoPadding", this.provider);
                        algorithmParameters = AlgorithmParameters.getInstance("CCM", this.provider);
                    }
                    algorithmParameters.init(instance2.getEncoded());
                    final KeyDerivationFunc keyDerivationFunc = instance.getKeyDerivationFunc();
                    if (array == null) {
                        array = new char[0];
                    }
                    cipher.init(2, new SecretKeySpec(this.generateKey(keyDerivationFunc, s, array), "AES"), algorithmParameters);
                    return cipher.doFinal(array2);
                }
                catch (Exception ex) {
                    throw new IOException(ex.toString());
                }
            }
            throw new IOException("BCFKS KeyStore cannot recognize protection encryption algorithm.");
        }
        throw new IOException("BCFKS KeyStore cannot recognize protection algorithm.");
    }
    
    private Date extractCreationDate(final ObjectData objectData, final Date date) {
        try {
            return objectData.getCreationDate().getDate();
        }
        catch (ParseException ex) {
            return date;
        }
    }
    
    private byte[] generateKey(final KeyDerivationFunc keyDerivationFunc, final String s, final char[] array) throws IOException {
        final byte[] pkcs12PasswordToBytes = PBEParametersGenerator.PKCS12PasswordToBytes(array);
        final byte[] pkcs12PasswordToBytes2 = PBEParametersGenerator.PKCS12PasswordToBytes(s.toCharArray());
        final PKCS5S2ParametersGenerator pkcs5S2ParametersGenerator = new PKCS5S2ParametersGenerator(new SHA512Digest());
        if (!keyDerivationFunc.getAlgorithm().equals(PKCSObjectIdentifiers.id_PBKDF2)) {
            throw new IOException("BCFKS KeyStore: unrecognized MAC PBKD.");
        }
        final PBKDF2Params instance = PBKDF2Params.getInstance(keyDerivationFunc.getParameters());
        if (instance.getPrf().getAlgorithm().equals(PKCSObjectIdentifiers.id_hmacWithSHA512)) {
            pkcs5S2ParametersGenerator.init(Arrays.concatenate(pkcs12PasswordToBytes, pkcs12PasswordToBytes2), instance.getSalt(), instance.getIterationCount().intValue());
            return ((KeyParameter)pkcs5S2ParametersGenerator.generateDerivedParameters(instance.getKeyLength().intValue() * 8)).getKey();
        }
        throw new IOException("BCFKS KeyStore: unrecognized MAC PBKD PRF.");
    }
    
    private KeyDerivationFunc generatePkbdAlgorithmIdentifier(final int n) {
        final byte[] array = new byte[64];
        this.getDefaultSecureRandom().nextBytes(array);
        return new KeyDerivationFunc(PKCSObjectIdentifiers.id_PBKDF2, new PBKDF2Params(array, 1024, n, new AlgorithmIdentifier(PKCSObjectIdentifiers.id_hmacWithSHA512, DERNull.INSTANCE)));
    }
    
    private SecureRandom getDefaultSecureRandom() {
        return new SecureRandom();
    }
    
    private static String getPublicKeyAlg(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        final String s = BcFKSKeyStoreSpi.publicAlgMap.get(asn1ObjectIdentifier);
        if (s != null) {
            return s;
        }
        return asn1ObjectIdentifier.getId();
    }
    
    private void verifyMac(final byte[] array, final PbkdMacIntegrityCheck pbkdMacIntegrityCheck, final char[] array2) throws NoSuchAlgorithmException, IOException {
        if (Arrays.constantTimeAreEqual(this.calculateMac(array, pbkdMacIntegrityCheck.getMacAlgorithm(), pbkdMacIntegrityCheck.getPbkdAlgorithm(), array2), pbkdMacIntegrityCheck.getMac())) {
            return;
        }
        throw new IOException("BCFKS KeyStore corrupted: MAC calculation failed.");
    }
    
    @Override
    public Enumeration<String> engineAliases() {
        return (Enumeration<String>)new Enumeration() {
            final /* synthetic */ Iterator val$it = new HashSet(BcFKSKeyStoreSpi.this.entries.keySet()).iterator();
            
            @Override
            public boolean hasMoreElements() {
                return this.val$it.hasNext();
            }
            
            @Override
            public Object nextElement() {
                return this.val$it.next();
            }
        };
    }
    
    @Override
    public boolean engineContainsAlias(final String s) {
        if (s != null) {
            return this.entries.containsKey(s);
        }
        throw new NullPointerException("alias value is null");
    }
    
    @Override
    public void engineDeleteEntry(final String s) throws KeyStoreException {
        if (this.entries.get(s) == null) {
            return;
        }
        this.privateKeyCache.remove(s);
        this.entries.remove(s);
        this.lastModifiedDate = new Date();
    }
    
    @Override
    public Certificate engineGetCertificate(final String s) {
        final ObjectData objectData = this.entries.get(s);
        if (objectData != null) {
            Object data;
            if (!objectData.getType().equals(BcFKSKeyStoreSpi.PRIVATE_KEY) && !objectData.getType().equals(BcFKSKeyStoreSpi.PROTECTED_PRIVATE_KEY)) {
                if (!objectData.getType().equals(BcFKSKeyStoreSpi.CERTIFICATE)) {
                    return null;
                }
                data = objectData.getData();
            }
            else {
                data = EncryptedPrivateKeyData.getInstance(objectData.getData()).getCertificateChain()[0];
            }
            return this.decodeCertificate(data);
        }
        return null;
    }
    
    @Override
    public String engineGetCertificateAlias(Certificate encoded) {
        if (encoded == null) {
            return null;
        }
        try {
            encoded = (Certificate)(Object)encoded.getEncoded();
            for (final String s : this.entries.keySet()) {
                final ObjectData objectData = this.entries.get(s);
                if (!objectData.getType().equals(BcFKSKeyStoreSpi.CERTIFICATE)) {
                    if (!objectData.getType().equals(BcFKSKeyStoreSpi.PRIVATE_KEY)) {
                        if (!objectData.getType().equals(BcFKSKeyStoreSpi.PROTECTED_PRIVATE_KEY)) {
                            continue;
                        }
                    }
                    try {
                        if (Arrays.areEqual(EncryptedPrivateKeyData.getInstance(objectData.getData()).getCertificateChain()[0].toASN1Primitive().getEncoded(), (byte[])(Object)encoded)) {
                            return s;
                        }
                        continue;
                    }
                    catch (IOException ex) {
                        continue;
                    }
                    break;
                }
                if (Arrays.areEqual(objectData.getData(), (byte[])(Object)encoded)) {
                    return s;
                }
            }
            return null;
        }
        catch (CertificateEncodingException ex2) {
            return null;
        }
    }
    
    @Override
    public Certificate[] engineGetCertificateChain(final String s) {
        final ObjectData objectData = this.entries.get(s);
        if (objectData != null && (objectData.getType().equals(BcFKSKeyStoreSpi.PRIVATE_KEY) || objectData.getType().equals(BcFKSKeyStoreSpi.PROTECTED_PRIVATE_KEY))) {
            final org.spongycastle.asn1.x509.Certificate[] certificateChain = EncryptedPrivateKeyData.getInstance(objectData.getData()).getCertificateChain();
            final int length = certificateChain.length;
            final X509Certificate[] array = new X509Certificate[length];
            for (int i = 0; i != length; ++i) {
                array[i] = (X509Certificate)this.decodeCertificate(certificateChain[i]);
            }
            return array;
        }
        return null;
    }
    
    @Override
    public Date engineGetCreationDate(final String s) {
        final ObjectData objectData = this.entries.get(s);
        if (objectData != null) {
            try {
                return objectData.getLastModifiedDate().getDate();
            }
            catch (ParseException ex) {
                return new Date();
            }
        }
        return null;
    }
    
    @Override
    public Key engineGetKey(final String s, final char[] array) throws NoSuchAlgorithmException, UnrecoverableKeyException {
        final ObjectData objectData = this.entries.get(s);
        if (objectData != null) {
            if (!objectData.getType().equals(BcFKSKeyStoreSpi.PRIVATE_KEY)) {
                if (!objectData.getType().equals(BcFKSKeyStoreSpi.PROTECTED_PRIVATE_KEY)) {
                    if (!objectData.getType().equals(BcFKSKeyStoreSpi.SECRET_KEY) && !objectData.getType().equals(BcFKSKeyStoreSpi.PROTECTED_SECRET_KEY)) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("BCFKS KeyStore unable to recover secret key (");
                        sb.append(s);
                        sb.append("): type not recognized");
                        throw new UnrecoverableKeyException(sb.toString());
                    }
                    final EncryptedSecretKeyData instance = EncryptedSecretKeyData.getInstance(objectData.getData());
                    try {
                        final SecretKeyData instance2 = SecretKeyData.getInstance(this.decryptData("SECRET_KEY_ENCRYPTION", instance.getKeyEncryptionAlgorithm(), array, instance.getEncryptedKeyData()));
                        SecretKeyFactory secretKeyFactory;
                        if (this.provider != null) {
                            secretKeyFactory = SecretKeyFactory.getInstance(instance2.getKeyAlgorithm().getId(), this.provider);
                        }
                        else {
                            secretKeyFactory = SecretKeyFactory.getInstance(instance2.getKeyAlgorithm().getId());
                        }
                        return secretKeyFactory.generateSecret(new SecretKeySpec(instance2.getKeyBytes(), instance2.getKeyAlgorithm().getId()));
                    }
                    catch (Exception ex) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("BCFKS KeyStore unable to recover secret key (");
                        sb2.append(s);
                        sb2.append("): ");
                        sb2.append(ex.getMessage());
                        throw new UnrecoverableKeyException(sb2.toString());
                    }
                }
            }
            final PrivateKey privateKey = this.privateKeyCache.get(s);
            if (privateKey != null) {
                return privateKey;
            }
            final EncryptedPrivateKeyInfo instance3 = EncryptedPrivateKeyInfo.getInstance(EncryptedPrivateKeyData.getInstance(objectData.getData()).getEncryptedPrivateKeyInfo());
            try {
                final PrivateKeyInfo instance4 = PrivateKeyInfo.getInstance(this.decryptData("PRIVATE_KEY_ENCRYPTION", instance3.getEncryptionAlgorithm(), array, instance3.getEncryptedData()));
                KeyFactory keyFactory;
                if (this.provider != null) {
                    keyFactory = KeyFactory.getInstance(instance4.getPrivateKeyAlgorithm().getAlgorithm().getId(), this.provider);
                }
                else {
                    keyFactory = KeyFactory.getInstance(getPublicKeyAlg(instance4.getPrivateKeyAlgorithm().getAlgorithm()));
                }
                final PrivateKey generatePrivate = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(instance4.getEncoded()));
                this.privateKeyCache.put(s, generatePrivate);
                return generatePrivate;
            }
            catch (Exception ex2) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("BCFKS KeyStore unable to recover private key (");
                sb3.append(s);
                sb3.append("): ");
                sb3.append(ex2.getMessage());
                throw new UnrecoverableKeyException(sb3.toString());
            }
        }
        return null;
    }
    
    @Override
    public boolean engineIsCertificateEntry(final String s) {
        final ObjectData objectData = this.entries.get(s);
        return objectData != null && objectData.getType().equals(BcFKSKeyStoreSpi.CERTIFICATE);
    }
    
    @Override
    public boolean engineIsKeyEntry(final String s) {
        final ObjectData objectData = this.entries.get(s);
        if (objectData != null) {
            final BigInteger type = objectData.getType();
            if (type.equals(BcFKSKeyStoreSpi.PRIVATE_KEY) || type.equals(BcFKSKeyStoreSpi.SECRET_KEY) || type.equals(BcFKSKeyStoreSpi.PROTECTED_PRIVATE_KEY) || type.equals(BcFKSKeyStoreSpi.PROTECTED_SECRET_KEY)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void engineLoad(final InputStream inputStream, final char[] array) throws IOException, NoSuchAlgorithmException, CertificateException {
        this.entries.clear();
        this.privateKeyCache.clear();
        this.creationDate = null;
        this.lastModifiedDate = null;
        this.hmacAlgorithm = null;
        if (inputStream == null) {
            final Date date = new Date();
            this.creationDate = date;
            this.lastModifiedDate = date;
            this.hmacAlgorithm = new AlgorithmIdentifier(PKCSObjectIdentifiers.id_hmacWithSHA512, DERNull.INSTANCE);
            this.hmacPkbdAlgorithm = this.generatePkbdAlgorithmIdentifier(64);
            return;
        }
        final ObjectStore instance = ObjectStore.getInstance(new ASN1InputStream(inputStream).readObject());
        final ObjectStoreIntegrityCheck integrityCheck = instance.getIntegrityCheck();
        if (integrityCheck.getType() == 0) {
            final PbkdMacIntegrityCheck instance2 = PbkdMacIntegrityCheck.getInstance(integrityCheck.getIntegrityCheck());
            this.hmacAlgorithm = instance2.getMacAlgorithm();
            this.hmacPkbdAlgorithm = instance2.getPbkdAlgorithm();
            this.verifyMac(instance.getStoreData().toASN1Primitive().getEncoded(), instance2, array);
            Object o;
            final ASN1Encodable asn1Encodable = (ASN1Encodable)(o = instance.getStoreData());
            if (asn1Encodable instanceof EncryptedObjectStoreData) {
                final EncryptedObjectStoreData encryptedObjectStoreData = (EncryptedObjectStoreData)asn1Encodable;
                o = this.decryptData("STORE_ENCRYPTION", encryptedObjectStoreData.getEncryptionAlgorithm(), array, encryptedObjectStoreData.getEncryptedContent().getOctets());
            }
            final ObjectStoreData instance3 = ObjectStoreData.getInstance(o);
            try {
                this.creationDate = instance3.getCreationDate().getDate();
                this.lastModifiedDate = instance3.getLastModifiedDate().getDate();
                if (instance3.getIntegrityAlgorithm().equals(this.hmacAlgorithm)) {
                    final Iterator<ASN1Encodable> iterator = instance3.getObjectDataSequence().iterator();
                    while (iterator.hasNext()) {
                        final ObjectData instance4 = ObjectData.getInstance(iterator.next());
                        this.entries.put(instance4.getIdentifier(), instance4);
                    }
                    return;
                }
                throw new IOException("BCFKS KeyStore storeData integrity algorithm does not match store integrity algorithm.");
            }
            catch (ParseException ex) {
                throw new IOException("BCFKS KeyStore unable to parse store data information.");
            }
        }
        throw new IOException("BCFKS KeyStore unable to recognize integrity check.");
    }
    
    @Override
    public void engineSetCertificateEntry(final String s, final Certificate certificate) throws KeyStoreException {
        final ObjectData objectData = this.entries.get(s);
        final Date lastModifiedDate = new Date();
        Date creationDate;
        if (objectData != null) {
            if (!objectData.getType().equals(BcFKSKeyStoreSpi.CERTIFICATE)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("BCFKS KeyStore already has a key entry with alias ");
                sb.append(s);
                throw new KeyStoreException(sb.toString());
            }
            creationDate = this.extractCreationDate(objectData, lastModifiedDate);
        }
        else {
            creationDate = lastModifiedDate;
        }
        try {
            this.entries.put(s, new ObjectData(BcFKSKeyStoreSpi.CERTIFICATE, s, creationDate, lastModifiedDate, certificate.getEncoded(), null));
            this.lastModifiedDate = lastModifiedDate;
        }
        catch (CertificateEncodingException ex) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("BCFKS KeyStore unable to handle certificate: ");
            sb2.append(ex.getMessage());
            throw new ExtKeyStoreException(sb2.toString(), ex);
        }
    }
    
    @Override
    public void engineSetKeyEntry(final String s, final Key key, char[] array, final Certificate[] array2) throws KeyStoreException {
        final Date lastModifiedDate = new Date();
        final ObjectData objectData = this.entries.get(s);
        Date creationDate;
        if (objectData != null) {
            creationDate = this.extractCreationDate(objectData, lastModifiedDate);
        }
        else {
            creationDate = lastModifiedDate;
        }
        this.privateKeyCache.remove(s);
        Label_0562: {
            if (key instanceof PrivateKey) {
                if (array2 != null) {
                    try {
                        final byte[] encoded = key.getEncoded();
                        final KeyDerivationFunc generatePkbdAlgorithmIdentifier = this.generatePkbdAlgorithmIdentifier(32);
                        if (array == null) {
                            array = new char[0];
                        }
                        final byte[] generateKey = this.generateKey(generatePkbdAlgorithmIdentifier, "PRIVATE_KEY_ENCRYPTION", array);
                        Cipher cipher;
                        if (this.provider == null) {
                            cipher = Cipher.getInstance("AES/CCM/NoPadding");
                        }
                        else {
                            cipher = Cipher.getInstance("AES/CCM/NoPadding", this.provider);
                        }
                        cipher.init(1, new SecretKeySpec(generateKey, "AES"));
                        this.entries.put(s, new ObjectData(BcFKSKeyStoreSpi.PRIVATE_KEY, s, creationDate, lastModifiedDate, this.createPrivateKeySequence(new EncryptedPrivateKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.id_PBES2, new PBES2Parameters(generatePkbdAlgorithmIdentifier, new EncryptionScheme(NISTObjectIdentifiers.id_aes256_CCM, CCMParameters.getInstance(cipher.getParameters().getEncoded())))), cipher.doFinal(encoded)), array2).getEncoded(), null));
                        break Label_0562;
                    }
                    catch (Exception ex) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("BCFKS KeyStore exception storing private key: ");
                        sb.append(ex.toString());
                        throw new ExtKeyStoreException(sb.toString(), ex);
                    }
                }
                throw new KeyStoreException("BCFKS KeyStore requires a certificate chain for private key storage.");
            }
            if (!(key instanceof SecretKey)) {
                throw new KeyStoreException("BCFKS KeyStore unable to recognize key.");
            }
            if (array2 != null) {
                throw new KeyStoreException("BCFKS KeyStore cannot store certificate chain with secret key.");
            }
            try {
                final byte[] encoded2 = key.getEncoded();
                final KeyDerivationFunc generatePkbdAlgorithmIdentifier2 = this.generatePkbdAlgorithmIdentifier(32);
                if (array == null) {
                    array = new char[0];
                }
                final byte[] generateKey2 = this.generateKey(generatePkbdAlgorithmIdentifier2, "SECRET_KEY_ENCRYPTION", array);
                Cipher cipher2;
                if (this.provider == null) {
                    cipher2 = Cipher.getInstance("AES/CCM/NoPadding");
                }
                else {
                    cipher2 = Cipher.getInstance("AES/CCM/NoPadding", this.provider);
                }
                cipher2.init(1, new SecretKeySpec(generateKey2, "AES"));
                final String upperCase = Strings.toUpperCase(key.getAlgorithm());
                byte[] array3;
                if (upperCase.indexOf("AES") > -1) {
                    array3 = new SecretKeyData(NISTObjectIdentifiers.aes, encoded2).getEncoded();
                }
                else {
                    final ASN1ObjectIdentifier asn1ObjectIdentifier = BcFKSKeyStoreSpi.oidMap.get(upperCase);
                    if (asn1ObjectIdentifier == null) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("BCFKS KeyStore cannot recognize secret key (");
                        sb2.append(upperCase);
                        sb2.append(") for storage.");
                        throw new KeyStoreException(sb2.toString());
                    }
                    array3 = new SecretKeyData(asn1ObjectIdentifier, encoded2).getEncoded();
                }
                this.entries.put(s, new ObjectData(BcFKSKeyStoreSpi.SECRET_KEY, s, creationDate, lastModifiedDate, new EncryptedSecretKeyData(new AlgorithmIdentifier(PKCSObjectIdentifiers.id_PBES2, new PBES2Parameters(generatePkbdAlgorithmIdentifier2, new EncryptionScheme(NISTObjectIdentifiers.id_aes256_CCM, CCMParameters.getInstance(cipher2.getParameters().getEncoded())))), cipher2.doFinal(array3)).getEncoded(), null));
                this.lastModifiedDate = lastModifiedDate;
                return;
            }
            catch (Exception ex2) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("BCFKS KeyStore exception storing private key: ");
                sb3.append(ex2.toString());
                throw new ExtKeyStoreException(sb3.toString(), ex2);
            }
        }
        throw new KeyStoreException("BCFKS KeyStore cannot store certificate chain with secret key.");
    }
    
    @Override
    public void engineSetKeyEntry(final String s, final byte[] array, final Certificate[] array2) throws KeyStoreException {
        final Date lastModifiedDate = new Date();
        final ObjectData objectData = this.entries.get(s);
        Date creationDate;
        if (objectData != null) {
            creationDate = this.extractCreationDate(objectData, lastModifiedDate);
        }
        else {
            creationDate = lastModifiedDate;
        }
        if (array2 != null) {
            try {
                final EncryptedPrivateKeyInfo instance = EncryptedPrivateKeyInfo.getInstance(array);
                try {
                    this.privateKeyCache.remove(s);
                    this.entries.put(s, new ObjectData(BcFKSKeyStoreSpi.PROTECTED_PRIVATE_KEY, s, creationDate, lastModifiedDate, this.createPrivateKeySequence(instance, array2).getEncoded(), null));
                }
                catch (Exception ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("BCFKS KeyStore exception storing protected private key: ");
                    sb.append(ex.toString());
                    throw new ExtKeyStoreException(sb.toString(), ex);
                }
            }
            catch (Exception ex2) {
                throw new ExtKeyStoreException("BCFKS KeyStore private key encoding must be an EncryptedPrivateKeyInfo.", ex2);
            }
        }
        try {
            this.entries.put(s, new ObjectData(BcFKSKeyStoreSpi.PROTECTED_SECRET_KEY, s, creationDate, lastModifiedDate, array, null));
            this.lastModifiedDate = lastModifiedDate;
        }
        catch (Exception ex3) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("BCFKS KeyStore exception storing protected private key: ");
            sb2.append(ex3.toString());
            throw new ExtKeyStoreException(sb2.toString(), ex3);
        }
    }
    
    @Override
    public int engineSize() {
        return this.entries.size();
    }
    
    @Override
    public void engineStore(final OutputStream outputStream, final char[] array) throws IOException, NoSuchAlgorithmException, CertificateException {
        final ObjectData[] array2 = this.entries.values().toArray(new ObjectData[this.entries.size()]);
        final KeyDerivationFunc generatePkbdAlgorithmIdentifier = this.generatePkbdAlgorithmIdentifier(32);
        char[] array3;
        if (array != null) {
            array3 = array;
        }
        else {
            array3 = new char[0];
        }
        final byte[] generateKey = this.generateKey(generatePkbdAlgorithmIdentifier, "STORE_ENCRYPTION", array3);
        final ObjectStoreData objectStoreData = new ObjectStoreData(this.hmacAlgorithm, this.creationDate, this.lastModifiedDate, new ObjectDataSequence(array2), null);
        try {
            Cipher cipher;
            if (this.provider == null) {
                cipher = Cipher.getInstance("AES/CCM/NoPadding");
            }
            else {
                cipher = Cipher.getInstance("AES/CCM/NoPadding", this.provider);
            }
            cipher.init(1, new SecretKeySpec(generateKey, "AES"));
            final EncryptedObjectStoreData encryptedObjectStoreData = new EncryptedObjectStoreData(new AlgorithmIdentifier(PKCSObjectIdentifiers.id_PBES2, new PBES2Parameters(generatePkbdAlgorithmIdentifier, new EncryptionScheme(NISTObjectIdentifiers.id_aes256_CCM, CCMParameters.getInstance(cipher.getParameters().getEncoded())))), cipher.doFinal(objectStoreData.getEncoded()));
            final PBKDF2Params instance = PBKDF2Params.getInstance(this.hmacPkbdAlgorithm.getParameters());
            final byte[] array4 = new byte[instance.getSalt().length];
            this.getDefaultSecureRandom().nextBytes(array4);
            this.hmacPkbdAlgorithm = new KeyDerivationFunc(this.hmacPkbdAlgorithm.getAlgorithm(), new PBKDF2Params(array4, instance.getIterationCount().intValue(), instance.getKeyLength().intValue(), instance.getPrf()));
            outputStream.write(new ObjectStore(encryptedObjectStoreData, new ObjectStoreIntegrityCheck(new PbkdMacIntegrityCheck(this.hmacAlgorithm, this.hmacPkbdAlgorithm, this.calculateMac(encryptedObjectStoreData.getEncoded(), this.hmacAlgorithm, this.hmacPkbdAlgorithm, array)))).getEncoded());
            outputStream.flush();
        }
        catch (InvalidKeyException ex) {
            throw new IOException(ex.toString());
        }
        catch (IllegalBlockSizeException ex2) {
            throw new IOException(ex2.toString());
        }
        catch (BadPaddingException ex3) {
            throw new IOException(ex3.toString());
        }
        catch (NoSuchPaddingException ex4) {
            throw new NoSuchAlgorithmException(ex4.toString());
        }
    }
    
    public static class Def extends BcFKSKeyStoreSpi
    {
        public Def() {
            super(null);
        }
    }
    
    private static class ExtKeyStoreException extends KeyStoreException
    {
        private final Throwable cause;
        
        ExtKeyStoreException(final String s, final Throwable cause) {
            super(s);
            this.cause = cause;
        }
        
        @Override
        public Throwable getCause() {
            return this.cause;
        }
    }
    
    public static class Std extends BcFKSKeyStoreSpi
    {
        public Std() {
            super(new BouncyCastleProvider());
        }
    }
}
