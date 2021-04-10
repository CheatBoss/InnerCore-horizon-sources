package com.zhekasmirnov.innercore.api.mod.coreengine;

import com.zhekasmirnov.innercore.api.mod.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import com.zhekasmirnov.innercore.api.mod.coreengine.builder.*;
import com.zhekasmirnov.innercore.mod.executable.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.mod.build.*;

public class CoreEngineAPI extends API
{
    public static final String LOGGER_TAG = "CORE-ENGINE";
    private static CEHandler ceHandlerSingleton;
    
    public static CEHandler getOrLoadCoreEngine() {
        synchronized (CoreEngineAPI.class) {
            if (CoreEngineAPI.ceHandlerSingleton == null) {
                Logger.info("CORE-ENGINE", "started loading");
                final long currentTimeMillis = System.currentTimeMillis();
                CoreEngineAPI.ceHandlerSingleton = CELoader.loadAndCreateHandler();
                if (CoreEngineAPI.ceHandlerSingleton != null) {
                    CoreEngineAPI.ceHandlerSingleton.load();
                }
                final long currentTimeMillis2 = System.currentTimeMillis();
                if (CoreEngineAPI.ceHandlerSingleton != null) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("successfully extracted and loaded in ");
                    sb.append(currentTimeMillis2 - currentTimeMillis);
                    sb.append(" ms");
                    Logger.info("CORE-ENGINE", sb.toString());
                }
                else {
                    Logger.info("CORE-ENGINE", "failed to create handler, look for details above");
                }
            }
            return CoreEngineAPI.ceHandlerSingleton;
        }
    }
    
    private void transferValue(final Executable executable, final String s) {
        executable.injectValueIntoScope(s, getOrLoadCoreEngine().requireGlobal(s));
    }
    
    @Override
    public int getLevel() {
        return 8;
    }
    
    @Override
    public String getName() {
        return "CoreEngine";
    }
    
    @Override
    public void injectIntoScope(final ScriptableObject scriptableObject) {
    }
    
    @Override
    public void onCallback(final String s, final Object[] array) {
    }
    
    @Override
    public void onLoaded() {
    }
    
    @Override
    public void onModLoaded(final Mod mod) {
    }
    
    @Override
    public void prepareExecutable(final Executable executable) {
        super.prepareExecutable(executable);
        if (this.isLoaded) {
            getOrLoadCoreEngine().injectCoreAPI(executable.getScope());
            this.transferValue(executable, "Particles");
            this.transferValue(executable, "ItemExtraData");
            this.transferValue(executable, "RenderMesh");
            return;
        }
        throw new RuntimeException("cannot prepare executable, Core Engine failed to load");
    }
    
    @Override
    public void setupCallbacks(final Executable executable) {
    }
}
