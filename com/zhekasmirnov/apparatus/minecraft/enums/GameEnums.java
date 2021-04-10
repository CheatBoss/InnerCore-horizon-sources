package com.zhekasmirnov.apparatus.minecraft.enums;

import com.zhekasmirnov.apparatus.minecraft.version.*;
import com.zhekasmirnov.innercore.utils.*;
import com.zhekasmirnov.innercore.api.log.*;
import org.json.*;
import java.util.*;

public class GameEnums
{
    private static final GameEnums singleton;
    private final Map<MinecraftVersion, EnumsContainer> enumsContainerForVersion;
    
    static {
        singleton = new GameEnums();
        for (final MinecraftVersion minecraftVersion : MinecraftVersions.getAllVersions()) {
            try {
                final EnumsContainer orAddContainerForVersion = getSingleton().getOrAddContainerForVersion(minecraftVersion);
                final StringBuilder sb = new StringBuilder();
                sb.append("innercore/enum/enums-");
                sb.append(minecraftVersion.getCode());
                sb.append(".json");
                orAddContainerForVersion.addEnumsFromJson(FileTools.getAssetAsJSON(sb.toString()));
            }
            catch (JSONException ex) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("GameEnums failed to load enums for game version ");
                sb2.append(minecraftVersion);
                ICLog.e("ERROR", sb2.toString(), (Throwable)ex);
            }
        }
    }
    
    public GameEnums() {
        this.enumsContainerForVersion = new HashMap<MinecraftVersion, EnumsContainer>();
    }
    
    public static int getInt(final Object o) {
        return getInt(o, 0);
    }
    
    public static int getInt(final Object o, final int n) {
        if (o instanceof Number) {
            return ((Number)o).intValue();
        }
        return n;
    }
    
    public static GameEnums getSingleton() {
        synchronized (GameEnums.class) {
            return GameEnums.singleton;
        }
    }
    
    public static String getString(final Object o) {
        return getString(o, null);
    }
    
    public static String getString(final Object o, final String s) {
        if (o instanceof String) {
            return (String)o;
        }
        return s;
    }
    
    public Object convertBetweenVersions(final String s, final Object o, final MinecraftVersion minecraftVersion, final MinecraftVersion minecraftVersion2) {
        final String keyForEnum = this.getKeyForEnum(s, o, minecraftVersion);
        if (keyForEnum != null) {
            return this.getEnum(s, keyForEnum, minecraftVersion2);
        }
        return null;
    }
    
    public Object convertFromVersion(final String s, final Object o, final MinecraftVersion minecraftVersion) {
        return this.convertBetweenVersions(s, o, minecraftVersion, MinecraftVersions.getCurrent());
    }
    
    public Object convertToVersion(final String s, final Object o, final MinecraftVersion minecraftVersion) {
        return this.convertBetweenVersions(s, o, MinecraftVersions.getCurrent(), minecraftVersion);
    }
    
    public Object getEnum(final String s, final String s2) {
        return this.getEnum(s, s2, MinecraftVersions.getCurrent());
    }
    
    public Object getEnum(final String s, final String s2, final MinecraftVersion minecraftVersion) {
        final EnumsContainer enumsContainer = this.enumsContainerForVersion.get(minecraftVersion);
        if (enumsContainer != null) {
            return enumsContainer.getEnum(s, s2);
        }
        return null;
    }
    
    public int getIntEnumOrConvertFromLegacyVersion(final String s, final Object o, final int n, final MinecraftVersion minecraftVersion) {
        if (o instanceof Number) {
            return getInt(this.convertFromVersion(s, ((Number)o).intValue(), minecraftVersion), n);
        }
        if (o instanceof String) {
            return getInt(this.getEnum(s, (String)o), n);
        }
        return n;
    }
    
    public String getKeyForEnum(final String s, final Object o) {
        return this.getKeyForEnum(s, o, MinecraftVersions.getCurrent());
    }
    
    public String getKeyForEnum(final String s, final Object o, final MinecraftVersion minecraftVersion) {
        final EnumsContainer enumsContainer = this.enumsContainerForVersion.get(minecraftVersion);
        if (enumsContainer != null) {
            return enumsContainer.getKeyForEnum(s, o);
        }
        return null;
    }
    
    public EnumsContainer getOrAddContainerForVersion(final MinecraftVersion minecraftVersion) {
        EnumsContainer enumsContainer;
        if ((enumsContainer = this.enumsContainerForVersion.get(minecraftVersion)) == null) {
            this.enumsContainerForVersion.put(minecraftVersion, enumsContainer = new EnumsContainer(minecraftVersion));
        }
        return enumsContainer;
    }
}
