package androidx.core.content.res;

import android.content.res.*;
import android.graphics.drawable.*;
import android.util.*;
import android.content.*;
import android.graphics.*;
import androidx.annotation.*;
import androidx.core.util.*;
import android.os.*;

public final class ResourcesCompat
{
    private static final String TAG = "ResourcesCompat";
    
    private ResourcesCompat() {
    }
    
    @ColorInt
    public static int getColor(@NonNull final Resources resources, @ColorRes final int n, @Nullable final Resources$Theme resources$Theme) throws Resources$NotFoundException {
        if (Build$VERSION.SDK_INT >= 23) {
            return resources.getColor(n, resources$Theme);
        }
        return resources.getColor(n);
    }
    
    @Nullable
    public static ColorStateList getColorStateList(@NonNull final Resources resources, @ColorRes final int n, @Nullable final Resources$Theme resources$Theme) throws Resources$NotFoundException {
        if (Build$VERSION.SDK_INT >= 23) {
            return resources.getColorStateList(n, resources$Theme);
        }
        return resources.getColorStateList(n);
    }
    
    @Nullable
    public static Drawable getDrawable(@NonNull final Resources resources, @DrawableRes final int n, @Nullable final Resources$Theme resources$Theme) throws Resources$NotFoundException {
        if (Build$VERSION.SDK_INT >= 21) {
            return resources.getDrawable(n, resources$Theme);
        }
        return resources.getDrawable(n);
    }
    
    @Nullable
    public static Drawable getDrawableForDensity(@NonNull final Resources resources, @DrawableRes final int n, final int n2, @Nullable final Resources$Theme resources$Theme) throws Resources$NotFoundException {
        if (Build$VERSION.SDK_INT >= 21) {
            return resources.getDrawableForDensity(n, n2, resources$Theme);
        }
        if (Build$VERSION.SDK_INT >= 15) {
            return resources.getDrawableForDensity(n, n2);
        }
        return resources.getDrawable(n);
    }
    
    public static float getFloat(@NonNull final Resources resources, @DimenRes final int n) {
        final TypedValue typedValue = new TypedValue();
        resources.getValue(n, typedValue, true);
        if (typedValue.type == 4) {
            return typedValue.getFloat();
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Resource ID #0x");
        sb.append(Integer.toHexString(n));
        sb.append(" type #0x");
        sb.append(Integer.toHexString(typedValue.type));
        sb.append(" is not valid");
        throw new Resources$NotFoundException(sb.toString());
    }
    
    @Nullable
    public static Typeface getFont(@NonNull final Context context, @FontRes final int n) throws Resources$NotFoundException {
        if (context.isRestricted()) {
            return null;
        }
        return loadFont(context, n, new TypedValue(), 0, null, null, false);
    }
    
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public static Typeface getFont(@NonNull final Context context, @FontRes final int n, final TypedValue typedValue, final int n2, @Nullable final FontCallback fontCallback) throws Resources$NotFoundException {
        if (context.isRestricted()) {
            return null;
        }
        return loadFont(context, n, typedValue, n2, fontCallback, null, true);
    }
    
    public static void getFont(@NonNull final Context context, @FontRes final int n, @NonNull final FontCallback fontCallback, @Nullable final Handler handler) throws Resources$NotFoundException {
        Preconditions.checkNotNull(fontCallback);
        if (context.isRestricted()) {
            fontCallback.callbackFailAsync(-4, handler);
            return;
        }
        loadFont(context, n, new TypedValue(), 0, fontCallback, handler, false);
    }
    
    private static Typeface loadFont(@NonNull final Context context, final int n, final TypedValue typedValue, final int n2, @Nullable final FontCallback fontCallback, @Nullable final Handler handler, final boolean b) {
        final Resources resources = context.getResources();
        resources.getValue(n, typedValue, true);
        final Typeface loadFont = loadFont(context, resources, typedValue, n, n2, fontCallback, handler, b);
        if (loadFont == null && fontCallback == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Font resource ID #0x");
            sb.append(Integer.toHexString(n));
            sb.append(" could not be retrieved.");
            throw new Resources$NotFoundException(sb.toString());
        }
        return loadFont;
    }
    
    private static Typeface loadFont(@NonNull final Context p0, final Resources p1, final TypedValue p2, final int p3, final int p4, @Nullable final FontCallback p5, @Nullable final Handler p6, final boolean p7) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        android/util/TypedValue.string:Ljava/lang/CharSequence;
        //     4: ifnonnull       73
        //     7: new             Ljava/lang/StringBuilder;
        //    10: dup            
        //    11: invokespecial   java/lang/StringBuilder.<init>:()V
        //    14: astore_0       
        //    15: aload_0        
        //    16: ldc             "Resource \""
        //    18: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    21: pop            
        //    22: aload_0        
        //    23: aload_1        
        //    24: iload_3        
        //    25: invokevirtual   android/content/res/Resources.getResourceName:(I)Ljava/lang/String;
        //    28: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    31: pop            
        //    32: aload_0        
        //    33: ldc             "\" ("
        //    35: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    38: pop            
        //    39: aload_0        
        //    40: iload_3        
        //    41: invokestatic    java/lang/Integer.toHexString:(I)Ljava/lang/String;
        //    44: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    47: pop            
        //    48: aload_0        
        //    49: ldc             ") is not a Font: "
        //    51: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    54: pop            
        //    55: aload_0        
        //    56: aload_2        
        //    57: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //    60: pop            
        //    61: new             Landroid/content/res/Resources$NotFoundException;
        //    64: dup            
        //    65: aload_0        
        //    66: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    69: invokespecial   android/content/res/Resources$NotFoundException.<init>:(Ljava/lang/String;)V
        //    72: athrow         
        //    73: aload_2        
        //    74: getfield        android/util/TypedValue.string:Ljava/lang/CharSequence;
        //    77: invokeinterface java/lang/CharSequence.toString:()Ljava/lang/String;
        //    82: astore_2       
        //    83: aload_2        
        //    84: ldc             "res/"
        //    86: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //    89: ifne            108
        //    92: aload           5
        //    94: ifnull          106
        //    97: aload           5
        //    99: bipush          -3
        //   101: aload           6
        //   103: invokevirtual   androidx/core/content/res/ResourcesCompat$FontCallback.callbackFailAsync:(ILandroid/os/Handler;)V
        //   106: aconst_null    
        //   107: areturn        
        //   108: aload_1        
        //   109: iload_3        
        //   110: iload           4
        //   112: invokestatic    androidx/core/graphics/TypefaceCompat.findFromCache:(Landroid/content/res/Resources;II)Landroid/graphics/Typeface;
        //   115: astore          9
        //   117: aload           9
        //   119: ifnull          139
        //   122: aload           5
        //   124: ifnull          136
        //   127: aload           5
        //   129: aload           9
        //   131: aload           6
        //   133: invokevirtual   androidx/core/content/res/ResourcesCompat$FontCallback.callbackSuccessAsync:(Landroid/graphics/Typeface;Landroid/os/Handler;)V
        //   136: aload           9
        //   138: areturn        
        //   139: aload_2        
        //   140: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
        //   143: ldc             ".xml"
        //   145: invokevirtual   java/lang/String.endsWith:(Ljava/lang/String;)Z
        //   148: istore          8
        //   150: iload           8
        //   152: ifeq            238
        //   155: aload_1        
        //   156: iload_3        
        //   157: invokevirtual   android/content/res/Resources.getXml:(I)Landroid/content/res/XmlResourceParser;
        //   160: aload_1        
        //   161: invokestatic    androidx/core/content/res/FontResourcesParserCompat.parse:(Lorg/xmlpull/v1/XmlPullParser;Landroid/content/res/Resources;)Landroidx/core/content/res/FontResourcesParserCompat$FamilyResourceEntry;
        //   164: astore          9
        //   166: aload           9
        //   168: ifnonnull       203
        //   171: ldc             "ResourcesCompat"
        //   173: ldc             "Failed to find font-family tag"
        //   175: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
        //   178: pop            
        //   179: aload           5
        //   181: ifnull          193
        //   184: aload           5
        //   186: bipush          -3
        //   188: aload           6
        //   190: invokevirtual   androidx/core/content/res/ResourcesCompat$FontCallback.callbackFailAsync:(ILandroid/os/Handler;)V
        //   193: aconst_null    
        //   194: areturn        
        //   195: astore_0       
        //   196: goto            287
        //   199: astore_0       
        //   200: goto            323
        //   203: aload_0        
        //   204: aload           9
        //   206: aload_1        
        //   207: iload_3        
        //   208: iload           4
        //   210: aload           5
        //   212: aload           6
        //   214: iload           7
        //   216: invokestatic    androidx/core/graphics/TypefaceCompat.createFromResourcesFamilyXml:(Landroid/content/Context;Landroidx/core/content/res/FontResourcesParserCompat$FamilyResourceEntry;Landroid/content/res/Resources;IILandroidx/core/content/res/ResourcesCompat$FontCallback;Landroid/os/Handler;Z)Landroid/graphics/Typeface;
        //   219: astore_0       
        //   220: aload_0        
        //   221: areturn        
        //   222: astore_0       
        //   223: goto            279
        //   226: astore_0       
        //   227: goto            283
        //   230: astore_0       
        //   231: goto            287
        //   234: astore_0       
        //   235: goto            323
        //   238: aload_0        
        //   239: aload_1        
        //   240: iload_3        
        //   241: aload_2        
        //   242: iload           4
        //   244: invokestatic    androidx/core/graphics/TypefaceCompat.createFromResourcesFontFile:(Landroid/content/Context;Landroid/content/res/Resources;ILjava/lang/String;I)Landroid/graphics/Typeface;
        //   247: astore_0       
        //   248: aload           5
        //   250: ifnull          276
        //   253: aload_0        
        //   254: ifnull          267
        //   257: aload           5
        //   259: aload_0        
        //   260: aload           6
        //   262: invokevirtual   androidx/core/content/res/ResourcesCompat$FontCallback.callbackSuccessAsync:(Landroid/graphics/Typeface;Landroid/os/Handler;)V
        //   265: aload_0        
        //   266: areturn        
        //   267: aload           5
        //   269: bipush          -3
        //   271: aload           6
        //   273: invokevirtual   androidx/core/content/res/ResourcesCompat$FontCallback.callbackFailAsync:(ILandroid/os/Handler;)V
        //   276: aload_0        
        //   277: areturn        
        //   278: astore_0       
        //   279: goto            287
        //   282: astore_0       
        //   283: goto            323
        //   286: astore_0       
        //   287: new             Ljava/lang/StringBuilder;
        //   290: dup            
        //   291: invokespecial   java/lang/StringBuilder.<init>:()V
        //   294: astore_1       
        //   295: aload_1        
        //   296: ldc             "Failed to read xml resource "
        //   298: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   301: pop            
        //   302: aload_1        
        //   303: aload_2        
        //   304: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   307: pop            
        //   308: ldc             "ResourcesCompat"
        //   310: aload_1        
        //   311: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   314: aload_0        
        //   315: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   318: pop            
        //   319: goto            355
        //   322: astore_0       
        //   323: new             Ljava/lang/StringBuilder;
        //   326: dup            
        //   327: invokespecial   java/lang/StringBuilder.<init>:()V
        //   330: astore_1       
        //   331: aload_1        
        //   332: ldc             "Failed to parse xml resource "
        //   334: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   337: pop            
        //   338: aload_1        
        //   339: aload_2        
        //   340: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   343: pop            
        //   344: ldc             "ResourcesCompat"
        //   346: aload_1        
        //   347: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   350: aload_0        
        //   351: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   354: pop            
        //   355: aload           5
        //   357: ifnull          369
        //   360: aload           5
        //   362: bipush          -3
        //   364: aload           6
        //   366: invokevirtual   androidx/core/content/res/ResourcesCompat$FontCallback.callbackFailAsync:(ILandroid/os/Handler;)V
        //   369: aconst_null    
        //   370: areturn        
        //   371: astore_0       
        //   372: goto            287
        //   375: astore_0       
        //   376: goto            323
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                   
        //  -----  -----  -----  -----  ---------------------------------------
        //  139    150    322    323    Lorg/xmlpull/v1/XmlPullParserException;
        //  139    150    286    287    Ljava/io/IOException;
        //  155    166    234    238    Lorg/xmlpull/v1/XmlPullParserException;
        //  155    166    230    234    Ljava/io/IOException;
        //  171    179    199    203    Lorg/xmlpull/v1/XmlPullParserException;
        //  171    179    195    199    Ljava/io/IOException;
        //  184    193    199    203    Lorg/xmlpull/v1/XmlPullParserException;
        //  184    193    195    199    Ljava/io/IOException;
        //  203    220    226    230    Lorg/xmlpull/v1/XmlPullParserException;
        //  203    220    222    226    Ljava/io/IOException;
        //  238    248    282    283    Lorg/xmlpull/v1/XmlPullParserException;
        //  238    248    278    279    Ljava/io/IOException;
        //  257    265    375    379    Lorg/xmlpull/v1/XmlPullParserException;
        //  257    265    371    375    Ljava/io/IOException;
        //  267    276    375    379    Lorg/xmlpull/v1/XmlPullParserException;
        //  267    276    371    375    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0267:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public abstract static class FontCallback
    {
        @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
        public final void callbackFailAsync(final int n, @Nullable final Handler handler) {
            Handler handler2 = handler;
            if (handler == null) {
                handler2 = new Handler(Looper.getMainLooper());
            }
            handler2.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    FontCallback.this.onFontRetrievalFailed(n);
                }
            });
        }
        
        @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
        public final void callbackSuccessAsync(final Typeface typeface, @Nullable final Handler handler) {
            Handler handler2 = handler;
            if (handler == null) {
                handler2 = new Handler(Looper.getMainLooper());
            }
            handler2.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    FontCallback.this.onFontRetrieved(typeface);
                }
            });
        }
        
        public abstract void onFontRetrievalFailed(final int p0);
        
        public abstract void onFontRetrieved(@NonNull final Typeface p0);
    }
}
