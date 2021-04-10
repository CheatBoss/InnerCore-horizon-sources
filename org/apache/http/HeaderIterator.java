package org.apache.http;

import java.util.*;

@Deprecated
public interface HeaderIterator extends Iterator
{
    boolean hasNext();
    
    Header nextHeader();
}
