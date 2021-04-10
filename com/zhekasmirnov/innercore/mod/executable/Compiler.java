package com.zhekasmirnov.innercore.mod.executable;

import com.zhekasmirnov.innercore.utils.*;
import com.zhekasmirnov.innercore.api.log.*;
import java.io.*;
import com.zhekasmirnov.innercore.mod.build.*;
import com.zhekasmirnov.innercore.ui.*;
import dalvik.system.*;
import com.faendir.rhino_android.*;
import java.util.*;
import com.zhekasmirnov.innercore.mod.executable.library.*;
import com.zhekasmirnov.innercore.api.mod.*;
import org.mozilla.javascript.*;

public class Compiler
{
    private static File classCacheDir;
    private static Context defaultContext;
    private static RhinoAndroidHelper rhinoAndroidHelper;
    private static int tempDexCounter;
    
    static {
        Compiler.classCacheDir = new File(System.getProperty("java.io.tmpdir", "."), "classes");
        Compiler.tempDexCounter = 0;
        Compiler.defaultContext = null;
    }
    
    public static Context assureContextForCurrentThread() {
        Context context;
        if ((context = Context.getCurrentContext()) == null) {
            context = enter(9);
        }
        return context;
    }
    
    public static boolean compileMod(final Mod mod, IMessageReceiver ex) {
        Object o;
        if (ex == null) {
            o = new IMessageReceiver() {
                @Override
                public void message(final String s) {
                    ICLog.i("COMPILER", s);
                }
            };
        }
        else {
            o = ex;
        }
        final BuildConfig buildConfig = mod.buildConfig;
        final ArrayList<BuildConfig.Source> allSourcesToCompile = buildConfig.getAllSourcesToCompile(false);
        final CompiledSources compiledSources = mod.createCompiledSources();
        final StringBuilder sb = new StringBuilder();
        sb.append("compiling mod ");
        sb.append(mod.getName());
        sb.append(" (");
        sb.append(allSourcesToCompile.size());
        sb.append(" source files)");
        ((IMessageReceiver)o).message(sb.toString());
        ((IMessageReceiver)o).message("cleaning up");
        compiledSources.reset();
        boolean b = true;
        int n = 1;
        for (final BuildConfig.Source source : allSourcesToCompile) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("compiling source: path=");
            sb2.append(source.path);
            sb2.append(" type=");
            sb2.append(source.sourceType);
            ((IMessageReceiver)o).message(sb2.toString());
            final BuildConfig.BuildableDir relatedBuildableDir = buildConfig.findRelatedBuildableDir(source);
            ArrayList<File> includesFile = null;
            if (relatedBuildableDir != null) {
                try {
                    includesFile = BuildHelper.readIncludesFile(new File(mod.dir, relatedBuildableDir.dir));
                }
                catch (IOException ex2) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("failed read includes, compiling result file: ");
                    sb3.append(ex2);
                    ((IMessageReceiver)o).message(sb3.toString());
                    includesFile = includesFile;
                }
            }
            ArrayList<File> list;
            if ((list = includesFile) == null) {
                list = new ArrayList<File>();
                final StringBuilder sb4 = new StringBuilder();
                sb4.append(mod.dir);
                sb4.append(source.path);
                list.add(new File(sb4.toString()));
            }
            for (int i = 0; i < list.size(); ++i) {
                ex = (Exception)list.get(i);
                try {
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append("$compiling: ");
                    sb5.append(((File)ex).getName());
                    sb5.append(" (");
                    sb5.append(i + 1);
                    sb5.append("/");
                    sb5.append(list.size());
                    sb5.append(")");
                    ((IMessageReceiver)o).message(sb5.toString());
                    final FileReader fileReader = new FileReader((File)ex);
                    final StringBuilder sb6 = new StringBuilder();
                    final int n2 = n + 1;
                    try {
                        sb6.append(n);
                        sb6.append("");
                        final File targetCompilationFile = compiledSources.getTargetCompilationFile(sb6.toString());
                        final StringBuilder sb7 = new StringBuilder();
                        try {
                            sb7.append(mod.getName());
                            sb7.append("$");
                            sb7.append(source.sourceName);
                            sb7.append("$");
                            sb7.append(((File)ex).getName());
                            compileScriptToFile(fileReader, sb7.toString(), targetCompilationFile.getAbsolutePath());
                            compiledSources.addCompiledSource(source.path, targetCompilationFile, source.sourceName);
                            n = n2;
                        }
                        catch (Exception ex) {
                            n = n2;
                        }
                    }
                    catch (Exception ex) {
                        n = n2;
                    }
                }
                catch (Exception ex3) {}
                ex.printStackTrace();
                final StringBuilder sb8 = new StringBuilder();
                sb8.append("failed: ");
                sb8.append(ex);
                ((IMessageReceiver)o).message(sb8.toString());
                b = false;
            }
        }
        ((IMessageReceiver)o).message("compilation finished");
        return b;
    }
    
    public static Executable compileReader(final Reader reader, final CompilerConfig compilerConfig) throws IOException {
        final Context enter = enter(compilerConfig.getOptimizationLevel());
        final StringBuilder sb = new StringBuilder();
        sb.append("Compiling ");
        sb.append(compilerConfig.getFullName());
        LoadingUI.setTip(sb.toString());
        final Script compileReader = enter.compileReader(reader, compilerConfig.getFullName(), 0, (Object)null);
        LoadingUI.setTip("");
        return wrapScript(enter, compileReader, compilerConfig);
    }
    
    public static void compileScriptToFile(final Reader reader, final String s, final String s2) throws IOException {
        AndroidClassLoader.enterCompilationMode(s2);
        final Context enter = enter(9);
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append("_");
        sb.append(genUniqueId());
        enter.compileReader(reader, sb.toString(), 0, (Object)null);
        AndroidClassLoader.exitCompilationMode();
    }
    
    public static Context enter(final int optimizationLevel) {
        if (Compiler.rhinoAndroidHelper == null) {
            Compiler.rhinoAndroidHelper = new RhinoAndroidHelper();
        }
        final Context enterContext = Compiler.rhinoAndroidHelper.enterContext();
        enterContext.setOptimizationLevel(optimizationLevel);
        enterContext.setLanguageVersion(200);
        return enterContext;
    }
    
    private static String genUniqueId() {
        final StringBuilder sb = new StringBuilder();
        sb.append(Integer.toHexString((int)(Math.random() * 1.6777216E7)));
        sb.append("_");
        sb.append(Integer.toHexString((int)(Math.random() * 1.6777216E7)));
        return sb.toString();
    }
    
    public static Context getDefaultContext() {
        if (Compiler.defaultContext == null) {
            Compiler.defaultContext = Context.enter();
        }
        return Compiler.defaultContext;
    }
    
    public static Executable loadDex(final File file, final CompilerConfig compilerConfig) throws IOException {
        final Context enter = enter(compilerConfig.getOptimizationLevel());
        final Script loadScriptFromDex = loadScriptFromDex(file);
        if (loadScriptFromDex == null) {
            return null;
        }
        final Executable wrapScript = wrapScript(enter, loadScriptFromDex, compilerConfig);
        wrapScript.isLoadedFromDex = true;
        return wrapScript;
    }
    
    public static Executable loadDexList(final File[] array, final CompilerConfig compilerConfig) {
        final Context enter = enter(compilerConfig.getOptimizationLevel());
        final MultidexScript multidexScript = new MultidexScript();
        final StringBuilder sb = new StringBuilder();
        sb.append("Wrapping ");
        sb.append(compilerConfig.getFullName());
        LoadingUI.setTip(sb.toString());
        for (int length = array.length, i = 0; i < length; ++i) {
            final File file = array[i];
            try {
                final Script loadScriptFromDex = loadScriptFromDex(file);
                if (loadScriptFromDex != null) {
                    multidexScript.addScript(loadScriptFromDex);
                }
            }
            catch (IOException ex) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("failed to load dex file into multi-dex executable: file=");
                sb2.append(file);
                ICLog.e("COMPILER", sb2.toString(), ex);
            }
        }
        if (multidexScript.getScriptCount() == 0) {
            return null;
        }
        final Executable wrapScript = wrapScript(enter, (Script)multidexScript, compilerConfig);
        wrapScript.isLoadedFromDex = true;
        LoadingUI.setTip("");
        return wrapScript;
    }
    
    public static Script loadScriptFromDex(File s) throws IOException {
        final String property = System.getProperty("java.io.tmpdir", ".");
        final StringBuilder sb = new StringBuilder();
        sb.append("classes/ic-dex-cache");
        final int tempDexCounter = Compiler.tempDexCounter;
        Compiler.tempDexCounter = tempDexCounter + 1;
        sb.append(tempDexCounter);
        final DexFile loadDex = DexFile.loadDex(((File)s).getAbsolutePath(), new File(property, sb.toString()).getAbsolutePath(), 0);
        final Enumeration entries = loadDex.entries();
        s = null;
        while (entries.hasMoreElements()) {
            final String s2 = entries.nextElement();
            if (s != null) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("invalid compiled js dex file: more than one class entries (");
                sb2.append((String)s);
                sb2.append(", ");
                sb2.append(s2);
                sb2.append(")");
                throw new IOException(sb2.toString());
            }
            s = s2;
        }
        if (s == null) {
            throw new IOException("invalid compiled js dex file: no class entries found");
        }
        try {
            return (Script)loadDex.loadClass((String)s, AndroidContextFactory.class.getClassLoader()).newInstance();
        }
        catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
        catch (InstantiationException ex2) {
            ex2.printStackTrace();
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("dex loading failed: ");
        sb3.append((String)s);
        ICLog.d("COMPILER", sb3.toString());
        return null;
    }
    
    private static Executable wrapScript(final Context context, final Script script, final CompilerConfig compilerConfig) {
        final API apiInstance = compilerConfig.getApiInstance();
        ScriptableObject scriptableObject;
        if (apiInstance == null) {
            scriptableObject = ScriptableObjectHelper.createEmpty();
        }
        else {
            scriptableObject = apiInstance.newInstance();
        }
        final ScriptableObject initStandardObjects = context.initStandardObjects(scriptableObject, false);
        if (compilerConfig.isLibrary) {
            return new Library(context, script, initStandardObjects, compilerConfig, compilerConfig.getApiInstance());
        }
        if (apiInstance != null) {
            apiInstance.injectIntoScope(initStandardObjects);
        }
        return new Executable(context, script, initStandardObjects, compilerConfig, compilerConfig.getApiInstance());
    }
}
