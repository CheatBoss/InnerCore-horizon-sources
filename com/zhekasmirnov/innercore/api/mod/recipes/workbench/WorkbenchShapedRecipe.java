package com.zhekasmirnov.innercore.api.mod.recipes.workbench;

import com.zhekasmirnov.innercore.api.*;
import com.zhekasmirnov.apparatus.minecraft.addon.recipe.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import com.zhekasmirnov.innercore.api.log.*;
import org.json.*;
import android.util.*;
import java.lang.reflect.*;
import java.util.*;

public class WorkbenchShapedRecipe extends WorkbenchRecipe
{
    private RecipeEntry[][] pattern;
    
    public WorkbenchShapedRecipe(final int n, final int n2, final int n3, final NativeItemInstanceExtra nativeItemInstanceExtra) {
        super(n, n2, n3, nativeItemInstanceExtra);
    }
    
    public WorkbenchShapedRecipe(final AddonRecipeParser addonRecipeParser, AddonRecipeParser.ParsedRecipe contents) {
        super(addonRecipeParser, contents);
        contents = (AddonRecipeParser.ParsedRecipe)contents.getContents();
        final JSONObject optJSONObject = ((JSONObject)contents).optJSONObject("key");
        final int n = 0;
        if (optJSONObject != null) {
            final JSONArray names = optJSONObject.names();
            if (names != null) {
                for (int i = 0; i < names.length(); ++i) {
                    while (true) {
                        while (true) {
                            Label_0312: {
                                try {
                                    final String optString = names.optString(i);
                                    final JSONObject jsonObject = optJSONObject.getJSONObject(optString);
                                    if (jsonObject != null) {
                                        final Pair<Integer, Integer> idAndDataForItemJson = addonRecipeParser.getIdAndDataForItemJson(jsonObject, -1);
                                        if (idAndDataForItemJson != null) {
                                            this.entries.put(optString.charAt(0), new RecipeEntry((int)idAndDataForItemJson.first, (int)idAndDataForItemJson.second));
                                            break Label_0312;
                                        }
                                        final StringBuilder sb = new StringBuilder();
                                        sb.append("cannot find vanilla numeric ID for ");
                                        sb.append(jsonObject);
                                        Logger.debug(sb.toString());
                                        this.isValid = false;
                                        break Label_0312;
                                    }
                                    else {
                                        this.isValid = false;
                                    }
                                }
                                catch (Exception ex) {
                                    final StringBuilder sb2 = new StringBuilder();
                                    sb2.append("failed to parse json for recipe json=");
                                    sb2.append(contents);
                                    ICLog.e("RECIPES", sb2.toString(), ex);
                                    this.isValid = false;
                                }
                                break;
                            }
                            continue;
                        }
                    }
                }
            }
            else {
                this.isValid = false;
            }
        }
        else {
            this.isValid = false;
        }
        final JSONArray optJSONArray = ((JSONObject)contents).optJSONArray("pattern");
        if (optJSONArray != null) {
            final String[] pattern = new String[optJSONArray.length()];
            for (int j = n; j < optJSONArray.length(); ++j) {
                pattern[j] = optJSONArray.optString(j);
            }
            this.setPattern(pattern);
            return;
        }
        this.setPattern(new String[0]);
        this.isValid = false;
    }
    
    private WorkbenchShapedRecipe generateVariantWithOffset(final int n, final int n2) {
        final RecipeEntry[][] pattern = (RecipeEntry[][])Array.newInstance(RecipeEntry.class, 3, 3);
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                final int n3 = i - n;
                final int n4 = j - n2;
                if (n3 >= 0 && n4 >= 0 && n4 < this.pattern.length && n3 < this.pattern[0].length) {
                    pattern[j][i] = this.pattern[n4][n3];
                }
                else {
                    pattern[j][i] = RecipeEntry.noentry;
                }
            }
        }
        final WorkbenchShapedRecipe workbenchShapedRecipe = new WorkbenchShapedRecipe(this.id, this.count, this.data, this.extra);
        workbenchShapedRecipe.setEntries(this.entries);
        workbenchShapedRecipe.setPattern(pattern);
        workbenchShapedRecipe.setPrefix(this.getPrefix());
        workbenchShapedRecipe.setCallback(this.getCallback());
        return workbenchShapedRecipe;
    }
    
    @Override
    public void addVariants(final ArrayList<WorkbenchRecipe> list) {
        if (this.pattern.length == 3 && this.pattern[0].length == 3) {
            list.add(this);
            return;
        }
        for (int i = 0; i < 4 - this.pattern.length; ++i) {
            for (int j = 0; j < 4 - this.pattern[0].length; ++j) {
                list.add(this.generateVariantWithOffset(j, i));
            }
        }
    }
    
    @Override
    public String getRecipeMask() {
        final RecipeEntry[][] pattern = this.pattern;
        final int length = pattern.length;
        String string = "";
        for (int i = 0; i < length; ++i) {
            final RecipeEntry[] array = pattern[i];
            for (int length2 = array.length, j = 0; j < length2; ++j) {
                final RecipeEntry recipeEntry = array[j];
                final StringBuilder sb = new StringBuilder();
                sb.append(string);
                sb.append(recipeEntry.getMask());
                string = sb.toString();
            }
        }
        return string;
    }
    
    @Override
    public RecipeEntry[] getSortedEntries() {
        final RecipeEntry[] array = new RecipeEntry[9];
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (j < this.pattern.length && i < this.pattern[0].length) {
                    array[j * 3 + i] = this.pattern[j][i];
                }
                else {
                    array[j * 3 + i] = RecipeEntry.noentry;
                }
            }
        }
        return array;
    }
    
    @Override
    public boolean isMatchingField(final WorkbenchField workbenchField) {
        for (int i = 0; i < this.pattern.length; ++i) {
            for (int j = 0; j < this.pattern[i].length; ++j) {
                if (!this.pattern[i][j].isMatching(workbenchField.getFieldSlot(i * 3 + j))) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public void setPattern(final String[] array) {
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
    
    public void setPattern(final RecipeEntry[][] pattern) {
        this.pattern = pattern;
    }
}
