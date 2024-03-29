package blargerist.cake.witherexpansion.entities;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityGiantWitherSkeleton extends Entity {
	
	private double startY;
	private double targetY;

	public EntityGiantWitherSkeleton(World world) {
		super(world);
	}

	public EntityGiantWitherSkeleton(World world, double x, double y, double z) {
		super(world);
		
		posX = x;
		startY = posY = y;
		posZ = z;
	}

	@Override
	protected void entityInit() {
		
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		//startY = nbttagcompound.getShort("Start");
		//targetY = nbttagcompound.getShort("Target");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		//nbttagcompound.setShort("Start", (short)startY);
		//nbttagcompound.setShort("Target", (short)targetY);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		//if (!worldObj.isRemote) {
			//if (targetY == 0 || Math.abs(posY - targetY) < 0.25) {
				//targetY = startY + worldObj.rand.nextDouble() * 5;
			//}

			//if (posY < targetY) {
				//motionY = 0.05;
			//} else {
				//motionY = -0.05;
			//}
		//}

		//setPosition(posX + motionX, posY + motionY, posZ + motionZ);
	}

}
