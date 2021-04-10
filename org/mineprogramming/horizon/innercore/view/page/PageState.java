package org.mineprogramming.horizon.innercore.view.page;

import com.zhekasmirnov.apparatus.util.*;
import java.util.*;

public class PageState extends HashMap<String, Object>
{
    private static final long serialVersionUID = -2796202591735004711L;
    
    public int getInt(final String s) {
        return ((HashMap<K, Integer>)this).get(s);
    }
    
    public int optInt(final String s, final int n) {
        return (int)Java8BackComp.getOrDefault((Map)this, (Object)s, (Object)n);
    }
}
