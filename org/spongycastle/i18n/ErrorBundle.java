package org.spongycastle.i18n;

import java.io.*;
import java.util.*;

public class ErrorBundle extends MessageBundle
{
    public static final String DETAIL_ENTRY = "details";
    public static final String SUMMARY_ENTRY = "summary";
    
    public ErrorBundle(final String s, final String s2) throws NullPointerException {
        super(s, s2);
    }
    
    public ErrorBundle(final String s, final String s2, final String s3) throws NullPointerException, UnsupportedEncodingException {
        super(s, s2, s3);
    }
    
    public ErrorBundle(final String s, final String s2, final String s3, final Object[] array) throws NullPointerException, UnsupportedEncodingException {
        super(s, s2, s3, array);
    }
    
    public ErrorBundle(final String s, final String s2, final Object[] array) throws NullPointerException {
        super(s, s2, array);
    }
    
    public String getDetail(final Locale locale) throws MissingEntryException {
        return this.getEntry("details", locale, TimeZone.getDefault());
    }
    
    public String getDetail(final Locale locale, final TimeZone timeZone) throws MissingEntryException {
        return this.getEntry("details", locale, timeZone);
    }
    
    public String getSummary(final Locale locale) throws MissingEntryException {
        return this.getEntry("summary", locale, TimeZone.getDefault());
    }
    
    public String getSummary(final Locale locale, final TimeZone timeZone) throws MissingEntryException {
        return this.getEntry("summary", locale, timeZone);
    }
}
