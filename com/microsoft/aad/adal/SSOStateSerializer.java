package com.microsoft.aad.adal;

import com.google.gson.annotations.*;
import java.lang.reflect.*;
import java.util.*;
import com.google.gson.*;
import org.json.*;

final class SSOStateSerializer
{
    private static final Gson GSON;
    @SerializedName("tokenCacheItems")
    private final List<TokenCacheItem> mTokenCacheItems;
    @SerializedName("version")
    private final int version;
    
    static {
        GSON = new GsonBuilder().registerTypeAdapter(TokenCacheItem.class, new TokenCacheItemSerializationAdapater()).create();
    }
    
    private SSOStateSerializer() {
        this.version = 1;
        this.mTokenCacheItems = new ArrayList<TokenCacheItem>();
    }
    
    private SSOStateSerializer(final TokenCacheItem tokenCacheItem) {
        this.version = 1;
        final ArrayList<TokenCacheItem> mTokenCacheItems = new ArrayList<TokenCacheItem>();
        this.mTokenCacheItems = mTokenCacheItems;
        if (tokenCacheItem != null) {
            mTokenCacheItems.add(tokenCacheItem);
            return;
        }
        throw new IllegalArgumentException("tokenItem is null");
    }
    
    static TokenCacheItem deserialize(final String s) throws AuthenticationException {
        return new SSOStateSerializer().internalDeserialize(s);
    }
    
    private TokenCacheItem getTokenItem() throws AuthenticationException {
        final List<TokenCacheItem> mTokenCacheItems = this.mTokenCacheItems;
        if (mTokenCacheItems != null && !mTokenCacheItems.isEmpty()) {
            return this.mTokenCacheItems.get(0);
        }
        throw new AuthenticationException(ADALError.TOKEN_CACHE_ITEM_NOT_FOUND, "There is no token cache item in the SSOStateContainer.");
    }
    
    private int getVersion() {
        return 1;
    }
    
    private TokenCacheItem internalDeserialize(final String s) throws AuthenticationException {
        try {
            final JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.getInt("version") == this.getVersion()) {
                return ((SSOStateSerializer)SSOStateSerializer.GSON.fromJson(s, (Class)SSOStateSerializer.class)).getTokenItem();
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Fail to deserialize because the blob version is incompatible. The version of the serializedBlob is ");
            sb.append(jsonObject.getInt("version"));
            sb.append(". And the target class version is ");
            sb.append(this.getVersion());
            throw new DeserializationAuthenticationException(sb.toString());
        }
        catch (JsonParseException | JSONException ex) {
            final Object o;
            throw new DeserializationAuthenticationException(((Throwable)o).getMessage());
        }
    }
    
    private String internalSerialize() {
        return SSOStateSerializer.GSON.toJson((Object)this);
    }
    
    static String serialize(final TokenCacheItem tokenCacheItem) {
        return new SSOStateSerializer(tokenCacheItem).internalSerialize();
    }
}
