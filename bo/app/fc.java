package bo.app;

import java.util.*;

public final class fc extends fb implements eq
{
    public fc(final List<eq> list) {
        super(list);
    }
    
    @Override
    public boolean a(final fk fk) {
        final Iterator<eq> iterator = this.a.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().a(fk)) {
                return true;
            }
        }
        return false;
    }
}
