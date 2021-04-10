package org.mozilla.javascript;

public interface RegExpProxy
{
    public static final int RA_MATCH = 1;
    public static final int RA_REPLACE = 2;
    public static final int RA_SEARCH = 3;
    
    Object action(final Context p0, final Scriptable p1, final Scriptable p2, final Object[] p3, final int p4);
    
    Object compileRegExp(final Context p0, final String p1, final String p2);
    
    int find_split(final Context p0, final Scriptable p1, final String p2, final String p3, final Scriptable p4, final int[] p5, final int[] p6, final boolean[] p7, final String[][] p8);
    
    boolean isRegExp(final Scriptable p0);
    
    Object js_split(final Context p0, final Scriptable p1, final String p2, final Object[] p3);
    
    Scriptable wrapRegExp(final Context p0, final Scriptable p1, final Object p2);
}
