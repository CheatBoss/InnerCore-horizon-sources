package java.time.chrono;

import java.time.*;
import java.util.*;
import java.time.temporal.*;
import java.time.format.*;
import java.io.*;

public final class MinguoChronology extends AbstractChronology implements Serializable
{
    public static final MinguoChronology INSTANCE;
    private static final long serialVersionUID = 1039765215346859963L;
    static final int YEARS_DIFFERENCE = 1911;
    
    private MinguoChronology() {
    }
    
    @Override
    public String getId() {
        return "Minguo";
    }
    
    @Override
    public String getCalendarType() {
        return "roc";
    }
    
    @Override
    public MinguoDate date(final Era era, final int n, final int n2, final int n3) {
        return this.date(this.prolepticYear(era, n), n2, n3);
    }
    
    @Override
    public MinguoDate date(final int n, final int n2, final int n3) {
        return new MinguoDate(LocalDate.of(n + 1911, n2, n3));
    }
    
    @Override
    public MinguoDate dateYearDay(final Era era, final int n, final int n2) {
        return this.dateYearDay(this.prolepticYear(era, n), n2);
    }
    
    @Override
    public MinguoDate dateYearDay(final int n, final int n2) {
        return new MinguoDate(LocalDate.ofYearDay(n + 1911, n2));
    }
    
    @Override
    public MinguoDate dateEpochDay(final long n) {
        return new MinguoDate(LocalDate.ofEpochDay(n));
    }
    
    @Override
    public MinguoDate dateNow() {
        return this.dateNow(Clock.systemDefaultZone());
    }
    
    @Override
    public MinguoDate dateNow(final ZoneId zoneId) {
        return this.dateNow(Clock.system(zoneId));
    }
    
    @Override
    public MinguoDate dateNow(final Clock clock) {
        return this.date(LocalDate.now(clock));
    }
    
    @Override
    public MinguoDate date(final TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof MinguoDate) {
            return (MinguoDate)temporalAccessor;
        }
        return new MinguoDate(LocalDate.from(temporalAccessor));
    }
    
    @Override
    public ChronoLocalDateTime<MinguoDate> localDateTime(final TemporalAccessor temporalAccessor) {
        return (ChronoLocalDateTime<MinguoDate>)super.localDateTime(temporalAccessor);
    }
    
    @Override
    public ChronoZonedDateTime<MinguoDate> zonedDateTime(final TemporalAccessor temporalAccessor) {
        return (ChronoZonedDateTime<MinguoDate>)super.zonedDateTime(temporalAccessor);
    }
    
    @Override
    public ChronoZonedDateTime<MinguoDate> zonedDateTime(final Instant instant, final ZoneId zoneId) {
        return (ChronoZonedDateTime<MinguoDate>)super.zonedDateTime(instant, zoneId);
    }
    
    @Override
    public boolean isLeapYear(final long n) {
        return IsoChronology.INSTANCE.isLeapYear(n + 1911L);
    }
    
    @Override
    public int prolepticYear(final Era era, final int n) {
        if (!(era instanceof MinguoEra)) {
            throw new ClassCastException("Era must be MinguoEra");
        }
        return (era == MinguoEra.ROC) ? n : (1 - n);
    }
    
    @Override
    public MinguoEra eraOf(final int n) {
        return MinguoEra.of(n);
    }
    
    @Override
    public List<Era> eras() {
        return (List<Era>)Arrays.asList(MinguoEra.values());
    }
    
    @Override
    public ValueRange range(final ChronoField chronoField) {
        switch (chronoField) {
            case PROLEPTIC_MONTH: {
                final ValueRange range = ChronoField.PROLEPTIC_MONTH.range();
                return ValueRange.of(range.getMinimum() - 22932L, range.getMaximum() - 22932L);
            }
            case YEAR_OF_ERA: {
                final ValueRange range2 = ChronoField.YEAR.range();
                return ValueRange.of(1L, range2.getMaximum() - 1911L, -range2.getMinimum() + 1L + 1911L);
            }
            case YEAR: {
                final ValueRange range3 = ChronoField.YEAR.range();
                return ValueRange.of(range3.getMinimum() - 1911L, range3.getMaximum() - 1911L);
            }
            default: {
                return chronoField.range();
            }
        }
    }
    
    @Override
    public MinguoDate resolveDate(final Map<TemporalField, Long> map, final ResolverStyle resolverStyle) {
        return (MinguoDate)super.resolveDate(map, resolverStyle);
    }
    
    @Override
    Object writeReplace() {
        return super.writeReplace();
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
    
    static {
        INSTANCE = new MinguoChronology();
    }
}
