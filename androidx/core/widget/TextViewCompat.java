package androidx.core.widget;

import android.widget.*;
import android.os.*;
import android.content.res.*;
import androidx.core.util.*;
import android.graphics.drawable.*;
import android.text.method.*;
import android.icu.text.*;
import androidx.core.text.*;
import android.util.*;
import android.graphics.*;
import androidx.annotation.*;
import java.lang.annotation.*;
import android.content.*;
import android.content.pm.*;
import android.app.*;
import java.util.*;
import android.text.*;
import java.lang.reflect.*;
import android.view.*;

public final class TextViewCompat
{
    public static final int AUTO_SIZE_TEXT_TYPE_NONE = 0;
    public static final int AUTO_SIZE_TEXT_TYPE_UNIFORM = 1;
    private static final int LINES = 1;
    private static final String LOG_TAG = "TextViewCompat";
    private static Field sMaxModeField;
    private static boolean sMaxModeFieldFetched;
    private static Field sMaximumField;
    private static boolean sMaximumFieldFetched;
    private static Field sMinModeField;
    private static boolean sMinModeFieldFetched;
    private static Field sMinimumField;
    private static boolean sMinimumFieldFetched;
    
    private TextViewCompat() {
    }
    
    public static int getAutoSizeMaxTextSize(@NonNull final TextView textView) {
        if (Build$VERSION.SDK_INT >= 27) {
            return textView.getAutoSizeMaxTextSize();
        }
        if (textView instanceof AutoSizeableTextView) {
            return ((AutoSizeableTextView)textView).getAutoSizeMaxTextSize();
        }
        return -1;
    }
    
    public static int getAutoSizeMinTextSize(@NonNull final TextView textView) {
        if (Build$VERSION.SDK_INT >= 27) {
            return textView.getAutoSizeMinTextSize();
        }
        if (textView instanceof AutoSizeableTextView) {
            return ((AutoSizeableTextView)textView).getAutoSizeMinTextSize();
        }
        return -1;
    }
    
    public static int getAutoSizeStepGranularity(@NonNull final TextView textView) {
        if (Build$VERSION.SDK_INT >= 27) {
            return textView.getAutoSizeStepGranularity();
        }
        if (textView instanceof AutoSizeableTextView) {
            return ((AutoSizeableTextView)textView).getAutoSizeStepGranularity();
        }
        return -1;
    }
    
    @NonNull
    public static int[] getAutoSizeTextAvailableSizes(@NonNull final TextView textView) {
        if (Build$VERSION.SDK_INT >= 27) {
            return textView.getAutoSizeTextAvailableSizes();
        }
        if (textView instanceof AutoSizeableTextView) {
            return ((AutoSizeableTextView)textView).getAutoSizeTextAvailableSizes();
        }
        return new int[0];
    }
    
    public static int getAutoSizeTextType(@NonNull final TextView textView) {
        if (Build$VERSION.SDK_INT >= 27) {
            return textView.getAutoSizeTextType();
        }
        if (textView instanceof AutoSizeableTextView) {
            return ((AutoSizeableTextView)textView).getAutoSizeTextType();
        }
        return 0;
    }
    
    @Nullable
    public static ColorStateList getCompoundDrawableTintList(@NonNull final TextView textView) {
        Preconditions.checkNotNull(textView);
        if (Build$VERSION.SDK_INT >= 23) {
            return textView.getCompoundDrawableTintList();
        }
        if (textView instanceof TintableCompoundDrawablesView) {
            return ((TintableCompoundDrawablesView)textView).getSupportCompoundDrawablesTintList();
        }
        return null;
    }
    
    @Nullable
    public static PorterDuff$Mode getCompoundDrawableTintMode(@NonNull final TextView textView) {
        Preconditions.checkNotNull(textView);
        if (Build$VERSION.SDK_INT >= 23) {
            return textView.getCompoundDrawableTintMode();
        }
        if (textView instanceof TintableCompoundDrawablesView) {
            return ((TintableCompoundDrawablesView)textView).getSupportCompoundDrawablesTintMode();
        }
        return null;
    }
    
    @NonNull
    public static Drawable[] getCompoundDrawablesRelative(@NonNull final TextView textView) {
        if (Build$VERSION.SDK_INT >= 18) {
            return textView.getCompoundDrawablesRelative();
        }
        if (Build$VERSION.SDK_INT >= 17) {
            final int layoutDirection = textView.getLayoutDirection();
            boolean b = true;
            if (layoutDirection != 1) {
                b = false;
            }
            final Drawable[] compoundDrawables = textView.getCompoundDrawables();
            if (b) {
                final Drawable drawable = compoundDrawables[2];
                final Drawable drawable2 = compoundDrawables[0];
                compoundDrawables[0] = drawable;
                compoundDrawables[2] = drawable2;
            }
            return compoundDrawables;
        }
        return textView.getCompoundDrawables();
    }
    
    public static int getFirstBaselineToTopHeight(@NonNull final TextView textView) {
        return textView.getPaddingTop() - textView.getPaint().getFontMetricsInt().top;
    }
    
    public static int getLastBaselineToBottomHeight(@NonNull final TextView textView) {
        return textView.getPaddingBottom() + textView.getPaint().getFontMetricsInt().bottom;
    }
    
    public static int getMaxLines(@NonNull final TextView textView) {
        if (Build$VERSION.SDK_INT >= 16) {
            return textView.getMaxLines();
        }
        if (!TextViewCompat.sMaxModeFieldFetched) {
            TextViewCompat.sMaxModeField = retrieveField("mMaxMode");
            TextViewCompat.sMaxModeFieldFetched = true;
        }
        if (TextViewCompat.sMaxModeField != null && retrieveIntFromField(TextViewCompat.sMaxModeField, textView) == 1) {
            if (!TextViewCompat.sMaximumFieldFetched) {
                TextViewCompat.sMaximumField = retrieveField("mMaximum");
                TextViewCompat.sMaximumFieldFetched = true;
            }
            if (TextViewCompat.sMaximumField != null) {
                return retrieveIntFromField(TextViewCompat.sMaximumField, textView);
            }
        }
        return -1;
    }
    
    public static int getMinLines(@NonNull final TextView textView) {
        if (Build$VERSION.SDK_INT >= 16) {
            return textView.getMinLines();
        }
        if (!TextViewCompat.sMinModeFieldFetched) {
            TextViewCompat.sMinModeField = retrieveField("mMinMode");
            TextViewCompat.sMinModeFieldFetched = true;
        }
        if (TextViewCompat.sMinModeField != null && retrieveIntFromField(TextViewCompat.sMinModeField, textView) == 1) {
            if (!TextViewCompat.sMinimumFieldFetched) {
                TextViewCompat.sMinimumField = retrieveField("mMinimum");
                TextViewCompat.sMinimumFieldFetched = true;
            }
            if (TextViewCompat.sMinimumField != null) {
                return retrieveIntFromField(TextViewCompat.sMinimumField, textView);
            }
        }
        return -1;
    }
    
    @RequiresApi(18)
    private static int getTextDirection(@NonNull final TextDirectionHeuristic textDirectionHeuristic) {
        if (textDirectionHeuristic == TextDirectionHeuristics.FIRSTSTRONG_RTL) {
            return 1;
        }
        if (textDirectionHeuristic == TextDirectionHeuristics.FIRSTSTRONG_LTR) {
            return 1;
        }
        if (textDirectionHeuristic == TextDirectionHeuristics.ANYRTL_LTR) {
            return 2;
        }
        if (textDirectionHeuristic == TextDirectionHeuristics.LTR) {
            return 3;
        }
        if (textDirectionHeuristic == TextDirectionHeuristics.RTL) {
            return 4;
        }
        if (textDirectionHeuristic == TextDirectionHeuristics.LOCALE) {
            return 5;
        }
        if (textDirectionHeuristic == TextDirectionHeuristics.FIRSTSTRONG_LTR) {
            return 6;
        }
        if (textDirectionHeuristic == TextDirectionHeuristics.FIRSTSTRONG_RTL) {
            return 7;
        }
        return 1;
    }
    
    @RequiresApi(18)
    private static TextDirectionHeuristic getTextDirectionHeuristic(@NonNull final TextView textView) {
        if (textView.getTransformationMethod() instanceof PasswordTransformationMethod) {
            return TextDirectionHeuristics.LTR;
        }
        final int sdk_INT = Build$VERSION.SDK_INT;
        boolean b = true;
        if (sdk_INT >= 28 && (textView.getInputType() & 0xF) == 0x3) {
            final byte directionality = Character.getDirectionality(DecimalFormatSymbols.getInstance(textView.getTextLocale()).getDigitStrings()[0].codePointAt(0));
            if (directionality != 1 && directionality != 2) {
                return TextDirectionHeuristics.LTR;
            }
            return TextDirectionHeuristics.RTL;
        }
        else {
            if (textView.getLayoutDirection() != 1) {
                b = false;
            }
            switch (textView.getTextDirection()) {
                default: {
                    if (b) {
                        return TextDirectionHeuristics.FIRSTSTRONG_RTL;
                    }
                    return TextDirectionHeuristics.FIRSTSTRONG_LTR;
                }
                case 7: {
                    return TextDirectionHeuristics.FIRSTSTRONG_RTL;
                }
                case 6: {
                    return TextDirectionHeuristics.FIRSTSTRONG_LTR;
                }
                case 5: {
                    return TextDirectionHeuristics.LOCALE;
                }
                case 4: {
                    return TextDirectionHeuristics.RTL;
                }
                case 3: {
                    return TextDirectionHeuristics.LTR;
                }
                case 2: {
                    return TextDirectionHeuristics.ANYRTL_LTR;
                }
            }
        }
    }
    
    @NonNull
    public static PrecomputedTextCompat.Params getTextMetricsParams(@NonNull final TextView textView) {
        if (Build$VERSION.SDK_INT >= 28) {
            return new PrecomputedTextCompat.Params(textView.getTextMetricsParams());
        }
        final PrecomputedTextCompat.Params.Builder builder = new PrecomputedTextCompat.Params.Builder(new TextPaint((Paint)textView.getPaint()));
        if (Build$VERSION.SDK_INT >= 23) {
            builder.setBreakStrategy(textView.getBreakStrategy());
            builder.setHyphenationFrequency(textView.getHyphenationFrequency());
        }
        if (Build$VERSION.SDK_INT >= 18) {
            builder.setTextDirection(getTextDirectionHeuristic(textView));
        }
        return builder.build();
    }
    
    private static Field retrieveField(final String s) {
        Field declaredField = null;
        try {
            final Field field = declaredField = TextView.class.getDeclaredField(s);
            field.setAccessible(true);
            return field;
        }
        catch (NoSuchFieldException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Could not retrieve ");
            sb.append(s);
            sb.append(" field.");
            Log.e("TextViewCompat", sb.toString());
            return declaredField;
        }
    }
    
    private static int retrieveIntFromField(final Field field, final TextView textView) {
        try {
            return field.getInt(textView);
        }
        catch (IllegalAccessException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Could not retrieve value of ");
            sb.append(field.getName());
            sb.append(" field.");
            Log.d("TextViewCompat", sb.toString());
            return -1;
        }
    }
    
    public static void setAutoSizeTextTypeUniformWithConfiguration(@NonNull final TextView textView, final int n, final int n2, final int n3, final int n4) throws IllegalArgumentException {
        if (Build$VERSION.SDK_INT >= 27) {
            textView.setAutoSizeTextTypeUniformWithConfiguration(n, n2, n3, n4);
            return;
        }
        if (textView instanceof AutoSizeableTextView) {
            ((AutoSizeableTextView)textView).setAutoSizeTextTypeUniformWithConfiguration(n, n2, n3, n4);
        }
    }
    
    public static void setAutoSizeTextTypeUniformWithPresetSizes(@NonNull final TextView textView, @NonNull final int[] array, final int n) throws IllegalArgumentException {
        if (Build$VERSION.SDK_INT >= 27) {
            textView.setAutoSizeTextTypeUniformWithPresetSizes(array, n);
            return;
        }
        if (textView instanceof AutoSizeableTextView) {
            ((AutoSizeableTextView)textView).setAutoSizeTextTypeUniformWithPresetSizes(array, n);
        }
    }
    
    public static void setAutoSizeTextTypeWithDefaults(@NonNull final TextView textView, final int n) {
        if (Build$VERSION.SDK_INT >= 27) {
            textView.setAutoSizeTextTypeWithDefaults(n);
            return;
        }
        if (textView instanceof AutoSizeableTextView) {
            ((AutoSizeableTextView)textView).setAutoSizeTextTypeWithDefaults(n);
        }
    }
    
    public static void setCompoundDrawableTintList(@NonNull final TextView textView, @Nullable final ColorStateList list) {
        Preconditions.checkNotNull(textView);
        if (Build$VERSION.SDK_INT >= 23) {
            textView.setCompoundDrawableTintList(list);
            return;
        }
        if (textView instanceof TintableCompoundDrawablesView) {
            ((TintableCompoundDrawablesView)textView).setSupportCompoundDrawablesTintList(list);
        }
    }
    
    public static void setCompoundDrawableTintMode(@NonNull final TextView textView, @Nullable final PorterDuff$Mode porterDuff$Mode) {
        Preconditions.checkNotNull(textView);
        if (Build$VERSION.SDK_INT >= 23) {
            textView.setCompoundDrawableTintMode(porterDuff$Mode);
            return;
        }
        if (textView instanceof TintableCompoundDrawablesView) {
            ((TintableCompoundDrawablesView)textView).setSupportCompoundDrawablesTintMode(porterDuff$Mode);
        }
    }
    
    public static void setCompoundDrawablesRelative(@NonNull final TextView textView, @Nullable Drawable drawable, @Nullable final Drawable drawable2, @Nullable final Drawable drawable3, @Nullable final Drawable drawable4) {
        if (Build$VERSION.SDK_INT >= 18) {
            textView.setCompoundDrawablesRelative(drawable, drawable2, drawable3, drawable4);
            return;
        }
        if (Build$VERSION.SDK_INT >= 17) {
            final int layoutDirection = textView.getLayoutDirection();
            boolean b = true;
            if (layoutDirection != 1) {
                b = false;
            }
            Drawable drawable5;
            if (b) {
                drawable5 = drawable3;
            }
            else {
                drawable5 = drawable;
            }
            if (!b) {
                drawable = drawable3;
            }
            textView.setCompoundDrawables(drawable5, drawable2, drawable, drawable4);
            return;
        }
        textView.setCompoundDrawables(drawable, drawable2, drawable3, drawable4);
    }
    
    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull final TextView textView, @DrawableRes int n, @DrawableRes final int n2, @DrawableRes final int n3, @DrawableRes final int n4) {
        if (Build$VERSION.SDK_INT >= 18) {
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(n, n2, n3, n4);
            return;
        }
        if (Build$VERSION.SDK_INT >= 17) {
            final int layoutDirection = textView.getLayoutDirection();
            boolean b = true;
            if (layoutDirection != 1) {
                b = false;
            }
            int n5;
            if (b) {
                n5 = n3;
            }
            else {
                n5 = n;
            }
            if (!b) {
                n = n3;
            }
            textView.setCompoundDrawablesWithIntrinsicBounds(n5, n2, n, n4);
            return;
        }
        textView.setCompoundDrawablesWithIntrinsicBounds(n, n2, n3, n4);
    }
    
    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull final TextView textView, @Nullable Drawable drawable, @Nullable final Drawable drawable2, @Nullable final Drawable drawable3, @Nullable final Drawable drawable4) {
        if (Build$VERSION.SDK_INT >= 18) {
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
            return;
        }
        if (Build$VERSION.SDK_INT >= 17) {
            final int layoutDirection = textView.getLayoutDirection();
            boolean b = true;
            if (layoutDirection != 1) {
                b = false;
            }
            Drawable drawable5;
            if (b) {
                drawable5 = drawable3;
            }
            else {
                drawable5 = drawable;
            }
            if (!b) {
                drawable = drawable3;
            }
            textView.setCompoundDrawablesWithIntrinsicBounds(drawable5, drawable2, drawable, drawable4);
            return;
        }
        textView.setCompoundDrawablesWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
    }
    
    public static void setCustomSelectionActionModeCallback(@NonNull final TextView textView, @NonNull final ActionMode$Callback actionMode$Callback) {
        textView.setCustomSelectionActionModeCallback(wrapCustomSelectionActionModeCallback(textView, actionMode$Callback));
    }
    
    public static void setFirstBaselineToTopHeight(@NonNull final TextView textView, @IntRange(from = 0L) @Px final int firstBaselineToTopHeight) {
        Preconditions.checkArgumentNonnegative(firstBaselineToTopHeight);
        if (Build$VERSION.SDK_INT >= 28) {
            textView.setFirstBaselineToTopHeight(firstBaselineToTopHeight);
            return;
        }
        final Paint$FontMetricsInt fontMetricsInt = textView.getPaint().getFontMetricsInt();
        int n;
        if (Build$VERSION.SDK_INT >= 16 && !textView.getIncludeFontPadding()) {
            n = fontMetricsInt.ascent;
        }
        else {
            n = fontMetricsInt.top;
        }
        if (firstBaselineToTopHeight > Math.abs(n)) {
            textView.setPadding(textView.getPaddingLeft(), firstBaselineToTopHeight - -n, textView.getPaddingRight(), textView.getPaddingBottom());
        }
    }
    
    public static void setLastBaselineToBottomHeight(@NonNull final TextView textView, @IntRange(from = 0L) @Px final int n) {
        Preconditions.checkArgumentNonnegative(n);
        final Paint$FontMetricsInt fontMetricsInt = textView.getPaint().getFontMetricsInt();
        int n2;
        if (Build$VERSION.SDK_INT >= 16 && !textView.getIncludeFontPadding()) {
            n2 = fontMetricsInt.descent;
        }
        else {
            n2 = fontMetricsInt.bottom;
        }
        if (n > Math.abs(n2)) {
            textView.setPadding(textView.getPaddingLeft(), textView.getPaddingTop(), textView.getPaddingRight(), n - n2);
        }
    }
    
    public static void setLineHeight(@NonNull final TextView textView, @IntRange(from = 0L) @Px final int n) {
        Preconditions.checkArgumentNonnegative(n);
        final int fontMetricsInt = textView.getPaint().getFontMetricsInt((Paint$FontMetricsInt)null);
        if (n != fontMetricsInt) {
            textView.setLineSpacing((float)(n - fontMetricsInt), 1.0f);
        }
    }
    
    public static void setPrecomputedText(@NonNull final TextView textView, @NonNull final PrecomputedTextCompat text) {
        if (!getTextMetricsParams(textView).equalsWithoutTextDirection(text.getParams())) {
            throw new IllegalArgumentException("Given text can not be applied to TextView.");
        }
        textView.setText((CharSequence)text);
    }
    
    public static void setTextAppearance(@NonNull final TextView textView, @StyleRes final int textAppearance) {
        if (Build$VERSION.SDK_INT >= 23) {
            textView.setTextAppearance(textAppearance);
            return;
        }
        textView.setTextAppearance(textView.getContext(), textAppearance);
    }
    
    public static void setTextMetricsParams(@NonNull final TextView textView, @NonNull final PrecomputedTextCompat.Params params) {
        if (Build$VERSION.SDK_INT >= 18) {
            textView.setTextDirection(getTextDirection(params.getTextDirection()));
        }
        if (Build$VERSION.SDK_INT < 23) {
            final float textScaleX = params.getTextPaint().getTextScaleX();
            textView.getPaint().set(params.getTextPaint());
            if (textScaleX == textView.getTextScaleX()) {
                textView.setTextScaleX(textScaleX / 2.0f + 1.0f);
            }
            textView.setTextScaleX(textScaleX);
            return;
        }
        textView.getPaint().set(params.getTextPaint());
        textView.setBreakStrategy(params.getBreakStrategy());
        textView.setHyphenationFrequency(params.getHyphenationFrequency());
    }
    
    @NonNull
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public static ActionMode$Callback wrapCustomSelectionActionModeCallback(@NonNull final TextView textView, @NonNull final ActionMode$Callback actionMode$Callback) {
        if (Build$VERSION.SDK_INT < 26 || Build$VERSION.SDK_INT > 27) {
            return actionMode$Callback;
        }
        if (actionMode$Callback instanceof OreoCallback) {
            return actionMode$Callback;
        }
        return (ActionMode$Callback)new OreoCallback(actionMode$Callback, textView);
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public @interface AutoSizeTextType {
    }
    
    @RequiresApi(26)
    private static class OreoCallback implements ActionMode$Callback
    {
        private static final int MENU_ITEM_ORDER_PROCESS_TEXT_INTENT_ACTIONS_START = 100;
        private final ActionMode$Callback mCallback;
        private boolean mCanUseMenuBuilderReferences;
        private boolean mInitializedMenuBuilderReferences;
        private Class mMenuBuilderClass;
        private Method mMenuBuilderRemoveItemAtMethod;
        private final TextView mTextView;
        
        OreoCallback(final ActionMode$Callback mCallback, final TextView mTextView) {
            this.mCallback = mCallback;
            this.mTextView = mTextView;
            this.mInitializedMenuBuilderReferences = false;
        }
        
        private Intent createProcessTextIntent() {
            return new Intent().setAction("android.intent.action.PROCESS_TEXT").setType("text/plain");
        }
        
        private Intent createProcessTextIntentForResolveInfo(final ResolveInfo resolveInfo, final TextView textView) {
            return this.createProcessTextIntent().putExtra("android.intent.extra.PROCESS_TEXT_READONLY", this.isEditable(textView) ^ true).setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
        }
        
        private List<ResolveInfo> getSupportedActivities(final Context context, final PackageManager packageManager) {
            final ArrayList<ResolveInfo> list = new ArrayList<ResolveInfo>();
            if (!(context instanceof Activity)) {
                return list;
            }
            for (final ResolveInfo resolveInfo : packageManager.queryIntentActivities(this.createProcessTextIntent(), 0)) {
                if (this.isSupportedActivity(resolveInfo, context)) {
                    list.add(resolveInfo);
                }
            }
            return list;
        }
        
        private boolean isEditable(final TextView textView) {
            return textView instanceof Editable && textView.onCheckIsTextEditor() && textView.isEnabled();
        }
        
        private boolean isSupportedActivity(final ResolveInfo resolveInfo, final Context context) {
            return context.getPackageName().equals(resolveInfo.activityInfo.packageName) || (resolveInfo.activityInfo.exported && (resolveInfo.activityInfo.permission == null || context.checkSelfPermission(resolveInfo.activityInfo.permission) == 0));
        }
        
        private void recomputeProcessTextMenuItems(final Menu menu) {
            final Context context = this.mTextView.getContext();
            final PackageManager packageManager = context.getPackageManager();
            if (!this.mInitializedMenuBuilderReferences) {
                this.mInitializedMenuBuilderReferences = true;
                try {
                    this.mMenuBuilderClass = Class.forName("com.android.internal.view.menu.MenuBuilder");
                    this.mMenuBuilderRemoveItemAtMethod = this.mMenuBuilderClass.getDeclaredMethod("removeItemAt", Integer.TYPE);
                    this.mCanUseMenuBuilderReferences = true;
                }
                catch (ClassNotFoundException | NoSuchMethodException ex) {
                    this.mMenuBuilderClass = null;
                    this.mMenuBuilderRemoveItemAtMethod = null;
                    this.mCanUseMenuBuilderReferences = false;
                }
            }
            try {
                Label_0130: {
                    if (this.mCanUseMenuBuilderReferences && this.mMenuBuilderClass.isInstance(menu)) {
                        final Method method = this.mMenuBuilderRemoveItemAtMethod;
                        break Label_0130;
                    }
                    try {
                        final Method method = menu.getClass().getDeclaredMethod("removeItemAt", Integer.TYPE);
                        for (int i = menu.size() - 1; i >= 0; --i) {
                            final MenuItem item = menu.getItem(i);
                            if (item.getIntent() != null && "android.intent.action.PROCESS_TEXT".equals(item.getIntent().getAction())) {
                                method.invoke(menu, i);
                            }
                        }
                        final List<ResolveInfo> supportedActivities = this.getSupportedActivities(context, packageManager);
                        for (int j = 0; j < supportedActivities.size(); ++j) {
                            final ResolveInfo resolveInfo = supportedActivities.get(j);
                            menu.add(0, 0, j + 100, resolveInfo.loadLabel(packageManager)).setIntent(this.createProcessTextIntentForResolveInfo(resolveInfo, this.mTextView)).setShowAsAction(1);
                        }
                    }
                    catch (IllegalAccessException ex2) {}
                }
            }
            catch (NoSuchMethodException ex3) {}
            catch (IllegalAccessException ex4) {}
            catch (InvocationTargetException ex5) {}
        }
        
        public boolean onActionItemClicked(final ActionMode actionMode, final MenuItem menuItem) {
            return this.mCallback.onActionItemClicked(actionMode, menuItem);
        }
        
        public boolean onCreateActionMode(final ActionMode actionMode, final Menu menu) {
            return this.mCallback.onCreateActionMode(actionMode, menu);
        }
        
        public void onDestroyActionMode(final ActionMode actionMode) {
            this.mCallback.onDestroyActionMode(actionMode);
        }
        
        public boolean onPrepareActionMode(final ActionMode actionMode, final Menu menu) {
            this.recomputeProcessTextMenuItems(menu);
            return this.mCallback.onPrepareActionMode(actionMode, menu);
        }
    }
}
