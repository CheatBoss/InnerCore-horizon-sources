package com.zhekasmirnov.apparatus.ecs;

import com.android.tools.r8.annotations.*;
import java.util.concurrent.*;
import java.util.*;

@SynthesizedClassMap({ -$$Lambda$Engine$DVd7c-8R_0EjNnS4PX8HXBUtM-k.class, -$$Lambda$Engine$Rwh9Ffx40NpgW-n4zwBkVE6PXqI.class, -$$Lambda$Engine$UOXv2N4QpmRwMr20ICMGNwLNB_s.class })
public final class Engine
{
    private List<Command> commands;
    private List<Entity> entities;
    private Map<EntityFamily, List<EntityListener>> filteredListeners;
    private List<EntityListener> listeners;
    private List<EngineSystem> systems;
    private boolean updating;
    private Map<EntityFamily, List<Entity>> views;
    
    public Engine() {
        this.entities = new ArrayList<Entity>();
        this.views = new HashMap<EntityFamily, List<Entity>>();
        this.commands = new ArrayList<Command>();
        this.systems = new ArrayList<EngineSystem>();
        this.listeners = new CopyOnWriteArrayList<EntityListener>();
        this.filteredListeners = new HashMap<EntityFamily, List<EntityListener>>();
    }
    
    private void addEntityInternal(final Entity entity) {
        if (entity.getEngine() != null) {
            throw new IllegalArgumentException("entity already added to an engine");
        }
        this.entities.add(entity);
        entity.setEngine(this);
        entity.activate();
        this.addEntityToViews(entity);
        final Iterator<EntityListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().entityAdded(entity);
        }
        for (final Map.Entry<EntityFamily, List<EntityListener>> entry : this.filteredListeners.entrySet()) {
            if (entry.getKey().isMember(entity)) {
                final Iterator<EntityListener> iterator3 = entry.getValue().iterator();
                while (iterator3.hasNext()) {
                    iterator3.next().entityAdded(entity);
                }
            }
        }
    }
    
    private void addEntityToViews(final Entity entity) {
        for (final EntityFamily entityFamily : this.views.keySet()) {
            if (entityFamily.isMember(entity)) {
                this.views.get(entityFamily).add(entity);
            }
        }
    }
    
    private void initView(final EntityFamily entityFamily, final List<Entity> list) {
        for (final Entity entity : this.entities) {
            if (entityFamily.isMember(entity)) {
                list.add(entity);
            }
        }
    }
    
    private void removeAllInternal() {
        while (!this.entities.isEmpty()) {
            this.removeEntityInternal(this.entities.get(0));
        }
    }
    
    private void removeEntityFromViews(final Entity entity) {
        final Iterator<List<Entity>> iterator = this.views.values().iterator();
        while (iterator.hasNext()) {
            iterator.next().remove(entity);
        }
    }
    
    private void removeEntityInternal(final Entity entity) {
        if (entity.getEngine() != this) {
            return;
        }
        final Iterator<EntityListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().entityRemoved(entity);
        }
        for (final Map.Entry<EntityFamily, List<EntityListener>> entry : this.filteredListeners.entrySet()) {
            if (entry.getKey().isMember(entity)) {
                final Iterator<EntityListener> iterator3 = entry.getValue().iterator();
                while (iterator3.hasNext()) {
                    iterator3.next().entityRemoved(entity);
                }
            }
        }
        entity.deactivate();
        entity.setEngine(null);
        this.entities.remove(entity);
        this.removeEntityFromViews(entity);
    }
    
    public void addEntity(final Entity entity) {
        if (this.updating) {
            this.commands.add((Command)new -$$Lambda$Engine$Rwh9Ffx40NpgW-n4zwBkVE6PXqI(this, entity));
            return;
        }
        this.addEntityInternal(entity);
    }
    
    public void addEntityListener(final EntityListener entityListener) {
        this.listeners.add(entityListener);
    }
    
    public void addEntityListener(final EntityListener entityListener, final EntityFamily entityFamily) {
        List<EntityListener> list;
        if ((list = this.filteredListeners.get(entityFamily)) == null) {
            list = new CopyOnWriteArrayList<EntityListener>();
            this.filteredListeners.put(entityFamily, list);
        }
        list.add(entityListener);
    }
    
    public void addSystem(final EngineSystem engineSystem) throws IllegalStateException, IllegalArgumentException {
        if (this.updating) {
            throw new IllegalStateException("cannot add system while updating");
        }
        if (this.systems.contains(engineSystem)) {
            throw new IllegalArgumentException("system already added");
        }
        engineSystem.setEngine(this);
        this.systems.add(engineSystem);
        engineSystem.addedToEngine(this);
    }
    
    public void dispose() throws IllegalStateException {
        if (this.updating) {
            throw new IllegalStateException("dispose not allowed during update");
        }
        for (final Entity entity : this.entities) {
            if (entity.isActivated()) {
                entity.deactivate();
                entity.setEngine(null);
            }
        }
        this.entities.clear();
        this.views.clear();
        for (int i = this.systems.size() - 1; i >= 0; --i) {
            final EngineSystem engineSystem = this.systems.get(i);
            engineSystem.setEnabled(false);
            engineSystem.removedFromEngine(this);
            engineSystem.setEngine(null);
        }
        this.systems.clear();
    }
    
    public List<Entity> getEntities(final EntityFamily entityFamily) {
        List<Entity> list;
        if ((list = this.views.get(entityFamily)) == null) {
            list = new ArrayList<Entity>();
            this.views.put(entityFamily, list);
            this.initView(entityFamily, list);
        }
        return (List<Entity>)Collections.unmodifiableList((List<?>)list);
    }
    
    public int getNumOfEntities() {
        return this.entities.size();
    }
    
    public int getNumOfSystems() {
        return this.systems.size();
    }
    
    public EngineSystem getSystem(final int n) throws IndexOutOfBoundsException {
        return this.systems.get(n);
    }
    
    public <T> T getSystem(final Class<T> clazz) throws IllegalArgumentException {
        for (final EngineSystem engineSystem : this.systems) {
            if (clazz.isInstance(engineSystem)) {
                return clazz.cast(engineSystem);
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("system not found ");
        sb.append(clazz.getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public boolean hasSystem(final Class<?> clazz) {
        final Iterator<EngineSystem> iterator = this.systems.iterator();
        while (iterator.hasNext()) {
            if (clazz.isInstance(iterator.next())) {
                return true;
            }
        }
        return false;
    }
    
    public void removeAll() {
        if (this.updating) {
            this.commands.add((Command)new -$$Lambda$Engine$UOXv2N4QpmRwMr20ICMGNwLNB_s(this));
            return;
        }
        this.removeAllInternal();
    }
    
    public void removeEntity(final Entity entity) {
        if (this.updating) {
            this.commands.add((Command)new -$$Lambda$Engine$DVd7c-8R_0EjNnS4PX8HXBUtM-k(this, entity));
            return;
        }
        this.removeEntityInternal(entity);
    }
    
    public void removeEntityListener(final EntityListener entityListener) {
        this.listeners.remove(entityListener);
    }
    
    public void removeEntityListener(final EntityListener entityListener, final EntityFamily entityFamily) {
        final List<EntityListener> list = this.filteredListeners.get(entityFamily);
        if (list != null) {
            list.remove(entityListener);
        }
    }
    
    public void removeSystem(final EngineSystem engineSystem) throws IllegalStateException, IllegalArgumentException {
        if (this.updating) {
            throw new IllegalStateException("cannot remove system while updating");
        }
        if (!this.systems.contains(engineSystem)) {
            throw new IllegalArgumentException("system is unknown");
        }
        engineSystem.removedFromEngine(this);
        this.systems.remove(engineSystem);
        engineSystem.setEngine(null);
    }
    
    public void update(final double n) {
        this.updating = true;
        for (final EngineSystem engineSystem : this.systems) {
            if (engineSystem.isEnabled()) {
                engineSystem.update(n);
            }
        }
        final Iterator<Command> iterator2 = this.commands.iterator();
        while (iterator2.hasNext()) {
            iterator2.next().execute();
        }
        this.commands.clear();
        this.updating = false;
    }
    
    private interface Command
    {
        void execute();
    }
}
