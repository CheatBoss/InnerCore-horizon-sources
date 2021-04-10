package org.apache.http.message;

import org.apache.http.*;
import org.apache.http.util.*;

@Deprecated
public class BasicHeaderValueFormatter implements HeaderValueFormatter
{
    public static final BasicHeaderValueFormatter DEFAULT;
    public static final String SEPARATORS = " ;,:@()<>\\\"/[]?={}\t";
    public static final String UNSAFE_CHARS = "\"\\";
    
    public BasicHeaderValueFormatter() {
        throw new RuntimeException("Stub!");
    }
    
    public static final String formatElements(final HeaderElement[] array, final boolean b, final HeaderValueFormatter headerValueFormatter) {
        throw new RuntimeException("Stub!");
    }
    
    public static final String formatHeaderElement(final HeaderElement headerElement, final boolean b, final HeaderValueFormatter headerValueFormatter) {
        throw new RuntimeException("Stub!");
    }
    
    public static final String formatNameValuePair(final NameValuePair nameValuePair, final boolean b, final HeaderValueFormatter headerValueFormatter) {
        throw new RuntimeException("Stub!");
    }
    
    public static final String formatParameters(final NameValuePair[] array, final boolean b, final HeaderValueFormatter headerValueFormatter) {
        throw new RuntimeException("Stub!");
    }
    
    protected void doFormatValue(final CharArrayBuffer charArrayBuffer, final String s, final boolean b) {
        throw new RuntimeException("Stub!");
    }
    
    protected int estimateElementsLen(final HeaderElement[] array) {
        throw new RuntimeException("Stub!");
    }
    
    protected int estimateHeaderElementLen(final HeaderElement headerElement) {
        throw new RuntimeException("Stub!");
    }
    
    protected int estimateNameValuePairLen(final NameValuePair nameValuePair) {
        throw new RuntimeException("Stub!");
    }
    
    protected int estimateParametersLen(final NameValuePair[] array) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public CharArrayBuffer formatElements(final CharArrayBuffer charArrayBuffer, final HeaderElement[] array, final boolean b) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public CharArrayBuffer formatHeaderElement(final CharArrayBuffer charArrayBuffer, final HeaderElement headerElement, final boolean b) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public CharArrayBuffer formatNameValuePair(final CharArrayBuffer charArrayBuffer, final NameValuePair nameValuePair, final boolean b) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public CharArrayBuffer formatParameters(final CharArrayBuffer charArrayBuffer, final NameValuePair[] array, final boolean b) {
        throw new RuntimeException("Stub!");
    }
    
    protected boolean isSeparator(final char c) {
        throw new RuntimeException("Stub!");
    }
    
    protected boolean isUnsafe(final char c) {
        throw new RuntimeException("Stub!");
    }
}
