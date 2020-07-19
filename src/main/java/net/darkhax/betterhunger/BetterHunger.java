package net.darkhax.betterhunger;

import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;

@Mod("betterhunger")
public class BetterHunger {
    
    private final Configuration config = new Configuration();
    
    public BetterHunger() {
        
        ModLoadingContext.get().registerConfig(Type.COMMON, this.config.getSpec());
        
        MinecraftForge.EVENT_BUS.addListener(this::onEntityTick);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onLoadComplete);
        
        if (FMLEnvironment.dist == Dist.CLIENT) {
        	
        	MinecraftForge.EVENT_BUS.addListener(this::onHudRender);
        }
    }
    
    private void onHudRender (RenderGameOverlayEvent.Pre event) {
        
        // Hide the food bar
        if (this.config.hideFoodBar() && event.getType() == ElementType.FOOD) {
            
            event.setCanceled(true);
        }
    }
    
    private void onEntityTick (PlayerTickEvent event) {
        
        // Make player always full
        if (this.config.neverHungry() && event.phase == Phase.START) {
            
            event.player.getFoodStats().addStats(20, 20f);
        }
        
        // Instant death when players run out of food
        else if (this.config.isHardcoreHunger() && event.player.getFoodStats().getFoodLevel() <= 0) {
            
            event.player.attackEntityFrom(DamageSource.STARVE, Float.MAX_VALUE);
        }
    }
    
    private void onLoadComplete (FMLLoadCompleteEvent event) {
        
        // Make all foods edible
        if (this.config.alwaysEdible()) {
            
            for (final Item item : ForgeRegistries.ITEMS) {
                
                final Food food = item.getFood();
                
                if (food != null) {
                    
                    ObfuscationReflectionHelper.setPrivateValue(Food.class, food, true, "field_221473_d");
                }
            }
        }
    }
}