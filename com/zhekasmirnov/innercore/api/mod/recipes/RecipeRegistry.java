package com.zhekasmirnov.innercore.api.mod.recipes;

import org.mozilla.javascript.annotations.*;
import com.zhekasmirnov.innercore.api.mod.*;
import com.zhekasmirnov.innercore.api.*;
import com.zhekasmirnov.innercore.api.commontypes.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.log.*;
import com.zhekasmirnov.innercore.api.mod.recipes.furnace.*;
import java.util.*;
import com.zhekasmirnov.innercore.api.unlimited.*;
import com.zhekasmirnov.innercore.api.mod.recipes.workbench.*;
import com.zhekasmirnov.innercore.api.mod.ui.container.*;

public class RecipeRegistry
{
    static final String LOGGER_TAG = "INNERCORE-RECIPES";
    
    @JSStaticFunction
    public static void addFurnace(final Object o, final Object o2, final Object o3, final Object o4, final Object o5) {
        int n;
        int convertToInt;
        int n2;
        int n3;
        String s;
        if (o4 instanceof Number) {
            n = convertToInt(o);
            convertToInt = convertToInt(o2);
            n2 = convertToInt(o3);
            n3 = convertToInt(o4);
            s = convertToString(o5);
        }
        else {
            if (!(o4 instanceof String) && !(o4 instanceof Undefined) && o4 != null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("illegal parameters of Recipes.addFurnace: ");
                sb.append(o);
                sb.append(" ");
                sb.append(o2);
                sb.append(" ");
                sb.append(o3);
                sb.append(" ");
                sb.append(o4);
                sb.append(" ");
                sb.append(o5);
                throw new IllegalArgumentException(sb.toString());
            }
            n = convertToInt(o);
            convertToInt = -1;
            n2 = convertToInt(o2);
            n3 = convertToInt(o3);
            s = convertToString(o4);
        }
        if (n != 0 && n2 != 0) {
            FurnaceRecipeRegistry.addFurnaceRecipe(n, convertToInt, n2, n3, s);
            return;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("illegal parameters of Recipes.addFurnace: ");
        sb2.append(o);
        sb2.append(" ");
        sb2.append(o2);
        sb2.append(" ");
        sb2.append(o3);
        sb2.append(" ");
        sb2.append(o4);
        sb2.append(" ");
        sb2.append(o5);
        throw new IllegalArgumentException(sb2.toString());
    }
    
    @JSStaticFunction
    public static void addFurnaceFuel(final int n, final int n2, final int n3) {
        FurnaceRecipeRegistry.addFuel(n, n2, n3);
    }
    
    @JSStaticFunction
    public static void addShaped(final ScriptableObject scriptableObject, final NativeArray nativeArray, final NativeArray nativeArray2, final Function callback, final String prefix) {
        int i = 0;
        final int intProperty = ScriptableObjectHelper.getIntProperty(scriptableObject, "id", 0);
        final int intProperty2 = ScriptableObjectHelper.getIntProperty(scriptableObject, "count", 1);
        final int intProperty3 = ScriptableObjectHelper.getIntProperty(scriptableObject, "data", 0);
        final NativeItemInstanceExtra unwrapObject = NativeItemInstanceExtra.unwrapObject(ScriptableObjectHelper.getProperty(scriptableObject, "extra", 0));
        if (intProperty == 0) {
            throw new IllegalArgumentException("recipe id is 0");
        }
        final WorkbenchShapedRecipe workbenchShapedRecipe = new WorkbenchShapedRecipe(intProperty, intProperty2, intProperty3, unwrapObject);
        workbenchShapedRecipe.setCallback(callback);
        workbenchShapedRecipe.setPrefix(prefix);
        final HashMap<Character, RecipeEntry> entries = extractEntries(nativeArray2, workbenchShapedRecipe);
        if (entries == null) {
            return;
        }
        workbenchShapedRecipe.setEntries(entries);
        final Object[] array = nativeArray.toArray();
        String[] pattern;
        for (pattern = new String[(int)nativeArray.getLength()]; i < pattern.length; ++i) {
            final StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(array[i]);
            pattern[i] = sb.toString();
        }
        workbenchShapedRecipe.setPattern(pattern);
        WorkbenchRecipeRegistry.addRecipe(workbenchShapedRecipe);
    }
    
    @JSStaticFunction
    public static void addShaped2(final int n, final int n2, final int n3, final NativeArray nativeArray, final NativeArray nativeArray2, final Function function, final String s) {
        addShaped(new ItemInstance(n, n2, n3), nativeArray, nativeArray2, function, s);
    }
    
    @JSStaticFunction
    public static void addShapeless(final ScriptableObject scriptableObject, final NativeArray nativeArray, final Function callback, final String prefix) {
        final int intProperty = ScriptableObjectHelper.getIntProperty(scriptableObject, "id", 0);
        final int intProperty2 = ScriptableObjectHelper.getIntProperty(scriptableObject, "count", 1);
        final int intProperty3 = ScriptableObjectHelper.getIntProperty(scriptableObject, "data", 0);
        final NativeItemInstanceExtra unwrapObject = NativeItemInstanceExtra.unwrapObject(ScriptableObjectHelper.getProperty(scriptableObject, "extra", 0));
        if (intProperty == 0) {
            throw new IllegalArgumentException("recipe id is 0");
        }
        final WorkbenchShapelessRecipe workbenchShapelessRecipe = new WorkbenchShapelessRecipe(intProperty, intProperty2, intProperty3, unwrapObject);
        workbenchShapelessRecipe.setCallback(callback);
        workbenchShapelessRecipe.setPrefix(prefix);
        final HashMap<Character, RecipeEntry> shapelessEntries = extractShapelessEntries(nativeArray, workbenchShapelessRecipe);
        if (shapelessEntries == null) {
            return;
        }
        workbenchShapelessRecipe.setEntries(shapelessEntries);
        WorkbenchRecipeRegistry.addRecipe(workbenchShapelessRecipe);
    }
    
    @JSStaticFunction
    public static void addShapeless2(final int n, final int n2, final int n3, final NativeArray nativeArray, final Function function, final String s) {
        addShapeless(new ItemInstance(n, n2, n3), nativeArray, function, s);
    }
    
    private static int convertToInt(final Object o) {
        try {
            return (int)Context.jsToJava(o, (Class)Integer.TYPE);
        }
        catch (Exception ex) {
            return 0;
        }
    }
    
    private static String convertToString(final Object o) {
        try {
            return (String)Context.jsToJava(o, (Class)String.class);
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    @JSStaticFunction
    public static void deleteRecipe(final ScriptableObject scriptableObject) {
        final int intProperty = ScriptableObjectHelper.getIntProperty(scriptableObject, "id", 0);
        final int intProperty2 = ScriptableObjectHelper.getIntProperty(scriptableObject, "count", 1);
        final int intProperty3 = ScriptableObjectHelper.getIntProperty(scriptableObject, "data", 0);
        if (intProperty == 0) {
            throw new IllegalArgumentException("recipe id is 0");
        }
        removeWorkbenchRecipe(intProperty, intProperty2, intProperty3);
    }
    
    private static HashMap<Character, RecipeEntry> extractEntries(final NativeArray nativeArray, final WorkbenchRecipe workbenchRecipe) {
        final StringBuilder sb = new StringBuilder();
        sb.append("In shaped recipe for ");
        sb.append(tryGetReadableStringFromResult(workbenchRecipe.getResult()));
        sb.append(": ");
        final String string = sb.toString();
        final HashMap<Character, RecipeEntry> hashMap = new HashMap<Character, RecipeEntry>();
        final Object[] array = nativeArray.toArray();
        if (array.length % 3 != 0) {
            reportComponentArrayError(array, string);
        }
        for (int i = 0; i < array.length / 3; ++i) {
            final Object o = array[i * 3];
            final Object o2 = array[i * 3 + 1];
            final Object o3 = array[i * 3 + 2];
            Label_0135: {
                Character value;
                if ((value = (Character)o) == null) {
                    break Label_0135;
                }
                value = (Character)o;
                try {
                    if (o instanceof CharSequence) {
                        value = ((CharSequence)o).charAt(0);
                    }
                    if (value instanceof Character) {
                        if (o2 == null || !(o2 instanceof Number)) {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append(string);
                            sb2.append("recipe entry id is invalid: ");
                            sb2.append(o2);
                            ICLog.i("ERROR", sb2.toString());
                            return null;
                        }
                        if (o3 == null || !(o3 instanceof Number)) {
                            final StringBuilder sb3 = new StringBuilder();
                            sb3.append(string);
                            sb3.append("recipe entry data is invalid: ");
                            sb3.append(o3);
                            ICLog.i("ERROR", sb3.toString());
                            return null;
                        }
                        if (((Number)o2).intValue() == 0) {
                            final StringBuilder sb4 = new StringBuilder();
                            sb4.append(string);
                            sb4.append("recipe entry id is invalid: ");
                            sb4.append(o2);
                            ICLog.i("ERROR", sb4.toString());
                            return null;
                        }
                        hashMap.put(value, new RecipeEntry(((Number)o2).intValue(), ((Number)o3).intValue()));
                    }
                    else {
                        final StringBuilder sb5 = new StringBuilder();
                        sb5.append(string);
                        sb5.append("recipe entry character is invalid: ");
                        sb5.append(value);
                        ICLog.i("ERROR", sb5.toString());
                    }
                }
                catch (Exception ex) {
                    final StringBuilder sb6 = new StringBuilder();
                    sb6.append(string);
                    sb6.append("recipe format error: ");
                    sb6.append(ex);
                    ICLog.i("ERROR", sb6.toString());
                    reportComponentArrayError(array, string);
                }
            }
        }
        return hashMap;
    }
    
    private static HashMap<Character, RecipeEntry> extractShapelessEntries(NativeArray ex, WorkbenchRecipe string) {
        final StringBuilder sb = new StringBuilder();
        sb.append("In shapeless recipe for ");
        sb.append(tryGetReadableStringFromResult(string.getResult()));
        sb.append(": ");
        string = (WorkbenchRecipe)sb.toString();
        final HashMap<Character, RecipeEntry> hashMap = new HashMap<Character, RecipeEntry>();
        final Object[] array = ((NativeArray)ex).toArray();
        final int length = array.length;
        int n = 0;
        for (int i = 0; i < length; ++i) {
            ex = (Exception)array[i];
            try {
                ex = ex;
                final int intProperty = ScriptableObjectHelper.getIntProperty((ScriptableObject)ex, "id", 0);
                final int intProperty2 = ScriptableObjectHelper.getIntProperty((ScriptableObject)ex, "data", 0);
                if (intProperty == 0) {
                    ex = (Exception)new StringBuilder();
                    ((StringBuilder)ex).append((String)string);
                    ((StringBuilder)ex).append("recipe entry id is invalid: ");
                    ((StringBuilder)ex).append(intProperty);
                    ICLog.i("ERROR", ((StringBuilder)ex).toString());
                    return null;
                }
                final int n2 = n + 1;
                final char c = (char)n;
                try {
                    hashMap.put(c, new RecipeEntry(intProperty, intProperty2));
                    n = n2;
                }
                catch (Exception ex) {
                    n = n2;
                }
            }
            catch (Exception ex2) {}
            final StringBuilder sb2 = new StringBuilder();
            sb2.append((String)string);
            sb2.append("recipe format error: ");
            sb2.append(ex);
            ICLog.i("ERROR", sb2.toString());
            reportComponentArrayError(array, (String)string);
        }
        return hashMap;
    }
    
    @JSStaticFunction
    public static int getFuelBurnDuration(final int n, final int n2) {
        return FurnaceRecipeRegistry.getBurnDuration(n, n2);
    }
    
    @JSStaticFunction
    public static ScriptableObject getFurnaceRecipeResult(final int n, final int n2, final String s) {
        final FurnaceRecipe recipe = FurnaceRecipeRegistry.getRecipe(n, n2, s);
        if (recipe != null) {
            return recipe.getResult();
        }
        return null;
    }
    
    @JSStaticFunction
    public static Collection<FurnaceRecipe> getFurnaceRecipesByResult(final int n, final int n2, final String s) {
        return FurnaceRecipeRegistry.getFurnaceRecipeByResult(n, n2, s);
    }
    
    @JSStaticFunction
    public static WorkbenchRecipe getRecipeByField(final Object o, final String s) {
        return WorkbenchRecipeRegistry.getRecipeFromField((WorkbenchField)Context.jsToJava(o, (Class)WorkbenchField.class), s);
    }
    
    @JSStaticFunction
    public static WorkbenchRecipe getRecipeByUid(final Object o) {
        return WorkbenchRecipeRegistry.getRecipeByUid((long)Context.jsToJava(o, (Class)Long.class));
    }
    
    @JSStaticFunction
    public static ItemInstance getRecipeResult(final Object o, final String s) {
        return WorkbenchRecipeRegistry.getRecipeResult((WorkbenchField)Context.jsToJava(o, (Class)WorkbenchField.class), s);
    }
    
    @JSStaticFunction
    public static Collection<WorkbenchRecipe> getWorkbenchRecipesByIngredient(final int n, final int n2) {
        return WorkbenchRecipeRegistry.getRecipesByIngredient(n, n2);
    }
    
    @JSStaticFunction
    public static Collection<WorkbenchRecipe> getWorkbenchRecipesByResult(final int n, final int n2, final int n3) {
        return WorkbenchRecipeRegistry.getRecipesByResult(n, n2, n3);
    }
    
    @JSStaticFunction
    public static ItemInstance provideRecipe(final Object o, final String s) {
        return WorkbenchRecipeRegistry.provideRecipe((WorkbenchField)Context.jsToJava(o, (Class)WorkbenchField.class), s);
    }
    
    @JSStaticFunction
    public static ItemInstance provideRecipeForPlayer(final Object o, final String s, final Object o2) {
        return WorkbenchRecipeRegistry.provideRecipeForPlayer((WorkbenchField)Context.jsToJava(o, (Class)WorkbenchField.class), s, (long)Context.jsToJava(o2, (Class)Long.class));
    }
    
    @JSStaticFunction
    public static void removeFurnaceFuel(final int n, final int n2) {
        FurnaceRecipeRegistry.removeFuel(n, n2);
    }
    
    @JSStaticFunction
    public static void removeFurnaceRecipe(final int n, final int n2) {
        FurnaceRecipeRegistry.removeFurnaceRecipe(n, n2);
    }
    
    @JSStaticFunction
    public static void removeWorkbenchRecipe(final int n, final int n2, final int n3) {
        WorkbenchRecipeRegistry.removeRecipeByResult(n, n2, n3);
    }
    
    private static void reportComponentArrayError(final Object[] array, final String s) {
        String s2 = "[";
        for (int length = array.length, i = 0; i < length; ++i) {
            final Object o = array[i];
            String string = s2;
            if (!s2.endsWith("[")) {
                final StringBuilder sb = new StringBuilder();
                sb.append(s2);
                sb.append(", ");
                string = sb.toString();
            }
            String string2 = string;
            if (o instanceof CharSequence) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(string);
                sb2.append("'");
                string2 = sb2.toString();
            }
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(string2);
            sb3.append(o);
            final String s3 = s2 = sb3.toString();
            if (o instanceof CharSequence) {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append(s3);
                sb4.append("'");
                s2 = sb4.toString();
            }
        }
        final StringBuilder sb5 = new StringBuilder();
        sb5.append(s);
        sb5.append("recipe component array must be formatted like this: 'letter', id, data, ... repeat, found: ");
        sb5.append(s2);
        sb5.append("]");
        throw new IllegalArgumentException(sb5.toString());
    }
    
    private static String tryGetReadableStringFromResult(final ItemInstance itemInstance) {
        final int id = itemInstance.getId();
        String s = IDRegistry.getNameByID(id);
        if (s == null) {
            s = Integer.toString(id);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(itemInstance.getCount());
        sb.append("x ");
        sb.append(s);
        sb.append("#");
        sb.append(itemInstance.getData());
        String s2;
        if (itemInstance.getExtraValue() != 0L) {
            s2 = "[+extra]";
        }
        else {
            s2 = "";
        }
        sb.append(s2);
        return sb.toString();
    }
    
    public static class WorkbenchUIHandler extends com.zhekasmirnov.innercore.api.mod.recipes.workbench.WorkbenchUIHandler
    {
        public WorkbenchUIHandler(final ScriptableObject scriptableObject, final Container container, final WorkbenchField workbenchField) {
            super(scriptableObject, container, workbenchField);
        }
        
        @Deprecated
        @JSStaticFunction
        public static void __placeholder() {
        }
    }
}
