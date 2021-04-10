package org.spongycastle.crypto.digests;

import org.spongycastle.crypto.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.params.*;

public class SkeinDigest implements ExtendedDigest, Memoable
{
    public static final int SKEIN_1024 = 1024;
    public static final int SKEIN_256 = 256;
    public static final int SKEIN_512 = 512;
    private SkeinEngine engine;
    
    public SkeinDigest(final int n, final int n2) {
        this.engine = new SkeinEngine(n, n2);
        this.init(null);
    }
    
    public SkeinDigest(final SkeinDigest skeinDigest) {
        this.engine = new SkeinEngine(skeinDigest.engine);
    }
    
    @Override
    public Memoable copy() {
        return new SkeinDigest(this);
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) {
        return this.engine.doFinal(array, n);
    }
    
    @Override
    public String getAlgorithmName() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Skein-");
        sb.append(this.engine.getBlockSize() * 8);
        sb.append("-");
        sb.append(this.engine.getOutputSize() * 8);
        return sb.toString();
    }
    
    @Override
    public int getByteLength() {
        return this.engine.getBlockSize();
    }
    
    @Override
    public int getDigestSize() {
        return this.engine.getOutputSize();
    }
    
    public void init(final SkeinParameters skeinParameters) {
        this.engine.init(skeinParameters);
    }
    
    @Override
    public void reset() {
        this.engine.reset();
    }
    
    @Override
    public void reset(final Memoable memoable) {
        this.engine.reset(((SkeinDigest)memoable).engine);
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
