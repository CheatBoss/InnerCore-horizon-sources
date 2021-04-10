package org.apache.http.impl;

import org.apache.http.*;
import java.util.*;

@Deprecated
public class EnglishReasonPhraseCatalog implements ReasonPhraseCatalog
{
    public static final EnglishReasonPhraseCatalog INSTANCE;
    
    protected EnglishReasonPhraseCatalog() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String getReason(final int n, final Locale locale) {
        throw new RuntimeException("Stub!");
    }
}
