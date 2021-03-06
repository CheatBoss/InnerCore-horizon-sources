package androidx.core.internal.view;

import androidx.annotation.*;
import android.view.*;
import android.content.res.*;
import android.graphics.*;
import androidx.core.view.*;

@RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
public interface SupportMenuItem extends MenuItem
{
    public static final int SHOW_AS_ACTION_ALWAYS = 2;
    public static final int SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW = 8;
    public static final int SHOW_AS_ACTION_IF_ROOM = 1;
    public static final int SHOW_AS_ACTION_NEVER = 0;
    public static final int SHOW_AS_ACTION_WITH_TEXT = 4;
    
    boolean collapseActionView();
    
    boolean expandActionView();
    
    View getActionView();
    
    int getAlphabeticModifiers();
    
    CharSequence getContentDescription();
    
    ColorStateList getIconTintList();
    
    PorterDuff$Mode getIconTintMode();
    
    int getNumericModifiers();
    
    ActionProvider getSupportActionProvider();
    
    CharSequence getTooltipText();
    
    boolean isActionViewExpanded();
    
    boolean requiresActionButton();
    
    boolean requiresOverflow();
    
    MenuItem setActionView(final int p0);
    
    MenuItem setActionView(final View p0);
    
    MenuItem setAlphabeticShortcut(final char p0, final int p1);
    
    SupportMenuItem setContentDescription(final CharSequence p0);
    
    MenuItem setIconTintList(final ColorStateList p0);
    
    MenuItem setIconTintMode(final PorterDuff$Mode p0);
    
    MenuItem setNumericShortcut(final char p0, final int p1);
    
    MenuItem setShortcut(final char p0, final char p1, final int p2, final int p3);
    
    void setShowAsAction(final int p0);
    
    MenuItem setShowAsActionFlags(final int p0);
    
    SupportMenuItem setSupportActionProvider(final ActionProvider p0);
    
    SupportMenuItem setTooltipText(final CharSequence p0);
}
