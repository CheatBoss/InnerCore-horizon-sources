package com.android.dx.rop.annotation;

import com.android.dx.util.*;

public enum AnnotationVisibility implements ToHuman
{
    BUILD("build"), 
    EMBEDDED("embedded"), 
    RUNTIME("runtime"), 
    SYSTEM("system");
    
    private final String human;
    
    private AnnotationVisibility(final String human) {
        this.human = human;
    }
    
    @Override
    public String toHuman() {
        return this.human;
    }
}
