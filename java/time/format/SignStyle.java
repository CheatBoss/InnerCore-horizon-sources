package java.time.format;

public enum SignStyle
{
    NORMAL, 
    ALWAYS, 
    NEVER, 
    NOT_NEGATIVE, 
    EXCEEDS_PAD;
    
    boolean parse(final boolean b, final boolean b2, final boolean b3) {
        switch (this.ordinal()) {
            case 0: {
                return !b || !b2;
            }
            case 1:
            case 4: {
                return true;
            }
            default: {
                return !b2 && !b3;
            }
        }
    }
}
