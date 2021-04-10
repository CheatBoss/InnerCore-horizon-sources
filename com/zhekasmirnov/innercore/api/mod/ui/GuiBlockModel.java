package com.zhekasmirnov.innercore.api.mod.ui;

import android.util.*;
import java.lang.reflect.*;
import android.graphics.*;
import com.zhekasmirnov.apparatus.minecraft.version.*;
import com.zhekasmirnov.innercore.utils.*;
import com.zhekasmirnov.innercore.mod.resource.*;
import com.zhekasmirnov.innercore.api.log.*;
import com.zhekasmirnov.innercore.api.unlimited.*;
import com.zhekasmirnov.innercore.api.*;
import java.util.*;
import com.zhekasmirnov.horizon.util.*;
import org.json.*;

public class GuiBlockModel
{
    private ArrayList<Box> boxes;
    private final int resolution;
    private boolean shadow;
    
    public GuiBlockModel() {
        this(128);
    }
    
    public GuiBlockModel(final int resolution) {
        this.shadow = true;
        this.boxes = new ArrayList<Box>();
        this.resolution = resolution;
    }
    
    public GuiBlockModel(final String[] array, final int[] array2) {
        this();
        final Box box = new Box();
        for (int i = 0; i < 6; ++i) {
            box.addTexture(array[i], array2[i]);
        }
        this.addBox(box);
    }
    
    public GuiBlockModel(final String[] array, final int[] array2, final BlockShape blockShape) {
        this();
        final Box box = new Box(blockShape);
        for (int i = 0; i < 6; ++i) {
            box.addTexture(array[i], array2[i]);
        }
        this.addBox(box);
    }
    
    public static GuiBlockModel createModelForBlockVariant(final BlockVariant blockVariant) {
        if (blockVariant.renderType == 0) {
            return new GuiBlockModel(blockVariant.textures, blockVariant.textureIds, blockVariant.shape);
        }
        final VanillaRenderType for1 = VanillaRenderType.getFor(blockVariant.renderType);
        if (for1 != null) {
            return for1.buildModelFor(blockVariant);
        }
        return null;
    }
    
    public void addBox(final Box box) {
        this.boxes.add(box);
        box.setShadow(this.shadow);
    }
    
    public void addToMesh(final NativeRenderMesh nativeRenderMesh, final float n, final float n2, final float n3) {
        final Iterator<Box> iterator = this.boxes.iterator();
        while (iterator.hasNext()) {
            iterator.next().addToMesh(nativeRenderMesh, n, n2, n3);
        }
    }
    
    public void addToRenderModelPart(final NativeRenderer.ModelPart modelPart, final float n, final float n2, final float n3) {
        NativeRenderMesh mesh;
        if ((mesh = modelPart.getMesh()) == null) {
            mesh = new NativeRenderMesh();
        }
        this.addToMesh(mesh, n, n2, n3);
        modelPart.setMesh(mesh);
    }
    
    public void clear() {
        this.boxes.clear();
    }
    
    public Bitmap genTexture() {
        final Bitmap bitmap = Bitmap.createBitmap(this.resolution, this.resolution, Bitmap$Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        final Iterator<Box> iterator = this.boxes.iterator();
        while (iterator.hasNext()) {
            final Bitmap genTexture = iterator.next().genTexture(this.resolution);
            canvas.drawBitmap(genTexture, 0.0f, 0.0f, (Paint)null);
            genTexture.recycle();
        }
        return bitmap;
    }
    
    public void setShadow(final boolean b) {
        this.shadow = b;
        final Iterator<Box> iterator = this.boxes.iterator();
        while (iterator.hasNext()) {
            iterator.next().setShadow(b);
        }
    }
    
    public void updateShape(final BlockShape blockShape) {
        if (this.boxes.size() > 0) {
            final Box box = new Box(this.boxes.get(0), blockShape);
            this.boxes.clear();
            this.boxes.add(box);
        }
    }
    
    public static class Box
    {
        private static final Bitmap EMPTY_BITMAP;
        private static final Bitmap MISSING_BITMAP;
        private float[][][] EPSILON;
        private int[][] SIDES;
        private int[] SIDE_SHADOW;
        public final boolean[] enabledSides;
        private float[][] projection;
        private boolean renderAllSides;
        private boolean shadow;
        private ArrayList<Pair<String, Integer>> textureNames;
        private ArrayList<String> textures;
        public final float x1;
        public final float x2;
        public final float y1;
        public final float y2;
        public final float z1;
        public final float z2;
        
        static {
            MISSING_BITMAP = TextureSource.instance.getSafe("missing_texture");
            EMPTY_BITMAP = Bitmap.createBitmap(16, 16, Bitmap$Config.ARGB_8888);
        }
        
        public Box() {
            this(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        }
        
        public Box(final float x1, final float y1, final float z1, final float x2, final float y2, final float z2) {
            this.enabledSides = new boolean[] { true, true, true, true, true, true };
            this.shadow = true;
            this.renderAllSides = false;
            this.SIDE_SHADOW = new int[] { 0, 0, 0, 130, 65, 0, 0 };
            this.SIDES = new int[][] { { 8, 5, 6, 7 }, { 4, 1, 2, 3 }, { 4, 1, 5, 8 }, { 3, 2, 6, 7 }, { 4, 3, 7, 8 }, { 1, 2, 6, 5 } };
            this.EPSILON = new float[][][] { { { -1.0f, 0.0f, 1.0f }, { 1.0f, 0.0f, 1.0f }, { 1.0f, 0.0f, -1.0f }, { -1.0f, 0.0f, -1.0f } }, { { -1.0f, 0.0f, 1.0f }, { 1.0f, 0.0f, 1.0f }, { 1.0f, 0.0f, -1.0f }, { -1.0f, 0.0f, -1.0f } }, { { -1.0f, 1.0f, 0.0f }, { 1.0f, 1.0f, 0.0f }, { 1.0f, -1.0f, 0.0f }, { -1.0f, -1.0f, 0.0f } }, { { -1.0f, 1.0f, 0.0f }, { 1.0f, 1.0f, 0.0f }, { 1.0f, -1.0f, 0.0f }, { -1.0f, -1.0f, 0.0f } }, { { 0.0f, 1.0f, 1.0f }, { 0.0f, 1.0f, -1.0f }, { 0.0f, -1.0f, -1.0f }, { 0.0f, -1.0f, 1.0f } }, { { 0.0f, 1.0f, 1.0f }, { 0.0f, 1.0f, -1.0f }, { 0.0f, -1.0f, -1.0f }, { 0.0f, -1.0f, 1.0f } } };
            this.textures = new ArrayList<String>();
            this.textureNames = new ArrayList<Pair<String, Integer>>();
            this.x1 = x1;
            this.y1 = y1;
            this.z1 = z1;
            this.x2 = x2;
            this.y2 = y2;
            this.z2 = z2;
            this.buildProjection();
        }
        
        public Box(final Box box, final BlockShape blockShape) {
            this(blockShape);
            this.textures = box.textures;
            this.textureNames = box.textureNames;
        }
        
        public Box(final BlockShape blockShape) {
            this(blockShape.x1, blockShape.y1, blockShape.z1, blockShape.x2, blockShape.y2, blockShape.z2);
        }
        
        public Box(final String s, final int n) {
            this();
            this.addTexture(s, n);
        }
        
        private void buildProjection() {
            this.projection = (float[][])Array.newInstance(Float.TYPE, 6, 8);
            final float[][] array = { { this.x2, this.y2, this.z2 }, { this.x2, this.y2, this.z1 }, { this.x1, this.y2, this.z1 }, { this.x1, this.y2, this.z2 }, { this.x2, this.y1, this.z2 }, { this.x2, this.y1, this.z1 }, { this.x1, this.y1, this.z1 }, { this.x1, this.y1, this.z2 } };
            final float n = 0.00546875f;
            final float n2 = 3.9269907f;
            int i = 0;
            final float[][] array2 = array;
            while (i < 6) {
                final int[] array3 = this.SIDES[i];
                final float[] array4 = this.projection[i];
                for (int j = 0; j < 4; ++j) {
                    final float n3 = array2[array3[j] - 1][0] - 0.5f + this.EPSILON[i][j][0] * n;
                    final float n4 = array2[array3[j] - 1][1] - 0.5f + this.EPSILON[i][j][1] * n;
                    final float n5 = array2[array3[j] - 1][2] - 0.5f + this.EPSILON[i][j][2] * n;
                    final float n6 = (float)Math.sqrt(n3 * n3 + n5 * n5);
                    float n7;
                    if (n6 > 0.0f) {
                        n7 = (float)Math.atan2(n3, n5);
                    }
                    else {
                        n7 = 0.0f;
                    }
                    final float n8 = n7 + n2;
                    final float n9 = (float)Math.sin(n8) * n6;
                    final float n10 = (float)Math.cos(n8);
                    final float n11 = (float)Math.sqrt(n9 * n9 + n4 * n4);
                    float n12;
                    if (n11 > 0.0f) {
                        n12 = (float)Math.atan2(n9, n4);
                    }
                    else {
                        n12 = 0.0f;
                    }
                    final float n13 = n12 + 0.5235988f;
                    final float n14 = (float)Math.sin(n13);
                    final float n15 = (float)Math.cos(n13);
                    array4[j * 2] = n10 * n6 * 0.56f + 0.5f;
                    array4[j * 2 + 1] = 1.0f - (n15 * n11 * 0.56f + 0.5f);
                }
                ++i;
            }
        }
        
        private Bitmap genProjectedSideTex(int n, final int n2) {
            final Bitmap sideCutout = this.getSideCutout(n);
            final Projection sideProjection = this.getSideProjection(n, sideCutout, n2);
            final Bitmap bitmap = Bitmap.createBitmap(sideCutout, 0, 0, sideCutout.getWidth(), sideCutout.getHeight(), sideProjection.mat, false);
            final Bitmap bitmap2 = Bitmap.createBitmap(n2, n2, Bitmap$Config.ARGB_8888);
            final Canvas canvas = new Canvas(bitmap2);
            if (!sideProjection.isZeroSize()) {
                final Paint paint = new Paint();
                if (this.shadow) {
                    n = 255 - this.SIDE_SHADOW[n];
                    paint.setColorFilter((ColorFilter)new PorterDuffColorFilter(Color.rgb(n, n, n), PorterDuff$Mode.MULTIPLY));
                }
                canvas.drawBitmap(bitmap, sideProjection.minX, sideProjection.minY, paint);
            }
            recycleBitmap(bitmap);
            recycleBitmap(sideCutout);
            return bitmap2;
        }
        
        private float[] getSideBounds(final int n) {
            float n2 = 0.0f;
            float n3 = 0.0f;
            float n4 = 1.0f;
            float n5 = 1.0f;
            switch (n) {
                case 4:
                case 5: {
                    n2 = this.z1;
                    n3 = this.y1;
                    n4 = this.z2 - this.z1;
                    n5 = this.y2 - this.y1;
                    break;
                }
                case 2:
                case 3: {
                    n2 = this.x1;
                    n3 = this.y1;
                    n4 = this.x2 - this.x1;
                    n5 = this.y2 - this.y1;
                    break;
                }
                case 0:
                case 1: {
                    n2 = this.x1;
                    n3 = this.z1;
                    n4 = this.x2 - this.x1;
                    n5 = this.z2 - this.z1;
                    break;
                }
            }
            final float max = Math.max(0.0f, Math.min(1.0f, n2));
            final float max2 = Math.max(0.0f, Math.min(1.0f, n3));
            return new float[] { max, max2, Math.max(0.0f, Math.min(1.0f - max, n4)), Math.max(0.0f, Math.min(1.0f - max2, n5)) };
        }
        
        private Bitmap getSideCutout(int height) {
            Bitmap bitmap;
            if ((bitmap = this.loadTexture(height)) == null) {
                bitmap = Box.MISSING_BITMAP;
            }
            final float[] sideBounds = this.getSideBounds(height);
            final float n = sideBounds[0];
            final float n2 = sideBounds[1];
            final float n3 = sideBounds[2];
            final float n4 = sideBounds[3];
            if (n3 >= 0.0f && n4 >= 0.0f) {
                Bitmap bitmap2;
                try {
                    final int width = bitmap.getWidth();
                    if ((height = bitmap.getHeight()) >= width * 2) {
                        height = width;
                    }
                    final int min = Math.min(width, Math.max(0, (int)(width * n + 0.5f)));
                    final int min2 = Math.min(height, Math.max(0, (int)(height * (1.0f - n4 - n2) + 0.5f)));
                    bitmap2 = Bitmap.createBitmap(bitmap, min, min2, Math.min(width - min, Math.max(0, (int)(width * n3 + 0.5f))), Math.min(height - min2, Math.max(0, (int)(height * n4 + 0.5f))));
                }
                catch (IllegalArgumentException ex) {
                    bitmap2 = Box.EMPTY_BITMAP;
                }
                if (bitmap2 != bitmap) {
                    recycleBitmap(bitmap);
                }
                return bitmap2;
            }
            return Box.EMPTY_BITMAP;
        }
        
        private Projection getSideProjection(int i, final Bitmap bitmap, final int n) {
            final Matrix mat = new Matrix();
            mat.reset();
            final int n2 = 0;
            final float n3 = (float)bitmap.getWidth();
            final float n4 = (float)bitmap.getWidth();
            final float n5 = (float)bitmap.getHeight();
            final float n6 = (float)bitmap.getHeight();
            final float[] array = this.projection[i];
            final float[] array2 = new float[8];
            for (i = 0; i < 8; ++i) {
                array2[i] = array[i] * n;
            }
            mat.setPolyToPoly(new float[] { 0.0f, 0.0f, n3, 0.0f, n4, n5, 0.0f, n6 }, 0, array2, 0, 4);
            final Projection projection = new Projection();
            projection.mat = mat;
            final float n7 = (float)n;
            projection.minY = n7;
            projection.minX = n7;
            projection.maxY = 0.0f;
            projection.maxX = 0.0f;
            float n8;
            float n9;
            for (i = n2; i < 4; ++i) {
                n8 = array2[i * 2];
                n9 = array2[i * 2 + 1];
                projection.maxX = Math.max(projection.maxX, n8);
                projection.maxY = Math.max(projection.maxY, n9);
                projection.minX = Math.min(projection.minX, n8);
                projection.minY = Math.min(projection.minY, n9);
            }
            return projection;
        }
        
        private Pair<String, Integer> getTextureName(int min) {
            min = Math.min(min, this.textureNames.size() - 1);
            if (min < 0) {
                return null;
            }
            return this.textureNames.get(min);
        }
        
        private Bitmap loadTexture(int min) {
            min = Math.min(min, this.textures.size() - 1);
            if (min == -1) {
                return Box.MISSING_BITMAP;
            }
            final String s = this.textures.get(min);
            if (s == null || s.equals("missing_texture")) {
                return Box.MISSING_BITMAP;
            }
            final Bitmap bitmapFromBytes = FileTools.bitmapFromBytes(FileTools.getAssetBytes(s, MinecraftVersions.getCurrent().getVanillaResourcePacksDirs(), true));
            if (bitmapFromBytes == null) {
                return Box.MISSING_BITMAP;
            }
            Bitmap bitmap = bitmapFromBytes;
            if (bitmapFromBytes.getHeight() > bitmapFromBytes.getWidth()) {
                final Bitmap bitmap2 = Bitmap.createBitmap(bitmapFromBytes, 0, 0, bitmapFromBytes.getWidth(), bitmapFromBytes.getWidth());
                if (bitmap2 != (bitmap = bitmapFromBytes)) {
                    recycleBitmap(bitmapFromBytes);
                    bitmap = bitmap2;
                }
            }
            return bitmap;
        }
        
        private static void recycleBitmap(final Bitmap bitmap) {
            if (bitmap != null && bitmap != Box.MISSING_BITMAP && bitmap != Box.EMPTY_BITMAP) {
                bitmap.recycle();
            }
        }
        
        public void addTexture(final Pair<String, Integer> pair) {
            this.textureNames.add(pair);
            String s;
            if ((s = ResourcePackManager.getBlockTextureName((String)pair.first, (int)pair.second)) == null) {
                s = ResourcePackManager.getBlockTextureName("missing_block", 0);
            }
            this.addTexturePath(s);
        }
        
        public void addTexture(final String s, final int n) {
            this.addTexture((Pair<String, Integer>)new Pair((Object)s, (Object)n));
        }
        
        public void addTexturePath(final String s) {
            if (s != null) {
                String string = s;
                if (!s.endsWith(".png")) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(s);
                    sb.append(".png");
                    string = sb.toString();
                }
                this.textures.add(string);
                return;
            }
            this.textures.add("missing_texture");
        }
        
        public void addToMesh(final NativeRenderMesh nativeRenderMesh, final float n, final float n2, final float n3) {
            nativeRenderMesh.setColor(1.0f, 1.0f, 1.0f);
            final float[] array = new float[4];
            for (int i = 0; i < 6; ++i) {
                final Pair<String, Integer> textureName = this.getTextureName(i);
                if (textureName == null) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("texture name for side ");
                    sb.append(i);
                    sb.append(" is null, cannot render side in model");
                    ICLog.i("ERROR", sb.toString());
                    return;
                }
                NativeAPI.getAtlasTextureCoords((String)textureName.first, (int)textureName.second, array);
                final float n4 = this.x1 * 1 + 0.0f;
                final float n5 = this.y1 * 1 + 0.0f;
                final float n6 = this.z1 * 1 + 0.0f;
                final float n7 = (this.x2 - this.x1) * 1;
                final float n8 = (this.y2 - this.y1) * 1;
                final float n9 = (this.z2 - this.z1) * 1;
                final float n10 = array[2] - array[0];
                final float n11 = array[3] - array[1];
                switch (i) {
                    case 5: {
                        final float n12 = array[0] + this.z1 * n10;
                        final float n13 = array[1] + (1.0f - this.y2) * n11;
                        final float n14 = array[0] + this.z2 * n10;
                        final float n15 = array[1] + (1.0f - this.y1) * n11;
                        nativeRenderMesh.setNormal(1.0f, 0.0f, 0.0f);
                        nativeRenderMesh.addVertex(n + n4 + n7, n2 + n5, n3 + n6, n12, n15);
                        nativeRenderMesh.addVertex(n + n4 + n7, n2 + n5 + n8, n3 + n6, n12, n13);
                        nativeRenderMesh.addVertex(n + n4 + n7, n2 + n5 + n8, n3 + n6 + n9, n14, n13);
                        nativeRenderMesh.addVertex(n + n4 + n7, n2 + n5 + n8, n3 + n6 + n9, n14, n13);
                        nativeRenderMesh.addVertex(n + n4 + n7, n2 + n5, n3 + n6 + n9, n14, n15);
                        nativeRenderMesh.addVertex(n + n4 + n7, n2 + n5, n3 + n6, n12, n15);
                        break;
                    }
                    case 4: {
                        final float n16 = array[0] + (1.0f - this.z2) * n10;
                        final float n17 = array[1] + (1.0f - this.y2) * n11;
                        final float n18 = array[0] + (1.0f - this.z1) * n10;
                        final float n19 = array[1] + (1.0f - this.y1) * n11;
                        nativeRenderMesh.setNormal(-1.0f, 0.0f, 0.0f);
                        nativeRenderMesh.addVertex(n + n4, n2 + n5, n3 + n6, n18, n19);
                        nativeRenderMesh.addVertex(n + n4, n2 + n5 + n8, n3 + n6, n18, n17);
                        nativeRenderMesh.addVertex(n + n4, n2 + n5 + n8, n3 + n6 + n9, n16, n17);
                        nativeRenderMesh.addVertex(n + n4, n2 + n5 + n8, n3 + n6 + n9, n16, n17);
                        nativeRenderMesh.addVertex(n + n4, n2 + n5, n3 + n6 + n9, n16, n19);
                        nativeRenderMesh.addVertex(n + n4, n2 + n5, n3 + n6, n18, n19);
                        break;
                    }
                    case 3: {
                        final float n20 = array[0] + this.x1 * n10;
                        final float n21 = array[1] + (1.0f - this.y2) * n11;
                        final float n22 = array[0] + this.x2 * n10;
                        final float n23 = array[1] + (1.0f - this.y1) * n11;
                        nativeRenderMesh.setNormal(0.0f, 0.0f, -1.0f);
                        nativeRenderMesh.addVertex(n + n4, n2 + n5, n3 + n6, n20, n23);
                        nativeRenderMesh.addVertex(n + n4, n2 + n5 + n8, n3 + n6, n20, n21);
                        nativeRenderMesh.addVertex(n + n4 + n7, n2 + n5 + n8, n3 + n6, n22, n21);
                        nativeRenderMesh.addVertex(n + n4 + n7, n2 + n5 + n8, n3 + n6, n22, n21);
                        nativeRenderMesh.addVertex(n + n4 + n7, n2 + n5, n3 + n6, n22, n23);
                        nativeRenderMesh.addVertex(n + n4, n2 + n5, n3 + n6, n20, n23);
                        break;
                    }
                    case 2: {
                        final float n24 = array[0] + this.x1 * n10;
                        final float n25 = array[1] + (1.0f - this.y2) * n11;
                        final float n26 = array[0] + this.x2 * n10;
                        final float n27 = array[1] + (1.0f - this.y1) * n11;
                        nativeRenderMesh.setNormal(0.0f, 0.0f, 1.0f);
                        nativeRenderMesh.addVertex(n + n4, n2 + n5, n3 + n6 + n9, n26, n27);
                        nativeRenderMesh.addVertex(n + n4, n2 + n5 + n8, n3 + n6 + n9, n26, n25);
                        nativeRenderMesh.addVertex(n + n4 + n7, n2 + n5 + n8, n3 + n6 + n9, n24, n25);
                        nativeRenderMesh.addVertex(n + n4 + n7, n2 + n5 + n8, n3 + n6 + n9, n24, n25);
                        nativeRenderMesh.addVertex(n + n4 + n7, n2 + n5, n3 + n6 + n9, n24, n27);
                        nativeRenderMesh.addVertex(n + n4, n2 + n5, n3 + n6 + n9, n26, n27);
                        break;
                    }
                    case 1: {
                        final float n28 = array[0] + this.x1 * n10;
                        final float n29 = array[1] + (1.0f - this.z2) * n11;
                        final float n30 = array[0] + this.x2 * n10;
                        final float n31 = array[1] + (1.0f - this.z1) * n11;
                        nativeRenderMesh.setNormal(0.0f, 1.0f, 0.0f);
                        nativeRenderMesh.addVertex(n + n4 + n7, n2 + n5 + n8, n3 + n6, n30, n31);
                        nativeRenderMesh.addVertex(n + n4 + n7, n2 + n5 + n8, n3 + n6 + n9, n30, n29);
                        nativeRenderMesh.addVertex(n + n4, n2 + n5 + n8, n3 + n6 + n9, n28, n29);
                        nativeRenderMesh.addVertex(n + n4, n2 + n5 + n8, n3 + n6 + n9, n28, n29);
                        nativeRenderMesh.addVertex(n + n4, n2 + n5 + n8, n3 + n6, n28, n31);
                        nativeRenderMesh.addVertex(n + n4 + n7, n2 + n5 + n8, n3 + n6, n30, n31);
                        break;
                    }
                    case 0: {
                        final float n32 = array[0] + this.x1 * n10;
                        final float n33 = array[1] + (1.0f - this.z2) * n11;
                        final float n34 = array[0] + this.x2 * n10;
                        final float n35 = array[1] + (1.0f - this.z1) * n11;
                        nativeRenderMesh.setNormal(0.0f, -1.0f, 0.0f);
                        nativeRenderMesh.addVertex(n + n4 + n7, n2 + n5, n3 + n6, n34, n33);
                        nativeRenderMesh.addVertex(n + n4 + n7, n2 + n5, n3 + n6 + n9, n34, n35);
                        nativeRenderMesh.addVertex(n + n4, n2 + n5, n3 + n6 + n9, n32, n35);
                        nativeRenderMesh.addVertex(n + n4, n2 + n5, n3 + n6 + n9, n32, n35);
                        nativeRenderMesh.addVertex(n + n4, n2 + n5, n3 + n6, n32, n33);
                        nativeRenderMesh.addVertex(n + n4 + n7, n2 + n5, n3 + n6, n34, n33);
                        break;
                    }
                }
            }
        }
        
        public Bitmap genTexture(final int n) {
            final Bitmap bitmap = Bitmap.createBitmap(n, n, Bitmap$Config.ARGB_8888);
            final Canvas canvas = new Canvas(bitmap);
            int[] array;
            if (!this.renderAllSides) {
                final int[] array2;
                array = (array2 = new int[3]);
                array2[0] = 3;
                array2[1] = 4;
                array2[2] = 1;
            }
            else {
                final int[] array3;
                array = (array3 = new int[6]);
                array3[0] = 0;
                array3[array3[1] = 2] = 5;
                array3[3] = 3;
                array3[4] = 4;
                array3[5] = 1;
            }
            for (int length = array.length, i = 0; i < length; ++i) {
                final int n2 = array[i];
                if (this.enabledSides[n2]) {
                    try {
                        final Bitmap genProjectedSideTex = this.genProjectedSideTex(n2, n);
                        canvas.drawBitmap(genProjectedSideTex, 0.0f, 0.0f, (Paint)null);
                        recycleBitmap(genProjectedSideTex);
                    }
                    catch (IllegalArgumentException ex) {}
                }
            }
            return bitmap;
        }
        
        public BlockShape getShape() {
            return new BlockShape(this.x1, this.y1, this.z1, this.x2, this.y2, this.z2);
        }
        
        public void setRenderAllSides(final boolean renderAllSides) {
            this.renderAllSides = renderAllSides;
        }
        
        public void setShadow(final boolean shadow) {
            this.shadow = shadow;
        }
        
        private class Projection
        {
            Matrix mat;
            float maxX;
            float maxY;
            float minX;
            float minY;
            
            boolean isZeroSize() {
                return Math.abs(this.maxX - this.minX) < 0.001f || Math.abs(this.maxY - this.minY) < 0.001f;
            }
        }
    }
    
    public static class Builder
    {
        private static final float E = 0.025f;
        private List<PrecompiledBox> boxes;
        
        public Builder() {
            this.boxes = new ArrayList<PrecompiledBox>();
        }
        
        public void add(final PrecompiledBox precompiledBox) {
            this.boxes.add(precompiledBox);
        }
        
        public void add(final Builder builder) {
            final Iterator<PrecompiledBox> iterator = builder.boxes.iterator();
            while (iterator.hasNext()) {
                this.add(iterator.next());
            }
        }
        
        public GuiBlockModel build(final boolean b) {
            final ArrayList list = new ArrayList<Object>((Collection<? extends T>)this.boxes);
            if (b) {
                int i = 1;
                while (i != 0) {
                    i = 0;
                    for (int n = 0; n < list.size() && i == 0; ++n) {
                        final PrecompiledBox precompiledBox = list.get(n);
                        for (int n2 = 0; n2 < list.size() && i == 0; ++n2) {
                            if (n != n2) {
                                final PrecompiledBox precompiledBox2 = list.get(n2);
                                if (precompiledBox.y1 + 0.025f < precompiledBox2.y2 && precompiledBox.y2 > precompiledBox2.y1 + 0.025f && precompiledBox.z1 + 0.025f < precompiledBox2.z2) {
                                    if (precompiledBox.z2 > precompiledBox2.z1 + 0.025f) {
                                        if (precompiledBox.x1 < precompiledBox2.x1 && precompiledBox.x2 > precompiledBox2.x1) {
                                            list.remove(n);
                                            list.add(new PrecompiledBox(precompiledBox, precompiledBox.x1, precompiledBox.y1, precompiledBox.z1, precompiledBox2.x1, precompiledBox.y2, precompiledBox.z2).disableSide(5));
                                            list.add(new PrecompiledBox(precompiledBox, precompiledBox2.x1, precompiledBox.y1, precompiledBox.z1, precompiledBox.x2, precompiledBox.y2, precompiledBox.z2).disableSide(4));
                                            i = 1;
                                            break;
                                        }
                                        if (precompiledBox.x1 < precompiledBox2.x2 && precompiledBox.x2 > precompiledBox2.x2) {
                                            list.remove(n);
                                            list.add(new PrecompiledBox(precompiledBox, precompiledBox.x1, precompiledBox.y1, precompiledBox.z1, precompiledBox2.x2, precompiledBox.y2, precompiledBox.z2).disableSide(5));
                                            list.add(new PrecompiledBox(precompiledBox, precompiledBox2.x2, precompiledBox.y1, precompiledBox.z1, precompiledBox.x2, precompiledBox.y2, precompiledBox.z2).disableSide(4));
                                            i = 1;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                int j = 1;
                while (j != 0) {
                    j = 0;
                    for (int n3 = 0; n3 < list.size() && j == 0; ++n3) {
                        final PrecompiledBox precompiledBox3 = list.get(n3);
                        int n4 = 0;
                        PrecompiledBox precompiledBox4 = null;
                        Block_29: {
                            int n5;
                            while (true) {
                                n5 = j;
                                if (n4 >= list.size() || (n5 = j) != 0) {
                                    break;
                                }
                                if (n3 != n4) {
                                    precompiledBox4 = list.get(n4);
                                    if (precompiledBox3.x1 + 0.025f < precompiledBox4.x2 && precompiledBox3.x2 > precompiledBox4.x1 + 0.025f && precompiledBox3.z1 + 0.025f < precompiledBox4.z2) {
                                        if (precompiledBox3.z2 > precompiledBox4.z1 + 0.025f) {
                                            if (precompiledBox3.y1 < precompiledBox4.y1 && precompiledBox3.y2 > precompiledBox4.y1) {
                                                list.remove(n3);
                                                list.add(new PrecompiledBox(precompiledBox3, precompiledBox3.x1, precompiledBox3.y1, precompiledBox3.z1, precompiledBox3.x2, precompiledBox4.y1, precompiledBox3.z2).disableSide(1));
                                                list.add(new PrecompiledBox(precompiledBox3, precompiledBox3.x1, precompiledBox4.y1, precompiledBox3.z1, precompiledBox3.x2, precompiledBox3.y2, precompiledBox3.z2).disableSide(0));
                                                n5 = 1;
                                                break;
                                            }
                                            if (precompiledBox3.y1 < precompiledBox4.y2 && precompiledBox3.y2 > precompiledBox4.y2) {
                                                break Block_29;
                                            }
                                        }
                                    }
                                }
                                ++n4;
                            }
                            j = n5;
                            continue;
                        }
                        list.remove(n3);
                        list.add(new PrecompiledBox(precompiledBox3, precompiledBox3.x1, precompiledBox3.y1, precompiledBox3.z1, precompiledBox3.x2, precompiledBox4.y2, precompiledBox3.z2).disableSide(1));
                        list.add(new PrecompiledBox(precompiledBox3, precompiledBox3.x1, precompiledBox4.y2, precompiledBox3.z1, precompiledBox3.x2, precompiledBox3.y2, precompiledBox3.z2).disableSide(0));
                        j = 1;
                    }
                }
                int k = 1;
                while (k != 0) {
                    k = 0;
                    for (int n6 = 0; n6 < list.size() && k == 0; ++n6) {
                        final PrecompiledBox precompiledBox5 = list.get(n6);
                        for (int n7 = 0; n7 < list.size() && k == 0; ++n7) {
                            if (n6 != n7) {
                                final PrecompiledBox precompiledBox6 = list.get(n7);
                                if (precompiledBox5.x1 + 0.025f < precompiledBox6.x2 && precompiledBox5.x2 > precompiledBox6.x1 + 0.025f && precompiledBox5.y1 + 0.025f < precompiledBox6.y2) {
                                    if (precompiledBox5.y2 > precompiledBox6.y1 + 0.025f) {
                                        if (precompiledBox5.z1 < precompiledBox6.z1 && precompiledBox5.z2 > precompiledBox6.z1) {
                                            list.remove(n6);
                                            list.add(new PrecompiledBox(precompiledBox5, precompiledBox5.x1, precompiledBox5.y1, precompiledBox5.z1, precompiledBox5.x2, precompiledBox5.y2, precompiledBox6.z1).disableSide(2));
                                            list.add(new PrecompiledBox(precompiledBox5, precompiledBox5.x1, precompiledBox5.y1, precompiledBox6.z1, precompiledBox5.x2, precompiledBox5.y2, precompiledBox5.z2).disableSide(3));
                                            k = 1;
                                            break;
                                        }
                                        if (precompiledBox5.z1 < precompiledBox6.z2 && precompiledBox5.z2 > precompiledBox6.z2) {
                                            list.remove(n6);
                                            list.add(new PrecompiledBox(precompiledBox5, precompiledBox5.x1, precompiledBox5.y1, precompiledBox5.z1, precompiledBox5.x2, precompiledBox5.y2, precompiledBox6.z2).disableSide(2));
                                            list.add(new PrecompiledBox(precompiledBox5, precompiledBox5.x1, precompiledBox5.y1, precompiledBox6.z2, precompiledBox5.x2, precompiledBox5.y2, precompiledBox5.z2).disableSide(3));
                                            k = 1;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                final Comparator<PrecompiledBox> comparator = new Comparator<PrecompiledBox>() {
                    @Override
                    public int compare(final PrecompiledBox precompiledBox, final PrecompiledBox precompiledBox2) {
                        if (precompiledBox.x1 == precompiledBox2.x1 && precompiledBox.y1 == precompiledBox2.y1 && precompiledBox.z1 == precompiledBox2.z1 && precompiledBox.x2 == precompiledBox2.x2 && precompiledBox.y2 == precompiledBox2.y2 && precompiledBox.z2 == precompiledBox2.z2) {
                            return 0;
                        }
                        if (precompiledBox.inFrontOf(precompiledBox2)) {
                            return 1;
                        }
                        if (precompiledBox2.inFrontOf(precompiledBox)) {
                            return -1;
                        }
                        if (precompiledBox.inside(precompiledBox2)) {
                            return -1;
                        }
                        if (precompiledBox2.inside(precompiledBox)) {
                            return 1;
                        }
                        return 0;
                    }
                };
                try {
                    Collections.sort((List<Object>)list, (Comparator<? super Object>)comparator);
                }
                catch (IllegalArgumentException ex) {
                    ICLog.e("GuiBlockModel", "failed to compile boxes", ex);
                }
            }
            final GuiBlockModel guiBlockModel = new GuiBlockModel();
            final Iterator<PrecompiledBox> iterator = list.iterator();
            while (iterator.hasNext()) {
                final Box compile = iterator.next().compile();
                if (compile != null) {
                    guiBlockModel.addBox(compile);
                }
            }
            return guiBlockModel;
        }
        
        public static class PrecompiledBox
        {
            public int blockData;
            public int blockId;
            public final boolean[] enabledSides;
            public ArrayList<Pair<String, Integer>> textureNames;
            public float x1;
            public float x2;
            public float y1;
            public float y2;
            public float z1;
            public float z2;
            
            public PrecompiledBox(final PrecompiledBox precompiledBox, final float n, final float n2, final float n3, final float n4, final float n5, final float n6) {
                this.textureNames = new ArrayList<Pair<String, Integer>>();
                this.enabledSides = new boolean[] { true, true, true, true, true, true };
                this.blockId = -1;
                int i = 0;
                this.blockData = 0;
                this.x1 = Math.min(n, n4);
                this.y1 = Math.min(n2, n5);
                this.z1 = Math.min(n3, n6);
                this.x2 = Math.max(n, n4);
                this.y2 = Math.max(n2, n5);
                this.z2 = Math.max(n3, n6);
                if (precompiledBox != null) {
                    this.textureNames = new ArrayList<Pair<String, Integer>>(precompiledBox.textureNames);
                    this.blockId = precompiledBox.blockId;
                    this.blockData = precompiledBox.blockData;
                    while (i < this.enabledSides.length) {
                        this.enabledSides[i] = precompiledBox.enabledSides[i];
                        ++i;
                    }
                }
            }
            
            public PrecompiledBox addTexture(final String s, final int n) {
                this.textureNames.add((Pair<String, Integer>)new Pair((Object)s, (Object)n));
                return this;
            }
            
            public Box compile() {
                final Box box = new Box(this.x1, this.y1, this.z1, this.x2, this.y2, this.z2);
                final int n = 0;
                for (int i = 0; i < this.enabledSides.length; ++i) {
                    box.enabledSides[i] = this.enabledSides[i];
                }
                if (this.blockId != -1) {
                    final BlockVariant blockVariant = BlockRegistry.getBlockVariant(this.blockId, this.blockData);
                    if (blockVariant != null) {
                        for (int j = n; j < 6; ++j) {
                            box.addTexture(blockVariant.textures[j], blockVariant.textureIds[j]);
                        }
                    }
                    else if (this.blockId < 8192) {
                        try {
                            final GuiBlockModel guiBlockModel = NativeItemModel.getForWithFallback(this.blockId, this.blockData).getGuiBlockModel();
                            if (guiBlockModel != null && guiBlockModel.boxes.size() > 0) {
                                final Iterator iterator = ((Box)guiBlockModel.boxes.get(0)).textureNames.iterator();
                                while (iterator.hasNext()) {
                                    box.addTexture((Pair<String, Integer>)iterator.next());
                                }
                            }
                        }
                        catch (StackOverflowError stackOverflowError) {}
                    }
                }
                final Iterator<Pair<String, Integer>> iterator2 = this.textureNames.iterator();
                while (iterator2.hasNext()) {
                    box.addTexture(iterator2.next());
                }
                return box;
            }
            
            public PrecompiledBox disableSide(final int n) {
                this.enabledSides[n] = false;
                return this;
            }
            
            public boolean inFrontOf(final PrecompiledBox precompiledBox) {
                return precompiledBox.y2 <= this.y1 || (precompiledBox.y1 < this.y2 && precompiledBox.z2 > this.z1 && (precompiledBox.z1 >= this.z2 || (precompiledBox.x2 > this.x1 && precompiledBox.x1 >= this.x2)));
            }
            
            public boolean inside(final PrecompiledBox precompiledBox) {
                return this.x1 >= precompiledBox.x1 && this.y1 >= precompiledBox.y1 && this.z1 >= precompiledBox.z1 && this.x2 <= precompiledBox.x2 && this.y2 <= precompiledBox.y2 && this.z2 <= precompiledBox.z2;
            }
            
            public boolean intersects(final PrecompiledBox precompiledBox) {
                return this.x1 < precompiledBox.x2 && precompiledBox.x1 < this.x2 && this.y1 < precompiledBox.y2 && precompiledBox.y1 < this.y2 && this.z1 < precompiledBox.z2 && precompiledBox.z1 >= this.z2 && false;
            }
            
            public PrecompiledBox setBlock(final int blockId, final int blockData) {
                this.blockId = blockId;
                this.blockData = blockData;
                return this;
            }
            
            @Override
            public String toString() {
                return String.format("[Box (%f %f %f), (%f %f %f)]", this.x1, this.y1, this.z1, this.x2, this.y2, this.z2);
            }
        }
    }
    
    public static class VanillaRenderType
    {
        private static boolean isDataLoaded;
        private static final HashMap<Integer, VanillaRenderType> renderTypeMap;
        private ArrayList<NoTextureBox> boxes;
        
        static {
            renderTypeMap = new HashMap<Integer, VanillaRenderType>();
            VanillaRenderType.isDataLoaded = false;
        }
        
        private VanillaRenderType(final JSONArray jsonArray) {
            this.boxes = new ArrayList<NoTextureBox>();
            for (final JSONArray jsonArray2 : new JsonIterator(jsonArray)) {
                if (jsonArray2 != null) {
                    this.boxes.add(new NoTextureBox(jsonArray2));
                }
            }
        }
        
        public static VanillaRenderType getFor(final int n) {
            loadIfRequired();
            return VanillaRenderType.renderTypeMap.get(n);
        }
        
        private static JSONObject loadDescriptionJson() {
            try {
                return new JSONObject(FileTools.getAssetAsString("innercore/icons/block_rendertypes.json"));
            }
            catch (JSONException ex) {
                ex.printStackTrace();
                return new JSONObject();
            }
        }
        
        private static void loadIfRequired() {
            synchronized (VanillaRenderType.class) {
                if (!VanillaRenderType.isDataLoaded) {
                    final JSONObject loadDescriptionJson = loadDescriptionJson();
                    for (final String s : new JsonIterator(loadDescriptionJson)) {
                        final int int1 = Integer.parseInt(s);
                        final JSONArray optJSONArray = loadDescriptionJson.optJSONArray(s);
                        if (optJSONArray != null) {
                            VanillaRenderType.renderTypeMap.put(int1, new VanillaRenderType(optJSONArray));
                        }
                    }
                }
            }
        }
        
        public GuiBlockModel buildModelFor(final BlockVariant blockVariant) {
            return this.buildModelFor(blockVariant.textures, blockVariant.textureIds);
        }
        
        public GuiBlockModel buildModelFor(final List<Pair<String, Integer>> list) {
            final String[] array = new String[6];
            final int[] array2 = new int[6];
            for (int i = 0; i < 6; ++i) {
                Pair pair;
                if (list.size() > 0) {
                    pair = list.get(Math.min(i, list.size() - 1));
                }
                else {
                    pair = new Pair((Object)"missing_texture", (Object)0);
                }
                array[i] = (String)pair.first;
                array2[i] = (int)pair.second;
            }
            return this.buildModelFor(array, array2);
        }
        
        public GuiBlockModel buildModelFor(final String[] array, final int[] array2) {
            final GuiBlockModel guiBlockModel = new GuiBlockModel();
            final Iterator<NoTextureBox> iterator = this.boxes.iterator();
            while (iterator.hasNext()) {
                guiBlockModel.addBox(iterator.next().asModelBox(array, array2));
            }
            return guiBlockModel;
        }
        
        private static class NoTextureBox
        {
            public int blockData;
            public int blockId;
            public final boolean[] enabledSides;
            public ArrayList<Pair<String, Integer>> textureNames;
            public float x1;
            public float x2;
            public float y1;
            public float y2;
            public float z1;
            public float z2;
            
            private NoTextureBox(final float n, final float n2, final float n3, final float n4, final float n5, final float n6) {
                this.textureNames = new ArrayList<Pair<String, Integer>>();
                this.enabledSides = new boolean[] { true, true, true, true, true, true };
                this.blockId = -1;
                this.blockData = 0;
                this.x1 = Math.min(n, n4);
                this.y1 = Math.min(n2, n5);
                this.z1 = Math.min(n3, n6);
                this.x2 = Math.max(n, n4);
                this.y2 = Math.max(n2, n5);
                this.z2 = Math.max(n3, n6);
            }
            
            private NoTextureBox(final JSONArray jsonArray) {
                this((float)jsonArray.optDouble(0, 0.0), (float)jsonArray.optDouble(1, 0.0), (float)jsonArray.optDouble(2, 0.0), (float)jsonArray.optDouble(3, 1.0), (float)jsonArray.optDouble(4, 1.0), (float)jsonArray.optDouble(5, 1.0));
            }
            
            private Box asModelBox(final String[] array, final int[] array2) {
                final Box box = new Box(this.x1, this.y1, this.z1, this.x2, this.y2, this.z2);
                for (int i = 0; i < 6; ++i) {
                    box.addTexture(array[i], array2[i]);
                }
                return box;
            }
        }
    }
}
