package net.lingala.zip4j.crypto;

import net.lingala.zip4j.crypto.engine.*;
import net.lingala.zip4j.exception.*;
import net.lingala.zip4j.crypto.PBKDF2.*;
import java.util.*;
import net.lingala.zip4j.model.*;
import net.lingala.zip4j.util.*;

public class AESDecrypter implements IDecrypter
{
    private int KEY_LENGTH;
    private int MAC_LENGTH;
    private final int PASSWORD_VERIFIER_LENGTH;
    private int SALT_LENGTH;
    private AESEngine aesEngine;
    private byte[] aesKey;
    private byte[] counterBlock;
    private byte[] derivedPasswordVerifier;
    private byte[] iv;
    private LocalFileHeader localFileHeader;
    private int loopCount;
    private MacBasedPRF mac;
    private byte[] macKey;
    private int nonce;
    private byte[] storedMac;
    
    public AESDecrypter(final LocalFileHeader localFileHeader, final byte[] array, final byte[] array2) throws ZipException {
        this.PASSWORD_VERIFIER_LENGTH = 2;
        this.nonce = 1;
        this.loopCount = 0;
        if (localFileHeader == null) {
            throw new ZipException("one of the input parameters is null in AESDecryptor Constructor");
        }
        this.localFileHeader = localFileHeader;
        this.storedMac = null;
        this.iv = new byte[16];
        this.counterBlock = new byte[16];
        this.init(array, array2);
    }
    
    private byte[] deriveKey(byte[] deriveKey, final char[] array) throws ZipException {
        try {
            deriveKey = new PBKDF2Engine(new PBKDF2Parameters("HmacSHA1", "ISO-8859-1", deriveKey, 1000)).deriveKey(array, this.KEY_LENGTH + this.MAC_LENGTH + 2);
            return deriveKey;
        }
        catch (Exception ex) {
            throw new ZipException(ex);
        }
    }
    
    private void init(byte[] deriveKey, final byte[] array) throws ZipException {
        if (this.localFileHeader == null) {
            throw new ZipException("invalid file header in init method of AESDecryptor");
        }
        final AESExtraDataRecord aesExtraDataRecord = this.localFileHeader.getAesExtraDataRecord();
        if (aesExtraDataRecord == null) {
            throw new ZipException("invalid aes extra data record - in init method of AESDecryptor");
        }
        switch (aesExtraDataRecord.getAesStrength()) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("invalid aes key strength for file: ");
                sb.append(this.localFileHeader.getFileName());
                throw new ZipException(sb.toString());
            }
            case 3: {
                this.KEY_LENGTH = 32;
                this.MAC_LENGTH = 32;
                this.SALT_LENGTH = 16;
                break;
            }
            case 2: {
                this.KEY_LENGTH = 24;
                this.MAC_LENGTH = 24;
                this.SALT_LENGTH = 12;
                break;
            }
            case 1: {
                this.KEY_LENGTH = 16;
                this.MAC_LENGTH = 16;
                this.SALT_LENGTH = 8;
                break;
            }
        }
        if (this.localFileHeader.getPassword() == null || this.localFileHeader.getPassword().length <= 0) {
            throw new ZipException("empty or null password provided for AES Decryptor");
        }
        deriveKey = this.deriveKey(deriveKey, this.localFileHeader.getPassword());
        if (deriveKey == null || deriveKey.length != this.KEY_LENGTH + this.MAC_LENGTH + 2) {
            throw new ZipException("invalid derived key");
        }
        this.aesKey = new byte[this.KEY_LENGTH];
        this.macKey = new byte[this.MAC_LENGTH];
        this.derivedPasswordVerifier = new byte[2];
        System.arraycopy(deriveKey, 0, this.aesKey, 0, this.KEY_LENGTH);
        System.arraycopy(deriveKey, this.KEY_LENGTH, this.macKey, 0, this.MAC_LENGTH);
        System.arraycopy(deriveKey, this.KEY_LENGTH + this.MAC_LENGTH, this.derivedPasswordVerifier, 0, 2);
        if (this.derivedPasswordVerifier == null) {
            throw new ZipException("invalid derived password verifier for AES");
        }
        if (!Arrays.equals(array, this.derivedPasswordVerifier)) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Wrong Password for file: ");
            sb2.append(this.localFileHeader.getFileName());
            throw new ZipException(sb2.toString(), 5);
        }
        this.aesEngine = new AESEngine(this.aesKey);
        (this.mac = new MacBasedPRF("HmacSHA1")).init(this.macKey);
    }
    
    @Override
    public int decryptData(final byte[] array) throws ZipException {
        return this.decryptData(array, 0, array.length);
    }
    
    @Override
    public int decryptData(final byte[] array, final int n, final int n2) throws ZipException {
        if (this.aesEngine == null) {
            throw new ZipException("AES not initialized properly");
        }
        int i = n;
        while (i < n + n2) {
            int loopCount;
            if (i + 16 <= n + n2) {
                loopCount = 16;
            }
            else {
                loopCount = n + n2 - i;
            }
            try {
                this.loopCount = loopCount;
                this.mac.update(array, i, this.loopCount);
                Raw.prepareBuffAESIVBytes(this.iv, this.nonce, 16);
                this.aesEngine.processBlock(this.iv, this.counterBlock);
                for (int j = 0; j < this.loopCount; ++j) {
                    array[i + j] ^= this.counterBlock[j];
                }
                ++this.nonce;
                i += 16;
                continue;
            }
            catch (Exception ex) {
                throw new ZipException(ex);
            }
            catch (ZipException ex2) {
                throw ex2;
            }
            break;
        }
        return n2;
    }
    
    public byte[] getCalculatedAuthenticationBytes() {
        return this.mac.doFinal();
    }
    
    public int getPasswordVerifierLength() {
        return 2;
    }
    
    public int getSaltLength() {
        return this.SALT_LENGTH;
    }
    
    public byte[] getStoredMac() {
        return this.storedMac;
    }
    
    public void setStoredMac(final byte[] storedMac) {
        this.storedMac = storedMac;
    }
}
