package org.spongycastle.util.io.pem;

import java.util.*;

public class PemObject implements PemObjectGenerator
{
    private static final List EMPTY_LIST;
    private byte[] content;
    private List headers;
    private String type;
    
    static {
        EMPTY_LIST = Collections.unmodifiableList((List<?>)new ArrayList<Object>());
    }
    
    public PemObject(final String type, final List list, final byte[] content) {
        this.type = type;
        this.headers = Collections.unmodifiableList((List<?>)list);
        this.content = content;
    }
    
    public PemObject(final String s, final byte[] array) {
        this(s, PemObject.EMPTY_LIST, array);
    }
    
    @Override
    public PemObject generate() throws PemGenerationException {
        return this;
    }
    
    public byte[] getContent() {
        return this.content;
    }
    
    public List getHeaders() {
        return this.headers;
    }
    
    public String getType() {
        return this.type;
    }
}
