package com.bumptech.glide.load.model;

import java.util.*;

public interface Headers
{
    public static final Headers NONE = new Headers() {
        @Override
        public Map<String, String> getHeaders() {
            return Collections.emptyMap();
        }
    };
    
    Map<String, String> getHeaders();
}
