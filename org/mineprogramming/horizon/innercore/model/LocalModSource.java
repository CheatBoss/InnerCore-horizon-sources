package org.mineprogramming.horizon.innercore.model;

import java.util.*;

public class LocalModSource extends ItemSource
{
    private final ModTracker modTracker;
    
    public LocalModSource(final ModTracker modTracker) {
        this.modTracker = modTracker;
    }
    
    @Override
    public void updateList() {
        this.clearItems();
        final Iterator<String> iterator = this.modTracker.getModLocations().iterator();
        while (iterator.hasNext()) {
            final ModItem modItem = new ModItem(iterator.next());
            if (modItem.getIcon() == null) {
                modItem.setIcon(LocalModSource.missingIcon);
            }
            this.addItem(modItem);
        }
        this.notifyLoadLast();
    }
}
