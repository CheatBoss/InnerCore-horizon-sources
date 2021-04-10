package java.time.temporal;

import java.time.*;
import java.util.*;
import java.time.format.*;
import java.time.chrono.*;

public final class JulianFields
{
    private static final long JULIAN_DAY_OFFSET = 2440588L;
    public static final TemporalField JULIAN_DAY;
    public static final TemporalField MODIFIED_JULIAN_DAY;
    public static final TemporalField RATA_DIE;
    
    private JulianFields() {
        throw new AssertionError((Object)"Not instantiable");
    }
    
    static {
        JULIAN_DAY = Field.JULIAN_DAY;
        MODIFIED_JULIAN_DAY = Field.MODIFIED_JULIAN_DAY;
        RATA_DIE = Field.RATA_DIE;
    }
    
    private enum Field implements TemporalField
    {
        JULIAN_DAY("JulianDay", (TemporalUnit)ChronoUnit.DAYS, (TemporalUnit)ChronoUnit.FOREVER, 2440588L), 
        MODIFIED_JULIAN_DAY("ModifiedJulianDay", (TemporalUnit)ChronoUnit.DAYS, (TemporalUnit)ChronoUnit.FOREVER, 40587L), 
        RATA_DIE("RataDie", (TemporalUnit)ChronoUnit.DAYS, (TemporalUnit)ChronoUnit.FOREVER, 719163L);
        
        private static final long serialVersionUID = -7501623920830201812L;
        private final transient String name;
        private final transient TemporalUnit baseUnit;
        private final transient TemporalUnit rangeUnit;
        private final transient ValueRange range;
        private final transient long offset;
        
        private Field(final String name, final TemporalUnit baseUnit, final TemporalUnit rangeUnit, final long offset) {
            this.name = name;
            this.baseUnit = baseUnit;
            this.rangeUnit = rangeUnit;
            this.range = ValueRange.of(-365243219162L + offset, 365241780471L + offset);
            this.offset = offset;
        }
        
        @Override
        public TemporalUnit getBaseUnit() {
            return this.baseUnit;
        }
        
        @Override
        public TemporalUnit getRangeUnit() {
            return this.rangeUnit;
        }
        
        @Override
        public boolean isDateBased() {
            return true;
        }
        
        @Override
        public boolean isTimeBased() {
            return false;
        }
        
        @Override
        public ValueRange range() {
            return this.range;
        }
        
        @Override
        public boolean isSupportedBy(final TemporalAccessor temporalAccessor) {
            return temporalAccessor.isSupported(ChronoField.EPOCH_DAY);
        }
        
        @Override
        public ValueRange rangeRefinedBy(final TemporalAccessor temporalAccessor) {
            if (!this.isSupportedBy(temporalAccessor)) {
                throw new DateTimeException("Unsupported field: " + this);
            }
            return this.range();
        }
        
        @Override
        public long getFrom(final TemporalAccessor temporalAccessor) {
            return temporalAccessor.getLong(ChronoField.EPOCH_DAY) + this.offset;
        }
        
        @Override
        public <R extends Temporal> R adjustInto(final R r, final long n) {
            if (!this.range().isValidValue(n)) {
                throw new DateTimeException("Invalid value: " + this.name + " " + n);
            }
            return (R)r.with(ChronoField.EPOCH_DAY, Math.subtractExact(n, this.offset));
        }
        
        @Override
        public ChronoLocalDate resolve(final Map<TemporalField, Long> map, final TemporalAccessor temporalAccessor, final ResolverStyle resolverStyle) {
            final long longValue = map.remove(this);
            final Chronology from = Chronology.from(temporalAccessor);
            if (resolverStyle == ResolverStyle.LENIENT) {
                return from.dateEpochDay(Math.subtractExact(longValue, this.offset));
            }
            this.range().checkValidValue(longValue, this);
            return from.dateEpochDay(longValue - this.offset);
        }
        
        @Override
        public String toString() {
            return this.name;
        }
    }
}
