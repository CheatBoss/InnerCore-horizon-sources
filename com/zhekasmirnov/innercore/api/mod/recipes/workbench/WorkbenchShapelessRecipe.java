package com.zhekasmirnov.innercore.api.mod.recipes.workbench;

import com.zhekasmirnov.innercore.api.*;
import com.zhekasmirnov.apparatus.minecraft.addon.recipe.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import org.json.*;
import android.util.*;
import java.util.*;

public class WorkbenchShapelessRecipe extends WorkbenchRecipe
{
    public WorkbenchShapelessRecipe(final int n, final int n2, final int n3, final NativeItemInstanceExtra nativeItemInstanceExtra) {
        super(n, n2, n3, nativeItemInstanceExtra);
    }
    
    public WorkbenchShapelessRecipe(final AddonRecipeParser addonRecipeParser, final AddonRecipeParser.ParsedRecipe parsedRecipe) {
        super(addonRecipeParser, parsedRecipe);
        final JSONArray optJSONArray = parsedRecipe.getContents().optJSONArray("ingredients");
        if (optJSONArray != null) {
            for (int i = 0; i < optJSONArray.length(); ++i) {
                final JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                if (optJSONObject != null) {
                    final Pair<Integer, Integer> idAndDataForItemJson = addonRecipeParser.getIdAndDataForItemJson(optJSONObject, -1);
                    if (idAndDataForItemJson != null) {
                        this.entries.put(Integer.toString(i).charAt(0), new RecipeEntry((int)idAndDataForItemJson.first, (int)idAndDataForItemJson.second));
                    }
                    else {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("cannot find vanilla numeric ID for ");
                        sb.append(optJSONObject);
                        Logger.debug(sb.toString());
                        this.isValid = false;
                    }
                }
                else {
                    this.isValid = false;
                }
            }
        }
        else {
            this.isValid = false;
        }
    }
    
    @Override
    public void addVariants(final ArrayList<WorkbenchRecipe> list) {
        list.add(this);
    }
    
    @Override
    public String getRecipeMask() {
        final ArrayList<Comparable> list = new ArrayList<Comparable>();
        final Iterator<RecipeEntry> iterator = this.entries.values().iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next().getMask());
        }
        Collections.sort(list);
        String string = "$$";
        for (final Character c : list) {
            final StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append(c);
            string = sb.toString();
        }
        return string;
    }
    
    @Override
    public RecipeEntry[] getSortedEntries() {
        final Collection<RecipeEntry> values = this.entries.values();
        final RecipeEntry[] array = new RecipeEntry[values.size()];
        values.toArray(array);
        return array;
    }
    
    @Override
    public boolean isMatchingField(final WorkbenchField workbenchField) {
        final boolean[] array = new boolean[9];
        for (final RecipeEntry recipeEntry : this.getEntryCollection()) {
            final boolean b = false;
            int n = 0;
            boolean b2;
            while (true) {
                b2 = b;
                if (n >= 9) {
                    break;
                }
                if (!array[n] && recipeEntry.isMatching(workbenchField.getFieldSlot(n))) {
                    array[n] = true;
                    b2 = true;
                    break;
                }
                ++n;
            }
            if (!b2) {
                return false;
            }
        }
        for (int i = 0; i < 9; ++i) {
            if (!array[i] && workbenchField.getFieldSlot(i).getId() != 0) {
                return false;
            }
        }
        return true;
    }
}
