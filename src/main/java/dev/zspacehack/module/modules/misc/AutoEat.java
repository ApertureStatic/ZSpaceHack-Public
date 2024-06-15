/**
 * autoEat Module
 */
package dev.zspacehack.module.modules.misc;

import dev.zspacehack.module.Module;
import dev.zspacehack.settings.SliderSetting;
import dev.zspacehack.utils.Wrapper;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;

public class AutoEat extends Module {
	public static AutoEat INSTANCE;
	private final SliderSetting hunger;
	public AutoEat() {
		super("AutoEat", Category.Misc);
		this.setDescription("Automatically eats the best food in your inventory.");
		INSTANCE = this;

		hunger = new SliderSetting("Hunger", "autoeat_hunger", 0f, 0f, 19f, 1f);
		this.addSetting(hunger);
	}

	@Override
	public void onUpdate() {
		if(Wrapper.mc.player.getHungerManager().getFoodLevel() <= hunger.getValueInt()) {
			int foodSlot= -1;
			FoodComponent bestFood = null;
			for(int i = 0; i< 9; i++) {
				Item item = Wrapper.mc.player.getInventory().getStack(i).getItem();
				
				if(!item.isFood()) {
					continue;
				}
				FoodComponent food = item.getFoodComponent();
				if(bestFood != null) {
					if(food.getHunger() > bestFood.getHunger()) {
						bestFood = food;
						foodSlot = i;
					}
				}else {
					bestFood = food;
					foodSlot = i;
				}
				
			}
			
		    if(bestFood != null) {
		    	Wrapper.mc.player.getInventory().selectedSlot = foodSlot;
		    	Wrapper.mc.options.useKey.setPressed(true);
		    }
		}
	}
}
