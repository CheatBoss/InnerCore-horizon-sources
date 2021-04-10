package androidx.core.util;

import androidx.annotation.*;

public class Pair<F, S>
{
    @Nullable
    public final F first;
    @Nullable
    public final S second;
    
    public Pair(@Nullable final F first, @Nullable final S second) {
        this.first = first;
        this.second = second;
    }
    
    @NonNull
    public static <A, B> Pair<A, B> create(@Nullable final A a, @Nullable final B b) {
        return new Pair<A, B>(a, b);
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof Pair;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final Pair pair = (Pair)o;
        boolean b3 = b2;
        if (ObjectsCompat.equals(pair.first, this.first)) {
            b3 = b2;
            if (ObjectsCompat.equals(pair.second, this.second)) {
                b3 = true;
            }
        }
        return b3;
    }
    
    @Override
    public int hashCode() {
        final F first = this.first;
        int hashCode = 0;
        int hashCode2;
        if (first == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = this.first.hashCode();
        }
        if (this.second != null) {
            hashCode = this.second.hashCode();
        }
        return hashCode2 ^ hashCode;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Pair{");
        sb.append(String.valueOf(this.first));
        sb.append(" ");
        sb.append(String.valueOf(this.second));
        sb.append("}");
        return sb.toString();
    }
}
