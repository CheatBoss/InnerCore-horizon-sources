package java.time.chrono;

import java.time.*;
import java.util.*;
import java.time.format.*;
import java.time.temporal.*;
import java.io.*;
import sun.util.calendar.*;

public final class JapaneseEra implements Era, Serializable
{
    static final int ERA_OFFSET = 2;
    static final sun.util.calendar.Era[] ERA_CONFIG;
    public static final JapaneseEra MEIJI;
    public static final JapaneseEra TAISHO;
    public static final JapaneseEra SHOWA;
    public static final JapaneseEra HEISEI;
    private static final JapaneseEra REIWA;
    private static final int N_ERA_CONSTANTS;
    private static final long serialVersionUID = 1466499369062886794L;
    private static final JapaneseEra[] KNOWN_ERAS;
    private final transient int eraValue;
    private final transient LocalDate since;
    
    private JapaneseEra(final int eraValue, final LocalDate since) {
        this.eraValue = eraValue;
        this.since = since;
    }
    
    sun.util.calendar.Era getPrivateEra() {
        return JapaneseEra.ERA_CONFIG[ordinal(this.eraValue)];
    }
    
    public static JapaneseEra of(final int n) {
        if (n < JapaneseEra.MEIJI.eraValue || n + 2 > JapaneseEra.KNOWN_ERAS.length) {
            throw new DateTimeException("Invalid era: " + n);
        }
        return JapaneseEra.KNOWN_ERAS[ordinal(n)];
    }
    
    public static JapaneseEra valueOf(final String s) {
        Objects.requireNonNull(s, "japaneseEra");
        for (final JapaneseEra japaneseEra : JapaneseEra.KNOWN_ERAS) {
            if (japaneseEra.getName().equals(s)) {
                return japaneseEra;
            }
        }
        throw new IllegalArgumentException("japaneseEra is invalid");
    }
    
    public static JapaneseEra[] values() {
        return Arrays.copyOf(JapaneseEra.KNOWN_ERAS, JapaneseEra.KNOWN_ERAS.length);
    }
    
    @Override
    public String getDisplayName(final TextStyle textStyle, final Locale locale) {
        if (this.getValue() > JapaneseEra.N_ERA_CONSTANTS - 2) {
            Objects.requireNonNull(locale, "locale");
            return (textStyle.asNormal() == TextStyle.NARROW) ? this.getAbbreviation() : this.getName();
        }
        return new DateTimeFormatterBuilder().appendText(ChronoField.ERA, textStyle).toFormatter(locale).withChronology(JapaneseChronology.INSTANCE).format((this == JapaneseEra.MEIJI) ? JapaneseDate.MEIJI_6_ISODATE : this.since);
    }
    
    static JapaneseEra from(final LocalDate localDate) {
        if (localDate.isBefore(JapaneseDate.MEIJI_6_ISODATE)) {
            throw new DateTimeException("JapaneseDate before Meiji 6 are not supported");
        }
        for (int i = JapaneseEra.KNOWN_ERAS.length - 1; i > 0; --i) {
            final JapaneseEra japaneseEra = JapaneseEra.KNOWN_ERAS[i];
            if (localDate.compareTo((ChronoLocalDate)japaneseEra.since) >= 0) {
                return japaneseEra;
            }
        }
        return null;
    }
    
    static JapaneseEra toJapaneseEra(final sun.util.calendar.Era era) {
        for (int i = JapaneseEra.ERA_CONFIG.length - 1; i >= 0; --i) {
            if (JapaneseEra.ERA_CONFIG[i].equals(era)) {
                return JapaneseEra.KNOWN_ERAS[i];
            }
        }
        return null;
    }
    
    static sun.util.calendar.Era privateEraFrom(final LocalDate localDate) {
        for (int i = JapaneseEra.KNOWN_ERAS.length - 1; i > 0; --i) {
            if (localDate.compareTo((ChronoLocalDate)JapaneseEra.KNOWN_ERAS[i].since) >= 0) {
                return JapaneseEra.ERA_CONFIG[i];
            }
        }
        return null;
    }
    
    private static int ordinal(final int n) {
        return n + 2 - 1;
    }
    
    @Override
    public int getValue() {
        return this.eraValue;
    }
    
    @Override
    public ValueRange range(final TemporalField temporalField) {
        if (temporalField == ChronoField.ERA) {
            return JapaneseChronology.INSTANCE.range(ChronoField.ERA);
        }
        return super.range(temporalField);
    }
    
    String getAbbreviation() {
        return JapaneseEra.ERA_CONFIG[ordinal(this.getValue())].getAbbreviation();
    }
    
    String getName() {
        return JapaneseEra.ERA_CONFIG[ordinal(this.getValue())].getName();
    }
    
    @Override
    public String toString() {
        return this.getName();
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
    
    private Object writeReplace() {
        return new Ser((byte)5, this);
    }
    
    void writeExternal(final DataOutput dataOutput) throws IOException {
        dataOutput.writeByte(this.getValue());
    }
    
    static JapaneseEra readExternal(final DataInput dataInput) throws IOException {
        return of(dataInput.readByte());
    }
    
    static {
        MEIJI = new JapaneseEra(-1, LocalDate.of(1868, 1, 1));
        TAISHO = new JapaneseEra(0, LocalDate.of(1912, 7, 30));
        SHOWA = new JapaneseEra(1, LocalDate.of(1926, 12, 25));
        HEISEI = new JapaneseEra(2, LocalDate.of(1989, 1, 8));
        REIWA = new JapaneseEra(3, LocalDate.of(2019, 5, 1));
        N_ERA_CONSTANTS = JapaneseEra.REIWA.getValue() + 2;
        ERA_CONFIG = JapaneseChronology.JCAL.getEras();
        (KNOWN_ERAS = new JapaneseEra[JapaneseEra.ERA_CONFIG.length])[0] = JapaneseEra.MEIJI;
        JapaneseEra.KNOWN_ERAS[1] = JapaneseEra.TAISHO;
        JapaneseEra.KNOWN_ERAS[2] = JapaneseEra.SHOWA;
        JapaneseEra.KNOWN_ERAS[3] = JapaneseEra.HEISEI;
        JapaneseEra.KNOWN_ERAS[4] = JapaneseEra.REIWA;
        for (int i = JapaneseEra.N_ERA_CONSTANTS; i < JapaneseEra.ERA_CONFIG.length; ++i) {
            final CalendarDate sinceDate = JapaneseEra.ERA_CONFIG[i].getSinceDate();
            JapaneseEra.KNOWN_ERAS[i] = new JapaneseEra(i - 2 + 1, LocalDate.of(sinceDate.getYear(), sinceDate.getMonth(), sinceDate.getDayOfMonth()));
        }
    }
}
