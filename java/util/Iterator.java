package java.util;

import java.util.function.*;

public interface Iterator<E>
{
    boolean hasNext();
    
    E next();
    
    default void remove() {
        throw new UnsupportedOperationException("remove");
    }
    
    default void forEachRemaining(final Consumer<? super E> consumer) {
        Objects.requireNonNull(consumer);
        while (this.hasNext()) {
            consumer.accept(this.next());
        }
    }
}
