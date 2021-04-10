package com.zhekasmirnov.horizon.modloader.resource.processor;

import com.zhekasmirnov.horizon.modloader.resource.*;
import com.zhekasmirnov.horizon.modloader.resource.directory.*;
import java.util.*;

public interface ResourceProcessor
{
    void initialize(final ResourceManager p0);
    
    void process(final Resource p0, final Collection<Resource> p1);
}
