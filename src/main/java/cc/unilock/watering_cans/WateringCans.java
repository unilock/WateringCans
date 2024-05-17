package cc.unilock.watering_cans;

import cc.unilock.watering_cans.registry.ModItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WateringCans implements ModInitializer {
	public static final String MOD_ID = "watering_cans";
    public static final Logger LOGGER = LoggerFactory.getLogger("WateringCans");

	@Override
	public void onInitialize() {
		ModItems.init();
		LOGGER.info("The Watering Cans are taking over!!");
	}

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}
