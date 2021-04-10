package java.util.stream;

import java.util.concurrent.*;
import java.util.function.*;
import java.util.*;

final class Nodes
{
    static final long MAX_ARRAY_SIZE = 2147483639L;
    static final String BAD_SIZE = "Stream size exceeds max array size";
    private static final Node EMPTY_NODE;
    private static final Node.OfInt EMPTY_INT_NODE;
    private static final Node.OfLong EMPTY_LONG_NODE;
    private static final Node.OfDouble EMPTY_DOUBLE_NODE;
    private static final int[] EMPTY_INT_ARRAY;
    private static final long[] EMPTY_LONG_ARRAY;
    private static final double[] EMPTY_DOUBLE_ARRAY;
    
    private Nodes() {
        throw new Error("no instances");
    }
    
    static <T> Node<T> emptyNode(final StreamShape streamShape) {
        switch (streamShape) {
            case REFERENCE: {
                return (Node<T>)Nodes.EMPTY_NODE;
            }
            case INT_VALUE: {
                return (Node<T>)Nodes.EMPTY_INT_NODE;
            }
            case LONG_VALUE: {
                return (Node<T>)Nodes.EMPTY_LONG_NODE;
            }
            case DOUBLE_VALUE: {
                return (Node<T>)Nodes.EMPTY_DOUBLE_NODE;
            }
            default: {
                throw new IllegalStateException("Unknown shape " + streamShape);
            }
        }
    }
    
    static <T> Node<T> conc(final StreamShape streamShape, final Node<T> node, final Node<T> node2) {
        switch (streamShape) {
            case REFERENCE: {
                return (Node<T>)new ConcNode((Node<Object>)node, (Node<Object>)node2);
            }
            case INT_VALUE: {
                return (Node<T>)new ConcNode.OfInt((Node.OfInt)node, (Node.OfInt)node2);
            }
            case LONG_VALUE: {
                return (Node<T>)new ConcNode.OfLong((Node.OfLong)node, (Node.OfLong)node2);
            }
            case DOUBLE_VALUE: {
                return (Node<T>)new ConcNode.OfDouble((Node.OfDouble)node, (Node.OfDouble)node2);
            }
            default: {
                throw new IllegalStateException("Unknown shape " + streamShape);
            }
        }
    }
    
    static <T> Node<T> node(final T[] array) {
        return new ArrayNode<T>(array);
    }
    
    static <T> Node<T> node(final Collection<T> collection) {
        return new CollectionNode<T>(collection);
    }
    
    static <T> Node.Builder<T> builder(final long n, final IntFunction<T[]> intFunction) {
        return (n >= 0L && n < 2147483639L) ? new FixedNodeBuilder<T>(n, intFunction) : builder();
    }
    
    static <T> Node.Builder<T> builder() {
        return new SpinedNodeBuilder<T>();
    }
    
    static Node.OfInt node(final int[] array) {
        return new IntArrayNode(array);
    }
    
    static Node.Builder.OfInt intBuilder(final long n) {
        return (n >= 0L && n < 2147483639L) ? new IntFixedNodeBuilder(n) : intBuilder();
    }
    
    static Node.Builder.OfInt intBuilder() {
        return new IntSpinedNodeBuilder();
    }
    
    static Node.OfLong node(final long[] array) {
        return new LongArrayNode(array);
    }
    
    static Node.Builder.OfLong longBuilder(final long n) {
        return (n >= 0L && n < 2147483639L) ? new LongFixedNodeBuilder(n) : longBuilder();
    }
    
    static Node.Builder.OfLong longBuilder() {
        return new LongSpinedNodeBuilder();
    }
    
    static Node.OfDouble node(final double[] array) {
        return new DoubleArrayNode(array);
    }
    
    static Node.Builder.OfDouble doubleBuilder(final long n) {
        return (n >= 0L && n < 2147483639L) ? new DoubleFixedNodeBuilder(n) : doubleBuilder();
    }
    
    static Node.Builder.OfDouble doubleBuilder() {
        return new DoubleSpinedNodeBuilder();
    }
    
    public static <P_IN, P_OUT> Node<P_OUT> collect(final PipelineHelper<P_OUT> pipelineHelper, final Spliterator<P_IN> spliterator, final boolean b, final IntFunction<P_OUT[]> intFunction) {
        final long exactOutputSizeIfKnown = pipelineHelper.exactOutputSizeIfKnown(spliterator);
        if (exactOutputSizeIfKnown < 0L || !spliterator.hasCharacteristics(16384)) {
            final Node<P_OUT> node = new CollectorTask.OfRef<Object, Object>((PipelineHelper<Object>)pipelineHelper, (IntFunction<Object[]>)intFunction, (Spliterator<Object>)spliterator).invoke();
            return b ? flatten(node, intFunction) : node;
        }
        if (exactOutputSizeIfKnown >= 2147483639L) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        final Object[] array = (Object[])intFunction.apply((int)exactOutputSizeIfKnown);
        new SizedCollectorTask.OfRef((Spliterator<Object>)spliterator, (PipelineHelper<Object>)pipelineHelper, array).invoke();
        return node(array);
    }
    
    public static <P_IN> Node.OfInt collectInt(final PipelineHelper<Integer> pipelineHelper, final Spliterator<P_IN> spliterator, final boolean b) {
        final long exactOutputSizeIfKnown = pipelineHelper.exactOutputSizeIfKnown(spliterator);
        if (exactOutputSizeIfKnown < 0L || !spliterator.hasCharacteristics(16384)) {
            final Node.OfInt ofInt = new CollectorTask.OfInt<Object>(pipelineHelper, (Spliterator<Object>)spliterator).invoke();
            return b ? flattenInt(ofInt) : ofInt;
        }
        if (exactOutputSizeIfKnown >= 2147483639L) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        final int[] array = new int[(int)exactOutputSizeIfKnown];
        new SizedCollectorTask.OfInt((Spliterator<Object>)spliterator, pipelineHelper, array).invoke();
        return node(array);
    }
    
    public static <P_IN> Node.OfLong collectLong(final PipelineHelper<Long> pipelineHelper, final Spliterator<P_IN> spliterator, final boolean b) {
        final long exactOutputSizeIfKnown = pipelineHelper.exactOutputSizeIfKnown(spliterator);
        if (exactOutputSizeIfKnown < 0L || !spliterator.hasCharacteristics(16384)) {
            final Node.OfLong ofLong = new CollectorTask.OfLong<Object>(pipelineHelper, (Spliterator<Object>)spliterator).invoke();
            return b ? flattenLong(ofLong) : ofLong;
        }
        if (exactOutputSizeIfKnown >= 2147483639L) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        final long[] array = new long[(int)exactOutputSizeIfKnown];
        new SizedCollectorTask.OfLong((Spliterator<Object>)spliterator, pipelineHelper, array).invoke();
        return node(array);
    }
    
    public static <P_IN> Node.OfDouble collectDouble(final PipelineHelper<Double> pipelineHelper, final Spliterator<P_IN> spliterator, final boolean b) {
        final long exactOutputSizeIfKnown = pipelineHelper.exactOutputSizeIfKnown(spliterator);
        if (exactOutputSizeIfKnown < 0L || !spliterator.hasCharacteristics(16384)) {
            final Node.OfDouble ofDouble = new CollectorTask.OfDouble<Object>(pipelineHelper, (Spliterator<Object>)spliterator).invoke();
            return b ? flattenDouble(ofDouble) : ofDouble;
        }
        if (exactOutputSizeIfKnown >= 2147483639L) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        final double[] array = new double[(int)exactOutputSizeIfKnown];
        new SizedCollectorTask.OfDouble((Spliterator<Object>)spliterator, pipelineHelper, array).invoke();
        return node(array);
    }
    
    public static <T> Node<T> flatten(final Node<T> node, final IntFunction<T[]> intFunction) {
        if (node.getChildCount() <= 0) {
            return node;
        }
        final long count = node.count();
        if (count >= 2147483639L) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        final T[] array = intFunction.apply((int)count);
        new ToArrayTask.OfRef((Node)node, (Object[])array, 0).invoke();
        return node(array);
    }
    
    public static Node.OfInt flattenInt(final Node.OfInt ofInt) {
        if (ofInt.getChildCount() <= 0) {
            return ofInt;
        }
        final long count = ofInt.count();
        if (count >= 2147483639L) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        final int[] array = new int[(int)count];
        new ToArrayTask.OfInt(ofInt, array, 0).invoke();
        return node(array);
    }
    
    public static Node.OfLong flattenLong(final Node.OfLong ofLong) {
        if (ofLong.getChildCount() <= 0) {
            return ofLong;
        }
        final long count = ofLong.count();
        if (count >= 2147483639L) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        final long[] array = new long[(int)count];
        new ToArrayTask.OfLong(ofLong, array, 0).invoke();
        return node(array);
    }
    
    public static Node.OfDouble flattenDouble(final Node.OfDouble ofDouble) {
        if (ofDouble.getChildCount() <= 0) {
            return ofDouble;
        }
        final long count = ofDouble.count();
        if (count >= 2147483639L) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        final double[] array = new double[(int)count];
        new ToArrayTask.OfDouble(ofDouble, array, 0).invoke();
        return node(array);
    }
    
    static {
        EMPTY_NODE = new EmptyNode.OfRef();
        EMPTY_INT_NODE = new EmptyNode.OfInt();
        EMPTY_LONG_NODE = new EmptyNode.OfLong();
        EMPTY_DOUBLE_NODE = new EmptyNode.OfDouble();
        EMPTY_INT_ARRAY = new int[0];
        EMPTY_LONG_ARRAY = new long[0];
        EMPTY_DOUBLE_ARRAY = new double[0];
    }
    
    private abstract static class AbstractConcNode<T, T_NODE extends Node<T>> implements Node<T>
    {
        protected final T_NODE left;
        protected final T_NODE right;
        private final long size;
        
        AbstractConcNode(final T_NODE left, final T_NODE right) {
            this.left = left;
            this.right = right;
            this.size = left.count() + right.count();
        }
        
        @Override
        public int getChildCount() {
            return 2;
        }
        
        @Override
        public T_NODE getChild(final int n) {
            if (n == 0) {
                return this.left;
            }
            if (n == 1) {
                return this.right;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public long count() {
            return this.size;
        }
    }
    
    private static class ArrayNode<T> implements Node<T>
    {
        final T[] array;
        int curSize;
        
        ArrayNode(final long n, final IntFunction<T[]> intFunction) {
            if (n >= 2147483639L) {
                throw new IllegalArgumentException("Stream size exceeds max array size");
            }
            this.array = intFunction.apply((int)n);
            this.curSize = 0;
        }
        
        ArrayNode(final T[] array) {
            this.array = array;
            this.curSize = array.length;
        }
        
        @Override
        public Spliterator<T> spliterator() {
            return Arrays.spliterator(this.array, 0, this.curSize);
        }
        
        @Override
        public void copyInto(final T[] array, final int n) {
            System.arraycopy(this.array, 0, array, n, this.curSize);
        }
        
        @Override
        public T[] asArray(final IntFunction<T[]> intFunction) {
            if (this.array.length == this.curSize) {
                return this.array;
            }
            throw new IllegalStateException();
        }
        
        @Override
        public long count() {
            return this.curSize;
        }
        
        @Override
        public void forEach(final Consumer<? super T> consumer) {
            for (int i = 0; i < this.curSize; ++i) {
                consumer.accept(this.array[i]);
            }
        }
        
        @Override
        public String toString() {
            return String.format("ArrayNode[%d][%s]", this.array.length - this.curSize, Arrays.toString(this.array));
        }
    }
    
    private static final class CollectionNode<T> implements Node<T>
    {
        private final Collection<T> c;
        
        CollectionNode(final Collection<T> c) {
            this.c = c;
        }
        
        @Override
        public Spliterator<T> spliterator() {
            return this.c.stream().spliterator();
        }
        
        @Override
        public void copyInto(final T[] array, int n) {
            final Iterator<T> iterator = this.c.iterator();
            while (iterator.hasNext()) {
                array[n++] = iterator.next();
            }
        }
        
        @Override
        public T[] asArray(final IntFunction<T[]> intFunction) {
            return this.c.toArray(intFunction.apply(this.c.size()));
        }
        
        @Override
        public long count() {
            return this.c.size();
        }
        
        @Override
        public void forEach(final Consumer<? super T> consumer) {
            this.c.forEach(consumer);
        }
        
        @Override
        public String toString() {
            return String.format("CollectionNode[%d][%s]", this.c.size(), this.c);
        }
    }
    
    private static class CollectorTask<P_IN, P_OUT, T_NODE extends Node<P_OUT>, T_BUILDER extends Node.Builder<P_OUT>> extends AbstractTask<P_IN, P_OUT, T_NODE, CollectorTask<P_IN, P_OUT, T_NODE, T_BUILDER>>
    {
        protected final PipelineHelper<P_OUT> helper;
        protected final LongFunction<T_BUILDER> builderFactory;
        protected final BinaryOperator<T_NODE> concFactory;
        
        CollectorTask(final PipelineHelper<P_OUT> helper, final Spliterator<P_IN> spliterator, final LongFunction<T_BUILDER> builderFactory, final BinaryOperator<T_NODE> concFactory) {
            super(helper, spliterator);
            this.helper = helper;
            this.builderFactory = builderFactory;
            this.concFactory = concFactory;
        }
        
        CollectorTask(final CollectorTask<P_IN, P_OUT, T_NODE, T_BUILDER> collectorTask, final Spliterator<P_IN> spliterator) {
            super(collectorTask, spliterator);
            this.helper = collectorTask.helper;
            this.builderFactory = collectorTask.builderFactory;
            this.concFactory = collectorTask.concFactory;
        }
        
        @Override
        protected CollectorTask<P_IN, P_OUT, T_NODE, T_BUILDER> makeChild(final Spliterator<P_IN> spliterator) {
            return new CollectorTask<P_IN, P_OUT, T_NODE, T_BUILDER>(this, spliterator);
        }
        
        @Override
        protected T_NODE doLeaf() {
            return (T_NODE)this.helper.wrapAndCopyInto(this.builderFactory.apply(this.helper.exactOutputSizeIfKnown(this.spliterator)), this.spliterator).build();
        }
        
        @Override
        public void onCompletion(final CountedCompleter<?> countedCompleter) {
            if (!this.isLeaf()) {
                this.setLocalResult(this.concFactory.apply((T_NODE)((CollectorTask)this.leftChild).getLocalResult(), (T_NODE)((CollectorTask)this.rightChild).getLocalResult()));
            }
            super.onCompletion(countedCompleter);
        }
        
        private static final class OfDouble<P_IN> extends CollectorTask<P_IN, Double, Node.OfDouble, Node.Builder.OfDouble>
        {
            OfDouble(final PipelineHelper<Double> pipelineHelper, final Spliterator<P_IN> spliterator) {
                super((PipelineHelper<Object>)pipelineHelper, spliterator, Nodes::doubleBuilder, ConcNode.OfDouble::new);
            }
        }
        
        private static final class OfInt<P_IN> extends CollectorTask<P_IN, Integer, Node.OfInt, Node.Builder.OfInt>
        {
            OfInt(final PipelineHelper<Integer> pipelineHelper, final Spliterator<P_IN> spliterator) {
                super((PipelineHelper<Object>)pipelineHelper, spliterator, Nodes::intBuilder, ConcNode.OfInt::new);
            }
        }
        
        private static final class OfLong<P_IN> extends CollectorTask<P_IN, Long, Node.OfLong, Node.Builder.OfLong>
        {
            OfLong(final PipelineHelper<Long> pipelineHelper, final Spliterator<P_IN> spliterator) {
                super((PipelineHelper<Object>)pipelineHelper, spliterator, Nodes::longBuilder, ConcNode.OfLong::new);
            }
        }
        
        private static final class OfRef<P_IN, P_OUT> extends CollectorTask<P_IN, P_OUT, Node<P_OUT>, Node.Builder<P_OUT>>
        {
            OfRef(final PipelineHelper<P_OUT> pipelineHelper, final IntFunction<P_OUT[]> intFunction, final Spliterator<P_IN> spliterator) {
                super(pipelineHelper, spliterator, n -> Nodes.builder(n, intFunction), ConcNode::new);
            }
        }
    }
    
    static final class ConcNode<T> extends AbstractConcNode<T, Node<T>> implements Node<T>
    {
        ConcNode(final Node<T> node, final Node<T> node2) {
            super(node, node2);
        }
        
        @Override
        public Spliterator<T> spliterator() {
            return (Spliterator<T>)new InternalNodeSpliterator.OfRef((Node<Object>)this);
        }
        
        @Override
        public void copyInto(final T[] array, final int n) {
            Objects.requireNonNull(array);
            this.left.copyInto((T[])array, n);
            this.right.copyInto((T[])array, n + (int)this.left.count());
        }
        
        @Override
        public T[] asArray(final IntFunction<T[]> intFunction) {
            final long count = this.count();
            if (count >= 2147483639L) {
                throw new IllegalArgumentException("Stream size exceeds max array size");
            }
            final T[] array = intFunction.apply((int)count);
            this.copyInto(array, 0);
            return array;
        }
        
        @Override
        public void forEach(final Consumer<? super T> consumer) {
            this.left.forEach((Consumer<? super T>)consumer);
            this.right.forEach((Consumer<? super T>)consumer);
        }
        
        @Override
        public Node<T> truncate(final long n, final long n2, final IntFunction<T[]> intFunction) {
            if (n == 0L && n2 == this.count()) {
                return this;
            }
            final long count = this.left.count();
            if (n >= count) {
                return (Node<T>)this.right.truncate(n - count, n2 - count, (IntFunction<T[]>)intFunction);
            }
            if (n2 <= count) {
                return (Node<T>)this.left.truncate(n, n2, (IntFunction<T[]>)intFunction);
            }
            return Nodes.conc(this.getShape(), (Node<T>)this.left.truncate(n, count, (IntFunction<T[]>)intFunction), (Node<T>)this.right.truncate(0L, n2 - count, (IntFunction<T[]>)intFunction));
        }
        
        @Override
        public String toString() {
            if (this.count() < 32L) {
                return String.format("ConcNode[%s.%s]", this.left, this.right);
            }
            return String.format("ConcNode[size=%d]", this.count());
        }
        
        static final class OfDouble extends ConcNode.OfPrimitive<Double, DoubleConsumer, double[], Spliterator.OfDouble, Node.OfDouble> implements Node.OfDouble
        {
            OfDouble(final Node.OfDouble ofDouble, final Node.OfDouble ofDouble2) {
                super(ofDouble, ofDouble2);
            }
            
            @Override
            public Spliterator.OfDouble spliterator() {
                return new InternalNodeSpliterator.OfDouble(this);
            }
        }
        
        private abstract static class OfPrimitive<E, T_CONS, T_ARR, T_SPLITR extends Spliterator.OfPrimitive<E, T_CONS, T_SPLITR>, T_NODE extends Node.OfPrimitive<E, T_CONS, T_ARR, T_SPLITR, T_NODE>> extends AbstractConcNode<E, T_NODE> implements Node.OfPrimitive<E, T_CONS, T_ARR, T_SPLITR, T_NODE>
        {
            OfPrimitive(final T_NODE t_NODE, final T_NODE t_NODE2) {
                super(t_NODE, t_NODE2);
            }
            
            @Override
            public void forEach(final T_CONS t_CONS) {
                ((Node.OfPrimitive)this.left).forEach(t_CONS);
                ((Node.OfPrimitive)this.right).forEach(t_CONS);
            }
            
            @Override
            public void copyInto(final T_ARR t_ARR, final int n) {
                ((Node.OfPrimitive)this.left).copyInto(t_ARR, n);
                ((Node.OfPrimitive)this.right).copyInto(t_ARR, n + (int)((Node.OfPrimitive)this.left).count());
            }
            
            @Override
            public T_ARR asPrimitiveArray() {
                final long count = this.count();
                if (count >= 2147483639L) {
                    throw new IllegalArgumentException("Stream size exceeds max array size");
                }
                final T_ARR array = this.newArray((int)count);
                this.copyInto(array, 0);
                return array;
            }
            
            @Override
            public String toString() {
                if (this.count() < 32L) {
                    return String.format("%s[%s.%s]", this.getClass().getName(), this.left, this.right);
                }
                return String.format("%s[size=%d]", this.getClass().getName(), this.count());
            }
        }
        
        static final class OfInt extends ConcNode.OfPrimitive<Integer, IntConsumer, int[], Spliterator.OfInt, Node.OfInt> implements Node.OfInt
        {
            OfInt(final Node.OfInt ofInt, final Node.OfInt ofInt2) {
                super(ofInt, ofInt2);
            }
            
            @Override
            public Spliterator.OfInt spliterator() {
                return new InternalNodeSpliterator.OfInt(this);
            }
        }
        
        static final class OfLong extends ConcNode.OfPrimitive<Long, LongConsumer, long[], Spliterator.OfLong, Node.OfLong> implements Node.OfLong
        {
            OfLong(final Node.OfLong ofLong, final Node.OfLong ofLong2) {
                super(ofLong, ofLong2);
            }
            
            @Override
            public Spliterator.OfLong spliterator() {
                return new InternalNodeSpliterator.OfLong(this);
            }
        }
    }
    
    private abstract static class InternalNodeSpliterator<T, S extends Spliterator<T>, N extends Node<T>> implements Spliterator<T>
    {
        N curNode;
        int curChildIndex;
        S lastNodeSpliterator;
        S tryAdvanceSpliterator;
        Deque<N> tryAdvanceStack;
        
        InternalNodeSpliterator(final N curNode) {
            this.curNode = curNode;
        }
        
        protected final Deque<N> initStack() {
            final ArrayDeque<Node<T>> arrayDeque = (ArrayDeque<Node<T>>)new ArrayDeque<N>(8);
            for (int i = this.curNode.getChildCount() - 1; i >= this.curChildIndex; --i) {
                arrayDeque.addFirst(this.curNode.getChild(i));
            }
            return (Deque<N>)arrayDeque;
        }
        
        protected final N findNextLeafNode(final Deque<N> deque) {
            Node<T> node;
            while ((node = deque.pollFirst()) != null) {
                if (node.getChildCount() == 0) {
                    if (node.count() > 0L) {
                        return (N)node;
                    }
                    continue;
                }
                else {
                    for (int i = node.getChildCount() - 1; i >= 0; --i) {
                        deque.addFirst((N)node.getChild(i));
                    }
                }
            }
            return null;
        }
        
        protected final boolean initTryAdvance() {
            if (this.curNode == null) {
                return false;
            }
            if (this.tryAdvanceSpliterator == null) {
                if (this.lastNodeSpliterator == null) {
                    this.tryAdvanceStack = this.initStack();
                    final Node<T> nextLeafNode = this.findNextLeafNode((Deque<Node<T>>)this.tryAdvanceStack);
                    if (nextLeafNode == null) {
                        this.curNode = null;
                        return false;
                    }
                    this.tryAdvanceSpliterator = (S)nextLeafNode.spliterator();
                }
                else {
                    this.tryAdvanceSpliterator = this.lastNodeSpliterator;
                }
            }
            return true;
        }
        
        @Override
        public final S trySplit() {
            if (this.curNode == null || this.tryAdvanceSpliterator != null) {
                return null;
            }
            if (this.lastNodeSpliterator != null) {
                return (S)this.lastNodeSpliterator.trySplit();
            }
            if (this.curChildIndex < this.curNode.getChildCount() - 1) {
                return (S)this.curNode.getChild(this.curChildIndex++).spliterator();
            }
            this.curNode = (N)this.curNode.getChild(this.curChildIndex);
            if (this.curNode.getChildCount() == 0) {
                this.lastNodeSpliterator = (S)this.curNode.spliterator();
                return (S)this.lastNodeSpliterator.trySplit();
            }
            this.curChildIndex = 0;
            return (S)this.curNode.getChild(this.curChildIndex++).spliterator();
        }
        
        @Override
        public final long estimateSize() {
            if (this.curNode == null) {
                return 0L;
            }
            if (this.lastNodeSpliterator != null) {
                return this.lastNodeSpliterator.estimateSize();
            }
            long n = 0L;
            for (int i = this.curChildIndex; i < this.curNode.getChildCount(); ++i) {
                n += this.curNode.getChild(i).count();
            }
            return n;
        }
        
        @Override
        public final int characteristics() {
            return 64;
        }
        
        private static final class OfDouble extends InternalNodeSpliterator.OfPrimitive<Double, DoubleConsumer, double[], Spliterator.OfDouble, Node.OfDouble> implements Spliterator.OfDouble
        {
            OfDouble(final Node.OfDouble ofDouble) {
                super(ofDouble);
            }
        }
        
        private abstract static class OfPrimitive<T, T_CONS, T_ARR, T_SPLITR extends Spliterator.OfPrimitive<T, T_CONS, T_SPLITR>, N extends Node.OfPrimitive<T, T_CONS, T_ARR, T_SPLITR, N>> extends InternalNodeSpliterator<T, T_SPLITR, N> implements Spliterator.OfPrimitive<T, T_CONS, T_SPLITR>
        {
            OfPrimitive(final N n) {
                super(n);
            }
            
            @Override
            public boolean tryAdvance(final T_CONS t_CONS) {
                if (!this.initTryAdvance()) {
                    return false;
                }
                final boolean tryAdvance = ((Spliterator.OfPrimitive)this.tryAdvanceSpliterator).tryAdvance(t_CONS);
                if (!tryAdvance) {
                    if (this.lastNodeSpliterator == null) {
                        final Node.OfPrimitive<T, T_CONS, T_ARR, Spliterator, T_NODE> ofPrimitive = this.findNextLeafNode((Deque<Node.OfPrimitive<T, T_CONS, T_ARR, Spliterator, T_NODE>>)this.tryAdvanceStack);
                        if (ofPrimitive != null) {
                            this.tryAdvanceSpliterator = (S)ofPrimitive.spliterator();
                            return ((Spliterator.OfPrimitive)this.tryAdvanceSpliterator).tryAdvance(t_CONS);
                        }
                    }
                    this.curNode = null;
                }
                return tryAdvance;
            }
            
            @Override
            public void forEachRemaining(final T_CONS t_CONS) {
                if (this.curNode == null) {
                    return;
                }
                if (this.tryAdvanceSpliterator == null) {
                    if (this.lastNodeSpliterator == null) {
                        Node.OfPrimitive<T, T_CONS, T_ARR, T_SPLITR, N> ofPrimitive;
                        while ((ofPrimitive = this.findNextLeafNode(this.initStack())) != null) {
                            ofPrimitive.forEach(t_CONS);
                        }
                        this.curNode = null;
                    }
                    else {
                        ((Spliterator.OfPrimitive)this.lastNodeSpliterator).forEachRemaining(t_CONS);
                    }
                }
                else {
                    while (this.tryAdvance(t_CONS)) {}
                }
            }
        }
        
        private static final class OfInt extends InternalNodeSpliterator.OfPrimitive<Integer, IntConsumer, int[], Spliterator.OfInt, Node.OfInt> implements Spliterator.OfInt
        {
            OfInt(final Node.OfInt ofInt) {
                super(ofInt);
            }
        }
        
        private static final class OfLong extends InternalNodeSpliterator.OfPrimitive<Long, LongConsumer, long[], Spliterator.OfLong, Node.OfLong> implements Spliterator.OfLong
        {
            OfLong(final Node.OfLong ofLong) {
                super(ofLong);
            }
        }
        
        private static final class OfRef<T> extends InternalNodeSpliterator<T, Spliterator<T>, Node<T>>
        {
            OfRef(final Node<T> node) {
                super(node);
            }
            
            @Override
            public boolean tryAdvance(final Consumer<? super T> consumer) {
                if (!this.initTryAdvance()) {
                    return false;
                }
                final boolean tryAdvance = this.tryAdvanceSpliterator.tryAdvance((Consumer<? super T>)consumer);
                if (!tryAdvance) {
                    if (this.lastNodeSpliterator == null) {
                        final Node<T> nextLeafNode = this.findNextLeafNode((Deque<Node<T>>)this.tryAdvanceStack);
                        if (nextLeafNode != null) {
                            this.tryAdvanceSpliterator = (S)nextLeafNode.spliterator();
                            return this.tryAdvanceSpliterator.tryAdvance((Consumer<? super T>)consumer);
                        }
                    }
                    this.curNode = null;
                }
                return tryAdvance;
            }
            
            @Override
            public void forEachRemaining(final Consumer<? super T> consumer) {
                if (this.curNode == null) {
                    return;
                }
                if (this.tryAdvanceSpliterator == null) {
                    if (this.lastNodeSpliterator == null) {
                        Node<T> nextLeafNode;
                        while ((nextLeafNode = this.findNextLeafNode(this.initStack())) != null) {
                            nextLeafNode.forEach(consumer);
                        }
                        this.curNode = null;
                    }
                    else {
                        this.lastNodeSpliterator.forEachRemaining((Consumer<? super T>)consumer);
                    }
                }
                else {
                    while (this.tryAdvance(consumer)) {}
                }
            }
        }
    }
    
    private static class DoubleArrayNode implements Node.OfDouble
    {
        final double[] array;
        int curSize;
        
        DoubleArrayNode(final long n) {
            if (n >= 2147483639L) {
                throw new IllegalArgumentException("Stream size exceeds max array size");
            }
            this.array = new double[(int)n];
            this.curSize = 0;
        }
        
        DoubleArrayNode(final double[] array) {
            this.array = array;
            this.curSize = array.length;
        }
        
        @Override
        public Spliterator.OfDouble spliterator() {
            return Arrays.spliterator(this.array, 0, this.curSize);
        }
        
        @Override
        public double[] asPrimitiveArray() {
            if (this.array.length == this.curSize) {
                return this.array;
            }
            return Arrays.copyOf(this.array, this.curSize);
        }
        
        @Override
        public void copyInto(final double[] array, final int n) {
            System.arraycopy(this.array, 0, array, n, this.curSize);
        }
        
        @Override
        public long count() {
            return this.curSize;
        }
        
        @Override
        public void forEach(final DoubleConsumer doubleConsumer) {
            for (int i = 0; i < this.curSize; ++i) {
                doubleConsumer.accept(this.array[i]);
            }
        }
        
        @Override
        public String toString() {
            return String.format("DoubleArrayNode[%d][%s]", this.array.length - this.curSize, Arrays.toString(this.array));
        }
    }
    
    private static final class DoubleFixedNodeBuilder extends DoubleArrayNode implements Node.Builder.OfDouble
    {
        DoubleFixedNodeBuilder(final long n) {
            super(n);
            assert n < 2147483639L;
        }
        
        @Override
        public Node.OfDouble build() {
            if (this.curSize < this.array.length) {
                throw new IllegalStateException(String.format("Current size %d is less than fixed size %d", this.curSize, this.array.length));
            }
            return this;
        }
        
        @Override
        public void begin(final long n) {
            if (n != this.array.length) {
                throw new IllegalStateException(String.format("Begin size %d is not equal to fixed size %d", n, this.array.length));
            }
            this.curSize = 0;
        }
        
        @Override
        public void accept(final double n) {
            if (this.curSize < this.array.length) {
                this.array[this.curSize++] = n;
                return;
            }
            throw new IllegalStateException(String.format("Accept exceeded fixed size of %d", this.array.length));
        }
        
        @Override
        public void end() {
            if (this.curSize < this.array.length) {
                throw new IllegalStateException(String.format("End size %d is less than fixed size %d", this.curSize, this.array.length));
            }
        }
        
        @Override
        public String toString() {
            return String.format("DoubleFixedNodeBuilder[%d][%s]", this.array.length - this.curSize, Arrays.toString(this.array));
        }
    }
    
    private static final class DoubleSpinedNodeBuilder extends SpinedBuffer.OfDouble implements Node.OfDouble, Node.Builder.OfDouble
    {
        private boolean building;
        
        DoubleSpinedNodeBuilder() {
            this.building = false;
        }
        
        @Override
        public Spliterator.OfDouble spliterator() {
            assert !this.building : "during building";
            return super.spliterator();
        }
        
        @Override
        public void forEach(final DoubleConsumer doubleConsumer) {
            assert !this.building : "during building";
            super.forEach(doubleConsumer);
        }
        
        @Override
        public void begin(final long n) {
            assert !this.building : "was already building";
            this.building = true;
            this.clear();
            this.ensureCapacity(n);
        }
        
        @Override
        public void accept(final double n) {
            assert this.building : "not building";
            super.accept(n);
        }
        
        @Override
        public void end() {
            assert this.building : "was not building";
            this.building = false;
        }
        
        @Override
        public void copyInto(final double[] array, final int n) {
            assert !this.building : "during building";
            super.copyInto(array, n);
        }
        
        @Override
        public double[] asPrimitiveArray() {
            assert !this.building : "during building";
            return super.asPrimitiveArray();
        }
        
        @Override
        public Node.OfDouble build() {
            assert !this.building : "during building";
            return this;
        }
    }
    
    private abstract static class EmptyNode<T, T_ARR, T_CONS> implements Node<T>
    {
        EmptyNode() {
        }
        
        @Override
        public T[] asArray(final IntFunction<T[]> intFunction) {
            return intFunction.apply(0);
        }
        
        public void copyInto(final T_ARR t_ARR, final int n) {
        }
        
        @Override
        public long count() {
            return 0L;
        }
        
        public void forEach(final T_CONS t_CONS) {
        }
        
        private static final class OfDouble extends EmptyNode<Double, double[], DoubleConsumer> implements Node.OfDouble
        {
            OfDouble() {
            }
            
            @Override
            public Spliterator.OfDouble spliterator() {
                return Spliterators.emptyDoubleSpliterator();
            }
            
            @Override
            public double[] asPrimitiveArray() {
                return Nodes.EMPTY_DOUBLE_ARRAY;
            }
        }
        
        private static final class OfInt extends EmptyNode<Integer, int[], IntConsumer> implements Node.OfInt
        {
            OfInt() {
            }
            
            @Override
            public Spliterator.OfInt spliterator() {
                return Spliterators.emptyIntSpliterator();
            }
            
            @Override
            public int[] asPrimitiveArray() {
                return Nodes.EMPTY_INT_ARRAY;
            }
        }
        
        private static final class OfLong extends EmptyNode<Long, long[], LongConsumer> implements Node.OfLong
        {
            OfLong() {
            }
            
            @Override
            public Spliterator.OfLong spliterator() {
                return Spliterators.emptyLongSpliterator();
            }
            
            @Override
            public long[] asPrimitiveArray() {
                return Nodes.EMPTY_LONG_ARRAY;
            }
        }
        
        private static class OfRef<T> extends EmptyNode<T, T[], Consumer<? super T>>
        {
            @Override
            public Spliterator<T> spliterator() {
                return Spliterators.emptySpliterator();
            }
        }
    }
    
    private static final class FixedNodeBuilder<T> extends ArrayNode<T> implements Node.Builder<T>
    {
        FixedNodeBuilder(final long n, final IntFunction<T[]> intFunction) {
            super(n, intFunction);
            assert n < 2147483639L;
        }
        
        @Override
        public Node<T> build() {
            if (this.curSize < this.array.length) {
                throw new IllegalStateException(String.format("Current size %d is less than fixed size %d", this.curSize, this.array.length));
            }
            return this;
        }
        
        @Override
        public void begin(final long n) {
            if (n != this.array.length) {
                throw new IllegalStateException(String.format("Begin size %d is not equal to fixed size %d", n, this.array.length));
            }
            this.curSize = 0;
        }
        
        @Override
        public void accept(final T t) {
            if (this.curSize < this.array.length) {
                this.array[this.curSize++] = t;
                return;
            }
            throw new IllegalStateException(String.format("Accept exceeded fixed size of %d", this.array.length));
        }
        
        @Override
        public void end() {
            if (this.curSize < this.array.length) {
                throw new IllegalStateException(String.format("End size %d is less than fixed size %d", this.curSize, this.array.length));
            }
        }
        
        @Override
        public String toString() {
            return String.format("FixedNodeBuilder[%d][%s]", this.array.length - this.curSize, Arrays.toString(this.array));
        }
    }
    
    private static class IntArrayNode implements Node.OfInt
    {
        final int[] array;
        int curSize;
        
        IntArrayNode(final long n) {
            if (n >= 2147483639L) {
                throw new IllegalArgumentException("Stream size exceeds max array size");
            }
            this.array = new int[(int)n];
            this.curSize = 0;
        }
        
        IntArrayNode(final int[] array) {
            this.array = array;
            this.curSize = array.length;
        }
        
        @Override
        public Spliterator.OfInt spliterator() {
            return Arrays.spliterator(this.array, 0, this.curSize);
        }
        
        @Override
        public int[] asPrimitiveArray() {
            if (this.array.length == this.curSize) {
                return this.array;
            }
            return Arrays.copyOf(this.array, this.curSize);
        }
        
        @Override
        public void copyInto(final int[] array, final int n) {
            System.arraycopy(this.array, 0, array, n, this.curSize);
        }
        
        @Override
        public long count() {
            return this.curSize;
        }
        
        @Override
        public void forEach(final IntConsumer intConsumer) {
            for (int i = 0; i < this.curSize; ++i) {
                intConsumer.accept(this.array[i]);
            }
        }
        
        @Override
        public String toString() {
            return String.format("IntArrayNode[%d][%s]", this.array.length - this.curSize, Arrays.toString(this.array));
        }
    }
    
    private static final class IntFixedNodeBuilder extends IntArrayNode implements Node.Builder.OfInt
    {
        IntFixedNodeBuilder(final long n) {
            super(n);
            assert n < 2147483639L;
        }
        
        @Override
        public Node.OfInt build() {
            if (this.curSize < this.array.length) {
                throw new IllegalStateException(String.format("Current size %d is less than fixed size %d", this.curSize, this.array.length));
            }
            return this;
        }
        
        @Override
        public void begin(final long n) {
            if (n != this.array.length) {
                throw new IllegalStateException(String.format("Begin size %d is not equal to fixed size %d", n, this.array.length));
            }
            this.curSize = 0;
        }
        
        @Override
        public void accept(final int n) {
            if (this.curSize < this.array.length) {
                this.array[this.curSize++] = n;
                return;
            }
            throw new IllegalStateException(String.format("Accept exceeded fixed size of %d", this.array.length));
        }
        
        @Override
        public void end() {
            if (this.curSize < this.array.length) {
                throw new IllegalStateException(String.format("End size %d is less than fixed size %d", this.curSize, this.array.length));
            }
        }
        
        @Override
        public String toString() {
            return String.format("IntFixedNodeBuilder[%d][%s]", this.array.length - this.curSize, Arrays.toString(this.array));
        }
    }
    
    private static final class IntSpinedNodeBuilder extends SpinedBuffer.OfInt implements Node.OfInt, Node.Builder.OfInt
    {
        private boolean building;
        
        IntSpinedNodeBuilder() {
            this.building = false;
        }
        
        @Override
        public Spliterator.OfInt spliterator() {
            assert !this.building : "during building";
            return super.spliterator();
        }
        
        @Override
        public void forEach(final IntConsumer intConsumer) {
            assert !this.building : "during building";
            super.forEach(intConsumer);
        }
        
        @Override
        public void begin(final long n) {
            assert !this.building : "was already building";
            this.building = true;
            this.clear();
            this.ensureCapacity(n);
        }
        
        @Override
        public void accept(final int n) {
            assert this.building : "not building";
            super.accept(n);
        }
        
        @Override
        public void end() {
            assert this.building : "was not building";
            this.building = false;
        }
        
        @Override
        public void copyInto(final int[] array, final int n) throws IndexOutOfBoundsException {
            assert !this.building : "during building";
            super.copyInto(array, n);
        }
        
        @Override
        public int[] asPrimitiveArray() {
            assert !this.building : "during building";
            return super.asPrimitiveArray();
        }
        
        @Override
        public Node.OfInt build() {
            assert !this.building : "during building";
            return this;
        }
    }
    
    private static class LongArrayNode implements Node.OfLong
    {
        final long[] array;
        int curSize;
        
        LongArrayNode(final long n) {
            if (n >= 2147483639L) {
                throw new IllegalArgumentException("Stream size exceeds max array size");
            }
            this.array = new long[(int)n];
            this.curSize = 0;
        }
        
        LongArrayNode(final long[] array) {
            this.array = array;
            this.curSize = array.length;
        }
        
        @Override
        public Spliterator.OfLong spliterator() {
            return Arrays.spliterator(this.array, 0, this.curSize);
        }
        
        @Override
        public long[] asPrimitiveArray() {
            if (this.array.length == this.curSize) {
                return this.array;
            }
            return Arrays.copyOf(this.array, this.curSize);
        }
        
        @Override
        public void copyInto(final long[] array, final int n) {
            System.arraycopy(this.array, 0, array, n, this.curSize);
        }
        
        @Override
        public long count() {
            return this.curSize;
        }
        
        @Override
        public void forEach(final LongConsumer longConsumer) {
            for (int i = 0; i < this.curSize; ++i) {
                longConsumer.accept(this.array[i]);
            }
        }
        
        @Override
        public String toString() {
            return String.format("LongArrayNode[%d][%s]", this.array.length - this.curSize, Arrays.toString(this.array));
        }
    }
    
    private static final class LongFixedNodeBuilder extends LongArrayNode implements Node.Builder.OfLong
    {
        LongFixedNodeBuilder(final long n) {
            super(n);
            assert n < 2147483639L;
        }
        
        @Override
        public Node.OfLong build() {
            if (this.curSize < this.array.length) {
                throw new IllegalStateException(String.format("Current size %d is less than fixed size %d", this.curSize, this.array.length));
            }
            return this;
        }
        
        @Override
        public void begin(final long n) {
            if (n != this.array.length) {
                throw new IllegalStateException(String.format("Begin size %d is not equal to fixed size %d", n, this.array.length));
            }
            this.curSize = 0;
        }
        
        @Override
        public void accept(final long n) {
            if (this.curSize < this.array.length) {
                this.array[this.curSize++] = n;
                return;
            }
            throw new IllegalStateException(String.format("Accept exceeded fixed size of %d", this.array.length));
        }
        
        @Override
        public void end() {
            if (this.curSize < this.array.length) {
                throw new IllegalStateException(String.format("End size %d is less than fixed size %d", this.curSize, this.array.length));
            }
        }
        
        @Override
        public String toString() {
            return String.format("LongFixedNodeBuilder[%d][%s]", this.array.length - this.curSize, Arrays.toString(this.array));
        }
    }
    
    private static final class LongSpinedNodeBuilder extends SpinedBuffer.OfLong implements Node.OfLong, Node.Builder.OfLong
    {
        private boolean building;
        
        LongSpinedNodeBuilder() {
            this.building = false;
        }
        
        @Override
        public Spliterator.OfLong spliterator() {
            assert !this.building : "during building";
            return super.spliterator();
        }
        
        @Override
        public void forEach(final LongConsumer longConsumer) {
            assert !this.building : "during building";
            super.forEach(longConsumer);
        }
        
        @Override
        public void begin(final long n) {
            assert !this.building : "was already building";
            this.building = true;
            this.clear();
            this.ensureCapacity(n);
        }
        
        @Override
        public void accept(final long n) {
            assert this.building : "not building";
            super.accept(n);
        }
        
        @Override
        public void end() {
            assert this.building : "was not building";
            this.building = false;
        }
        
        @Override
        public void copyInto(final long[] array, final int n) {
            assert !this.building : "during building";
            super.copyInto(array, n);
        }
        
        @Override
        public long[] asPrimitiveArray() {
            assert !this.building : "during building";
            return super.asPrimitiveArray();
        }
        
        @Override
        public Node.OfLong build() {
            assert !this.building : "during building";
            return this;
        }
    }
    
    private abstract static class SizedCollectorTask<P_IN, P_OUT, T_SINK extends Sink<P_OUT>, K extends SizedCollectorTask<P_IN, P_OUT, T_SINK, K>> extends CountedCompleter<Void> implements Sink<P_OUT>
    {
        protected final Spliterator<P_IN> spliterator;
        protected final PipelineHelper<P_OUT> helper;
        protected final long targetSize;
        protected long offset;
        protected long length;
        protected int index;
        protected int fence;
        
        SizedCollectorTask(final Spliterator<P_IN> spliterator, final PipelineHelper<P_OUT> helper, final int n) {
            assert spliterator.hasCharacteristics(16384);
            this.spliterator = spliterator;
            this.helper = helper;
            this.targetSize = AbstractTask.suggestTargetSize(spliterator.estimateSize());
            this.offset = 0L;
            this.length = n;
        }
        
        SizedCollectorTask(final K k, final Spliterator<P_IN> spliterator, final long offset, final long length, final int n) {
            super(k);
            assert spliterator.hasCharacteristics(16384);
            this.spliterator = spliterator;
            this.helper = k.helper;
            this.targetSize = k.targetSize;
            this.offset = offset;
            this.length = length;
            if (offset < 0L || length < 0L || offset + length - 1L >= n) {
                throw new IllegalArgumentException(String.format("offset and length interval [%d, %d + %d) is not within array size interval [0, %d)", offset, offset, length, n));
            }
        }
        
        @Override
        public void compute() {
            SizedCollectorTask<P_IN, P_OUT, T_SINK, SizedCollectorTask<P_IN, P_OUT, T_SINK, SizedCollectorTask<P_IN, P_OUT, T_SINK, SizedCollectorTask>>> child;
            Spliterator<P_IN> spliterator;
            Spliterator<P_IN> trySplit;
            long estimateSize;
            for (child = (SizedCollectorTask<P_IN, P_OUT, T_SINK, SizedCollectorTask<P_IN, P_OUT, T_SINK, SizedCollectorTask<P_IN, P_OUT, T_SINK, SizedCollectorTask>>>)this, spliterator = this.spliterator; spliterator.estimateSize() > child.targetSize && (trySplit = spliterator.trySplit()) != null; child = (SizedCollectorTask<P_IN, P_OUT, T_SINK, SizedCollectorTask<P_IN, P_OUT, T_SINK, SizedCollectorTask<P_IN, P_OUT, T_SINK, SizedCollectorTask>>>)child.makeChild(spliterator, child.offset + estimateSize, child.length - estimateSize)) {
                child.setPendingCount(1);
                estimateSize = trySplit.estimateSize();
                child.makeChild(trySplit, child.offset, estimateSize).fork();
            }
            assert child.offset + child.length < 2147483639L;
            child.helper.wrapAndCopyInto((SizedCollectorTask<P_IN, P_OUT, T_SINK, SizedCollectorTask<P_IN, P_OUT, T_SINK, SizedCollectorTask<P_IN, P_OUT, T_SINK, SizedCollectorTask<P_IN, P_OUT, T_SINK, SizedCollectorTask<P_IN, P_OUT, T_SINK, SizedCollectorTask>>>>>)child, spliterator);
            child.propagateCompletion();
        }
        
        abstract K makeChild(final Spliterator<P_IN> p0, final long p1, final long p2);
        
        @Override
        public void begin(final long n) {
            if (n > this.length) {
                throw new IllegalStateException("size passed to Sink.begin exceeds array length");
            }
            this.index = (int)this.offset;
            this.fence = this.index + (int)this.length;
        }
        
        static final class OfDouble<P_IN> extends SizedCollectorTask<P_IN, Double, Sink.OfDouble, OfDouble<P_IN>> implements Sink.OfDouble
        {
            private final double[] array;
            
            OfDouble(final Spliterator<P_IN> spliterator, final PipelineHelper<Double> pipelineHelper, final double[] array) {
                super(spliterator, (PipelineHelper<Object>)pipelineHelper, array.length);
                this.array = array;
            }
            
            OfDouble(final OfDouble<P_IN> ofDouble, final Spliterator<P_IN> spliterator, final long n, final long n2) {
                super(ofDouble, spliterator, n, n2, ofDouble.array.length);
                this.array = ofDouble.array;
            }
            
            @Override
            OfDouble<P_IN> makeChild(final Spliterator<P_IN> spliterator, final long n, final long n2) {
                return new OfDouble<P_IN>(this, spliterator, n, n2);
            }
            
            @Override
            public void accept(final double n) {
                if (this.index >= this.fence) {
                    throw new IndexOutOfBoundsException(Integer.toString(this.index));
                }
                this.array[this.index++] = n;
            }
        }
        
        static final class OfInt<P_IN> extends SizedCollectorTask<P_IN, Integer, Sink.OfInt, OfInt<P_IN>> implements Sink.OfInt
        {
            private final int[] array;
            
            OfInt(final Spliterator<P_IN> spliterator, final PipelineHelper<Integer> pipelineHelper, final int[] array) {
                super(spliterator, (PipelineHelper<Object>)pipelineHelper, array.length);
                this.array = array;
            }
            
            OfInt(final OfInt<P_IN> ofInt, final Spliterator<P_IN> spliterator, final long n, final long n2) {
                super(ofInt, spliterator, n, n2, ofInt.array.length);
                this.array = ofInt.array;
            }
            
            @Override
            OfInt<P_IN> makeChild(final Spliterator<P_IN> spliterator, final long n, final long n2) {
                return new OfInt<P_IN>(this, spliterator, n, n2);
            }
            
            @Override
            public void accept(final int n) {
                if (this.index >= this.fence) {
                    throw new IndexOutOfBoundsException(Integer.toString(this.index));
                }
                this.array[this.index++] = n;
            }
        }
        
        static final class OfLong<P_IN> extends SizedCollectorTask<P_IN, Long, Sink.OfLong, OfLong<P_IN>> implements Sink.OfLong
        {
            private final long[] array;
            
            OfLong(final Spliterator<P_IN> spliterator, final PipelineHelper<Long> pipelineHelper, final long[] array) {
                super(spliterator, (PipelineHelper<Object>)pipelineHelper, array.length);
                this.array = array;
            }
            
            OfLong(final OfLong<P_IN> ofLong, final Spliterator<P_IN> spliterator, final long n, final long n2) {
                super(ofLong, spliterator, n, n2, ofLong.array.length);
                this.array = ofLong.array;
            }
            
            @Override
            OfLong<P_IN> makeChild(final Spliterator<P_IN> spliterator, final long n, final long n2) {
                return new OfLong<P_IN>(this, spliterator, n, n2);
            }
            
            @Override
            public void accept(final long n) {
                if (this.index >= this.fence) {
                    throw new IndexOutOfBoundsException(Integer.toString(this.index));
                }
                this.array[this.index++] = n;
            }
        }
        
        static final class OfRef<P_IN, P_OUT> extends SizedCollectorTask<P_IN, P_OUT, Sink<P_OUT>, OfRef<P_IN, P_OUT>> implements Sink<P_OUT>
        {
            private final P_OUT[] array;
            
            OfRef(final Spliterator<P_IN> spliterator, final PipelineHelper<P_OUT> pipelineHelper, final P_OUT[] array) {
                super(spliterator, pipelineHelper, array.length);
                this.array = array;
            }
            
            OfRef(final OfRef<P_IN, P_OUT> ofRef, final Spliterator<P_IN> spliterator, final long n, final long n2) {
                super(ofRef, spliterator, n, n2, ofRef.array.length);
                this.array = ofRef.array;
            }
            
            @Override
            OfRef<P_IN, P_OUT> makeChild(final Spliterator<P_IN> spliterator, final long n, final long n2) {
                return new OfRef<P_IN, P_OUT>(this, spliterator, n, n2);
            }
            
            @Override
            public void accept(final P_OUT p_OUT) {
                if (this.index >= this.fence) {
                    throw new IndexOutOfBoundsException(Integer.toString(this.index));
                }
                this.array[this.index++] = p_OUT;
            }
        }
    }
    
    private static final class SpinedNodeBuilder<T> extends SpinedBuffer<T> implements Node<T>, Builder<T>
    {
        private boolean building;
        
        SpinedNodeBuilder() {
            this.building = false;
        }
        
        @Override
        public Spliterator<T> spliterator() {
            assert !this.building : "during building";
            return super.spliterator();
        }
        
        @Override
        public void forEach(final Consumer<? super T> consumer) {
            assert !this.building : "during building";
            super.forEach(consumer);
        }
        
        @Override
        public void begin(final long n) {
            assert !this.building : "was already building";
            this.building = true;
            this.clear();
            this.ensureCapacity(n);
        }
        
        @Override
        public void accept(final T t) {
            assert this.building : "not building";
            super.accept(t);
        }
        
        @Override
        public void end() {
            assert this.building : "was not building";
            this.building = false;
        }
        
        @Override
        public void copyInto(final T[] array, final int n) {
            assert !this.building : "during building";
            super.copyInto(array, n);
        }
        
        @Override
        public T[] asArray(final IntFunction<T[]> intFunction) {
            assert !this.building : "during building";
            return super.asArray(intFunction);
        }
        
        @Override
        public Node<T> build() {
            assert !this.building : "during building";
            return this;
        }
    }
    
    private abstract static class ToArrayTask<T, T_NODE extends Node<T>, K extends ToArrayTask<T, T_NODE, K>> extends CountedCompleter<Void>
    {
        protected final T_NODE node;
        protected final int offset;
        
        ToArrayTask(final T_NODE node, final int offset) {
            this.node = node;
            this.offset = offset;
        }
        
        ToArrayTask(final K k, final T_NODE node, final int offset) {
            super(k);
            this.node = node;
            this.offset = offset;
        }
        
        abstract void copyNodeToArray();
        
        abstract K makeChild(final int p0, final int p1);
        
        @Override
        public void compute() {
            ToArrayTask<T, T_NODE, ToArrayTask> child;
            int n;
            int i;
            for (child = (ToArrayTask<T, T_NODE, ToArrayTask>)this; child.node.getChildCount() != 0; child = child.makeChild(i, child.offset + n)) {
                child.setPendingCount(child.node.getChildCount() - 1);
                n = 0;
                for (i = 0; i < child.node.getChildCount() - 1; ++i) {
                    final ToArrayTask child2 = child.makeChild(i, child.offset + n);
                    n += (int)child2.node.count();
                    child2.fork();
                }
            }
            child.copyNodeToArray();
            child.propagateCompletion();
        }
        
        private static final class OfDouble extends OfPrimitive<Double, DoubleConsumer, double[], Spliterator.OfDouble, Node.OfDouble>
        {
            private OfDouble(final Node.OfDouble ofDouble, final double[] array, final int n) {
                super((Node.OfPrimitive)ofDouble, (Object)array, n);
            }
        }
        
        private static class OfPrimitive<T, T_CONS, T_ARR, T_SPLITR extends Spliterator.OfPrimitive<T, T_CONS, T_SPLITR>, T_NODE extends Node.OfPrimitive<T, T_CONS, T_ARR, T_SPLITR, T_NODE>> extends ToArrayTask<T, T_NODE, OfPrimitive<T, T_CONS, T_ARR, T_SPLITR, T_NODE>>
        {
            private final T_ARR array;
            
            private OfPrimitive(final T_NODE t_NODE, final T_ARR array, final int n) {
                super(t_NODE, n);
                this.array = array;
            }
            
            private OfPrimitive(final OfPrimitive<T, T_CONS, T_ARR, T_SPLITR, T_NODE> ofPrimitive, final T_NODE t_NODE, final int n) {
                super(ofPrimitive, t_NODE, n);
                this.array = ofPrimitive.array;
            }
            
            @Override
            OfPrimitive<T, T_CONS, T_ARR, T_SPLITR, T_NODE> makeChild(final int n, final int n2) {
                return new OfPrimitive<T, T_CONS, T_ARR, T_SPLITR, T_NODE>(this, ((Node.OfPrimitive)this.node).getChild(n), n2);
            }
            
            @Override
            void copyNodeToArray() {
                ((Node.OfPrimitive)this.node).copyInto(this.array, this.offset);
            }
        }
        
        private static final class OfInt extends OfPrimitive<Integer, IntConsumer, int[], Spliterator.OfInt, Node.OfInt>
        {
            private OfInt(final Node.OfInt ofInt, final int[] array, final int n) {
                super((Node.OfPrimitive)ofInt, (Object)array, n);
            }
        }
        
        private static final class OfLong extends OfPrimitive<Long, LongConsumer, long[], Spliterator.OfLong, Node.OfLong>
        {
            private OfLong(final Node.OfLong ofLong, final long[] array, final int n) {
                super((Node.OfPrimitive)ofLong, (Object)array, n);
            }
        }
        
        private static final class OfRef<T> extends ToArrayTask<T, Node<T>, OfRef<T>>
        {
            private final T[] array;
            
            private OfRef(final Node<T> node, final T[] array, final int n) {
                super(node, n);
                this.array = array;
            }
            
            private OfRef(final OfRef<T> ofRef, final Node<T> node, final int n) {
                super(ofRef, node, n);
                this.array = ofRef.array;
            }
            
            @Override
            OfRef<T> makeChild(final int n, final int n2) {
                return new OfRef<T>(this, (Node<T>)this.node.getChild(n), n2);
            }
            
            @Override
            void copyNodeToArray() {
                this.node.copyInto((T[])this.array, this.offset);
            }
        }
    }
}
