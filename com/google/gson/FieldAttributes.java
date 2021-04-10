package com.google.gson;

import java.lang.reflect.*;
import com.google.gson.internal.*;

public final class FieldAttributes
{
    private final Field field;
    
    public FieldAttributes(final Field field) {
        $Gson$Preconditions.checkNotNull(field);
        this.field = field;
    }
}
