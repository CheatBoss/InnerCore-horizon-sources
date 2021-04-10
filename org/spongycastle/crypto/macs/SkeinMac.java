package org.spongycastle.crypto.macs;

import org.spongycastle.crypto.digests.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class SkeinMac implements Mac
{
    public static final int SKEIN_1024 = 1024;
    public static final int SKEIN_256 = 256;
    public static final int SKEIN_512 = 512;
    private SkeinEngine engine;
    
    public SkeinMac(final int n, final int n2) {
        this.engine = new SkeinEngine(n, n2);
    }
    
    public SkeinMac(final SkeinMac skeinMac) {
        this.engine = new SkeinEngine(skeinMac.engine);
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) {
        return this.engine.doFinal(array, n);
    }
    
    @Override
    public String getAlgorithmName() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Skein-MAC-");
        sb.append(this.engine.getBlockSize() * 8);
        sb.append("-");
        sb.append(this.engine.getOutputSize() * 8);
        return sb.toString();
    }
    
    @Override
    public int getMacSize() {
        return this.engine.getOutputSize();
    }
    
    @Override
    public void init(final CipherParameters cipherParameters) throws IllegalArgumentException {
        SkeinParameters build;
        if (cipherParameters instanceof SkeinParameters) {
            build = (SkeinParameters)cipherParameters;
        }
        else {
            if (!(cipherParameters instanceof KeyParameter)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Invalid parameter passed to Skein MAC init - ");
                sb.append(cipherParameters.getClass().getName());
                throw new IllegalArgumentException(sb.toString());
            }
            build = new SkeinParameters.Builder().setKey(((KeyParameter)cipherParameters).getKey()).build();
        }
        if (build.getKey() != null) {
            this.engine.init(build);
            return;
        }
        throw new IllegalArgumentException("Skein MAC requires a key parameter.");
    }
    
    @Override
    public void reset() {
        this.engine.reset();
    }
    
    @Override
    public void update(final byte b) {
        this.engine.update(b);
    }
    
    @Override
    public void update(final byte[] array, final int n, final int n2) {
        this.engine.update(array, n, n2);
    }
}
