package bo.app;

import com.appboy.models.*;
import java.util.*;

public final class ce implements IPutIntoJson<String>
{
    private final UUID a;
    private final String b;
    
    public ce(final UUID a) {
        this.a = a;
        this.b = a.toString();
    }
    
    public static ce a() {
        return new ce(UUID.randomUUID());
    }
    
    public static ce a(final String s) {
        return new ce(UUID.fromString(s));
    }
    
    public String b() {
        return this.b;
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o != null && this.getClass() == o.getClass() && this.a.equals(((ce)o).a));
    }
    
    @Override
    public int hashCode() {
        return this.a.hashCode();
    }
    
    @Override
    public String toString() {
        return this.b;
    }
}
