package com.zhekasmirnov.apparatus.ecs.signal;

import java.util.concurrent.*;
import java.util.*;

public class Signal<T>
{
    private List<SignalListener<T>> listeners;
    
    public Signal() {
        this.listeners = new CopyOnWriteArrayList<SignalListener<T>>();
    }
    
    public void addSignalListener(final SignalListener<T> signalListener) {
        this.listeners.add(signalListener);
    }
    
    public void dispatch(final T t) {
        final Iterator<SignalListener<T>> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().receive(t);
        }
    }
    
    public void removeSignalListener(final SignalListener<T> signalListener) {
        this.listeners.remove(signalListener);
    }
}
