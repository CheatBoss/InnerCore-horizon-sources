package java.time.chrono;

import java.util.concurrent.*;
import sun.util.logging.*;
import java.time.format.*;
import java.util.*;
import java.time.temporal.*;
import java.time.*;
import java.lang.invoke.*;
import java.io.*;

public abstract class AbstractChronology implements Chronology
{
    static final Comparator<ChronoLocalDate> DATE_ORDER;
    static final Comparator<ChronoLocalDateTime<? extends ChronoLocalDate>> DATE_TIME_ORDER;
    static final Comparator<ChronoZonedDateTime<?>> INSTANT_ORDER;
    private static final ConcurrentHashMap<String, Chronology> CHRONOS_BY_ID;
    private static final ConcurrentHashMap<String, Chronology> CHRONOS_BY_TYPE;
    
    static Chronology registerChrono(final Chronology chronology) {
        return registerChrono(chronology, chronology.getId());
    }
    
    static Chronology registerChrono(final Chronology chronology, final String s) {
        final Chronology chronology2 = AbstractChronology.CHRONOS_BY_ID.putIfAbsent(s, chronology);
        if (chronology2 == null) {
            final String calendarType = chronology.getCalendarType();
            if (calendarType != null) {
                AbstractChronology.CHRONOS_BY_TYPE.putIfAbsent(calendarType, chronology);
            }
        }
        return chronology2;
    }
    
    private static boolean initCache() {
        if (AbstractChronology.CHRONOS_BY_ID.get("ISO") == null) {
            registerChrono(HijrahChronology.INSTANCE);
            registerChrono(JapaneseChronology.INSTANCE);
            registerChrono(MinguoChronology.INSTANCE);
            registerChrono(ThaiBuddhistChronology.INSTANCE);
            for (final AbstractChronology abstractChronology : ServiceLoader.load(AbstractChronology.class, null)) {
                final String id = abstractChronology.getId();
                if (id.equals("ISO") || registerChrono(abstractChronology) != null) {
                    PlatformLogger.getLogger("java.time.chrono").warning("Ignoring duplicate Chronology, from ServiceLoader configuration " + id);
                }
            }
            registerChrono(IsoChronology.INSTANCE);
            return true;
        }
        return false;
    }
    
    static Chronology ofLocale(final Locale locale) {
        Objects.requireNonNull(locale, "locale");
        final String unicodeLocaleType = locale.getUnicodeLocaleType("ca");
        if (unicodeLocaleType == null || "iso".equals(unicodeLocaleType) || "iso8601".equals(unicodeLocaleType)) {
            return IsoChronology.INSTANCE;
        }
        do {
            final Chronology chronology = AbstractChronology.CHRONOS_BY_TYPE.get(unicodeLocaleType);
            if (chronology != null) {
                return chronology;
            }
        } while (initCache());
        for (final Chronology chronology2 : ServiceLoader.load(Chronology.class)) {
            if (unicodeLocaleType.equals(chronology2.getCalendarType())) {
                return chronology2;
            }
        }
        throw new DateTimeException("Unknown calendar system: " + unicodeLocaleType);
    }
    
    static Chronology of(final String s) {
        Objects.requireNonNull(s, "id");
        do {
            final Chronology of0 = of0(s);
            if (of0 != null) {
                return of0;
            }
        } while (initCache());
        for (final Chronology chronology : ServiceLoader.load(Chronology.class)) {
            if (s.equals(chronology.getId()) || s.equals(chronology.getCalendarType())) {
                return chronology;
            }
        }
        throw new DateTimeException("Unknown chronology: " + s);
    }
    
    private static Chronology of0(final String s) {
        Chronology chronology = AbstractChronology.CHRONOS_BY_ID.get(s);
        if (chronology == null) {
            chronology = AbstractChronology.CHRONOS_BY_TYPE.get(s);
        }
        return chronology;
    }
    
    static Set<Chronology> getAvailableChronologies() {
        initCache();
        final HashSet<Chronology> set = new HashSet<Chronology>(AbstractChronology.CHRONOS_BY_ID.values());
        final Iterator<Chronology> iterator = ServiceLoader.load(Chronology.class).iterator();
        while (iterator.hasNext()) {
            set.add(iterator.next());
        }
        return set;
    }
    
    protected AbstractChronology() {
    }
    
    @Override
    public ChronoLocalDate resolveDate(final Map<TemporalField, Long> map, final ResolverStyle resolverStyle) {
        if (map.containsKey(ChronoField.EPOCH_DAY)) {
            return this.dateEpochDay(map.remove(ChronoField.EPOCH_DAY));
        }
        this.resolveProlepticMonth(map, resolverStyle);
        final ChronoLocalDate resolveYearOfEra = this.resolveYearOfEra(map, resolverStyle);
        if (resolveYearOfEra != null) {
            return resolveYearOfEra;
        }
        if (map.containsKey(ChronoField.YEAR)) {
            if (map.containsKey(ChronoField.MONTH_OF_YEAR)) {
                if (map.containsKey(ChronoField.DAY_OF_MONTH)) {
                    return this.resolveYMD(map, resolverStyle);
                }
                if (map.containsKey(ChronoField.ALIGNED_WEEK_OF_MONTH)) {
                    if (map.containsKey(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH)) {
                        return this.resolveYMAA(map, resolverStyle);
                    }
                    if (map.containsKey(ChronoField.DAY_OF_WEEK)) {
                        return this.resolveYMAD(map, resolverStyle);
                    }
                }
            }
            if (map.containsKey(ChronoField.DAY_OF_YEAR)) {
                return this.resolveYD(map, resolverStyle);
            }
            if (map.containsKey(ChronoField.ALIGNED_WEEK_OF_YEAR)) {
                if (map.containsKey(ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR)) {
                    return this.resolveYAA(map, resolverStyle);
                }
                if (map.containsKey(ChronoField.DAY_OF_WEEK)) {
                    return this.resolveYAD(map, resolverStyle);
                }
            }
        }
        return null;
    }
    
    void resolveProlepticMonth(final Map<TemporalField, Long> map, final ResolverStyle resolverStyle) {
        final Long n = map.remove(ChronoField.PROLEPTIC_MONTH);
        if (n != null) {
            if (resolverStyle != ResolverStyle.LENIENT) {
                ChronoField.PROLEPTIC_MONTH.checkValidValue(n);
            }
            final ChronoLocalDate with = this.dateNow().with((TemporalField)ChronoField.DAY_OF_MONTH, 1L).with((TemporalField)ChronoField.PROLEPTIC_MONTH, (long)n);
            this.addFieldValue(map, ChronoField.MONTH_OF_YEAR, with.get(ChronoField.MONTH_OF_YEAR));
            this.addFieldValue(map, ChronoField.YEAR, with.get(ChronoField.YEAR));
        }
    }
    
    ChronoLocalDate resolveYearOfEra(final Map<TemporalField, Long> map, final ResolverStyle resolverStyle) {
        final Long n = map.remove(ChronoField.YEAR_OF_ERA);
        if (n != null) {
            final Long n2 = map.remove(ChronoField.ERA);
            int n3;
            if (resolverStyle != ResolverStyle.LENIENT) {
                n3 = this.range(ChronoField.YEAR_OF_ERA).checkValidIntValue(n, ChronoField.YEAR_OF_ERA);
            }
            else {
                n3 = Math.toIntExact(n);
            }
            if (n2 != null) {
                this.addFieldValue(map, ChronoField.YEAR, this.prolepticYear(this.eraOf(this.range(ChronoField.ERA).checkValidIntValue(n2, ChronoField.ERA)), n3));
            }
            else if (map.containsKey(ChronoField.YEAR)) {
                this.addFieldValue(map, ChronoField.YEAR, this.prolepticYear(this.dateYearDay(this.range(ChronoField.YEAR).checkValidIntValue(map.get(ChronoField.YEAR), ChronoField.YEAR), 1).getEra(), n3));
            }
            else if (resolverStyle == ResolverStyle.STRICT) {
                map.put(ChronoField.YEAR_OF_ERA, n);
            }
            else {
                final List<Era> eras = this.eras();
                if (eras.isEmpty()) {
                    this.addFieldValue(map, ChronoField.YEAR, n3);
                }
                else {
                    this.addFieldValue(map, ChronoField.YEAR, this.prolepticYear(eras.get(eras.size() - 1), n3));
                }
            }
        }
        else if (map.containsKey(ChronoField.ERA)) {
            this.range(ChronoField.ERA).checkValidValue(map.get(ChronoField.ERA), ChronoField.ERA);
        }
        return null;
    }
    
    ChronoLocalDate resolveYMD(final Map<TemporalField, Long> map, final ResolverStyle resolverStyle) {
        final int checkValidIntValue = this.range(ChronoField.YEAR).checkValidIntValue(map.remove(ChronoField.YEAR), ChronoField.YEAR);
        if (resolverStyle == ResolverStyle.LENIENT) {
            return this.date(checkValidIntValue, 1, 1).plus(Math.subtractExact(map.remove(ChronoField.MONTH_OF_YEAR), 1L), (TemporalUnit)ChronoUnit.MONTHS).plus(Math.subtractExact(map.remove(ChronoField.DAY_OF_MONTH), 1L), (TemporalUnit)ChronoUnit.DAYS);
        }
        final int checkValidIntValue2 = this.range(ChronoField.MONTH_OF_YEAR).checkValidIntValue(map.remove(ChronoField.MONTH_OF_YEAR), ChronoField.MONTH_OF_YEAR);
        final int checkValidIntValue3 = this.range(ChronoField.DAY_OF_MONTH).checkValidIntValue(map.remove(ChronoField.DAY_OF_MONTH), ChronoField.DAY_OF_MONTH);
        if (resolverStyle == ResolverStyle.SMART) {
            try {
                return this.date(checkValidIntValue, checkValidIntValue2, checkValidIntValue3);
            }
            catch (DateTimeException ex) {
                return this.date(checkValidIntValue, checkValidIntValue2, 1).with(TemporalAdjusters.lastDayOfMonth());
            }
        }
        return this.date(checkValidIntValue, checkValidIntValue2, checkValidIntValue3);
    }
    
    ChronoLocalDate resolveYD(final Map<TemporalField, Long> map, final ResolverStyle resolverStyle) {
        final int checkValidIntValue = this.range(ChronoField.YEAR).checkValidIntValue(map.remove(ChronoField.YEAR), ChronoField.YEAR);
        if (resolverStyle == ResolverStyle.LENIENT) {
            return this.dateYearDay(checkValidIntValue, 1).plus(Math.subtractExact(map.remove(ChronoField.DAY_OF_YEAR), 1L), (TemporalUnit)ChronoUnit.DAYS);
        }
        return this.dateYearDay(checkValidIntValue, this.range(ChronoField.DAY_OF_YEAR).checkValidIntValue(map.remove(ChronoField.DAY_OF_YEAR), ChronoField.DAY_OF_YEAR));
    }
    
    ChronoLocalDate resolveYMAA(final Map<TemporalField, Long> map, final ResolverStyle resolverStyle) {
        final int checkValidIntValue = this.range(ChronoField.YEAR).checkValidIntValue(map.remove(ChronoField.YEAR), ChronoField.YEAR);
        if (resolverStyle == ResolverStyle.LENIENT) {
            return this.date(checkValidIntValue, 1, 1).plus(Math.subtractExact(map.remove(ChronoField.MONTH_OF_YEAR), 1L), (TemporalUnit)ChronoUnit.MONTHS).plus(Math.subtractExact(map.remove(ChronoField.ALIGNED_WEEK_OF_MONTH), 1L), (TemporalUnit)ChronoUnit.WEEKS).plus(Math.subtractExact(map.remove(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH), 1L), (TemporalUnit)ChronoUnit.DAYS);
        }
        final int checkValidIntValue2 = this.range(ChronoField.MONTH_OF_YEAR).checkValidIntValue(map.remove(ChronoField.MONTH_OF_YEAR), ChronoField.MONTH_OF_YEAR);
        final ChronoLocalDate plus = this.date(checkValidIntValue, checkValidIntValue2, 1).plus((long)((this.range(ChronoField.ALIGNED_WEEK_OF_MONTH).checkValidIntValue(map.remove(ChronoField.ALIGNED_WEEK_OF_MONTH), ChronoField.ALIGNED_WEEK_OF_MONTH) - 1) * 7 + (this.range(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH).checkValidIntValue(map.remove(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH), ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH) - 1)), (TemporalUnit)ChronoUnit.DAYS);
        if (resolverStyle == ResolverStyle.STRICT && plus.get(ChronoField.MONTH_OF_YEAR) != checkValidIntValue2) {
            throw new DateTimeException("Strict mode rejected resolved date as it is in a different month");
        }
        return plus;
    }
    
    ChronoLocalDate resolveYMAD(final Map<TemporalField, Long> map, final ResolverStyle resolverStyle) {
        final int checkValidIntValue = this.range(ChronoField.YEAR).checkValidIntValue(map.remove(ChronoField.YEAR), ChronoField.YEAR);
        if (resolverStyle == ResolverStyle.LENIENT) {
            return this.resolveAligned(this.date(checkValidIntValue, 1, 1), Math.subtractExact(map.remove(ChronoField.MONTH_OF_YEAR), 1L), Math.subtractExact(map.remove(ChronoField.ALIGNED_WEEK_OF_MONTH), 1L), Math.subtractExact(map.remove(ChronoField.DAY_OF_WEEK), 1L));
        }
        final int checkValidIntValue2 = this.range(ChronoField.MONTH_OF_YEAR).checkValidIntValue(map.remove(ChronoField.MONTH_OF_YEAR), ChronoField.MONTH_OF_YEAR);
        final ChronoLocalDate with = this.date(checkValidIntValue, checkValidIntValue2, 1).plus((long)((this.range(ChronoField.ALIGNED_WEEK_OF_MONTH).checkValidIntValue(map.remove(ChronoField.ALIGNED_WEEK_OF_MONTH), ChronoField.ALIGNED_WEEK_OF_MONTH) - 1) * 7), (TemporalUnit)ChronoUnit.DAYS).with(TemporalAdjusters.nextOrSame(DayOfWeek.of(this.range(ChronoField.DAY_OF_WEEK).checkValidIntValue(map.remove(ChronoField.DAY_OF_WEEK), ChronoField.DAY_OF_WEEK))));
        if (resolverStyle == ResolverStyle.STRICT && with.get(ChronoField.MONTH_OF_YEAR) != checkValidIntValue2) {
            throw new DateTimeException("Strict mode rejected resolved date as it is in a different month");
        }
        return with;
    }
    
    ChronoLocalDate resolveYAA(final Map<TemporalField, Long> map, final ResolverStyle resolverStyle) {
        final int checkValidIntValue = this.range(ChronoField.YEAR).checkValidIntValue(map.remove(ChronoField.YEAR), ChronoField.YEAR);
        if (resolverStyle == ResolverStyle.LENIENT) {
            return this.dateYearDay(checkValidIntValue, 1).plus(Math.subtractExact(map.remove(ChronoField.ALIGNED_WEEK_OF_YEAR), 1L), (TemporalUnit)ChronoUnit.WEEKS).plus(Math.subtractExact(map.remove(ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR), 1L), (TemporalUnit)ChronoUnit.DAYS);
        }
        final ChronoLocalDate plus = this.dateYearDay(checkValidIntValue, 1).plus((long)((this.range(ChronoField.ALIGNED_WEEK_OF_YEAR).checkValidIntValue(map.remove(ChronoField.ALIGNED_WEEK_OF_YEAR), ChronoField.ALIGNED_WEEK_OF_YEAR) - 1) * 7 + (this.range(ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR).checkValidIntValue(map.remove(ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR), ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR) - 1)), (TemporalUnit)ChronoUnit.DAYS);
        if (resolverStyle == ResolverStyle.STRICT && plus.get(ChronoField.YEAR) != checkValidIntValue) {
            throw new DateTimeException("Strict mode rejected resolved date as it is in a different year");
        }
        return plus;
    }
    
    ChronoLocalDate resolveYAD(final Map<TemporalField, Long> map, final ResolverStyle resolverStyle) {
        final int checkValidIntValue = this.range(ChronoField.YEAR).checkValidIntValue(map.remove(ChronoField.YEAR), ChronoField.YEAR);
        if (resolverStyle == ResolverStyle.LENIENT) {
            return this.resolveAligned(this.dateYearDay(checkValidIntValue, 1), 0L, Math.subtractExact(map.remove(ChronoField.ALIGNED_WEEK_OF_YEAR), 1L), Math.subtractExact(map.remove(ChronoField.DAY_OF_WEEK), 1L));
        }
        final ChronoLocalDate with = this.dateYearDay(checkValidIntValue, 1).plus((long)((this.range(ChronoField.ALIGNED_WEEK_OF_YEAR).checkValidIntValue(map.remove(ChronoField.ALIGNED_WEEK_OF_YEAR), ChronoField.ALIGNED_WEEK_OF_YEAR) - 1) * 7), (TemporalUnit)ChronoUnit.DAYS).with(TemporalAdjusters.nextOrSame(DayOfWeek.of(this.range(ChronoField.DAY_OF_WEEK).checkValidIntValue(map.remove(ChronoField.DAY_OF_WEEK), ChronoField.DAY_OF_WEEK))));
        if (resolverStyle == ResolverStyle.STRICT && with.get(ChronoField.YEAR) != checkValidIntValue) {
            throw new DateTimeException("Strict mode rejected resolved date as it is in a different year");
        }
        return with;
    }
    
    ChronoLocalDate resolveAligned(final ChronoLocalDate chronoLocalDate, final long n, final long n2, long n3) {
        ChronoLocalDate chronoLocalDate2 = chronoLocalDate.plus(n, (TemporalUnit)ChronoUnit.MONTHS).plus(n2, (TemporalUnit)ChronoUnit.WEEKS);
        if (n3 > 7L) {
            chronoLocalDate2 = chronoLocalDate2.plus((n3 - 1L) / 7L, (TemporalUnit)ChronoUnit.WEEKS);
            n3 = (n3 - 1L) % 7L + 1L;
        }
        else if (n3 < 1L) {
            chronoLocalDate2 = chronoLocalDate2.plus(Math.subtractExact(n3, 7L) / 7L, (TemporalUnit)ChronoUnit.WEEKS);
            n3 = (n3 + 6L) % 7L + 1L;
        }
        return chronoLocalDate2.with(TemporalAdjusters.nextOrSame(DayOfWeek.of((int)n3)));
    }
    
    void addFieldValue(final Map<TemporalField, Long> map, final ChronoField chronoField, final long n) {
        final Long n2 = map.get(chronoField);
        if (n2 != null && n2 != n) {
            throw new DateTimeException("Conflict found: " + chronoField + " " + n2 + " differs from " + chronoField + " " + n);
        }
        map.put(chronoField, n);
    }
    
    @Override
    public int compareTo(final Chronology chronology) {
        return this.getId().compareTo(chronology.getId());
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o instanceof AbstractChronology && this.compareTo((Chronology)o) == 0);
    }
    
    @Override
    public int hashCode() {
        return this.getClass().hashCode() ^ this.getId().hashCode();
    }
    
    @Override
    public String toString() {
        return this.getId();
    }
    
    Object writeReplace() {
        return new Ser((byte)1, this);
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws ObjectStreamException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
    
    void writeExternal(final DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(this.getId());
    }
    
    static Chronology readExternal(final DataInput dataInput) throws IOException {
        return Chronology.of(dataInput.readUTF());
    }
    
    static {
        DATE_ORDER = ((chronoLocalDate, chronoLocalDate2) -> Long.compare(chronoLocalDate.toEpochDay(), chronoLocalDate2.toEpochDay()));
        int compare = 0;
        DATE_TIME_ORDER = ((chronoLocalDateTime, chronoLocalDateTime2) -> {
            Long.compare(chronoLocalDateTime.toLocalDate().toEpochDay(), chronoLocalDateTime2.toLocalDate().toEpochDay());
            if (compare == 0) {
                compare = Long.compare(chronoLocalDateTime.toLocalTime().toNanoOfDay(), chronoLocalDateTime2.toLocalTime().toNanoOfDay());
            }
            return compare;
        });
        int compare2 = 0;
        INSTANT_ORDER = ((chronoZonedDateTime, chronoZonedDateTime2) -> {
            Long.compare(chronoZonedDateTime.toEpochSecond(), chronoZonedDateTime2.toEpochSecond());
            if (compare2 == 0) {
                compare2 = Long.compare(chronoZonedDateTime.toLocalTime().getNano(), chronoZonedDateTime2.toLocalTime().getNano());
            }
            return compare2;
        });
        CHRONOS_BY_ID = new ConcurrentHashMap<String, Chronology>();
        CHRONOS_BY_TYPE = new ConcurrentHashMap<String, Chronology>();
    }
}
