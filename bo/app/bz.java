package bo.app;

import com.appboy.support.*;
import com.appboy.enums.*;
import com.appboy.models.cards.*;
import org.json.*;
import java.util.*;

public final class bz
{
    private static final String a;
    
    static {
        a = AppboyLogger.getAppboyLogTag(bz.class);
    }
    
    private static Card a(final String s, final CardKey.Provider provider, final bl bl, final dn dn, final c c) {
        return a(new JSONObject(s), provider, bl, dn, c);
    }
    
    static Card a(final JSONObject jsonObject, final CardKey.Provider provider, final bl bl, final dn dn, final c c) {
        switch (bz$1.a[provider.getCardTypeFromJson(jsonObject).ordinal()]) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Failed to construct java object from JSON [");
                sb.append(jsonObject.toString());
                sb.append("]");
                throw new JSONException(sb.toString());
            }
            case 6: {
                return new ControlCard(jsonObject, provider, bl, dn, c);
            }
            case 5: {
                return new TextAnnouncementCard(jsonObject, provider, bl, dn, c);
            }
            case 4: {
                return new ShortNewsCard(jsonObject, provider, bl, dn, c);
            }
            case 3: {
                return new CrossPromotionSmallCard(jsonObject, provider, bl, dn, c);
            }
            case 2: {
                return new CaptionedImageCard(jsonObject, provider, bl, dn, c);
            }
            case 1: {
                return new BannerImageCard(jsonObject, provider, bl, dn, c);
            }
        }
    }
    
    public static List<Card> a(final JSONArray jsonArray, final CardKey.Provider provider, final bl bl, final dn dn, final c c) {
        final ArrayList<Card> list = new ArrayList<Card>();
        for (int i = 0; i < jsonArray.length(); ++i) {
            try {
                final Card a = a(jsonArray.optString(i), provider, bl, dn, c);
                if (a != null) {
                    list.add(a);
                }
            }
            catch (Exception ex) {
                final String a2 = bz.a;
                final StringBuilder sb = new StringBuilder();
                sb.append("Unable to create Card JSON in array. Ignoring. Was on element index: ");
                sb.append(i);
                sb.append(" of json array: ");
                sb.append(jsonArray.toString());
                AppboyLogger.e(a2, sb.toString(), ex);
            }
        }
        return list;
    }
}
