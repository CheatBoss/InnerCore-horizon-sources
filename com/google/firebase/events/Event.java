package com.google.firebase.events;

public class Event<T>
{
    private final Class<T> zza;
    private final T zzb;
    
    public T getPayload() {
        return this.zzb;
    }
    
    public Class<T> getType() {
        return this.zza;
    }
    
    @Override
    public String toString() {
        return String.format("Event{type: %s, payload: %s}", this.zza, this.zzb);
    }
}
