package org.spongycastle.crypto.modes;

import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class GCFBBlockCipher extends StreamBlockCipher
{
    private static final byte[] C;
    private final CFBBlockCipher cfbEngine;
    private long counter;
    private boolean forEncryption;
    private KeyParameter key;
    
    static {
        C = new byte[] { 105, 0, 114, 34, 100, -55, 4, 35, -115, 58, -37, -106, 70, -23, 42, -60, 24, -2, -84, -108, 0, -19, 7, 18, -64, -122, -36, -62, -17, 76, -87, 43 };
    }
    
    public GCFBBlockCipher(final BlockCipher blockCipher) {
        super(blockCipher);
        this.counter = 0L;
        this.cfbEngine = new CFBBlockCipher(blockCipher, blockCipher.getBlockSize() * 8);
    }
    
    @Override
    protected byte calculateByte(final byte b) {
        final long counter = this.counter;
        if (counter > 0L && counter % 1024L == 0L) {
            final BlockCipher underlyingCipher = this.cfbEngine.getUnderlyingCipher();
            underlyingCipher.init(false, this.key);
            final byte[] array = new byte[32];
            underlyingCipher.processBlock(GCFBBlockCipher.C, 0, array, 0);
            underlyingCipher.processBlock(GCFBBlockCipher.C, 8, array, 8);
            underlyingCipher.processBlock(GCFBBlockCipher.C, 16, array, 16);
            underlyingCipher.processBlock(GCFBBlockCipher.C, 24, array, 24);
            underlyingCipher.init(true, this.key = new KeyParameter(array));
            final byte[] currentIV = this.cfbEngine.getCurrentIV();
            underlyingCipher.processBlock(currentIV, 0, currentIV, 0);
            this.cfbEngine.init(this.forEncryption, new ParametersWithIV(this.key, currentIV));
        }
        ++this.counter;
        return this.cfbEngine.calculateByte(b);
    }
    
    @Override
    public String getAlgorithmName() {
        final String algorithmName = this.cfbEngine.getAlgorithmName();
        final StringBuilder sb = new StringBuilder();
        sb.append(algorithmName.substring(0, algorithmName.indexOf(47)));
        sb.append("/G");
        sb.append(algorithmName.substring(algorithmName.indexOf(47) + 1));
        return sb.toString();
    }
    
    @Override
    public int getBlockSize() {
        return this.cfbEngine.getBlockSize();
    }
    
    @Override
    public void init(final boolean forEncryption, CipherParameters parameters) throws IllegalArgumentException {
        this.counter = 0L;
        this.cfbEngine.init(forEncryption, parameters);
        this.forEncryption = forEncryption;
        CipherParameters parameters2 = parameters;
        if (parameters instanceof ParametersWithIV) {
            parameters2 = ((ParametersWithIV)parameters).getParameters();
        }
        parameters = parameters2;
        if (parameters2 instanceof ParametersWithRandom) {
            parameters = ((ParametersWithRandom)parameters2).getParameters();
        }
        CipherParameters parameters3 = parameters;
        if (parameters instanceof ParametersWithSBox) {
            parameters3 = ((ParametersWithSBox)parameters).getParameters();
        }
        this.key = (KeyParameter)parameters3;
    }
    
    @Override
    public int processBlock(final byte[] array, final int n, final byte[] array2, final int n2) throws DataLengthException, IllegalStateException {
        this.processBytes(array, n, this.cfbEngine.getBlockSize(), array2, n2);
        return this.cfbEngine.getBlockSize();
    }
    
    @Override
    public void reset() {
        this.counter = 0L;
        this.cfbEngine.reset();
    }
}
