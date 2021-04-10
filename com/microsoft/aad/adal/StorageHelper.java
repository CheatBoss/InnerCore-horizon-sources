package com.microsoft.aad.adal;

import android.content.*;
import java.security.spec.*;
import android.security.*;
import java.util.*;
import javax.security.auth.x500.*;
import java.math.*;
import java.security.cert.*;
import java.security.*;
import java.io.*;
import android.util.*;
import javax.crypto.*;
import javax.crypto.spec.*;

public class StorageHelper
{
    private static final String ADALKS = "adalks";
    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    public static final int DATA_KEY_LENGTH = 16;
    private static final String ENCODE_VERSION = "E1";
    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final String HMAC_KEY_HASH_ALGORITHM = "SHA256";
    public static final int HMAC_LENGTH = 32;
    private static final String KEYSPEC_ALGORITHM = "AES";
    private static final int KEY_FILE_SIZE = 1024;
    private static final int KEY_SIZE = 256;
    private static final String KEY_STORE_CERT_ALIAS = "AdalKey";
    private static final int KEY_VERSION_BLOB_LENGTH = 4;
    private static final String TAG = "StorageHelper";
    public static final String VERSION_ANDROID_KEY_STORE = "A001";
    public static final String VERSION_USER_DEFINED = "U001";
    private static final String WRAP_ALGORITHM = "RSA/ECB/PKCS1Padding";
    private String mBlobVersion;
    private final Context mContext;
    private SecretKey mHMACKey;
    private SecretKey mKey;
    private KeyPair mKeyPair;
    private final SecureRandom mRandom;
    private SecretKey mSecretKeyFromAndroidKeyStore;
    
    public StorageHelper(final Context mContext) {
        this.mKey = null;
        this.mHMACKey = null;
        this.mSecretKeyFromAndroidKeyStore = null;
        this.mContext = mContext;
        this.mRandom = new SecureRandom();
    }
    
    private void assertHMac(final byte[] array, final int n, final int n2, final byte[] array2) throws DigestException {
        if (array2.length != n2 - n) {
            throw new IllegalArgumentException("Unexpected HMAC length");
        }
        int i = n;
        byte b = 0;
        while (i < n2) {
            b |= (byte)(array2[i - n] ^ array[i]);
            ++i;
        }
        if (b == 0) {
            return;
        }
        throw new DigestException();
    }
    
    private void deleteKeyFile() {
        final Context mContext = this.mContext;
        final File file = new File(mContext.getDir(mContext.getPackageName(), 0), "adalks");
        if (file.exists()) {
            Logger.v("StorageHelper:deleteKeyFile", "Delete KeyFile");
            if (!file.delete()) {
                Logger.v("StorageHelper:deleteKeyFile", "Delete KeyFile failed");
            }
        }
    }
    
    private boolean doesKeyPairExist() throws GeneralSecurityException, IOException {
        synchronized (this) {
            final KeyStore instance = KeyStore.getInstance("AndroidKeyStore");
            instance.load(null);
            try {
                return instance.containsAlias("AdalKey");
            }
            catch (NullPointerException ex) {
                throw new KeyStoreException(ex);
            }
        }
    }
    
    private KeyPair generateKeyPairFromAndroidKeyStore() throws GeneralSecurityException, IOException {
        synchronized (this) {
            KeyStore.getInstance("AndroidKeyStore").load(null);
            Logger.v("StorageHelper:generateKeyPairFromAndroidKeyStore", "Generate KeyPair from AndroidKeyStore");
            final Calendar instance = Calendar.getInstance();
            final Calendar instance2 = Calendar.getInstance();
            instance2.add(1, 100);
            final KeyPairGenerator instance3 = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
            instance3.initialize(this.getKeyPairGeneratorSpec(this.mContext, instance.getTime(), instance2.getTime()));
            try {
                return instance3.generateKeyPair();
            }
            catch (IllegalStateException ex) {
                throw new KeyStoreException(ex);
            }
        }
    }
    
    private SecretKey generateSecretKey() throws NoSuchAlgorithmException {
        final KeyGenerator instance = KeyGenerator.getInstance("AES");
        instance.init(256, this.mRandom);
        return instance.generateKey();
    }
    
    private char getEncodeVersionLengthPrefix() {
        return 99;
    }
    
    private SecretKey getHMacKey(SecretKey secretKey) throws NoSuchAlgorithmException {
        final byte[] encoded = secretKey.getEncoded();
        if (encoded != null) {
            secretKey = new SecretKeySpec(MessageDigest.getInstance("SHA256").digest(encoded), "AES");
        }
        return secretKey;
    }
    
    private SecretKey getKey(final String s) throws GeneralSecurityException, IOException {
        while (true) {
            while (true) {
                Label_0128: {
                    synchronized (this) {
                        final int hashCode = s.hashCode();
                        int n;
                        if (hashCode != 1984080) {
                            if (hashCode != 2579900) {
                                break Label_0128;
                            }
                            if (!s.equals("U001")) {
                                break Label_0128;
                            }
                            n = 0;
                        }
                        else {
                            if (!s.equals("A001")) {
                                break Label_0128;
                            }
                            n = 1;
                        }
                        if (n == 0) {
                            return this.getSecretKey(AuthenticationSettings.INSTANCE.getSecretKeyData());
                        }
                        if (n != 1) {
                            throw new IOException("Unknown keyVersion.");
                        }
                        if (this.mSecretKeyFromAndroidKeyStore != null) {
                            return this.mSecretKeyFromAndroidKeyStore;
                        }
                        this.mKeyPair = this.readKeyPair();
                        return this.mSecretKeyFromAndroidKeyStore = this.getUnwrappedSecretKey();
                    }
                }
                int n = -1;
                continue;
            }
        }
    }
    
    private SecretKey getKeyOrCreate(final String s) throws GeneralSecurityException, IOException {
        synchronized (this) {
            if ("U001".equals(s)) {
                return this.getSecretKey(AuthenticationSettings.INSTANCE.getSecretKeyData());
            }
            try {
                this.mSecretKeyFromAndroidKeyStore = this.getKey(s);
            }
            catch (IOException | GeneralSecurityException ex) {
                Logger.v("StorageHelper:getKeyOrCreate", "Key does not exist in AndroidKeyStore, try to generate new keys.");
            }
            if (this.mSecretKeyFromAndroidKeyStore == null) {
                this.mKeyPair = this.generateKeyPairFromAndroidKeyStore();
                final SecretKey generateSecretKey = this.generateSecretKey();
                this.mSecretKeyFromAndroidKeyStore = generateSecretKey;
                this.writeKeyData(this.wrap(generateSecretKey));
            }
            return this.mSecretKeyFromAndroidKeyStore;
        }
    }
    
    private AlgorithmParameterSpec getKeyPairGeneratorSpec(final Context context, final Date startDate, final Date endDate) {
        return (AlgorithmParameterSpec)new KeyPairGeneratorSpec$Builder(context).setAlias("AdalKey").setSubject(new X500Principal(String.format(Locale.ROOT, "CN=%s, OU=%s", "AdalKey", context.getPackageName()))).setSerialNumber(BigInteger.ONE).setStartDate(startDate).setEndDate(endDate).build();
    }
    
    private SecretKey getSecretKey(final byte[] array) {
        if (array != null) {
            return new SecretKeySpec(array, "AES");
        }
        throw new IllegalArgumentException("rawBytes");
    }
    
    private SecretKey getUnwrappedSecretKey() throws GeneralSecurityException, IOException {
        synchronized (this) {
            Logger.v("StorageHelper:getUnwrappedSecretKey", "Reading SecretKey");
            try {
                final SecretKey unwrap = this.unwrap(this.readKeyData());
                Logger.v("StorageHelper:getUnwrappedSecretKey", "Finished reading SecretKey");
                return unwrap;
            }
            catch (GeneralSecurityException | IOException ex3) {
                final IOException ex2;
                final IOException ex = ex2;
                Logger.e("StorageHelper:getUnwrappedSecretKey", "Unwrap failed for AndroidKeyStore", "", ADALError.ANDROIDKEYSTORE_FAILED, ex);
                this.mKeyPair = null;
                this.deleteKeyFile();
                this.resetKeyPairFromAndroidKeyStore();
                Logger.v("StorageHelper:getUnwrappedSecretKey", "Removed previous key pair info.");
                throw ex;
            }
        }
    }
    
    private byte[] readKeyData() throws IOException {
        final Context mContext = this.mContext;
        final File file = new File(mContext.getDir(mContext.getPackageName(), 0), "adalks");
        if (file.exists()) {
            Logger.v("StorageHelper", "Reading key data from a file");
            final FileInputStream fileInputStream = new FileInputStream(file);
            try {
                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                final byte[] array = new byte[1024];
                while (true) {
                    final int read = fileInputStream.read(array);
                    if (read == -1) {
                        break;
                    }
                    byteArrayOutputStream.write(array, 0, read);
                }
                return byteArrayOutputStream.toByteArray();
            }
            finally {
                fileInputStream.close();
            }
        }
        throw new IOException("Key file to read does not exist");
    }
    
    private KeyPair readKeyPair() throws GeneralSecurityException, IOException {
        synchronized (this) {
            if (this.doesKeyPairExist()) {
                Logger.v("StorageHelper:readKeyPair", "Reading Key entry");
                final KeyStore instance = KeyStore.getInstance("AndroidKeyStore");
                instance.load(null);
                try {
                    final KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)instance.getEntry("AdalKey", null);
                    return new KeyPair(privateKeyEntry.getCertificate().getPublicKey(), privateKeyEntry.getPrivateKey());
                }
                catch (RuntimeException ex) {
                    throw new KeyStoreException(ex);
                }
            }
            throw new KeyStoreException("KeyPair entry does not exist.");
        }
    }
    
    private void resetKeyPairFromAndroidKeyStore() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        synchronized (this) {
            final KeyStore instance = KeyStore.getInstance("AndroidKeyStore");
            instance.load(null);
            instance.deleteEntry("AdalKey");
        }
    }
    
    private SecretKey unwrap(final byte[] array) throws GeneralSecurityException {
        final Cipher instance = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        instance.init(4, this.mKeyPair.getPrivate());
        try {
            return (SecretKey)instance.unwrap(array, "AES", 3);
        }
        catch (IllegalArgumentException ex) {
            throw new KeyStoreException(ex);
        }
    }
    
    private byte[] wrap(final SecretKey secretKey) throws GeneralSecurityException {
        Logger.v("StorageHelper", "Wrap secret key.");
        final Cipher instance = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        instance.init(3, this.mKeyPair.getPublic());
        return instance.wrap(secretKey);
    }
    
    private void writeKeyData(final byte[] array) throws IOException {
        Logger.v("StorageHelper", "Writing key data to a file");
        final Context mContext = this.mContext;
        final FileOutputStream fileOutputStream = new FileOutputStream(new File(mContext.getDir(mContext.getPackageName(), 0), "adalks"));
        try {
            fileOutputStream.write(array);
        }
        finally {
            fileOutputStream.close();
        }
    }
    
    public String decrypt(String s) throws GeneralSecurityException, IOException {
        Logger.v("StorageHelper:decrypt", "Starting decryption");
        if (StringExtensions.isNullOrBlank(s)) {
            throw new IllegalArgumentException("Input is empty or null");
        }
        final int n = s.charAt(0) - 'a';
        if (n <= 0) {
            throw new IllegalArgumentException(String.format("Encode version length: '%s' is not valid, it must be greater of equal to 0", n));
        }
        final int n2 = n + 1;
        if (!s.substring(1, n2).equals("E1")) {
            throw new IllegalArgumentException(String.format("Encode version received was: '%s', Encode version supported is: '%s'", s, "E1"));
        }
        final byte[] decode = Base64.decode(s.substring(n2), 0);
        final String s2 = new String(decode, 0, 4, "UTF_8");
        final StringBuilder sb = new StringBuilder();
        sb.append("Encrypt version:");
        sb.append(s2);
        Logger.i("StorageHelper:decrypt", "", sb.toString());
        final SecretKey key = this.getKey(s2);
        final SecretKey hMacKey = this.getHMacKey(key);
        final int n3 = decode.length - 16 - 32;
        final int n4 = decode.length - 32;
        final int n5 = n3 - 4;
        if (n3 >= 0 && n4 >= 0 && n5 >= 0) {
            final Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
            final Mac instance2 = Mac.getInstance("HmacSHA256");
            instance2.init(hMacKey);
            instance2.update(decode, 0, n4);
            this.assertHMac(decode, n4, decode.length, instance2.doFinal());
            instance.init(2, key, new IvParameterSpec(decode, n3, 16));
            s = new String(instance.doFinal(decode, 4, n5), "UTF_8");
            Logger.v("StorageHelper:decrypt", "Finished decryption");
            return s;
        }
        throw new IOException("Invalid byte array input for decryption.");
    }
    
    public String encrypt(String s) throws GeneralSecurityException, IOException {
        Logger.v("StorageHelper:encrypt", "Starting encryption");
        if (!StringExtensions.isNullOrBlank(s)) {
            final SecretKey loadSecretKeyForEncryption = this.loadSecretKeyForEncryption();
            this.mKey = loadSecretKeyForEncryption;
            this.mHMACKey = this.getHMacKey(loadSecretKeyForEncryption);
            final StringBuilder sb = new StringBuilder();
            sb.append("Encrypt version:");
            sb.append(this.mBlobVersion);
            Logger.i("StorageHelper:encrypt", "", sb.toString());
            final byte[] bytes = this.mBlobVersion.getBytes("UTF_8");
            final byte[] bytes2 = s.getBytes("UTF_8");
            final byte[] array = new byte[16];
            this.mRandom.nextBytes(array);
            final IvParameterSpec ivParameterSpec = new IvParameterSpec(array);
            final Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
            final Mac instance2 = Mac.getInstance("HmacSHA256");
            instance.init(1, this.mKey, ivParameterSpec);
            final byte[] doFinal = instance.doFinal(bytes2);
            instance2.init(this.mHMACKey);
            instance2.update(bytes);
            instance2.update(doFinal);
            instance2.update(array);
            final byte[] doFinal2 = instance2.doFinal();
            final byte[] array2 = new byte[bytes.length + doFinal.length + 16 + doFinal2.length];
            System.arraycopy(bytes, 0, array2, 0, bytes.length);
            System.arraycopy(doFinal, 0, array2, bytes.length, doFinal.length);
            System.arraycopy(array, 0, array2, bytes.length + doFinal.length, 16);
            System.arraycopy(doFinal2, 0, array2, bytes.length + doFinal.length + 16, doFinal2.length);
            s = new String(Base64.encode(array2, 2), "UTF_8");
            Logger.v("StorageHelper:encrypt", "Finished encryption");
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(this.getEncodeVersionLengthPrefix());
            sb2.append("E1");
            sb2.append(s);
            return sb2.toString();
        }
        throw new IllegalArgumentException("Input is empty or null");
    }
    
    SecretKey loadSecretKeyForEncryption() throws IOException, GeneralSecurityException {
        synchronized (this) {
            String s;
            if (AuthenticationSettings.INSTANCE.getSecretKeyData() == null) {
                s = "A001";
            }
            else {
                s = "U001";
            }
            return this.loadSecretKeyForEncryption(s);
        }
    }
    
    SecretKey loadSecretKeyForEncryption(final String mBlobVersion) throws IOException, GeneralSecurityException {
        synchronized (this) {
            if (this.mKey != null && this.mHMACKey != null) {
                return this.mKey;
            }
            this.mBlobVersion = mBlobVersion;
            return this.getKeyOrCreate(mBlobVersion);
        }
    }
}
