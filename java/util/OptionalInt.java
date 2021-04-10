package java.util;

import java.util.function.*;

public final class OptionalInt
{
    private static final OptionalInt EMPTY;
    private final boolean isPresent;
    private final int value;
    
    private OptionalInt() {
        this.isPresent = false;
        this.value = 0;
    }
    
    public static OptionalInt empty() {
        return OptionalInt.EMPTY;
    }
    
    private OptionalInt(final int value) {
        this.isPresent = true;
        this.value = value;
    }
    
    public static OptionalInt of(final int n) {
        return new OptionalInt(n);
    }
    
    public int getAsInt() {
        if (!this.isPresent) {
            throw new NoSuchElementException("No value present");
        }
        return this.value;
    }
    
    public boolean isPresent() {
        return this.isPresent;
    }
    
    public void ifPresent(final IntConsumer intConsumer) {
        if (this.isPresent) {
            intConsumer.accept(this.value);
        }
    }
    
    public int orElse(final int n) {
        return this.isPresent ? this.value : n;
    }
    
    public int orElseGet(final IntSupplier intSupplier) {
        return this.isPresent ? this.value : intSupplier.getAsInt();
    }
    
    public <X extends Throwable> int orElseThrow(final Supplier<X> supplier) throws X, Throwable {
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
        if (!(o instanceof OptionalInt)) {
            return false;
        }
        final OptionalInt optionalInt = (OptionalInt)o;
        return (this.isPresent && optionalInt.isPresent) ? (this.value == optionalInt.value) : (this.isPresent == optionalInt.isPresent);
    }
    
    @Override
    public int hashCode() {
        return this.isPresent ? Integer.hashCode(this.value) : 0;
    }
    
    @Override
    public String toString() {
        return this.isPresent ? String.format("OptionalInt[%s]", this.value) : "OptionalInt.empty";
    }
    
    static {
        EMPTY = new OptionalInt();
    }
}
