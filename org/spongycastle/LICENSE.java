package org.spongycastle;

import org.spongycastle.util.*;

public class LICENSE
{
    public static String licenseText;
    
    static {
        final StringBuilder sb = new StringBuilder();
        sb.append("Copyright (c) 2000-2017 The Legion of the Bouncy Castle Inc. (http://www.bouncycastle.org) ");
        sb.append(Strings.lineSeparator());
        sb.append(Strings.lineSeparator());
        sb.append("Permission is hereby granted, free of charge, to any person obtaining a copy of this software ");
        sb.append(Strings.lineSeparator());
        sb.append("and associated documentation files (the \"Software\"), to deal in the Software without restriction, ");
        sb.append(Strings.lineSeparator());
        sb.append("including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, ");
        sb.append(Strings.lineSeparator());
        sb.append("and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,");
        sb.append(Strings.lineSeparator());
        sb.append("subject to the following conditions:");
        sb.append(Strings.lineSeparator());
        sb.append(Strings.lineSeparator());
        sb.append("The above copyright notice and this permission notice shall be included in all copies or substantial");
        sb.append(Strings.lineSeparator());
        sb.append("portions of the Software.");
        sb.append(Strings.lineSeparator());
        sb.append(Strings.lineSeparator());
        sb.append("THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,");
        sb.append(Strings.lineSeparator());
        sb.append("INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR");
        sb.append(Strings.lineSeparator());
        sb.append("PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE");
        sb.append(Strings.lineSeparator());
        sb.append("LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR");
        sb.append(Strings.lineSeparator());
        sb.append("OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER");
        sb.append(Strings.lineSeparator());
        sb.append("DEALINGS IN THE SOFTWARE.");
        LICENSE.licenseText = sb.toString();
    }
    
    public static void main(final String[] array) {
        System.out.println(LICENSE.licenseText);
    }
}
