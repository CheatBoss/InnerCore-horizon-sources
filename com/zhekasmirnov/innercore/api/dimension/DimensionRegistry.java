package com.zhekasmirnov.innercore.api.dimension;

import java.util.*;
import com.zhekasmirnov.innercore.api.*;
import com.zhekasmirnov.innercore.api.log.*;
import org.json.*;
import com.zhekasmirnov.innercore.utils.*;
import com.zhekasmirnov.innercore.api.runtime.*;
import com.zhekasmirnov.innercore.api.runtime.other.*;
import java.io.*;

public class DimensionRegistry
{
    private static CustomDimension currentDimension;
    private static int frame;
    private static HashMap<Long, CustomDimension> registeredDimensions;
    
    static {
        DimensionRegistry.frame = 0;
        DimensionRegistry.registeredDimensions = new HashMap<Long, CustomDimension>();
        DimensionDataHandler.readStoredData();
    }
    
    private static Region findFreeRegion() {
        final ArrayList<Region> list = new ArrayList<Region>();
        final Iterator<DimensionData> iterator = DimensionDataHandler.getDimensionMap().values().iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next().region);
        }
        for (int i = 1; i < 20; ++i) {
            for (int j = -i; j <= i; ++j) {
                for (int k = -i; k <= i; ++k) {
                    if (j == -i || k == -i || j == i || k == i) {
                        final Region region = new Region(j, k);
                        if (!list.contains(region)) {
                            return region;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public static CustomDimension getCurrentCustomDimension() {
        return DimensionRegistry.registeredDimensions.get(nativeGetCurrentCustomDimension());
    }
    
    public static int getCurrentDimensionId() {
        if (DimensionRegistry.currentDimension != null) {
            return DimensionRegistry.currentDimension.getId();
        }
        return NativeAPI.getDimension();
    }
    
    public static int getCurrentDimensionIdImmediate() {
        DimensionRegistry.currentDimension = getCurrentCustomDimension();
        if (DimensionRegistry.currentDimension != null) {
            return DimensionRegistry.currentDimension.getId();
        }
        return NativeAPI.getDimension();
    }
    
    public static CustomDimension getDimensionById(final int n) {
        for (final CustomDimension customDimension : DimensionRegistry.registeredDimensions.values()) {
            if (customDimension.getId() == n) {
                return customDimension;
            }
        }
        return null;
    }
    
    static void mapCustomDimension(final CustomDimension customDimension) {
        DimensionRegistry.registeredDimensions.put(customDimension.pointer, customDimension);
        if (customDimension.getRegion() != null) {
            final Region.Bounds bounds = customDimension.getRegion().getBounds();
            nativeRegisterCustomDimensionRegion(customDimension.pointer, bounds.minX, bounds.minZ, bounds.maxX, bounds.maxZ);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("failed to map dimension ");
        sb.append(customDimension.getId());
        sb.append(" region is missing (maybe it was not found).");
        ICLog.i("ERROR", sb.toString());
    }
    
    private static native long nativeGetCurrentCustomDimension();
    
    private static native void nativeRegisterCustomDimensionRegion(final long p0, final int p1, final int p2, final int p3, final int p4);
    
    private static native void nativeSetCurrentCustomDimension(final long p0);
    
    public static void onDataSaved() {
        writeCurrentDimensionId();
    }
    
    public static void onLevelCreated() {
        loadAndSetupCurrentDimension();
    }
    
    static DimensionData registerCustomDimension(final String s) {
        final HashMap<String, DimensionData> dimensionMap = DimensionDataHandler.getDimensionMap();
        if (dimensionMap.containsKey(s)) {
            return (DimensionData)dimensionMap.get(s);
        }
        int n = 7;
        for (final DimensionData dimensionData : dimensionMap.values()) {
            int n2;
            if (dimensionData.id >= (n2 = n)) {
                n2 = dimensionData.id + 1;
            }
            n = n2;
        }
        final Region freeRegion = findFreeRegion();
        if (freeRegion == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("failed to find region for dimension ");
            sb.append(s);
            sb.append(" maybe too much dimensions were registered.");
            throw new RuntimeException(sb.toString());
        }
        final DimensionData dimensionData2 = new DimensionData(n, freeRegion);
        dimensionMap.put(s, dimensionData2);
        DimensionDataHandler.writeStoredData();
        return dimensionData2;
    }
    
    public static void setCurrentCustomDimension(final CustomDimension currentDimension) {
        if (currentDimension != null) {
            nativeSetCurrentCustomDimension(currentDimension.pointer);
        }
        else {
            nativeSetCurrentCustomDimension(0L);
        }
        DimensionRegistry.currentDimension = currentDimension;
        onDataSaved();
    }
    
    public static void update() {
        final int frame = DimensionRegistry.frame;
        DimensionRegistry.frame = frame + 1;
        if (frame % 10 == 0) {
            DimensionRegistry.currentDimension = getCurrentCustomDimension();
        }
    }
    
    public static class DimensionData
    {
        private int id;
        private Region region;
        
        DimensionData(final int id, final Region region) {
            this.region = region;
            this.id = id;
        }
        
        DimensionData(final JSONObject jsonObject) {
            this.id = jsonObject.optInt("id");
            if (this.id == 0) {
                throw new IllegalArgumentException("failed to read dimension data");
            }
            final JSONArray optJSONArray = jsonObject.optJSONArray("region");
            if (optJSONArray == null || optJSONArray.length() != 2) {
                throw new IllegalArgumentException("failed to read dimension data");
            }
            this.region = new Region(optJSONArray.optInt(0), optJSONArray.optInt(1));
            if (this.region.isOverworldRegion()) {
                throw new IllegalArgumentException("failed to read dimension data");
            }
        }
        
        JSONObject asJson() {
            final JSONObject jsonObject = new JSONObject();
            try {
                final JSONArray jsonArray = new JSONArray();
                jsonArray.put(0, this.region.regionX);
                jsonArray.put(1, this.region.regionZ);
                jsonObject.put("id", this.id);
                jsonObject.put("region", (Object)jsonArray);
                return jsonObject;
            }
            catch (JSONException ex) {
                return jsonObject;
            }
        }
        
        public int getId() {
            return this.id;
        }
        
        public Region getRegion() {
            return this.region;
        }
    }
    
    private static class DimensionDataHandler
    {
        private static final File dataFile;
        private static final Object dataLock;
        static HashMap<String, DimensionData> dimensionDataByStrId;
        
        static {
            dataLock = new Object();
            FileTools.assureFileDir(dataFile = new File(FileTools.DIR_MINECRAFT, "mods/.dimension-regions"));
            DimensionDataHandler.dimensionDataByStrId = new HashMap<String, DimensionData>();
        }
        
        public static HashMap<String, DimensionData> getDimensionMap() {
            return DimensionDataHandler.dimensionDataByStrId;
        }
        
        private static void loadAndSetupCurrentDimension() {
            // monitorenter(DimensionDataHandler.dataLock)
            try {
                final String absoluteDir = LevelInfo.getAbsoluteDir();
                if (absoluteDir == null) {
                    return;
                }
                final File file = new File(absoluteDir, "dimension-id");
                try {
                    Integer.valueOf(FileTools.readFileText(file.getAbsolutePath()).trim());
                }
                catch (Throwable t) {
                    ICLog.e("ERROR", "failed to read dimension id file", t);
                    PrintStacking.print("FAILED TO READ DIMENSION ID");
                }
                catch (IOException ex) {}
            }
            finally {}
        }
        
        static void readStoredData() {
            synchronized (DimensionDataHandler.dataLock) {
                if (DimensionDataHandler.dimensionDataByStrId.size() > 0) {
                    ICLog.i("WARNING", "reading dimension data file with already some dimension data stored.");
                }
                DimensionDataHandler.dimensionDataByStrId.clear();
                if (!DimensionDataHandler.dataFile.exists()) {
                    return;
                }
                try {
                    final JSONObject json = FileTools.readJSON(DimensionDataHandler.dataFile.getAbsolutePath());
                    final Iterator keys = json.keys();
                    while (keys.hasNext()) {
                        final String s = keys.next();
                        try {
                            DimensionDataHandler.dimensionDataByStrId.put(s, new DimensionData(json.optJSONObject(s)));
                        }
                        catch (Throwable t) {
                            PrintStacking.print("ERROR OCCURED DURING READING DIMENSION DATA");
                            final StringBuilder sb = new StringBuilder();
                            sb.append("failed to read existing data for dimension with id: ");
                            sb.append(s);
                            sb.append("");
                            ICLog.e("ERROR", sb.toString(), t);
                        }
                    }
                }
                catch (IOException | JSONException ex) {
                    final Object o;
                    final Throwable t2 = (Throwable)o;
                    PrintStacking.print("ERROR OCCURED DURING READING DIMENSION DATA");
                    ICLog.e("ERROR", "failed to read dimension data file", t2);
                }
            }
        }
        
        private static void writeCurrentDimensionId() {
            while (true) {
                while (true) {
                    Label_0082: {
                        synchronized (DimensionDataHandler.dataLock) {
                            final String absoluteDir = LevelInfo.getAbsoluteDir();
                            if (absoluteDir == null) {
                                return;
                            }
                            final File file = new File(absoluteDir, "dimension-id");
                            try {
                                final String absolutePath = file.getAbsolutePath();
                                if (DimensionRegistry.currentDimension == null) {
                                    break Label_0082;
                                }
                                final int id = DimensionRegistry.currentDimension.getId();
                                FileTools.writeFileText(absolutePath, String.valueOf(id));
                            }
                            catch (IOException ex) {
                                ICLog.e("ERROR", "failed to write dimension id file", ex);
                                PrintStacking.print("FAILED TO WRITE DIMENSION ID");
                            }
                            return;
                        }
                    }
                    final int id = -1;
                    continue;
                }
            }
        }
        
        static void writeStoredData() {
            synchronized (DimensionDataHandler.dataLock) {
                final JSONObject jsonObject = new JSONObject();
                for (final String s : DimensionDataHandler.dimensionDataByStrId.keySet()) {
                    try {
                        jsonObject.put(s, (Object)DimensionDataHandler.dimensionDataByStrId.get(s).asJson());
                    }
                    catch (JSONException ex2) {}
                }
                try {
                    FileTools.writeJSON(DimensionDataHandler.dataFile.getAbsolutePath(), jsonObject);
                }
                catch (IOException ex) {
                    ICLog.e("ERROR", "wailed to write stored dimension data", ex);
                }
            }
        }
    }
}
