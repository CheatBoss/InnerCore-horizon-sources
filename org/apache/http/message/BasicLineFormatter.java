package org.apache.http.message;

import org.apache.http.*;
import org.apache.http.util.*;

@Deprecated
public class BasicLineFormatter implements LineFormatter
{
    public static final BasicLineFormatter DEFAULT;
    
    public BasicLineFormatter() {
        throw new RuntimeException("Stub!");
    }
    
    public static final String formatHeader(final Header header, final LineFormatter lineFormatter) {
        throw new RuntimeException("Stub!");
    }
    
    public static final String formatProtocolVersion(final ProtocolVersion protocolVersion, final LineFormatter lineFormatter) {
        throw new RuntimeException("Stub!");
    }
    
    public static final String formatRequestLine(final RequestLine requestLine, final LineFormatter lineFormatter) {
        throw new RuntimeException("Stub!");
    }
    
    public static final String formatStatusLine(final StatusLine statusLine, final LineFormatter lineFormatter) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public CharArrayBuffer appendProtocolVersion(final CharArrayBuffer charArrayBuffer, final ProtocolVersion protocolVersion) {
        throw new RuntimeException("Stub!");
    }
    
    protected void doFormatHeader(final CharArrayBuffer charArrayBuffer, final Header header) {
        throw new RuntimeException("Stub!");
    }
    
    protected void doFormatRequestLine(final CharArrayBuffer charArrayBuffer, final RequestLine requestLine) {
        throw new RuntimeException("Stub!");
    }
    
    protected void doFormatStatusLine(final CharArrayBuffer charArrayBuffer, final StatusLine statusLine) {
        throw new RuntimeException("Stub!");
    }
    
    protected int estimateProtocolVersionLen(final ProtocolVersion protocolVersion) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public CharArrayBuffer formatHeader(final CharArrayBuffer charArrayBuffer, final Header header) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public CharArrayBuffer formatRequestLine(final CharArrayBuffer charArrayBuffer, final RequestLine requestLine) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public CharArrayBuffer formatStatusLine(final CharArrayBuffer charArrayBuffer, final StatusLine statusLine) {
        throw new RuntimeException("Stub!");
    }
    
    protected CharArrayBuffer initBuffer(final CharArrayBuffer charArrayBuffer) {
        throw new RuntimeException("Stub!");
    }
}
