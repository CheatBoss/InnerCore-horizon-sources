package com.zhekasmirnov.innercore.api.constants;

public final class TileEntityType
{
    public static int BEACON;
    public static int BREWING_STAND;
    public static int CAULDRON;
    public static int CHEST;
    public static int DISPENSER;
    public static int FURNACE;
    public static int HOPPER;
    public static int JUKEBOX;
    public static int LECTERN;
    public static int NONE;
    
    static {
        TileEntityType.NONE = -1;
        TileEntityType.BEACON = 21;
        TileEntityType.BREWING_STAND = 8;
        TileEntityType.CAULDRON = 16;
        TileEntityType.CHEST = 0;
        TileEntityType.DISPENSER = 13;
        TileEntityType.FURNACE = 1;
        TileEntityType.HOPPER = 2;
        TileEntityType.JUKEBOX = 33;
        TileEntityType.LECTERN = 37;
    }
}
