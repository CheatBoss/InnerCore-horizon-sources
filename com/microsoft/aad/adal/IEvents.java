package com.microsoft.aad.adal;

import android.util.*;
import java.util.*;

interface IEvents
{
    int getDefaultEventCount();
    
    List<Pair<String, String>> getEvents();
    
    void processEvent(final Map<String, String> p0);
    
    void setProperty(final String p0, final String p1);
}
