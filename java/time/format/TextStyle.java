package java.time.format;

public enum TextStyle
{
    FULL(2, 0), 
    FULL_STANDALONE(32770, 0), 
    SHORT(1, 1), 
    SHORT_STANDALONE(32769, 1), 
    NARROW(4, 1), 
    NARROW_STANDALONE(32772, 1);
    
    private final int calendarStyle;
    private final int zoneNameStyleIndex;
    
    private TextStyle(final int calendarStyle, final int zoneNameStyleIndex) {
        this.calendarStyle = calendarStyle;
        this.zoneNameStyleIndex = zoneNameStyleIndex;
    }
    
    public boolean isStandalone() {
        return (this.ordinal() & 0x1) == 0x1;
    }
    
    public TextStyle asStandalone() {
        return values()[this.ordinal() | 0x1];
    }
    
    public TextStyle asNormal() {
        return values()[this.ordinal() & 0xFFFFFFFE];
    }
    
    int toCalendarStyle() {
        return this.calendarStyle;
    }
    
    int zoneNameStyleIndex() {
        return this.zoneNameStyleIndex;
    }
}
