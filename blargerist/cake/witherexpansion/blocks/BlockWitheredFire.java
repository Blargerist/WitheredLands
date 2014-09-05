package blargerist.cake.witherexpansion.blocks;

import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.UP;
import static net.minecraftforge.common.util.ForgeDirection.WEST;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockFire;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockWitheredFire extends BlockFire
{
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
    } // TODO hitBox & burning & spread
    
    @Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
        if (world.getBlock(x, y - 1, z) == ModBlocks.witheredDirt)
        {
            //BlockWitheredPortal.tryToCreatePortal(world, x, y, z);
        }
        if (world.provider.dimensionId > 0 || !ModBlocks.portal.func_150000_e(world, x, y, z))
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
