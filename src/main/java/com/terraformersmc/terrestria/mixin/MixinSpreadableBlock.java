package com.terraformersmc.terrestria.mixin;

import com.terraformersmc.terrestria.init.TerrestriaBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowyBlock;
import net.minecraft.block.SpreadableBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ViewableWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import java.util.Random;

@Mixin(SpreadableBlock.class)
public abstract class MixinSpreadableBlock extends SnowyBlock {
	protected MixinSpreadableBlock(Settings block$Settings_1) {
		super(block$Settings_1);
	}

	@Shadow
	protected abstract boolean canSurvive(BlockState blockState_1, ViewableWorld viewableWorld_1, BlockPos blockPos_1);

	@Shadow
	protected abstract boolean canSpread(BlockState blockState_1, ViewableWorld viewableWorld_1, BlockPos blockPos_1);

	/**
	 * @author NeusFear
	 */
	@Shadow
	@Inject(method = "onScheduledTick", at = @At("HEAD"), cancellable = true)
	public void onScheduledTick(BlockState blockState_1, World world_1, BlockPos blockPos_1, Random random_1) {
		if (!world_1.isClient) {
			if (!canSurvive(blockState_1, world_1, blockPos_1)) {
				world_1.setBlockState(blockPos_1, Blocks.DIRT.getDefaultState());
			} else {
				if (world_1.getLightLevel(blockPos_1.up()) >= 9) {
					BlockState blockState_2 = this.getDefaultState();

					for (int int_1 = 0; int_1 < 4; ++int_1) {
						BlockPos blockPos_2 = blockPos_1.add(random_1.nextInt(3) - 1, random_1.nextInt(5) - 3, random_1.nextInt(3) - 1);
						if (world_1.getBlockState(blockPos_2).getBlock() == TerrestriaBlocks.BASALT_DIRT && canSpread(blockState_2, world_1, blockPos_2)) {
							world_1.setBlockState(blockPos_2, (BlockState) blockState_2.with(SNOWY, world_1.getBlockState(blockPos_2.up()).getBlock() == Blocks.SNOW));
						}
					}
				}
			}
		}
	}
}
