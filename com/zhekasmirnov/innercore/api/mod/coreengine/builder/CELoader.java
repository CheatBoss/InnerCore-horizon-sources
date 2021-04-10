package com.zhekasmirnov.innercore.api.mod.coreengine.builder;

import com.zhekasmirnov.innercore.api.mod.coreengine.*;
import com.zhekasmirnov.innercore.api.log.*;
import com.zhekasmirnov.innercore.mod.executable.*;
import java.io.*;
import com.zhekasmirnov.innercore.api.mod.*;
import com.zhekasmirnov.apparatus.minecraft.version.*;
import com.zhekasmirnov.apparatus.minecraft.enums.*;
import org.mozilla.javascript.*;

public class CELoader
{
    public static CEHandler loadAndCreateHandler() {
        try {
            final Executable loadCoreEngine = loadCoreEngine();
            if (loadCoreEngine != null) {
                return new CEHandler(loadCoreEngine);
            }
            ICLog.e("COREENGINE", "failed to create handler, core engine executable is null", new RuntimeException());
            return null;
        }
        catch (Exception ex) {
            ICLog.e("COREENGINE", "failed to create handler, compilation of IO error occurred", ex);
            return null;
        }
    }
    
    public static Executable loadCoreEngine() throws IOException {
        CEExtractor.extractIfNeeded();
        final boolean compiledExecutable = CEExtractor.isCompiledExecutable();
        final File executableFile = CEExtractor.getExecutableFile();
        if (executableFile != null) {
            final CompilerConfig compilerConfig = new CompilerConfig(API.getInstanceByName("AdaptedScript"));
            compilerConfig.setName("Core Engine");
            Executable executable;
            if (compiledExecutable) {
                executable = Compiler.loadDex(executableFile, compilerConfig);
            }
            else {
                executable = Compiler.compileReader(new FileReader(executableFile), compilerConfig);
            }
            if (executable != null) {
                setupExecutable(executable, executableFile.getParentFile());
            }
            return executable;
        }
        return null;
    }
    
    private static void setupExecutable(final Executable executable, final File file) {
        final ScriptableObject empty = ScriptableObjectHelper.createEmpty();
        empty.put("__version__", (Scriptable)empty, (Object)MinecraftVersions.getCurrent().getCode());
        empty.put("__name__", (Scriptable)empty, (Object)"core-engine");
        empty.put("__dir__", (Scriptable)empty, (Object)file.getAbsolutePath());
        new EnumsJsInjector((Scriptable)empty, true).injectAllEnumScopes("E");
        executable.addToScope(empty);
    }
}
