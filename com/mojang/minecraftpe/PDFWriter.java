package com.mojang.minecraftpe;

import android.text.*;
import android.graphics.pdf.*;
import android.graphics.*;
import android.os.*;
import java.io.*;

public class PDFWriter
{
    private Rect mImageRect;
    private PdfDocument mOpenDocument;
    private Rect mPageRect;
    private TextPaint mPageTextPaint;
    private Rect mTextRect;
    private Rect mTitleRect;
    private TextPaint mTitleTextPaint;
    
    public PDFWriter() {
        this.mPageRect = new Rect(0, 0, 612, 792);
        (this.mTitleRect = new Rect(0, 0, this.mPageRect.width(), (int)(this.mPageRect.height() * 0.7f))).offset(0, (int)(this.mPageRect.height() * 0.3f));
        (this.mTextRect = new Rect(this.mPageRect)).inset(20, 20);
        (this.mImageRect = new Rect(0, 0, 500, 500)).offset(this.mPageRect.centerX() - this.mImageRect.centerX(), this.mPageRect.centerY() - this.mImageRect.centerY());
        Typeface typeface = Typeface.DEFAULT_BOLD;
        try {
            typeface = Typeface.createFromAsset(MainActivity.mInstance.getAssets(), "fonts/Mojangles.ttf");
        }
        catch (Exception ex) {
            final PrintStream out = System.out;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to load mojangles font: ");
            sb.append(ex.getMessage());
            out.println(sb.toString());
        }
        (this.mTitleTextPaint = new TextPaint()).setAntiAlias(true);
        this.mTitleTextPaint.setTextSize(64.0f);
        this.mTitleTextPaint.setColor(-16777216);
        this.mTitleTextPaint.setTypeface(typeface);
        (this.mPageTextPaint = new TextPaint()).setAntiAlias(true);
        this.mPageTextPaint.setTextSize(32.0f);
        this.mPageTextPaint.setColor(-16777216);
    }
    
    private void _drawTextInRect(final String s, final PdfDocument$Page pdfDocument$Page, final TextPaint textPaint, final Rect rect, final Layout$Alignment layout$Alignment) {
        final StaticLayout staticLayout = new StaticLayout((CharSequence)s, textPaint, rect.width(), layout$Alignment, 1.0f, 0.0f, false);
        final Canvas canvas = pdfDocument$Page.getCanvas();
        canvas.translate((float)rect.left, (float)rect.top);
        staticLayout.draw(canvas);
    }
    
    private String _getExtension(final String s) {
        final int lastIndex = s.lastIndexOf(46);
        if (lastIndex >= 0) {
            final int n = lastIndex + 1;
            if (n < s.length()) {
                return s.substring(n).toLowerCase();
            }
        }
        return "";
    }
    
    private PdfDocument$PageInfo _getPageInfo(final int n) {
        return new PdfDocument$PageInfo$Builder(this.mPageRect.width(), this.mPageRect.height(), n).create();
    }
    
    private String _readFileToString(final String s) throws FileNotFoundException, IOException {
        final File file = new File(s);
        final FileInputStream fileInputStream = new FileInputStream(file);
        final byte[] array = new byte[(int)file.length()];
        fileInputStream.read(array);
        fileInputStream.close();
        return new String(array);
    }
    
    public void closeDocument() {
        final PdfDocument mOpenDocument = this.mOpenDocument;
        if (mOpenDocument != null) {
            mOpenDocument.close();
            this.mOpenDocument = null;
        }
    }
    
    public boolean createDocument(final String[] array, String s) {
        final PdfDocument mOpenDocument = this.mOpenDocument;
        if (mOpenDocument != null) {
            mOpenDocument.close();
        }
        final PdfDocument mOpenDocument2 = new PdfDocument();
        this.mOpenDocument = mOpenDocument2;
        final PdfDocument$Page startPage = mOpenDocument2.startPage(this._getPageInfo(1));
        this._drawTextInRect(s, startPage, this.mTitleTextPaint, this.mTitleRect, Layout$Alignment.ALIGN_CENTER);
        this.mOpenDocument.finishPage(startPage);
        int i = 0;
        while (i < array.length) {
            s = array[i];
            final PdfDocument$Page startPage2 = this.mOpenDocument.startPage(this._getPageInfo(i + 2));
            try {
                final String getExtension = this._getExtension(s);
                if (getExtension.equals("txt")) {
                    this._drawTextInRect(this._readFileToString(s), startPage2, this.mPageTextPaint, this.mTextRect, Layout$Alignment.ALIGN_NORMAL);
                }
                else {
                    if (!getExtension.equals("jpeg")) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Unsupported extension from file: ");
                        sb.append(s);
                        throw new UnsupportedOperationException(sb.toString());
                    }
                    startPage2.getCanvas().drawBitmap(BitmapFactory.decodeFile(s), (Rect)null, this.mImageRect, (Paint)null);
                }
                this.mOpenDocument.finishPage(startPage2);
                ++i;
                continue;
            }
            catch (Exception ex) {
                final PrintStream out = System.out;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Failed to write page: ");
                sb2.append(ex.getMessage());
                out.println(sb2.toString());
                this.closeDocument();
                return false;
            }
            break;
        }
        return true;
    }
    
    public String getPicturesDirectory() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
    }
    
    public boolean writeDocumentToFile(final String s) {
        try {
            this.mOpenDocument.writeTo((OutputStream)new FileOutputStream(s));
            return true;
        }
        catch (Exception ex) {
            final PrintStream out = System.out;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to write pdf file: ");
            sb.append(ex.getMessage());
            out.println(sb.toString());
            return false;
        }
    }
}
