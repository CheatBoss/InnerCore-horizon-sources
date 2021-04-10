package org.apache.http;

import java.io.*;

@Deprecated
public class ProtocolVersion implements Serializable
{
    protected final int major;
    protected final int minor;
    protected final String protocol;
    
    public ProtocolVersion(final String s, final int n, final int n2) {
        throw new RuntimeException("Stub!");
    }
    
    public Object clone() throws CloneNotSupportedException {
        throw new RuntimeException("Stub!");
    }
    
    public int compareToVersion(final ProtocolVersion protocolVersion) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public final boolean equals(final Object o) {
        throw new RuntimeException("Stub!");
    }
    
    public ProtocolVersion forVersion(final int n, final int n2) {
        throw new RuntimeException("Stub!");
    }
    
    public final int getMajor() {
        throw new RuntimeException("Stub!");
    }
    
    public final int getMinor() {
        throw new RuntimeException("Stub!");
    }
    
    public final String getProtocol() {
        throw new RuntimeException("Stub!");
    }
    
    public final boolean greaterEquals(final ProtocolVersion protocolVersion) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public final int hashCode() {
        throw new RuntimeException("Stub!");
    }
    
    public boolean isComparable(final ProtocolVersion protocolVersion) {
        throw new RuntimeException("Stub!");
    }
    
    public final boolean lessEquals(final ProtocolVersion protocolVersion) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String toString() {
        throw new RuntimeException("Stub!");
    }
}
