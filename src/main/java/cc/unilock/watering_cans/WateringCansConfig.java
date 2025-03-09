package cc.unilock.watering_cans;

import folk.sisby.kaleido.api.ReflectiveConfig;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.Comment;
import folk.sisby.kaleido.lib.quiltconfig.api.values.TrackedValue;

public class WateringCansConfig extends ReflectiveConfig {
	@Comment("Whether Watering Cans should only attempt to grow blocks that can accept bonemeal")
	public final TrackedValue<Boolean> fertilizable = value(Boolean.TRUE);
}
