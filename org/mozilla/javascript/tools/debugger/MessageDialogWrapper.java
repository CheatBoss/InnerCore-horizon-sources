package org.mozilla.javascript.tools.debugger;

import java.awt.*;
import javax.swing.*;

class MessageDialogWrapper
{
    public static void showMessageDialog(final Component component, final String s, final String s2, final int n) {
        String string = s;
        if (s.length() > 60) {
            final StringBuilder sb = new StringBuilder();
            final int length = s.length();
            int n2 = 0;
            int n3;
            for (int i = 0; i < length; ++i, n2 = n3 + 1) {
                final char char1 = s.charAt(i);
                sb.append(char1);
                n3 = n2;
                if (Character.isWhitespace(char1)) {
                    int n4;
                    for (n4 = i + 1; n4 < length && !Character.isWhitespace(s.charAt(n4)); ++n4) {}
                    n3 = n2;
                    if (n4 < length) {
                        n3 = n2;
                        if (n2 + (n4 - i) > 60) {
                            sb.append('\n');
                            n3 = 0;
                        }
                    }
                }
            }
            string = sb.toString();
        }
        JOptionPane.showMessageDialog(component, string, s2, n);
    }
}
