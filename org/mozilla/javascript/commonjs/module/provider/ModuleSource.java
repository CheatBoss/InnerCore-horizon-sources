package org.mozilla.javascript.commonjs.module.provider;

import java.net.*;
import java.io.*;

public class ModuleSource implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final URI base;
    private final Reader reader;
    private final Object securityDomain;
    private final URI uri;
    private final Object validator;
    
    public ModuleSource(final Reader reader, final Object securityDomain, final URI uri, final URI base, final Object validator) {
        this.reader = reader;
        this.securityDomain = securityDomain;
        this.uri = uri;
        this.base = base;
        this.validator = validator;
    }
    
    public URI getBase() {
        return this.base;
    }
    
    public Reader getReader() {
        return this.reader;
    }
    
    public Object getSecurityDomain() {
        return this.securityDomain;
    }
    
    public URI getUri() {
        return this.uri;
    }
    
    public Object getValidator() {
        return this.validator;
    }
}
