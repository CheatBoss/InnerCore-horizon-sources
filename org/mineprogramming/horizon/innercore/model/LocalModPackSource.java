package org.mineprogramming.horizon.innercore.model;

import com.zhekasmirnov.innercore.modpack.*;
import java.util.*;

public class LocalModPackSource extends ItemSource
{
    public LocalModPackSource() {
        final Iterator<ModPack> iterator = ModPackContext.getInstance().getStorage().getAllModPacks().iterator();
        while (iterator.hasNext()) {
            final ModPackItem modPackItem = new ModPackItem(iterator.next());
            if (modPackItem.getIcon() == null) {
                modPackItem.setIcon(LocalModPackSource.missingIcon);
            }
            this.addItem(modPackItem);
        }
    }
}
