package java.time.format;

import java.util.concurrent.*;
import java.math.*;
import java.text.*;
import java.time.chrono.*;
import java.time.temporal.*;
import java.time.zone.*;
import java.lang.ref.*;
import sun.util.locale.provider.*;
import java.util.*;
import java.time.*;

public final class DateTimeFormatterBuilder
{
    private static final TemporalQuery<ZoneId> QUERY_REGION_ONLY;
    private DateTimeFormatterBuilder active;
    private final DateTimeFormatterBuilder parent;
    private final List<DateTimePrinterParser> printerParsers;
    private final boolean optional;
    private int padNextWidth;
    private char padNextChar;
    private int valueParserIndex;
    private static final Map<Character, TemporalField> FIELD_MAP;
    static final Comparator<String> LENGTH_SORT;
    
    public static String getLocalizedDateTimePattern(final FormatStyle formatStyle, final FormatStyle formatStyle2, final Chronology chronology, final Locale locale) {
        Objects.requireNonNull(locale, "locale");
        Objects.requireNonNull(chronology, "chrono");
        if (formatStyle == null && formatStyle2 == null) {
            throw new IllegalArgumentException("Either dateStyle or timeStyle must be non-null");
        }
        return LocaleProviderAdapter.getResourceBundleBased().getLocaleResources(locale).getJavaTimeDateTimePattern(convertStyle(formatStyle2), convertStyle(formatStyle), chronology.getCalendarType());
    }
    
    private static int convertStyle(final FormatStyle formatStyle) {
        if (formatStyle == null) {
            return -1;
        }
        return formatStyle.ordinal();
    }
    
    public DateTimeFormatterBuilder() {
        this.active = this;
        this.printerParsers = new ArrayList<DateTimePrinterParser>();
        this.valueParserIndex = -1;
        this.parent = null;
        this.optional = false;
    }
    
    private DateTimeFormatterBuilder(final DateTimeFormatterBuilder parent, final boolean optional) {
        this.active = this;
        this.printerParsers = new ArrayList<DateTimePrinterParser>();
        this.valueParserIndex = -1;
        this.parent = parent;
        this.optional = optional;
    }
    
    public DateTimeFormatterBuilder parseCaseSensitive() {
        this.appendInternal(SettingsParser.SENSITIVE);
        return this;
    }
    
    public DateTimeFormatterBuilder parseCaseInsensitive() {
        this.appendInternal(SettingsParser.INSENSITIVE);
        return this;
    }
    
    public DateTimeFormatterBuilder parseStrict() {
        this.appendInternal(SettingsParser.STRICT);
        return this;
    }
    
    public DateTimeFormatterBuilder parseLenient() {
        this.appendInternal(SettingsParser.LENIENT);
        return this;
    }
    
    public DateTimeFormatterBuilder parseDefaulting(final TemporalField temporalField, final long n) {
        Objects.requireNonNull(temporalField, "field");
        this.appendInternal(new DefaultValueParser(temporalField, n));
        return this;
    }
    
    public DateTimeFormatterBuilder appendValue(final TemporalField temporalField) {
        Objects.requireNonNull(temporalField, "field");
        this.appendValue(new NumberPrinterParser(temporalField, 1, 19, SignStyle.NORMAL));
        return this;
    }
    
    public DateTimeFormatterBuilder appendValue(final TemporalField temporalField, final int n) {
        Objects.requireNonNull(temporalField, "field");
        if (n < 1 || n > 19) {
            throw new IllegalArgumentException("The width must be from 1 to 19 inclusive but was " + n);
        }
        this.appendValue(new NumberPrinterParser(temporalField, n, n, SignStyle.NOT_NEGATIVE));
        return this;
    }
    
    public DateTimeFormatterBuilder appendValue(final TemporalField temporalField, final int n, final int n2, final SignStyle signStyle) {
        if (n == n2 && signStyle == SignStyle.NOT_NEGATIVE) {
            return this.appendValue(temporalField, n2);
        }
        Objects.requireNonNull(temporalField, "field");
        Objects.requireNonNull(signStyle, "signStyle");
        if (n < 1 || n > 19) {
            throw new IllegalArgumentException("The minimum width must be from 1 to 19 inclusive but was " + n);
        }
        if (n2 < 1 || n2 > 19) {
            throw new IllegalArgumentException("The maximum width must be from 1 to 19 inclusive but was " + n2);
        }
        if (n2 < n) {
            throw new IllegalArgumentException("The maximum width must exceed or equal the minimum width but " + n2 + " < " + n);
        }
        this.appendValue(new NumberPrinterParser(temporalField, n, n2, signStyle));
        return this;
    }
    
    public DateTimeFormatterBuilder appendValueReduced(final TemporalField temporalField, final int n, final int n2, final int n3) {
        Objects.requireNonNull(temporalField, "field");
        this.appendValue(new ReducedPrinterParser(temporalField, n, n2, n3, null));
        return this;
    }
    
    public DateTimeFormatterBuilder appendValueReduced(final TemporalField temporalField, final int n, final int n2, final ChronoLocalDate chronoLocalDate) {
        Objects.requireNonNull(temporalField, "field");
        Objects.requireNonNull(chronoLocalDate, "baseDate");
        this.appendValue(new ReducedPrinterParser(temporalField, n, n2, 0, chronoLocalDate));
        return this;
    }
    
    private DateTimeFormatterBuilder appendValue(final NumberPrinterParser numberPrinterParser) {
        if (this.active.valueParserIndex >= 0) {
            final int valueParserIndex = this.active.valueParserIndex;
            final NumberPrinterParser numberPrinterParser2 = this.active.printerParsers.get(valueParserIndex);
            NumberPrinterParser numberPrinterParser3;
            if (numberPrinterParser.minWidth == numberPrinterParser.maxWidth && numberPrinterParser.signStyle == SignStyle.NOT_NEGATIVE) {
                numberPrinterParser3 = numberPrinterParser2.withSubsequentWidth(numberPrinterParser.maxWidth);
                this.appendInternal(numberPrinterParser.withFixedWidth());
                this.active.valueParserIndex = valueParserIndex;
            }
            else {
                numberPrinterParser3 = numberPrinterParser2.withFixedWidth();
                this.active.valueParserIndex = this.appendInternal(numberPrinterParser);
            }
            this.active.printerParsers.set(valueParserIndex, numberPrinterParser3);
        }
        else {
            this.active.valueParserIndex = this.appendInternal(numberPrinterParser);
        }
        return this;
    }
    
    public DateTimeFormatterBuilder appendFraction(final TemporalField temporalField, final int n, final int n2, final boolean b) {
        this.appendInternal(new FractionPrinterParser(temporalField, n, n2, b));
        return this;
    }
    
    public DateTimeFormatterBuilder appendText(final TemporalField temporalField) {
        return this.appendText(temporalField, TextStyle.FULL);
    }
    
    public DateTimeFormatterBuilder appendText(final TemporalField temporalField, final TextStyle textStyle) {
        Objects.requireNonNull(temporalField, "field");
        Objects.requireNonNull(textStyle, "textStyle");
        this.appendInternal(new TextPrinterParser(temporalField, textStyle, DateTimeTextProvider.getInstance()));
        return this;
    }
    
    public DateTimeFormatterBuilder appendText(final TemporalField temporalField, final Map<Long, String> map) {
        Objects.requireNonNull(temporalField, "field");
        Objects.requireNonNull(map, "textLookup");
        this.appendInternal(new TextPrinterParser(temporalField, TextStyle.FULL, new DateTimeTextProvider() {
            final /* synthetic */ LocaleStore val$store = new DateTimeTextProvider.LocaleStore((Map<TextStyle, Map<Long, String>>)Collections.singletonMap(TextStyle.FULL, new LinkedHashMap(map)));
            
            @Override
            public String getText(final Chronology chronology, final TemporalField temporalField, final long n, final TextStyle textStyle, final Locale locale) {
                return this.val$store.getText(n, textStyle);
            }
            
            @Override
            public String getText(final TemporalField temporalField, final long n, final TextStyle textStyle, final Locale locale) {
                return this.val$store.getText(n, textStyle);
            }
            
            @Override
            public Iterator<Map.Entry<String, Long>> getTextIterator(final Chronology chronology, final TemporalField temporalField, final TextStyle textStyle, final Locale locale) {
                return this.val$store.getTextIterator(textStyle);
            }
            
            @Override
            public Iterator<Map.Entry<String, Long>> getTextIterator(final TemporalField temporalField, final TextStyle textStyle, final Locale locale) {
                return this.val$store.getTextIterator(textStyle);
            }
        }));
        return this;
    }
    
    public DateTimeFormatterBuilder appendInstant() {
        this.appendInternal(new InstantPrinterParser(-2));
        return this;
    }
    
    public DateTimeFormatterBuilder appendInstant(final int n) {
        if (n < -1 || n > 9) {
            throw new IllegalArgumentException("The fractional digits must be from -1 to 9 inclusive but was " + n);
        }
        this.appendInternal(new InstantPrinterParser(n));
        return this;
    }
    
    public DateTimeFormatterBuilder appendOffsetId() {
        this.appendInternal(OffsetIdPrinterParser.INSTANCE_ID_Z);
        return this;
    }
    
    public DateTimeFormatterBuilder appendOffset(final String s, final String s2) {
        this.appendInternal(new OffsetIdPrinterParser(s, s2));
        return this;
    }
    
    public DateTimeFormatterBuilder appendLocalizedOffset(final TextStyle textStyle) {
        Objects.requireNonNull(textStyle, "style");
        if (textStyle != TextStyle.FULL && textStyle != TextStyle.SHORT) {
            throw new IllegalArgumentException("Style must be either full or short");
        }
        this.appendInternal(new LocalizedOffsetIdPrinterParser(textStyle));
        return this;
    }
    
    public DateTimeFormatterBuilder appendZoneId() {
        this.appendInternal(new ZoneIdPrinterParser(TemporalQueries.zoneId(), "ZoneId()"));
        return this;
    }
    
    public DateTimeFormatterBuilder appendZoneRegionId() {
        this.appendInternal(new ZoneIdPrinterParser(DateTimeFormatterBuilder.QUERY_REGION_ONLY, "ZoneRegionId()"));
        return this;
    }
    
    public DateTimeFormatterBuilder appendZoneOrOffsetId() {
        this.appendInternal(new ZoneIdPrinterParser(TemporalQueries.zone(), "ZoneOrOffsetId()"));
        return this;
    }
    
    public DateTimeFormatterBuilder appendZoneText(final TextStyle textStyle) {
        this.appendInternal(new ZoneTextPrinterParser(textStyle, null));
        return this;
    }
    
    public DateTimeFormatterBuilder appendZoneText(final TextStyle textStyle, final Set<ZoneId> set) {
        Objects.requireNonNull(set, "preferredZones");
        this.appendInternal(new ZoneTextPrinterParser(textStyle, set));
        return this;
    }
    
    public DateTimeFormatterBuilder appendChronologyId() {
        this.appendInternal(new ChronoPrinterParser(null));
        return this;
    }
    
    public DateTimeFormatterBuilder appendChronologyText(final TextStyle textStyle) {
        Objects.requireNonNull(textStyle, "textStyle");
        this.appendInternal(new ChronoPrinterParser(textStyle));
        return this;
    }
    
    public DateTimeFormatterBuilder appendLocalized(final FormatStyle formatStyle, final FormatStyle formatStyle2) {
        if (formatStyle == null && formatStyle2 == null) {
            throw new IllegalArgumentException("Either the date or time style must be non-null");
        }
        this.appendInternal(new LocalizedPrinterParser(formatStyle, formatStyle2));
        return this;
    }
    
    public DateTimeFormatterBuilder appendLiteral(final char c) {
        this.appendInternal(new CharLiteralPrinterParser(c));
        return this;
    }
    
    public DateTimeFormatterBuilder appendLiteral(final String s) {
        Objects.requireNonNull(s, "literal");
        if (s.length() > 0) {
            if (s.length() == 1) {
                this.appendInternal(new CharLiteralPrinterParser(s.charAt(0)));
            }
            else {
                this.appendInternal(new StringLiteralPrinterParser(s));
            }
        }
        return this;
    }
    
    public DateTimeFormatterBuilder append(final DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        this.appendInternal(dateTimeFormatter.toPrinterParser(false));
        return this;
    }
    
    public DateTimeFormatterBuilder appendOptional(final DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        this.appendInternal(dateTimeFormatter.toPrinterParser(true));
        return this;
    }
    
    public DateTimeFormatterBuilder appendPattern(final String s) {
        Objects.requireNonNull(s, "pattern");
        this.parsePattern(s);
        return this;
    }
    
    private void parsePattern(final String s) {
        for (int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
                int n;
                for (n = i++; i < s.length() && s.charAt(i) == c; ++i) {}
                int n2 = i - n;
                if (c == 'p') {
                    int n3 = 0;
                    if (i < s.length()) {
                        c = s.charAt(i);
                        if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
                            n3 = n2;
                            int n4;
                            for (n4 = i++; i < s.length() && s.charAt(i) == c; ++i) {}
                            n2 = i - n4;
                        }
                    }
                    if (n3 == 0) {
                        throw new IllegalArgumentException("Pad letter 'p' must be followed by valid pad pattern: " + s);
                    }
                    this.padNext(n3);
                }
                final TemporalField temporalField = DateTimeFormatterBuilder.FIELD_MAP.get(c);
                if (temporalField != null) {
                    this.parseField(c, n2, temporalField);
                }
                else if (c == 'z') {
                    if (n2 > 4) {
                        throw new IllegalArgumentException("Too many pattern letters: " + c);
                    }
                    if (n2 == 4) {
                        this.appendZoneText(TextStyle.FULL);
                    }
                    else {
                        this.appendZoneText(TextStyle.SHORT);
                    }
                }
                else if (c == 'V') {
                    if (n2 != 2) {
                        throw new IllegalArgumentException("Pattern letter count must be 2: " + c);
                    }
                    this.appendZoneId();
                }
                else if (c == 'Z') {
                    if (n2 < 4) {
                        this.appendOffset("+HHMM", "+0000");
                    }
                    else if (n2 == 4) {
                        this.appendLocalizedOffset(TextStyle.FULL);
                    }
                    else {
                        if (n2 != 5) {
                            throw new IllegalArgumentException("Too many pattern letters: " + c);
                        }
                        this.appendOffset("+HH:MM:ss", "Z");
                    }
                }
                else if (c == 'O') {
                    if (n2 == 1) {
                        this.appendLocalizedOffset(TextStyle.SHORT);
                    }
                    else {
                        if (n2 != 4) {
                            throw new IllegalArgumentException("Pattern letter count must be 1 or 4: " + c);
                        }
                        this.appendLocalizedOffset(TextStyle.FULL);
                    }
                }
                else if (c == 'X') {
                    if (n2 > 5) {
                        throw new IllegalArgumentException("Too many pattern letters: " + c);
                    }
                    this.appendOffset(OffsetIdPrinterParser.PATTERNS[n2 + ((n2 != 1) ? 1 : 0)], "Z");
                }
                else if (c == 'x') {
                    if (n2 > 5) {
                        throw new IllegalArgumentException("Too many pattern letters: " + c);
                    }
                    this.appendOffset(OffsetIdPrinterParser.PATTERNS[n2 + ((n2 != 1) ? 1 : 0)], (n2 == 1) ? "+00" : ((n2 % 2 == 0) ? "+0000" : "+00:00"));
                }
                else if (c == 'W') {
                    if (n2 > 1) {
                        throw new IllegalArgumentException("Too many pattern letters: " + c);
                    }
                    this.appendInternal(new WeekBasedFieldPrinterParser(c, n2));
                }
                else if (c == 'w') {
                    if (n2 > 2) {
                        throw new IllegalArgumentException("Too many pattern letters: " + c);
                    }
                    this.appendInternal(new WeekBasedFieldPrinterParser(c, n2));
                }
                else {
                    if (c != 'Y') {
                        throw new IllegalArgumentException("Unknown pattern letter: " + c);
                    }
                    this.appendInternal(new WeekBasedFieldPrinterParser(c, n2));
                }
                --i;
            }
            else if (c == '\'') {
                int n5;
                for (n5 = i++; i < s.length(); ++i) {
                    if (s.charAt(i) == '\'') {
                        if (i + 1 >= s.length() || s.charAt(i + 1) != '\'') {
                            break;
                        }
                        ++i;
                    }
                }
                if (i >= s.length()) {
                    throw new IllegalArgumentException("Pattern ends with an incomplete string literal: " + s);
                }
                final String substring = s.substring(n5 + 1, i);
                if (substring.length() == 0) {
                    this.appendLiteral('\'');
                }
                else {
                    this.appendLiteral(substring.replace("''", "'"));
                }
            }
            else if (c == '[') {
                this.optionalStart();
            }
            else if (c == ']') {
                if (this.active.parent == null) {
                    throw new IllegalArgumentException("Pattern invalid as it contains ] without previous [");
                }
                this.optionalEnd();
            }
            else {
                if (c == '{' || c == '}' || c == '#') {
                    throw new IllegalArgumentException("Pattern includes reserved character: '" + c + "'");
                }
                this.appendLiteral(c);
            }
        }
    }
    
    private void parseField(final char c, final int n, final TemporalField temporalField) {
        boolean b = false;
        Label_0843: {
            switch (c) {
                case 'u':
                case 'y': {
                    if (n == 2) {
                        this.appendValueReduced(temporalField, 2, 2, ReducedPrinterParser.BASE_DATE);
                        break;
                    }
                    if (n < 4) {
                        this.appendValue(temporalField, n, 19, SignStyle.NORMAL);
                        break;
                    }
                    this.appendValue(temporalField, n, 19, SignStyle.EXCEEDS_PAD);
                    break;
                }
                case 'c': {
                    if (n == 2) {
                        throw new IllegalArgumentException("Invalid pattern \"cc\"");
                    }
                }
                case 'L':
                case 'q': {
                    b = true;
                }
                case 'E':
                case 'M':
                case 'Q':
                case 'e': {
                    switch (n) {
                        case 1:
                        case 2: {
                            if (c == 'c' || c == 'e') {
                                this.appendInternal(new WeekBasedFieldPrinterParser(c, n));
                                break Label_0843;
                            }
                            if (c == 'E') {
                                this.appendText(temporalField, TextStyle.SHORT);
                                break Label_0843;
                            }
                            if (n == 1) {
                                this.appendValue(temporalField);
                                break Label_0843;
                            }
                            this.appendValue(temporalField, 2);
                            break Label_0843;
                        }
                        case 3: {
                            this.appendText(temporalField, b ? TextStyle.SHORT_STANDALONE : TextStyle.SHORT);
                            break Label_0843;
                        }
                        case 4: {
                            this.appendText(temporalField, b ? TextStyle.FULL_STANDALONE : TextStyle.FULL);
                            break Label_0843;
                        }
                        case 5: {
                            this.appendText(temporalField, b ? TextStyle.NARROW_STANDALONE : TextStyle.NARROW);
                            break Label_0843;
                        }
                        default: {
                            throw new IllegalArgumentException("Too many pattern letters: " + c);
                        }
                    }
                    break;
                }
                case 'a': {
                    if (n == 1) {
                        this.appendText(temporalField, TextStyle.SHORT);
                        break;
                    }
                    throw new IllegalArgumentException("Too many pattern letters: " + c);
                }
                case 'G': {
                    switch (n) {
                        case 1:
                        case 2:
                        case 3: {
                            this.appendText(temporalField, TextStyle.SHORT);
                            break Label_0843;
                        }
                        case 4: {
                            this.appendText(temporalField, TextStyle.FULL);
                            break Label_0843;
                        }
                        case 5: {
                            this.appendText(temporalField, TextStyle.NARROW);
                            break Label_0843;
                        }
                        default: {
                            throw new IllegalArgumentException("Too many pattern letters: " + c);
                        }
                    }
                    break;
                }
                case 'S': {
                    this.appendFraction(ChronoField.NANO_OF_SECOND, n, n, false);
                    break;
                }
                case 'F': {
                    if (n == 1) {
                        this.appendValue(temporalField);
                        break;
                    }
                    throw new IllegalArgumentException("Too many pattern letters: " + c);
                }
                case 'H':
                case 'K':
                case 'd':
                case 'h':
                case 'k':
                case 'm':
                case 's': {
                    if (n == 1) {
                        this.appendValue(temporalField);
                        break;
                    }
                    if (n == 2) {
                        this.appendValue(temporalField, n);
                        break;
                    }
                    throw new IllegalArgumentException("Too many pattern letters: " + c);
                }
                case 'D': {
                    if (n == 1) {
                        this.appendValue(temporalField);
                        break;
                    }
                    if (n <= 3) {
                        this.appendValue(temporalField, n);
                        break;
                    }
                    throw new IllegalArgumentException("Too many pattern letters: " + c);
                }
                default: {
                    if (n == 1) {
                        this.appendValue(temporalField);
                        break;
                    }
                    this.appendValue(temporalField, n);
                    break;
                }
            }
        }
    }
    
    public DateTimeFormatterBuilder padNext(final int n) {
        return this.padNext(n, ' ');
    }
    
    public DateTimeFormatterBuilder padNext(final int padNextWidth, final char padNextChar) {
        if (padNextWidth < 1) {
            throw new IllegalArgumentException("The pad width must be at least one but was " + padNextWidth);
        }
        this.active.padNextWidth = padNextWidth;
        this.active.padNextChar = padNextChar;
        this.active.valueParserIndex = -1;
        return this;
    }
    
    public DateTimeFormatterBuilder optionalStart() {
        this.active.valueParserIndex = -1;
        this.active = new DateTimeFormatterBuilder(this.active, true);
        return this;
    }
    
    public DateTimeFormatterBuilder optionalEnd() {
        if (this.active.parent == null) {
            throw new IllegalStateException("Cannot call optionalEnd() as there was no previous call to optionalStart()");
        }
        if (this.active.printerParsers.size() > 0) {
            final CompositePrinterParser compositePrinterParser = new CompositePrinterParser(this.active.printerParsers, this.active.optional);
            this.active = this.active.parent;
            this.appendInternal(compositePrinterParser);
        }
        else {
            this.active = this.active.parent;
        }
        return this;
    }
    
    private int appendInternal(DateTimePrinterParser dateTimePrinterParser) {
        Objects.requireNonNull(dateTimePrinterParser, "pp");
        if (this.active.padNextWidth > 0) {
            if (dateTimePrinterParser != null) {
                dateTimePrinterParser = new PadPrinterParserDecorator(dateTimePrinterParser, this.active.padNextWidth, this.active.padNextChar);
            }
            this.active.padNextWidth = 0;
            this.active.padNextChar = '\0';
        }
        this.active.printerParsers.add(dateTimePrinterParser);
        this.active.valueParserIndex = -1;
        return this.active.printerParsers.size() - 1;
    }
    
    public DateTimeFormatter toFormatter() {
        return this.toFormatter(Locale.getDefault(Locale.Category.FORMAT));
    }
    
    public DateTimeFormatter toFormatter(final Locale locale) {
        return this.toFormatter(locale, ResolverStyle.SMART, null);
    }
    
    DateTimeFormatter toFormatter(final ResolverStyle resolverStyle, final Chronology chronology) {
        return this.toFormatter(Locale.getDefault(Locale.Category.FORMAT), resolverStyle, chronology);
    }
    
    private DateTimeFormatter toFormatter(final Locale locale, final ResolverStyle resolverStyle, final Chronology chronology) {
        Objects.requireNonNull(locale, "locale");
        while (this.active.parent != null) {
            this.optionalEnd();
        }
        return new DateTimeFormatter(new CompositePrinterParser(this.printerParsers, false), locale, DecimalStyle.STANDARD, resolverStyle, null, chronology, null);
    }
    
    static {
        final ZoneId zoneId;
        QUERY_REGION_ONLY = (temporalAccessor -> {
            zoneId = temporalAccessor.query(TemporalQueries.zoneId());
            return (zoneId != null && !(zoneId instanceof ZoneOffset)) ? zoneId : null;
        });
        (FIELD_MAP = new HashMap<Character, TemporalField>()).put('G', ChronoField.ERA);
        DateTimeFormatterBuilder.FIELD_MAP.put('y', ChronoField.YEAR_OF_ERA);
        DateTimeFormatterBuilder.FIELD_MAP.put('u', ChronoField.YEAR);
        DateTimeFormatterBuilder.FIELD_MAP.put('Q', IsoFields.QUARTER_OF_YEAR);
        DateTimeFormatterBuilder.FIELD_MAP.put('q', IsoFields.QUARTER_OF_YEAR);
        DateTimeFormatterBuilder.FIELD_MAP.put('M', ChronoField.MONTH_OF_YEAR);
        DateTimeFormatterBuilder.FIELD_MAP.put('L', ChronoField.MONTH_OF_YEAR);
        DateTimeFormatterBuilder.FIELD_MAP.put('D', ChronoField.DAY_OF_YEAR);
        DateTimeFormatterBuilder.FIELD_MAP.put('d', ChronoField.DAY_OF_MONTH);
        DateTimeFormatterBuilder.FIELD_MAP.put('F', ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH);
        DateTimeFormatterBuilder.FIELD_MAP.put('E', ChronoField.DAY_OF_WEEK);
        DateTimeFormatterBuilder.FIELD_MAP.put('c', ChronoField.DAY_OF_WEEK);
        DateTimeFormatterBuilder.FIELD_MAP.put('e', ChronoField.DAY_OF_WEEK);
        DateTimeFormatterBuilder.FIELD_MAP.put('a', ChronoField.AMPM_OF_DAY);
        DateTimeFormatterBuilder.FIELD_MAP.put('H', ChronoField.HOUR_OF_DAY);
        DateTimeFormatterBuilder.FIELD_MAP.put('k', ChronoField.CLOCK_HOUR_OF_DAY);
        DateTimeFormatterBuilder.FIELD_MAP.put('K', ChronoField.HOUR_OF_AMPM);
        DateTimeFormatterBuilder.FIELD_MAP.put('h', ChronoField.CLOCK_HOUR_OF_AMPM);
        DateTimeFormatterBuilder.FIELD_MAP.put('m', ChronoField.MINUTE_OF_HOUR);
        DateTimeFormatterBuilder.FIELD_MAP.put('s', ChronoField.SECOND_OF_MINUTE);
        DateTimeFormatterBuilder.FIELD_MAP.put('S', ChronoField.NANO_OF_SECOND);
        DateTimeFormatterBuilder.FIELD_MAP.put('A', ChronoField.MILLI_OF_DAY);
        DateTimeFormatterBuilder.FIELD_MAP.put('n', ChronoField.NANO_OF_SECOND);
        DateTimeFormatterBuilder.FIELD_MAP.put('N', ChronoField.NANO_OF_DAY);
        LENGTH_SORT = new Comparator<String>() {
            @Override
            public int compare(final String s, final String s2) {
                return (s.length() == s2.length()) ? s.compareTo(s2) : (s.length() - s2.length());
            }
        };
    }
    
    static final class CharLiteralPrinterParser implements DateTimePrinterParser
    {
        private final char literal;
        
        CharLiteralPrinterParser(final char literal) {
            this.literal = literal;
        }
        
        @Override
        public boolean format(final DateTimePrintContext dateTimePrintContext, final StringBuilder sb) {
            sb.append(this.literal);
            return true;
        }
        
        @Override
        public int parse(final DateTimeParseContext dateTimeParseContext, final CharSequence charSequence, final int n) {
            if (n == charSequence.length()) {
                return ~n;
            }
            final char char1 = charSequence.charAt(n);
            if (char1 != this.literal && (dateTimeParseContext.isCaseSensitive() || (Character.toUpperCase(char1) != Character.toUpperCase(this.literal) && Character.toLowerCase(char1) != Character.toLowerCase(this.literal)))) {
                return ~n;
            }
            return n + 1;
        }
        
        @Override
        public String toString() {
            if (this.literal == '\'') {
                return "''";
            }
            return "'" + this.literal + "'";
        }
    }
    
    interface DateTimePrinterParser
    {
        boolean format(final DateTimePrintContext p0, final StringBuilder p1);
        
        int parse(final DateTimeParseContext p0, final CharSequence p1, final int p2);
    }
    
    static final class ChronoPrinterParser implements DateTimePrinterParser
    {
        private final TextStyle textStyle;
        
        ChronoPrinterParser(final TextStyle textStyle) {
            this.textStyle = textStyle;
        }
        
        @Override
        public boolean format(final DateTimePrintContext dateTimePrintContext, final StringBuilder sb) {
            final Chronology chronology = dateTimePrintContext.getValue(TemporalQueries.chronology());
            if (chronology == null) {
                return false;
            }
            if (this.textStyle == null) {
                sb.append(chronology.getId());
            }
            else {
                sb.append(this.getChronologyName(chronology, dateTimePrintContext.getLocale()));
            }
            return true;
        }
        
        @Override
        public int parse(final DateTimeParseContext dateTimeParseContext, final CharSequence charSequence, final int n) {
            if (n < 0 || n > charSequence.length()) {
                throw new IndexOutOfBoundsException();
            }
            final Set<Chronology> availableChronologies = Chronology.getAvailableChronologies();
            Chronology parsed = null;
            int n2 = -1;
            for (final Chronology chronology : availableChronologies) {
                String s;
                if (this.textStyle == null) {
                    s = chronology.getId();
                }
                else {
                    s = this.getChronologyName(chronology, dateTimeParseContext.getLocale());
                }
                final int length = s.length();
                if (length > n2 && dateTimeParseContext.subSequenceEquals(charSequence, n, s, 0, length)) {
                    parsed = chronology;
                    n2 = length;
                }
            }
            if (parsed == null) {
                return ~n;
            }
            dateTimeParseContext.setParsed(parsed);
            return n + n2;
        }
        
        private String getChronologyName(final Chronology chronology, final Locale locale) {
            final String s = DateTimeTextProvider.getLocalizedResource("calendarname." + chronology.getCalendarType(), locale);
            return (s != null) ? s : chronology.getId();
        }
    }
    
    static final class CompositePrinterParser implements DateTimePrinterParser
    {
        private final DateTimePrinterParser[] printerParsers;
        private final boolean optional;
        
        CompositePrinterParser(final List<DateTimePrinterParser> list, final boolean b) {
            this(list.toArray(new DateTimePrinterParser[list.size()]), b);
        }
        
        CompositePrinterParser(final DateTimePrinterParser[] printerParsers, final boolean optional) {
            this.printerParsers = printerParsers;
            this.optional = optional;
        }
        
        public CompositePrinterParser withOptional(final boolean b) {
            if (b == this.optional) {
                return this;
            }
            return new CompositePrinterParser(this.printerParsers, b);
        }
        
        @Override
        public boolean format(final DateTimePrintContext dateTimePrintContext, final StringBuilder sb) {
            final int length = sb.length();
            if (this.optional) {
                dateTimePrintContext.startOptional();
            }
            try {
                final DateTimePrinterParser[] printerParsers = this.printerParsers;
                for (int length2 = printerParsers.length, i = 0; i < length2; ++i) {
                    if (!printerParsers[i].format(dateTimePrintContext, sb)) {
                        sb.setLength(length);
                        return true;
                    }
                }
            }
            finally {
                if (this.optional) {
                    dateTimePrintContext.endOptional();
                }
            }
            return true;
        }
        
        @Override
        public int parse(final DateTimeParseContext dateTimeParseContext, final CharSequence charSequence, int parse) {
            if (this.optional) {
                dateTimeParseContext.startOptional();
                int parse2 = parse;
                final DateTimePrinterParser[] printerParsers = this.printerParsers;
                for (int length = printerParsers.length, i = 0; i < length; ++i) {
                    parse2 = printerParsers[i].parse(dateTimeParseContext, charSequence, parse2);
                    if (parse2 < 0) {
                        dateTimeParseContext.endOptional(false);
                        return parse;
                    }
                }
                dateTimeParseContext.endOptional(true);
                return parse2;
            }
            final DateTimePrinterParser[] printerParsers2 = this.printerParsers;
            for (int length2 = printerParsers2.length, j = 0; j < length2; ++j) {
                parse = printerParsers2[j].parse(dateTimeParseContext, charSequence, parse);
                if (parse < 0) {
                    break;
                }
            }
            return parse;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            if (this.printerParsers != null) {
                sb.append(this.optional ? "[" : "(");
                final DateTimePrinterParser[] printerParsers = this.printerParsers;
                for (int length = printerParsers.length, i = 0; i < length; ++i) {
                    sb.append(printerParsers[i]);
                }
                sb.append(this.optional ? "]" : ")");
            }
            return sb.toString();
        }
    }
    
    static class DefaultValueParser implements DateTimePrinterParser
    {
        private final TemporalField field;
        private final long value;
        
        DefaultValueParser(final TemporalField field, final long value) {
            this.field = field;
            this.value = value;
        }
        
        @Override
        public boolean format(final DateTimePrintContext dateTimePrintContext, final StringBuilder sb) {
            return true;
        }
        
        @Override
        public int parse(final DateTimeParseContext dateTimeParseContext, final CharSequence charSequence, final int n) {
            if (dateTimeParseContext.getParsed(this.field) == null) {
                dateTimeParseContext.setParsedField(this.field, this.value, n, n);
            }
            return n;
        }
    }
    
    static final class FractionPrinterParser implements DateTimePrinterParser
    {
        private final TemporalField field;
        private final int minWidth;
        private final int maxWidth;
        private final boolean decimalPoint;
        
        FractionPrinterParser(final TemporalField field, final int minWidth, final int maxWidth, final boolean decimalPoint) {
            Objects.requireNonNull(field, "field");
            if (!field.range().isFixed()) {
                throw new IllegalArgumentException("Field must have a fixed set of values: " + field);
            }
            if (minWidth < 0 || minWidth > 9) {
                throw new IllegalArgumentException("Minimum width must be from 0 to 9 inclusive but was " + minWidth);
            }
            if (maxWidth < 1 || maxWidth > 9) {
                throw new IllegalArgumentException("Maximum width must be from 1 to 9 inclusive but was " + maxWidth);
            }
            if (maxWidth < minWidth) {
                throw new IllegalArgumentException("Maximum width must exceed or equal the minimum width but " + maxWidth + " < " + minWidth);
            }
            this.field = field;
            this.minWidth = minWidth;
            this.maxWidth = maxWidth;
            this.decimalPoint = decimalPoint;
        }
        
        @Override
        public boolean format(final DateTimePrintContext dateTimePrintContext, final StringBuilder sb) {
            final Long value = dateTimePrintContext.getValue(this.field);
            if (value == null) {
                return false;
            }
            final DecimalStyle decimalStyle = dateTimePrintContext.getDecimalStyle();
            final BigDecimal convertToFraction = this.convertToFraction(value);
            if (convertToFraction.scale() == 0) {
                if (this.minWidth > 0) {
                    if (this.decimalPoint) {
                        sb.append(decimalStyle.getDecimalSeparator());
                    }
                    for (int i = 0; i < this.minWidth; ++i) {
                        sb.append(decimalStyle.getZeroDigit());
                    }
                }
            }
            else {
                final String convertNumberToI18N = decimalStyle.convertNumberToI18N(convertToFraction.setScale(Math.min(Math.max(convertToFraction.scale(), this.minWidth), this.maxWidth), RoundingMode.FLOOR).toPlainString().substring(2));
                if (this.decimalPoint) {
                    sb.append(decimalStyle.getDecimalSeparator());
                }
                sb.append(convertNumberToI18N);
            }
            return true;
        }
        
        @Override
        public int parse(final DateTimeParseContext dateTimeParseContext, final CharSequence charSequence, int n) {
            final int n2 = dateTimeParseContext.isStrict() ? this.minWidth : 0;
            final int n3 = dateTimeParseContext.isStrict() ? this.maxWidth : 9;
            final int length = charSequence.length();
            if (n == length) {
                return (n2 > 0) ? (~n) : n;
            }
            if (this.decimalPoint) {
                if (charSequence.charAt(n) != dateTimeParseContext.getDecimalStyle().getDecimalSeparator()) {
                    return (n2 > 0) ? (~n) : n;
                }
                ++n;
            }
            final int n4 = n + n2;
            if (n4 > length) {
                return ~n;
            }
            final int min = Math.min(n + n3, length);
            int n5 = 0;
            int i = n;
            while (i < min) {
                final int convertToDigit = dateTimeParseContext.getDecimalStyle().convertToDigit(charSequence.charAt(i++));
                if (convertToDigit < 0) {
                    if (i < n4) {
                        return ~n;
                    }
                    --i;
                    break;
                }
                else {
                    n5 = n5 * 10 + convertToDigit;
                }
            }
            return dateTimeParseContext.setParsedField(this.field, this.convertFromFraction(new BigDecimal(n5).movePointLeft(i - n)), n, i);
        }
        
        private BigDecimal convertToFraction(final long n) {
            final ValueRange range = this.field.range();
            range.checkValidValue(n, this.field);
            final BigDecimal value = BigDecimal.valueOf(range.getMinimum());
            final BigDecimal divide = BigDecimal.valueOf(n).subtract(value).divide(BigDecimal.valueOf(range.getMaximum()).subtract(value).add(BigDecimal.ONE), 9, RoundingMode.FLOOR);
            return (divide.compareTo(BigDecimal.ZERO) == 0) ? BigDecimal.ZERO : divide.stripTrailingZeros();
        }
        
        private long convertFromFraction(final BigDecimal bigDecimal) {
            final ValueRange range = this.field.range();
            final BigDecimal value = BigDecimal.valueOf(range.getMinimum());
            return bigDecimal.multiply(BigDecimal.valueOf(range.getMaximum()).subtract(value).add(BigDecimal.ONE)).setScale(0, RoundingMode.FLOOR).add(value).longValueExact();
        }
        
        @Override
        public String toString() {
            return "Fraction(" + this.field + "," + this.minWidth + "," + this.maxWidth + (this.decimalPoint ? ",DecimalPoint" : "") + ")";
        }
    }
    
    static final class InstantPrinterParser implements DateTimePrinterParser
    {
        private static final long SECONDS_PER_10000_YEARS = 315569520000L;
        private static final long SECONDS_0000_TO_1970 = 62167219200L;
        private final int fractionalDigits;
        
        InstantPrinterParser(final int fractionalDigits) {
            this.fractionalDigits = fractionalDigits;
        }
        
        @Override
        public boolean format(final DateTimePrintContext dateTimePrintContext, final StringBuilder sb) {
            final Long value = dateTimePrintContext.getValue(ChronoField.INSTANT_SECONDS);
            Long value2 = null;
            if (dateTimePrintContext.getTemporal().isSupported(ChronoField.NANO_OF_SECOND)) {
                value2 = dateTimePrintContext.getTemporal().getLong(ChronoField.NANO_OF_SECOND);
            }
            if (value == null) {
                return false;
            }
            final long longValue = value;
            int checkValidIntValue = ChronoField.NANO_OF_SECOND.checkValidIntValue((value2 != null) ? ((long)value2) : 0L);
            if (longValue >= -62167219200L) {
                final long n = longValue - 315569520000L + 62167219200L;
                final long n2 = Math.floorDiv(n, 315569520000L) + 1L;
                final LocalDateTime ofEpochSecond = LocalDateTime.ofEpochSecond(Math.floorMod(n, 315569520000L) - 62167219200L, 0, ZoneOffset.UTC);
                if (n2 > 0L) {
                    sb.append('+').append(n2);
                }
                sb.append(ofEpochSecond);
                if (ofEpochSecond.getSecond() == 0) {
                    sb.append(":00");
                }
            }
            else {
                final long n3 = longValue + 62167219200L;
                final long n4 = n3 / 315569520000L;
                final long n5 = n3 % 315569520000L;
                final LocalDateTime ofEpochSecond2 = LocalDateTime.ofEpochSecond(n5 - 62167219200L, 0, ZoneOffset.UTC);
                final int length = sb.length();
                sb.append(ofEpochSecond2);
                if (ofEpochSecond2.getSecond() == 0) {
                    sb.append(":00");
                }
                if (n4 < 0L) {
                    if (ofEpochSecond2.getYear() == -10000) {
                        sb.replace(length, length + 2, Long.toString(n4 - 1L));
                    }
                    else if (n5 == 0L) {
                        sb.insert(length, n4);
                    }
                    else {
                        sb.insert(length + 1, Math.abs(n4));
                    }
                }
            }
            if ((this.fractionalDigits < 0 && checkValidIntValue > 0) || this.fractionalDigits > 0) {
                sb.append('.');
                int n8;
                for (int n6 = 100000000, n7 = 0; (this.fractionalDigits == -1 && checkValidIntValue > 0) || (this.fractionalDigits == -2 && (checkValidIntValue > 0 || n7 % 3 != 0)) || n7 < this.fractionalDigits; checkValidIntValue -= n8 * n6, n6 /= 10, ++n7) {
                    n8 = checkValidIntValue / n6;
                    sb.append((char)(n8 + 48));
                }
            }
            sb.append('Z');
            return true;
        }
        
        @Override
        public int parse(final DateTimeParseContext dateTimeParseContext, final CharSequence charSequence, final int n) {
            final CompositePrinterParser printerParser = new DateTimeFormatterBuilder().append(DateTimeFormatter.ISO_LOCAL_DATE).appendLiteral('T').appendValue(ChronoField.HOUR_OF_DAY, 2).appendLiteral(':').appendValue(ChronoField.MINUTE_OF_HOUR, 2).appendLiteral(':').appendValue(ChronoField.SECOND_OF_MINUTE, 2).appendFraction(ChronoField.NANO_OF_SECOND, (this.fractionalDigits < 0) ? 0 : this.fractionalDigits, (this.fractionalDigits < 0) ? 9 : this.fractionalDigits, true).appendLiteral('Z').toFormatter().toPrinterParser(false);
            final DateTimeParseContext copy = dateTimeParseContext.copy();
            final int parse = printerParser.parse(copy, charSequence, n);
            if (parse < 0) {
                return parse;
            }
            final long longValue = copy.getParsed(ChronoField.YEAR);
            final int intValue = copy.getParsed(ChronoField.MONTH_OF_YEAR).intValue();
            final int intValue2 = copy.getParsed(ChronoField.DAY_OF_MONTH).intValue();
            int intValue3 = copy.getParsed(ChronoField.HOUR_OF_DAY).intValue();
            final int intValue4 = copy.getParsed(ChronoField.MINUTE_OF_HOUR).intValue();
            final Long parsed = copy.getParsed(ChronoField.SECOND_OF_MINUTE);
            final Long parsed2 = copy.getParsed(ChronoField.NANO_OF_SECOND);
            int n2 = (parsed != null) ? parsed.intValue() : 0;
            final int n3 = (parsed2 != null) ? parsed2.intValue() : 0;
            boolean b = false;
            if (intValue3 == 24 && intValue4 == 0 && n2 == 0 && n3 == 0) {
                intValue3 = 0;
                b = true;
            }
            else if (intValue3 == 23 && intValue4 == 59 && n2 == 60) {
                dateTimeParseContext.setParsedLeapSecond();
                n2 = 59;
            }
            final int n4 = (int)longValue % 10000;
            long n5;
            try {
                n5 = LocalDateTime.of(n4, intValue, intValue2, intValue3, intValue4, n2, 0).plusDays(b ? 1 : 0).toEpochSecond(ZoneOffset.UTC) + Math.multiplyExact(longValue / 10000L, 315569520000L);
            }
            catch (RuntimeException ex) {
                return ~n;
            }
            return dateTimeParseContext.setParsedField(ChronoField.NANO_OF_SECOND, n3, n, dateTimeParseContext.setParsedField(ChronoField.INSTANT_SECONDS, n5, n, parse));
        }
        
        @Override
        public String toString() {
            return "Instant()";
        }
    }
    
    static final class LocalizedOffsetIdPrinterParser implements DateTimePrinterParser
    {
        private final TextStyle style;
        
        LocalizedOffsetIdPrinterParser(final TextStyle style) {
            this.style = style;
        }
        
        private static StringBuilder appendHMS(final StringBuilder sb, final int n) {
            return sb.append((char)(n / 10 + 48)).append((char)(n % 10 + 48));
        }
        
        @Override
        public boolean format(final DateTimePrintContext dateTimePrintContext, final StringBuilder sb) {
            final Long value = dateTimePrintContext.getValue(ChronoField.OFFSET_SECONDS);
            if (value == null) {
                return false;
            }
            final String s = "GMT";
            if (s != null) {
                sb.append(s);
            }
            final int intExact = Math.toIntExact(value);
            if (intExact != 0) {
                final int abs = Math.abs(intExact / 3600 % 100);
                final int abs2 = Math.abs(intExact / 60 % 60);
                final int abs3 = Math.abs(intExact % 60);
                sb.append((intExact < 0) ? "-" : "+");
                if (this.style == TextStyle.FULL) {
                    appendHMS(sb, abs);
                    sb.append(':');
                    appendHMS(sb, abs2);
                    if (abs3 != 0) {
                        sb.append(':');
                        appendHMS(sb, abs3);
                    }
                }
                else {
                    if (abs >= 10) {
                        sb.append((char)(abs / 10 + 48));
                    }
                    sb.append((char)(abs % 10 + 48));
                    if (abs2 != 0 || abs3 != 0) {
                        sb.append(':');
                        appendHMS(sb, abs2);
                        if (abs3 != 0) {
                            sb.append(':');
                            appendHMS(sb, abs3);
                        }
                    }
                }
            }
            return true;
        }
        
        int getDigit(final CharSequence charSequence, final int n) {
            final char char1 = charSequence.charAt(n);
            if (char1 < '0' || char1 > '9') {
                return -1;
            }
            return char1 - '0';
        }
        
        @Override
        public int parse(final DateTimeParseContext dateTimeParseContext, final CharSequence charSequence, final int n) {
            int n2 = n;
            final int n3 = n2 + charSequence.length();
            final String s = "GMT";
            if (s != null) {
                if (!dateTimeParseContext.subSequenceEquals(charSequence, n2, s, 0, s.length())) {
                    return ~n;
                }
                n2 += s.length();
            }
            if (n2 == n3) {
                return dateTimeParseContext.setParsedField(ChronoField.OFFSET_SECONDS, 0L, n, n2);
            }
            final char char1 = charSequence.charAt(n2);
            int n4;
            if (char1 == '+') {
                n4 = 1;
            }
            else {
                if (char1 != '-') {
                    return dateTimeParseContext.setParsedField(ChronoField.OFFSET_SECONDS, 0L, n, n2);
                }
                n4 = -1;
            }
            ++n2;
            int n5 = 0;
            int n6 = 0;
            int digit3;
            if (this.style == TextStyle.FULL) {
                final int digit = this.getDigit(charSequence, n2++);
                final int digit2 = this.getDigit(charSequence, n2++);
                if (digit < 0 || digit2 < 0 || charSequence.charAt(n2++) != ':') {
                    return ~n;
                }
                digit3 = digit * 10 + digit2;
                final int digit4 = this.getDigit(charSequence, n2++);
                final int digit5 = this.getDigit(charSequence, n2++);
                if (digit4 < 0 || digit5 < 0) {
                    return ~n;
                }
                n5 = digit4 * 10 + digit5;
                if (n2 + 2 < n3 && charSequence.charAt(n2) == ':') {
                    final int digit6 = this.getDigit(charSequence, n2 + 1);
                    final int digit7 = this.getDigit(charSequence, n2 + 2);
                    if (digit6 >= 0 && digit7 >= 0) {
                        n6 = digit6 * 10 + digit7;
                        n2 += 3;
                    }
                }
            }
            else {
                digit3 = this.getDigit(charSequence, n2++);
                if (digit3 < 0) {
                    return ~n;
                }
                if (n2 < n3) {
                    final int digit8 = this.getDigit(charSequence, n2);
                    if (digit8 >= 0) {
                        digit3 = digit3 * 10 + digit8;
                        ++n2;
                    }
                    if (n2 + 2 < n3 && charSequence.charAt(n2) == ':' && n2 + 2 < n3 && charSequence.charAt(n2) == ':') {
                        final int digit9 = this.getDigit(charSequence, n2 + 1);
                        final int digit10 = this.getDigit(charSequence, n2 + 2);
                        if (digit9 >= 0 && digit10 >= 0) {
                            n5 = digit9 * 10 + digit10;
                            n2 += 3;
                            if (n2 + 2 < n3 && charSequence.charAt(n2) == ':') {
                                final int digit11 = this.getDigit(charSequence, n2 + 1);
                                final int digit12 = this.getDigit(charSequence, n2 + 2);
                                if (digit11 >= 0 && digit12 >= 0) {
                                    n6 = digit11 * 10 + digit12;
                                    n2 += 3;
                                }
                            }
                        }
                    }
                }
            }
            return dateTimeParseContext.setParsedField(ChronoField.OFFSET_SECONDS, n4 * (digit3 * 3600L + n5 * 60L + n6), n, n2);
        }
        
        @Override
        public String toString() {
            return "LocalizedOffset(" + this.style + ")";
        }
    }
    
    static final class LocalizedPrinterParser implements DateTimePrinterParser
    {
        private static final ConcurrentMap<String, DateTimeFormatter> FORMATTER_CACHE;
        private final FormatStyle dateStyle;
        private final FormatStyle timeStyle;
        
        LocalizedPrinterParser(final FormatStyle dateStyle, final FormatStyle timeStyle) {
            this.dateStyle = dateStyle;
            this.timeStyle = timeStyle;
        }
        
        @Override
        public boolean format(final DateTimePrintContext dateTimePrintContext, final StringBuilder sb) {
            return this.formatter(dateTimePrintContext.getLocale(), Chronology.from(dateTimePrintContext.getTemporal())).toPrinterParser(false).format(dateTimePrintContext, sb);
        }
        
        @Override
        public int parse(final DateTimeParseContext dateTimeParseContext, final CharSequence charSequence, final int n) {
            return this.formatter(dateTimeParseContext.getLocale(), dateTimeParseContext.getEffectiveChronology()).toPrinterParser(false).parse(dateTimeParseContext, charSequence, n);
        }
        
        private DateTimeFormatter formatter(final Locale locale, final Chronology chronology) {
            final String string = chronology.getId() + '|' + locale.toString() + '|' + this.dateStyle + this.timeStyle;
            DateTimeFormatter formatter = LocalizedPrinterParser.FORMATTER_CACHE.get(string);
            if (formatter == null) {
                formatter = new DateTimeFormatterBuilder().appendPattern(DateTimeFormatterBuilder.getLocalizedDateTimePattern(this.dateStyle, this.timeStyle, chronology, locale)).toFormatter(locale);
                final DateTimeFormatter dateTimeFormatter = LocalizedPrinterParser.FORMATTER_CACHE.putIfAbsent(string, formatter);
                if (dateTimeFormatter != null) {
                    formatter = dateTimeFormatter;
                }
            }
            return formatter;
        }
        
        @Override
        public String toString() {
            return "Localized(" + ((this.dateStyle != null) ? this.dateStyle : "") + "," + ((this.timeStyle != null) ? this.timeStyle : "") + ")";
        }
        
        static {
            FORMATTER_CACHE = new ConcurrentHashMap<String, DateTimeFormatter>(16, 0.75f, 2);
        }
    }
    
    static class NumberPrinterParser implements DateTimePrinterParser
    {
        static final long[] EXCEED_POINTS;
        final TemporalField field;
        final int minWidth;
        final int maxWidth;
        private final SignStyle signStyle;
        final int subsequentWidth;
        
        NumberPrinterParser(final TemporalField field, final int minWidth, final int maxWidth, final SignStyle signStyle) {
            this.field = field;
            this.minWidth = minWidth;
            this.maxWidth = maxWidth;
            this.signStyle = signStyle;
            this.subsequentWidth = 0;
        }
        
        protected NumberPrinterParser(final TemporalField field, final int minWidth, final int maxWidth, final SignStyle signStyle, final int subsequentWidth) {
            this.field = field;
            this.minWidth = minWidth;
            this.maxWidth = maxWidth;
            this.signStyle = signStyle;
            this.subsequentWidth = subsequentWidth;
        }
        
        NumberPrinterParser withFixedWidth() {
            if (this.subsequentWidth == -1) {
                return this;
            }
            return new NumberPrinterParser(this.field, this.minWidth, this.maxWidth, this.signStyle, -1);
        }
        
        NumberPrinterParser withSubsequentWidth(final int n) {
            return new NumberPrinterParser(this.field, this.minWidth, this.maxWidth, this.signStyle, this.subsequentWidth + n);
        }
        
        @Override
        public boolean format(final DateTimePrintContext dateTimePrintContext, final StringBuilder sb) {
            final Long value = dateTimePrintContext.getValue(this.field);
            if (value == null) {
                return false;
            }
            final long value2 = this.getValue(dateTimePrintContext, value);
            final DecimalStyle decimalStyle = dateTimePrintContext.getDecimalStyle();
            final String s = (value2 == Long.MIN_VALUE) ? "9223372036854775808" : Long.toString(Math.abs(value2));
            if (s.length() > this.maxWidth) {
                throw new DateTimeException("Field " + this.field + " cannot be printed as the value " + value2 + " exceeds the maximum print width of " + this.maxWidth);
            }
            final String convertNumberToI18N = decimalStyle.convertNumberToI18N(s);
            if (value2 >= 0L) {
                switch (this.signStyle) {
                    case EXCEEDS_PAD: {
                        if (this.minWidth < 19 && value2 >= NumberPrinterParser.EXCEED_POINTS[this.minWidth]) {
                            sb.append(decimalStyle.getPositiveSign());
                            break;
                        }
                        break;
                    }
                    case ALWAYS: {
                        sb.append(decimalStyle.getPositiveSign());
                        break;
                    }
                }
            }
            else {
                switch (this.signStyle) {
                    case EXCEEDS_PAD:
                    case ALWAYS:
                    case NORMAL: {
                        sb.append(decimalStyle.getNegativeSign());
                        break;
                    }
                    case NOT_NEGATIVE: {
                        throw new DateTimeException("Field " + this.field + " cannot be printed as the value " + value2 + " cannot be negative according to the SignStyle");
                    }
                }
            }
            for (int i = 0; i < this.minWidth - convertNumberToI18N.length(); ++i) {
                sb.append(decimalStyle.getZeroDigit());
            }
            sb.append(convertNumberToI18N);
            return true;
        }
        
        long getValue(final DateTimePrintContext dateTimePrintContext, final long n) {
            return n;
        }
        
        boolean isFixedWidth(final DateTimeParseContext dateTimeParseContext) {
            return this.subsequentWidth == -1 || (this.subsequentWidth > 0 && this.minWidth == this.maxWidth && this.signStyle == SignStyle.NOT_NEGATIVE);
        }
        
        @Override
        public int parse(final DateTimeParseContext dateTimeParseContext, final CharSequence charSequence, int n) {
            final int length = charSequence.length();
            if (n == length) {
                return ~n;
            }
            final char char1 = charSequence.charAt(n);
            boolean b = false;
            boolean b2 = false;
            if (char1 == dateTimeParseContext.getDecimalStyle().getPositiveSign()) {
                if (!this.signStyle.parse(true, dateTimeParseContext.isStrict(), this.minWidth == this.maxWidth)) {
                    return ~n;
                }
                b2 = true;
                ++n;
            }
            else if (char1 == dateTimeParseContext.getDecimalStyle().getNegativeSign()) {
                if (!this.signStyle.parse(false, dateTimeParseContext.isStrict(), this.minWidth == this.maxWidth)) {
                    return ~n;
                }
                b = true;
                ++n;
            }
            else if (this.signStyle == SignStyle.ALWAYS && dateTimeParseContext.isStrict()) {
                return ~n;
            }
            final int n2 = (dateTimeParseContext.isStrict() || this.isFixedWidth(dateTimeParseContext)) ? this.minWidth : 1;
            final int n3 = n + n2;
            if (n3 > length) {
                return ~n;
            }
            int max = ((dateTimeParseContext.isStrict() || this.isFixedWidth(dateTimeParseContext)) ? this.maxWidth : 9) + Math.max(this.subsequentWidth, 0);
            long n4 = 0L;
            BigInteger bigInteger = null;
            int i = n;
            for (int j = 0; j < 2; ++j) {
                while (i < Math.min(i + max, length)) {
                    final int convertToDigit = dateTimeParseContext.getDecimalStyle().convertToDigit(charSequence.charAt(i++));
                    if (convertToDigit < 0) {
                        if (--i < n3) {
                            return ~n;
                        }
                        break;
                    }
                    else if (i - n > 18) {
                        if (bigInteger == null) {
                            bigInteger = BigInteger.valueOf(n4);
                        }
                        bigInteger = bigInteger.multiply(BigInteger.TEN).add(BigInteger.valueOf(convertToDigit));
                    }
                    else {
                        n4 = n4 * 10L + convertToDigit;
                    }
                }
                if (this.subsequentWidth <= 0 || j != 0) {
                    break;
                }
                max = Math.max(n2, i - n - this.subsequentWidth);
                i = n;
                n4 = 0L;
                bigInteger = null;
            }
            if (b) {
                if (bigInteger != null) {
                    if (bigInteger.equals(BigInteger.ZERO) && dateTimeParseContext.isStrict()) {
                        return ~(n - 1);
                    }
                    bigInteger = bigInteger.negate();
                }
                else {
                    if (n4 == 0L && dateTimeParseContext.isStrict()) {
                        return ~(n - 1);
                    }
                    n4 = -n4;
                }
            }
            else if (this.signStyle == SignStyle.EXCEEDS_PAD && dateTimeParseContext.isStrict()) {
                final int n5 = i - n;
                if (b2) {
                    if (n5 <= this.minWidth) {
                        return ~(n - 1);
                    }
                }
                else if (n5 > this.minWidth) {
                    return ~n;
                }
            }
            if (bigInteger != null) {
                if (bigInteger.bitLength() > 63) {
                    bigInteger = bigInteger.divide(BigInteger.TEN);
                    --i;
                }
                return this.setValue(dateTimeParseContext, bigInteger.longValue(), n, i);
            }
            return this.setValue(dateTimeParseContext, n4, n, i);
        }
        
        int setValue(final DateTimeParseContext dateTimeParseContext, final long n, final int n2, final int n3) {
            return dateTimeParseContext.setParsedField(this.field, n, n2, n3);
        }
        
        @Override
        public String toString() {
            if (this.minWidth == 1 && this.maxWidth == 19 && this.signStyle == SignStyle.NORMAL) {
                return "Value(" + this.field + ")";
            }
            if (this.minWidth == this.maxWidth && this.signStyle == SignStyle.NOT_NEGATIVE) {
                return "Value(" + this.field + "," + this.minWidth + ")";
            }
            return "Value(" + this.field + "," + this.minWidth + "," + this.maxWidth + "," + this.signStyle + ")";
        }
        
        static {
            EXCEED_POINTS = new long[] { 0L, 10L, 100L, 1000L, 10000L, 100000L, 1000000L, 10000000L, 100000000L, 1000000000L, 10000000000L };
        }
    }
    
    static final class OffsetIdPrinterParser implements DateTimePrinterParser
    {
        static final String[] PATTERNS;
        static final OffsetIdPrinterParser INSTANCE_ID_Z;
        static final OffsetIdPrinterParser INSTANCE_ID_ZERO;
        private final String noOffsetText;
        private final int type;
        
        OffsetIdPrinterParser(final String s, final String noOffsetText) {
            Objects.requireNonNull(s, "pattern");
            Objects.requireNonNull(noOffsetText, "noOffsetText");
            this.type = this.checkPattern(s);
            this.noOffsetText = noOffsetText;
        }
        
        private int checkPattern(final String s) {
            for (int i = 0; i < OffsetIdPrinterParser.PATTERNS.length; ++i) {
                if (OffsetIdPrinterParser.PATTERNS[i].equals(s)) {
                    return i;
                }
            }
            throw new IllegalArgumentException("Invalid zone offset pattern: " + s);
        }
        
        @Override
        public boolean format(final DateTimePrintContext dateTimePrintContext, final StringBuilder sb) {
            final Long value = dateTimePrintContext.getValue(ChronoField.OFFSET_SECONDS);
            if (value == null) {
                return false;
            }
            final int intExact = Math.toIntExact(value);
            if (intExact == 0) {
                sb.append(this.noOffsetText);
            }
            else {
                final int abs = Math.abs(intExact / 3600 % 100);
                final int abs2 = Math.abs(intExact / 60 % 60);
                final int abs3 = Math.abs(intExact % 60);
                final int length = sb.length();
                int n = abs;
                sb.append((intExact < 0) ? "-" : "+").append((char)(abs / 10 + 48)).append((char)(abs % 10 + 48));
                if (this.type >= 3 || (this.type >= 1 && abs2 > 0)) {
                    sb.append((this.type % 2 == 0) ? ":" : "").append((char)(abs2 / 10 + 48)).append((char)(abs2 % 10 + 48));
                    n += abs2;
                    if (this.type >= 7 || (this.type >= 5 && abs3 > 0)) {
                        sb.append((this.type % 2 == 0) ? ":" : "").append((char)(abs3 / 10 + 48)).append((char)(abs3 % 10 + 48));
                        n += abs3;
                    }
                }
                if (n == 0) {
                    sb.setLength(length);
                    sb.append(this.noOffsetText);
                }
            }
            return true;
        }
        
        @Override
        public int parse(final DateTimeParseContext dateTimeParseContext, final CharSequence charSequence, final int n) {
            final int length = charSequence.length();
            final int length2 = this.noOffsetText.length();
            if (length2 == 0) {
                if (n == length) {
                    return dateTimeParseContext.setParsedField(ChronoField.OFFSET_SECONDS, 0L, n, n);
                }
            }
            else {
                if (n == length) {
                    return ~n;
                }
                if (dateTimeParseContext.subSequenceEquals(charSequence, n, this.noOffsetText, 0, length2)) {
                    return dateTimeParseContext.setParsedField(ChronoField.OFFSET_SECONDS, 0L, n, n + length2);
                }
            }
            final char char1 = charSequence.charAt(n);
            if (char1 == '+' || char1 == '-') {
                final int n2 = (char1 == '-') ? -1 : 1;
                final int[] array = new int[4];
                array[0] = n + 1;
                if (!this.parseNumber(array, 1, charSequence, true) && !this.parseNumber(array, 2, charSequence, this.type >= 3) && !this.parseNumber(array, 3, charSequence, false)) {
                    return dateTimeParseContext.setParsedField(ChronoField.OFFSET_SECONDS, n2 * (array[1] * 3600L + array[2] * 60L + array[3]), n, array[0]);
                }
            }
            if (length2 == 0) {
                return dateTimeParseContext.setParsedField(ChronoField.OFFSET_SECONDS, 0L, n, n + length2);
            }
            return ~n;
        }
        
        private boolean parseNumber(final int[] array, final int n, final CharSequence charSequence, final boolean b) {
            if ((this.type + 3) / 2 < n) {
                return false;
            }
            int n2 = array[0];
            if (this.type % 2 == 0 && n > 1) {
                if (n2 + 1 > charSequence.length() || charSequence.charAt(n2) != ':') {
                    return b;
                }
                ++n2;
            }
            if (n2 + 2 > charSequence.length()) {
                return b;
            }
            final char char1 = charSequence.charAt(n2++);
            final char char2 = charSequence.charAt(n2++);
            if (char1 < '0' || char1 > '9' || char2 < '0' || char2 > '9') {
                return b;
            }
            final int n3 = (char1 - '0') * 10 + (char2 - '0');
            if (n3 < 0 || n3 > 59) {
                return b;
            }
            array[n] = n3;
            array[0] = n2;
            return false;
        }
        
        @Override
        public String toString() {
            return "Offset(" + OffsetIdPrinterParser.PATTERNS[this.type] + ",'" + this.noOffsetText.replace("'", "''") + "')";
        }
        
        static {
            PATTERNS = new String[] { "+HH", "+HHmm", "+HH:mm", "+HHMM", "+HH:MM", "+HHMMss", "+HH:MM:ss", "+HHMMSS", "+HH:MM:SS" };
            INSTANCE_ID_Z = new OffsetIdPrinterParser("+HH:MM:ss", "Z");
            INSTANCE_ID_ZERO = new OffsetIdPrinterParser("+HH:MM:ss", "0");
        }
    }
    
    static final class PadPrinterParserDecorator implements DateTimePrinterParser
    {
        private final DateTimePrinterParser printerParser;
        private final int padWidth;
        private final char padChar;
        
        PadPrinterParserDecorator(final DateTimePrinterParser printerParser, final int padWidth, final char padChar) {
            this.printerParser = printerParser;
            this.padWidth = padWidth;
            this.padChar = padChar;
        }
        
        @Override
        public boolean format(final DateTimePrintContext dateTimePrintContext, final StringBuilder sb) {
            final int length = sb.length();
            if (!this.printerParser.format(dateTimePrintContext, sb)) {
                return false;
            }
            final int n = sb.length() - length;
            if (n > this.padWidth) {
                throw new DateTimeException("Cannot print as output of " + n + " characters exceeds pad width of " + this.padWidth);
            }
            for (int i = 0; i < this.padWidth - n; ++i) {
                sb.insert(length, this.padChar);
            }
            return true;
        }
        
        @Override
        public int parse(final DateTimeParseContext dateTimeParseContext, CharSequence subSequence, final int n) {
            final boolean strict = dateTimeParseContext.isStrict();
            if (n > subSequence.length()) {
                throw new IndexOutOfBoundsException();
            }
            if (n == subSequence.length()) {
                return ~n;
            }
            int length = n + this.padWidth;
            if (length > subSequence.length()) {
                if (strict) {
                    return ~n;
                }
                length = subSequence.length();
            }
            int n2;
            for (n2 = n; n2 < length && dateTimeParseContext.charEquals(subSequence.charAt(n2), this.padChar); ++n2) {}
            subSequence = subSequence.subSequence(0, length);
            final int parse = this.printerParser.parse(dateTimeParseContext, subSequence, n2);
            if (parse != length && strict) {
                return ~(n + n2);
            }
            return parse;
        }
        
        @Override
        public String toString() {
            return "Pad(" + this.printerParser + "," + this.padWidth + ((this.padChar == ' ') ? ")" : (",'" + this.padChar + "')"));
        }
    }
    
    static class PrefixTree
    {
        protected String key;
        protected String value;
        protected char c0;
        protected PrefixTree child;
        protected PrefixTree sibling;
        
        private PrefixTree(final String key, final String value, final PrefixTree child) {
            this.key = key;
            this.value = value;
            this.child = child;
            if (key.length() == 0) {
                this.c0 = '\uffff';
            }
            else {
                this.c0 = this.key.charAt(0);
            }
        }
        
        public static PrefixTree newTree(final DateTimeParseContext dateTimeParseContext) {
            if (dateTimeParseContext.isCaseSensitive()) {
                return new PrefixTree("", null, null);
            }
            return new CI("", (String)null, (PrefixTree)null);
        }
        
        public static PrefixTree newTree(final Set<String> set, final DateTimeParseContext dateTimeParseContext) {
            final PrefixTree tree = newTree(dateTimeParseContext);
            for (final String s : set) {
                tree.add0(s, s);
            }
            return tree;
        }
        
        public PrefixTree copyTree() {
            final PrefixTree prefixTree = new PrefixTree(this.key, this.value, null);
            if (this.child != null) {
                prefixTree.child = this.child.copyTree();
            }
            if (this.sibling != null) {
                prefixTree.sibling = this.sibling.copyTree();
            }
            return prefixTree;
        }
        
        public boolean add(final String s, final String s2) {
            return this.add0(s, s2);
        }
        
        private boolean add0(String key, final String s) {
            key = this.toKey(key);
            final int prefixLength = this.prefixLength(key);
            if (prefixLength != this.key.length()) {
                final PrefixTree node = this.newNode(this.key.substring(prefixLength), this.value, this.child);
                this.key = key.substring(0, prefixLength);
                this.child = node;
                if (prefixLength < key.length()) {
                    this.child.sibling = this.newNode(key.substring(prefixLength), s, null);
                    this.value = null;
                }
                else {
                    this.value = s;
                }
                return true;
            }
            if (prefixLength < key.length()) {
                final String substring = key.substring(prefixLength);
                for (PrefixTree prefixTree = this.child; prefixTree != null; prefixTree = prefixTree.sibling) {
                    if (this.isEqual(prefixTree.c0, substring.charAt(0))) {
                        return prefixTree.add0(substring, s);
                    }
                }
                final PrefixTree node2 = this.newNode(substring, s, null);
                node2.sibling = this.child;
                this.child = node2;
                return true;
            }
            this.value = s;
            return true;
        }
        
        public String match(final CharSequence charSequence, int n, final int n2) {
            if (!this.prefixOf(charSequence, n, n2)) {
                return null;
            }
            if (this.child != null && (n += this.key.length()) != n2) {
                PrefixTree prefixTree = this.child;
                while (!this.isEqual(prefixTree.c0, charSequence.charAt(n))) {
                    prefixTree = prefixTree.sibling;
                    if (prefixTree == null) {
                        return this.value;
                    }
                }
                final String match = prefixTree.match(charSequence, n, n2);
                if (match != null) {
                    return match;
                }
                return this.value;
            }
            return this.value;
        }
        
        public String match(final CharSequence charSequence, final ParsePosition parsePosition) {
            final int index = parsePosition.getIndex();
            final int length = charSequence.length();
            if (!this.prefixOf(charSequence, index, length)) {
                return null;
            }
            final int n = index + this.key.length();
            Label_0108: {
                if (this.child != null && n != length) {
                    PrefixTree prefixTree = this.child;
                    while (!this.isEqual(prefixTree.c0, charSequence.charAt(n))) {
                        prefixTree = prefixTree.sibling;
                        if (prefixTree == null) {
                            break Label_0108;
                        }
                    }
                    parsePosition.setIndex(n);
                    final String match = prefixTree.match(charSequence, parsePosition);
                    if (match != null) {
                        return match;
                    }
                }
            }
            parsePosition.setIndex(n);
            return this.value;
        }
        
        protected String toKey(final String s) {
            return s;
        }
        
        protected PrefixTree newNode(final String s, final String s2, final PrefixTree prefixTree) {
            return new PrefixTree(s, s2, prefixTree);
        }
        
        protected boolean isEqual(final char c, final char c2) {
            return c == c2;
        }
        
        protected boolean prefixOf(final CharSequence charSequence, int n, final int n2) {
            if (charSequence instanceof String) {
                return ((String)charSequence).startsWith(this.key, n);
            }
            int length = this.key.length();
            if (length > n2 - n) {
                return false;
            }
            int n3 = 0;
            while (length-- > 0) {
                if (!this.isEqual(this.key.charAt(n3++), charSequence.charAt(n++))) {
                    return false;
                }
            }
            return true;
        }
        
        private int prefixLength(final String s) {
            int n;
            for (n = 0; n < s.length() && n < this.key.length(); ++n) {
                if (!this.isEqual(s.charAt(n), this.key.charAt(n))) {
                    return n;
                }
            }
            return n;
        }
        
        private static class CI extends PrefixTree
        {
            private CI(final String s, final String s2, final PrefixTree prefixTree) {
                super(s, s2, prefixTree);
            }
            
            @Override
            protected CI newNode(final String s, final String s2, final PrefixTree prefixTree) {
                return new CI(s, s2, prefixTree);
            }
            
            @Override
            protected boolean isEqual(final char c, final char c2) {
                return DateTimeParseContext.charEqualsIgnoreCase(c, c2);
            }
            
            @Override
            protected boolean prefixOf(final CharSequence charSequence, int n, final int n2) {
                int length = this.key.length();
                if (length > n2 - n) {
                    return false;
                }
                int n3 = 0;
                while (length-- > 0) {
                    if (!this.isEqual(this.key.charAt(n3++), charSequence.charAt(n++))) {
                        return false;
                    }
                }
                return true;
            }
        }
        
        private static class LENIENT extends CI
        {
            private LENIENT(final String s, final String s2, final PrefixTree prefixTree) {
                super(s, s2, prefixTree);
            }
            
            @Override
            protected CI newNode(final String s, final String s2, final PrefixTree prefixTree) {
                return new LENIENT(s, s2, prefixTree);
            }
            
            private boolean isLenientChar(final char c) {
                return c == ' ' || c == '_' || c == '/';
            }
            
            @Override
            protected String toKey(final String s) {
                for (int i = 0; i < s.length(); ++i) {
                    if (this.isLenientChar(s.charAt(i))) {
                        final StringBuilder sb = new StringBuilder(s.length());
                        sb.append(s, 0, i);
                        ++i;
                        while (i < s.length()) {
                            if (!this.isLenientChar(s.charAt(i))) {
                                sb.append(s.charAt(i));
                            }
                            ++i;
                        }
                        return sb.toString();
                    }
                }
                return s;
            }
            
            @Override
            public String match(final CharSequence charSequence, final ParsePosition parsePosition) {
                int index = parsePosition.getIndex();
                final int length = charSequence.length();
                final int length2 = this.key.length();
                int n = 0;
                while (n < length2 && index < length) {
                    if (this.isLenientChar(charSequence.charAt(index))) {
                        ++index;
                    }
                    else {
                        if (!this.isEqual(this.key.charAt(n++), charSequence.charAt(index++))) {
                            return null;
                        }
                        continue;
                    }
                }
                if (n != length2) {
                    return null;
                }
                Label_0210: {
                    if (this.child != null && index != length) {
                        int index2;
                        for (index2 = index; index2 < length && this.isLenientChar(charSequence.charAt(index2)); ++index2) {}
                        if (index2 < length) {
                            PrefixTree prefixTree = this.child;
                            while (!this.isEqual(prefixTree.c0, charSequence.charAt(index2))) {
                                prefixTree = prefixTree.sibling;
                                if (prefixTree == null) {
                                    break Label_0210;
                                }
                            }
                            parsePosition.setIndex(index2);
                            final String match = prefixTree.match(charSequence, parsePosition);
                            if (match != null) {
                                return match;
                            }
                        }
                    }
                }
                parsePosition.setIndex(index);
                return this.value;
            }
        }
    }
    
    static final class ReducedPrinterParser extends NumberPrinterParser
    {
        static final LocalDate BASE_DATE;
        private final int baseValue;
        private final ChronoLocalDate baseDate;
        
        ReducedPrinterParser(final TemporalField temporalField, final int n, final int n2, final int n3, final ChronoLocalDate chronoLocalDate) {
            this(temporalField, n, n2, n3, chronoLocalDate, 0);
            if (n < 1 || n > 10) {
                throw new IllegalArgumentException("The minWidth must be from 1 to 10 inclusive but was " + n);
            }
            if (n2 < 1 || n2 > 10) {
                throw new IllegalArgumentException("The maxWidth must be from 1 to 10 inclusive but was " + n);
            }
            if (n2 < n) {
                throw new IllegalArgumentException("Maximum width must exceed or equal the minimum width but " + n2 + " < " + n);
            }
            if (chronoLocalDate == null) {
                if (!temporalField.range().isValidValue(n3)) {
                    throw new IllegalArgumentException("The base value must be within the range of the field");
                }
                if (n3 + ReducedPrinterParser.EXCEED_POINTS[n2] > 2147483647L) {
                    throw new DateTimeException("Unable to add printer-parser as the range exceeds the capacity of an int");
                }
            }
        }
        
        private ReducedPrinterParser(final TemporalField temporalField, final int n, final int n2, final int baseValue, final ChronoLocalDate baseDate, final int n3) {
            super(temporalField, n, n2, SignStyle.NOT_NEGATIVE, n3);
            this.baseValue = baseValue;
            this.baseDate = baseDate;
        }
        
        @Override
        long getValue(final DateTimePrintContext dateTimePrintContext, final long n) {
            final long abs = Math.abs(n);
            int n2 = this.baseValue;
            if (this.baseDate != null) {
                n2 = Chronology.from(dateTimePrintContext.getTemporal()).date(this.baseDate).get(this.field);
            }
            if (n >= n2 && n < n2 + ReducedPrinterParser.EXCEED_POINTS[this.minWidth]) {
                return abs % ReducedPrinterParser.EXCEED_POINTS[this.minWidth];
            }
            return abs % ReducedPrinterParser.EXCEED_POINTS[this.maxWidth];
        }
        
        @Override
        int setValue(final DateTimeParseContext dateTimeParseContext, long n, final int n2, final int n3) {
            int n4 = this.baseValue;
            if (this.baseDate != null) {
                n4 = dateTimeParseContext.getEffectiveChronology().date(this.baseDate).get(this.field);
                dateTimeParseContext.addChronoChangedListener(p4 -> this.setValue(dateTimeParseContext, n, n2, n3));
            }
            if (n3 - n2 == this.minWidth && n >= 0L) {
                final long n5 = ReducedPrinterParser.EXCEED_POINTS[this.minWidth];
                final long n6 = n4 - n4 % n5;
                if (n4 > 0) {
                    n += n6;
                }
                else {
                    n = n6 - n;
                }
                if (n < n4) {
                    n += n5;
                }
            }
            return dateTimeParseContext.setParsedField(this.field, n, n2, n3);
        }
        
        @Override
        ReducedPrinterParser withFixedWidth() {
            if (this.subsequentWidth == -1) {
                return this;
            }
            return new ReducedPrinterParser(this.field, this.minWidth, this.maxWidth, this.baseValue, this.baseDate, -1);
        }
        
        @Override
        ReducedPrinterParser withSubsequentWidth(final int n) {
            return new ReducedPrinterParser(this.field, this.minWidth, this.maxWidth, this.baseValue, this.baseDate, this.subsequentWidth + n);
        }
        
        @Override
        boolean isFixedWidth(final DateTimeParseContext dateTimeParseContext) {
            return dateTimeParseContext.isStrict() && super.isFixedWidth(dateTimeParseContext);
        }
        
        @Override
        public String toString() {
            return "ReducedValue(" + this.field + "," + this.minWidth + "," + this.maxWidth + "," + ((this.baseDate != null) ? this.baseDate : Integer.valueOf(this.baseValue)) + ")";
        }
        
        static {
            BASE_DATE = LocalDate.of(2000, 1, 1);
        }
    }
    
    enum SettingsParser implements DateTimePrinterParser
    {
        SENSITIVE, 
        INSENSITIVE, 
        STRICT, 
        LENIENT;
        
        @Override
        public boolean format(final DateTimePrintContext dateTimePrintContext, final StringBuilder sb) {
            return true;
        }
        
        @Override
        public int parse(final DateTimeParseContext dateTimeParseContext, final CharSequence charSequence, final int n) {
            switch (this.ordinal()) {
                case 0: {
                    dateTimeParseContext.setCaseSensitive(true);
                    break;
                }
                case 1: {
                    dateTimeParseContext.setCaseSensitive(false);
                    break;
                }
                case 2: {
                    dateTimeParseContext.setStrict(true);
                    break;
                }
                case 3: {
                    dateTimeParseContext.setStrict(false);
                    break;
                }
            }
            return n;
        }
        
        @Override
        public String toString() {
            switch (this.ordinal()) {
                case 0: {
                    return "ParseCaseSensitive(true)";
                }
                case 1: {
                    return "ParseCaseSensitive(false)";
                }
                case 2: {
                    return "ParseStrict(true)";
                }
                case 3: {
                    return "ParseStrict(false)";
                }
                default: {
                    throw new IllegalStateException("Unreachable");
                }
            }
        }
    }
    
    static final class StringLiteralPrinterParser implements DateTimePrinterParser
    {
        private final String literal;
        
        StringLiteralPrinterParser(final String literal) {
            this.literal = literal;
        }
        
        @Override
        public boolean format(final DateTimePrintContext dateTimePrintContext, final StringBuilder sb) {
            sb.append(this.literal);
            return true;
        }
        
        @Override
        public int parse(final DateTimeParseContext dateTimeParseContext, final CharSequence charSequence, final int n) {
            if (n > charSequence.length() || n < 0) {
                throw new IndexOutOfBoundsException();
            }
            if (!dateTimeParseContext.subSequenceEquals(charSequence, n, this.literal, 0, this.literal.length())) {
                return ~n;
            }
            return n + this.literal.length();
        }
        
        @Override
        public String toString() {
            return "'" + this.literal.replace("'", "''") + "'";
        }
    }
    
    static final class TextPrinterParser implements DateTimePrinterParser
    {
        private final TemporalField field;
        private final TextStyle textStyle;
        private final DateTimeTextProvider provider;
        private volatile NumberPrinterParser numberPrinterParser;
        
        TextPrinterParser(final TemporalField field, final TextStyle textStyle, final DateTimeTextProvider provider) {
            this.field = field;
            this.textStyle = textStyle;
            this.provider = provider;
        }
        
        @Override
        public boolean format(final DateTimePrintContext dateTimePrintContext, final StringBuilder sb) {
            final Long value = dateTimePrintContext.getValue(this.field);
            if (value == null) {
                return false;
            }
            final Chronology chronology = dateTimePrintContext.getTemporal().query(TemporalQueries.chronology());
            String s;
            if (chronology == null || chronology == IsoChronology.INSTANCE) {
                s = this.provider.getText(this.field, value, this.textStyle, dateTimePrintContext.getLocale());
            }
            else {
                s = this.provider.getText(chronology, this.field, value, this.textStyle, dateTimePrintContext.getLocale());
            }
            if (s == null) {
                return this.numberPrinterParser().format(dateTimePrintContext, sb);
            }
            sb.append(s);
            return true;
        }
        
        @Override
        public int parse(final DateTimeParseContext dateTimeParseContext, final CharSequence charSequence, final int n) {
            final int length = charSequence.length();
            if (n < 0 || n > length) {
                throw new IndexOutOfBoundsException();
            }
            final TextStyle textStyle = dateTimeParseContext.isStrict() ? this.textStyle : null;
            final Chronology effectiveChronology = dateTimeParseContext.getEffectiveChronology();
            Iterator<Map.Entry<String, Long>> iterator;
            if (effectiveChronology == null || effectiveChronology == IsoChronology.INSTANCE) {
                iterator = this.provider.getTextIterator(this.field, textStyle, dateTimeParseContext.getLocale());
            }
            else {
                iterator = this.provider.getTextIterator(effectiveChronology, this.field, textStyle, dateTimeParseContext.getLocale());
            }
            if (iterator != null) {
                while (iterator.hasNext()) {
                    final Map.Entry<String, Long> entry = iterator.next();
                    final String s = entry.getKey();
                    if (dateTimeParseContext.subSequenceEquals(s, 0, charSequence, n, s.length())) {
                        return dateTimeParseContext.setParsedField(this.field, entry.getValue(), n, n + s.length());
                    }
                }
                if (dateTimeParseContext.isStrict()) {
                    return ~n;
                }
            }
            return this.numberPrinterParser().parse(dateTimeParseContext, charSequence, n);
        }
        
        private NumberPrinterParser numberPrinterParser() {
            if (this.numberPrinterParser == null) {
                this.numberPrinterParser = new NumberPrinterParser(this.field, 1, 19, SignStyle.NORMAL);
            }
            return this.numberPrinterParser;
        }
        
        @Override
        public String toString() {
            if (this.textStyle == TextStyle.FULL) {
                return "Text(" + this.field + ")";
            }
            return "Text(" + this.field + "," + this.textStyle + ")";
        }
    }
    
    static final class WeekBasedFieldPrinterParser implements DateTimePrinterParser
    {
        private char chr;
        private int count;
        
        WeekBasedFieldPrinterParser(final char chr, final int count) {
            this.chr = chr;
            this.count = count;
        }
        
        @Override
        public boolean format(final DateTimePrintContext dateTimePrintContext, final StringBuilder sb) {
            return this.printerParser(dateTimePrintContext.getLocale()).format(dateTimePrintContext, sb);
        }
        
        @Override
        public int parse(final DateTimeParseContext dateTimeParseContext, final CharSequence charSequence, final int n) {
            return this.printerParser(dateTimeParseContext.getLocale()).parse(dateTimeParseContext, charSequence, n);
        }
        
        private DateTimePrinterParser printerParser(final Locale locale) {
            final WeekFields of = WeekFields.of(locale);
            TemporalField temporalField = null;
            switch (this.chr) {
                case 'Y': {
                    final TemporalField weekBasedYear = of.weekBasedYear();
                    if (this.count == 2) {
                        return new ReducedPrinterParser(weekBasedYear, 2, 2, 0, (ChronoLocalDate)ReducedPrinterParser.BASE_DATE, 0);
                    }
                    return new NumberPrinterParser(weekBasedYear, this.count, 19, (this.count < 4) ? SignStyle.NORMAL : SignStyle.EXCEEDS_PAD, -1);
                }
                case 'c':
                case 'e': {
                    temporalField = of.dayOfWeek();
                    break;
                }
                case 'w': {
                    temporalField = of.weekOfWeekBasedYear();
                    break;
                }
                case 'W': {
                    temporalField = of.weekOfMonth();
                    break;
                }
                default: {
                    throw new IllegalStateException("unreachable");
                }
            }
            return new NumberPrinterParser(temporalField, (this.count == 2) ? 2 : 1, 2, SignStyle.NOT_NEGATIVE);
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder(30);
            sb.append("Localized(");
            if (this.chr == 'Y') {
                if (this.count == 1) {
                    sb.append("WeekBasedYear");
                }
                else if (this.count == 2) {
                    sb.append("ReducedValue(WeekBasedYear,2,2,2000-01-01)");
                }
                else {
                    sb.append("WeekBasedYear,").append(this.count).append(",").append(19).append(",").append((this.count < 4) ? SignStyle.NORMAL : SignStyle.EXCEEDS_PAD);
                }
            }
            else {
                switch (this.chr) {
                    case 'c':
                    case 'e': {
                        sb.append("DayOfWeek");
                        break;
                    }
                    case 'w': {
                        sb.append("WeekOfWeekBasedYear");
                        break;
                    }
                    case 'W': {
                        sb.append("WeekOfMonth");
                        break;
                    }
                }
                sb.append(",");
                sb.append(this.count);
            }
            sb.append(")");
            return sb.toString();
        }
    }
    
    static class ZoneIdPrinterParser implements DateTimePrinterParser
    {
        private final TemporalQuery<ZoneId> query;
        private final String description;
        private static volatile Map.Entry<Integer, PrefixTree> cachedPrefixTree;
        private static volatile Map.Entry<Integer, PrefixTree> cachedPrefixTreeCI;
        
        ZoneIdPrinterParser(final TemporalQuery<ZoneId> query, final String description) {
            this.query = query;
            this.description = description;
        }
        
        @Override
        public boolean format(final DateTimePrintContext dateTimePrintContext, final StringBuilder sb) {
            final ZoneId zoneId = dateTimePrintContext.getValue(this.query);
            if (zoneId == null) {
                return false;
            }
            sb.append(zoneId.getId());
            return true;
        }
        
        protected PrefixTree getTree(final DateTimeParseContext dateTimeParseContext) {
            final Set<String> availableZoneIds = ZoneRulesProvider.getAvailableZoneIds();
            final int size = availableZoneIds.size();
            Map.Entry<Integer, PrefixTree> entry = dateTimeParseContext.isCaseSensitive() ? ZoneIdPrinterParser.cachedPrefixTree : ZoneIdPrinterParser.cachedPrefixTreeCI;
            if (entry == null || entry.getKey() != size) {
                synchronized (this) {
                    entry = (dateTimeParseContext.isCaseSensitive() ? ZoneIdPrinterParser.cachedPrefixTree : ZoneIdPrinterParser.cachedPrefixTreeCI);
                    if (entry == null || entry.getKey() != size) {
                        entry = new AbstractMap.SimpleImmutableEntry<Integer, PrefixTree>(size, PrefixTree.newTree(availableZoneIds, dateTimeParseContext));
                        if (dateTimeParseContext.isCaseSensitive()) {
                            ZoneIdPrinterParser.cachedPrefixTree = entry;
                        }
                        else {
                            ZoneIdPrinterParser.cachedPrefixTreeCI = entry;
                        }
                    }
                }
            }
            return entry.getValue();
        }
        
        @Override
        public int parse(final DateTimeParseContext dateTimeParseContext, final CharSequence charSequence, final int n) {
            final int length = charSequence.length();
            if (n > length) {
                throw new IndexOutOfBoundsException();
            }
            if (n == length) {
                return ~n;
            }
            final char char1 = charSequence.charAt(n);
            if (char1 == '+' || char1 == '-') {
                return this.parseOffsetBased(dateTimeParseContext, charSequence, n, n, OffsetIdPrinterParser.INSTANCE_ID_Z);
            }
            if (length >= n + 2) {
                final char char2 = charSequence.charAt(n + 1);
                if (dateTimeParseContext.charEquals(char1, 'U') && dateTimeParseContext.charEquals(char2, 'T')) {
                    if (length >= n + 3 && dateTimeParseContext.charEquals(charSequence.charAt(n + 2), 'C')) {
                        return this.parseOffsetBased(dateTimeParseContext, charSequence, n, n + 3, OffsetIdPrinterParser.INSTANCE_ID_ZERO);
                    }
                    return this.parseOffsetBased(dateTimeParseContext, charSequence, n, n + 2, OffsetIdPrinterParser.INSTANCE_ID_ZERO);
                }
                else if (dateTimeParseContext.charEquals(char1, 'G') && length >= n + 3 && dateTimeParseContext.charEquals(char2, 'M') && dateTimeParseContext.charEquals(charSequence.charAt(n + 2), 'T')) {
                    return this.parseOffsetBased(dateTimeParseContext, charSequence, n, n + 3, OffsetIdPrinterParser.INSTANCE_ID_ZERO);
                }
            }
            final PrefixTree tree = this.getTree(dateTimeParseContext);
            final ParsePosition parsePosition = new ParsePosition(n);
            final String match = tree.match(charSequence, parsePosition);
            if (match != null) {
                dateTimeParseContext.setParsed(ZoneId.of(match));
                return parsePosition.getIndex();
            }
            if (dateTimeParseContext.charEquals(char1, 'Z')) {
                dateTimeParseContext.setParsed(ZoneOffset.UTC);
                return n + 1;
            }
            return ~n;
        }
        
        private int parseOffsetBased(final DateTimeParseContext dateTimeParseContext, final CharSequence charSequence, final int n, final int n2, final OffsetIdPrinterParser offsetIdPrinterParser) {
            final String upperCase = charSequence.toString().substring(n, n2).toUpperCase();
            if (n2 >= charSequence.length()) {
                dateTimeParseContext.setParsed(ZoneId.of(upperCase));
                return n2;
            }
            if (charSequence.charAt(n2) == '0' || dateTimeParseContext.charEquals(charSequence.charAt(n2), 'Z')) {
                dateTimeParseContext.setParsed(ZoneId.of(upperCase));
                return n2;
            }
            final DateTimeParseContext copy = dateTimeParseContext.copy();
            final int parse = offsetIdPrinterParser.parse(copy, charSequence, n2);
            try {
                if (parse >= 0) {
                    dateTimeParseContext.setParsed(ZoneId.ofOffset(upperCase, ZoneOffset.ofTotalSeconds((int)(long)copy.getParsed(ChronoField.OFFSET_SECONDS))));
                    return parse;
                }
                if (offsetIdPrinterParser == OffsetIdPrinterParser.INSTANCE_ID_Z) {
                    return ~n;
                }
                dateTimeParseContext.setParsed(ZoneId.of(upperCase));
                return n2;
            }
            catch (DateTimeException ex) {
                return ~n;
            }
        }
        
        @Override
        public String toString() {
            return this.description;
        }
    }
    
    static final class ZoneTextPrinterParser extends ZoneIdPrinterParser
    {
        private final TextStyle textStyle;
        private Set<String> preferredZones;
        private static final int STD = 0;
        private static final int DST = 1;
        private static final int GENERIC = 2;
        private static final Map<String, SoftReference<Map<Locale, String[]>>> cache;
        private final Map<Locale, Map.Entry<Integer, SoftReference<PrefixTree>>> cachedTree;
        private final Map<Locale, Map.Entry<Integer, SoftReference<PrefixTree>>> cachedTreeCI;
        
        ZoneTextPrinterParser(final TextStyle textStyle, final Set<ZoneId> set) {
            super(TemporalQueries.zone(), "ZoneText(" + textStyle + ")");
            this.cachedTree = new HashMap<Locale, Map.Entry<Integer, SoftReference<PrefixTree>>>();
            this.cachedTreeCI = new HashMap<Locale, Map.Entry<Integer, SoftReference<PrefixTree>>>();
            this.textStyle = Objects.requireNonNull(textStyle, "textStyle");
            if (set != null && set.size() != 0) {
                this.preferredZones = new HashSet<String>();
                final Iterator<ZoneId> iterator = set.iterator();
                while (iterator.hasNext()) {
                    this.preferredZones.add(iterator.next().getId());
                }
            }
        }
        
        private String getDisplayName(final String s, final int n, final Locale locale) {
            if (this.textStyle == TextStyle.NARROW) {
                return null;
            }
            final SoftReference<Map<Locale, String[]>> softReference = ZoneTextPrinterParser.cache.get(s);
            Map<Locale, String[]> map = null;
            String[] array;
            if (softReference == null || (map = softReference.get()) == null || (array = map.get(locale)) == null) {
                final String[] retrieveDisplayNames = TimeZoneNameUtility.retrieveDisplayNames(s, locale);
                if (retrieveDisplayNames == null) {
                    return null;
                }
                array = Arrays.copyOfRange(retrieveDisplayNames, 0, 7);
                array[5] = TimeZoneNameUtility.retrieveGenericDisplayName(s, 1, locale);
                if (array[5] == null) {
                    array[5] = array[0];
                }
                array[6] = TimeZoneNameUtility.retrieveGenericDisplayName(s, 0, locale);
                if (array[6] == null) {
                    array[6] = array[0];
                }
                if (map == null) {
                    map = new ConcurrentHashMap<Locale, String[]>();
                }
                map.put(locale, array);
                ZoneTextPrinterParser.cache.put(s, new SoftReference<Map<Locale, String[]>>(map));
            }
            switch (n) {
                case 0: {
                    return array[this.textStyle.zoneNameStyleIndex() + 1];
                }
                case 1: {
                    return array[this.textStyle.zoneNameStyleIndex() + 3];
                }
                default: {
                    return array[this.textStyle.zoneNameStyleIndex() + 5];
                }
            }
        }
        
        @Override
        public boolean format(final DateTimePrintContext dateTimePrintContext, final StringBuilder sb) {
            final ZoneId zoneId = dateTimePrintContext.getValue(TemporalQueries.zoneId());
            if (zoneId == null) {
                return false;
            }
            String id = zoneId.getId();
            if (!(zoneId instanceof ZoneOffset)) {
                final TemporalAccessor temporal = dateTimePrintContext.getTemporal();
                final String displayName = this.getDisplayName(id, temporal.isSupported(ChronoField.INSTANT_SECONDS) ? (zoneId.getRules().isDaylightSavings(Instant.from(temporal)) ? 1 : 0) : 2, dateTimePrintContext.getLocale());
                if (displayName != null) {
                    id = displayName;
                }
            }
            sb.append(id);
            return true;
        }
        
        @Override
        protected PrefixTree getTree(final DateTimeParseContext dateTimeParseContext) {
            if (this.textStyle == TextStyle.NARROW) {
                return super.getTree(dateTimeParseContext);
            }
            final Locale locale = dateTimeParseContext.getLocale();
            final boolean caseSensitive = dateTimeParseContext.isCaseSensitive();
            final Set<String> availableZoneIds = ZoneRulesProvider.getAvailableZoneIds();
            final int size = availableZoneIds.size();
            final Map<Locale, Map.Entry<Integer, SoftReference<PrefixTree>>> map = caseSensitive ? this.cachedTree : this.cachedTreeCI;
            final String[][] array = null;
            final AbstractMap.SimpleImmutableEntry<Integer, SoftReference<PrefixTree>> simpleImmutableEntry;
            PrefixTree tree;
            if ((simpleImmutableEntry = map.get(locale)) == null || simpleImmutableEntry.getKey() != size || (tree = simpleImmutableEntry.getValue().get()) == null) {
                tree = PrefixTree.newTree(dateTimeParseContext);
                final String[][] zoneStrings;
                final String[][] array2 = zoneStrings = TimeZoneNameUtility.getZoneStrings(locale);
                for (final String[] array3 : zoneStrings) {
                    final String s = array3[0];
                    if (availableZoneIds.contains(s)) {
                        tree.add(s, s);
                        final String zid = ZoneName.toZid(s, locale);
                        for (int j = (this.textStyle == TextStyle.FULL) ? 1 : 2; j < array3.length; j += 2) {
                            tree.add(array3[j], zid);
                        }
                    }
                }
                if (this.preferredZones != null) {
                    for (final String[] array5 : array2) {
                        final String s2 = array5[0];
                        if (this.preferredZones.contains(s2)) {
                            if (availableZoneIds.contains(s2)) {
                                for (int l = (this.textStyle == TextStyle.FULL) ? 1 : 2; l < array5.length; l += 2) {
                                    tree.add(array5[l], s2);
                                }
                            }
                        }
                    }
                }
                map.put(locale, new AbstractMap.SimpleImmutableEntry<Integer, SoftReference<PrefixTree>>(size, new SoftReference<PrefixTree>(tree)));
            }
            return tree;
        }
        
        static {
            cache = new ConcurrentHashMap<String, SoftReference<Map<Locale, String[]>>>();
        }
    }
}
