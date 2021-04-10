package java.time;

import java.time.temporal.*;

public final class -$$Lambda$2Dm_gBEmfWAFyI8wDj_HTrgAgUc implements TemporalQuery
{
    @Override
    public final Object queryFrom(final TemporalAccessor temporalAccessor) {
        return LocalTime.from(temporalAccessor);
    }
}
