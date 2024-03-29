package blargerist.cake.witherexpansion.blocks;

import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.UP;
import static net.minecraftforge.common.util.ForgeDirection.WEST;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockWitheredFire extends BlockFire
{
    private static final World world = null;
    @SideOnly(Side.CLIENT)
    private IIcon[] icons;
    
    protected BlockWitheredFire()
    {
        super();
        setCreativeTab(CreativeTabs.tabMisc);
        setBlockName(BlockInfo.WITHERED_FIRE_UNLOCALIZED_NAME);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register)
    {
        this.icons = new IIcon[]
        { register.registerIcon(BlockInfo.TEXTURE_LOCATION + ":" + BlockInfo.WITHERED_FIRE_TEXTURE_1), register.registerIcon(BlockInfo.TEXTURE_LOCATION + ":" + BlockInfo.WITHERED_FIRE_TEXTURE_2) };
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getFireIcon(int layer)
    {
        return this.icons[layer];
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta)
    {
        return this.icons[0];
    } 
    
    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        if (entity instanceof EntityLivingBase)
        {
            ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(Potion.wither.id, 200, 0, false));
        }
    }
    
    @Override
    public void updateTick(World world, int x, int y, int z, Random random)
    {
        if (world.getGameRules().getGameRuleBooleanValue("doFireTick"))
        {
            boolean flag = world.getBlock(x, y - 1, z).isFireSource(world, x, y - 1, z, UP);

            if (!this.canPlaceBlockAt(world, x, y, z))
            {
                world.setBlockToAir(x, y, z);
            }

            if (!flag && world.isRaining() && (world.canLightningStrikeAt(x, y, z) || world.canLightningStrikeAt(x - 1, y, z) || world.canLightningStrikeAt(x + 1, y, z) || world.canLightningStrikeAt(x, y, z - 1) || world.canLightningStrikeAt(x, y, z + 1)))
            {
                world.setBlockToAir(x, y, z);
            }
            else
            {
                int l = world.getBlockMetadata(x, y, z);

                if (l < 15)
                {
                    world.setBlockMetadataWithNotify(x, y, z, l + random.nextInt(3) / 2, 4);
                }

                world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world) + random.nextInt(10));

                if (!flag && !this.canNeighborBurn(world, x, y, z))
                {
                    if (!World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) || l > 3)
                    {
                        world.setBlockToAir(x, y, z);
                    }
                }
                else if (!flag && !this.canCatchFire(world, x, y - 1, z, UP) && l == 15 && random.nextInt(4) == 0)
                {
                    world.setBlockToAir(x, y, z);
                }
                else
                {
                    boolean flag1 = world.isBlockHighHumidity(x, y, z);
                    byte b0 = 0;

                    if (flag1)
                    {
                        b0 = -50;
                    }

                    this.tryCatchFire(world, x + 1, y, z, 300 + b0, random, l, WEST );
                    this.tryCatchFire(world, x - 1, y, z, 300 + b0, random, l, EAST );
                    this.tryCatchFire(world, x, y - 1, z, 250 + b0, random, l, UP   );
                    this.tryCatchFire(world, x, y + 1, z, 250 + b0, random, l, DOWN );
                    this.tryCatchFire(world, x, y, z - 1, 300 + b0, random, l, SOUTH);
                    this.tryCatchFire(world, x, y, z + 1, 300 + b0, random, l, NORTH);

                    for (int i1 = x - 1; i1 <= x + 1; ++i1)
                    {
                        for (int j1 = z - 1; j1 <= z + 1; ++j1)
                        {
                            for (int k1 = y - 1; k1 <= y + 4; ++k1)
                            {
                                if (i1 != x || k1 != y || j1 != z)
                                {
                                    int l1 = 100;

                                    if (k1 > y + 1)
                                    {
                                        l1 += (k1 - (y + 1)) * 100;
                                    }

                                    int i2 = this.getChanceOfNeighborsEncouragingFire(world, i1, k1, j1);

                                    if (i2 > 0)
                                    {
                                        int j2 = (i2 + 40 + world.difficultySetting.getDifficultyId() * 7) / (l + 30);

                                        if (flag1)
                                        {
                                            j2 /= 2;
                                        }

                                        if (j2 > 0 && random.nextInt(l1) <= j2 && (!world.isRaining() || !world.canLightningStrikeAt(i1, k1, j1)) && !world.canLightningStrikeAt(i1 - 1, k1, z) && !world.canLightningStrikeAt(i1 + 1, k1, j1) && !world.canLightningStrikeAt(i1, k1, j1 - 1) && !world.canLightningStrikeAt(i1, k1, j1 + 1))
                                        {
                                            int k2 = l + random.nextInt(5) / 4;

                                            if (k2 > 15)
                                            {
                                                k2 = 15;
                                            }

                                            world.setBlock(i1, k1, j1, this, k2, 3);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void tryCatchFire(World world, int x, int y, int z, int randRange, Random random, int meta, ForgeDirection face)
    {
        int j1 = world.getBlock(x, y, z).getFlammability(world, x, y, z, face);

        if (random.nextInt(randRange) < j1)
        {
            boolean flag = world.getBlock(x, y, z) == Blocks.tnt;

            if (random.nextInt(meta + 10) < 5 && !world.canLightningStrikeAt(x, y, z))
            {
                int k1 = meta + random.nextInt(5) / 4;

                if (k1 > 15)
                {
                    k1 = 15;
                }

                world.setBlock(x, y, z, this, k1, 3);
            }
            else
            {
                world.setBlockToAir(x, y, z);
            }

            if (flag)
            {
                Blocks.tnt.onBlockDestroyedByPlayer(world, x, y, z, 1);
            }
        }
    }
    
    private int getChanceOfNeighborsEncouragingFire(World world, int x, int y, int z)
    {
        byte b0 = 0;

        if (!world.isAirBlock(x, y, z))
        {
            return 0;
        }
        else
        {
            int l = b0;
            l = this.getChanceToEncourageFire(world, x + 1, y, z, l, WEST );
            l = this.getChanceToEncourageFire(world, x - 1, y, z, l, EAST );
            l = this.getChanceToEncourageFire(world, x, y - 1, z, l, UP   );
            l = this.getChanceToEncourageFire(world, x, y + 1, z, l, DOWN );
            l = this.getChanceToEncourageFire(world, x, y, z - 1, l, SOUTH);
            l = this.getChanceToEncourageFire(world, x, y, z + 1, l, NORTH);
            return l;
        }
    }
    
    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        return World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) || this.canNeighborBurn(world, x, y, z);
    }
    
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        if (!World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) && !this.canNeighborBurn(world, x, y, z))
        {
            world.setBlockToAir(x, y, z);
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        if (random.nextInt(24) == 0)
        {
            world.playSound((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), "fire.fire", 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
        }

        int l;
        float f;
        float f1;
        float f2;

        if (!World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) && !Blocks.fire.canCatchFire(world, x, y - 1, z, UP))
        {
            if (Blocks.fire.canCatchFire(world, x - 1, y, z, EAST))
            {
                for (l = 0; l < 2; ++l)
                {
                    f = (float)x + random.nextFloat() * 0.1F;
                    f1 = (float)y + random.nextFloat();
                    f2 = (float)z + random.nextFloat();
                    world.spawnParticle("largesmoke", (double)f, (double)f1, (double)f2, 0.0D, 0.0D, 0.0D);
                }
            }

            if (Blocks.fire.canCatchFire(world, x + 1, y, z, WEST))
            {
                for (l = 0; l < 2; ++l)
                {
                    f = (float)(x + 1) - random.nextFloat() * 0.1F;
                    f1 = (float)y + random.nextFloat();
                    f2 = (float)z + random.nextFloat();
                    world.spawnParticle("largesmoke", (double)f, (double)f1, (double)f2, 0.0D, 0.0D, 0.0D);
                }
            }

            if (Blocks.fire.canCatchFire(world, x, y, z - 1, SOUTH))
            {
                for (l = 0; l < 2; ++l)
                {
                    f = (float)x + random.nextFloat();
                    f1 = (float)y + random.nextFloat();
                    f2 = (float)z + random.nextFloat() * 0.1F;
                    world.spawnParticle("largesmoke", (double)f, (double)f1, (double)f2, 0.0D, 0.0D, 0.0D);
                }
            }

            if (Blocks.fire.canCatchFire(world, x, y, z + 1, NORTH))
            {
                for (l = 0; l < 2; ++l)
                {
                    f = (float)x + random.nextFloat();
                    f1 = (float)y + random.nextFloat();
                    f2 = (float)(z + 1) - random.nextFloat() * 0.1F;
                    world.spawnParticle("largesmoke", (double)f, (double)f1, (double)f2, 0.0D, 0.0D, 0.0D);
                }
            }

            if (Blocks.fire.canCatchFire(world, x, y + 1, z, DOWN))
            {
                for (l = 0; l < 2; ++l)
                {
                    f = (float)x + random.nextFloat();
                    f1 = (float)(y + 1) - random.nextFloat() * 0.1F;
                    f2 = (float)z + random.nextFloat();
                    world.spawnParticle("largesmoke", (double)f, (double)f1, (double)f2, 0.0D, 0.0D, 0.0D);
                }
            }
        }
        else
        {
            for (l = 0; l < 3; ++l)
            {
                f = (float)x + random.nextFloat();
                f1 = (float)y + random.nextFloat() * 0.5F + 0.5F;
                f2 = (float)z + random.nextFloat();
                world.spawnParticle("largesmoke", (double)f, (double)f1, (double)f2, 0.0D, 0.0D, 0.0D);
            }
        }
    }
    
    @Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
        if (world.getBlock(x, y - 1, z) != ModBlocks.witheredDirt || !ModBlocks.WITHERED_PORTAL.tryToCreatePortal(world, x, y, z))
        {
            if (!World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) && !this.canNeighborBurn(world, x, y, z))
            {
                world.setBlockToAir(x, y, z);
            } else
            {
                world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world) + world.rand.nextInt(10));
            }
        }
    }
    
    private boolean canNeighborBurn(World world, int x, int y, int z)
    {
        return this.canCatchFire(world, x + 1, y, z, WEST) || this.canCatchFire(world, x - 1, y, z, EAST) || this.canCatchFire(world, x, y - 1, z, UP) || this.canCatchFire(world, x, y + 1, z, DOWN) || this.canCatchFire(world, x, y, z - 1, SOUTH) || this.canCatchFire(world, x, y, z + 1, NORTH);
    }
}