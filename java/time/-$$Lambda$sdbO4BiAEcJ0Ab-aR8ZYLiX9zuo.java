package java.time;

import java.time.temporal.*;

public final class -$$Lambda$sdbO4BiAEcJ0Ab-aR8ZYLiX9zuo implements TemporalQuery
{
    @Override
    public final Object queryFrom(final TemporalAccessor temporalAccessor) {
        return OffsetDateTime.from(temporalAccessor);
    }
}
