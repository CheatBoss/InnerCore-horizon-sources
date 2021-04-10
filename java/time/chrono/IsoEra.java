package java.time.chrono;

import java.time.*;

public enum IsoEra implements Era
{
    BCE, 
    CE;
    
    public static IsoEra of(final int n) {
        switch (n) {
            case 0: {
                return IsoEra.BCE;
            }
            case 1: {
                return IsoEra.CE;
            }
            default: {
                throw new DateTimeException("Invalid era: " + n);
            }
        }
    }
    
    @Override
    public int getValue() {
        return this.ordinal();
    }
}
