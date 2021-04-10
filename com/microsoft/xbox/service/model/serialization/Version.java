package com.microsoft.xbox.service.model.serialization;

import org.simpleframework.xml.*;

@Root(name = "version")
public class Version
{
    @Element
    public int latest;
    @Element
    public int min;
    @Element
    public String url;
}
