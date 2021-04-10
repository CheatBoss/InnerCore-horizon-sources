package com.zhekasmirnov.apparatus.ecs.signal;

public interface SignalListener<T>
{
    void receive(final T p0);
}
