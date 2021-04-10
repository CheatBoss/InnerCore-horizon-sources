package java.time;

import java.time.temporal.*;

public final class -$$Lambda$up1HpCqucM_DXyY-rpDOyCcdmIA implements TemporalQuery
{
    @Override
    public final Object queryFrom(final TemporalAccessor temporalAccessor) {
        return ZonedDateTime.from(temporalAccessor);
    }
}
