package org.apache.http.cookie;

import java.util.*;

@Deprecated
public interface Cookie
{
    String getComment();
    
    String getCommentURL();
    
    String getDomain();
    
    Date getExpiryDate();
    
    String getName();
    
    String getPath();
    
    int[] getPorts();
    
    String getValue();
    
    int getVersion();
    
    boolean isExpired(final Date p0);
    
    boolean isPersistent();
    
    boolean isSecure();
}
