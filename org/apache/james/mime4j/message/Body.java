package org.apache.james.mime4j.message;

public interface Body extends Disposable
{
    Entity getParent();
    
    void setParent(final Entity p0);
}
