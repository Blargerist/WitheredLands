package blargerist.cake.witherexpansion.dimensions;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;

public class WorldProviderWitherLands extends WorldProvider{
    
    public static int dimensionId = DimensionInfo.WITHER_LANDS_ID;

    @Override
    public String getDimensionName() {
        return DimensionInfo.WITHER_LANDS_NAME;
    }
    
    @Override
    public void registerWorldChunkManager()
    {
        this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.getBiome(140), 0.1F);
        this.dimensionId = DimensionInfo.WITHER_LANDS_ID;
    }
    
    @Override
    public IChunkProvider createChunkGenerator()
    {
        return new ChunkProviderWitherLands(worldObj, worldObj.getSeed(), true);
        
    }
    
}
