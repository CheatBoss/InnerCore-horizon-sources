package org.spongycastle.crypto.generators;

import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class PKCS5S1ParametersGenerator extends PBEParametersGenerator
{
    private Digest digest;
    
    public PKCS5S1ParametersGenerator(final Digest digest) {
        this.digest = digest;
    }
    
    private byte[] generateDerivedKey() {
        final int digestSize = this.digest.getDigestSize();
        final byte[] array = new byte[digestSize];
        this.digest.update(this.password, 0, this.password.length);
        this.digest.update(this.salt, 0, this.salt.length);
        this.digest.doFinal(array, 0);
        for (int i = 1; i < this.iterationCount; ++i) {
            this.digest.update(array, 0, digestSize);
            this.digest.doFinal(array, 0);
        }
        return array;
    }
    
    @Override
    public CipherParameters generateDerivedMacParameters(final int n) {
        return this.generateDerivedParameters(n);
    }
    
    @Override
    public CipherParameters generateDerivedParameters(int n) {
        n /= 8;
        if (n <= this.digest.getDigestSize()) {
            return new KeyParameter(this.generateDerivedKey(), 0, n);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Can't generate a derived key ");
        sb.append(n);
        sb.append(" bytes long.");
        throw new IllegalArgumentException(sb.toString());
    }
    
    @Override
    public CipherParameters generateDerivedParameters(int n, int n2) {
        n /= 8;
        n2 /= 8;
        final int n3 = n + n2;
        if (n3 <= this.digest.getDigestSize()) {
            final byte[] generateDerivedKey = this.generateDerivedKey();
            return new ParametersWithIV(new KeyParameter(generateDerivedKey, 0, n), generateDerivedKey, n, n2);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Can't generate a derived key ");
        sb.append(n3);
        sb.append(" bytes long.");
        throw new IllegalArgumentException(sb.toString());
    }
}
