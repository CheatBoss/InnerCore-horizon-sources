package com.zhekasmirnov.innercore.api.mod.ui;

import android.graphics.*;
import javax.microedition.khronos.opengles.*;
import android.opengl.*;
import java.nio.*;

public class GuiRenderMesh
{
    private Bitmap mBitmap;
    private FloatBuffer mColorBuffer;
    private ShortBuffer mIndicesBuffer;
    private int mNumOfIndices;
    private final float[] mRGBA;
    private boolean mShouldLoadTexture;
    private FloatBuffer mTextureBuffer;
    private int mTextureId;
    private FloatBuffer mVerticesBuffer;
    public float rx;
    public float ry;
    public float rz;
    public float x;
    public float y;
    public float z;
    
    public GuiRenderMesh() {
        this.mVerticesBuffer = null;
        this.mIndicesBuffer = null;
        this.mTextureId = -1;
        this.mShouldLoadTexture = false;
        this.mNumOfIndices = -1;
        this.mRGBA = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
        this.mColorBuffer = null;
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
        this.rx = 0.0f;
        this.ry = 0.0f;
        this.rz = 0.0f;
    }
    
    private void loadGLTexture(final GL10 gl10) {
        final int[] array = { 0 };
        gl10.glGenTextures(1, array, 0);
        gl10.glBindTexture(3553, this.mTextureId = array[0]);
        gl10.glTexParameterf(3553, 10241, 9729.0f);
        gl10.glTexParameterf(3553, 10240, 9729.0f);
        gl10.glTexParameterf(3553, 10242, 33071.0f);
        gl10.glTexParameterf(3553, 10243, 10497.0f);
        GLUtils.texImage2D(3553, 0, this.mBitmap, 0);
    }
    
    public void draw(final GL10 gl10) {
        gl10.glFrontFace(2305);
        gl10.glEnable(2884);
        gl10.glCullFace(1029);
        gl10.glEnableClientState(32884);
        gl10.glVertexPointer(3, 5126, 0, (Buffer)this.mVerticesBuffer);
        gl10.glColor4f(this.mRGBA[0], this.mRGBA[1], this.mRGBA[2], this.mRGBA[3]);
        if (this.mColorBuffer != null) {
            gl10.glEnableClientState(32886);
            gl10.glColorPointer(4, 5126, 0, (Buffer)this.mColorBuffer);
        }
        if (this.mShouldLoadTexture) {
            this.loadGLTexture(gl10);
            this.mShouldLoadTexture = false;
        }
        if (this.mTextureId != -1 && this.mTextureBuffer != null) {
            gl10.glEnable(3553);
            gl10.glEnableClientState(32888);
            gl10.glTexCoordPointer(2, 5126, 0, (Buffer)this.mTextureBuffer);
            gl10.glBindTexture(3553, this.mTextureId);
        }
        gl10.glTranslatef(this.x, this.y, this.z);
        gl10.glRotatef(this.rx, 1.0f, 0.0f, 0.0f);
        gl10.glRotatef(this.ry, 0.0f, 1.0f, 0.0f);
        gl10.glRotatef(this.rz, 0.0f, 0.0f, 1.0f);
        gl10.glDrawElements(4, this.mNumOfIndices, 5123, (Buffer)this.mIndicesBuffer);
        gl10.glDisableClientState(32884);
        if (this.mTextureId != -1 && this.mTextureBuffer != null) {
            gl10.glDisableClientState(32888);
        }
        gl10.glDisable(2884);
    }
    
    public void loadBitmap(final Bitmap mBitmap) {
        this.mBitmap = mBitmap;
        this.mShouldLoadTexture = true;
    }
    
    protected void setColor(final float n, final float n2, final float n3, final float n4) {
        this.mRGBA[0] = n;
        this.mRGBA[1] = n2;
        this.mRGBA[2] = n3;
        this.mRGBA[3] = n4;
    }
    
    public void setColors(final float[] array) {
        final ByteBuffer allocateDirect = ByteBuffer.allocateDirect(array.length * 4);
        allocateDirect.order(ByteOrder.nativeOrder());
        (this.mColorBuffer = allocateDirect.asFloatBuffer()).put(array);
        this.mColorBuffer.position(0);
    }
    
    public void setIndices(final short[] array) {
        final ByteBuffer allocateDirect = ByteBuffer.allocateDirect(array.length * 2);
        allocateDirect.order(ByteOrder.nativeOrder());
        (this.mIndicesBuffer = allocateDirect.asShortBuffer()).put(array);
        this.mIndicesBuffer.position(0);
        this.mNumOfIndices = array.length;
    }
    
    public void setTextureCoordinates(final float[] array) {
        final ByteBuffer allocateDirect = ByteBuffer.allocateDirect(array.length * 4);
        allocateDirect.order(ByteOrder.nativeOrder());
        (this.mTextureBuffer = allocateDirect.asFloatBuffer()).put(array);
        this.mTextureBuffer.position(0);
    }
    
    public void setVertices(final float[] array) {
        final ByteBuffer allocateDirect = ByteBuffer.allocateDirect(array.length * 4);
        allocateDirect.order(ByteOrder.nativeOrder());
        (this.mVerticesBuffer = allocateDirect.asFloatBuffer()).put(array);
        this.mVerticesBuffer.position(0);
    }
}
