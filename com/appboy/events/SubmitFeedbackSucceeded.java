package com.appboy.events;

import com.appboy.models.outgoing.*;

public final class SubmitFeedbackSucceeded
{
    private final Feedback a;
    
    public SubmitFeedbackSucceeded(final Feedback a) {
        this.a = a;
    }
    
    public Feedback getFeedback() {
        return this.a;
    }
}
