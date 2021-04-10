package org.spongycastle.util.test;

import java.math.*;
import java.util.*;
import java.security.*;
import java.io.*;
import org.spongycastle.util.*;
import org.spongycastle.util.encoders.*;

public class FixedSecureRandom extends SecureRandom
{
    private static java.math.BigInteger ANDROID;
    private static java.math.BigInteger CLASSPATH;
    private static java.math.BigInteger REGULAR;
    private static final boolean isAndroidStyle;
    private static final boolean isClasspathStyle;
    private static final boolean isRegularStyle;
    private byte[] _data;
    private int _index;
    
    static {
        FixedSecureRandom.REGULAR = new java.math.BigInteger("01020304ffffffff0506070811111111", 16);
        FixedSecureRandom.ANDROID = new java.math.BigInteger("1111111105060708ffffffff01020304", 16);
        FixedSecureRandom.CLASSPATH = new java.math.BigInteger("3020104ffffffff05060708111111", 16);
        final java.math.BigInteger bigInteger = new java.math.BigInteger(128, new RandomChecker());
        final java.math.BigInteger bigInteger2 = new java.math.BigInteger(120, new RandomChecker());
        isAndroidStyle = bigInteger.equals(FixedSecureRandom.ANDROID);
        isRegularStyle = bigInteger.equals(FixedSecureRandom.REGULAR);
        isClasspathStyle = bigInteger2.equals(FixedSecureRandom.CLASSPATH);
    }
    
    public FixedSecureRandom(final byte[] array) {
        this(new Source[] { (Source)new Data(array) });
    }
    
    public FixedSecureRandom(final Source[] array) {
        super(null, new DummyProvider());
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final boolean isRegularStyle = FixedSecureRandom.isRegularStyle;
        int i = 0;
        final int n = 0;
        Label_0366: {
            Label_0198: {
                if (isRegularStyle) {
                    Label_0163: {
                        if (FixedSecureRandom.isClasspathStyle) {
                            int j = n;
                            while (j != array.length) {
                                try {
                                    if (array[j] instanceof BigInteger) {
                                        final byte[] data = array[j].data;
                                        final int n2 = data.length - data.length % 4;
                                        int n3 = data.length - n2;
                                        while (true) {
                                            --n3;
                                            if (n3 < 0) {
                                                break;
                                            }
                                            byteArrayOutputStream.write(data[n3]);
                                        }
                                        for (int k = data.length - n2; k < data.length; k += 4) {
                                            byteArrayOutputStream.write(data, k, 4);
                                        }
                                    }
                                    else {
                                        byteArrayOutputStream.write(array[j].data);
                                    }
                                    ++j;
                                    continue;
                                }
                                catch (IOException ex) {
                                    throw new IllegalArgumentException("can't save value source.");
                                }
                                break Label_0163;
                            }
                            break Label_0366;
                        }
                    }
                    while (i != array.length) {
                        try {
                            byteArrayOutputStream.write(array[i].data);
                            ++i;
                            continue;
                        }
                        catch (IOException ex2) {
                            throw new IllegalArgumentException("can't save value source.");
                        }
                        break Label_0198;
                    }
                    break Label_0366;
                }
            }
            if (!FixedSecureRandom.isAndroidStyle) {
                throw new IllegalStateException("Unrecognized BigInteger implementation");
            }
            for (int l = 0; l != array.length; ++l) {
            Label_0308_Outer:
                while (true) {
                    while (true) {
                        Label_0386: {
                            try {
                                if (!(array[l] instanceof BigInteger)) {
                                    byteArrayOutputStream.write(array[l].data);
                                    Label_0348: {
                                        break;
                                    }
                                }
                                final byte[] data2 = array[l].data;
                                final int n4 = data2.length - data2.length % 4;
                                int n5 = 0;
                                while (n5 < n4) {
                                    final int length = data2.length;
                                    n5 += 4;
                                    byteArrayOutputStream.write(data2, length - n5, 4);
                                }
                                if (data2.length - n4 != 0) {
                                    for (int n6 = 0; n6 != 4 - (data2.length - n4); ++n6) {
                                        byteArrayOutputStream.write(0);
                                    }
                                }
                                break Label_0386;
                                while (true) {
                                    final int n7;
                                    byteArrayOutputStream.write(data2[n4 + n7]);
                                    ++n7;
                                    continue Label_0308_Outer;
                                }
                            }
                            // iftrue(Label_0348:, n7 == data2.length - n4)
                            catch (IOException ex3) {
                                throw new IllegalArgumentException("can't save value source.");
                            }
                            break Label_0366;
                        }
                        int n7 = 0;
                        continue;
                    }
                }
            }
        }
        this._data = byteArrayOutputStream.toByteArray();
    }
    
    public FixedSecureRandom(final byte[][] array) {
        this((Source[])buildDataArray(array));
    }
    
    private static Data[] buildDataArray(final byte[][] array) {
        final Data[] array2 = new Data[array.length];
        for (int i = 0; i != array.length; ++i) {
            array2[i] = new Data(array[i]);
        }
        return array2;
    }
    
    private static byte[] expandToBitLength(int n, final byte[] array) {
        final int n2 = (n + 7) / 8;
        if (n2 > array.length) {
            final byte[] array2 = new byte[n2];
            System.arraycopy(array, 0, array2, n2 - array.length, array.length);
            if (FixedSecureRandom.isAndroidStyle) {
                n %= 8;
                if (n != 0) {
                    Pack.intToBigEndian(Pack.bigEndianToInt(array2, 0) << 8 - n, array2, 0);
                }
            }
            return array2;
        }
        if (FixedSecureRandom.isAndroidStyle && n < array.length * 8) {
            n %= 8;
            if (n != 0) {
                Pack.intToBigEndian(Pack.bigEndianToInt(array, 0) << 8 - n, array, 0);
            }
        }
        return array;
    }
    
    private int nextValue() {
        return this._data[this._index++] & 0xFF;
    }
    
    @Override
    public byte[] generateSeed(final int n) {
        final byte[] array = new byte[n];
        this.nextBytes(array);
        return array;
    }
    
    public boolean isExhausted() {
        return this._index == this._data.length;
    }
    
    @Override
    public void nextBytes(final byte[] array) {
        System.arraycopy(this._data, this._index, array, 0, array.length);
        this._index += array.length;
    }
    
    @Override
    public int nextInt() {
        return this.nextValue() << 24 | 0x0 | this.nextValue() << 16 | this.nextValue() << 8 | this.nextValue();
    }
    
    @Override
    public long nextLong() {
        return (long)this.nextValue() << 56 | 0x0L | (long)this.nextValue() << 48 | (long)this.nextValue() << 40 | (long)this.nextValue() << 32 | (long)this.nextValue() << 24 | (long)this.nextValue() << 16 | (long)this.nextValue() << 8 | (long)this.nextValue();
    }
    
    public static class BigInteger extends Source
    {
        public BigInteger(final int n, final String s) {
            super(expandToBitLength(n, Hex.decode(s)));
        }
        
        public BigInteger(final int n, final byte[] array) {
            super(expandToBitLength(n, array));
        }
        
        public BigInteger(final String s) {
            this(Hex.decode(s));
        }
        
        public BigInteger(final byte[] array) {
            super(array);
        }
    }
    
    public static class Data extends Source
    {
        public Data(final byte[] array) {
            super(array);
        }
    }
    
    private static class DummyProvider extends Provider
    {
        DummyProvider() {
            super("BCFIPS_FIXED_RNG", 1.0, "BCFIPS Fixed Secure Random Provider");
        }
    }
    
    private static class RandomChecker extends SecureRandom
    {
        byte[] data;
        int index;
        
        RandomChecker() {
            super(null, new DummyProvider());
            this.data = Hex.decode("01020304ffffffff0506070811111111");
            this.index = 0;
        }
        
        @Override
        public void nextBytes(final byte[] array) {
            System.arraycopy(this.data, this.index, array, 0, array.length);
            this.index += array.length;
        }
    }
    
    public static class Source
    {
        byte[] data;
        
        Source(final byte[] data) {
            this.data = data;
        }
    }
}
