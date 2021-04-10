package org.spongycastle.jcajce.provider.asymmetric.util;

import org.spongycastle.jce.spec.*;
import org.spongycastle.crypto.*;

public class IESUtil
{
    public static IESParameterSpec guessParameterSpec(final BufferedBlockCipher bufferedBlockCipher, final byte[] array) {
        if (bufferedBlockCipher == null) {
            return new IESParameterSpec(null, null, 128);
        }
        final BlockCipher underlyingCipher = bufferedBlockCipher.getUnderlyingCipher();
        if (underlyingCipher.getAlgorithmName().equals("DES") || underlyingCipher.getAlgorithmName().equals("RC2") || underlyingCipher.getAlgorithmName().equals("RC5-32") || underlyingCipher.getAlgorithmName().equals("RC5-64")) {
            return new IESParameterSpec(null, null, 64, 64, array);
        }
        if (underlyingCipher.getAlgorithmName().equals("SKIPJACK")) {
            return new IESParameterSpec(null, null, 80, 80, array);
        }
        if (underlyingCipher.getAlgorithmName().equals("GOST28147")) {
            return new IESParameterSpec(null, null, 256, 256, array);
        }
        return new IESParameterSpec(null, null, 128, 128, array);
    }
}
