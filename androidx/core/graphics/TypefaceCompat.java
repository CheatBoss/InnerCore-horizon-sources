package androidx.core.graphics;

import androidx.collection.*;
import android.graphics.*;
import android.content.*;
import androidx.core.provider.*;
import androidx.annotation.*;
import android.content.res.*;
import androidx.core.content.res.*;
import android.os.*;

public class TypefaceCompat
{
    private static final LruCache<String, Typeface> sTypefaceCache;
    private static final TypefaceCompatBaseImpl sTypefaceCompatImpl;
    
    static {
        if (Build$VERSION.SDK_INT >= 28) {
            sTypefaceCompatImpl = new TypefaceCompatApi28Impl();
        }
        else if (Build$VERSION.SDK_INT >= 26) {
            sTypefaceCompatImpl = new TypefaceCompatApi26Impl();
        }
        else if (Build$VERSION.SDK_INT >= 24 && TypefaceCompatApi24Impl.isUsable()) {
            sTypefaceCompatImpl = new TypefaceCompatApi24Impl();
        }
        else if (Build$VERSION.SDK_INT >= 21) {
            sTypefaceCompatImpl = new TypefaceCompatApi21Impl();
        }
        else {
            sTypefaceCompatImpl = new TypefaceCompatBaseImpl();
        }
        sTypefaceCache = new LruCache(16);
    }
    
    private TypefaceCompat() {
    }
    
    @NonNull
    public static Typeface create(@NonNull final Context context, @Nullable final Typeface typeface, final int n) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        if (Build$VERSION.SDK_INT < 21) {
            final Typeface bestFontFromFamily = getBestFontFromFamily(context, typeface, n);
            if (bestFontFromFamily != null) {
                return bestFontFromFamily;
            }
        }
        return Typeface.create(typeface, n);
    }
    
    @Nullable
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public static Typeface createFromFontInfo(@NonNull final Context context, @Nullable final CancellationSignal cancellationSignal, @NonNull final FontsContractCompat.FontInfo[] array, final int n) {
        return TypefaceCompat.sTypefaceCompatImpl.createFromFontInfo(context, cancellationSignal, array, n);
    }
    
    @Nullable
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public static Typeface createFromResourcesFamilyXml(@NonNull final Context context, @NonNull final FontResourcesParserCompat.FamilyResourceEntry familyResourceEntry, @NonNull final Resources resources, final int n, final int n2, @Nullable final ResourcesCompat.FontCallback fontCallback, @Nullable final Handler handler, final boolean b) {
        Typeface typeface;
        if (familyResourceEntry instanceof FontResourcesParserCompat.ProviderResourceEntry) {
            final FontResourcesParserCompat.ProviderResourceEntry providerResourceEntry = (FontResourcesParserCompat.ProviderResourceEntry)familyResourceEntry;
            final boolean b2 = b ? (providerResourceEntry.getFetchStrategy() == 0) : (fontCallback == null);
            int timeout;
            if (b) {
                timeout = providerResourceEntry.getTimeout();
            }
            else {
                timeout = -1;
            }
            typeface = FontsContractCompat.getFontSync(context, providerResourceEntry.getRequest(), fontCallback, handler, b2, timeout, n2);
        }
        else {
            final Typeface typeface2 = typeface = TypefaceCompat.sTypefaceCompatImpl.createFromFontFamilyFilesResourceEntry(context, (FontResourcesParserCompat.FontFamilyFilesResourceEntry)familyResourceEntry, resources, n2);
            if (fontCallback != null) {
                if (typeface2 != null) {
                    fontCallback.callbackSuccessAsync(typeface2, handler);
                    typeface = typeface2;
                }
                else {
                    fontCallback.callbackFailAsync(-3, handler);
                    typeface = typeface2;
                }
            }
        }
        if (typeface != null) {
            TypefaceCompat.sTypefaceCache.put((Object)createResourceUid(resources, n, n2), (Object)typeface);
        }
        return typeface;
    }
    
    @Nullable
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public static Typeface createFromResourcesFontFile(@NonNull final Context context, @NonNull final Resources resources, final int n, final String s, final int n2) {
        final Typeface fromResourcesFontFile = TypefaceCompat.sTypefaceCompatImpl.createFromResourcesFontFile(context, resources, n, s, n2);
        if (fromResourcesFontFile != null) {
            TypefaceCompat.sTypefaceCache.put((Object)createResourceUid(resources, n, n2), (Object)fromResourcesFontFile);
        }
        return fromResourcesFontFile;
    }
    
    private static String createResourceUid(final Resources resources, final int n, final int n2) {
        final StringBuilder sb = new StringBuilder();
        sb.append(resources.getResourcePackageName(n));
        sb.append("-");
        sb.append(n);
        sb.append("-");
        sb.append(n2);
        return sb.toString();
    }
    
    @Nullable
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public static Typeface findFromCache(@NonNull final Resources resources, final int n, final int n2) {
        return (Typeface)TypefaceCompat.sTypefaceCache.get((Object)createResourceUid(resources, n, n2));
    }
    
    @Nullable
    private static Typeface getBestFontFromFamily(final Context context, final Typeface typeface, final int n) {
        final FontResourcesParserCompat.FontFamilyFilesResourceEntry fontFamily = TypefaceCompat.sTypefaceCompatImpl.getFontFamily(typeface);
        if (fontFamily == null) {
            return null;
        }
        return TypefaceCompat.sTypefaceCompatImpl.createFromFontFamilyFilesResourceEntry(context, fontFamily, context.getResources(), n);
    }
}
