package com.microsoft.xbox.toolkit;

public class XLEAssert
{
    public static void assertFalse(final String s, final boolean b) {
        assertTrue(s, b ^ true);
    }
    
    public static void assertIsNotUIThread() {
        assertTrue(Thread.currentThread() != ThreadManager.UIThread);
    }
    
    public static void assertIsUIThread() {
        assertTrue(Thread.currentThread() == ThreadManager.UIThread);
    }
    
    public static void assertNotNull(final Object o) {
        assertTrue(null, o != null);
    }
    
    public static void assertNotNull(final String s, final Object o) {
        assertTrue(s, o != null);
    }
    
    public static void assertNull(final Object o) {
        assertTrue(null, o == null);
    }
    
    public static void assertTrue(final String s, final boolean b) {
    }
    
    public static void assertTrue(final boolean b) {
        assertTrue(null, b);
    }
    
    public static void fail(final String s) {
        assertTrue(s, false);
    }
    
    private static String getCallerLocation() {
        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        int n = 0;
        int n2;
        while (true) {
            n2 = n;
            if (n >= stackTrace.length) {
                break;
            }
            if (stackTrace[n].getClassName().equals(XLEAssert.class.getName()) && stackTrace[n].getMethodName().equals("getCallerLocation")) {
                n2 = n;
                break;
            }
            ++n;
        }
        while (n2 < stackTrace.length && stackTrace[n2].getClassName().equals(XLEAssert.class.getName())) {
            ++n2;
        }
        if (n2 < stackTrace.length) {
            return stackTrace[n2].toString();
        }
        return "unknown";
    }
}
