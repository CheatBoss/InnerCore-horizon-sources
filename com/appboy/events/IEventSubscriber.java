package com.appboy.events;

public interface IEventSubscriber<T>
{
    void trigger(final T p0);
}
