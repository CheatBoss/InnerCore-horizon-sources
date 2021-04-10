package org.apache.james.mime4j.parser;

import org.apache.james.mime4j.*;
import org.apache.james.mime4j.descriptor.*;
import java.io.*;

public interface EntityStateMachine
{
    EntityStateMachine advance() throws IOException, MimeException;
    
    BodyDescriptor getBodyDescriptor() throws IllegalStateException;
    
    InputStream getContentStream() throws IllegalStateException;
    
    Field getField() throws IllegalStateException;
    
    int getState();
    
    void setRecursionMode(final int p0);
}
