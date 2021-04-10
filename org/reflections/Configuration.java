package org.reflections;

import javax.annotation.*;
import java.util.concurrent.*;
import com.google.common.base.*;
import org.reflections.adapters.*;
import java.util.*;
import org.reflections.scanners.*;
import org.reflections.serializers.*;
import java.net.*;

public interface Configuration
{
    @Nullable
    ClassLoader[] getClassLoaders();
    
    ExecutorService getExecutorService();
    
    @Nullable
    Predicate<String> getInputsFilter();
    
    MetadataAdapter getMetadataAdapter();
    
    Set<Scanner> getScanners();
    
    Serializer getSerializer();
    
    Set<URL> getUrls();
}
