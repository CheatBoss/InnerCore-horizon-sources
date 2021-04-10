package bo.app;

import com.appboy.models.*;

public class cf implements IPutIntoJson<String>
{
    private final String a;
    
    public cf(final String a) {
        this.a = a;
    }
    
    public String a() {
        return this.a;
    }
    
    @Override
    public String toString() {
        return this.a;
    }
}
