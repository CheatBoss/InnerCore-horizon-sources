package org.apache.james.mime4j.parser;

import org.apache.james.mime4j.util.*;

class RawField implements Field
{
    private String body;
    private int colonIdx;
    private String name;
    private final ByteSequence raw;
    
    public RawField(final ByteSequence raw, final int colonIdx) {
        this.raw = raw;
        this.colonIdx = colonIdx;
    }
    
    private String parseBody() {
        final int n = this.colonIdx + 1;
        return ContentUtil.decode(this.raw, n, this.raw.length() - n);
    }
    
    private String parseName() {
        return ContentUtil.decode(this.raw, 0, this.colonIdx);
    }
    
    @Override
    public String getBody() {
        if (this.body == null) {
            this.body = this.parseBody();
        }
        return this.body;
    }
    
    @Override
    public String getName() {
        if (this.name == null) {
            this.name = this.parseName();
        }
        return this.name;
    }
    
    @Override
    public ByteSequence getRaw() {
        return this.raw;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getName());
        sb.append(':');
        sb.append(this.getBody());
        return sb.toString();
    }
}
