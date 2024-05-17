package cc.unilock.watering_cans.registry;

import cc.unilock.watering_cans.WateringCans;
import cc.unilock.watering_cans.item.WateringCanItem;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

public class ModItems {
	public static final WateringCanItem BASE_WATERING_CAN = register("base_watering_can", new WateringCanItem(0, 20));
	public static final WateringCanItem KILO_WATERING_CAN = register("kilo_watering_can", new WateringCanItem(1, 15));
	public static final WateringCanItem MEGA_WATERING_CAN = register("mega_watering_can", new WateringCanItem(2, 10));
	public static final WateringCanItem GIGA_WATERING_CAN = register("giga_watering_can", new WateringCanItem(3, 5));

	public static final ItemGroup WATERING_CANS = Registry.register(
		Registries.ITEM_GROUP,
		WateringCans.id("watering_cans"),
		FabricItemGroup.builder()
			.displayName(Text.translatable("itemGroup.watering_cans.watering_cans"))
			.icon(BASE_WATERING_CAN::getDefaultStack)
			.entries((displayContext, entries) -> {
				entries.add(BASE_WATERING_CAN);
				entries.add(KILO_WATERING_CAN);
				entries.add(MEGA_WATERING_CAN);
				entries.add(GIGA_WATERING_CAN);
			})
			.build()
	);

	private static <T extends Item> T register(String path, T item) {
		return Registry.register(Registries.ITEM, WateringCans.id(path), item);
	}

	public static void init() {

	}
}
