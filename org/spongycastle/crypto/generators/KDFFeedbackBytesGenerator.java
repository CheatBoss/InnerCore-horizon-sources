package org.spongycastle.crypto.generators;

import java.math.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class KDFFeedbackBytesGenerator implements MacDerivationFunction
{
    private static final BigInteger INTEGER_MAX;
    private static final BigInteger TWO;
    private byte[] fixedInputData;
    private int generatedBytes;
    private final int h;
    private byte[] ios;
    private byte[] iv;
    private byte[] k;
    private int maxSizeExcl;
    private final Mac prf;
    private boolean useCounter;
    
    static {
        INTEGER_MAX = BigInteger.valueOf(2147483647L);
        TWO = BigInteger.valueOf(2L);
    }
    
    public KDFFeedbackBytesGenerator(final Mac prf) {
        this.prf = prf;
        final int macSize = prf.getMacSize();
        this.h = macSize;
        this.k = new byte[macSize];
    }
    
    private void generateNext() {
        Mac mac;
        byte[] array;
        int n;
        if (this.generatedBytes == 0) {
            mac = this.prf;
            array = this.iv;
            n = array.length;
        }
        else {
            mac = this.prf;
            array = this.k;
            n = array.length;
        }
        mac.update(array, 0, n);
        if (this.useCounter) {
            final int n2 = this.generatedBytes / this.h + 1;
            final byte[] ios = this.ios;
            final int length = ios.length;
            if (length != 1) {
                if (length != 2) {
                    if (length != 3) {
                        if (length != 4) {
                            throw new IllegalStateException("Unsupported size of counter i");
                        }
                        ios[0] = (byte)(n2 >>> 24);
                    }
                    final byte[] ios2 = this.ios;
                    ios2[ios2.length - 3] = (byte)(n2 >>> 16);
                }
                final byte[] ios3 = this.ios;
                ios3[ios3.length - 2] = (byte)(n2 >>> 8);
            }
            final byte[] ios4 = this.ios;
            ios4[ios4.length - 1] = (byte)n2;
            this.prf.update(ios4, 0, ios4.length);
        }
        final Mac prf = this.prf;
        final byte[] fixedInputData = this.fixedInputData;
        prf.update(fixedInputData, 0, fixedInputData.length);
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
        if (derivationParameters instanceof KDFFeedbackParameters) {
            final KDFFeedbackParameters kdfFeedbackParameters = (KDFFeedbackParameters)derivationParameters;
            this.prf.init(new KeyParameter(kdfFeedbackParameters.getKI()));
            this.fixedInputData = kdfFeedbackParameters.getFixedInputData();
            final int r = kdfFeedbackParameters.getR();
            this.ios = new byte[r / 8];
            final boolean useCounter = kdfFeedbackParameters.useCounter();
            int intValue = Integer.MAX_VALUE;
            if (useCounter) {
                final BigInteger multiply = KDFFeedbackBytesGenerator.TWO.pow(r).multiply(BigInteger.valueOf(this.h));
                if (multiply.compareTo(KDFFeedbackBytesGenerator.INTEGER_MAX) == 1) {
                    intValue = intValue;
                }
                else {
                    intValue = multiply.intValue();
                }
            }
            this.maxSizeExcl = intValue;
            this.iv = kdfFeedbackParameters.getIV();
            this.useCounter = kdfFeedbackParameters.useCounter();
            this.generatedBytes = 0;
            return;
        }
        throw new IllegalArgumentException("Wrong type of arguments given");
    }
}
