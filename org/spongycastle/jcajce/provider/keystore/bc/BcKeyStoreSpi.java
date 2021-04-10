package org.spongycastle.jcajce.provider.keystore.bc;

import org.spongycastle.jce.interfaces.*;
import org.spongycastle.jcajce.util.*;
import java.security.cert.*;
import org.spongycastle.jce.provider.*;
import java.util.*;
import java.security.*;
import org.spongycastle.crypto.digests.*;
import org.spongycastle.crypto.macs.*;
import org.spongycastle.crypto.generators.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.*;
import javax.crypto.spec.*;
import java.security.spec.*;
import org.spongycastle.util.io.*;
import javax.crypto.*;
import org.spongycastle.crypto.io.*;
import java.io.*;

public class BcKeyStoreSpi extends KeyStoreSpi implements BCKeyStore
{
    static final int CERTIFICATE = 1;
    static final int KEY = 2;
    private static final String KEY_CIPHER = "PBEWithSHAAnd3-KeyTripleDES-CBC";
    static final int KEY_PRIVATE = 0;
    static final int KEY_PUBLIC = 1;
    private static final int KEY_SALT_SIZE = 20;
    static final int KEY_SECRET = 2;
    private static final int MIN_ITERATIONS = 1024;
    static final int NULL = 0;
    static final int SEALED = 4;
    static final int SECRET = 3;
    private static final String STORE_CIPHER = "PBEWithSHAAndTwofish-CBC";
    private static final int STORE_SALT_SIZE = 20;
    private static final int STORE_VERSION = 2;
    private final JcaJceHelper helper;
    protected SecureRandom random;
    protected Hashtable table;
    protected int version;
    
    public BcKeyStoreSpi(final int version) {
        this.table = new Hashtable();
        this.random = new SecureRandom();
        this.helper = new BCJcaJceHelper();
        this.version = version;
    }
    
    private Certificate decodeCertificate(final DataInputStream dataInputStream) throws IOException {
        final String utf = dataInputStream.readUTF();
        final byte[] array = new byte[dataInputStream.readInt()];
        dataInputStream.readFully(array);
        try {
            return this.helper.createCertificateFactory(utf).generateCertificate(new ByteArrayInputStream(array));
        }
        catch (CertificateException ex) {
            throw new IOException(ex.toString());
        }
        catch (NoSuchProviderException ex2) {
            throw new IOException(ex2.toString());
        }
    }
    
    private Key decodeKey(final DataInputStream dataInputStream) throws IOException {
        final int read = dataInputStream.read();
        final String utf = dataInputStream.readUTF();
        final String utf2 = dataInputStream.readUTF();
        final byte[] array = new byte[dataInputStream.readInt()];
        dataInputStream.readFully(array);
        EncodedKeySpec encodedKeySpec;
        if (!utf.equals("PKCS#8") && !utf.equals("PKCS8")) {
            if (!utf.equals("X.509") && !utf.equals("X509")) {
                if (utf.equals("RAW")) {
                    return new SecretKeySpec(array, utf2);
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("Key format ");
                sb.append(utf);
                sb.append(" not recognised!");
                throw new IOException(sb.toString());
            }
            else {
                encodedKeySpec = new X509EncodedKeySpec(array);
            }
        }
        else {
            encodedKeySpec = new PKCS8EncodedKeySpec(array);
        }
        Label_0245: {
            if (read == 0) {
                break Label_0245;
            }
            Label_0230: {
                if (read == 1) {
                    break Label_0230;
                }
                Label_0190: {
                    if (read != 2) {
                        break Label_0190;
                    }
                    try {
                        return this.helper.createSecretKeyFactory(utf2).generateSecret(encodedKeySpec);
                        return this.helper.createKeyFactory(utf2).generatePrivate(encodedKeySpec);
                        return this.helper.createKeyFactory(utf2).generatePublic(encodedKeySpec);
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Key type ");
                        sb2.append(read);
                        sb2.append(" not recognised!");
                        throw new IOException(sb2.toString());
                    }
                    catch (Exception ex) {
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("Exception creating key: ");
                        sb3.append(ex.toString());
                        throw new IOException(sb3.toString());
                    }
                }
            }
        }
    }
    
    private void encodeCertificate(final Certificate certificate, final DataOutputStream dataOutputStream) throws IOException {
        try {
            final byte[] encoded = certificate.getEncoded();
            dataOutputStream.writeUTF(certificate.getType());
            dataOutputStream.writeInt(encoded.length);
            dataOutputStream.write(encoded);
        }
        catch (CertificateEncodingException ex) {
            throw new IOException(ex.toString());
        }
    }
    
    private void encodeKey(final Key key, final DataOutputStream dataOutputStream) throws IOException {
        final byte[] encoded = key.getEncoded();
        int n;
        if (key instanceof PrivateKey) {
            n = 0;
        }
        else if (key instanceof PublicKey) {
            n = 1;
        }
        else {
            n = 2;
        }
        dataOutputStream.write(n);
        dataOutputStream.writeUTF(key.getFormat());
        dataOutputStream.writeUTF(key.getAlgorithm());
        dataOutputStream.writeInt(encoded.length);
        dataOutputStream.write(encoded);
    }
    
    static Provider getBouncyCastleProvider() {
        if (Security.getProvider("SC") != null) {
            return Security.getProvider("SC");
        }
        return new BouncyCastleProvider();
    }
    
    @Override
    public Enumeration engineAliases() {
        return this.table.keys();
    }
    
    @Override
    public boolean engineContainsAlias(final String s) {
        return this.table.get(s) != null;
    }
    
    @Override
    public void engineDeleteEntry(final String s) throws KeyStoreException {
        if (this.table.get(s) == null) {
            return;
        }
        this.table.remove(s);
    }
    
    @Override
    public Certificate engineGetCertificate(final String s) {
        final StoreEntry storeEntry = this.table.get(s);
        if (storeEntry != null) {
            if (storeEntry.getType() == 1) {
                return (Certificate)storeEntry.getObject();
            }
            final Certificate[] certificateChain = storeEntry.getCertificateChain();
            if (certificateChain != null) {
                return certificateChain[0];
            }
        }
        return null;
    }
    
    @Override
    public String engineGetCertificateAlias(final Certificate certificate) {
        final Enumeration<StoreEntry> elements = this.table.elements();
        while (elements.hasMoreElements()) {
            final StoreEntry storeEntry = elements.nextElement();
            if (storeEntry.getObject() instanceof Certificate) {
                if (((Certificate)storeEntry.getObject()).equals(certificate)) {
                    return storeEntry.getAlias();
                }
                continue;
            }
            else {
                final Certificate[] certificateChain = storeEntry.getCertificateChain();
                if (certificateChain != null && certificateChain[0].equals(certificate)) {
                    return storeEntry.getAlias();
                }
                continue;
            }
        }
        return null;
    }
    
    @Override
    public Certificate[] engineGetCertificateChain(final String s) {
        final StoreEntry storeEntry = this.table.get(s);
        if (storeEntry != null) {
            return storeEntry.getCertificateChain();
        }
        return null;
    }
    
    @Override
    public Date engineGetCreationDate(final String s) {
        final StoreEntry storeEntry = this.table.get(s);
        if (storeEntry != null) {
            return storeEntry.getDate();
        }
        return null;
    }
    
    @Override
    public Key engineGetKey(final String s, final char[] array) throws NoSuchAlgorithmException, UnrecoverableKeyException {
        final StoreEntry storeEntry = this.table.get(s);
        if (storeEntry != null && storeEntry.getType() != 1) {
            return (Key)storeEntry.getObject(array);
        }
        return null;
    }
    
    @Override
    public boolean engineIsCertificateEntry(final String s) {
        final StoreEntry storeEntry = this.table.get(s);
        return storeEntry != null && storeEntry.getType() == 1;
    }
    
    @Override
    public boolean engineIsKeyEntry(final String s) {
        final StoreEntry storeEntry = this.table.get(s);
        return storeEntry != null && storeEntry.getType() != 1;
    }
    
    @Override
    public void engineLoad(final InputStream inputStream, final char[] array) throws IOException {
        this.table.clear();
        if (inputStream == null) {
            return;
        }
        final DataInputStream dataInputStream = new DataInputStream(inputStream);
        final int int1 = dataInputStream.readInt();
        if (int1 != 2 && int1 != 0 && int1 != 1) {
            throw new IOException("Wrong version of key store.");
        }
        final int int2 = dataInputStream.readInt();
        if (int2 <= 0) {
            throw new IOException("Invalid salt detected");
        }
        final byte[] array2 = new byte[int2];
        dataInputStream.readFully(array2);
        final int int3 = dataInputStream.readInt();
        final HMac hMac = new HMac(new SHA1Digest());
        if (array == null || array.length == 0) {
            this.loadStore(dataInputStream);
            dataInputStream.readFully(new byte[hMac.getMacSize()]);
            return;
        }
        final byte[] pkcs12PasswordToBytes = PBEParametersGenerator.PKCS12PasswordToBytes(array);
        final PKCS12ParametersGenerator pkcs12ParametersGenerator = new PKCS12ParametersGenerator(new SHA1Digest());
        pkcs12ParametersGenerator.init(pkcs12PasswordToBytes, array2, int3);
        int macSize;
        if (int1 != 2) {
            macSize = hMac.getMacSize();
        }
        else {
            macSize = hMac.getMacSize() * 8;
        }
        final CipherParameters generateDerivedMacParameters = pkcs12ParametersGenerator.generateDerivedMacParameters(macSize);
        Arrays.fill(pkcs12PasswordToBytes, (byte)0);
        hMac.init(generateDerivedMacParameters);
        this.loadStore(new MacInputStream(dataInputStream, hMac));
        final byte[] array3 = new byte[hMac.getMacSize()];
        hMac.doFinal(array3, 0);
        final byte[] array4 = new byte[hMac.getMacSize()];
        dataInputStream.readFully(array4);
        if (Arrays.constantTimeAreEqual(array3, array4)) {
            return;
        }
        this.table.clear();
        throw new IOException("KeyStore integrity check failed.");
    }
    
    @Override
    public void engineSetCertificateEntry(final String s, final Certificate certificate) throws KeyStoreException {
        final StoreEntry storeEntry = this.table.get(s);
        if (storeEntry != null && storeEntry.getType() != 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("key store already has a key entry with alias ");
            sb.append(s);
            throw new KeyStoreException(sb.toString());
        }
        this.table.put(s, new StoreEntry(s, certificate));
    }
    
    @Override
    public void engineSetKeyEntry(final String s, final Key key, final char[] array, final Certificate[] array2) throws KeyStoreException {
        if (key instanceof PrivateKey) {
            if (array2 == null) {
                throw new KeyStoreException("no certificate chain for private key");
            }
        }
        try {
            this.table.put(s, new StoreEntry(s, key, array, array2));
        }
        catch (Exception ex) {
            throw new KeyStoreException(ex.toString());
        }
    }
    
    @Override
    public void engineSetKeyEntry(final String s, final byte[] array, final Certificate[] array2) throws KeyStoreException {
        this.table.put(s, new StoreEntry(s, array, array2));
    }
    
    @Override
    public int engineSize() {
        return this.table.size();
    }
    
    @Override
    public void engineStore(final OutputStream outputStream, final char[] array) throws IOException {
        final DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        final byte[] array2 = new byte[20];
        final int n = (this.random.nextInt() & 0x3FF) + 1024;
        this.random.nextBytes(array2);
        dataOutputStream.writeInt(this.version);
        dataOutputStream.writeInt(20);
        dataOutputStream.write(array2);
        dataOutputStream.writeInt(n);
        final HMac hMac = new HMac(new SHA1Digest());
        final MacOutputStream macOutputStream = new MacOutputStream(hMac);
        final PKCS12ParametersGenerator pkcs12ParametersGenerator = new PKCS12ParametersGenerator(new SHA1Digest());
        final byte[] pkcs12PasswordToBytes = PBEParametersGenerator.PKCS12PasswordToBytes(array);
        pkcs12ParametersGenerator.init(pkcs12PasswordToBytes, array2, n);
        int macSize;
        if (this.version < 2) {
            macSize = hMac.getMacSize();
        }
        else {
            macSize = hMac.getMacSize() * 8;
        }
        hMac.init(pkcs12ParametersGenerator.generateDerivedMacParameters(macSize));
        for (int i = 0; i != pkcs12PasswordToBytes.length; ++i) {
            pkcs12PasswordToBytes[i] = 0;
        }
        this.saveStore(new TeeOutputStream(dataOutputStream, macOutputStream));
        final byte[] array3 = new byte[hMac.getMacSize()];
        hMac.doFinal(array3, 0);
        dataOutputStream.write(array3);
        dataOutputStream.close();
    }
    
    protected void loadStore(final InputStream inputStream) throws IOException {
        final DataInputStream dataInputStream = new DataInputStream(inputStream);
        for (int i = dataInputStream.read(); i > 0; i = dataInputStream.read()) {
            final String utf = dataInputStream.readUTF();
            final Date date = new Date(dataInputStream.readLong());
            final int int1 = dataInputStream.readInt();
            Certificate[] array = null;
            if (int1 != 0) {
                final Certificate[] array2 = new Certificate[int1];
                int n = 0;
                while (true) {
                    array = array2;
                    if (n == int1) {
                        break;
                    }
                    array2[n] = this.decodeCertificate(dataInputStream);
                    ++n;
                }
            }
            Hashtable table2;
            StoreEntry storeEntry2;
            if (i != 1) {
                if (i != 2) {
                    if (i != 3 && i != 4) {
                        throw new RuntimeException("Unknown object type in store.");
                    }
                    final byte[] array3 = new byte[dataInputStream.readInt()];
                    dataInputStream.readFully(array3);
                    this.table.put(utf, new StoreEntry(utf, date, i, array3, array));
                    continue;
                }
                else {
                    final Key decodeKey = this.decodeKey(dataInputStream);
                    final Hashtable table = this.table;
                    final StoreEntry storeEntry = new StoreEntry(utf, date, 2, decodeKey, array);
                    table2 = table;
                    storeEntry2 = storeEntry;
                }
            }
            else {
                final Certificate decodeCertificate = this.decodeCertificate(dataInputStream);
                table2 = this.table;
                storeEntry2 = new StoreEntry(utf, date, 1, decodeCertificate);
            }
            table2.put(utf, storeEntry2);
        }
    }
    
    protected Cipher makePBECipher(final String s, final int n, final char[] array, final byte[] array2, final int n2) throws IOException {
        try {
            final PBEKeySpec pbeKeySpec = new PBEKeySpec(array);
            final SecretKeyFactory secretKeyFactory = this.helper.createSecretKeyFactory(s);
            final PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(array2, n2);
            final Cipher cipher = this.helper.createCipher(s);
            cipher.init(n, secretKeyFactory.generateSecret(pbeKeySpec), pbeParameterSpec);
            return cipher;
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Error initialising store of key store: ");
            sb.append(ex);
            throw new IOException(sb.toString());
        }
    }
    
    protected void saveStore(final OutputStream outputStream) throws IOException {
        final Enumeration elements = this.table.elements();
        final DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        while (true) {
            final boolean hasMoreElements = elements.hasMoreElements();
            int i = 0;
            if (!hasMoreElements) {
                dataOutputStream.write(0);
                return;
            }
            final StoreEntry storeEntry = elements.nextElement();
            dataOutputStream.write(storeEntry.getType());
            dataOutputStream.writeUTF(storeEntry.getAlias());
            dataOutputStream.writeLong(storeEntry.getDate().getTime());
            final Certificate[] certificateChain = storeEntry.getCertificateChain();
            if (certificateChain == null) {
                dataOutputStream.writeInt(0);
            }
            else {
                dataOutputStream.writeInt(certificateChain.length);
                while (i != certificateChain.length) {
                    this.encodeCertificate(certificateChain[i], dataOutputStream);
                    ++i;
                }
            }
            final int type = storeEntry.getType();
            if (type != 1) {
                if (type != 2) {
                    if (type != 3 && type != 4) {
                        throw new RuntimeException("Unknown object type in store.");
                    }
                    final byte[] array = (byte[])storeEntry.getObject();
                    dataOutputStream.writeInt(array.length);
                    dataOutputStream.write(array);
                }
                else {
                    this.encodeKey((Key)storeEntry.getObject(), dataOutputStream);
                }
            }
            else {
                this.encodeCertificate((Certificate)storeEntry.getObject(), dataOutputStream);
            }
        }
    }
    
    @Override
    public void setRandom(final SecureRandom random) {
        this.random = random;
    }
    
    public static class BouncyCastleStore extends BcKeyStoreSpi
    {
        public BouncyCastleStore() {
            super(1);
        }
        
        @Override
        public void engineLoad(final InputStream inputStream, final char[] array) throws IOException {
            this.table.clear();
            if (inputStream == null) {
                return;
            }
            final DataInputStream dataInputStream = new DataInputStream(inputStream);
            final int int1 = dataInputStream.readInt();
            if (int1 != 2 && int1 != 0 && int1 != 1) {
                throw new IOException("Wrong version of key store.");
            }
            final int int2 = dataInputStream.readInt();
            final byte[] array2 = new byte[int2];
            if (int2 != 20) {
                throw new IOException("Key store corrupted.");
            }
            dataInputStream.readFully(array2);
            final int int3 = dataInputStream.readInt();
            if (int3 < 0 || int3 > 65536) {
                throw new IOException("Key store corrupted.");
            }
            String s;
            if (int1 == 0) {
                s = "OldPBEWithSHAAndTwofish-CBC";
            }
            else {
                s = "PBEWithSHAAndTwofish-CBC";
            }
            final CipherInputStream cipherInputStream = new CipherInputStream(dataInputStream, this.makePBECipher(s, 2, array, array2, int3));
            final SHA1Digest sha1Digest = new SHA1Digest();
            this.loadStore(new DigestInputStream(cipherInputStream, sha1Digest));
            final byte[] array3 = new byte[sha1Digest.getDigestSize()];
            sha1Digest.doFinal(array3, 0);
            final byte[] array4 = new byte[sha1Digest.getDigestSize()];
            Streams.readFully(cipherInputStream, array4);
            if (Arrays.constantTimeAreEqual(array3, array4)) {
                return;
            }
            this.table.clear();
            throw new IOException("KeyStore integrity check failed.");
        }
        
        @Override
        public void engineStore(final OutputStream outputStream, final char[] array) throws IOException {
            final DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            final byte[] array2 = new byte[20];
            final int n = (this.random.nextInt() & 0x3FF) + 1024;
            this.random.nextBytes(array2);
            dataOutputStream.writeInt(this.version);
            dataOutputStream.writeInt(20);
            dataOutputStream.write(array2);
            dataOutputStream.writeInt(n);
            final CipherOutputStream cipherOutputStream = new CipherOutputStream(dataOutputStream, this.makePBECipher("PBEWithSHAAndTwofish-CBC", 1, array, array2, n));
            final DigestOutputStream digestOutputStream = new DigestOutputStream(new SHA1Digest());
            this.saveStore(new TeeOutputStream(cipherOutputStream, digestOutputStream));
            cipherOutputStream.write(digestOutputStream.getDigest());
            cipherOutputStream.close();
        }
    }
    
    public static class Std extends BcKeyStoreSpi
    {
        public Std() {
            super(2);
        }
    }
    
    private class StoreEntry
    {
        String alias;
        Certificate[] certChain;
        Date date;
        Object obj;
        int type;
        
        StoreEntry(final String alias, final Key key, final char[] array, final Certificate[] certChain) throws Exception {
            this.date = new Date();
            this.type = 4;
            this.alias = alias;
            this.certChain = certChain;
            final byte[] array2 = new byte[20];
            BcKeyStoreSpi.this.random.setSeed(System.currentTimeMillis());
            BcKeyStoreSpi.this.random.nextBytes(array2);
            final int n = (BcKeyStoreSpi.this.random.nextInt() & 0x3FF) + 1024;
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            dataOutputStream.writeInt(20);
            dataOutputStream.write(array2);
            dataOutputStream.writeInt(n);
            final DataOutputStream dataOutputStream2 = new DataOutputStream(new CipherOutputStream(dataOutputStream, BcKeyStoreSpi.this.makePBECipher("PBEWithSHAAnd3-KeyTripleDES-CBC", 1, array, array2, n)));
            BcKeyStoreSpi.this.encodeKey(key, dataOutputStream2);
            dataOutputStream2.close();
            this.obj = byteArrayOutputStream.toByteArray();
        }
        
        StoreEntry(final String alias, final Certificate obj) {
            this.date = new Date();
            this.type = 1;
            this.alias = alias;
            this.obj = obj;
            this.certChain = null;
        }
        
        StoreEntry(final String alias, final Date date, final int type, final Object obj) {
            this.date = new Date();
            this.alias = alias;
            this.date = date;
            this.type = type;
            this.obj = obj;
        }
        
        StoreEntry(final String alias, final Date date, final int type, final Object obj, final Certificate[] certChain) {
            this.date = new Date();
            this.alias = alias;
            this.date = date;
            this.type = type;
            this.obj = obj;
            this.certChain = certChain;
        }
        
        StoreEntry(final String alias, final byte[] obj, final Certificate[] certChain) {
            this.date = new Date();
            this.type = 3;
            this.alias = alias;
            this.obj = obj;
            this.certChain = certChain;
        }
        
        String getAlias() {
            return this.alias;
        }
        
        Certificate[] getCertificateChain() {
            return this.certChain;
        }
        
        Date getDate() {
            return this.date;
        }
        
        Object getObject() {
            return this.obj;
        }
        
        Object getObject(final char[] array) throws NoSuchAlgorithmException, UnrecoverableKeyException {
            if (array == null || array.length == 0) {
                final Object obj = this.obj;
                if (obj instanceof Key) {
                    return obj;
                }
            }
            if (this.type == 4) {
                final DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream((byte[])this.obj));
                try {
                    final byte[] array2 = new byte[dataInputStream.readInt()];
                    dataInputStream.readFully(array2);
                    final CipherInputStream cipherInputStream = new CipherInputStream(dataInputStream, BcKeyStoreSpi.this.makePBECipher("PBEWithSHAAnd3-KeyTripleDES-CBC", 2, array, array2, dataInputStream.readInt()));
                    try {
                        return BcKeyStoreSpi.this.decodeKey(new DataInputStream(cipherInputStream));
                    }
                    catch (Exception ex) {
                        final DataInputStream dataInputStream2 = new DataInputStream(new ByteArrayInputStream((byte[])this.obj));
                        byte[] array3 = new byte[dataInputStream2.readInt()];
                        dataInputStream2.readFully(array3);
                        int n = dataInputStream2.readInt();
                        final CipherInputStream cipherInputStream2 = new CipherInputStream(dataInputStream2, BcKeyStoreSpi.this.makePBECipher("BrokenPBEWithSHAAnd3-KeyTripleDES-CBC", 2, array, array3, n));
                        Key key;
                        try {
                            key = BcKeyStoreSpi.this.decodeKey(new DataInputStream(cipherInputStream2));
                        }
                        catch (Exception ex2) {
                            final DataInputStream dataInputStream3 = new DataInputStream(new ByteArrayInputStream((byte[])this.obj));
                            array3 = new byte[dataInputStream3.readInt()];
                            dataInputStream3.readFully(array3);
                            n = dataInputStream3.readInt();
                            key = BcKeyStoreSpi.this.decodeKey(new DataInputStream(new CipherInputStream(dataInputStream3, BcKeyStoreSpi.this.makePBECipher("OldPBEWithSHAAnd3-KeyTripleDES-CBC", 2, array, array3, n))));
                        }
                        if (key != null) {
                            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
                            dataOutputStream.writeInt(array3.length);
                            dataOutputStream.write(array3);
                            dataOutputStream.writeInt(n);
                            final DataOutputStream dataOutputStream2 = new DataOutputStream(new CipherOutputStream(dataOutputStream, BcKeyStoreSpi.this.makePBECipher("PBEWithSHAAnd3-KeyTripleDES-CBC", 1, array, array3, n)));
                            BcKeyStoreSpi.this.encodeKey(key, dataOutputStream2);
                            dataOutputStream2.close();
                            this.obj = byteArrayOutputStream.toByteArray();
                            return key;
                        }
                        throw new UnrecoverableKeyException("no match");
                    }
                }
                catch (Exception ex3) {
                    throw new UnrecoverableKeyException("no match");
                }
            }
            throw new RuntimeException("forget something!");
        }
        
        int getType() {
            return this.type;
        }
    }
    
    public static class Version1 extends BcKeyStoreSpi
    {
        public Version1() {
            super(1);
        }
    }
}
