package com.google.firebase.events;

import java.util.concurrent.*;

public interface Subscriber
{
     <T> void subscribe(final Class<T> p0, final EventHandler<? super T> p1);
    
     <T> void subscribe(final Class<T> p0, final Executor p1, final EventHandler<? super T> p2);
}
