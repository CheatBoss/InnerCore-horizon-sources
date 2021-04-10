package com.zhekasmirnov.innercore.api.mod.recipes.furnace;

import com.zhekasmirnov.apparatus.minecraft.addon.recipe.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import org.json.*;
import android.util.*;
import com.zhekasmirnov.innercore.api.mod.recipes.workbench.*;
import com.zhekasmirnov.innercore.api.commontypes.*;

public class FurnaceRecipe
{
    public final int inData;
    public final int inId;
    private final boolean isValid;
    private String prefix;
    public final int resData;
    public final int resId;
    
    public FurnaceRecipe(final int inId, final int inData, final int resId, final int resData) {
        this.inId = inId;
        this.inData = inData;
        this.resId = resId;
        this.resData = resData;
        this.isValid = true;
    }
    
    public FurnaceRecipe(final AddonRecipeParser addonRecipeParser, final AddonRecipeParser.ParsedRecipe parsedRecipe) {
        final JSONObject contents = parsedRecipe.getContents();
        final String optString = contents.optString("input");
        final Pair<Integer, Integer> idAndDataFromItemString = addonRecipeParser.getIdAndDataFromItemString(optString, -1);
        final String optString2 = contents.optString("output");
        final Pair<Integer, Integer> idAndDataFromItemString2 = addonRecipeParser.getIdAndDataFromItemString(optString2, 0);
        if (idAndDataFromItemString == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("cannot find vanilla numeric ID for ");
            sb.append(optString);
            Logger.debug(sb.toString());
            this.resData = 0;
            this.resId = 0;
            this.inData = 0;
            this.inId = 0;
            this.isValid = false;
            return;
        }
        if (idAndDataFromItemString2 == null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("cannot find vanilla numeric ID for ");
            sb2.append(optString2);
            Logger.debug(sb2.toString());
            this.resData = 0;
            this.resId = 0;
            this.inData = 0;
            this.inId = 0;
            this.isValid = false;
            return;
        }
        this.inId = (int)idAndDataFromItemString.first;
        this.inData = (int)idAndDataFromItemString.second;
        this.resId = (int)idAndDataFromItemString2.first;
        this.resData = (int)idAndDataFromItemString2.second;
        this.isValid = true;
    }
    
    public long getInputKey() {
        return RecipeEntry.getCodeByItem(this.inId, this.inData);
    }
    
    public String getPrefix() {
        return this.prefix;
    }
    
    public ItemInstance getResult() {
        return new ItemInstance(this.resId, 1, this.resData);
    }
    
    public boolean isMatchingPrefix(final String s) {
        if (s != null && !s.isEmpty() && !s.equals("undefined")) {
            return s.contains(this.prefix);
        }
        return this.prefix == null || this.prefix.isEmpty();
    }
    
    public boolean isValid() {
        return this.isValid;
    }
    
    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }
}
