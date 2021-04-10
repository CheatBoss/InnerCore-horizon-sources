package org.spongycastle.util.encoders;

public class BufferedEncoder
{
    protected byte[] buf;
    protected int bufOff;
    protected Translator translator;
    
    public BufferedEncoder(final Translator translator, final int n) {
        this.translator = translator;
        if (n % translator.getEncodedBlockSize() == 0) {
            this.buf = new byte[n];
            this.bufOff = 0;
            return;
        }
        throw new IllegalArgumentException("buffer size not multiple of input block size");
    }
    
    public int processByte(final byte b, final byte[] array, int encode) {
        final byte[] buf = this.buf;
        final int bufOff = this.bufOff;
        final int bufOff2 = bufOff + 1;
        this.bufOff = bufOff2;
        buf[bufOff] = b;
        if (bufOff2 == buf.length) {
            encode = this.translator.encode(buf, 0, buf.length, array, encode);
            this.bufOff = 0;
            return encode;
        }
        return 0;
    }
    
    public int processBytes(final byte[] array, int n, int n2, final byte[] array2, final int n3) {
        if (n2 >= 0) {
            final byte[] buf = this.buf;
            final int length = buf.length;
            final int bufOff = this.bufOff;
            final int n4 = length - bufOff;
            int n5 = 0;
            int n6 = n;
            int n7;
            if ((n7 = n2) > n4) {
                System.arraycopy(array, n, buf, bufOff, n4);
                final Translator translator = this.translator;
                final byte[] buf2 = this.buf;
                final int n8 = translator.encode(buf2, 0, buf2.length, array2, n3) + 0;
                this.bufOff = 0;
                n2 -= n4;
                n += n4;
                final int n9 = n2 - n2 % this.buf.length;
                n5 = n8 + this.translator.encode(array, n, n9, array2, n3 + n8);
                n7 = n2 - n9;
                n6 = n + n9;
            }
            if (n7 != 0) {
                System.arraycopy(array, n6, this.buf, this.bufOff, n7);
                this.bufOff += n7;
            }
            return n5;
        }
        throw new IllegalArgumentException("Can't have a negative input length!");
    }
}
