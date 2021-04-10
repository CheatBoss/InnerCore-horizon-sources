package org.spongycastle.crypto.engines;

import org.spongycastle.util.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class Salsa20Engine implements SkippingStreamCipher
{
    public static final int DEFAULT_ROUNDS = 20;
    private static final int STATE_SIZE = 16;
    private static final int[] TAU_SIGMA;
    protected static final byte[] sigma;
    protected static final byte[] tau;
    private int cW0;
    private int cW1;
    private int cW2;
    protected int[] engineState;
    private int index;
    private boolean initialised;
    private byte[] keyStream;
    protected int rounds;
    protected int[] x;
    
    static {
        TAU_SIGMA = Pack.littleEndianToInt(Strings.toByteArray("expand 16-byte kexpand 32-byte k"), 0, 8);
        sigma = Strings.toByteArray("expand 32-byte k");
        tau = Strings.toByteArray("expand 16-byte k");
    }
    
    public Salsa20Engine() {
        this(20);
    }
    
    public Salsa20Engine(final int rounds) {
        this.index = 0;
        this.engineState = new int[16];
        this.x = new int[16];
        this.keyStream = new byte[64];
        this.initialised = false;
        if (rounds > 0 && (rounds & 0x1) == 0x0) {
            this.rounds = rounds;
            return;
        }
        throw new IllegalArgumentException("'rounds' must be a positive, even number");
    }
    
    private boolean limitExceeded() {
        final int cw0 = this.cW0 + 1;
        this.cW0 = cw0;
        if (cw0 == 0 && ++this.cW1 == 0) {
            final int cw2 = this.cW2 + 1;
            this.cW2 = cw2;
            if ((cw2 & 0x20) != 0x0) {
                return true;
            }
        }
        return false;
    }
    
    private boolean limitExceeded(int n) {
        final int cw0 = this.cW0 + n;
        this.cW0 = cw0;
        if (cw0 < n && cw0 >= 0) {
            n = this.cW1 + 1;
            if ((this.cW1 = n) == 0) {
                n = this.cW2 + 1;
                this.cW2 = n;
                if ((n & 0x20) != 0x0) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private void resetLimitCounter() {
        this.cW0 = 0;
        this.cW1 = 0;
        this.cW2 = 0;
    }
    
    protected static int rotl(final int n, final int n2) {
        return n << n2 | n >>> -n2;
    }
    
    public static void salsaCore(int i, final int[] array, final int[] array2) {
        if (array.length != 16) {
            throw new IllegalArgumentException();
        }
        if (array2.length != 16) {
            throw new IllegalArgumentException();
        }
        if (i % 2 == 0) {
            int n = array[0];
            int n2 = array[1];
            int n3 = array[2];
            int n4 = array[3];
            int n5 = array[4];
            int n6 = array[5];
            int n7 = array[6];
            int n8 = array[7];
            int n9 = array[8];
            int n10 = array[9];
            int n11 = array[10];
            int n12 = array[11];
            int n13 = array[12];
            int n14 = array[13];
            int n15 = array[14];
            int n16 = array[15];
            while (i > 0) {
                final int n17 = rotl(n + n13, 7) ^ n5;
                final int n18 = n9 ^ rotl(n17 + n, 9);
                final int n19 = n13 ^ rotl(n18 + n17, 13);
                final int n20 = n ^ rotl(n19 + n18, 18);
                final int n21 = n10 ^ rotl(n6 + n2, 7);
                final int n22 = n14 ^ rotl(n21 + n6, 9);
                final int n23 = n2 ^ rotl(n22 + n21, 13);
                final int n24 = rotl(n23 + n22, 18) ^ n6;
                final int n25 = n15 ^ rotl(n11 + n7, 7);
                final int n26 = n3 ^ rotl(n25 + n11, 9);
                final int n27 = n7 ^ rotl(n26 + n25, 13);
                final int n28 = n11 ^ rotl(n27 + n26, 18);
                final int n29 = n4 ^ rotl(n16 + n12, 7);
                final int n30 = n8 ^ rotl(n29 + n16, 9);
                final int n31 = n12 ^ rotl(n30 + n29, 13);
                final int n32 = n16 ^ rotl(n31 + n30, 18);
                n2 = (n23 ^ rotl(n20 + n29, 7));
                n3 = (n26 ^ rotl(n2 + n20, 9));
                n4 = (n29 ^ rotl(n3 + n2, 13));
                final int rotl = rotl(n4 + n3, 18);
                n7 = (n27 ^ rotl(n24 + n17, 7));
                n8 = (n30 ^ rotl(n7 + n24, 9));
                n5 = (rotl(n8 + n7, 13) ^ n17);
                final int rotl2 = rotl(n5 + n8, 18);
                n12 = (n31 ^ rotl(n28 + n21, 7));
                n9 = (rotl(n12 + n28, 9) ^ n18);
                n10 = (n21 ^ rotl(n9 + n12, 13));
                n11 = (n28 ^ rotl(n10 + n9, 18));
                n13 = (n19 ^ rotl(n32 + n25, 7));
                n14 = (n22 ^ rotl(n13 + n32, 9));
                n15 = (n25 ^ rotl(n14 + n13, 13));
                n16 = (n32 ^ rotl(n15 + n14, 18));
                n = (n20 ^ rotl);
                i -= 2;
                n6 = (rotl2 ^ n24);
            }
            array2[0] = n + array[0];
            array2[1] = n2 + array[1];
            array2[2] = n3 + array[2];
            array2[3] = n4 + array[3];
            array2[4] = n5 + array[4];
            array2[5] = n6 + array[5];
            array2[6] = n7 + array[6];
            array2[7] = n8 + array[7];
            array2[8] = n9 + array[8];
            array2[9] = n10 + array[9];
            array2[10] = n11 + array[10];
            array2[11] = n12 + array[11];
            array2[12] = n13 + array[12];
            array2[13] = n14 + array[13];
            array2[14] = n15 + array[14];
            array2[15] = n16 + array[15];
            return;
        }
        throw new IllegalArgumentException("Number of rounds must be even");
    }
    
    protected void advanceCounter() {
        final int[] engineState = this.engineState;
        final int n = engineState[8] + 1;
        engineState[8] = n;
        if (n == 0) {
            ++engineState[9];
        }
    }
    
    protected void advanceCounter(final long n) {
        final int n2 = (int)(n >>> 32);
        final int n3 = (int)n;
        if (n2 > 0) {
            final int[] engineState = this.engineState;
            engineState[9] += n2;
        }
        final int[] engineState2 = this.engineState;
        final int n4 = engineState2[8];
        engineState2[8] += n3;
        if (n4 != 0 && engineState2[8] < n4) {
            ++engineState2[9];
        }
    }
    
    protected void generateKeyStream(final byte[] array) {
        salsaCore(this.rounds, this.engineState, this.x);
        Pack.intToLittleEndian(this.x, array, 0);
    }
    
    @Override
    public String getAlgorithmName() {
        final int rounds = this.rounds;
        String string = "Salsa20";
        if (rounds != 20) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Salsa20");
            sb.append("/");
            sb.append(this.rounds);
            string = sb.toString();
        }
        return string;
    }
    
    protected long getCounter() {
        final int[] engineState = this.engineState;
        return (long)engineState[9] << 32 | ((long)engineState[8] & 0xFFFFFFFFL);
    }
    
    protected int getNonceSize() {
        return 8;
    }
    
    @Override
    public long getPosition() {
        return this.getCounter() * 64L + this.index;
    }
    
    @Override
    public void init(final boolean b, CipherParameters parameters) {
        if (!(parameters instanceof ParametersWithIV)) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.getAlgorithmName());
            sb.append(" Init parameters must include an IV");
            throw new IllegalArgumentException(sb.toString());
        }
        final ParametersWithIV parametersWithIV = (ParametersWithIV)parameters;
        final byte[] iv = parametersWithIV.getIV();
        if (iv != null && iv.length == this.getNonceSize()) {
            parameters = parametersWithIV.getParameters();
            byte[] key;
            if (parameters == null) {
                if (!this.initialised) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(this.getAlgorithmName());
                    sb2.append(" KeyParameter can not be null for first initialisation");
                    throw new IllegalStateException(sb2.toString());
                }
                key = null;
            }
            else {
                if (!(parameters instanceof KeyParameter)) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append(this.getAlgorithmName());
                    sb3.append(" Init parameters must contain a KeyParameter (or null for re-init)");
                    throw new IllegalArgumentException(sb3.toString());
                }
                key = ((KeyParameter)parameters).getKey();
            }
            this.setKey(key, iv);
            this.reset();
            this.initialised = true;
            return;
        }
        final StringBuilder sb4 = new StringBuilder();
        sb4.append(this.getAlgorithmName());
        sb4.append(" requires exactly ");
        sb4.append(this.getNonceSize());
        sb4.append(" bytes of IV");
        throw new IllegalArgumentException(sb4.toString());
    }
    
    protected void packTauOrSigma(int n, final int[] array, final int n2) {
        n = (n - 16) / 4;
        final int[] tau_SIGMA = Salsa20Engine.TAU_SIGMA;
        array[n2] = tau_SIGMA[n];
        array[n2 + 1] = tau_SIGMA[n + 1];
        array[n2 + 2] = tau_SIGMA[n + 2];
        array[n2 + 3] = tau_SIGMA[n + 3];
    }
    
    @Override
    public int processBytes(final byte[] array, final int n, final int n2, final byte[] array2, final int n3) {
        if (!this.initialised) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.getAlgorithmName());
            sb.append(" not initialised");
            throw new IllegalStateException(sb.toString());
        }
        if (n + n2 > array.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 + n2 > array2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        if (!this.limitExceeded(n2)) {
            for (int i = 0; i < n2; ++i) {
                final byte[] keyStream = this.keyStream;
                final int index = this.index;
                array2[i + n3] = (byte)(keyStream[index] ^ array[i + n]);
                if ((this.index = (index + 1 & 0x3F)) == 0) {
                    this.advanceCounter();
                    this.generateKeyStream(this.keyStream);
                }
            }
            return n2;
        }
        throw new MaxBytesExceededException("2^70 byte limit per IV would be exceeded; Change IV");
    }
    
    @Override
    public void reset() {
        this.index = 0;
        this.resetLimitCounter();
        this.resetCounter();
        this.generateKeyStream(this.keyStream);
    }
    
    protected void resetCounter() {
        final int[] engineState = this.engineState;
        engineState[8] = (engineState[9] = 0);
    }
    
    protected void retreatCounter() {
        final int[] engineState = this.engineState;
        if (engineState[8] == 0 && engineState[9] == 0) {
            throw new IllegalStateException("attempt to reduce counter past zero.");
        }
        final int[] engineState2 = this.engineState;
        if (--engineState2[8] == -1) {
            --engineState2[9];
        }
    }
    
    protected void retreatCounter(final long n) {
        final int n2 = (int)(n >>> 32);
        final int n3 = (int)n;
        if (n2 != 0) {
            final int[] engineState = this.engineState;
            if (((long)engineState[9] & 0xFFFFFFFFL) < ((long)n2 & 0xFFFFFFFFL)) {
                throw new IllegalStateException("attempt to reduce counter past zero.");
            }
            engineState[9] -= n2;
        }
        final int[] engineState2 = this.engineState;
        if (((long)engineState2[8] & 0xFFFFFFFFL) >= ((long)n3 & 0xFFFFFFFFL)) {
            engineState2[8] -= n3;
            return;
        }
        if (engineState2[9] != 0) {
            --engineState2[9];
            engineState2[8] -= n3;
            return;
        }
        throw new IllegalStateException("attempt to reduce counter past zero.");
    }
    
    @Override
    public byte returnByte(final byte b) {
        if (!this.limitExceeded()) {
            final byte[] keyStream = this.keyStream;
            final int index = this.index;
            final byte b2 = (byte)(b ^ keyStream[index]);
            if ((this.index = (index + 1 & 0x3F)) == 0) {
                this.advanceCounter();
                this.generateKeyStream(this.keyStream);
            }
            return b2;
        }
        throw new MaxBytesExceededException("2^70 byte limit per IV; Change IV");
    }
    
    @Override
    public long seekTo(final long n) {
        this.reset();
        return this.skip(n);
    }
    
    protected void setKey(final byte[] array, final byte[] array2) {
        if (array != null) {
            if (array.length != 16 && array.length != 32) {
                final StringBuilder sb = new StringBuilder();
                sb.append(this.getAlgorithmName());
                sb.append(" requires 128 bit or 256 bit key");
                throw new IllegalArgumentException(sb.toString());
            }
            final int n = (array.length - 16) / 4;
            final int[] engineState = this.engineState;
            final int[] tau_SIGMA = Salsa20Engine.TAU_SIGMA;
            engineState[0] = tau_SIGMA[n];
            engineState[5] = tau_SIGMA[n + 1];
            engineState[10] = tau_SIGMA[n + 2];
            engineState[15] = tau_SIGMA[n + 3];
            Pack.littleEndianToInt(array, 0, engineState, 1, 4);
            Pack.littleEndianToInt(array, array.length - 16, this.engineState, 11, 4);
        }
        Pack.littleEndianToInt(array2, 0, this.engineState, 6, 2);
    }
    
    @Override
    public long skip(final long n) {
        final long n2 = 0L;
        if (n >= 0L) {
            long n4;
            if (n >= 64L) {
                final long n3 = n / 64L;
                this.advanceCounter(n3);
                n4 = n - n3 * 64L;
            }
            else {
                n4 = n;
            }
            final int index = this.index;
            final int index2 = (int)n4 + index & 0x3F;
            this.index = index2;
            if (index2 < index) {
                this.advanceCounter();
            }
        }
        else {
            final long n5 = -n;
            long n6 = n2;
            long n7 = n5;
            if (n5 >= 64L) {
                final long n8 = n5 / 64L;
                this.retreatCounter(n8);
                n7 = n5 - n8 * 64L;
                n6 = n2;
            }
            while (n6 < n7) {
                if (this.index == 0) {
                    this.retreatCounter();
                }
                this.index = (this.index - 1 & 0x3F);
                ++n6;
            }
        }
        this.generateKeyStream(this.keyStream);
        return n;
    }
}
