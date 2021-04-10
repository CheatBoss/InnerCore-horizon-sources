package com.microsoft.xbox.xle.app;

import java.net.*;
import com.microsoft.xboxtcui.*;

public class ImageUtil
{
    public static final int LARGE_PHONE = 640;
    public static final int LARGE_TABLET = 800;
    public static final int MEDIUM_PHONE = 300;
    public static final int MEDIUM_TABLET = 424;
    public static final int SMALL = 200;
    public static final int TINY = 100;
    public static final String resizeFormatter = "&w=%d&h=%d&format=png";
    public static final String resizeFormatterSizeOnly = "&w=%d&h=%d";
    public static final String resizeFormatterWithPadding = "&mode=padding&w=%d&h=%d&format=png";
    
    private static URI createUri(final String s) {
        if (s != null) {
            try {
                return URI.create(s);
            }
            catch (IllegalArgumentException ex) {}
        }
        return null;
    }
    
    private static URI formatString(String s, final int n, final int n2) {
        if (s == null) {
            return null;
        }
        if (s.contains("images-eds")) {
            final boolean contains = s.contains("&w=");
            final boolean contains2 = s.contains("&h=");
            if (contains && contains2) {
                final StringBuilder sb = new StringBuilder();
                sb.append("w=");
                sb.append(n);
                s = s.replaceAll("w=[0-9]+", sb.toString());
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("h=");
                sb2.append(n2);
                s = s.replaceAll("h=[0-9]+", sb2.toString());
            }
            else {
                StringBuilder sb5;
                if (contains) {
                    final StringBuilder sb3 = new StringBuilder();
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("w=");
                    sb4.append(n);
                    sb3.append(s.replaceAll("w=[0-9]+", sb4.toString()));
                    sb3.append("&h=");
                    sb3.append(n2);
                    sb5 = sb3;
                }
                else if (contains2) {
                    final StringBuilder sb6 = new StringBuilder();
                    final StringBuilder sb7 = new StringBuilder();
                    sb7.append("h=");
                    sb7.append(n2);
                    sb6.append(s.replaceAll("h=[0-9]+", sb7.toString()));
                    sb6.append("&w=");
                    sb6.append(n);
                    sb5 = sb6;
                }
                else {
                    String s2;
                    Object[] array;
                    if (s.contains("format=")) {
                        final StringBuilder sb8 = new StringBuilder();
                        sb8.append(s);
                        s2 = "&w=%d&h=%d";
                        array = new Object[] { n, n2 };
                        sb5 = sb8;
                    }
                    else {
                        final StringBuilder sb9 = new StringBuilder();
                        sb9.append(s);
                        s2 = "&w=%d&h=%d&format=png";
                        final Object[] array2 = { n, n2 };
                        sb5 = sb9;
                        array = array2;
                    }
                    sb5.append(String.format(s2, array));
                }
                s = sb5.toString();
            }
            return createUri(s);
        }
        return null;
    }
    
    private static URI formatURI(final URI uri, final int n, final int n2) {
        if (uri == null) {
            return null;
        }
        final URI formatString = formatString(uri.toString(), n, n2);
        if (formatString == null) {
            return uri;
        }
        return formatString;
    }
    
    public static URI getLarge(final String s) {
        int n;
        if (XboxTcuiSdk.getIsTablet()) {
            n = 800;
        }
        else {
            n = 640;
        }
        URI uri2;
        final URI uri = uri2 = formatString(s, n, n);
        if (uri == null) {
            uri2 = uri;
            if (s != null) {
                uri2 = createUri(s);
            }
        }
        return uri2;
    }
    
    public static URI getLarge(final URI uri) {
        int n;
        if (XboxTcuiSdk.getIsTablet()) {
            n = 800;
        }
        else {
            n = 640;
        }
        return formatURI(uri, n, n);
    }
    
    public static URI getLarge3X4(final String s) {
        return formatString(s, 720, 1080);
    }
    
    public static URI getLarge3X4(final URI uri) {
        return formatURI(uri, 720, 1080);
    }
    
    public static URI getMedium(final String s) {
        int n;
        if (XboxTcuiSdk.getIsTablet()) {
            n = 424;
        }
        else {
            n = 300;
        }
        URI uri2;
        final URI uri = uri2 = formatString(s, n, n);
        if (uri == null) {
            uri2 = uri;
            if (s != null) {
                uri2 = createUri(s);
            }
        }
        return uri2;
    }
    
    public static URI getMedium(final URI uri) {
        int n;
        if (XboxTcuiSdk.getIsTablet()) {
            n = 424;
        }
        else {
            n = 300;
        }
        return formatURI(uri, n, n);
    }
    
    public static URI getMedium2X1(final String s) {
        return formatString(s, 480, 270);
    }
    
    public static URI getMedium2X1(final URI uri) {
        return formatURI(uri, 480, 270);
    }
    
    public static URI getMedium3X4(final String s) {
        return formatString(s, 426, 640);
    }
    
    public static URI getMedium3X4(final URI uri) {
        return formatURI(uri, 426, 640);
    }
    
    public static URI getMedium4X3(final String s) {
        return formatString(s, 562, 316);
    }
    
    public static URI getMedium4X3(final URI uri) {
        return formatURI(uri, 562, 316);
    }
    
    public static URI getSmall(final String s) {
        URI uri2;
        final URI uri = uri2 = formatString(s, 200, 200);
        if (uri == null) {
            uri2 = uri;
            if (s != null) {
                uri2 = createUri(s);
            }
        }
        return uri2;
    }
    
    public static URI getSmall(final URI uri) {
        return formatURI(uri, 200, 200);
    }
    
    public static URI getSmall2X1(final String s) {
        return formatString(s, 243, 137);
    }
    
    public static URI getSmall2X1(final URI uri) {
        return formatURI(uri, 243, 137);
    }
    
    public static URI getSmall3X4(final String s) {
        return formatString(s, 215, 294);
    }
    
    public static URI getSmall3X4(final URI uri) {
        return formatURI(uri, 215, 294);
    }
    
    public static URI getSmall4X3(final String s) {
        return formatString(s, 275, 216);
    }
    
    public static URI getSmall4X3(final URI uri) {
        return formatURI(uri, 275, 216);
    }
    
    public static URI getTiny(final String s) {
        URI uri;
        if ((uri = formatString(s, 100, 100)) == null) {
            uri = createUri(s);
        }
        return uri;
    }
    
    public static URI getTiny(final URI uri) {
        return formatURI(uri, 100, 100);
    }
    
    public static URI getTiny2X1(final String s) {
        return formatString(s, 150, 84);
    }
    
    public static URI getTiny2X1(final URI uri) {
        return formatURI(uri, 150, 84);
    }
    
    public static URI getTiny3X4(final String s) {
        return formatString(s, 85, 120);
    }
    
    public static URI getTiny3X4(final URI uri) {
        return formatURI(uri, 85, 120);
    }
    
    public static URI getTiny4X3(final String s) {
        return formatString(s, 120, 90);
    }
    
    public static URI getTiny4X3(final URI uri) {
        return formatURI(uri, 120, 90);
    }
    
    public static URI getURI(final String s, final int n, final int n2) {
        URI uri;
        if ((uri = formatString(s, n, n2)) == null) {
            uri = createUri(s);
        }
        return uri;
    }
    
    public static URI getURI(final URI uri, final int n, final int n2) {
        return formatURI(uri, n, n2);
    }
    
    public static URI getUri(final String s, final ImageType imageType) {
        if (imageType == null) {
            return getSmall(s);
        }
        switch (ImageUtil$1.$SwitchMap$com$microsoft$xbox$xle$app$ImageUtil$ImageType[imageType.ordinal()]) {
            default: {
                return getSmall(s);
            }
            case 11: {
                return getLarge3X4(s);
            }
            case 10: {
                return getLarge(s);
            }
            case 9: {
                return getMedium4X3(s);
            }
            case 8: {
                return getMedium3X4(s);
            }
            case 7: {
                return getMedium(s);
            }
            case 6: {
                return getSmall4X3(s);
            }
            case 5: {
                return getSmall3X4(s);
            }
            case 4: {
                return getSmall(s);
            }
            case 3: {
                return getTiny4X3(s);
            }
            case 2: {
                return getTiny3X4(s);
            }
            case 1: {
                return getTiny(s);
            }
        }
    }
    
    public static URI getUri(final URI uri, final ImageType imageType) {
        if (imageType == null) {
            return getSmall(uri);
        }
        switch (ImageUtil$1.$SwitchMap$com$microsoft$xbox$xle$app$ImageUtil$ImageType[imageType.ordinal()]) {
            default: {
                return getSmall(uri);
            }
            case 11: {
                return getLarge3X4(uri);
            }
            case 10: {
                return getLarge(uri);
            }
            case 9: {
                return getMedium4X3(uri);
            }
            case 8: {
                return getMedium3X4(uri);
            }
            case 7: {
                return getMedium(uri);
            }
            case 6: {
                return getSmall4X3(uri);
            }
            case 5: {
                return getSmall3X4(uri);
            }
            case 4: {
                return getSmall(uri);
            }
            case 3: {
                return getTiny4X3(uri);
            }
            case 2: {
                return getTiny3X4(uri);
            }
            case 1: {
                return getTiny(uri);
            }
        }
    }
    
    public enum ImageType
    {
        LARGE, 
        LARGE_3X4, 
        MEDIUM, 
        MEDIUM_3X4, 
        MEDIUM_4X3, 
        SMALL, 
        SMALL_3X4, 
        SMALL_4X3, 
        TINY, 
        TINY_3X4, 
        TINY_4X3;
        
        public static ImageType fromString(final String s) {
            try {
                return valueOf(s);
            }
            catch (NullPointerException | IllegalArgumentException ex) {
                return null;
            }
        }
    }
}
