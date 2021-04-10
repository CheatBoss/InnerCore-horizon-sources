package java.util.stream;

import java.util.function.*;
import java.util.*;

final class Streams
{
    static final Object NONE;
    
    private Streams() {
        throw new Error("no instances");
    }
    
    static Runnable composeWithExceptions(final Runnable runnable, final Runnable runnable2) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    runnable.run();
                }
                catch (Throwable t) {
                    try {
                        runnable2.run();
                    }
                    catch (Throwable t2) {
                        try {
                            t.addSuppressed(t2);
                        }
                        catch (Throwable t3) {}
                    }
                    throw t;
                }
                runnable2.run();
            }
        };
    }
    
    static Runnable composedClose(final BaseStream<?, ?> baseStream, final BaseStream<?, ?> baseStream2) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    baseStream.close();
                }
                catch (Throwable t) {
                    try {
                        baseStream2.close();
                    }
                    catch (Throwable t2) {
                        try {
                            t.addSuppressed(t2);
                        }
                        catch (Throwable t3) {}
                    }
                    throw t;
                }
                baseStream2.close();
            }
        };
    }
    
    static {
        NONE = new Object();
    }
    
    private abstract static class AbstractStreamBuilderImpl<T, S extends Spliterator<T>> implements Spliterator<T>
    {
        int count;
        
        @Override
        public S trySplit() {
            return null;
        }
        
        @Override
        public long estimateSize() {
            return -this.count - 1;
        }
        
        @Override
        public int characteristics() {
            return 17488;
        }
    }
    
    abstract static class ConcatSpliterator<T, T_SPLITR extends Spliterator<T>> implements Spliterator<T>
    {
        protected final T_SPLITR aSpliterator;
        protected final T_SPLITR bSpliterator;
        boolean beforeSplit;
        final boolean unsized;
        
        public ConcatSpliterator(final T_SPLITR aSpliterator, final T_SPLITR bSpliterator) {
            this.aSpliterator = aSpliterator;
            this.bSpliterator = bSpliterator;
            this.beforeSplit = true;
            this.unsized = (aSpliterator.estimateSize() + bSpliterator.estimateSize() < 0L);
        }
        
        @Override
        public T_SPLITR trySplit() {
            final Spliterator<T> spliterator = this.beforeSplit ? this.aSpliterator : this.bSpliterator.trySplit();
            this.beforeSplit = false;
            return (T_SPLITR)spliterator;
        }
        
        @Override
        public boolean tryAdvance(final Consumer<? super T> consumer) {
            boolean b;
            if (this.beforeSplit) {
                b = this.aSpliterator.tryAdvance(consumer);
                if (!b) {
                    this.beforeSplit = false;
                    b = this.bSpliterator.tryAdvance(consumer);
                }
            }
            else {
                b = this.bSpliterator.tryAdvance(consumer);
            }
            return b;
        }
        
        @Override
        public void forEachRemaining(final Consumer<? super T> consumer) {
            if (this.beforeSplit) {
                this.aSpliterator.forEachRemaining(consumer);
            }
            this.bSpliterator.forEachRemaining(consumer);
        }
        
        @Override
        public long estimateSize() {
            if (this.beforeSplit) {
                final long n = this.aSpliterator.estimateSize() + this.bSpliterator.estimateSize();
                return (n >= 0L) ? n : Long.MAX_VALUE;
            }
            return this.bSpliterator.estimateSize();
        }
        
        @Override
        public int characteristics() {
            if (this.beforeSplit) {
                return this.aSpliterator.characteristics() & this.bSpliterator.characteristics() & ~(0x5 | (this.unsized ? 16448 : 0));
            }
            return this.bSpliterator.characteristics();
        }
        
        @Override
        public Comparator<? super T> getComparator() {
            if (this.beforeSplit) {
                throw new IllegalStateException();
            }
            return this.bSpliterator.getComparator();
        }
        
        static class OfDouble extends ConcatSpliterator.OfPrimitive<Double, DoubleConsumer, Spliterator.OfDouble> implements Spliterator.OfDouble
        {
            OfDouble(final Spliterator.OfDouble ofDouble, final Spliterator.OfDouble ofDouble2) {
                super((Spliterator.OfPrimitive)ofDouble, (Spliterator.OfPrimitive)ofDouble2);
            }
        }
        
        private abstract static class OfPrimitive<T, T_CONS, T_SPLITR extends Spliterator.OfPrimitive<T, T_CONS, T_SPLITR>> extends ConcatSpliterator<T, T_SPLITR> implements Spliterator.OfPrimitive<T, T_CONS, T_SPLITR>
        {
            private OfPrimitive(final T_SPLITR t_SPLITR, final T_SPLITR t_SPLITR2) {
                super(t_SPLITR, t_SPLITR2);
            }
            
            @Override
            public boolean tryAdvance(final T_CONS t_CONS) {
                boolean b;
                if (this.beforeSplit) {
                    b = ((Spliterator.OfPrimitive)this.aSpliterator).tryAdvance(t_CONS);
                    if (!b) {
                        this.beforeSplit = false;
                        b = ((Spliterator.OfPrimitive)this.bSpliterator).tryAdvance(t_CONS);
                    }
                }
                else {
                    b = ((Spliterator.OfPrimitive)this.bSpliterator).tryAdvance(t_CONS);
                }
                return b;
            }
            
            @Override
            public void forEachRemaining(final T_CONS t_CONS) {
                if (this.beforeSplit) {
                    ((Spliterator.OfPrimitive)this.aSpliterator).forEachRemaining(t_CONS);
                }
                ((Spliterator.OfPrimitive)this.bSpliterator).forEachRemaining(t_CONS);
            }
        }
        
        static class OfInt extends ConcatSpliterator.OfPrimitive<Integer, IntConsumer, Spliterator.OfInt> implements Spliterator.OfInt
        {
            OfInt(final Spliterator.OfInt ofInt, final Spliterator.OfInt ofInt2) {
                super((Spliterator.OfPrimitive)ofInt, (Spliterator.OfPrimitive)ofInt2);
            }
        }
        
        static class OfLong extends ConcatSpliterator.OfPrimitive<Long, LongConsumer, Spliterator.OfLong> implements Spliterator.OfLong
        {
            OfLong(final Spliterator.OfLong ofLong, final Spliterator.OfLong ofLong2) {
                super((Spliterator.OfPrimitive)ofLong, (Spliterator.OfPrimitive)ofLong2);
            }
        }
        
        static class OfRef<T> extends ConcatSpliterator<T, Spliterator<T>>
        {
            OfRef(final Spliterator<T> spliterator, final Spliterator<T> spliterator2) {
                super(spliterator, spliterator2);
            }
        }
    }
    
    static final class DoubleStreamBuilderImpl extends AbstractStreamBuilderImpl<Double, Spliterator.OfDouble> implements DoubleStream.Builder, Spliterator.OfDouble
    {
        double first;
        SpinedBuffer.OfDouble buffer;
        
        DoubleStreamBuilderImpl() {
        }
        
        DoubleStreamBuilderImpl(final double first) {
            this.first = first;
            this.count = -2;
        }
        
        @Override
        public void accept(final double first) {
            if (this.count == 0) {
                this.first = first;
                ++this.count;
            }
            else {
                if (this.count <= 0) {
                    throw new IllegalStateException();
                }
                if (this.buffer == null) {
                    (this.buffer = new SpinedBuffer.OfDouble()).accept(this.first);
                    ++this.count;
                }
                this.buffer.accept(first);
            }
        }
        
        @Override
        public DoubleStream build() {
            final int count = this.count;
            if (count >= 0) {
                this.count = -this.count - 1;
                return (count < 2) ? StreamSupport.doubleStream(this, false) : StreamSupport.doubleStream(this.buffer.spliterator(), false);
            }
            throw new IllegalStateException();
        }
        
        @Override
        public boolean tryAdvance(final DoubleConsumer doubleConsumer) {
            Objects.requireNonNull(doubleConsumer);
            if (this.count == -2) {
                doubleConsumer.accept(this.first);
                this.count = -1;
                return true;
            }
            return false;
        }
        
        @Override
        public void forEachRemaining(final DoubleConsumer doubleConsumer) {
            Objects.requireNonNull(doubleConsumer);
            if (this.count == -2) {
                doubleConsumer.accept(this.first);
                this.count = -1;
            }
        }
    }
    
    static final class IntStreamBuilderImpl extends AbstractStreamBuilderImpl<Integer, Spliterator.OfInt> implements IntStream.Builder, Spliterator.OfInt
    {
        int first;
        SpinedBuffer.OfInt buffer;
        
        IntStreamBuilderImpl() {
        }
        
        IntStreamBuilderImpl(final int first) {
            this.first = first;
            this.count = -2;
        }
        
        @Override
        public void accept(final int first) {
            if (this.count == 0) {
                this.first = first;
                ++this.count;
            }
            else {
                if (this.count <= 0) {
                    throw new IllegalStateException();
                }
                if (this.buffer == null) {
                    (this.buffer = new SpinedBuffer.OfInt()).accept(this.first);
                    ++this.count;
                }
                this.buffer.accept(first);
            }
        }
        
        @Override
        public IntStream build() {
            final int count = this.count;
            if (count >= 0) {
                this.count = -this.count - 1;
                return (count < 2) ? StreamSupport.intStream(this, false) : StreamSupport.intStream(this.buffer.spliterator(), false);
            }
            throw new IllegalStateException();
        }
        
        @Override
        public boolean tryAdvance(final IntConsumer intConsumer) {
            Objects.requireNonNull(intConsumer);
            if (this.count == -2) {
                intConsumer.accept(this.first);
                this.count = -1;
                return true;
            }
            return false;
        }
        
        @Override
        public void forEachRemaining(final IntConsumer intConsumer) {
            Objects.requireNonNull(intConsumer);
            if (this.count == -2) {
                intConsumer.accept(this.first);
                this.count = -1;
            }
        }
    }
    
    static final class RangeIntSpliterator implements Spliterator.OfInt
    {
        private int from;
        private final int upTo;
        private int last;
        private static final int BALANCED_SPLIT_THRESHOLD = 16777216;
        private static final int RIGHT_BALANCED_SPLIT_RATIO = 8;
        
        RangeIntSpliterator(final int n, final int n2, final boolean b) {
            this(n, n2, b ? 1 : 0);
        }
        
        private RangeIntSpliterator(final int from, final int upTo, final int last) {
            this.from = from;
            this.upTo = upTo;
            this.last = last;
        }
        
        @Override
        public boolean tryAdvance(final IntConsumer intConsumer) {
            Objects.requireNonNull(intConsumer);
            final int from = this.from;
            if (from < this.upTo) {
                ++this.from;
                intConsumer.accept(from);
                return true;
            }
            if (this.last > 0) {
                this.last = 0;
                intConsumer.accept(from);
                return true;
            }
            return false;
        }
        
        @Override
        public void forEachRemaining(final IntConsumer intConsumer) {
            Objects.requireNonNull(intConsumer);
            int i = this.from;
            final int upTo = this.upTo;
            final int last = this.last;
            this.from = this.upTo;
            this.last = 0;
            while (i < upTo) {
                intConsumer.accept(i++);
            }
            if (last > 0) {
                intConsumer.accept(i);
            }
        }
        
        @Override
        public long estimateSize() {
            return this.upTo - (long)this.from + this.last;
        }
        
        @Override
        public int characteristics() {
            return 17749;
        }
        
        @Override
        public Comparator<? super Integer> getComparator() {
            return null;
        }
        
        @Override
        public Spliterator.OfInt trySplit() {
            final long estimateSize = this.estimateSize();
            return (estimateSize <= 1L) ? null : new RangeIntSpliterator(this.from, this.from += this.splitPoint(estimateSize), 0);
        }
        
        private int splitPoint(final long n) {
            return (int)(n / ((n < 16777216L) ? 2 : 8));
        }
    }
    
    static final class LongStreamBuilderImpl extends AbstractStreamBuilderImpl<Long, Spliterator.OfLong> implements LongStream.Builder, Spliterator.OfLong
    {
        long first;
        SpinedBuffer.OfLong buffer;
        
        LongStreamBuilderImpl() {
        }
        
        LongStreamBuilderImpl(final long first) {
            this.first = first;
            this.count = -2;
        }
        
        @Override
        public void accept(final long first) {
            if (this.count == 0) {
                this.first = first;
                ++this.count;
            }
            else {
                if (this.count <= 0) {
                    throw new IllegalStateException();
                }
                if (this.buffer == null) {
                    (this.buffer = new SpinedBuffer.OfLong()).accept(this.first);
                    ++this.count;
                }
                this.buffer.accept(first);
            }
        }
        
        @Override
        public LongStream build() {
            final int count = this.count;
            if (count >= 0) {
                this.count = -this.count - 1;
                return (count < 2) ? StreamSupport.longStream(this, false) : StreamSupport.longStream(this.buffer.spliterator(), false);
            }
            throw new IllegalStateException();
        }
        
        @Override
        public boolean tryAdvance(final LongConsumer longConsumer) {
            Objects.requireNonNull(longConsumer);
            if (this.count == -2) {
                longConsumer.accept(this.first);
                this.count = -1;
                return true;
            }
            return false;
        }
        
        @Override
        public void forEachRemaining(final LongConsumer longConsumer) {
            Objects.requireNonNull(longConsumer);
            if (this.count == -2) {
                longConsumer.accept(this.first);
                this.count = -1;
            }
        }
    }
    
    static final class RangeLongSpliterator implements Spliterator.OfLong
    {
        private long from;
        private final long upTo;
        private int last;
        private static final long BALANCED_SPLIT_THRESHOLD = 16777216L;
        private static final long RIGHT_BALANCED_SPLIT_RATIO = 8L;
        
        RangeLongSpliterator(final long n, final long n2, final boolean b) {
            this(n, n2, b ? 1 : 0);
        }
        
        private RangeLongSpliterator(final long from, final long upTo, final int last) {
            assert upTo - from + last > 0L;
            this.from = from;
            this.upTo = upTo;
            this.last = last;
        }
        
        @Override
        public boolean tryAdvance(final LongConsumer longConsumer) {
            Objects.requireNonNull(longConsumer);
            final long from = this.from;
            if (from < this.upTo) {
                ++this.from;
                longConsumer.accept(from);
                return true;
            }
            if (this.last > 0) {
                this.last = 0;
                longConsumer.accept(from);
                return true;
            }
            return false;
        }
        
        @Override
        public void forEachRemaining(final LongConsumer longConsumer) {
            Objects.requireNonNull(longConsumer);
            long from = this.from;
            final long upTo = this.upTo;
            final int last = this.last;
            this.from = this.upTo;
            this.last = 0;
            while (from < upTo) {
                longConsumer.accept(from++);
            }
            if (last > 0) {
                longConsumer.accept(from);
            }
        }
        
        @Override
        public long estimateSize() {
            return this.upTo - this.from + this.last;
        }
        
        @Override
        public int characteristics() {
            return 17749;
        }
        
        @Override
        public Comparator<? super Long> getComparator() {
            return null;
        }
        
        @Override
        public Spliterator.OfLong trySplit() {
            final long estimateSize = this.estimateSize();
            return (estimateSize <= 1L) ? null : new RangeLongSpliterator(this.from, this.from += this.splitPoint(estimateSize), 0);
        }
        
        private long splitPoint(final long n) {
            return n / ((n < 16777216L) ? 2L : 8L);
        }
    }
    
    static final class StreamBuilderImpl<T> extends AbstractStreamBuilderImpl<T, Spliterator<T>> implements Stream.Builder<T>
    {
        T first;
        SpinedBuffer<T> buffer;
        
        StreamBuilderImpl() {
        }
        
        StreamBuilderImpl(final T first) {
            this.first = first;
            this.count = -2;
        }
        
        @Override
        public void accept(final T first) {
            if (this.count == 0) {
                this.first = first;
                ++this.count;
            }
            else {
                if (this.count <= 0) {
                    throw new IllegalStateException();
                }
                if (this.buffer == null) {
                    (this.buffer = new SpinedBuffer<T>()).accept(this.first);
                    ++this.count;
                }
                this.buffer.accept(first);
            }
        }
        
        @Override
        public Stream.Builder<T> add(final T t) {
            this.accept(t);
            return this;
        }
        
        @Override
        public Stream<T> build() {
            final int count = this.count;
            if (count >= 0) {
                this.count = -this.count - 1;
                return (count < 2) ? StreamSupport.stream((Spliterator<T>)this, false) : StreamSupport.stream(this.buffer.spliterator(), false);
            }
            throw new IllegalStateException();
        }
        
        @Override
        public boolean tryAdvance(final Consumer<? super T> consumer) {
            Objects.requireNonNull(consumer);
            if (this.count == -2) {
                consumer.accept(this.first);
                this.count = -1;
                return true;
            }
            return false;
        }
        
        @Override
        public void forEachRemaining(final Consumer<? super T> consumer) {
            Objects.requireNonNull(consumer);
            if (this.count == -2) {
                consumer.accept(this.first);
                this.count = -1;
            }
        }
    }
}
