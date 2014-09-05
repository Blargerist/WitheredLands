package blargerist.cake.witherexpansion.items;

import net.minecraft.item.Item;
import blargerist.cake.witherexpansion.ModInfo;
import cpw.mods.fml.common.registry.GameRegistry;

public class Items {
	
	public static Item itemSpawnMob;
	
	public static void init()
	{
		itemSpawnMob = new ItemSpawnMob();
		GameRegistry.registerItem(itemSpawnMob, ItemInfo.ITEM_SPAWN_MOB_KEY);
	}
}
