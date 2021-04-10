package com.google.firebase.components;

import com.google.firebase.inject.*;

public interface ComponentContainer
{
     <T> T get(final Class<T> p0);
    
     <T> Provider<T> getProvider(final Class<T> p0);
}
