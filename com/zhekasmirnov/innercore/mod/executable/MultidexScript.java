package com.zhekasmirnov.innercore.mod.executable;

import org.mozilla.javascript.*;
import java.util.*;

public class MultidexScript implements Script
{
    private ArrayList<Script> scripts;
    
    public MultidexScript() {
        this.scripts = new ArrayList<Script>();
    }
    
    public void addScript(final Script script) {
        this.scripts.add(script);
    }
    
    public Object exec(final Context context, final Scriptable scriptable) {
        Object o = null;
        final Context assureContextForCurrentThread = Compiler.assureContextForCurrentThread();
        final Iterator<Script> iterator = this.scripts.iterator();
        while (iterator.hasNext()) {
            final Object exec = iterator.next().exec(assureContextForCurrentThread, scriptable);
            if (exec != null) {
                o = exec;
            }
        }
        return o;
    }
    
    public int getScriptCount() {
        return this.scripts.size();
    }
}
