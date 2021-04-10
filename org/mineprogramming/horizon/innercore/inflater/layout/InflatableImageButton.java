package org.mineprogramming.horizon.innercore.inflater.layout;

import android.content.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import android.widget.*;
import android.view.*;

class InflatableImageButton extends InflatableViewBase<ImageButton>
{
    private ImageButton imageButton;
    
    public InflatableImageButton(final Context context, final ViewGroup viewGroup, final InflatedView inflatedView, final JSONObject jsonObject) throws JsonInflaterException {
        super(context, viewGroup, inflatedView, jsonObject);
        this.imageButton = new ImageButton(context);
        final String optString = jsonObject.optString("src");
        if (!optString.isEmpty()) {
            this.imageButton.setImageDrawable(this.getDrawable(optString));
        }
        final String optString2 = jsonObject.optString("scaleType");
        if (!optString2.isEmpty()) {
            switch (optString2) {
                default: {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Invalid image scale type: ");
                    sb.append(optString2);
                    throw new JsonInflaterException(sb.toString());
                }
                case "matrix": {
                    this.imageButton.setScaleType(ImageView$ScaleType.MATRIX);
                    break;
                }
                case "fitXY": {
                    this.imageButton.setScaleType(ImageView$ScaleType.FIT_XY);
                    break;
                }
                case "fitStart": {
                    this.imageButton.setScaleType(ImageView$ScaleType.FIT_START);
                    break;
                }
                case "fitEnd": {
                    this.imageButton.setScaleType(ImageView$ScaleType.FIT_END);
                    break;
                }
                case "fitCenter": {
                    this.imageButton.setScaleType(ImageView$ScaleType.FIT_CENTER);
                    break;
                }
                case "centerInside": {
                    this.imageButton.setScaleType(ImageView$ScaleType.CENTER_INSIDE);
                    break;
                }
                case "centerCrop": {
                    this.imageButton.setScaleType(ImageView$ScaleType.CENTER_CROP);
                    break;
                }
                case "center": {
                    this.imageButton.setScaleType(ImageView$ScaleType.CENTER);
                    break;
                }
            }
        }
        this.applyProperties(this.imageButton);
    }
    
    @Override
    public ImageButton getView() {
        return this.imageButton;
    }
}
