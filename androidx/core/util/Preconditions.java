package androidx.core.util;

import java.util.*;
import androidx.annotation.*;

@RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
public final class Preconditions
{
    private Preconditions() {
    }
    
    public static void checkArgument(final boolean b) {
        if (!b) {
            throw new IllegalArgumentException();
        }
    }
    
    public static void checkArgument(final boolean b, @NonNull final Object o) {
        if (!b) {
            throw new IllegalArgumentException(String.valueOf(o));
        }
    }
    
    public static int checkArgumentInRange(final int n, final int n2, final int n3, @NonNull final String s) {
        if (n < n2) {
            throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%d, %d] (too low)", s, n2, n3));
        }
        if (n > n3) {
            throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%d, %d] (too high)", s, n2, n3));
        }
        return n;
    }
    
    @IntRange(from = 0L)
    public static int checkArgumentNonnegative(final int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        return n;
    }
    
    @IntRange(from = 0L)
    public static int checkArgumentNonnegative(final int n, @Nullable final String s) {
        if (n < 0) {
            throw new IllegalArgumentException(s);
        }
        return n;
    }
    
    @NonNull
    public static <T> T checkNotNull(@Nullable final T t) {
        if (t == null) {
            throw new NullPointerException();
        }
        return t;
    }
    
    @NonNull
    public static <T> T checkNotNull(@Nullable final T t, @NonNull final Object o) {
        if (t == null) {
            throw new NullPointerException(String.valueOf(o));
        }
        return t;
    }
    
    public static void checkState(final boolean b) {
        checkState(b, null);
    }
    
    public static void checkState(final boolean b, @Nullable final String s) {
        if (!b) {
            throw new IllegalStateException(s);
        }
    }
}
