package com.google.firebase.events;

public interface EventHandler<T>
{
    void handle(final Event<T> p0);
}
