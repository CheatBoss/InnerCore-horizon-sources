package java.time;

import java.time.temporal.*;

public final class -$$Lambda$PTL8WkLA4o-1z4zIUBjrvwi808w implements TemporalQuery
{
    @Override
    public final Object queryFrom(final TemporalAccessor temporalAccessor) {
        return Instant.from(temporalAccessor);
    }
}
