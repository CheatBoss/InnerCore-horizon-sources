package java.util;

import java.util.function.*;

public final class OptionalDouble
{
    private static final OptionalDouble EMPTY;
    private final boolean isPresent;
    private final double value;
    
    private OptionalDouble() {
        this.isPresent = false;
        this.value = Double.NaN;
    }
    
    public static OptionalDouble empty() {
        return OptionalDouble.EMPTY;
    }
    
    private OptionalDouble(final double value) {
        this.isPresent = true;
        this.value = value;
    }
    
    public static OptionalDouble of(final double n) {
        return new OptionalDouble(n);
    }
    
    public double getAsDouble() {
        if (!this.isPresent) {
            throw new NoSuchElementException("No value present");
        }
        return this.value;
    }
    
    public boolean isPresent() {
        return this.isPresent;
    }
    
    public void ifPresent(final DoubleConsumer doubleConsumer) {
        if (this.isPresent) {
            doubleConsumer.accept(this.value);
        }
    }
    
    public double orElse(final double n) {
        return this.isPresent ? this.value : n;
    }
    
    public double orElseGet(final DoubleSupplier doubleSupplier) {
        return this.isPresent ? this.value : doubleSupplier.getAsDouble();
    }
    
    public <X extends Throwable> double orElseThrow(final Supplier<X> supplier) throws X, Throwable {
        if (this.isPresent) {
            return this.value;
        }
        throw supplier.get();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OptionalDouble)) {
            return false;
        }
        final OptionalDouble optionalDouble = (OptionalDouble)o;
        return (this.isPresent && optionalDouble.isPresent) ? (Double.compare(this.value, optionalDouble.value) == 0) : (this.isPresent == optionalDouble.isPresent);
    }
    
    @Override
    public int hashCode() {
        return this.isPresent ? Double.hashCode(this.value) : 0;
    }
    
    @Override
    public String toString() {
        return this.isPresent ? String.format("OptionalDouble[%s]", this.value) : "OptionalDouble.empty";
    }
    
    static {
        EMPTY = new OptionalDouble();
    }
}
