package org.apache.james.mime4j.storage;

import javax.crypto.spec.*;
import java.security.*;
import javax.crypto.*;
import java.io.*;

public class CipherStorageProvider extends AbstractStorageProvider
{
    private final String algorithm;
    private final StorageProvider backend;
    private final KeyGenerator keygen;
    
    public CipherStorageProvider(final StorageProvider storageProvider) {
        this(storageProvider, "Blowfish");
    }
    
    public CipherStorageProvider(final StorageProvider backend, final String algorithm) {
        if (backend != null) {
            try {
                this.backend = backend;
                this.algorithm = algorithm;
                this.keygen = KeyGenerator.getInstance(algorithm);
                return;
            }
            catch (NoSuchAlgorithmException ex) {
                throw new IllegalArgumentException(ex);
            }
        }
        throw new IllegalArgumentException();
    }
    
    private SecretKeySpec getSecretKeySpec() {
        return new SecretKeySpec(this.keygen.generateKey().getEncoded(), this.algorithm);
    }
    
    @Override
    public StorageOutputStream createStorageOutputStream() throws IOException {
        return new CipherStorageOutputStream(this.backend.createStorageOutputStream(), this.algorithm, this.getSecretKeySpec());
    }
    
    private static final class CipherStorage implements Storage
    {
        private final String algorithm;
        private Storage encrypted;
        private final SecretKeySpec skeySpec;
        
        public CipherStorage(final Storage encrypted, final String algorithm, final SecretKeySpec skeySpec) {
            this.encrypted = encrypted;
            this.algorithm = algorithm;
            this.skeySpec = skeySpec;
        }
        
        @Override
        public void delete() {
            final Storage encrypted = this.encrypted;
            if (encrypted != null) {
                encrypted.delete();
                this.encrypted = null;
            }
        }
        
        @Override
        public InputStream getInputStream() throws IOException {
            if (this.encrypted != null) {
                try {
                    final Cipher instance = Cipher.getInstance(this.algorithm);
                    instance.init(2, this.skeySpec);
                    return new CipherInputStream(this.encrypted.getInputStream(), instance);
                }
                catch (GeneralSecurityException ex) {
                    throw (IOException)new IOException().initCause(ex);
                }
            }
            throw new IllegalStateException("storage has been deleted");
        }
    }
    
    private static final class CipherStorageOutputStream extends StorageOutputStream
    {
        private final String algorithm;
        private final CipherOutputStream cipherOut;
        private final SecretKeySpec skeySpec;
        private final StorageOutputStream storageOut;
        
        public CipherStorageOutputStream(final StorageOutputStream storageOut, final String algorithm, final SecretKeySpec skeySpec) throws IOException {
            try {
                this.storageOut = storageOut;
                this.algorithm = algorithm;
                this.skeySpec = skeySpec;
                final Cipher instance = Cipher.getInstance(algorithm);
                instance.init(1, skeySpec);
                this.cipherOut = new CipherOutputStream(storageOut, instance);
            }
            catch (GeneralSecurityException ex) {
                throw (IOException)new IOException().initCause(ex);
            }
        }
        
        @Override
        public void close() throws IOException {
            super.close();
            this.cipherOut.close();
        }
        
        @Override
        protected Storage toStorage0() throws IOException {
            return new CipherStorage(this.storageOut.toStorage(), this.algorithm, this.skeySpec);
        }
        
        @Override
        protected void write0(final byte[] array, final int n, final int n2) throws IOException {
            this.cipherOut.write(array, n, n2);
        }
    }
}
