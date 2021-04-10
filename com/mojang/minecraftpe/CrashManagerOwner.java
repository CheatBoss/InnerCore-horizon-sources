package com.mojang.minecraftpe;

interface CrashManagerOwner
{
    SessionInfo findSessionInfoForCrash(final CrashManager p0, final String p1);
    
    String getCachedDeviceId(final CrashManager p0);
    
    void notifyCrashUploadCompleted(final CrashManager p0, final SessionInfo p1);
}
