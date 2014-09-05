package blargerist.cake.witherexpansion;

import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import blargerist.cake.witherexpansion.blocks.ModBlocks;
import blargerist.cake.witherexpansion.dimensions.Dimensions;
import blargerist.cake.witherexpansion.entities.Entities;
import blargerist.cake.witherexpansion.items.Items;
import blargerist.cake.witherexpansion.proxies.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = ModInfo.MODID, version = ModInfo.VERSION, dependencies = "")
public class WitherExpansion
{
	public static final Logger Log = LogManager.getLogger(ModInfo.MODID);
	
	@SidedProxy(clientSide = "blargerist.cake.witherexpansion.proxies.ClientProxy", serverSide = "blargerist.cake.witherexpansion.proxies.CommonProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		ModConfig.init(event.getSuggestedConfigurationFile());
		MinecraftForge.EVENT_BUS.register(new EventHandlerClass());
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		ModBlocks.init();
		Items.init();
		Entities.init();
		Dimensions.init();
		
		
		proxy.initSounds();
		proxy.initRenderers();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
	}

	@EventHandler
    public void serverStarting(FMLServerStartingEvent event)
	{
	}
}
