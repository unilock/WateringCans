package cc.unilock.watering_cans.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.block.Fertilizable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class WateringCanItem extends Item {
	private static final double MAX_WATERING_DISTANCE = Math.sqrt(ServerPlayNetworkHandler.MAX_BREAK_SQUARED_DISTANCE) - 1.0;

	private final int range;
	private final int rate;

	public WateringCanItem(int range, int rate) {
		super(new FabricItemSettings().maxCount(1));
		this.range = range;
		this.rate = rate;
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 200;
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BRUSH;
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		PlayerEntity player = context.getPlayer();

		if (player != null && this.getHitResult(player).getType() == HitResult.Type.BLOCK) {
			player.setCurrentHand(context.getHand());
		}

		return ActionResult.CONSUME;
	}

	@Override
	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		if (user.isUsingItem() && user.getActiveItem() == stack && remainingUseTicks >= 0) {
			HitResult hitResult = this.getHitResult(user);

			if (hitResult instanceof BlockHitResult blockHitResult && hitResult.getType() == HitResult.Type.BLOCK) {
				if (!world.isClient) {
					List<BlockPos> blocks = BlockPos.streamOutwards(blockHitResult.getBlockPos(), this.range, 0, this.range)
						.map(BlockPos::toImmutable)
						.toList();

					blocks.forEach(pos ->
						((ServerWorld) world).spawnParticles(ParticleTypes.SPLASH,
							pos.getX() + world.random.nextDouble(),
							pos.getY() + 1,
							pos.getZ() + world.random.nextDouble(),
							1,
							0.0,
							0.0,
							0.0,
							1.0)
					);

					if ((this.getMaxUseTime(stack) - remainingUseTicks + 1) % this.rate == 0) {
						// TODO: this can likely be simplified further?
						List<BlockPos> fertilizeables = new ArrayList<>();

						blocks.forEach(base -> {
							if (world.getBlockState(base).getBlock() instanceof FarmlandBlock) {
								world.setBlockState(base, world.getBlockState(base).with(FarmlandBlock.MOISTURE, 7), Block.NOTIFY_LISTENERS);
							}

							if (isFertilizable(world, base)) {
								fertilizeables.add(base);
							}

							BlockPos up = base.up();
							if (isFertilizable(world, up)) {
								fertilizeables.add(up);
							}
						});

						if (fertilizeables.isEmpty()) return;

						fertilizeables.forEach(pos -> {
							if (world.getBlockState(pos).hasRandomTicks()) {
								world.getBlockState(pos).randomTick((ServerWorld) world, pos, world.random);
							}
						});

//						for (int i = 0; i <= this.range; i++) {
//							BlockPos pos = list.get(world.random.nextInt(list.size()));
//							BlockState state = world.getBlockState(pos);
//							Fertilizable fertilizable = (Fertilizable) state.getBlock();
//
//							if (fertilizable.canGrow(world, world.random, pos, state)) {
//								fertilizable.grow((ServerWorld) world, world.random, pos, state);
//							}
//
//							world.syncWorldEvent(WorldEvents.BONE_MEAL_USED, pos, 0);
//						}
					}
				}

				return;
			}

			user.stopUsingItem();
		} else {
			user.stopUsingItem();
		}
	}

	private HitResult getHitResult(LivingEntity user) {
		return ProjectileUtil.getCollision(user, entity -> !entity.isSpectator() && entity.canHit(), MAX_WATERING_DISTANCE);
	}

	private static boolean isFertilizable(World world, BlockPos pos) {
		return world.getBlockState(pos).getBlock() instanceof Fertilizable fertilizable && fertilizable.isFertilizable(world, pos, world.getBlockState(pos), world.isClient);
	}
}
