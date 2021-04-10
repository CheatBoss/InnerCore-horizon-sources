package java.util.stream;

import java.util.*;
import java.util.function.*;

interface Sink<T> extends Consumer<T>
{
    default void begin(final long n) {
    }
    
    default void end() {
    }
    
    default boolean cancellationRequested() {
        return false;
    }
    
    default void accept(final int n) {
        throw new IllegalStateException("called wrong accept method");
    }
    
    default void accept(final long n) {
        throw new IllegalStateException("called wrong accept method");
    }
    
    default void accept(final double n) {
        throw new IllegalStateException("called wrong accept method");
    }
    
    public abstract static class ChainedDouble<E_OUT> implements OfDouble
    {
        protected final Sink<? super E_OUT> downstream;
        
        public ChainedDouble(final Sink<? super E_OUT> sink) {
            this.downstream = Objects.requireNonNull(sink);
        }
        
        @Override
        public void begin(final long n) {
            this.downstream.begin(n);
        }
        
        @Override
        public void end() {
            this.downstream.end();
        }
        
        @Override
        public boolean cancellationRequested() {
            return this.downstream.cancellationRequested();
        }
    }
    
    public interface OfDouble extends Sink<Double>, DoubleConsumer
    {
        void accept(final double p0);
        
        default void accept(final Double n) {
            if (Tripwire.ENABLED) {
                Tripwire.trip(this.getClass(), "{0} calling Sink.OfDouble.accept(Double)");
            }
            this.accept((double)n);
        }
    }
    
    public abstract static class ChainedInt<E_OUT> implements OfInt
    {
        protected final Sink<? super E_OUT> downstream;
        
        public ChainedInt(final Sink<? super E_OUT> sink) {
            this.downstream = Objects.requireNonNull(sink);
        }
        
        @Override
        public void begin(final long n) {
            this.downstream.begin(n);
        }
        
        @Override
        public void end() {
            this.downstream.end();
        }
        
        @Override
        public boolean cancellationRequested() {
            return this.downstream.cancellationRequested();
        }
    }
    
    public interface OfInt extends Sink<Integer>, IntConsumer
    {
        void accept(final int p0);
        
        default void accept(final Integer n) {
            if (Tripwire.ENABLED) {
                Tripwire.trip(this.getClass(), "{0} calling Sink.OfInt.accept(Integer)");
            }
            this.accept((int)n);
        }
    }
    
    public abstract static class ChainedLong<E_OUT> implements OfLong
    {
        protected final Sink<? super E_OUT> downstream;
        
        public ChainedLong(final Sink<? super E_OUT> sink) {
            this.downstream = Objects.requireNonNull(sink);
        }
        
        @Override
        public void begin(final long n) {
            this.downstream.begin(n);
        }
        
        @Override
        public void end() {
            this.downstream.end();
        }
        
        @Override
        public boolean cancellationRequested() {
            return this.downstream.cancellationRequested();
        }
    }
    
    public interface OfLong extends Sink<Long>, LongConsumer
    {
        void accept(final long p0);
        
        default void accept(final Long n) {
            if (Tripwire.ENABLED) {
                Tripwire.trip(this.getClass(), "{0} calling Sink.OfLong.accept(Long)");
            }
            this.accept((long)n);
        }
    }
    
    public abstract static class ChainedReference<T, E_OUT> implements Sink<T>
    {
        protected final Sink<? super E_OUT> downstream;
        
        public ChainedReference(final Sink<? super E_OUT> sink) {
            this.downstream = Objects.requireNonNull(sink);
        }
        
        @Override
        public void begin(final long n) {
            this.downstream.begin(n);
        }
        
        @Override
        public void end() {
            this.downstream.end();
        }
        
        @Override
        public boolean cancellationRequested() {
            return this.downstream.cancellationRequested();
        }
    }
}
