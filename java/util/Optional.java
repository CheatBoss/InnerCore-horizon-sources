package java.util;

import java.util.function.*;

public final class Optional<T>
{
    private static final Optional<?> EMPTY;
    private final T value;
    
    private Optional() {
        this.value = null;
    }
    
    public static <T> Optional<T> empty() {
        return (Optional<T>)Optional.EMPTY;
    }
    
    private Optional(final T t) {
        this.value = Objects.requireNonNull(t);
    }
    
    public static <T> Optional<T> of(final T t) {
        return new Optional<T>(t);
    }
    
    public static <T> Optional<T> ofNullable(final T t) {
        return (Optional<T>)((t == null) ? empty() : of((Object)t));
    }
    
    public T get() {
        if (this.value == null) {
            throw new NoSuchElementException("No value present");
        }
        return this.value;
    }
    
    public boolean isPresent() {
        return this.value != null;
    }
    
    public void ifPresent(final Consumer<? super T> consumer) {
        if (this.value != null) {
            consumer.accept(this.value);
        }
    }
    
    public Optional<T> filter(final Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        if (!this.isPresent()) {
            return this;
        }
        return predicate.test(this.value) ? this : empty();
    }
    
    public <U> Optional<U> map(final Function<? super T, ? extends U> function) {
        Objects.requireNonNull(function);
        if (!this.isPresent()) {
            return empty();
        }
        return ofNullable((U)function.apply(this.value));
    }
    
    public <U> Optional<U> flatMap(final Function<? super T, Optional<U>> function) {
        Objects.requireNonNull(function);
        if (!this.isPresent()) {
            return empty();
        }
        return Objects.requireNonNull(function.apply(this.value));
    }
    
    public T orElse(final T t) {
        return (this.value != null) ? this.value : t;
    }
    
    public T orElseGet(final Supplier<? extends T> supplier) {
        return (this.value != null) ? this.value : supplier.get();
    }
    
    public <X extends Throwable> T orElseThrow(final Supplier<? extends X> supplier) throws X, Throwable {
        if (this.value != null) {
            return this.value;
        }
        throw (Throwable)supplier.get();
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o instanceof Optional && Objects.equals(this.value, ((Optional)o).value));
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.value);
    }
    
    @Override
    public String toString() {
        return (this.value != null) ? String.format("Optional[%s]", this.value) : "Optional.empty";
    }
    
    static {
        EMPTY = new Optional<Object>();
    }
}
