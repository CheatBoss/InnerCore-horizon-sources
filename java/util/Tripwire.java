package java.util;

import sun.util.logging.*;
import java.security.*;

final class Tripwire
{
    private static final String TRIPWIRE_PROPERTY = "org.openjdk.java.util.stream.tripwire";
    static final boolean ENABLED;
    
    private Tripwire() {
    }
    
    static void trip(final Class<?> clazz, final String s) {
        PlatformLogger.getLogger(clazz.getName()).warning(s, clazz.getName());
    }
    
    static {
        ENABLED = AccessController.doPrivileged(() -> Boolean.getBoolean("org.openjdk.java.util.stream.tripwire"));
    }
}
