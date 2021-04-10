package com.zhekasmirnov.innercore.api.mod.recipes.workbench;

import java.util.*;
import com.zhekasmirnov.innercore.api.log.*;
import org.json.*;

public abstract class WorkbenchRecipeLegacy
{
    protected int count;
    protected int data;
    protected HashMap<Character, RecipeEntry> entries;
    protected int id;
    protected WorkbenchRecipe workbenchRecipe;
    
    WorkbenchRecipeLegacy(final JSONObject jsonObject) {
        this.entries = new HashMap<Character, RecipeEntry>();
        final JSONArray optJSONArray = jsonObject.optJSONArray("result");
        if (optJSONArray != null) {
            this.id = optJSONArray.optInt(0);
            this.count = optJSONArray.optInt(1);
            this.data = optJSONArray.optInt(2);
        }
        final JSONObject optJSONObject = jsonObject.optJSONObject("components");
        if (optJSONObject != null) {
            final JSONArray names = optJSONObject.names();
            if (names != null) {
                for (int i = 0; i < names.length(); ++i) {
                    try {
                        final String optString = names.optString(i);
                        final JSONArray jsonArray = optJSONObject.getJSONArray(optString);
                        if (jsonArray != null) {
                            this.entries.put(optString.charAt(0), new RecipeEntry(jsonArray.optInt(0), jsonArray.optInt(1)));
                        }
                    }
                    catch (Exception ex) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("failed to parse json for recipe json=");
                        sb.append(jsonObject);
                        ICLog.e("RECIPES", sb.toString(), ex);
                    }
                }
            }
        }
    }
    
    RecipeEntry getEntry(final char c) {
        if (this.entries.containsKey(c)) {
            return this.entries.get(c);
        }
        return RecipeEntry.noentry;
    }
    
    public WorkbenchRecipe getRecipe() {
        this.workbenchRecipe.setEntries(this.entries);
        return this.workbenchRecipe;
    }
}
