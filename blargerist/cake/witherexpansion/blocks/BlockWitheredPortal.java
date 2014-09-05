package blargerist.cake.witherexpansion.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import blargerist.cake.witherexpansion.WitherExpansion;
import blargerist.cake.witherexpansion.dimensions.DimensionInfo;

public class BlockWitheredPortal extends BlockPortal
{
    
    public static final int[][] field_150001_a = new int[][]
    { new int[0],
    { 3, 1 },
    { 2, 0 } };
    
    protected BlockWitheredPortal()
    {
        super();
        setCreativeTab(CreativeTabs.tabMisc);
        setBlockName(BlockInfo.WITHERED_PORTAL_UNLOCALIZED_NAME);
    }
    
    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        if (entity instanceof EntityPlayerMP && entity.riddenByEntity == null && entity.ridingEntity == null)
        {
            EntityPlayerMP player = (EntityPlayerMP) entity;
            MinecraftServer server = MinecraftServer.getServer();
            
            if (player.timeUntilPortal > 0)
            {
                player.timeUntilPortal = player.getPortalCooldown();
            } else
            {
                if (player.dimension != DimensionInfo.WITHER_LANDS_ID)
                {
                    player.timeUntilPortal = 300;
                    server.getConfigurationManager().transferPlayerToDimension(player, DimensionInfo.WITHER_LANDS_ID, new WitheredTeleporter(server.worldServerForDimension(DimensionInfo.WITHER_LANDS_ID)));
                } else if (player.dimension == DimensionInfo.WITHER_LANDS_ID)
                {
                    player.timeUntilPortal = 300;
                    server.getConfigurationManager().transferPlayerToDimension(player, 0, new WitheredTeleporter(server.worldServerForDimension(0)));
                }
            }
        }
    }
    
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        int meta = func_149999_b(world.getBlockMetadata(x, y, z));
        BlockWitheredPortal.Size size = new BlockWitheredPortal.Size(world, x, y, z, 1);
        BlockWitheredPortal.Size size1 = new BlockWitheredPortal.Size(world, x, y, z, 2);
        
        if (meta == 1 && (!size.isSizeValid() || size.field_150864_e < size.width * size.height))
        {
            world.setBlock(x, y, z, Blocks.air);
        } else if (meta == 2 && (!size1.isSizeValid() || size1.field_150864_e < size1.width * size1.height))
        {
            world.setBlock(x, y, z, Blocks.air);
        } else if (meta == 0 && !size.isSizeValid() && !size1.isSizeValid())
        {
            world.setBlock(x, y, z, Blocks.air);
        }
    }
    
    @Override
    public boolean func_150000_e(World world, int x, int y, int z)
    {
        return tryToCreatePortal(world, x, y, z);
    }
    
    public static boolean tryToCreatePortal(World world, int x, int y, int z)
    {
        BlockWitheredPortal.Size size = new BlockWitheredPortal.Size(world, x, y, z, 1);
        BlockWitheredPortal.Size size1 = new BlockWitheredPortal.Size(world, x, y, z, 2);
        
        if (size.isSizeValid() && size.field_150864_e == 0)
        {
            size.setPortalBlocks();
            return true;
        } else if (size1.isSizeValid() && size1.field_150864_e == 0)
        {
            size1.setPortalBlocks();
            return true;
        } else
        {
            return false;
        }
    }
    
    public static class Size
    {
        private final World world;
        private final int field_150865_b;
        private final int field_150866_c;
        private final int field_150863_d;
        private int field_150864_e = 0;
        private ChunkCoordinates chunkCoords;
        private int height;
        private int width;
        private static final String __OBFID = "CL_00000285";
        
        public Size(World world, int x, int y, int z, int p_i45415_5_)
        {
            this.world = world;
            this.field_150865_b = p_i45415_5_;
            this.field_150863_d = BlockWitheredPortal.field_150001_a[p_i45415_5_][0];
            this.field_150866_c = BlockWitheredPortal.field_150001_a[p_i45415_5_][1];
            
            for (int i1 = y; y > i1 - 21 && y > 0 && this.isBlockValid(world.getBlock(x, y - 1, z)); --y)
            {
                ;
            }
            
            int j1 = this.func_150853_a(x, y, z, this.field_150863_d) - 1;
            
            if (j1 >= 0)
            {
                this.chunkCoords = new ChunkCoordinates(x + j1 * Direction.offsetX[this.field_150863_d], y, z + j1 * Direction.offsetZ[this.field_150863_d]);
                this.width = this.func_150853_a(this.chunkCoords.posX, this.chunkCoords.posY, this.chunkCoords.posZ, this.field_150866_c);
                
                if (this.width < 2 || this.width > 21)
                {
                    this.chunkCoords = null;
                    this.width = 0;
                }
            }
            
            if (this.chunkCoords != null)
            {
                this.height = this.func_150858_a();
            }
        }
        
        protected int func_150853_a(int p_150853_1_, int p_150853_2_, int p_150853_3_, int p_150853_4_)
        {
            int j1 = Direction.offsetX[p_150853_4_];
            int k1 = Direction.offsetZ[p_150853_4_];
            int i1;
            Block block;
            
            for (i1 = 0; i1 < 22; ++i1)
            {
                block = this.world.getBlock(p_150853_1_ + j1 * i1, p_150853_2_, p_150853_3_ + k1 * i1);
                
                if (!this.isBlockValid(block))
                {
                    break;
                }
                
                Block block1 = this.world.getBlock(p_150853_1_ + j1 * i1, p_150853_2_ - 1, p_150853_3_ + k1 * i1);
                
                if (block1 != ModBlocks.witheredDirt)
                {
                    break;
                }
            }
            
            block = this.world.getBlock(p_150853_1_ + j1 * i1, p_150853_2_, p_150853_3_ + k1 * i1);
            return block == ModBlocks.witheredDirt ? i1 : 0;
        }
        
        protected int func_150858_a()
        {
            int i;
            int j;
            int k;
            int l;
            label56:
            
            for (this.height = 0; this.height < 21; ++this.height)
            {
                i = this.chunkCoords.posY + this.height;
                
                for (j = 0; j < this.width; ++j)
                {
                    k = this.chunkCoords.posX + j * Direction.offsetX[BlockWitheredPortal.field_150001_a[this.field_150865_b][1]];
                    l = this.chunkCoords.posZ + j * Direction.offsetZ[BlockWitheredPortal.field_150001_a[this.field_150865_b][1]];
                    Block block = this.world.getBlock(k, i, l);
                    
                    if (!this.isBlockValid(block))
                    {
                        break label56;
                    }
                    
                    if (block == ModBlocks.witheredPortal)
                    {
                        ++this.field_150864_e;
                    }
                    
                    if (j == 0)
                    {
                        block = this.world.getBlock(k + Direction.offsetX[BlockWitheredPortal.field_150001_a[this.field_150865_b][0]], i, l + Direction.offsetZ[BlockWitheredPortal.field_150001_a[this.field_150865_b][0]]);
                        
                        if (block != ModBlocks.witheredDirt)
                        {
                            break label56;
                        }
                    } else if (j == this.width - 1)
                    {
                        block = this.world.getBlock(k + Direction.offsetX[BlockWitheredPortal.field_150001_a[this.field_150865_b][1]], i, l + Direction.offsetZ[BlockWitheredPortal.field_150001_a[this.field_150865_b][1]]);
                        
                        if (block != ModBlocks.witheredDirt)
                        {
                            break label56;
                        }
                    }
                }
            }
            
            for (i = 0; i < this.width; ++i)
            {
                j = this.chunkCoords.posX + i * Direction.offsetX[BlockWitheredPortal.field_150001_a[this.field_150865_b][1]];
                k = this.chunkCoords.posY + this.height;
                l = this.chunkCoords.posZ + i * Direction.offsetZ[BlockWitheredPortal.field_150001_a[this.field_150865_b][1]];
                
                if (this.world.getBlock(j, k, l) != ModBlocks.witheredDirt)
                {
                    this.height = 0;
                    break;
                }
            }
            
            if (this.height <= 21 && this.height >= 3)
            {
                return this.height;
            } else
            {
                this.chunkCoords = null;
                this.width = 0;
                this.height = 0;
                return 0;
            }
        }
        
        protected boolean isBlockValid(Block block)
        {
            return block.getMaterial() == Material.air || block == Blocks.fire || block == ModBlocks.witheredPortal;
        }
        
        public boolean isSizeValid()
        {
            return this.chunkCoords != null && this.width >= 2 && this.width <= 21 && this.height >= 3 && this.height <= 21;
        }
        
        public void setPortalBlocks()
        {
            for (int i = 0; i < this.width; ++i)
            {
                int x = this.chunkCoords.posX + Direction.offsetX[this.field_150866_c] * i;
                int z = this.chunkCoords.posZ + Direction.offsetZ[this.field_150866_c] * i;
                
                for (int l = 0; l < this.height; ++l)
                {
                    int y = this.chunkCoords.posY + l;
                    this.world.setBlock(x, y, z, ModBlocks.witheredPortal, this.field_150865_b, 2);
                }
            }
        }
    }
}
