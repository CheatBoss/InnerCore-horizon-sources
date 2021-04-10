package java.util.stream;

import java.util.function.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.*;
import java.io.*;
import java.util.*;

final class DistinctOps
{
    private DistinctOps() {
    }
    
    static <T> ReferencePipeline<T, T> makeRef(final AbstractPipeline<?, T, ?> abstractPipeline) {
        return new ReferencePipeline.StatefulOp<T, T>(abstractPipeline, StreamShape.REFERENCE, StreamOpFlag.IS_DISTINCT | StreamOpFlag.NOT_SIZED) {
             <P_IN> Node<T> reduce(final PipelineHelper<T> pipelineHelper, final Spliterator<P_IN> spliterator) {
                return Nodes.node(ReduceOps.makeRef(LinkedHashSet::new, HashSet::add, AbstractCollection::addAll).evaluateParallel(pipelineHelper, spliterator));
            }
            
            @Override
             <P_IN> Node<T> opEvaluateParallel(final PipelineHelper<T> pipelineHelper, final Spliterator<P_IN> spliterator, final IntFunction<T[]> intFunction) {
                if (StreamOpFlag.DISTINCT.isKnown(pipelineHelper.getStreamAndOpFlags())) {
                    return pipelineHelper.evaluate(spliterator, false, intFunction);
                }
                if (StreamOpFlag.ORDERED.isKnown(pipelineHelper.getStreamAndOpFlags())) {
                    return this.reduce(pipelineHelper, spliterator);
                }
                final AtomicBoolean atomicBoolean = new AtomicBoolean(false);
                final ConcurrentHashMap<Object, Object> concurrentHashMap = new ConcurrentHashMap<Object, Object>();
                final AtomicBoolean atomicBoolean2;
                final ConcurrentHashMap<Object, Boolean> concurrentHashMap2;
                ForEachOps.makeRef(o -> {
                    if (o == null) {
                        atomicBoolean2.set(true);
                    }
                    else {
                        concurrentHashMap2.putIfAbsent(o, Boolean.TRUE);
                    }
                    return;
                }, false).evaluateParallel((PipelineHelper<Object>)pipelineHelper, spliterator);
                Serializable keySet = concurrentHashMap.keySet();
                if (atomicBoolean.get()) {
                    keySet = new HashSet<Object>((Collection<?>)keySet);
                    ((Set<Object>)keySet).add(null);
                }
                return Nodes.node((Collection<T>)keySet);
            }
            
            @Override
             <P_IN> Spliterator<T> opEvaluateParallelLazy(final PipelineHelper<T> pipelineHelper, final Spliterator<P_IN> spliterator) {
                if (StreamOpFlag.DISTINCT.isKnown(pipelineHelper.getStreamAndOpFlags())) {
                    return pipelineHelper.wrapSpliterator(spliterator);
                }
                if (StreamOpFlag.ORDERED.isKnown(pipelineHelper.getStreamAndOpFlags())) {
                    return this.reduce(pipelineHelper, spliterator).spliterator();
                }
                return new StreamSpliterators.DistinctSpliterator<T>(pipelineHelper.wrapSpliterator(spliterator));
            }
            
            @Override
            Sink<T> opWrapSink(final int n, final Sink<T> sink) {
                Objects.requireNonNull(sink);
                if (StreamOpFlag.DISTINCT.isKnown(n)) {
                    return sink;
                }
                if (StreamOpFlag.SORTED.isKnown(n)) {
                    return new Sink.ChainedReference<T, T>(sink) {
                        boolean seenNull;
                        T lastSeen;
                        
                        @Override
                        public void begin(final long n) {
                            this.seenNull = false;
                            this.lastSeen = null;
                            this.downstream.begin(-1L);
                        }
                        
                        @Override
                        public void end() {
                            this.seenNull = false;
                            this.lastSeen = null;
                            this.downstream.end();
                        }
                        
                        @Override
                        public void accept(final T lastSeen) {
                            if (lastSeen == null) {
                                if (!this.seenNull) {
                                    this.seenNull = true;
                                    this.downstream.accept((Object)(this.lastSeen = null));
                                }
                            }
                            else if (this.lastSeen == null || !lastSeen.equals(this.lastSeen)) {
                                this.downstream.accept((Object)(this.lastSeen = lastSeen));
                            }
                        }
                    };
                }
                return new Sink.ChainedReference<T, T>(sink) {
                    Set<T> seen;
                    
                    @Override
                    public void begin(final long n) {
                        this.seen = new HashSet<T>();
                        this.downstream.begin(-1L);
                    }
                    
                    @Override
                    public void end() {
                        this.seen = null;
                        this.downstream.end();
                    }
                    
                    @Override
                    public void accept(final T t) {
                        if (!this.seen.contains(t)) {
                            this.seen.add(t);
                            this.downstream.accept((Object)t);
                        }
                    }
                };
            }
        };
    }
}
