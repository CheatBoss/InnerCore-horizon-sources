package org.reflections.scanners;

import com.google.common.base.*;
import com.google.common.collect.*;
import org.reflections.vfs.*;
import javax.annotation.*;
import org.reflections.*;

public interface Scanner
{
    boolean acceptResult(final String p0);
    
    boolean acceptsInput(final String p0);
    
    Scanner filterResultsBy(final Predicate<String> p0);
    
    Multimap<String, String> getStore();
    
    Object scan(final Vfs.File p0, @Nullable final Object p1);
    
    void setConfiguration(final Configuration p0);
    
    void setStore(final Multimap<String, String> p0);
}
