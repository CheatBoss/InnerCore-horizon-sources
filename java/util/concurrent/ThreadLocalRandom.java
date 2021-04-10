package java.util.concurrent;

import java.util.concurrent.atomic.*;
import sun.misc.*;
import java.security.*;
import java.util.*;
import java.util.stream.*;
import java.io.*;
import java.util.function.*;

public class ThreadLocalRandom extends Random
{
    private static final AtomicInteger probeGenerator;
    private static final AtomicLong seeder;
    private static final long GAMMA = -7046029254386353131L;
    private static final int PROBE_INCREMENT = -1640531527;
    private static final long SEEDER_INCREMENT = -4942790177534073029L;
    private static final double DOUBLE_UNIT = 1.1102230246251565E-16;
    private static final float FLOAT_UNIT = 5.9604645E-8f;
    private static final ThreadLocal<Double> nextLocalGaussian;
    boolean initialized;
    static final ThreadLocalRandom instance;
    static final String BadBound = "bound must be positive";
    static final String BadRange = "bound must be greater than origin";
    static final String BadSize = "size must be non-negative";
    private static final long serialVersionUID = -5851777807851030925L;
    private static final ObjectStreamField[] serialPersistentFields;
    private static final Unsafe UNSAFE;
    private static final long SEED;
    private static final long PROBE;
    private static final long SECONDARY;
    
    private static long initialSeed() {
        if (Boolean.parseBoolean(VM.getSavedProperty("java.util.secureRandomSeed"))) {
            final byte[] seed = SecureRandom.getSeed(8);
            long n = (long)seed[0] & 0xFFL;
            for (int i = 1; i < 8; ++i) {
                n = (n << 8 | ((long)seed[i] & 0xFFL));
            }
            return n;
        }
        return mix64(System.currentTimeMillis()) ^ mix64(System.nanoTime());
    }
    
    private static long mix64(long n) {
        n = (n ^ n >>> 33) * -49064778989728563L;
        n = (n ^ n >>> 33) * -4265267296055464877L;
        return n ^ n >>> 33;
    }
    
    private static int mix32(long n) {
        n = (n ^ n >>> 33) * -49064778989728563L;
        return (int)((n ^ n >>> 33) * -4265267296055464877L >>> 32);
    }
    
    private ThreadLocalRandom() {
        this.initialized = true;
    }
    
    static final void localInit() {
        final int addAndGet = ThreadLocalRandom.probeGenerator.addAndGet(-1640531527);
        final int n = (addAndGet == 0) ? 1 : addAndGet;
        final long mix64 = mix64(ThreadLocalRandom.seeder.getAndAdd(-4942790177534073029L));
        final Thread currentThread = Thread.currentThread();
        ThreadLocalRandom.UNSAFE.putLong(currentThread, ThreadLocalRandom.SEED, mix64);
        ThreadLocalRandom.UNSAFE.putInt(currentThread, ThreadLocalRandom.PROBE, n);
    }
    
    public static ThreadLocalRandom current() {
        if (ThreadLocalRandom.UNSAFE.getInt(Thread.currentThread(), ThreadLocalRandom.PROBE) == 0) {
            localInit();
        }
        return ThreadLocalRandom.instance;
    }
    
    @Override
    public void setSeed(final long n) {
        if (this.initialized) {
            throw new UnsupportedOperationException();
        }
    }
    
    final long nextSeed() {
        final Unsafe unsafe = ThreadLocalRandom.UNSAFE;
        final Thread currentThread = Thread.currentThread();
        final long n;
        unsafe.putLong(currentThread, ThreadLocalRandom.SEED, n = ThreadLocalRandom.UNSAFE.getLong(currentThread, ThreadLocalRandom.SEED) - 7046029254386353131L);
        return n;
    }
    
    @Override
    protected int next(final int n) {
        return (int)(mix64(this.nextSeed()) >>> 64 - n);
    }
    
    final long internalNextLong(final long n, final long n2) {
        long n3 = mix64(this.nextSeed());
        if (n < n2) {
            final long n4 = n2 - n;
            final long n5 = n4 - 1L;
            if ((n4 & n5) == 0x0L) {
                n3 = (n3 & n5) + n;
            }
            else if (n4 > 0L) {
                long n7;
                for (long n6 = n3 >>> 1; n6 + n5 - (n7 = n6 % n4) < 0L; n6 = mix64(this.nextSeed()) >>> 1) {}
                n3 = n7 + n;
            }
            else {
                while (n3 < n || n3 >= n2) {
                    n3 = mix64(this.nextSeed());
                }
            }
        }
        return n3;
    }
    
    final int internalNextInt(final int n, final int n2) {
        int n3 = mix32(this.nextSeed());
        if (n < n2) {
            final int n4 = n2 - n;
            final int n5 = n4 - 1;
            if ((n4 & n5) == 0x0) {
                n3 = (n3 & n5) + n;
            }
            else if (n4 > 0) {
                int n7;
                for (int n6 = n3 >>> 1; n6 + n5 - (n7 = n6 % n4) < 0; n6 = mix32(this.nextSeed()) >>> 1) {}
                n3 = n7 + n;
            }
            else {
                while (n3 < n || n3 >= n2) {
                    n3 = mix32(this.nextSeed());
                }
            }
        }
        return n3;
    }
    
    final double internalNextDouble(final double n, final double n2) {
        double longBitsToDouble = (this.nextLong() >>> 11) * 1.1102230246251565E-16;
        if (n < n2) {
            longBitsToDouble = longBitsToDouble * (n2 - n) + n;
            if (longBitsToDouble >= n2) {
                longBitsToDouble = Double.longBitsToDouble(Double.doubleToLongBits(n2) - 1L);
            }
        }
        return longBitsToDouble;
    }
    
    @Override
    public int nextInt() {
        return mix32(this.nextSeed());
    }
    
    @Override
    public int nextInt(final int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("bound must be positive");
        }
        final int mix32 = mix32(this.nextSeed());
        final int n2 = n - 1;
        int n3;
        if ((n & n2) == 0x0) {
            n3 = (mix32 & n2);
        }
        else {
            for (int n4 = mix32 >>> 1; n4 + n2 - (n3 = n4 % n) < 0; n4 = mix32(this.nextSeed()) >>> 1) {}
        }
        return n3;
    }
    
    public int nextInt(final int n, final int n2) {
        if (n >= n2) {
            throw new IllegalArgumentException("bound must be greater than origin");
        }
        return this.internalNextInt(n, n2);
    }
    
    @Override
    public long nextLong() {
        return mix64(this.nextSeed());
    }
    
    public long nextLong(final long n) {
        if (n <= 0L) {
            throw new IllegalArgumentException("bound must be positive");
        }
        final long mix64 = mix64(this.nextSeed());
        final long n2 = n - 1L;
        long n3;
        if ((n & n2) == 0x0L) {
            n3 = (mix64 & n2);
        }
        else {
            for (long n4 = mix64 >>> 1; n4 + n2 - (n3 = n4 % n) < 0L; n4 = mix64(this.nextSeed()) >>> 1) {}
        }
        return n3;
    }
    
    public long nextLong(final long n, final long n2) {
        if (n >= n2) {
            throw new IllegalArgumentException("bound must be greater than origin");
        }
        return this.internalNextLong(n, n2);
    }
    
    @Override
    public double nextDouble() {
        return (mix64(this.nextSeed()) >>> 11) * 1.1102230246251565E-16;
    }
    
    public double nextDouble(final double n) {
        if (n <= 0.0) {
            throw new IllegalArgumentException("bound must be positive");
        }
        final double n2 = (mix64(this.nextSeed()) >>> 11) * 1.1102230246251565E-16 * n;
        return (n2 < n) ? n2 : Double.longBitsToDouble(Double.doubleToLongBits(n) - 1L);
    }
    
    public double nextDouble(final double n, final double n2) {
        if (n >= n2) {
            throw new IllegalArgumentException("bound must be greater than origin");
        }
        return this.internalNextDouble(n, n2);
    }
    
    @Override
    public boolean nextBoolean() {
        return mix32(this.nextSeed()) < 0;
    }
    
    @Override
    public float nextFloat() {
        return (mix32(this.nextSeed()) >>> 8) * 5.9604645E-8f;
    }
    
    @Override
    public double nextGaussian() {
        final Double n = ThreadLocalRandom.nextLocalGaussian.get();
        if (n != null) {
            ThreadLocalRandom.nextLocalGaussian.set(null);
            return n;
        }
        double n2;
        double n3;
        double n4;
        do {
            n3 = 2.0 * this.nextDouble() - 1.0;
            n4 = 2.0 * this.nextDouble() - 1.0;
            n2 = n3 * n3 + n4 * n4;
        } while (n2 >= 1.0 || n2 == 0.0);
        final double sqrt = StrictMath.sqrt(-2.0 * StrictMath.log(n2) / n2);
        ThreadLocalRandom.nextLocalGaussian.set(new Double(n4 * sqrt));
        return n3 * sqrt;
    }
    
    @Override
    public IntStream ints(final long n) {
        if (n < 0L) {
            throw new IllegalArgumentException("size must be non-negative");
        }
        return StreamSupport.intStream(new RandomIntsSpliterator(0L, n, Integer.MAX_VALUE, 0), false);
    }
    
    @Override
    public IntStream ints() {
        return StreamSupport.intStream(new RandomIntsSpliterator(0L, Long.MAX_VALUE, Integer.MAX_VALUE, 0), false);
    }
    
    @Override
    public IntStream ints(final long n, final int n2, final int n3) {
        if (n < 0L) {
            throw new IllegalArgumentException("size must be non-negative");
        }
        if (n2 >= n3) {
            throw new IllegalArgumentException("bound must be greater than origin");
        }
        return StreamSupport.intStream(new RandomIntsSpliterator(0L, n, n2, n3), false);
    }
    
    @Override
    public IntStream ints(final int n, final int n2) {
        if (n >= n2) {
            throw new IllegalArgumentException("bound must be greater than origin");
        }
        return StreamSupport.intStream(new RandomIntsSpliterator(0L, Long.MAX_VALUE, n, n2), false);
    }
    
    @Override
    public LongStream longs(final long n) {
        if (n < 0L) {
            throw new IllegalArgumentException("size must be non-negative");
        }
        return StreamSupport.longStream(new RandomLongsSpliterator(0L, n, Long.MAX_VALUE, 0L), false);
    }
    
    @Override
    public LongStream longs() {
        return StreamSupport.longStream(new RandomLongsSpliterator(0L, Long.MAX_VALUE, Long.MAX_VALUE, 0L), false);
    }
    
    @Override
    public LongStream longs(final long n, final long n2, final long n3) {
        if (n < 0L) {
            throw new IllegalArgumentException("size must be non-negative");
        }
        if (n2 >= n3) {
            throw new IllegalArgumentException("bound must be greater than origin");
        }
        return StreamSupport.longStream(new RandomLongsSpliterator(0L, n, n2, n3), false);
    }
    
    @Override
    public LongStream longs(final long n, final long n2) {
        if (n >= n2) {
            throw new IllegalArgumentException("bound must be greater than origin");
        }
        return StreamSupport.longStream(new RandomLongsSpliterator(0L, Long.MAX_VALUE, n, n2), false);
    }
    
    @Override
    public DoubleStream doubles(final long n) {
        if (n < 0L) {
            throw new IllegalArgumentException("size must be non-negative");
        }
        return StreamSupport.doubleStream(new RandomDoublesSpliterator(0L, n, Double.MAX_VALUE, 0.0), false);
    }
    
    @Override
    public DoubleStream doubles() {
        return StreamSupport.doubleStream(new RandomDoublesSpliterator(0L, Long.MAX_VALUE, Double.MAX_VALUE, 0.0), false);
    }
    
    @Override
    public DoubleStream doubles(final long n, final double n2, final double n3) {
        if (n < 0L) {
            throw new IllegalArgumentException("size must be non-negative");
        }
        if (n2 >= n3) {
            throw new IllegalArgumentException("bound must be greater than origin");
        }
        return StreamSupport.doubleStream(new RandomDoublesSpliterator(0L, n, n2, n3), false);
    }
    
    @Override
    public DoubleStream doubles(final double n, final double n2) {
        if (n >= n2) {
            throw new IllegalArgumentException("bound must be greater than origin");
        }
        return StreamSupport.doubleStream(new RandomDoublesSpliterator(0L, Long.MAX_VALUE, n, n2), false);
    }
    
    static final int getProbe() {
        return ThreadLocalRandom.UNSAFE.getInt(Thread.currentThread(), ThreadLocalRandom.PROBE);
    }
    
    static final int advanceProbe(int n) {
        n ^= n << 13;
        n ^= n >>> 17;
        n ^= n << 5;
        ThreadLocalRandom.UNSAFE.putInt(Thread.currentThread(), ThreadLocalRandom.PROBE, n);
        return n;
    }
    
    static final int nextSecondarySeed() {
        final Thread currentThread = Thread.currentThread();
        final int int1;
        int n3;
        if ((int1 = ThreadLocalRandom.UNSAFE.getInt(currentThread, ThreadLocalRandom.SECONDARY)) != 0) {
            final int n = int1 ^ int1 << 13;
            final int n2 = n ^ n >>> 17;
            n3 = (n2 ^ n2 << 5);
        }
        else {
            localInit();
            if ((n3 = (int)ThreadLocalRandom.UNSAFE.getLong(currentThread, ThreadLocalRandom.SEED)) == 0) {
                n3 = 1;
            }
        }
        ThreadLocalRandom.UNSAFE.putInt(currentThread, ThreadLocalRandom.SECONDARY, n3);
        return n3;
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        final ObjectOutputStream.PutField putFields = objectOutputStream.putFields();
        putFields.put("rnd", ThreadLocalRandom.UNSAFE.getLong(Thread.currentThread(), ThreadLocalRandom.SEED));
        putFields.put("initialized", true);
        objectOutputStream.writeFields();
    }
    
    private Object readResolve() {
        return current();
    }
    
    static {
        probeGenerator = new AtomicInteger();
        seeder = new AtomicLong(initialSeed());
        nextLocalGaussian = new ThreadLocal<Double>();
        instance = new ThreadLocalRandom();
        serialPersistentFields = new ObjectStreamField[] { new ObjectStreamField("rnd", Long.TYPE), new ObjectStreamField("initialized", Boolean.TYPE) };
        try {
            UNSAFE = Unsafe.getUnsafe();
            final Class<Thread> clazz = Thread.class;
            SEED = ThreadLocalRandom.UNSAFE.objectFieldOffset(clazz.getDeclaredField("threadLocalRandomSeed"));
            PROBE = ThreadLocalRandom.UNSAFE.objectFieldOffset(clazz.getDeclaredField("threadLocalRandomProbe"));
            SECONDARY = ThreadLocalRandom.UNSAFE.objectFieldOffset(clazz.getDeclaredField("threadLocalRandomSecondarySeed"));
        }
        catch (Exception ex) {
            throw new Error(ex);
        }
    }
    
    static final class RandomDoublesSpliterator implements Spliterator.OfDouble
    {
        long index;
        final long fence;
        final double origin;
        final double bound;
        
        RandomDoublesSpliterator(final long index, final long fence, final double origin, final double bound) {
            this.index = index;
            this.fence = fence;
            this.origin = origin;
            this.bound = bound;
        }
        
        @Override
        public RandomDoublesSpliterator trySplit() {
            final long index = this.index;
            final long index2 = index + this.fence >>> 1;
            return (index2 <= index) ? null : new RandomDoublesSpliterator(index, this.index = index2, this.origin, this.bound);
        }
        
        @Override
        public long estimateSize() {
            return this.fence - this.index;
        }
        
        @Override
        public int characteristics() {
            return 17728;
        }
        
        @Override
        public boolean tryAdvance(final DoubleConsumer doubleConsumer) {
            if (doubleConsumer == null) {
                throw new NullPointerException();
            }
            final long index = this.index;
            if (index < this.fence) {
                doubleConsumer.accept(ThreadLocalRandom.current().internalNextDouble(this.origin, this.bound));
                this.index = index + 1L;
                return true;
            }
            return false;
        }
        
        @Override
        public void forEachRemaining(final DoubleConsumer doubleConsumer) {
            if (doubleConsumer == null) {
                throw new NullPointerException();
            }
            long index = this.index;
            final long fence = this.fence;
            if (index < fence) {
                this.index = fence;
                final double origin = this.origin;
                final double bound = this.bound;
                final ThreadLocalRandom current = ThreadLocalRandom.current();
                do {
                    doubleConsumer.accept(current.internalNextDouble(origin, bound));
                } while (++index < fence);
            }
        }
    }
    
    static final class RandomIntsSpliterator implements Spliterator.OfInt
    {
        long index;
        final long fence;
        final int origin;
        final int bound;
        
        RandomIntsSpliterator(final long index, final long fence, final int origin, final int bound) {
            this.index = index;
            this.fence = fence;
            this.origin = origin;
            this.bound = bound;
        }
        
        @Override
        public RandomIntsSpliterator trySplit() {
            final long index = this.index;
            final long index2 = index + this.fence >>> 1;
            return (index2 <= index) ? null : new RandomIntsSpliterator(index, this.index = index2, this.origin, this.bound);
        }
        
        @Override
        public long estimateSize() {
            return this.fence - this.index;
        }
        
        @Override
        public int characteristics() {
            return 17728;
        }
        
        @Override
        public boolean tryAdvance(final IntConsumer intConsumer) {
            if (intConsumer == null) {
                throw new NullPointerException();
            }
            final long index = this.index;
            if (index < this.fence) {
                intConsumer.accept(ThreadLocalRandom.current().internalNextInt(this.origin, this.bound));
                this.index = index + 1L;
                return true;
            }
            return false;
        }
        
        @Override
        public void forEachRemaining(final IntConsumer intConsumer) {
            if (intConsumer == null) {
                throw new NullPointerException();
            }
            long index = this.index;
            final long fence = this.fence;
            if (index < fence) {
                this.index = fence;
                final int origin = this.origin;
                final int bound = this.bound;
                final ThreadLocalRandom current = ThreadLocalRandom.current();
                do {
                    intConsumer.accept(current.internalNextInt(origin, bound));
                } while (++index < fence);
            }
        }
    }
    
    static final class RandomLongsSpliterator implements Spliterator.OfLong
    {
        long index;
        final long fence;
        final long origin;
        final long bound;
        
        RandomLongsSpliterator(final long index, final long fence, final long origin, final long bound) {
            this.index = index;
            this.fence = fence;
            this.origin = origin;
            this.bound = bound;
        }
        
        @Override
        public RandomLongsSpliterator trySplit() {
            final long index = this.index;
            final long index2 = index + this.fence >>> 1;
            return (index2 <= index) ? null : new RandomLongsSpliterator(index, this.index = index2, this.origin, this.bound);
        }
        
        @Override
        public long estimateSize() {
            return this.fence - this.index;
        }
        
        @Override
        public int characteristics() {
            return 17728;
        }
        
        @Override
        public boolean tryAdvance(final LongConsumer longConsumer) {
            if (longConsumer == null) {
                throw new NullPointerException();
            }
            final long index = this.index;
            if (index < this.fence) {
                longConsumer.accept(ThreadLocalRandom.current().internalNextLong(this.origin, this.bound));
                this.index = index + 1L;
                return true;
            }
            return false;
        }
        
        @Override
        public void forEachRemaining(final LongConsumer longConsumer) {
            if (longConsumer == null) {
                throw new NullPointerException();
            }
            long index = this.index;
            final long fence = this.fence;
            if (index < fence) {
                this.index = fence;
                final long origin = this.origin;
                final long bound = this.bound;
                final ThreadLocalRandom current = ThreadLocalRandom.current();
                do {
                    longConsumer.accept(current.internalNextLong(origin, bound));
                } while (++index < fence);
            }
        }
    }
}
