package org.spongycastle.crypto;

public class BufferedBlockCipher
{
    protected byte[] buf;
    protected int bufOff;
    protected BlockCipher cipher;
    protected boolean forEncryption;
    protected boolean partialBlockOkay;
    protected boolean pgpCFB;
    
    protected BufferedBlockCipher() {
    }
    
    public BufferedBlockCipher(final BlockCipher cipher) {
        this.cipher = cipher;
        this.buf = new byte[cipher.getBlockSize()];
        final boolean b = false;
        this.bufOff = 0;
        final String algorithmName = cipher.getAlgorithmName();
        final int n = algorithmName.indexOf(47) + 1;
        final boolean pgpCFB = n > 0 && algorithmName.startsWith("PGP", n);
        this.pgpCFB = pgpCFB;
        if (!pgpCFB && !(cipher instanceof StreamCipher)) {
            boolean partialBlockOkay = b;
            if (n > 0) {
                partialBlockOkay = b;
                if (algorithmName.startsWith("OpenPGP", n)) {
                    partialBlockOkay = true;
                }
            }
            this.partialBlockOkay = partialBlockOkay;
            return;
        }
        this.partialBlockOkay = true;
    }
    
    public int doFinal(final byte[] array, int n) throws DataLengthException, IllegalStateException, InvalidCipherTextException {
        try {
            if (this.bufOff + n <= array.length) {
                if (this.bufOff != 0) {
                    if (!this.partialBlockOkay) {
                        throw new DataLengthException("data not block size aligned");
                    }
                    this.cipher.processBlock(this.buf, 0, this.buf, 0);
                    final int bufOff = this.bufOff;
                    this.bufOff = 0;
                    System.arraycopy(this.buf, 0, array, n, bufOff);
                    n = bufOff;
                }
                else {
                    n = 0;
                }
                return n;
            }
            throw new OutputLengthException("output buffer too short for doFinal()");
        }
        finally {
            this.reset();
        }
    }
    
    public int getBlockSize() {
        return this.cipher.getBlockSize();
    }
    
    public int getOutputSize(final int n) {
        return n + this.bufOff;
    }
    
    public BlockCipher getUnderlyingCipher() {
        return this.cipher;
    }
    
    public int getUpdateOutputSize(int n) {
        final int n2 = n + this.bufOff;
        if (this.pgpCFB) {
            if (this.forEncryption) {
                n = n2 % this.buf.length - (this.cipher.getBlockSize() + 2);
                return n2 - n;
            }
            n = this.buf.length;
        }
        else {
            n = this.buf.length;
        }
        n = n2 % n;
        return n2 - n;
    }
    
    public void init(final boolean forEncryption, final CipherParameters cipherParameters) throws IllegalArgumentException {
        this.forEncryption = forEncryption;
        this.reset();
        this.cipher.init(forEncryption, cipherParameters);
    }
    
    public int processByte(final byte b, final byte[] array, int processBlock) throws DataLengthException, IllegalStateException {
        final byte[] buf = this.buf;
        final int bufOff = this.bufOff;
        final int bufOff2 = bufOff + 1;
        this.bufOff = bufOff2;
        buf[bufOff] = b;
        if (bufOff2 == buf.length) {
            processBlock = this.cipher.processBlock(buf, 0, array, processBlock);
            this.bufOff = 0;
            return processBlock;
        }
        return 0;
    }
    
    public int processBytes(byte[] buf, int n, int bufOff, final byte[] array, final int n2) throws DataLengthException, IllegalStateException {
        if (bufOff < 0) {
            throw new IllegalArgumentException("Can't have a negative input length!");
        }
        final int blockSize = this.getBlockSize();
        final int updateOutputSize = this.getUpdateOutputSize(bufOff);
        if (updateOutputSize > 0 && updateOutputSize + n2 > array.length) {
            throw new OutputLengthException("output buffer too short");
        }
        final byte[] buf2 = this.buf;
        final int length = buf2.length;
        final int bufOff2 = this.bufOff;
        final int n3 = length - bufOff2;
        int n5;
        int n6;
        int n7;
        if (bufOff > n3) {
            System.arraycopy(buf, n, buf2, bufOff2, n3);
            int n4 = this.cipher.processBlock(this.buf, 0, array, n2) + 0;
            this.bufOff = 0;
            bufOff -= n3;
            n += n3;
            while (true) {
                n5 = n4;
                n6 = n;
                n7 = bufOff;
                if (bufOff <= this.buf.length) {
                    break;
                }
                n4 += this.cipher.processBlock(buf, n, array, n2 + n4);
                bufOff -= blockSize;
                n += blockSize;
            }
        }
        else {
            n5 = 0;
            n7 = bufOff;
            n6 = n;
        }
        System.arraycopy(buf, n6, this.buf, this.bufOff, n7);
        bufOff = this.bufOff + n7;
        this.bufOff = bufOff;
        buf = this.buf;
        n = n5;
        if (bufOff == buf.length) {
            n = n5 + this.cipher.processBlock(buf, 0, array, n2 + n5);
            this.bufOff = 0;
        }
        return n;
    }
    
    public void reset() {
        int n = 0;
        while (true) {
            final byte[] buf = this.buf;
            if (n >= buf.length) {
                break;
            }
            buf[n] = 0;
            ++n;
        }
        this.bufOff = 0;
        this.cipher.reset();
    }
}
