package org.spongycastle.crypto.macs;

import java.util.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class HMac implements Mac
{
    private static final byte IPAD = 54;
    private static final byte OPAD = 92;
    private static Hashtable blockLengths;
    private int blockLength;
    private Digest digest;
    private int digestSize;
    private byte[] inputPad;
    private Memoable ipadState;
    private Memoable opadState;
    private byte[] outputBuf;
    
    static {
        (HMac.blockLengths = new Hashtable()).put("GOST3411", Integers.valueOf(32));
        HMac.blockLengths.put("MD2", Integers.valueOf(16));
        HMac.blockLengths.put("MD4", Integers.valueOf(64));
        HMac.blockLengths.put("MD5", Integers.valueOf(64));
        HMac.blockLengths.put("RIPEMD128", Integers.valueOf(64));
        HMac.blockLengths.put("RIPEMD160", Integers.valueOf(64));
        HMac.blockLengths.put("SHA-1", Integers.valueOf(64));
        HMac.blockLengths.put("SHA-224", Integers.valueOf(64));
        HMac.blockLengths.put("SHA-256", Integers.valueOf(64));
        HMac.blockLengths.put("SHA-384", Integers.valueOf(128));
        HMac.blockLengths.put("SHA-512", Integers.valueOf(128));
        HMac.blockLengths.put("Tiger", Integers.valueOf(64));
        HMac.blockLengths.put("Whirlpool", Integers.valueOf(64));
    }
    
    public HMac(final Digest digest) {
        this(digest, getByteLength(digest));
    }
    
    private HMac(final Digest digest, final int blockLength) {
        this.digest = digest;
        final int digestSize = digest.getDigestSize();
        this.digestSize = digestSize;
        this.blockLength = blockLength;
        this.inputPad = new byte[blockLength];
        this.outputBuf = new byte[blockLength + digestSize];
    }
    
    private static int getByteLength(final Digest digest) {
        if (digest instanceof ExtendedDigest) {
            return ((ExtendedDigest)digest).getByteLength();
        }
        final Integer n = HMac.blockLengths.get(digest.getAlgorithmName());
        if (n != null) {
            return n;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("unknown digest passed: ");
        sb.append(digest.getAlgorithmName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    private static void xorPad(final byte[] array, final int n, final byte b) {
        for (int i = 0; i < n; ++i) {
            array[i] ^= b;
        }
    }
    
    @Override
    public int doFinal(byte[] outputBuf, int blockLength) {
        this.digest.doFinal(this.outputBuf, this.blockLength);
        final Memoable opadState = this.opadState;
        if (opadState != null) {
            ((Memoable)this.digest).reset(opadState);
            final Digest digest = this.digest;
            digest.update(this.outputBuf, this.blockLength, digest.getDigestSize());
        }
        else {
            final Digest digest2 = this.digest;
            final byte[] outputBuf2 = this.outputBuf;
            digest2.update(outputBuf2, 0, outputBuf2.length);
        }
        final int doFinal = this.digest.doFinal(outputBuf, blockLength);
        blockLength = this.blockLength;
        while (true) {
            outputBuf = this.outputBuf;
            if (blockLength >= outputBuf.length) {
                break;
            }
            outputBuf[blockLength] = 0;
            ++blockLength;
        }
        final Memoable ipadState = this.ipadState;
        if (ipadState != null) {
            ((Memoable)this.digest).reset(ipadState);
            return doFinal;
        }
        final Digest digest3 = this.digest;
        final byte[] inputPad = this.inputPad;
        digest3.update(inputPad, 0, inputPad.length);
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
        int n = key.length;
        if (n > this.blockLength) {
            this.digest.update(key, 0, n);
            this.digest.doFinal(this.inputPad, 0);
            n = this.digestSize;
        }
        else {
            System.arraycopy(key, 0, this.inputPad, 0, n);
        }
        byte[] inputPad;
        while (true) {
            inputPad = this.inputPad;
            if (n >= inputPad.length) {
                break;
            }
            inputPad[n] = 0;
            ++n;
        }
        System.arraycopy(inputPad, 0, this.outputBuf, 0, this.blockLength);
        xorPad(this.inputPad, this.blockLength, (byte)54);
        xorPad(this.outputBuf, this.blockLength, (byte)92);
        final Digest digest = this.digest;
        if (digest instanceof Memoable) {
            final Memoable copy = ((Memoable)digest).copy();
            this.opadState = copy;
            ((Digest)copy).update(this.outputBuf, 0, this.blockLength);
        }
        final Digest digest2 = this.digest;
        final byte[] inputPad2 = this.inputPad;
        digest2.update(inputPad2, 0, inputPad2.length);
        final Digest digest3 = this.digest;
        if (digest3 instanceof Memoable) {
            this.ipadState = ((Memoable)digest3).copy();
        }
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
