package org.spongycastle.crypto;

public class BufferedAsymmetricBlockCipher
{
    protected byte[] buf;
    protected int bufOff;
    private final AsymmetricBlockCipher cipher;
    
    public BufferedAsymmetricBlockCipher(final AsymmetricBlockCipher cipher) {
        this.cipher = cipher;
    }
    
    public byte[] doFinal() throws InvalidCipherTextException {
        final byte[] processBlock = this.cipher.processBlock(this.buf, 0, this.bufOff);
        this.reset();
        return processBlock;
    }
    
    public int getBufferPosition() {
        return this.bufOff;
    }
    
    public int getInputBlockSize() {
        return this.cipher.getInputBlockSize();
    }
    
    public int getOutputBlockSize() {
        return this.cipher.getOutputBlockSize();
    }
    
    public AsymmetricBlockCipher getUnderlyingCipher() {
        return this.cipher;
    }
    
    public void init(final boolean b, final CipherParameters cipherParameters) {
        throw new Runtime("d2j fail translate: java.lang.RuntimeException: \r\n\tat com.googlecode.dex2jar.ir.ts.NewTransformer.transform(NewTransformer.java:134)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:148)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n");
    }
    
    public void processByte(final byte b) {
        final int bufOff = this.bufOff;
        final byte[] buf = this.buf;
        if (bufOff < buf.length) {
            this.bufOff = bufOff + 1;
            buf[bufOff] = b;
            return;
        }
        throw new DataLengthException("attempt to process message too long for cipher");
    }
    
    public void processBytes(final byte[] array, final int n, final int n2) {
        if (n2 == 0) {
            return;
        }
        if (n2 < 0) {
            throw new IllegalArgumentException("Can't have a negative input length!");
        }
        final int bufOff = this.bufOff;
        final byte[] buf = this.buf;
        if (bufOff + n2 <= buf.length) {
            System.arraycopy(array, n, buf, bufOff, n2);
            this.bufOff += n2;
            return;
        }
        throw new DataLengthException("attempt to process message too long for cipher");
    }
    
    public void reset() {
        if (this.buf != null) {
            int n = 0;
            while (true) {
                final byte[] buf = this.buf;
                if (n >= buf.length) {
                    break;
                }
                buf[n] = 0;
                ++n;
            }
        }
        this.bufOff = 0;
    }
}
