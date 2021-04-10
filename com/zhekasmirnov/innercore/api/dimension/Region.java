package com.zhekasmirnov.innercore.api.dimension;

public class Region
{
    public static final Region OVERWORLD;
    public static final int REGION_SIZE = 200000;
    public final int regionX;
    public final int regionZ;
    
    static {
        OVERWORLD = new Region(0, 0);
    }
    
    public Region(final int regionX, final int regionZ) {
        this.regionX = regionX;
        this.regionZ = regionZ;
    }
    
    public static Region getRegionAt(final int n, final int n2) {
        return new Region((int)Math.floor(n / 200000.0 + 0.5), (int)Math.floor(n2 / 200000.0 + 0.5));
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o instanceof Region) {
            return this.regionX == ((Region)o).regionX && this.regionZ == ((Region)o).regionZ;
        }
        return super.equals(o);
    }
    
    public Bounds getBounds() {
        return new Bounds(this.regionX * 200000 - 100000, this.regionZ * 200000 - 100000, this.regionX * 200000 + 100000, this.regionZ * 200000 + 100000);
    }
    
    public int getMiddleX() {
        return this.regionX * 200000;
    }
    
    public int getMiddleZ() {
        return this.regionZ * 200000;
    }
    
    public boolean isOverworldRegion() {
        return this.regionX == 0 && this.regionZ == 0;
    }
    
    public static class Bounds
    {
        public final int maxX;
        public final int maxZ;
        public final int minX;
        public final int minZ;
        
        private Bounds(final int minX, final int minZ, final int maxX, final int maxZ) {
            this.minX = minX;
            this.minZ = minZ;
            this.maxX = maxX;
            this.maxZ = maxZ;
        }
        
        public boolean isInBounds(final double n, final double n2) {
            return this.minX < n && n < this.maxX && this.minZ < n2 && n2 < this.maxZ;
        }
    }
}
