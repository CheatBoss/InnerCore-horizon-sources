package java.time.format;

import java.time.chrono.*;
import java.util.*;
import java.time.*;
import java.time.temporal.*;

final class Parsed implements TemporalAccessor
{
    final Map<TemporalField, Long> fieldValues;
    ZoneId zone;
    Chronology chrono;
    boolean leapSecond;
    private ResolverStyle resolverStyle;
    private ChronoLocalDate date;
    private LocalTime time;
    Period excessDays;
    
    Parsed() {
        this.fieldValues = new HashMap<TemporalField, Long>();
        this.excessDays = Period.ZERO;
    }
    
    Parsed copy() {
        final Parsed parsed = new Parsed();
        parsed.fieldValues.putAll(this.fieldValues);
        parsed.zone = this.zone;
        parsed.chrono = this.chrono;
        parsed.leapSecond = this.leapSecond;
        return parsed;
    }
    
    @Override
    public boolean isSupported(final TemporalField temporalField) {
        return this.fieldValues.containsKey(temporalField) || (this.date != null && this.date.isSupported(temporalField)) || (this.time != null && this.time.isSupported(temporalField)) || (temporalField != null && !(temporalField instanceof ChronoField) && temporalField.isSupportedBy(this));
    }
    
    @Override
    public long getLong(final TemporalField temporalField) {
        Objects.requireNonNull(temporalField, "field");
        final Long n = this.fieldValues.get(temporalField);
        if (n != null) {
            return n;
        }
        if (this.date != null && this.date.isSupported(temporalField)) {
            return this.date.getLong(temporalField);
        }
        if (this.time != null && this.time.isSupported(temporalField)) {
            return this.time.getLong(temporalField);
        }
        if (temporalField instanceof ChronoField) {
            throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
        }
        return temporalField.getFrom(this);
    }
    
    @Override
    public <R> R query(final TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.zoneId()) {
            return (R)this.zone;
        }
        if (temporalQuery == TemporalQueries.chronology()) {
            return (R)this.chrono;
        }
        if (temporalQuery == TemporalQueries.localDate()) {
            return (R)((this.date != null) ? LocalDate.from((TemporalAccessor)this.date) : null);
        }
        if (temporalQuery == TemporalQueries.localTime()) {
            return (R)this.time;
        }
        if (temporalQuery == TemporalQueries.zone() || temporalQuery == TemporalQueries.offset()) {
            return (R)temporalQuery.queryFrom(this);
        }
        if (temporalQuery == TemporalQueries.precision()) {
            return null;
        }
        return (R)temporalQuery.queryFrom(this);
    }
    
    TemporalAccessor resolve(final ResolverStyle resolverStyle, final Set<TemporalField> set) {
        if (set != null) {
            this.fieldValues.keySet().retainAll(set);
        }
        this.resolverStyle = resolverStyle;
        this.resolveFields();
        this.resolveTimeLenient();
        this.crossCheck();
        this.resolvePeriod();
        this.resolveFractional();
        this.resolveInstant();
        return this;
    }
    
    private void resolveFields() {
        this.resolveInstantFields();
        this.resolveDateFields();
        this.resolveTimeFields();
        if (this.fieldValues.size() > 0) {
            int i = 0;
        Label_0026:
            while (i < 50) {
                final Iterator<Map.Entry<TemporalField, Long>> iterator = this.fieldValues.entrySet().iterator();
                while (iterator.hasNext()) {
                    final TemporalField temporalField = iterator.next().getKey();
                    TemporalAccessor temporalAccessor = temporalField.resolve(this.fieldValues, this, this.resolverStyle);
                    if (temporalAccessor != null) {
                        if (temporalAccessor instanceof ChronoZonedDateTime) {
                            final ChronoZonedDateTime<ChronoLocalDate> chronoZonedDateTime = (ChronoZonedDateTime<ChronoLocalDate>)temporalAccessor;
                            if (this.zone == null) {
                                this.zone = chronoZonedDateTime.getZone();
                            }
                            else if (!this.zone.equals(chronoZonedDateTime.getZone())) {
                                throw new DateTimeException("ChronoZonedDateTime must use the effective parsed zone: " + this.zone);
                            }
                            temporalAccessor = chronoZonedDateTime.toLocalDateTime();
                        }
                        if (temporalAccessor instanceof ChronoLocalDateTime) {
                            final ChronoLocalDateTime<ChronoLocalDate> chronoLocalDateTime = (ChronoLocalDateTime<ChronoLocalDate>)temporalAccessor;
                            this.updateCheckConflict(chronoLocalDateTime.toLocalTime(), Period.ZERO);
                            this.updateCheckConflict(chronoLocalDateTime.toLocalDate());
                            ++i;
                            continue Label_0026;
                        }
                        if (temporalAccessor instanceof ChronoLocalDate) {
                            this.updateCheckConflict((ChronoLocalDate)temporalAccessor);
                            ++i;
                            continue Label_0026;
                        }
                        if (temporalAccessor instanceof LocalTime) {
                            this.updateCheckConflict((LocalTime)temporalAccessor, Period.ZERO);
                            ++i;
                            continue Label_0026;
                        }
                        throw new DateTimeException("Method resolve() can only return ChronoZonedDateTime, ChronoLocalDateTime, ChronoLocalDate or LocalTime");
                    }
                    else {
                        if (!this.fieldValues.containsKey(temporalField)) {
                            ++i;
                            continue Label_0026;
                        }
                        continue;
                    }
                }
                break;
            }
            if (i == 50) {
                throw new DateTimeException("One of the parsed fields has an incorrectly implemented resolve method");
            }
            if (i > 0) {
                this.resolveInstantFields();
                this.resolveDateFields();
                this.resolveTimeFields();
            }
        }
    }
    
    private void updateCheckConflict(final TemporalField temporalField, final TemporalField temporalField2, final Long n) {
        final Long n2 = this.fieldValues.put(temporalField2, n);
        if (n2 != null && n2 != (long)n) {
            throw new DateTimeException("Conflict found: " + temporalField2 + " " + n2 + " differs from " + temporalField2 + " " + n + " while resolving  " + temporalField);
        }
    }
    
    private void resolveInstantFields() {
        if (this.fieldValues.containsKey(ChronoField.INSTANT_SECONDS)) {
            if (this.zone != null) {
                this.resolveInstantFields0(this.zone);
            }
            else {
                final Long n = this.fieldValues.get(ChronoField.OFFSET_SECONDS);
                if (n != null) {
                    this.resolveInstantFields0(ZoneOffset.ofTotalSeconds(n.intValue()));
                }
            }
        }
    }
    
    private void resolveInstantFields0(final ZoneId zoneId) {
        final ChronoZonedDateTime<? extends ChronoLocalDate> zonedDateTime = this.chrono.zonedDateTime(Instant.ofEpochSecond(this.fieldValues.remove(ChronoField.INSTANT_SECONDS)), zoneId);
        this.updateCheckConflict((ChronoLocalDate)zonedDateTime.toLocalDate());
        this.updateCheckConflict(ChronoField.INSTANT_SECONDS, ChronoField.SECOND_OF_DAY, (long)zonedDateTime.toLocalTime().toSecondOfDay());
    }
    
    private void resolveDateFields() {
        this.updateCheckConflict(this.chrono.resolveDate(this.fieldValues, this.resolverStyle));
    }
    
    private void updateCheckConflict(final ChronoLocalDate date) {
        if (this.date != null) {
            if (date != null && !this.date.equals(date)) {
                throw new DateTimeException("Conflict found: Fields resolved to two different dates: " + this.date + " " + date);
            }
        }
        else if (date != null) {
            if (!this.chrono.equals(date.getChronology())) {
                throw new DateTimeException("ChronoLocalDate must use the effective parsed chronology: " + this.chrono);
            }
            this.date = date;
        }
    }
    
    private void resolveTimeFields() {
        if (this.fieldValues.containsKey(ChronoField.CLOCK_HOUR_OF_DAY)) {
            final long longValue = this.fieldValues.remove(ChronoField.CLOCK_HOUR_OF_DAY);
            if (this.resolverStyle == ResolverStyle.STRICT || (this.resolverStyle == ResolverStyle.SMART && longValue != 0L)) {
                ChronoField.CLOCK_HOUR_OF_DAY.checkValidValue(longValue);
            }
            this.updateCheckConflict(ChronoField.CLOCK_HOUR_OF_DAY, ChronoField.HOUR_OF_DAY, (longValue == 24L) ? 0L : longValue);
        }
        if (this.fieldValues.containsKey(ChronoField.CLOCK_HOUR_OF_AMPM)) {
            final long longValue2 = this.fieldValues.remove(ChronoField.CLOCK_HOUR_OF_AMPM);
            if (this.resolverStyle == ResolverStyle.STRICT || (this.resolverStyle == ResolverStyle.SMART && longValue2 != 0L)) {
                ChronoField.CLOCK_HOUR_OF_AMPM.checkValidValue(longValue2);
            }
            this.updateCheckConflict(ChronoField.CLOCK_HOUR_OF_AMPM, ChronoField.HOUR_OF_AMPM, (longValue2 == 12L) ? 0L : longValue2);
        }
        if (this.fieldValues.containsKey(ChronoField.AMPM_OF_DAY) && this.fieldValues.containsKey(ChronoField.HOUR_OF_AMPM)) {
            final long longValue3 = this.fieldValues.remove(ChronoField.AMPM_OF_DAY);
            final long longValue4 = this.fieldValues.remove(ChronoField.HOUR_OF_AMPM);
            if (this.resolverStyle == ResolverStyle.LENIENT) {
                this.updateCheckConflict(ChronoField.AMPM_OF_DAY, ChronoField.HOUR_OF_DAY, Math.addExact(Math.multiplyExact(longValue3, 12L), longValue4));
            }
            else {
                ChronoField.AMPM_OF_DAY.checkValidValue(longValue3);
                ChronoField.HOUR_OF_AMPM.checkValidValue(longValue3);
                this.updateCheckConflict(ChronoField.AMPM_OF_DAY, ChronoField.HOUR_OF_DAY, longValue3 * 12L + longValue4);
            }
        }
        if (this.fieldValues.containsKey(ChronoField.NANO_OF_DAY)) {
            final long longValue5 = this.fieldValues.remove(ChronoField.NANO_OF_DAY);
            if (this.resolverStyle != ResolverStyle.LENIENT) {
                ChronoField.NANO_OF_DAY.checkValidValue(longValue5);
            }
            this.updateCheckConflict(ChronoField.NANO_OF_DAY, ChronoField.HOUR_OF_DAY, longValue5 / 3600000000000L);
            this.updateCheckConflict(ChronoField.NANO_OF_DAY, ChronoField.MINUTE_OF_HOUR, longValue5 / 60000000000L % 60L);
            this.updateCheckConflict(ChronoField.NANO_OF_DAY, ChronoField.SECOND_OF_MINUTE, longValue5 / 1000000000L % 60L);
            this.updateCheckConflict(ChronoField.NANO_OF_DAY, ChronoField.NANO_OF_SECOND, longValue5 % 1000000000L);
        }
        if (this.fieldValues.containsKey(ChronoField.MICRO_OF_DAY)) {
            final long longValue6 = this.fieldValues.remove(ChronoField.MICRO_OF_DAY);
            if (this.resolverStyle != ResolverStyle.LENIENT) {
                ChronoField.MICRO_OF_DAY.checkValidValue(longValue6);
            }
            this.updateCheckConflict(ChronoField.MICRO_OF_DAY, ChronoField.SECOND_OF_DAY, longValue6 / 1000000L);
            this.updateCheckConflict(ChronoField.MICRO_OF_DAY, ChronoField.MICRO_OF_SECOND, longValue6 % 1000000L);
        }
        if (this.fieldValues.containsKey(ChronoField.MILLI_OF_DAY)) {
            final long longValue7 = this.fieldValues.remove(ChronoField.MILLI_OF_DAY);
            if (this.resolverStyle != ResolverStyle.LENIENT) {
                ChronoField.MILLI_OF_DAY.checkValidValue(longValue7);
            }
            this.updateCheckConflict(ChronoField.MILLI_OF_DAY, ChronoField.SECOND_OF_DAY, longValue7 / 1000L);
            this.updateCheckConflict(ChronoField.MILLI_OF_DAY, ChronoField.MILLI_OF_SECOND, longValue7 % 1000L);
        }
        if (this.fieldValues.containsKey(ChronoField.SECOND_OF_DAY)) {
            final long longValue8 = this.fieldValues.remove(ChronoField.SECOND_OF_DAY);
            if (this.resolverStyle != ResolverStyle.LENIENT) {
                ChronoField.SECOND_OF_DAY.checkValidValue(longValue8);
            }
            this.updateCheckConflict(ChronoField.SECOND_OF_DAY, ChronoField.HOUR_OF_DAY, longValue8 / 3600L);
            this.updateCheckConflict(ChronoField.SECOND_OF_DAY, ChronoField.MINUTE_OF_HOUR, longValue8 / 60L % 60L);
            this.updateCheckConflict(ChronoField.SECOND_OF_DAY, ChronoField.SECOND_OF_MINUTE, longValue8 % 60L);
        }
        if (this.fieldValues.containsKey(ChronoField.MINUTE_OF_DAY)) {
            final long longValue9 = this.fieldValues.remove(ChronoField.MINUTE_OF_DAY);
            if (this.resolverStyle != ResolverStyle.LENIENT) {
                ChronoField.MINUTE_OF_DAY.checkValidValue(longValue9);
            }
            this.updateCheckConflict(ChronoField.MINUTE_OF_DAY, ChronoField.HOUR_OF_DAY, longValue9 / 60L);
            this.updateCheckConflict(ChronoField.MINUTE_OF_DAY, ChronoField.MINUTE_OF_HOUR, longValue9 % 60L);
        }
        if (this.fieldValues.containsKey(ChronoField.NANO_OF_SECOND)) {
            long longValue10 = this.fieldValues.get(ChronoField.NANO_OF_SECOND);
            if (this.resolverStyle != ResolverStyle.LENIENT) {
                ChronoField.NANO_OF_SECOND.checkValidValue(longValue10);
            }
            if (this.fieldValues.containsKey(ChronoField.MICRO_OF_SECOND)) {
                final long longValue11 = this.fieldValues.remove(ChronoField.MICRO_OF_SECOND);
                if (this.resolverStyle != ResolverStyle.LENIENT) {
                    ChronoField.MICRO_OF_SECOND.checkValidValue(longValue11);
                }
                longValue10 = longValue11 * 1000L + longValue10 % 1000L;
                this.updateCheckConflict(ChronoField.MICRO_OF_SECOND, ChronoField.NANO_OF_SECOND, longValue10);
            }
            if (this.fieldValues.containsKey(ChronoField.MILLI_OF_SECOND)) {
                final long longValue12 = this.fieldValues.remove(ChronoField.MILLI_OF_SECOND);
                if (this.resolverStyle != ResolverStyle.LENIENT) {
                    ChronoField.MILLI_OF_SECOND.checkValidValue(longValue12);
                }
                this.updateCheckConflict(ChronoField.MILLI_OF_SECOND, ChronoField.NANO_OF_SECOND, longValue12 * 1000000L + longValue10 % 1000000L);
            }
        }
        if (this.fieldValues.containsKey(ChronoField.HOUR_OF_DAY) && this.fieldValues.containsKey(ChronoField.MINUTE_OF_HOUR) && this.fieldValues.containsKey(ChronoField.SECOND_OF_MINUTE) && this.fieldValues.containsKey(ChronoField.NANO_OF_SECOND)) {
            this.resolveTime(this.fieldValues.remove(ChronoField.HOUR_OF_DAY), this.fieldValues.remove(ChronoField.MINUTE_OF_HOUR), this.fieldValues.remove(ChronoField.SECOND_OF_MINUTE), this.fieldValues.remove(ChronoField.NANO_OF_SECOND));
        }
    }
    
    private void resolveTimeLenient() {
        if (this.time == null) {
            if (this.fieldValues.containsKey(ChronoField.MILLI_OF_SECOND)) {
                final long longValue = this.fieldValues.remove(ChronoField.MILLI_OF_SECOND);
                if (this.fieldValues.containsKey(ChronoField.MICRO_OF_SECOND)) {
                    final long n = longValue * 1000L + this.fieldValues.get(ChronoField.MICRO_OF_SECOND) % 1000L;
                    this.updateCheckConflict(ChronoField.MILLI_OF_SECOND, ChronoField.MICRO_OF_SECOND, n);
                    this.fieldValues.remove(ChronoField.MICRO_OF_SECOND);
                    this.fieldValues.put(ChronoField.NANO_OF_SECOND, n * 1000L);
                }
                else {
                    this.fieldValues.put(ChronoField.NANO_OF_SECOND, longValue * 1000000L);
                }
            }
            else if (this.fieldValues.containsKey(ChronoField.MICRO_OF_SECOND)) {
                this.fieldValues.put(ChronoField.NANO_OF_SECOND, this.fieldValues.remove(ChronoField.MICRO_OF_SECOND) * 1000L);
            }
            final Long n2 = this.fieldValues.get(ChronoField.HOUR_OF_DAY);
            if (n2 != null) {
                final Long n3 = this.fieldValues.get(ChronoField.MINUTE_OF_HOUR);
                final Long n4 = this.fieldValues.get(ChronoField.SECOND_OF_MINUTE);
                final Long n5 = this.fieldValues.get(ChronoField.NANO_OF_SECOND);
                if ((n3 == null && (n4 != null || n5 != null)) || (n3 != null && n4 == null && n5 != null)) {
                    return;
                }
                this.resolveTime(n2, (n3 != null) ? ((long)n3) : 0L, (n4 != null) ? ((long)n4) : 0L, (n5 != null) ? ((long)n5) : 0L);
                this.fieldValues.remove(ChronoField.HOUR_OF_DAY);
                this.fieldValues.remove(ChronoField.MINUTE_OF_HOUR);
                this.fieldValues.remove(ChronoField.SECOND_OF_MINUTE);
                this.fieldValues.remove(ChronoField.NANO_OF_SECOND);
            }
        }
        if (this.resolverStyle != ResolverStyle.LENIENT && this.fieldValues.size() > 0) {
            for (final Map.Entry<TemporalField, Long> entry : this.fieldValues.entrySet()) {
                final TemporalField temporalField = entry.getKey();
                if (temporalField instanceof ChronoField && temporalField.isTimeBased()) {
                    ((ChronoField)temporalField).checkValidValue(entry.getValue());
                }
            }
        }
    }
    
    private void resolveTime(final long n, final long n2, final long n3, final long n4) {
        if (this.resolverStyle == ResolverStyle.LENIENT) {
            final long addExact = Math.addExact(Math.addExact(Math.addExact(Math.multiplyExact(n, 3600000000000L), Math.multiplyExact(n2, 60000000000L)), Math.multiplyExact(n3, 1000000000L)), n4);
            this.updateCheckConflict(LocalTime.ofNanoOfDay(Math.floorMod(addExact, 86400000000000L)), Period.ofDays((int)Math.floorDiv(addExact, 86400000000000L)));
        }
        else {
            final int checkValidIntValue = ChronoField.MINUTE_OF_HOUR.checkValidIntValue(n2);
            final int checkValidIntValue2 = ChronoField.NANO_OF_SECOND.checkValidIntValue(n4);
            if (this.resolverStyle == ResolverStyle.SMART && n == 24L && checkValidIntValue == 0 && n3 == 0L && checkValidIntValue2 == 0) {
                this.updateCheckConflict(LocalTime.MIDNIGHT, Period.ofDays(1));
            }
            else {
                this.updateCheckConflict(LocalTime.of(ChronoField.HOUR_OF_DAY.checkValidIntValue(n), checkValidIntValue, ChronoField.SECOND_OF_MINUTE.checkValidIntValue(n3), checkValidIntValue2), Period.ZERO);
            }
        }
    }
    
    private void resolvePeriod() {
        if (this.date != null && this.time != null && !this.excessDays.isZero()) {
            this.date = this.date.plus((TemporalAmount)this.excessDays);
            this.excessDays = Period.ZERO;
        }
    }
    
    private void resolveFractional() {
        if (this.time == null && (this.fieldValues.containsKey(ChronoField.INSTANT_SECONDS) || this.fieldValues.containsKey(ChronoField.SECOND_OF_DAY) || this.fieldValues.containsKey(ChronoField.SECOND_OF_MINUTE))) {
            if (this.fieldValues.containsKey(ChronoField.NANO_OF_SECOND)) {
                final long longValue = this.fieldValues.get(ChronoField.NANO_OF_SECOND);
                this.fieldValues.put(ChronoField.MICRO_OF_SECOND, longValue / 1000L);
                this.fieldValues.put(ChronoField.MILLI_OF_SECOND, longValue / 1000000L);
            }
            else {
                this.fieldValues.put(ChronoField.NANO_OF_SECOND, 0L);
                this.fieldValues.put(ChronoField.MICRO_OF_SECOND, 0L);
                this.fieldValues.put(ChronoField.MILLI_OF_SECOND, 0L);
            }
        }
    }
    
    private void resolveInstant() {
        if (this.date != null && this.time != null) {
            if (this.zone != null) {
                this.fieldValues.put(ChronoField.INSTANT_SECONDS, this.date.atTime(this.time).atZone(this.zone).getLong(ChronoField.INSTANT_SECONDS));
            }
            else {
                final Long n = this.fieldValues.get(ChronoField.OFFSET_SECONDS);
                if (n != null) {
                    this.fieldValues.put(ChronoField.INSTANT_SECONDS, this.date.atTime(this.time).atZone(ZoneOffset.ofTotalSeconds(n.intValue())).getLong(ChronoField.INSTANT_SECONDS));
                }
            }
        }
    }
    
    private void updateCheckConflict(final LocalTime time, final Period period) {
        if (this.time != null) {
            if (!this.time.equals(time)) {
                throw new DateTimeException("Conflict found: Fields resolved to different times: " + this.time + " " + time);
            }
            if (!this.excessDays.isZero() && !period.isZero() && !this.excessDays.equals(period)) {
                throw new DateTimeException("Conflict found: Fields resolved to different excess periods: " + this.excessDays + " " + period);
            }
            this.excessDays = period;
        }
        else {
            this.time = time;
            this.excessDays = period;
        }
    }
    
    private void crossCheck() {
        if (this.date != null) {
            this.crossCheck(this.date);
        }
        if (this.time != null) {
            this.crossCheck(this.time);
            if (this.date != null && this.fieldValues.size() > 0) {
                this.crossCheck(this.date.atTime(this.time));
            }
        }
    }
    
    private void crossCheck(final TemporalAccessor temporalAccessor) {
        final Iterator<Map.Entry<TemporalField, Long>> iterator = this.fieldValues.entrySet().iterator();
        while (iterator.hasNext()) {
            final Map.Entry<TemporalField, Long> entry = iterator.next();
            final TemporalField temporalField = entry.getKey();
            if (temporalAccessor.isSupported(temporalField)) {
                long long1;
                try {
                    long1 = temporalAccessor.getLong(temporalField);
                }
                catch (RuntimeException ex) {
                    continue;
                }
                final long longValue = entry.getValue();
                if (long1 != longValue) {
                    throw new DateTimeException("Conflict found: Field " + temporalField + " " + long1 + " differs from " + temporalField + " " + longValue + " derived from " + temporalAccessor);
                }
                iterator.remove();
            }
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(64);
        sb.append(this.fieldValues).append(',').append(this.chrono);
        if (this.zone != null) {
            sb.append(',').append(this.zone);
        }
        if (this.date != null || this.time != null) {
            sb.append(" resolved to ");
            if (this.date != null) {
                sb.append(this.date);
                if (this.time != null) {
                    sb.append('T').append(this.time);
                }
            }
            else {
                sb.append(this.time);
            }
        }
        return sb.toString();
    }
}
