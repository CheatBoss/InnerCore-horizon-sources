package java.util.stream;

import java.util.function.*;
import java.util.*;

final class SortedOps
{
    private SortedOps() {
    }
    
    static <T> Stream<T> makeRef(final AbstractPipeline<?, T, ?> abstractPipeline) {
        return (Stream<T>)new OfRef((AbstractPipeline<?, Object, ?>)abstractPipeline);
    }
    
    static <T> Stream<T> makeRef(final AbstractPipeline<?, T, ?> abstractPipeline, final Comparator<? super T> comparator) {
        return (Stream<T>)new OfRef((AbstractPipeline<?, Object, ?>)abstractPipeline, (Comparator<? super Object>)comparator);
    }
    
    static <T> IntStream makeInt(final AbstractPipeline<?, Integer, ?> abstractPipeline) {
        return new OfInt(abstractPipeline);
    }
    
    static <T> LongStream makeLong(final AbstractPipeline<?, Long, ?> abstractPipeline) {
        return new OfLong(abstractPipeline);
    }
    
    static <T> DoubleStream makeDouble(final AbstractPipeline<?, Double, ?> abstractPipeline) {
        return new OfDouble(abstractPipeline);
    }
    
    private abstract static class AbstractDoubleSortingSink extends Sink.ChainedDouble<Double>
    {
        protected boolean cancellationWasRequested;
        
        AbstractDoubleSortingSink(final Sink<? super Double> sink) {
            super(sink);
        }
        
        @Override
        public final boolean cancellationRequested() {
            this.cancellationWasRequested = true;
            return false;
        }
    }
    
    private abstract static class AbstractIntSortingSink extends Sink.ChainedInt<Integer>
    {
        protected boolean cancellationWasRequested;
        
        AbstractIntSortingSink(final Sink<? super Integer> sink) {
            super(sink);
        }
        
        @Override
        public final boolean cancellationRequested() {
            this.cancellationWasRequested = true;
            return false;
        }
    }
    
    private abstract static class AbstractLongSortingSink extends Sink.ChainedLong<Long>
    {
        protected boolean cancellationWasRequested;
        
        AbstractLongSortingSink(final Sink<? super Long> sink) {
            super(sink);
        }
        
        @Override
        public final boolean cancellationRequested() {
            this.cancellationWasRequested = true;
            return false;
        }
    }
    
    private abstract static class AbstractRefSortingSink<T> extends Sink.ChainedReference<T, T>
    {
        protected final Comparator<? super T> comparator;
        protected boolean cancellationWasRequested;
        
        AbstractRefSortingSink(final Sink<? super T> sink, final Comparator<? super T> comparator) {
            super(sink);
            this.comparator = comparator;
        }
        
        @Override
        public final boolean cancellationRequested() {
            this.cancellationWasRequested = true;
            return false;
        }
    }
    
    private static final class DoubleSortingSink extends AbstractDoubleSortingSink
    {
        private SpinedBuffer.OfDouble b;
        
        DoubleSortingSink(final Sink<? super Double> sink) {
            super(sink);
        }
        
        @Override
        public void begin(final long n) {
            if (n >= 2147483639L) {
                throw new IllegalArgumentException("Stream size exceeds max array size");
            }
            this.b = ((n > 0L) ? new SpinedBuffer.OfDouble((int)n) : new SpinedBuffer.OfDouble());
        }
        
        @Override
        public void end() {
            final double[] array = ((SpinedBuffer.OfPrimitive<E, double[], T_CONS>)this.b).asPrimitiveArray();
            Arrays.sort(array);
            this.downstream.begin(array.length);
            if (!this.cancellationWasRequested) {
                final double[] array2 = array;
                for (int length = array2.length, i = 0; i < length; ++i) {
                    this.downstream.accept(array2[i]);
                }
            }
            else {
                for (final double n : array) {
                    if (this.downstream.cancellationRequested()) {
                        break;
                    }
                    this.downstream.accept(n);
                }
            }
            this.downstream.end();
        }
        
        @Override
        public void accept(final double n) {
            this.b.accept(n);
        }
    }
    
    private static final class IntSortingSink extends AbstractIntSortingSink
    {
        private SpinedBuffer.OfInt b;
        
        IntSortingSink(final Sink<? super Integer> sink) {
            super(sink);
        }
        
        @Override
        public void begin(final long n) {
            if (n >= 2147483639L) {
                throw new IllegalArgumentException("Stream size exceeds max array size");
            }
            this.b = ((n > 0L) ? new SpinedBuffer.OfInt((int)n) : new SpinedBuffer.OfInt());
        }
        
        @Override
        public void end() {
            final int[] array = ((SpinedBuffer.OfPrimitive<E, int[], T_CONS>)this.b).asPrimitiveArray();
            Arrays.sort(array);
            this.downstream.begin(array.length);
            if (!this.cancellationWasRequested) {
                final int[] array2 = array;
                for (int length = array2.length, i = 0; i < length; ++i) {
                    this.downstream.accept(array2[i]);
                }
            }
            else {
                for (final int n : array) {
                    if (this.downstream.cancellationRequested()) {
                        break;
                    }
                    this.downstream.accept(n);
                }
            }
            this.downstream.end();
        }
        
        @Override
        public void accept(final int n) {
            this.b.accept(n);
        }
    }
    
    private static final class LongSortingSink extends AbstractLongSortingSink
    {
        private SpinedBuffer.OfLong b;
        
        LongSortingSink(final Sink<? super Long> sink) {
            super(sink);
        }
        
        @Override
        public void begin(final long n) {
            if (n >= 2147483639L) {
                throw new IllegalArgumentException("Stream size exceeds max array size");
            }
            this.b = ((n > 0L) ? new SpinedBuffer.OfLong((int)n) : new SpinedBuffer.OfLong());
        }
        
        @Override
        public void end() {
            final long[] array = ((SpinedBuffer.OfPrimitive<E, long[], T_CONS>)this.b).asPrimitiveArray();
            Arrays.sort(array);
            this.downstream.begin(array.length);
            if (!this.cancellationWasRequested) {
                final long[] array2 = array;
                for (int length = array2.length, i = 0; i < length; ++i) {
                    this.downstream.accept(array2[i]);
                }
            }
            else {
                for (final long n : array) {
                    if (this.downstream.cancellationRequested()) {
                        break;
                    }
                    this.downstream.accept(n);
                }
            }
            this.downstream.end();
        }
        
        @Override
        public void accept(final long n) {
            this.b.accept(n);
        }
    }
    
    private static final class OfDouble extends StatefulOp<Double>
    {
        OfDouble(final AbstractPipeline<?, Double, ?> abstractPipeline) {
            super(abstractPipeline, StreamShape.DOUBLE_VALUE, StreamOpFlag.IS_ORDERED | StreamOpFlag.IS_SORTED);
        }
        
        public Sink<Double> opWrapSink(final int n, final Sink<Double> sink) {
            Objects.requireNonNull(sink);
            if (StreamOpFlag.SORTED.isKnown(n)) {
                return sink;
            }
            if (StreamOpFlag.SIZED.isKnown(n)) {
                return new SizedDoubleSortingSink(sink);
            }
            return new DoubleSortingSink(sink);
        }
        
        public <P_IN> Node<Double> opEvaluateParallel(final PipelineHelper<Double> pipelineHelper, final Spliterator<P_IN> spliterator, final IntFunction<Double[]> intFunction) {
            if (StreamOpFlag.SORTED.isKnown(pipelineHelper.getStreamAndOpFlags())) {
                return pipelineHelper.evaluate(spliterator, false, intFunction);
            }
            final double[] array = ((Node.OfPrimitive<T, T_CONS, double[], T_SPLITR, T_NODE>)pipelineHelper.evaluate(spliterator, true, intFunction)).asPrimitiveArray();
            Arrays.parallelSort(array);
            return Nodes.node(array);
        }
    }
    
    private static final class SizedDoubleSortingSink extends AbstractDoubleSortingSink
    {
        private double[] array;
        private int offset;
        
        SizedDoubleSortingSink(final Sink<? super Double> sink) {
            super(sink);
        }
        
        @Override
        public void begin(final long n) {
            if (n >= 2147483639L) {
                throw new IllegalArgumentException("Stream size exceeds max array size");
            }
            this.array = new double[(int)n];
        }
        
        @Override
        public void end() {
            Arrays.sort(this.array, 0, this.offset);
            this.downstream.begin(this.offset);
            if (!this.cancellationWasRequested) {
                for (int i = 0; i < this.offset; ++i) {
                    this.downstream.accept(this.array[i]);
                }
            }
            else {
                for (int n = 0; n < this.offset && !this.downstream.cancellationRequested(); ++n) {
                    this.downstream.accept(this.array[n]);
                }
            }
            this.downstream.end();
            this.array = null;
        }
        
        @Override
        public void accept(final double n) {
            this.array[this.offset++] = n;
        }
    }
    
    private static final class OfInt extends StatefulOp<Integer>
    {
        OfInt(final AbstractPipeline<?, Integer, ?> abstractPipeline) {
            super(abstractPipeline, StreamShape.INT_VALUE, StreamOpFlag.IS_ORDERED | StreamOpFlag.IS_SORTED);
        }
        
        public Sink<Integer> opWrapSink(final int n, final Sink<Integer> sink) {
            Objects.requireNonNull(sink);
            if (StreamOpFlag.SORTED.isKnown(n)) {
                return sink;
            }
            if (StreamOpFlag.SIZED.isKnown(n)) {
                return new SizedIntSortingSink(sink);
            }
            return new IntSortingSink(sink);
        }
        
        public <P_IN> Node<Integer> opEvaluateParallel(final PipelineHelper<Integer> pipelineHelper, final Spliterator<P_IN> spliterator, final IntFunction<Integer[]> intFunction) {
            if (StreamOpFlag.SORTED.isKnown(pipelineHelper.getStreamAndOpFlags())) {
                return pipelineHelper.evaluate(spliterator, false, intFunction);
            }
            final int[] array = ((Node.OfPrimitive<T, T_CONS, int[], T_SPLITR, T_NODE>)pipelineHelper.evaluate(spliterator, true, intFunction)).asPrimitiveArray();
            Arrays.parallelSort(array);
            return Nodes.node(array);
        }
    }
    
    private static final class SizedIntSortingSink extends AbstractIntSortingSink
    {
        private int[] array;
        private int offset;
        
        SizedIntSortingSink(final Sink<? super Integer> sink) {
            super(sink);
        }
        
        @Override
        public void begin(final long n) {
            if (n >= 2147483639L) {
                throw new IllegalArgumentException("Stream size exceeds max array size");
            }
            this.array = new int[(int)n];
        }
        
        @Override
        public void end() {
            Arrays.sort(this.array, 0, this.offset);
            this.downstream.begin(this.offset);
            if (!this.cancellationWasRequested) {
                for (int i = 0; i < this.offset; ++i) {
                    this.downstream.accept(this.array[i]);
                }
            }
            else {
                for (int n = 0; n < this.offset && !this.downstream.cancellationRequested(); ++n) {
                    this.downstream.accept(this.array[n]);
                }
            }
            this.downstream.end();
            this.array = null;
        }
        
        @Override
        public void accept(final int n) {
            this.array[this.offset++] = n;
        }
    }
    
    private static final class OfLong extends StatefulOp<Long>
    {
        OfLong(final AbstractPipeline<?, Long, ?> abstractPipeline) {
            super(abstractPipeline, StreamShape.LONG_VALUE, StreamOpFlag.IS_ORDERED | StreamOpFlag.IS_SORTED);
        }
        
        public Sink<Long> opWrapSink(final int n, final Sink<Long> sink) {
            Objects.requireNonNull(sink);
            if (StreamOpFlag.SORTED.isKnown(n)) {
                return sink;
            }
            if (StreamOpFlag.SIZED.isKnown(n)) {
                return new SizedLongSortingSink(sink);
            }
            return new LongSortingSink(sink);
        }
        
        public <P_IN> Node<Long> opEvaluateParallel(final PipelineHelper<Long> pipelineHelper, final Spliterator<P_IN> spliterator, final IntFunction<Long[]> intFunction) {
            if (StreamOpFlag.SORTED.isKnown(pipelineHelper.getStreamAndOpFlags())) {
                return pipelineHelper.evaluate(spliterator, false, intFunction);
            }
            final long[] array = ((Node.OfPrimitive<T, T_CONS, long[], T_SPLITR, T_NODE>)pipelineHelper.evaluate(spliterator, true, intFunction)).asPrimitiveArray();
            Arrays.parallelSort(array);
            return Nodes.node(array);
        }
    }
    
    private static final class SizedLongSortingSink extends AbstractLongSortingSink
    {
        private long[] array;
        private int offset;
        
        SizedLongSortingSink(final Sink<? super Long> sink) {
            super(sink);
        }
        
        @Override
        public void begin(final long n) {
            if (n >= 2147483639L) {
                throw new IllegalArgumentException("Stream size exceeds max array size");
            }
            this.array = new long[(int)n];
        }
        
        @Override
        public void end() {
            Arrays.sort(this.array, 0, this.offset);
            this.downstream.begin(this.offset);
            if (!this.cancellationWasRequested) {
                for (int i = 0; i < this.offset; ++i) {
                    this.downstream.accept(this.array[i]);
                }
            }
            else {
                for (int n = 0; n < this.offset && !this.downstream.cancellationRequested(); ++n) {
                    this.downstream.accept(this.array[n]);
                }
            }
            this.downstream.end();
            this.array = null;
        }
        
        @Override
        public void accept(final long n) {
            this.array[this.offset++] = n;
        }
    }
    
    private static final class OfRef<T> extends StatefulOp<T, T>
    {
        private final boolean isNaturalSort;
        private final Comparator<? super T> comparator;
        
        OfRef(final AbstractPipeline<?, T, ?> abstractPipeline) {
            super(abstractPipeline, StreamShape.REFERENCE, StreamOpFlag.IS_ORDERED | StreamOpFlag.IS_SORTED);
            this.isNaturalSort = true;
            this.comparator = Comparator.naturalOrder();
        }
        
        OfRef(final AbstractPipeline<?, T, ?> abstractPipeline, final Comparator<? super T> comparator) {
            super(abstractPipeline, StreamShape.REFERENCE, StreamOpFlag.IS_ORDERED | StreamOpFlag.NOT_SORTED);
            this.isNaturalSort = false;
            this.comparator = Objects.requireNonNull(comparator);
        }
        
        public Sink<T> opWrapSink(final int n, final Sink<T> sink) {
            Objects.requireNonNull(sink);
            if (StreamOpFlag.SORTED.isKnown(n) && this.isNaturalSort) {
                return sink;
            }
            if (StreamOpFlag.SIZED.isKnown(n)) {
                return (Sink<T>)new SizedRefSortingSink((Sink<? super Object>)sink, (Comparator<? super Object>)this.comparator);
            }
            return (Sink<T>)new RefSortingSink((Sink<? super Object>)sink, (Comparator<? super Object>)this.comparator);
        }
        
        public <P_IN> Node<T> opEvaluateParallel(final PipelineHelper<T> pipelineHelper, final Spliterator<P_IN> spliterator, final IntFunction<T[]> intFunction) {
            if (StreamOpFlag.SORTED.isKnown(pipelineHelper.getStreamAndOpFlags()) && this.isNaturalSort) {
                return pipelineHelper.evaluate(spliterator, false, intFunction);
            }
            final T[] array = pipelineHelper.evaluate(spliterator, true, intFunction).asArray(intFunction);
            Arrays.parallelSort(array, this.comparator);
            return Nodes.node(array);
        }
    }
    
    private static final class RefSortingSink<T> extends AbstractRefSortingSink<T>
    {
        private ArrayList<T> list;
        
        RefSortingSink(final Sink<? super T> sink, final Comparator<? super T> comparator) {
            super(sink, comparator);
        }
        
        @Override
        public void begin(final long n) {
            if (n >= 2147483639L) {
                throw new IllegalArgumentException("Stream size exceeds max array size");
            }
            this.list = ((n >= 0L) ? new ArrayList<T>((int)n) : new ArrayList<T>());
        }
        
        @Override
        public void end() {
            this.list.sort(this.comparator);
            this.downstream.begin(this.list.size());
            if (!this.cancellationWasRequested) {
                this.list.forEach(this.downstream::accept);
            }
            else {
                for (final T next : this.list) {
                    if (this.downstream.cancellationRequested()) {
                        break;
                    }
                    this.downstream.accept((Object)next);
                }
            }
            this.downstream.end();
            this.list = null;
        }
        
        @Override
        public void accept(final T t) {
            this.list.add(t);
        }
    }
    
    private static final class SizedRefSortingSink<T> extends AbstractRefSortingSink<T>
    {
        private T[] array;
        private int offset;
        
        SizedRefSortingSink(final Sink<? super T> sink, final Comparator<? super T> comparator) {
            super(sink, comparator);
        }
        
        @Override
        public void begin(final long n) {
            if (n >= 2147483639L) {
                throw new IllegalArgumentException("Stream size exceeds max array size");
            }
            this.array = (T[])new Object[(int)n];
        }
        
        @Override
        public void end() {
            Arrays.sort(this.array, 0, this.offset, this.comparator);
            this.downstream.begin(this.offset);
            if (!this.cancellationWasRequested) {
                for (int i = 0; i < this.offset; ++i) {
                    this.downstream.accept((Object)this.array[i]);
                }
            }
            else {
                for (int n = 0; n < this.offset && !this.downstream.cancellationRequested(); ++n) {
                    this.downstream.accept((Object)this.array[n]);
                }
            }
            this.downstream.end();
            this.array = null;
        }
        
        @Override
        public void accept(final T t) {
            this.array[this.offset++] = t;
        }
    }
}
