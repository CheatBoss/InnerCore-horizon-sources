package androidx.core.content.pm;

import java.util.*;
import androidx.annotation.*;

@RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
public abstract class ShortcutInfoCompatSaver<T>
{
    @AnyThread
    public abstract T addShortcuts(final List<ShortcutInfoCompat> p0);
    
    @WorkerThread
    public List<ShortcutInfoCompat> getShortcuts() throws Exception {
        return new ArrayList<ShortcutInfoCompat>();
    }
    
    @AnyThread
    public abstract T removeAllShortcuts();
    
    @AnyThread
    public abstract T removeShortcuts(final List<String> p0);
    
    @RestrictTo({ RestrictTo$Scope.LIBRARY })
    public static class NoopImpl extends ShortcutInfoCompatSaver<Void>
    {
        @Override
        public Void addShortcuts(final List<ShortcutInfoCompat> list) {
            return null;
        }
        
        @Override
        public Void removeAllShortcuts() {
            return null;
        }
        
        @Override
        public Void removeShortcuts(final List<String> list) {
            return null;
        }
    }
}
