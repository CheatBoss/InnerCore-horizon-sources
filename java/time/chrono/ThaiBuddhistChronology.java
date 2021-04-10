package java.time.chrono;

import java.time.*;
import java.util.*;
import java.time.temporal.*;
import java.time.format.*;
import java.io.*;

public final class ThaiBuddhistChronology extends AbstractChronology implements Serializable
{
    public static final ThaiBuddhistChronology INSTANCE;
    private static final long serialVersionUID = 2775954514031616474L;
    static final int YEARS_DIFFERENCE = 543;
    private static final HashMap<String, String[]> ERA_NARROW_NAMES;
    private static final HashMap<String, String[]> ERA_SHORT_NAMES;
    private static final HashMap<String, String[]> ERA_FULL_NAMES;
    private static final String FALLBACK_LANGUAGE = "en";
    private static final String TARGET_LANGUAGE = "th";
    
    private ThaiBuddhistChronology() {
    }
    
    @Override
    public String getId() {
        return "ThaiBuddhist";
    }
    
    @Override
    public String getCalendarType() {
        return "buddhist";
    }
    
    @Override
    public ThaiBuddhistDate date(final Era era, final int n, final int n2, final int n3) {
        return this.date(this.prolepticYear(era, n), n2, n3);
    }
    
    @Override
    public ThaiBuddhistDate date(final int n, final int n2, final int n3) {
        return new ThaiBuddhistDate(LocalDate.of(n - 543, n2, n3));
    }
    
    @Override
    public ThaiBuddhistDate dateYearDay(final Era era, final int n, final int n2) {
        return this.dateYearDay(this.prolepticYear(era, n), n2);
    }
    
    @Override
    public ThaiBuddhistDate dateYearDay(final int n, final int n2) {
        return new ThaiBuddhistDate(LocalDate.ofYearDay(n - 543, n2));
    }
    
    @Override
    public ThaiBuddhistDate dateEpochDay(final long n) {
        return new ThaiBuddhistDate(LocalDate.ofEpochDay(n));
    }
    
    @Override
    public ThaiBuddhistDate dateNow() {
        return this.dateNow(Clock.systemDefaultZone());
    }
    
    @Override
    public ThaiBuddhistDate dateNow(final ZoneId zoneId) {
        return this.dateNow(Clock.system(zoneId));
    }
    
    @Override
    public ThaiBuddhistDate dateNow(final Clock clock) {
        return this.date(LocalDate.now(clock));
    }
    
    @Override
    public ThaiBuddhistDate date(final TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof ThaiBuddhistDate) {
            return (ThaiBuddhistDate)temporalAccessor;
        }
        return new ThaiBuddhistDate(LocalDate.from(temporalAccessor));
    }
    
    @Override
    public ChronoLocalDateTime<ThaiBuddhistDate> localDateTime(final TemporalAccessor temporalAccessor) {
        return (ChronoLocalDateTime<ThaiBuddhistDate>)super.localDateTime(temporalAccessor);
    }
    
    @Override
    public ChronoZonedDateTime<ThaiBuddhistDate> zonedDateTime(final TemporalAccessor temporalAccessor) {
        return (ChronoZonedDateTime<ThaiBuddhistDate>)super.zonedDateTime(temporalAccessor);
    }
    
    @Override
    public ChronoZonedDateTime<ThaiBuddhistDate> zonedDateTime(final Instant instant, final ZoneId zoneId) {
        return (ChronoZonedDateTime<ThaiBuddhistDate>)super.zonedDateTime(instant, zoneId);
    }
    
    @Override
    public boolean isLeapYear(final long n) {
        return IsoChronology.INSTANCE.isLeapYear(n - 543L);
    }
    
    @Override
    public int prolepticYear(final Era era, final int n) {
        if (!(era instanceof ThaiBuddhistEra)) {
            throw new ClassCastException("Era must be BuddhistEra");
        }
        return (era == ThaiBuddhistEra.BE) ? n : (1 - n);
    }
    
    @Override
    public ThaiBuddhistEra eraOf(final int n) {
        return ThaiBuddhistEra.of(n);
    }
    
    @Override
    public List<Era> eras() {
        return (List<Era>)Arrays.asList(ThaiBuddhistEra.values());
    }
    
    @Override
    public ValueRange range(final ChronoField chronoField) {
        switch (chronoField) {
            case PROLEPTIC_MONTH: {
                final ValueRange range = ChronoField.PROLEPTIC_MONTH.range();
                return ValueRange.of(range.getMinimum() + 6516L, range.getMaximum() + 6516L);
            }
            case YEAR_OF_ERA: {
                final ValueRange range2 = ChronoField.YEAR.range();
                return ValueRange.of(1L, -(range2.getMinimum() + 543L) + 1L, range2.getMaximum() + 543L);
            }
            case YEAR: {
                final ValueRange range3 = ChronoField.YEAR.range();
                return ValueRange.of(range3.getMinimum() + 543L, range3.getMaximum() + 543L);
            }
            default: {
                return chronoField.range();
            }
        }
    }
    
    @Override
    public ThaiBuddhistDate resolveDate(final Map<TemporalField, Long> map, final ResolverStyle resolverStyle) {
        return (ThaiBuddhistDate)super.resolveDate(map, resolverStyle);
    }
    
    @Override
    Object writeReplace() {
        return super.writeReplace();
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
    
    static {
        INSTANCE = new ThaiBuddhistChronology();
        ERA_NARROW_NAMES = new HashMap<String, String[]>();
        ERA_SHORT_NAMES = new HashMap<String, String[]>();
        ERA_FULL_NAMES = new HashMap<String, String[]>();
        ThaiBuddhistChronology.ERA_NARROW_NAMES.put("en", new String[] { "BB", "BE" });
        ThaiBuddhistChronology.ERA_NARROW_NAMES.put("th", new String[] { "BB", "BE" });
        ThaiBuddhistChronology.ERA_SHORT_NAMES.put("en", new String[] { "B.B.", "B.E." });
        ThaiBuddhistChronology.ERA_SHORT_NAMES.put("th", new String[] { "\u0e1e.\u0e28.", "\u0e1b\u0e35\u0e01\u0e48\u0e2d\u0e19\u0e04\u0e23\u0e34\u0e2a\u0e15\u0e4c\u0e01\u0e32\u0e25\u0e17\u0e35\u0e48" });
        ThaiBuddhistChronology.ERA_FULL_NAMES.put("en", new String[] { "Before Buddhist", "Budhhist Era" });
        ThaiBuddhistChronology.ERA_FULL_NAMES.put("th", new String[] { "\u0e1e\u0e38\u0e17\u0e18\u0e28\u0e31\u0e01\u0e23\u0e32\u0e0a", "\u0e1b\u0e35\u0e01\u0e48\u0e2d\u0e19\u0e04\u0e23\u0e34\u0e2a\u0e15\u0e4c\u0e01\u0e32\u0e25\u0e17\u0e35\u0e48" });
    }
}
