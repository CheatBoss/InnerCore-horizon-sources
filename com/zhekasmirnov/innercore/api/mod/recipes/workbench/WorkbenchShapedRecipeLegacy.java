package com.zhekasmirnov.innercore.api.mod.recipes.workbench;

import com.zhekasmirnov.innercore.api.*;
import org.json.*;
import java.lang.reflect.*;

public class WorkbenchShapedRecipeLegacy extends WorkbenchRecipeLegacy
{
    private RecipeEntry[][] pattern;
    
    public WorkbenchShapedRecipeLegacy(final JSONObject jsonObject) {
        super(jsonObject);
        this.workbenchRecipe = new WorkbenchShapedRecipe(this.id, this.count, this.data, null);
        final JSONArray optJSONArray = jsonObject.optJSONArray("pattern");
        int i = 0;
        if (optJSONArray != null) {
            final String[] pattern = new String[optJSONArray.length()];
            while (i < optJSONArray.length()) {
                pattern[i] = optJSONArray.optString(i);
                ++i;
            }
            this.setPattern(pattern);
        }
        else {
            this.setPattern(new String[0]);
        }
        ((WorkbenchShapedRecipe)this.workbenchRecipe).setPattern(this.pattern);
    }
    
    void setPattern(final String[] array) {
        final int length = array.length;
        final int length2 = array.length;
        int n = 0;
        int length3;
        for (int i = 0; i < length2; ++i, n = length3) {
            final String s = array[i];
            if (s.length() > (length3 = n)) {
                length3 = s.length();
            }
        }
        if (length == 0) {
            throw new IllegalArgumentException("invalid recipe pattern: empty array (height=0)");
        }
        if (n == 0) {
            throw new IllegalArgumentException("invalid recipe pattern: all lines are empty (width=0)");
        }
        this.pattern = (RecipeEntry[][])Array.newInstance(RecipeEntry.class, length, n);
        for (int j = 0; j < length; ++j) {
            final String s2 = array[j];
            for (int k = 0; k < n; ++k) {
                if (k >= s2.length()) {
                    this.pattern[j][k] = RecipeEntry.noentry;
                }
                else {
                    this.pattern[j][k] = this.getEntry(s2.charAt(k));
                }
            }
        }
    }
}
