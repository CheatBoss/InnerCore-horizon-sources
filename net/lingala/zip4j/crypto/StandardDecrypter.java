package net.lingala.zip4j.crypto;

import net.lingala.zip4j.model.*;
import net.lingala.zip4j.crypto.engine.*;
import net.lingala.zip4j.exception.*;

public class StandardDecrypter implements IDecrypter
{
    private byte[] crc;
    private FileHeader fileHeader;
    private ZipCryptoEngine zipCryptoEngine;
    
    public StandardDecrypter(final FileHeader fileHeader, final byte[] array) throws ZipException {
        this.crc = new byte[4];
        if (fileHeader == null) {
            throw new ZipException("one of more of the input parameters were null in StandardDecryptor");
        }
        this.fileHeader = fileHeader;
        this.zipCryptoEngine = new ZipCryptoEngine();
        this.init(array);
    }
    
    @Override
    public int decryptData(final byte[] array) throws ZipException {
        return this.decryptData(array, 0, array.length);
    }
    
    @Override
    public int decryptData(final byte[] array, int i, final int n) throws ZipException {
        if (i >= 0 && n >= 0) {
            i = 0;
            while (i < n) {
                final byte b = array[i];
                try {
                    final int n2 = (this.zipCryptoEngine.decryptByte() ^ (b & 0xFF)) & 0xFF;
                    this.zipCryptoEngine.updateKeys((byte)n2);
                    array[i] = (byte)n2;
                    ++i;
                    continue;
                }
                catch (Exception ex) {
                    throw new ZipException(ex);
                }
                break;
            }
            return n;
        }
        throw new ZipException("one of the input parameters were null in standard decrpyt data");
    }
    
    public void init(final byte[] array) throws ZipException {
        final byte[] crcBuff = this.fileHeader.getCrcBuff();
        this.crc[3] = (byte)(crcBuff[3] & 0xFF);
        this.crc[2] = (byte)(crcBuff[3] >> 8 & 0xFF);
        this.crc[1] = (byte)(crcBuff[3] >> 16 & 0xFF);
        final byte[] crc = this.crc;
        final byte b = (byte)(crcBuff[3] >> 24 & 0xFF);
        int n = 0;
        crc[0] = b;
        if (this.crc[2] <= 0 && this.crc[1] <= 0 && this.crc[0] <= 0) {
            if (this.fileHeader.getPassword() != null) {
                if (this.fileHeader.getPassword().length > 0) {
                    this.zipCryptoEngine.initKeys(this.fileHeader.getPassword());
                    byte b2 = array[0];
                    while (true) {
                        if (n < 12) {
                            try {
                                this.zipCryptoEngine.updateKeys((byte)(this.zipCryptoEngine.decryptByte() ^ b2));
                                if (n + 1 != 12) {
                                    b2 = array[n + 1];
                                }
                                ++n;
                                continue;
                            }
                            catch (Exception ex) {
                                throw new ZipException(ex);
                            }
                            break;
                        }
                        return;
                    }
                }
            }
            throw new ZipException("Wrong password!", 5);
        }
        throw new IllegalStateException("Invalid CRC in File Header");
    }
}
