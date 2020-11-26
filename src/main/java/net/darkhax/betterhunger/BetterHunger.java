package net.darkhax.betterhunger;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

@Mod("betterhunger")
public class BetterHunger {
    
    private final Configuration config = new Configuration();
	private float newExhaustion;
    
    public BetterHunger() {
        ModLoadingContext.get().registerConfig(Type.COMMON, this.config.getSpec());
        MinecraftForge.EVENT_BUS.addListener(this::onEntityTick);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onLoadComplete);
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> MinecraftForge.EVENT_BUS.addListener(this::onHudRender));
        MinecraftForge.EVENT_BUS.addListener(this::stopbreaking);
        MinecraftForge.EVENT_BUS.addListener(this::onLivingPlayer);
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
        
        // Even when you are doing nothing, your food levels are still going down slowly
        // Values are based off of vanilla behavior. https://minecraft.gamepedia.com/Hunger
        // But check if we are using neverHungry as it would make no sense.
        if (!this.config.neverHungry() && this.config.hc() && (event.player instanceof PlayerEntity) && event.phase == Phase.START) {
        	
        	// 20 ticks = 1 sec ~> 18000 ticks
        	// with 2f = 40 exhaustion/tick
        	//~> 20*5*4 = 400 max exhaustion
        	//~> 20*5 = 100 max saturation
        	// ((400 - 2f*20)/5)/4 = 18 amount of hunger left after each tick
        	// -2 hunger each tick
        	newExhaustion = this.config.hchunger().floatValue();
        	event.player.addExhaustion(newExhaustion);
        }
    }
    
    // Stops the player from breaking blocks if he has a low amount of hunger.
    private void stopbreaking(PlayerEvent.BreakSpeed event)
    {
    	if (this.config.hc() && (this.config.hcbreak() != -1) && (event.getPlayer().getFoodStats().getFoodLevel() <= this.config.hcbreak())) {
            event.setNewSpeed(0);
    	}
    }
    
    // If the player is swimming or standing in the water add exhaustion to player.
    private void onLivingPlayer(LivingEvent.LivingUpdateEvent event){
    	if (event.getEntity() instanceof PlayerEntity) { //only get the player entity
    		PlayerEntity player = (PlayerEntity) event.getEntity(); // TODO: is this even needed?
        	if (player.isInWater() || player.isSwimming()) { // TODO: This will have to do, until I find a way to detect movement in water.
        		newExhaustion = this.config.hcswimming().floatValue();
        		player.getFoodStats().addExhaustion(newExhaustion);
        	}
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