package java.time.zone;

import java.util.concurrent.*;
import java.security.*;
import java.util.*;

public abstract class ZoneRulesProvider
{
    private static final CopyOnWriteArrayList<ZoneRulesProvider> PROVIDERS;
    private static final ConcurrentMap<String, ZoneRulesProvider> ZONES;
    
    public static Set<String> getAvailableZoneIds() {
        return new HashSet<String>((Collection<? extends String>)ZoneRulesProvider.ZONES.keySet());
    }
    
    public static ZoneRules getRules(final String s, final boolean b) {
        Objects.requireNonNull(s, "zoneId");
        return getProvider(s).provideRules(s, b);
    }
    
    public static NavigableMap<String, ZoneRules> getVersions(final String s) {
        Objects.requireNonNull(s, "zoneId");
        return getProvider(s).provideVersions(s);
    }
    
    private static ZoneRulesProvider getProvider(final String s) {
        final ZoneRulesProvider zoneRulesProvider = ZoneRulesProvider.ZONES.get(s);
        if (zoneRulesProvider != null) {
            return zoneRulesProvider;
        }
        if (ZoneRulesProvider.ZONES.isEmpty()) {
            throw new ZoneRulesException("No time-zone data files registered");
        }
        throw new ZoneRulesException("Unknown time-zone ID: " + s);
    }
    
    public static void registerProvider(final ZoneRulesProvider zoneRulesProvider) {
        Objects.requireNonNull(zoneRulesProvider, "provider");
        registerProvider0(zoneRulesProvider);
        ZoneRulesProvider.PROVIDERS.add(zoneRulesProvider);
    }
    
    private static void registerProvider0(final ZoneRulesProvider zoneRulesProvider) {
        for (final String s : zoneRulesProvider.provideZoneIds()) {
            Objects.requireNonNull(s, "zoneId");
            if (ZoneRulesProvider.ZONES.putIfAbsent(s, zoneRulesProvider) != null) {
                throw new ZoneRulesException("Unable to register zone as one already registered with that ID: " + s + ", currently loading from provider: " + zoneRulesProvider);
            }
        }
    }
    
    public static boolean refresh() {
        boolean b = false;
        final Iterator<ZoneRulesProvider> iterator = ZoneRulesProvider.PROVIDERS.iterator();
        while (iterator.hasNext()) {
            b |= iterator.next().provideRefresh();
        }
        return b;
    }
    
    protected ZoneRulesProvider() {
    }
    
    protected abstract Set<String> provideZoneIds();
    
    protected abstract ZoneRules provideRules(final String p0, final boolean p1);
    
    protected abstract NavigableMap<String, ZoneRules> provideVersions(final String p0);
    
    protected boolean provideRefresh() {
        return false;
    }
    
    static {
        PROVIDERS = new CopyOnWriteArrayList<ZoneRulesProvider>();
        ZONES = new ConcurrentHashMap<String, ZoneRulesProvider>(512, 0.75f, 2);
        final ArrayList<Object> list = new ArrayList<Object>();
        AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction<Object>() {
            final /* synthetic */ List val$loaded = list;
            
            @Override
            public Object run() {
                final String property = System.getProperty("java.time.zone.DefaultZoneRulesProvider");
                if (property != null) {
                    try {
                        final ZoneRulesProvider zoneRulesProvider = ZoneRulesProvider.class.cast(Class.forName(property, true, ClassLoader.getSystemClassLoader()).newInstance());
                        ZoneRulesProvider.registerProvider(zoneRulesProvider);
                        this.val$loaded.add(zoneRulesProvider);
                        return null;
                    }
                    catch (Exception ex) {
                        throw new Error(ex);
                    }
                }
                ZoneRulesProvider.registerProvider(new TzdbZoneRulesProvider());
                return null;
            }
        });
        final Iterator<ZoneRulesProvider> iterator = ServiceLoader.load(ZoneRulesProvider.class, ClassLoader.getSystemClassLoader()).iterator();
        while (iterator.hasNext()) {
            ZoneRulesProvider zoneRulesProvider;
            try {
                zoneRulesProvider = iterator.next();
            }
            catch (ServiceConfigurationError serviceConfigurationError) {
                if (serviceConfigurationError.getCause() instanceof SecurityException) {
                    continue;
                }
                throw serviceConfigurationError;
            }
            boolean b = false;
            final Iterator<ZoneRulesProvider> iterator2 = list.iterator();
            while (iterator2.hasNext()) {
                if (iterator2.next().getClass() == zoneRulesProvider.getClass()) {
                    b = true;
                }
            }
            if (!b) {
                registerProvider0(zoneRulesProvider);
                list.add(zoneRulesProvider);
            }
        }
        ZoneRulesProvider.PROVIDERS.addAll((Collection<? extends ZoneRulesProvider>)list);
    }
}
