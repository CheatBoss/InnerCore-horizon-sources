package com.zhekasmirnov.innercore.api;

import org.mozilla.javascript.*;
import com.zhekasmirnov.horizon.launcher.env.*;
import java.io.*;
import com.zhekasmirnov.innercore.api.mod.*;
import com.zhekasmirnov.innercore.api.mod.ui.*;
import com.zhekasmirnov.innercore.mod.resource.*;

public class NativeRenderMesh
{
    private int mBlockTextureId;
    private String mBlockTextureName;
    private final long ptr;
    
    public NativeRenderMesh() {
        this.mBlockTextureName = null;
        this.mBlockTextureId = 0;
        this.ptr = nativeConstructNew();
    }
    
    private static native void nativeAddMeshToMesh(final long p0, final long p1, final float p2, final float p3, final float p4, final float p5, final float p6, final float p7);
    
    private static native void nativeAddVertex(final long p0, final float p1, final float p2, final float p3, final float p4, final float p5);
    
    private static native void nativeClear(final long p0);
    
    private static native long nativeConstructNew();
    
    private static native void nativeFitIn(final long p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6, final boolean p7);
    
    private static native void nativeGetMeshData(final long p0, final short[] p1, final float[] p2, final float[] p3, final float[] p4);
    
    private static native int nativeGetMeshDataSize(final long p0);
    
    private static native void nativeImportFromFile(final long p0, final String p1, final String p2, final float p3, final float p4, final float p5, final float p6, final float p7, final float p8, final boolean p9);
    
    private static native void nativeInvalidate(final long p0);
    
    private static native void nativeRebuild(final long p0);
    
    private static native void nativeResetColor(final long p0);
    
    private static native void nativeResetTargetTexture(final long p0);
    
    private static native void nativeRotate(final long p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6);
    
    private static native void nativeScale(final long p0, final float p1, final float p2, final float p3);
    
    private static native void nativeSetColor(final long p0, final float p1, final float p2, final float p3, final float p4);
    
    private static native void nativeSetLightDir(final long p0, final float p1, final float p2, final float p3);
    
    private static native void nativeSetLightIgnore(final long p0, final boolean p1, final boolean p2);
    
    private static native void nativeSetLightParams(final long p0, final float p1, final float p2, final float p3);
    
    private static native void nativeSetLightPos(final long p0, final int p1, final int p2, final int p3);
    
    private static native void nativeSetNormal(final long p0, final float p1, final float p2, final float p3);
    
    private static native void nativeSetTargetBlockTexture(final long p0, final String p1, final int p2);
    
    private static native void nativeSetTintSource(final long p0, final boolean p1, final boolean p2, final boolean p3, final int p4);
    
    private static native void nativeTranslate(final long p0, final float p1, final float p2, final float p3);
    
    public void addMesh(final NativeRenderMesh nativeRenderMesh) {
        this.addMesh(nativeRenderMesh, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public void addMesh(final NativeRenderMesh nativeRenderMesh, final float n, final float n2, final float n3) {
        this.addMesh(nativeRenderMesh, n, n2, n3, 1.0f, 1.0f, 1.0f);
    }
    
    public void addMesh(final NativeRenderMesh nativeRenderMesh, final float n, final float n2, final float n3, final float n4, final float n5, final float n6) {
        if (nativeRenderMesh != null) {
            nativeAddMeshToMesh(this.ptr, nativeRenderMesh.ptr, n, n2, n3, n4, n5, n6);
        }
    }
    
    public void addVertex(final float n, final float n2, final float n3) {
        nativeAddVertex(this.ptr, n, n2, n3, 0.5f, 0.5f);
    }
    
    public void addVertex(final float n, final float n2, final float n3, final float n4, final float n5) {
        nativeAddVertex(this.ptr, n, n2, n3, n4, n5);
    }
    
    public void clear() {
        nativeClear(this.ptr);
        this.invalidate();
    }
    
    public NativeRenderMesh clone() {
        final NativeRenderMesh nativeRenderMesh = new NativeRenderMesh();
        if (this.mBlockTextureName != null) {
            nativeRenderMesh.setBlockTexture(this.mBlockTextureName, this.mBlockTextureId);
        }
        nativeRenderMesh.addMesh(this);
        return nativeRenderMesh;
    }
    
    public void fitIn(final float n, final float n2, final float n3, final float n4, final float n5, final float n6) {
        this.fitIn(n, n2, n3, n4, n5, n6, true);
    }
    
    public void fitIn(final float n, final float n2, final float n3, final float n4, final float n5, final float n6, final boolean b) {
        nativeFitIn(this.ptr, n, n2, n3, n4, n5, n6, b);
        this.invalidate();
    }
    
    public long getPtr() {
        return this.ptr;
    }
    
    public ReadOnlyVertexData getReadOnlyVertexData() {
        final ReadOnlyVertexData readOnlyVertexData = new ReadOnlyVertexData(nativeGetMeshDataSize(this.ptr));
        nativeGetMeshData(this.ptr, readOnlyVertexData.indices, readOnlyVertexData.vertices, readOnlyVertexData.colors, readOnlyVertexData.uvs);
        return readOnlyVertexData;
    }
    
    public void importFromFile(String s, String lowerCase, Scriptable empty) {
        final String redirectedPath = AssetPatch.getRedirectedPath(s);
        final StringBuilder sb = new StringBuilder();
        sb.append("models/");
        sb.append(s);
        final String redirectedPath2 = AssetPatch.getRedirectedPath(sb.toString());
        if (redirectedPath2 != null && new File(redirectedPath2).exists()) {
            s = redirectedPath2;
        }
        else if (redirectedPath2 != null && new File(redirectedPath).exists()) {
            s = redirectedPath;
        }
        else if (!new File(s).exists()) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Cannot import RenderMesh, file does not exists, tried paths: ");
            sb2.append(s);
            sb2.append("; resources://");
            sb2.append(s);
            sb2.append(" (redirected to ");
            sb2.append(redirectedPath);
            sb2.append("), resources://models/");
            sb2.append(s);
            sb2.append(" (redirected to ");
            sb2.append(redirectedPath2);
            sb2.append(")");
            throw new IllegalArgumentException(sb2.toString());
        }
        lowerCase = lowerCase.toLowerCase();
        int n = -1;
        if (lowerCase.hashCode() == 109815) {
            if (lowerCase.equals("obj")) {
                n = 0;
            }
        }
        if (n != 0) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Cannot import RenderMesh, invalid file type given: ");
            sb3.append(lowerCase);
            throw new IllegalArgumentException(sb3.toString());
        }
        if (empty == null) {
            empty = (Scriptable)ScriptableObjectHelper.createEmpty();
        }
        final ScriptableObjectWrapper scriptableObjectWrapper = new ScriptableObjectWrapper(empty);
        if (scriptableObjectWrapper.getBoolean("clear")) {
            this.clear();
        }
        final float[] vec3Template = scriptableObjectWrapper.getVec3Template("translate", 0.0f);
        final float[] vec3Template2 = scriptableObjectWrapper.getVec3Template("scale", 1.0f);
        nativeImportFromFile(this.ptr, s, lowerCase, vec3Template[0], vec3Template[1], vec3Template[2], vec3Template2[0], vec3Template2[1], vec3Template2[2], scriptableObjectWrapper.getBoolean("invertV"));
        if (!scriptableObjectWrapper.getBoolean("noRebuild")) {
            this.rebuild();
            return;
        }
        this.invalidate();
    }
    
    public void invalidate() {
        nativeInvalidate(this.ptr);
    }
    
    public GuiRenderMesh newGuiRenderMesh() {
        final int nativeGetMeshDataSize = nativeGetMeshDataSize(this.ptr);
        nativeGetMeshData(this.ptr, new short[nativeGetMeshDataSize], new float[nativeGetMeshDataSize * 3], new float[nativeGetMeshDataSize * 4], new float[nativeGetMeshDataSize * 2]);
        final float n = -500.0f;
        final float n2 = -500.0f;
        final float n3 = -500.0f;
        final float n4 = -500.0f;
        final float n5 = -500.0f;
        final float n6 = -500.0f;
        final float n7 = -500.0f;
        final float n8 = -500.0f;
        final float n9 = -500.0f;
        final float n10 = -500.0f;
        final float n11 = -500.0f;
        final float n12 = -500.0f;
        final GuiRenderMesh guiRenderMesh = new GuiRenderMesh();
        guiRenderMesh.setIndices(new short[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 });
        guiRenderMesh.setVertices(new float[] { n, n2, 0.0f, n3, 500.0f, 0.0f, 500.0f, 500.0f, 0.0f, n4, n5, 0.0f, 500.0f, 500.0f, 0.0f, n6, 500.0f, 0.0f, n7, n8, 0.0f, 500.0f, n9, 0.0f, 500.0f, 500.0f, 0.0f, n10, n11, 0.0f, 500.0f, 500.0f, 0.0f, 500.0f, n12, 0.0f });
        return guiRenderMesh;
    }
    
    public void rebuild() {
        nativeRebuild(this.ptr);
        this.invalidate();
    }
    
    public void resetColor() {
        nativeResetColor(this.ptr);
    }
    
    public void resetTexture() {
        nativeResetTargetTexture(this.ptr);
        this.mBlockTextureName = null;
        this.mBlockTextureId = 0;
    }
    
    public void rotate(final float n, final float n2, final float n3) {
        this.rotate(0.0f, 0.0f, 0.0f, n, n2, n3);
    }
    
    public void rotate(final float n, final float n2, final float n3, final float n4, final float n5, final float n6) {
        nativeRotate(this.ptr, n, n2, n3, n4, n5, n6);
        this.invalidate();
    }
    
    public void scale(final float n, final float n2, final float n3) {
        nativeScale(this.ptr, n, n2, n3);
        this.invalidate();
    }
    
    public void setBlockTexture(final String mBlockTextureName, final int mBlockTextureId) {
        if (ResourcePackManager.isValidBlockTexture(mBlockTextureName, mBlockTextureId)) {
            nativeSetTargetBlockTexture(this.ptr, mBlockTextureName, mBlockTextureId);
            this.mBlockTextureName = mBlockTextureName;
            this.mBlockTextureId = mBlockTextureId;
            return;
        }
        nativeSetTargetBlockTexture(this.ptr, "missing_block", 0);
        this.mBlockTextureName = "missing_block";
        this.mBlockTextureId = 0;
    }
    
    public void setColor(final float n, final float n2, final float n3) {
        this.setColor(n, n2, n3, 1.0f);
    }
    
    public void setColor(final float n, final float n2, final float n3, final float n4) {
        nativeSetColor(this.ptr, n, n2, n3, n4);
    }
    
    public void setFoliageTinted() {
        this.setFoliageTinted(0);
    }
    
    public void setFoliageTinted(final int n) {
        nativeSetTintSource(this.ptr, true, false, false, n);
    }
    
    public void setGrassTinted() {
        nativeSetTintSource(this.ptr, false, true, false, 0);
    }
    
    public void setLightDir(final float n, final float n2, final float n3) {
        nativeSetLightDir(this.ptr, n, n2, n3);
    }
    
    public void setLightIgnore(final boolean b, final boolean b2) {
        nativeSetLightIgnore(this.ptr, b, b2);
    }
    
    public void setLightParams(final float n, final float n2, final float n3) {
        nativeSetLightParams(this.ptr, n, n2, n3);
    }
    
    public void setLightPos(final int n, final int n2, final int n3) {
        nativeSetLightPos(this.ptr, n, n2, n3);
    }
    
    public void setNoTint() {
        nativeSetTintSource(this.ptr, false, false, false, 0);
    }
    
    public void setNormal(final float n, final float n2, final float n3) {
        nativeSetNormal(this.ptr, n, n2, n3);
    }
    
    public void setWaterTinted() {
        nativeSetTintSource(this.ptr, false, false, true, 0);
    }
    
    public void translate(final float n, final float n2, final float n3) {
        nativeTranslate(this.ptr, n, n2, n3);
        this.invalidate();
    }
    
    public class ReadOnlyVertexData
    {
        public final float[] colors;
        public final int dataSize;
        public final short[] indices;
        public final float[] uvs;
        public final float[] vertices;
        
        private ReadOnlyVertexData(final int dataSize) {
            this.dataSize = dataSize;
            this.indices = new short[dataSize];
            this.vertices = new float[dataSize * 3];
            this.colors = new float[dataSize * 4];
            this.uvs = new float[dataSize * 2];
        }
    }
}
