package java.util.stream;

import java.util.*;
import java.util.function.*;

class SpinedBuffer<E> extends AbstractSpinedBuffer implements Consumer<E>, Iterable<E>
{
    protected E[] curChunk;
    protected E[][] spine;
    private static final int SPLITERATOR_CHARACTERISTICS = 16464;
    
    SpinedBuffer(final int n) {
        super(n);
        this.curChunk = (E[])new Object[1 << this.initialChunkPower];
    }
    
    SpinedBuffer() {
        this.curChunk = (E[])new Object[1 << this.initialChunkPower];
    }
    
    protected long capacity() {
        return (this.spineIndex == 0) ? this.curChunk.length : (this.priorElementCount[this.spineIndex] + this.spine[this.spineIndex].length);
    }
    
    private void inflateSpine() {
        if (this.spine == null) {
            this.spine = (E[][])new Object[8][];
            this.priorElementCount = new long[8];
            this.spine[0] = this.curChunk;
        }
    }
    
    protected final void ensureCapacity(final long n) {
        long capacity = this.capacity();
        if (n > capacity) {
            this.inflateSpine();
            int chunkSize;
            for (int n2 = this.spineIndex + 1; n > capacity; capacity += chunkSize, ++n2) {
                if (n2 >= this.spine.length) {
                    final int n3 = this.spine.length * 2;
                    this.spine = Arrays.copyOf(this.spine, n3);
                    this.priorElementCount = Arrays.copyOf(this.priorElementCount, n3);
                }
                chunkSize = this.chunkSize(n2);
                this.spine[n2] = (E[])new Object[chunkSize];
                this.priorElementCount[n2] = this.priorElementCount[n2 - 1] + this.spine[n2 - 1].length;
            }
        }
    }
    
    protected void increaseCapacity() {
        this.ensureCapacity(this.capacity() + 1L);
    }
    
    public E get(final long n) {
        if (this.spineIndex == 0) {
            if (n < this.elementIndex) {
                return this.curChunk[(int)n];
            }
            throw new IndexOutOfBoundsException(Long.toString(n));
        }
        else {
            if (n >= this.count()) {
                throw new IndexOutOfBoundsException(Long.toString(n));
            }
            for (int i = 0; i <= this.spineIndex; ++i) {
                if (n < this.priorElementCount[i] + this.spine[i].length) {
                    return this.spine[i][(int)(n - this.priorElementCount[i])];
                }
            }
            throw new IndexOutOfBoundsException(Long.toString(n));
        }
    }
    
    public void copyInto(final E[] array, int n) {
        final long n2 = n + this.count();
        if (n2 > array.length || n2 < n) {
            throw new IndexOutOfBoundsException("does not fit");
        }
        if (this.spineIndex == 0) {
            System.arraycopy(this.curChunk, 0, array, n, this.elementIndex);
        }
        else {
            for (int i = 0; i < this.spineIndex; ++i) {
                System.arraycopy(this.spine[i], 0, array, n, this.spine[i].length);
                n += this.spine[i].length;
            }
            if (this.elementIndex > 0) {
                System.arraycopy(this.curChunk, 0, array, n, this.elementIndex);
            }
        }
    }
    
    public E[] asArray(final IntFunction<E[]> intFunction) {
        final long count = this.count();
        if (count >= 2147483639L) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        final E[] array = intFunction.apply((int)count);
        this.copyInto(array, 0);
        return array;
    }
    
    @Override
    public void clear() {
        if (this.spine != null) {
            this.curChunk = this.spine[0];
            for (int i = 0; i < this.curChunk.length; ++i) {
                this.curChunk[i] = null;
            }
            this.spine = null;
            this.priorElementCount = null;
        }
        else {
            for (int j = 0; j < this.elementIndex; ++j) {
                this.curChunk[j] = null;
            }
        }
        this.elementIndex = 0;
        this.spineIndex = 0;
    }
    
    @Override
    public Iterator<E> iterator() {
        return Spliterators.iterator(this.spliterator());
    }
    
    @Override
    public void forEach(final Consumer<? super E> consumer) {
        for (int i = 0; i < this.spineIndex; ++i) {
            final E[] array = this.spine[i];
            for (int length = array.length, j = 0; j < length; ++j) {
                consumer.accept(array[j]);
            }
        }
        for (int k = 0; k < this.elementIndex; ++k) {
            consumer.accept(this.curChunk[k]);
        }
    }
    
    @Override
    public void accept(final E e) {
        if (this.elementIndex == this.curChunk.length) {
            this.inflateSpine();
            if (this.spineIndex + 1 >= this.spine.length || this.spine[this.spineIndex + 1] == null) {
                this.increaseCapacity();
            }
            this.elementIndex = 0;
            ++this.spineIndex;
            this.curChunk = this.spine[this.spineIndex];
        }
        this.curChunk[this.elementIndex++] = e;
    }
    
    @Override
    public String toString() {
        final ArrayList<Object> list = new ArrayList<Object>();
        this.forEach(list::add);
        return "SpinedBuffer:" + list.toString();
    }
    
    @Override
    public Spliterator<E> spliterator() {
        class Splitr implements Spliterator<E>
        {
            int splSpineIndex = 0;
            final int lastSpineIndex = SpinedBuffer.this.spineIndex;
            int splElementIndex = 0;
            final int lastSpineElementFence = SpinedBuffer.this.elementIndex;
            E[] splChunk;
            
            Splitr(final int lastSpineIndex, final int splElementIndex, final int lastSpineElementFence) {
                assert splSpineIndex == 0 && lastSpineIndex == 0;
                this.splChunk = ((SpinedBuffer.this.spine == null) ? SpinedBuffer.this.curChunk : SpinedBuffer.this.spine[splSpineIndex]);
            }
            
            @Override
            public long estimateSize() {
                return (this.splSpineIndex == this.lastSpineIndex) ? (this.lastSpineElementFence - (long)this.splElementIndex) : (SpinedBuffer.this.priorElementCount[this.lastSpineIndex] + this.lastSpineElementFence - SpinedBuffer.this.priorElementCount[this.splSpineIndex] - this.splElementIndex);
            }
            
            @Override
            public int characteristics() {
                return 16464;
            }
            
            @Override
            public boolean tryAdvance(final Consumer<? super E> consumer) {
                Objects.requireNonNull(consumer);
                if (this.splSpineIndex < this.lastSpineIndex || (this.splSpineIndex == this.lastSpineIndex && this.splElementIndex < this.lastSpineElementFence)) {
                    consumer.accept(this.splChunk[this.splElementIndex++]);
                    if (this.splElementIndex == this.splChunk.length) {
                        this.splElementIndex = 0;
                        ++this.splSpineIndex;
                        if (SpinedBuffer.this.spine != null && this.splSpineIndex <= this.lastSpineIndex) {
                            this.splChunk = SpinedBuffer.this.spine[this.splSpineIndex];
                        }
                    }
                    return true;
                }
                return false;
            }
            
            @Override
            public void forEachRemaining(final Consumer<? super E> consumer) {
                Objects.requireNonNull(consumer);
                if (this.splSpineIndex < this.lastSpineIndex || (this.splSpineIndex == this.lastSpineIndex && this.splElementIndex < this.lastSpineElementFence)) {
                    int i = this.splElementIndex;
                    for (int j = this.splSpineIndex; j < this.lastSpineIndex; ++j) {
                        for (E[] array = SpinedBuffer.this.spine[j]; i < array.length; ++i) {
                            consumer.accept(array[i]);
                        }
                        i = 0;
                    }
                    final E[] array2 = (this.splSpineIndex == this.lastSpineIndex) ? this.splChunk : SpinedBuffer.this.spine[this.lastSpineIndex];
                    while (i < this.lastSpineElementFence) {
                        consumer.accept(array2[i]);
                        ++i;
                    }
                    this.splSpineIndex = this.lastSpineIndex;
                    this.splElementIndex = this.lastSpineElementFence;
                }
            }
            
            @Override
            public Spliterator<E> trySplit() {
                if (this.splSpineIndex < this.lastSpineIndex) {
                    final Splitr splitr = new Splitr(this.splSpineIndex, this.lastSpineIndex - 1, this.splElementIndex, SpinedBuffer.this.spine[this.lastSpineIndex - 1].length);
                    this.splSpineIndex = this.lastSpineIndex;
                    this.splElementIndex = 0;
                    this.splChunk = SpinedBuffer.this.spine[this.splSpineIndex];
                    return splitr;
                }
                if (this.splSpineIndex != this.lastSpineIndex) {
                    return null;
                }
                final int n = (this.lastSpineElementFence - this.splElementIndex) / 2;
                if (n == 0) {
                    return null;
                }
                final Spliterator<E> spliterator = Arrays.spliterator(this.splChunk, this.splElementIndex, this.splElementIndex + n);
                this.splElementIndex += n;
                return spliterator;
            }
        }
        return new Splitr(SpinedBuffer.this.spineIndex, SpinedBuffer.this.elementIndex);
    }
    
    static class OfDouble extends OfPrimitive<Double, double[], DoubleConsumer> implements DoubleConsumer
    {
        OfDouble() {
        }
        
        OfDouble(final int n) {
            super(n);
        }
        
        @Override
        public void forEach(final Consumer<? super Double> consumer) {
            if (consumer instanceof DoubleConsumer) {
                ((OfPrimitive<E, T_ARR, DoubleConsumer>)this).forEach((DoubleConsumer)consumer);
            }
            else {
                if (Tripwire.ENABLED) {
                    Tripwire.trip(this.getClass(), "{0} calling SpinedBuffer.OfDouble.forEach(Consumer)");
                }
                this.spliterator().forEachRemaining(consumer);
            }
        }
        
        @Override
        protected double[][] newArrayArray(final int n) {
            return new double[n][];
        }
        
        @Override
        public double[] newArray(final int n) {
            return new double[n];
        }
        
        @Override
        protected int arrayLength(final double[] array) {
            return array.length;
        }
        
        @Override
        protected void arrayForEach(final double[] array, final int n, final int n2, final DoubleConsumer doubleConsumer) {
            for (int i = n; i < n2; ++i) {
                doubleConsumer.accept(array[i]);
            }
        }
        
        @Override
        public void accept(final double n) {
            this.preAccept();
            ((double[])(Object)this.curChunk)[this.elementIndex++] = n;
        }
        
        public double get(final long n) {
            final int chunk = this.chunkFor(n);
            if (this.spineIndex == 0 && chunk == 0) {
                return ((double[])(Object)this.curChunk)[(int)n];
            }
            return ((double[][])(Object)this.spine)[chunk][(int)(n - this.priorElementCount[chunk])];
        }
        
        @Override
        public PrimitiveIterator.OfDouble iterator() {
            return Spliterators.iterator(this.spliterator());
        }
        
        @Override
        public Spliterator.OfDouble spliterator() {
            class Splitr extends BaseSpliterator<Spliterator.OfDouble> implements Spliterator.OfDouble
            {
                Splitr(final int n2, final int n3, final int n4) {
                    super(n, n2, n3, n4);
                }
                
                @Override
                Splitr newSpliterator(final int n, final int n2, final int n3, final int n4) {
                    return new Splitr(n, n2, n3, n4);
                }
                
                void arrayForOne(final double[] array, final int n, final DoubleConsumer doubleConsumer) {
                    doubleConsumer.accept(array[n]);
                }
                
                Spliterator.OfDouble arraySpliterator(final double[] array, final int n, final int n2) {
                    return Arrays.spliterator(array, n, n + n2);
                }
            }
            return new Splitr(0, this.spineIndex, 0, this.elementIndex);
        }
        
        @Override
        public String toString() {
            final double[] array = ((OfPrimitive<E, double[], T_CONS>)this).asPrimitiveArray();
            if (array.length < 200) {
                return String.format("%s[length=%d, chunks=%d]%s", this.getClass().getSimpleName(), array.length, this.spineIndex, Arrays.toString(array));
            }
            return String.format("%s[length=%d, chunks=%d]%s...", this.getClass().getSimpleName(), array.length, this.spineIndex, Arrays.toString(Arrays.copyOf(array, 200)));
        }
    }
    
    abstract static class OfPrimitive<E, T_ARR, T_CONS> extends AbstractSpinedBuffer implements Iterable<E>
    {
        T_ARR curChunk;
        T_ARR[] spine;
        
        OfPrimitive(final int n) {
            super(n);
            this.curChunk = this.newArray(1 << this.initialChunkPower);
        }
        
        OfPrimitive() {
            this.curChunk = this.newArray(1 << this.initialChunkPower);
        }
        
        @Override
        public abstract Iterator<E> iterator();
        
        @Override
        public abstract void forEach(final Consumer<? super E> p0);
        
        protected abstract T_ARR[] newArrayArray(final int p0);
        
        public abstract T_ARR newArray(final int p0);
        
        protected abstract int arrayLength(final T_ARR p0);
        
        protected abstract void arrayForEach(final T_ARR p0, final int p1, final int p2, final T_CONS p3);
        
        protected long capacity() {
            return (this.spineIndex == 0) ? this.arrayLength(this.curChunk) : (this.priorElementCount[this.spineIndex] + this.arrayLength(this.spine[this.spineIndex]));
        }
        
        private void inflateSpine() {
            if (this.spine == null) {
                this.spine = this.newArrayArray(8);
                this.priorElementCount = new long[8];
                this.spine[0] = this.curChunk;
            }
        }
        
        protected final void ensureCapacity(final long n) {
            long capacity = this.capacity();
            if (n > capacity) {
                this.inflateSpine();
                int chunkSize;
                for (int n2 = this.spineIndex + 1; n > capacity; capacity += chunkSize, ++n2) {
                    if (n2 >= this.spine.length) {
                        final int n3 = this.spine.length * 2;
                        this.spine = Arrays.copyOf(this.spine, n3);
                        this.priorElementCount = Arrays.copyOf(this.priorElementCount, n3);
                    }
                    chunkSize = this.chunkSize(n2);
                    this.spine[n2] = this.newArray(chunkSize);
                    this.priorElementCount[n2] = this.priorElementCount[n2 - 1] + this.arrayLength(this.spine[n2 - 1]);
                }
            }
        }
        
        protected void increaseCapacity() {
            this.ensureCapacity(this.capacity() + 1L);
        }
        
        protected int chunkFor(final long n) {
            if (this.spineIndex == 0) {
                if (n < this.elementIndex) {
                    return 0;
                }
                throw new IndexOutOfBoundsException(Long.toString(n));
            }
            else {
                if (n >= this.count()) {
                    throw new IndexOutOfBoundsException(Long.toString(n));
                }
                for (int i = 0; i <= this.spineIndex; ++i) {
                    if (n < this.priorElementCount[i] + this.arrayLength(this.spine[i])) {
                        return i;
                    }
                }
                throw new IndexOutOfBoundsException(Long.toString(n));
            }
        }
        
        public void copyInto(final T_ARR t_ARR, int n) {
            final long n2 = n + this.count();
            if (n2 > this.arrayLength(t_ARR) || n2 < n) {
                throw new IndexOutOfBoundsException("does not fit");
            }
            if (this.spineIndex == 0) {
                System.arraycopy(this.curChunk, 0, t_ARR, n, this.elementIndex);
            }
            else {
                for (int i = 0; i < this.spineIndex; ++i) {
                    System.arraycopy(this.spine[i], 0, t_ARR, n, this.arrayLength(this.spine[i]));
                    n += this.arrayLength(this.spine[i]);
                }
                if (this.elementIndex > 0) {
                    System.arraycopy(this.curChunk, 0, t_ARR, n, this.elementIndex);
                }
            }
        }
        
        public T_ARR asPrimitiveArray() {
            final long count = this.count();
            if (count >= 2147483639L) {
                throw new IllegalArgumentException("Stream size exceeds max array size");
            }
            final T_ARR array = this.newArray((int)count);
            this.copyInto(array, 0);
            return array;
        }
        
        protected void preAccept() {
            if (this.elementIndex == this.arrayLength(this.curChunk)) {
                this.inflateSpine();
                if (this.spineIndex + 1 >= this.spine.length || this.spine[this.spineIndex + 1] == null) {
                    this.increaseCapacity();
                }
                this.elementIndex = 0;
                ++this.spineIndex;
                this.curChunk = this.spine[this.spineIndex];
            }
        }
        
        @Override
        public void clear() {
            if (this.spine != null) {
                this.curChunk = this.spine[0];
                this.spine = null;
                this.priorElementCount = null;
            }
            this.elementIndex = 0;
            this.spineIndex = 0;
        }
        
        public void forEach(final T_CONS t_CONS) {
            for (int i = 0; i < this.spineIndex; ++i) {
                this.arrayForEach(this.spine[i], 0, this.arrayLength(this.spine[i]), t_CONS);
            }
            this.arrayForEach(this.curChunk, 0, this.elementIndex, t_CONS);
        }
        
        abstract class BaseSpliterator<T_SPLITR extends Spliterator.OfPrimitive<E, T_CONS, T_SPLITR>> implements Spliterator.OfPrimitive<E, T_CONS, T_SPLITR>
        {
            int splSpineIndex;
            final int lastSpineIndex;
            int splElementIndex;
            final int lastSpineElementFence;
            T_ARR splChunk;
            
            BaseSpliterator(final int splSpineIndex, final int lastSpineIndex, final int splElementIndex, final int lastSpineElementFence) {
                this.splSpineIndex = splSpineIndex;
                this.lastSpineIndex = lastSpineIndex;
                this.splElementIndex = splElementIndex;
                this.lastSpineElementFence = lastSpineElementFence;
                assert splSpineIndex == 0 && lastSpineIndex == 0;
                this.splChunk = ((SpinedBuffer.OfPrimitive.this.spine == null) ? SpinedBuffer.OfPrimitive.this.curChunk : SpinedBuffer.OfPrimitive.this.spine[splSpineIndex]);
            }
            
            abstract T_SPLITR newSpliterator(final int p0, final int p1, final int p2, final int p3);
            
            abstract void arrayForOne(final T_ARR p0, final int p1, final T_CONS p2);
            
            abstract T_SPLITR arraySpliterator(final T_ARR p0, final int p1, final int p2);
            
            @Override
            public long estimateSize() {
                return (this.splSpineIndex == this.lastSpineIndex) ? (this.lastSpineElementFence - (long)this.splElementIndex) : (SpinedBuffer.OfPrimitive.this.priorElementCount[this.lastSpineIndex] + this.lastSpineElementFence - SpinedBuffer.OfPrimitive.this.priorElementCount[this.splSpineIndex] - this.splElementIndex);
            }
            
            @Override
            public int characteristics() {
                return 16464;
            }
            
            @Override
            public boolean tryAdvance(final T_CONS t_CONS) {
                Objects.requireNonNull(t_CONS);
                if (this.splSpineIndex < this.lastSpineIndex || (this.splSpineIndex == this.lastSpineIndex && this.splElementIndex < this.lastSpineElementFence)) {
                    this.arrayForOne(this.splChunk, this.splElementIndex++, t_CONS);
                    if (this.splElementIndex == SpinedBuffer.OfPrimitive.this.arrayLength(this.splChunk)) {
                        this.splElementIndex = 0;
                        ++this.splSpineIndex;
                        if (SpinedBuffer.OfPrimitive.this.spine != null && this.splSpineIndex <= this.lastSpineIndex) {
                            this.splChunk = SpinedBuffer.OfPrimitive.this.spine[this.splSpineIndex];
                        }
                    }
                    return true;
                }
                return false;
            }
            
            @Override
            public void forEachRemaining(final T_CONS t_CONS) {
                Objects.requireNonNull(t_CONS);
                if (this.splSpineIndex < this.lastSpineIndex || (this.splSpineIndex == this.lastSpineIndex && this.splElementIndex < this.lastSpineElementFence)) {
                    int splElementIndex = this.splElementIndex;
                    for (int i = this.splSpineIndex; i < this.lastSpineIndex; ++i) {
                        final T_ARR t_ARR = SpinedBuffer.OfPrimitive.this.spine[i];
                        SpinedBuffer.OfPrimitive.this.arrayForEach(t_ARR, splElementIndex, SpinedBuffer.OfPrimitive.this.arrayLength(t_ARR), t_CONS);
                        splElementIndex = 0;
                    }
                    SpinedBuffer.OfPrimitive.this.arrayForEach((this.splSpineIndex == this.lastSpineIndex) ? this.splChunk : SpinedBuffer.OfPrimitive.this.spine[this.lastSpineIndex], splElementIndex, this.lastSpineElementFence, t_CONS);
                    this.splSpineIndex = this.lastSpineIndex;
                    this.splElementIndex = this.lastSpineElementFence;
                }
            }
            
            @Override
            public T_SPLITR trySplit() {
                if (this.splSpineIndex < this.lastSpineIndex) {
                    final Spliterator.OfPrimitive<E, T_CONS, T_SPLITR> spliterator = (Spliterator.OfPrimitive<E, T_CONS, T_SPLITR>)this.newSpliterator(this.splSpineIndex, this.lastSpineIndex - 1, this.splElementIndex, SpinedBuffer.OfPrimitive.this.arrayLength(SpinedBuffer.OfPrimitive.this.spine[this.lastSpineIndex - 1]));
                    this.splSpineIndex = this.lastSpineIndex;
                    this.splElementIndex = 0;
                    this.splChunk = SpinedBuffer.OfPrimitive.this.spine[this.splSpineIndex];
                    return (T_SPLITR)spliterator;
                }
                if (this.splSpineIndex != this.lastSpineIndex) {
                    return null;
                }
                final int n = (this.lastSpineElementFence - this.splElementIndex) / 2;
                if (n == 0) {
                    return null;
                }
                final Spliterator.OfPrimitive<E, T_CONS, T_SPLITR> arraySpliterator = (Spliterator.OfPrimitive<E, T_CONS, T_SPLITR>)this.arraySpliterator(this.splChunk, this.splElementIndex, n);
                this.splElementIndex += n;
                return (T_SPLITR)arraySpliterator;
            }
        }
    }
    
    static class OfInt extends OfPrimitive<Integer, int[], IntConsumer> implements IntConsumer
    {
        OfInt() {
        }
        
        OfInt(final int n) {
            super(n);
        }
        
        @Override
        public void forEach(final Consumer<? super Integer> consumer) {
            if (consumer instanceof IntConsumer) {
                ((OfPrimitive<E, T_ARR, IntConsumer>)this).forEach((IntConsumer)consumer);
            }
            else {
                if (Tripwire.ENABLED) {
                    Tripwire.trip(this.getClass(), "{0} calling SpinedBuffer.OfInt.forEach(Consumer)");
                }
                this.spliterator().forEachRemaining(consumer);
            }
        }
        
        @Override
        protected int[][] newArrayArray(final int n) {
            return new int[n][];
        }
        
        @Override
        public int[] newArray(final int n) {
            return new int[n];
        }
        
        @Override
        protected int arrayLength(final int[] array) {
            return array.length;
        }
        
        @Override
        protected void arrayForEach(final int[] array, final int n, final int n2, final IntConsumer intConsumer) {
            for (int i = n; i < n2; ++i) {
                intConsumer.accept(array[i]);
            }
        }
        
        @Override
        public void accept(final int n) {
            this.preAccept();
            ((int[])(Object)this.curChunk)[this.elementIndex++] = n;
        }
        
        public int get(final long n) {
            final int chunk = this.chunkFor(n);
            if (this.spineIndex == 0 && chunk == 0) {
                return ((int[])(Object)this.curChunk)[(int)n];
            }
            return ((int[][])(Object)this.spine)[chunk][(int)(n - this.priorElementCount[chunk])];
        }
        
        @Override
        public PrimitiveIterator.OfInt iterator() {
            return Spliterators.iterator(this.spliterator());
        }
        
        @Override
        public Spliterator.OfInt spliterator() {
            class Splitr extends BaseSpliterator<Spliterator.OfInt> implements Spliterator.OfInt
            {
                Splitr(final int n2, final int n3, final int n4) {
                    super(n, n2, n3, n4);
                }
                
                @Override
                Splitr newSpliterator(final int n, final int n2, final int n3, final int n4) {
                    return new Splitr(n, n2, n3, n4);
                }
                
                void arrayForOne(final int[] array, final int n, final IntConsumer intConsumer) {
                    intConsumer.accept(array[n]);
                }
                
                Spliterator.OfInt arraySpliterator(final int[] array, final int n, final int n2) {
                    return Arrays.spliterator(array, n, n + n2);
                }
            }
            return new Splitr(0, this.spineIndex, 0, this.elementIndex);
        }
        
        @Override
        public String toString() {
            final int[] array = ((OfPrimitive<E, int[], T_CONS>)this).asPrimitiveArray();
            if (array.length < 200) {
                return String.format("%s[length=%d, chunks=%d]%s", this.getClass().getSimpleName(), array.length, this.spineIndex, Arrays.toString(array));
            }
            return String.format("%s[length=%d, chunks=%d]%s...", this.getClass().getSimpleName(), array.length, this.spineIndex, Arrays.toString(Arrays.copyOf(array, 200)));
        }
    }
    
    static class OfLong extends OfPrimitive<Long, long[], LongConsumer> implements LongConsumer
    {
        OfLong() {
        }
        
        OfLong(final int n) {
            super(n);
        }
        
        @Override
        public void forEach(final Consumer<? super Long> consumer) {
            if (consumer instanceof LongConsumer) {
                ((OfPrimitive<E, T_ARR, LongConsumer>)this).forEach((LongConsumer)consumer);
            }
            else {
                if (Tripwire.ENABLED) {
                    Tripwire.trip(this.getClass(), "{0} calling SpinedBuffer.OfLong.forEach(Consumer)");
                }
                this.spliterator().forEachRemaining(consumer);
            }
        }
        
        @Override
        protected long[][] newArrayArray(final int n) {
            return new long[n][];
        }
        
        @Override
        public long[] newArray(final int n) {
            return new long[n];
        }
        
        @Override
        protected int arrayLength(final long[] array) {
            return array.length;
        }
        
        @Override
        protected void arrayForEach(final long[] array, final int n, final int n2, final LongConsumer longConsumer) {
            for (int i = n; i < n2; ++i) {
                longConsumer.accept(array[i]);
            }
        }
        
        @Override
        public void accept(final long n) {
            this.preAccept();
            ((long[])(Object)this.curChunk)[this.elementIndex++] = n;
        }
        
        public long get(final long n) {
            final int chunk = this.chunkFor(n);
            if (this.spineIndex == 0 && chunk == 0) {
                return ((long[])(Object)this.curChunk)[(int)n];
            }
            return ((long[][])(Object)this.spine)[chunk][(int)(n - this.priorElementCount[chunk])];
        }
        
        @Override
        public PrimitiveIterator.OfLong iterator() {
            return Spliterators.iterator(this.spliterator());
        }
        
        @Override
        public Spliterator.OfLong spliterator() {
            class Splitr extends BaseSpliterator<Spliterator.OfLong> implements Spliterator.OfLong
            {
                Splitr(final int n2, final int n3, final int n4) {
                    super(n, n2, n3, n4);
                }
                
                @Override
                Splitr newSpliterator(final int n, final int n2, final int n3, final int n4) {
                    return new Splitr(n, n2, n3, n4);
                }
                
                void arrayForOne(final long[] array, final int n, final LongConsumer longConsumer) {
                    longConsumer.accept(array[n]);
                }
                
                Spliterator.OfLong arraySpliterator(final long[] array, final int n, final int n2) {
                    return Arrays.spliterator(array, n, n + n2);
                }
            }
            return new Splitr(0, this.spineIndex, 0, this.elementIndex);
        }
        
        @Override
        public String toString() {
            final long[] array = ((OfPrimitive<E, long[], T_CONS>)this).asPrimitiveArray();
            if (array.length < 200) {
                return String.format("%s[length=%d, chunks=%d]%s", this.getClass().getSimpleName(), array.length, this.spineIndex, Arrays.toString(array));
            }
            return String.format("%s[length=%d, chunks=%d]%s...", this.getClass().getSimpleName(), array.length, this.spineIndex, Arrays.toString(Arrays.copyOf(array, 200)));
        }
    }
}
