package com.appboy.support;

import android.net.*;
import java.io.*;
import android.graphics.*;
import android.content.*;
import com.appboy.enums.*;
import android.util.*;
import android.view.*;

public class AppboyImageUtils
{
    private static final String a;
    
    static {
        a = AppboyLogger.getAppboyLogTag(AppboyImageUtils.class);
    }
    
    static int a(final BitmapFactory$Options bitmapFactory$Options, final int n, final int n2) {
        final int outHeight = bitmapFactory$Options.outHeight;
        final int outWidth = bitmapFactory$Options.outWidth;
        final String a = AppboyImageUtils.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("Calculating sample size for source image bounds: (height ");
        sb.append(bitmapFactory$Options.outHeight);
        sb.append(" width ");
        sb.append(bitmapFactory$Options.outWidth);
        sb.append(") and destination image bounds: (height ");
        sb.append(n2);
        sb.append(" width ");
        sb.append(n);
        sb.append(")");
        AppboyLogger.d(a, sb.toString());
        int n3 = 1;
        int n4 = 1;
        if (outHeight > n2 || outWidth > n) {
            final int n5 = outHeight / 2;
            final int n6 = outWidth / 2;
            while (true) {
                n3 = n4;
                if (n5 / n4 < n2) {
                    break;
                }
                n3 = n4;
                if (n6 / n4 < n) {
                    break;
                }
                n4 *= 2;
            }
        }
        final String a2 = AppboyImageUtils.a;
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Using image sample size of ");
        sb2.append(n3);
        AppboyLogger.d(a2, sb2.toString());
        return n3;
    }
    
    private static Bitmap a(Uri ex) {
        String s;
        StringBuilder sb2;
        String string;
        try {
            final File file = new File(((Uri)ex).getPath());
            if (file.exists()) {
                final String a = AppboyImageUtils.a;
                final StringBuilder sb = new StringBuilder();
                sb.append("Retrieving image from path: ");
                sb.append(file.getAbsolutePath());
                AppboyLogger.i(a, sb.toString());
                return BitmapFactory.decodeFile(file.getAbsolutePath());
            }
            return null;
        }
        catch (Exception ex) {
            AppboyLogger.e(AppboyImageUtils.a, "Exception occurred when attempting to retrieve local bitmap.", ex);
            return null;
        }
        catch (OutOfMemoryError ex2) {
            s = AppboyImageUtils.a;
            sb2 = new StringBuilder();
            sb2.append("Out of Memory Error in local bitmap file retrieval for Uri: ");
            sb2.append(((Uri)ex).toString());
            string = ".";
            ex = ex2;
        }
        finally {
            s = AppboyImageUtils.a;
            sb2 = new StringBuilder();
            sb2.append("Throwable caught in local bitmap file retrieval for Uri: ");
            string = ((Uri)ex).toString();
            final Exception ex3;
            ex = ex3;
        }
        sb2.append(string);
        AppboyLogger.e(s, sb2.toString(), ex);
        return null;
    }
    
    private static Bitmap a(final Uri p0, final int p1, final int p2) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   android/net/Uri.toString:()Ljava/lang/String;
        //     4: astore          8
        //     6: invokestatic    com/appboy/Appboy.getOutboundNetworkRequestsOffline:()Z
        //     9: ifeq            53
        //    12: getstatic       com/appboy/support/AppboyImageUtils.a:Ljava/lang/String;
        //    15: astore_0       
        //    16: new             Ljava/lang/StringBuilder;
        //    19: dup            
        //    20: invokespecial   java/lang/StringBuilder.<init>:()V
        //    23: astore          4
        //    25: aload           4
        //    27: ldc             "SDK is in offline mode, not downloading remote bitmap with uri: "
        //    29: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    32: pop            
        //    33: aload           4
        //    35: aload           8
        //    37: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    40: pop            
        //    41: aload_0        
        //    42: aload           4
        //    44: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    47: invokestatic    com/appboy/support/AppboyLogger.i:(Ljava/lang/String;Ljava/lang/String;)I
        //    50: pop            
        //    51: aconst_null    
        //    52: areturn        
        //    53: new             Ljava/net/URL;
        //    56: dup            
        //    57: aload           8
        //    59: invokespecial   java/net/URL.<init>:(Ljava/lang/String;)V
        //    62: astore          6
        //    64: aload           6
        //    66: invokestatic    bo/app/k.a:(Ljava/net/URL;)Ljava/net/URLConnection;
        //    69: checkcast       Ljava/net/HttpURLConnection;
        //    72: astore          5
        //    74: aload           5
        //    76: invokevirtual   java/net/HttpURLConnection.getResponseCode:()I
        //    79: istore_3       
        //    80: iload_3        
        //    81: sipush          200
        //    84: if_icmpeq       161
        //    87: getstatic       com/appboy/support/AppboyImageUtils.a:Ljava/lang/String;
        //    90: astore_0       
        //    91: new             Ljava/lang/StringBuilder;
        //    94: dup            
        //    95: invokespecial   java/lang/StringBuilder.<init>:()V
        //    98: astore          4
        //   100: aload           4
        //   102: ldc             "HTTP response code was "
        //   104: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   107: pop            
        //   108: aload           4
        //   110: iload_3        
        //   111: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   114: pop            
        //   115: aload           4
        //   117: ldc             ". Bitmap with url "
        //   119: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   122: pop            
        //   123: aload           4
        //   125: aload           6
        //   127: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   130: pop            
        //   131: aload           4
        //   133: ldc             " could not be downloaded."
        //   135: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   138: pop            
        //   139: aload_0        
        //   140: aload           4
        //   142: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   145: invokestatic    com/appboy/support/AppboyLogger.w:(Ljava/lang/String;Ljava/lang/String;)I
        //   148: pop            
        //   149: aload           5
        //   151: ifnull          159
        //   154: aload           5
        //   156: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //   159: aconst_null    
        //   160: areturn        
        //   161: aload           5
        //   163: invokevirtual   java/net/HttpURLConnection.getInputStream:()Ljava/io/InputStream;
        //   166: astore_0       
        //   167: iload_1        
        //   168: ifeq            545
        //   171: iload_2        
        //   172: ifeq            545
        //   175: getstatic       com/appboy/support/AppboyImageUtils.a:Ljava/lang/String;
        //   178: astore          4
        //   180: new             Ljava/lang/StringBuilder;
        //   183: dup            
        //   184: invokespecial   java/lang/StringBuilder.<init>:()V
        //   187: astore          7
        //   189: aload           7
        //   191: ldc             "Sampling bitmap with destination image bounds: (height "
        //   193: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   196: pop            
        //   197: aload           7
        //   199: iload_2        
        //   200: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   203: pop            
        //   204: aload           7
        //   206: ldc             " width "
        //   208: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   211: pop            
        //   212: aload           7
        //   214: iload_1        
        //   215: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   218: pop            
        //   219: aload           7
        //   221: ldc             ")"
        //   223: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   226: pop            
        //   227: aload           4
        //   229: aload           7
        //   231: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   234: invokestatic    com/appboy/support/AppboyLogger.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   237: pop            
        //   238: aload_0        
        //   239: invokestatic    com/appboy/support/AppboyImageUtils.a:(Ljava/io/InputStream;)Landroid/graphics/BitmapFactory$Options;
        //   242: astore          7
        //   244: aload           5
        //   246: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //   249: aload           6
        //   251: invokestatic    bo/app/k.a:(Ljava/net/URL;)Ljava/net/URLConnection;
        //   254: checkcast       Ljava/net/HttpURLConnection;
        //   257: astore          4
        //   259: aload           4
        //   261: invokevirtual   java/net/HttpURLConnection.getInputStream:()Ljava/io/InputStream;
        //   264: astore          5
        //   266: aload           7
        //   268: getfield        android/graphics/BitmapFactory$Options.outHeight:I
        //   271: ifeq            332
        //   274: aload           7
        //   276: getfield        android/graphics/BitmapFactory$Options.outWidth:I
        //   279: ifne            285
        //   282: goto            332
        //   285: aload           5
        //   287: aload           7
        //   289: iload_1        
        //   290: iload_2        
        //   291: invokestatic    com/appboy/support/AppboyImageUtils.a:(Ljava/io/InputStream;Landroid/graphics/BitmapFactory$Options;II)Landroid/graphics/Bitmap;
        //   294: astore_0       
        //   295: aload           4
        //   297: ifnull          305
        //   300: aload           4
        //   302: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //   305: aload           5
        //   307: ifnull          330
        //   310: aload           5
        //   312: invokevirtual   java/io/InputStream.close:()V
        //   315: aload_0        
        //   316: areturn        
        //   317: astore          4
        //   319: getstatic       com/appboy/support/AppboyImageUtils.a:Ljava/lang/String;
        //   322: ldc             "IOException during closing of bitmap metadata download stream."
        //   324: aload           4
        //   326: invokestatic    com/appboy/support/AppboyLogger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   329: pop            
        //   330: aload_0        
        //   331: areturn        
        //   332: getstatic       com/appboy/support/AppboyImageUtils.a:Ljava/lang/String;
        //   335: astore_0       
        //   336: new             Ljava/lang/StringBuilder;
        //   339: dup            
        //   340: invokespecial   java/lang/StringBuilder.<init>:()V
        //   343: astore          9
        //   345: aload           9
        //   347: ldc             "The bitmap metadata with image url "
        //   349: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   352: pop            
        //   353: aload           9
        //   355: aload           6
        //   357: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   360: pop            
        //   361: aload           9
        //   363: ldc             " had bounds: (height "
        //   365: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   368: pop            
        //   369: aload           9
        //   371: aload           7
        //   373: getfield        android/graphics/BitmapFactory$Options.outHeight:I
        //   376: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   379: pop            
        //   380: aload           9
        //   382: ldc             " width "
        //   384: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   387: pop            
        //   388: aload           9
        //   390: aload           7
        //   392: getfield        android/graphics/BitmapFactory$Options.outWidth:I
        //   395: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   398: pop            
        //   399: aload           9
        //   401: ldc             "). Returning a bitmap with no sampling."
        //   403: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   406: pop            
        //   407: aload_0        
        //   408: aload           9
        //   410: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   413: invokestatic    com/appboy/support/AppboyLogger.w:(Ljava/lang/String;Ljava/lang/String;)I
        //   416: pop            
        //   417: aload           5
        //   419: invokestatic    android/graphics/BitmapFactory.decodeStream:(Ljava/io/InputStream;)Landroid/graphics/Bitmap;
        //   422: astore_0       
        //   423: aload           4
        //   425: ifnull          433
        //   428: aload           4
        //   430: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //   433: aload           5
        //   435: ifnull          458
        //   438: aload           5
        //   440: invokevirtual   java/io/InputStream.close:()V
        //   443: aload_0        
        //   444: areturn        
        //   445: astore          4
        //   447: getstatic       com/appboy/support/AppboyImageUtils.a:Ljava/lang/String;
        //   450: ldc             "IOException during closing of bitmap metadata download stream."
        //   452: aload           4
        //   454: invokestatic    com/appboy/support/AppboyLogger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   457: pop            
        //   458: aload_0        
        //   459: areturn        
        //   460: astore          7
        //   462: goto            490
        //   465: astore          7
        //   467: goto            502
        //   470: astore          7
        //   472: goto            514
        //   475: astore          7
        //   477: goto            526
        //   480: astore          7
        //   482: goto            538
        //   485: astore          7
        //   487: aload_0        
        //   488: astore          5
        //   490: aload           4
        //   492: astore          6
        //   494: goto            725
        //   497: astore          7
        //   499: aload_0        
        //   500: astore          5
        //   502: aload           4
        //   504: astore          6
        //   506: goto            834
        //   509: astore          7
        //   511: aload_0        
        //   512: astore          5
        //   514: aload           4
        //   516: astore          6
        //   518: goto            947
        //   521: astore          7
        //   523: aload_0        
        //   524: astore          5
        //   526: aload           4
        //   528: astore          6
        //   530: goto            1071
        //   533: astore          7
        //   535: aload_0        
        //   536: astore          5
        //   538: aload           4
        //   540: astore          6
        //   542: goto            1195
        //   545: aload_0        
        //   546: invokestatic    android/graphics/BitmapFactory.decodeStream:(Ljava/io/InputStream;)Landroid/graphics/Bitmap;
        //   549: astore          4
        //   551: aload           5
        //   553: ifnull          561
        //   556: aload           5
        //   558: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //   561: aload_0        
        //   562: ifnull          583
        //   565: aload_0        
        //   566: invokevirtual   java/io/InputStream.close:()V
        //   569: aload           4
        //   571: areturn        
        //   572: astore_0       
        //   573: getstatic       com/appboy/support/AppboyImageUtils.a:Ljava/lang/String;
        //   576: ldc             "IOException during closing of bitmap metadata download stream."
        //   578: aload_0        
        //   579: invokestatic    com/appboy/support/AppboyLogger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   582: pop            
        //   583: aload           4
        //   585: areturn        
        //   586: astore          7
        //   588: aload           5
        //   590: astore          6
        //   592: aload_0        
        //   593: astore          5
        //   595: goto            725
        //   598: astore          7
        //   600: aload           5
        //   602: astore          6
        //   604: aload_0        
        //   605: astore          5
        //   607: goto            834
        //   610: astore          7
        //   612: aload           5
        //   614: astore          6
        //   616: aload_0        
        //   617: astore          5
        //   619: goto            947
        //   622: astore          7
        //   624: aload           5
        //   626: astore          6
        //   628: aload_0        
        //   629: astore          5
        //   631: goto            1071
        //   634: astore          7
        //   636: aload           5
        //   638: astore          6
        //   640: aload_0        
        //   641: astore          5
        //   643: goto            1195
        //   646: astore          7
        //   648: aconst_null    
        //   649: astore_0       
        //   650: aload           5
        //   652: astore          6
        //   654: aload_0        
        //   655: astore          5
        //   657: goto            725
        //   660: astore          7
        //   662: aconst_null    
        //   663: astore_0       
        //   664: aload           5
        //   666: astore          6
        //   668: aload_0        
        //   669: astore          5
        //   671: goto            834
        //   674: astore          7
        //   676: aconst_null    
        //   677: astore_0       
        //   678: aload           5
        //   680: astore          6
        //   682: aload_0        
        //   683: astore          5
        //   685: goto            947
        //   688: astore          7
        //   690: aconst_null    
        //   691: astore_0       
        //   692: aload           5
        //   694: astore          6
        //   696: aload_0        
        //   697: astore          5
        //   699: goto            1071
        //   702: astore          7
        //   704: aconst_null    
        //   705: astore_0       
        //   706: aload           5
        //   708: astore          6
        //   710: aload_0        
        //   711: astore          5
        //   713: goto            1195
        //   716: astore          7
        //   718: aconst_null    
        //   719: astore          6
        //   721: aload           6
        //   723: astore          5
        //   725: aload           6
        //   727: astore_0       
        //   728: aload           5
        //   730: astore          4
        //   732: getstatic       com/appboy/support/AppboyImageUtils.a:Ljava/lang/String;
        //   735: astore          9
        //   737: aload           6
        //   739: astore_0       
        //   740: aload           5
        //   742: astore          4
        //   744: new             Ljava/lang/StringBuilder;
        //   747: dup            
        //   748: invokespecial   java/lang/StringBuilder.<init>:()V
        //   751: astore          10
        //   753: aload           6
        //   755: astore_0       
        //   756: aload           5
        //   758: astore          4
        //   760: aload           10
        //   762: ldc             "Throwable caught in image bitmap download for Uri: "
        //   764: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   767: pop            
        //   768: aload           6
        //   770: astore_0       
        //   771: aload           5
        //   773: astore          4
        //   775: aload           10
        //   777: aload           8
        //   779: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   782: pop            
        //   783: aload           6
        //   785: astore_0       
        //   786: aload           5
        //   788: astore          4
        //   790: aload           9
        //   792: aload           10
        //   794: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   797: aload           7
        //   799: invokestatic    com/appboy/support/AppboyLogger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   802: pop            
        //   803: aload           6
        //   805: ifnull          813
        //   808: aload           6
        //   810: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //   813: aload           5
        //   815: ifnull          1320
        //   818: aload           5
        //   820: invokevirtual   java/io/InputStream.close:()V
        //   823: aconst_null    
        //   824: areturn        
        //   825: astore          7
        //   827: aconst_null    
        //   828: astore          6
        //   830: aload           6
        //   832: astore          5
        //   834: aload           6
        //   836: astore_0       
        //   837: aload           5
        //   839: astore          4
        //   841: getstatic       com/appboy/support/AppboyImageUtils.a:Ljava/lang/String;
        //   844: astore          9
        //   846: aload           6
        //   848: astore_0       
        //   849: aload           5
        //   851: astore          4
        //   853: new             Ljava/lang/StringBuilder;
        //   856: dup            
        //   857: invokespecial   java/lang/StringBuilder.<init>:()V
        //   860: astore          10
        //   862: aload           6
        //   864: astore_0       
        //   865: aload           5
        //   867: astore          4
        //   869: aload           10
        //   871: ldc             "Exception in image bitmap download for Uri: "
        //   873: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   876: pop            
        //   877: aload           6
        //   879: astore_0       
        //   880: aload           5
        //   882: astore          4
        //   884: aload           10
        //   886: aload           8
        //   888: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   891: pop            
        //   892: aload           6
        //   894: astore_0       
        //   895: aload           5
        //   897: astore          4
        //   899: aload           9
        //   901: aload           10
        //   903: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   906: aload           7
        //   908: invokestatic    com/appboy/support/AppboyLogger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   911: pop            
        //   912: aload           6
        //   914: ifnull          922
        //   917: aload           6
        //   919: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //   922: aload           5
        //   924: ifnull          1320
        //   927: aload           5
        //   929: invokevirtual   java/io/InputStream.close:()V
        //   932: aconst_null    
        //   933: areturn        
        //   934: astore_0       
        //   935: goto            1310
        //   938: astore          7
        //   940: aconst_null    
        //   941: astore          6
        //   943: aload           6
        //   945: astore          5
        //   947: aload           6
        //   949: astore_0       
        //   950: aload           5
        //   952: astore          4
        //   954: getstatic       com/appboy/support/AppboyImageUtils.a:Ljava/lang/String;
        //   957: astore          9
        //   959: aload           6
        //   961: astore_0       
        //   962: aload           5
        //   964: astore          4
        //   966: new             Ljava/lang/StringBuilder;
        //   969: dup            
        //   970: invokespecial   java/lang/StringBuilder.<init>:()V
        //   973: astore          10
        //   975: aload           6
        //   977: astore_0       
        //   978: aload           5
        //   980: astore          4
        //   982: aload           10
        //   984: ldc             "Malformed URL Exception in image bitmap download for Uri: "
        //   986: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   989: pop            
        //   990: aload           6
        //   992: astore_0       
        //   993: aload           5
        //   995: astore          4
        //   997: aload           10
        //   999: aload           8
        //  1001: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1004: pop            
        //  1005: aload           6
        //  1007: astore_0       
        //  1008: aload           5
        //  1010: astore          4
        //  1012: aload           10
        //  1014: ldc             ". Image Uri may be corrupted."
        //  1016: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1019: pop            
        //  1020: aload           6
        //  1022: astore_0       
        //  1023: aload           5
        //  1025: astore          4
        //  1027: aload           9
        //  1029: aload           10
        //  1031: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  1034: aload           7
        //  1036: invokestatic    com/appboy/support/AppboyLogger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //  1039: pop            
        //  1040: aload           6
        //  1042: ifnull          1050
        //  1045: aload           6
        //  1047: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //  1050: aload           5
        //  1052: ifnull          1320
        //  1055: aload           5
        //  1057: invokevirtual   java/io/InputStream.close:()V
        //  1060: aconst_null    
        //  1061: areturn        
        //  1062: astore          7
        //  1064: aconst_null    
        //  1065: astore          6
        //  1067: aload           6
        //  1069: astore          5
        //  1071: aload           6
        //  1073: astore_0       
        //  1074: aload           5
        //  1076: astore          4
        //  1078: getstatic       com/appboy/support/AppboyImageUtils.a:Ljava/lang/String;
        //  1081: astore          9
        //  1083: aload           6
        //  1085: astore_0       
        //  1086: aload           5
        //  1088: astore          4
        //  1090: new             Ljava/lang/StringBuilder;
        //  1093: dup            
        //  1094: invokespecial   java/lang/StringBuilder.<init>:()V
        //  1097: astore          10
        //  1099: aload           6
        //  1101: astore_0       
        //  1102: aload           5
        //  1104: astore          4
        //  1106: aload           10
        //  1108: ldc             "Unknown Host Exception in image bitmap download for Uri: "
        //  1110: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1113: pop            
        //  1114: aload           6
        //  1116: astore_0       
        //  1117: aload           5
        //  1119: astore          4
        //  1121: aload           10
        //  1123: aload           8
        //  1125: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1128: pop            
        //  1129: aload           6
        //  1131: astore_0       
        //  1132: aload           5
        //  1134: astore          4
        //  1136: aload           10
        //  1138: ldc             ". Device may be offline."
        //  1140: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1143: pop            
        //  1144: aload           6
        //  1146: astore_0       
        //  1147: aload           5
        //  1149: astore          4
        //  1151: aload           9
        //  1153: aload           10
        //  1155: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  1158: aload           7
        //  1160: invokestatic    com/appboy/support/AppboyLogger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //  1163: pop            
        //  1164: aload           6
        //  1166: ifnull          1174
        //  1169: aload           6
        //  1171: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //  1174: aload           5
        //  1176: ifnull          1320
        //  1179: aload           5
        //  1181: invokevirtual   java/io/InputStream.close:()V
        //  1184: aconst_null    
        //  1185: areturn        
        //  1186: astore          7
        //  1188: aconst_null    
        //  1189: astore          6
        //  1191: aload           6
        //  1193: astore          5
        //  1195: aload           6
        //  1197: astore_0       
        //  1198: aload           5
        //  1200: astore          4
        //  1202: getstatic       com/appboy/support/AppboyImageUtils.a:Ljava/lang/String;
        //  1205: astore          9
        //  1207: aload           6
        //  1209: astore_0       
        //  1210: aload           5
        //  1212: astore          4
        //  1214: new             Ljava/lang/StringBuilder;
        //  1217: dup            
        //  1218: invokespecial   java/lang/StringBuilder.<init>:()V
        //  1221: astore          10
        //  1223: aload           6
        //  1225: astore_0       
        //  1226: aload           5
        //  1228: astore          4
        //  1230: aload           10
        //  1232: ldc             "Out of Memory Error in image bitmap download for Uri: "
        //  1234: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1237: pop            
        //  1238: aload           6
        //  1240: astore_0       
        //  1241: aload           5
        //  1243: astore          4
        //  1245: aload           10
        //  1247: aload           8
        //  1249: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1252: pop            
        //  1253: aload           6
        //  1255: astore_0       
        //  1256: aload           5
        //  1258: astore          4
        //  1260: aload           10
        //  1262: ldc             "."
        //  1264: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1267: pop            
        //  1268: aload           6
        //  1270: astore_0       
        //  1271: aload           5
        //  1273: astore          4
        //  1275: aload           9
        //  1277: aload           10
        //  1279: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  1282: aload           7
        //  1284: invokestatic    com/appboy/support/AppboyLogger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //  1287: pop            
        //  1288: aload           6
        //  1290: ifnull          1298
        //  1293: aload           6
        //  1295: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //  1298: aload           5
        //  1300: ifnull          1320
        //  1303: aload           5
        //  1305: invokevirtual   java/io/InputStream.close:()V
        //  1308: aconst_null    
        //  1309: areturn        
        //  1310: getstatic       com/appboy/support/AppboyImageUtils.a:Ljava/lang/String;
        //  1313: ldc             "IOException during closing of bitmap metadata download stream."
        //  1315: aload_0        
        //  1316: invokestatic    com/appboy/support/AppboyLogger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //  1319: pop            
        //  1320: aconst_null    
        //  1321: areturn        
        //  1322: astore          5
        //  1324: aload_0        
        //  1325: ifnull          1332
        //  1328: aload_0        
        //  1329: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //  1332: aload           4
        //  1334: ifnull          1356
        //  1337: aload           4
        //  1339: invokevirtual   java/io/InputStream.close:()V
        //  1342: goto            1356
        //  1345: astore_0       
        //  1346: getstatic       com/appboy/support/AppboyImageUtils.a:Ljava/lang/String;
        //  1349: ldc             "IOException during closing of bitmap metadata download stream."
        //  1351: aload_0        
        //  1352: invokestatic    com/appboy/support/AppboyLogger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //  1355: pop            
        //  1356: aload           5
        //  1358: athrow         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                            
        //  -----  -----  -----  -----  --------------------------------
        //  53     74     1186   1195   Ljava/lang/OutOfMemoryError;
        //  53     74     1062   1071   Ljava/net/UnknownHostException;
        //  53     74     938    947    Ljava/net/MalformedURLException;
        //  53     74     825    834    Ljava/lang/Exception;
        //  53     74     716    725    Any
        //  74     80     702    716    Ljava/lang/OutOfMemoryError;
        //  74     80     688    702    Ljava/net/UnknownHostException;
        //  74     80     674    688    Ljava/net/MalformedURLException;
        //  74     80     660    674    Ljava/lang/Exception;
        //  74     80     646    660    Any
        //  87     149    702    716    Ljava/lang/OutOfMemoryError;
        //  87     149    688    702    Ljava/net/UnknownHostException;
        //  87     149    674    688    Ljava/net/MalformedURLException;
        //  87     149    660    674    Ljava/lang/Exception;
        //  87     149    646    660    Any
        //  161    167    702    716    Ljava/lang/OutOfMemoryError;
        //  161    167    688    702    Ljava/net/UnknownHostException;
        //  161    167    674    688    Ljava/net/MalformedURLException;
        //  161    167    660    674    Ljava/lang/Exception;
        //  161    167    646    660    Any
        //  175    259    634    646    Ljava/lang/OutOfMemoryError;
        //  175    259    622    634    Ljava/net/UnknownHostException;
        //  175    259    610    622    Ljava/net/MalformedURLException;
        //  175    259    598    610    Ljava/lang/Exception;
        //  175    259    586    598    Any
        //  259    266    533    538    Ljava/lang/OutOfMemoryError;
        //  259    266    521    526    Ljava/net/UnknownHostException;
        //  259    266    509    514    Ljava/net/MalformedURLException;
        //  259    266    497    502    Ljava/lang/Exception;
        //  259    266    485    490    Any
        //  266    282    480    485    Ljava/lang/OutOfMemoryError;
        //  266    282    475    480    Ljava/net/UnknownHostException;
        //  266    282    470    475    Ljava/net/MalformedURLException;
        //  266    282    465    470    Ljava/lang/Exception;
        //  266    282    460    465    Any
        //  285    295    480    485    Ljava/lang/OutOfMemoryError;
        //  285    295    475    480    Ljava/net/UnknownHostException;
        //  285    295    470    475    Ljava/net/MalformedURLException;
        //  285    295    465    470    Ljava/lang/Exception;
        //  285    295    460    465    Any
        //  310    315    317    330    Ljava/io/IOException;
        //  332    423    480    485    Ljava/lang/OutOfMemoryError;
        //  332    423    475    480    Ljava/net/UnknownHostException;
        //  332    423    470    475    Ljava/net/MalformedURLException;
        //  332    423    465    470    Ljava/lang/Exception;
        //  332    423    460    465    Any
        //  438    443    445    458    Ljava/io/IOException;
        //  545    551    634    646    Ljava/lang/OutOfMemoryError;
        //  545    551    622    634    Ljava/net/UnknownHostException;
        //  545    551    610    622    Ljava/net/MalformedURLException;
        //  545    551    598    610    Ljava/lang/Exception;
        //  545    551    586    598    Any
        //  565    569    572    583    Ljava/io/IOException;
        //  732    737    1322   1359   Any
        //  744    753    1322   1359   Any
        //  760    768    1322   1359   Any
        //  775    783    1322   1359   Any
        //  790    803    1322   1359   Any
        //  818    823    934    938    Ljava/io/IOException;
        //  841    846    1322   1359   Any
        //  853    862    1322   1359   Any
        //  869    877    1322   1359   Any
        //  884    892    1322   1359   Any
        //  899    912    1322   1359   Any
        //  927    932    934    938    Ljava/io/IOException;
        //  954    959    1322   1359   Any
        //  966    975    1322   1359   Any
        //  982    990    1322   1359   Any
        //  997    1005   1322   1359   Any
        //  1012   1020   1322   1359   Any
        //  1027   1040   1322   1359   Any
        //  1055   1060   934    938    Ljava/io/IOException;
        //  1078   1083   1322   1359   Any
        //  1090   1099   1322   1359   Any
        //  1106   1114   1322   1359   Any
        //  1121   1129   1322   1359   Any
        //  1136   1144   1322   1359   Any
        //  1151   1164   1322   1359   Any
        //  1179   1184   934    938    Ljava/io/IOException;
        //  1202   1207   1322   1359   Any
        //  1214   1223   1322   1359   Any
        //  1230   1238   1322   1359   Any
        //  1245   1253   1322   1359   Any
        //  1260   1268   1322   1359   Any
        //  1275   1288   1322   1359   Any
        //  1303   1308   934    938    Ljava/io/IOException;
        //  1337   1342   1345   1356   Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0922:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private static Bitmap a(final InputStream inputStream, final BitmapFactory$Options bitmapFactory$Options, final int n, final int n2) {
        bitmapFactory$Options.inSampleSize = a(bitmapFactory$Options, n, n2);
        bitmapFactory$Options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(inputStream, (Rect)null, bitmapFactory$Options);
    }
    
    static BitmapFactory$Options a(final InputStream inputStream) {
        final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
        bitmapFactory$Options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, (Rect)null, bitmapFactory$Options);
        return bitmapFactory$Options;
    }
    
    public static Bitmap getBitmap(final Context context, final Uri uri, final AppboyViewBounds appboyViewBounds) {
        if (uri == null) {
            AppboyLogger.i(AppboyImageUtils.a, "Null Uri received. Not getting image.");
            return null;
        }
        if (AppboyFileUtils.isLocalUri(uri)) {
            return a(uri);
        }
        if (!AppboyFileUtils.isRemoteUri(uri)) {
            AppboyLogger.w(AppboyImageUtils.a, "Uri with unknown scheme received. Not getting image.");
            return null;
        }
        if (context != null && appboyViewBounds != null) {
            final DisplayMetrics defaultScreenDisplayMetrics = getDefaultScreenDisplayMetrics(context);
            return a(uri, getPixelsFromDensityAndDp(defaultScreenDisplayMetrics.densityDpi, appboyViewBounds.getWidthDp()), getPixelsFromDensityAndDp(defaultScreenDisplayMetrics.densityDpi, appboyViewBounds.getHeightDp()));
        }
        return a(uri, 0, 0);
    }
    
    public static Bitmap getBitmap(final Uri uri) {
        return getBitmap(null, uri, null);
    }
    
    public static DisplayMetrics getDefaultScreenDisplayMetrics(final Context context) {
        final WindowManager windowManager = (WindowManager)context.getSystemService("window");
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }
    
    public static int getImageLoaderCacheSize() {
        return Math.max(1024, Math.min((int)Math.min(Runtime.getRuntime().maxMemory() / 8L, 2147483647L), 33554432));
    }
    
    public static int getPixelsFromDensityAndDp(final int n, final int n2) {
        return Math.abs(n * n2 / 160);
    }
}
