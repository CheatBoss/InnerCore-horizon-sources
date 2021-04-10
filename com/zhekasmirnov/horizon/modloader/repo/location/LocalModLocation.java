package com.zhekasmirnov.horizon.modloader.repo.location;

import java.io.*;
import com.zhekasmirnov.horizon.modloader.repo.storage.*;
import com.zhekasmirnov.horizon.runtime.logger.*;

public class LocalModLocation extends ModLocation
{
    private final File mod;
    
    public LocalModLocation(final File mod) {
        this.mod = mod;
    }
    
    @Override
    public File initializeInLocalStorage(final TemporaryStorage storage, final EventLogger logger) {
        return this.mod;
    }
    
    @Override
    public String toString() {
        return "[LocalModLocation path=" + this.mod + "]";
    }
}
