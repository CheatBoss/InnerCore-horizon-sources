package com.zhekasmirnov.innercore.api.mod.recipes.workbench;

import org.json.*;
import com.zhekasmirnov.innercore.api.*;

public class WorkbenchShapelessRecipeLegacy extends WorkbenchRecipeLegacy
{
    public WorkbenchShapelessRecipeLegacy(final JSONObject jsonObject) {
        super(jsonObject);
        this.workbenchRecipe = new WorkbenchShapelessRecipe(this.id, this.count, this.data, null);
    }
}
