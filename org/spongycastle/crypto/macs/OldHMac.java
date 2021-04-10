package org.spongycastle.crypto.macs;

import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class OldHMac implements Mac
{
    private static final int BLOCK_LENGTH = 64;
    private static final byte IPAD = 54;
    private static final byte OPAD = 92;
    private Digest digest;
    private int digestSize;
    private byte[] inputPad;
    private byte[] outputPad;
    
    public OldHMac(final Digest digest) {
        this.inputPad = new byte[64];
        this.outputPad = new byte[64];
        this.digest = digest;
        this.digestSize = digest.getDigestSize();
    }
    
    @Override
    public int doFinal(final byte[] array, int doFinal) {
        final int digestSize = this.digestSize;
        final byte[] array2 = new byte[digestSize];
        this.digest.doFinal(array2, 0);
        final Digest digest = this.digest;
        final byte[] outputPad = this.outputPad;
        digest.update(outputPad, 0, outputPad.length);
        this.digest.update(array2, 0, digestSize);
        doFinal = this.digest.doFinal(array, doFinal);
        this.reset();
        return doFinal;
    }
    
    @Override
    public String getAlgorithmName() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.digest.getAlgorithmName());
        sb.append("/HMAC");
        return sb.toString();
    }
    
    @Override
    public int getMacSize() {
        return this.digestSize;
    }
    
    public Digest getUnderlyingDigest() {
        return this.digest;
    }
    
    @Override
    public void init(final CipherParameters cipherParameters) {
        this.digest.reset();
        final byte[] key = ((KeyParameter)cipherParameters).getKey();
        if (key.length > 64) {
            this.digest.update(key, 0, key.length);
            this.digest.doFinal(this.inputPad, 0);
            int digestSize = this.digestSize;
            while (true) {
                final byte[] inputPad = this.inputPad;
                if (digestSize >= inputPad.length) {
                    break;
                }
                inputPad[digestSize] = 0;
                ++digestSize;
            }
        }
        else {
            System.arraycopy(key, 0, this.inputPad, 0, key.length);
            int length = key.length;
            while (true) {
                final byte[] inputPad2 = this.inputPad;
                if (length >= inputPad2.length) {
                    break;
                }
                inputPad2[length] = 0;
                ++length;
            }
        }
        final byte[] inputPad3 = this.inputPad;
        System.arraycopy(inputPad3, 0, this.outputPad = new byte[inputPad3.length], 0, inputPad3.length);
        int n = 0;
        while (true) {
            final byte[] inputPad4 = this.inputPad;
            if (n >= inputPad4.length) {
                break;
            }
            inputPad4[n] ^= 0x36;
            ++n;
        }
        int n2 = 0;
        while (true) {
            final byte[] outputPad = this.outputPad;
            if (n2 >= outputPad.length) {
                break;
            }
            outputPad[n2] ^= 0x5C;
            ++n2;
        }
        final Digest digest = this.digest;
        final byte[] inputPad5 = this.inputPad;
        digest.update(inputPad5, 0, inputPad5.length);
    }
    
    @Override
    public void reset() {
        this.digest.reset();
        final Digest digest = this.digest;
        final byte[] inputPad = this.inputPad;
        digest.update(inputPad, 0, inputPad.length);
    }
    
    @Override
    public void update(final byte b) {
        this.digest.update(b);
    }
    
    @Override
    public void update(final byte[] array, final int n, final int n2) {
        this.digest.update(array, n, n2);
    }
}
