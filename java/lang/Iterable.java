package java.lang;

import java.util.function.*;
import java.util.*;

public interface Iterable<T>
{
    Iterator<T> iterator();
    
    default void forEach(final Consumer<? super T> consumer) {
        Objects.requireNonNull(consumer);
        final Iterator<Object> iterator = this.iterator();
        while (iterator.hasNext()) {
            consumer.accept((Object)iterator.next());
        }
    }
    
    default Spliterator<T> spliterator() {
        return Spliterators.spliteratorUnknownSize(this.iterator(), 0);
    }
}
