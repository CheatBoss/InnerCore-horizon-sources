package androidx.core.graphics;

import java.util.*;
import androidx.annotation.*;
import android.graphics.*;
import android.util.*;

public class PathParser
{
    private static final String LOGTAG = "PathParser";
    
    private PathParser() {
    }
    
    private static void addNode(final ArrayList<PathDataNode> list, final char c, final float[] array) {
        list.add(new PathDataNode(c, array));
    }
    
    public static boolean canMorph(@Nullable final PathDataNode[] array, @Nullable final PathDataNode[] array2) {
        if (array == null) {
            return false;
        }
        if (array2 == null) {
            return false;
        }
        if (array.length != array2.length) {
            return false;
        }
        for (int i = 0; i < array.length; ++i) {
            if (array[i].mType != array2[i].mType) {
                return false;
            }
            if (array[i].mParams.length != array2[i].mParams.length) {
                return false;
            }
        }
        return true;
    }
    
    static float[] copyOfRange(final float[] array, final int n, int n2) {
        if (n > n2) {
            throw new IllegalArgumentException();
        }
        final int length = array.length;
        if (n >= 0 && n <= length) {
            n2 -= n;
            final int min = Math.min(n2, length - n);
            final float[] array2 = new float[n2];
            System.arraycopy(array, n, array2, 0, min);
            return array2;
        }
        throw new ArrayIndexOutOfBoundsException();
    }
    
    public static PathDataNode[] createNodesFromPathData(final String s) {
        if (s == null) {
            return null;
        }
        int n = 0;
        int i = 1;
        final ArrayList<PathDataNode> list = new ArrayList<PathDataNode>();
        while (i < s.length()) {
            final int nextStart = nextStart(s, i);
            final String trim = s.substring(n, nextStart).trim();
            if (trim.length() > 0) {
                addNode(list, trim.charAt(0), getFloats(trim));
            }
            n = nextStart;
            i = nextStart + 1;
        }
        if (i - n == 1 && n < s.length()) {
            addNode(list, s.charAt(n), new float[0]);
        }
        return list.toArray(new PathDataNode[list.size()]);
    }
    
    public static Path createPathFromPathData(final String s) {
        final Path path = new Path();
        final PathDataNode[] nodesFromPathData = createNodesFromPathData(s);
        if (nodesFromPathData != null) {
            try {
                PathDataNode.nodesToPath(nodesFromPathData, path);
                return path;
            }
            catch (RuntimeException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Error in parsing ");
                sb.append(s);
                throw new RuntimeException(sb.toString(), ex);
            }
        }
        return null;
    }
    
    public static PathDataNode[] deepCopyNodes(final PathDataNode[] array) {
        if (array == null) {
            return null;
        }
        final PathDataNode[] array2 = new PathDataNode[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = new PathDataNode(array[i]);
        }
        return array2;
    }
    
    private static void extract(final String s, final int n, final ExtractFloatResult extractFloatResult) {
        int i = n;
        int n2 = 0;
        int n3 = 0;
        extractFloatResult.mEndWithNegOrDot = false;
        int n4 = 0;
        while (i < s.length()) {
            final boolean b = false;
            final char char1 = s.charAt(i);
            int n5 = 0;
            int n6 = 0;
            int n7 = 0;
            Label_0204: {
                if (char1 != ' ') {
                    if (char1 == 'E' || char1 == 'e') {
                        n5 = 1;
                        n6 = n2;
                        n7 = n4;
                        break Label_0204;
                    }
                    switch (char1) {
                        default: {
                            n6 = n2;
                            n5 = (b ? 1 : 0);
                            n7 = n4;
                            break Label_0204;
                        }
                        case 46: {
                            if (n4 == 0) {
                                n7 = 1;
                                n6 = n2;
                                n5 = (b ? 1 : 0);
                                break Label_0204;
                            }
                            n6 = 1;
                            extractFloatResult.mEndWithNegOrDot = true;
                            n5 = (b ? 1 : 0);
                            n7 = n4;
                            break Label_0204;
                        }
                        case 45: {
                            n6 = n2;
                            n5 = (b ? 1 : 0);
                            n7 = n4;
                            if (i == n) {
                                break Label_0204;
                            }
                            n6 = n2;
                            n5 = (b ? 1 : 0);
                            n7 = n4;
                            if (n3 == 0) {
                                n6 = 1;
                                extractFloatResult.mEndWithNegOrDot = true;
                                n5 = (b ? 1 : 0);
                                n7 = n4;
                            }
                            break Label_0204;
                        }
                        case 44: {
                            break;
                        }
                    }
                }
                n6 = 1;
                n7 = n4;
                n5 = (b ? 1 : 0);
            }
            if (n6 != 0) {
                break;
            }
            ++i;
            n2 = n6;
            n3 = n5;
            n4 = n7;
        }
        extractFloatResult.mEndPosition = i;
    }
    
    private static float[] getFloats(final String s) {
        if (s.charAt(0) != 'z') {
            if (s.charAt(0) != 'Z') {
                while (true) {
                    while (true) {
                        int mEndPosition = 0;
                        int n2 = 0;
                        Label_0177: {
                            try {
                                final float[] array = new float[s.length()];
                                int n = 0;
                                int i = 1;
                                final ExtractFloatResult extractFloatResult = new ExtractFloatResult();
                                final int length = s.length();
                                while (i < length) {
                                    extract(s, i, extractFloatResult);
                                    mEndPosition = extractFloatResult.mEndPosition;
                                    n2 = n;
                                    if (i < mEndPosition) {
                                        array[n] = Float.parseFloat(s.substring(i, mEndPosition));
                                        n2 = n + 1;
                                    }
                                    if (!extractFloatResult.mEndWithNegOrDot) {
                                        break Label_0177;
                                    }
                                    i = mEndPosition;
                                    n = n2;
                                }
                                return copyOfRange(array, 0, n);
                            }
                            catch (NumberFormatException ex) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("error in parsing \"");
                                sb.append(s);
                                sb.append("\"");
                                throw new RuntimeException(sb.toString(), ex);
                            }
                            break;
                        }
                        int i = mEndPosition + 1;
                        int n = n2;
                        continue;
                    }
                }
            }
        }
        return new float[0];
    }
    
    public static boolean interpolatePathDataNodes(final PathDataNode[] array, final PathDataNode[] array2, final PathDataNode[] array3, final float n) {
        if (array == null || array2 == null || array3 == null) {
            throw new IllegalArgumentException("The nodes to be interpolated and resulting nodes cannot be null");
        }
        if (array.length != array2.length || array2.length != array3.length) {
            throw new IllegalArgumentException("The nodes to be interpolated and resulting nodes must have the same length");
        }
        final boolean canMorph = canMorph(array2, array3);
        int i = 0;
        if (!canMorph) {
            return false;
        }
        while (i < array.length) {
            array[i].interpolatePathDataNode(array2[i], array3[i], n);
            ++i;
        }
        return true;
    }
    
    private static int nextStart(final String s, int i) {
        while (i < s.length()) {
            final char char1 = s.charAt(i);
            if (((char1 - 'A') * (char1 - 'Z') <= 0 || (char1 - 'a') * (char1 - 'z') <= 0) && char1 != 'e' && char1 != 'E') {
                return i;
            }
            ++i;
        }
        return i;
    }
    
    public static void updateNodes(final PathDataNode[] array, final PathDataNode[] array2) {
        for (int i = 0; i < array2.length; ++i) {
            array[i].mType = array2[i].mType;
            for (int j = 0; j < array2[i].mParams.length; ++j) {
                array[i].mParams[j] = array2[i].mParams[j];
            }
        }
    }
    
    private static class ExtractFloatResult
    {
        int mEndPosition;
        boolean mEndWithNegOrDot;
        
        ExtractFloatResult() {
        }
    }
    
    public static class PathDataNode
    {
        public float[] mParams;
        public char mType;
        
        PathDataNode(final char mType, final float[] mParams) {
            this.mType = mType;
            this.mParams = mParams;
        }
        
        PathDataNode(final PathDataNode pathDataNode) {
            this.mType = pathDataNode.mType;
            this.mParams = PathParser.copyOfRange(pathDataNode.mParams, 0, pathDataNode.mParams.length);
        }
        
        private static void addCommand(final Path path, final float[] array, final char c, final char c2, final float[] array2) {
            int n = 2;
            float n2 = array[0];
            float n3 = array[1];
            float n4 = array[2];
            float n5 = array[3];
            final float n6 = array[4];
            final float n7 = array[5];
            switch (c2) {
                case 'Z':
                case 'z': {
                    path.close();
                    n2 = n6;
                    n3 = n7;
                    n4 = n6;
                    n5 = n7;
                    path.moveTo(n2, n3);
                    break;
                }
                case 'Q':
                case 'S':
                case 'q':
                case 's': {
                    n = 4;
                    break;
                }
                case 'L':
                case 'M':
                case 'T':
                case 'l':
                case 'm':
                case 't': {
                    n = 2;
                    break;
                }
                case 'H':
                case 'V':
                case 'h':
                case 'v': {
                    n = 1;
                    break;
                }
                case 'C':
                case 'c': {
                    n = 6;
                    break;
                }
                case 'A':
                case 'a': {
                    n = 7;
                    break;
                }
            }
            final float n8 = n2;
            final float n9 = n3;
            float n10 = n4;
            float n11 = n5;
            final int n12 = 0;
            float n13 = n7;
            float n14 = n6;
            float n15 = n8;
            float n16 = n9;
            char c3 = c;
            for (int i = n12; i < array2.length; i += n) {
                Label_2021: {
                    float n36 = 0.0f;
                    float n37 = 0.0f;
                    float n38 = 0.0f;
                    float n39 = 0.0f;
                    Label_1851: {
                        switch (c2) {
                            case 'v': {
                                path.rLineTo(0.0f, array2[i + 0]);
                                n16 += array2[i + 0];
                                break;
                            }
                            case 't': {
                                float n17 = 0.0f;
                                float n18 = 0.0f;
                                if (c3 == 'q' || c3 == 't' || c3 == 'Q' || c3 == 'T') {
                                    n17 = n15 - n10;
                                    n18 = n16 - n11;
                                }
                                path.rQuadTo(n17, n18, array2[i + 0], array2[i + 1]);
                                final float n19 = n15 + array2[i + 0];
                                final float n20 = n16 + array2[i + 1];
                                final float n21 = n15 + n17;
                                final float n22 = n16 + n18;
                                n16 = n20;
                                n15 = n19;
                                n10 = n21;
                                n11 = n22;
                                break;
                            }
                            case 's': {
                                float n23;
                                float n24;
                                if (c3 != 'c' && c3 != 's' && c3 != 'C' && c3 != 'S') {
                                    n23 = 0.0f;
                                    n24 = 0.0f;
                                }
                                else {
                                    n23 = n15 - n10;
                                    n24 = n16 - n11;
                                }
                                final float n25 = array2[i + 0];
                                final float n26 = array2[i + 1];
                                final float n27 = array2[i + 2];
                                final float n28 = array2[i + 3];
                                final int n29 = i;
                                path.rCubicTo(n23, n24, n25, n26, n27, n28);
                                n10 = array2[n29 + 0] + n15;
                                n11 = array2[n29 + 1] + n16;
                                n15 += array2[n29 + 2];
                                n16 += array2[n29 + 3];
                                break;
                            }
                            case 'q': {
                                final int n30 = i;
                                path.rQuadTo(array2[n30 + 0], array2[n30 + 1], array2[n30 + 2], array2[n30 + 3]);
                                n10 = array2[n30 + 0] + n15;
                                n11 = array2[n30 + 1] + n16;
                                n15 += array2[n30 + 2];
                                n16 += array2[n30 + 3];
                                break;
                            }
                            case 'm': {
                                final int n31 = i;
                                n15 += array2[n31 + 0];
                                n16 += array2[n31 + 1];
                                if (n31 > 0) {
                                    path.rLineTo(array2[n31 + 0], array2[n31 + 1]);
                                    break;
                                }
                                path.rMoveTo(array2[n31 + 0], array2[n31 + 1]);
                                n14 = n15;
                                n13 = n16;
                                break;
                            }
                            case 'l': {
                                final int n32 = i;
                                path.rLineTo(array2[n32 + 0], array2[n32 + 1]);
                                n15 += array2[n32 + 0];
                                n16 += array2[n32 + 1];
                                break;
                            }
                            case 'h': {
                                final int n33 = i;
                                path.rLineTo(array2[n33 + 0], 0.0f);
                                n15 += array2[n33 + 0];
                                break;
                            }
                            case 'c': {
                                final int n34 = i;
                                path.rCubicTo(array2[n34 + 0], array2[n34 + 1], array2[n34 + 2], array2[n34 + 3], array2[n34 + 4], array2[n34 + 5]);
                                n10 = array2[n34 + 2] + n15;
                                n11 = array2[n34 + 3] + n16;
                                n15 += array2[n34 + 4];
                                n16 += array2[n34 + 5];
                                break;
                            }
                            case 'a': {
                                final int n35 = i;
                                drawArc(path, n15, n16, array2[n35 + 5] + n15, array2[n35 + 6] + n16, array2[n35 + 0], array2[n35 + 1], array2[n35 + 2], array2[n35 + 3] != 0.0f, array2[n35 + 4] != 0.0f);
                                n36 = n15 + array2[n35 + 5];
                                n37 = n16 + array2[n35 + 6];
                                n38 = n36;
                                n39 = n37;
                                break Label_1851;
                            }
                            case 'V': {
                                final int n40 = i;
                                path.lineTo(n15, array2[n40 + 0]);
                                n16 = array2[n40 + 0];
                                break;
                            }
                            case 'T': {
                                final int n41 = i;
                                float n42 = n15;
                                float n43 = n16;
                                if (c3 == 'q' || c3 == 't' || c3 == 'Q' || c3 == 'T') {
                                    n42 = n15 * 2.0f - n10;
                                    n43 = n16 * 2.0f - n11;
                                }
                                path.quadTo(n42, n43, array2[n41 + 0], array2[n41 + 1]);
                                n15 = array2[n41 + 0];
                                n16 = array2[n41 + 1];
                                n10 = n42;
                                n11 = n43;
                                break;
                            }
                            case 'S': {
                                final int n44 = i;
                                if (c3 == 'c' || c3 == 's' || c3 == 'C' || c3 == 'S') {
                                    n15 = n15 * 2.0f - n10;
                                    n16 = n16 * 2.0f - n11;
                                }
                                path.cubicTo(n15, n16, array2[n44 + 0], array2[n44 + 1], array2[n44 + 2], array2[n44 + 3]);
                                n10 = array2[n44 + 0];
                                n11 = array2[n44 + 1];
                                n15 = array2[n44 + 2];
                                n16 = array2[n44 + 3];
                                break;
                            }
                            case 'Q': {
                                final int n45 = i;
                                path.quadTo(array2[n45 + 0], array2[n45 + 1], array2[n45 + 2], array2[n45 + 3]);
                                n38 = array2[n45 + 0];
                                n39 = array2[n45 + 1];
                                n36 = array2[n45 + 2];
                                n37 = array2[n45 + 3];
                                break Label_1851;
                            }
                            case 'M': {
                                final int n46 = i;
                                n15 = array2[n46 + 0];
                                n16 = array2[n46 + 1];
                                if (n46 > 0) {
                                    path.lineTo(array2[n46 + 0], array2[n46 + 1]);
                                    break;
                                }
                                path.moveTo(array2[n46 + 0], array2[n46 + 1]);
                                n14 = n15;
                                n13 = n16;
                                break;
                            }
                            case 'L': {
                                final int n47 = i;
                                path.lineTo(array2[n47 + 0], array2[n47 + 1]);
                                n15 = array2[n47 + 0];
                                n16 = array2[n47 + 1];
                                break;
                            }
                            case 'H': {
                                final int n48 = i;
                                path.lineTo(array2[n48 + 0], n16);
                                n15 = array2[n48 + 0];
                                break;
                            }
                            case 'C': {
                                final int n49 = i;
                                path.cubicTo(array2[n49 + 0], array2[n49 + 1], array2[n49 + 2], array2[n49 + 3], array2[n49 + 4], array2[n49 + 5]);
                                n36 = array2[n49 + 4];
                                n37 = array2[n49 + 5];
                                n38 = array2[n49 + 2];
                                n39 = array2[n49 + 3];
                                break Label_1851;
                            }
                            case 'A': {
                                final int n50 = i;
                                drawArc(path, n15, n16, array2[n50 + 5], array2[n50 + 6], array2[n50 + 0], array2[n50 + 1], array2[n50 + 2], array2[n50 + 3] != 0.0f, array2[n50 + 4] != 0.0f);
                                n36 = array2[n50 + 5];
                                n37 = array2[n50 + 6];
                                n38 = n36;
                                n39 = n37;
                                break Label_1851;
                            }
                        }
                        break Label_2021;
                    }
                    final float n51 = n38;
                    final float n52 = n39;
                    n16 = n37;
                    n15 = n36;
                    n10 = n51;
                    n11 = n52;
                }
                c3 = c2;
            }
            array[0] = n15;
            array[1] = n16;
            array[2] = n10;
            array[3] = n11;
            array[4] = n14;
            array[5] = n13;
        }
        
        private static void arcToBezier(final Path path, final double n, final double n2, final double n3, final double n4, double n5, double n6, double n7, double cos, double n8) {
            final int n9 = (int)Math.ceil(Math.abs(n8 * 4.0 / 3.141592653589793));
            double n10 = cos;
            final double cos2 = Math.cos(n7);
            final double sin = Math.sin(n7);
            cos = Math.cos(n10);
            final double sin2 = Math.sin(n10);
            final double n11 = -n3;
            final double n12 = -n3;
            n7 = n8 / n9;
            int i = 0;
            double n13 = n12 * sin * sin2 + n4 * cos2 * cos;
            n8 = n11 * cos2 * sin2 - n4 * sin * cos;
            double n14 = n6;
            cos = n5;
            n5 = sin;
            n6 = cos2;
            while (i < n9) {
                final double n15 = n10 + n7;
                final double sin3 = Math.sin(n15);
                final double cos3 = Math.cos(n15);
                final double n16 = n + n3 * n6 * cos3 - n4 * n5 * sin3;
                final double n17 = n2 + n3 * n5 * cos3 + n4 * n6 * sin3;
                final double n18 = -n3 * n6 * sin3 - n4 * n5 * cos3;
                final double n19 = -n3 * n5 * sin3 + n4 * n6 * cos3;
                final double tan = Math.tan((n15 - n10) / 2.0);
                final double n20 = Math.sin(n15 - n10) * (Math.sqrt(tan * 3.0 * tan + 4.0) - 1.0) / 3.0;
                path.rLineTo(0.0f, 0.0f);
                path.cubicTo((float)(cos + n20 * n8), (float)(n14 + n20 * n13), (float)(n16 - n20 * n18), (float)(n17 - n20 * n19), (float)n16, (float)n17);
                cos = n16;
                n14 = n17;
                n8 = n18;
                n13 = n19;
                ++i;
                n10 = n15;
            }
        }
        
        private static void drawArc(final Path path, final float n, final float n2, final float n3, final float n4, final float n5, final float n6, final float n7, final boolean b, final boolean b2) {
            final double radians = Math.toRadians(n7);
            final double cos = Math.cos(radians);
            final double sin = Math.sin(radians);
            final double n8 = (n * cos + n2 * sin) / n5;
            final double n9 = (-n * sin + n2 * cos) / n6;
            final double n10 = (n3 * cos + n4 * sin) / n5;
            final double n11 = (-n3 * sin + n4 * cos) / n6;
            final double n12 = n8 - n10;
            final double n13 = n9 - n11;
            final double n14 = (n8 + n10) / 2.0;
            final double n15 = (n9 + n11) / 2.0;
            final double n16 = n12 * n12 + n13 * n13;
            if (n16 == 0.0) {
                Log.w("PathParser", " Points are coincident");
                return;
            }
            final double n17 = 1.0 / n16 - 0.25;
            if (n17 < 0.0) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Points are too far apart ");
                sb.append(n16);
                Log.w("PathParser", sb.toString());
                final float n18 = (float)(Math.sqrt(n16) / 1.99999);
                drawArc(path, n, n2, n3, n4, n5 * n18, n6 * n18, n7, b, b2);
                return;
            }
            final double sqrt = Math.sqrt(n17);
            final double n19 = sqrt * n12;
            final double n20 = sqrt * n13;
            double n21;
            double n22;
            if (b == b2) {
                n21 = n14 - n20;
                n22 = n15 + n19;
            }
            else {
                n21 = n14 + n20;
                n22 = n15 - n19;
            }
            final double atan2 = Math.atan2(n9 - n22, n8 - n21);
            final double n23 = Math.atan2(n11 - n22, n10 - n21) - atan2;
            final boolean b3 = n23 >= 0.0;
            double n24 = n23;
            if (b2 != b3) {
                if (n23 > 0.0) {
                    n24 = n23 - 6.283185307179586;
                }
                else {
                    n24 = n23 + 6.283185307179586;
                }
            }
            final double n25 = n21 * n5;
            final double n26 = n22 * n6;
            arcToBezier(path, n25 * cos - n26 * sin, n25 * sin + n26 * cos, n5, n6, n, n2, radians, atan2, n24);
        }
        
        public static void nodesToPath(final PathDataNode[] array, final Path path) {
            final float[] array2 = new float[6];
            char mType = 'm';
            for (int i = 0; i < array.length; ++i) {
                addCommand(path, array2, mType, array[i].mType, array[i].mParams);
                mType = array[i].mType;
            }
        }
        
        public void interpolatePathDataNode(final PathDataNode pathDataNode, final PathDataNode pathDataNode2, final float n) {
            this.mType = pathDataNode.mType;
            for (int i = 0; i < pathDataNode.mParams.length; ++i) {
                this.mParams[i] = pathDataNode.mParams[i] * (1.0f - n) + pathDataNode2.mParams[i] * n;
            }
        }
    }
}
