package java.time.temporal;

import java.util.*;
import java.time.format.*;

public interface TemporalField
{
    default String getDisplayName(final Locale locale) {
        Objects.requireNonNull(locale, "locale");
        return this.toString();
    }
    
    TemporalUnit getBaseUnit();
    
    TemporalUnit getRangeUnit();
    
    ValueRange range();
    
    boolean isDateBased();
    
    boolean isTimeBased();
    
    boolean isSupportedBy(final TemporalAccessor p0);
    
    ValueRange rangeRefinedBy(final TemporalAccessor p0);
    
    long getFrom(final TemporalAccessor p0);
    
     <R extends Temporal> R adjustInto(final R p0, final long p1);
    
    default TemporalAccessor resolve(final Map<TemporalField, Long> map, final TemporalAccessor temporalAccessor, final ResolverStyle resolverStyle) {
        return null;
    }
    
    String toString();
}
