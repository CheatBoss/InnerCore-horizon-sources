package org.spongycastle.crypto.modes.gcm;

public class BasicGCMMultiplier implements GCMMultiplier
{
    private int[] H;
    
    @Override
    public void init(final byte[] array) {
        this.H = GCMUtil.asInts(array);
    }
    
    @Override
    public void multiplyH(final byte[] array) {
        final int[] ints = GCMUtil.asInts(array);
        GCMUtil.multiply(ints, this.H);
        GCMUtil.asBytes(ints, array);
    }
}
