package bo.app;

import com.appboy.support.*;
import com.appboy.models.*;
import org.json.*;
import java.util.*;

public final class gb
{
    private static final String a;
    
    static {
        a = AppboyLogger.getAppboyLogTag(gb.class);
    }
    
    public static IInAppMessage a(final JSONObject jsonObject, final br br) {
        while (true) {
            if (jsonObject == null) {
                while (true) {
                    try {
                        AppboyLogger.d(gb.a, "Templated message Json was null. Not de-serializing templated message.");
                        return null;
                        final String string = jsonObject.getString("type");
                        // iftrue(Label_0042:, !string.equals((Object)"inapp"))
                        return eb.a(jsonObject.getJSONObject("data"), br);
                        final String a;
                        Label_0042: {
                            a = gb.a;
                        }
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Received templated message Json with unknown type: ");
                        sb.append(string);
                        sb.append(". Not parsing.");
                        AppboyLogger.w(a, sb.toString());
                        return null;
                        final String a2 = gb.a;
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Encountered JSONException processing templated message: ");
                        sb2.append(jsonObject);
                        final Exception ex;
                        AppboyLogger.w(a2, sb2.toString(), ex);
                        return null;
                    }
                    catch (Exception ex3) {}
                    catch (JSONException ex2) {}
                    final JSONException ex2;
                    final Exception ex = (Exception)ex2;
                    continue;
                }
            }
            continue;
        }
    }
    
    public static List<er> a(final JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }
        final ArrayList<er> list = new ArrayList<er>();
        for (int i = 0; i < jsonArray.length(); ++i) {
            final JSONObject optJSONObject = jsonArray.optJSONObject(i);
            String s = null;
            String string2 = null;
            Label_0042: {
                if (optJSONObject != null) {
                    final String string = optJSONObject.getString("type");
                    er er = null;
                    Label_0076: {
                        if (string.equals("purchase")) {
                            er = new ev(optJSONObject);
                        }
                        else if (string.equals("custom_event")) {
                            er = new eo(optJSONObject);
                        }
                        else {
                            if (!string.equals("push_click")) {
                                er er2;
                                if (string.equals("open")) {
                                    er2 = new et();
                                }
                                else {
                                    if (string.equals("iam_click")) {
                                        er = new es(optJSONObject);
                                        break Label_0076;
                                    }
                                    if (string.equals("test")) {
                                        er2 = new ey();
                                    }
                                    else {
                                        if (string.equals("custom_event_property")) {
                                            er = new ep(optJSONObject);
                                            break Label_0076;
                                        }
                                        if (string.equals("purchase_property")) {
                                            er = new ew(optJSONObject);
                                            break Label_0076;
                                        }
                                        s = gb.a;
                                        final StringBuilder sb = new StringBuilder();
                                        sb.append("Received triggered condition Json with unknown type: ");
                                        sb.append(string);
                                        sb.append(". Not parsing.");
                                        string2 = sb.toString();
                                        break Label_0042;
                                    }
                                }
                                list.add(er2);
                                continue;
                            }
                            er = new ex(optJSONObject);
                        }
                    }
                    list.add(er);
                    continue;
                }
                s = gb.a;
                string2 = "Received null or blank trigger condition Json. Not parsing.";
            }
            AppboyLogger.w(s, string2);
        }
        return list;
    }
    
    public static List<ek> a(final JSONArray jsonArray, final br br) {
    Label_0109_Outer:
        while (true) {
            if (jsonArray == null) {
                while (true) {
                    try {
                        AppboyLogger.d(gb.a, "Triggered actions Json array was null. Not de-serializing triggered actions.");
                        return null;
                        while (true) {
                            final ArrayList<ek> list;
                            final ek b;
                            list.add(b);
                            int n = 0;
                            Label_0025: {
                                Label_0058: {
                                    break Label_0058;
                                    Label_0065: {
                                        return list;
                                    }
                                    list = new ArrayList<ek>();
                                    n = 0;
                                    break Label_0025;
                                }
                                ++n;
                                break Label_0025;
                                final String a = gb.a;
                                final StringBuilder sb = new StringBuilder();
                                sb.append("Encountered JSONException processing triggered actions Json array: ");
                                sb.append(jsonArray);
                                final Exception ex;
                                AppboyLogger.w(a, sb.toString(), ex);
                                return null;
                            }
                            b = b(jsonArray.getJSONObject(n), br);
                            continue Label_0109_Outer;
                        }
                    }
                    // iftrue(Label_0065:, n >= jsonArray.length())
                    // iftrue(Label_0058:, b == null)
                    catch (Exception ex3) {}
                    catch (JSONException ex2) {}
                    final JSONException ex2;
                    final Exception ex = (Exception)ex2;
                    continue;
                }
            }
            continue;
        }
    }
    
    public static ek b(final JSONObject jsonObject, final br br) {
        try {
            final String string = jsonObject.getString("type");
            if (string.equals("inapp")) {
                return new el(jsonObject, br);
            }
            if (string.equals("templated_iam")) {
                return new em(jsonObject, br);
            }
            final String a = gb.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Received unknown trigger type: ");
            sb.append(string);
            AppboyLogger.i(a, sb.toString());
            return null;
        }
        catch (Exception o) {
            final String s = gb.a;
            final StringBuilder sb2 = new StringBuilder();
        }
        catch (JSONException o) {
            final String s = gb.a;
            final StringBuilder sb2 = new StringBuilder();
            goto Label_0098;
        }
    }
}
