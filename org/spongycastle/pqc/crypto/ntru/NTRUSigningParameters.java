package org.spongycastle.pqc.crypto.ntru;

import org.spongycastle.crypto.digests.*;
import org.spongycastle.crypto.*;
import java.text.*;
import java.io.*;

public class NTRUSigningParameters implements Cloneable
{
    public int B;
    public int N;
    double beta;
    public double betaSq;
    int bitsF;
    public int d;
    public int d1;
    public int d2;
    public int d3;
    public Digest hashAlg;
    double normBound;
    public double normBoundSq;
    public int q;
    public int signFailTolerance;
    
    public NTRUSigningParameters(final int n, final int q, final int d, final int b, final double beta, final double normBound, final Digest hashAlg) {
        this.signFailTolerance = 100;
        this.bitsF = 6;
        this.N = n;
        this.q = q;
        this.d = d;
        this.B = b;
        this.beta = beta;
        this.normBound = normBound;
        this.hashAlg = hashAlg;
        this.init();
    }
    
    public NTRUSigningParameters(final int n, final int q, final int d1, final int d2, final int d3, final int b, final double beta, final double normBound, final double n2, final Digest hashAlg) {
        this.signFailTolerance = 100;
        this.bitsF = 6;
        this.N = n;
        this.q = q;
        this.d1 = d1;
        this.d2 = d2;
        this.d3 = d3;
        this.B = b;
        this.beta = beta;
        this.normBound = normBound;
        this.hashAlg = hashAlg;
        this.init();
    }
    
    public NTRUSigningParameters(final InputStream inputStream) throws IOException {
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
        this.beta = dataInputStream.readDouble();
        this.normBound = dataInputStream.readDouble();
        this.signFailTolerance = dataInputStream.readInt();
        this.bitsF = dataInputStream.readInt();
        final String utf = dataInputStream.readUTF();
        Label_0163: {
            ExtendedDigest hashAlg;
            if ("SHA-512".equals(utf)) {
                hashAlg = new SHA512Digest();
            }
            else {
                if (!"SHA-256".equals(utf)) {
                    break Label_0163;
                }
                hashAlg = new SHA256Digest();
            }
            this.hashAlg = hashAlg;
        }
        this.init();
    }
    
    private void init() {
        final double beta = this.beta;
        this.betaSq = beta * beta;
        final double normBound = this.normBound;
        this.normBoundSq = normBound * normBound;
    }
    
    public NTRUSigningParameters clone() {
        return new NTRUSigningParameters(this.N, this.q, this.d, this.B, this.beta, this.normBound, this.hashAlg);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof NTRUSigningParameters)) {
            return false;
        }
        final NTRUSigningParameters ntruSigningParameters = (NTRUSigningParameters)o;
        if (this.B != ntruSigningParameters.B) {
            return false;
        }
        if (this.N != ntruSigningParameters.N) {
            return false;
        }
        if (Double.doubleToLongBits(this.beta) != Double.doubleToLongBits(ntruSigningParameters.beta)) {
            return false;
        }
        if (Double.doubleToLongBits(this.betaSq) != Double.doubleToLongBits(ntruSigningParameters.betaSq)) {
            return false;
        }
        if (this.bitsF != ntruSigningParameters.bitsF) {
            return false;
        }
        if (this.d != ntruSigningParameters.d) {
            return false;
        }
        if (this.d1 != ntruSigningParameters.d1) {
            return false;
        }
        if (this.d2 != ntruSigningParameters.d2) {
            return false;
        }
        if (this.d3 != ntruSigningParameters.d3) {
            return false;
        }
        final Digest hashAlg = this.hashAlg;
        if (hashAlg == null) {
            if (ntruSigningParameters.hashAlg != null) {
                return false;
            }
        }
        else if (!hashAlg.getAlgorithmName().equals(ntruSigningParameters.hashAlg.getAlgorithmName())) {
            return false;
        }
        return Double.doubleToLongBits(this.normBound) == Double.doubleToLongBits(ntruSigningParameters.normBound) && Double.doubleToLongBits(this.normBoundSq) == Double.doubleToLongBits(ntruSigningParameters.normBoundSq) && this.q == ntruSigningParameters.q && this.signFailTolerance == ntruSigningParameters.signFailTolerance;
    }
    
    @Override
    public int hashCode() {
        final int b = this.B;
        final int n = this.N;
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
        final long doubleToLongBits3 = Double.doubleToLongBits(this.normBound);
        final int n4 = (int)(doubleToLongBits3 ^ doubleToLongBits3 >>> 32);
        final long doubleToLongBits4 = Double.doubleToLongBits(this.normBoundSq);
        return (((((((((((((b + 31) * 31 + n) * 31 + n2) * 31 + n3) * 31 + bitsF) * 31 + d) * 31 + d2) * 31 + d3) * 31 + d4) * 31 + hashCode) * 31 + n4) * 31 + (int)(doubleToLongBits4 ^ doubleToLongBits4 >>> 32)) * 31 + this.q) * 31 + this.signFailTolerance;
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
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(" B=");
        sb3.append(this.B);
        sb3.append(" beta=");
        sb3.append(decimalFormat.format(this.beta));
        sb3.append(" normBound=");
        sb3.append(decimalFormat.format(this.normBound));
        sb3.append(" hashAlg=");
        sb3.append(this.hashAlg);
        sb3.append(")");
        sb2.append(sb3.toString());
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
        dataOutputStream.writeDouble(this.beta);
        dataOutputStream.writeDouble(this.normBound);
        dataOutputStream.writeInt(this.signFailTolerance);
        dataOutputStream.writeInt(this.bitsF);
        dataOutputStream.writeUTF(this.hashAlg.getAlgorithmName());
    }
}
