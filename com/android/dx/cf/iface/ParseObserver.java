package com.android.dx.cf.iface;

import com.android.dx.util.*;

public interface ParseObserver
{
    void changeIndent(final int p0);
    
    void endParsingMember(final ByteArray p0, final int p1, final String p2, final String p3, final Member p4);
    
    void parsed(final ByteArray p0, final int p1, final int p2, final String p3);
    
    void startParsingMember(final ByteArray p0, final int p1, final String p2, final String p3);
}
