package org.apache.james.mime4j.parser;

import java.io.*;
import org.apache.james.mime4j.descriptor.*;

public class RawEntity implements EntityStateMachine
{
    private int state;
    private final InputStream stream;
    
    RawEntity(final InputStream stream) {
        this.stream = stream;
        this.state = 2;
    }
    
    @Override
    public EntityStateMachine advance() {
        this.state = -1;
        return null;
    }
    
    @Override
    public BodyDescriptor getBodyDescriptor() {
        return null;
    }
    
    @Override
    public InputStream getContentStream() {
        return this.stream;
    }
    
    @Override
    public Field getField() {
        return null;
    }
    
    public String getFieldName() {
        return null;
    }
    
    public String getFieldValue() {
        return null;
    }
    
    @Override
    public int getState() {
        return this.state;
    }
    
    @Override
    public void setRecursionMode(final int n) {
    }
}
