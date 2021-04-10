package org.apache.http.entity.mime;

import org.apache.james.mime4j.parser.*;
import org.apache.james.mime4j.util.*;

public class MinimalField implements Field
{
    private final String name;
    private ByteSequence raw;
    private final String value;
    
    MinimalField(final String name, final String value) {
        this.name = name;
        this.value = value;
        this.raw = null;
    }
    
    @Override
    public String getBody() {
        return this.value;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public ByteSequence getRaw() {
        if (this.raw == null) {
            this.raw = ContentUtil.encode(this.toString());
        }
        return this.raw;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.name);
        sb.append(": ");
        sb.append(this.value);
        return sb.toString();
    }
}
