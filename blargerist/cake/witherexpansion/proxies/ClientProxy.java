package blargerist.cake.witherexpansion.proxies;

import blargerist.cake.witherexpansion.client.RenderGiantSkeleton;
import blargerist.cake.witherexpansion.entities.EntityGiantWitherSkeleton;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
    
    @Override
    public void initSounds() {
    }
    
    @Override
    public void initRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(EntityGiantWitherSkeleton.class, new RenderGiantSkeleton());
    }
}
