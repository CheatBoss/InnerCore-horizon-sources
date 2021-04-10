package org.spongycastle.crypto.engines;

import org.spongycastle.util.*;
import org.spongycastle.crypto.modes.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class CryptoProWrapEngine extends GOST28147WrapEngine
{
    private static boolean bitSet(final byte b, final int n) {
        return (b & 1 << n) != 0x0;
    }
    
    private static byte[] cryptoProDiversify(final byte[] array, final byte[] array2, final byte[] array3) {
        for (int i = 0; i != 8; ++i) {
            int j = 0;
            int n = 0;
            int n2 = 0;
            while (j != 8) {
                final int littleEndianToInt = Pack.littleEndianToInt(array, j * 4);
                if (bitSet(array2[i], j)) {
                    n += littleEndianToInt;
                }
                else {
                    n2 += littleEndianToInt;
                }
                ++j;
            }
            final byte[] array4 = new byte[8];
            Pack.intToLittleEndian(n, array4, 0);
            Pack.intToLittleEndian(n2, array4, 4);
            final GCFBBlockCipher gcfbBlockCipher = new GCFBBlockCipher(new GOST28147Engine());
            gcfbBlockCipher.init(true, new ParametersWithIV(new ParametersWithSBox(new KeyParameter(array), array3), array4));
            gcfbBlockCipher.processBlock(array, 0, array, 0);
            gcfbBlockCipher.processBlock(array, 8, array, 8);
            gcfbBlockCipher.processBlock(array, 16, array, 16);
            gcfbBlockCipher.processBlock(array, 24, array, 24);
        }
        return array;
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) {
        CipherParameters parameters = cipherParameters;
        if (cipherParameters instanceof ParametersWithRandom) {
            parameters = ((ParametersWithRandom)cipherParameters).getParameters();
        }
        final ParametersWithUKM parametersWithUKM = (ParametersWithUKM)parameters;
        KeyParameter keyParameter;
        byte[] sBox;
        if (parametersWithUKM.getParameters() instanceof ParametersWithSBox) {
            keyParameter = (KeyParameter)((ParametersWithSBox)parametersWithUKM.getParameters()).getParameters();
            sBox = ((ParametersWithSBox)parametersWithUKM.getParameters()).getSBox();
        }
        else {
            keyParameter = (KeyParameter)parametersWithUKM.getParameters();
            sBox = null;
        }
        final KeyParameter keyParameter2 = new KeyParameter(cryptoProDiversify(keyParameter.getKey(), parametersWithUKM.getUKM(), sBox));
        ParametersWithUKM parametersWithUKM2;
        if (sBox != null) {
            parametersWithUKM2 = new ParametersWithUKM(new ParametersWithSBox(keyParameter2, sBox), parametersWithUKM.getUKM());
        }
        else {
            parametersWithUKM2 = new ParametersWithUKM(keyParameter2, parametersWithUKM.getUKM());
        }
        super.init(b, parametersWithUKM2);
    }
}
