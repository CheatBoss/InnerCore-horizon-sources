package java.time;

import java.time.temporal.*;

public final class -$$Lambda$1t2bycXU085eFZcwODXkbd0X4Bk implements TemporalQuery
{
    @Override
    public final Object queryFrom(final TemporalAccessor temporalAccessor) {
        return Year.from(temporalAccessor);
    }
}
