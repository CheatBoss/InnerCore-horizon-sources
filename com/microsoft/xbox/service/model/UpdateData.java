package com.microsoft.xbox.service.model;

import android.os.*;

public final class UpdateData
{
    private final Bundle extra;
    private final boolean isFinal;
    private final UpdateType updateType;
    
    public UpdateData(final UpdateType updateType, final boolean isFinal) {
        this.updateType = updateType;
        this.isFinal = isFinal;
        this.extra = null;
    }
    
    public UpdateData(final UpdateType updateType, final boolean isFinal, final Bundle extra) {
        this.updateType = updateType;
        this.isFinal = isFinal;
        this.extra = extra;
    }
    
    public Bundle getExtra() {
        return this.extra;
    }
    
    public boolean getIsFinal() {
        return this.isFinal;
    }
    
    public UpdateType getUpdateType() {
        return this.updateType;
    }
}
