package com.microsoft.aad.adal;

import java.lang.reflect.*;
import com.google.gson.*;

public final class TokenCacheItemSerializationAdapater implements JsonDeserializer<TokenCacheItem>, JsonSerializer<TokenCacheItem>
{
    private static final String TAG;
    
    static {
        TAG = TokenCacheItemSerializationAdapater.class.getSimpleName();
    }
    
    private void throwIfParameterMissing(final JsonObject jsonObject, final String s) {
        if (jsonObject.has(s)) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(TokenCacheItemSerializationAdapater.TAG);
        sb.append("Attribute ");
        sb.append(s);
        sb.append(" is missing for deserialization.");
        throw new JsonParseException(sb.toString());
    }
    
    public TokenCacheItem deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        final JsonObject asJsonObject = jsonElement.getAsJsonObject();
        this.throwIfParameterMissing(asJsonObject, "authority");
        this.throwIfParameterMissing(asJsonObject, "id_token");
        this.throwIfParameterMissing(asJsonObject, "foci");
        this.throwIfParameterMissing(asJsonObject, "refresh_token");
        final String asString = asJsonObject.get("id_token").getAsString();
        final TokenCacheItem tokenCacheItem = new TokenCacheItem();
        try {
            final IdToken idToken = new IdToken(asString);
            tokenCacheItem.setUserInfo(new UserInfo(idToken));
            tokenCacheItem.setTenantId(idToken.getTenantId());
            tokenCacheItem.setAuthority(asJsonObject.get("authority").getAsString());
            tokenCacheItem.setIsMultiResourceRefreshToken(true);
            tokenCacheItem.setRawIdToken(asString);
            tokenCacheItem.setFamilyClientId(asJsonObject.get("foci").getAsString());
            tokenCacheItem.setRefreshToken(asJsonObject.get("refresh_token").getAsString());
            return tokenCacheItem;
        }
        catch (AuthenticationException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append(TokenCacheItemSerializationAdapater.TAG);
            sb.append(": Could not deserialize into a tokenCacheItem object");
            throw new RuntimeException(sb.toString(), ex);
        }
    }
    
    @Override
    public JsonElement serialize(final TokenCacheItem tokenCacheItem, final Type type, final JsonSerializationContext jsonSerializationContext) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.add("authority", new JsonPrimitive(tokenCacheItem.getAuthority()));
        jsonObject.add("refresh_token", new JsonPrimitive(tokenCacheItem.getRefreshToken()));
        jsonObject.add("id_token", new JsonPrimitive(tokenCacheItem.getRawIdToken()));
        jsonObject.add("foci", new JsonPrimitive(tokenCacheItem.getFamilyClientId()));
        return jsonObject;
    }
}
