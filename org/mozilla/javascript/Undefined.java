package org.mozilla.javascript;

import java.io.*;

public class Undefined implements Serializable
{
    public static final Object instance;
    static final long serialVersionUID = 9195680630202616767L;
    
    static {
        instance = new Undefined();
    }
    
    private Undefined() {
    }
    
    public Object readResolve() {
        return Undefined.instance;
    }
}
