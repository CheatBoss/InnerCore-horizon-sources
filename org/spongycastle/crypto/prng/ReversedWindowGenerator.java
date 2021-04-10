package org.spongycastle.crypto.prng;

public class ReversedWindowGenerator implements RandomGenerator
{
    private final RandomGenerator generator;
    private byte[] window;
    private int windowCount;
    
    public ReversedWindowGenerator(final RandomGenerator generator, final int n) {
        if (generator == null) {
            throw new IllegalArgumentException("generator cannot be null");
        }
        if (n >= 2) {
            this.generator = generator;
            this.window = new byte[n];
            return;
        }
        throw new IllegalArgumentException("windowSize must be at least 2");
    }
    
    private void doNextBytes(final byte[] array, final int n, final int n2) {
        // monitorenter(this)
        int n3 = 0;
        while (true) {
            Label_0091: {
                if (n3 >= n2) {
                    break Label_0091;
                }
                try {
                    if (this.windowCount < 1) {
                        this.generator.nextBytes(this.window, 0, this.window.length);
                        this.windowCount = this.window.length;
                    }
                    final byte[] window = this.window;
                    final int windowCount = this.windowCount - 1;
                    this.windowCount = windowCount;
                    array[n3 + n] = window[windowCount];
                    ++n3;
                }
                finally {
                }
                // monitorexit(this)
                // monitorexit(this)
            }
        }
    }
    
    @Override
    public void addSeedMaterial(final long n) {
        synchronized (this) {
            this.windowCount = 0;
            this.generator.addSeedMaterial(n);
        }
    }
    
    @Override
    public void addSeedMaterial(final byte[] array) {
        synchronized (this) {
            this.windowCount = 0;
            this.generator.addSeedMaterial(array);
        }
    }
    
    @Override
    public void nextBytes(final byte[] array) {
        this.doNextBytes(array, 0, array.length);
    }
    
    @Override
    public void nextBytes(final byte[] array, final int n, final int n2) {
        this.doNextBytes(array, n, n2);
    }
}
