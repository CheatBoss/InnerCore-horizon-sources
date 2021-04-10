package com.zhekasmirnov.horizon.launcher.pack;

import java.util.*;

public abstract class PackRepository
{
    public abstract void fetch();
    
    public abstract List<String> getAllPacksUUIDs();
    
    public abstract IPackLocation getLocationForUUID(final String p0);
    
    public List<String> getPackSuggestions() {
        return new ArrayList<String>();
    }
}
