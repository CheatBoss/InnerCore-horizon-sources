package java.time;

import java.time.temporal.*;

public final class -$$Lambda$sL_1zXqh7GXCv2G9X40ozp_OBMA implements TemporalQuery
{
    @Override
    public final Object queryFrom(final TemporalAccessor temporalAccessor) {
        return MonthDay.from(temporalAccessor);
    }
}
