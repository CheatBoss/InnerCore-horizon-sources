package com.bumptech.glide.manager;

import com.bumptech.glide.request.*;
import com.bumptech.glide.util.*;
import java.util.*;

public class RequestTracker
{
    private boolean isPaused;
    private final List<Request> pendingRequests;
    private final Set<Request> requests;
    
    public RequestTracker() {
        this.requests = Collections.newSetFromMap(new WeakHashMap<Request, Boolean>());
        this.pendingRequests = new ArrayList<Request>();
    }
    
    void addRequest(final Request request) {
        this.requests.add(request);
    }
    
    public void clearRequests() {
        final Iterator<Request> iterator = Util.getSnapshot(this.requests).iterator();
        while (iterator.hasNext()) {
            iterator.next().clear();
        }
        this.pendingRequests.clear();
    }
    
    public boolean isPaused() {
        return this.isPaused;
    }
    
    public void pauseRequests() {
        this.isPaused = true;
        for (final Request request : Util.getSnapshot(this.requests)) {
            if (request.isRunning()) {
                request.pause();
                this.pendingRequests.add(request);
            }
        }
    }
    
    public void removeRequest(final Request request) {
        this.requests.remove(request);
        this.pendingRequests.remove(request);
    }
    
    public void restartRequests() {
        for (final Request request : Util.getSnapshot(this.requests)) {
            if (!request.isComplete() && !request.isCancelled()) {
                request.pause();
                if (!this.isPaused) {
                    request.begin();
                }
                else {
                    this.pendingRequests.add(request);
                }
            }
        }
    }
    
    public void resumeRequests() {
        this.isPaused = false;
        for (final Request request : Util.getSnapshot(this.requests)) {
            if (!request.isComplete() && !request.isCancelled() && !request.isRunning()) {
                request.begin();
            }
        }
        this.pendingRequests.clear();
    }
    
    public void runRequest(final Request request) {
        this.requests.add(request);
        if (!this.isPaused) {
            request.begin();
            return;
        }
        this.pendingRequests.add(request);
    }
}
