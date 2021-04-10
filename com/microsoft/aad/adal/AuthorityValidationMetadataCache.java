package com.microsoft.aad.adal;

import java.util.concurrent.*;
import java.net.*;
import org.json.*;
import java.util.*;

final class AuthorityValidationMetadataCache
{
    private static final String ALIASES = "aliases";
    static final String META_DATA = "metadata";
    private static final String PREFERRED_CACHE = "preferred_cache";
    private static final String PREFERRED_NETWORK = "preferred_network";
    private static final String TAG;
    static final String TENANT_DISCOVERY_ENDPOINT = "tenant_discovery_endpoint";
    private static ConcurrentMap<String, InstanceDiscoveryMetadata> sAadAuthorityHostMetadata;
    
    static {
        TAG = AuthorityValidationMetadataCache.class.getSimpleName();
        AuthorityValidationMetadataCache.sAadAuthorityHostMetadata = new ConcurrentHashMap<String, InstanceDiscoveryMetadata>();
    }
    
    private AuthorityValidationMetadataCache() {
    }
    
    static void clearAuthorityValidationCache() {
        AuthorityValidationMetadataCache.sAadAuthorityHostMetadata.clear();
    }
    
    static boolean containsAuthorityHost(final URL url) {
        return AuthorityValidationMetadataCache.sAadAuthorityHostMetadata.containsKey(url.getHost().toLowerCase(Locale.US));
    }
    
    static Map<String, InstanceDiscoveryMetadata> getAuthorityValidationMetadataCache() {
        return Collections.unmodifiableMap((Map<? extends String, ? extends InstanceDiscoveryMetadata>)AuthorityValidationMetadataCache.sAadAuthorityHostMetadata);
    }
    
    static InstanceDiscoveryMetadata getCachedInstanceDiscoveryMetadata(final URL url) {
        return AuthorityValidationMetadataCache.sAadAuthorityHostMetadata.get(url.getHost().toLowerCase(Locale.US));
    }
    
    static boolean isAuthorityValidated(final URL url) {
        return containsAuthorityHost(url) && getCachedInstanceDiscoveryMetadata(url).isValidated();
    }
    
    static void processInstanceDiscoveryMetadata(final URL url, final Map<String, String> map) throws JSONException {
        final boolean containsKey = map.containsKey("tenant_discovery_endpoint");
        final String s = map.get("metadata");
        final String lowerCase = url.getHost().toLowerCase(Locale.US);
        ConcurrentMap<String, InstanceDiscoveryMetadata> concurrentMap;
        InstanceDiscoveryMetadata instanceDiscoveryMetadata;
        if (!containsKey) {
            concurrentMap = AuthorityValidationMetadataCache.sAadAuthorityHostMetadata;
            instanceDiscoveryMetadata = new InstanceDiscoveryMetadata(false);
        }
        else {
            if (!StringExtensions.isNullOrBlank(s)) {
                processInstanceDiscoveryResponse(s);
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append(AuthorityValidationMetadataCache.TAG);
            sb.append(":processInstanceDiscoveryMetadata");
            Logger.v(sb.toString(), "No metadata returned from instance discovery.");
            concurrentMap = AuthorityValidationMetadataCache.sAadAuthorityHostMetadata;
            instanceDiscoveryMetadata = new InstanceDiscoveryMetadata(lowerCase, lowerCase);
        }
        concurrentMap.put(lowerCase, instanceDiscoveryMetadata);
    }
    
    private static void processInstanceDiscoveryResponse(final String s) throws JSONException {
        final JSONArray jsonArray = new JSONArray(s);
        for (int i = 0; i < jsonArray.length(); ++i) {
            final InstanceDiscoveryMetadata processSingleJsonArray = processSingleJsonArray(new JSONObject(jsonArray.get(i).toString()));
            final Iterator<String> iterator = processSingleJsonArray.getAliases().iterator();
            while (iterator.hasNext()) {
                AuthorityValidationMetadataCache.sAadAuthorityHostMetadata.put(iterator.next().toLowerCase(Locale.US), processSingleJsonArray);
            }
        }
    }
    
    private static InstanceDiscoveryMetadata processSingleJsonArray(final JSONObject jsonObject) throws JSONException {
        final String string = jsonObject.getString("preferred_network");
        final String string2 = jsonObject.getString("preferred_cache");
        final JSONArray jsonArray = jsonObject.getJSONArray("aliases");
        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < jsonArray.length(); ++i) {
            list.add(jsonArray.getString(i));
        }
        return new InstanceDiscoveryMetadata(string, string2, list);
    }
    
    static void updateInstanceDiscoveryMap(final String s, final InstanceDiscoveryMetadata instanceDiscoveryMetadata) {
        AuthorityValidationMetadataCache.sAadAuthorityHostMetadata.put(s.toLowerCase(Locale.US), instanceDiscoveryMetadata);
    }
}
