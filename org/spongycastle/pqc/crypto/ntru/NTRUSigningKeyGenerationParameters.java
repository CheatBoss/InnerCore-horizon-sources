package org.spongycastle.pqc.crypto.ntru;

import org.spongycastle.crypto.digests.*;
import java.security.*;
import org.spongycastle.crypto.*;
import java.text.*;
import java.io.*;

public class NTRUSigningKeyGenerationParameters extends KeyGenerationParameters implements Cloneable
{
    public static final NTRUSigningKeyGenerationParameters APR2011_439;
    public static final NTRUSigningKeyGenerationParameters APR2011_439_PROD;
    public static final NTRUSigningKeyGenerationParameters APR2011_743;
    public static final NTRUSigningKeyGenerationParameters APR2011_743_PROD;
    public static final int BASIS_TYPE_STANDARD = 0;
    public static final int BASIS_TYPE_TRANSPOSE = 1;
    public static final int KEY_GEN_ALG_FLOAT = 1;
    public static final int KEY_GEN_ALG_RESULTANT = 0;
    public static final NTRUSigningKeyGenerationParameters TEST157;
    public static final NTRUSigningKeyGenerationParameters TEST157_PROD;
    public int B;
    public int N;
    public int basisType;
    double beta;
    public double betaSq;
    int bitsF;
    public int d;
    public int d1;
    public int d2;
    public int d3;
    public Digest hashAlg;
    public int keyGenAlg;
    double keyNormBound;
    public double keyNormBoundSq;
    double normBound;
    public double normBoundSq;
    public int polyType;
    public boolean primeCheck;
    public int q;
    public int signFailTolerance;
    public boolean sparse;
    
    static {
        APR2011_439 = new NTRUSigningKeyGenerationParameters(439, 2048, 146, 1, 1, 0.165, 490.0, 280.0, false, true, 0, new SHA256Digest());
        APR2011_439_PROD = new NTRUSigningKeyGenerationParameters(439, 2048, 9, 8, 5, 1, 1, 0.165, 490.0, 280.0, false, true, 0, new SHA256Digest());
        APR2011_743 = new NTRUSigningKeyGenerationParameters(743, 2048, 248, 1, 1, 0.127, 560.0, 360.0, true, false, 0, new SHA512Digest());
        APR2011_743_PROD = new NTRUSigningKeyGenerationParameters(743, 2048, 11, 11, 15, 1, 1, 0.127, 560.0, 360.0, true, false, 0, new SHA512Digest());
        TEST157 = new NTRUSigningKeyGenerationParameters(157, 256, 29, 1, 1, 0.38, 200.0, 80.0, false, false, 0, new SHA256Digest());
        TEST157_PROD = new NTRUSigningKeyGenerationParameters(157, 256, 5, 5, 8, 1, 1, 0.38, 200.0, 80.0, false, false, 0, new SHA256Digest());
    }
    
    public NTRUSigningKeyGenerationParameters(final int n, final int q, final int d, final int b, final int basisType, final double beta, final double normBound, final double keyNormBound, final boolean primeCheck, final boolean sparse, final int keyGenAlg, final Digest hashAlg) {
        super(new SecureRandom(), n);
        this.signFailTolerance = 100;
        this.bitsF = 6;
        this.N = n;
        this.q = q;
        this.d = d;
        this.B = b;
        this.basisType = basisType;
        this.beta = beta;
        this.normBound = normBound;
        this.keyNormBound = keyNormBound;
        this.primeCheck = primeCheck;
        this.sparse = sparse;
        this.keyGenAlg = keyGenAlg;
        this.hashAlg = hashAlg;
        this.polyType = 0;
        this.init();
    }
    
    public NTRUSigningKeyGenerationParameters(final int n, final int q, final int d1, final int d2, final int d3, final int b, final int basisType, final double beta, final double normBound, final double keyNormBound, final boolean primeCheck, final boolean sparse, final int keyGenAlg, final Digest hashAlg) {
        super(new SecureRandom(), n);
        this.signFailTolerance = 100;
        this.bitsF = 6;
        this.N = n;
        this.q = q;
        this.d1 = d1;
        this.d2 = d2;
        this.d3 = d3;
        this.B = b;
        this.basisType = basisType;
        this.beta = beta;
        this.normBound = normBound;
        this.keyNormBound = keyNormBound;
        this.primeCheck = primeCheck;
        this.sparse = sparse;
        this.keyGenAlg = keyGenAlg;
        this.hashAlg = hashAlg;
        this.polyType = 1;
        this.init();
    }
    
    public NTRUSigningKeyGenerationParameters(final InputStream inputStream) throws IOException {
        super(new SecureRandom(), 0);
        this.signFailTolerance = 100;
        this.bitsF = 6;
        final DataInputStream dataInputStream = new DataInputStream(inputStream);
        this.N = dataInputStream.readInt();
        this.q = dataInputStream.readInt();
        this.d = dataInputStream.readInt();
        this.d1 = dataInputStream.readInt();
        this.d2 = dataInputStream.readInt();
        this.d3 = dataInputStream.readInt();
        this.B = dataInputStream.readInt();
        this.basisType = dataInputStream.readInt();
        this.beta = dataInputStream.readDouble();
        this.normBound = dataInputStream.readDouble();
        this.keyNormBound = dataInputStream.readDouble();
        this.signFailTolerance = dataInputStream.readInt();
        this.primeCheck = dataInputStream.readBoolean();
        this.sparse = dataInputStream.readBoolean();
        this.bitsF = dataInputStream.readInt();
        this.keyGenAlg = dataInputStream.read();
        final String utf = dataInputStream.readUTF();
        Label_0211: {
            ExtendedDigest hashAlg;
            if ("SHA-512".equals(utf)) {
                hashAlg = new SHA512Digest();
            }
            else {
                if (!"SHA-256".equals(utf)) {
                    break Label_0211;
                }
                hashAlg = new SHA256Digest();
            }
            this.hashAlg = hashAlg;
        }
        this.polyType = dataInputStream.read();
        this.init();
    }
    
    private void init() {
        final double beta = this.beta;
        this.betaSq = beta * beta;
        final double normBound = this.normBound;
        this.normBoundSq = normBound * normBound;
        final double keyNormBound = this.keyNormBound;
        this.keyNormBoundSq = keyNormBound * keyNormBound;
    }
    
    public NTRUSigningKeyGenerationParameters clone() {
        if (this.polyType == 0) {
            return new NTRUSigningKeyGenerationParameters(this.N, this.q, this.d, this.B, this.basisType, this.beta, this.normBound, this.keyNormBound, this.primeCheck, this.sparse, this.keyGenAlg, this.hashAlg);
        }
        return new NTRUSigningKeyGenerationParameters(this.N, this.q, this.d1, this.d2, this.d3, this.B, this.basisType, this.beta, this.normBound, this.keyNormBound, this.primeCheck, this.sparse, this.keyGenAlg, this.hashAlg);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof NTRUSigningKeyGenerationParameters)) {
            return false;
        }
        final NTRUSigningKeyGenerationParameters ntruSigningKeyGenerationParameters = (NTRUSigningKeyGenerationParameters)o;
        if (this.B != ntruSigningKeyGenerationParameters.B) {
            return false;
        }
        if (this.N != ntruSigningKeyGenerationParameters.N) {
            return false;
        }
        if (this.basisType != ntruSigningKeyGenerationParameters.basisType) {
            return false;
        }
        if (Double.doubleToLongBits(this.beta) != Double.doubleToLongBits(ntruSigningKeyGenerationParameters.beta)) {
            return false;
        }
        if (Double.doubleToLongBits(this.betaSq) != Double.doubleToLongBits(ntruSigningKeyGenerationParameters.betaSq)) {
            return false;
        }
        if (this.bitsF != ntruSigningKeyGenerationParameters.bitsF) {
            return false;
        }
        if (this.d != ntruSigningKeyGenerationParameters.d) {
            return false;
        }
        if (this.d1 != ntruSigningKeyGenerationParameters.d1) {
            return false;
        }
        if (this.d2 != ntruSigningKeyGenerationParameters.d2) {
            return false;
        }
        if (this.d3 != ntruSigningKeyGenerationParameters.d3) {
            return false;
        }
        final Digest hashAlg = this.hashAlg;
        if (hashAlg == null) {
            if (ntruSigningKeyGenerationParameters.hashAlg != null) {
                return false;
            }
        }
        else if (!hashAlg.getAlgorithmName().equals(ntruSigningKeyGenerationParameters.hashAlg.getAlgorithmName())) {
            return false;
        }
        return this.keyGenAlg == ntruSigningKeyGenerationParameters.keyGenAlg && Double.doubleToLongBits(this.keyNormBound) == Double.doubleToLongBits(ntruSigningKeyGenerationParameters.keyNormBound) && Double.doubleToLongBits(this.keyNormBoundSq) == Double.doubleToLongBits(ntruSigningKeyGenerationParameters.keyNormBoundSq) && Double.doubleToLongBits(this.normBound) == Double.doubleToLongBits(ntruSigningKeyGenerationParameters.normBound) && Double.doubleToLongBits(this.normBoundSq) == Double.doubleToLongBits(ntruSigningKeyGenerationParameters.normBoundSq) && this.polyType == ntruSigningKeyGenerationParameters.polyType && this.primeCheck == ntruSigningKeyGenerationParameters.primeCheck && this.q == ntruSigningKeyGenerationParameters.q && this.signFailTolerance == ntruSigningKeyGenerationParameters.signFailTolerance && this.sparse == ntruSigningKeyGenerationParameters.sparse;
    }
    
    public NTRUSigningParameters getSigningParameters() {
        return new NTRUSigningParameters(this.N, this.q, this.d, this.B, this.beta, this.normBound, this.hashAlg);
    }
    
    @Override
    public int hashCode() {
        final int b = this.B;
        final int n = this.N;
        final int basisType = this.basisType;
        final long doubleToLongBits = Double.doubleToLongBits(this.beta);
        final int n2 = (int)(doubleToLongBits ^ doubleToLongBits >>> 32);
        final long doubleToLongBits2 = Double.doubleToLongBits(this.betaSq);
        final int n3 = (int)(doubleToLongBits2 ^ doubleToLongBits2 >>> 32);
        final int bitsF = this.bitsF;
        final int d = this.d;
        final int d2 = this.d1;
        final int d3 = this.d2;
        final int d4 = this.d3;
        final Digest hashAlg = this.hashAlg;
        int hashCode;
        if (hashAlg == null) {
            hashCode = 0;
        }
        else {
            hashCode = hashAlg.getAlgorithmName().hashCode();
        }
        final int keyGenAlg = this.keyGenAlg;
        final long doubleToLongBits3 = Double.doubleToLongBits(this.keyNormBound);
        final int n4 = (int)(doubleToLongBits3 ^ doubleToLongBits3 >>> 32);
        final long doubleToLongBits4 = Double.doubleToLongBits(this.keyNormBoundSq);
        final int n5 = (int)(doubleToLongBits4 ^ doubleToLongBits4 >>> 32);
        final long doubleToLongBits5 = Double.doubleToLongBits(this.normBound);
        final int n6 = (int)(doubleToLongBits5 ^ doubleToLongBits5 >>> 32);
        final long doubleToLongBits6 = Double.doubleToLongBits(this.normBoundSq);
        final int n7 = (int)(doubleToLongBits6 ^ doubleToLongBits6 >>> 32);
        final int polyType = this.polyType;
        final boolean primeCheck = this.primeCheck;
        int n8 = 1231;
        int n9;
        if (primeCheck) {
            n9 = 1231;
        }
        else {
            n9 = 1237;
        }
        final int q = this.q;
        final int signFailTolerance = this.signFailTolerance;
        if (!this.sparse) {
            n8 = 1237;
        }
        return ((((((((((((((((((((b + 31) * 31 + n) * 31 + basisType) * 31 + n2) * 31 + n3) * 31 + bitsF) * 31 + d) * 31 + d2) * 31 + d3) * 31 + d4) * 31 + hashCode) * 31 + keyGenAlg) * 31 + n4) * 31 + n5) * 31 + n6) * 31 + n7) * 31 + polyType) * 31 + n9) * 31 + q) * 31 + signFailTolerance) * 31 + n8;
    }
    
    @Override
    public String toString() {
        final DecimalFormat decimalFormat = new DecimalFormat("0.00");
        final StringBuilder sb = new StringBuilder();
        sb.append("SignatureParameters(N=");
        sb.append(this.N);
        sb.append(" q=");
        sb.append(this.q);
        final StringBuilder sb2 = new StringBuilder(sb.toString());
        StringBuilder sb3;
        int n;
        if (this.polyType == 0) {
            sb3 = new StringBuilder();
            sb3.append(" polyType=SIMPLE d=");
            n = this.d;
        }
        else {
            sb3 = new StringBuilder();
            sb3.append(" polyType=PRODUCT d1=");
            sb3.append(this.d1);
            sb3.append(" d2=");
            sb3.append(this.d2);
            sb3.append(" d3=");
            n = this.d3;
        }
        sb3.append(n);
        sb2.append(sb3.toString());
        final StringBuilder sb4 = new StringBuilder();
        sb4.append(" B=");
        sb4.append(this.B);
        sb4.append(" basisType=");
        sb4.append(this.basisType);
        sb4.append(" beta=");
        sb4.append(decimalFormat.format(this.beta));
        sb4.append(" normBound=");
        sb4.append(decimalFormat.format(this.normBound));
        sb4.append(" keyNormBound=");
        sb4.append(decimalFormat.format(this.keyNormBound));
        sb4.append(" prime=");
        sb4.append(this.primeCheck);
        sb4.append(" sparse=");
        sb4.append(this.sparse);
        sb4.append(" keyGenAlg=");
        sb4.append(this.keyGenAlg);
        sb4.append(" hashAlg=");
        sb4.append(this.hashAlg);
        sb4.append(")");
        sb2.append(sb4.toString());
        return sb2.toString();
    }
    
    public void writeTo(final OutputStream outputStream) throws IOException {
        final DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        dataOutputStream.writeInt(this.N);
        dataOutputStream.writeInt(this.q);
        dataOutputStream.writeInt(this.d);
        dataOutputStream.writeInt(this.d1);
        dataOutputStream.writeInt(this.d2);
        dataOutputStream.writeInt(this.d3);
        dataOutputStream.writeInt(this.B);
        dataOutputStream.writeInt(this.basisType);
        dataOutputStream.writeDouble(this.beta);
        dataOutputStream.writeDouble(this.normBound);
        dataOutputStream.writeDouble(this.keyNormBound);
        dataOutputStream.writeInt(this.signFailTolerance);
        dataOutputStream.writeBoolean(this.primeCheck);
        dataOutputStream.writeBoolean(this.sparse);
        dataOutputStream.writeInt(this.bitsF);
        dataOutputStream.write(this.keyGenAlg);
        dataOutputStream.writeUTF(this.hashAlg.getAlgorithmName());
        dataOutputStream.write(this.polyType);
    }
}
