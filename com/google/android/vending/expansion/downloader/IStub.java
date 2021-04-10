package com.google.android.vending.expansion.downloader;

import android.content.*;
import android.os.*;

public interface IStub
{
    void connect(final Context p0);
    
    void disconnect(final Context p0);
    
    Messenger getMessenger();
}
