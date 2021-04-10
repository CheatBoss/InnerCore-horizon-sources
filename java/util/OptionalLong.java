package java.util;

import java.util.function.*;

public final class OptionalLong
{
    private static final OptionalLong EMPTY;
    private final boolean isPresent;
    private final long value;
    
    private OptionalLong() {
        this.isPresent = false;
        this.value = 0L;
    }
    
    public static OptionalLong empty() {
        return OptionalLong.EMPTY;
    }
    
    private OptionalLong(final long value) {
        this.isPresent = true;
        this.value = value;
    }
    
    public static OptionalLong of(final long n) {
        return new OptionalLong(n);
    }
    
    public long getAsLong() {
        if (!this.isPresent) {
            throw new NoSuchElementException("No value present");
        }
        return this.value;
    }
    
    public boolean isPresent() {
        return this.isPresent;
    }
    
    public void ifPresent(final LongConsumer longConsumer) {
        if (this.isPresent) {
            longConsumer.accept(this.value);
        }
    }
    
    public long orElse(final long n) {
        return this.isPresent ? this.value : n;
    }
    
    public long orElseGet(final LongSupplier longSupplier) {
        return this.isPresent ? this.value : longSupplier.getAsLong();
    }
    
    public <X extends Throwable> long orElseThrow(final Supplier<X> supplier) throws X, Throwable {
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
        if (!(o instanceof OptionalLong)) {
            return false;
        }
        final OptionalLong optionalLong = (OptionalLong)o;
        return (this.isPresent && optionalLong.isPresent) ? (this.value == optionalLong.value) : (this.isPresent == optionalLong.isPresent);
    }
    
    @Override
    public int hashCode() {
        return this.isPresent ? Long.hashCode(this.value) : 0;
    }
    
    @Override
    public String toString() {
        return this.isPresent ? String.format("OptionalLong[%s]", this.value) : "OptionalLong.empty";
    }
    
    static {
        EMPTY = new OptionalLong();
    }
}
