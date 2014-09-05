package blargerist.cake.witherexpansion.dimensions;

import net.minecraftforge.common.DimensionManager;

public class Dimensions {
    
    
    public static void init()
    {
        registerDimensions();
    }
    
    public static void registerDimensions()
    {
        DimensionManager.registerProviderType(DimensionInfo.WITHER_LANDS_ID, WorldProviderWitherLands.class, false);
        DimensionManager.registerDimension(DimensionInfo.WITHER_LANDS_ID, DimensionInfo.WITHER_LANDS_ID);
    }
}
