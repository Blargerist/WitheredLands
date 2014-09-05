package blargerist.cake.witherexpansion.blocks;

import blargerist.cake.witherexpansion.ModInfo;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPortal;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ModBlocks {
	
	public static Block witheredDirt;
	public static Block witheredPortal;
	public static Block witheredFire;
	
    public static final BlockWitheredPortal portal = (BlockWitheredPortal)Block.getBlockFromName(BlockInfo.WITHERED_PORTAL_UNLOCALIZED_NAME);
	
	public static void init()
	{
		witheredDirt = new BlockWitheredDirt();
		GameRegistry.registerBlock(witheredDirt, BlockInfo.WITHERED_DIRT_KEY);
		
		witheredPortal = new BlockWitheredPortal();
		GameRegistry.registerBlock(witheredPortal, BlockInfo.WITHERED_PORTAL_KEY);
		
		witheredFire = new BlockWitheredFire();
		GameRegistry.registerBlock(witheredFire, BlockInfo.WITHERED_FIRE_KEY);
	}
}