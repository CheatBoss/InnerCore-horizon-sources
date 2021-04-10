package android.support.v4.print;

import android.content.*;
import android.net.*;
import android.graphics.*;
import android.util.*;
import android.print.pdf.*;
import java.io.*;
import android.graphics.pdf.*;
import android.print.*;
import android.os.*;

class PrintHelperKitkat
{
    public static final int COLOR_MODE_COLOR = 2;
    public static final int COLOR_MODE_MONOCHROME = 1;
    private static final String LOG_TAG = "PrintHelperKitkat";
    private static final int MAX_PRINT_SIZE = 3500;
    public static final int ORIENTATION_LANDSCAPE = 1;
    public static final int ORIENTATION_PORTRAIT = 2;
    public static final int SCALE_MODE_FILL = 2;
    public static final int SCALE_MODE_FIT = 1;
    int mColorMode;
    final Context mContext;
    BitmapFactory$Options mDecodeOptions;
    private final Object mLock;
    int mOrientation;
    int mScaleMode;
    
    PrintHelperKitkat(final Context mContext) {
        this.mDecodeOptions = null;
        this.mLock = new Object();
        this.mScaleMode = 2;
        this.mColorMode = 2;
        this.mOrientation = 1;
        this.mContext = mContext;
    }
    
    private Bitmap convertBitmapForColorMode(final Bitmap bitmap, final int n) {
        if (n != 1) {
            return bitmap;
        }
        final Bitmap bitmap2 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap$Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap2);
        final Paint paint = new Paint();
        final ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0.0f);
        paint.setColorFilter((ColorFilter)new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        canvas.setBitmap((Bitmap)null);
        return bitmap2;
    }
    
    private Matrix getMatrix(final int n, final int n2, final RectF rectF, final int n3) {
        final Matrix matrix = new Matrix();
        final float width = rectF.width();
        final float n4 = (float)n;
        final float n5 = width / n4;
        float n6;
        if (n3 == 2) {
            n6 = Math.max(n5, rectF.height() / n2);
        }
        else {
            n6 = Math.min(n5, rectF.height() / n2);
        }
        matrix.postScale(n6, n6);
        matrix.postTranslate((rectF.width() - n4 * n6) / 2.0f, (rectF.height() - n2 * n6) / 2.0f);
        return matrix;
    }
    
    private Bitmap loadBitmap(Uri openInputStream, BitmapFactory$Options decodeStream) throws FileNotFoundException {
        if (openInputStream == null || this.mContext == null) {
            throw new IllegalArgumentException("bad argument to loadBitmap");
        }
        final InputStream inputStream = null;
        InputStream inputStream2;
        try {
            openInputStream = (IOException)this.mContext.getContentResolver().openInputStream((Uri)openInputStream);
            try {
                decodeStream = (BitmapFactory$Options)BitmapFactory.decodeStream((InputStream)openInputStream, (Rect)null, decodeStream);
                if (openInputStream != null) {
                    try {
                        ((InputStream)openInputStream).close();
                        return (Bitmap)decodeStream;
                    }
                    catch (IOException openInputStream) {
                        Log.w("PrintHelperKitkat", "close fail ", (Throwable)openInputStream);
                    }
                }
                return (Bitmap)decodeStream;
            }
            finally {}
        }
        finally {
            inputStream2 = inputStream;
        }
        if (inputStream2 != null) {
            try {
                inputStream2.close();
            }
            catch (IOException ex) {
                Log.w("PrintHelperKitkat", "close fail ", (Throwable)ex);
            }
        }
    }
    
    private Bitmap loadConstrainedBitmap(final Uri uri, final int n) throws FileNotFoundException {
        if (n > 0 && uri != null && this.mContext != null) {
            final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
            bitmapFactory$Options.inJustDecodeBounds = true;
            this.loadBitmap(uri, bitmapFactory$Options);
            final int outWidth = bitmapFactory$Options.outWidth;
            final int outHeight = bitmapFactory$Options.outHeight;
            if (outWidth > 0) {
                if (outHeight <= 0) {
                    return null;
                }
                int i;
                int inSampleSize;
                for (i = Math.max(outWidth, outHeight), inSampleSize = 1; i > n; i >>>= 1, inSampleSize <<= 1) {}
                if (inSampleSize > 0 && Math.min(outWidth, outHeight) / inSampleSize > 0) {
                    final Object mLock = this.mLock;
                    synchronized (mLock) {
                        this.mDecodeOptions = new BitmapFactory$Options();
                        this.mDecodeOptions.inMutable = true;
                        this.mDecodeOptions.inSampleSize = inSampleSize;
                        final BitmapFactory$Options mDecodeOptions = this.mDecodeOptions;
                        // monitorexit(mLock)
                        try {
                            final Bitmap loadBitmap = this.loadBitmap(uri, mDecodeOptions);
                            synchronized (this.mLock) {
                                this.mDecodeOptions = null;
                                return loadBitmap;
                            }
                        }
                        finally {
                            synchronized (this.mLock) {
                                this.mDecodeOptions = null;
                            }
                            // monitorexit(this.mLock)
                        }
                    }
                }
            }
            return null;
        }
        throw new IllegalArgumentException("bad argument to getScaledBitmap");
    }
    
    public int getColorMode() {
        return this.mColorMode;
    }
    
    public int getOrientation() {
        return this.mOrientation;
    }
    
    public int getScaleMode() {
        return this.mScaleMode;
    }
    
    public void printBitmap(final String s, final Bitmap bitmap, final OnPrintFinishCallback onPrintFinishCallback) {
        if (bitmap == null) {
            return;
        }
        final int mScaleMode = this.mScaleMode;
        final PrintManager printManager = (PrintManager)this.mContext.getSystemService("print");
        PrintAttributes$MediaSize mediaSize = PrintAttributes$MediaSize.UNKNOWN_PORTRAIT;
        if (bitmap.getWidth() > bitmap.getHeight()) {
            mediaSize = PrintAttributes$MediaSize.UNKNOWN_LANDSCAPE;
        }
        printManager.print(s, (PrintDocumentAdapter)new PrintDocumentAdapter() {
            private PrintAttributes mAttributes;
            
            public void onFinish() {
                if (onPrintFinishCallback != null) {
                    onPrintFinishCallback.onFinish();
                }
            }
            
            public void onLayout(final PrintAttributes printAttributes, final PrintAttributes mAttributes, final CancellationSignal cancellationSignal, final PrintDocumentAdapter$LayoutResultCallback printDocumentAdapter$LayoutResultCallback, final Bundle bundle) {
                this.mAttributes = mAttributes;
                printDocumentAdapter$LayoutResultCallback.onLayoutFinished(new PrintDocumentInfo$Builder(s).setContentType(1).setPageCount(1).build(), mAttributes.equals((Object)printAttributes) ^ true);
            }
            
            public void onWrite(PageRange[] access$000, final ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, final PrintDocumentAdapter$WriteResultCallback printDocumentAdapter$WriteResultCallback) {
                cancellationSignal = (CancellationSignal)new PrintedPdfDocument(PrintHelperKitkat.this.mContext, this.mAttributes);
                access$000 = (PageRange[])(Object)PrintHelperKitkat.this.convertBitmapForColorMode(bitmap, this.mAttributes.getColorMode());
                try {
                    final PdfDocument$Page startPage = ((PrintedPdfDocument)cancellationSignal).startPage(1);
                    startPage.getCanvas().drawBitmap((Bitmap)(Object)access$000, PrintHelperKitkat.this.getMatrix(((Bitmap)(Object)access$000).getWidth(), ((Bitmap)(Object)access$000).getHeight(), new RectF(startPage.getInfo().getContentRect()), mScaleMode), (Paint)null);
                    ((PrintedPdfDocument)cancellationSignal).finishPage(startPage);
                    try {
                        ((PrintedPdfDocument)cancellationSignal).writeTo((OutputStream)new FileOutputStream(parcelFileDescriptor.getFileDescriptor()));
                        printDocumentAdapter$WriteResultCallback.onWriteFinished(new PageRange[] { PageRange.ALL_PAGES });
                    }
                    catch (IOException ex) {
                        Log.e("PrintHelperKitkat", "Error writing printed content", (Throwable)ex);
                        printDocumentAdapter$WriteResultCallback.onWriteFailed((CharSequence)null);
                    }
                }
                finally {
                    if (cancellationSignal != null) {
                        ((PrintedPdfDocument)cancellationSignal).close();
                    }
                    if (parcelFileDescriptor != null) {
                        try {
                            parcelFileDescriptor.close();
                        }
                        catch (IOException ex2) {}
                    }
                    if (access$000 != bitmap) {
                        ((Bitmap)(Object)access$000).recycle();
                    }
                }
            }
        }, new PrintAttributes$Builder().setMediaSize(mediaSize).setColorMode(this.mColorMode).build());
    }
    
    public void printBitmap(final String s, final Uri uri, final OnPrintFinishCallback onPrintFinishCallback) throws FileNotFoundException {
        final PrintDocumentAdapter printDocumentAdapter = new PrintDocumentAdapter() {
            private PrintAttributes mAttributes;
            Bitmap mBitmap = null;
            AsyncTask<Uri, Boolean, Bitmap> mLoadBitmap;
            final /* synthetic */ int val$fittingMode = PrintHelperKitkat.this.mScaleMode;
            
            private void cancelLoad() {
                synchronized (PrintHelperKitkat.this.mLock) {
                    if (PrintHelperKitkat.this.mDecodeOptions != null) {
                        PrintHelperKitkat.this.mDecodeOptions.requestCancelDecode();
                        PrintHelperKitkat.this.mDecodeOptions = null;
                    }
                }
            }
            
            public void onFinish() {
                super.onFinish();
                this.cancelLoad();
                if (this.mLoadBitmap != null) {
                    this.mLoadBitmap.cancel(true);
                }
                if (onPrintFinishCallback != null) {
                    onPrintFinishCallback.onFinish();
                }
                if (this.mBitmap != null) {
                    this.mBitmap.recycle();
                    this.mBitmap = null;
                }
            }
            
            public void onLayout(final PrintAttributes printAttributes, final PrintAttributes mAttributes, final CancellationSignal cancellationSignal, final PrintDocumentAdapter$LayoutResultCallback printDocumentAdapter$LayoutResultCallback, final Bundle bundle) {
                this.mAttributes = mAttributes;
                if (cancellationSignal.isCanceled()) {
                    printDocumentAdapter$LayoutResultCallback.onLayoutCancelled();
                    return;
                }
                if (this.mBitmap != null) {
                    printDocumentAdapter$LayoutResultCallback.onLayoutFinished(new PrintDocumentInfo$Builder(s).setContentType(1).setPageCount(1).build(), mAttributes.equals((Object)printAttributes) ^ true);
                    return;
                }
                this.mLoadBitmap = (AsyncTask<Uri, Boolean, Bitmap>)new AsyncTask<Uri, Boolean, Bitmap>() {
                    protected Bitmap doInBackground(final Uri... array) {
                        try {
                            return PrintHelperKitkat.this.loadConstrainedBitmap(uri, 3500);
                        }
                        catch (FileNotFoundException ex) {
                            return null;
                        }
                    }
                    
                    protected void onCancelled(final Bitmap bitmap) {
                        printDocumentAdapter$LayoutResultCallback.onLayoutCancelled();
                        PrintDocumentAdapter.this.mLoadBitmap = null;
                    }
                    
                    protected void onPostExecute(final Bitmap mBitmap) {
                        super.onPostExecute((Object)mBitmap);
                        PrintDocumentAdapter.this.mBitmap = mBitmap;
                        if (mBitmap != null) {
                            printDocumentAdapter$LayoutResultCallback.onLayoutFinished(new PrintDocumentInfo$Builder(s).setContentType(1).setPageCount(1).build(), true ^ mAttributes.equals((Object)printAttributes));
                        }
                        else {
                            printDocumentAdapter$LayoutResultCallback.onLayoutFailed((CharSequence)null);
                        }
                        PrintDocumentAdapter.this.mLoadBitmap = null;
                    }
                    
                    protected void onPreExecute() {
                        cancellationSignal.setOnCancelListener((CancellationSignal$OnCancelListener)new CancellationSignal$OnCancelListener() {
                            public void onCancel() {
                                PrintHelperKitkat$2.this.cancelLoad();
                                AsyncTask.this.cancel(false);
                            }
                        });
                    }
                }.execute((Object[])new Uri[0]);
            }
            
            public void onWrite(PageRange[] access$000, final ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, final PrintDocumentAdapter$WriteResultCallback printDocumentAdapter$WriteResultCallback) {
                cancellationSignal = (CancellationSignal)new PrintedPdfDocument(PrintHelperKitkat.this.mContext, this.mAttributes);
                access$000 = (PageRange[])(Object)PrintHelperKitkat.this.convertBitmapForColorMode(this.mBitmap, this.mAttributes.getColorMode());
                try {
                    final PdfDocument$Page startPage = ((PrintedPdfDocument)cancellationSignal).startPage(1);
                    startPage.getCanvas().drawBitmap((Bitmap)(Object)access$000, PrintHelperKitkat.this.getMatrix(this.mBitmap.getWidth(), this.mBitmap.getHeight(), new RectF(startPage.getInfo().getContentRect()), this.val$fittingMode), (Paint)null);
                    ((PrintedPdfDocument)cancellationSignal).finishPage(startPage);
                    try {
                        ((PrintedPdfDocument)cancellationSignal).writeTo((OutputStream)new FileOutputStream(parcelFileDescriptor.getFileDescriptor()));
                        printDocumentAdapter$WriteResultCallback.onWriteFinished(new PageRange[] { PageRange.ALL_PAGES });
                    }
                    catch (IOException ex) {
                        Log.e("PrintHelperKitkat", "Error writing printed content", (Throwable)ex);
                        printDocumentAdapter$WriteResultCallback.onWriteFailed((CharSequence)null);
                    }
                }
                finally {
                    if (cancellationSignal != null) {
                        ((PrintedPdfDocument)cancellationSignal).close();
                    }
                    if (parcelFileDescriptor != null) {
                        try {
                            parcelFileDescriptor.close();
                        }
                        catch (IOException ex2) {}
                    }
                    if (access$000 != this.mBitmap) {
                        ((Bitmap)(Object)access$000).recycle();
                    }
                }
            }
        };
        final PrintManager printManager = (PrintManager)this.mContext.getSystemService("print");
        final PrintAttributes$Builder printAttributes$Builder = new PrintAttributes$Builder();
        printAttributes$Builder.setColorMode(this.mColorMode);
        Label_0086: {
            PrintAttributes$MediaSize mediaSize;
            if (this.mOrientation == 1) {
                mediaSize = PrintAttributes$MediaSize.UNKNOWN_LANDSCAPE;
            }
            else {
                if (this.mOrientation != 2) {
                    break Label_0086;
                }
                mediaSize = PrintAttributes$MediaSize.UNKNOWN_PORTRAIT;
            }
            printAttributes$Builder.setMediaSize(mediaSize);
        }
        printManager.print(s, (PrintDocumentAdapter)printDocumentAdapter, printAttributes$Builder.build());
    }
    
    public void setColorMode(final int mColorMode) {
        this.mColorMode = mColorMode;
    }
    
    public void setOrientation(final int mOrientation) {
        this.mOrientation = mOrientation;
    }
    
    public void setScaleMode(final int mScaleMode) {
        this.mScaleMode = mScaleMode;
    }
    
    public interface OnPrintFinishCallback
    {
        void onFinish();
    }
}
