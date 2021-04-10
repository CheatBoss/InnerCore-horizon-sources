package java.time;

import java.time.temporal.*;

public final class -$$Lambda$Bq8PKq1YWr8nyVk9SSfRYKrOu4A implements TemporalQuery
{
    @Override
    public final Object queryFrom(final TemporalAccessor temporalAccessor) {
        return LocalDate.from(temporalAccessor);
    }
}
