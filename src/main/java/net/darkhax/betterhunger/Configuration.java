package net.darkhax.betterhunger;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public class Configuration {
    
    private final ForgeConfigSpec spec;
    
    private final BooleanValue alwaysEdible;
    private final BooleanValue hideFoodBar;
    private final BooleanValue neverHungry;
    private final BooleanValue instantHungerDeath;
    
    public Configuration() {
        
        final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        
        // General Configs
        builder.comment("General settings for the mod.");
        builder.push("general");
        
        builder.comment("Should food always be edible?");
        this.alwaysEdible = builder.define("always-edible", true);
        
        builder.comment("Should the food bar be hidden?");
        this.hideFoodBar = builder.define("hide-food-bar", false);
        
        builder.comment("Should players never get hungry?");
        this.neverHungry = builder.define("never-hungry", false);
        
        builder.comment("Should players insta-die if they run out of food?");
        this.instantHungerDeath = builder.define("hardcore-hunger", false);
        
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
}