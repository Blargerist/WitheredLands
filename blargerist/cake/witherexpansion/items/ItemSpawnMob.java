package blargerist.cake.witherexpansion.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import blargerist.cake.witherexpansion.entities.EntityGiantWitherSkeleton;

public class ItemSpawnMob extends Item {

	private IIcon texture;

	public ItemSpawnMob() {
		super();
		setCreativeTab(CreativeTabs.tabMisc);
		setUnlocalizedName(ItemInfo.ITEM_SPAWN_MOB_UNLOCALIZED_NAME);
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player,
			World world, int x, int y, int z, int side, float hitX, float hitY,
			float hitZ) {
		if (!world.isRemote) {
			//world.spawnEntityInWorld(new EntityGiantWitherSkeleton(world,
					//x + 0.5, y + 4.4, z + 0.5));
			//stack.stackSize--;

			return true;
		} else {
			return false;
		}
	}
}
