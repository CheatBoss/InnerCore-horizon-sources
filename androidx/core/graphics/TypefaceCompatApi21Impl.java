package androidx.core.graphics;

import android.graphics.*;
import java.lang.reflect.*;
import androidx.annotation.*;
import android.system.*;
import android.util.*;
import androidx.core.content.res.*;
import android.content.res.*;
import android.os.*;
import androidx.core.provider.*;
import java.io.*;
import android.content.*;

@RequiresApi(21)
@RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
class TypefaceCompatApi21Impl extends TypefaceCompatBaseImpl
{
    private static final String ADD_FONT_WEIGHT_STYLE_METHOD = "addFontWeightStyle";
    private static final String CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD = "createFromFamiliesWithDefault";
    private static final String FONT_FAMILY_CLASS = "android.graphics.FontFamily";
    private static final String TAG = "TypefaceCompatApi21Impl";
    private static Method sAddFontWeightStyle;
    private static Method sCreateFromFamiliesWithDefault;
    private static Class sFontFamily;
    private static Constructor sFontFamilyCtor;
    private static boolean sHasInitBeenCalled;
    
    static {
        TypefaceCompatApi21Impl.sHasInitBeenCalled = false;
    }
    
    private static boolean addFontWeightStyle(final Object o, final String s, final int n, final boolean b) {
        init();
        try {
            return (boolean)TypefaceCompatApi21Impl.sAddFontWeightStyle.invoke(o, s, n, b);
        }
        catch (IllegalAccessException | InvocationTargetException ex) {
            final Object o2;
            throw new RuntimeException((Throwable)o2);
        }
    }
    
    private static Typeface createFromFamiliesWithDefault(final Object o) {
        init();
        try {
            final Object instance = Array.newInstance(TypefaceCompatApi21Impl.sFontFamily, 1);
            Array.set(instance, 0, o);
            return (Typeface)TypefaceCompatApi21Impl.sCreateFromFamiliesWithDefault.invoke(null, instance);
        }
        catch (IllegalAccessException | InvocationTargetException ex) {
            final Object o2;
            throw new RuntimeException((Throwable)o2);
        }
    }
    
    private File getFile(@NonNull final ParcelFileDescriptor parcelFileDescriptor) {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("/proc/self/fd/");
            sb.append(parcelFileDescriptor.getFd());
            final String readlink = Os.readlink(sb.toString());
            if (OsConstants.S_ISREG(Os.stat(readlink).st_mode)) {
                return new File(readlink);
            }
            return null;
        }
        catch (ErrnoException ex) {
            return null;
        }
    }
    
    private static void init() {
        if (TypefaceCompatApi21Impl.sHasInitBeenCalled) {
            return;
        }
        TypefaceCompatApi21Impl.sHasInitBeenCalled = true;
        Class<?> forName;
        Constructor<?> constructor;
        Method method;
        Method method2;
        try {
            forName = Class.forName("android.graphics.FontFamily");
            constructor = forName.getConstructor((Class<?>[])new Class[0]);
            method = forName.getMethod("addFontWeightStyle", String.class, Integer.TYPE, Boolean.TYPE);
            method2 = Typeface.class.getMethod("createFromFamiliesWithDefault", Array.newInstance(forName, 1).getClass());
        }
        catch (ClassNotFoundException | NoSuchMethodException ex3) {
            final NoSuchMethodException ex2;
            final NoSuchMethodException ex = ex2;
            Log.e("TypefaceCompatApi21Impl", ex.getClass().getName(), (Throwable)ex);
            forName = null;
            constructor = null;
            method = null;
            method2 = null;
        }
        TypefaceCompatApi21Impl.sFontFamilyCtor = constructor;
        TypefaceCompatApi21Impl.sFontFamily = forName;
        TypefaceCompatApi21Impl.sAddFontWeightStyle = method;
        TypefaceCompatApi21Impl.sCreateFromFamiliesWithDefault = method2;
    }
    
    private static Object newFamily() {
        init();
        try {
            return TypefaceCompatApi21Impl.sFontFamilyCtor.newInstance(new Object[0]);
        }
        catch (IllegalAccessException | InstantiationException | InvocationTargetException ex) {
            final Object o;
            throw new RuntimeException((Throwable)o);
        }
    }
    
    @Override
    public Typeface createFromFontFamilyFilesResourceEntry(final Context context, FontResourcesParserCompat.FontFamilyFilesResourceEntry tempFile, final Resources resources, int i) {
        final Object family = newFamily();
        final FontResourcesParserCompat.FontFileResourceEntry[] entries = tempFile.getEntries();
        final int length = entries.length;
        i = 0;
        while (i < length) {
            final FontResourcesParserCompat.FontFileResourceEntry fontFileResourceEntry = entries[i];
            tempFile = (FontResourcesParserCompat.FontFamilyFilesResourceEntry)TypefaceCompatUtil.getTempFile(context);
            if (tempFile == null) {
                return null;
            }
            try {
                if (!TypefaceCompatUtil.copyToFile((File)tempFile, resources, fontFileResourceEntry.getResourceId())) {
                    return null;
                }
                if (!addFontWeightStyle(family, ((File)tempFile).getPath(), fontFileResourceEntry.getWeight(), fontFileResourceEntry.isItalic())) {
                    return null;
                }
                ((File)tempFile).delete();
                ++i;
                continue;
            }
            catch (RuntimeException ex) {
                return null;
            }
            finally {
                ((File)tempFile).delete();
            }
            break;
        }
        return createFromFamiliesWithDefault(family);
    }
    
    @Override
    public Typeface createFromFontInfo(Context t, final CancellationSignal cancellationSignal, @NonNull final FontsContractCompat.FontInfo[] array, final int n) {
        if (array.length < 1) {
            return null;
        }
        final FontsContractCompat.FontInfo bestInfo = this.findBestInfo(array, n);
        final ContentResolver contentResolver = ((Context)t).getContentResolver();
        try {
            final ParcelFileDescriptor openFileDescriptor = contentResolver.openFileDescriptor(bestInfo.getUri(), "r", cancellationSignal);
            if (openFileDescriptor == null) {
                if (openFileDescriptor != null) {
                    openFileDescriptor.close();
                }
                return null;
            }
            Throwable t4;
            try {
                final File file = this.getFile(openFileDescriptor);
                if (file != null && file.canRead()) {
                    final Typeface fromFile = Typeface.createFromFile(file);
                    if (openFileDescriptor != null) {
                        openFileDescriptor.close();
                    }
                    return fromFile;
                }
                final FileInputStream fileInputStream = new FileInputStream(openFileDescriptor.getFileDescriptor());
                Throwable t2;
                try {
                    final Typeface fromInputStream = super.createFromInputStream((Context)t, fileInputStream);
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                    if (openFileDescriptor != null) {
                        openFileDescriptor.close();
                    }
                    return fromInputStream;
                }
                catch (Throwable t) {
                    try {
                        throw t;
                    }
                    finally {
                        t2 = t;
                        final Throwable t3;
                        t = t3;
                    }
                }
                finally {
                    t2 = null;
                }
                if (fileInputStream != null) {
                    if (t2 != null) {
                        try {
                            fileInputStream.close();
                        }
                        catch (Throwable t6) {}
                    }
                    else {
                        fileInputStream.close();
                    }
                }
                throw t;
            }
            catch (Throwable t) {
                try {
                    throw t;
                }
                finally {
                    t4 = t;
                    final Throwable t5;
                    t = t5;
                }
            }
            finally {
                t4 = null;
            }
            if (openFileDescriptor != null) {
                if (t4 != null) {
                    try {
                        openFileDescriptor.close();
                    }
                    catch (Throwable t7) {}
                }
                else {
                    openFileDescriptor.close();
                }
            }
            throw t;
        }
        catch (IOException ex) {
            return null;
        }
    }
}
