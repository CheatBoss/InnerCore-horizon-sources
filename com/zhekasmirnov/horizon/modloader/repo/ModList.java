package com.zhekasmirnov.horizon.modloader.repo;

import com.zhekasmirnov.horizon.modloader.*;
import com.zhekasmirnov.horizon.modloader.repo.storage.*;
import com.zhekasmirnov.horizon.runtime.task.*;
import com.zhekasmirnov.horizon.activity.util.*;
import com.zhekasmirnov.horizon.modloader.repo.location.*;
import java.util.*;
import com.zhekasmirnov.horizon.modloader.mod.*;

public class ModList
{
    private final ModContext context;
    private final TemporaryStorage storage;
    private final TaskManager manager;
    private Runnable interrupt;
    private final List<ModRepository> repositories;
    private TaskWatcher.Callback taskCallback;
    private final TaskSequence REFRESH_SEQUENCE;
    private final TaskSequence REBUILD_SEQUENCE;
    private final TaskSequence LAUNCH_SEQUENCE;
    
    public ModList(final ModContext context, final TaskManager manager, final TemporaryStorage storage) {
        this.interrupt = null;
        this.repositories = new ArrayList<ModRepository>();
        this.taskCallback = new TaskWatcher.Callback() {
            @Override
            public void complete(final Task task) {
            }
            
            @Override
            public boolean error(final Task task, final Throwable error) {
                ModList.this.manager.interruptTaskSequence(ModList.this.LAUNCH_SEQUENCE);
                ModList.this.context.getEventLogger().fault("UPDATE", "Failed to update mod list", error);
                CompilerErrorDialogHelper.showErrors(ModList.this.context, 2131624034);
                if (ModList.this.interrupt != null) {
                    ModList.this.interrupt.run();
                }
                return true;
            }
        };
        this.REFRESH_SEQUENCE = new TaskSequence("mod_list_rebuild_and_launch_sequence", new Object[] { new TaskSequence.AnonymousTask() {
                @Override
                public void run() {
                    ModList.this.initializeContext();
                }
                
                @Override
                public String getDescription() {
                    return "refreshing lists";
                }
            } });
        this.REBUILD_SEQUENCE = new TaskSequence("mod_list_rebuild_and_launch_sequence", new Object[] { this.REFRESH_SEQUENCE, new TaskSequence.AnonymousTask() {
                @Override
                public void run() {
                    ModList.this.context.injectAll();
                    ModList.this.context.buildAll();
                    final boolean errorsFound = CompilerErrorDialogHelper.showCompilationErrors(ModList.this.context);
                    if (errorsFound) {
                        ModList.this.manager.interruptTaskSequence(ModList.this.LAUNCH_SEQUENCE);
                        if (ModList.this.interrupt != null) {
                            ModList.this.interrupt.run();
                        }
                    }
                }
                
                @Override
                public String getDescription() {
                    return "build";
                }
            }, new TaskSequence.AnonymousTask() {
                @Override
                public void run() {
                    ModList.this.context.initializeAll();
                }
                
                @Override
                public String getDescription() {
                    return "initialization";
                }
            } });
        this.LAUNCH_SEQUENCE = new TaskSequence("mod_list_rebuild_and_launch_sequence", new Object[] { this.REBUILD_SEQUENCE, new TaskSequence.AnonymousTask() {
                @Override
                public void run() {
                    ModList.this.context.launchAll();
                }
                
                @Override
                public String getDescription() {
                    return "launching";
                }
            } });
        this.context = context;
        this.storage = storage;
        this.manager = manager;
    }
    
    public void initializeContext() {
        this.context.clearModsAndContext();
        for (final ModRepository repository : this.repositories) {
            repository.refresh(this.context.getEventLogger());
            for (final ModLocation location : repository.getAllLocations()) {
                this.addModByLocation(location);
            }
        }
    }
    
    private void addModByLocation(final ModLocation location) {
        final Mod mod = new Mod(this.context, location.initializeInLocalStorage(this.storage, this.context.getEventLogger()));
        this.context.addMod(mod);
        for (final ModLocation subLocation : mod.subModLocations) {
            this.addModByLocation(subLocation);
        }
        mod.getGraphics();
    }
    
    public void addModRepository(final ModRepository repository) {
        this.repositories.add(repository);
    }
    
    public void startRefreshTask(final Runnable callback) {
        this.manager.addTaskSequence(this.REFRESH_SEQUENCE, this.taskCallback, callback);
    }
    
    public void startRebuildTask(final Runnable callback) {
        this.manager.addTaskSequence(this.REBUILD_SEQUENCE, callback);
    }
    
    public void startLaunchTask(final Runnable callback, final Runnable interrupt) {
        this.interrupt = interrupt;
        this.manager.addTaskSequence(this.LAUNCH_SEQUENCE, this.taskCallback, callback);
    }
}
