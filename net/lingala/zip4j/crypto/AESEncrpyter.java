package net.lingala.zip4j.crypto;

import net.lingala.zip4j.crypto.engine.*;
import net.lingala.zip4j.exception.*;
import net.lingala.zip4j.crypto.PBKDF2.*;
import java.util.*;
import net.lingala.zip4j.util.*;

public class AESEncrpyter implements IEncrypter
{
    private int KEY_LENGTH;
    private int MAC_LENGTH;
    private final int PASSWORD_VERIFIER_LENGTH;
    private int SALT_LENGTH;
    private AESEngine aesEngine;
    private byte[] aesKey;
    private byte[] counterBlock;
    private byte[] derivedPasswordVerifier;
    private boolean finished;
    private byte[] iv;
    private int keyStrength;
    private int loopCount;
    private MacBasedPRF mac;
    private byte[] macKey;
    private int nonce;
    private char[] password;
    private byte[] saltBytes;
    
    public AESEncrpyter(final char[] password, final int keyStrength) throws ZipException {
        this.PASSWORD_VERIFIER_LENGTH = 2;
        this.nonce = 1;
        this.loopCount = 0;
        if (password == null || password.length == 0) {
            throw new ZipException("input password is empty or null in AES encrypter constructor");
        }
        if (keyStrength != 1 && keyStrength != 3) {
            throw new ZipException("Invalid key strength in AES encrypter constructor");
        }
        this.password = password;
        this.keyStrength = keyStrength;
        this.finished = false;
        this.counterBlock = new byte[16];
        this.iv = new byte[16];
        this.init();
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
    
    private static byte[] generateSalt(int i) throws ZipException {
        if (i != 8 && i != 16) {
            throw new ZipException("invalid salt size, cannot generate salt");
        }
        int n = 0;
        if (i == 8) {
            n = 2;
        }
        if (i == 16) {
            n = 4;
        }
        final byte[] array = new byte[i];
        int nextInt;
        for (i = 0; i < n; ++i) {
            nextInt = new Random().nextInt();
            array[i * 4 + 0] = (byte)(nextInt >> 24);
            array[i * 4 + 1] = (byte)(nextInt >> 16);
            array[i * 4 + 2] = (byte)(nextInt >> 8);
            array[i * 4 + 3] = (byte)nextInt;
        }
        return array;
    }
    
    private void init() throws ZipException {
        final int keyStrength = this.keyStrength;
        if (keyStrength != 1) {
            if (keyStrength != 3) {
                throw new ZipException("invalid aes key strength, cannot determine key sizes");
            }
            this.KEY_LENGTH = 32;
            this.MAC_LENGTH = 32;
            this.SALT_LENGTH = 16;
        }
        else {
            this.KEY_LENGTH = 16;
            this.MAC_LENGTH = 16;
            this.SALT_LENGTH = 8;
        }
        this.saltBytes = generateSalt(this.SALT_LENGTH);
        final byte[] deriveKey = this.deriveKey(this.saltBytes, this.password);
        if (deriveKey != null && deriveKey.length == this.KEY_LENGTH + this.MAC_LENGTH + 2) {
            this.aesKey = new byte[this.KEY_LENGTH];
            this.macKey = new byte[this.MAC_LENGTH];
            this.derivedPasswordVerifier = new byte[2];
            System.arraycopy(deriveKey, 0, this.aesKey, 0, this.KEY_LENGTH);
            System.arraycopy(deriveKey, this.KEY_LENGTH, this.macKey, 0, this.MAC_LENGTH);
            System.arraycopy(deriveKey, this.KEY_LENGTH + this.MAC_LENGTH, this.derivedPasswordVerifier, 0, 2);
            this.aesEngine = new AESEngine(this.aesKey);
            (this.mac = new MacBasedPRF("HmacSHA1")).init(this.macKey);
            return;
        }
        throw new ZipException("invalid key generated, cannot decrypt file");
    }
    
    @Override
    public int encryptData(final byte[] array) throws ZipException {
        if (array == null) {
            throw new ZipException("input bytes are null, cannot perform AES encrpytion");
        }
        return this.encryptData(array, 0, array.length);
    }
    
    @Override
    public int encryptData(final byte[] array, final int n, final int n2) throws ZipException {
        if (this.finished) {
            throw new ZipException("AES Encrypter is in finished state (A non 16 byte block has already been passed to encrypter)");
        }
        if (n2 % 16 != 0) {
            this.finished = true;
        }
        for (int i = n; i < n + n2; i += 16) {
            int loopCount;
            if (i + 16 <= n + n2) {
                loopCount = 16;
            }
            else {
                loopCount = n + n2 - i;
            }
            this.loopCount = loopCount;
            Raw.prepareBuffAESIVBytes(this.iv, this.nonce, 16);
            this.aesEngine.processBlock(this.iv, this.counterBlock);
            for (int j = 0; j < this.loopCount; ++j) {
                array[i + j] ^= this.counterBlock[j];
            }
            this.mac.update(array, i, this.loopCount);
            ++this.nonce;
        }
        return n2;
    }
    
    public byte[] getDerivedPasswordVerifier() {
        return this.derivedPasswordVerifier;
    }
    
    public byte[] getFinalMac() {
        final byte[] doFinal = this.mac.doFinal();
        final byte[] array = new byte[10];
        System.arraycopy(doFinal, 0, array, 0, 10);
        return array;
    }
    
    public int getPasswordVeriifierLength() {
        return 2;
    }
    
    public byte[] getSaltBytes() {
        return this.saltBytes;
    }
    
    public int getSaltLength() {
        return this.SALT_LENGTH;
    }
    
    public void setDerivedPasswordVerifier(final byte[] derivedPasswordVerifier) {
        this.derivedPasswordVerifier = derivedPasswordVerifier;
    }
    
    public void setSaltBytes(final byte[] saltBytes) {
        this.saltBytes = saltBytes;
    }
}
