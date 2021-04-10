package com.microsoft.xbox.idp.model;

import com.google.gson.reflect.*;
import java.io.*;
import com.google.gson.*;
import java.lang.reflect.*;
import com.google.gson.stream.*;
import java.util.*;

public final class Privacy
{
    public static GsonBuilder registerAdapters(final GsonBuilder gsonBuilder) {
        return gsonBuilder.registerTypeAdapter(new TypeToken<Map<Key, Value>>() {}.getType(), new SettingsAdapter());
    }
    
    public enum Key
    {
        CanShareIdentity, 
        CollectVoiceData, 
        CommunicateUsingTextAndVoice, 
        CommunicateUsingVideo, 
        None, 
        ShareExerciseInfo, 
        ShareFriendList, 
        ShareGameHistory, 
        ShareIdentity, 
        ShareIdentityTransitively, 
        SharePresence, 
        ShareProfile, 
        ShareRecordedGameSessions, 
        ShareVideoAndMusicStatus, 
        ShareXboxMusicActivity;
    }
    
    public static class Setting
    {
        public Key setting;
        public Value value;
    }
    
    public static class Settings
    {
        public Map<Key, Value> settings;
        
        public static Settings newWithMap() {
            final Settings settings = new Settings();
            settings.settings = new HashMap<Key, Value>();
            return settings;
        }
        
        public boolean isSettingSet(final Key key) {
            final Map<Key, Value> settings = this.settings;
            if (settings != null) {
                final Value value = settings.get(key);
                if (value != null && value != Value.NotSet) {
                    return true;
                }
            }
            return false;
        }
    }
    
    private static class SettingsAdapter extends TypeAdapter<Map<Key, Value>>
    {
        public Map<Key, Value> read(final JsonReader jsonReader) throws IOException {
            final Setting[] array = (Setting[])new Gson().fromJson(jsonReader, (Type)Setting[].class);
            final HashMap<Key, Value> hashMap = new HashMap<Key, Value>();
            for (int length = array.length, i = 0; i < length; ++i) {
                final Setting setting = array[i];
                if (setting.setting != null && setting.value != null) {
                    hashMap.put(setting.setting, setting.value);
                }
            }
            return hashMap;
        }
        
        @Override
        public void write(final JsonWriter jsonWriter, final Map<Key, Value> map) throws IOException {
            final Setting[] array = new Setting[map.size()];
            final Iterator<Map.Entry<Key, Value>> iterator = map.entrySet().iterator();
            int n = -1;
            while (iterator.hasNext()) {
                final Map.Entry<Key, Value> entry = iterator.next();
                final Setting setting = new Setting();
                setting.setting = entry.getKey();
                setting.value = entry.getValue();
                ++n;
                array[n] = setting;
            }
            new Gson().toJson((Object)array, (Type)Setting[].class, jsonWriter);
        }
    }
    
    public enum Value
    {
        Blocked, 
        Everyone, 
        FriendCategoryShareIdentity, 
        NotSet, 
        PeopleOnMyList;
    }
}
