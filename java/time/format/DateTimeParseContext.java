package java.time.format;

import java.util.function.*;
import java.time.chrono.*;
import java.time.temporal.*;
import java.util.*;
import java.time.*;

final class DateTimeParseContext
{
    private DateTimeFormatter formatter;
    private boolean caseSensitive;
    private boolean strict;
    private final ArrayList<Parsed> parsed;
    private ArrayList<Consumer<Chronology>> chronoListeners;
    
    DateTimeParseContext(final DateTimeFormatter formatter) {
        this.caseSensitive = true;
        this.strict = true;
        this.parsed = new ArrayList<Parsed>();
        this.chronoListeners = null;
        this.formatter = formatter;
        this.parsed.add(new Parsed());
    }
    
    DateTimeParseContext copy() {
        final DateTimeParseContext dateTimeParseContext = new DateTimeParseContext(this.formatter);
        dateTimeParseContext.caseSensitive = this.caseSensitive;
        dateTimeParseContext.strict = this.strict;
        return dateTimeParseContext;
    }
    
    Locale getLocale() {
        return this.formatter.getLocale();
    }
    
    DecimalStyle getDecimalStyle() {
        return this.formatter.getDecimalStyle();
    }
    
    Chronology getEffectiveChronology() {
        Chronology chronology = this.currentParsed().chrono;
        if (chronology == null) {
            chronology = this.formatter.getChronology();
            if (chronology == null) {
                chronology = IsoChronology.INSTANCE;
            }
        }
        return chronology;
    }
    
    boolean isCaseSensitive() {
        return this.caseSensitive;
    }
    
    void setCaseSensitive(final boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }
    
    boolean subSequenceEquals(final CharSequence charSequence, final int n, final CharSequence charSequence2, final int n2, final int n3) {
        if (n + n3 > charSequence.length() || n2 + n3 > charSequence2.length()) {
            return false;
        }
        if (this.isCaseSensitive()) {
            for (int i = 0; i < n3; ++i) {
                if (charSequence.charAt(n + i) != charSequence2.charAt(n2 + i)) {
                    return false;
                }
            }
        }
        else {
            for (int j = 0; j < n3; ++j) {
                final char char1 = charSequence.charAt(n + j);
                final char char2 = charSequence2.charAt(n2 + j);
                if (char1 != char2 && Character.toUpperCase(char1) != Character.toUpperCase(char2) && Character.toLowerCase(char1) != Character.toLowerCase(char2)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    boolean charEquals(final char c, final char c2) {
        if (this.isCaseSensitive()) {
            return c == c2;
        }
        return charEqualsIgnoreCase(c, c2);
    }
    
    static boolean charEqualsIgnoreCase(final char c, final char c2) {
        return c == c2 || Character.toUpperCase(c) == Character.toUpperCase(c2) || Character.toLowerCase(c) == Character.toLowerCase(c2);
    }
    
    boolean isStrict() {
        return this.strict;
    }
    
    void setStrict(final boolean strict) {
        this.strict = strict;
    }
    
    void startOptional() {
        this.parsed.add(this.currentParsed().copy());
    }
    
    void endOptional(final boolean b) {
        if (b) {
            this.parsed.remove(this.parsed.size() - 2);
        }
        else {
            this.parsed.remove(this.parsed.size() - 1);
        }
    }
    
    private Parsed currentParsed() {
        return this.parsed.get(this.parsed.size() - 1);
    }
    
    Parsed toUnresolved() {
        return this.currentParsed();
    }
    
    TemporalAccessor toResolved(final ResolverStyle resolverStyle, final Set<TemporalField> set) {
        final Parsed currentParsed = this.currentParsed();
        currentParsed.chrono = this.getEffectiveChronology();
        currentParsed.zone = ((currentParsed.zone != null) ? currentParsed.zone : this.formatter.getZone());
        return currentParsed.resolve(resolverStyle, set);
    }
    
    Long getParsed(final TemporalField temporalField) {
        return this.currentParsed().fieldValues.get(temporalField);
    }
    
    int setParsedField(final TemporalField temporalField, final long n, final int n2, final int n3) {
        Objects.requireNonNull(temporalField, "field");
        final Long n4 = this.currentParsed().fieldValues.put(temporalField, n);
        return (n4 != null && n4 != n) ? (~n2) : n3;
    }
    
    void setParsed(final Chronology chrono) {
        Objects.requireNonNull(chrono, "chrono");
        this.currentParsed().chrono = chrono;
        if (this.chronoListeners != null && !this.chronoListeners.isEmpty()) {
            final Consumer[] array = this.chronoListeners.toArray(new Consumer[1]);
            this.chronoListeners.clear();
            final Consumer[] array2 = array;
            for (int length = array2.length, i = 0; i < length; ++i) {
                array2[i].accept(chrono);
            }
        }
    }
    
    void addChronoChangedListener(final Consumer<Chronology> consumer) {
        if (this.chronoListeners == null) {
            this.chronoListeners = new ArrayList<Consumer<Chronology>>();
        }
        this.chronoListeners.add(consumer);
    }
    
    void setParsed(final ZoneId zone) {
        Objects.requireNonNull(zone, "zone");
        this.currentParsed().zone = zone;
    }
    
    void setParsedLeapSecond() {
        this.currentParsed().leapSecond = true;
    }
    
    @Override
    public String toString() {
        return this.currentParsed().toString();
    }
}
