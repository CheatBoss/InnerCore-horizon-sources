package org.apache.http.message;

import org.apache.http.util.*;
import org.apache.http.*;

@Deprecated
public interface LineParser
{
    boolean hasProtocolVersion(final CharArrayBuffer p0, final ParserCursor p1);
    
    Header parseHeader(final CharArrayBuffer p0) throws ParseException;
    
    ProtocolVersion parseProtocolVersion(final CharArrayBuffer p0, final ParserCursor p1) throws ParseException;
    
    RequestLine parseRequestLine(final CharArrayBuffer p0, final ParserCursor p1) throws ParseException;
    
    StatusLine parseStatusLine(final CharArrayBuffer p0, final ParserCursor p1) throws ParseException;
}
