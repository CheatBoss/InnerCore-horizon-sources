package okhttp3.internal;

import java.nio.charset.*;
import java.util.regex.*;
import java.util.function.*;
import java.net.*;
import javax.annotation.*;
import okhttp3.*;
import java.util.*;
import okio.*;
import java.io.*;
import java.util.concurrent.*;

public final class Util
{
    public static final byte[] EMPTY_BYTE_ARRAY;
    public static final RequestBody EMPTY_REQUEST;
    public static final ResponseBody EMPTY_RESPONSE;
    public static final String[] EMPTY_STRING_ARRAY;
    public static final Charset ISO_8859_1;
    public static final Comparator<String> NATURAL_ORDER;
    public static final TimeZone UTC;
    private static final Charset UTF_16_BE;
    private static final ByteString UTF_16_BE_BOM;
    private static final Charset UTF_16_LE;
    private static final ByteString UTF_16_LE_BOM;
    private static final Charset UTF_32_BE;
    private static final ByteString UTF_32_BE_BOM;
    private static final Charset UTF_32_LE;
    private static final ByteString UTF_32_LE_BOM;
    public static final Charset UTF_8;
    private static final ByteString UTF_8_BOM;
    private static final Pattern VERIFY_AS_IP_ADDRESS;
    
    static {
        final byte[] array = EMPTY_BYTE_ARRAY = new byte[0];
        EMPTY_STRING_ARRAY = new String[0];
        EMPTY_RESPONSE = ResponseBody.create(null, array);
        EMPTY_REQUEST = RequestBody.create(null, Util.EMPTY_BYTE_ARRAY);
        UTF_8_BOM = ByteString.decodeHex("efbbbf");
        UTF_16_BE_BOM = ByteString.decodeHex("feff");
        UTF_16_LE_BOM = ByteString.decodeHex("fffe");
        UTF_32_BE_BOM = ByteString.decodeHex("0000ffff");
        UTF_32_LE_BOM = ByteString.decodeHex("ffff0000");
        UTF_8 = Charset.forName("UTF-8");
        ISO_8859_1 = Charset.forName("ISO-8859-1");
        UTF_16_BE = Charset.forName("UTF-16BE");
        UTF_16_LE = Charset.forName("UTF-16LE");
        UTF_32_BE = Charset.forName("UTF-32BE");
        UTF_32_LE = Charset.forName("UTF-32LE");
        UTC = TimeZone.getTimeZone("GMT");
        NATURAL_ORDER = new Comparator<String>() {
            @Override
            public int compare(final String s, final String s2) {
                return s.compareTo(s2);
            }
            
            @Override
            public Comparator<Object> reversed() {
                return Comparator-CC.$default$reversed();
            }
            
            @Override
            public Comparator<Object> thenComparing(final Comparator<?> p0) {
                // 
                // This method could not be decompiled.
                // 
                // Could not show original bytecode, likely due to the same error.
                // 
                // The error that occurred was:
                // 
                // java.lang.NullPointerException
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
            
            @Override
            public <U extends Comparable<? super U>> Comparator<Object> thenComparing(final Function<?, ? extends U> p0) {
                // 
                // This method could not be decompiled.
                // 
                // Could not show original bytecode, likely due to the same error.
                // 
                // The error that occurred was:
                // 
                // java.lang.NullPointerException
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
                //     at com.strobel.assembler.metadata.MetadataHelper$5.visitWildcard(MetadataHelper.java:1793)
                //     at com.strobel.assembler.metadata.MetadataHelper$5.visitWildcard(MetadataHelper.java:1790)
                //     at com.strobel.assembler.metadata.WildcardType.accept(WildcardType.java:83)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
                //     at com.strobel.assembler.metadata.MetadataHelper.getLowerBound(MetadataHelper.java:1240)
                //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitWildcard(MetadataHelper.java:2278)
                //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitWildcard(MetadataHelper.java:2222)
                //     at com.strobel.assembler.metadata.WildcardType.accept(WildcardType.java:83)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.adaptRecursive(MetadataHelper.java:2256)
                //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.adaptRecursive(MetadataHelper.java:2233)
                //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitParameterizedType(MetadataHelper.java:2246)
                //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitParameterizedType(MetadataHelper.java:2222)
                //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.MetadataHelper.adapt(MetadataHelper.java:1312)
                //     at com.strobel.assembler.metadata.MetadataHelper$11.visitClassType(MetadataHelper.java:2708)
                //     at com.strobel.assembler.metadata.MetadataHelper$11.visitClassType(MetadataHelper.java:2692)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visitParameterizedType(DefaultTypeVisitor.java:65)
                //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.MetadataHelper.asSubType(MetadataHelper.java:720)
                //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:926)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
                //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
                //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1656)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
                //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
                //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
                //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
                //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
                //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:109)
                //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1162)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1009)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
            
            @Override
            public <U> Comparator<Object> thenComparing(final Function<?, ? extends U> p0, final Comparator<? super U> p1) {
                // 
                // This method could not be decompiled.
                // 
                // Could not show original bytecode, likely due to the same error.
                // 
                // The error that occurred was:
                // 
                // java.lang.NullPointerException
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
                //     at com.strobel.assembler.metadata.MetadataHelper$5.visitWildcard(MetadataHelper.java:1793)
                //     at com.strobel.assembler.metadata.MetadataHelper$5.visitWildcard(MetadataHelper.java:1790)
                //     at com.strobel.assembler.metadata.WildcardType.accept(WildcardType.java:83)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
                //     at com.strobel.assembler.metadata.MetadataHelper.getLowerBound(MetadataHelper.java:1240)
                //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitWildcard(MetadataHelper.java:2278)
                //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitWildcard(MetadataHelper.java:2222)
                //     at com.strobel.assembler.metadata.WildcardType.accept(WildcardType.java:83)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.adaptRecursive(MetadataHelper.java:2256)
                //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.adaptRecursive(MetadataHelper.java:2233)
                //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitParameterizedType(MetadataHelper.java:2246)
                //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitParameterizedType(MetadataHelper.java:2222)
                //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.MetadataHelper.adapt(MetadataHelper.java:1312)
                //     at com.strobel.assembler.metadata.MetadataHelper$11.visitClassType(MetadataHelper.java:2708)
                //     at com.strobel.assembler.metadata.MetadataHelper$11.visitClassType(MetadataHelper.java:2692)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visitParameterizedType(DefaultTypeVisitor.java:65)
                //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.MetadataHelper.asSubType(MetadataHelper.java:720)
                //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:926)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
                //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
                //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1656)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
                //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
                //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
                //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
                //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
                //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:109)
                //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1162)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1009)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
            
            @Override
            public Comparator<Object> thenComparingDouble(final ToDoubleFunction<?> p0) {
                // 
                // This method could not be decompiled.
                // 
                // Could not show original bytecode, likely due to the same error.
                // 
                // The error that occurred was:
                // 
                // java.lang.NullPointerException
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
            
            @Override
            public Comparator<Object> thenComparingInt(final ToIntFunction<?> p0) {
                // 
                // This method could not be decompiled.
                // 
                // Could not show original bytecode, likely due to the same error.
                // 
                // The error that occurred was:
                // 
                // java.lang.NullPointerException
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
            
            @Override
            public Comparator<Object> thenComparingLong(final ToLongFunction<?> p0) {
                // 
                // This method could not be decompiled.
                // 
                // Could not show original bytecode, likely due to the same error.
                // 
                // The error that occurred was:
                // 
                // java.lang.NullPointerException
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
        };
        VERIFY_AS_IP_ADDRESS = Pattern.compile("([0-9a-fA-F]*:[0-9a-fA-F:.]*)|([\\d.]+)");
    }
    
    public static AssertionError assertionError(String s, final Exception ex) {
        s = (String)new AssertionError((Object)s);
        try {
            ((Throwable)s).initCause(ex);
            return (AssertionError)s;
        }
        catch (IllegalStateException ex2) {
            return (AssertionError)s;
        }
    }
    
    public static Charset bomAwareCharset(final BufferedSource bufferedSource, final Charset charset) throws IOException {
        if (bufferedSource.rangeEquals(0L, Util.UTF_8_BOM)) {
            bufferedSource.skip(Util.UTF_8_BOM.size());
            return Util.UTF_8;
        }
        if (bufferedSource.rangeEquals(0L, Util.UTF_16_BE_BOM)) {
            bufferedSource.skip(Util.UTF_16_BE_BOM.size());
            return Util.UTF_16_BE;
        }
        if (bufferedSource.rangeEquals(0L, Util.UTF_16_LE_BOM)) {
            bufferedSource.skip(Util.UTF_16_LE_BOM.size());
            return Util.UTF_16_LE;
        }
        if (bufferedSource.rangeEquals(0L, Util.UTF_32_BE_BOM)) {
            bufferedSource.skip(Util.UTF_32_BE_BOM.size());
            return Util.UTF_32_BE;
        }
        if (bufferedSource.rangeEquals(0L, Util.UTF_32_LE_BOM)) {
            bufferedSource.skip(Util.UTF_32_LE_BOM.size());
            return Util.UTF_32_LE;
        }
        return charset;
    }
    
    public static String canonicalizeHost(String lowerCase) {
        if (lowerCase.contains(":")) {
            InetAddress inetAddress;
            if (lowerCase.startsWith("[") && lowerCase.endsWith("]")) {
                inetAddress = decodeIpv6(lowerCase, 1, lowerCase.length() - 1);
            }
            else {
                inetAddress = decodeIpv6(lowerCase, 0, lowerCase.length());
            }
            if (inetAddress == null) {
                return null;
            }
            final byte[] address = inetAddress.getAddress();
            if (address.length == 16) {
                return inet6AddressToAscii(address);
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid IPv6 address: '");
            sb.append(lowerCase);
            sb.append("'");
            throw new AssertionError((Object)sb.toString());
        }
        else {
            try {
                lowerCase = IDN.toASCII(lowerCase).toLowerCase(Locale.US);
                if (lowerCase.isEmpty()) {
                    return null;
                }
                if (containsInvalidHostnameAsciiCodes(lowerCase)) {
                    return null;
                }
                return lowerCase;
            }
            catch (IllegalArgumentException ex) {
                return null;
            }
        }
    }
    
    public static int checkDuration(final String s, final long n, final TimeUnit timeUnit) {
        if (n < 0L) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(" < 0");
            throw new IllegalArgumentException(sb.toString());
        }
        if (timeUnit == null) {
            throw new NullPointerException("unit == null");
        }
        final long millis = timeUnit.toMillis(n);
        if (millis > 2147483647L) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append(" too large.");
            throw new IllegalArgumentException(sb2.toString());
        }
        if (millis == 0L && n > 0L) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(s);
            sb3.append(" too small.");
            throw new IllegalArgumentException(sb3.toString());
        }
        return (int)millis;
    }
    
    public static void checkOffsetAndCount(final long n, final long n2, final long n3) {
        if ((n2 | n3) >= 0L && n2 <= n && n - n2 >= n3) {
            return;
        }
        throw new ArrayIndexOutOfBoundsException();
    }
    
    public static void closeQuietly(final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            }
            catch (Exception ex2) {}
            catch (RuntimeException ex) {
                throw ex;
            }
        }
    }
    
    public static void closeQuietly(final Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            }
            catch (Exception ex2) {}
            catch (RuntimeException ex) {
                throw ex;
            }
            catch (AssertionError assertionError) {
                if (isAndroidGetsocknameError(assertionError)) {
                    return;
                }
                throw assertionError;
            }
        }
    }
    
    public static String[] concat(final String[] array, final String s) {
        final int n = array.length + 1;
        final String[] array2 = new String[n];
        System.arraycopy(array, 0, array2, 0, array.length);
        array2[n - 1] = s;
        return array2;
    }
    
    private static boolean containsInvalidHostnameAsciiCodes(final String s) {
        for (int i = 0; i < s.length(); ++i) {
            final char char1 = s.charAt(i);
            if (char1 <= '\u001f') {
                return true;
            }
            if (char1 >= '\u007f') {
                return true;
            }
            if (" #%/:?@[\\]".indexOf(char1) != -1) {
                return true;
            }
        }
        return false;
    }
    
    public static int decodeHexDigit(final char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        char c2 = 'A';
        if (c >= 'a' && c <= 'f') {
            c2 = 'a';
        }
        else if (c < 'A' || c > 'F') {
            return -1;
        }
        return c - c2 + 10;
    }
    
    private static boolean decodeIpv4Suffix(final String s, int n, final int n2, final byte[] array, final int n3) {
        int n4 = n3;
        int i = n;
        while (i < n2) {
            if (n4 == array.length) {
                return false;
            }
            n = i;
            if (n4 != n3) {
                if (s.charAt(i) != '.') {
                    return false;
                }
                n = i + 1;
            }
            i = n;
            int n5 = 0;
            while (i < n2) {
                final char char1 = s.charAt(i);
                if (char1 < '0') {
                    break;
                }
                if (char1 > '9') {
                    break;
                }
                if (n5 == 0 && n != i) {
                    return false;
                }
                n5 = n5 * 10 + char1 - 48;
                if (n5 > 255) {
                    return false;
                }
                ++i;
            }
            if (i - n == 0) {
                return false;
            }
            array[n4] = (byte)n5;
            ++n4;
        }
        return n4 == n3 + 4;
    }
    
    @Nullable
    private static InetAddress decodeIpv6(final String s, int n, final int n2) {
        final byte[] array = new byte[16];
        int i = n;
        n = 0;
        int n3 = -1;
        int n4 = -1;
        int n5;
        int n6;
        while (true) {
            n5 = n3;
            n6 = n;
            if (i >= n2) {
                break;
            }
            if (n == 16) {
                return null;
            }
            final int n7 = i + 2;
            int n9;
            int n10;
            if (n7 <= n2 && s.regionMatches(i, "::", 0, 2)) {
                if (n3 != -1) {
                    return null;
                }
                final int n8 = n + 2;
                if (n7 == n2) {
                    n5 = n8;
                    n6 = n8;
                    break;
                }
                n9 = n8;
                n = n7;
                n10 = n8;
            }
            else {
                int n11 = i;
                if (n != 0) {
                    if (s.regionMatches(i, ":", 0, 1)) {
                        n11 = i + 1;
                    }
                    else {
                        if (!s.regionMatches(i, ".", 0, 1)) {
                            return null;
                        }
                        if (!decodeIpv4Suffix(s, n4, n2, array, n - 2)) {
                            return null;
                        }
                        n6 = n + 2;
                        n5 = n3;
                        break;
                    }
                }
                n10 = n;
                n = n11;
                n9 = n3;
            }
            i = n;
            int n12 = 0;
            while (i < n2) {
                final int decodeHexDigit = decodeHexDigit(s.charAt(i));
                if (decodeHexDigit == -1) {
                    break;
                }
                n12 = (n12 << 4) + decodeHexDigit;
                ++i;
            }
            final int n13 = i - n;
            if (n13 == 0) {
                return null;
            }
            if (n13 > 4) {
                return null;
            }
            final int n14 = n10 + 1;
            array[n10] = (byte)(n12 >>> 8 & 0xFF);
            final int n15 = n14 + 1;
            array[n14] = (byte)(n12 & 0xFF);
            n3 = n9;
            n4 = n;
            n = n15;
        }
        if (n6 != 16) {
            if (n5 == -1) {
                return null;
            }
            n = n6 - n5;
            System.arraycopy(array, n5, array, 16 - n, n);
            Arrays.fill(array, n5, 16 - n6 + n5, (byte)0);
        }
        try {
            return InetAddress.getByAddress(array);
        }
        catch (UnknownHostException ex) {
            throw new AssertionError();
        }
    }
    
    public static int delimiterOffset(final String s, int i, final int n, final char c) {
        while (i < n) {
            if (s.charAt(i) == c) {
                return i;
            }
            ++i;
        }
        return n;
    }
    
    public static int delimiterOffset(final String s, int i, final int n, final String s2) {
        while (i < n) {
            if (s2.indexOf(s.charAt(i)) != -1) {
                return i;
            }
            ++i;
        }
        return n;
    }
    
    public static boolean discard(final Source source, final int n, final TimeUnit timeUnit) {
        try {
            return skipAll(source, n, timeUnit);
        }
        catch (IOException ex) {
            return false;
        }
    }
    
    public static boolean equal(final Object o, final Object o2) {
        return o == o2 || (o != null && o.equals(o2));
    }
    
    public static String format(final String s, final Object... array) {
        return String.format(Locale.US, s, array);
    }
    
    public static String hostHeader(final HttpUrl httpUrl, final boolean b) {
        String s;
        if (httpUrl.host().contains(":")) {
            final StringBuilder sb = new StringBuilder();
            sb.append("[");
            sb.append(httpUrl.host());
            sb.append("]");
            s = sb.toString();
        }
        else {
            s = httpUrl.host();
        }
        if (!b) {
            final String string = s;
            if (httpUrl.port() == HttpUrl.defaultPort(httpUrl.scheme())) {
                return string;
            }
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(s);
        sb2.append(":");
        sb2.append(httpUrl.port());
        return sb2.toString();
    }
    
    public static <T> List<T> immutableList(final List<T> list) {
        return Collections.unmodifiableList((List<? extends T>)new ArrayList<T>((Collection<? extends T>)list));
    }
    
    public static <T> List<T> immutableList(final T... array) {
        return Collections.unmodifiableList((List<? extends T>)Arrays.asList((T[])array.clone()));
    }
    
    public static int indexOf(final Comparator<String> comparator, final String[] array, final String s) {
        for (int length = array.length, i = 0; i < length; ++i) {
            if (comparator.compare(array[i], s) == 0) {
                return i;
            }
        }
        return -1;
    }
    
    public static int indexOfControlOrNonAscii(final String s) {
        for (int length = s.length(), i = 0; i < length; ++i) {
            final char char1 = s.charAt(i);
            if (char1 <= '\u001f') {
                return i;
            }
            if (char1 >= '\u007f') {
                return i;
            }
        }
        return -1;
    }
    
    private static String inet6AddressToAscii(final byte[] array) {
        final int n = 0;
        int i = 0;
        int n2 = 0;
        int n3 = -1;
        while (i < array.length) {
            int n4;
            for (n4 = i; n4 < 16 && array[n4] == 0 && array[n4 + 1] == 0; n4 += 2) {}
            final int n5 = n4 - i;
            int n6 = n2;
            int n7 = n3;
            if (n5 > n2) {
                n6 = n2;
                n7 = n3;
                if (n5 >= 4) {
                    n6 = n5;
                    n7 = i;
                }
            }
            i = n4 + 2;
            n2 = n6;
            n3 = n7;
        }
        final Buffer buffer = new Buffer();
        int j = n;
        while (j < array.length) {
            if (j == n3) {
                buffer.writeByte(58);
                final int n8 = j + n2;
                if ((j = n8) != 16) {
                    continue;
                }
                buffer.writeByte(58);
                j = n8;
            }
            else {
                if (j > 0) {
                    buffer.writeByte(58);
                }
                buffer.writeHexadecimalUnsignedLong((long)((array[j] & 0xFF) << 8 | (array[j + 1] & 0xFF)));
                j += 2;
            }
        }
        return buffer.readUtf8();
    }
    
    public static String[] intersect(final Comparator<? super String> comparator, final String[] array, final String[] array2) {
        final ArrayList<String> list = new ArrayList<String>();
        for (int length = array.length, i = 0; i < length; ++i) {
            final String s = array[i];
            for (int length2 = array2.length, j = 0; j < length2; ++j) {
                if (comparator.compare(s, array2[j]) == 0) {
                    list.add(s);
                    break;
                }
            }
        }
        return list.toArray(new String[list.size()]);
    }
    
    public static boolean isAndroidGetsocknameError(final AssertionError assertionError) {
        return assertionError.getCause() != null && assertionError.getMessage() != null && assertionError.getMessage().contains("getsockname failed");
    }
    
    public static boolean nonEmptyIntersection(final Comparator<String> comparator, final String[] array, final String[] array2) {
        if (array != null && array2 != null && array.length != 0) {
            if (array2.length == 0) {
                return false;
            }
            for (int length = array.length, i = 0; i < length; ++i) {
                final String s = array[i];
                for (int length2 = array2.length, j = 0; j < length2; ++j) {
                    if (comparator.compare(s, array2[j]) == 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static boolean skipAll(final Source source, final int n, final TimeUnit timeUnit) throws IOException {
        final long nanoTime = System.nanoTime();
        long n2;
        if (source.timeout().hasDeadline()) {
            n2 = source.timeout().deadlineNanoTime() - nanoTime;
        }
        else {
            n2 = Long.MAX_VALUE;
        }
        source.timeout().deadlineNanoTime(Math.min(n2, timeUnit.toNanos(n)) + nanoTime);
        try {
            final Buffer buffer = new Buffer();
            while (source.read(buffer, 8192L) != -1L) {
                buffer.clear();
            }
            if (n2 == Long.MAX_VALUE) {
                source.timeout().clearDeadline();
                return true;
            }
            source.timeout().deadlineNanoTime(nanoTime + n2);
            return true;
        }
        catch (InterruptedIOException ex) {
            if (n2 == Long.MAX_VALUE) {
                source.timeout().clearDeadline();
                return false;
            }
            source.timeout().deadlineNanoTime(nanoTime + n2);
            return false;
        }
        finally {
            if (n2 == Long.MAX_VALUE) {
                source.timeout().clearDeadline();
            }
            else {
                source.timeout().deadlineNanoTime(nanoTime + n2);
            }
        }
    }
    
    public static int skipLeadingAsciiWhitespace(final String s, int i, final int n) {
        while (i < n) {
            final char char1 = s.charAt(i);
            if (char1 != '\t' && char1 != '\n' && char1 != '\f' && char1 != '\r' && char1 != ' ') {
                return i;
            }
            ++i;
        }
        return n;
    }
    
    public static int skipTrailingAsciiWhitespace(final String s, final int n, int n2) {
        while (true) {
            final int n3 = n2 - 1;
            if (n3 < n) {
                return n;
            }
            final char char1 = s.charAt(n3);
            n2 = n3;
            if (char1 == '\t') {
                continue;
            }
            n2 = n3;
            if (char1 == '\n') {
                continue;
            }
            n2 = n3;
            if (char1 == '\f') {
                continue;
            }
            n2 = n3;
            if (char1 == '\r') {
                continue;
            }
            n2 = n3;
            if (char1 != ' ') {
                return n3 + 1;
            }
        }
    }
    
    public static ThreadFactory threadFactory(final String s, final boolean b) {
        return new ThreadFactory() {
            @Override
            public Thread newThread(final Runnable runnable) {
                final Thread thread = new Thread(runnable, s);
                thread.setDaemon(b);
                return thread;
            }
        };
    }
    
    public static String trimSubstring(final String s, int skipLeadingAsciiWhitespace, final int n) {
        skipLeadingAsciiWhitespace = skipLeadingAsciiWhitespace(s, skipLeadingAsciiWhitespace, n);
        return s.substring(skipLeadingAsciiWhitespace, skipTrailingAsciiWhitespace(s, skipLeadingAsciiWhitespace, n));
    }
    
    public static boolean verifyAsIpAddress(final String s) {
        return Util.VERIFY_AS_IP_ADDRESS.matcher(s).matches();
    }
}
