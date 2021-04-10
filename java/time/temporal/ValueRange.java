package java.time.temporal;

import java.time.*;
import java.io.*;

public final class ValueRange implements Serializable
{
    private static final long serialVersionUID = -7317881728594519368L;
    private final long minSmallest;
    private final long minLargest;
    private final long maxSmallest;
    private final long maxLargest;
    
    public static ValueRange of(final long n, final long n2) {
        if (n > n2) {
            throw new IllegalArgumentException("Minimum value must be less than maximum value");
        }
        return new ValueRange(n, n, n2, n2);
    }
    
    public static ValueRange of(final long n, final long n2, final long n3) {
        return of(n, n, n2, n3);
    }
    
    public static ValueRange of(final long n, final long n2, final long n3, final long n4) {
        if (n > n2) {
            throw new IllegalArgumentException("Smallest minimum value must be less than largest minimum value");
        }
        if (n3 > n4) {
            throw new IllegalArgumentException("Smallest maximum value must be less than largest maximum value");
        }
        if (n2 > n4) {
            throw new IllegalArgumentException("Minimum value must be less than maximum value");
        }
        return new ValueRange(n, n2, n3, n4);
    }
    
    private ValueRange(final long minSmallest, final long minLargest, final long maxSmallest, final long maxLargest) {
        this.minSmallest = minSmallest;
        this.minLargest = minLargest;
        this.maxSmallest = maxSmallest;
        this.maxLargest = maxLargest;
    }
    
    public boolean isFixed() {
        return this.minSmallest == this.minLargest && this.maxSmallest == this.maxLargest;
    }
    
    public long getMinimum() {
        return this.minSmallest;
    }
    
    public long getLargestMinimum() {
        return this.minLargest;
    }
    
    public long getSmallestMaximum() {
        return this.maxSmallest;
    }
    
    public long getMaximum() {
        return this.maxLargest;
    }
    
    public boolean isIntValue() {
        return this.getMinimum() >= -2147483648L && this.getMaximum() <= 2147483647L;
    }
    
    public boolean isValidValue(final long n) {
        return n >= this.getMinimum() && n <= this.getMaximum();
    }
    
    public boolean isValidIntValue(final long n) {
        return this.isIntValue() && this.isValidValue(n);
    }
    
    public long checkValidValue(final long n, final TemporalField temporalField) {
        if (!this.isValidValue(n)) {
            throw new DateTimeException(this.genInvalidFieldMessage(temporalField, n));
        }
        return n;
    }
    
    public int checkValidIntValue(final long n, final TemporalField temporalField) {
        if (!this.isValidIntValue(n)) {
            throw new DateTimeException(this.genInvalidFieldMessage(temporalField, n));
        }
        return (int)n;
    }
    
    private String genInvalidFieldMessage(final TemporalField temporalField, final long n) {
        if (temporalField != null) {
            return "Invalid value for " + temporalField + " (valid values " + this + "): " + n;
        }
        return "Invalid value (valid values " + this + "): " + n;
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException, InvalidObjectException {
        objectInputStream.defaultReadObject();
        if (this.minSmallest > this.minLargest) {
            throw new InvalidObjectException("Smallest minimum value must be less than largest minimum value");
        }
        if (this.maxSmallest > this.maxLargest) {
            throw new InvalidObjectException("Smallest maximum value must be less than largest maximum value");
        }
        if (this.minLargest > this.maxLargest) {
            throw new InvalidObjectException("Minimum value must be less than maximum value");
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ValueRange) {
            final ValueRange valueRange = (ValueRange)o;
            return this.minSmallest == valueRange.minSmallest && this.minLargest == valueRange.minLargest && this.maxSmallest == valueRange.maxSmallest && this.maxLargest == valueRange.maxLargest;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        final long n = this.minSmallest + (this.minLargest << 16) + (this.minLargest >> 48) + (this.maxSmallest << 32) + (this.maxSmallest >> 32) + (this.maxLargest << 48) + (this.maxLargest >> 16);
        return (int)(n ^ n >>> 32);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.minSmallest);
        if (this.minSmallest != this.minLargest) {
            sb.append('/').append(this.minLargest);
        }
        sb.append(" - ").append(this.maxSmallest);
        if (this.maxSmallest != this.maxLargest) {
            sb.append('/').append(this.maxLargest);
        }
        return sb.toString();
    }
}
