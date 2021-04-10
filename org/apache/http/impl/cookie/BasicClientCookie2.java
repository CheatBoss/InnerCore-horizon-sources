package org.apache.http.impl.cookie;

import org.apache.http.cookie.*;
import java.util.*;

@Deprecated
public class BasicClientCookie2 extends BasicClientCookie implements SetCookie2
{
    public BasicClientCookie2(String s, final String s2) {
        s = null;
        super(s, s);
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String getCommentURL() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public int[] getPorts() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean isExpired(final Date date) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean isPersistent() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void setCommentURL(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void setDiscard(final boolean b) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void setPorts(final int[] array) {
        throw new RuntimeException("Stub!");
    }
}
