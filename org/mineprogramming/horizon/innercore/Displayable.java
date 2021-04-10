package org.mineprogramming.horizon.innercore;

import android.content.*;
import java.util.*;
import android.view.*;

public abstract class Displayable
{
    protected final Context context;
    protected final String language;
    
    protected Displayable(final Context context) {
        this.context = context;
        this.language = Locale.getDefault().getLanguage();
    }
    
    public abstract void display(final ViewGroup p0);
}
