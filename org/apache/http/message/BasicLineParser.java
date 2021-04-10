package org.apache.http.message;

import org.apache.http.*;
import org.apache.http.util.*;

@Deprecated
public class BasicLineParser implements LineParser
{
    public static final BasicLineParser DEFAULT;
    protected final ProtocolVersion protocol;
    
    public BasicLineParser() {
        throw new RuntimeException("Stub!");
    }
    
    public BasicLineParser(final ProtocolVersion protocolVersion) {
        throw new RuntimeException("Stub!");
    }
    
    public static final Header parseHeader(final String s, final LineParser lineParser) throws ParseException {
        throw new RuntimeException("Stub!");
    }
    
    public static final ProtocolVersion parseProtocolVersion(final String s, final LineParser lineParser) throws ParseException {
        throw new RuntimeException("Stub!");
    }
    
    public static final RequestLine parseRequestLine(final String s, final LineParser lineParser) throws ParseException {
        throw new RuntimeException("Stub!");
    }
    
    public static final StatusLine parseStatusLine(final String s, final LineParser lineParser) throws ParseException {
        throw new RuntimeException("Stub!");
    }
    
    protected ProtocolVersion createProtocolVersion(final int n, final int n2) {
        throw new RuntimeException("Stub!");
    }
    
    protected RequestLine createRequestLine(final String s, final String s2, final ProtocolVersion protocolVersion) {
        throw new RuntimeException("Stub!");
    }
    
    protected StatusLine createStatusLine(final ProtocolVersion protocolVersion, final int n, final String s) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean hasProtocolVersion(final CharArrayBuffer charArrayBuffer, final ParserCursor parserCursor) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public Header parseHeader(final CharArrayBuffer charArrayBuffer) throws ParseException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public ProtocolVersion parseProtocolVersion(final CharArrayBuffer charArrayBuffer, final ParserCursor parserCursor) throws ParseException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public RequestLine parseRequestLine(final CharArrayBuffer charArrayBuffer, final ParserCursor parserCursor) throws ParseException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public StatusLine parseStatusLine(final CharArrayBuffer charArrayBuffer, final ParserCursor parserCursor) throws ParseException {
        throw new RuntimeException("Stub!");
    }
    
    protected void skipWhitespace(final CharArrayBuffer charArrayBuffer, final ParserCursor parserCursor) {
        throw new RuntimeException("Stub!");
    }
}
