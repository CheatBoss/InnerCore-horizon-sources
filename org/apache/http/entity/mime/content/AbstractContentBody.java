package org.apache.http.entity.mime.content;

import org.apache.james.mime4j.message.*;
import java.util.*;

public abstract class AbstractContentBody extends SingleBody implements ContentBody
{
    private final String mediaType;
    private final String mimeType;
    private Entity parent;
    private final String subType;
    
    public AbstractContentBody(final String s) {
        this.parent = null;
        if (s == null) {
            throw new IllegalArgumentException("MIME type may not be null");
        }
        this.mimeType = s;
        final int index = s.indexOf(47);
        if (index != -1) {
            this.mediaType = s.substring(0, index);
            this.subType = s.substring(index + 1);
            return;
        }
        this.mediaType = s;
        this.subType = null;
    }
    
    @Override
    public void dispose() {
    }
    
    @Override
    public Map<String, String> getContentTypeParameters() {
        return Collections.emptyMap();
    }
    
    @Override
    public String getMediaType() {
        return this.mediaType;
    }
    
    @Override
    public String getMimeType() {
        return this.mimeType;
    }
    
    @Override
    public Entity getParent() {
        return this.parent;
    }
    
    @Override
    public String getSubType() {
        return this.subType;
    }
    
    @Override
    public void setParent(final Entity parent) {
        this.parent = parent;
    }
}
