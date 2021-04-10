package com.zhekasmirnov.innercore.api.commontypes;

import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.mod.*;

public class Coords extends ScriptableObject
{
    public Coords(final double n, final double n2, final double n3) {
        this.put("x", (Scriptable)this, (Object)n);
        this.put("y", (Scriptable)this, (Object)n2);
        this.put("z", (Scriptable)this, (Object)n3);
    }
    
    public Coords(final int n, final int n2, final int n3) {
        this.put("x", (Scriptable)this, (Object)n);
        this.put("y", (Scriptable)this, (Object)n2);
        this.put("z", (Scriptable)this, (Object)n3);
    }
    
    public Coords(final int n, final int n2, final int n3, final int n4) {
        this(n, n2, n3, n4, false);
    }
    
    public Coords(final int n, final int n2, final int n3, final int n4, final boolean b) {
        int n5 = n;
        int n6 = n2;
        int n7 = n3;
        if (!b) {
            switch (n4) {
                default: {
                    n5 = n;
                    n6 = n2;
                    n7 = n3;
                    break;
                }
                case 5: {
                    n5 = n + 1;
                    n6 = n2;
                    n7 = n3;
                    break;
                }
                case 4: {
                    n5 = n - 1;
                    n6 = n2;
                    n7 = n3;
                    break;
                }
                case 3: {
                    n7 = n3 + 1;
                    n5 = n;
                    n6 = n2;
                    break;
                }
                case 2: {
                    n7 = n3 - 1;
                    n5 = n;
                    n6 = n2;
                    break;
                }
                case 1: {
                    n6 = n2 + 1;
                    n5 = n;
                    n7 = n3;
                    break;
                }
                case 0: {
                    n6 = n2 - 1;
                    n7 = n3;
                    n5 = n;
                    break;
                }
            }
        }
        this.put("x", (Scriptable)this, (Object)n);
        this.put("y", (Scriptable)this, (Object)n2);
        this.put("z", (Scriptable)this, (Object)n3);
        this.put("side", (Scriptable)this, (Object)n4);
        final ScriptableObject empty = ScriptableObjectHelper.createEmpty();
        empty.put("x", (Scriptable)empty, (Object)n5);
        empty.put("y", (Scriptable)empty, (Object)n6);
        empty.put("z", (Scriptable)empty, (Object)n7);
        this.put("relative", (Scriptable)this, (Object)empty);
    }
    
    public String getClassName() {
        return "Coords";
    }
    
    public Coords setSide(final int n) {
        this.put("side", (Scriptable)this, (Object)n);
        return this;
    }
}
