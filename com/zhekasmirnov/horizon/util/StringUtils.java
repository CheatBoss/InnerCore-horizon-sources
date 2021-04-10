package com.zhekasmirnov.horizon.util;

import java.io.*;

public class StringUtils
{
    public static Integer toIntegerOrNull(final String string) {
        try {
            return Integer.valueOf(string);
        }
        catch (NumberFormatException e) {
            return null;
        }
    }
    
    public static Double toDoubleOrNull(final String string) {
        try {
            return Double.valueOf(string);
        }
        catch (NumberFormatException e) {
            return null;
        }
    }
    
    public static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
}
