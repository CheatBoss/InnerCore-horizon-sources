package java.util;

import java.util.function.*;

public class LongSummaryStatistics implements LongConsumer, IntConsumer
{
    private long count;
    private long sum;
    private long min;
    private long max;
    
    public LongSummaryStatistics() {
        this.min = Long.MAX_VALUE;
        this.max = Long.MIN_VALUE;
    }
    
    @Override
    public void accept(final int n) {
        this.accept((long)n);
    }
    
    @Override
    public void accept(final long n) {
        ++this.count;
        this.sum += n;
        this.min = Math.min(this.min, n);
        this.max = Math.max(this.max, n);
    }
    
    public void combine(final LongSummaryStatistics longSummaryStatistics) {
        this.count += longSummaryStatistics.count;
        this.sum += longSummaryStatistics.sum;
        this.min = Math.min(this.min, longSummaryStatistics.min);
        this.max = Math.max(this.max, longSummaryStatistics.max);
    }
    
    public final long getCount() {
        return this.count;
    }
    
    public final long getSum() {
        return this.sum;
    }
    
    public final long getMin() {
        return this.min;
    }
    
    public final long getMax() {
        return this.max;
    }
    
    public final double getAverage() {
        return (this.getCount() > 0L) ? (this.getSum() / (double)this.getCount()) : 0.0;
    }
    
    @Override
    public String toString() {
        return String.format("%s{count=%d, sum=%d, min=%d, average=%f, max=%d}", this.getClass().getSimpleName(), this.getCount(), this.getSum(), this.getMin(), this.getAverage(), this.getMax());
    }
}
