package org.mineprogramming.horizon.innercore.inflater.layout;

import android.widget.*;
import android.content.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import android.animation.*;
import android.view.*;

class InflatableButton extends InflatableViewBase<Button>
{
    private Button button;
    
    public InflatableButton(final Context context, final ViewGroup viewGroup, final InflatedView inflatedView, final JSONObject jsonObject) throws JsonInflaterException {
        super(context, viewGroup, inflatedView, jsonObject);
        this.button = new Button(context);
        final String optString = jsonObject.optString("textColor");
        if (!optString.isEmpty()) {
            this.button.setTextColor(this.getColor(optString));
        }
        final String optString2 = jsonObject.optString("text");
        if (!optString2.isEmpty()) {
            this.button.setText((CharSequence)this.getText(optString2));
        }
        if (this.api(21)) {
            final String optString3 = jsonObject.optString("stateListAnimator");
            if (!optString3.isEmpty()) {
                if (!optString3.equals("@null")) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unsupported stateListAnimator: ");
                    sb.append(optString3);
                    throw new JsonInflaterException(sb.toString());
                }
                this.button.setStateListAnimator((StateListAnimator)null);
            }
        }
        this.applyProperties(this.button);
    }
    
    @Override
    public Button getView() {
        return this.button;
    }
}
