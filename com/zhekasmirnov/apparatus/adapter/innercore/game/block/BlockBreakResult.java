package com.zhekasmirnov.apparatus.adapter.innercore.game.block;

import com.android.tools.r8.annotations.*;
import com.zhekasmirnov.apparatus.adapter.innercore.game.item.*;
import com.zhekasmirnov.innercore.api.mod.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.apparatus.util.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

@SynthesizedClassMap({ -$$Lambda$Kpkvth9XbAy56gKaJDODmfpIQ10.class })
public class BlockBreakResult
{
    private int experience;
    private final List<ItemStack> items;
    
    public BlockBreakResult() {
        this.items = new ArrayList<ItemStack>();
    }
    
    public void addExperience(final int n) {
        this.experience += n;
    }
    
    public ScriptableObject asScriptable() {
        final ScriptableObject empty = ScriptableObjectHelper.createEmpty();
        empty.put("experience", (Scriptable)empty, (Object)this.experience);
        empty.put("items", (Scriptable)empty, (Object)ScriptableObjectHelper.createArray(Java8BackComp.stream(this.items).map((Function<? super ItemStack, ?>)-$$Lambda$Kpkvth9XbAy56gKaJDODmfpIQ10.INSTANCE).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList())));
        return empty;
    }
    
    public int getExperience() {
        return this.experience;
    }
    
    public List<ItemStack> getItems() {
        return this.items;
    }
    
    public void setExperience(final int experience) {
        this.experience = experience;
    }
}
