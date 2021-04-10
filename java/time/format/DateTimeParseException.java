package java.time.format;

import java.time.*;

public class DateTimeParseException extends DateTimeException
{
    private static final long serialVersionUID = 4304633501674722597L;
    private final String parsedString;
    private final int errorIndex;
    
    public DateTimeParseException(final String s, final CharSequence charSequence, final int errorIndex) {
        super(s);
        this.parsedString = charSequence.toString();
        this.errorIndex = errorIndex;
    }
    
    public DateTimeParseException(final String s, final CharSequence charSequence, final int errorIndex, final Throwable t) {
        super(s, t);
        this.parsedString = charSequence.toString();
        this.errorIndex = errorIndex;
    }
    
    public String getParsedString() {
        return this.parsedString;
    }
    
    public int getErrorIndex() {
        return this.errorIndex;
    }
}
