package com.zhekasmirnov.horizon.modloader.repo.storage;

import com.zhekasmirnov.horizon.runtime.logger.*;
import java.util.*;
import com.zhekasmirnov.horizon.modloader.repo.location.*;

public abstract class ModRepository
{
    public abstract void refresh(final EventLogger p0);
    
    public abstract List<ModLocation> getAllLocations();
}
