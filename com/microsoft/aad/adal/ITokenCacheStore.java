package com.microsoft.aad.adal;

import java.io.*;
import java.util.*;

public interface ITokenCacheStore extends Serializable
{
    boolean contains(final String p0);
    
    Iterator<TokenCacheItem> getAll();
    
    TokenCacheItem getItem(final String p0);
    
    void removeAll();
    
    void removeItem(final String p0);
    
    void setItem(final String p0, final TokenCacheItem p1);
}
