package org.apache.http;

import org.apache.http.util.*;

@Deprecated
public interface FormattedHeader extends Header
{
    CharArrayBuffer getBuffer();
    
    int getValuePos();
}
