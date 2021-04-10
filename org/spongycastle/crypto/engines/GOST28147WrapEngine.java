package org.spongycastle.crypto.engines;

import org.spongycastle.crypto.macs.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.*;

public class GOST28147WrapEngine implements Wrapper
{
    private GOST28147Engine cipher;
    private GOST28147Mac mac;
    
    public GOST28147WrapEngine() {
        this.cipher = new GOST28147Engine();
        this.mac = new GOST28147Mac();
    }
    
    @Override
    public String getAlgorithmName() {
        return "GOST28147Wrap";
    }
    
    @Override
    public void init(final boolean b, CipherParameters cipherParameters) {
        CipherParameters parameters = cipherParameters;
        if (cipherParameters instanceof ParametersWithRandom) {
            parameters = ((ParametersWithRandom)cipherParameters).getParameters();
        }
        final ParametersWithUKM parametersWithUKM = (ParametersWithUKM)parameters;
        this.cipher.init(b, parametersWithUKM.getParameters());
        if (parametersWithUKM.getParameters() instanceof ParametersWithSBox) {
            cipherParameters = ((ParametersWithSBox)parametersWithUKM.getParameters()).getParameters();
        }
        else {
            cipherParameters = parametersWithUKM.getParameters();
        }
        this.mac.init(new ParametersWithIV(cipherParameters, parametersWithUKM.getUKM()));
    }
    
    @Override
    public byte[] unwrap(final byte[] array, final int n, final int n2) throws InvalidCipherTextException {
        final int n3 = n2 - this.mac.getMacSize();
        final byte[] array2 = new byte[n3];
        this.cipher.processBlock(array, n, array2, 0);
        this.cipher.processBlock(array, n + 8, array2, 8);
        this.cipher.processBlock(array, n + 16, array2, 16);
        this.cipher.processBlock(array, n + 24, array2, 24);
        final byte[] array3 = new byte[this.mac.getMacSize()];
        this.mac.update(array2, 0, n3);
        this.mac.doFinal(array3, 0);
        final byte[] array4 = new byte[this.mac.getMacSize()];
        System.arraycopy(array, n + n2 - 4, array4, 0, this.mac.getMacSize());
        if (Arrays.constantTimeAreEqual(array3, array4)) {
            return array2;
        }
        throw new IllegalStateException("mac mismatch");
    }
    
    @Override
    public byte[] wrap(final byte[] array, final int n, final int n2) {
        this.mac.update(array, n, n2);
        final byte[] array2 = new byte[this.mac.getMacSize() + n2];
        this.cipher.processBlock(array, n, array2, 0);
        this.cipher.processBlock(array, n + 8, array2, 8);
        this.cipher.processBlock(array, n + 16, array2, 16);
        this.cipher.processBlock(array, n + 24, array2, 24);
        this.mac.doFinal(array2, n2);
        return array2;
    }
}
