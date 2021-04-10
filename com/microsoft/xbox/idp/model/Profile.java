package com.microsoft.xbox.idp.model;

import com.google.gson.reflect.*;
import java.io.*;
import com.google.gson.*;
import java.lang.reflect.*;
import com.google.gson.stream.*;
import java.util.*;

public final class Profile
{
    public static GsonBuilder registerAdapters(final GsonBuilder gsonBuilder) {
        return gsonBuilder.registerTypeAdapter(new TypeToken<Map<SettingId, String>>() {}.getType(), new SettingsAdapter());
    }
    
    public static final class GamerpicChangeRequest
    {
        public UserSetting userSetting;
        
        public GamerpicChangeRequest(final String s) {
            this.userSetting = new UserSetting("PublicGamerpic", s);
        }
    }
    
    public static final class GamerpicChoiceList
    {
        public List<GamerpicListEntry> gamerpics;
    }
    
    public static final class GamerpicListEntry
    {
        public String id;
    }
    
    public static final class GamerpicUpdateResponse
    {
    }
    
    public static final class Response
    {
        public User[] profileUsers;
    }
    
    public static final class Setting
    {
        public SettingId id;
        public String value;
    }
    
    public enum SettingId
    {
        AccountTier, 
        AppDisplayName, 
        AppDisplayPicRaw, 
        Background, 
        FirstName, 
        GameDisplayName, 
        GameDisplayPicRaw, 
        Gamerscore, 
        Gamertag, 
        LastName, 
        PreferredColor, 
        PublicGamerpicType, 
        RealName, 
        ShowUserAsAvatar, 
        TenureLevel, 
        TileTransparency, 
        Watermarks, 
        XboxOneRep;
    }
    
    private static class SettingsAdapter extends TypeAdapter<Map<SettingId, String>>
    {
        public Map<SettingId, String> read(final JsonReader jsonReader) throws IOException {
            final Setting[] array = (Setting[])new Gson().fromJson(jsonReader, (Type)Setting[].class);
            final HashMap<SettingId, String> hashMap = new HashMap<SettingId, String>();
            for (int length = array.length, i = 0; i < length; ++i) {
                final Setting setting = array[i];
                hashMap.put(setting.id, setting.value);
            }
            return hashMap;
        }
        
        @Override
        public void write(final JsonWriter jsonWriter, final Map<SettingId, String> map) throws IOException {
            final Setting[] array = new Setting[map.size()];
            final Iterator<Map.Entry<SettingId, String>> iterator = map.entrySet().iterator();
            int n = -1;
            while (iterator.hasNext()) {
                final Map.Entry<SettingId, String> entry = iterator.next();
                final Setting setting = new Setting();
                setting.id = entry.getKey();
                setting.value = entry.getValue();
                ++n;
                array[n] = setting;
            }
            new Gson().toJson((Object)array, (Type)Setting[].class, jsonWriter);
        }
    }
    
    public static final class User
    {
        public String id;
        public boolean isSponsoredUser;
        public Map<SettingId, String> settings;
    }
    
    public static final class UserSetting
    {
        public String id;
        public String value;
        
        public UserSetting(final String id, final String value) {
            this.id = id;
            this.value = value;
        }
    }
}
