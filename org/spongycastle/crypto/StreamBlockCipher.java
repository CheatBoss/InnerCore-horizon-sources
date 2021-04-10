package org.spongycastle.crypto;

public abstract class StreamBlockCipher implements BlockCipher, StreamCipher
{
    private final BlockCipher cipher;
    
    protected StreamBlockCipher(final BlockCipher cipher) {
        this.cipher = cipher;
    }
    
    protected abstract byte calculateByte(final byte p0);
    
    public BlockCipher getUnderlyingCipher() {
        return this.cipher;
    }
    
    @Override
    public int processBytes(final byte[] array, int i, final int n, final byte[] array2, int n2) throws DataLengthException {
        final int n3 = i + n;
        if (n3 > array.length) {
            throw new DataLengthException("input buffer too small");
        }
        if (n2 + n <= array2.length) {
            while (i < n3) {
                array2[n2] = this.calculateByte(array[i]);
                ++n2;
                ++i;
            }
            return n;
        }
        throw new OutputLengthException("output buffer too short");
    }
    
    @Override
    public final byte returnByte(final byte b) {
        return this.calculateByte(b);
    }
}
