package com.zhekasmirnov.apparatus.ecs.entity;

import com.zhekasmirnov.apparatus.ecs.core.*;

public class LoadedStatusProviderComponent extends EntityComponent
{
    public int getDesiredCheckRate() {
        return 1;
    }
    
    public boolean isLoaded() {
        return true;
    }
}
