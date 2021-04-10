package com.zhekasmirnov.innercore.api.mod.coreengine.builder;

import com.zhekasmirnov.innercore.utils.*;
import com.zhekasmirnov.innercore.api.log.*;
import com.zhekasmirnov.innercore.mod.executable.*;
import java.io.*;

public class CEExtractor
{
    private static final boolean CE_DEBUG = true;
    private static final boolean COMPILE = false;
    private static final String DIR_CORE_ENGINE;
    private static final String PATH_IN_ASSETS = "innercore/coreengine/";
    private static boolean isExtracted;
    private static boolean isExtractionSucceeded;
    
    static {
        CEExtractor.isExtracted = false;
        final StringBuilder sb = new StringBuilder();
        sb.append(FileTools.DIR_WORK);
        sb.append("coreengine/");
        DIR_CORE_ENGINE = sb.toString();
        CEExtractor.isExtractionSucceeded = false;
    }
    
    public static void extractIfNeeded() {
        if (!CEExtractor.isExtracted) {
            CEExtractor.isExtracted = true;
            prepareExtraction();
            CEExtractor.isExtractionSucceeded = true;
        }
    }
    
    public static File getExecutableFile() {
        if (CEExtractor.isExtractionSucceeded) {
            return new File(CEExtractor.DIR_CORE_ENGINE, "core-engine.dev.js");
        }
        return null;
    }
    
    public static boolean isCompiledExecutable() {
        return false;
    }
    
    public static boolean isExtracted() {
        return CEExtractor.isExtracted;
    }
    
    private static void prepareExtraction() {
        FileTools.assureDir(CEExtractor.DIR_CORE_ENGINE);
    }
    
    private static boolean tryReleaseBuild() {
        return unpackAsset("core-engine.script");
    }
    
    private static boolean tryToCompile() {
        final long currentTimeMillis = System.currentTimeMillis();
        ICLog.i("CORE-ENGINE", "starting compilation of Core Engine");
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append(CEExtractor.DIR_CORE_ENGINE);
            sb.append("core-engine.dev.js");
            final FileReader fileReader = new FileReader(new File(sb.toString()));
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(CEExtractor.DIR_CORE_ENGINE);
            sb2.append("core-engine.script");
            Compiler.compileScriptToFile(fileReader, "core-engine", sb2.toString());
            final long currentTimeMillis2 = System.currentTimeMillis();
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("successfully compiled in ");
            sb3.append(currentTimeMillis2 - currentTimeMillis);
            sb3.append(" ms");
            ICLog.i("CORE-ENGINE", sb3.toString());
            return true;
        }
        catch (IOException ex) {
            ICLog.e("CORE-ENGINE", "compilation failed", ex);
            return false;
        }
    }
    
    private static boolean unpackAsset(final String s) {
        return unpackAsset(s, s);
    }
    
    private static boolean unpackAsset(final String s, String string) {
        final StringBuilder sb = new StringBuilder();
        sb.append(CEExtractor.DIR_CORE_ENGINE);
        sb.append(string);
        string = sb.toString();
        FileTools.assureFileDir(new File(string));
        try {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("innercore/coreengine/");
            sb2.append(s);
            FileTools.unpackAsset(sb2.toString(), string);
            return true;
        }
        catch (IOException ex) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("unpacking core engine file failed name=");
            sb3.append(s);
            ICLog.e("COREENGINE", sb3.toString(), ex);
            return false;
        }
    }
}
