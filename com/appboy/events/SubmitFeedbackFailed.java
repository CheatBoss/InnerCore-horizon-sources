package com.appboy.events;

import com.appboy.models.outgoing.*;
import com.appboy.models.response.*;

public final class SubmitFeedbackFailed
{
    private final Feedback a;
    private final ResponseError b;
    
    public SubmitFeedbackFailed(final Feedback a, final ResponseError b) {
        this.a = a;
        this.b = b;
    }
    
    public ResponseError getError() {
        return this.b;
    }
    
    public Feedback getFeedback() {
        return this.a;
    }
}
