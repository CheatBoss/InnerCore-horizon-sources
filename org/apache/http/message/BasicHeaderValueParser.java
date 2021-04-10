package org.apache.http.message;

import org.apache.http.*;
import org.apache.http.util.*;

@Deprecated
public class BasicHeaderValueParser implements HeaderValueParser
{
    public static final BasicHeaderValueParser DEFAULT;
    
    public BasicHeaderValueParser() {
        throw new RuntimeException("Stub!");
    }
    
    public static final HeaderElement[] parseElements(final String s, final HeaderValueParser headerValueParser) throws ParseException {
        throw new RuntimeException("Stub!");
    }
    
    public static final HeaderElement parseHeaderElement(final String s, final HeaderValueParser headerValueParser) throws ParseException {
        throw new RuntimeException("Stub!");
    }
    
    public static final NameValuePair parseNameValuePair(final String s, final HeaderValueParser headerValueParser) throws ParseException {
        throw new RuntimeException("Stub!");
    }
    
    public static final NameValuePair[] parseParameters(final String s, final HeaderValueParser headerValueParser) throws ParseException {
        throw new RuntimeException("Stub!");
    }
    
    protected HeaderElement createHeaderElement(final String s, final String s2, final NameValuePair[] array) {
        throw new RuntimeException("Stub!");
    }
    
    protected NameValuePair createNameValuePair(final String s, final String s2) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public HeaderElement[] parseElements(final CharArrayBuffer charArrayBuffer, final ParserCursor parserCursor) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public HeaderElement parseHeaderElement(final CharArrayBuffer charArrayBuffer, final ParserCursor parserCursor) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public NameValuePair parseNameValuePair(final CharArrayBuffer charArrayBuffer, final ParserCursor parserCursor) {
        throw new RuntimeException("Stub!");
    }
    
    public NameValuePair parseNameValuePair(final CharArrayBuffer charArrayBuffer, final ParserCursor parserCursor, final char[] array) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public NameValuePair[] parseParameters(final CharArrayBuffer charArrayBuffer, final ParserCursor parserCursor) {
        throw new RuntimeException("Stub!");
    }
}
