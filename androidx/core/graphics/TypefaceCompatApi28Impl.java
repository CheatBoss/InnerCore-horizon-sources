package androidx.core.graphics;

import androidx.annotation.*;
import android.graphics.*;
import java.lang.reflect.*;

@RequiresApi(28)
@RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
public class TypefaceCompatApi28Impl extends TypefaceCompatApi26Impl
{
    private static final String CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD = "createFromFamiliesWithDefault";
    private static final String DEFAULT_FAMILY = "sans-serif";
    private static final int RESOLVE_BY_FONT_TABLE = -1;
    
    @Override
    protected Typeface createFromFamiliesWithDefault(final Object o) {
        try {
            final Object instance = Array.newInstance(this.mFontFamily, 1);
            Array.set(instance, 0, o);
            return (Typeface)this.mCreateFromFamiliesWithDefault.invoke(null, instance, "sans-serif", -1, -1);
        }
        catch (IllegalAccessException | InvocationTargetException ex) {
            final Object o2;
            throw new RuntimeException((Throwable)o2);
        }
    }
    
    @Override
    protected Method obtainCreateFromFamiliesWithDefaultMethod(final Class clazz) throws NoSuchMethodException {
        final Method declaredMethod = Typeface.class.getDeclaredMethod("createFromFamiliesWithDefault", Array.newInstance(clazz, 1).getClass(), String.class, Integer.TYPE, Integer.TYPE);
        declaredMethod.setAccessible(true);
        return declaredMethod;
    }
}
