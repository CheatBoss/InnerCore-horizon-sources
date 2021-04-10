package com.microsoft.aad.adal;

import com.google.gson.*;
import java.lang.reflect.*;
import android.content.pm.*;
import java.security.*;
import java.io.*;
import android.os.*;
import java.util.*;
import android.content.*;

public class DefaultTokenCacheStore implements ITokenCacheStore, ITokenStoreQuery
{
    private static final Object LOCK;
    private static final String SHARED_PREFERENCE_NAME = "com.microsoft.aad.adal.cache";
    private static final String TAG = "DefaultTokenCacheStore";
    private static final int TOKEN_VALIDITY_WINDOW = 10;
    private static StorageHelper sHelper;
    private static final long serialVersionUID = 1L;
    private Context mContext;
    private Gson mGson;
    private SharedPreferences mPrefs;
    
    static {
        LOCK = new Object();
    }
    
    public DefaultTokenCacheStore(final Context mContext) {
        this.mGson = new GsonBuilder().registerTypeAdapter(Date.class, new DateTimeAdapter()).create();
        if (mContext == null) {
            throw new IllegalArgumentException("Context is null");
        }
        this.mContext = mContext;
        if (!StringExtensions.isNullOrBlank(AuthenticationSettings.INSTANCE.getSharedPrefPackageName())) {
            try {
                this.mContext = mContext.createPackageContext(AuthenticationSettings.INSTANCE.getSharedPrefPackageName(), 0);
            }
            catch (PackageManager$NameNotFoundException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Package name:");
                sb.append(AuthenticationSettings.INSTANCE.getSharedPrefPackageName());
                sb.append(" is not found");
                throw new IllegalArgumentException(sb.toString());
            }
        }
        if ((this.mPrefs = this.mContext.getSharedPreferences("com.microsoft.aad.adal.cache", 0)) != null) {
            this.validateSecretKeySetting();
            return;
        }
        throw new IllegalStateException(ADALError.DEVICE_SHARED_PREF_IS_NOT_AVAILABLE.getDescription());
    }
    
    private String decrypt(final String s, String decrypt) {
        if (!StringExtensions.isNullOrBlank(s)) {
            try {
                decrypt = this.getStorageHelper().decrypt(decrypt);
                return decrypt;
            }
            catch (GeneralSecurityException | IOException ex) {
                final Throwable t;
                Logger.e("DefaultTokenCacheStore", "Decryption failure. ", "", ADALError.DECRYPTION_FAILED, t);
                this.removeItem(s);
                return null;
            }
        }
        throw new IllegalArgumentException("key is null or blank");
    }
    
    private String encrypt(String encrypt) {
        try {
            encrypt = this.getStorageHelper().encrypt(encrypt);
            return encrypt;
        }
        catch (GeneralSecurityException | IOException ex) {
            final Throwable t;
            Logger.e("DefaultTokenCacheStore", "Encryption failure. ", "", ADALError.ENCRYPTION_FAILED, t);
            return null;
        }
    }
    
    private static Calendar getTokenValidityTime() {
        final Calendar instance = Calendar.getInstance();
        instance.add(13, 10);
        return instance;
    }
    
    private boolean isAboutToExpire(final Date date) {
        final Date time = getTokenValidityTime().getTime();
        return date != null && date.before(time);
    }
    
    private void validateSecretKeySetting() {
        if (AuthenticationSettings.INSTANCE.getSecretKeyData() != null) {
            return;
        }
        if (Build$VERSION.SDK_INT >= 18) {
            return;
        }
        throw new IllegalArgumentException("Secret key must be provided for API < 18. Use AuthenticationSettings.INSTANCE.setSecretKey()");
    }
    
    @Override
    public void clearTokensForUser(final String s) {
        for (final TokenCacheItem tokenCacheItem : this.getTokensForUser(s)) {
            if (tokenCacheItem.getUserInfo() != null && tokenCacheItem.getUserInfo().getUserId() != null && tokenCacheItem.getUserInfo().getUserId().equalsIgnoreCase(s)) {
                try {
                    this.removeItem(CacheKey.createCacheKey(tokenCacheItem));
                }
                catch (AuthenticationException ex) {
                    Logger.e("DefaultTokenCacheStore", "Fail to create cache key. ", "", ex.getCode(), ex);
                }
            }
        }
    }
    
    @Override
    public boolean contains(final String s) {
        if (s != null) {
            return this.mPrefs.contains(s);
        }
        throw new IllegalArgumentException("key");
    }
    
    @Override
    public Iterator<TokenCacheItem> getAll() {
        final Map all = this.mPrefs.getAll();
        final ArrayList list = new ArrayList<TokenCacheItem>(all.values().size());
        for (final Map.Entry<String, V> entry : all.entrySet()) {
            final String decrypt = this.decrypt(entry.getKey(), (String)entry.getValue());
            if (decrypt != null) {
                list.add((TokenCacheItem)this.mGson.fromJson(decrypt, (Class)TokenCacheItem.class));
            }
        }
        return list.iterator();
    }
    
    @Override
    public TokenCacheItem getItem(String decrypt) {
        if (decrypt != null) {
            if (this.mPrefs.contains(decrypt)) {
                decrypt = this.decrypt(decrypt, this.mPrefs.getString(decrypt, ""));
                if (decrypt != null) {
                    return (TokenCacheItem)this.mGson.fromJson(decrypt, (Class)TokenCacheItem.class);
                }
            }
            return null;
        }
        throw new IllegalArgumentException("The key is null.");
    }
    
    protected StorageHelper getStorageHelper() {
        synchronized (DefaultTokenCacheStore.LOCK) {
            if (DefaultTokenCacheStore.sHelper == null) {
                Logger.v("DefaultTokenCacheStore", "Started to initialize storage helper");
                DefaultTokenCacheStore.sHelper = new StorageHelper(this.mContext);
                Logger.v("DefaultTokenCacheStore", "Finished to initialize storage helper");
            }
            return DefaultTokenCacheStore.sHelper;
        }
    }
    
    @Override
    public List<TokenCacheItem> getTokensAboutToExpire() {
        final Iterator<TokenCacheItem> all = this.getAll();
        final ArrayList<TokenCacheItem> list = new ArrayList<TokenCacheItem>();
        while (all.hasNext()) {
            final TokenCacheItem tokenCacheItem = all.next();
            if (this.isAboutToExpire(tokenCacheItem.getExpiresOn())) {
                list.add(tokenCacheItem);
            }
        }
        return list;
    }
    
    @Override
    public List<TokenCacheItem> getTokensForResource(final String s) {
        final Iterator<TokenCacheItem> all = this.getAll();
        final ArrayList<TokenCacheItem> list = new ArrayList<TokenCacheItem>();
        while (all.hasNext()) {
            final TokenCacheItem tokenCacheItem = all.next();
            if (s.equals(tokenCacheItem.getResource())) {
                list.add(tokenCacheItem);
            }
        }
        return list;
    }
    
    @Override
    public List<TokenCacheItem> getTokensForUser(final String s) {
        final Iterator<TokenCacheItem> all = this.getAll();
        final ArrayList<TokenCacheItem> list = new ArrayList<TokenCacheItem>();
        while (all.hasNext()) {
            final TokenCacheItem tokenCacheItem = all.next();
            if (tokenCacheItem.getUserInfo() != null && tokenCacheItem.getUserInfo().getUserId().equalsIgnoreCase(s)) {
                list.add(tokenCacheItem);
            }
        }
        return list;
    }
    
    @Override
    public Set<String> getUniqueUsersWithTokenCache() {
        final Iterator<TokenCacheItem> all = this.getAll();
        final HashSet<String> set = new HashSet<String>();
        while (all.hasNext()) {
            final TokenCacheItem tokenCacheItem = all.next();
            if (tokenCacheItem.getUserInfo() != null && !set.contains(tokenCacheItem.getUserInfo().getUserId())) {
                set.add(tokenCacheItem.getUserInfo().getUserId());
            }
        }
        return set;
    }
    
    @Override
    public void removeAll() {
        final SharedPreferences$Editor edit = this.mPrefs.edit();
        edit.clear();
        edit.apply();
    }
    
    @Override
    public void removeItem(final String s) {
        if (s != null) {
            if (this.mPrefs.contains(s)) {
                final SharedPreferences$Editor edit = this.mPrefs.edit();
                edit.remove(s);
                edit.apply();
            }
            return;
        }
        throw new IllegalArgumentException("key");
    }
    
    @Override
    public void setItem(final String s, final TokenCacheItem tokenCacheItem) {
        if (s == null) {
            throw new IllegalArgumentException("key");
        }
        if (tokenCacheItem == null) {
            throw new IllegalArgumentException("item");
        }
        final String encrypt = this.encrypt(this.mGson.toJson((Object)tokenCacheItem));
        if (encrypt != null) {
            final SharedPreferences$Editor edit = this.mPrefs.edit();
            edit.putString(s, encrypt);
            edit.apply();
            return;
        }
        Logger.e("DefaultTokenCacheStore", "Encrypted output is null. ", "", ADALError.ENCRYPTION_FAILED);
    }
}
