package blargerist.cake.witherexpansion.client;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import blargerist.cake.witherexpansion.ModInfo;
import blargerist.cake.witherexpansion.entities.EntityGiantWitherSkeleton;

public class RenderGiantSkeleton extends Render {
    
    public RenderGiantSkeleton()
    {
        model = new ModelGiantSkeleton();
        shadowSize = 0.5F;
    }
    
    private ModelGiantSkeleton model;
    private static final ResourceLocation giantWitherSkeletonTexture = new ResourceLocation(ModInfo.MODID, "textures/models/giantWitherSkeleton.png");
    
    public void renderGiantWitherSkeleton(EntityGiantWitherSkeleton skeleton, double x, double y, double z, float yaw, float partialTickTime) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)z);
        GL11.glScalef(-1F, -1F, 1F);
        GL11.glDisable(GL11.GL_CULL_FACE);
        
        bindEntityTexture(skeleton);
        
        model.render(skeleton, 0F, 0F, 0F, 0F, 0F, 0.0625F);
        
        GL11.glPopMatrix();
    }
    
    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTickTime) {
        
        if (entity instanceof EntityGiantWitherSkeleton) {
            renderGiantWitherSkeleton((EntityGiantWitherSkeleton) entity, x, y, z, yaw, partialTickTime);
        }
    }
    
    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        if (entity instanceof EntityGiantWitherSkeleton)
        {
            return giantWitherSkeletonTexture;
        } else {
        return null;
        }
    }
    
}
