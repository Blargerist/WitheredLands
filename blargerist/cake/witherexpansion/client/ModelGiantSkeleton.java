package blargerist.cake.witherexpansion.client;

import java.util.ArrayList;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelGiantSkeleton extends ModelBase {
    
    private ArrayList<ModelRenderer> parts;
    
    public ModelGiantSkeleton() {
        parts = new ArrayList<ModelRenderer>();
        textureHeight = 128;
        textureWidth = 128;
        
        ModelRenderer bipedHead = new ModelRenderer(this, 0, 48);
        bipedHead.addBox(-12, -42, -12, 
                         24, 24, 24);
        bipedHead.setRotationPoint(0, 0, 0);
        parts.add(bipedHead);
        
        ModelRenderer bipedBody = new ModelRenderer(this, 0, 0);
        bipedBody.addBox(-12, -18, -6, 
                          24, 36, 12);
        bipedBody.setRotationPoint(0, 0, 0);
        parts.add(bipedBody);
        
        ModelRenderer bipedRightLeg = new ModelRenderer(this, 72, 0);
        bipedRightLeg.addBox(4, 18, -3,
                              6, 36, 6);
        bipedRightLeg.setRotationPoint(0, 0, 0);
        parts.add(bipedRightLeg);
        
        ModelRenderer bipedLeftLeg = new ModelRenderer(this, 72, 0);
        bipedLeftLeg.addBox(-10, 18, -3,
                             6, 36, 6);
        bipedLeftLeg.setRotationPoint(0, 0, 0);
        parts.add(bipedLeftLeg);
        
        ModelRenderer bipedRightArm = new ModelRenderer(this, 96, 0);
        bipedRightArm.addBox(12, -10, -3, 
                             6, 36, 6);
        bipedRightArm.setRotationPoint(0, 0, 0);
        parts.add(bipedRightArm);
        
        ModelRenderer bipedLeftArm = new ModelRenderer(this, 96, 0);
        bipedLeftArm.addBox(-18, -10, -3, 
                             6, 36, 6);
        bipedLeftArm.setRotationPoint(0, 0, 0);
        parts.add(bipedLeftArm);
    }
    
    @Override
    public void render(Entity entity, float par1, float par2, float par3, float par4, float par5, float mult) {
        
        for(ModelRenderer part : parts) {
            part.render(mult);
        }
    }
}
