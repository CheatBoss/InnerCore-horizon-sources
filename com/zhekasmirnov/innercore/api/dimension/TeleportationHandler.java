package com.zhekasmirnov.innercore.api.dimension;

import java.util.*;
import com.zhekasmirnov.innercore.api.log.*;

public class TeleportationHandler
{
    private static final Object teleportationLock;
    private static ArrayList<Teleporter> teleportationQueue;
    
    static {
        teleportationLock = new Object();
        TeleportationHandler.teleportationQueue = new ArrayList<Teleporter>();
    }
    
    static void enqueueTeleportation(final Teleporter teleporter) {
        synchronized (TeleportationHandler.teleportationLock) {
            teleporter.setState(1);
            TeleportationHandler.teleportationQueue.add(teleporter);
        }
    }
    
    public static void handleTeleportation() {
        synchronized (TeleportationHandler.teleportationLock) {
            if (TeleportationHandler.teleportationQueue.size() > 0) {
                try {
                    final Teleporter teleporter = TeleportationHandler.teleportationQueue.get(0);
                    if (teleporter.getState() == 1) {
                        if (teleporter.start()) {
                            teleporter.setState(2);
                        }
                        else {
                            teleporter.setState(0);
                        }
                    }
                    else if (teleporter.getState() == 2) {
                        if (teleporter.handle()) {
                            teleporter.finish();
                            teleporter.setState(3);
                        }
                    }
                    else {
                        teleporter.setState(0);
                        TeleportationHandler.teleportationQueue.remove(0);
                    }
                }
                catch (Throwable t) {
                    ICLog.e("ERROR", "error in teleportation handling", t);
                }
            }
        }
    }
}
