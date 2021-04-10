package com.zhekasmirnov.apparatus.adapter.env;

import com.zhekasmirnov.apparatus.modloader.*;
import java.io.*;

public interface EnvironmentSetupProxy
{
    void addBehaviorPackDirectory(final ApparatusMod p0, final File p1);
    
    void addGuiAssetsDirectory(final ApparatusMod p0, final File p1);
    
    void addJavaDirectory(final ApparatusMod p0, final File p1);
    
    void addNativeDirectory(final ApparatusMod p0, final File p1);
    
    void addResourceDirectory(final ApparatusMod p0, final File p1);
    
    void addResourcePackDirectory(final ApparatusMod p0, final File p1);
}
