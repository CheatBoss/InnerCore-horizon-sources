package java.util.stream;

import java.util.concurrent.*;
import java.util.*;
import java.util.function.*;
import java.util.concurrent.atomic.*;

class StreamSpliterators
{
    private abstract static class AbstractWrappingSpliterator<P_IN, P_OUT, T_BUFFER extends AbstractSpinedBuffer> implements Spliterator<P_OUT>
    {
        final boolean isParallel;
        final PipelineHelper<P_OUT> ph;
        private Supplier<Spliterator<P_IN>> spliteratorSupplier;
        Spliterator<P_IN> spliterator;
        Sink<P_IN> bufferSink;
        BooleanSupplier pusher;
        long nextToConsume;
        T_BUFFER buffer;
        boolean finished;
        
        AbstractWrappingSpliterator(final PipelineHelper<P_OUT> ph, final Supplier<Spliterator<P_IN>> spliteratorSupplier, final boolean isParallel) {
            this.ph = ph;
            this.spliteratorSupplier = spliteratorSupplier;
            this.spliterator = null;
            this.isParallel = isParallel;
        }
        
        AbstractWrappingSpliterator(final PipelineHelper<P_OUT> ph, final Spliterator<P_IN> spliterator, final boolean isParallel) {
            this.ph = ph;
            this.spliteratorSupplier = null;
            this.spliterator = spliterator;
            this.isParallel = isParallel;
        }
        
        final void init() {
            if (this.spliterator == null) {
                this.spliterator = this.spliteratorSupplier.get();
                this.spliteratorSupplier = null;
            }
        }
        
        final boolean doAdvance() {
            if (this.buffer != null) {
                ++this.nextToConsume;
                boolean fillBuffer = this.nextToConsume < this.buffer.count();
                if (!fillBuffer) {
                    this.nextToConsume = 0L;
                    this.buffer.clear();
                    fillBuffer = this.fillBuffer();
                }
                return fillBuffer;
            }
            if (this.finished) {
                return false;
            }
            this.init();
            this.initPartialTraversalState();
            this.nextToConsume = 0L;
            this.bufferSink.begin(this.spliterator.getExactSizeIfKnown());
            return this.fillBuffer();
        }
        
        abstract AbstractWrappingSpliterator<P_IN, P_OUT, ?> wrap(final Spliterator<P_IN> p0);
        
        abstract void initPartialTraversalState();
        
        @Override
        public Spliterator<P_OUT> trySplit() {
            if (this.isParallel && !this.finished) {
                this.init();
                final Spliterator<P_IN> trySplit = this.spliterator.trySplit();
                return (trySplit == null) ? null : this.wrap(trySplit);
            }
            return null;
        }
        
        private boolean fillBuffer() {
            while (this.buffer.count() == 0L) {
                if (this.bufferSink.cancellationRequested() || !this.pusher.getAsBoolean()) {
                    if (this.finished) {
                        return false;
                    }
                    this.bufferSink.end();
                    this.finished = true;
                }
            }
            return true;
        }
        
        @Override
        public final long estimateSize() {
            this.init();
            return this.spliterator.estimateSize();
        }
        
        @Override
        public final long getExactSizeIfKnown() {
            this.init();
            return StreamOpFlag.SIZED.isKnown(this.ph.getStreamAndOpFlags()) ? this.spliterator.getExactSizeIfKnown() : -1L;
        }
        
        @Override
        public final int characteristics() {
            this.init();
            int characteristics = StreamOpFlag.toCharacteristics(StreamOpFlag.toStreamFlags(this.ph.getStreamAndOpFlags()));
            if ((characteristics & 0x40) != 0x0) {
                characteristics = ((characteristics & 0xFFFFBFBF) | (this.spliterator.characteristics() & 0x4040));
            }
            return characteristics;
        }
        
        @Override
        public Comparator<? super P_OUT> getComparator() {
            if (!this.hasCharacteristics(4)) {
                throw new IllegalStateException();
            }
            return null;
        }
        
        @Override
        public final String toString() {
            return String.format("%s[%s]", this.getClass().getName(), this.spliterator);
        }
    }
    
    abstract static class ArrayBuffer
    {
        int index;
        
        void reset() {
            this.index = 0;
        }
        
        static final class OfDouble extends OfPrimitive<DoubleConsumer> implements DoubleConsumer
        {
            final double[] array;
            
            OfDouble(final int n) {
                this.array = new double[n];
            }
            
            @Override
            public void accept(final double n) {
                this.array[this.index++] = n;
            }
            
            @Override
            void forEach(final DoubleConsumer doubleConsumer, final long n) {
                for (int n2 = 0; n2 < n; ++n2) {
                    doubleConsumer.accept(this.array[n2]);
                }
            }
        }
        
        abstract static class OfPrimitive<T_CONS> extends ArrayBuffer
        {
            int index;
            
            @Override
            void reset() {
                this.index = 0;
            }
            
            abstract void forEach(final T_CONS p0, final long p1);
        }
        
        static final class OfInt extends OfPrimitive<IntConsumer> implements IntConsumer
        {
            final int[] array;
            
            OfInt(final int n) {
                this.array = new int[n];
            }
            
            @Override
            public void accept(final int n) {
                this.array[this.index++] = n;
            }
            
            public void forEach(final IntConsumer intConsumer, final long n) {
                for (int n2 = 0; n2 < n; ++n2) {
                    intConsumer.accept(this.array[n2]);
                }
            }
        }
        
        static final class OfLong extends OfPrimitive<LongConsumer> implements LongConsumer
        {
            final long[] array;
            
            OfLong(final int n) {
                this.array = new long[n];
            }
            
            @Override
            public void accept(final long n) {
                this.array[this.index++] = n;
            }
            
            public void forEach(final LongConsumer longConsumer, final long n) {
                for (int n2 = 0; n2 < n; ++n2) {
                    longConsumer.accept(this.array[n2]);
                }
            }
        }
        
        static final class OfRef<T> extends ArrayBuffer implements Consumer<T>
        {
            final Object[] array;
            
            OfRef(final int n) {
                this.array = new Object[n];
            }
            
            @Override
            public void accept(final T t) {
                this.array[this.index++] = t;
            }
            
            public void forEach(final Consumer<? super T> consumer, final long n) {
                for (int n2 = 0; n2 < n; ++n2) {
                    consumer.accept((Object)this.array[n2]);
                }
            }
        }
    }
    
    static class DelegatingSpliterator<T, T_SPLITR extends Spliterator<T>> implements Spliterator<T>
    {
        private final Supplier<? extends T_SPLITR> supplier;
        private T_SPLITR s;
        
        DelegatingSpliterator(final Supplier<? extends T_SPLITR> supplier) {
            this.supplier = supplier;
        }
        
        T_SPLITR get() {
            if (this.s == null) {
                this.s = (T_SPLITR)this.supplier.get();
            }
            return this.s;
        }
        
        @Override
        public T_SPLITR trySplit() {
            return (T_SPLITR)this.get().trySplit();
        }
        
        @Override
        public boolean tryAdvance(final Consumer<? super T> consumer) {
            return this.get().tryAdvance(consumer);
        }
        
        @Override
        public void forEachRemaining(final Consumer<? super T> consumer) {
            this.get().forEachRemaining(consumer);
        }
        
        @Override
        public long estimateSize() {
            return this.get().estimateSize();
        }
        
        @Override
        public int characteristics() {
            return this.get().characteristics();
        }
        
        @Override
        public Comparator<? super T> getComparator() {
            return this.get().getComparator();
        }
        
        @Override
        public long getExactSizeIfKnown() {
            return this.get().getExactSizeIfKnown();
        }
        
        @Override
        public String toString() {
            return this.getClass().getName() + "[" + this.get() + "]";
        }
        
        static final class OfDouble extends DelegatingSpliterator.OfPrimitive<Double, DoubleConsumer, Spliterator.OfDouble> implements Spliterator.OfDouble
        {
            OfDouble(final Supplier<Spliterator.OfDouble> supplier) {
                super(supplier);
            }
        }
        
        static class OfPrimitive<T, T_CONS, T_SPLITR extends Spliterator.OfPrimitive<T, T_CONS, T_SPLITR>> extends DelegatingSpliterator<T, T_SPLITR> implements Spliterator.OfPrimitive<T, T_CONS, T_SPLITR>
        {
            OfPrimitive(final Supplier<? extends T_SPLITR> supplier) {
                super(supplier);
            }
            
            @Override
            public boolean tryAdvance(final T_CONS t_CONS) {
                return this.get().tryAdvance(t_CONS);
            }
            
            @Override
            public void forEachRemaining(final T_CONS t_CONS) {
                this.get().forEachRemaining(t_CONS);
            }
        }
        
        static final class OfInt extends DelegatingSpliterator.OfPrimitive<Integer, IntConsumer, Spliterator.OfInt> implements Spliterator.OfInt
        {
            OfInt(final Supplier<Spliterator.OfInt> supplier) {
                super(supplier);
            }
        }
        
        static final class OfLong extends DelegatingSpliterator.OfPrimitive<Long, LongConsumer, Spliterator.OfLong> implements Spliterator.OfLong
        {
            OfLong(final Supplier<Spliterator.OfLong> supplier) {
                super(supplier);
            }
        }
    }
    
    static final class DistinctSpliterator<T> implements Spliterator<T>, Consumer<T>
    {
        private static final Object NULL_VALUE;
        private final Spliterator<T> s;
        private final ConcurrentHashMap<T, Boolean> seen;
        private T tmpSlot;
        
        DistinctSpliterator(final Spliterator<T> spliterator) {
            this(spliterator, (ConcurrentHashMap)new ConcurrentHashMap());
        }
        
        private DistinctSpliterator(final Spliterator<T> s, final ConcurrentHashMap<T, Boolean> seen) {
            this.s = s;
            this.seen = seen;
        }
        
        @Override
        public void accept(final T tmpSlot) {
            this.tmpSlot = tmpSlot;
        }
        
        private T mapNull(final T t) {
            return (T)((t != null) ? t : DistinctSpliterator.NULL_VALUE);
        }
        
        @Override
        public boolean tryAdvance(final Consumer<? super T> consumer) {
            while (this.s.tryAdvance(this)) {
                if (this.seen.putIfAbsent(this.mapNull(this.tmpSlot), Boolean.TRUE) == null) {
                    consumer.accept(this.tmpSlot);
                    this.tmpSlot = null;
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public void forEachRemaining(final Consumer<? super T> consumer) {
            this.s.forEachRemaining(t -> {
                if (this.seen.putIfAbsent(this.mapNull(t), Boolean.TRUE) == null) {
                    consumer.accept(t);
                }
            });
        }
        
        @Override
        public Spliterator<T> trySplit() {
            final Spliterator<T> trySplit = this.s.trySplit();
            return (trySplit != null) ? new DistinctSpliterator((Spliterator<Object>)trySplit, (ConcurrentHashMap<Object, Boolean>)this.seen) : null;
        }
        
        @Override
        public long estimateSize() {
            return this.s.estimateSize();
        }
        
        @Override
        public int characteristics() {
            return (this.s.characteristics() & 0xFFFFBFAB) | 0x1;
        }
        
        @Override
        public Comparator<? super T> getComparator() {
            return this.s.getComparator();
        }
        
        static {
            NULL_VALUE = new Object();
        }
    }
    
    static final class DoubleWrappingSpliterator<P_IN> extends AbstractWrappingSpliterator<P_IN, Double, SpinedBuffer.OfDouble> implements Spliterator.OfDouble
    {
        DoubleWrappingSpliterator(final PipelineHelper<Double> pipelineHelper, final Supplier<Spliterator<P_IN>> supplier, final boolean b) {
            super(pipelineHelper, supplier, b);
        }
        
        DoubleWrappingSpliterator(final PipelineHelper<Double> pipelineHelper, final Spliterator<P_IN> spliterator, final boolean b) {
            super(pipelineHelper, spliterator, b);
        }
        
        @Override
        AbstractWrappingSpliterator<P_IN, Double, ?> wrap(final Spliterator<P_IN> spliterator) {
            return new DoubleWrappingSpliterator((PipelineHelper<Double>)this.ph, (Spliterator<Object>)spliterator, this.isParallel);
        }
        
        @Override
        void initPartialTraversalState() {
            final SpinedBuffer.OfDouble buffer = new SpinedBuffer.OfDouble();
            this.buffer = (T_BUFFER)buffer;
            this.bufferSink = (Sink<P_IN>)this.ph.wrapSink((Sink<P_OUT>)buffer::accept);
            this.pusher = (() -> this.spliterator.tryAdvance(this.bufferSink));
        }
        
        @Override
        public Spliterator.OfDouble trySplit() {
            return (Spliterator.OfDouble)super.trySplit();
        }
        
        @Override
        public boolean tryAdvance(final DoubleConsumer doubleConsumer) {
            Objects.requireNonNull(doubleConsumer);
            final boolean doAdvance = this.doAdvance();
            if (doAdvance) {
                doubleConsumer.accept(((SpinedBuffer.OfDouble)this.buffer).get(this.nextToConsume));
            }
            return doAdvance;
        }
        
        @Override
        public void forEachRemaining(final DoubleConsumer doubleConsumer) {
            if (this.buffer == null && !this.finished) {
                Objects.requireNonNull(doubleConsumer);
                this.init();
                this.ph.wrapAndCopyInto((Sink)doubleConsumer::accept, this.spliterator);
                this.finished = true;
            }
            else {
                while (this.tryAdvance(doubleConsumer)) {}
            }
        }
    }
    
    abstract static class InfiniteSupplyingSpliterator<T> implements Spliterator<T>
    {
        long estimate;
        
        protected InfiniteSupplyingSpliterator(final long estimate) {
            this.estimate = estimate;
        }
        
        @Override
        public long estimateSize() {
            return this.estimate;
        }
        
        @Override
        public int characteristics() {
            return 1024;
        }
        
        static final class OfDouble extends InfiniteSupplyingSpliterator<Double> implements Spliterator.OfDouble
        {
            final DoubleSupplier s;
            
            OfDouble(final long n, final DoubleSupplier s) {
                super(n);
                this.s = s;
            }
            
            @Override
            public boolean tryAdvance(final DoubleConsumer doubleConsumer) {
                Objects.requireNonNull(doubleConsumer);
                doubleConsumer.accept(this.s.getAsDouble());
                return true;
            }
            
            @Override
            public Spliterator.OfDouble trySplit() {
                if (this.estimate == 0L) {
                    return null;
                }
                return new OfDouble(this.estimate >>>= 1, this.s);
            }
        }
        
        static final class OfInt extends InfiniteSupplyingSpliterator<Integer> implements Spliterator.OfInt
        {
            final IntSupplier s;
            
            OfInt(final long n, final IntSupplier s) {
                super(n);
                this.s = s;
            }
            
            @Override
            public boolean tryAdvance(final IntConsumer intConsumer) {
                Objects.requireNonNull(intConsumer);
                intConsumer.accept(this.s.getAsInt());
                return true;
            }
            
            @Override
            public Spliterator.OfInt trySplit() {
                if (this.estimate == 0L) {
                    return null;
                }
                return new OfInt(this.estimate >>>= 1, this.s);
            }
        }
        
        static final class OfLong extends InfiniteSupplyingSpliterator<Long> implements Spliterator.OfLong
        {
            final LongSupplier s;
            
            OfLong(final long n, final LongSupplier s) {
                super(n);
                this.s = s;
            }
            
            @Override
            public boolean tryAdvance(final LongConsumer longConsumer) {
                Objects.requireNonNull(longConsumer);
                longConsumer.accept(this.s.getAsLong());
                return true;
            }
            
            @Override
            public Spliterator.OfLong trySplit() {
                if (this.estimate == 0L) {
                    return null;
                }
                return new OfLong(this.estimate >>>= 1, this.s);
            }
        }
        
        static final class OfRef<T> extends InfiniteSupplyingSpliterator<T>
        {
            final Supplier<T> s;
            
            OfRef(final long n, final Supplier<T> s) {
                super(n);
                this.s = s;
            }
            
            @Override
            public boolean tryAdvance(final Consumer<? super T> consumer) {
                Objects.requireNonNull(consumer);
                consumer.accept(this.s.get());
                return true;
            }
            
            @Override
            public Spliterator<T> trySplit() {
                if (this.estimate == 0L) {
                    return null;
                }
                return new OfRef(this.estimate >>>= 1, (Supplier<Object>)this.s);
            }
        }
    }
    
    static final class IntWrappingSpliterator<P_IN> extends AbstractWrappingSpliterator<P_IN, Integer, SpinedBuffer.OfInt> implements Spliterator.OfInt
    {
        IntWrappingSpliterator(final PipelineHelper<Integer> pipelineHelper, final Supplier<Spliterator<P_IN>> supplier, final boolean b) {
            super(pipelineHelper, supplier, b);
        }
        
        IntWrappingSpliterator(final PipelineHelper<Integer> pipelineHelper, final Spliterator<P_IN> spliterator, final boolean b) {
            super(pipelineHelper, spliterator, b);
        }
        
        @Override
        AbstractWrappingSpliterator<P_IN, Integer, ?> wrap(final Spliterator<P_IN> spliterator) {
            return new IntWrappingSpliterator((PipelineHelper<Integer>)this.ph, (Spliterator<Object>)spliterator, this.isParallel);
        }
        
        @Override
        void initPartialTraversalState() {
            final SpinedBuffer.OfInt buffer = new SpinedBuffer.OfInt();
            this.buffer = (T_BUFFER)buffer;
            this.bufferSink = (Sink<P_IN>)this.ph.wrapSink((Sink<P_OUT>)buffer::accept);
            this.pusher = (() -> this.spliterator.tryAdvance(this.bufferSink));
        }
        
        @Override
        public Spliterator.OfInt trySplit() {
            return (Spliterator.OfInt)super.trySplit();
        }
        
        @Override
        public boolean tryAdvance(final IntConsumer intConsumer) {
            Objects.requireNonNull(intConsumer);
            final boolean doAdvance = this.doAdvance();
            if (doAdvance) {
                intConsumer.accept(((SpinedBuffer.OfInt)this.buffer).get(this.nextToConsume));
            }
            return doAdvance;
        }
        
        @Override
        public void forEachRemaining(final IntConsumer intConsumer) {
            if (this.buffer == null && !this.finished) {
                Objects.requireNonNull(intConsumer);
                this.init();
                this.ph.wrapAndCopyInto((Sink)intConsumer::accept, this.spliterator);
                this.finished = true;
            }
            else {
                while (this.tryAdvance(intConsumer)) {}
            }
        }
    }
    
    static final class LongWrappingSpliterator<P_IN> extends AbstractWrappingSpliterator<P_IN, Long, SpinedBuffer.OfLong> implements Spliterator.OfLong
    {
        LongWrappingSpliterator(final PipelineHelper<Long> pipelineHelper, final Supplier<Spliterator<P_IN>> supplier, final boolean b) {
            super(pipelineHelper, supplier, b);
        }
        
        LongWrappingSpliterator(final PipelineHelper<Long> pipelineHelper, final Spliterator<P_IN> spliterator, final boolean b) {
            super(pipelineHelper, spliterator, b);
        }
        
        @Override
        AbstractWrappingSpliterator<P_IN, Long, ?> wrap(final Spliterator<P_IN> spliterator) {
            return new LongWrappingSpliterator((PipelineHelper<Long>)this.ph, (Spliterator<Object>)spliterator, this.isParallel);
        }
        
        @Override
        void initPartialTraversalState() {
            final SpinedBuffer.OfLong buffer = new SpinedBuffer.OfLong();
            this.buffer = (T_BUFFER)buffer;
            this.bufferSink = (Sink<P_IN>)this.ph.wrapSink((Sink<P_OUT>)buffer::accept);
            this.pusher = (() -> this.spliterator.tryAdvance(this.bufferSink));
        }
        
        @Override
        public Spliterator.OfLong trySplit() {
            return (Spliterator.OfLong)super.trySplit();
        }
        
        @Override
        public boolean tryAdvance(final LongConsumer longConsumer) {
            Objects.requireNonNull(longConsumer);
            final boolean doAdvance = this.doAdvance();
            if (doAdvance) {
                longConsumer.accept(((SpinedBuffer.OfLong)this.buffer).get(this.nextToConsume));
            }
            return doAdvance;
        }
        
        @Override
        public void forEachRemaining(final LongConsumer longConsumer) {
            if (this.buffer == null && !this.finished) {
                Objects.requireNonNull(longConsumer);
                this.init();
                this.ph.wrapAndCopyInto((Sink)longConsumer::accept, this.spliterator);
                this.finished = true;
            }
            else {
                while (this.tryAdvance(longConsumer)) {}
            }
        }
    }
    
    abstract static class SliceSpliterator<T, T_SPLITR extends Spliterator<T>>
    {
        final long sliceOrigin;
        final long sliceFence;
        T_SPLITR s;
        long index;
        long fence;
        
        SliceSpliterator(final T_SPLITR s, final long sliceOrigin, final long sliceFence, final long index, final long fence) {
            assert s.hasCharacteristics(16384);
            this.s = s;
            this.sliceOrigin = sliceOrigin;
            this.sliceFence = sliceFence;
            this.index = index;
            this.fence = fence;
        }
        
        protected abstract T_SPLITR makeSpliterator(final T_SPLITR p0, final long p1, final long p2, final long p3, final long p4);
        
        public T_SPLITR trySplit() {
            if (this.sliceOrigin >= this.fence) {
                return null;
            }
            if (this.index >= this.fence) {
                return null;
            }
            while (true) {
                final Spliterator<T> trySplit = this.s.trySplit();
                if (trySplit == null) {
                    return null;
                }
                final long n = this.index + trySplit.estimateSize();
                final long min = Math.min(n, this.sliceFence);
                if (this.sliceOrigin >= min) {
                    this.index = min;
                }
                else if (min >= this.sliceFence) {
                    this.s = (T_SPLITR)trySplit;
                    this.fence = min;
                }
                else {
                    if (this.index >= this.sliceOrigin && n <= this.sliceFence) {
                        this.index = min;
                        return (T_SPLITR)trySplit;
                    }
                    final Spliterator<T> spliterator = trySplit;
                    final long sliceOrigin = this.sliceOrigin;
                    final long sliceFence = this.sliceFence;
                    final long index = this.index;
                    final long index2 = min;
                    this.index = index2;
                    return this.makeSpliterator((T_SPLITR)spliterator, sliceOrigin, sliceFence, index, index2);
                }
            }
        }
        
        public long estimateSize() {
            return (this.sliceOrigin < this.fence) ? (this.fence - Math.max(this.sliceOrigin, this.index)) : 0L;
        }
        
        public int characteristics() {
            return this.s.characteristics();
        }
        
        static final class OfDouble extends SliceSpliterator.OfPrimitive<Double, Spliterator.OfDouble, DoubleConsumer> implements Spliterator.OfDouble
        {
            OfDouble(final Spliterator.OfDouble ofDouble, final long n, final long n2) {
                super(ofDouble, n, n2);
            }
            
            OfDouble(final Spliterator.OfDouble ofDouble, final long n, final long n2, final long n3, final long n4) {
                super((Spliterator.OfPrimitive)ofDouble, n, n2, n3, n4);
            }
            
            protected Spliterator.OfDouble makeSpliterator(final Spliterator.OfDouble ofDouble, final long n, final long n2, final long n3, final long n4) {
                return new OfDouble(ofDouble, n, n2, n3, n4);
            }
            
            @Override
            protected DoubleConsumer emptyConsumer() {
                return p0 -> {};
            }
        }
        
        abstract static class OfPrimitive<T, T_SPLITR extends Spliterator.OfPrimitive<T, T_CONS, T_SPLITR>, T_CONS> extends SliceSpliterator<T, T_SPLITR> implements Spliterator.OfPrimitive<T, T_CONS, T_SPLITR>
        {
            OfPrimitive(final T_SPLITR t_SPLITR, final long n, final long n2) {
                this((Spliterator.OfPrimitive)t_SPLITR, n, n2, 0L, Math.min(t_SPLITR.estimateSize(), n2));
            }
            
            private OfPrimitive(final T_SPLITR t_SPLITR, final long n, final long n2, final long n3, final long n4) {
                super(t_SPLITR, n, n2, n3, n4);
            }
            
            @Override
            public boolean tryAdvance(final T_CONS t_CONS) {
                Objects.requireNonNull(t_CONS);
                if (this.sliceOrigin >= this.fence) {
                    return false;
                }
                while (this.sliceOrigin > this.index) {
                    ((Spliterator.OfPrimitive)this.s).tryAdvance(this.emptyConsumer());
                    ++this.index;
                }
                if (this.index >= this.fence) {
                    return false;
                }
                ++this.index;
                return ((Spliterator.OfPrimitive)this.s).tryAdvance(t_CONS);
            }
            
            @Override
            public void forEachRemaining(final T_CONS t_CONS) {
                Objects.requireNonNull(t_CONS);
                if (this.sliceOrigin >= this.fence) {
                    return;
                }
                if (this.index >= this.fence) {
                    return;
                }
                if (this.index >= this.sliceOrigin && this.index + ((Spliterator.OfPrimitive)this.s).estimateSize() <= this.sliceFence) {
                    ((Spliterator.OfPrimitive)this.s).forEachRemaining(t_CONS);
                    this.index = this.fence;
                }
                else {
                    while (this.sliceOrigin > this.index) {
                        ((Spliterator.OfPrimitive)this.s).tryAdvance(this.emptyConsumer());
                        ++this.index;
                    }
                    while (this.index < this.fence) {
                        ((Spliterator.OfPrimitive)this.s).tryAdvance(t_CONS);
                        ++this.index;
                    }
                }
            }
            
            protected abstract T_CONS emptyConsumer();
        }
        
        static final class OfInt extends SliceSpliterator.OfPrimitive<Integer, Spliterator.OfInt, IntConsumer> implements Spliterator.OfInt
        {
            OfInt(final Spliterator.OfInt ofInt, final long n, final long n2) {
                super(ofInt, n, n2);
            }
            
            OfInt(final Spliterator.OfInt ofInt, final long n, final long n2, final long n3, final long n4) {
                super((Spliterator.OfPrimitive)ofInt, n, n2, n3, n4);
            }
            
            protected Spliterator.OfInt makeSpliterator(final Spliterator.OfInt ofInt, final long n, final long n2, final long n3, final long n4) {
                return new OfInt(ofInt, n, n2, n3, n4);
            }
            
            @Override
            protected IntConsumer emptyConsumer() {
                return p0 -> {};
            }
        }
        
        static final class OfLong extends SliceSpliterator.OfPrimitive<Long, Spliterator.OfLong, LongConsumer> implements Spliterator.OfLong
        {
            OfLong(final Spliterator.OfLong ofLong, final long n, final long n2) {
                super(ofLong, n, n2);
            }
            
            OfLong(final Spliterator.OfLong ofLong, final long n, final long n2, final long n3, final long n4) {
                super((Spliterator.OfPrimitive)ofLong, n, n2, n3, n4);
            }
            
            protected Spliterator.OfLong makeSpliterator(final Spliterator.OfLong ofLong, final long n, final long n2, final long n3, final long n4) {
                return new OfLong(ofLong, n, n2, n3, n4);
            }
            
            @Override
            protected LongConsumer emptyConsumer() {
                return p0 -> {};
            }
        }
        
        static final class OfRef<T> extends SliceSpliterator<T, Spliterator<T>> implements Spliterator<T>
        {
            OfRef(final Spliterator<T> spliterator, final long n, final long n2) {
                this(spliterator, n, n2, 0L, Math.min(spliterator.estimateSize(), n2));
            }
            
            private OfRef(final Spliterator<T> spliterator, final long n, final long n2, final long n3, final long n4) {
                super(spliterator, n, n2, n3, n4);
            }
            
            @Override
            protected Spliterator<T> makeSpliterator(final Spliterator<T> spliterator, final long n, final long n2, final long n3, final long n4) {
                return new OfRef((Spliterator<Object>)spliterator, n, n2, n3, n4);
            }
            
            @Override
            public boolean tryAdvance(final Consumer<? super T> consumer) {
                Objects.requireNonNull(consumer);
                if (this.sliceOrigin >= this.fence) {
                    return false;
                }
                while (this.sliceOrigin > this.index) {
                    this.s.tryAdvance(p0 -> {});
                    ++this.index;
                }
                if (this.index >= this.fence) {
                    return false;
                }
                ++this.index;
                return this.s.tryAdvance((Consumer<? super T>)consumer);
            }
            
            @Override
            public void forEachRemaining(final Consumer<? super T> consumer) {
                Objects.requireNonNull(consumer);
                if (this.sliceOrigin >= this.fence) {
                    return;
                }
                if (this.index >= this.fence) {
                    return;
                }
                if (this.index >= this.sliceOrigin && this.index + this.s.estimateSize() <= this.sliceFence) {
                    this.s.forEachRemaining((Consumer<? super T>)consumer);
                    this.index = this.fence;
                }
                else {
                    while (this.sliceOrigin > this.index) {
                        this.s.tryAdvance(p0 -> {});
                        ++this.index;
                    }
                    while (this.index < this.fence) {
                        this.s.tryAdvance((Consumer<? super T>)consumer);
                        ++this.index;
                    }
                }
            }
        }
    }
    
    abstract static class UnorderedSliceSpliterator<T, T_SPLITR extends Spliterator<T>>
    {
        static final int CHUNK_SIZE = 128;
        protected final T_SPLITR s;
        protected final boolean unlimited;
        private final long skipThreshold;
        private final AtomicLong permits;
        
        UnorderedSliceSpliterator(final T_SPLITR s, final long n, final long n2) {
            this.s = s;
            this.unlimited = (n2 < 0L);
            this.skipThreshold = ((n2 >= 0L) ? n2 : 0L);
            this.permits = new AtomicLong((n2 >= 0L) ? (n + n2) : n);
        }
        
        UnorderedSliceSpliterator(final T_SPLITR s, final UnorderedSliceSpliterator<T, T_SPLITR> unorderedSliceSpliterator) {
            this.s = s;
            this.unlimited = unorderedSliceSpliterator.unlimited;
            this.permits = unorderedSliceSpliterator.permits;
            this.skipThreshold = unorderedSliceSpliterator.skipThreshold;
        }
        
        protected final long acquirePermits(final long n) {
            assert n > 0L;
            long min;
            long value;
            do {
                value = this.permits.get();
                if (value == 0L) {
                    return this.unlimited ? n : 0L;
                }
                min = Math.min(value, n);
            } while (min > 0L && !this.permits.compareAndSet(value, value - min));
            if (this.unlimited) {
                return Math.max(n - min, 0L);
            }
            if (value > this.skipThreshold) {
                return Math.max(min - (value - this.skipThreshold), 0L);
            }
            return min;
        }
        
        protected final PermitStatus permitStatus() {
            if (this.permits.get() > 0L) {
                return PermitStatus.MAYBE_MORE;
            }
            return this.unlimited ? PermitStatus.UNLIMITED : PermitStatus.NO_MORE;
        }
        
        public final T_SPLITR trySplit() {
            if (this.permits.get() == 0L) {
                return null;
            }
            final Spliterator<T> trySplit = this.s.trySplit();
            return (T_SPLITR)((trySplit == null) ? null : this.makeSpliterator(trySplit));
        }
        
        protected abstract T_SPLITR makeSpliterator(final T_SPLITR p0);
        
        public final long estimateSize() {
            return this.s.estimateSize();
        }
        
        public final int characteristics() {
            return this.s.characteristics() & 0xFFFFBFAF;
        }
        
        static final class OfDouble extends UnorderedSliceSpliterator.OfPrimitive<Double, DoubleConsumer, ArrayBuffer.OfDouble, Spliterator.OfDouble> implements Spliterator.OfDouble, DoubleConsumer
        {
            double tmpValue;
            
            OfDouble(final Spliterator.OfDouble ofDouble, final long n, final long n2) {
                super(ofDouble, n, n2);
            }
            
            OfDouble(final Spliterator.OfDouble ofDouble, final OfDouble ofDouble2) {
                super(ofDouble, (UnorderedSliceSpliterator.OfPrimitive<Object, Object, ArrayBuffer.OfPrimitive, Spliterator.OfDouble>)ofDouble2);
            }
            
            @Override
            public void accept(final double tmpValue) {
                this.tmpValue = tmpValue;
            }
            
            @Override
            protected void acceptConsumed(final DoubleConsumer doubleConsumer) {
                doubleConsumer.accept(this.tmpValue);
            }
            
            @Override
            protected ArrayBuffer.OfDouble bufferCreate(final int n) {
                return new ArrayBuffer.OfDouble(n);
            }
            
            protected Spliterator.OfDouble makeSpliterator(final Spliterator.OfDouble ofDouble) {
                return new OfDouble(ofDouble, this);
            }
        }
        
        abstract static class OfPrimitive<T, T_CONS, T_BUFF extends ArrayBuffer.OfPrimitive<T_CONS>, T_SPLITR extends Spliterator.OfPrimitive<T, T_CONS, T_SPLITR>> extends UnorderedSliceSpliterator<T, T_SPLITR> implements Spliterator.OfPrimitive<T, T_CONS, T_SPLITR>
        {
            OfPrimitive(final T_SPLITR t_SPLITR, final long n, final long n2) {
                super(t_SPLITR, n, n2);
            }
            
            OfPrimitive(final T_SPLITR t_SPLITR, final OfPrimitive<T, T_CONS, T_BUFF, T_SPLITR> ofPrimitive) {
                super(t_SPLITR, (UnorderedSliceSpliterator<Object, T_SPLITR>)ofPrimitive);
            }
            
            @Override
            public boolean tryAdvance(final T_CONS t_CONS) {
                Objects.requireNonNull(t_CONS);
                while (this.permitStatus() != PermitStatus.NO_MORE) {
                    if (!((Spliterator.OfPrimitive)this.s).tryAdvance(this)) {
                        return false;
                    }
                    if (this.acquirePermits(1L) == 1L) {
                        this.acceptConsumed(t_CONS);
                        return true;
                    }
                }
                return false;
            }
            
            protected abstract void acceptConsumed(final T_CONS p0);
            
            @Override
            public void forEachRemaining(final T_CONS t_CONS) {
                Objects.requireNonNull(t_CONS);
                ArrayBuffer.OfPrimitive<T_CONS> bufferCreate = null;
                PermitStatus permitStatus;
                while ((permitStatus = this.permitStatus()) != PermitStatus.NO_MORE) {
                    if (permitStatus != PermitStatus.MAYBE_MORE) {
                        ((Spliterator.OfPrimitive)this.s).forEachRemaining(t_CONS);
                        return;
                    }
                    if (bufferCreate == null) {
                        bufferCreate = this.bufferCreate(128);
                    }
                    else {
                        bufferCreate.reset();
                    }
                    final ArrayBuffer.OfPrimitive<T_CONS> ofPrimitive = bufferCreate;
                    long n = 0L;
                    while (((Spliterator.OfPrimitive)this.s).tryAdvance(ofPrimitive) && ++n < 128L) {}
                    if (n == 0L) {
                        return;
                    }
                    bufferCreate.forEach(t_CONS, this.acquirePermits(n));
                }
            }
            
            protected abstract T_BUFF bufferCreate(final int p0);
        }
        
        enum PermitStatus
        {
            NO_MORE, 
            MAYBE_MORE, 
            UNLIMITED;
        }
        
        static final class OfInt extends UnorderedSliceSpliterator.OfPrimitive<Integer, IntConsumer, ArrayBuffer.OfInt, Spliterator.OfInt> implements Spliterator.OfInt, IntConsumer
        {
            int tmpValue;
            
            OfInt(final Spliterator.OfInt ofInt, final long n, final long n2) {
                super(ofInt, n, n2);
            }
            
            OfInt(final Spliterator.OfInt ofInt, final OfInt ofInt2) {
                super(ofInt, (UnorderedSliceSpliterator.OfPrimitive<Object, Object, ArrayBuffer.OfPrimitive, Spliterator.OfInt>)ofInt2);
            }
            
            @Override
            public void accept(final int tmpValue) {
                this.tmpValue = tmpValue;
            }
            
            @Override
            protected void acceptConsumed(final IntConsumer intConsumer) {
                intConsumer.accept(this.tmpValue);
            }
            
            @Override
            protected ArrayBuffer.OfInt bufferCreate(final int n) {
                return new ArrayBuffer.OfInt(n);
            }
            
            protected Spliterator.OfInt makeSpliterator(final Spliterator.OfInt ofInt) {
                return new OfInt(ofInt, this);
            }
        }
        
        static final class OfLong extends UnorderedSliceSpliterator.OfPrimitive<Long, LongConsumer, ArrayBuffer.OfLong, Spliterator.OfLong> implements Spliterator.OfLong, LongConsumer
        {
            long tmpValue;
            
            OfLong(final Spliterator.OfLong ofLong, final long n, final long n2) {
                super(ofLong, n, n2);
            }
            
            OfLong(final Spliterator.OfLong ofLong, final OfLong ofLong2) {
                super(ofLong, (UnorderedSliceSpliterator.OfPrimitive<Object, Object, ArrayBuffer.OfPrimitive, Spliterator.OfLong>)ofLong2);
            }
            
            @Override
            public void accept(final long tmpValue) {
                this.tmpValue = tmpValue;
            }
            
            @Override
            protected void acceptConsumed(final LongConsumer longConsumer) {
                longConsumer.accept(this.tmpValue);
            }
            
            @Override
            protected ArrayBuffer.OfLong bufferCreate(final int n) {
                return new ArrayBuffer.OfLong(n);
            }
            
            protected Spliterator.OfLong makeSpliterator(final Spliterator.OfLong ofLong) {
                return new OfLong(ofLong, this);
            }
        }
        
        static final class OfRef<T> extends UnorderedSliceSpliterator<T, Spliterator<T>> implements Spliterator<T>, Consumer<T>
        {
            T tmpSlot;
            
            OfRef(final Spliterator<T> spliterator, final long n, final long n2) {
                super(spliterator, n, n2);
            }
            
            OfRef(final Spliterator<T> spliterator, final OfRef<T> ofRef) {
                super(spliterator, (UnorderedSliceSpliterator<Object, Spliterator<T>>)ofRef);
            }
            
            @Override
            public final void accept(final T tmpSlot) {
                this.tmpSlot = tmpSlot;
            }
            
            @Override
            public boolean tryAdvance(final Consumer<? super T> consumer) {
                Objects.requireNonNull(consumer);
                while (this.permitStatus() != PermitStatus.NO_MORE) {
                    if (!this.s.tryAdvance((Consumer<? super T>)this)) {
                        return false;
                    }
                    if (this.acquirePermits(1L) == 1L) {
                        consumer.accept(this.tmpSlot);
                        this.tmpSlot = null;
                        return true;
                    }
                }
                return false;
            }
            
            @Override
            public void forEachRemaining(final Consumer<? super T> consumer) {
                Objects.requireNonNull(consumer);
                ArrayBuffer.OfRef<Object> ofRef = null;
                PermitStatus permitStatus;
                while ((permitStatus = this.permitStatus()) != PermitStatus.NO_MORE) {
                    if (permitStatus != PermitStatus.MAYBE_MORE) {
                        this.s.forEachRemaining((Consumer<? super T>)consumer);
                        return;
                    }
                    if (ofRef == null) {
                        ofRef = new ArrayBuffer.OfRef<Object>(128);
                    }
                    else {
                        ofRef.reset();
                    }
                    long n = 0L;
                    while (this.s.tryAdvance(ofRef) && ++n < 128L) {}
                    if (n == 0L) {
                        return;
                    }
                    ofRef.forEach((Consumer<? super Object>)consumer, this.acquirePermits(n));
                }
            }
            
            @Override
            protected Spliterator<T> makeSpliterator(final Spliterator<T> spliterator) {
                return new OfRef((Spliterator<Object>)spliterator, (OfRef<Object>)this);
            }
        }
    }
    
    static final class WrappingSpliterator<P_IN, P_OUT> extends AbstractWrappingSpliterator<P_IN, P_OUT, SpinedBuffer<P_OUT>>
    {
        WrappingSpliterator(final PipelineHelper<P_OUT> pipelineHelper, final Supplier<Spliterator<P_IN>> supplier, final boolean b) {
            super(pipelineHelper, supplier, b);
        }
        
        WrappingSpliterator(final PipelineHelper<P_OUT> pipelineHelper, final Spliterator<P_IN> spliterator, final boolean b) {
            super(pipelineHelper, spliterator, b);
        }
        
        @Override
        WrappingSpliterator<P_IN, P_OUT> wrap(final Spliterator<P_IN> spliterator) {
            return new WrappingSpliterator<P_IN, P_OUT>((PipelineHelper<P_OUT>)this.ph, spliterator, this.isParallel);
        }
        
        @Override
        void initPartialTraversalState() {
            final SpinedBuffer<Object> buffer = new SpinedBuffer<Object>();
            this.buffer = (T_BUFFER)buffer;
            this.bufferSink = (Sink<P_IN>)this.ph.wrapSink((Sink<P_OUT>)buffer::accept);
            this.pusher = (() -> this.spliterator.tryAdvance(this.bufferSink));
        }
        
        @Override
        public boolean tryAdvance(final Consumer<? super P_OUT> consumer) {
            Objects.requireNonNull(consumer);
            final boolean doAdvance = this.doAdvance();
            if (doAdvance) {
                consumer.accept(((SpinedBuffer)this.buffer).get(this.nextToConsume));
            }
            return doAdvance;
        }
        
        @Override
        public void forEachRemaining(final Consumer<? super P_OUT> consumer) {
            if (this.buffer == null && !this.finished) {
                Objects.requireNonNull(consumer);
                this.init();
                this.ph.wrapAndCopyInto((Sink)consumer::accept, this.spliterator);
                this.finished = true;
            }
            else {
                while (this.tryAdvance(consumer)) {}
            }
        }
    }
}
