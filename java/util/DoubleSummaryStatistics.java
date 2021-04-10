package java.util;

import java.util.function.*;

public class DoubleSummaryStatistics implements DoubleConsumer
{
    private long count;
    private double sum;
    private double sumCompensation;
    private double simpleSum;
    private double min;
    private double max;
    
    public DoubleSummaryStatistics() {
        this.min = Double.POSITIVE_INFINITY;
        this.max = Double.NEGATIVE_INFINITY;
    }
    
    @Override
    public void accept(final double n) {
        ++this.count;
        this.simpleSum += n;
        this.sumWithCompensation(n);
        this.min = Math.min(this.min, n);
        this.max = Math.max(this.max, n);
    }
    
    public void combine(final DoubleSummaryStatistics doubleSummaryStatistics) {
        this.count += doubleSummaryStatistics.count;
        this.simpleSum += doubleSummaryStatistics.simpleSum;
        this.sumWithCompensation(doubleSummaryStatistics.sum);
        this.sumWithCompensation(doubleSummaryStatistics.sumCompensation);
        this.min = Math.min(this.min, doubleSummaryStatistics.min);
        this.max = Math.max(this.max, doubleSummaryStatistics.max);
    }
    
    private void sumWithCompensation(final double n) {
        final double n2 = n - this.sumCompensation;
        final double sum = this.sum + n2;
        this.sumCompensation = sum - this.sum - n2;
        this.sum = sum;
    }
    
    public final long getCount() {
        return this.count;
    }
    
    public final double getSum() {
        final double n = this.sum + this.sumCompensation;
        if (Double.isNaN(n) && Double.isInfinite(this.simpleSum)) {
            return this.simpleSum;
        }
        return n;
    }
    
    public final double getMin() {
        return this.min;
    }
    
    public final double getMax() {
        return this.max;
    }
    
    public final double getAverage() {
        return (this.getCount() > 0L) ? (this.getSum() / this.getCount()) : 0.0;
    }
    
    @Override
    public String toString() {
        return String.format("%s{count=%d, sum=%f, min=%f, average=%f, max=%f}", this.getClass().getSimpleName(), this.getCount(), this.getSum(), this.getMin(), this.getAverage(), this.getMax());
    }
}
