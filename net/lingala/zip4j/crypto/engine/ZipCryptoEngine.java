package net.lingala.zip4j.crypto.engine;

public class ZipCryptoEngine
{
    private static final int[] CRC_TABLE;
    private final int[] keys;
    
    static {
        CRC_TABLE = new int[256];
        for (int i = 0; i < 256; ++i) {
            int n = i;
            for (int j = 0; j < 8; ++j) {
                if ((n & 0x1) == 0x1) {
                    n = (n >>> 1 ^ 0xEDB88320);
                }
                else {
                    n >>>= 1;
                }
            }
            ZipCryptoEngine.CRC_TABLE[i] = n;
        }
    }
    
    public ZipCryptoEngine() {
        this.keys = new int[3];
    }
    
    private int crc32(final int n, final byte b) {
        return n >>> 8 ^ ZipCryptoEngine.CRC_TABLE[(n ^ b) & 0xFF];
    }
    
    public byte decryptByte() {
        final int n = this.keys[2] | 0x2;
        return (byte)((n ^ 0x1) * n >>> 8);
    }
    
    public void initKeys(final char[] array) {
        final int[] keys = this.keys;
        int i = 0;
        keys[0] = 305419896;
        this.keys[1] = 591751049;
        this.keys[2] = 878082192;
        while (i < array.length) {
            this.updateKeys((byte)(array[i] & '\u00ff'));
            ++i;
        }
    }
    
    public void updateKeys(final byte b) {
        this.keys[0] = this.crc32(this.keys[0], b);
        final int[] keys = this.keys;
        keys[1] += (this.keys[0] & 0xFF);
        this.keys[1] = this.keys[1] * 134775813 + 1;
        this.keys[2] = this.crc32(this.keys[2], (byte)(this.keys[1] >> 24));
    }
}
