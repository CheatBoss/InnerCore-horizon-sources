package com.zhekasmirnov.apparatus.adapter.minecraft.addon.recipe;

import java.util.*;
import org.json.*;

public class AddonRecipeParser16 extends AddonRecipeParser
{
    @Override
    public List<ParsedRecipe> parse(final JSONObject jsonObject) {
        final ArrayList<ParsedRecipe> list = new ArrayList<ParsedRecipe>();
        final Iterator keys = jsonObject.keys();
        while (keys.hasNext()) {
            final String s = keys.next();
            final JSONObject optJSONObject = jsonObject.optJSONObject(s);
            if (optJSONObject != null) {
                final ArrayList<String> list2 = new ArrayList<String>();
                final JSONArray optJSONArray = optJSONObject.optJSONArray("tags");
                if (optJSONArray != null) {
                    for (int i = 0; i < optJSONArray.length(); ++i) {
                        list2.add(optJSONArray.optString(i));
                    }
                }
                list.add(new ParsedRecipe(s, list2, optJSONObject));
            }
        }
        return list;
    }
}
