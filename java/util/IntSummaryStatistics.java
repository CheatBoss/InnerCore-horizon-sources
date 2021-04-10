package java.util;

import java.util.function.*;

public class IntSummaryStatistics implements IntConsumer
{
    private long count;
    private long sum;
    private int min;
    private int max;
    
    public IntSummaryStatistics() {
        this.min = Integer.MAX_VALUE;
        this.max = Integer.MIN_VALUE;
    }
    
    @Override
    public void accept(final int n) {
        ++this.count;
        this.sum += n;
        this.min = Math.min(this.min, n);
        this.max = Math.max(this.max, n);
    }
    
    public void combine(final IntSummaryStatistics intSummaryStatistics) {
        this.count += intSummaryStatistics.count;
        this.sum += intSummaryStatistics.sum;
        this.min = Math.min(this.min, intSummaryStatistics.min);
        this.max = Math.max(this.max, intSummaryStatistics.max);
    }
    
    public final long getCount() {
        return this.count;
    }
    
    public final long getSum() {
        return this.sum;
    }
    
    public final int getMin() {
        return this.min;
    }
    
    public final int getMax() {
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
