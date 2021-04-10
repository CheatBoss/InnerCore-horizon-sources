package com.zhekasmirnov.innercore.api.mod.ui.elements;

import java.util.*;
import com.zhekasmirnov.innercore.api.mod.ui.window.*;
import com.zhekasmirnov.innercore.api.mod.ui.types.*;
import android.graphics.*;
import com.zhekasmirnov.innercore.api.mod.ui.*;
import com.zhekasmirnov.innercore.utils.*;
import com.zhekasmirnov.innercore.api.mod.ui.icon.*;
import com.zhekasmirnov.innercore.api.commontypes.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.*;
import com.zhekasmirnov.innercore.api.mod.ui.container.*;

public class UISlotElement extends UIElement
{
    protected static UISlotElement currentSelectedSlot;
    private static final HashMap<String, Bitmap> dynamicIconCache;
    public Texture background;
    public int curCount;
    public int curData;
    public NativeItemInstanceExtra curExtra;
    public int curId;
    private Font font;
    private Bitmap glintOverlay;
    private final Paint glintOverlayPaint;
    private boolean hadGlint;
    private boolean hasGlint;
    private float iconPadding;
    public boolean isDarken;
    public boolean isDarkenAtZero;
    private boolean isDirty;
    private boolean isExternalSource;
    private boolean isIconAnimated;
    protected boolean isSelected;
    protected boolean isSelectionForced;
    public boolean isVisual;
    public int maxStackSize;
    private int oldCount;
    private int oldData;
    private int oldId;
    public int size;
    public String slotName;
    public UiVisualSlotImpl source;
    public String textOverride;
    protected boolean wasSelected;
    
    static {
        UISlotElement.currentSelectedSlot = null;
        dynamicIconCache = new HashMap<String, Bitmap>();
    }
    
    public UISlotElement(final UIWindow uiWindow, final ScriptableObject scriptableObject) {
        super(uiWindow, scriptableObject);
        this.maxStackSize = -1;
        this.textOverride = null;
        this.oldId = -1;
        this.oldCount = -1;
        this.oldData = -1;
        this.curId = 0;
        this.curCount = 0;
        this.curData = 0;
        this.hadGlint = false;
        this.hasGlint = false;
        this.isIconAnimated = false;
        this.isSelectionForced = false;
        this.wasSelected = false;
        this.isSelected = false;
        this.isExternalSource = false;
        this.isDirty = false;
        this.iconPadding = 0.0f;
        this.glintOverlay = null;
        this.glintOverlayPaint = new Paint();
    }
    
    private void _onTouchEvent(final TouchEvent touchEvent) {
        this.refresh();
        int n = Math.min(this.curCount, 1);
        if (touchEvent.type == TouchEventType.LONG_CLICK) {
            n = this.curCount;
        }
        if (touchEvent.type == TouchEventType.CLICK || touchEvent.type == TouchEventType.LONG_CLICK) {
            if (UISlotElement.currentSelectedSlot == this) {
                if (n > 0 && this.slotName != null) {
                    this.container.handleSlotToInventoryTransaction(this.slotName, n);
                }
            }
            else {
                (UISlotElement.currentSelectedSlot = this).refresh();
            }
        }
    }
    
    private void drawGlintOverlay(final Canvas canvas, final float n) {
        if (this.glintOverlay != null) {
            this.glintOverlayPaint.setAlpha((int)((Math.sin(System.currentTimeMillis() * 0.003) * 0.5 + 0.5) * 255.0));
            canvas.drawBitmap(this.glintOverlay, this.x * n + Math.round(this.iconPadding), this.y * n + Math.round(this.iconPadding), this.glintOverlayPaint);
        }
    }
    
    private void drawSlot(final Canvas canvas, final float n) {
        canvas.drawColor(0, PorterDuff$Mode.CLEAR);
        if (!this.background.isAnimated()) {
            this.background.draw(canvas, 0.0f, 0.0f, n);
        }
        final int n2 = (int)(this.size * n * 0.82 / 16.0) * 16;
        final float iconPadding = (this.size * n - n2) / 2.0f;
        this.iconPadding = iconPadding;
        if (this.curId != 0) {
            final Bitmap setupIcon = this.setupIcon(n2);
            final Paint paint = new Paint();
            if (this.isDarken || (this.isDarkenAtZero && this.curCount < 1)) {
                paint.setColorFilter((ColorFilter)new PorterDuffColorFilter(Color.rgb(90, 90, 90), PorterDuff$Mode.MULTIPLY));
            }
            canvas.drawBitmap(setupIcon, iconPadding, iconPadding, paint);
            setupIcon.recycle();
            String s;
            if (this.textOverride != null) {
                s = this.textOverride;
            }
            else {
                final StringBuilder sb = new StringBuilder();
                sb.append(this.curCount);
                sb.append("");
                s = sb.toString();
            }
            if (this.curCount > 1 || (this.isDarkenAtZero && this.curCount != 1)) {
                if (s.length() > 2) {
                    this.font.drawText(canvas, 0.3f * (this.size * n), 0.83f * (this.size * n), s, n);
                }
                else if (s.length() > 1) {
                    this.font.drawText(canvas, 0.5f * (this.size * n), 0.83f * (this.size * n), s, n);
                }
                else {
                    this.font.drawText(canvas, 0.7f * (this.size * n), 0.83f * (this.size * n), s, n);
                }
            }
        }
        else {
            this.releaseGlintOverlay();
        }
        if (this.isSelected) {
            final Bitmap scaledBitmap = Bitmap.createScaledBitmap(TextureSource.instance.get(this.style.getBitmapName("style:selection")), (int)(this.size * n), (int)(this.size * n), false);
            canvas.drawBitmap(scaledBitmap, 0.0f, 0.0f, (Paint)null);
            scaledBitmap.recycle();
        }
    }
    
    private Bitmap getDynamicIcon() {
        final NativeItemModel forWithFallback = NativeItemModel.getForWithFallback(this.curId, this.curData);
        final NativeItemModel modelForItemInstance = forWithFallback.getModelForItemInstance(this.curId, this.curData, this.curCount, this.curExtra);
        if (forWithFallback != modelForItemInstance && modelForItemInstance != null) {
            return modelForItemInstance.getIconBitmap();
        }
        final String dynamicItemIconOverride = NativeItem.getDynamicItemIconOverride(this.curId, this.curCount, this.curData, this.curExtra);
        if (dynamicItemIconOverride == null) {
            return null;
        }
        if (UISlotElement.dynamicIconCache.containsKey(dynamicItemIconOverride)) {
            return UISlotElement.dynamicIconCache.get(dynamicItemIconOverride);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("resource_packs/vanilla/");
        sb.append(dynamicItemIconOverride);
        final Bitmap assetAsBitmap = FileTools.getAssetAsBitmap(sb.toString());
        UISlotElement.dynamicIconCache.put(dynamicItemIconOverride, assetAsBitmap);
        return assetAsBitmap;
    }
    
    private void releaseGlintOverlay() {
        if (this.glintOverlay != null) {
            this.glintOverlay.recycle();
            this.glintOverlay = null;
        }
    }
    
    private Bitmap setupIcon(final int n) {
        final Bitmap dynamicIcon = this.getDynamicIcon();
        if (ItemIconSource.isGlintAnimationEnabled() && this.hadGlint) {
            this.releaseGlintOverlay();
            this.glintOverlay = ItemIconSource.instance.getScaledIcon(dynamicIcon, this.curId, this.curData, n, 1);
            return ItemIconSource.instance.getScaledIcon(dynamicIcon, this.curId, this.curData, n, 0);
        }
        this.releaseGlintOverlay();
        final ItemIconSource instance = ItemIconSource.instance;
        final int curId = this.curId;
        final int curData = this.curData;
        int n2;
        if (this.hasGlint) {
            n2 = 0;
        }
        else {
            n2 = -1;
        }
        return instance.getScaledIcon(dynamicIcon, curId, curData, n, n2);
    }
    
    public int getMaxItemTransferAmount(final UISlotElement uiSlotElement) {
        if (uiSlotElement.curId != 0 && (uiSlotElement.curId != this.curId || uiSlotElement.curData != this.curData)) {
            return 0;
        }
        if (!uiSlotElement.isValidItem(this.curId, this.curCount, this.curData, this.curExtra)) {
            return 0;
        }
        return Math.min(this.curCount, uiSlotElement.getMaxStackSize() - uiSlotElement.curCount);
    }
    
    public int getMaxStackSize() {
        final int maxStackForId = NativeItem.getMaxStackForId(this.curId, this.curData);
        if (this.maxStackSize >= 0) {
            return Math.min(this.maxStackSize, maxStackForId);
        }
        return maxStackForId;
    }
    
    public boolean isValidItem(final int n, final int n2, final int n3, final NativeItemInstanceExtra nativeItemInstanceExtra) {
        final Object callDescriptionMethodSafe = this.callDescriptionMethodSafe("isValid", n, n2, n3, this.container, new ItemInstance(n, n2, n3, nativeItemInstanceExtra));
        return callDescriptionMethodSafe == null || (boolean)Context.jsToJava(callDescriptionMethodSafe, (Class)Boolean.TYPE);
    }
    
    @Override
    public void onBindingUpdated(final String s, final Object o) {
        if (s.equals("source")) {
            if (o instanceof UiVisualSlotImpl) {
                this.source = (UiVisualSlotImpl)o;
            }
            else {
                this.source = new ScriptableUiVisualSlotImpl((ScriptableObject)o);
            }
        }
        if (s.equals("text")) {
            String string;
            if (o != null) {
                string = o.toString();
            }
            else {
                string = null;
            }
            this.textOverride = string;
        }
        if (s.equals("selection_forced")) {
            this.isSelectionForced = (boolean)o;
        }
    }
    
    @Override
    public void onDraw(final Canvas canvas, final float n) {
        this.refresh();
        if (this.isDirty || this.isIconAnimated) {
            this.isDirty = false;
            this.drawSlot(this.getCacheCanvas((float)this.size, (float)this.size, n), n);
        }
        if (this.background.isAnimated()) {
            this.background.draw(canvas, this.x * n, this.y * n, n);
        }
        this.drawCache(canvas, n);
        this.drawGlintOverlay(canvas, n);
    }
    
    @Override
    public void onRelease() {
        super.onRelease();
        this.background.release();
        this.releaseGlintOverlay();
    }
    
    @Override
    public void onReset() {
        if (UISlotElement.currentSelectedSlot == this) {
            UISlotElement.currentSelectedSlot = null;
        }
    }
    
    @Override
    public void onSetup(final ScriptableObject scriptableObject) {
        this.background = this.createTexture(this.optStringFromDesctiption("bitmap", "style:slot"));
        this.size = (int)this.optFloatFromDesctiption("size", 60.0f);
        this.background.resizeAll((float)this.size, (float)this.size);
        this.setSize((float)this.size, (float)this.size);
        this.maxStackSize = (int)this.optFloatFromDesctiption("maxStackSize", -1.0f);
        boolean isExternalSource = false;
        this.isVisual = this.optBooleanFromDesctiption("visual", false);
        this.isDarken = this.optBooleanFromDesctiption("darken", false);
        this.isDarkenAtZero = this.optBooleanFromDesctiption("isDarkenAtZero", true);
        UiVisualSlotImpl source = null;
        this.textOverride = this.optStringFromDesctiption("text", null);
        final ScriptableObject scriptableObject2 = (ScriptableObject)this.optValFromDescription("source", null);
        if (scriptableObject2 != null) {
            source = new ScriptableUiVisualSlotImpl(scriptableObject2);
        }
        this.source = source;
        if (this.source != null) {
            isExternalSource = true;
        }
        this.isExternalSource = isExternalSource;
        this.font = new Font(-1, this.size * 0.25f, 0.45f);
        this.setBinding("source", this.source);
        this.isDirty = true;
    }
    
    @Override
    public void onTouchEvent(final TouchEvent touchEvent) {
        if (!this.isVisual && (touchEvent.type != TouchEventType.LONG_CLICK || !this.hasDescriptionMethod("onLongClick")) && (touchEvent.type != TouchEventType.CLICK || !this.hasDescriptionMethod("onClick"))) {
            this._onTouchEvent(touchEvent);
            return;
        }
        super.onTouchEvent(touchEvent);
    }
    
    protected void refresh() {
        if (this.source != null) {
            this.curId = this.source.getId();
            this.curCount = this.source.getCount();
            this.curData = this.source.getData();
            this.curExtra = this.source.getExtra();
            this.hasGlint = NativeAPI.isGlintItemInstance(this.curId, this.curData, NativeItemInstanceExtra.getValueOrNullPtr(this.curExtra));
        }
        final int curId = this.curId;
        final int oldId = this.oldId;
        final boolean b = false;
        if (curId != oldId || this.curCount != this.oldCount || this.curData != this.oldData || this.hadGlint != this.hasGlint) {
            this.callDescriptionMethodSafe("onItemChanged", this.container, this.oldId, this.oldCount, this.oldData);
            this.oldId = this.curId;
            this.oldCount = this.curCount;
            this.oldData = this.curData;
            this.hadGlint = this.hasGlint;
            this.isIconAnimated = (NativeItem.isDynamicIconItem(this.curId) || NativeItemModel.getForWithFallback(this.curId, this.curData).isUsingOverrideCallback());
            this.isDirty = true;
        }
        this.isSelected = (this == UISlotElement.currentSelectedSlot || this.isSelectionForced || b);
        if (this.isSelected != this.wasSelected) {
            this.wasSelected = this.isSelected;
            this.isDirty = true;
        }
    }
    
    @Override
    public void setupInitialBindings(final UiAbstractContainer uiAbstractContainer, final String slotName) {
        super.setupInitialBindings(uiAbstractContainer, slotName);
        if (!this.isExternalSource) {
            this.setBinding("source", uiAbstractContainer.getSlotVisualImpl(slotName));
        }
        this.slotName = slotName;
    }
}
