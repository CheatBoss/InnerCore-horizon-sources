package com.zhekasmirnov.horizon.modloader.repo.location;

import com.zhekasmirnov.horizon.modloader.repo.storage.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import java.io.*;

public abstract class ModLocation
{
    public abstract File initializeInLocalStorage(final TemporaryStorage p0, final EventLogger p1);
}
