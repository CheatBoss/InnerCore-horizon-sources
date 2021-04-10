package com.microsoft.aad.adal;

import java.net.*;
import java.util.*;

class TokenCacheAccessor
{
    private static final String TAG;
    private String mAuthority;
    private final String mTelemetryRequestId;
    private final ITokenCacheStore mTokenCacheStore;
    
    static {
        TAG = TokenCacheAccessor.class.getSimpleName();
    }
    
    TokenCacheAccessor(final ITokenCacheStore mTokenCacheStore, final String mAuthority, final String mTelemetryRequestId) {
        if (mTokenCacheStore == null) {
            throw new IllegalArgumentException("tokenCacheStore");
        }
        if (StringExtensions.isNullOrBlank(mAuthority)) {
            throw new IllegalArgumentException("authority");
        }
        if (!StringExtensions.isNullOrBlank(mTelemetryRequestId)) {
            this.mTokenCacheStore = mTokenCacheStore;
            this.mAuthority = mAuthority;
            this.mTelemetryRequestId = mTelemetryRequestId;
            return;
        }
        throw new IllegalArgumentException("requestId");
    }
    
    private String constructAuthorityUrl(final String s) throws MalformedURLException {
        final URL url = new URL(this.mAuthority);
        if (url.getHost().equalsIgnoreCase(s)) {
            return this.mAuthority;
        }
        return Utility.constructAuthorityUrl(url, s).toString();
    }
    
    private String getCacheKey(final String s, final String s2, final String s3, final String s4, final String s5, final TokenEntryType tokenEntryType) {
        final int n = TokenCacheAccessor$1.$SwitchMap$com$microsoft$aad$adal$TokenEntryType[tokenEntryType.ordinal()];
        if (n == 1) {
            return CacheKey.createCacheKeyForRTEntry(s, s2, s3, s4);
        }
        if (n == 2) {
            return CacheKey.createCacheKeyForMRRT(s, s3, s4);
        }
        if (n != 3) {
            return null;
        }
        return CacheKey.createCacheKeyForFRT(s, s5, s4);
    }
    
    private InstanceDiscoveryMetadata getInstanceDiscoveryMetadata() throws MalformedURLException {
        return AuthorityValidationMetadataCache.getCachedInstanceDiscoveryMetadata(new URL(this.mAuthority));
    }
    
    private List<String> getKeyListToRemoveForFRT(final TokenCacheItem tokenCacheItem) {
        final ArrayList<String> list = new ArrayList<String>();
        if (tokenCacheItem.getUserInfo() != null) {
            list.add(CacheKey.createCacheKeyForFRT(this.mAuthority, tokenCacheItem.getFamilyClientId(), tokenCacheItem.getUserInfo().getDisplayableId()));
            list.add(CacheKey.createCacheKeyForFRT(this.mAuthority, tokenCacheItem.getFamilyClientId(), tokenCacheItem.getUserInfo().getUserId()));
        }
        return list;
    }
    
    private List<String> getKeyListToRemoveForMRRT(final TokenCacheItem tokenCacheItem) {
        final ArrayList<String> list = new ArrayList<String>();
        list.add(CacheKey.createCacheKeyForMRRT(this.mAuthority, tokenCacheItem.getClientId(), null));
        if (tokenCacheItem.getUserInfo() != null) {
            list.add(CacheKey.createCacheKeyForMRRT(this.mAuthority, tokenCacheItem.getClientId(), tokenCacheItem.getUserInfo().getDisplayableId()));
            list.add(CacheKey.createCacheKeyForMRRT(this.mAuthority, tokenCacheItem.getClientId(), tokenCacheItem.getUserInfo().getUserId()));
        }
        return list;
    }
    
    private List<String> getKeyListToRemoveForRT(final TokenCacheItem tokenCacheItem) {
        final ArrayList<String> list = new ArrayList<String>();
        list.add(CacheKey.createCacheKeyForRTEntry(this.mAuthority, tokenCacheItem.getResource(), tokenCacheItem.getClientId(), null));
        if (tokenCacheItem.getUserInfo() != null) {
            list.add(CacheKey.createCacheKeyForRTEntry(this.mAuthority, tokenCacheItem.getResource(), tokenCacheItem.getClientId(), tokenCacheItem.getUserInfo().getDisplayableId()));
            list.add(CacheKey.createCacheKeyForRTEntry(this.mAuthority, tokenCacheItem.getResource(), tokenCacheItem.getClientId(), tokenCacheItem.getUserInfo().getUserId()));
        }
        return list;
    }
    
    private TokenCacheItem getTokenCacheItemFromAliasedHost(final String s, final String s2, final String s3, final String s4, final TokenEntryType tokenEntryType) throws MalformedURLException {
        final InstanceDiscoveryMetadata instanceDiscoveryMetadata = this.getInstanceDiscoveryMetadata();
        final TokenCacheItem tokenCacheItem = null;
        if (instanceDiscoveryMetadata == null) {
            return null;
        }
        final Iterator<String> iterator = instanceDiscoveryMetadata.getAliases().iterator();
        TokenCacheItem item;
        while (true) {
            item = tokenCacheItem;
            if (!iterator.hasNext()) {
                break;
            }
            final String constructAuthorityUrl = this.constructAuthorityUrl(iterator.next());
            if (constructAuthorityUrl.equalsIgnoreCase(this.mAuthority)) {
                continue;
            }
            if (constructAuthorityUrl.equalsIgnoreCase(this.getAuthorityUrlWithPreferredCache())) {
                continue;
            }
            item = this.mTokenCacheStore.getItem(this.getCacheKey(constructAuthorityUrl, s, s2, s4, s3, tokenEntryType));
            if (item != null) {
                break;
            }
        }
        return item;
    }
    
    private TokenCacheItem getTokenCacheItemFromPassedInAuthority(String cacheKey, final String s, final String s2, final String s3, final TokenEntryType tokenEntryType) throws MalformedURLException {
        if (this.getAuthorityUrlWithPreferredCache().equalsIgnoreCase(this.mAuthority)) {
            return null;
        }
        cacheKey = this.getCacheKey(this.mAuthority, cacheKey, s, s3, s2, tokenEntryType);
        return this.mTokenCacheStore.getItem(cacheKey);
    }
    
    private boolean isUserMisMatch(final String s, final TokenCacheItem tokenCacheItem) {
        final boolean nullOrBlank = StringExtensions.isNullOrBlank(s);
        boolean b2;
        final boolean b = b2 = false;
        if (!nullOrBlank) {
            if (tokenCacheItem.getUserInfo() == null) {
                return false;
            }
            b2 = b;
            if (!s.equalsIgnoreCase(tokenCacheItem.getUserInfo().getDisplayableId())) {
                b2 = b;
                if (!s.equalsIgnoreCase(tokenCacheItem.getUserInfo().getUserId())) {
                    b2 = true;
                }
            }
        }
        return b2;
    }
    
    private void logReturnedToken(final AuthenticationResult authenticationResult) {
        if (authenticationResult != null && authenticationResult.getAccessToken() != null) {
            Logger.i(TokenCacheAccessor.TAG, "Access tokenID and refresh tokenID returned. ", null);
        }
    }
    
    private TokenCacheItem performAdditionalCacheLookup(final String s, final String s2, final String s3, final String s4, final TokenEntryType tokenEntryType) throws MalformedURLException {
        TokenCacheItem tokenCacheItem;
        if ((tokenCacheItem = this.getTokenCacheItemFromPassedInAuthority(s, s2, s3, s4, tokenEntryType)) == null) {
            tokenCacheItem = this.getTokenCacheItemFromAliasedHost(s, s2, s3, s4, tokenEntryType);
        }
        return tokenCacheItem;
    }
    
    private void setItemToCacheForUser(final String s, final String s2, final AuthenticationResult authenticationResult, final String s3) throws MalformedURLException {
        this.logReturnedToken(authenticationResult);
        final StringBuilder sb = new StringBuilder();
        sb.append(TokenCacheAccessor.TAG);
        sb.append(":setItemToCacheForUser");
        Logger.v(sb.toString(), "Save regular token into cache.");
        final CacheEvent cacheEvent = new CacheEvent("Microsoft.ADAL.token_cache_write");
        cacheEvent.setRequestId(this.mTelemetryRequestId);
        Telemetry.getInstance().startEvent(this.mTelemetryRequestId, "Microsoft.ADAL.token_cache_write");
        if (!StringExtensions.isNullOrBlank(authenticationResult.getAuthority())) {
            this.mAuthority = authenticationResult.getAuthority();
        }
        this.mTokenCacheStore.setItem(CacheKey.createCacheKeyForRTEntry(this.getAuthorityUrlWithPreferredCache(), s, s2, s3), TokenCacheItem.createRegularTokenCacheItem(this.getAuthorityUrlWithPreferredCache(), s, s2, authenticationResult));
        cacheEvent.setTokenTypeRT(true);
        if (authenticationResult.getIsMultiResourceRefreshToken()) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(TokenCacheAccessor.TAG);
            sb2.append(":setItemToCacheForUser");
            Logger.v(sb2.toString(), "Save Multi Resource Refresh token to cache.");
            this.mTokenCacheStore.setItem(CacheKey.createCacheKeyForMRRT(this.getAuthorityUrlWithPreferredCache(), s2, s3), TokenCacheItem.createMRRTTokenCacheItem(this.getAuthorityUrlWithPreferredCache(), s2, authenticationResult));
            cacheEvent.setTokenTypeMRRT(true);
        }
        if (!StringExtensions.isNullOrBlank(authenticationResult.getFamilyClientId()) && !StringExtensions.isNullOrBlank(s3)) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(TokenCacheAccessor.TAG);
            sb3.append(":setItemToCacheForUser");
            Logger.v(sb3.toString(), "Save Family Refresh token into cache.");
            this.mTokenCacheStore.setItem(CacheKey.createCacheKeyForFRT(this.getAuthorityUrlWithPreferredCache(), authenticationResult.getFamilyClientId(), s3), TokenCacheItem.createFRRTTokenCacheItem(this.getAuthorityUrlWithPreferredCache(), authenticationResult));
            cacheEvent.setTokenTypeFRT(true);
        }
        Telemetry.getInstance().stopEvent(this.mTelemetryRequestId, cacheEvent, "Microsoft.ADAL.token_cache_write");
    }
    
    private CacheEvent startCacheTelemetryRequest(final String tokenType) {
        final CacheEvent cacheEvent = new CacheEvent("Microsoft.ADAL.token_cache_lookup");
        cacheEvent.setTokenType(tokenType);
        cacheEvent.setRequestId(this.mTelemetryRequestId);
        Telemetry.getInstance().startEvent(this.mTelemetryRequestId, "Microsoft.ADAL.token_cache_lookup");
        return cacheEvent;
    }
    
    private void throwIfMultipleATExisted(final String s, final String s2, final String s3) throws AuthenticationException {
        if (!StringExtensions.isNullOrBlank(s3)) {
            return;
        }
        if (!this.isMultipleRTsMatchingGivenAppAndResource(s, s2)) {
            return;
        }
        throw new AuthenticationException(ADALError.AUTH_FAILED_USER_MISMATCH, "No user is provided and multiple access tokens exist for the given app and resource.");
    }
    
    TokenCacheItem getATFromCache(String s, String s2, final String s3) throws AuthenticationException {
        try {
            final TokenCacheItem regularRefreshTokenCacheItem = this.getRegularRefreshTokenCacheItem(s, s2, s3);
            if (regularRefreshTokenCacheItem == null) {
                final StringBuilder sb = new StringBuilder();
                sb.append(TokenCacheAccessor.TAG);
                sb.append(":getATFromCache");
                s = sb.toString();
                s2 = "No access token exists.";
            }
            else {
                this.throwIfMultipleATExisted(s2, s, s3);
                if (StringExtensions.isNullOrBlank(regularRefreshTokenCacheItem.getAccessToken())) {
                    return regularRefreshTokenCacheItem;
                }
                if (TokenCacheItem.isTokenExpired(regularRefreshTokenCacheItem.getExpiresOn())) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(TokenCacheAccessor.TAG);
                    sb2.append(":getATFromCache");
                    s = sb2.toString();
                    s2 = "Access token exists, but already expired.";
                }
                else {
                    if (!this.isUserMisMatch(s3, regularRefreshTokenCacheItem)) {
                        return regularRefreshTokenCacheItem;
                    }
                    throw new AuthenticationException(ADALError.AUTH_FAILED_USER_MISMATCH);
                }
            }
            Logger.v(s, s2);
            return null;
        }
        catch (MalformedURLException ex) {
            throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_URL, ex.getMessage(), ex);
        }
    }
    
    String getAuthorityUrlWithPreferredCache() throws MalformedURLException {
        final InstanceDiscoveryMetadata instanceDiscoveryMetadata = this.getInstanceDiscoveryMetadata();
        if (instanceDiscoveryMetadata != null && instanceDiscoveryMetadata.isValidated()) {
            return this.constructAuthorityUrl(instanceDiscoveryMetadata.getPreferredCache());
        }
        return this.mAuthority;
    }
    
    TokenCacheItem getFRTItem(final String s, final String s2) throws MalformedURLException {
        final CacheEvent startCacheTelemetryRequest = this.startCacheTelemetryRequest("Microsoft.ADAL.frt");
        if (StringExtensions.isNullOrBlank(s2)) {
            Telemetry.getInstance().stopEvent(this.mTelemetryRequestId, startCacheTelemetryRequest, "Microsoft.ADAL.token_cache_lookup");
            return null;
        }
        TokenCacheItem tokenCacheItem;
        if ((tokenCacheItem = this.mTokenCacheStore.getItem(CacheKey.createCacheKeyForFRT(this.getAuthorityUrlWithPreferredCache(), s, s2))) == null) {
            tokenCacheItem = this.performAdditionalCacheLookup(null, null, s, s2, TokenEntryType.FRT_TOKEN_ENTRY);
        }
        if (tokenCacheItem != null) {
            startCacheTelemetryRequest.setTokenTypeFRT(true);
        }
        Telemetry.getInstance().stopEvent(this.mTelemetryRequestId, startCacheTelemetryRequest, "Microsoft.ADAL.token_cache_lookup");
        return tokenCacheItem;
    }
    
    TokenCacheItem getMRRTItem(final String s, final String s2) throws MalformedURLException {
        final CacheEvent startCacheTelemetryRequest = this.startCacheTelemetryRequest("Microsoft.ADAL.mrrt");
        TokenCacheItem tokenCacheItem;
        if ((tokenCacheItem = this.mTokenCacheStore.getItem(CacheKey.createCacheKeyForMRRT(this.getAuthorityUrlWithPreferredCache(), s, s2))) == null) {
            tokenCacheItem = this.performAdditionalCacheLookup(null, s, null, s2, TokenEntryType.MRRT_TOKEN_ENTRY);
        }
        if (tokenCacheItem != null) {
            startCacheTelemetryRequest.setTokenTypeMRRT(true);
            startCacheTelemetryRequest.setTokenTypeFRT(tokenCacheItem.isFamilyToken());
        }
        Telemetry.getInstance().stopEvent(this.mTelemetryRequestId, startCacheTelemetryRequest, "Microsoft.ADAL.token_cache_lookup");
        return tokenCacheItem;
    }
    
    TokenCacheItem getRegularRefreshTokenCacheItem(final String s, final String s2, final String s3) throws MalformedURLException {
        final CacheEvent startCacheTelemetryRequest = this.startCacheTelemetryRequest("Microsoft.ADAL.rt");
        TokenCacheItem tokenCacheItem;
        if ((tokenCacheItem = this.mTokenCacheStore.getItem(CacheKey.createCacheKeyForRTEntry(this.getAuthorityUrlWithPreferredCache(), s, s2, s3))) == null) {
            tokenCacheItem = this.performAdditionalCacheLookup(s, s2, null, s3, TokenEntryType.REGULAR_TOKEN_ENTRY);
        }
        if (tokenCacheItem != null) {
            startCacheTelemetryRequest.setTokenTypeRT(true);
            startCacheTelemetryRequest.setSpeRing(tokenCacheItem.getSpeRing());
        }
        Telemetry.getInstance().stopEvent(this.mTelemetryRequestId, startCacheTelemetryRequest, "Microsoft.ADAL.token_cache_lookup");
        return tokenCacheItem;
    }
    
    TokenCacheItem getStaleToken(final AuthenticationRequest authenticationRequest) throws AuthenticationException {
        try {
            final TokenCacheItem regularRefreshTokenCacheItem = this.getRegularRefreshTokenCacheItem(authenticationRequest.getResource(), authenticationRequest.getClientId(), authenticationRequest.getUserFromRequest());
            if (regularRefreshTokenCacheItem != null && !StringExtensions.isNullOrBlank(regularRefreshTokenCacheItem.getAccessToken()) && regularRefreshTokenCacheItem.getExtendedExpiresOn() != null && !TokenCacheItem.isTokenExpired(regularRefreshTokenCacheItem.getExtendedExpiresOn())) {
                this.throwIfMultipleATExisted(authenticationRequest.getClientId(), authenticationRequest.getResource(), authenticationRequest.getUserFromRequest());
                final StringBuilder sb = new StringBuilder();
                sb.append(TokenCacheAccessor.TAG);
                sb.append(":getStaleToken");
                Logger.i(sb.toString(), "The stale access token is returned.", "");
                return regularRefreshTokenCacheItem;
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(TokenCacheAccessor.TAG);
            sb2.append(":getStaleToken");
            Logger.i(sb2.toString(), "The stale access token is not found.", "");
            return null;
        }
        catch (MalformedURLException ex) {
            throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_URL, ex.getMessage(), ex);
        }
    }
    
    boolean isMultipleMRRTsMatchingGivenApp(final String s) {
        final Iterator<TokenCacheItem> all = this.mTokenCacheStore.getAll();
        final ArrayList<TokenCacheItem> list = new ArrayList<TokenCacheItem>();
        while (all.hasNext()) {
            final TokenCacheItem tokenCacheItem = all.next();
            if (tokenCacheItem.getAuthority().equalsIgnoreCase(this.mAuthority) && tokenCacheItem.getClientId().equalsIgnoreCase(s) && (tokenCacheItem.getIsMultiResourceRefreshToken() || StringExtensions.isNullOrBlank(tokenCacheItem.getResource()))) {
                list.add(tokenCacheItem);
            }
        }
        return list.size() > 1;
    }
    
    boolean isMultipleRTsMatchingGivenAppAndResource(final String s, final String s2) {
        final Iterator<TokenCacheItem> all = this.mTokenCacheStore.getAll();
        final ArrayList<TokenCacheItem> list = new ArrayList<TokenCacheItem>();
        while (all.hasNext()) {
            final TokenCacheItem tokenCacheItem = all.next();
            if (tokenCacheItem.getAuthority().equalsIgnoreCase(this.mAuthority) && s.equalsIgnoreCase(tokenCacheItem.getClientId()) && s2.equalsIgnoreCase(tokenCacheItem.getResource()) && !tokenCacheItem.getIsMultiResourceRefreshToken()) {
                list.add(tokenCacheItem);
            }
        }
        return list.size() > 1;
    }
    
    void removeTokenCacheItem(TokenCacheItem tokenCacheItem, String resource) throws AuthenticationException {
        final CacheEvent cacheEvent = new CacheEvent("Microsoft.ADAL.token_cache_delete");
        cacheEvent.setRequestId(this.mTelemetryRequestId);
        Telemetry.getInstance().startEvent(this.mTelemetryRequestId, "Microsoft.ADAL.token_cache_delete");
        final int n = TokenCacheAccessor$1.$SwitchMap$com$microsoft$aad$adal$TokenEntryType[tokenCacheItem.getTokenEntryType().ordinal()];
        List<String> list;
        if (n != 1) {
            if (n != 2) {
                if (n != 3) {
                    throw new AuthenticationException(ADALError.INVALID_TOKEN_CACHE_ITEM);
                }
                cacheEvent.setTokenTypeFRT(true);
                final StringBuilder sb = new StringBuilder();
                sb.append(TokenCacheAccessor.TAG);
                sb.append(":removeTokenCacheItem");
                Logger.v(sb.toString(), "FRT was used to get access token, remove entries for FRT entries.");
                list = this.getKeyListToRemoveForFRT(tokenCacheItem);
            }
            else {
                cacheEvent.setTokenTypeMRRT(true);
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(TokenCacheAccessor.TAG);
                sb2.append(":removeTokenCacheItem");
                Logger.v(sb2.toString(), "MRRT was used to get access token, remove entries for both MRRT entries and regular RT entries.");
                final List<String> keyListToRemoveForMRRT = this.getKeyListToRemoveForMRRT(tokenCacheItem);
                tokenCacheItem = new TokenCacheItem(tokenCacheItem);
                tokenCacheItem.setResource(resource);
                keyListToRemoveForMRRT.addAll(this.getKeyListToRemoveForRT(tokenCacheItem));
                list = keyListToRemoveForMRRT;
            }
        }
        else {
            cacheEvent.setTokenTypeRT(true);
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(TokenCacheAccessor.TAG);
            sb3.append(":removeTokenCacheItem");
            Logger.v(sb3.toString(), "Regular RT was used to get access token, remove entries for regular RT entries.");
            list = this.getKeyListToRemoveForRT(tokenCacheItem);
        }
        final Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            resource = iterator.next();
            this.mTokenCacheStore.removeItem(resource);
        }
        Telemetry.getInstance().stopEvent(this.mTelemetryRequestId, cacheEvent, "Microsoft.ADAL.token_cache_delete");
    }
    
    void updateCachedItemWithResult(final String s, final String s2, final AuthenticationResult authenticationResult, final TokenCacheItem tokenCacheItem) throws AuthenticationException {
        if (authenticationResult != null) {
            if (authenticationResult.getStatus() == AuthenticationResult.AuthenticationStatus.Succeeded) {
                final StringBuilder sb = new StringBuilder();
                sb.append(TokenCacheAccessor.TAG);
                sb.append(":updateCachedItemWithResult");
                Logger.v(sb.toString(), "Save returned AuthenticationResult into cache.");
                if (tokenCacheItem != null && tokenCacheItem.getUserInfo() != null && authenticationResult.getUserInfo() == null) {
                    authenticationResult.setUserInfo(tokenCacheItem.getUserInfo());
                    authenticationResult.setIdToken(tokenCacheItem.getRawIdToken());
                    authenticationResult.setTenantId(tokenCacheItem.getTenantId());
                }
                try {
                    this.updateTokenCache(s, s2, authenticationResult);
                    return;
                }
                catch (MalformedURLException ex) {
                    throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_URL, ex.getMessage(), ex);
                }
            }
            if ("invalid_grant".equalsIgnoreCase(authenticationResult.getErrorCode())) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(TokenCacheAccessor.TAG);
                sb2.append(":updateCachedItemWithResult");
                Logger.v(sb2.toString(), "Received INVALID_GRANT error code, remove existing cache entry.");
                this.removeTokenCacheItem(tokenCacheItem, s);
            }
            return;
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(TokenCacheAccessor.TAG);
        sb3.append(":updateCachedItemWithResult");
        Logger.v(sb3.toString(), "AuthenticationResult is null, cannot update cache.");
        throw new IllegalArgumentException("result");
    }
    
    void updateTokenCache(final String s, final String s2, final AuthenticationResult authenticationResult) throws MalformedURLException {
        if (authenticationResult != null) {
            if (StringExtensions.isNullOrBlank(authenticationResult.getAccessToken())) {
                return;
            }
            if (authenticationResult.getUserInfo() != null) {
                if (!StringExtensions.isNullOrBlank(authenticationResult.getUserInfo().getDisplayableId())) {
                    this.setItemToCacheForUser(s, s2, authenticationResult, authenticationResult.getUserInfo().getDisplayableId());
                }
                if (!StringExtensions.isNullOrBlank(authenticationResult.getUserInfo().getUserId())) {
                    this.setItemToCacheForUser(s, s2, authenticationResult, authenticationResult.getUserInfo().getUserId());
                }
            }
            this.setItemToCacheForUser(s, s2, authenticationResult, null);
        }
    }
}
