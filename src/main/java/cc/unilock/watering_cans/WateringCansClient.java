package cc.unilock.watering_cans;

import net.fabricmc.api.ClientModInitializer;

public class WateringCansClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		//ModelPredicateProviderRegistry.register(WateringCans.id("watering"), (stack, world, entity, seed) -> entity != null && entity.getActiveItem().isIn(ModTags.Items.WATERING_CANS) ? (float)(entity.getItemUseTimeLeft() % 10) / 10.0F : 0.0F);
	}
}
