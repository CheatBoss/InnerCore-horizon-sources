package org.mineprogramming.horizon.innercore.model;

import com.android.tools.r8.annotations.*;
import com.zhekasmirnov.innercore.utils.*;
import com.zhekasmirnov.innercore.modpack.*;
import android.content.*;
import com.zhekasmirnov.innercore.mod.executable.*;
import com.zhekasmirnov.innercore.mod.build.enums.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import java.util.concurrent.*;
import java.io.*;
import java.util.*;
import com.zhekasmirnov.innercore.mod.build.*;

@SynthesizedClassMap({ -$$Lambda$ModCompilationHelper$sB48hJwhfouNYPBsFad_WAPjCG8.class })
public class ModCompilationHelper
{
    private Mod mod;
    
    public ModCompilationHelper(final File file) {
        final StringBuilder sb = new StringBuilder();
        sb.append(file.getAbsolutePath());
        sb.append("/");
        this.mod = new Mod(sb.toString());
        this.mod.buildConfig = ModBuilder.loadBuildConfigForDir(this.mod.dir);
    }
    
    public void compile(final Context context, final ModPack$TaskReporter modPack$TaskReporter) {
        this.mod.buildConfig.read();
        Executors.newSingleThreadExecutor().execute(new -$$Lambda$ModCompilationHelper$sB48hJwhfouNYPBsFad_WAPjCG8(this, (IMessageReceiver)new IMessageReceiver() {
            int maxProgress = 0;
            int progress;
            
            {
                try {
                    final Iterator<BuildConfig$Source> iterator = ModCompilationHelper.this.mod.buildConfig.getAllSourcesToCompile(false).iterator();
                    while (iterator.hasNext()) {
                        final BuildConfig$BuildableDir relatedBuildableDir = ModCompilationHelper.this.mod.buildConfig.findRelatedBuildableDir((BuildConfig$Source)iterator.next());
                        if (relatedBuildableDir != null) {
                            this.maxProgress += BuildHelper.readIncludesFile(new File(ModCompilationHelper.this.mod.dir, relatedBuildableDir.dir)).size();
                        }
                        else {
                            ++this.maxProgress;
                        }
                    }
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
                this.progress = 0;
            }
            
            public void message(final String s) {
                try {
                    modPack$TaskReporter.reportProgress(s, 1, Math.min(this.progress, this.maxProgress), this.maxProgress);
                    ++this.progress;
                }
                catch (InterruptedException ex) {
                    throw new RuntimeException("Unable to report compilation progress");
                }
            }
        }, modPack$TaskReporter, context));
    }
    
    public boolean isCompiled() {
        return this.mod.getBuildType() == BuildType.RELEASE;
    }
    
    public void setDevelop() {
        this.mod.setBuildType(BuildType.DEVELOP);
    }
}
