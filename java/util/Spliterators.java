package java.util;

import java.util.function.*;

public final class Spliterators
{
    private static final Spliterator<Object> EMPTY_SPLITERATOR;
    private static final Spliterator.OfInt EMPTY_INT_SPLITERATOR;
    private static final Spliterator.OfLong EMPTY_LONG_SPLITERATOR;
    private static final Spliterator.OfDouble EMPTY_DOUBLE_SPLITERATOR;
    
    private Spliterators() {
    }
    
    public static <T> Spliterator<T> emptySpliterator() {
        return (Spliterator<T>)Spliterators.EMPTY_SPLITERATOR;
    }
    
    public static Spliterator.OfInt emptyIntSpliterator() {
        return Spliterators.EMPTY_INT_SPLITERATOR;
    }
    
    public static Spliterator.OfLong emptyLongSpliterator() {
        return Spliterators.EMPTY_LONG_SPLITERATOR;
    }
    
    public static Spliterator.OfDouble emptyDoubleSpliterator() {
        return Spliterators.EMPTY_DOUBLE_SPLITERATOR;
    }
    
    public static <T> Spliterator<T> spliterator(final Object[] array, final int n) {
        return new ArraySpliterator<T>(Objects.requireNonNull(array), n);
    }
    
    public static <T> Spliterator<T> spliterator(final Object[] array, final int n, final int n2, final int n3) {
        checkFromToBounds(Objects.requireNonNull(array).length, n, n2);
        return new ArraySpliterator<T>(array, n, n2, n3);
    }
    
    public static Spliterator.OfInt spliterator(final int[] array, final int n) {
        return new IntArraySpliterator(Objects.requireNonNull(array), n);
    }
    
    public static Spliterator.OfInt spliterator(final int[] array, final int n, final int n2, final int n3) {
        checkFromToBounds(Objects.requireNonNull(array).length, n, n2);
        return new IntArraySpliterator(array, n, n2, n3);
    }
    
    public static Spliterator.OfLong spliterator(final long[] array, final int n) {
        return new LongArraySpliterator(Objects.requireNonNull(array), n);
    }
    
    public static Spliterator.OfLong spliterator(final long[] array, final int n, final int n2, final int n3) {
        checkFromToBounds(Objects.requireNonNull(array).length, n, n2);
        return new LongArraySpliterator(array, n, n2, n3);
    }
    
    public static Spliterator.OfDouble spliterator(final double[] array, final int n) {
        return new DoubleArraySpliterator(Objects.requireNonNull(array), n);
    }
    
    public static Spliterator.OfDouble spliterator(final double[] array, final int n, final int n2, final int n3) {
        checkFromToBounds(Objects.requireNonNull(array).length, n, n2);
        return new DoubleArraySpliterator(array, n, n2, n3);
    }
    
    private static void checkFromToBounds(final int n, final int n2, final int n3) {
        if (n2 > n3) {
            throw new ArrayIndexOutOfBoundsException("origin(" + n2 + ") > fence(" + n3 + ")");
        }
        if (n2 < 0) {
            throw new ArrayIndexOutOfBoundsException(n2);
        }
        if (n3 > n) {
            throw new ArrayIndexOutOfBoundsException(n3);
        }
    }
    
    public static <T> Spliterator<T> spliterator(final Collection<? extends T> collection, final int n) {
        return new IteratorSpliterator<T>(Objects.requireNonNull(collection), n);
    }
    
    public static <T> Spliterator<T> spliterator(final Iterator<? extends T> iterator, final long n, final int n2) {
        return new IteratorSpliterator<T>(Objects.requireNonNull(iterator), n, n2);
    }
    
    public static <T> Spliterator<T> spliteratorUnknownSize(final Iterator<? extends T> iterator, final int n) {
        return new IteratorSpliterator<T>(Objects.requireNonNull(iterator), n);
    }
    
    public static Spliterator.OfInt spliterator(final PrimitiveIterator.OfInt ofInt, final long n, final int n2) {
        return new IntIteratorSpliterator(Objects.requireNonNull(ofInt), n, n2);
    }
    
    public static Spliterator.OfInt spliteratorUnknownSize(final PrimitiveIterator.OfInt ofInt, final int n) {
        return new IntIteratorSpliterator(Objects.requireNonNull(ofInt), n);
    }
    
    public static Spliterator.OfLong spliterator(final PrimitiveIterator.OfLong ofLong, final long n, final int n2) {
        return new LongIteratorSpliterator(Objects.requireNonNull(ofLong), n, n2);
    }
    
    public static Spliterator.OfLong spliteratorUnknownSize(final PrimitiveIterator.OfLong ofLong, final int n) {
        return new LongIteratorSpliterator(Objects.requireNonNull(ofLong), n);
    }
    
    public static Spliterator.OfDouble spliterator(final PrimitiveIterator.OfDouble ofDouble, final long n, final int n2) {
        return new DoubleIteratorSpliterator(Objects.requireNonNull(ofDouble), n, n2);
    }
    
    public static Spliterator.OfDouble spliteratorUnknownSize(final PrimitiveIterator.OfDouble ofDouble, final int n) {
        return new DoubleIteratorSpliterator(Objects.requireNonNull(ofDouble), n);
    }
    
    public static <T> Iterator<T> iterator(final Spliterator<? extends T> spliterator) {
        Objects.requireNonNull(spliterator);
        class Adapter implements Iterator<T>, Consumer<T>
        {
            boolean valueReady;
            T nextElement;
            
            Adapter() {
                this.valueReady = false;
            }
            
            @Override
            public void accept(final T nextElement) {
                this.valueReady = true;
                this.nextElement = nextElement;
            }
            
            @Override
            public boolean hasNext() {
                if (!this.valueReady) {
                    spliterator.tryAdvance(this);
                }
                return this.valueReady;
            }
            
            @Override
            public T next() {
                if (!this.valueReady && !this.hasNext()) {
                    throw new NoSuchElementException();
                }
                this.valueReady = false;
                return this.nextElement;
            }
        }
        return new Adapter();
    }
    
    public static PrimitiveIterator.OfInt iterator(final Spliterator.OfInt ofInt) {
        Objects.requireNonNull(ofInt);
        class Adapter implements PrimitiveIterator.OfInt, IntConsumer
        {
            boolean valueReady;
            int nextElement;
            
            Adapter() {
                this.valueReady = false;
            }
            
            @Override
            public void accept(final int nextElement) {
                this.valueReady = true;
                this.nextElement = nextElement;
            }
            
            @Override
            public boolean hasNext() {
                if (!this.valueReady) {
                    ofInt.tryAdvance((IntConsumer)this);
                }
                return this.valueReady;
            }
            
            @Override
            public int nextInt() {
                if (!this.valueReady && !this.hasNext()) {
                    throw new NoSuchElementException();
                }
                this.valueReady = false;
                return this.nextElement;
            }
        }
        return new Adapter();
    }
    
    public static PrimitiveIterator.OfLong iterator(final Spliterator.OfLong ofLong) {
        Objects.requireNonNull(ofLong);
        class Adapter implements PrimitiveIterator.OfLong, LongConsumer
        {
            boolean valueReady;
            long nextElement;
            
            Adapter() {
                this.valueReady = false;
            }
            
            @Override
            public void accept(final long nextElement) {
                this.valueReady = true;
                this.nextElement = nextElement;
            }
            
            @Override
            public boolean hasNext() {
                if (!this.valueReady) {
                    ofLong.tryAdvance((LongConsumer)this);
                }
                return this.valueReady;
            }
            
            @Override
            public long nextLong() {
                if (!this.valueReady && !this.hasNext()) {
                    throw new NoSuchElementException();
                }
                this.valueReady = false;
                return this.nextElement;
            }
        }
        return new Adapter();
    }
    
    public static PrimitiveIterator.OfDouble iterator(final Spliterator.OfDouble ofDouble) {
        Objects.requireNonNull(ofDouble);
        class Adapter implements PrimitiveIterator.OfDouble, DoubleConsumer
        {
            boolean valueReady;
            double nextElement;
            
            Adapter() {
                this.valueReady = false;
            }
            
            @Override
            public void accept(final double nextElement) {
                this.valueReady = true;
                this.nextElement = nextElement;
            }
            
            @Override
            public boolean hasNext() {
                if (!this.valueReady) {
                    ofDouble.tryAdvance((DoubleConsumer)this);
                }
                return this.valueReady;
            }
            
            @Override
            public double nextDouble() {
                if (!this.valueReady && !this.hasNext()) {
                    throw new NoSuchElementException();
                }
                this.valueReady = false;
                return this.nextElement;
            }
        }
        return new Adapter();
    }
    
    static {
        EMPTY_SPLITERATOR = new EmptySpliterator.OfRef<Object>();
        EMPTY_INT_SPLITERATOR = new EmptySpliterator.OfInt();
        EMPTY_LONG_SPLITERATOR = new EmptySpliterator.OfLong();
        EMPTY_DOUBLE_SPLITERATOR = new EmptySpliterator.OfDouble();
    }
    
    public abstract static class AbstractDoubleSpliterator implements Spliterator.OfDouble
    {
        static final int MAX_BATCH = 33554432;
        static final int BATCH_UNIT = 1024;
        private final int characteristics;
        private long est;
        private int batch;
        
        protected AbstractDoubleSpliterator(final long est, final int n) {
            this.est = est;
            this.characteristics = (((n & 0x40) != 0x0) ? (n | 0x4000) : n);
        }
        
        @Override
        public Spliterator.OfDouble trySplit() {
            final HoldingDoubleConsumer holdingDoubleConsumer = new HoldingDoubleConsumer();
            final long est = this.est;
            if (est > 1L && this.tryAdvance((DoubleConsumer)holdingDoubleConsumer)) {
                int n = this.batch + 1024;
                if (n > est) {
                    n = (int)est;
                }
                if (n > 33554432) {
                    n = 33554432;
                }
                final double[] array = new double[n];
                int batch = 0;
                do {
                    array[batch] = holdingDoubleConsumer.value;
                } while (++batch < n && this.tryAdvance((DoubleConsumer)holdingDoubleConsumer));
                this.batch = batch;
                if (this.est != Long.MAX_VALUE) {
                    this.est -= batch;
                }
                return new DoubleArraySpliterator(array, 0, batch, this.characteristics());
            }
            return null;
        }
        
        @Override
        public long estimateSize() {
            return this.est;
        }
        
        @Override
        public int characteristics() {
            return this.characteristics;
        }
        
        static final class HoldingDoubleConsumer implements DoubleConsumer
        {
            double value;
            
            @Override
            public void accept(final double value) {
                this.value = value;
            }
        }
    }
    
    static final class DoubleArraySpliterator implements Spliterator.OfDouble
    {
        private final double[] array;
        private int index;
        private final int fence;
        private final int characteristics;
        
        public DoubleArraySpliterator(final double[] array, final int n) {
            this(array, 0, array.length, n);
        }
        
        public DoubleArraySpliterator(final double[] array, final int index, final int fence, final int n) {
            this.array = array;
            this.index = index;
            this.fence = fence;
            this.characteristics = (n | 0x40 | 0x4000);
        }
        
        @Override
        public Spliterator.OfDouble trySplit() {
            final int index = this.index;
            final int index2 = index + this.fence >>> 1;
            return (index >= index2) ? null : new DoubleArraySpliterator(this.array, index, this.index = index2, this.characteristics);
        }
        
        @Override
        public void forEachRemaining(final DoubleConsumer doubleConsumer) {
            if (doubleConsumer == null) {
                throw new NullPointerException();
            }
            final double[] array;
            final int fence;
            int index;
            if ((array = this.array).length >= (fence = this.fence) && (index = this.index) >= 0 && index < (this.index = fence)) {
                do {
                    doubleConsumer.accept(array[index]);
                } while (++index < fence);
            }
        }
        
        @Override
        public boolean tryAdvance(final DoubleConsumer doubleConsumer) {
            if (doubleConsumer == null) {
                throw new NullPointerException();
            }
            if (this.index >= 0 && this.index < this.fence) {
                doubleConsumer.accept(this.array[this.index++]);
                return true;
            }
            return false;
        }
        
        @Override
        public long estimateSize() {
            return this.fence - this.index;
        }
        
        @Override
        public int characteristics() {
            return this.characteristics;
        }
        
        @Override
        public Comparator<? super Double> getComparator() {
            if (this.hasCharacteristics(4)) {
                return null;
            }
            throw new IllegalStateException();
        }
    }
    
    public abstract static class AbstractSpliterator<T> implements Spliterator<T>
    {
        static final int BATCH_UNIT = 1024;
        static final int MAX_BATCH = 33554432;
        private final int characteristics;
        private long est;
        private int batch;
        
        protected AbstractSpliterator(final long est, final int n) {
            this.est = est;
            this.characteristics = (((n & 0x40) != 0x0) ? (n | 0x4000) : n);
        }
        
        @Override
        public Spliterator<T> trySplit() {
            final HoldingConsumer<Object> holdingConsumer = new HoldingConsumer<Object>();
            final long est = this.est;
            if (est > 1L && this.tryAdvance(holdingConsumer)) {
                int n = this.batch + 1024;
                if (n > est) {
                    n = (int)est;
                }
                if (n > 33554432) {
                    n = 33554432;
                }
                final Object[] array = new Object[n];
                int batch = 0;
                do {
                    array[batch] = holdingConsumer.value;
                } while (++batch < n && this.tryAdvance(holdingConsumer));
                this.batch = batch;
                if (this.est != Long.MAX_VALUE) {
                    this.est -= batch;
                }
                return new ArraySpliterator<T>(array, 0, batch, this.characteristics());
            }
            return null;
        }
        
        @Override
        public long estimateSize() {
            return this.est;
        }
        
        @Override
        public int characteristics() {
            return this.characteristics;
        }
        
        static final class HoldingConsumer<T> implements Consumer<T>
        {
            Object value;
            
            @Override
            public void accept(final T value) {
                this.value = value;
            }
        }
    }
    
    static final class ArraySpliterator<T> implements Spliterator<T>
    {
        private final Object[] array;
        private int index;
        private final int fence;
        private final int characteristics;
        
        public ArraySpliterator(final Object[] array, final int n) {
            this(array, 0, array.length, n);
        }
        
        public ArraySpliterator(final Object[] array, final int index, final int fence, final int n) {
            this.array = array;
            this.index = index;
            this.fence = fence;
            this.characteristics = (n | 0x40 | 0x4000);
        }
        
        @Override
        public Spliterator<T> trySplit() {
            final int index = this.index;
            final int index2 = index + this.fence >>> 1;
            return (index >= index2) ? null : new ArraySpliterator(this.array, index, this.index = index2, this.characteristics);
        }
        
        @Override
        public void forEachRemaining(final Consumer<? super T> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            final Object[] array;
            final int fence;
            int index;
            if ((array = this.array).length >= (fence = this.fence) && (index = this.index) >= 0 && index < (this.index = fence)) {
                do {
                    consumer.accept((Object)array[index]);
                } while (++index < fence);
            }
        }
        
        @Override
        public boolean tryAdvance(final Consumer<? super T> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            if (this.index >= 0 && this.index < this.fence) {
                consumer.accept((Object)this.array[this.index++]);
                return true;
            }
            return false;
        }
        
        @Override
        public long estimateSize() {
            return this.fence - this.index;
        }
        
        @Override
        public int characteristics() {
            return this.characteristics;
        }
        
        @Override
        public Comparator<? super T> getComparator() {
            if (this.hasCharacteristics(4)) {
                return null;
            }
            throw new IllegalStateException();
        }
    }
    
    public abstract static class AbstractIntSpliterator implements Spliterator.OfInt
    {
        static final int MAX_BATCH = 33554432;
        static final int BATCH_UNIT = 1024;
        private final int characteristics;
        private long est;
        private int batch;
        
        protected AbstractIntSpliterator(final long est, final int n) {
            this.est = est;
            this.characteristics = (((n & 0x40) != 0x0) ? (n | 0x4000) : n);
        }
        
        @Override
        public Spliterator.OfInt trySplit() {
            final HoldingIntConsumer holdingIntConsumer = new HoldingIntConsumer();
            final long est = this.est;
            if (est > 1L && this.tryAdvance((IntConsumer)holdingIntConsumer)) {
                int n = this.batch + 1024;
                if (n > est) {
                    n = (int)est;
                }
                if (n > 33554432) {
                    n = 33554432;
                }
                final int[] array = new int[n];
                int batch = 0;
                do {
                    array[batch] = holdingIntConsumer.value;
                } while (++batch < n && this.tryAdvance((IntConsumer)holdingIntConsumer));
                this.batch = batch;
                if (this.est != Long.MAX_VALUE) {
                    this.est -= batch;
                }
                return new IntArraySpliterator(array, 0, batch, this.characteristics());
            }
            return null;
        }
        
        @Override
        public long estimateSize() {
            return this.est;
        }
        
        @Override
        public int characteristics() {
            return this.characteristics;
        }
        
        static final class HoldingIntConsumer implements IntConsumer
        {
            int value;
            
            @Override
            public void accept(final int value) {
                this.value = value;
            }
        }
    }
    
    static final class IntArraySpliterator implements Spliterator.OfInt
    {
        private final int[] array;
        private int index;
        private final int fence;
        private final int characteristics;
        
        public IntArraySpliterator(final int[] array, final int n) {
            this(array, 0, array.length, n);
        }
        
        public IntArraySpliterator(final int[] array, final int index, final int fence, final int n) {
            this.array = array;
            this.index = index;
            this.fence = fence;
            this.characteristics = (n | 0x40 | 0x4000);
        }
        
        @Override
        public Spliterator.OfInt trySplit() {
            final int index = this.index;
            final int index2 = index + this.fence >>> 1;
            return (index >= index2) ? null : new IntArraySpliterator(this.array, index, this.index = index2, this.characteristics);
        }
        
        @Override
        public void forEachRemaining(final IntConsumer intConsumer) {
            if (intConsumer == null) {
                throw new NullPointerException();
            }
            final int[] array;
            final int fence;
            int index;
            if ((array = this.array).length >= (fence = this.fence) && (index = this.index) >= 0 && index < (this.index = fence)) {
                do {
                    intConsumer.accept(array[index]);
                } while (++index < fence);
            }
        }
        
        @Override
        public boolean tryAdvance(final IntConsumer intConsumer) {
            if (intConsumer == null) {
                throw new NullPointerException();
            }
            if (this.index >= 0 && this.index < this.fence) {
                intConsumer.accept(this.array[this.index++]);
                return true;
            }
            return false;
        }
        
        @Override
        public long estimateSize() {
            return this.fence - this.index;
        }
        
        @Override
        public int characteristics() {
            return this.characteristics;
        }
        
        @Override
        public Comparator<? super Integer> getComparator() {
            if (this.hasCharacteristics(4)) {
                return null;
            }
            throw new IllegalStateException();
        }
    }
    
    public abstract static class AbstractLongSpliterator implements Spliterator.OfLong
    {
        static final int MAX_BATCH = 33554432;
        static final int BATCH_UNIT = 1024;
        private final int characteristics;
        private long est;
        private int batch;
        
        protected AbstractLongSpliterator(final long est, final int n) {
            this.est = est;
            this.characteristics = (((n & 0x40) != 0x0) ? (n | 0x4000) : n);
        }
        
        @Override
        public Spliterator.OfLong trySplit() {
            final HoldingLongConsumer holdingLongConsumer = new HoldingLongConsumer();
            final long est = this.est;
            if (est > 1L && this.tryAdvance((LongConsumer)holdingLongConsumer)) {
                int n = this.batch + 1024;
                if (n > est) {
                    n = (int)est;
                }
                if (n > 33554432) {
                    n = 33554432;
                }
                final long[] array = new long[n];
                int batch = 0;
                do {
                    array[batch] = holdingLongConsumer.value;
                } while (++batch < n && this.tryAdvance((LongConsumer)holdingLongConsumer));
                this.batch = batch;
                if (this.est != Long.MAX_VALUE) {
                    this.est -= batch;
                }
                return new LongArraySpliterator(array, 0, batch, this.characteristics());
            }
            return null;
        }
        
        @Override
        public long estimateSize() {
            return this.est;
        }
        
        @Override
        public int characteristics() {
            return this.characteristics;
        }
        
        static final class HoldingLongConsumer implements LongConsumer
        {
            long value;
            
            @Override
            public void accept(final long value) {
                this.value = value;
            }
        }
    }
    
    static final class LongArraySpliterator implements Spliterator.OfLong
    {
        private final long[] array;
        private int index;
        private final int fence;
        private final int characteristics;
        
        public LongArraySpliterator(final long[] array, final int n) {
            this(array, 0, array.length, n);
        }
        
        public LongArraySpliterator(final long[] array, final int index, final int fence, final int n) {
            this.array = array;
            this.index = index;
            this.fence = fence;
            this.characteristics = (n | 0x40 | 0x4000);
        }
        
        @Override
        public Spliterator.OfLong trySplit() {
            final int index = this.index;
            final int index2 = index + this.fence >>> 1;
            return (index >= index2) ? null : new LongArraySpliterator(this.array, index, this.index = index2, this.characteristics);
        }
        
        @Override
        public void forEachRemaining(final LongConsumer longConsumer) {
            if (longConsumer == null) {
                throw new NullPointerException();
            }
            final long[] array;
            final int fence;
            int index;
            if ((array = this.array).length >= (fence = this.fence) && (index = this.index) >= 0 && index < (this.index = fence)) {
                do {
                    longConsumer.accept(array[index]);
                } while (++index < fence);
            }
        }
        
        @Override
        public boolean tryAdvance(final LongConsumer longConsumer) {
            if (longConsumer == null) {
                throw new NullPointerException();
            }
            if (this.index >= 0 && this.index < this.fence) {
                longConsumer.accept(this.array[this.index++]);
                return true;
            }
            return false;
        }
        
        @Override
        public long estimateSize() {
            return this.fence - this.index;
        }
        
        @Override
        public int characteristics() {
            return this.characteristics;
        }
        
        @Override
        public Comparator<? super Long> getComparator() {
            if (this.hasCharacteristics(4)) {
                return null;
            }
            throw new IllegalStateException();
        }
    }
    
    static final class DoubleIteratorSpliterator implements Spliterator.OfDouble
    {
        static final int BATCH_UNIT = 1024;
        static final int MAX_BATCH = 33554432;
        private PrimitiveIterator.OfDouble it;
        private final int characteristics;
        private long est;
        private int batch;
        
        public DoubleIteratorSpliterator(final PrimitiveIterator.OfDouble it, final long est, final int n) {
            this.it = it;
            this.est = est;
            this.characteristics = (((n & 0x1000) == 0x0) ? (n | 0x40 | 0x4000) : n);
        }
        
        public DoubleIteratorSpliterator(final PrimitiveIterator.OfDouble it, final int n) {
            this.it = it;
            this.est = Long.MAX_VALUE;
            this.characteristics = (n & 0xFFFFBFBF);
        }
        
        @Override
        public Spliterator.OfDouble trySplit() {
            final PrimitiveIterator.OfDouble it = this.it;
            final long est = this.est;
            if (est > 1L && it.hasNext()) {
                int n = this.batch + 1024;
                if (n > est) {
                    n = (int)est;
                }
                if (n > 33554432) {
                    n = 33554432;
                }
                final double[] array = new double[n];
                int batch = 0;
                do {
                    array[batch] = it.nextDouble();
                } while (++batch < n && it.hasNext());
                this.batch = batch;
                if (this.est != Long.MAX_VALUE) {
                    this.est -= batch;
                }
                return new DoubleArraySpliterator(array, 0, batch, this.characteristics);
            }
            return null;
        }
        
        @Override
        public void forEachRemaining(final DoubleConsumer doubleConsumer) {
            if (doubleConsumer == null) {
                throw new NullPointerException();
            }
            this.it.forEachRemaining(doubleConsumer);
        }
        
        @Override
        public boolean tryAdvance(final DoubleConsumer doubleConsumer) {
            if (doubleConsumer == null) {
                throw new NullPointerException();
            }
            if (this.it.hasNext()) {
                doubleConsumer.accept(this.it.nextDouble());
                return true;
            }
            return false;
        }
        
        @Override
        public long estimateSize() {
            return this.est;
        }
        
        @Override
        public int characteristics() {
            return this.characteristics;
        }
        
        @Override
        public Comparator<? super Double> getComparator() {
            if (this.hasCharacteristics(4)) {
                return null;
            }
            throw new IllegalStateException();
        }
    }
    
    static class IteratorSpliterator<T> implements Spliterator<T>
    {
        static final int BATCH_UNIT = 1024;
        static final int MAX_BATCH = 33554432;
        private final Collection<? extends T> collection;
        private Iterator<? extends T> it;
        private final int characteristics;
        private long est;
        private int batch;
        
        public IteratorSpliterator(final Collection<? extends T> collection, final int n) {
            this.collection = collection;
            this.it = null;
            this.characteristics = (((n & 0x1000) == 0x0) ? (n | 0x40 | 0x4000) : n);
        }
        
        public IteratorSpliterator(final Iterator<? extends T> it, final long est, final int n) {
            this.collection = null;
            this.it = it;
            this.est = est;
            this.characteristics = (((n & 0x1000) == 0x0) ? (n | 0x40 | 0x4000) : n);
        }
        
        public IteratorSpliterator(final Iterator<? extends T> it, final int n) {
            this.collection = null;
            this.it = it;
            this.est = Long.MAX_VALUE;
            this.characteristics = (n & 0xFFFFBFBF);
        }
        
        @Override
        public Spliterator<T> trySplit() {
            Iterator<? extends T> it;
            long est2;
            if ((it = this.it) == null) {
                final Iterator<? extends T> iterator = this.collection.iterator();
                this.it = iterator;
                it = iterator;
                final long est = this.collection.size();
                this.est = est;
                est2 = est;
            }
            else {
                est2 = this.est;
            }
            if (est2 > 1L && it.hasNext()) {
                int n = this.batch + 1024;
                if (n > est2) {
                    n = (int)est2;
                }
                if (n > 33554432) {
                    n = 33554432;
                }
                final Object[] array = new Object[n];
                int batch = 0;
                do {
                    array[batch] = it.next();
                } while (++batch < n && it.hasNext());
                this.batch = batch;
                if (this.est != Long.MAX_VALUE) {
                    this.est -= batch;
                }
                return new ArraySpliterator<T>(array, 0, batch, this.characteristics);
            }
            return null;
        }
        
        @Override
        public void forEachRemaining(final Consumer<? super T> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            Iterator<? extends T> it;
            if ((it = this.it) == null) {
                final Iterator<? extends T> iterator = this.collection.iterator();
                this.it = iterator;
                it = iterator;
                this.est = this.collection.size();
            }
            it.forEachRemaining(consumer);
        }
        
        @Override
        public boolean tryAdvance(final Consumer<? super T> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            if (this.it == null) {
                this.it = this.collection.iterator();
                this.est = this.collection.size();
            }
            if (this.it.hasNext()) {
                consumer.accept((Object)this.it.next());
                return true;
            }
            return false;
        }
        
        @Override
        public long estimateSize() {
            if (this.it == null) {
                this.it = this.collection.iterator();
                return this.est = this.collection.size();
            }
            return this.est;
        }
        
        @Override
        public int characteristics() {
            return this.characteristics;
        }
        
        @Override
        public Comparator<? super T> getComparator() {
            if (this.hasCharacteristics(4)) {
                return null;
            }
            throw new IllegalStateException();
        }
    }
    
    private abstract static class EmptySpliterator<T, S extends Spliterator<T>, C>
    {
        EmptySpliterator() {
        }
        
        public S trySplit() {
            return null;
        }
        
        public boolean tryAdvance(final C c) {
            Objects.requireNonNull(c);
            return false;
        }
        
        public void forEachRemaining(final C c) {
            Objects.requireNonNull(c);
        }
        
        public long estimateSize() {
            return 0L;
        }
        
        public int characteristics() {
            return 16448;
        }
        
        private static final class OfDouble extends EmptySpliterator<Double, Spliterator.OfDouble, DoubleConsumer> implements Spliterator.OfDouble
        {
            OfDouble() {
            }
        }
        
        private static final class OfInt extends EmptySpliterator<Integer, Spliterator.OfInt, IntConsumer> implements Spliterator.OfInt
        {
            OfInt() {
            }
        }
        
        private static final class OfLong extends EmptySpliterator<Long, Spliterator.OfLong, LongConsumer> implements Spliterator.OfLong
        {
            OfLong() {
            }
        }
        
        private static final class OfRef<T> extends EmptySpliterator<T, Spliterator<T>, Consumer<? super T>> implements Spliterator<T>
        {
            OfRef() {
            }
        }
    }
    
    static final class IntIteratorSpliterator implements Spliterator.OfInt
    {
        static final int BATCH_UNIT = 1024;
        static final int MAX_BATCH = 33554432;
        private PrimitiveIterator.OfInt it;
        private final int characteristics;
        private long est;
        private int batch;
        
        public IntIteratorSpliterator(final PrimitiveIterator.OfInt it, final long est, final int n) {
            this.it = it;
            this.est = est;
            this.characteristics = (((n & 0x1000) == 0x0) ? (n | 0x40 | 0x4000) : n);
        }
        
        public IntIteratorSpliterator(final PrimitiveIterator.OfInt it, final int n) {
            this.it = it;
            this.est = Long.MAX_VALUE;
            this.characteristics = (n & 0xFFFFBFBF);
        }
        
        @Override
        public Spliterator.OfInt trySplit() {
            final PrimitiveIterator.OfInt it = this.it;
            final long est = this.est;
            if (est > 1L && it.hasNext()) {
                int n = this.batch + 1024;
                if (n > est) {
                    n = (int)est;
                }
                if (n > 33554432) {
                    n = 33554432;
                }
                final int[] array = new int[n];
                int batch = 0;
                do {
                    array[batch] = it.nextInt();
                } while (++batch < n && it.hasNext());
                this.batch = batch;
                if (this.est != Long.MAX_VALUE) {
                    this.est -= batch;
                }
                return new IntArraySpliterator(array, 0, batch, this.characteristics);
            }
            return null;
        }
        
        @Override
        public void forEachRemaining(final IntConsumer intConsumer) {
            if (intConsumer == null) {
                throw new NullPointerException();
            }
            this.it.forEachRemaining(intConsumer);
        }
        
        @Override
        public boolean tryAdvance(final IntConsumer intConsumer) {
            if (intConsumer == null) {
                throw new NullPointerException();
            }
            if (this.it.hasNext()) {
                intConsumer.accept(this.it.nextInt());
                return true;
            }
            return false;
        }
        
        @Override
        public long estimateSize() {
            return this.est;
        }
        
        @Override
        public int characteristics() {
            return this.characteristics;
        }
        
        @Override
        public Comparator<? super Integer> getComparator() {
            if (this.hasCharacteristics(4)) {
                return null;
            }
            throw new IllegalStateException();
        }
    }
    
    static final class LongIteratorSpliterator implements Spliterator.OfLong
    {
        static final int BATCH_UNIT = 1024;
        static final int MAX_BATCH = 33554432;
        private PrimitiveIterator.OfLong it;
        private final int characteristics;
        private long est;
        private int batch;
        
        public LongIteratorSpliterator(final PrimitiveIterator.OfLong it, final long est, final int n) {
            this.it = it;
            this.est = est;
            this.characteristics = (((n & 0x1000) == 0x0) ? (n | 0x40 | 0x4000) : n);
        }
        
        public LongIteratorSpliterator(final PrimitiveIterator.OfLong it, final int n) {
            this.it = it;
            this.est = Long.MAX_VALUE;
            this.characteristics = (n & 0xFFFFBFBF);
        }
        
        @Override
        public Spliterator.OfLong trySplit() {
            final PrimitiveIterator.OfLong it = this.it;
            final long est = this.est;
            if (est > 1L && it.hasNext()) {
                int n = this.batch + 1024;
                if (n > est) {
                    n = (int)est;
                }
                if (n > 33554432) {
                    n = 33554432;
                }
                final long[] array = new long[n];
                int batch = 0;
                do {
                    array[batch] = it.nextLong();
                } while (++batch < n && it.hasNext());
                this.batch = batch;
                if (this.est != Long.MAX_VALUE) {
                    this.est -= batch;
                }
                return new LongArraySpliterator(array, 0, batch, this.characteristics);
            }
            return null;
        }
        
        @Override
        public void forEachRemaining(final LongConsumer longConsumer) {
            if (longConsumer == null) {
                throw new NullPointerException();
            }
            this.it.forEachRemaining(longConsumer);
        }
        
        @Override
        public boolean tryAdvance(final LongConsumer longConsumer) {
            if (longConsumer == null) {
                throw new NullPointerException();
            }
            if (this.it.hasNext()) {
                longConsumer.accept(this.it.nextLong());
                return true;
            }
            return false;
        }
        
        @Override
        public long estimateSize() {
            return this.est;
        }
        
        @Override
        public int characteristics() {
            return this.characteristics;
        }
        
        @Override
        public Comparator<? super Long> getComparator() {
            if (this.hasCharacteristics(4)) {
                return null;
            }
            throw new IllegalStateException();
        }
    }
}
