package java.util;

public class DesugarLinkedHashSet
{
    private DesugarLinkedHashSet() {
    }
    
    public static <E> Spliterator<E> spliterator(final LinkedHashSet<E> set) {
        return Spliterators.spliterator((Collection<? extends E>)set, 17);
    }
}
