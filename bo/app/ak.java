package bo.app;

import com.appboy.models.*;
import org.json.*;

public final class ak
{
    private final IInAppMessage a;
    private final String b;
    private final ek c;
    
    public ak(final ek c, final IInAppMessage a, final String b) {
        this.b = b;
        if (a != null) {
            this.a = a;
            this.c = c;
            return;
        }
        throw null;
    }
    
    public ek a() {
        return this.c;
    }
    
    public IInAppMessage b() {
        return this.a;
    }
    
    public String c() {
        return this.b;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(ec.a(this.a.forJsonPut()));
        sb.append("\nTriggered Action Id: ");
        sb.append(this.c.b());
        sb.append("\nUser Id: ");
        sb.append(this.b);
        return sb.toString();
    }
}
