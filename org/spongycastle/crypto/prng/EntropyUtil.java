package org.spongycastle.crypto.prng;

public class EntropyUtil
{
    public static byte[] generateSeed(final EntropySource entropySource, final int n) {
        final byte[] array = new byte[n];
        if (n * 8 <= entropySource.entropySize()) {
            System.arraycopy(entropySource.getEntropy(), 0, array, 0, n);
            return array;
        }
        for (int n2 = entropySource.entropySize() / 8, i = 0; i < n; i += n2) {
            final byte[] entropy = entropySource.getEntropy();
            final int length = entropy.length;
            final int n3 = n - i;
            if (length <= n3) {
                System.arraycopy(entropy, 0, array, i, entropy.length);
            }
            else {
                System.arraycopy(entropy, 0, array, i, n3);
            }
        }
        return array;
    }
}
