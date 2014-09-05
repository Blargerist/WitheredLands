package blargerist.cake.witherexpansion.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockWitheredDirt extends Block
{
	
	private IIcon texture;
	
	public BlockWitheredDirt()
	{
        super(Material.ground);
        setCreativeTab(CreativeTabs.tabBlock);
        setStepSound(Block.soundTypeGrass);
        setBlockName(BlockInfo.WITHERED_DIRT_UNLOCALIZED_NAME);
        setHarvestLevel("shovel", 0);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		this.texture = register.registerIcon(BlockInfo.TEXTURE_LOCATION + ":" + BlockInfo.WITHERED_DIRT_TEXTURE);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int meta)
	{
		return this.texture;
	}
}
