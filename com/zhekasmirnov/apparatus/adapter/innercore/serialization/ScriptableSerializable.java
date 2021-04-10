package com.zhekasmirnov.apparatus.adapter.innercore.serialization;

public interface ScriptableSerializable
{
    Object deserialize(final ScriptableData p0);
    
    ScriptableData serialize(final Object p0);
}
