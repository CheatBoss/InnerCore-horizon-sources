package com.zhekasmirnov.horizon.launcher.pack;

import java.io.*;
import android.support.annotation.*;

public interface IPackLocation
{
    InputStream getInstallationPackageStream();
    
    int getInstallationPackageSize();
    
    InputStream getVisualDataStream();
    
    PackManifest getManifest();
    
    @Nullable
    String getUUID();
    
    int getNewestVersionCode();
    
    String getChangelog();
}
