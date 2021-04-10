package org.spongycastle.crypto.generators;

import org.spongycastle.crypto.macs.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class HKDFBytesGenerator implements DerivationFunction
{
    private byte[] currentT;
    private int generatedBytes;
    private HMac hMacHash;
    private int hashLen;
    private byte[] info;
    
    public HKDFBytesGenerator(final Digest digest) {
        this.hMacHash = new HMac(digest);
        this.hashLen = digest.getDigestSize();
    }
    
    private void expandNext() throws DataLengthException {
        final int generatedBytes = this.generatedBytes;
        final int hashLen = this.hashLen;
        final int n = generatedBytes / hashLen + 1;
        if (n < 256) {
            if (generatedBytes != 0) {
                this.hMacHash.update(this.currentT, 0, hashLen);
            }
            final HMac hMacHash = this.hMacHash;
            final byte[] info = this.info;
            hMacHash.update(info, 0, info.length);
            this.hMacHash.update((byte)n);
            this.hMacHash.doFinal(this.currentT, 0);
            return;
        }
        throw new DataLengthException("HKDF cannot generate more than 255 blocks of HashLen size");
    }
    
    private KeyParameter extract(byte[] array, final byte[] array2) {
        if (array == null) {
            this.hMacHash.init(new KeyParameter(new byte[this.hashLen]));
        }
        else {
            this.hMacHash.init(new KeyParameter(array));
        }
        this.hMacHash.update(array2, 0, array2.length);
        array = new byte[this.hashLen];
        this.hMacHash.doFinal(array, 0);
        return new KeyParameter(array);
    }
    
    @Override
    public int generateBytes(final byte[] array, int n, final int n2) throws DataLengthException, IllegalArgumentException {
        final int generatedBytes = this.generatedBytes;
        final int hashLen = this.hashLen;
        if (generatedBytes + n2 <= hashLen * 255) {
            if (generatedBytes % hashLen == 0) {
                this.expandNext();
            }
            final int generatedBytes2 = this.generatedBytes;
            final int hashLen2 = this.hashLen;
            final int n3 = generatedBytes2 % hashLen2;
            int n4 = Math.min(hashLen2 - n3, n2);
            System.arraycopy(this.currentT, n3, array, n, n4);
            this.generatedBytes += n4;
            final int n5 = n2 - n4;
            int n6 = n;
            n = n5;
            while (true) {
                n6 += n4;
                if (n <= 0) {
                    break;
                }
                this.expandNext();
                n4 = Math.min(this.hashLen, n);
                System.arraycopy(this.currentT, 0, array, n6, n4);
                this.generatedBytes += n4;
                n -= n4;
            }
            return n2;
        }
        throw new DataLengthException("HKDF may only be used for 255 * HashLen bytes of output");
    }
    
    public Digest getDigest() {
        return this.hMacHash.getUnderlyingDigest();
    }
    
    @Override
    public void init(final DerivationParameters derivationParameters) {
        if (derivationParameters instanceof HKDFParameters) {
            final HKDFParameters hkdfParameters = (HKDFParameters)derivationParameters;
            HMac hMac;
            KeyParameter extract;
            if (hkdfParameters.skipExtract()) {
                hMac = this.hMacHash;
                extract = new KeyParameter(hkdfParameters.getIKM());
            }
            else {
                hMac = this.hMacHash;
                extract = this.extract(hkdfParameters.getSalt(), hkdfParameters.getIKM());
            }
            hMac.init(extract);
            this.info = hkdfParameters.getInfo();
            this.generatedBytes = 0;
            this.currentT = new byte[this.hashLen];
            return;
        }
        throw new IllegalArgumentException("HKDF parameters required for HKDFBytesGenerator");
    }
}
