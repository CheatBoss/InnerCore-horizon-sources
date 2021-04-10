package org.mozilla.javascript;

public class ContinuationPending extends RuntimeException
{
    private static final long serialVersionUID = 4956008116771118856L;
    private Object applicationState;
    private NativeContinuation continuationState;
    
    ContinuationPending(final NativeContinuation continuationState) {
        this.continuationState = continuationState;
    }
    
    public Object getApplicationState() {
        return this.applicationState;
    }
    
    public Object getContinuation() {
        return this.continuationState;
    }
    
    NativeContinuation getContinuationState() {
        return this.continuationState;
    }
    
    public void setApplicationState(final Object applicationState) {
        this.applicationState = applicationState;
    }
}
