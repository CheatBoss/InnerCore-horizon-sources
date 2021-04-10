package java.util;

import java.time.*;

public class DesugarDate
{
    private DesugarDate() {
    }
    
    public static Date from(final Instant instant) {
        try {
            return new Date(instant.toEpochMilli());
        }
        catch (ArithmeticException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
    
    public static Instant toInstant(final Date date) {
        return Instant.ofEpochMilli(date.getTime());
    }
}
