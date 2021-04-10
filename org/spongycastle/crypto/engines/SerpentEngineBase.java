package org.spongycastle.crypto.engines;

import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public abstract class SerpentEngineBase implements BlockCipher
{
    protected static final int BLOCK_SIZE = 16;
    static final int PHI = -1640531527;
    static final int ROUNDS = 32;
    protected int X0;
    protected int X1;
    protected int X2;
    protected int X3;
    protected boolean encrypting;
    protected int[] wKey;
    
    SerpentEngineBase() {
    }
    
    protected static int rotateLeft(final int n, final int n2) {
        return n << n2 | n >>> -n2;
    }
    
    protected static int rotateRight(final int n, final int n2) {
        return n >>> n2 | n << -n2;
    }
    
    protected final void LT() {
        final int rotateLeft = rotateLeft(this.X0, 13);
        final int rotateLeft2 = rotateLeft(this.X2, 3);
        final int x1 = this.X1;
        final int x2 = this.X3;
        this.X1 = rotateLeft(x1 ^ rotateLeft ^ rotateLeft2, 1);
        final int rotateLeft3 = rotateLeft(x2 ^ rotateLeft2 ^ rotateLeft << 3, 7);
        this.X3 = rotateLeft3;
        this.X0 = rotateLeft(rotateLeft ^ this.X1 ^ rotateLeft3, 5);
        this.X2 = rotateLeft(this.X3 ^ rotateLeft2 ^ this.X1 << 7, 22);
    }
    
    protected abstract void decryptBlock(final byte[] p0, final int p1, final byte[] p2, final int p3);
    
    protected abstract void encryptBlock(final byte[] p0, final int p1, final byte[] p2, final int p3);
    
    @Override
    public String getAlgorithmName() {
        return "Serpent";
    }
    
    @Override
    public int getBlockSize() {
        return 16;
    }
    
    protected final void ib0(int x3, int n, int n2, int n3) {
        final int n4 = ~x3;
        final int n5 = n ^ x3;
        n = ((n4 | n5) ^ n3);
        n2 ^= n;
        final int x4 = n5 ^ n2;
        this.X2 = x4;
        n3 = ((n5 & n3) ^ n4);
        final int x5 = (x4 & n3) ^ n;
        this.X1 = x5;
        x3 = ((x3 & n) ^ (x5 | n2));
        this.X3 = x3;
        this.X0 = (x3 ^ (n3 ^ n2));
    }
    
    protected final void ib1(int n, int n2, int x3, int x4) {
        x4 ^= n2;
        final int n3 = n ^ (n2 & x4);
        n = (x4 ^ n3);
        x3 ^= n;
        this.X3 = x3;
        n2 ^= (x4 & n3);
        x4 = (n3 ^ (x3 | n2));
        this.X1 = x4;
        x4 ^= -1;
        n2 ^= x3;
        this.X0 = (x4 ^ n2);
        this.X2 = ((x4 | n2) ^ n);
    }
    
    protected final void ib2(int x3, int x4, int n, final int n2) {
        final int n3 = x4 ^ n2;
        final int n4 = x3 ^ n;
        n ^= n3;
        x4 = ((x4 & n) ^ n4);
        this.X0 = x4;
        x3 = ((((x3 | ~n3) ^ n2) | n4) ^ n3);
        this.X3 = x3;
        n ^= -1;
        x3 |= x4;
        this.X1 = (n ^ x3);
        this.X2 = (x3 ^ n4 ^ (n2 & n));
    }
    
    protected final void ib3(int n, int x3, final int n2, int n3) {
        final int n4 = x3 ^ n2;
        final int n5 = (x3 & n4) ^ n;
        final int n6 = n3 | n5;
        final int x4 = n4 ^ n6;
        this.X0 = x4;
        n3 ^= (n4 | n6);
        this.X2 = (n2 ^ n5 ^ n3);
        n = ((n | x3) ^ n3);
        x3 = ((x4 & n) ^ n5);
        this.X3 = x3;
        this.X1 = (n ^ x4 ^ x3);
    }
    
    protected final void ib4(int n, int n2, int x1, int n3) {
        n2 ^= ((x1 | n3) & n);
        final int n4 = x1 ^ (n & n2);
        x1 = (n3 ^ n4);
        this.X1 = x1;
        n ^= -1;
        final int x2 = (n4 & x1) ^ n2;
        this.X3 = x2;
        n3 ^= (x1 | n);
        this.X0 = (x2 ^ n3);
        this.X2 = (n ^ x1 ^ (n2 & n3));
    }
    
    protected final void ib5(final int n, final int n2, final int n3, int n4) {
        final int n5 = ~n3;
        final int n6 = (n2 & n5) ^ n4;
        final int n7 = n & n6;
        final int x3 = n2 ^ n5 ^ n7;
        this.X3 = x3;
        final int n8 = x3 | n2;
        this.X1 = (n6 ^ (n & n8));
        n4 |= n;
        this.X0 = (n5 ^ n8 ^ n4);
        this.X2 = (((n ^ n3) | n7) ^ (n2 & n4));
    }
    
    protected final void ib6(int n, int n2, int n3, final int n4) {
        final int n5 = ~n;
        final int n6 = n ^ n2;
        n = (n3 ^ n6);
        final int n7 = (n3 | n5) ^ n4;
        this.X1 = (n ^ n7);
        n3 = (n6 ^ (n & n7));
        final int x3 = n7 ^ (n2 | n3);
        this.X3 = x3;
        n2 |= x3;
        this.X0 = (n3 ^ n2);
        this.X2 = ((n4 & n5) ^ (n2 ^ n));
    }
    
    protected final void ib7(final int n, int x1, int x2, final int n2) {
        final int n3 = (n & x1) | x2;
        final int n4 = (n | x1) & n2;
        final int x3 = n3 ^ n4;
        this.X3 = x3;
        final int n5 = x1 ^ n4;
        x1 = (((~n2 ^ x3) | n5) ^ n);
        this.X1 = x1;
        x2 = (n5 ^ x2 ^ (n2 | x1));
        this.X0 = x2;
        this.X2 = ((n & x3) ^ x2 ^ (n3 ^ x1));
    }
    
    @Override
    public void init(final boolean encrypting, final CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            this.encrypting = encrypting;
            this.wKey = this.makeWorkingKey(((KeyParameter)cipherParameters).getKey());
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("invalid parameter passed to ");
        sb.append(this.getAlgorithmName());
        sb.append(" init - ");
        sb.append(cipherParameters.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    protected final void inverseLT() {
        final int n = rotateRight(this.X2, 22) ^ this.X3 ^ this.X1 << 7;
        final int rotateRight = rotateRight(this.X0, 5);
        final int x1 = this.X1;
        final int x2 = this.X3;
        final int n2 = rotateRight ^ x1 ^ x2;
        final int rotateRight2 = rotateRight(x2, 7);
        final int rotateRight3 = rotateRight(this.X1, 1);
        this.X3 = (rotateRight2 ^ n ^ n2 << 3);
        this.X1 = (rotateRight3 ^ n2 ^ n);
        this.X2 = rotateRight(n, 3);
        this.X0 = rotateRight(n2, 13);
    }
    
    protected abstract int[] makeWorkingKey(final byte[] p0);
    
    @Override
    public final int processBlock(final byte[] array, final int n, final byte[] array2, final int n2) {
        if (this.wKey == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.getAlgorithmName());
            sb.append(" not initialised");
            throw new IllegalStateException(sb.toString());
        }
        if (n + 16 > array.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n2 + 16 <= array2.length) {
            if (this.encrypting) {
                this.encryptBlock(array, n, array2, n2);
            }
            else {
                this.decryptBlock(array, n, array2, n2);
            }
            return 16;
        }
        throw new OutputLengthException("output buffer too short");
    }
    
    @Override
    public void reset() {
    }
    
    protected final void sb0(int n, int n2, final int n3, int x3) {
        final int n4 = n ^ x3;
        final int n5 = n3 ^ n4;
        final int n6 = n2 ^ n5;
        x3 = ((x3 & n) ^ n6);
        this.X3 = x3;
        n ^= (n2 & n4);
        this.X2 = ((n3 | n) ^ n6);
        n2 = ((n5 ^ n) & x3);
        this.X1 = (~n5 ^ n2);
        this.X0 = (~n ^ n2);
    }
    
    protected final void sb1(int n, int n2, int x2, int x3) {
        final int n3 = ~n ^ n2;
        n = ((n | n3) ^ x2);
        x2 = (x3 ^ n);
        this.X2 = x2;
        n2 ^= (x3 | n3);
        x2 ^= n3;
        x3 = ((n & n2) ^ x2);
        this.X3 = x3;
        n2 ^= n;
        this.X1 = (x3 ^ n2);
        this.X0 = (n ^ (n2 & x2));
    }
    
    protected final void sb2(int x2, int x3, int n, final int n2) {
        final int n3 = ~x2;
        final int n4 = x3 ^ n2;
        final int x4 = (n & n3) ^ n4;
        this.X0 = x4;
        final int n5 = n ^ n3;
        n = (x3 & (n ^ x4));
        x3 = (n5 ^ n);
        this.X3 = x3;
        x2 ^= ((n | n2) & (x4 | n5));
        this.X2 = x2;
        this.X1 = (x2 ^ (n2 | n3) ^ (n4 ^ x3));
    }
    
    protected final void sb3(int x2, final int n, int n2, final int n3) {
        final int n4 = x2 ^ n;
        final int n5 = x2 | n3;
        final int n6 = n2 ^ n3;
        n2 = ((x2 & n2) | (n4 & n5));
        x2 = (n6 ^ n2);
        this.X2 = x2;
        n2 ^= (n5 ^ n);
        final int x3 = n4 ^ (n6 & n2);
        this.X0 = x3;
        x2 &= x3;
        this.X1 = (n2 ^ x2);
        this.X3 = ((n | n3) ^ (n6 ^ x2));
    }
    
    protected final void sb4(final int n, int n2, int n3, int n4) {
        final int n5 = n ^ n4;
        n3 ^= (n4 & n5);
        n4 = (n2 | n3);
        this.X3 = (n5 ^ n4);
        final int n6 = ~n2;
        n2 = ((n5 | n6) ^ n3);
        this.X0 = n2;
        final int n7 = n6 ^ n5;
        n2 = ((n4 & n7) ^ (n2 & n));
        this.X2 = n2;
        this.X1 = (n ^ n3 ^ (n7 & n2));
    }
    
    protected final void sb5(int n, final int n2, int x0, int n3) {
        final int n4 = ~n;
        final int n5 = n ^ n2;
        n ^= n3;
        x0 = (x0 ^ n4 ^ (n5 | n));
        this.X0 = x0;
        n3 &= x0;
        final int x2 = n5 ^ x0 ^ n3;
        this.X1 = x2;
        n ^= (x0 | n4);
        this.X2 = ((n5 | n3) ^ n);
        this.X3 = ((n & x2) ^ (n2 ^ n3));
    }
    
    protected final void sb6(int n, int n2, int n3, final int n4) {
        final int n5 = n ^ n4;
        final int n6 = n2 ^ n5;
        n = ((~n | n5) ^ n3);
        n2 ^= n;
        this.X1 = n2;
        n3 = ((n2 | n5) ^ n4);
        n2 = ((n & n3) ^ n6);
        this.X2 = n2;
        n3 ^= n;
        this.X0 = (n2 ^ n3);
        this.X3 = (~n ^ (n3 & n6));
    }
    
    protected final void sb7(int x3, int x4, int x5, final int n) {
        final int n2 = x4 ^ x5;
        x5 = ((x5 & n2) ^ n);
        final int n3 = x3 ^ x5;
        x4 ^= ((n | n2) & n3);
        this.X1 = x4;
        x3 = ((x3 & n3) ^ n2);
        this.X3 = x3;
        x4 = ((x4 | x5) ^ n3);
        x5 ^= (x3 & x4);
        this.X2 = x5;
        this.X0 = ((x3 & x5) ^ ~x4);
    }
}
