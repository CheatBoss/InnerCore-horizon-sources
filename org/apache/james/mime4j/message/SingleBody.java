package org.apache.james.mime4j.message;

import java.io.*;

public abstract class SingleBody implements Body
{
    private Entity parent;
    
    protected SingleBody() {
        this.parent = null;
    }
    
    public SingleBody copy() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void dispose() {
    }
    
    @Override
    public Entity getParent() {
        return this.parent;
    }
    
    @Override
    public void setParent(final Entity parent) {
        this.parent = parent;
    }
    
    public abstract void writeTo(final OutputStream p0) throws IOException;
}
