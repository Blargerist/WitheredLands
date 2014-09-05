package blargerist.cake.witherexpansion.entities;

import blargerist.cake.witherexpansion.ModInfo;
import cpw.mods.fml.common.registry.EntityRegistry;

public class Entities {

	public static void init()
	{
		EntityRegistry.registerModEntity(EntityGiantWitherSkeleton.class, "GiantWitherSkeleton", 1, ModInfo.MODID, 80, 3, true);
	}
}
