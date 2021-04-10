package org.apache.http.client;

import org.apache.http.cookie.*;
import java.util.*;

@Deprecated
public interface CookieStore
{
    void addCookie(final Cookie p0);
    
    void clear();
    
    boolean clearExpired(final Date p0);
    
    List<Cookie> getCookies();
}
