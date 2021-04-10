package org.spongycastle.crypto.prng;

public class ThreadedSeedGenerator
{
    public byte[] generateSeed(final int n, final boolean b) {
        return new SeedGenerator().generateSeed(n, b);
    }
    
    private class SeedGenerator implements Runnable
    {
        private volatile int counter;
        private volatile boolean stop;
        
        private SeedGenerator() {
            this.counter = 0;
            this.stop = false;
        }
        
        public byte[] generateSeed(int n, final boolean b) {
            final Thread thread = new Thread(this);
            final byte[] array = new byte[n];
            int i = 0;
            this.counter = 0;
            this.stop = false;
            thread.start();
            if (!b) {
                n *= 8;
            }
            int counter = 0;
            while (i < n) {
                while (this.counter == counter) {
                    try {
                        Thread.sleep(1L);
                    }
                    catch (InterruptedException ex) {}
                }
                counter = this.counter;
                if (b) {
                    array[i] = (byte)(counter & 0xFF);
                }
                else {
                    final int n2 = i / 8;
                    array[n2] = (byte)(array[n2] << 1 | (counter & 0x1));
                }
                ++i;
            }
            this.stop = true;
            return array;
        }
        
        @Override
        public void run() {
            while (!this.stop) {
                ++this.counter;
            }
        }
    }
}
