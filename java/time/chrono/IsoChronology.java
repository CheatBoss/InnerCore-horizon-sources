package java.time.chrono;

import java.util.*;
import java.time.format.*;
import java.time.temporal.*;
import java.time.*;
import java.io.*;

public final class IsoChronology extends AbstractChronology implements Serializable
{
    public static final IsoChronology INSTANCE;
    private static final long serialVersionUID = -1440403870442975015L;
    
    private IsoChronology() {
    }
    
    @Override
    public String getId() {
        return "ISO";
    }
    
    @Override
    public String getCalendarType() {
        return "iso8601";
    }
    
    @Override
    public LocalDate date(final Era era, final int n, final int n2, final int n3) {
        return this.date(this.prolepticYear(era, n), n2, n3);
    }
    
    @Override
    public LocalDate date(final int n, final int n2, final int n3) {
        return LocalDate.of(n, n2, n3);
    }
    
    @Override
    public LocalDate dateYearDay(final Era era, final int n, final int n2) {
        return this.dateYearDay(this.prolepticYear(era, n), n2);
    }
    
    @Override
    public LocalDate dateYearDay(final int n, final int n2) {
        return LocalDate.ofYearDay(n, n2);
    }
    
    @Override
    public LocalDate dateEpochDay(final long n) {
        return LocalDate.ofEpochDay(n);
    }
    
    @Override
    public LocalDate date(final TemporalAccessor temporalAccessor) {
        return LocalDate.from(temporalAccessor);
    }
    
    @Override
    public LocalDateTime localDateTime(final TemporalAccessor temporalAccessor) {
        return LocalDateTime.from(temporalAccessor);
    }
    
    @Override
    public ZonedDateTime zonedDateTime(final TemporalAccessor temporalAccessor) {
        return ZonedDateTime.from(temporalAccessor);
    }
    
    @Override
    public ZonedDateTime zonedDateTime(final Instant instant, final ZoneId zoneId) {
        return ZonedDateTime.ofInstant(instant, zoneId);
    }
    
    @Override
    public LocalDate dateNow() {
        return this.dateNow(Clock.systemDefaultZone());
    }
    
    @Override
    public LocalDate dateNow(final ZoneId zoneId) {
        return this.dateNow(Clock.system(zoneId));
    }
    
    @Override
    public LocalDate dateNow(final Clock clock) {
        Objects.requireNonNull(clock, "clock");
        return this.date(LocalDate.now(clock));
    }
    
    @Override
    public boolean isLeapYear(final long n) {
        return (n & 0x3L) == 0x0L && (n % 100L != 0L || n % 400L == 0L);
    }
    
    @Override
    public int prolepticYear(final Era era, final int n) {
        if (!(era instanceof IsoEra)) {
            throw new ClassCastException("Era must be IsoEra");
        }
        return (era == IsoEra.CE) ? n : (1 - n);
    }
    
    @Override
    public IsoEra eraOf(final int n) {
        return IsoEra.of(n);
    }
    
    @Override
    public List<Era> eras() {
        return (List<Era>)Arrays.asList(IsoEra.values());
    }
    
    @Override
    public LocalDate resolveDate(final Map<TemporalField, Long> map, final ResolverStyle resolverStyle) {
        return (LocalDate)super.resolveDate(map, resolverStyle);
    }
    
    @Override
    void resolveProlepticMonth(final Map<TemporalField, Long> map, final ResolverStyle resolverStyle) {
        final Long n = map.remove(ChronoField.PROLEPTIC_MONTH);
        if (n != null) {
            if (resolverStyle != ResolverStyle.LENIENT) {
                ChronoField.PROLEPTIC_MONTH.checkValidValue(n);
            }
            this.addFieldValue(map, ChronoField.MONTH_OF_YEAR, Math.floorMod(n, 12L) + 1L);
            this.addFieldValue(map, ChronoField.YEAR, Math.floorDiv(n, 12L));
        }
    }
    
    @Override
    LocalDate resolveYearOfEra(final Map<TemporalField, Long> map, final ResolverStyle resolverStyle) {
        final Long n = map.remove(ChronoField.YEAR_OF_ERA);
        if (n != null) {
            if (resolverStyle != ResolverStyle.LENIENT) {
                ChronoField.YEAR_OF_ERA.checkValidValue(n);
            }
            final Long n2 = map.remove(ChronoField.ERA);
            if (n2 == null) {
                final Long n3 = map.get(ChronoField.YEAR);
                if (resolverStyle == ResolverStyle.STRICT) {
                    if (n3 != null) {
                        this.addFieldValue(map, ChronoField.YEAR, (n3 > 0L) ? ((long)n) : Math.subtractExact(1L, n));
                    }
                    else {
                        map.put(ChronoField.YEAR_OF_ERA, n);
                    }
                }
                else {
                    this.addFieldValue(map, ChronoField.YEAR, (n3 == null || n3 > 0L) ? ((long)n) : Math.subtractExact(1L, n));
                }
            }
            else if (n2 == 1L) {
                this.addFieldValue(map, ChronoField.YEAR, n);
            }
            else {
                if (n2 != 0L) {
                    throw new DateTimeException("Invalid value for era: " + n2);
                }
                this.addFieldValue(map, ChronoField.YEAR, Math.subtractExact(1L, n));
            }
        }
        else if (map.containsKey(ChronoField.ERA)) {
            ChronoField.ERA.checkValidValue(map.get(ChronoField.ERA));
        }
        return null;
    }
    
    @Override
    LocalDate resolveYMD(final Map<TemporalField, Long> map, final ResolverStyle resolverStyle) {
        final int checkValidIntValue = ChronoField.YEAR.checkValidIntValue(map.remove(ChronoField.YEAR));
        if (resolverStyle == ResolverStyle.LENIENT) {
            return LocalDate.of(checkValidIntValue, 1, 1).plusMonths(Math.subtractExact(map.remove(ChronoField.MONTH_OF_YEAR), 1L)).plusDays(Math.subtractExact(map.remove(ChronoField.DAY_OF_MONTH), 1L));
        }
        final int checkValidIntValue2 = ChronoField.MONTH_OF_YEAR.checkValidIntValue(map.remove(ChronoField.MONTH_OF_YEAR));
        int n = ChronoField.DAY_OF_MONTH.checkValidIntValue(map.remove(ChronoField.DAY_OF_MONTH));
        if (resolverStyle == ResolverStyle.SMART) {
            if (checkValidIntValue2 == 4 || checkValidIntValue2 == 6 || checkValidIntValue2 == 9 || checkValidIntValue2 == 11) {
                n = Math.min(n, 30);
            }
            else if (checkValidIntValue2 == 2) {
                n = Math.min(n, Month.FEBRUARY.length(Year.isLeap(checkValidIntValue)));
            }
        }
        return LocalDate.of(checkValidIntValue, checkValidIntValue2, n);
    }
    
    @Override
    public ValueRange range(final ChronoField chronoField) {
        return chronoField.range();
    }
    
    @Override
    public Period period(final int n, final int n2, final int n3) {
        return Period.of(n, n2, n3);
    }
    
    @Override
    Object writeReplace() {
        return super.writeReplace();
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
    
    static {
        INSTANCE = new IsoChronology();
    }
}
