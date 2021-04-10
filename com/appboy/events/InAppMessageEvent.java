package com.appboy.events;

import com.appboy.models.*;
import org.json.*;
import bo.app.*;

public final class InAppMessageEvent
{
    private final IInAppMessage a;
    private final String b;
    
    public InAppMessageEvent(final IInAppMessage a, final String b) {
        this.b = b;
        if (a != null) {
            this.a = a;
            return;
        }
        throw null;
    }
    
    public IInAppMessage getInAppMessage() {
        return this.a;
    }
    
    public String getUserId() {
        return this.b;
    }
    
    @Override
    public String toString() {
        return ec.a(this.a.forJsonPut());
    }
}
