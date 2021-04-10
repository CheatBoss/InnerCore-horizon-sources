package com.bumptech.glide.request;

public class ThumbnailRequestCoordinator implements RequestCoordinator, Request
{
    private RequestCoordinator coordinator;
    private Request full;
    private Request thumb;
    
    public ThumbnailRequestCoordinator() {
        this(null);
    }
    
    public ThumbnailRequestCoordinator(final RequestCoordinator coordinator) {
        this.coordinator = coordinator;
    }
    
    private boolean parentCanNotifyStatusChanged() {
        return this.coordinator == null || this.coordinator.canNotifyStatusChanged(this);
    }
    
    private boolean parentCanSetImage() {
        return this.coordinator == null || this.coordinator.canSetImage(this);
    }
    
    private boolean parentIsAnyResourceSet() {
        return this.coordinator != null && this.coordinator.isAnyResourceSet();
    }
    
    @Override
    public void begin() {
        if (!this.thumb.isRunning()) {
            this.thumb.begin();
        }
        if (!this.full.isRunning()) {
            this.full.begin();
        }
    }
    
    @Override
    public boolean canNotifyStatusChanged(final Request request) {
        return this.parentCanNotifyStatusChanged() && request.equals(this.full) && !this.isAnyResourceSet();
    }
    
    @Override
    public boolean canSetImage(final Request request) {
        return this.parentCanSetImage() && (request.equals(this.full) || !this.full.isResourceSet());
    }
    
    @Override
    public void clear() {
        this.thumb.clear();
        this.full.clear();
    }
    
    @Override
    public boolean isAnyResourceSet() {
        return this.parentIsAnyResourceSet() || this.isResourceSet();
    }
    
    @Override
    public boolean isCancelled() {
        return this.full.isCancelled();
    }
    
    @Override
    public boolean isComplete() {
        return this.full.isComplete() || this.thumb.isComplete();
    }
    
    @Override
    public boolean isFailed() {
        return this.full.isFailed();
    }
    
    @Override
    public boolean isPaused() {
        return this.full.isPaused();
    }
    
    @Override
    public boolean isResourceSet() {
        return this.full.isResourceSet() || this.thumb.isResourceSet();
    }
    
    @Override
    public boolean isRunning() {
        return this.full.isRunning();
    }
    
    @Override
    public void onRequestSuccess(final Request request) {
        if (request.equals(this.thumb)) {
            return;
        }
        if (this.coordinator != null) {
            this.coordinator.onRequestSuccess(this);
        }
        if (!this.thumb.isComplete()) {
            this.thumb.clear();
        }
    }
    
    @Override
    public void pause() {
        this.full.pause();
        this.thumb.pause();
    }
    
    @Override
    public void recycle() {
        this.full.recycle();
        this.thumb.recycle();
    }
    
    public void setRequests(final Request full, final Request thumb) {
        this.full = full;
        this.thumb = thumb;
    }
}
