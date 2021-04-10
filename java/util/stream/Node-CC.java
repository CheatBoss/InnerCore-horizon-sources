package java.util.stream;

import java.util.function.*;
import java.util.*;

public final class Node-CC
{
    public static Node<Object> $default$getChild(final Node node, final int n) {
        throw new IndexOutOfBoundsException();
    }
    
    public static int $default$getChildCount(final Node node) {
        return 0;
    }
    
    public static StreamShape $default$getShape(final Node node) {
        return StreamShape.REFERENCE;
    }
    
    public static Node $default$truncate(final Node node, final long n, long n2, final IntFunction intFunction) {
        if (n == 0L && n2 == node.count()) {
            return node;
        }
        final Spliterator spliterator = node.spliterator();
        n2 -= n;
        final Node.Builder<Object> builder = Nodes.builder(n2, (IntFunction<Object[]>)intFunction);
        builder.begin(n2);
        final int n3 = 0;
        for (int n4 = 0; n4 < n && spliterator.tryAdvance(-$$Lambda$Node$fa69PlTVbbnR3yr46T9Wo0_bIhg.INSTANCE); ++n4) {}
        for (int n5 = n3; n5 < n2 && spliterator.tryAdvance(builder); ++n5) {}
        builder.end();
        return builder.build();
    }
}
