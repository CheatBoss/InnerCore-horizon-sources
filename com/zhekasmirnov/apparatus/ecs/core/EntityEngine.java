package com.zhekasmirnov.apparatus.ecs.core;

import com.android.tools.r8.annotations.*;
import com.zhekasmirnov.apparatus.util.*;
import java.util.function.*;
import java.util.*;
import java.util.stream.*;

@SynthesizedClassMap({ -$$Lambda$EntityEngine$nQMF__bUWbCxb1btD1RFLXxZVKo.class, -$$Lambda$EntityEngine$CpyBZQrub0Ty1PnF6fC3o87XjzM.class, -$$Lambda$EntityEngine$47aPsqHjGBXJhh614zUrICkQRIs.class, -$$Lambda$EntityEngine$-4s8kXxfvYZGzGBPi6IbGOE_0cg.class, -$$Lambda$EntityEngine$mf8SXC5U6HEl5xtahZ6P3zbbfyc.class, -$$Lambda$EntityEngine$MejwpTb4OmBzdkvy6TREbpN8Gfo.class, -$$Lambda$EntityEngine$IH4I4GL8tOZuI5SiSQ_Nv2q0y9E.class, -$$Lambda$IlSmZ2rMKnDe4nWNHq4P8VFn9T0.class, -$$Lambda$EntityEngine$mHTm5B9LXLJ3YRYbhSOxCafZFNk.class, -$$Lambda$EntityEngine$hffX3J6sDVXT7Fm1gHmXdKEJMIQ.class, -$$Lambda$EntityEngine$ZaQ21dAjR6fu8_1xForAfNNbY_U.class })
public class EntityEngine
{
    private static final Map<Class<?>, EntityFamily> entityFamilyByClassCache;
    private static final Map<String, EntityFamily> entityFamilyByComponentNameCache;
    private final LinkedList<Runnable> actionQueue;
    private final Map<EntityFamily, EntityFamilyListenersAndCache> activeEntitiesCacheByFamily;
    private final Set<GameEntity> allActiveEntities;
    private final Set<GameEntity> allEntities;
    private int currentCacheFactor;
    private final Map<EntityFamily, EntityFamilyListenersAndCache> entitiesCacheByFamily;
    private final LinkedList<GameEntity> entitiesToFlushPacketQueue;
    private final Map<String, EntityService> services;
    private final Map<ActiveEntityListener, EntityListener> wrappedEntityListenerMap;
    
    static {
        entityFamilyByComponentNameCache = new HashMap<String, EntityFamily>();
        entityFamilyByClassCache = new HashMap<Class<?>, EntityFamily>();
    }
    
    public EntityEngine() {
        this.services = new HashMap<String, EntityService>();
        this.allEntities = new LinkedHashSet<GameEntity>();
        this.allActiveEntities = new LinkedHashSet<GameEntity>();
        this.currentCacheFactor = 1024;
        this.entitiesCacheByFamily = new HashMap<EntityFamily, EntityFamilyListenersAndCache>();
        this.activeEntitiesCacheByFamily = new HashMap<EntityFamily, EntityFamilyListenersAndCache>();
        this.actionQueue = new LinkedList<Runnable>();
        this.entitiesToFlushPacketQueue = new LinkedList<GameEntity>();
        this.wrappedEntityListenerMap = new HashMap<ActiveEntityListener, EntityListener>();
    }
    
    private static EntityFamily getFamilyForComponentType(final Class<?> clazz) {
        return Java8BackComp.computeIfAbsent(EntityEngine.entityFamilyByClassCache, clazz, -$$Lambda$EntityEngine$nQMF__bUWbCxb1btD1RFLXxZVKo.INSTANCE);
    }
    
    private static EntityFamily getFamilyForComponentType(final String s) {
        return Java8BackComp.computeIfAbsent(EntityEngine.entityFamilyByComponentNameCache, s, -$$Lambda$EntityEngine$CpyBZQrub0Ty1PnF6fC3o87XjzM.INSTANCE);
    }
    
    public static EntityEngine getSingleton(final EngineSingleton engineSingleton) {
        return engineSingleton.singleton;
    }
    
    public static void loadClass() {
        loadClass();
    }
    
    public void addEntity(final GameEntity gameEntity) {
        synchronized (this.allEntities) {
            this.allEntities.add(gameEntity);
            gameEntity.setEngine(this);
            Java8BackComp.removeIf(this.entitiesCacheByFamily.entrySet(), new -$$Lambda$EntityEngine$hffX3J6sDVXT7Fm1gHmXdKEJMIQ(gameEntity));
            gameEntity.setActive(true);
        }
    }
    
    void addEntityToFlushPacketQueue(final GameEntity gameEntity) {
        synchronized (this.entitiesToFlushPacketQueue) {
            this.entitiesToFlushPacketQueue.add(gameEntity);
        }
    }
    
    public void addListener(final EntityFamily entityFamily, final ActiveEntityListener activeEntityListener) {
        synchronized (this.allEntities) {
            Java8BackComp.computeIfAbsent(this.activeEntitiesCacheByFamily, entityFamily, new -$$Lambda$EntityEngine$ZaQ21dAjR6fu8_1xForAfNNbY_U(this, entityFamily)).addListener((EntityListener)this.wrappedEntityListenerMap.put(activeEntityListener, (EntityListener)new EntityListener() {
                @Override
                public void onEntityAdded(final GameEntity gameEntity) {
                    activeEntityListener.onEntityActivated(gameEntity);
                }
                
                @Override
                public void onEntityRemoved(final GameEntity gameEntity) {
                    activeEntityListener.onEntityDeactivated(gameEntity);
                }
            }));
        }
    }
    
    public void addListener(final EntityFamily entityFamily, final EntityListener entityListener) {
        synchronized (this.allEntities) {
            Java8BackComp.computeIfAbsent(this.entitiesCacheByFamily, entityFamily, new -$$Lambda$EntityEngine$47aPsqHjGBXJhh614zUrICkQRIs(this, entityFamily)).addListener(entityListener);
        }
    }
    
    public void addListener(final Class<?> clazz, final ActiveEntityListener activeEntityListener) {
        this.addListener(getFamilyForComponentType(clazz), activeEntityListener);
    }
    
    public void addListener(final Class<?> clazz, final EntityListener entityListener) {
        this.addListener(getFamilyForComponentType(clazz), entityListener);
    }
    
    public void addListener(final String s, final ActiveEntityListener activeEntityListener) {
        this.addListener(getFamilyForComponentType(s), activeEntityListener);
    }
    
    public void addListener(final String s, final EntityListener entityListener) {
        this.addListener(getFamilyForComponentType(s), entityListener);
    }
    
    public void addService(final EntityService entityService) {
        this.services.put(entityService.getClass().getCanonicalName(), entityService);
    }
    
    public void addService(final String s, final EntityService entityService) {
        this.services.put(s, entityService);
    }
    
    public void dropCacheForFamily(final EntityFamily entityFamily) {
        synchronized (this.allEntities) {
            final EntityFamilyListenersAndCache entityFamilyListenersAndCache = this.entitiesCacheByFamily.get(entityFamily);
            if (entityFamilyListenersAndCache != null) {
                entityFamilyListenersAndCache.resetCache();
            }
        }
    }
    
    public void flushNetworkQueues() {
        synchronized (this.entitiesToFlushPacketQueue) {
            final Iterator<GameEntity> iterator = this.entitiesToFlushPacketQueue.iterator();
            while (iterator.hasNext()) {
                iterator.next().onPacketQueueFlushed();
            }
        }
    }
    
    public Set<GameEntity> getAllActiveEntities() {
        return this.allActiveEntities;
    }
    
    public Set<GameEntity> getAllEntities() {
        return this.allEntities;
    }
    
    public int getCurrentCacheFactor() {
        return this.currentCacheFactor;
    }
    
    public List<GameEntity> getEntitiesByFamily(final EntityFamily entityFamily) {
        return this.getEntitiesByFamily(entityFamily, false);
    }
    
    public List<GameEntity> getEntitiesByFamily(final EntityFamily entityFamily, final boolean b) {
        final Set<GameEntity> allEntities = this.allEntities;
        // monitorenter(allEntities)
        Label_0035: {
            if (!b) {
                break Label_0035;
            }
            while (true) {
                try {
                    EntityFamilyListenersAndCache entityFamilyListenersAndCache = Java8BackComp.computeIfAbsent(this.entitiesCacheByFamily, entityFamily, new -$$Lambda$EntityEngine$mHTm5B9LXLJ3YRYbhSOxCafZFNk(this, entityFamily));
                    while (true) {
                        final ArrayList list = new ArrayList<GameEntity>(entityFamilyListenersAndCache.getCachedEntities());
                        return (List<GameEntity>)list;
                        throw;
                        entityFamilyListenersAndCache = Java8BackComp.computeIfAbsent(this.activeEntitiesCacheByFamily, entityFamily, new -$$Lambda$EntityEngine$-4s8kXxfvYZGzGBPi6IbGOE_0cg(this, entityFamily));
                        continue;
                    }
                }
                // monitorexit(allEntities)
                // monitorexit(allEntities)
                finally {
                    continue;
                }
                break;
            }
        }
    }
    
    public List<GameEntity> getEntitiesByPredicate(final EntityFilter entityFilter) {
        return this.getEntitiesByPredicate(entityFilter, false);
    }
    
    public List<GameEntity> getEntitiesByPredicate(final EntityFilter entityFilter, final boolean b) {
        final Set<GameEntity> allEntities = this.allEntities;
        // monitorenter(allEntities)
        Label_0021: {
            if (!b) {
                break Label_0021;
            }
            while (true) {
                try {
                    Set<GameEntity> set = this.allEntities;
                    // monitorexit(allEntities)
                    while (true) {
                        final Stream<Object> stream = Java8BackComp.stream((Collection<Object>)set);
                        entityFilter.getClass();
                        final List<Object> list = stream.filter(new -$$Lambda$IlSmZ2rMKnDe4nWNHq4P8VFn9T0(entityFilter)).collect((Collector<? super Object, ?, List<Object>>)Collectors.toList());
                        return (List<GameEntity>)list;
                        set = this.allActiveEntities;
                        continue;
                    }
                    // monitorexit(allEntities)
                    throw;
                }
                finally {
                    continue;
                }
                break;
            }
        }
    }
    
    public List<GameEntity> getEntitiesWithComponent(final Class<?> clazz) {
        return this.getEntitiesWithComponent(clazz, false);
    }
    
    public List<GameEntity> getEntitiesWithComponent(final Class<?> clazz, final boolean b) {
        return this.getEntitiesByFamily(getFamilyForComponentType(clazz), b);
    }
    
    public List<GameEntity> getEntitiesWithComponent(final String s) {
        return this.getEntitiesWithComponent(s, false);
    }
    
    public List<GameEntity> getEntitiesWithComponent(final String s, final boolean b) {
        return this.getEntitiesByFamily(getFamilyForComponentType(s), b);
    }
    
    public <T extends EntityService> T getService(final Class<T> clazz) {
        return this.getService(clazz.getCanonicalName(), clazz);
    }
    
    public <T extends EntityService> T getService(final String s, final Class<T> clazz) {
        return (T)this.services.get(s);
    }
    
    void onEntityActivated(final GameEntity gameEntity) {
        synchronized (this.allActiveEntities) {
            this.allActiveEntities.add(gameEntity);
            Java8BackComp.removeIf(this.activeEntitiesCacheByFamily.entrySet(), new -$$Lambda$EntityEngine$MejwpTb4OmBzdkvy6TREbpN8Gfo(gameEntity));
            // monitorexit(this.allActiveEntities)
            this.flushNetworkQueues();
        }
    }
    
    void onEntityDeactivated(final GameEntity gameEntity) {
        synchronized (this.allActiveEntities) {
            this.allActiveEntities.remove(gameEntity);
            Java8BackComp.removeIf(this.activeEntitiesCacheByFamily.entrySet(), new -$$Lambda$EntityEngine$IH4I4GL8tOZuI5SiSQ_Nv2q0y9E(gameEntity));
            // monitorexit(this.allActiveEntities)
            this.flushNetworkQueues();
        }
    }
    
    public void removeEntity(final GameEntity gameEntity) {
        synchronized (this.allEntities) {
            gameEntity.setActive(false);
            gameEntity.setEngine(null);
            this.allEntities.remove(gameEntity);
            Java8BackComp.removeIf(this.entitiesCacheByFamily.entrySet(), new -$$Lambda$EntityEngine$mf8SXC5U6HEl5xtahZ6P3zbbfyc(gameEntity));
        }
    }
    
    public void removeListener(final EntityFamily entityFamily, final ActiveEntityListener activeEntityListener) {
        synchronized (this.allEntities) {
            final EntityFamilyListenersAndCache entityFamilyListenersAndCache = this.entitiesCacheByFamily.get(entityFamily);
            if (entityFamilyListenersAndCache != null) {
                entityFamilyListenersAndCache.removeListener((EntityListener)this.wrappedEntityListenerMap.remove(activeEntityListener));
                if (entityFamilyListenersAndCache.shouldBeRemoved()) {
                    this.entitiesCacheByFamily.remove(entityFamily);
                }
            }
        }
    }
    
    public void removeListener(final EntityFamily entityFamily, final EntityListener entityListener) {
        synchronized (this.allEntities) {
            final EntityFamilyListenersAndCache entityFamilyListenersAndCache = this.entitiesCacheByFamily.get(entityFamily);
            if (entityFamilyListenersAndCache != null) {
                entityFamilyListenersAndCache.removeListener(entityListener);
                if (entityFamilyListenersAndCache.shouldBeRemoved()) {
                    this.entitiesCacheByFamily.remove(entityFamily);
                }
            }
        }
    }
    
    public void removeListener(final Class<?> clazz, final ActiveEntityListener activeEntityListener) {
        this.removeListener(getFamilyForComponentType(clazz), activeEntityListener);
    }
    
    public void removeListener(final Class<?> clazz, final EntityListener entityListener) {
        this.removeListener(getFamilyForComponentType(clazz), entityListener);
    }
    
    public void removeListener(final String s, final ActiveEntityListener activeEntityListener) {
        this.removeListener(getFamilyForComponentType(s), activeEntityListener);
    }
    
    public void removeListener(final String s, final EntityListener entityListener) {
        this.removeListener(getFamilyForComponentType(s), entityListener);
    }
    
    public void setCurrentCacheFactor(final int currentCacheFactor) {
        this.currentCacheFactor = currentCacheFactor;
    }
    
    public interface ActiveEntityListener
    {
        void onEntityActivated(final GameEntity p0);
        
        void onEntityDeactivated(final GameEntity p0);
    }
    
    public enum EngineSingleton
    {
        CLIENT, 
        DATA, 
        SERVER;
        
        private final EntityEngine singleton;
        
        private EngineSingleton() {
            this.singleton = new EntityEngine();
        }
        
        private static void loadClass() {
        }
    }
    
    private class EntityFamilyListenersAndCache
    {
        private int accessCalls;
        private int cacheFactor;
        private final Set<GameEntity> entities;
        private final EntityFamily family;
        private boolean isCachingEntities;
        private final List<EntityListener> listeners;
        private int modifyCalls;
        private final Set<GameEntity> source;
        
        private EntityFamilyListenersAndCache(final Set<GameEntity> source, final EntityFamily family) {
            this.listeners = new ArrayList<EntityListener>();
            this.isCachingEntities = false;
            this.modifyCalls = 0;
            this.accessCalls = 0;
            this.cacheFactor = EntityEngine.this.currentCacheFactor;
            this.entities = new LinkedHashSet<GameEntity>();
            this.source = source;
            this.family = family;
        }
        
        private Set<GameEntity> getCachedEntities() {
            if (!this.isCachingEntities) {
                this.rebuildCache();
            }
            ++this.accessCalls;
            this.isCachingEntities = true;
            return this.entities;
        }
        
        private void onEntityAdded(final GameEntity gameEntity) {
            this.onModifyCall();
            if (this.family.isMember(gameEntity)) {
                if (this.isCachingEntities) {
                    this.entities.add(gameEntity);
                }
                final Iterator<EntityListener> iterator = this.listeners.iterator();
                while (iterator.hasNext()) {
                    iterator.next().onEntityAdded(gameEntity);
                }
            }
        }
        
        private void onEntityRemoved(final GameEntity gameEntity) {
            this.onModifyCall();
            if (this.family.isMember(gameEntity)) {
                if (this.isCachingEntities) {
                    this.entities.remove(gameEntity);
                }
                final Iterator<EntityListener> iterator = this.listeners.iterator();
                while (iterator.hasNext()) {
                    iterator.next().onEntityRemoved(gameEntity);
                }
            }
        }
        
        private void onModifyCall() {
            if (this.isCachingEntities) {
                ++this.modifyCalls;
                if (this.modifyCalls > this.accessCalls * this.cacheFactor) {
                    this.isCachingEntities = false;
                    this.resetCache();
                }
            }
        }
        
        private void rebuildCache() {
            this.resetCache();
            for (final GameEntity gameEntity : this.source) {
                if (this.family.isMember(gameEntity)) {
                    this.entities.add(gameEntity);
                }
            }
        }
        
        private void resetCache() {
            this.entities.clear();
            this.accessCalls = 0;
            this.modifyCalls = 0;
            this.cacheFactor = EntityEngine.this.currentCacheFactor;
        }
        
        private boolean shouldBeRemoved() {
            return this.listeners.isEmpty() && !this.isCachingEntities;
        }
        
        public void addListener(final EntityListener entityListener) {
            this.listeners.add(entityListener);
        }
        
        public void removeListener(final EntityListener entityListener) {
            this.listeners.remove(entityListener);
        }
    }
    
    public interface EntityFilter
    {
        boolean test(final GameEntity p0);
    }
    
    public interface EntityListener
    {
        void onEntityAdded(final GameEntity p0);
        
        void onEntityRemoved(final GameEntity p0);
    }
}
