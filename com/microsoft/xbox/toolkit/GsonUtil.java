package com.microsoft.xbox.toolkit;

import com.google.gson.stream.*;
import com.google.gson.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;

public class GsonUtil
{
    public static String buildJsonBody(final JsonBodyBuilder jsonBodyBuilder) throws IOException {
        final StringWriter stringWriter = new StringWriter();
        try {
            final JsonWriter jsonWriter = new JsonWriter(stringWriter);
            try {
                jsonBodyBuilder.buildBody(jsonWriter);
                return stringWriter.toString();
            }
            finally {
                jsonWriter.close();
            }
        }
        finally {
            stringWriter.close();
        }
    }
    
    public static GsonBuilder createMinimumGsonBuilder() {
        return new GsonBuilder().excludeFieldsWithModifiers(128);
    }
    
    public static <T> T deserializeJson(final Gson p0, final InputStream p1, final Class<T> p2) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore          4
        //     3: new             Ljava/io/InputStreamReader;
        //     6: dup            
        //     7: aload_1        
        //     8: invokespecial   java/io/InputStreamReader.<init>:(Ljava/io/InputStream;)V
        //    11: astore_1       
        //    12: new             Ljava/io/BufferedReader;
        //    15: dup            
        //    16: aload_1        
        //    17: invokespecial   java/io/BufferedReader.<init>:(Ljava/io/Reader;)V
        //    20: astore_3       
        //    21: aload_0        
        //    22: aload_3        
        //    23: aload_2        
        //    24: invokevirtual   com/google/gson/Gson.fromJson:(Ljava/io/Reader;Ljava/lang/Class;)Ljava/lang/Object;
        //    27: astore_0       
        //    28: aload_3        
        //    29: invokevirtual   java/io/BufferedReader.close:()V
        //    32: goto            119
        //    35: astore_2       
        //    36: goto            119
        //    39: astore_0       
        //    40: aload_3        
        //    41: astore_2       
        //    42: goto            70
        //    45: astore_0       
        //    46: aload_3        
        //    47: astore_0       
        //    48: goto            101
        //    51: astore_0       
        //    52: aload           4
        //    54: astore_2       
        //    55: goto            70
        //    58: astore_0       
        //    59: aconst_null    
        //    60: astore_0       
        //    61: goto            101
        //    64: astore_0       
        //    65: aconst_null    
        //    66: astore_1       
        //    67: aload           4
        //    69: astore_2       
        //    70: aload_2        
        //    71: ifnull          82
        //    74: aload_2        
        //    75: invokevirtual   java/io/BufferedReader.close:()V
        //    78: goto            82
        //    81: astore_2       
        //    82: aload_1        
        //    83: ifnull          94
        //    86: aload_1        
        //    87: invokevirtual   java/io/InputStreamReader.close:()V
        //    90: goto            94
        //    93: astore_1       
        //    94: aload_0        
        //    95: athrow         
        //    96: astore_0       
        //    97: aconst_null    
        //    98: astore_0       
        //    99: aload_0        
        //   100: astore_1       
        //   101: aload_0        
        //   102: ifnull          113
        //   105: aload_0        
        //   106: invokevirtual   java/io/BufferedReader.close:()V
        //   109: goto            113
        //   112: astore_0       
        //   113: aload_1        
        //   114: ifnull          128
        //   117: aconst_null    
        //   118: astore_0       
        //   119: aload_1        
        //   120: invokevirtual   java/io/InputStreamReader.close:()V
        //   123: aload_0        
        //   124: areturn        
        //   125: astore_1       
        //   126: aload_0        
        //   127: areturn        
        //   128: aconst_null    
        //   129: areturn        
        //    Signature:
        //  <T:Ljava/lang/Object;>(Lcom/google/gson/Gson;Ljava/io/InputStream;Ljava/lang/Class<TT;>;)TT;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  3      12     96     101    Ljava/lang/Exception;
        //  3      12     64     70     Any
        //  12     21     58     64     Ljava/lang/Exception;
        //  12     21     51     58     Any
        //  21     28     45     51     Ljava/lang/Exception;
        //  21     28     39     45     Any
        //  28     32     35     39     Ljava/lang/Exception;
        //  74     78     81     82     Ljava/lang/Exception;
        //  86     90     93     94     Ljava/lang/Exception;
        //  105    109    112    113    Ljava/lang/Exception;
        //  119    123    125    128    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 81, Size: 81
        //     at java.util.ArrayList.rangeCheck(Unknown Source)
        //     at java.util.ArrayList.get(Unknown Source)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
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
    
    public static <T> T deserializeJson(final Gson gson, final String s, final Class<T> clazz) {
        try {
            return (T)gson.fromJson(s, (Class)clazz);
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public static <T> T deserializeJson(final InputStream inputStream, final Class<T> clazz) {
        return deserializeJson(createMinimumGsonBuilder().create(), inputStream, clazz);
    }
    
    public static <T> T deserializeJson(final InputStream inputStream, final Class<T> clazz, final Type type, final Object o) {
        return deserializeJson(createMinimumGsonBuilder().registerTypeAdapter(type, o).create(), inputStream, clazz);
    }
    
    public static <T> T deserializeJson(final InputStream inputStream, final Class<T> clazz, final Map<Type, Object> map) {
        final GsonBuilder minimumGsonBuilder = createMinimumGsonBuilder();
        for (final Map.Entry<Type, Object> entry : map.entrySet()) {
            minimumGsonBuilder.registerTypeAdapter(entry.getKey(), entry.getValue());
        }
        return deserializeJson(minimumGsonBuilder.create(), inputStream, clazz);
    }
    
    public static <T> T deserializeJson(final String s, final Class<T> clazz) {
        return deserializeJson(createMinimumGsonBuilder().create(), s, clazz);
    }
    
    public static <T> T deserializeJson(final String s, final Class<T> clazz, final Type type, final Object o) {
        return deserializeJson(createMinimumGsonBuilder().registerTypeAdapter(type, o).create(), s, clazz);
    }
    
    public static String toJsonString(final Object o) {
        return new Gson().toJson(o);
    }
    
    public interface JsonBodyBuilder
    {
        void buildBody(final JsonWriter p0) throws IOException;
    }
}
