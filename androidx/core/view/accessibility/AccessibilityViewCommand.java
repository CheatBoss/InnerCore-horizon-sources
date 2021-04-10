package androidx.core.view.accessibility;

import android.view.*;
import android.os.*;
import androidx.annotation.*;

public interface AccessibilityViewCommand
{
    boolean perform(@NonNull final View p0, @Nullable final CommandArguments p1);
    
    public abstract static class CommandArguments
    {
        private static final Bundle sEmptyBundle;
        Bundle mBundle;
        
        static {
            sEmptyBundle = new Bundle();
        }
        
        @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
        public void setBundle(final Bundle mBundle) {
            this.mBundle = mBundle;
        }
    }
    
    public static final class MoveAtGranularityArguments extends CommandArguments
    {
        public boolean getExtendSelection() {
            return this.mBundle.getBoolean("ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN");
        }
        
        public int getGranularity() {
            return this.mBundle.getInt("ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT");
        }
    }
    
    public static final class MoveHtmlArguments extends CommandArguments
    {
        public String getHTMLElement() {
            return this.mBundle.getString("ACTION_ARGUMENT_HTML_ELEMENT_STRING");
        }
    }
    
    public static final class MoveWindowArguments extends CommandArguments
    {
        public int getX() {
            return this.mBundle.getInt("ACTION_ARGUMENT_MOVE_WINDOW_X");
        }
        
        public int getY() {
            return this.mBundle.getInt("ACTION_ARGUMENT_MOVE_WINDOW_Y");
        }
    }
    
    public static final class ScrollToPositionArguments extends CommandArguments
    {
        public int getColumn() {
            return this.mBundle.getInt("android.view.accessibility.action.ARGUMENT_COLUMN_INT");
        }
        
        public int getRow() {
            return this.mBundle.getInt("android.view.accessibility.action.ARGUMENT_ROW_INT");
        }
    }
    
    public static final class SetProgressArguments extends CommandArguments
    {
        public float getProgress() {
            return this.mBundle.getFloat("android.view.accessibility.action.ARGUMENT_PROGRESS_VALUE");
        }
    }
    
    public static final class SetSelectionArguments extends CommandArguments
    {
        public int getEnd() {
            return this.mBundle.getInt("ACTION_ARGUMENT_SELECTION_END_INT");
        }
        
        public int getStart() {
            return this.mBundle.getInt("ACTION_ARGUMENT_SELECTION_START_INT");
        }
    }
    
    public static final class SetTextArguments extends CommandArguments
    {
        public CharSequence getText() {
            return this.mBundle.getCharSequence("ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE");
        }
    }
}
