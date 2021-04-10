package org.spongycastle.crypto.generators;

import java.math.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class KDFCounterBytesGenerator implements MacDerivationFunction
{
    private static final BigInteger INTEGER_MAX;
    private static final BigInteger TWO;
    private byte[] fixedInputDataCtrPrefix;
    private byte[] fixedInputData_afterCtr;
    private int generatedBytes;
    private final int h;
    private byte[] ios;
    private byte[] k;
    private int maxSizeExcl;
    private final Mac prf;
    
    static {
        INTEGER_MAX = BigInteger.valueOf(2147483647L);
        TWO = BigInteger.valueOf(2L);
    }
    
    public KDFCounterBytesGenerator(final Mac prf) {
        this.prf = prf;
        final int macSize = prf.getMacSize();
        this.h = macSize;
        this.k = new byte[macSize];
    }
    
    private void generateNext() {
        final int n = this.generatedBytes / this.h + 1;
        final byte[] ios = this.ios;
        final int length = ios.length;
        if (length != 1) {
            if (length != 2) {
                if (length != 3) {
                    if (length != 4) {
                        throw new IllegalStateException("Unsupported size of counter i");
                    }
                    ios[0] = (byte)(n >>> 24);
                }
                final byte[] ios2 = this.ios;
                ios2[ios2.length - 3] = (byte)(n >>> 16);
            }
            final byte[] ios3 = this.ios;
            ios3[ios3.length - 2] = (byte)(n >>> 8);
        }
        final byte[] ios4 = this.ios;
        ios4[ios4.length - 1] = (byte)n;
        final Mac prf = this.prf;
        final byte[] fixedInputDataCtrPrefix = this.fixedInputDataCtrPrefix;
        prf.update(fixedInputDataCtrPrefix, 0, fixedInputDataCtrPrefix.length);
        final Mac prf2 = this.prf;
        final byte[] ios5 = this.ios;
        prf2.update(ios5, 0, ios5.length);
        final Mac prf3 = this.prf;
        final byte[] fixedInputData_afterCtr = this.fixedInputData_afterCtr;
        prf3.update(fixedInputData_afterCtr, 0, fixedInputData_afterCtr.length);
        this.prf.doFinal(this.k, 0);
    }
    
    @Override
    public int generateBytes(final byte[] array, int n, final int n2) throws DataLengthException, IllegalArgumentException {
        final int generatedBytes = this.generatedBytes;
        final int n3 = generatedBytes + n2;
        if (n3 >= 0 && n3 < this.maxSizeExcl) {
            if (generatedBytes % this.h == 0) {
                this.generateNext();
            }
            final int generatedBytes2 = this.generatedBytes;
            final int h = this.h;
            final int n4 = generatedBytes2 % h;
            int n5 = Math.min(h - n4, n2);
            System.arraycopy(this.k, n4, array, n, n5);
            this.generatedBytes += n5;
            final int n6 = n2 - n5;
            int n7 = n;
            n = n6;
            while (true) {
                n7 += n5;
                if (n <= 0) {
                    break;
                }
                this.generateNext();
                n5 = Math.min(this.h, n);
                System.arraycopy(this.k, 0, array, n7, n5);
                this.generatedBytes += n5;
                n -= n5;
            }
            return n2;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Current KDFCTR may only be used for ");
        sb.append(this.maxSizeExcl);
        sb.append(" bytes");
        throw new DataLengthException(sb.toString());
    }
    
    @Override
    public Mac getMac() {
        return this.prf;
    }
    
    @Override
    public void init(final DerivationParameters derivationParameters) {
        if (derivationParameters instanceof KDFCounterParameters) {
            final KDFCounterParameters kdfCounterParameters = (KDFCounterParameters)derivationParameters;
            this.prf.init(new KeyParameter(kdfCounterParameters.getKI()));
            this.fixedInputDataCtrPrefix = kdfCounterParameters.getFixedInputDataCounterPrefix();
            this.fixedInputData_afterCtr = kdfCounterParameters.getFixedInputDataCounterSuffix();
            final int r = kdfCounterParameters.getR();
            this.ios = new byte[r / 8];
            final BigInteger multiply = KDFCounterBytesGenerator.TWO.pow(r).multiply(BigInteger.valueOf(this.h));
            int intValue;
            if (multiply.compareTo(KDFCounterBytesGenerator.INTEGER_MAX) == 1) {
                intValue = Integer.MAX_VALUE;
            }
            else {
                intValue = multiply.intValue();
            }
            this.maxSizeExcl = intValue;
            this.generatedBytes = 0;
            return;
        }
        throw new IllegalArgumentException("Wrong type of arguments given");
    }
}
