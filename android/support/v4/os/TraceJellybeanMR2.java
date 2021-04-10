package android.support.v4.os;

import android.os.*;

class TraceJellybeanMR2
{
    public static void beginSection(final String s) {
        Trace.beginSection(s);
    }
    
    public static void endSection() {
        Trace.endSection();
    }
}
