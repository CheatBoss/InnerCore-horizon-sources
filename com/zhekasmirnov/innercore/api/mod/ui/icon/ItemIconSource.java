package com.zhekasmirnov.innercore.api.mod.ui.icon;

import com.zhekasmirnov.innercore.api.mod.ui.*;
import com.zhekasmirnov.innercore.ui.*;
import java.util.*;
import com.zhekasmirnov.innercore.utils.*;
import com.zhekasmirnov.innercore.api.*;
import android.graphics.*;

public class ItemIconSource
{
    public static final ItemIconSource instance;
    private HashMap<String, Bitmap> cachedIcons;
    private final Bitmap[] glintOverlays;
    private final Paint glintPaintAdd;
    private final Paint glintPaintCut;
    private HashMap<String, String> iconNameOverrides;
    private boolean isGlintAnimationEnabled;
    private HashMap<String, Bitmap> loadedIcons;
    
    static {
        instance = new ItemIconSource();
        ItemIconLoader.load();
    }
    
    private ItemIconSource() {
        this.isGlintAnimationEnabled = true;
        this.glintOverlays = new Bitmap[2];
        this.loadedIcons = new HashMap<String, Bitmap>();
        this.iconNameOverrides = new HashMap<String, String>();
        this.cachedIcons = new HashMap<String, Bitmap>();
        (this.glintPaintCut = new Paint()).setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff$Mode.SRC_ATOP));
        (this.glintPaintAdd = new Paint()).setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff$Mode.LIGHTEN));
        for (int i = 0; i < this.glintOverlays.length; ++i) {
            final Bitmap[] glintOverlays = this.glintOverlays;
            final TextureSource instance = TextureSource.instance;
            final StringBuilder sb = new StringBuilder();
            sb.append("_glint_overlay");
            sb.append(i);
            glintOverlays[i] = instance.getSafe(sb.toString());
        }
    }
    
    public static void generateAllModItemModels() {
        final Collection<NativeItemModel> allModels = NativeItemModel.getAllModels();
        int n = 0;
        long n2 = 0L;
        for (final NativeItemModel nativeItemModel : new ArrayList<NativeItemModel>(allModels)) {
            ++n;
            long currentTimeMillis = n2;
            if (nativeItemModel != null) {
                currentTimeMillis = n2;
                if (!nativeItemModel.isLazyLoading) {
                    currentTimeMillis = n2;
                    if (n2 + 100L < System.currentTimeMillis()) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Generating item models: ");
                        sb.append(n);
                        sb.append("/");
                        sb.append(allModels.size());
                        LoadingUI.setText(sb.toString());
                        currentTimeMillis = System.currentTimeMillis();
                    }
                    nativeItemModel.reloadIcon(true);
                }
            }
            n2 = currentTimeMillis;
        }
    }
    
    private Bitmap getIconNoDamageBar(final int n, final int n2) {
        final Bitmap nullableIcon = this.getNullableIcon(n, n2);
        if (nullableIcon != null) {
            return nullableIcon;
        }
        return TextureSource.instance.getSafe("missing_icon");
    }
    
    private String getOverride(final String s) {
        if (this.iconNameOverrides.containsKey(s)) {
            return this.iconNameOverrides.get(s);
        }
        return s;
    }
    
    public static void init() {
    }
    
    public static boolean isGlintAnimationEnabled() {
        return ItemIconSource.instance.isGlintAnimationEnabled;
    }
    
    public Bitmap checkoutIcon(String string) {
        final String override = this.getOverride(string);
        if (this.loadedIcons.containsKey(override)) {
            return this.loadedIcons.get(override);
        }
        final Bitmap bitmap = null;
        Object assetAsBitmap = override;
        Object o;
        try {
            if (override.equals(string)) {
                assetAsBitmap = override;
                final StringBuilder sb = new StringBuilder();
                assetAsBitmap = override;
                sb.append("resource_packs/vanilla/");
                assetAsBitmap = override;
                sb.append(override);
                assetAsBitmap = override;
                final Bitmap assetAsBitmap2 = FileTools.getAssetAsBitmap(sb.toString());
                string = override;
                assetAsBitmap = assetAsBitmap2;
            }
            else {
                string = override;
                assetAsBitmap = override;
                if (!override.endsWith(".png")) {
                    assetAsBitmap = override;
                    final StringBuilder sb2 = new StringBuilder();
                    assetAsBitmap = override;
                    sb2.append(override);
                    assetAsBitmap = override;
                    sb2.append(".png");
                    assetAsBitmap = override;
                    string = sb2.toString();
                }
                assetAsBitmap = string;
                final StringBuilder sb3 = new StringBuilder();
                assetAsBitmap = string;
                sb3.append("resource_packs/vanilla/");
                assetAsBitmap = string;
                sb3.append(string);
                assetAsBitmap = string;
                assetAsBitmap = FileTools.getAssetAsBitmap(sb3.toString());
            }
            o = assetAsBitmap;
        }
        catch (Exception ex) {
            o = bitmap;
            string = (String)assetAsBitmap;
        }
        this.loadedIcons.put(string, (Bitmap)o);
        return (Bitmap)o;
    }
    
    public Bitmap getIcon(final int n, int round, final Bitmap bitmap, final boolean b) {
        final int maxDamageForId = NativeItem.getMaxDamageForId(n, 0);
        if (round <= 0 || maxDamageForId <= 0) {
            return bitmap;
        }
        final float n2 = (maxDamageForId - round) / (float)maxDamageForId;
        round = Math.round(Math.round(n2 * 16.0f) * bitmap.getWidth() / 16.0f);
        final int width = bitmap.getWidth();
        final int n3 = bitmap.getHeight() / 16;
        final StringBuilder sb = new StringBuilder();
        sb.append(n);
        sb.append("$dmg");
        sb.append(round);
        final String string = sb.toString();
        Bitmap bitmap2;
        if (b) {
            bitmap2 = this.cachedIcons.get(string);
        }
        else {
            bitmap2 = null;
        }
        if (bitmap2 != null) {
            return bitmap2;
        }
        final Bitmap bitmap3 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap$Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap3);
        final Paint paint = new Paint();
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint)null);
        paint.setColor(Color.rgb(0, 0, 0));
        canvas.drawRect(new Rect(0, n3 * 14, width, n3 * 15), paint);
        paint.setColor(Color.rgb(Math.round((1.0f - n2) * 255.0f), Math.round(255.0f * n2), 0));
        canvas.drawRect(new Rect(0, n3 * 14, round, n3 * 15), paint);
        this.cachedIcons.put(string, bitmap3);
        return bitmap3;
    }
    
    public String getIconName(final int n, final int n2) {
        final StringBuilder sb = new StringBuilder();
        sb.append(n);
        sb.append("_");
        sb.append(n2);
        final String string = sb.toString();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(n);
        sb2.append("");
        final String string2 = sb2.toString();
        if (this.checkoutIcon(string) != null) {
            return string;
        }
        if (this.checkoutIcon(string2) != null) {
            return string2;
        }
        return null;
    }
    
    public String getIconPath(final int n, final int n2) {
        final String iconName = this.getIconName(n, n2);
        if (this.iconNameOverrides.containsKey(iconName)) {
            return this.iconNameOverrides.get(iconName);
        }
        return iconName;
    }
    
    public Bitmap getNullableIcon(final int n, final int n2) {
        return NativeItemModel.getForWithFallback(n, n2).getIconBitmap();
    }
    
    public Bitmap getScaledIcon(final int n, final int n2, final int n3) {
        return this.getScaledIcon(null, n, n2, n3, -1);
    }
    
    public Bitmap getScaledIcon(Bitmap copy, final int n, final int n2, final int n3, final int n4) {
        int n5 = n2;
        if (n2 == -1) {
            n5 = 0;
        }
        Bitmap iconNoDamageBar;
        if (copy != null) {
            iconNoDamageBar = copy;
        }
        else {
            iconNoDamageBar = this.getIconNoDamageBar(n, n5);
        }
        final Bitmap icon = this.getIcon(n, n5, iconNoDamageBar, copy == null);
        final Bitmap scaledBitmap = Bitmap.createScaledBitmap(icon, n3, n3, false);
        if (n4 != -1 || (copy = scaledBitmap) == icon) {
            copy = scaledBitmap.copy(scaledBitmap.getConfig(), true);
        }
        if (n4 != -1) {
            final Canvas canvas = new Canvas(copy);
            canvas.drawBitmap(this.glintOverlays[n4], (Rect)null, new Rect(0, 0, n3, n3), this.glintPaintCut);
            canvas.drawBitmap(scaledBitmap, 0.0f, 0.0f, this.glintPaintAdd);
            if (scaledBitmap != icon) {
                scaledBitmap.recycle();
            }
        }
        return copy;
    }
    
    public void registerIcon(final int n, final int n2, final Bitmap bitmap) {
        final StringBuilder sb = new StringBuilder();
        sb.append(n);
        String string;
        if (n2 == -1) {
            string = "";
        }
        else {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("_");
            sb2.append(n2);
            string = sb2.toString();
        }
        sb.append(string);
        final String string2 = sb.toString();
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(n);
        sb3.append("");
        final String string3 = sb3.toString();
        if (!string2.equals(string3) && !this.loadedIcons.containsKey(string3)) {
            this.loadedIcons.put(string3, bitmap);
        }
        this.loadedIcons.put(string2, bitmap);
    }
    
    public void registerIcon(final int n, final int n2, final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append(n);
        String string;
        if (n2 == -1) {
            string = "";
        }
        else {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("_");
            sb2.append(n2);
            string = sb2.toString();
        }
        sb.append(string);
        final String string2 = sb.toString();
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(n);
        sb3.append("");
        final String string3 = sb3.toString();
        if (!string2.equals(string3) && !this.iconNameOverrides.containsKey(string3)) {
            this.iconNameOverrides.put(string3, s);
        }
        this.iconNameOverrides.put(string2, s);
    }
    
    public void registerIcon(final int n, final Bitmap bitmap) {
        this.registerIcon(n, -1, bitmap);
    }
    
    public void registerIcon(final int n, final String s) {
        this.registerIcon(n, -1, s);
    }
}
