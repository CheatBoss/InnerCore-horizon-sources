package net.lingala.zip4j.crypto;

import net.lingala.zip4j.crypto.engine.*;
import net.lingala.zip4j.exception.*;
import java.util.*;

public class StandardEncrypter implements IEncrypter
{
    private byte[] headerBytes;
    private ZipCryptoEngine zipCryptoEngine;
    
    public StandardEncrypter(final char[] array, final int n) throws ZipException {
        if (array != null && array.length > 0) {
            this.zipCryptoEngine = new ZipCryptoEngine();
            this.headerBytes = new byte[12];
            this.init(array, n);
            return;
        }
        throw new ZipException("input password is null or empty in standard encrpyter constructor");
    }
    
    private void init(final char[] array, final int n) throws ZipException {
        if (array == null || array.length <= 0) {
            throw new ZipException("input password is null or empty, cannot initialize standard encrypter");
        }
        this.zipCryptoEngine.initKeys(array);
        this.headerBytes = this.generateRandomBytes(12);
        this.zipCryptoEngine.initKeys(array);
        this.headerBytes[11] = (byte)(n >>> 24);
        this.headerBytes[10] = (byte)(n >>> 16);
        if (this.headerBytes.length < 12) {
            throw new ZipException("invalid header bytes generated, cannot perform standard encryption");
        }
        this.encryptData(this.headerBytes);
    }
    
    protected byte encryptByte(final byte b) {
        final byte b2 = (byte)((this.zipCryptoEngine.decryptByte() & 0xFF) ^ b);
        this.zipCryptoEngine.updateKeys(b);
        return b2;
    }
    
    @Override
    public int encryptData(final byte[] array) throws ZipException {
        if (array == null) {
            throw new NullPointerException();
        }
        return this.encryptData(array, 0, array.length);
    }
    
    @Override
    public int encryptData(final byte[] array, final int n, final int n2) throws ZipException {
        if (n2 < 0) {
            throw new ZipException("invalid length specified to decrpyt data");
        }
        int i = n;
        while (i < n + n2) {
            try {
                array[i] = this.encryptByte(array[i]);
                ++i;
                continue;
            }
            catch (Exception ex) {
                throw new ZipException(ex);
            }
            break;
        }
        return n2;
    }
    
    protected byte[] generateRandomBytes(int i) throws ZipException {
        if (i <= 0) {
            throw new ZipException("size is either 0 or less than 0, cannot generate header for standard encryptor");
        }
        final byte[] array = new byte[i];
        final Random random = new Random();
        for (i = 0; i < array.length; ++i) {
            array[i] = this.encryptByte((byte)random.nextInt(256));
        }
        return array;
    }
    
    public byte[] getHeaderBytes() {
        return this.headerBytes;
    }
}
