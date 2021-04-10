package com.google.firebase.components;

import java.util.*;

public class DependencyCycleException extends DependencyException
{
    private final List<Component<?>> zza;
    
    public DependencyCycleException(final List<Component<?>> zza) {
        final StringBuilder sb = new StringBuilder("Dependency cycle detected: ");
        sb.append(Arrays.toString(zza.toArray()));
        super(sb.toString());
        this.zza = zza;
    }
}
