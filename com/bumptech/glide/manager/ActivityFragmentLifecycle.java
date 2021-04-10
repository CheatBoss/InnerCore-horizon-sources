package com.bumptech.glide.manager;

import com.bumptech.glide.util.*;
import java.util.*;

class ActivityFragmentLifecycle implements Lifecycle
{
    private boolean isDestroyed;
    private boolean isStarted;
    private final Set<LifecycleListener> lifecycleListeners;
    
    ActivityFragmentLifecycle() {
        this.lifecycleListeners = Collections.newSetFromMap(new WeakHashMap<LifecycleListener, Boolean>());
    }
    
    @Override
    public void addListener(final LifecycleListener lifecycleListener) {
        this.lifecycleListeners.add(lifecycleListener);
        if (this.isDestroyed) {
            lifecycleListener.onDestroy();
            return;
        }
        if (this.isStarted) {
            lifecycleListener.onStart();
            return;
        }
        lifecycleListener.onStop();
    }
    
    void onDestroy() {
        this.isDestroyed = true;
        final Iterator<LifecycleListener> iterator = Util.getSnapshot(this.lifecycleListeners).iterator();
        while (iterator.hasNext()) {
            iterator.next().onDestroy();
        }
    }
    
    void onStart() {
        this.isStarted = true;
        final Iterator<LifecycleListener> iterator = Util.getSnapshot(this.lifecycleListeners).iterator();
        while (iterator.hasNext()) {
            iterator.next().onStart();
        }
    }
    
    void onStop() {
        this.isStarted = false;
        final Iterator<LifecycleListener> iterator = Util.getSnapshot(this.lifecycleListeners).iterator();
        while (iterator.hasNext()) {
            iterator.next().onStop();
        }
    }
}
