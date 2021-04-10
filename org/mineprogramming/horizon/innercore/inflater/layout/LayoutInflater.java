package org.mineprogramming.horizon.innercore.inflater.layout;

import android.content.*;
import org.json.*;
import android.view.*;
import org.mineprogramming.horizon.innercore.inflater.*;

public class LayoutInflater
{
    public static InflatedView inflateLayout(final Context context, final ViewGroup viewGroup, final JSONObject jsonObject) throws JsonInflaterException {
        final InflatedView inflatedView = new InflatedView();
        inflatedView.setView(inflateView(context, viewGroup, inflatedView, jsonObject));
        return inflatedView;
    }
    
    static View inflateView(final Context context, final ViewGroup viewGroup, final InflatedView inflatedView, final JSONObject jsonObject) throws JsonInflaterException {
        final String optString = jsonObject.optString("type");
        Inflatable inflatable;
        if (optString.equals("LinearLayout")) {
            inflatable = new InflatableLinearLayout(context, viewGroup, inflatedView, jsonObject);
        }
        else if (optString.equals("RelativeLayout")) {
            inflatable = new InflatableRelativeLayout(context, viewGroup, inflatedView, jsonObject);
        }
        else if (optString.equals("FrameLayout")) {
            inflatable = new InflatableFrameLayout(context, viewGroup, inflatedView, jsonObject);
        }
        else if (optString.equals("ImageView")) {
            inflatable = new InflatableImageView(context, viewGroup, inflatedView, jsonObject);
        }
        else if (optString.equals("ImageButton")) {
            inflatable = new InflatableImageButton(context, viewGroup, inflatedView, jsonObject);
        }
        else if (optString.equals("TextView")) {
            inflatable = new InflatableTextView(context, viewGroup, inflatedView, jsonObject);
        }
        else if (optString.equals("Button")) {
            inflatable = new InflatableButton(context, viewGroup, inflatedView, jsonObject);
        }
        else if (optString.equals("Switch")) {
            inflatable = new InflatableSwitch(context, viewGroup, inflatedView, jsonObject);
        }
        else if (optString.equals("EditText")) {
            inflatable = new InflatableEditText(context, viewGroup, inflatedView, jsonObject);
        }
        else if (optString.equals("ProgressBar")) {
            inflatable = new InflatableProgressBar(context, viewGroup, inflatedView, jsonObject);
        }
        else if (optString.equals("RecyclerView")) {
            inflatable = new InflatableRecyclerView(context, viewGroup, inflatedView, jsonObject);
        }
        else if (optString.equals("CardView")) {
            inflatable = new InflatableCardView(context, viewGroup, inflatedView, jsonObject);
        }
        else if (optString.equals("ExpandableListView")) {
            inflatable = new InflatableExpandableListView(context, viewGroup, inflatedView, jsonObject);
        }
        else if (optString.equals("ViewPager")) {
            inflatable = new InflatableViewPager(context, viewGroup, inflatedView, jsonObject);
        }
        else if (optString.equals("View")) {
            inflatable = new InflatableView(context, viewGroup, inflatedView, jsonObject);
        }
        else if (optString.equals("Spinner")) {
            inflatable = new InflatableSpinner(context, viewGroup, inflatedView, jsonObject);
        }
        else if (optString.equals("ScrollView")) {
            inflatable = new InflatableScrollView(context, viewGroup, inflatedView, jsonObject);
        }
        else if (optString.equals("UnifiedNativeAdView")) {
            inflatable = new InflatableUnifiedNativeAdView(context, viewGroup, inflatedView, jsonObject);
        }
        else if (optString.equals("MeidaView")) {
            inflatable = new InflatableMediaView(context, viewGroup, inflatedView, jsonObject);
        }
        else {
            if (!optString.equals("include")) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Invalid view type: ");
                sb.append(optString);
                throw new JsonInflaterException(sb.toString());
            }
            inflatable = new InflatableInclude(context, viewGroup, inflatedView, jsonObject);
        }
        return ((InflatableViewBase<View>)inflatable).getView();
    }
}
