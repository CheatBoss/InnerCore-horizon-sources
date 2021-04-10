package com.zhekasmirnov.apparatus.minecraft.addon.recipe;

import java.util.*;
import org.json.*;

public class AddonRecipeParser11 extends AddonRecipeParser
{
    private static final Map<String, String> legacyRecipeType;
    
    static {
        (legacyRecipeType = new HashMap<String, String>()).put("crafting_shaped", "minecraft:recipe_shaped");
        AddonRecipeParser11.legacyRecipeType.put("crafting_shapeless", "minecraft:recipe_shaped");
        AddonRecipeParser11.legacyRecipeType.put("furnace_recipe", "minecraft:recipe_shaped");
    }
    
    @Override
    public List<ParsedRecipe> parse(final JSONObject jsonObject) {
        final ArrayList<String> list = new ArrayList<String>();
        final JSONArray optJSONArray = jsonObject.optJSONArray("tags");
        if (optJSONArray != null) {
            for (int i = 0; i < optJSONArray.length(); ++i) {
                list.add(optJSONArray.optString(i));
            }
        }
        String optString;
        final String s = optString = jsonObject.optString("type");
        if (AddonRecipeParser11.legacyRecipeType.containsKey(s)) {
            optString = AddonRecipeParser11.legacyRecipeType.get(s);
        }
        final ArrayList<ParsedRecipe> list2 = new ArrayList<ParsedRecipe>();
        list2.add(new ParsedRecipe(null, optString, list, jsonObject));
        return list2;
    }
}
