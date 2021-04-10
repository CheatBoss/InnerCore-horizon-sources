package org.apache.http;

import java.util.*;

@Deprecated
public interface HeaderElementIterator extends Iterator
{
    boolean hasNext();
    
    HeaderElement nextElement();
}
