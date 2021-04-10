package bo.app;

import java.util.*;

public final class fa extends fb implements eq
{
    public fa(final List<eq> list) {
        super(list);
    }
    
    @Override
    public boolean a(final fk fk) {
        final Iterator<eq> iterator = this.a.iterator();
        boolean b = false;
        while (iterator.hasNext()) {
            if (!iterator.next().a(fk)) {
                return false;
            }
            b = true;
        }
        return b;
    }
}
