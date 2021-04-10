package com.zhekasmirnov.innercore.api.mod.recipes.workbench;

import com.zhekasmirnov.innercore.api.mod.ui.container.*;

public class WorkbenchFieldAPI
{
    public final WorkbenchField container;
    private boolean isPrevented;
    
    public WorkbenchFieldAPI(final WorkbenchField container) {
        this.isPrevented = false;
        this.container = container;
    }
    
    public void decreaseFieldSlot(final int n) {
        final AbstractSlot fieldSlot = this.getFieldSlot(n);
        fieldSlot.set(fieldSlot.getId(), Math.max(0, fieldSlot.getCount() - 1), fieldSlot.getData(), fieldSlot.getExtra());
    }
    
    public AbstractSlot getFieldSlot(final int n) {
        return this.container.getFieldSlot(n);
    }
    
    public boolean isPrevented() {
        return this.isPrevented;
    }
    
    public void prevent() {
        this.isPrevented = true;
    }
}
