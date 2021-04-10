package java.util;

import java.time.*;

public class DesugarTimeZone
{
    private DesugarTimeZone() {
    }
    
    public static TimeZone getTimeZone(final String s) {
        return TimeZone.getTimeZone(s);
    }
    
    public static TimeZone getTimeZone(final ZoneId zoneId) {
        final String id = zoneId.getId();
        final char char1 = id.charAt(0);
        String string;
        if (char1 != '+' && char1 != '-') {
            string = id;
            if (char1 == 'Z') {
                string = id;
                if (id.length() == 1) {
                    string = "UTC";
                }
            }
        }
        else {
            final StringBuilder sb = new StringBuilder();
            sb.append("GMT");
            sb.append(id);
            string = sb.toString();
        }
        return TimeZone.getTimeZone(string);
    }
    
    public static ZoneId toZoneId(final TimeZone timeZone) {
        return ZoneId.of(timeZone.getID(), ZoneId.SHORT_IDS);
    }
}
