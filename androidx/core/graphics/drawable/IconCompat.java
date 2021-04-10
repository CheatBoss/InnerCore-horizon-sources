package androidx.core.graphics.drawable;

import androidx.versionedparcelable.*;
import android.util.*;
import androidx.core.util.*;
import android.content.res.*;
import android.net.*;
import android.os.*;
import java.lang.reflect.*;
import android.content.pm.*;
import android.graphics.drawable.*;
import android.text.*;
import androidx.core.content.res.*;
import android.content.*;
import androidx.core.content.*;
import android.app.*;
import java.nio.charset.*;
import android.graphics.*;
import java.io.*;
import androidx.annotation.*;
import java.lang.annotation.*;

public class IconCompat extends CustomVersionedParcelable
{
    private static final float ADAPTIVE_ICON_INSET_FACTOR = 0.25f;
    private static final int AMBIENT_SHADOW_ALPHA = 30;
    private static final float BLUR_FACTOR = 0.010416667f;
    static final PorterDuff$Mode DEFAULT_TINT_MODE;
    private static final float DEFAULT_VIEW_PORT_SCALE = 0.6666667f;
    private static final String EXTRA_INT1 = "int1";
    private static final String EXTRA_INT2 = "int2";
    private static final String EXTRA_OBJ = "obj";
    private static final String EXTRA_TINT_LIST = "tint_list";
    private static final String EXTRA_TINT_MODE = "tint_mode";
    private static final String EXTRA_TYPE = "type";
    private static final float ICON_DIAMETER_FACTOR = 0.9166667f;
    private static final int KEY_SHADOW_ALPHA = 61;
    private static final float KEY_SHADOW_OFFSET_FACTOR = 0.020833334f;
    private static final String TAG = "IconCompat";
    public static final int TYPE_UNKNOWN = -1;
    @RestrictTo({ RestrictTo$Scope.LIBRARY })
    public byte[] mData;
    @RestrictTo({ RestrictTo$Scope.LIBRARY })
    public int mInt1;
    @RestrictTo({ RestrictTo$Scope.LIBRARY })
    public int mInt2;
    Object mObj1;
    @RestrictTo({ RestrictTo$Scope.LIBRARY })
    public Parcelable mParcelable;
    @RestrictTo({ RestrictTo$Scope.LIBRARY })
    public ColorStateList mTintList;
    PorterDuff$Mode mTintMode;
    @RestrictTo({ RestrictTo$Scope.LIBRARY })
    public String mTintModeStr;
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public int mType;
    
    static {
        DEFAULT_TINT_MODE = PorterDuff$Mode.SRC_IN;
    }
    
    @RestrictTo({ RestrictTo$Scope.LIBRARY })
    public IconCompat() {
        this.mType = -1;
        this.mData = null;
        this.mParcelable = null;
        this.mInt1 = 0;
        this.mInt2 = 0;
        this.mTintList = null;
        this.mTintMode = IconCompat.DEFAULT_TINT_MODE;
        this.mTintModeStr = null;
    }
    
    private IconCompat(final int mType) {
        this.mType = -1;
        this.mData = null;
        this.mParcelable = null;
        this.mInt1 = 0;
        this.mInt2 = 0;
        this.mTintList = null;
        this.mTintMode = IconCompat.DEFAULT_TINT_MODE;
        this.mTintModeStr = null;
        this.mType = mType;
    }
    
    @Nullable
    public static IconCompat createFromBundle(@NonNull final Bundle bundle) {
        final int int1 = bundle.getInt("type");
        final IconCompat iconCompat = new IconCompat(int1);
        iconCompat.mInt1 = bundle.getInt("int1");
        iconCompat.mInt2 = bundle.getInt("int2");
        if (bundle.containsKey("tint_list")) {
            iconCompat.mTintList = (ColorStateList)bundle.getParcelable("tint_list");
        }
        if (bundle.containsKey("tint_mode")) {
            iconCompat.mTintMode = PorterDuff$Mode.valueOf(bundle.getString("tint_mode"));
        }
        if (int1 != -1) {
            switch (int1) {
                default: {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unknown type ");
                    sb.append(int1);
                    Log.w("IconCompat", sb.toString());
                    return null;
                }
                case 3: {
                    iconCompat.mObj1 = bundle.getByteArray("obj");
                    return iconCompat;
                }
                case 2:
                case 4: {
                    iconCompat.mObj1 = bundle.getString("obj");
                    return iconCompat;
                }
                case 1:
                case 5: {
                    break;
                }
            }
        }
        iconCompat.mObj1 = bundle.getParcelable("obj");
        return iconCompat;
    }
    
    @Nullable
    @RequiresApi(23)
    public static IconCompat createFromIcon(@NonNull final Context context, @NonNull final Icon mObj1) {
        Preconditions.checkNotNull(mObj1);
        final int type = getType(mObj1);
        if (type != 2) {
            if (type != 4) {
                final IconCompat iconCompat = new IconCompat(-1);
                iconCompat.mObj1 = mObj1;
                return iconCompat;
            }
            return createWithContentUri(getUri(mObj1));
        }
        else {
            final String resPackage = getResPackage(mObj1);
            try {
                return createWithResource(getResources(context, resPackage), resPackage, getResId(mObj1));
            }
            catch (Resources$NotFoundException ex) {
                throw new IllegalArgumentException("Icon resource cannot be found");
            }
        }
    }
    
    @Nullable
    @RequiresApi(23)
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public static IconCompat createFromIcon(@NonNull final Icon mObj1) {
        Preconditions.checkNotNull(mObj1);
        final int type = getType(mObj1);
        if (type == 2) {
            return createWithResource(null, getResPackage(mObj1), getResId(mObj1));
        }
        if (type != 4) {
            final IconCompat iconCompat = new IconCompat(-1);
            iconCompat.mObj1 = mObj1;
            return iconCompat;
        }
        return createWithContentUri(getUri(mObj1));
    }
    
    @VisibleForTesting
    static Bitmap createLegacyIconFromAdaptiveIcon(final Bitmap bitmap, final boolean b) {
        final int n = (int)(Math.min(bitmap.getWidth(), bitmap.getHeight()) * 0.6666667f);
        final Bitmap bitmap2 = Bitmap.createBitmap(n, n, Bitmap$Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap2);
        final Paint paint = new Paint(3);
        final float n2 = n * 0.5f;
        final float n3 = 0.9166667f * n2;
        if (b) {
            final float n4 = n * 0.010416667f;
            paint.setColor(0);
            paint.setShadowLayer(n4, 0.0f, n * 0.020833334f, 1023410176);
            canvas.drawCircle(n2, n2, n3, paint);
            paint.setShadowLayer(n4, 0.0f, 0.0f, 503316480);
            canvas.drawCircle(n2, n2, n3, paint);
            paint.clearShadowLayer();
        }
        paint.setColor(-16777216);
        final BitmapShader shader = new BitmapShader(bitmap, Shader$TileMode.CLAMP, Shader$TileMode.CLAMP);
        final Matrix localMatrix = new Matrix();
        localMatrix.setTranslate((float)(-(bitmap.getWidth() - n) / 2), (float)(-(bitmap.getHeight() - n) / 2));
        shader.setLocalMatrix(localMatrix);
        paint.setShader((Shader)shader);
        canvas.drawCircle(n2, n2, n3, paint);
        canvas.setBitmap((Bitmap)null);
        return bitmap2;
    }
    
    public static IconCompat createWithAdaptiveBitmap(final Bitmap mObj1) {
        if (mObj1 == null) {
            throw new IllegalArgumentException("Bitmap must not be null.");
        }
        final IconCompat iconCompat = new IconCompat(5);
        iconCompat.mObj1 = mObj1;
        return iconCompat;
    }
    
    public static IconCompat createWithBitmap(final Bitmap mObj1) {
        if (mObj1 == null) {
            throw new IllegalArgumentException("Bitmap must not be null.");
        }
        final IconCompat iconCompat = new IconCompat(1);
        iconCompat.mObj1 = mObj1;
        return iconCompat;
    }
    
    public static IconCompat createWithContentUri(final Uri uri) {
        if (uri == null) {
            throw new IllegalArgumentException("Uri must not be null.");
        }
        return createWithContentUri(uri.toString());
    }
    
    public static IconCompat createWithContentUri(final String mObj1) {
        if (mObj1 == null) {
            throw new IllegalArgumentException("Uri must not be null.");
        }
        final IconCompat iconCompat = new IconCompat(4);
        iconCompat.mObj1 = mObj1;
        return iconCompat;
    }
    
    public static IconCompat createWithData(final byte[] mObj1, final int mInt1, final int mInt2) {
        if (mObj1 == null) {
            throw new IllegalArgumentException("Data must not be null.");
        }
        final IconCompat iconCompat = new IconCompat(3);
        iconCompat.mObj1 = mObj1;
        iconCompat.mInt1 = mInt1;
        iconCompat.mInt2 = mInt2;
        return iconCompat;
    }
    
    public static IconCompat createWithResource(final Context context, @DrawableRes final int n) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null.");
        }
        return createWithResource(context.getResources(), context.getPackageName(), n);
    }
    
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public static IconCompat createWithResource(final Resources resources, final String mObj1, @DrawableRes final int mInt1) {
        if (mObj1 == null) {
            throw new IllegalArgumentException("Package must not be null.");
        }
        if (mInt1 == 0) {
            throw new IllegalArgumentException("Drawable resource ID must not be 0");
        }
        final IconCompat iconCompat = new IconCompat(2);
        iconCompat.mInt1 = mInt1;
        if (resources != null) {
            try {
                iconCompat.mObj1 = resources.getResourceName(mInt1);
                return iconCompat;
            }
            catch (Resources$NotFoundException ex) {
                throw new IllegalArgumentException("Icon resource cannot be found");
            }
        }
        iconCompat.mObj1 = mObj1;
        return iconCompat;
    }
    
    @DrawableRes
    @IdRes
    @RequiresApi(23)
    private static int getResId(@NonNull final Icon icon) {
        if (Build$VERSION.SDK_INT >= 28) {
            return icon.getResId();
        }
        try {
            return (int)icon.getClass().getMethod("getResId", (Class<?>[])new Class[0]).invoke(icon, new Object[0]);
        }
        catch (NoSuchMethodException ex) {
            Log.e("IconCompat", "Unable to get icon resource", (Throwable)ex);
            return 0;
        }
        catch (InvocationTargetException ex2) {
            Log.e("IconCompat", "Unable to get icon resource", (Throwable)ex2);
            return 0;
        }
        catch (IllegalAccessException ex3) {
            Log.e("IconCompat", "Unable to get icon resource", (Throwable)ex3);
            return 0;
        }
    }
    
    @Nullable
    @RequiresApi(23)
    private static String getResPackage(@NonNull final Icon icon) {
        if (Build$VERSION.SDK_INT >= 28) {
            return icon.getResPackage();
        }
        try {
            return (String)icon.getClass().getMethod("getResPackage", (Class<?>[])new Class[0]).invoke(icon, new Object[0]);
        }
        catch (NoSuchMethodException ex) {
            Log.e("IconCompat", "Unable to get icon package", (Throwable)ex);
            return null;
        }
        catch (InvocationTargetException ex2) {
            Log.e("IconCompat", "Unable to get icon package", (Throwable)ex2);
            return null;
        }
        catch (IllegalAccessException ex3) {
            Log.e("IconCompat", "Unable to get icon package", (Throwable)ex3);
            return null;
        }
    }
    
    private static Resources getResources(final Context context, final String s) {
        if ("android".equals(s)) {
            return Resources.getSystem();
        }
        final PackageManager packageManager = context.getPackageManager();
        try {
            final ApplicationInfo applicationInfo = packageManager.getApplicationInfo(s, 8192);
            if (applicationInfo != null) {
                return packageManager.getResourcesForApplication(applicationInfo);
            }
            return null;
        }
        catch (PackageManager$NameNotFoundException ex) {
            Log.e("IconCompat", String.format("Unable to find pkg=%s for icon", s), (Throwable)ex);
            return null;
        }
    }
    
    @RequiresApi(23)
    private static int getType(@NonNull final Icon icon) {
        if (Build$VERSION.SDK_INT >= 28) {
            return icon.getType();
        }
        try {
            return (int)icon.getClass().getMethod("getType", (Class<?>[])new Class[0]).invoke(icon, new Object[0]);
        }
        catch (NoSuchMethodException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unable to get icon type ");
            sb.append(icon);
            Log.e("IconCompat", sb.toString(), (Throwable)ex);
            return -1;
        }
        catch (InvocationTargetException ex2) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Unable to get icon type ");
            sb2.append(icon);
            Log.e("IconCompat", sb2.toString(), (Throwable)ex2);
            return -1;
        }
        catch (IllegalAccessException ex3) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Unable to get icon type ");
            sb3.append(icon);
            Log.e("IconCompat", sb3.toString(), (Throwable)ex3);
            return -1;
        }
    }
    
    @Nullable
    @RequiresApi(23)
    private static Uri getUri(@NonNull final Icon icon) {
        if (Build$VERSION.SDK_INT >= 28) {
            return icon.getUri();
        }
        try {
            return (Uri)icon.getClass().getMethod("getUri", (Class<?>[])new Class[0]).invoke(icon, new Object[0]);
        }
        catch (NoSuchMethodException ex) {
            Log.e("IconCompat", "Unable to get icon uri", (Throwable)ex);
            return null;
        }
        catch (InvocationTargetException ex2) {
            Log.e("IconCompat", "Unable to get icon uri", (Throwable)ex2);
            return null;
        }
        catch (IllegalAccessException ex3) {
            Log.e("IconCompat", "Unable to get icon uri", (Throwable)ex3);
            return null;
        }
    }
    
    private Drawable loadDrawableInner(final Context context) {
        switch (this.mType) {
            case 5: {
                return (Drawable)new BitmapDrawable(context.getResources(), createLegacyIconFromAdaptiveIcon((Bitmap)this.mObj1, false));
            }
            case 4: {
                final Uri parse = Uri.parse((String)this.mObj1);
                final String scheme = parse.getScheme();
                final InputStream inputStream = null;
                InputStream openInputStream = null;
                Label_0240: {
                    if (!"content".equals(scheme)) {
                        if (!"file".equals(scheme)) {
                            try {
                                openInputStream = new FileInputStream(new File((String)this.mObj1));
                            }
                            catch (FileNotFoundException ex) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("Unable to load image from path: ");
                                sb.append(parse);
                                Log.w("IconCompat", sb.toString(), (Throwable)ex);
                                openInputStream = inputStream;
                            }
                            break Label_0240;
                        }
                    }
                    try {
                        openInputStream = context.getContentResolver().openInputStream(parse);
                    }
                    catch (Exception ex2) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Unable to load image from URI: ");
                        sb2.append(parse);
                        Log.w("IconCompat", sb2.toString(), (Throwable)ex2);
                    }
                }
                if (openInputStream != null) {
                    return (Drawable)new BitmapDrawable(context.getResources(), BitmapFactory.decodeStream(openInputStream));
                }
                break;
            }
            case 3: {
                return (Drawable)new BitmapDrawable(context.getResources(), BitmapFactory.decodeByteArray((byte[])this.mObj1, this.mInt1, this.mInt2));
            }
            case 2: {
                String s;
                if (TextUtils.isEmpty((CharSequence)(s = this.getResPackage()))) {
                    s = context.getPackageName();
                }
                final Resources resources = getResources(context, s);
                try {
                    return ResourcesCompat.getDrawable(resources, this.mInt1, context.getTheme());
                }
                catch (RuntimeException ex3) {
                    Log.e("IconCompat", String.format("Unable to load resource 0x%08x from pkg=%s", this.mInt1, this.mObj1), (Throwable)ex3);
                    break;
                }
            }
            case 1: {
                return (Drawable)new BitmapDrawable(context.getResources(), (Bitmap)this.mObj1);
            }
        }
        return null;
    }
    
    private static String typeToString(final int n) {
        switch (n) {
            default: {
                return "UNKNOWN";
            }
            case 5: {
                return "BITMAP_MASKABLE";
            }
            case 4: {
                return "URI";
            }
            case 3: {
                return "DATA";
            }
            case 2: {
                return "RESOURCE";
            }
            case 1: {
                return "BITMAP";
            }
        }
    }
    
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public void addToShortcutIntent(@NonNull final Intent intent, @Nullable final Drawable drawable, @NonNull Context packageContext) {
        this.checkResource(packageContext);
        final int mType = this.mType;
        Bitmap bitmap = null;
        if (mType != 5) {
            switch (mType) {
                default: {
                    throw new IllegalArgumentException("Icon type not supported for intent shortcuts");
                }
                case 2: {
                    try {
                        packageContext = packageContext.createPackageContext(this.getResPackage(), 0);
                        if (drawable == null) {
                            intent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", (Parcelable)Intent$ShortcutIconResource.fromContext(packageContext, this.mInt1));
                            return;
                        }
                        final Drawable drawable2 = ContextCompat.getDrawable(packageContext, this.mInt1);
                        if (drawable2.getIntrinsicWidth() > 0 && drawable2.getIntrinsicHeight() > 0) {
                            bitmap = Bitmap.createBitmap(drawable2.getIntrinsicWidth(), drawable2.getIntrinsicHeight(), Bitmap$Config.ARGB_8888);
                        }
                        else {
                            final int launcherLargeIconSize = ((ActivityManager)packageContext.getSystemService("activity")).getLauncherLargeIconSize();
                            bitmap = Bitmap.createBitmap(launcherLargeIconSize, launcherLargeIconSize, Bitmap$Config.ARGB_8888);
                        }
                        drawable2.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                        drawable2.draw(new Canvas(bitmap));
                        break;
                    }
                    catch (PackageManager$NameNotFoundException ex) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Can't find package ");
                        sb.append(this.mObj1);
                        throw new IllegalArgumentException(sb.toString(), (Throwable)ex);
                    }
                }
                case 1: {
                    final Bitmap bitmap2 = bitmap = (Bitmap)this.mObj1;
                    if (drawable != null) {
                        bitmap = bitmap2.copy(bitmap2.getConfig(), true);
                        break;
                    }
                    break;
                }
            }
        }
        else {
            bitmap = createLegacyIconFromAdaptiveIcon((Bitmap)this.mObj1, true);
        }
        if (drawable != null) {
            final int width = bitmap.getWidth();
            final int height = bitmap.getHeight();
            drawable.setBounds(width / 2, height / 2, width, height);
            drawable.draw(new Canvas(bitmap));
        }
        intent.putExtra("android.intent.extra.shortcut.ICON", (Parcelable)bitmap);
    }
    
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public void checkResource(final Context context) {
        if (this.mType == 2) {
            final String s = (String)this.mObj1;
            if (!s.contains(":")) {
                return;
            }
            final String s2 = s.split(":", -1)[1];
            final String s3 = s2.split("/", -1)[0];
            final String s4 = s2.split("/", -1)[1];
            final String s5 = s.split(":", -1)[0];
            final int identifier = getResources(context, s5).getIdentifier(s4, s3, s5);
            if (this.mInt1 != identifier) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Id has changed for ");
                sb.append(s5);
                sb.append("/");
                sb.append(s4);
                Log.i("IconCompat", sb.toString());
                this.mInt1 = identifier;
            }
        }
    }
    
    @Nullable
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public Bitmap getBitmap() {
        if (this.mType == -1 && Build$VERSION.SDK_INT >= 23) {
            if (this.mObj1 instanceof Bitmap) {
                return (Bitmap)this.mObj1;
            }
            return null;
        }
        else {
            if (this.mType == 1) {
                return (Bitmap)this.mObj1;
            }
            if (this.mType == 5) {
                return createLegacyIconFromAdaptiveIcon((Bitmap)this.mObj1, true);
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("called getBitmap() on ");
            sb.append(this);
            throw new IllegalStateException(sb.toString());
        }
    }
    
    @IdRes
    public int getResId() {
        if (this.mType == -1 && Build$VERSION.SDK_INT >= 23) {
            return getResId((Icon)this.mObj1);
        }
        if (this.mType != 2) {
            final StringBuilder sb = new StringBuilder();
            sb.append("called getResId() on ");
            sb.append(this);
            throw new IllegalStateException(sb.toString());
        }
        return this.mInt1;
    }
    
    @NonNull
    public String getResPackage() {
        if (this.mType == -1 && Build$VERSION.SDK_INT >= 23) {
            return getResPackage((Icon)this.mObj1);
        }
        if (this.mType != 2) {
            final StringBuilder sb = new StringBuilder();
            sb.append("called getResPackage() on ");
            sb.append(this);
            throw new IllegalStateException(sb.toString());
        }
        return ((String)this.mObj1).split(":", -1)[0];
    }
    
    public int getType() {
        if (this.mType == -1 && Build$VERSION.SDK_INT >= 23) {
            return getType((Icon)this.mObj1);
        }
        return this.mType;
    }
    
    @NonNull
    public Uri getUri() {
        if (this.mType == -1 && Build$VERSION.SDK_INT >= 23) {
            return getUri((Icon)this.mObj1);
        }
        return Uri.parse((String)this.mObj1);
    }
    
    public Drawable loadDrawable(final Context context) {
        this.checkResource(context);
        if (Build$VERSION.SDK_INT >= 23) {
            return this.toIcon().loadDrawable(context);
        }
        final Drawable loadDrawableInner = this.loadDrawableInner(context);
        if (loadDrawableInner != null && (this.mTintList != null || this.mTintMode != IconCompat.DEFAULT_TINT_MODE)) {
            loadDrawableInner.mutate();
            DrawableCompat.setTintList(loadDrawableInner, this.mTintList);
            DrawableCompat.setTintMode(loadDrawableInner, this.mTintMode);
        }
        return loadDrawableInner;
    }
    
    public void onPostParceling() {
        this.mTintMode = PorterDuff$Mode.valueOf(this.mTintModeStr);
        final int mType = this.mType;
        if (mType != -1) {
            switch (mType) {
                default: {}
                case 3: {
                    this.mObj1 = this.mData;
                }
                case 2:
                case 4: {
                    this.mObj1 = new String(this.mData, Charset.forName("UTF-16"));
                }
                case 1:
                case 5: {
                    if (this.mParcelable != null) {
                        this.mObj1 = this.mParcelable;
                        return;
                    }
                    this.mObj1 = this.mData;
                    this.mType = 3;
                    this.mInt1 = 0;
                    this.mInt2 = this.mData.length;
                }
            }
        }
        else {
            if (this.mParcelable != null) {
                this.mObj1 = this.mParcelable;
                return;
            }
            throw new IllegalArgumentException("Invalid icon");
        }
    }
    
    public void onPreParceling(final boolean b) {
        this.mTintModeStr = this.mTintMode.name();
        final int mType = this.mType;
        if (mType != -1) {
            switch (mType) {
                default: {}
                case 4: {
                    this.mData = this.mObj1.toString().getBytes(Charset.forName("UTF-16"));
                }
                case 3: {
                    this.mData = (byte[])this.mObj1;
                }
                case 2: {
                    this.mData = ((String)this.mObj1).getBytes(Charset.forName("UTF-16"));
                }
                case 1:
                case 5: {
                    if (b) {
                        final Bitmap bitmap = (Bitmap)this.mObj1;
                        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap$CompressFormat.PNG, 90, (OutputStream)byteArrayOutputStream);
                        this.mData = byteArrayOutputStream.toByteArray();
                        return;
                    }
                    this.mParcelable = (Parcelable)this.mObj1;
                }
            }
        }
        else {
            if (b) {
                throw new IllegalArgumentException("Can't serialize Icon created with IconCompat#createFromIcon");
            }
            this.mParcelable = (Parcelable)this.mObj1;
        }
    }
    
    public IconCompat setTint(@ColorInt final int n) {
        return this.setTintList(ColorStateList.valueOf(n));
    }
    
    public IconCompat setTintList(final ColorStateList mTintList) {
        this.mTintList = mTintList;
        return this;
    }
    
    public IconCompat setTintMode(final PorterDuff$Mode mTintMode) {
        this.mTintMode = mTintMode;
        return this;
    }
    
    public Bundle toBundle() {
        final Bundle bundle = new Bundle();
        final int mType = this.mType;
        if (mType != -1) {
            switch (mType) {
                default: {
                    throw new IllegalArgumentException("Invalid icon");
                }
                case 3: {
                    bundle.putByteArray("obj", (byte[])this.mObj1);
                    break;
                }
                case 2:
                case 4: {
                    bundle.putString("obj", (String)this.mObj1);
                    break;
                }
                case 1:
                case 5: {
                    bundle.putParcelable("obj", (Parcelable)this.mObj1);
                    break;
                }
            }
        }
        else {
            bundle.putParcelable("obj", (Parcelable)this.mObj1);
        }
        bundle.putInt("type", this.mType);
        bundle.putInt("int1", this.mInt1);
        bundle.putInt("int2", this.mInt2);
        if (this.mTintList != null) {
            bundle.putParcelable("tint_list", (Parcelable)this.mTintList);
        }
        if (this.mTintMode != IconCompat.DEFAULT_TINT_MODE) {
            bundle.putString("tint_mode", this.mTintMode.name());
        }
        return bundle;
    }
    
    @RequiresApi(23)
    public Icon toIcon() {
        final int mType = this.mType;
        if (mType != -1) {
            Icon icon = null;
            switch (mType) {
                default: {
                    throw new IllegalArgumentException("Unknown type");
                }
                case 5: {
                    if (Build$VERSION.SDK_INT >= 26) {
                        icon = Icon.createWithAdaptiveBitmap((Bitmap)this.mObj1);
                        break;
                    }
                    icon = Icon.createWithBitmap(createLegacyIconFromAdaptiveIcon((Bitmap)this.mObj1, false));
                    break;
                }
                case 4: {
                    icon = Icon.createWithContentUri((String)this.mObj1);
                    break;
                }
                case 3: {
                    icon = Icon.createWithData((byte[])this.mObj1, this.mInt1, this.mInt2);
                    break;
                }
                case 2: {
                    icon = Icon.createWithResource(this.getResPackage(), this.mInt1);
                    break;
                }
                case 1: {
                    icon = Icon.createWithBitmap((Bitmap)this.mObj1);
                    break;
                }
            }
            if (this.mTintList != null) {
                icon.setTintList(this.mTintList);
            }
            if (this.mTintMode != IconCompat.DEFAULT_TINT_MODE) {
                icon.setTintMode(this.mTintMode);
            }
            return icon;
        }
        return (Icon)this.mObj1;
    }
    
    public String toString() {
        if (this.mType == -1) {
            return String.valueOf(this.mObj1);
        }
        final StringBuilder append = new StringBuilder("Icon(typ=").append(typeToString(this.mType));
        switch (this.mType) {
            case 4: {
                append.append(" uri=");
                append.append(this.mObj1);
                break;
            }
            case 3: {
                append.append(" len=");
                append.append(this.mInt1);
                if (this.mInt2 != 0) {
                    append.append(" off=");
                    append.append(this.mInt2);
                    break;
                }
                break;
            }
            case 2: {
                append.append(" pkg=");
                append.append(this.getResPackage());
                append.append(" id=");
                append.append(String.format("0x%08x", this.getResId()));
                break;
            }
            case 1:
            case 5: {
                append.append(" size=");
                append.append(((Bitmap)this.mObj1).getWidth());
                append.append("x");
                append.append(((Bitmap)this.mObj1).getHeight());
                break;
            }
        }
        if (this.mTintList != null) {
            append.append(" tint=");
            append.append(this.mTintList);
        }
        if (this.mTintMode != IconCompat.DEFAULT_TINT_MODE) {
            append.append(" mode=");
            append.append(this.mTintMode);
        }
        append.append(")");
        return append.toString();
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo$Scope.LIBRARY })
    public @interface IconType {
    }
}
