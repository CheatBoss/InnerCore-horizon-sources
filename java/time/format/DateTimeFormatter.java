package java.time.format;

import java.time.chrono.*;
import java.time.*;
import java.io.*;
import java.time.temporal.*;
import java.util.*;
import java.text.*;

public final class DateTimeFormatter
{
    private final DateTimeFormatterBuilder.CompositePrinterParser printerParser;
    private final Locale locale;
    private final DecimalStyle decimalStyle;
    private final ResolverStyle resolverStyle;
    private final Set<TemporalField> resolverFields;
    private final Chronology chrono;
    private final ZoneId zone;
    public static final DateTimeFormatter ISO_LOCAL_DATE;
    public static final DateTimeFormatter ISO_OFFSET_DATE;
    public static final DateTimeFormatter ISO_DATE;
    public static final DateTimeFormatter ISO_LOCAL_TIME;
    public static final DateTimeFormatter ISO_OFFSET_TIME;
    public static final DateTimeFormatter ISO_TIME;
    public static final DateTimeFormatter ISO_LOCAL_DATE_TIME;
    public static final DateTimeFormatter ISO_OFFSET_DATE_TIME;
    public static final DateTimeFormatter ISO_ZONED_DATE_TIME;
    public static final DateTimeFormatter ISO_DATE_TIME;
    public static final DateTimeFormatter ISO_ORDINAL_DATE;
    public static final DateTimeFormatter ISO_WEEK_DATE;
    public static final DateTimeFormatter ISO_INSTANT;
    public static final DateTimeFormatter BASIC_ISO_DATE;
    public static final DateTimeFormatter RFC_1123_DATE_TIME;
    private static final TemporalQuery<Period> PARSED_EXCESS_DAYS;
    private static final TemporalQuery<Boolean> PARSED_LEAP_SECOND;
    
    public static DateTimeFormatter ofPattern(final String s) {
        return new DateTimeFormatterBuilder().appendPattern(s).toFormatter();
    }
    
    public static DateTimeFormatter ofPattern(final String s, final Locale locale) {
        return new DateTimeFormatterBuilder().appendPattern(s).toFormatter(locale);
    }
    
    public static DateTimeFormatter ofLocalizedDate(final FormatStyle formatStyle) {
        Objects.requireNonNull(formatStyle, "dateStyle");
        return new DateTimeFormatterBuilder().appendLocalized(formatStyle, null).toFormatter(ResolverStyle.SMART, IsoChronology.INSTANCE);
    }
    
    public static DateTimeFormatter ofLocalizedTime(final FormatStyle formatStyle) {
        Objects.requireNonNull(formatStyle, "timeStyle");
        return new DateTimeFormatterBuilder().appendLocalized(null, formatStyle).toFormatter(ResolverStyle.SMART, IsoChronology.INSTANCE);
    }
    
    public static DateTimeFormatter ofLocalizedDateTime(final FormatStyle formatStyle) {
        Objects.requireNonNull(formatStyle, "dateTimeStyle");
        return new DateTimeFormatterBuilder().appendLocalized(formatStyle, formatStyle).toFormatter(ResolverStyle.SMART, IsoChronology.INSTANCE);
    }
    
    public static DateTimeFormatter ofLocalizedDateTime(final FormatStyle formatStyle, final FormatStyle formatStyle2) {
        Objects.requireNonNull(formatStyle, "dateStyle");
        Objects.requireNonNull(formatStyle2, "timeStyle");
        return new DateTimeFormatterBuilder().appendLocalized(formatStyle, formatStyle2).toFormatter(ResolverStyle.SMART, IsoChronology.INSTANCE);
    }
    
    public static final TemporalQuery<Period> parsedExcessDays() {
        return DateTimeFormatter.PARSED_EXCESS_DAYS;
    }
    
    public static final TemporalQuery<Boolean> parsedLeapSecond() {
        return DateTimeFormatter.PARSED_LEAP_SECOND;
    }
    
    DateTimeFormatter(final DateTimeFormatterBuilder.CompositePrinterParser compositePrinterParser, final Locale locale, final DecimalStyle decimalStyle, final ResolverStyle resolverStyle, final Set<TemporalField> resolverFields, final Chronology chrono, final ZoneId zone) {
        this.printerParser = Objects.requireNonNull(compositePrinterParser, "printerParser");
        this.resolverFields = resolverFields;
        this.locale = Objects.requireNonNull(locale, "locale");
        this.decimalStyle = Objects.requireNonNull(decimalStyle, "decimalStyle");
        this.resolverStyle = Objects.requireNonNull(resolverStyle, "resolverStyle");
        this.chrono = chrono;
        this.zone = zone;
    }
    
    public Locale getLocale() {
        return this.locale;
    }
    
    public DateTimeFormatter withLocale(final Locale locale) {
        if (this.locale.equals(locale)) {
            return this;
        }
        return new DateTimeFormatter(this.printerParser, locale, this.decimalStyle, this.resolverStyle, this.resolverFields, this.chrono, this.zone);
    }
    
    public DecimalStyle getDecimalStyle() {
        return this.decimalStyle;
    }
    
    public DateTimeFormatter withDecimalStyle(final DecimalStyle decimalStyle) {
        if (this.decimalStyle.equals(decimalStyle)) {
            return this;
        }
        return new DateTimeFormatter(this.printerParser, this.locale, decimalStyle, this.resolverStyle, this.resolverFields, this.chrono, this.zone);
    }
    
    public Chronology getChronology() {
        return this.chrono;
    }
    
    public DateTimeFormatter withChronology(final Chronology chronology) {
        if (Objects.equals(this.chrono, chronology)) {
            return this;
        }
        return new DateTimeFormatter(this.printerParser, this.locale, this.decimalStyle, this.resolverStyle, this.resolverFields, chronology, this.zone);
    }
    
    public ZoneId getZone() {
        return this.zone;
    }
    
    public DateTimeFormatter withZone(final ZoneId zoneId) {
        if (Objects.equals(this.zone, zoneId)) {
            return this;
        }
        return new DateTimeFormatter(this.printerParser, this.locale, this.decimalStyle, this.resolverStyle, this.resolverFields, this.chrono, zoneId);
    }
    
    public ResolverStyle getResolverStyle() {
        return this.resolverStyle;
    }
    
    public DateTimeFormatter withResolverStyle(final ResolverStyle resolverStyle) {
        Objects.requireNonNull(resolverStyle, "resolverStyle");
        if (Objects.equals(this.resolverStyle, resolverStyle)) {
            return this;
        }
        return new DateTimeFormatter(this.printerParser, this.locale, this.decimalStyle, resolverStyle, this.resolverFields, this.chrono, this.zone);
    }
    
    public Set<TemporalField> getResolverFields() {
        return this.resolverFields;
    }
    
    public DateTimeFormatter withResolverFields(final TemporalField... array) {
        Set<TemporalField> unmodifiableSet = null;
        if (array != null) {
            unmodifiableSet = Collections.unmodifiableSet((Set<? extends TemporalField>)new HashSet<TemporalField>(Arrays.asList(array)));
        }
        if (Objects.equals(this.resolverFields, unmodifiableSet)) {
            return this;
        }
        return new DateTimeFormatter(this.printerParser, this.locale, this.decimalStyle, this.resolverStyle, unmodifiableSet, this.chrono, this.zone);
    }
    
    public DateTimeFormatter withResolverFields(Set<TemporalField> unmodifiableSet) {
        if (Objects.equals(this.resolverFields, unmodifiableSet)) {
            return this;
        }
        if (unmodifiableSet != null) {
            unmodifiableSet = (Set<TemporalField>)Collections.unmodifiableSet((Set<?>)new HashSet<Object>(unmodifiableSet));
        }
        return new DateTimeFormatter(this.printerParser, this.locale, this.decimalStyle, this.resolverStyle, unmodifiableSet, this.chrono, this.zone);
    }
    
    public String format(final TemporalAccessor temporalAccessor) {
        final StringBuilder sb = new StringBuilder(32);
        this.formatTo(temporalAccessor, sb);
        return sb.toString();
    }
    
    public void formatTo(final TemporalAccessor temporalAccessor, final Appendable appendable) {
        Objects.requireNonNull(temporalAccessor, "temporal");
        Objects.requireNonNull(appendable, "appendable");
        try {
            final DateTimePrintContext dateTimePrintContext = new DateTimePrintContext(temporalAccessor, this);
            if (appendable instanceof StringBuilder) {
                this.printerParser.format(dateTimePrintContext, (StringBuilder)appendable);
            }
            else {
                final StringBuilder sb = new StringBuilder(32);
                this.printerParser.format(dateTimePrintContext, sb);
                appendable.append(sb);
            }
        }
        catch (IOException ex) {
            throw new DateTimeException(ex.getMessage(), ex);
        }
    }
    
    public TemporalAccessor parse(final CharSequence charSequence) {
        Objects.requireNonNull(charSequence, "text");
        try {
            return this.parseResolved0(charSequence, null);
        }
        catch (DateTimeParseException ex) {
            throw ex;
        }
        catch (RuntimeException ex2) {
            throw this.createError(charSequence, ex2);
        }
    }
    
    public TemporalAccessor parse(final CharSequence charSequence, final ParsePosition parsePosition) {
        Objects.requireNonNull(charSequence, "text");
        Objects.requireNonNull(parsePosition, "position");
        try {
            return this.parseResolved0(charSequence, parsePosition);
        }
        catch (DateTimeParseException | IndexOutOfBoundsException ex2) {
            throw;
        }
        catch (RuntimeException ex) {
            throw this.createError(charSequence, ex);
        }
    }
    
    public <T> T parse(final CharSequence charSequence, final TemporalQuery<T> temporalQuery) {
        Objects.requireNonNull(charSequence, "text");
        Objects.requireNonNull(temporalQuery, "query");
        try {
            return this.parseResolved0(charSequence, null).query(temporalQuery);
        }
        catch (DateTimeParseException ex) {
            throw ex;
        }
        catch (RuntimeException ex2) {
            throw this.createError(charSequence, ex2);
        }
    }
    
    public TemporalAccessor parseBest(final CharSequence charSequence, final TemporalQuery<?>... array) {
        Objects.requireNonNull(charSequence, "text");
        Objects.requireNonNull(array, "queries");
        if (array.length < 2) {
            throw new IllegalArgumentException("At least two queries must be specified");
        }
        try {
            final TemporalAccessor resolved0 = this.parseResolved0(charSequence, null);
            final int length = array.length;
            int i = 0;
            while (i < length) {
                final TemporalQuery<?> temporalQuery = array[i];
                try {
                    return resolved0.query(temporalQuery);
                }
                catch (RuntimeException ex3) {
                    ++i;
                    continue;
                }
                break;
            }
            throw new DateTimeException("Unable to convert parsed text using any of the specified queries");
        }
        catch (DateTimeParseException ex) {
            throw ex;
        }
        catch (RuntimeException ex2) {
            throw this.createError(charSequence, ex2);
        }
    }
    
    private DateTimeParseException createError(final CharSequence charSequence, final RuntimeException ex) {
        String s;
        if (charSequence.length() > 64) {
            s = charSequence.subSequence(0, 64).toString() + "...";
        }
        else {
            s = charSequence.toString();
        }
        return new DateTimeParseException("Text '" + s + "' could not be parsed: " + ex.getMessage(), charSequence, 0, ex);
    }
    
    private TemporalAccessor parseResolved0(final CharSequence charSequence, final ParsePosition parsePosition) {
        final ParsePosition parsePosition2 = (parsePosition != null) ? parsePosition : new ParsePosition(0);
        final DateTimeParseContext unresolved0 = this.parseUnresolved0(charSequence, parsePosition2);
        if (unresolved0 != null && parsePosition2.getErrorIndex() < 0 && (parsePosition != null || parsePosition2.getIndex() >= charSequence.length())) {
            return unresolved0.toResolved(this.resolverStyle, this.resolverFields);
        }
        String s;
        if (charSequence.length() > 64) {
            s = charSequence.subSequence(0, 64).toString() + "...";
        }
        else {
            s = charSequence.toString();
        }
        if (parsePosition2.getErrorIndex() >= 0) {
            throw new DateTimeParseException("Text '" + s + "' could not be parsed at index " + parsePosition2.getErrorIndex(), charSequence, parsePosition2.getErrorIndex());
        }
        throw new DateTimeParseException("Text '" + s + "' could not be parsed, unparsed text found at index " + parsePosition2.getIndex(), charSequence, parsePosition2.getIndex());
    }
    
    public TemporalAccessor parseUnresolved(final CharSequence charSequence, final ParsePosition parsePosition) {
        final DateTimeParseContext unresolved0 = this.parseUnresolved0(charSequence, parsePosition);
        if (unresolved0 == null) {
            return null;
        }
        return unresolved0.toUnresolved();
    }
    
    private DateTimeParseContext parseUnresolved0(final CharSequence charSequence, final ParsePosition parsePosition) {
        Objects.requireNonNull(charSequence, "text");
        Objects.requireNonNull(parsePosition, "position");
        final DateTimeParseContext dateTimeParseContext = new DateTimeParseContext(this);
        final int parse = this.printerParser.parse(dateTimeParseContext, charSequence, parsePosition.getIndex());
        if (parse < 0) {
            parsePosition.setErrorIndex(~parse);
            return null;
        }
        parsePosition.setIndex(parse);
        return dateTimeParseContext;
    }
    
    DateTimeFormatterBuilder.CompositePrinterParser toPrinterParser(final boolean b) {
        return this.printerParser.withOptional(b);
    }
    
    public Format toFormat() {
        return new ClassicFormat(this, null);
    }
    
    public Format toFormat(final TemporalQuery<?> temporalQuery) {
        Objects.requireNonNull(temporalQuery, "parseQuery");
        return new ClassicFormat(this, temporalQuery);
    }
    
    @Override
    public String toString() {
        final String string = this.printerParser.toString();
        return string.startsWith("[") ? string : string.substring(1, string.length() - 1);
    }
    
    static {
        ISO_LOCAL_DATE = new DateTimeFormatterBuilder().appendValue(ChronoField.YEAR, 4, 10, SignStyle.EXCEEDS_PAD).appendLiteral('-').appendValue(ChronoField.MONTH_OF_YEAR, 2).appendLiteral('-').appendValue(ChronoField.DAY_OF_MONTH, 2).toFormatter(ResolverStyle.STRICT, IsoChronology.INSTANCE);
        ISO_OFFSET_DATE = new DateTimeFormatterBuilder().parseCaseInsensitive().append(DateTimeFormatter.ISO_LOCAL_DATE).appendOffsetId().toFormatter(ResolverStyle.STRICT, IsoChronology.INSTANCE);
        ISO_DATE = new DateTimeFormatterBuilder().parseCaseInsensitive().append(DateTimeFormatter.ISO_LOCAL_DATE).optionalStart().appendOffsetId().toFormatter(ResolverStyle.STRICT, IsoChronology.INSTANCE);
        ISO_LOCAL_TIME = new DateTimeFormatterBuilder().appendValue(ChronoField.HOUR_OF_DAY, 2).appendLiteral(':').appendValue(ChronoField.MINUTE_OF_HOUR, 2).optionalStart().appendLiteral(':').appendValue(ChronoField.SECOND_OF_MINUTE, 2).optionalStart().appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true).toFormatter(ResolverStyle.STRICT, null);
        ISO_OFFSET_TIME = new DateTimeFormatterBuilder().parseCaseInsensitive().append(DateTimeFormatter.ISO_LOCAL_TIME).appendOffsetId().toFormatter(ResolverStyle.STRICT, null);
        ISO_TIME = new DateTimeFormatterBuilder().parseCaseInsensitive().append(DateTimeFormatter.ISO_LOCAL_TIME).optionalStart().appendOffsetId().toFormatter(ResolverStyle.STRICT, null);
        ISO_LOCAL_DATE_TIME = new DateTimeFormatterBuilder().parseCaseInsensitive().append(DateTimeFormatter.ISO_LOCAL_DATE).appendLiteral('T').append(DateTimeFormatter.ISO_LOCAL_TIME).toFormatter(ResolverStyle.STRICT, IsoChronology.INSTANCE);
        ISO_OFFSET_DATE_TIME = new DateTimeFormatterBuilder().parseCaseInsensitive().append(DateTimeFormatter.ISO_LOCAL_DATE_TIME).appendOffsetId().toFormatter(ResolverStyle.STRICT, IsoChronology.INSTANCE);
        ISO_ZONED_DATE_TIME = new DateTimeFormatterBuilder().append(DateTimeFormatter.ISO_OFFSET_DATE_TIME).optionalStart().appendLiteral('[').parseCaseSensitive().appendZoneRegionId().appendLiteral(']').toFormatter(ResolverStyle.STRICT, IsoChronology.INSTANCE);
        ISO_DATE_TIME = new DateTimeFormatterBuilder().append(DateTimeFormatter.ISO_LOCAL_DATE_TIME).optionalStart().appendOffsetId().optionalStart().appendLiteral('[').parseCaseSensitive().appendZoneRegionId().appendLiteral(']').toFormatter(ResolverStyle.STRICT, IsoChronology.INSTANCE);
        ISO_ORDINAL_DATE = new DateTimeFormatterBuilder().parseCaseInsensitive().appendValue(ChronoField.YEAR, 4, 10, SignStyle.EXCEEDS_PAD).appendLiteral('-').appendValue(ChronoField.DAY_OF_YEAR, 3).optionalStart().appendOffsetId().toFormatter(ResolverStyle.STRICT, IsoChronology.INSTANCE);
        ISO_WEEK_DATE = new DateTimeFormatterBuilder().parseCaseInsensitive().appendValue(IsoFields.WEEK_BASED_YEAR, 4, 10, SignStyle.EXCEEDS_PAD).appendLiteral("-W").appendValue(IsoFields.WEEK_OF_WEEK_BASED_YEAR, 2).appendLiteral('-').appendValue(ChronoField.DAY_OF_WEEK, 1).optionalStart().appendOffsetId().toFormatter(ResolverStyle.STRICT, IsoChronology.INSTANCE);
        ISO_INSTANT = new DateTimeFormatterBuilder().parseCaseInsensitive().appendInstant().toFormatter(ResolverStyle.STRICT, null);
        BASIC_ISO_DATE = new DateTimeFormatterBuilder().parseCaseInsensitive().appendValue(ChronoField.YEAR, 4).appendValue(ChronoField.MONTH_OF_YEAR, 2).appendValue(ChronoField.DAY_OF_MONTH, 2).optionalStart().appendOffset("+HHMMss", "Z").toFormatter(ResolverStyle.STRICT, IsoChronology.INSTANCE);
        final HashMap<Long, String> hashMap = new HashMap<Long, String>();
        hashMap.put(1L, "Mon");
        hashMap.put(2L, "Tue");
        hashMap.put(3L, "Wed");
        hashMap.put(4L, "Thu");
        hashMap.put(5L, "Fri");
        hashMap.put(6L, "Sat");
        hashMap.put(7L, "Sun");
        final HashMap<Long, String> hashMap2 = new HashMap<Long, String>();
        hashMap2.put(1L, "Jan");
        hashMap2.put(2L, "Feb");
        hashMap2.put(3L, "Mar");
        hashMap2.put(4L, "Apr");
        hashMap2.put(5L, "May");
        hashMap2.put(6L, "Jun");
        hashMap2.put(7L, "Jul");
        hashMap2.put(8L, "Aug");
        hashMap2.put(9L, "Sep");
        hashMap2.put(10L, "Oct");
        hashMap2.put(11L, "Nov");
        hashMap2.put(12L, "Dec");
        RFC_1123_DATE_TIME = new DateTimeFormatterBuilder().parseCaseInsensitive().parseLenient().optionalStart().appendText(ChronoField.DAY_OF_WEEK, hashMap).appendLiteral(", ").optionalEnd().appendValue(ChronoField.DAY_OF_MONTH, 1, 2, SignStyle.NOT_NEGATIVE).appendLiteral(' ').appendText(ChronoField.MONTH_OF_YEAR, hashMap2).appendLiteral(' ').appendValue(ChronoField.YEAR, 4).appendLiteral(' ').appendValue(ChronoField.HOUR_OF_DAY, 2).appendLiteral(':').appendValue(ChronoField.MINUTE_OF_HOUR, 2).optionalStart().appendLiteral(':').appendValue(ChronoField.SECOND_OF_MINUTE, 2).optionalEnd().appendLiteral(' ').appendOffset("+HHMM", "GMT").toFormatter(ResolverStyle.SMART, IsoChronology.INSTANCE);
        PARSED_EXCESS_DAYS = (parsed -> {
            if (parsed instanceof Parsed) {
                return parsed.excessDays;
            }
            else {
                return Period.ZERO;
            }
        });
        PARSED_LEAP_SECOND = (parsed2 -> {
            if (parsed2 instanceof Parsed) {
                return Boolean.valueOf(parsed2.leapSecond);
            }
            else {
                return Boolean.FALSE;
            }
        });
    }
    
    static class ClassicFormat extends Format
    {
        private final DateTimeFormatter formatter;
        private final TemporalQuery<?> parseType;
        
        public ClassicFormat(final DateTimeFormatter formatter, final TemporalQuery<?> parseType) {
            this.formatter = formatter;
            this.parseType = parseType;
        }
        
        @Override
        public StringBuffer format(final Object o, final StringBuffer sb, final FieldPosition fieldPosition) {
            Objects.requireNonNull(o, "obj");
            Objects.requireNonNull(sb, "toAppendTo");
            Objects.requireNonNull(fieldPosition, "pos");
            if (!(o instanceof TemporalAccessor)) {
                throw new IllegalArgumentException("Format target must implement TemporalAccessor");
            }
            fieldPosition.setBeginIndex(0);
            fieldPosition.setEndIndex(0);
            try {
                this.formatter.formatTo((TemporalAccessor)o, sb);
            }
            catch (RuntimeException ex) {
                throw new IllegalArgumentException(ex.getMessage(), ex);
            }
            return sb;
        }
        
        @Override
        public Object parseObject(final String s) throws ParseException {
            Objects.requireNonNull(s, "text");
            try {
                if (this.parseType == null) {
                    return this.formatter.parseResolved0(s, null);
                }
                return this.formatter.parse(s, this.parseType);
            }
            catch (DateTimeParseException ex) {
                throw new ParseException(ex.getMessage(), ex.getErrorIndex());
            }
            catch (RuntimeException ex2) {
                throw (ParseException)new ParseException(ex2.getMessage(), 0).initCause(ex2);
            }
        }
        
        @Override
        public Object parseObject(final String s, final ParsePosition parsePosition) {
            Objects.requireNonNull(s, "text");
            DateTimeParseContext access$100;
            try {
                access$100 = this.formatter.parseUnresolved0(s, parsePosition);
            }
            catch (IndexOutOfBoundsException ex) {
                if (parsePosition.getErrorIndex() < 0) {
                    parsePosition.setErrorIndex(0);
                }
                return null;
            }
            if (access$100 == null) {
                if (parsePosition.getErrorIndex() < 0) {
                    parsePosition.setErrorIndex(0);
                }
                return null;
            }
            try {
                final TemporalAccessor resolved = access$100.toResolved(this.formatter.resolverStyle, this.formatter.resolverFields);
                if (this.parseType == null) {
                    return resolved;
                }
                return resolved.query(this.parseType);
            }
            catch (RuntimeException ex2) {
                parsePosition.setErrorIndex(0);
                return null;
            }
        }
    }
}
