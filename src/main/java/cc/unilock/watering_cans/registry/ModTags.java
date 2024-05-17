package cc.unilock.watering_cans.registry;

import cc.unilock.watering_cans.WateringCans;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ModTags {
	public static final class Items {
		public static final TagKey<Item> WATERING_CANS = TagKey.of(RegistryKeys.ITEM, WateringCans.id("watering_cans"));

		public static void init() {

		}
	}

	public static void init() {
		Items.init();
	}
}
