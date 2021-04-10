package java.time.chrono;

import sun.util.logging.*;
import java.time.*;
import java.time.temporal.*;
import java.time.format.*;
import java.security.*;
import java.util.*;
import sun.util.calendar.*;
import java.io.*;

public final class HijrahChronology extends AbstractChronology implements Serializable
{
    private final transient String typeId;
    private final transient String calendarType;
    private static final long serialVersionUID = 3127340209035924785L;
    public static final HijrahChronology INSTANCE;
    private transient volatile boolean initComplete;
    private transient int[] hijrahEpochMonthStartDays;
    private transient int minEpochDay;
    private transient int maxEpochDay;
    private transient int hijrahStartEpochMonth;
    private transient int minMonthLength;
    private transient int maxMonthLength;
    private transient int minYearLength;
    private transient int maxYearLength;
    private static final transient Properties calendarProperties;
    private static final String PROP_PREFIX = "calendar.hijrah.";
    private static final String PROP_TYPE_SUFFIX = ".type";
    private static final String KEY_ID = "id";
    private static final String KEY_TYPE = "type";
    private static final String KEY_VERSION = "version";
    private static final String KEY_ISO_START = "iso-start";
    
    private static void registerVariants() {
        for (final String s : HijrahChronology.calendarProperties.stringPropertyNames()) {
            if (s.startsWith("calendar.hijrah.")) {
                final String substring = s.substring("calendar.hijrah.".length());
                if (substring.indexOf(46) >= 0) {
                    continue;
                }
                if (substring.equals(HijrahChronology.INSTANCE.getId())) {
                    continue;
                }
                try {
                    AbstractChronology.registerChrono(new HijrahChronology(substring));
                }
                catch (DateTimeException ex) {
                    PlatformLogger.getLogger("java.time.chrono").severe("Unable to initialize Hijrah calendar: " + substring, ex);
                }
            }
        }
    }
    
    private HijrahChronology(final String typeId) throws DateTimeException {
        if (typeId.isEmpty()) {
            throw new IllegalArgumentException("calendar id is empty");
        }
        final String string = "calendar.hijrah." + typeId + ".type";
        final String property = HijrahChronology.calendarProperties.getProperty(string);
        if (property == null || property.isEmpty()) {
            throw new DateTimeException("calendarType is missing or empty for: " + string);
        }
        this.typeId = typeId;
        this.calendarType = property;
    }
    
    private void checkCalendarInit() {
        if (!this.initComplete) {
            this.loadCalendarData();
            this.initComplete = true;
        }
    }
    
    @Override
    public String getId() {
        return this.typeId;
    }
    
    @Override
    public String getCalendarType() {
        return this.calendarType;
    }
    
    @Override
    public HijrahDate date(final Era era, final int n, final int n2, final int n3) {
        return this.date(this.prolepticYear(era, n), n2, n3);
    }
    
    @Override
    public HijrahDate date(final int n, final int n2, final int n3) {
        return HijrahDate.of(this, n, n2, n3);
    }
    
    @Override
    public HijrahDate dateYearDay(final Era era, final int n, final int n2) {
        return this.dateYearDay(this.prolepticYear(era, n), n2);
    }
    
    @Override
    public HijrahDate dateYearDay(final int n, final int n2) {
        final HijrahDate of = HijrahDate.of(this, n, 1, 1);
        if (n2 > of.lengthOfYear()) {
            throw new DateTimeException("Invalid dayOfYear: " + n2);
        }
        return of.plusDays(n2 - 1);
    }
    
    @Override
    public HijrahDate dateEpochDay(final long n) {
        return HijrahDate.ofEpochDay(this, n);
    }
    
    @Override
    public HijrahDate dateNow() {
        return this.dateNow(Clock.systemDefaultZone());
    }
    
    @Override
    public HijrahDate dateNow(final ZoneId zoneId) {
        return this.dateNow(Clock.system(zoneId));
    }
    
    @Override
    public HijrahDate dateNow(final Clock clock) {
        return this.date(LocalDate.now(clock));
    }
    
    @Override
    public HijrahDate date(final TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof HijrahDate) {
            return (HijrahDate)temporalAccessor;
        }
        return HijrahDate.ofEpochDay(this, temporalAccessor.getLong(ChronoField.EPOCH_DAY));
    }
    
    @Override
    public ChronoLocalDateTime<HijrahDate> localDateTime(final TemporalAccessor temporalAccessor) {
        return (ChronoLocalDateTime<HijrahDate>)super.localDateTime(temporalAccessor);
    }
    
    @Override
    public ChronoZonedDateTime<HijrahDate> zonedDateTime(final TemporalAccessor temporalAccessor) {
        return (ChronoZonedDateTime<HijrahDate>)super.zonedDateTime(temporalAccessor);
    }
    
    @Override
    public ChronoZonedDateTime<HijrahDate> zonedDateTime(final Instant instant, final ZoneId zoneId) {
        return (ChronoZonedDateTime<HijrahDate>)super.zonedDateTime(instant, zoneId);
    }
    
    @Override
    public boolean isLeapYear(final long n) {
        this.checkCalendarInit();
        return n >= this.getMinimumYear() && n <= this.getMaximumYear() && this.getYearLength((int)n) > 354;
    }
    
    @Override
    public int prolepticYear(final Era era, final int n) {
        if (!(era instanceof HijrahEra)) {
            throw new ClassCastException("Era must be HijrahEra");
        }
        return n;
    }
    
    @Override
    public HijrahEra eraOf(final int n) {
        switch (n) {
            case 1: {
                return HijrahEra.AH;
            }
            default: {
                throw new DateTimeException("invalid Hijrah era");
            }
        }
    }
    
    @Override
    public List<Era> eras() {
        return (List<Era>)Arrays.asList(HijrahEra.values());
    }
    
    @Override
    public ValueRange range(final ChronoField chronoField) {
        this.checkCalendarInit();
        if (!(chronoField instanceof ChronoField)) {
            return chronoField.range();
        }
        switch (chronoField) {
            case DAY_OF_MONTH: {
                return ValueRange.of(1L, 1L, this.getMinimumMonthLength(), this.getMaximumMonthLength());
            }
            case DAY_OF_YEAR: {
                return ValueRange.of(1L, this.getMaximumDayOfYear());
            }
            case ALIGNED_WEEK_OF_MONTH: {
                return ValueRange.of(1L, 5L);
            }
            case YEAR:
            case YEAR_OF_ERA: {
                return ValueRange.of(this.getMinimumYear(), this.getMaximumYear());
            }
            case ERA: {
                return ValueRange.of(1L, 1L);
            }
            default: {
                return chronoField.range();
            }
        }
    }
    
    @Override
    public HijrahDate resolveDate(final Map<TemporalField, Long> map, final ResolverStyle resolverStyle) {
        return (HijrahDate)super.resolveDate(map, resolverStyle);
    }
    
    int checkValidYear(final long n) {
        if (n < this.getMinimumYear() || n > this.getMaximumYear()) {
            throw new DateTimeException("Invalid Hijrah year: " + n);
        }
        return (int)n;
    }
    
    void checkValidDayOfYear(final int n) {
        if (n < 1 || n > this.getMaximumDayOfYear()) {
            throw new DateTimeException("Invalid Hijrah day of year: " + n);
        }
    }
    
    void checkValidMonth(final int n) {
        if (n < 1 || n > 12) {
            throw new DateTimeException("Invalid Hijrah month: " + n);
        }
    }
    
    int[] getHijrahDateInfo(final int n) {
        this.checkCalendarInit();
        if (n < this.minEpochDay || n >= this.maxEpochDay) {
            throw new DateTimeException("Hijrah date out of range");
        }
        final int epochDayToEpochMonth = this.epochDayToEpochMonth(n);
        return new int[] { this.epochMonthToYear(epochDayToEpochMonth), this.epochMonthToMonth(epochDayToEpochMonth) + 1, n - this.epochMonthToEpochDay(epochDayToEpochMonth) + 1 };
    }
    
    long getEpochDay(final int n, final int n2, final int n3) {
        this.checkCalendarInit();
        this.checkValidMonth(n2);
        final int n4 = this.yearToEpochMonth(n) + (n2 - 1);
        if (n4 < 0 || n4 >= this.hijrahEpochMonthStartDays.length) {
            throw new DateTimeException("Invalid Hijrah date, year: " + n + ", month: " + n2);
        }
        if (n3 < 1 || n3 > this.getMonthLength(n, n2)) {
            throw new DateTimeException("Invalid Hijrah day of month: " + n3);
        }
        return this.epochMonthToEpochDay(n4) + (n3 - 1);
    }
    
    int getDayOfYear(final int n, final int n2) {
        return this.yearMonthToDayOfYear(n, n2 - 1);
    }
    
    int getMonthLength(final int n, final int n2) {
        final int n3 = this.yearToEpochMonth(n) + (n2 - 1);
        if (n3 < 0 || n3 >= this.hijrahEpochMonthStartDays.length) {
            throw new DateTimeException("Invalid Hijrah date, year: " + n + ", month: " + n2);
        }
        return this.epochMonthLength(n3);
    }
    
    int getYearLength(final int n) {
        return this.yearMonthToDayOfYear(n, 12);
    }
    
    int getMinimumYear() {
        return this.epochMonthToYear(0);
    }
    
    int getMaximumYear() {
        return this.epochMonthToYear(this.hijrahEpochMonthStartDays.length - 1) - 1;
    }
    
    int getMaximumMonthLength() {
        return this.maxMonthLength;
    }
    
    int getMinimumMonthLength() {
        return this.minMonthLength;
    }
    
    int getMaximumDayOfYear() {
        return this.maxYearLength;
    }
    
    int getSmallestMaximumDayOfYear() {
        return this.minYearLength;
    }
    
    private int epochDayToEpochMonth(final int n) {
        int binarySearch = Arrays.binarySearch(this.hijrahEpochMonthStartDays, n);
        if (binarySearch < 0) {
            binarySearch = -binarySearch - 2;
        }
        return binarySearch;
    }
    
    private int epochMonthToYear(final int n) {
        return (n + this.hijrahStartEpochMonth) / 12;
    }
    
    private int yearToEpochMonth(final int n) {
        return n * 12 - this.hijrahStartEpochMonth;
    }
    
    private int epochMonthToMonth(final int n) {
        return (n + this.hijrahStartEpochMonth) % 12;
    }
    
    private int epochMonthToEpochDay(final int n) {
        return this.hijrahEpochMonthStartDays[n];
    }
    
    private int yearMonthToDayOfYear(final int n, final int n2) {
        final int yearToEpochMonth = this.yearToEpochMonth(n);
        return this.epochMonthToEpochDay(yearToEpochMonth + n2) - this.epochMonthToEpochDay(yearToEpochMonth);
    }
    
    private int epochMonthLength(final int n) {
        return this.hijrahEpochMonthStartDays[n + 1] - this.hijrahEpochMonthStartDays[n];
    }
    
    private static Properties readConfigProperties(final String s) throws Exception {
        try {
            final File file;
            final Properties properties;
            final FileInputStream fileInputStream;
            final Throwable t2;
            return AccessController.doPrivileged(() -> {
                file = new File(System.getProperty("java.home") + File.separator + "lib", s);
                properties = new Properties();
                fileInputStream = new FileInputStream(file);
                try {
                    properties.load(fileInputStream);
                }
                catch (Throwable t) {
                    throw t;
                }
                finally {
                    if (fileInputStream != null) {
                        if (t2 != null) {
                            try {
                                fileInputStream.close();
                            }
                            catch (Throwable t3) {
                                t2.addSuppressed(t3);
                            }
                        }
                        else {
                            fileInputStream.close();
                        }
                    }
                }
                return properties;
            });
        }
        catch (PrivilegedActionException ex) {
            throw ex.getException();
        }
    }
    
    private void loadCalendarData() {
        try {
            final String property = HijrahChronology.calendarProperties.getProperty("calendar.hijrah." + this.typeId);
            Objects.requireNonNull(property, "Resource missing for calendar: calendar.hijrah." + this.typeId);
            final Properties configProperties = readConfigProperties(property);
            final HashMap<Integer, int[]> hashMap = new HashMap<Integer, int[]>();
            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;
            String s = null;
            String s2 = null;
            String s3 = null;
            int minEpochDay = 0;
            for (final Map.Entry<Object, Object> entry : configProperties.entrySet()) {
                final String s5;
                final String s4 = s5 = entry.getKey();
                switch (s5) {
                    case "id": {
                        s = entry.getValue();
                        continue;
                    }
                    case "type": {
                        s2 = entry.getValue();
                        continue;
                    }
                    case "version": {
                        s3 = entry.getValue();
                        continue;
                    }
                    case "iso-start": {
                        final int[] ymd = this.parseYMD(entry.getValue());
                        minEpochDay = (int)LocalDate.of(ymd[0], ymd[1], ymd[2]).toEpochDay();
                        continue;
                    }
                    default: {
                        try {
                            final int intValue = Integer.valueOf(s4);
                            hashMap.put(intValue, this.parseMonths(entry.getValue()));
                            max = Math.max(max, intValue);
                            min = Math.min(min, intValue);
                        }
                        catch (NumberFormatException ex2) {
                            throw new IllegalArgumentException("bad key: " + s4);
                        }
                        continue;
                    }
                }
            }
            if (!this.getId().equals(s)) {
                throw new IllegalArgumentException("Configuration is for a different calendar: " + s);
            }
            if (!this.getCalendarType().equals(s2)) {
                throw new IllegalArgumentException("Configuration is for a different calendar type: " + s2);
            }
            if (s3 == null || s3.isEmpty()) {
                throw new IllegalArgumentException("Configuration does not contain a version");
            }
            if (minEpochDay == 0) {
                throw new IllegalArgumentException("Configuration does not contain a ISO start date");
            }
            this.hijrahStartEpochMonth = min * 12;
            this.minEpochDay = minEpochDay;
            this.hijrahEpochMonthStartDays = this.createEpochMonths(this.minEpochDay, min, max, hashMap);
            this.maxEpochDay = this.hijrahEpochMonthStartDays[this.hijrahEpochMonthStartDays.length - 1];
            for (int i = min; i < max; ++i) {
                final int yearLength = this.getYearLength(i);
                this.minYearLength = Math.min(this.minYearLength, yearLength);
                this.maxYearLength = Math.max(this.maxYearLength, yearLength);
            }
        }
        catch (Exception ex) {
            PlatformLogger.getLogger("java.time.chrono").severe("Unable to initialize Hijrah calendar proxy: " + this.typeId, ex);
            throw new DateTimeException("Unable to initialize HijrahCalendar: " + this.typeId, ex);
        }
    }
    
    private int[] createEpochMonths(int n, final int n2, final int n3, final Map<Integer, int[]> map) {
        final int n4 = (n3 - n2 + 1) * 12 + 1;
        int n5 = 0;
        final int[] array = new int[n4];
        this.minMonthLength = Integer.MAX_VALUE;
        this.maxMonthLength = Integer.MIN_VALUE;
        for (int i = n2; i <= n3; ++i) {
            final int[] array2 = map.get(i);
            for (int j = 0; j < 12; ++j) {
                final int n6 = array2[j];
                array[n5++] = n;
                if (n6 < 29 || n6 > 32) {
                    throw new IllegalArgumentException("Invalid month length in year: " + n2);
                }
                n += n6;
                this.minMonthLength = Math.min(this.minMonthLength, n6);
                this.maxMonthLength = Math.max(this.maxMonthLength, n6);
            }
        }
        array[n5++] = n;
        if (n5 != array.length) {
            throw new IllegalStateException("Did not fill epochMonths exactly: ndx = " + n5 + " should be " + array.length);
        }
        return array;
    }
    
    private int[] parseMonths(final String s) {
        final int[] array = new int[12];
        final String[] split = s.split("\\s");
        if (split.length != 12) {
            throw new IllegalArgumentException("wrong number of months on line: " + Arrays.toString(split) + "; count: " + split.length);
        }
        for (int i = 0; i < 12; ++i) {
            try {
                array[i] = Integer.valueOf(split[i]);
            }
            catch (NumberFormatException ex) {
                throw new IllegalArgumentException("bad key: " + split[i]);
            }
        }
        return array;
    }
    
    private int[] parseYMD(String trim) {
        trim = trim.trim();
        try {
            if (trim.charAt(4) != '-' || trim.charAt(7) != '-') {
                throw new IllegalArgumentException("date must be yyyy-MM-dd");
            }
            return new int[] { Integer.valueOf(trim.substring(0, 4)), Integer.valueOf(trim.substring(5, 7)), Integer.valueOf(trim.substring(8, 10)) };
        }
        catch (NumberFormatException ex) {
            throw new IllegalArgumentException("date must be yyyy-MM-dd", ex);
        }
    }
    
    @Override
    Object writeReplace() {
        return super.writeReplace();
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
    
    static {
        try {
            calendarProperties = CalendarSystem.getCalendarProperties();
        }
        catch (IOException ex) {
            throw new InternalError("Can't initialize lib/calendars.properties", ex);
        }
        try {
            AbstractChronology.registerChrono(INSTANCE = new HijrahChronology("Hijrah-umalqura"), "Hijrah");
            AbstractChronology.registerChrono(HijrahChronology.INSTANCE, "islamic");
        }
        catch (DateTimeException ex2) {
            PlatformLogger.getLogger("java.time.chrono").severe("Unable to initialize Hijrah calendar: Hijrah-umalqura", ex2);
            throw new RuntimeException("Unable to initialize Hijrah-umalqura calendar", ex2.getCause());
        }
        registerVariants();
    }
}
