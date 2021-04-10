package org.spongycastle.crypto.engines;

import org.spongycastle.util.*;
import java.util.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class GOST28147Engine implements BlockCipher
{
    protected static final int BLOCK_SIZE = 8;
    private static byte[] DSbox_A;
    private static byte[] DSbox_Test;
    private static byte[] ESbox_A;
    private static byte[] ESbox_B;
    private static byte[] ESbox_C;
    private static byte[] ESbox_D;
    private static byte[] ESbox_Test;
    private static byte[] Sbox_Default;
    private static Hashtable sBoxes;
    private byte[] S;
    private boolean forEncryption;
    private int[] workingKey;
    
    static {
        GOST28147Engine.Sbox_Default = new byte[] { 4, 10, 9, 2, 13, 8, 0, 14, 6, 11, 1, 12, 7, 15, 5, 3, 14, 11, 4, 12, 6, 13, 15, 10, 2, 3, 8, 1, 0, 7, 5, 9, 5, 8, 1, 13, 10, 3, 4, 2, 14, 15, 12, 7, 6, 0, 9, 11, 7, 13, 10, 1, 0, 8, 9, 15, 14, 4, 6, 12, 11, 2, 5, 3, 6, 12, 7, 1, 5, 15, 13, 8, 4, 10, 9, 14, 0, 3, 11, 2, 4, 11, 10, 0, 7, 2, 1, 13, 3, 6, 8, 5, 9, 12, 15, 14, 13, 11, 4, 1, 3, 15, 5, 9, 0, 10, 14, 7, 6, 8, 2, 12, 1, 15, 13, 0, 5, 7, 10, 4, 9, 2, 3, 14, 6, 11, 8, 12 };
        GOST28147Engine.ESbox_Test = new byte[] { 4, 2, 15, 5, 9, 1, 0, 8, 14, 3, 11, 12, 13, 7, 10, 6, 12, 9, 15, 14, 8, 1, 3, 10, 2, 7, 4, 13, 6, 0, 11, 5, 13, 8, 14, 12, 7, 3, 9, 10, 1, 5, 2, 4, 6, 15, 0, 11, 14, 9, 11, 2, 5, 15, 7, 1, 0, 13, 12, 6, 10, 4, 3, 8, 3, 14, 5, 9, 6, 8, 0, 13, 10, 11, 7, 12, 2, 1, 15, 4, 8, 15, 6, 11, 1, 9, 12, 5, 13, 3, 7, 10, 0, 14, 2, 4, 9, 11, 12, 0, 3, 6, 7, 5, 4, 8, 14, 15, 1, 10, 2, 13, 12, 6, 5, 2, 11, 0, 9, 13, 3, 14, 7, 10, 15, 4, 1, 8 };
        GOST28147Engine.ESbox_A = new byte[] { 9, 6, 3, 2, 8, 11, 1, 7, 10, 4, 14, 15, 12, 0, 13, 5, 3, 7, 14, 9, 8, 10, 15, 0, 5, 2, 6, 12, 11, 4, 13, 1, 14, 4, 6, 2, 11, 3, 13, 8, 12, 15, 5, 10, 0, 7, 1, 9, 14, 7, 10, 12, 13, 1, 3, 9, 0, 2, 11, 4, 15, 8, 5, 6, 11, 5, 1, 9, 8, 13, 15, 0, 14, 4, 2, 3, 12, 7, 10, 6, 3, 10, 13, 12, 1, 2, 0, 11, 7, 5, 9, 4, 8, 15, 14, 6, 1, 13, 2, 9, 7, 10, 6, 0, 8, 12, 4, 5, 15, 3, 11, 14, 11, 10, 15, 5, 0, 12, 14, 8, 6, 2, 3, 9, 1, 7, 13, 4 };
        GOST28147Engine.ESbox_B = new byte[] { 8, 4, 11, 1, 3, 5, 0, 9, 2, 14, 10, 12, 13, 6, 7, 15, 0, 1, 2, 10, 4, 13, 5, 12, 9, 7, 3, 15, 11, 8, 6, 14, 14, 12, 0, 10, 9, 2, 13, 11, 7, 5, 8, 15, 3, 6, 1, 4, 7, 5, 0, 13, 11, 6, 1, 2, 3, 10, 12, 15, 4, 14, 9, 8, 2, 7, 12, 15, 9, 5, 10, 11, 1, 4, 0, 13, 6, 8, 14, 3, 8, 3, 2, 6, 4, 13, 14, 11, 12, 1, 7, 15, 10, 0, 9, 5, 5, 2, 10, 11, 9, 1, 12, 3, 7, 4, 13, 0, 6, 15, 8, 14, 0, 4, 11, 14, 8, 3, 7, 1, 10, 2, 9, 6, 15, 13, 5, 12 };
        GOST28147Engine.ESbox_C = new byte[] { 1, 11, 12, 2, 9, 13, 0, 15, 4, 5, 8, 14, 10, 7, 6, 3, 0, 1, 7, 13, 11, 4, 5, 2, 8, 14, 15, 12, 9, 10, 6, 3, 8, 2, 5, 0, 4, 9, 15, 10, 3, 7, 12, 13, 6, 14, 1, 11, 3, 6, 0, 1, 5, 13, 10, 8, 11, 2, 9, 7, 14, 15, 12, 4, 8, 13, 11, 0, 4, 5, 1, 2, 9, 3, 12, 14, 6, 15, 10, 7, 12, 9, 11, 1, 8, 14, 2, 4, 7, 3, 6, 5, 10, 0, 15, 13, 10, 9, 6, 8, 13, 14, 2, 0, 15, 3, 5, 11, 4, 1, 12, 7, 7, 4, 0, 5, 10, 2, 15, 14, 12, 6, 1, 11, 13, 9, 3, 8 };
        GOST28147Engine.ESbox_D = new byte[] { 15, 12, 2, 10, 6, 4, 5, 0, 7, 9, 14, 13, 1, 11, 8, 3, 11, 6, 3, 4, 12, 15, 14, 2, 7, 13, 8, 0, 5, 10, 9, 1, 1, 12, 11, 0, 15, 14, 6, 5, 10, 13, 4, 8, 9, 3, 7, 2, 1, 5, 14, 12, 10, 7, 0, 13, 6, 2, 11, 4, 9, 3, 15, 8, 0, 12, 8, 9, 13, 2, 10, 11, 7, 3, 6, 5, 4, 14, 15, 1, 8, 0, 15, 3, 2, 5, 14, 11, 1, 10, 4, 7, 12, 9, 13, 6, 3, 0, 6, 15, 1, 14, 9, 2, 13, 8, 12, 4, 11, 10, 5, 7, 1, 10, 6, 8, 15, 11, 0, 4, 12, 3, 5, 9, 7, 13, 2, 14 };
        GOST28147Engine.DSbox_Test = new byte[] { 4, 10, 9, 2, 13, 8, 0, 14, 6, 11, 1, 12, 7, 15, 5, 3, 14, 11, 4, 12, 6, 13, 15, 10, 2, 3, 8, 1, 0, 7, 5, 9, 5, 8, 1, 13, 10, 3, 4, 2, 14, 15, 12, 7, 6, 0, 9, 11, 7, 13, 10, 1, 0, 8, 9, 15, 14, 4, 6, 12, 11, 2, 5, 3, 6, 12, 7, 1, 5, 15, 13, 8, 4, 10, 9, 14, 0, 3, 11, 2, 4, 11, 10, 0, 7, 2, 1, 13, 3, 6, 8, 5, 9, 12, 15, 14, 13, 11, 4, 1, 3, 15, 5, 9, 0, 10, 14, 7, 6, 8, 2, 12, 1, 15, 13, 0, 5, 7, 10, 4, 9, 2, 3, 14, 6, 11, 8, 12 };
        GOST28147Engine.DSbox_A = new byte[] { 10, 4, 5, 6, 8, 1, 3, 7, 13, 12, 14, 0, 9, 2, 11, 15, 5, 15, 4, 0, 2, 13, 11, 9, 1, 7, 6, 3, 12, 14, 10, 8, 7, 15, 12, 14, 9, 4, 1, 0, 3, 11, 5, 2, 6, 10, 8, 13, 4, 10, 7, 12, 0, 15, 2, 8, 14, 1, 6, 5, 13, 11, 9, 3, 7, 6, 4, 11, 9, 12, 2, 10, 1, 8, 0, 14, 15, 13, 3, 5, 7, 6, 2, 4, 13, 9, 15, 0, 10, 1, 5, 11, 8, 14, 12, 3, 13, 14, 4, 1, 7, 0, 5, 10, 3, 12, 8, 15, 6, 2, 9, 11, 1, 3, 10, 9, 5, 11, 4, 15, 8, 6, 7, 14, 13, 0, 2, 12 };
        GOST28147Engine.sBoxes = new Hashtable();
        addSBox("Default", GOST28147Engine.Sbox_Default);
        addSBox("E-TEST", GOST28147Engine.ESbox_Test);
        addSBox("E-A", GOST28147Engine.ESbox_A);
        addSBox("E-B", GOST28147Engine.ESbox_B);
        addSBox("E-C", GOST28147Engine.ESbox_C);
        addSBox("E-D", GOST28147Engine.ESbox_D);
        addSBox("D-TEST", GOST28147Engine.DSbox_Test);
        addSBox("D-A", GOST28147Engine.DSbox_A);
    }
    
    public GOST28147Engine() {
        this.workingKey = null;
        this.S = GOST28147Engine.Sbox_Default;
    }
    
    private void GOST28147Func(final int[] array, final byte[] array2, int n, final byte[] array3, final int n2) {
        int bytesToint = this.bytesToint(array2, n);
        n = this.bytesToint(array2, n + 4);
        final boolean forEncryption = this.forEncryption;
        final int n3 = 7;
        int n7;
        int n8;
        if (forEncryption) {
            for (int i = 0; i < 3; ++i) {
                int n4;
                for (int j = 0; j < 8; j = n4) {
                    final int gost28147_mainStep = this.GOST28147_mainStep(bytesToint, array[j]);
                    n4 = j + 1;
                    final int n5 = bytesToint;
                    bytesToint = (n ^ gost28147_mainStep);
                    n = n5;
                }
            }
            int n6 = n3;
            while (true) {
                n7 = n;
                n8 = bytesToint;
                if (n6 <= 0) {
                    break;
                }
                final int gost28147_mainStep2 = this.GOST28147_mainStep(bytesToint, array[n6]);
                --n6;
                final int n9 = n ^ gost28147_mainStep2;
                n = bytesToint;
                bytesToint = n9;
            }
        }
        else {
            int gost28147_mainStep3;
            int n10;
            for (int k = 0; k < 8; ++k, n10 = (n ^ gost28147_mainStep3), n = bytesToint, bytesToint = n10) {
                gost28147_mainStep3 = this.GOST28147_mainStep(bytesToint, array[k]);
            }
            int n11 = 0;
            while (true) {
                n7 = n;
                n8 = bytesToint;
                if (n11 >= 3) {
                    break;
                }
                int gost28147_mainStep4;
                int n13;
                for (int n12 = 7; n12 >= 0 && (n11 != 2 || n12 != 0); --n12, n13 = (n ^ gost28147_mainStep4), n = bytesToint, bytesToint = n13) {
                    gost28147_mainStep4 = this.GOST28147_mainStep(bytesToint, array[n12]);
                }
                ++n11;
            }
        }
        n = this.GOST28147_mainStep(n8, array[0]);
        this.intTobytes(n8, array3, n2);
        this.intTobytes(n ^ n7, array3, n2 + 4);
    }
    
    private int GOST28147_mainStep(int n, final int n2) {
        n += n2;
        final byte[] s = this.S;
        n = (s[(n >> 0 & 0xF) + 0] << 0) + (s[(n >> 4 & 0xF) + 16] << 4) + (s[(n >> 8 & 0xF) + 32] << 8) + (s[(n >> 12 & 0xF) + 48] << 12) + (s[(n >> 16 & 0xF) + 64] << 16) + (s[(n >> 20 & 0xF) + 80] << 20) + (s[(n >> 24 & 0xF) + 96] << 24) + (s[(n >> 28 & 0xF) + 112] << 28);
        return n << 11 | n >>> 21;
    }
    
    private static void addSBox(final String s, final byte[] array) {
        GOST28147Engine.sBoxes.put(Strings.toUpperCase(s), array);
    }
    
    private int bytesToint(final byte[] array, final int n) {
        return (array[n + 3] << 24 & 0xFF000000) + (array[n + 2] << 16 & 0xFF0000) + (array[n + 1] << 8 & 0xFF00) + (array[n] & 0xFF);
    }
    
    private int[] generateWorkingKey(final boolean forEncryption, final byte[] array) {
        this.forEncryption = forEncryption;
        if (array.length == 32) {
            final int[] array2 = new int[8];
            for (int i = 0; i != 8; ++i) {
                array2[i] = this.bytesToint(array, i * 4);
            }
            return array2;
        }
        throw new IllegalArgumentException("Key length invalid. Key needs to be 32 byte - 256 bit!!!");
    }
    
    public static byte[] getSBox(final String s) {
        final byte[] array = GOST28147Engine.sBoxes.get(Strings.toUpperCase(s));
        if (array != null) {
            return Arrays.clone(array);
        }
        throw new IllegalArgumentException("Unknown S-Box - possible types: \"Default\", \"E-Test\", \"E-A\", \"E-B\", \"E-C\", \"E-D\", \"D-Test\", \"D-A\".");
    }
    
    public static String getSBoxName(final byte[] array) {
        final Enumeration<String> keys = GOST28147Engine.sBoxes.keys();
        while (keys.hasMoreElements()) {
            final String s = keys.nextElement();
            if (Arrays.areEqual((byte[])GOST28147Engine.sBoxes.get(s), array)) {
                return s;
            }
        }
        throw new IllegalArgumentException("SBOX provided did not map to a known one");
    }
    
    private void intTobytes(final int n, final byte[] array, final int n2) {
        array[n2 + 3] = (byte)(n >>> 24);
        array[n2 + 2] = (byte)(n >>> 16);
        array[n2 + 1] = (byte)(n >>> 8);
        array[n2] = (byte)n;
    }
    
    @Override
    public String getAlgorithmName() {
        return "GOST28147";
    }
    
    @Override
    public int getBlockSize() {
        return 8;
    }
    
    @Override
    public void init(final boolean b, CipherParameters parameters) {
        if (parameters instanceof ParametersWithSBox) {
            final ParametersWithSBox parametersWithSBox = (ParametersWithSBox)parameters;
            final byte[] sBox = parametersWithSBox.getSBox();
            if (sBox.length != GOST28147Engine.Sbox_Default.length) {
                throw new IllegalArgumentException("invalid S-box passed to GOST28147 init");
            }
            this.S = Arrays.clone(sBox);
            if (parametersWithSBox.getParameters() == null) {
                return;
            }
            parameters = parametersWithSBox.getParameters();
        }
        else if (!(parameters instanceof KeyParameter)) {
            if (parameters == null) {
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("invalid parameter passed to GOST28147 init - ");
            sb.append(parameters.getClass().getName());
            throw new IllegalArgumentException(sb.toString());
        }
        this.workingKey = this.generateWorkingKey(b, ((KeyParameter)parameters).getKey());
    }
    
    @Override
    public int processBlock(final byte[] array, final int n, final byte[] array2, final int n2) {
        final int[] workingKey = this.workingKey;
        if (workingKey == null) {
            throw new IllegalStateException("GOST28147 engine not initialised");
        }
        if (n + 8 > array.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n2 + 8 <= array2.length) {
            this.GOST28147Func(workingKey, array, n, array2, n2);
            return 8;
        }
        throw new OutputLengthException("output buffer too short");
    }
    
    @Override
    public void reset() {
    }
}
