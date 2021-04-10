package com.zhekasmirnov.apparatus.adapter.minecraft.addon.recipe;

import org.json.*;
import android.util.*;
import com.zhekasmirnov.innercore.api.unlimited.*;
import java.util.*;

public abstract class AddonRecipeParser
{
    public Pair<Integer, Integer> getIdAndDataForItemJson(final JSONObject jsonObject, int optInt) {
        final String optString = jsonObject.optString("item");
        final int n = optInt;
        final boolean b = false;
        int intValue = n;
        int intValue2 = b ? 1 : 0;
        if (optString != null) {
            final Pair<Integer, Integer> idAndDataFromItemString = this.getIdAndDataFromItemString(optString, optInt);
            intValue = n;
            intValue2 = (b ? 1 : 0);
            if (idAndDataFromItemString != null) {
                intValue2 = (int)idAndDataFromItemString.first;
                intValue = (int)idAndDataFromItemString.second;
            }
        }
        if (intValue2 == 0) {
            return null;
        }
        optInt = jsonObject.optInt("data", -1);
        if (optInt != -1) {
            intValue = optInt;
        }
        return (Pair<Integer, Integer>)new Pair((Object)intValue2, (Object)intValue);
    }
    
    public Pair<Integer, Integer> getIdAndDataFromItemString(String s, int int1) {
        final String[] split = s.split(":");
        if (split.length == 1) {
            s = split[0];
        }
        else {
            s = split[1];
        }
        final int idByName = IDRegistry.getIDByName(s.toLowerCase());
        if (split.length == 3) {
            int1 = Integer.parseInt(split[2]);
        }
        if (idByName != 0) {
            return (Pair<Integer, Integer>)new Pair((Object)idByName, (Object)int1);
        }
        return null;
    }
    
    public abstract List<ParsedRecipe> parse(final JSONObject p0);
    
    public static class ParsedRecipe
    {
        private final JSONObject contents;
        private final List<String> tags;
        private final String type;
        
        public ParsedRecipe(final String type, final List<String> tags, final JSONObject contents) {
            this.type = type;
            this.tags = tags;
            this.contents = contents;
        }
        
        public JSONObject getContents() {
            return this.contents;
        }
        
        public List<String> getTags() {
            return this.tags;
        }
        
        public String getType() {
            return this.type;
        }
    }
}
