package com.microsoft.aad.adal;

import android.util.*;
import java.util.*;

class DefaultDispatcher
{
    private final IDispatcher mDispatcher;
    private final Map<String, List<IEvents>> mObjectsToBeDispatched;
    
    private DefaultDispatcher() {
        this.mObjectsToBeDispatched = new HashMap<String, List<IEvents>>();
        this.mDispatcher = null;
    }
    
    DefaultDispatcher(final IDispatcher mDispatcher) {
        this.mObjectsToBeDispatched = new HashMap<String, List<IEvents>>();
        this.mDispatcher = mDispatcher;
    }
    
    void flush(final String s) {
    }
    // monitorenter(this)
    // monitorexit(this)
    
    IDispatcher getDispatcher() {
        return this.mDispatcher;
    }
    
    Map<String, List<IEvents>> getObjectsToBeDispatched() {
        return this.mObjectsToBeDispatched;
    }
    
    void receive(final String s, final IEvents events) {
        if (this.mDispatcher == null) {
            return;
        }
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        for (final Pair<String, String> pair : events.getEvents()) {
            hashMap.put((String)pair.first, (String)pair.second);
        }
        this.mDispatcher.dispatchEvent(hashMap);
    }
}
