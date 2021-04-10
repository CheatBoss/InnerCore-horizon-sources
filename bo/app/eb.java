package bo.app;

import com.appboy.support.*;
import org.json.*;
import com.appboy.enums.inappmessage.*;
import com.appboy.models.*;

public final class eb
{
    private static final String a;
    
    static {
        a = AppboyLogger.getAppboyLogTag(eb.class);
    }
    
    public static IInAppMessage a(final String s, final br br) {
        try {
            if (StringUtils.isNullOrBlank(s)) {
                AppboyLogger.i(eb.a, "In-app message string was null or blank. Not de-serializing message.");
                return null;
            }
            return a(new JSONObject(s), br);
        }
        catch (Exception ex) {
            final String a = eb.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to deserialize the in-app message string.");
            sb.append(s);
            AppboyLogger.e(a, sb.toString(), ex);
            return null;
        }
        catch (JSONException ex2) {
            final String a2 = eb.a;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Encountered JSONException processing in-app message string: ");
            sb2.append(s);
            AppboyLogger.w(a2, sb2.toString(), (Throwable)ex2);
            return null;
        }
    }
    
    public static IInAppMessage a(final JSONObject jsonObject, final br br) {
        String a;
        StringBuilder sb;
        String a2;
        StringBuilder sb2;
        MessageType messageType;
        int n;
        String a3;
        StringBuilder sb3;
        Throwable t;
        final JSONException ex;
        Block_8_Outer:Block_7_Outer:Label_0248_Outer:
        while (true) {
            if (jsonObject == null) {
                while (true) {
                    try {
                        AppboyLogger.d(eb.a, "In-app message Json was null. Not deserializing message.");
                        return null;
                        // iftrue(Label_0041:, !a(jsonObject))
                        // iftrue(Label_0097:, messageType != null)
                        // iftrue(Label_0195:, n == 1)
                        // iftrue(Label_0185:, n == 2)
                        // iftrue(Label_0165:, n == 4)
                        while (true) {
                            Block_6: {
                                while (true) {
                                    Block_3: {
                                        break Block_3;
                                        Label_0195: {
                                            return new InAppMessageFull(jsonObject, br);
                                        }
                                        while (true) {
                                            a = eb.a;
                                            sb = new StringBuilder();
                                            sb.append("In-app message type was null. Not deserializing message: ");
                                            sb.append(ec.a(jsonObject));
                                            AppboyLogger.i(a, sb.toString());
                                            return null;
                                            a2 = eb.a;
                                            sb2 = new StringBuilder();
                                            sb2.append("Unknown in-app message type. Not deserializing message: ");
                                            sb2.append(ec.a(jsonObject));
                                            AppboyLogger.e(a2, sb2.toString());
                                            return null;
                                            Label_0041:
                                            messageType = ec.a(jsonObject, "type", MessageType.class, null);
                                            continue Block_8_Outer;
                                        }
                                        Label_0185:
                                        return new InAppMessageModal(jsonObject, br);
                                        Label_0097:
                                        n = eb$1.a[messageType.ordinal()];
                                        break Block_6;
                                    }
                                    AppboyLogger.d(eb.a, "Deserializing control in-app message.");
                                    return new InAppMessageControl(jsonObject, br);
                                    continue Block_7_Outer;
                                }
                                Label_0175: {
                                    return new InAppMessageSlideup(jsonObject, br);
                                }
                                Label_0165:
                                return new InAppMessageHtmlFull(jsonObject, br);
                                a3 = eb.a;
                                sb3 = new StringBuilder();
                                sb3.append("Encountered JSONException processing in-app message: ");
                                sb3.append(ec.a(jsonObject));
                                AppboyLogger.w(a3, sb3.toString(), t);
                                return null;
                            }
                            continue Label_0248_Outer;
                        }
                    }
                    // iftrue(Label_0175:, n == 3)
                    catch (Exception ex2) {}
                    catch (JSONException ex) {}
                    t = (Throwable)ex;
                    continue;
                }
            }
            continue;
        }
    }
    
    static boolean a(final JSONObject jsonObject) {
        return jsonObject.optBoolean("is_control", false);
    }
}
