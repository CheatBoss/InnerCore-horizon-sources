package org.spongycastle.crypto.generators;

import org.spongycastle.crypto.util.*;
import org.spongycastle.crypto.macs.*;
import org.spongycastle.crypto.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.params.*;

public class PKCS5S2ParametersGenerator extends PBEParametersGenerator
{
    private Mac hMac;
    private byte[] state;
    
    public PKCS5S2ParametersGenerator() {
        this(DigestFactory.createSHA1());
    }
    
    public PKCS5S2ParametersGenerator(final Digest digest) {
        final HMac hMac = new HMac(digest);
        this.hMac = hMac;
        this.state = new byte[hMac.getMacSize()];
    }
    
    private void F(byte[] array, final int n, byte[] state, final byte[] array2, final int n2) {
        if (n != 0) {
            if (array != null) {
                this.hMac.update(array, 0, array.length);
            }
            this.hMac.update(state, 0, state.length);
            this.hMac.doFinal(this.state, 0);
            array = this.state;
            System.arraycopy(array, 0, array2, n2, array.length);
            for (int i = 1; i < n; ++i) {
                final Mac hMac = this.hMac;
                state = this.state;
                hMac.update(state, 0, state.length);
                this.hMac.doFinal(this.state, 0);
                int n3 = 0;
                while (true) {
                    array = this.state;
                    if (n3 == array.length) {
                        break;
                    }
                    final int n4 = n2 + n3;
                    array2[n4] ^= array[n3];
                    ++n3;
                }
            }
            return;
        }
        throw new IllegalArgumentException("iteration count must be at least 1.");
    }
    
    private byte[] generateDerivedKey(int i) {
        final int macSize = this.hMac.getMacSize();
        final int n = (i + macSize - 1) / macSize;
        final byte[] array = new byte[4];
        final byte[] array2 = new byte[n * macSize];
        this.hMac.init(new KeyParameter(this.password));
        i = 1;
        int n2 = 0;
        while (i <= n) {
            int n3 = 3;
            while (true) {
                final byte b = (byte)(array[n3] + 1);
                array[n3] = b;
                if (b != 0) {
                    break;
                }
                --n3;
            }
            this.F(this.salt, this.iterationCount, array, array2, n2);
            n2 += macSize;
            ++i;
        }
        return array2;
    }
    
    @Override
    public CipherParameters generateDerivedMacParameters(final int n) {
        return this.generateDerivedParameters(n);
    }
    
    @Override
    public CipherParameters generateDerivedParameters(int n) {
        n /= 8;
        return new KeyParameter(Arrays.copyOfRange(this.generateDerivedKey(n), 0, n), 0, n);
    }
    
    @Override
    public CipherParameters generateDerivedParameters(int n, int n2) {
        n /= 8;
        n2 /= 8;
        final byte[] generateDerivedKey = this.generateDerivedKey(n + n2);
        return new ParametersWithIV(new KeyParameter(generateDerivedKey, 0, n), generateDerivedKey, n, n2);
    }
}
