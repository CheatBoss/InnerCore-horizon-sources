package org.mineprogramming.horizon.innercore.inflater.layout;

import android.content.*;
import android.*;
import android.widget.*;
import android.view.*;
import android.graphics.drawable.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import org.json.*;

public abstract class InflatableViewBase<T extends View> extends Inflatable
{
    private final String jsonId;
    private final ViewGroup parent;
    private final InflatedView result;
    protected int style;
    
    protected InflatableViewBase(final Context context, final ViewGroup parent, final InflatedView result, final JSONObject jsonObject) throws JsonInflaterException {
        super(context, jsonObject);
        this.style = 0;
        this.parent = parent;
        this.result = result;
        final String optString = jsonObject.optString("style");
        if (!optString.isEmpty() && optString.startsWith("?android:attr/")) {
            final String substring = optString.substring(14);
            try {
                this.style = R$attr.class.getDeclaredField(substring).getInt(null);
            }
            catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ex) {
                final Object o;
                throw new JsonInflaterException("Unable to parse style attribute", (Throwable)o);
            }
        }
        this.jsonId = jsonObject.optString("json_id");
    }
    
    protected void applyProperties(final T t) throws JsonInflaterException {
        if (this.parent != null) {
            this.parent.addView((View)t);
        }
        if (!this.jsonId.isEmpty()) {
            this.result.addJsonId(this.jsonId, t);
        }
        final String optString = this.object.optString("tag");
        if (!optString.isEmpty()) {
            t.setTag((Object)optString);
        }
        final String optString2 = this.object.optString("background");
        if (!optString2.isEmpty()) {
            final Drawable drawable = this.getDrawable(optString2);
            if (drawable != null) {
                t.setBackground(drawable);
            }
            else {
                t.setBackgroundColor(this.getColor(optString2));
            }
        }
        final String optString3 = this.object.optString("textAlignment");
        if (!optString3.isEmpty()) {
            if (optString3.equals("inherit")) {
                t.setTextAlignment(0);
            }
            else if (optString3.equals("gravity")) {
                t.setTextAlignment(1);
            }
            else if (optString3.equals("textStart")) {
                t.setTextAlignment(2);
            }
            else if (optString3.equals("textEnd")) {
                t.setTextAlignment(3);
            }
            else if (optString3.equals("center")) {
                t.setTextAlignment(4);
            }
            else if (optString3.equals("viewStart")) {
                t.setTextAlignment(5);
            }
            else {
                if (!optString3.equals("viewEnd")) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Invalid text alignment type: ");
                    sb.append(optString3);
                    throw new JsonInflaterException(sb.toString());
                }
                t.setTextAlignment(6);
            }
        }
        final String optString4 = this.object.optString("elevation");
        if (!optString4.isEmpty() && this.api(21)) {
            t.setElevation(this.getFloat(optString4));
        }
        final String optString5 = this.object.optString("visibility");
        if (!optString5.isEmpty()) {
            if (optString5.equals("visible")) {
                t.setVisibility(0);
            }
            else if (optString5.equals("invisible")) {
                t.setVisibility(4);
            }
            else if (optString5.equals("gone")) {
                t.setVisibility(8);
            }
        }
        final String optString6 = this.object.optString("clipToPadding");
        if (!optString6.isEmpty()) {
            ((ViewGroup)t).setClipToPadding(this.getBool(optString6));
        }
        final String optString7 = this.object.optString("padding");
        if (!optString7.isEmpty()) {
            final int size = this.getSize(optString7);
            t.setPadding(size, size, size, size);
        }
        final String optString8 = this.object.optString("paddingHorizontal");
        if (!optString8.isEmpty()) {
            final int size2 = this.getSize(optString8);
            t.setPadding(size2, t.getPaddingTop(), size2, t.getPaddingBottom());
        }
        final String optString9 = this.object.optString("paddingVertical");
        if (!optString9.isEmpty()) {
            final int size3 = this.getSize(optString9);
            t.setPadding(t.getPaddingLeft(), size3, t.getPaddingRight(), size3);
        }
        final String optString10 = this.object.optString("paddingLeft");
        if (!optString10.isEmpty()) {
            t.setPadding(this.getSize(optString10), t.getPaddingTop(), t.getPaddingRight(), t.getPaddingBottom());
        }
        final String optString11 = this.object.optString("paddingTop");
        if (!optString11.isEmpty()) {
            t.setPadding(t.getPaddingLeft(), this.getSize(optString11), t.getPaddingRight(), t.getPaddingBottom());
        }
        final String optString12 = this.object.optString("paddingRight");
        if (!optString12.isEmpty()) {
            t.setPadding(t.getPaddingLeft(), t.getPaddingTop(), this.getSize(optString12), t.getPaddingBottom());
        }
        final String optString13 = this.object.optString("paddingBottom");
        if (!optString13.isEmpty()) {
            t.setPadding(t.getPaddingLeft(), t.getPaddingTop(), t.getPaddingRight(), this.getSize(optString13));
        }
        final String optString14 = this.object.optString("paddingStart");
        if (!optString14.isEmpty()) {
            t.setPaddingRelative(this.getSize(optString14), t.getPaddingTop(), t.getPaddingEnd(), t.getPaddingBottom());
        }
        final String optString15 = this.object.optString("paddingEnd");
        if (!optString15.isEmpty()) {
            t.setPaddingRelative(t.getPaddingStart(), t.getPaddingTop(), this.getSize(optString15), t.getPaddingBottom());
        }
        final String optString16 = this.object.optString("minWidth");
        if (!optString16.isEmpty()) {
            t.setMinimumWidth(this.getSize(optString16));
        }
        final String optString17 = this.object.optString("minHeight");
        if (!optString17.isEmpty()) {
            t.setMinimumHeight(this.getSize(optString17));
        }
        final String optString18 = this.object.optString("layout_width");
        final String optString19 = this.object.optString("layout_height");
        ViewGroup$MarginLayoutParams layoutParams = (ViewGroup$MarginLayoutParams)t.getLayoutParams();
        if (layoutParams == null) {
            if (optString18.isEmpty() || optString19.isEmpty()) {
                throw new JsonInflaterException("layout_width or layout_height of the layout is not specified");
            }
            layoutParams = new ViewGroup$MarginLayoutParams(this.getDimension(optString18), this.getDimension(optString19));
        }
        if (!optString18.isEmpty()) {
            layoutParams.width = this.getDimension(optString18);
        }
        if (!optString19.isEmpty()) {
            layoutParams.height = this.getDimension(optString19);
        }
        final String optString20 = this.object.optString("layout_weight");
        if (!optString20.isEmpty()) {
            ((LinearLayout$LayoutParams)layoutParams).weight = this.getFloat(optString20);
        }
        final String optString21 = this.object.optString("layout_margin");
        if (!optString21.isEmpty()) {
            final int size4 = this.getSize(optString21);
            layoutParams.setMargins(size4, size4, size4, size4);
        }
        final String optString22 = this.object.optString("layout_marginTop");
        if (!optString22.isEmpty()) {
            layoutParams.topMargin = this.getSize(optString22);
        }
        final String optString23 = this.object.optString("layout_marginRight");
        if (!optString23.isEmpty()) {
            layoutParams.topMargin = this.getSize(optString23);
        }
        final String optString24 = this.object.optString("layout_marginEnd");
        if (!optString24.isEmpty()) {
            layoutParams.setMarginEnd(this.getSize(optString24));
        }
        final String optString25 = this.object.optString("layout_marginBottom");
        if (!optString25.isEmpty()) {
            layoutParams.bottomMargin = this.getSize(optString25);
        }
        final String optString26 = this.object.optString("layout_marginLeft");
        if (!optString26.isEmpty()) {
            layoutParams.leftMargin = this.getSize(optString26);
        }
        final String optString27 = this.object.optString("layout_marginStart");
        if (!optString27.isEmpty()) {
            layoutParams.setMarginStart(this.getSize(optString27));
        }
        final String optString28 = this.object.optString("layout_gravity");
        if (!optString28.isEmpty()) {
            if (layoutParams instanceof LinearLayout$LayoutParams) {
                ((LinearLayout$LayoutParams)layoutParams).gravity = this.getGravity(optString28);
            }
            else if (layoutParams instanceof FrameLayout$LayoutParams) {
                ((FrameLayout$LayoutParams)layoutParams).gravity = this.getGravity(optString28);
            }
        }
        if (this.object.optString("layout_alignParentTop").equals("true") && layoutParams instanceof RelativeLayout$LayoutParams) {
            ((RelativeLayout$LayoutParams)layoutParams).addRule(10);
        }
        if (this.object.optString("layout_alignParentRight").equals("true") && layoutParams instanceof RelativeLayout$LayoutParams) {
            ((RelativeLayout$LayoutParams)layoutParams).addRule(11);
        }
        if (this.object.optString("layout_alignParentBottom").equals("true") && layoutParams instanceof RelativeLayout$LayoutParams) {
            ((RelativeLayout$LayoutParams)layoutParams).addRule(12);
        }
        if (this.object.optString("layout_alignParentLeft").equals("true") && layoutParams instanceof RelativeLayout$LayoutParams) {
            ((RelativeLayout$LayoutParams)layoutParams).addRule(9);
        }
        t.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
    }
    
    protected int getDimension(final String s) throws JsonInflaterException {
        if (s.equals("match_parent")) {
            return -1;
        }
        if (s.equals("wrap_content")) {
            return -2;
        }
        return this.getSize(s);
    }
    
    protected int getGravity(final String s) {
        int n = 0;
        final String[] split = s.split("\\|");
        int n2;
        for (int length = split.length, i = 0; i < length; ++i, n = n2) {
            final String trim = split[i].trim();
            if (trim.equals("bottom")) {
                n2 = (n | 0x50);
            }
            else if (trim.equals("center")) {
                n2 = (n | 0x11);
            }
            else if (trim.equals("center_horizontal")) {
                n2 = (n | 0x1);
            }
            else if (trim.equals("center_vertical")) {
                n2 = (n | 0x10);
            }
            else if (trim.equals("clip_horizontal")) {
                n2 = (n | 0x8);
            }
            else if (trim.equals("clip_vertical")) {
                n2 = (n | 0x80);
            }
            else if (trim.equals("end")) {
                n2 = (n | 0x800005);
            }
            else if (trim.equals("fill")) {
                n2 = (n | 0x77);
            }
            else if (trim.equals("fill_horizontal")) {
                n2 = (n | 0x7);
            }
            else if (trim.equals("fill_vertical")) {
                n2 = (n | 0x70);
            }
            else if (trim.equals("left")) {
                n2 = (n | 0x3);
            }
            else if (trim.equals("right")) {
                n2 = (n | 0x5);
            }
            else if (trim.equals("start")) {
                n2 = (n | 0x800003);
            }
            else {
                n2 = n;
                if (trim.equals("top")) {
                    n2 = (n | 0x30);
                }
            }
        }
        return n;
    }
    
    protected String getText(String substring) throws JsonInflaterException {
        if (substring.startsWith("@string/")) {
            substring = substring.substring(8);
            return JsonInflater.getString(this.context, substring);
        }
        return substring;
    }
    
    public abstract T getView();
    
    protected void processChildren(final ViewGroup viewGroup) throws JsonInflaterException {
        final JSONArray optJSONArray = this.object.optJSONArray("children");
        int i = 0;
        int length;
        if (optJSONArray == null) {
            length = 0;
        }
        else {
            length = optJSONArray.length();
        }
        while (i < length) {
            try {
                LayoutInflater.inflateView(this.context, viewGroup, this.result, optJSONArray.getJSONObject(i));
                ++i;
                continue;
            }
            catch (JSONException ex) {
                throw new JsonInflaterException("Unable to inflate layer item", (Throwable)ex);
            }
            break;
        }
    }
}
