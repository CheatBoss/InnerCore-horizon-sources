package java.time.temporal;

import java.util.*;
import java.time.format.*;

public final class TemporalField-CC
{
    public static String $default$getDisplayName(final TemporalField temporalField, final Locale locale) {
        Objects.requireNonNull(locale, "locale");
        return temporalField.toString();
    }
    
    public static TemporalAccessor $default$resolve(final TemporalField temporalField, final Map<TemporalField, Long> map, final TemporalAccessor temporalAccessor, final ResolverStyle resolverStyle) {
        return null;
    }
}
