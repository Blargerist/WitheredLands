package blargerist.cake.witherexpansion.dimensions;

import java.util.List;
import java.util.Random;

import blargerist.cake.witherexpansion.WitherExpansion;
import cpw.mods.fml.common.eventhandler.Event.Result;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockSand;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.MapGenCaves;
import net.minecraft.world.gen.MapGenRavine;
import net.minecraft.world.gen.NoiseGenerator;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.ChunkProviderEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.*;
import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.*;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAKE;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAVA;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.DUNGEON;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ICE;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ANIMALS;
import static net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.*;

public class ChunkProviderWitherLands implements IChunkProvider {
    
    private Random seed;
    
    private World worldObj;
    private final boolean mapFeaturesEnabled;
    private WorldType worldType;
    
    private NoiseGeneratorOctaves noiseGen1;
    private NoiseGeneratorOctaves noiseGen2;
    private NoiseGeneratorOctaves noiseGen3;
    private NoiseGeneratorPerlin noiseGen4;
    public NoiseGeneratorOctaves noiseGen5;
    public NoiseGeneratorOctaves noiseGen6;
    public NoiseGeneratorOctaves mobSpawnerNoise;
    
    private final double[] noiseArray;
    private double[] stoneNoise = new double[256];
    
    private MapGenBase caveGenerator = new MapGenCaves();
    private MapGenStronghold strongholdGenerator = new MapGenStronghold();
    private MapGenVillage villageGenerator = new MapGenVillage();
    private MapGenScatteredFeature scatteredFeatureGenerator = new MapGenScatteredFeature();
    private MapGenBase ravineGenerator = new MapGenRavine();
    private MapGenMineshaft mineshaftGenerator = new MapGenMineshaft();
    
    private BiomeGenBase[] biomesForGeneration;
    
    double[] noise1;
    double[] noise2;
    double[] noise3;
    double[] noise4;
    
    private final float[] parabolicField;
    int[][] field = new int[32][32];
    
    {
        caveGenerator = TerrainGen.getModdedMapGen(caveGenerator, CAVE);
        strongholdGenerator = (MapGenStronghold) TerrainGen.getModdedMapGen(strongholdGenerator, STRONGHOLD);
        villageGenerator = (MapGenVillage) TerrainGen.getModdedMapGen(villageGenerator, VILLAGE);
        mineshaftGenerator = (MapGenMineshaft) TerrainGen.getModdedMapGen(mineshaftGenerator, MINESHAFT);
        scatteredFeatureGenerator = (MapGenScatteredFeature) TerrainGen.getModdedMapGen(scatteredFeatureGenerator, SCATTERED_FEATURE);
        ravineGenerator = TerrainGen.getModdedMapGen(ravineGenerator, RAVINE);
    }
    
    public ChunkProviderWitherLands(World worldObj, long seed, boolean features) {
        this.worldObj = worldObj;
        this.mapFeaturesEnabled = features;
        this.worldType = worldObj.getWorldInfo().getTerrainType();
        this.seed = new Random(seed);
        this.noiseGen1 = new NoiseGeneratorOctaves(this.seed, 16);
        this.noiseGen2 = new NoiseGeneratorOctaves(this.seed, 16);
        this.noiseGen3 = new NoiseGeneratorOctaves(this.seed, 8);
        this.noiseGen4 = new NoiseGeneratorPerlin(this.seed, 4);
        this.noiseGen5 = new NoiseGeneratorOctaves(this.seed, 10);
        this.noiseGen6 = new NoiseGeneratorOctaves(this.seed, 16);
        this.mobSpawnerNoise = new NoiseGeneratorOctaves(this.seed, 8);
        this.noiseArray = new double[825];
        this.parabolicField = new float[25];
        
        for (int j = -2; j <= 2; ++j) {
            for (int k = -2; k <= 2; ++k) {
                float f = 10.0F / MathHelper.sqrt_float((float) (j * j + k * k) + 0.2F);
                this.parabolicField[j + 2 + (k + 2) * 5] = f;
            }
        }
        
        NoiseGenerator[] noiseGens = { noiseGen1, noiseGen2, noiseGen3,
                noiseGen4, noiseGen5, noiseGen6, mobSpawnerNoise };
        noiseGens = TerrainGen.getModdedNoiseGenerators(worldObj, this.seed, noiseGens);
        this.noiseGen1 = (NoiseGeneratorOctaves) noiseGens[0];
        this.noiseGen2 = (NoiseGeneratorOctaves) noiseGens[1];
        this.noiseGen3 = (NoiseGeneratorOctaves) noiseGens[2];
        this.noiseGen4 = (NoiseGeneratorPerlin) noiseGens[3];
        this.noiseGen5 = (NoiseGeneratorOctaves) noiseGens[4];
        this.noiseGen6 = (NoiseGeneratorOctaves) noiseGens[5];
        this.mobSpawnerNoise = (NoiseGeneratorOctaves) noiseGens[6];
    }
    
    @Override
    public boolean chunkExists(int x, int z) {
        return true;
    }
    
    @Override
    public Chunk provideChunk(int x, int z) {
        this.seed.setSeed((long) x * 341873128712L + (long) z * 132897987541L);
        
        Block[] blockArray = new Block[65536];
        byte[] metaArray = new byte[65536];
        
        this.generateTerrain(x, z, blockArray);
        this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(biomesForGeneration, x * 16, z * 16, 16, 16);
        this.replaceBlocksForBiome(x, z, blockArray, metaArray, this.biomesForGeneration);
        
        this.caveGenerator.func_151539_a(this, this.worldObj, x, z, blockArray);
        this.ravineGenerator.func_151539_a(this, this.worldObj, x, z, blockArray);
        
        if (this.mapFeaturesEnabled) {
            this.strongholdGenerator.func_151539_a(this, this.worldObj, x, z, blockArray);
            this.villageGenerator.func_151539_a(this, this.worldObj, x, z, blockArray);
            this.scatteredFeatureGenerator.func_151539_a(this, this.worldObj, x, z, blockArray);
            this.mineshaftGenerator.func_151539_a(this, this.worldObj, x, z, blockArray);
        }
        
        Chunk chunk = new Chunk(this.worldObj, blockArray, metaArray, x, z);
        
        byte[] byteArray = chunk.getBiomeArray();
        
        for (int i = 0; i < byteArray.length; i++) {
            byteArray[i] = (byte) this.biomesForGeneration[i].biomeID;
        }
        
        chunk.generateSkylightMap();
        return chunk;
    }
    
    private void replaceBlocksForBiome(int chunkX, int chunkZ, Block[] blockArray, byte[] metaArray, BiomeGenBase[] biomesForGeneration) {
        ChunkProviderEvent.ReplaceBiomeBlocks event = new ChunkProviderEvent.ReplaceBiomeBlocks(this, chunkX, chunkZ, blockArray, metaArray, biomesForGeneration, this.worldObj);
        MinecraftForge.EVENT_BUS.post(event);
        
        if (event.getResult() == Result.DENY) {
            return;
        }
        
        double d = 0.03125D;
        this.stoneNoise = this.noiseGen4.func_151599_a(this.stoneNoise, (double)(chunkX * 16), (double)(chunkZ * 16), 16, 16, d * 2D, d * 2D, 1.0D);
        
        for (int k = 0; k < 16; ++k) {
            for (int l = 0; l < 16; ++l) {
                BiomeGenBase biomegenbase = biomesForGeneration[l + k * 16];
                biomegenbase.genTerrainBlocks(this.worldObj, this.seed, blockArray, metaArray, chunkX * 16 + k, chunkZ * 16 + l, this.stoneNoise[l + k * 16]);
            }
        }
    }
    
    // TODO understand math
    private void generateTerrain(int x, int z, Block[] blockArray) {
        byte b0 = 63;
        this.biomesForGeneration = this.worldObj.getWorldChunkManager().getBiomesForGeneration(this.biomesForGeneration, x * 4 - 2, z * 4 - 2, 10, 10);
        this.initializeNoiseArray(x * 4, 0, z * 4);
        
        for (int k = 0; k < 4; ++k) {
            int l = k * 5;
            int i1 = (k + 1) * 5;
            
            for (int j1 = 0; j1 < 4; ++j1) {
                int k1 = (l + j1) * 33;
                int l1 = (l + j1 + 1) * 33;
                int i2 = (i1 + j1) * 33;
                int j2 = (i1 + j1 + 1) * 33;
                
                for (int k2 = 0; k2 < 32; ++k2) {
                    double d0 = 0.125D;
                    double d1 = this.noiseArray[k1 + k2];
                    double d2 = this.noiseArray[l1 + k2];
                    double d3 = this.noiseArray[i2 + k2];
                    double d4 = this.noiseArray[j2 + k2];
                    double d5 = (this.noiseArray[k1 + k2 + 1] - d1) * d0;
                    double d6 = (this.noiseArray[l1 + k2 + 1] - d2) * d0;
                    double d7 = (this.noiseArray[i2 + k2 + 1] - d3) * d0;
                    double d8 = (this.noiseArray[j2 + k2 + 1] - d4) * d0;
                    
                    for (int l2 = 0; l2 < 8; ++l2) {
                        double d9 = 0.25D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * d9;
                        double d13 = (d4 - d2) * d9;
                        
                        for (int i3 = 0; i3 < 4; ++i3) {
                            int j3 = i3 + k * 4 << 12 | 0 + j1 * 4 << 8 | k2
                                    * 8 + l2;
                            short short1 = 256;
                            j3 -= short1;
                            double d14 = 0.25D;
                            double d16 = (d11 - d10) * d14;
                            double d15 = d10 - d16;
                            
                            for (int k3 = 0; k3 < 4; ++k3) {
                                if ((d15 += d16) > 0.0D) {
                                    blockArray[j3 += short1] = Blocks.stone;
                                } else if (k2 * 8 + l2 < b0) {
                                    blockArray[j3 += short1] = Blocks.water;
                                } else {
                                    blockArray[j3 += short1] = null;
                                }
                            }
                            
                            d10 += d12;
                            d11 += d13;
                        }
                        
                        d1 += d5;
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                    }
                }
            }
        }
    }
    
    // TODO understand this math
    private void initializeNoiseArray(int x, int y, int z) {
        double d0 = 684.412D;
        double d1 = 684.412D;
        double d2 = 512.0D;
        double d3 = 512.0D;
        this.noise4 = this.noiseGen6.generateNoiseOctaves(this.noise4, x, z, 5, 5, 200.0D, 200.0D, 0.5D);
        this.noise1 = this.noiseGen3.generateNoiseOctaves(this.noise1, x, y, z, 5, 33, 5, 8.555150000000001D, 4.277575000000001D, 8.555150000000001D);
        this.noise2 = this.noiseGen1.generateNoiseOctaves(this.noise2, x, y, z, 5, 33, 5, 684.412D, 684.412D, 684.412D);
        this.noise3 = this.noiseGen2.generateNoiseOctaves(this.noise3, x, y, z, 5, 33, 5, 684.412D, 684.412D, 684.412D);
        boolean flag1 = false;
        boolean flag = false;
        int l = 0;
        int i1 = 0;
        double d4 = 8.5D;
        
        for (int j1 = 0; j1 < 5; ++j1) {
            for (int k1 = 0; k1 < 5; ++k1) {
                float f = 0.0F;
                float f1 = 0.0F;
                float f2 = 0.0F;
                byte b0 = 2;
                BiomeGenBase biomegenbase = this.biomesForGeneration[j1 + 2
                        + (k1 + 2) * 10];
                
                for (int l1 = -b0; l1 <= b0; ++l1) {
                    for (int i2 = -b0; i2 <= b0; ++i2) {
                        BiomeGenBase biomegenbase1 = this.biomesForGeneration[j1
                                + l1 + 2 + (k1 + i2 + 2) * 10];
                        float f3 = biomegenbase1.rootHeight;
                        float f4 = biomegenbase1.heightVariation;
                        
                        if (this.worldType == WorldType.AMPLIFIED && f3 > 0.0F) {
                            f3 = 1.0F + f3 * 2.0F;
                            f4 = 1.0F + f4 * 4.0F;
                        }
                        
                        float f5 = this.parabolicField[l1 + 2 + (i2 + 2) * 5]
                                / (f3 + 2.0F);
                        
                        if (biomegenbase1.rootHeight > biomegenbase.rootHeight) {
                            f5 /= 2.0F;
                        }
                        
                        f += f4 * f5;
                        f1 += f3 * f5;
                        f2 += f5;
                    }
                }
                
                f /= f2;
                f1 /= f2;
                f = f * 0.9F + 0.1F;
                f1 = (f1 * 4.0F - 1.0F) / 8.0F;
                double d12 = this.noise4[i1] / 8000.0D;
                
                if (d12 < 0.0D) {
                    d12 = -d12 * 0.3D;
                }
                
                d12 = d12 * 3.0D - 2.0D;
                
                if (d12 < 0.0D) {
                    d12 /= 2.0D;
                    
                    if (d12 < -1.0D) {
                        d12 = -1.0D;
                    }
                    
                    d12 /= 1.4D;
                    d12 /= 2.0D;
                } else {
                    if (d12 > 1.0D) {
                        d12 = 1.0D;
                    }
                    
                    d12 /= 8.0D;
                }
                
                ++i1;
                double d13 = (double) f1;
                double d14 = (double) f;
                d13 += d12 * 0.2D;
                d13 = d13 * 8.5D / 8.0D;
                double d5 = 8.5D + d13 * 4.0D;
                
                for (int j2 = 0; j2 < 33; ++j2) {
                    double d6 = ((double) j2 - d5) * 12.0D * 128.0D / 256.0D
                            / d14;
                    
                    if (d6 < 0.0D) {
                        d6 *= 4.0D;
                    }
                    
                    double d7 = this.noise2[l] / 512.0D;
                    double d8 = this.noise3[l] / 512.0D;
                    double d9 = (this.noise1[l] / 10.0D + 1.0D) / 2.0D;
                    double d10 = MathHelper.denormalizeClamp(d7, d8, d9) - d6;
                    
                    if (j2 > 29) {
                        double d11 = (double) ((float) (j2 - 29) / 3.0F);
                        d10 = d10 * (1.0D - d11) + -10.0D * d11;
                    }
                    
                    this.noiseArray[l] = d10;
                    ++l;
                }
            }
        }
    }
    
    @Override
    public Chunk loadChunk(int x, int z) {
        return this.provideChunk(x, z);
    }
    
    @Override
    public void populate(IChunkProvider provider, int chunkX, int chunkZ) {
        BlockFalling.fallInstantly = true;
        int x = chunkX * 16;
        int z = chunkZ * 16;
        BiomeGenBase biome = this.worldObj.getBiomeGenForCoords(x + 16, z + 16);
        this.seed.setSeed(this.worldObj.getSeed());
        long randomX = this.seed.nextLong() / 2L * 2L + 1L;
        long randomZ = this.seed.nextLong() / 2L * 2L + 1L;
        this.seed.setSeed((long) chunkX * randomX + (long) chunkZ * randomZ
                ^ this.worldObj.getSeed());
        boolean hasGenerated = false;
        
        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Pre(provider, worldObj, seed, chunkX, chunkZ, hasGenerated));
        
        if (mapFeaturesEnabled) {
            this.mineshaftGenerator.generateStructuresInChunk(this.worldObj, this.seed, chunkX, chunkZ);
            hasGenerated = this.villageGenerator.generateStructuresInChunk(this.worldObj, this.seed, chunkX, chunkZ);
            this.strongholdGenerator.generateStructuresInChunk(this.worldObj, this.seed, chunkX, chunkZ);
            this.scatteredFeatureGenerator.generateStructuresInChunk(this.worldObj, this.seed, chunkX, chunkZ);
            
        }
        
        int targetX;
        int targetY;
        int targetZ;
        
        if (biome != BiomeGenBase.desert
                && biome != BiomeGenBase.desertHills
                && !hasGenerated
                && this.seed.nextInt(4) == 0
                && TerrainGen.populate(provider, this.worldObj, seed, chunkX, chunkZ, hasGenerated, LAKE)) {
            targetX = x + this.seed.nextInt(16) + 8;
            targetY = this.seed.nextInt(256);
            targetZ = z + this.seed.nextInt(16) + 8;
            (new WorldGenLakes(Blocks.water)).generate(this.worldObj, this.seed, targetX, targetY, targetZ);
        }
        
        if (TerrainGen.populate(provider, worldObj, seed, chunkX, chunkZ, hasGenerated, LAVA)
                && !hasGenerated && this.seed.nextInt(8) == 0) {
            targetX = x + this.seed.nextInt(16) + 8;
            targetY = this.seed.nextInt((this.seed.nextInt(248)) + 8);
            targetZ = z + this.seed.nextInt(16) + 8;
            
            if (targetY < 63 || this.seed.nextInt(10) == 0) {
                (new WorldGenLakes(Blocks.lava)).generate(this.worldObj, this.seed, targetX, targetY, targetZ);
            }
        }
        
        boolean doGen = TerrainGen.populate(provider, worldObj, seed, chunkX, chunkZ, hasGenerated, DUNGEON);
        for (int i = 0; doGen && i < 8; i++) {
            targetX = x + this.seed.nextInt(16) + 8;
            targetY = this.seed.nextInt(256);
            targetZ = z + this.seed.nextInt(16) + 8;
            (new WorldGenDungeons()).generate(worldObj, seed, targetX, targetY, targetZ);
        }
        
        biome.decorate(this.worldObj, this.seed, x, z);
        if (TerrainGen.populate(provider, worldObj, seed, chunkX, chunkZ, hasGenerated, ANIMALS)) {
            SpawnerAnimals.performWorldGenSpawning(this.worldObj, biome, x + 8, z + 8, 16, 16, this.seed);
        }
        
        x += 8;
        z += 8;
        
        doGen = TerrainGen.populate(provider, worldObj, seed, chunkX, chunkZ, hasGenerated, ICE);
        for (int xI = 0; doGen && xI < 16; xI++) {
            for (int zI = 0; zI < 16; zI++) {
                int y = this.worldObj.getPrecipitationHeight(x + xI, z + zI);
                
                if (this.worldObj.isBlockFreezable(x + xI, y - 1, z + zI)) {
                    this.worldObj.setBlock(x, y - 1, z, Blocks.ice, 0, 2);
                }
                
                if (this.worldObj.func_147478_e(x + xI, y, z + zI, true)) {
                    this.worldObj.setBlock(x + xI, y, z + zI, Blocks.snow_layer, 0, 2);
                }
            }
        }
        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Post(provider, worldObj, seed, chunkX, chunkZ, hasGenerated));
        BlockFalling.fallInstantly = false;
    }
    
    @Override
    public boolean saveChunks(boolean flag, IProgressUpdate progressUpdate) {
        return true;
    }
    
    @Override
    public boolean unloadQueuedChunks() {
        return false;
    }
    
    @Override
    public boolean canSave() {
        return true;
    }
    
    @Override
    public String makeString() {
        return "RandomLevelSource";
    }
    
    @Override
    public List getPossibleCreatures(EnumCreatureType creatureType, int x, int y, int z) {
        BiomeGenBase biome = this.worldObj.getBiomeGenForCoords(x, z);
        return creatureType == EnumCreatureType.monster && this.scatteredFeatureGenerator.func_143030_a(x, y, z) ? this.scatteredFeatureGenerator.getScatteredFeatureSpawnList() : biome.getSpawnableList(creatureType);
    }
    
    @Override
    public ChunkPosition func_147416_a(World world, String string, int x, int y, int z) {
        return "Stronghold".equals(string) && this.strongholdGenerator != null ? this.strongholdGenerator.func_151545_a(world, x, y, z) : null;
    }
    
    @Override
    public int getLoadedChunkCount() {
        return 0;
    }
    
    @Override
    public void recreateStructures(int x, int z) {
        if (this.mapFeaturesEnabled) {
            this.mineshaftGenerator.func_151539_a(this, this.worldObj, x, z, (Block[]) null);
            this.villageGenerator.func_151539_a(this, this.worldObj, x, z, (Block[]) null);
            this.strongholdGenerator.func_151539_a(this, this.worldObj, x, z, (Block[]) null);
            this.scatteredFeatureGenerator.func_151539_a(this, this.worldObj, x, z, (Block[]) null);
        }
    }
    
    @Override
    public void saveExtraData() {
        
    }
    
}
