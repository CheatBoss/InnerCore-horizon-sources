package org.mineprogramming.horizon.innercore.model;

import com.zhekasmirnov.innercore.modpack.*;
import java.io.*;
import org.json.*;
import java.util.*;

public class ArchiveModPackSource extends ItemSource
{
    public ArchiveModPackSource() {
        for (final File file : ModPackContext.getInstance().getStorage().getAllArchivedPacks()) {
            try {
                this.addItem(new ModPackItem(file));
            }
            catch (IOException | JSONException ex) {
                final Throwable t;
                t.printStackTrace();
            }
        }
    }
}
