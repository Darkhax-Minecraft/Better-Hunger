package net.darkhax.betterhunger;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class Configuration {
    
    private final ForgeConfigSpec spec;
    
    private final BooleanValue alwaysEdible;
    private final BooleanValue hideFoodBar;
    private final BooleanValue neverHungry;
    private final BooleanValue instantHungerDeath;
    private final BooleanValue hc;
    private final ConfigValue<Double> hchunger;
    private final ConfigValue<Integer> hcbreak;
    private final ConfigValue<Double> hcswimming;
    
    public Configuration() {
        
        final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        
        // General Configs
        builder.comment("General settings for the mod.");
        builder.push("General");
        
        builder.comment("Should food always be edible?");
        this.alwaysEdible = builder.define("always-edible", true);
        
        builder.comment("Should the food bar be hidden?");
        this.hideFoodBar = builder.define("hide-food-bar", false);
        
        builder.comment("Should players never get hungry?");
        this.neverHungry = builder.define("never-hungry", false);
        
        builder.comment("Should players insta-die if they run out of food?");
        this.instantHungerDeath = builder.define("hardcore-hunger", false);
        
        builder.pop();
        builder.push("Hardcore Hunger");
        builder.comment("In order for these features to work properly you need neverHungry = false !!!\nShould Harcore Hunger mode be enabled?");
        this.hc = builder.define("harcore", false);
        builder.comment("How much hunger will you lose every tick? 20 ticks = 1 second\ndefault value = 0.01\nfor reference, the vanilla sprinting value = 0.1");
        this.hchunger = builder.define("harcore-depletion", 0.01);  //Vanilla sprinting is 0.1
        builder.comment("You can no longer break ANY blocks below this amount of shanks. Hunger is divided by 2.\nExample: 13 = 6.5 shanks\nExample: 6 = 3 shanks\ndefault value = 6\n-1 removes this feature");
        this.hcbreak = builder.define("harcore-breaking", 6);
        builder.comment("Swimming and/or being inside water depletes your hunger every tick.\ndefault value = 0.02\nfor reference, the vanilla sprinting value = 0.1");
        this.hcswimming = builder.define("hardcore-swimming", 0.01);
        builder.pop();
        this.spec = builder.build();
    }
    
    public ForgeConfigSpec getSpec () {
        
        return this.spec;
    }
    
    public boolean alwaysEdible () {
        
        return this.alwaysEdible.get();
    }
    
    public boolean hideFoodBar () {
        
        return this.hideFoodBar.get();
    }
    
    public boolean neverHungry () {
        
        return this.neverHungry.get();
    }
    
    public boolean isHardcoreHunger () {
        
        return this.instantHungerDeath.get();
    }
    
    public boolean hc () {
    	
    	return this.hc.get();
    }
    
    public Double hchunger () {
    	
    	return this.hchunger.get();
    }
    
    public Integer hcbreak () {
    	
    	return this.hcbreak.get();
    }
    
    public Double hcswimming() {
    	return this.hcswimming.get();
    }

}