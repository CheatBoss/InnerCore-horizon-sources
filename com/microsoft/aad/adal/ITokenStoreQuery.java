package com.microsoft.aad.adal;

import java.util.*;

public interface ITokenStoreQuery
{
    void clearTokensForUser(final String p0);
    
    Iterator<TokenCacheItem> getAll();
    
    List<TokenCacheItem> getTokensAboutToExpire();
    
    List<TokenCacheItem> getTokensForResource(final String p0);
    
    List<TokenCacheItem> getTokensForUser(final String p0);
    
    Set<String> getUniqueUsersWithTokenCache();
}
